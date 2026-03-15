/*
 * External method calls:
 *   Lnet/minecraft/world/gen/feature/EndPortalFeature;offsetOrigin(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/BlockPos;
 *   Lnet/minecraft/entity/boss/dragon/EnderDragonEntity;findPath(IILnet/minecraft/entity/ai/pathing/PathNode;)Lnet/minecraft/entity/ai/pathing/Path;
 *   Lnet/minecraft/entity/boss/dragon/phase/PhaseManager;create(Lnet/minecraft/entity/boss/dragon/phase/PhaseType;)Lnet/minecraft/entity/boss/dragon/phase/Phase;
 *   Lnet/minecraft/entity/ai/TargetPredicate;createAttackable()Lnet/minecraft/entity/ai/TargetPredicate;
 *   Lnet/minecraft/entity/ai/TargetPredicate;ignoreVisibility()Lnet/minecraft/entity/ai/TargetPredicate;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/boss/dragon/phase/HoldingPatternPhase;tickInRange(Lnet/minecraft/server/world/ServerWorld;)V
 *   Lnet/minecraft/entity/boss/dragon/phase/HoldingPatternPhase;strafePlayer(Lnet/minecraft/entity/player/PlayerEntity;)V
 */
package net.minecraft.entity.boss.dragon.phase;

import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.phase.AbstractPhase;
import net.minecraft.entity.boss.dragon.phase.PhaseType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.feature.EndPortalFeature;
import org.jspecify.annotations.Nullable;

public class HoldingPatternPhase
extends AbstractPhase {
    private static final TargetPredicate PLAYERS_IN_RANGE_PREDICATE = TargetPredicate.createAttackable().ignoreVisibility();
    private @Nullable Path path;
    private @Nullable Vec3d pathTarget;
    private boolean shouldFindNewPath;

    public HoldingPatternPhase(EnderDragonEntity arg) {
        super(arg);
    }

    public PhaseType<HoldingPatternPhase> getType() {
        return PhaseType.HOLDING_PATTERN;
    }

    @Override
    public void serverTick(ServerWorld world) {
        double d;
        double d2 = d = this.pathTarget == null ? 0.0 : this.pathTarget.squaredDistanceTo(this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
        if (d < 100.0 || d > 22500.0 || this.dragon.horizontalCollision || this.dragon.verticalCollision) {
            this.tickInRange(world);
        }
    }

    @Override
    public void beginPhase() {
        this.path = null;
        this.pathTarget = null;
    }

    @Override
    public @Nullable Vec3d getPathTarget() {
        return this.pathTarget;
    }

    private void tickInRange(ServerWorld world) {
        int i;
        if (this.path != null && this.path.isFinished()) {
            BlockPos lv = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EndPortalFeature.offsetOrigin(this.dragon.getFightOrigin()));
            int n = i = this.dragon.getFight() == null ? 0 : this.dragon.getFight().getAliveEndCrystals();
            if (this.dragon.getRandom().nextInt(i + 3) == 0) {
                this.dragon.getPhaseManager().setPhase(PhaseType.LANDING_APPROACH);
                return;
            }
            PlayerEntity lv2 = world.getClosestPlayer(PLAYERS_IN_RANGE_PREDICATE, this.dragon, (double)lv.getX(), (double)lv.getY(), lv.getZ());
            double d = lv2 != null ? lv.getSquaredDistance(lv2.getEntityPos()) / 512.0 : 64.0;
            if (lv2 != null && (this.dragon.getRandom().nextInt((int)(d + 2.0)) == 0 || this.dragon.getRandom().nextInt(i + 2) == 0)) {
                this.strafePlayer(lv2);
                return;
            }
        }
        if (this.path == null || this.path.isFinished()) {
            int j;
            i = j = this.dragon.getNearestPathNodeIndex();
            if (this.dragon.getRandom().nextInt(8) == 0) {
                this.shouldFindNewPath = !this.shouldFindNewPath;
                i += 6;
            }
            i = this.shouldFindNewPath ? ++i : --i;
            if (this.dragon.getFight() == null || this.dragon.getFight().getAliveEndCrystals() < 0) {
                i -= 12;
                i &= 7;
                i += 12;
            } else if ((i %= 12) < 0) {
                i += 12;
            }
            this.path = this.dragon.findPath(j, i, null);
            if (this.path != null) {
                this.path.next();
            }
        }
        this.followPath();
    }

    private void strafePlayer(PlayerEntity player) {
        this.dragon.getPhaseManager().setPhase(PhaseType.STRAFE_PLAYER);
        this.dragon.getPhaseManager().create(PhaseType.STRAFE_PLAYER).setTargetEntity(player);
    }

    private void followPath() {
        if (this.path != null && !this.path.isFinished()) {
            double f;
            BlockPos lv = this.path.getCurrentNodePos();
            this.path.next();
            double d = lv.getX();
            double e = lv.getZ();
            while ((f = (double)((float)lv.getY() + this.dragon.getRandom().nextFloat() * 20.0f)) < (double)lv.getY()) {
            }
            this.pathTarget = new Vec3d(d, f, e);
        }
    }

    @Override
    public void crystalDestroyed(EndCrystalEntity crystal, BlockPos pos, DamageSource source, @Nullable PlayerEntity player) {
        if (player != null && this.dragon.canTarget(player)) {
            this.strafePlayer(player);
        }
    }
}

