/*
 * External method calls:
 *   Lnet/minecraft/client/render/debug/DebugRenderer$Renderer;render(DDDLnet/minecraft/world/debug/DebugDataStore;Lnet/minecraft/client/render/Frustum;F)V
 *   Lnet/minecraft/entity/projectile/ProjectileUtil;raycast(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;D)Lnet/minecraft/util/hit/EntityHitResult;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/debug/DebugRenderer;hueToRgb(F)Lnet/minecraft/util/math/Vec3d;
 */
package net.minecraft.client.render.debug;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.debug.DebugHudEntries;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.debug.BeeDebugRenderer;
import net.minecraft.client.render.debug.BlockOutlineDebugRenderer;
import net.minecraft.client.render.debug.BrainDebugRenderer;
import net.minecraft.client.render.debug.BreezeDebugRenderer;
import net.minecraft.client.render.debug.ChunkBorderDebugRenderer;
import net.minecraft.client.render.debug.ChunkDebugRenderer;
import net.minecraft.client.render.debug.ChunkLoadingDebugRenderer;
import net.minecraft.client.render.debug.CollisionDebugRenderer;
import net.minecraft.client.render.debug.EntityBlockIntersectionsDebugRenderer;
import net.minecraft.client.render.debug.EntityHitboxDebugRenderer;
import net.minecraft.client.render.debug.GameEventDebugRenderer;
import net.minecraft.client.render.debug.GoalSelectorDebugRenderer;
import net.minecraft.client.render.debug.HeightmapDebugRenderer;
import net.minecraft.client.render.debug.LightDebugRenderer;
import net.minecraft.client.render.debug.NeighborUpdateDebugRenderer;
import net.minecraft.client.render.debug.OctreeDebugRenderer;
import net.minecraft.client.render.debug.PathfindingDebugRenderer;
import net.minecraft.client.render.debug.PoiDebugRenderer;
import net.minecraft.client.render.debug.RaidCenterDebugRenderer;
import net.minecraft.client.render.debug.RedstoneUpdateOrderDebugRenderer;
import net.minecraft.client.render.debug.SkyLightDebugRenderer;
import net.minecraft.client.render.debug.StructureDebugRenderer;
import net.minecraft.client.render.debug.SupportingBlockDebugRenderer;
import net.minecraft.client.render.debug.VillageSectionsDebugRenderer;
import net.minecraft.client.render.debug.WaterDebugRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.minecraft.world.debug.DebugDataStore;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class DebugRenderer {
    private final List<Renderer> renderers = new ArrayList<Renderer>();
    private long currentVersion;

    public DebugRenderer() {
        this.initRenderers();
    }

    public void initRenderers() {
        MinecraftClient lv = MinecraftClient.getInstance();
        this.renderers.clear();
        if (lv.debugHudEntryList.isEntryVisible(DebugHudEntries.CHUNK_BORDERS)) {
            this.renderers.add(new ChunkBorderDebugRenderer(lv));
        }
        if (lv.debugHudEntryList.isEntryVisible(DebugHudEntries.CHUNK_SECTION_OCTREE)) {
            this.renderers.add(new OctreeDebugRenderer(lv));
        }
        if (SharedConstants.PATHFINDING) {
            this.renderers.add(new PathfindingDebugRenderer());
        }
        if (lv.debugHudEntryList.isEntryVisible(DebugHudEntries.VISUALIZE_WATER_LEVELS)) {
            this.renderers.add(new WaterDebugRenderer(lv));
        }
        if (lv.debugHudEntryList.isEntryVisible(DebugHudEntries.VISUALIZE_HEIGHTMAP)) {
            this.renderers.add(new HeightmapDebugRenderer(lv));
        }
        if (lv.debugHudEntryList.isEntryVisible(DebugHudEntries.VISUALIZE_COLLISION_BOXES)) {
            this.renderers.add(new CollisionDebugRenderer(lv));
        }
        if (lv.debugHudEntryList.isEntryVisible(DebugHudEntries.VISUALIZE_ENTITY_SUPPORTING_BLOCKS)) {
            this.renderers.add(new SupportingBlockDebugRenderer(lv));
        }
        if (SharedConstants.NEIGHBORSUPDATE) {
            this.renderers.add(new NeighborUpdateDebugRenderer());
        }
        if (SharedConstants.EXPERIMENTAL_REDSTONEWIRE_UPDATE_ORDER) {
            this.renderers.add(new RedstoneUpdateOrderDebugRenderer());
        }
        if (SharedConstants.STRUCTURES) {
            this.renderers.add(new StructureDebugRenderer());
        }
        if (lv.debugHudEntryList.isEntryVisible(DebugHudEntries.VISUALIZE_BLOCK_LIGHT_LEVELS) || lv.debugHudEntryList.isEntryVisible(DebugHudEntries.VISUALIZE_SKY_LIGHT_LEVELS)) {
            this.renderers.add(new SkyLightDebugRenderer(lv, lv.debugHudEntryList.isEntryVisible(DebugHudEntries.VISUALIZE_BLOCK_LIGHT_LEVELS), lv.debugHudEntryList.isEntryVisible(DebugHudEntries.VISUALIZE_SKY_LIGHT_LEVELS)));
        }
        if (lv.debugHudEntryList.isEntryVisible(DebugHudEntries.VISUALIZE_SOLID_FACES)) {
            this.renderers.add(new BlockOutlineDebugRenderer(lv));
        }
        if (SharedConstants.VILLAGE_SECTIONS) {
            this.renderers.add(new VillageSectionsDebugRenderer());
        }
        if (SharedConstants.BRAIN) {
            this.renderers.add(new BrainDebugRenderer(lv));
        }
        if (SharedConstants.POI) {
            this.renderers.add(new PoiDebugRenderer(new BrainDebugRenderer(lv)));
        }
        if (SharedConstants.BEES) {
            this.renderers.add(new BeeDebugRenderer(lv));
        }
        if (SharedConstants.RAIDS) {
            this.renderers.add(new RaidCenterDebugRenderer(lv));
        }
        if (SharedConstants.GOAL_SELECTOR) {
            this.renderers.add(new GoalSelectorDebugRenderer(lv));
        }
        if (lv.debugHudEntryList.isEntryVisible(DebugHudEntries.VISUALIZE_CHUNKS_ON_SERVER)) {
            this.renderers.add(new ChunkLoadingDebugRenderer(lv));
        }
        if (SharedConstants.GAME_EVENT_LISTENERS) {
            this.renderers.add(new GameEventDebugRenderer());
        }
        if (lv.debugHudEntryList.isEntryVisible(DebugHudEntries.VISUALIZE_SKY_LIGHT_SECTIONS)) {
            this.renderers.add(new LightDebugRenderer(lv, LightType.SKY));
        }
        if (SharedConstants.BREEZE_MOB) {
            this.renderers.add(new BreezeDebugRenderer(lv));
        }
        if (SharedConstants.ENTITY_BLOCK_INTERSECTION) {
            this.renderers.add(new EntityBlockIntersectionsDebugRenderer());
        }
        if (lv.debugHudEntryList.isEntryVisible(DebugHudEntries.ENTITY_HITBOXES)) {
            this.renderers.add(new EntityHitboxDebugRenderer(lv));
        }
        this.renderers.add(new ChunkDebugRenderer(lv));
    }

    public void render(Frustum frustum, double cameraX, double cameraY, double cameraZ, float tickProgress) {
        MinecraftClient lv = MinecraftClient.getInstance();
        DebugDataStore lv2 = lv.getNetworkHandler().getDebugDataStore();
        if (lv.debugHudEntryList.getVersion() != this.currentVersion) {
            this.currentVersion = lv.debugHudEntryList.getVersion();
            this.initRenderers();
        }
        for (Renderer lv3 : this.renderers) {
            lv3.render(cameraX, cameraY, cameraZ, lv2, frustum, tickProgress);
        }
    }

    public static Optional<Entity> getTargetedEntity(@Nullable Entity entity, int maxDistance) {
        int j;
        Box lv4;
        Vec3d lv2;
        Vec3d lv3;
        if (entity == null) {
            return Optional.empty();
        }
        Vec3d lv = entity.getEyePos();
        EntityHitResult lv5 = ProjectileUtil.raycast(entity, lv, lv3 = lv.add(lv2 = entity.getRotationVec(1.0f).multiply(maxDistance)), lv4 = entity.getBoundingBox().stretch(lv2).expand(1.0), EntityPredicates.CAN_HIT, j = maxDistance * maxDistance);
        if (lv5 == null) {
            return Optional.empty();
        }
        if (lv.squaredDistanceTo(lv5.getPos()) > (double)j) {
            return Optional.empty();
        }
        return Optional.of(lv5.getEntity());
    }

    private static Vec3d hueToRgb(float hue) {
        float g = 5.99999f;
        int i = (int)(MathHelper.clamp(hue, 0.0f, 1.0f) * 5.99999f);
        float h = hue * 5.99999f - (float)i;
        return switch (i) {
            case 0 -> new Vec3d(1.0, h, 0.0);
            case 1 -> new Vec3d(1.0f - h, 1.0, 0.0);
            case 2 -> new Vec3d(0.0, 1.0, h);
            case 3 -> new Vec3d(0.0, 1.0 - (double)h, 1.0);
            case 4 -> new Vec3d(h, 0.0, 1.0);
            case 5 -> new Vec3d(1.0, 0.0, 1.0 - (double)h);
            default -> throw new IllegalStateException("Unexpected value: " + i);
        };
    }

    private static Vec3d shiftHue(float r, float g, float b, float dHue) {
        Vec3d lv = DebugRenderer.hueToRgb(dHue).multiply(r);
        Vec3d lv2 = DebugRenderer.hueToRgb((dHue + 0.33333334f) % 1.0f).multiply(g);
        Vec3d lv3 = DebugRenderer.hueToRgb((dHue + 0.6666667f) % 1.0f).multiply(b);
        Vec3d lv4 = lv.add(lv2).add(lv3);
        double d = Math.max(Math.max(1.0, lv4.x), Math.max(lv4.y, lv4.z));
        return new Vec3d(lv4.x / d, lv4.y / d, lv4.z / d);
    }

    @Environment(value=EnvType.CLIENT)
    public static interface Renderer {
        public void render(double var1, double var3, double var5, DebugDataStore var7, Frustum var8, float var9);
    }
}

