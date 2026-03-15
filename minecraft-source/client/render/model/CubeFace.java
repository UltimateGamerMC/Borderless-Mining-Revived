/*
 * External method calls:
 *   Lnet/minecraft/util/Util;make(Ljava/lang/Object;Ljava/util/function/Consumer;)Ljava/lang/Object;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/model/CubeFace;method_36913()[Lnet/minecraft/client/render/model/CubeFace;
 */
package net.minecraft.client.render.model;

import java.util.EnumMap;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Util;
import net.minecraft.util.math.Direction;
import org.joml.Vector3f;
import org.joml.Vector3fc;

@Environment(value=EnvType.CLIENT)
public enum CubeFace {
    DOWN(new Corner(CornerCoord.MIN_X, CornerCoord.MIN_Y, CornerCoord.MAX_Z), new Corner(CornerCoord.MIN_X, CornerCoord.MIN_Y, CornerCoord.MIN_Z), new Corner(CornerCoord.MAX_X, CornerCoord.MIN_Y, CornerCoord.MIN_Z), new Corner(CornerCoord.MAX_X, CornerCoord.MIN_Y, CornerCoord.MAX_Z)),
    UP(new Corner(CornerCoord.MIN_X, CornerCoord.MAX_Y, CornerCoord.MIN_Z), new Corner(CornerCoord.MIN_X, CornerCoord.MAX_Y, CornerCoord.MAX_Z), new Corner(CornerCoord.MAX_X, CornerCoord.MAX_Y, CornerCoord.MAX_Z), new Corner(CornerCoord.MAX_X, CornerCoord.MAX_Y, CornerCoord.MIN_Z)),
    NORTH(new Corner(CornerCoord.MAX_X, CornerCoord.MAX_Y, CornerCoord.MIN_Z), new Corner(CornerCoord.MAX_X, CornerCoord.MIN_Y, CornerCoord.MIN_Z), new Corner(CornerCoord.MIN_X, CornerCoord.MIN_Y, CornerCoord.MIN_Z), new Corner(CornerCoord.MIN_X, CornerCoord.MAX_Y, CornerCoord.MIN_Z)),
    SOUTH(new Corner(CornerCoord.MIN_X, CornerCoord.MAX_Y, CornerCoord.MAX_Z), new Corner(CornerCoord.MIN_X, CornerCoord.MIN_Y, CornerCoord.MAX_Z), new Corner(CornerCoord.MAX_X, CornerCoord.MIN_Y, CornerCoord.MAX_Z), new Corner(CornerCoord.MAX_X, CornerCoord.MAX_Y, CornerCoord.MAX_Z)),
    WEST(new Corner(CornerCoord.MIN_X, CornerCoord.MAX_Y, CornerCoord.MIN_Z), new Corner(CornerCoord.MIN_X, CornerCoord.MIN_Y, CornerCoord.MIN_Z), new Corner(CornerCoord.MIN_X, CornerCoord.MIN_Y, CornerCoord.MAX_Z), new Corner(CornerCoord.MIN_X, CornerCoord.MAX_Y, CornerCoord.MAX_Z)),
    EAST(new Corner(CornerCoord.MAX_X, CornerCoord.MAX_Y, CornerCoord.MAX_Z), new Corner(CornerCoord.MAX_X, CornerCoord.MIN_Y, CornerCoord.MAX_Z), new Corner(CornerCoord.MAX_X, CornerCoord.MIN_Y, CornerCoord.MIN_Z), new Corner(CornerCoord.MAX_X, CornerCoord.MAX_Y, CornerCoord.MIN_Z));

    private static final Map<Direction, CubeFace> DIRECTION_LOOKUP;
    private final Corner[] corners;

    public static CubeFace getFace(Direction direction) {
        return DIRECTION_LOOKUP.get(direction);
    }

    private CubeFace(Corner ... corners) {
        this.corners = corners;
    }

    public Corner getCorner(int corner) {
        return this.corners[corner];
    }

    static {
        DIRECTION_LOOKUP = Util.make(new EnumMap(Direction.class), map -> {
            map.put(Direction.DOWN, DOWN);
            map.put(Direction.UP, UP);
            map.put(Direction.NORTH, NORTH);
            map.put(Direction.SOUTH, SOUTH);
            map.put(Direction.WEST, WEST);
            map.put(Direction.EAST, EAST);
        });
    }

    @Environment(value=EnvType.CLIENT)
    public record Corner(CornerCoord xSide, CornerCoord ySide, CornerCoord zSide) {
        public Vector3f get(Vector3fc from, Vector3fc to) {
            return new Vector3f(this.xSide.get(from, to), this.ySide.get(from, to), this.zSide.get(from, to));
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static enum CornerCoord {
        MIN_X,
        MIN_Y,
        MIN_Z,
        MAX_X,
        MAX_Y,
        MAX_Z;


        public float get(Vector3fc from, Vector3fc to) {
            return switch (this.ordinal()) {
                default -> throw new MatchException(null, null);
                case 0 -> from.x();
                case 1 -> from.y();
                case 2 -> from.z();
                case 3 -> to.x();
                case 4 -> to.y();
                case 5 -> to.z();
            };
        }

        public float get(float fromX, float fromY, float fromZ, float toX, float toY, float toZ) {
            return switch (this.ordinal()) {
                default -> throw new MatchException(null, null);
                case 0 -> fromX;
                case 1 -> fromY;
                case 2 -> fromZ;
                case 3 -> toX;
                case 4 -> toY;
                case 5 -> toZ;
            };
        }
    }
}

