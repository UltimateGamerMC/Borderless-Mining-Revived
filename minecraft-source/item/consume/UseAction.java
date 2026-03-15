/*
 * External method calls:
 *   Lnet/minecraft/util/function/ValueLists;createIndexToValueFunction(Ljava/util/function/ToIntFunction;[Ljava/lang/Object;Lnet/minecraft/util/function/ValueLists$OutOfBoundsHandling;)Ljava/util/function/IntFunction;
 *   Lnet/minecraft/util/StringIdentifiable;createCodec(Ljava/util/function/Supplier;)Lnet/minecraft/util/StringIdentifiable$EnumCodec;
 *   Lnet/minecraft/network/codec/PacketCodecs;indexed(Ljava/util/function/IntFunction;Ljava/util/function/ToIntFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/item/consume/UseAction;method_36686()[Lnet/minecraft/item/consume/UseAction;
 *   Lnet/minecraft/item/consume/UseAction;values()[Lnet/minecraft/item/consume/UseAction;
 */
package net.minecraft.item.consume;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;

public enum UseAction implements StringIdentifiable
{
    NONE(0, "none"),
    EAT(1, "eat", true),
    DRINK(2, "drink", true),
    BLOCK(3, "block"),
    BOW(4, "bow"),
    TRIDENT(5, "trident"),
    CROSSBOW(6, "crossbow"),
    SPYGLASS(7, "spyglass"),
    TOOT_HORN(8, "toot_horn"),
    BRUSH(9, "brush"),
    BUNDLE(10, "bundle"),
    SPEAR(11, "spear", true);

    private static final IntFunction<UseAction> BY_ID;
    public static final Codec<UseAction> CODEC;
    public static final PacketCodec<ByteBuf, UseAction> PACKET_CODEC;
    private final int id;
    private final String name;
    private final boolean noOffset;

    private UseAction(int id, String name) {
        this(id, name, false);
    }

    private UseAction(int id, String name, boolean noOffset) {
        this.id = id;
        this.name = name;
        this.noOffset = noOffset;
    }

    public int getId() {
        return this.id;
    }

    @Override
    public String asString() {
        return this.name;
    }

    public boolean hasNoOffset() {
        return this.noOffset;
    }

    static {
        BY_ID = ValueLists.createIndexToValueFunction(UseAction::getId, UseAction.values(), ValueLists.OutOfBoundsHandling.ZERO);
        CODEC = StringIdentifiable.createCodec(UseAction::values);
        PACKET_CODEC = PacketCodecs.indexed(BY_ID, UseAction::getId);
    }
}

