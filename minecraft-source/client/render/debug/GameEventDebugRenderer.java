/*
 * External method calls:
 *   Lnet/minecraft/world/debug/DebugDataStore;forEachBlockData(Lnet/minecraft/world/debug/DebugSubscriptionType;Ljava/util/function/BiConsumer;)V
 *   Lnet/minecraft/world/debug/DebugDataStore;forEachEntityData(Lnet/minecraft/world/debug/DebugSubscriptionType;Ljava/util/function/BiConsumer;)V
 *   Lnet/minecraft/world/debug/DebugDataStore;forEachEvent(Lnet/minecraft/world/debug/DebugSubscriptionType;Lnet/minecraft/world/debug/DebugDataStore$EventConsumer;)V
 *   Lnet/minecraft/world/debug/data/GameEventDebugData;pos()Lnet/minecraft/util/math/Vec3d;
 *   Lnet/minecraft/client/render/DrawStyle;filled(I)Lnet/minecraft/client/render/DrawStyle;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;box(Lnet/minecraft/util/math/Box;Lnet/minecraft/client/render/DrawStyle;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/world/debug/data/GameEventDebugData;event()Lnet/minecraft/registry/entry/RegistryEntry;
 *   Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;left(I)Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;
 *   Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;scaled(F)Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;text(Ljava/lang/String;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;left()Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;
 *   Lnet/minecraft/client/render/debug/GameEventDebugRenderer$EventConsumer;accept(Lnet/minecraft/util/math/Vec3d;I)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/debug/GameEventDebugRenderer;forEachEventData(Lnet/minecraft/world/debug/DebugDataStore;Lnet/minecraft/client/render/debug/GameEventDebugRenderer$EventConsumer;)V
 */
package net.minecraft.client.render.debug;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.DrawStyle;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.debug.DebugDataStore;
import net.minecraft.world.debug.DebugSubscriptionTypes;
import net.minecraft.world.debug.gizmo.GizmoDrawing;
import net.minecraft.world.debug.gizmo.TextGizmo;

@Environment(value=EnvType.CLIENT)
public class GameEventDebugRenderer
implements DebugRenderer.Renderer {
    private static final float field_32900 = 1.0f;

    private void forEachEventData(DebugDataStore dataStore, EventConsumer consumer) {
        dataStore.forEachBlockData(DebugSubscriptionTypes.GAME_EVENT_LISTENERS, (pos, data) -> consumer.accept(pos.toCenterPos(), data.listenerRadius()));
        dataStore.forEachEntityData(DebugSubscriptionTypes.GAME_EVENT_LISTENERS, (entity, data) -> consumer.accept(entity.getEntityPos(), data.listenerRadius()));
    }

    @Override
    public void render(double cameraX, double cameraY, double cameraZ, DebugDataStore store, Frustum frustum, float tickProgress) {
        this.forEachEventData(store, (pos, radius) -> {
            double d = (double)radius * 2.0;
            GizmoDrawing.box(Box.of(pos, d, d, d), DrawStyle.filled(ColorHelper.fromFloats(0.35f, 1.0f, 1.0f, 0.0f)));
        });
        this.forEachEventData(store, (pos, radius) -> GizmoDrawing.box(Box.of(pos, 0.5, 1.0, 0.5).offset(0.0, 0.5, 0.0), DrawStyle.filled(ColorHelper.fromFloats(0.35f, 1.0f, 1.0f, 0.0f))));
        this.forEachEventData(store, (pos, radius) -> {
            GizmoDrawing.text("Listener Origin", pos.add(0.0, 1.8, 0.0), TextGizmo.Style.left().scaled(0.4f));
            GizmoDrawing.text(BlockPos.ofFloored(pos).toString(), pos.add(0.0, 1.5, 0.0), TextGizmo.Style.left(-6959665).scaled(0.4f));
        });
        store.forEachEvent(DebugSubscriptionTypes.GAME_EVENTS, (data, remainingTime, expiry) -> {
            Vec3d lv = data.pos();
            double d = 0.4;
            Box lv2 = Box.of(lv.add(0.0, 0.5, 0.0), 0.4, 0.9, 0.4);
            GizmoDrawing.box(lv2, DrawStyle.filled(ColorHelper.fromFloats(0.2f, 1.0f, 1.0f, 1.0f)));
            GizmoDrawing.text(data.event().getIdAsString(), lv.add(0.0, 0.85, 0.0), TextGizmo.Style.left(-7564911).scaled(0.12f));
        });
    }

    @FunctionalInterface
    @Environment(value=EnvType.CLIENT)
    static interface EventConsumer {
        public void accept(Vec3d var1, int var2);
    }
}

