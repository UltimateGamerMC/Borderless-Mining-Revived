/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/FuzzyTargeting;find(Lnet/minecraft/entity/mob/PathAwareEntity;II)Lnet/minecraft/util/math/Vec3d;
 *   Lnet/minecraft/entity/ai/FuzzyTargeting;findTo(Lnet/minecraft/entity/mob/PathAwareEntity;IILnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/ai/goal/IronGolemWanderAroundGoal;findRandomInRange()Lnet/minecraft/util/math/Vec3d;
 *   Lnet/minecraft/entity/ai/goal/IronGolemWanderAroundGoal;findVillagerPos()Lnet/minecraft/util/math/Vec3d;
 *   Lnet/minecraft/entity/ai/goal/IronGolemWanderAroundGoal;findRandomBlockPos()Lnet/minecraft/util/math/Vec3d;
 *   Lnet/minecraft/entity/ai/goal/IronGolemWanderAroundGoal;findRandomChunkPos()Lnet/minecraft/util/math/ChunkSectionPos;
 *   Lnet/minecraft/entity/ai/goal/IronGolemWanderAroundGoal;findRandomPosInChunk(Lnet/minecraft/util/math/ChunkSectionPos;)Lnet/minecraft/util/math/BlockPos;
 */
package net.minecraft.entity.ai.goal;

import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.poi.PointOfInterest;
import net.minecraft.world.poi.PointOfInterestStorage;
import org.jspecify.annotations.Nullable;

public class IronGolemWanderAroundGoal
extends WanderAroundGoal {
    private static final int CHUNK_RANGE = 2;
    private static final int ENTITY_COLLISION_RANGE = 32;
    private static final int HORIZONTAL_RANGE = 10;
    private static final int VERTICAL_RANGE = 7;

    public IronGolemWanderAroundGoal(PathAwareEntity arg, double d) {
        super(arg, d, 240, false);
    }

    @Override
    protected @Nullable Vec3d getWanderTarget() {
        Vec3d lv;
        float f = this.mob.getEntityWorld().random.nextFloat();
        if (this.mob.getEntityWorld().random.nextFloat() < 0.3f) {
            return this.findRandomInRange();
        }
        if (f < 0.7f) {
            lv = this.findVillagerPos();
            if (lv == null) {
                lv = this.findRandomBlockPos();
            }
        } else {
            lv = this.findRandomBlockPos();
            if (lv == null) {
                lv = this.findVillagerPos();
            }
        }
        return lv == null ? this.findRandomInRange() : lv;
    }

    private @Nullable Vec3d findRandomInRange() {
        return FuzzyTargeting.find(this.mob, 10, 7);
    }

    private @Nullable Vec3d findVillagerPos() {
        ServerWorld lv = (ServerWorld)this.mob.getEntityWorld();
        List<VillagerEntity> list = lv.getEntitiesByType(EntityType.VILLAGER, this.mob.getBoundingBox().expand(32.0), this::canVillagerSummonGolem);
        if (list.isEmpty()) {
            return null;
        }
        VillagerEntity lv2 = list.get(this.mob.getEntityWorld().random.nextInt(list.size()));
        Vec3d lv3 = lv2.getEntityPos();
        return FuzzyTargeting.findTo(this.mob, 10, 7, lv3);
    }

    private @Nullable Vec3d findRandomBlockPos() {
        ChunkSectionPos lv = this.findRandomChunkPos();
        if (lv == null) {
            return null;
        }
        BlockPos lv2 = this.findRandomPosInChunk(lv);
        if (lv2 == null) {
            return null;
        }
        return FuzzyTargeting.findTo(this.mob, 10, 7, Vec3d.ofBottomCenter(lv2));
    }

    private @Nullable ChunkSectionPos findRandomChunkPos() {
        ServerWorld lv = (ServerWorld)this.mob.getEntityWorld();
        List list = ChunkSectionPos.stream(ChunkSectionPos.from(this.mob), 2).filter(sectionPos -> lv.getOccupiedPointOfInterestDistance((ChunkSectionPos)sectionPos) == 0).collect(Collectors.toList());
        if (list.isEmpty()) {
            return null;
        }
        return (ChunkSectionPos)list.get(lv.random.nextInt(list.size()));
    }

    private @Nullable BlockPos findRandomPosInChunk(ChunkSectionPos pos) {
        ServerWorld lv = (ServerWorld)this.mob.getEntityWorld();
        PointOfInterestStorage lv2 = lv.getPointOfInterestStorage();
        List list = lv2.getInCircle(arg -> true, pos.getCenterPos(), 8, PointOfInterestStorage.OccupationStatus.IS_OCCUPIED).map(PointOfInterest::getPos).collect(Collectors.toList());
        if (list.isEmpty()) {
            return null;
        }
        return (BlockPos)list.get(lv.random.nextInt(list.size()));
    }

    private boolean canVillagerSummonGolem(VillagerEntity villager) {
        return villager.canSummonGolem(this.mob.getEntityWorld().getTime());
    }
}

