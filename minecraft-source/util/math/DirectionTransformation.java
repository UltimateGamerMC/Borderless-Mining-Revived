/*
 * External method calls:
 *   Lnet/minecraft/util/Util;mapEnum(Ljava/lang/Class;Ljava/util/function/Function;)Ljava/util/Map;
 *   Lnet/minecraft/block/enums/Orientation;byDirections(Lnet/minecraft/util/math/Direction;Lnet/minecraft/util/math/Direction;)Lnet/minecraft/block/enums/Orientation;
 *   Lnet/minecraft/util/Util;make(Ljava/util/function/Supplier;)Ljava/lang/Object;
 */
package net.minecraft.util.math;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.block.enums.Orientation;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.Util;
import net.minecraft.util.math.AxisTransformation;
import net.minecraft.util.math.Direction;
import org.joml.Matrix3f;
import org.joml.Matrix3fc;
import org.joml.Vector3i;
import org.jspecify.annotations.Nullable;

public enum DirectionTransformation implements StringIdentifiable
{
    IDENTITY("identity", AxisTransformation.P123, false, false, false),
    ROT_180_FACE_XY("rot_180_face_xy", AxisTransformation.P123, true, true, false),
    ROT_180_FACE_XZ("rot_180_face_xz", AxisTransformation.P123, true, false, true),
    ROT_180_FACE_YZ("rot_180_face_yz", AxisTransformation.P123, false, true, true),
    ROT_120_NNN("rot_120_nnn", AxisTransformation.P231, false, false, false),
    ROT_120_NNP("rot_120_nnp", AxisTransformation.P312, true, false, true),
    ROT_120_NPN("rot_120_npn", AxisTransformation.P312, false, true, true),
    ROT_120_NPP("rot_120_npp", AxisTransformation.P231, true, false, true),
    ROT_120_PNN("rot_120_pnn", AxisTransformation.P312, true, true, false),
    ROT_120_PNP("rot_120_pnp", AxisTransformation.P231, true, true, false),
    ROT_120_PPN("rot_120_ppn", AxisTransformation.P231, false, true, true),
    ROT_120_PPP("rot_120_ppp", AxisTransformation.P312, false, false, false),
    ROT_180_EDGE_XY_NEG("rot_180_edge_xy_neg", AxisTransformation.P213, true, true, true),
    ROT_180_EDGE_XY_POS("rot_180_edge_xy_pos", AxisTransformation.P213, false, false, true),
    ROT_180_EDGE_XZ_NEG("rot_180_edge_xz_neg", AxisTransformation.P321, true, true, true),
    ROT_180_EDGE_XZ_POS("rot_180_edge_xz_pos", AxisTransformation.P321, false, true, false),
    ROT_180_EDGE_YZ_NEG("rot_180_edge_yz_neg", AxisTransformation.P132, true, true, true),
    ROT_180_EDGE_YZ_POS("rot_180_edge_yz_pos", AxisTransformation.P132, true, false, false),
    ROT_90_X_NEG("rot_90_x_neg", AxisTransformation.P132, false, false, true),
    ROT_90_X_POS("rot_90_x_pos", AxisTransformation.P132, false, true, false),
    ROT_90_Y_NEG("rot_90_y_neg", AxisTransformation.P321, true, false, false),
    ROT_90_Y_POS("rot_90_y_pos", AxisTransformation.P321, false, false, true),
    ROT_90_Z_NEG("rot_90_z_neg", AxisTransformation.P213, false, true, false),
    ROT_90_Z_POS("rot_90_z_pos", AxisTransformation.P213, true, false, false),
    INVERSION("inversion", AxisTransformation.P123, true, true, true),
    INVERT_X("invert_x", AxisTransformation.P123, true, false, false),
    INVERT_Y("invert_y", AxisTransformation.P123, false, true, false),
    INVERT_Z("invert_z", AxisTransformation.P123, false, false, true),
    ROT_60_REF_NNN("rot_60_ref_nnn", AxisTransformation.P312, true, true, true),
    ROT_60_REF_NNP("rot_60_ref_nnp", AxisTransformation.P231, true, false, false),
    ROT_60_REF_NPN("rot_60_ref_npn", AxisTransformation.P231, false, false, true),
    ROT_60_REF_NPP("rot_60_ref_npp", AxisTransformation.P312, false, false, true),
    ROT_60_REF_PNN("rot_60_ref_pnn", AxisTransformation.P231, false, true, false),
    ROT_60_REF_PNP("rot_60_ref_pnp", AxisTransformation.P312, true, false, false),
    ROT_60_REF_PPN("rot_60_ref_ppn", AxisTransformation.P312, false, true, false),
    ROT_60_REF_PPP("rot_60_ref_ppp", AxisTransformation.P231, true, true, true),
    SWAP_XY("swap_xy", AxisTransformation.P213, false, false, false),
    SWAP_YZ("swap_yz", AxisTransformation.P132, false, false, false),
    SWAP_XZ("swap_xz", AxisTransformation.P321, false, false, false),
    SWAP_NEG_XY("swap_neg_xy", AxisTransformation.P213, true, true, false),
    SWAP_NEG_YZ("swap_neg_yz", AxisTransformation.P132, false, true, true),
    SWAP_NEG_XZ("swap_neg_xz", AxisTransformation.P321, true, false, true),
    ROT_90_REF_X_NEG("rot_90_ref_x_neg", AxisTransformation.P132, true, false, true),
    ROT_90_REF_X_POS("rot_90_ref_x_pos", AxisTransformation.P132, true, true, false),
    ROT_90_REF_Y_NEG("rot_90_ref_y_neg", AxisTransformation.P321, true, true, false),
    ROT_90_REF_Y_POS("rot_90_ref_y_pos", AxisTransformation.P321, false, true, true),
    ROT_90_REF_Z_NEG("rot_90_ref_z_neg", AxisTransformation.P213, false, true, true),
    ROT_90_REF_Z_POS("rot_90_ref_z_pos", AxisTransformation.P213, true, false, true);

    public static final DirectionTransformation field_64506;
    public static final DirectionTransformation field_64507;
    public static final DirectionTransformation field_64508;
    public static final DirectionTransformation field_64509;
    public static final DirectionTransformation field_64510;
    public static final DirectionTransformation field_64511;
    public static final DirectionTransformation field_64512;
    public static final DirectionTransformation field_64513;
    public static final DirectionTransformation field_64514;
    private final Matrix3fc matrix;
    private final String name;
    private @Nullable Map<Direction, Direction> mappings;
    private final boolean flipX;
    private final boolean flipY;
    private final boolean flipZ;
    private final AxisTransformation axisTransformation;
    private static final DirectionTransformation[][] COMBINATIONS;
    private static final DirectionTransformation[] INVERSES;

    private DirectionTransformation(String name, AxisTransformation axisTransformation, boolean flipX, boolean flipY, boolean flipZ) {
        this.name = name;
        this.flipX = flipX;
        this.flipY = flipY;
        this.flipZ = flipZ;
        this.axisTransformation = axisTransformation;
        this.matrix = new Matrix3f().scaling(flipX ? -1.0f : 1.0f, flipY ? -1.0f : 1.0f, flipZ ? -1.0f : 1.0f).mul(axisTransformation.getMatrix());
    }

    private static int toIndex(boolean flipX, boolean flipY, boolean flipZ, AxisTransformation axisTransformation) {
        int i = (flipZ ? 4 : 0) + (flipY ? 2 : 0) + (flipX ? 1 : 0);
        return axisTransformation.ordinal() << 3 | i;
    }

    private int getIndex() {
        return DirectionTransformation.toIndex(this.flipX, this.flipY, this.flipZ, this.axisTransformation);
    }

    public DirectionTransformation prepend(DirectionTransformation transformation) {
        return COMBINATIONS[this.ordinal()][transformation.ordinal()];
    }

    public DirectionTransformation inverse() {
        return INVERSES[this.ordinal()];
    }

    public Matrix3fc getMatrix() {
        return this.matrix;
    }

    public String toString() {
        return this.name;
    }

    @Override
    public String asString() {
        return this.name;
    }

    public Direction map(Direction direction) {
        if (this.mappings == null) {
            this.mappings = Util.mapEnum(Direction.class, d -> {
                Direction.Axis lv = d.getAxis();
                Direction.AxisDirection lv2 = d.getDirection();
                Direction.Axis lv3 = this.axisTransformation.getInverse().map(lv);
                Direction.AxisDirection lv4 = this.shouldFlipDirection(lv3) ? lv2.getOpposite() : lv2;
                return Direction.from(lv3, lv4);
            });
        }
        return this.mappings.get(direction);
    }

    public Vector3i map(Vector3i vec) {
        this.axisTransformation.map(vec);
        vec.x = vec.x * (this.flipX ? -1 : 1);
        vec.y = vec.y * (this.flipY ? -1 : 1);
        vec.z = vec.z * (this.flipZ ? -1 : 1);
        return vec;
    }

    public boolean shouldFlipDirection(Direction.Axis axis) {
        return switch (axis) {
            default -> throw new MatchException(null, null);
            case Direction.Axis.X -> this.flipX;
            case Direction.Axis.Y -> this.flipY;
            case Direction.Axis.Z -> this.flipZ;
        };
    }

    public AxisTransformation getAxisTransformation() {
        return this.axisTransformation;
    }

    public Orientation mapJigsawOrientation(Orientation orientation) {
        return Orientation.byDirections(this.map(orientation.getFacing()), this.map(orientation.getRotation()));
    }

    static {
        field_64506 = ROT_90_X_POS;
        field_64507 = ROT_180_FACE_YZ;
        field_64508 = ROT_90_X_NEG;
        field_64509 = ROT_90_Y_POS;
        field_64510 = ROT_180_FACE_XZ;
        field_64511 = ROT_90_Y_NEG;
        field_64512 = ROT_90_Z_POS;
        field_64513 = ROT_180_FACE_XY;
        field_64514 = ROT_90_Z_NEG;
        COMBINATIONS = Util.make(() -> {
            DirectionTransformation[] lvs = DirectionTransformation.values();
            DirectionTransformation[][] lvs2 = new DirectionTransformation[lvs.length][lvs.length];
            Map<Integer, DirectionTransformation> map = Arrays.stream(lvs).collect(Collectors.toMap(DirectionTransformation::getIndex, transformation -> transformation));
            for (DirectionTransformation lv : lvs) {
                for (DirectionTransformation lv2 : lvs) {
                    AxisTransformation lv3 = lv2.axisTransformation.prepend(lv.axisTransformation);
                    boolean bl = lv.shouldFlipDirection(Direction.Axis.X) ^ lv2.shouldFlipDirection(lv.axisTransformation.map(Direction.Axis.X));
                    boolean bl2 = lv.shouldFlipDirection(Direction.Axis.Y) ^ lv2.shouldFlipDirection(lv.axisTransformation.map(Direction.Axis.Y));
                    boolean bl3 = lv.shouldFlipDirection(Direction.Axis.Z) ^ lv2.shouldFlipDirection(lv.axisTransformation.map(Direction.Axis.Z));
                    lvs2[lv.ordinal()][lv2.ordinal()] = map.get(DirectionTransformation.toIndex(bl, bl2, bl3, lv3));
                }
            }
            return lvs2;
        });
        INVERSES = (DirectionTransformation[])Arrays.stream(DirectionTransformation.values()).map((? super T a) -> Arrays.stream(DirectionTransformation.values()).filter(b -> a.prepend((DirectionTransformation)b) == IDENTITY).findAny().get()).toArray(DirectionTransformation[]::new);
    }
}

