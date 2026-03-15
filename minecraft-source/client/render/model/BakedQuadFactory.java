/*
 * External method calls:
 *   Lnet/minecraft/client/render/model/json/ModelElementFace;uvs()Lnet/minecraft/client/render/model/json/ModelElementFace$UV;
 *   Lnet/minecraft/client/render/model/ModelBakeSettings;reverse(Lnet/minecraft/util/math/Direction;)Lorg/joml/Matrix4fc;
 *   Lnet/minecraft/client/render/model/json/ModelElementFace;rotation()Lnet/minecraft/util/math/AxisRotation;
 *   Lnet/minecraft/client/render/model/json/ModelElementRotation;origin()Lorg/joml/Vector3fc;
 *   Lnet/minecraft/client/render/model/json/ModelElementRotation;transform()Lorg/joml/Matrix4fc;
 *   Lnet/minecraft/client/render/model/Baker$Vec3fInterner;intern(Lorg/joml/Vector3fc;)Lorg/joml/Vector3fc;
 *   Lnet/minecraft/client/render/model/CubeFace$Corner;xSide()Lnet/minecraft/client/render/model/CubeFace$CornerCoord;
 *   Lnet/minecraft/client/render/model/CubeFace$Corner;ySide()Lnet/minecraft/client/render/model/CubeFace$CornerCoord;
 *   Lnet/minecraft/client/render/model/CubeFace$Corner;zSide()Lnet/minecraft/client/render/model/CubeFace$CornerCoord;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/model/BakedQuadFactory;packVertexData(ILnet/minecraft/client/render/model/CubeFace;Lnet/minecraft/client/render/model/json/ModelElementFace$UV;Lnet/minecraft/util/math/AxisRotation;Lorg/joml/Matrix4fc;Lorg/joml/Vector3fc;Lorg/joml/Vector3fc;Lnet/minecraft/client/texture/Sprite;Lnet/minecraft/util/math/AffineTransformation;Lnet/minecraft/client/render/model/json/ModelElementRotation;[Lorg/joml/Vector3fc;[JLnet/minecraft/client/render/model/Baker$Vec3fInterner;)V
 *   Lnet/minecraft/client/render/model/BakedQuadFactory;decodeDirection([Lorg/joml/Vector3fc;)Lnet/minecraft/util/math/Direction;
 *   Lnet/minecraft/client/render/model/BakedQuadFactory;encodeDirection([Lorg/joml/Vector3fc;[JLnet/minecraft/util/math/Direction;)V
 *   Lnet/minecraft/client/render/model/BakedQuadFactory;transformVertex(Lorg/joml/Vector3f;Lorg/joml/Vector3fc;Lorg/joml/Matrix4fc;)V
 *   Lnet/minecraft/client/render/model/BakedQuadFactory;method_76655([Lorg/joml/Vector3fc;IFFF)I
 *   Lnet/minecraft/client/render/model/BakedQuadFactory;method_76656([Lorg/joml/Vector3fc;II)V
 *   Lnet/minecraft/client/render/model/BakedQuadFactory;method_76654([JII)V
 */
package net.minecraft.client.render.model;

import com.google.common.annotations.VisibleForTesting;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.CubeFace;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.json.ModelElementFace;
import net.minecraft.client.render.model.json.ModelElementRotation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.util.math.AffineTransformation;
import net.minecraft.util.math.AxisRotation;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MatrixUtil;
import org.joml.GeometryUtils;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class BakedQuadFactory {
    private static final Vector3fc CENTER = new Vector3f(0.5f, 0.5f, 0.5f);

    @VisibleForTesting
    static ModelElementFace.UV setDefaultUV(Vector3fc from, Vector3fc to, Direction facing) {
        return switch (facing) {
            default -> throw new MatchException(null, null);
            case Direction.DOWN -> new ModelElementFace.UV(from.x(), 16.0f - to.z(), to.x(), 16.0f - from.z());
            case Direction.UP -> new ModelElementFace.UV(from.x(), from.z(), to.x(), to.z());
            case Direction.NORTH -> new ModelElementFace.UV(16.0f - to.x(), 16.0f - to.y(), 16.0f - from.x(), 16.0f - from.y());
            case Direction.SOUTH -> new ModelElementFace.UV(from.x(), 16.0f - to.y(), to.x(), 16.0f - from.y());
            case Direction.WEST -> new ModelElementFace.UV(from.z(), 16.0f - to.y(), to.z(), 16.0f - from.y());
            case Direction.EAST -> new ModelElementFace.UV(16.0f - to.z(), 16.0f - to.y(), 16.0f - from.z(), 16.0f - from.y());
        };
    }

    public static BakedQuad bake(Baker.Vec3fInterner interner, Vector3fc from, Vector3fc to, ModelElementFace face, Sprite sprite, Direction direction, ModelBakeSettings settings, @Nullable ModelElementRotation rotation, boolean shade, int lightEmission) {
        ModelElementFace.UV lv = face.uvs();
        if (lv == null) {
            lv = BakedQuadFactory.setDefaultUV(from, to, direction);
        }
        Matrix4fc matrix4fc = settings.reverse(direction);
        Vector3fc[] vector3fcs = new Vector3fc[4];
        long[] ls = new long[4];
        CubeFace lv2 = CubeFace.getFace(direction);
        for (int j = 0; j < 4; ++j) {
            BakedQuadFactory.packVertexData(j, lv2, lv, face.rotation(), matrix4fc, from, to, sprite, settings.getRotation(), rotation, vector3fcs, ls, interner);
        }
        Direction lv3 = BakedQuadFactory.decodeDirection(vector3fcs);
        if (rotation == null && lv3 != null) {
            BakedQuadFactory.encodeDirection(vector3fcs, ls, lv3);
        }
        return new BakedQuad(vector3fcs[0], vector3fcs[1], vector3fcs[2], vector3fcs[3], ls[0], ls[1], ls[2], ls[3], face.tintIndex(), Objects.requireNonNullElse(lv3, Direction.UP), sprite, shade, lightEmission);
    }

    private static void packVertexData(int corner, CubeFace cubeFace, ModelElementFace.UV uv, AxisRotation axisRotation, Matrix4fc matrix, Vector3fc from, Vector3fc to, Sprite sprite, AffineTransformation affineTransformation, @Nullable ModelElementRotation rotation, Vector3fc[] positions, long[] packedUvs, Baker.Vec3fInterner interner) {
        float j;
        float h;
        CubeFace.Corner lv = cubeFace.getCorner(corner);
        Vector3f vector3f = lv.get(from, to).div(16.0f);
        if (rotation != null) {
            BakedQuadFactory.transformVertex(vector3f, rotation.origin(), rotation.transform());
        }
        if (affineTransformation != AffineTransformation.identity()) {
            BakedQuadFactory.transformVertex(vector3f, CENTER, affineTransformation.getMatrix());
        }
        float f = ModelElementFace.getUValue(uv, axisRotation, corner);
        float g = ModelElementFace.getVValue(uv, axisRotation, corner);
        if (MatrixUtil.isIdentity(matrix)) {
            h = f;
            j = g;
        } else {
            Vector3f vector3f2 = matrix.transformPosition(new Vector3f(BakedQuadFactory.setCenterBack(f), BakedQuadFactory.setCenterBack(g), 0.0f));
            h = BakedQuadFactory.setCenterForward(vector3f2.x);
            j = BakedQuadFactory.setCenterForward(vector3f2.y);
        }
        positions[corner] = interner.intern(vector3f);
        packedUvs[corner] = Vector2f.toLong(sprite.getFrameU(h), sprite.getFrameV(j));
    }

    private static float setCenterBack(float f) {
        return f - 0.5f;
    }

    private static float setCenterForward(float f) {
        return f + 0.5f;
    }

    private static void transformVertex(Vector3f vertex, Vector3fc vector3fc, Matrix4fc matrix4fc) {
        vertex.sub(vector3fc);
        matrix4fc.transformPosition(vertex);
        vertex.add(vector3fc);
    }

    private static @Nullable Direction decodeDirection(Vector3fc[] vecs) {
        Vector3f vector3f = new Vector3f();
        GeometryUtils.normal(vecs[0], vecs[1], vecs[2], vector3f);
        return BakedQuadFactory.getDirection(vector3f);
    }

    private static @Nullable Direction getDirection(Vector3f vec) {
        if (!vec.isFinite()) {
            return null;
        }
        Direction lv = null;
        float f = 0.0f;
        for (Direction lv2 : Direction.values()) {
            float g = vec.dot(lv2.getFloatVector());
            if (!(g >= 0.0f) || !(g > f)) continue;
            f = g;
            lv = lv2;
        }
        return lv;
    }

    private static void encodeDirection(Vector3fc[] vector3fcs, long[] ls, Direction arg) {
        float o;
        float n;
        float f = 999.0f;
        float g = 999.0f;
        float h = 999.0f;
        float i = -999.0f;
        float j = -999.0f;
        float k = -999.0f;
        for (int l = 0; l < 4; ++l) {
            Vector3fc vector3fc = vector3fcs[l];
            float m = vector3fc.x();
            n = vector3fc.y();
            o = vector3fc.z();
            if (m < f) {
                f = m;
            }
            if (n < g) {
                g = n;
            }
            if (o < h) {
                h = o;
            }
            if (m > i) {
                i = m;
            }
            if (n > j) {
                j = n;
            }
            if (!(o > k)) continue;
            k = o;
        }
        CubeFace lv = CubeFace.getFace(arg);
        for (int p = 0; p < 4; ++p) {
            float q;
            CubeFace.Corner lv2 = lv.getCorner(p);
            n = lv2.xSide().get(f, g, h, i, j, k);
            int r = BakedQuadFactory.method_76655(vector3fcs, p, n, o = lv2.ySide().get(f, g, h, i, j, k), q = lv2.zSide().get(f, g, h, i, j, k));
            if (r == -1) {
                throw new IllegalStateException("Can't find vertex to swap");
            }
            if (r == p) continue;
            BakedQuadFactory.method_76656(vector3fcs, r, p);
            BakedQuadFactory.method_76654(ls, r, p);
        }
    }

    private static int method_76655(Vector3fc[] vector3fcs, int i, float f, float g, float h) {
        for (int j = i; j < 4; ++j) {
            Vector3fc vector3fc = vector3fcs[j];
            if (f != vector3fc.x() || g != vector3fc.y() || h != vector3fc.z()) continue;
            return j;
        }
        return -1;
    }

    private static void method_76656(Vector3fc[] vector3fcs, int i, int j) {
        Vector3fc vector3fc = vector3fcs[i];
        vector3fcs[i] = vector3fcs[j];
        vector3fcs[j] = vector3fc;
    }

    private static void method_76654(long[] ls, int i, int j) {
        long l = ls[i];
        ls[i] = ls[j];
        ls[j] = l;
    }
}

