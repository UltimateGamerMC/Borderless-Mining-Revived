/*
 * External method calls:
 *   Lnet/minecraft/util/StringIdentifiable;createCodec(Ljava/util/function/Supplier;)Lnet/minecraft/util/StringIdentifiable$EnumCodec;
 *   Lnet/minecraft/util/StringIdentifiable;createMapper([Ljava/lang/Object;Ljava/util/function/Function;)Ljava/util/function/Function;
 *   Lnet/minecraft/network/codec/PacketCodec;xmap(Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/player/PlayerSkinType;method_47439()[Lnet/minecraft/entity/player/PlayerSkinType;
 *   Lnet/minecraft/entity/player/PlayerSkinType;values()[Lnet/minecraft/entity/player/PlayerSkinType;
 */
package net.minecraft.entity.player;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.Objects;
import java.util.function.Function;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.StringIdentifiable;
import org.jspecify.annotations.Nullable;

public enum PlayerSkinType implements StringIdentifiable
{
    SLIM("slim", "slim"),
    WIDE("wide", "default");

    public static final Codec<PlayerSkinType> CODEC;
    private static final Function<String, PlayerSkinType> BY_MODEL_METADATA;
    public static final PacketCodec<ByteBuf, PlayerSkinType> PACKET_CODEC;
    private final String name;
    private final String modelMetadata;

    private PlayerSkinType(String name, String modelMetadata) {
        this.name = name;
        this.modelMetadata = modelMetadata;
    }

    public static PlayerSkinType byModelMetadata(@Nullable String modelMetadata) {
        return Objects.requireNonNullElse(BY_MODEL_METADATA.apply(modelMetadata), WIDE);
    }

    @Override
    public String asString() {
        return this.name;
    }

    static {
        CODEC = StringIdentifiable.createCodec(PlayerSkinType::values);
        BY_MODEL_METADATA = StringIdentifiable.createMapper(PlayerSkinType.values(), arg -> arg.modelMetadata);
        PACKET_CODEC = PacketCodecs.BOOLEAN.xmap(slim -> slim != false ? SLIM : WIDE, model -> model == SLIM);
    }
}

