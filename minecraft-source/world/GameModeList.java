/*
 * External method calls:
 *   Lnet/minecraft/world/GameMode;values()[Lnet/minecraft/world/GameMode;
 *   Lnet/minecraft/util/StringIdentifiable$EnumCodec;listOf()Lcom/mojang/serialization/Codec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/world/GameModeList;of([Lnet/minecraft/world/GameMode;)Lnet/minecraft/world/GameModeList;
 */
package net.minecraft.world;

import com.mojang.serialization.Codec;
import java.util.Arrays;
import java.util.List;
import net.minecraft.world.GameMode;

public record GameModeList(List<GameMode> gameModes) {
    public static final GameModeList ALL = GameModeList.of(GameMode.values());
    public static final GameModeList SURVIVAL_LIKE = GameModeList.of(GameMode.SURVIVAL, GameMode.ADVENTURE);
    public static final Codec<GameModeList> CODEC = GameMode.CODEC.listOf().xmap(GameModeList::new, GameModeList::gameModes);

    public static GameModeList of(GameMode ... gameModes) {
        return new GameModeList(Arrays.stream(gameModes).toList());
    }

    public boolean contains(GameMode gameMode) {
        return this.gameModes.contains(gameMode);
    }
}

