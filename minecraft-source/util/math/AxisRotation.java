package net.minecraft.util.math;

import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.util.math.DirectionTransformation;
import net.minecraft.util.math.MathHelper;

public enum AxisRotation {
    R0(0, DirectionTransformation.IDENTITY, DirectionTransformation.IDENTITY, DirectionTransformation.IDENTITY),
    R90(1, DirectionTransformation.field_64508, DirectionTransformation.field_64511, DirectionTransformation.field_64514),
    R180(2, DirectionTransformation.field_64507, DirectionTransformation.field_64510, DirectionTransformation.field_64513),
    R270(3, DirectionTransformation.field_64506, DirectionTransformation.field_64509, DirectionTransformation.field_64512);

    public static final Codec<AxisRotation> CODEC;
    public final int index;
    public final DirectionTransformation field_64521;
    public final DirectionTransformation field_64522;
    public final DirectionTransformation field_64523;

    private AxisRotation(int index, DirectionTransformation arg, DirectionTransformation arg2, DirectionTransformation arg3) {
        this.index = index;
        this.field_64521 = arg;
        this.field_64522 = arg2;
        this.field_64523 = arg3;
    }

    @Deprecated
    public static AxisRotation fromDegrees(int degrees) {
        return switch (MathHelper.floorMod(degrees, 360)) {
            case 0 -> R0;
            case 90 -> R90;
            case 180 -> R180;
            case 270 -> R270;
            default -> throw new JsonParseException("Invalid rotation " + degrees + " found, only 0/90/180/270 allowed");
        };
    }

    public static DirectionTransformation method_76599(AxisRotation arg, AxisRotation arg2) {
        return arg2.field_64522.prepend(arg.field_64521);
    }

    public static DirectionTransformation method_76600(AxisRotation arg, AxisRotation arg2, AxisRotation arg3) {
        return arg3.field_64523.prepend(arg2.field_64522.prepend(arg.field_64521));
    }

    public int rotate(int index) {
        return (index + this.index) % 4;
    }

    static {
        CODEC = Codec.INT.comapFlatMap(degrees -> switch (MathHelper.floorMod(degrees, 360)) {
            case 0 -> DataResult.success(R0);
            case 90 -> DataResult.success(R90);
            case 180 -> DataResult.success(R180);
            case 270 -> DataResult.success(R270);
            default -> DataResult.error(() -> "Invalid rotation " + degrees + " found, only 0/90/180/270 allowed");
        }, rotation -> switch (rotation.ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> 0;
            case 1 -> 90;
            case 2 -> 180;
            case 3 -> 270;
        });
    }
}

