/*
 * External method calls:
 *   Lnet/minecraft/util/profiler/Profiler;push(Ljava/util/function/Supplier;)V
 *   Lnet/minecraft/server/function/CommandFunction;withMacroReplaced(Lnet/minecraft/nbt/NbtCompound;Lcom/mojang/brigadier/CommandDispatcher;)Lnet/minecraft/server/function/Procedure;
 *   Lnet/minecraft/server/command/CommandManager;callWithContext(Lnet/minecraft/server/command/ServerCommandSource;Ljava/util/function/Consumer;)V
 *   Lnet/minecraft/server/function/CommandFunction;id()Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/server/command/ServerCommandSource;withPermissions(Lnet/minecraft/command/permission/PermissionPredicate;)Lnet/minecraft/server/command/ServerCommandSource;
 *   Lnet/minecraft/server/command/ServerCommandSource;withSilent()Lnet/minecraft/server/command/ServerCommandSource;
 *   Lnet/minecraft/command/CommandExecutionContext;enqueueProcedureCall(Lnet/minecraft/command/CommandExecutionContext;Lnet/minecraft/server/function/Procedure;Lnet/minecraft/server/command/AbstractServerCommandSource;Lnet/minecraft/command/ReturnValueConsumer;)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/function/CommandFunctionManager;load(Lnet/minecraft/server/function/FunctionLoader;)V
 *   Lnet/minecraft/server/function/CommandFunctionManager;executeAll(Ljava/util/Collection;Lnet/minecraft/util/Identifier;)V
 *   Lnet/minecraft/server/function/CommandFunctionManager;execute(Lnet/minecraft/server/function/CommandFunction;Lnet/minecraft/server/command/ServerCommandSource;)V
 */
package net.minecraft.server.function;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import net.minecraft.command.CommandExecutionContext;
import net.minecraft.command.ReturnValueConsumer;
import net.minecraft.command.permission.LeveledPermissionPredicate;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.function.CommandFunction;
import net.minecraft.server.function.FunctionLoader;
import net.minecraft.server.function.MacroException;
import net.minecraft.server.function.Procedure;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.profiler.Profilers;
import org.slf4j.Logger;

public class CommandFunctionManager {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Identifier TICK_TAG_ID = Identifier.ofVanilla("tick");
    private static final Identifier LOAD_TAG_ID = Identifier.ofVanilla("load");
    private final MinecraftServer server;
    private List<CommandFunction<ServerCommandSource>> tickFunctions = ImmutableList.of();
    private boolean justLoaded;
    private FunctionLoader loader;

    public CommandFunctionManager(MinecraftServer server, FunctionLoader loader) {
        this.server = server;
        this.loader = loader;
        this.load(loader);
    }

    public CommandDispatcher<ServerCommandSource> getDispatcher() {
        return this.server.getCommandManager().getDispatcher();
    }

    public void tick() {
        if (!this.server.getTickManager().shouldTick()) {
            return;
        }
        if (this.justLoaded) {
            this.justLoaded = false;
            List<CommandFunction<ServerCommandSource>> collection = this.loader.getTagOrEmpty(LOAD_TAG_ID);
            this.executeAll(collection, LOAD_TAG_ID);
        }
        this.executeAll(this.tickFunctions, TICK_TAG_ID);
    }

    private void executeAll(Collection<CommandFunction<ServerCommandSource>> functions, Identifier label) {
        Profilers.get().push(label::toString);
        for (CommandFunction<ServerCommandSource> lv : functions) {
            this.execute(lv, this.getScheduledCommandSource());
        }
        Profilers.get().pop();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void execute(CommandFunction<ServerCommandSource> function, ServerCommandSource source) {
        Profiler lv = Profilers.get();
        lv.push(() -> "function " + String.valueOf(function.id()));
        try {
            Procedure<ServerCommandSource> lv2 = function.withMacroReplaced(null, this.getDispatcher());
            CommandManager.callWithContext(source, context -> CommandExecutionContext.enqueueProcedureCall(context, lv2, source, ReturnValueConsumer.EMPTY));
        } catch (MacroException lv2) {
        } catch (Exception exception) {
            LOGGER.warn("Failed to execute function {}", (Object)function.id(), (Object)exception);
        } finally {
            lv.pop();
        }
    }

    public void setFunctions(FunctionLoader loader) {
        this.loader = loader;
        this.load(loader);
    }

    private void load(FunctionLoader loader) {
        this.tickFunctions = List.copyOf(loader.getTagOrEmpty(TICK_TAG_ID));
        this.justLoaded = true;
    }

    public ServerCommandSource getScheduledCommandSource() {
        return this.server.getCommandSource().withPermissions(LeveledPermissionPredicate.GAMEMASTERS).withSilent();
    }

    public Optional<CommandFunction<ServerCommandSource>> getFunction(Identifier id) {
        return this.loader.get(id);
    }

    public List<CommandFunction<ServerCommandSource>> getTag(Identifier id) {
        return this.loader.getTagOrEmpty(id);
    }

    public Iterable<Identifier> getAllFunctions() {
        return this.loader.getFunctions().keySet();
    }

    public Iterable<Identifier> getFunctionTags() {
        return this.loader.getTags();
    }
}

