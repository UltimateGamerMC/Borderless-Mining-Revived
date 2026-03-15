/*
 * External method calls:
 *   Lnet/minecraft/world/GameMode;byId(Ljava/lang/String;Lnet/minecraft/world/GameMode;)Lnet/minecraft/world/GameMode;
 *   Lnet/minecraft/command/CommandSource;suggestMatching(Ljava/util/stream/Stream;Lcom/mojang/brigadier/suggestion/SuggestionsBuilder;)Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/text/Text;stringifiedTranslatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/world/GameMode;values()[Lnet/minecraft/world/GameMode;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/command/argument/GameModeArgumentType;parse(Lcom/mojang/brigadier/StringReader;)Lnet/minecraft/world/GameMode;
 */
package net.minecraft.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;

public class GameModeArgumentType
implements ArgumentType<GameMode> {
    private static final Collection<String> EXAMPLES = Stream.of(GameMode.SURVIVAL, GameMode.CREATIVE).map(GameMode::getId).collect(Collectors.toList());
    private static final GameMode[] VALUES = GameMode.values();
    private static final DynamicCommandExceptionType INVALID_GAME_MODE_EXCEPTION = new DynamicCommandExceptionType(gameMode -> Text.stringifiedTranslatable("argument.gamemode.invalid", gameMode));

    @Override
    public GameMode parse(StringReader stringReader) throws CommandSyntaxException {
        String string = stringReader.readUnquotedString();
        GameMode lv = GameMode.byId(string, null);
        if (lv == null) {
            throw INVALID_GAME_MODE_EXCEPTION.createWithContext(stringReader, string);
        }
        return lv;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (context.getSource() instanceof CommandSource) {
            return CommandSource.suggestMatching(Arrays.stream(VALUES).map(GameMode::getId), builder);
        }
        return Suggestions.empty();
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static GameModeArgumentType gameMode() {
        return new GameModeArgumentType();
    }

    public static GameMode getGameMode(CommandContext<ServerCommandSource> context, String name) throws CommandSyntaxException {
        return context.getArgument(name, GameMode.class);
    }

    @Override
    public /* synthetic */ Object parse(StringReader reader) throws CommandSyntaxException {
        return this.parse(reader);
    }
}

