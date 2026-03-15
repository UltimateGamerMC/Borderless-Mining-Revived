/*
 * External method calls:
 *   Lnet/minecraft/world/debug/DebugDataStore;forEachBlockData(Lnet/minecraft/world/debug/DebugSubscriptionType;Ljava/util/function/BiConsumer;)V
 *   Lnet/minecraft/client/render/DrawStyle;filled(I)Lnet/minecraft/client/render/DrawStyle;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;box(Lnet/minecraft/util/math/BlockPos;FLnet/minecraft/client/render/DrawStyle;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;blockLabel(Ljava/lang/String;Lnet/minecraft/util/math/BlockPos;IIF)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/world/debug/data/PoiDebugData;poiType()Lnet/minecraft/registry/entry/RegistryEntry;
 *   Lnet/minecraft/world/debug/data/PoiDebugData;pos()Lnet/minecraft/util/math/BlockPos;
 *   Lnet/minecraft/world/debug/DebugDataStore;forEachEntityData(Lnet/minecraft/world/debug/DebugSubscriptionType;Ljava/util/function/BiConsumer;)V
 *   Lnet/minecraft/world/debug/data/BrainDebugData;potentialPoiContains(Lnet/minecraft/util/math/BlockPos;)Z
 *   Lnet/minecraft/world/debug/data/BrainDebugData;poiContains(Lnet/minecraft/util/math/BlockPos;)Z
 *   Lnet/minecraft/util/NameGenerator;name(Ljava/util/UUID;)Ljava/lang/String;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/debug/PoiDebugRenderer;drawTextOverPoi(Ljava/lang/String;Lnet/minecraft/world/debug/data/PoiDebugData;II)V
 *   Lnet/minecraft/client/render/debug/PoiDebugRenderer;drawGhostPoi(Lnet/minecraft/util/math/BlockPos;Ljava/util/List;)V
 *   Lnet/minecraft/client/render/debug/PoiDebugRenderer;accentuatePoi(Lnet/minecraft/util/math/BlockPos;)V
 *   Lnet/minecraft/client/render/debug/PoiDebugRenderer;drawPoiInfo(Lnet/minecraft/world/debug/data/PoiDebugData;Lnet/minecraft/world/debug/DebugDataStore;)V
 */
package net.minecraft.client.render.debug;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.render.DrawStyle;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.debug.BrainDebugRenderer;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.util.NameGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.debug.DebugDataStore;
import net.minecraft.world.debug.DebugSubscriptionTypes;
import net.minecraft.world.debug.data.PoiDebugData;
import net.minecraft.world.debug.gizmo.GizmoDrawing;

@Environment(value=EnvType.CLIENT)
public class PoiDebugRenderer
implements DebugRenderer.Renderer {
    private static final int field_62976 = 30;
    private static final float field_62977 = 0.32f;
    private static final int ORANGE_COLOR = -23296;
    private final BrainDebugRenderer brainDebugRenderer;

    public PoiDebugRenderer(BrainDebugRenderer brainDebugRenderer) {
        this.brainDebugRenderer = brainDebugRenderer;
    }

    @Override
    public void render(double cameraX, double cameraY, double cameraZ, DebugDataStore store, Frustum frustum, float tickProgress) {
        BlockPos lv = BlockPos.ofFloored(cameraX, cameraY, cameraZ);
        store.forEachBlockData(DebugSubscriptionTypes.POIS, (pos, data) -> {
            if (lv.isWithinDistance((Vec3i)pos, 30.0)) {
                PoiDebugRenderer.accentuatePoi(pos);
                this.drawPoiInfo((PoiDebugData)data, store);
            }
        });
        this.brainDebugRenderer.getGhostPointsOfInterest(store).forEach((pos, ghostPois) -> {
            if (store.getBlockData(DebugSubscriptionTypes.POIS, (BlockPos)pos) != null) {
                return;
            }
            if (lv.isWithinDistance((Vec3i)pos, 30.0)) {
                this.drawGhostPoi((BlockPos)pos, (List<String>)ghostPois);
            }
        });
    }

    private static void accentuatePoi(BlockPos pos) {
        float f = 0.05f;
        GizmoDrawing.box(pos, 0.05f, DrawStyle.filled(ColorHelper.fromFloats(0.3f, 0.2f, 0.2f, 1.0f)));
    }

    private void drawGhostPoi(BlockPos pos, List<String> ghostPois) {
        float f = 0.05f;
        GizmoDrawing.box(pos, 0.05f, DrawStyle.filled(ColorHelper.fromFloats(0.3f, 0.2f, 0.2f, 1.0f)));
        GizmoDrawing.blockLabel(ghostPois.toString(), pos, 0, -256, 0.32f);
        GizmoDrawing.blockLabel("Ghost POI", pos, 1, -65536, 0.32f);
    }

    private void drawPoiInfo(PoiDebugData data, DebugDataStore store) {
        int i = 0;
        if (SharedConstants.BRAIN) {
            List<String> list = this.getTicketHolders(data, false, store);
            if (list.size() < 4) {
                PoiDebugRenderer.drawTextOverPoi("Owners: " + String.valueOf(list), data, i, -256);
            } else {
                PoiDebugRenderer.drawTextOverPoi(list.size() + " ticket holders", data, i, -256);
            }
            ++i;
            List<String> list2 = this.getTicketHolders(data, true, store);
            if (list2.size() < 4) {
                PoiDebugRenderer.drawTextOverPoi("Candidates: " + String.valueOf(list2), data, i, -23296);
            } else {
                PoiDebugRenderer.drawTextOverPoi(list2.size() + " potential owners", data, i, -23296);
            }
            ++i;
        }
        PoiDebugRenderer.drawTextOverPoi("Free tickets: " + data.freeTicketCount(), data, i, -256);
        PoiDebugRenderer.drawTextOverPoi(data.poiType().getIdAsString(), data, ++i, -1);
    }

    private static void drawTextOverPoi(String string, PoiDebugData data, int yOffset, int color) {
        GizmoDrawing.blockLabel(string, data.pos(), yOffset, color, 0.32f);
    }

    private List<String> getTicketHolders(PoiDebugData poiData, boolean potential, DebugDataStore store) {
        ArrayList<String> list = new ArrayList<String>();
        store.forEachEntityData(DebugSubscriptionTypes.BRAINS, (entity, grainData) -> {
            boolean bl2;
            boolean bl3 = bl2 = potential ? grainData.potentialPoiContains(poiData.pos()) : grainData.poiContains(poiData.pos());
            if (bl2) {
                list.add(NameGenerator.name(entity.getUuid()));
            }
        });
        return list;
    }
}

