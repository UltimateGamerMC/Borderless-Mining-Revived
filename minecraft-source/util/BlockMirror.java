/*
 * External method calls:
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/util/StringIdentifiable;createCodec(Ljava/util/function/Supplier;)Lnet/minecraft/util/StringIdentifiable$EnumCodec;
 *   Lnet/minecraft/util/dynamic/Codecs;enumByName(Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/util/BlockMirror;method_36706()[Lnet/minecraft/util/BlockMirror;
 */
package net.minecraft.util;

import com.mojang.serialization.Codec;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.DirectionTransformation;

public enum BlockMirror implements StringIdentifiable
{
    NONE("none", DirectionTransformation.IDENTITY),
    LEFT_RIGHT("left_right", DirectionTransformation.INVERT_Z),
    FRONT_BACK("front_back", DirectionTransformation.INVERT_X);

    public static final Codec<BlockMirror> CODEC;
    @Deprecated
    public static final Codec<BlockMirror> ENUM_NAME_CODEC;
    private final String id;
    private final Text name;
    private final DirectionTransformation directionTransformation;

    private BlockMirror(String id, DirectionTransformation directionTransformation) {
        this.id = id;
        this.name = Text.translatable("mirror." + id);
        this.directionTransformation = directionTransformation;
    }

    public int mirror(int rotation, int fullTurn) {
        int k = fullTurn / 2;
        int l = rotation > k ? rotation - fullTurn : rotation;
        switch (this.ordinal()) {
            case 2: {
                return (fullTurn - l) % fullTurn;
            }
            case 1: {
                return (k - l + fullTurn) % fullTurn;
            }
        }
        return rotation;
    }

    public BlockRotation getRotation(Direction direction) {
        Direction.Axis lv = direction.getAxis();
        return this == LEFT_RIGHT && lv == Direction.Axis.Z || this == FRONT_BACK && lv == Direction.Axis.X ? BlockRotation.CLOCKWISE_180 : BlockRotation.NONE;
    }

    public Direction apply(Direction direction) {
        if (this == FRONT_BACK && direction.getAxis() == Direction.Axis.X) {
            return direction.getOpposite();
        }
        if (this == LEFT_RIGHT && direction.getAxis() == Direction.Axis.Z) {
            return direction.getOpposite();
        }
        return direction;
    }

    public DirectionTransformation getDirectionTransformation() {
        return this.directionTransformation;
    }

    public Text getName() {
        return this.name;
    }

    @Override
    public String asString() {
        return this.id;
    }

    static {
        CODEC = StringIdentifiable.createCodec(BlockMirror::values);
        ENUM_NAME_CODEC = Codecs.enumByName(BlockMirror::valueOf);
    }
}

