/*
 * External method calls:
 *   Lnet/minecraft/util/function/ValueLists;createIndexToValueFunction(Ljava/util/function/ToIntFunction;[Ljava/lang/Object;Lnet/minecraft/util/function/ValueLists$OutOfBoundsHandling;)Ljava/util/function/IntFunction;
 *   Lnet/minecraft/util/StringIdentifiable;createCodec(Ljava/util/function/Supplier;)Lnet/minecraft/util/StringIdentifiable$EnumCodec;
 *   Lnet/minecraft/network/codec/PacketCodecs;indexed(Ljava/util/function/IntFunction;Ljava/util/function/ToIntFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/EntityPose;method_36612()[Lnet/minecraft/entity/EntityPose;
 *   Lnet/minecraft/entity/EntityPose;values()[Lnet/minecraft/entity/EntityPose;
 */
package net.minecraft.entity;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;

public enum EntityPose implements StringIdentifiable
{
    STANDING(0, "standing"),
    GLIDING(1, "fall_flying"),
    SLEEPING(2, "sleeping"),
    SWIMMING(3, "swimming"),
    SPIN_ATTACK(4, "spin_attack"),
    CROUCHING(5, "crouching"),
    LONG_JUMPING(6, "long_jumping"),
    DYING(7, "dying"),
    CROAKING(8, "croaking"),
    USING_TONGUE(9, "using_tongue"),
    SITTING(10, "sitting"),
    ROARING(11, "roaring"),
    SNIFFING(12, "sniffing"),
    EMERGING(13, "emerging"),
    DIGGING(14, "digging"),
    SLIDING(15, "sliding"),
    SHOOTING(16, "shooting"),
    INHALING(17, "inhaling");

    public static final IntFunction<EntityPose> INDEX_TO_VALUE;
    public static final Codec<EntityPose> CODEC;
    public static final PacketCodec<ByteBuf, EntityPose> PACKET_CODEC;
    private final int index;
    private final String name;

    private EntityPose(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public int getIndex() {
        return this.index;
    }

    @Override
    public String asString() {
        return this.name;
    }

    static {
        INDEX_TO_VALUE = ValueLists.createIndexToValueFunction(EntityPose::getIndex, EntityPose.values(), ValueLists.OutOfBoundsHandling.ZERO);
        CODEC = StringIdentifiable.createCodec(EntityPose::values);
        PACKET_CODEC = PacketCodecs.indexed(INDEX_TO_VALUE, EntityPose::getIndex);
    }
}

