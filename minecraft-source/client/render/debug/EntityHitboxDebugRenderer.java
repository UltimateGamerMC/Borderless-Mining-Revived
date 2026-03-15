/*
 * External method calls:
 *   Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;left(I)Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;text(Ljava/lang/String;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/client/render/DrawStyle;stroked(I)Lnet/minecraft/client/render/DrawStyle;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;box(Lnet/minecraft/util/math/Box;Lnet/minecraft/client/render/DrawStyle;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;point(Lnet/minecraft/util/math/Vec3d;IF)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;arrow(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;I)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/debug/EntityHitboxDebugRenderer;drawHitbox(Lnet/minecraft/entity/Entity;FZ)V
 */
package net.minecraft.client.render.debug;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.DrawStyle;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.debug.DebugDataStore;
import net.minecraft.world.debug.gizmo.GizmoDrawing;
import net.minecraft.world.debug.gizmo.TextGizmo;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class EntityHitboxDebugRenderer
implements DebugRenderer.Renderer {
    final MinecraftClient client;

    public EntityHitboxDebugRenderer(MinecraftClient client) {
        this.client = client;
    }

    @Override
    public void render(double cameraX, double cameraY, double cameraZ, DebugDataStore store, Frustum frustum, float tickProgress) {
        if (this.client.world == null) {
            return;
        }
        for (Entity lv : this.client.world.getEntities()) {
            if (lv.isInvisible() || !frustum.isVisible(lv.getBoundingBox()) || lv == this.client.getCameraEntity() && this.client.options.getPerspective() == Perspective.FIRST_PERSON) continue;
            this.drawHitbox(lv, tickProgress, false);
            if (!SharedConstants.SHOW_LOCAL_SERVER_ENTITY_HIT_BOXES) continue;
            Entity lv2 = this.getLocalServerEntity(lv);
            if (lv2 != null) {
                this.drawHitbox(lv, tickProgress, true);
                continue;
            }
            GizmoDrawing.text("Missing Server Entity", lv.getLerpedPos(tickProgress).add(0.0, lv.getBoundingBox().getLengthY() + 1.5, 0.0), TextGizmo.Style.left(-65536));
        }
    }

    private @Nullable Entity getLocalServerEntity(Entity entity) {
        ServerWorld lv2;
        IntegratedServer lv = this.client.getServer();
        if (lv != null && (lv2 = lv.getWorld(entity.getEntityWorld().getRegistryKey())) != null) {
            return lv2.getEntityById(entity.getId());
        }
        return null;
    }

    private void drawHitbox(Entity entity, float tickProgress, boolean inLocalServer) {
        Vec3d lv5;
        float h;
        Vec3d lv = entity.getEntityPos();
        Vec3d lv2 = entity.getLerpedPos(tickProgress);
        Vec3d lv3 = lv2.subtract(lv);
        int i = inLocalServer ? -16711936 : -1;
        GizmoDrawing.box(entity.getBoundingBox().offset(lv3), DrawStyle.stroked(i));
        GizmoDrawing.point(lv2, i, 2.0f);
        Entity lv4 = entity.getVehicle();
        if (lv4 != null) {
            float g = Math.min(lv4.getWidth(), entity.getWidth()) / 2.0f;
            h = 0.0625f;
            lv5 = lv4.getPassengerRidingPos(entity).add(lv3);
            GizmoDrawing.box(new Box(lv5.x - (double)g, lv5.y, lv5.z - (double)g, lv5.x + (double)g, lv5.y + 0.0625, lv5.z + (double)g), DrawStyle.stroked(-256));
        }
        if (entity instanceof LivingEntity) {
            Box lv6 = entity.getBoundingBox().offset(lv3);
            h = 0.01f;
            GizmoDrawing.box(new Box(lv6.minX, lv6.minY + (double)entity.getStandingEyeHeight() - (double)0.01f, lv6.minZ, lv6.maxX, lv6.minY + (double)entity.getStandingEyeHeight() + (double)0.01f, lv6.maxZ), DrawStyle.stroked(-65536));
        }
        if (entity instanceof EnderDragonEntity) {
            EnderDragonEntity lv7 = (EnderDragonEntity)entity;
            for (EnderDragonPart lv8 : lv7.getBodyParts()) {
                Vec3d lv9 = lv8.getEntityPos();
                Vec3d lv10 = lv8.getLerpedPos(tickProgress);
                Vec3d lv11 = lv10.subtract(lv9);
                GizmoDrawing.box(lv8.getBoundingBox().offset(lv11), DrawStyle.stroked(ColorHelper.fromFloats(1.0f, 0.25f, 1.0f, 0.0f)));
            }
        }
        Vec3d lv12 = lv2.add(0.0, entity.getStandingEyeHeight(), 0.0);
        Vec3d lv13 = entity.getRotationVec(tickProgress);
        GizmoDrawing.arrow(lv12, lv12.add(lv13.multiply(2.0)), -16776961);
        if (inLocalServer) {
            lv5 = entity.getVelocity();
            GizmoDrawing.arrow(lv2, lv2.add(lv5), -256);
        }
    }
}

