/*
 * External method calls:
 *   Lnet/minecraft/server/command/CommandManager;literal(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
 *   Lnet/minecraft/server/command/CommandManager;requirePermissionLevel(Lnet/minecraft/command/permission/PermissionCheck;)Lnet/minecraft/command/permission/PermissionSourcePredicate;
 *   Lnet/minecraft/entity/SpawnGroup;values()[Lnet/minecraft/entity/SpawnGroup;
 *   Lnet/minecraft/command/argument/BlockPosArgumentType;blockPos()Lnet/minecraft/command/argument/BlockPosArgumentType;
 *   Lnet/minecraft/server/command/CommandManager;argument(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
 *   Lnet/minecraft/world/SpawnHelper;spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/command/DebugMobSpawningCommand;execute(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/util/math/BlockPos;)I
 */
package net.minecraft.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.SpawnHelper;

public class DebugMobSpawningCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder literalArgumentBuilder = (LiteralArgumentBuilder)CommandManager.literal("debugmobspawning").requires(CommandManager.requirePermissionLevel(CommandManager.GAMEMASTERS_CHECK));
        for (SpawnGroup lv : SpawnGroup.values()) {
            literalArgumentBuilder.then(CommandManager.literal(lv.getName()).then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("at", BlockPosArgumentType.blockPos()).executes(context -> DebugMobSpawningCommand.execute((ServerCommandSource)context.getSource(), lv, BlockPosArgumentType.getLoadedBlockPos(context, "at")))));
        }
        dispatcher.register(literalArgumentBuilder);
    }

    private static int execute(ServerCommandSource source, SpawnGroup group, BlockPos pos) {
        SpawnHelper.spawnEntitiesInChunk(group, source.getWorld(), pos);
        return 1;
    }
}

