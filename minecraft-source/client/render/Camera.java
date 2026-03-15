/*
 * External method calls:
 *   Lnet/minecraft/world/attribute/EnvironmentAttributeInterpolator;update(Lnet/minecraft/world/World;Lnet/minecraft/util/math/Vec3d;)V
 *   Lnet/minecraft/world/World;raycast(Lnet/minecraft/world/RaycastContext;)Lnet/minecraft/util/hit/BlockHitResult;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/Camera;clipToSpace(F)F
 *   Lnet/minecraft/client/render/Camera;moveBy(FFF)V
 */
package net.minecraft.client.render;

import java.util.Arrays;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.vehicle.ExperimentalMinecartController;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.attribute.EnvironmentAttributeInterpolator;
import net.minecraft.world.waypoint.TrackedWaypoint;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;

@Environment(value=EnvType.CLIENT)
public class Camera
implements TrackedWaypoint.YawProvider {
    private static final float BASE_CAMERA_DISTANCE = 4.0f;
    private static final Vector3f HORIZONTAL = new Vector3f(0.0f, 0.0f, -1.0f);
    private static final Vector3f VERTICAL = new Vector3f(0.0f, 1.0f, 0.0f);
    private static final Vector3f DIAGONAL = new Vector3f(-1.0f, 0.0f, 0.0f);
    private boolean ready;
    private World area;
    private Entity focusedEntity;
    private Vec3d pos = Vec3d.ZERO;
    private final BlockPos.Mutable blockPos = new BlockPos.Mutable();
    private final Vector3f horizontalPlane = new Vector3f(HORIZONTAL);
    private final Vector3f verticalPlane = new Vector3f(VERTICAL);
    private final Vector3f diagonalPlane = new Vector3f(DIAGONAL);
    private float pitch;
    private float yaw;
    private final Quaternionf rotation = new Quaternionf();
    private boolean thirdPerson;
    private float cameraY;
    private float lastCameraY;
    private float lastTickProgress;
    private final EnvironmentAttributeInterpolator environmentAttributeInterpolator = new EnvironmentAttributeInterpolator();

    public void update(World area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickProgress) {
        ExperimentalMinecartController lv2;
        MinecartEntity lv;
        Object object;
        this.ready = true;
        this.area = area;
        this.focusedEntity = focusedEntity;
        this.thirdPerson = thirdPerson;
        this.lastTickProgress = tickProgress;
        if (focusedEntity.hasVehicle() && (object = focusedEntity.getVehicle()) instanceof MinecartEntity && (object = (lv = (MinecartEntity)object).getController()) instanceof ExperimentalMinecartController && (lv2 = (ExperimentalMinecartController)object).hasCurrentLerpSteps()) {
            Vec3d lv3 = lv.getPassengerRidingPos(focusedEntity).subtract(lv.getEntityPos()).subtract(focusedEntity.getVehicleAttachmentPos(lv)).add(new Vec3d(0.0, MathHelper.lerp(tickProgress, this.lastCameraY, this.cameraY), 0.0));
            this.setRotation(focusedEntity.getYaw(tickProgress), focusedEntity.getPitch(tickProgress));
            this.setPos(lv2.getLerpedPosition(tickProgress).add(lv3));
        } else {
            this.setRotation(focusedEntity.getYaw(tickProgress), focusedEntity.getPitch(tickProgress));
            this.setPos(MathHelper.lerp((double)tickProgress, focusedEntity.lastX, focusedEntity.getX()), MathHelper.lerp((double)tickProgress, focusedEntity.lastY, focusedEntity.getY()) + (double)MathHelper.lerp(tickProgress, this.lastCameraY, this.cameraY), MathHelper.lerp((double)tickProgress, focusedEntity.lastZ, focusedEntity.getZ()));
        }
        if (thirdPerson) {
            Entity entity;
            if (inverseView) {
                this.setRotation(this.yaw + 180.0f, -this.pitch);
            }
            float g = 4.0f;
            float h = 1.0f;
            if (focusedEntity instanceof LivingEntity) {
                LivingEntity lv4 = (LivingEntity)focusedEntity;
                h = lv4.getScale();
                g = (float)lv4.getAttributeValue(EntityAttributes.CAMERA_DISTANCE);
            }
            float i = h;
            float j = g;
            if (focusedEntity.hasVehicle() && (entity = focusedEntity.getVehicle()) instanceof LivingEntity) {
                LivingEntity lv5 = (LivingEntity)entity;
                i = lv5.getScale();
                j = (float)lv5.getAttributeValue(EntityAttributes.CAMERA_DISTANCE);
            }
            this.moveBy(-this.clipToSpace(Math.max(h * g, i * j)), 0.0f, 0.0f);
        } else if (focusedEntity instanceof LivingEntity && ((LivingEntity)focusedEntity).isSleeping()) {
            Direction lv6 = ((LivingEntity)focusedEntity).getSleepingDirection();
            this.setRotation(lv6 != null ? lv6.getPositiveHorizontalDegrees() - 180.0f : 0.0f, 0.0f);
            this.moveBy(0.0f, 0.3f, 0.0f);
        }
    }

    public void updateEyeHeight() {
        if (this.focusedEntity != null) {
            this.lastCameraY = this.cameraY;
            this.cameraY += (this.focusedEntity.getStandingEyeHeight() - this.cameraY) * 0.5f;
            this.environmentAttributeInterpolator.update(this.area, this.pos);
        }
    }

    private float clipToSpace(float distance) {
        float g = 0.1f;
        for (int i = 0; i < 8; ++i) {
            float l;
            Vec3d lv2;
            float h = (i & 1) * 2 - 1;
            float j = (i >> 1 & 1) * 2 - 1;
            float k = (i >> 2 & 1) * 2 - 1;
            Vec3d lv = this.pos.add(h * 0.1f, j * 0.1f, k * 0.1f);
            BlockHitResult lv3 = this.area.raycast(new RaycastContext(lv, lv2 = lv.add(new Vec3d(this.horizontalPlane).multiply(-distance)), RaycastContext.ShapeType.VISUAL, RaycastContext.FluidHandling.NONE, this.focusedEntity));
            if (((HitResult)lv3).getType() == HitResult.Type.MISS || !((l = (float)lv3.getPos().squaredDistanceTo(this.pos)) < MathHelper.square(distance))) continue;
            distance = MathHelper.sqrt(l);
        }
        return distance;
    }

    protected void moveBy(float surge, float heave, float sway) {
        Vector3f vector3f = new Vector3f(sway, heave, -surge).rotate(this.rotation);
        this.setPos(new Vec3d(this.pos.x + (double)vector3f.x, this.pos.y + (double)vector3f.y, this.pos.z + (double)vector3f.z));
    }

    protected void setRotation(float yaw, float pitch) {
        this.pitch = pitch;
        this.yaw = yaw;
        this.rotation.rotationYXZ((float)Math.PI - yaw * ((float)Math.PI / 180), -pitch * ((float)Math.PI / 180), 0.0f);
        HORIZONTAL.rotate(this.rotation, this.horizontalPlane);
        VERTICAL.rotate(this.rotation, this.verticalPlane);
        DIAGONAL.rotate(this.rotation, this.diagonalPlane);
    }

    protected void setPos(double x, double y, double z) {
        this.setPos(new Vec3d(x, y, z));
    }

    protected void setPos(Vec3d pos) {
        this.pos = pos;
        this.blockPos.set(pos.x, pos.y, pos.z);
    }

    @Override
    public Vec3d getCameraPos() {
        return this.pos;
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public float getPitch() {
        return this.pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    @Override
    public float getCameraYaw() {
        return MathHelper.wrapDegrees(this.getYaw());
    }

    public Quaternionf getRotation() {
        return this.rotation;
    }

    public Entity getFocusedEntity() {
        return this.focusedEntity;
    }

    public boolean isReady() {
        return this.ready;
    }

    public boolean isThirdPerson() {
        return this.thirdPerson;
    }

    public EnvironmentAttributeInterpolator getEnvironmentAttributeInterpolator() {
        return this.environmentAttributeInterpolator;
    }

    public Projection getProjection() {
        MinecraftClient lv = MinecraftClient.getInstance();
        double d = (double)lv.getWindow().getFramebufferWidth() / (double)lv.getWindow().getFramebufferHeight();
        double e = Math.tan((double)((float)lv.options.getFov().getValue().intValue() * ((float)Math.PI / 180)) / 2.0) * (double)0.05f;
        double f = e * d;
        Vec3d lv2 = new Vec3d(this.horizontalPlane).multiply(0.05f);
        Vec3d lv3 = new Vec3d(this.diagonalPlane).multiply(f);
        Vec3d lv4 = new Vec3d(this.verticalPlane).multiply(e);
        return new Projection(lv2, lv3, lv4);
    }

    public CameraSubmersionType getSubmersionType() {
        if (!this.ready) {
            return CameraSubmersionType.NONE;
        }
        FluidState lv = this.area.getFluidState(this.blockPos);
        if (lv.isIn(FluidTags.WATER) && this.pos.y < (double)((float)this.blockPos.getY() + lv.getHeight(this.area, this.blockPos))) {
            return CameraSubmersionType.WATER;
        }
        Projection lv2 = this.getProjection();
        List<Vec3d> list = Arrays.asList(lv2.center, lv2.getBottomRight(), lv2.getTopRight(), lv2.getBottomLeft(), lv2.getTopLeft());
        for (Vec3d lv3 : list) {
            Vec3d lv4 = this.pos.add(lv3);
            BlockPos lv5 = BlockPos.ofFloored(lv4);
            FluidState lv6 = this.area.getFluidState(lv5);
            if (lv6.isIn(FluidTags.LAVA)) {
                if (!(lv4.y <= (double)(lv6.getHeight(this.area, lv5) + (float)lv5.getY()))) continue;
                return CameraSubmersionType.LAVA;
            }
            BlockState lv7 = this.area.getBlockState(lv5);
            if (!lv7.isOf(Blocks.POWDER_SNOW)) continue;
            return CameraSubmersionType.POWDER_SNOW;
        }
        return CameraSubmersionType.NONE;
    }

    public Vector3fc getHorizontalPlane() {
        return this.horizontalPlane;
    }

    public Vector3fc getVerticalPlane() {
        return this.verticalPlane;
    }

    public Vector3fc getDiagonalPlane() {
        return this.diagonalPlane;
    }

    public void reset() {
        this.area = null;
        this.focusedEntity = null;
        this.environmentAttributeInterpolator.clear();
        this.ready = false;
    }

    public float getLastTickProgress() {
        return this.lastTickProgress;
    }

    @Environment(value=EnvType.CLIENT)
    public static class Projection {
        final Vec3d center;
        private final Vec3d x;
        private final Vec3d y;

        Projection(Vec3d center, Vec3d x, Vec3d y) {
            this.center = center;
            this.x = x;
            this.y = y;
        }

        public Vec3d getBottomRight() {
            return this.center.add(this.y).add(this.x);
        }

        public Vec3d getTopRight() {
            return this.center.add(this.y).subtract(this.x);
        }

        public Vec3d getBottomLeft() {
            return this.center.subtract(this.y).add(this.x);
        }

        public Vec3d getTopLeft() {
            return this.center.subtract(this.y).subtract(this.x);
        }

        public Vec3d getPosition(float factorX, float factorY) {
            return this.center.add(this.y.multiply(factorY)).subtract(this.x.multiply(factorX));
        }
    }
}

