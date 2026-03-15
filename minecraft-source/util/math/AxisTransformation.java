/*
 * External method calls:
 *   Lnet/minecraft/util/Util;make(Ljava/util/function/Supplier;)Ljava/lang/Object;
 */
package net.minecraft.util.math;

import java.util.Arrays;
import net.minecraft.util.Util;
import net.minecraft.util.math.Direction;
import org.joml.Matrix3f;
import org.joml.Matrix3fc;
import org.joml.Vector3f;
import org.joml.Vector3i;

public enum AxisTransformation {
    P123(0, 1, 2),
    P213(1, 0, 2),
    P132(0, 2, 1),
    P312(2, 0, 1),
    P231(1, 2, 0),
    P321(2, 1, 0);

    private final int xMapping;
    private final int yMapping;
    private final int zMapping;
    private final Matrix3fc matrix;
    private static final AxisTransformation[][] COMBINATIONS;
    private static final AxisTransformation[] INVERSE;

    private AxisTransformation(int xMapping, int yMapping, int zMapping) {
        this.xMapping = xMapping;
        this.yMapping = yMapping;
        this.zMapping = zMapping;
        this.matrix = new Matrix3f().zero().set(this.map(0), 0, 1.0f).set(this.map(1), 1, 1.0f).set(this.map(2), 2, 1.0f);
    }

    public AxisTransformation prepend(AxisTransformation transformation) {
        return COMBINATIONS[this.ordinal()][transformation.ordinal()];
    }

    public AxisTransformation getInverse() {
        return INVERSE[this.ordinal()];
    }

    public int map(int axis) {
        return switch (axis) {
            case 0 -> this.xMapping;
            case 1 -> this.yMapping;
            case 2 -> this.zMapping;
            default -> throw new IllegalArgumentException("Must be 0, 1 or 2, but got " + axis);
        };
    }

    public Direction.Axis map(Direction.Axis axis) {
        return Direction.Axis.VALUES[this.map(axis.ordinal())];
    }

    public Vector3f map(Vector3f vec) {
        float f = vec.get(this.xMapping);
        float g = vec.get(this.yMapping);
        float h = vec.get(this.zMapping);
        return vec.set(f, g, h);
    }

    public Vector3i map(Vector3i vec) {
        int i = vec.get(this.xMapping);
        int j = vec.get(this.yMapping);
        int k = vec.get(this.zMapping);
        return vec.set(i, j, k);
    }

    public Matrix3fc getMatrix() {
        return this.matrix;
    }

    static {
        COMBINATIONS = Util.make(() -> {
            AxisTransformation[] lvs = AxisTransformation.values();
            AxisTransformation[][] lvs2 = new AxisTransformation[lvs.length][lvs.length];
            for (AxisTransformation lv : lvs) {
                for (AxisTransformation lv2 : lvs) {
                    AxisTransformation lv3;
                    int i = lv.map(lv2.xMapping);
                    int j = lv.map(lv2.yMapping);
                    int k = lv.map(lv2.zMapping);
                    lvs2[lv.ordinal()][lv2.ordinal()] = lv3 = Arrays.stream(lvs).filter(transformation -> transformation.xMapping == i && transformation.yMapping == j && transformation.zMapping == k).findFirst().get();
                }
            }
            return lvs2;
        });
        INVERSE = Util.make(() -> {
            AxisTransformation[] lvs = AxisTransformation.values();
            return (AxisTransformation[])Arrays.stream(lvs).map((? super T a) -> Arrays.stream(AxisTransformation.values()).filter(b -> a.prepend((AxisTransformation)((Object)((Object)((Object)b)))) == P123).findAny().get()).toArray(AxisTransformation[]::new);
        });
    }
}

