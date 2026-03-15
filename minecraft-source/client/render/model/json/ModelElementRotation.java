/*
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/model/json/ModelElementRotation;transform(Lnet/minecraft/client/render/model/json/ModelElementRotation$RotationValue;Z)Lorg/joml/Matrix4f;
 *   Lnet/minecraft/client/render/model/json/ModelElementRotation;scale(Lorg/joml/Matrix4fc;)Lorg/joml/Vector3fc;
 *   Lnet/minecraft/client/render/model/json/ModelElementRotation;scale(Lorg/joml/Matrix4fc;Lnet/minecraft/util/math/Direction$Axis;Lorg/joml/Vector3f;)F
 */
package net.minecraft.client.render.model.json;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MatrixUtil;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

@Environment(value=EnvType.CLIENT)
public record ModelElementRotation(Vector3fc origin, RotationValue value, boolean rescale, Matrix4fc transform) {
    public ModelElementRotation(Vector3fc origin, RotationValue value, boolean rescale) {
        this(origin, value, rescale, ModelElementRotation.transform(value, rescale));
    }

    private static Matrix4f transform(RotationValue value, boolean rescale) {
        Matrix4f matrix4f = value.getMatrix();
        if (rescale && !MatrixUtil.isIdentity(matrix4f)) {
            Vector3fc vector3fc = ModelElementRotation.scale(matrix4f);
            matrix4f.scale(vector3fc);
        }
        return matrix4f;
    }

    private static Vector3fc scale(Matrix4fc matrix) {
        Vector3f vector3f = new Vector3f();
        float f = ModelElementRotation.scale(matrix, Direction.Axis.X, vector3f);
        float g = ModelElementRotation.scale(matrix, Direction.Axis.Y, vector3f);
        float h = ModelElementRotation.scale(matrix, Direction.Axis.Z, vector3f);
        return vector3f.set(f, g, h);
    }

    private static float scale(Matrix4fc matrix, Direction.Axis axis, Vector3f vec) {
        Vector3f vector3f2 = vec.set(axis.getPositiveDirection().getFloatVector());
        Vector3f vector3f3 = matrix.transformDirection(vector3f2);
        float f = Math.abs(vector3f3.x);
        float g = Math.abs(vector3f3.y);
        float h = Math.abs(vector3f3.z);
        float i = Math.max(Math.max(f, g), h);
        return 1.0f / i;
    }

    @Environment(value=EnvType.CLIENT)
    public static interface RotationValue {
        public Matrix4f getMatrix();
    }

    @Environment(value=EnvType.CLIENT)
    public record OfEuler(float x, float y, float z) implements RotationValue
    {
        @Override
        public Matrix4f getMatrix() {
            return new Matrix4f().rotationZYX(this.z * ((float)java.lang.Math.PI / 180), this.y * ((float)java.lang.Math.PI / 180), this.x * ((float)java.lang.Math.PI / 180));
        }
    }

    @Environment(value=EnvType.CLIENT)
    public record OfAxisAngle(Direction.Axis axis, float angle) implements RotationValue
    {
        @Override
        public Matrix4f getMatrix() {
            Matrix4f matrix4f = new Matrix4f();
            if (this.angle == 0.0f) {
                return matrix4f;
            }
            Vector3fc vector3fc = this.axis.getPositiveDirection().getFloatVector();
            matrix4f.rotation(this.angle * ((float)java.lang.Math.PI / 180), vector3fc);
            return matrix4f;
        }
    }
}

