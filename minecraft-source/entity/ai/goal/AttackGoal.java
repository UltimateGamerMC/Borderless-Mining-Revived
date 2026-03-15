/*
 * External method calls:
 *   Lnet/minecraft/entity/mob/MobEntity;squaredDistanceTo(Lnet/minecraft/entity/Entity;)D
 *   Lnet/minecraft/entity/ai/control/LookControl;lookAt(Lnet/minecraft/entity/Entity;FF)V
 *   Lnet/minecraft/entity/mob/MobEntity;squaredDistanceTo(DDD)D
 *   Lnet/minecraft/entity/ai/pathing/EntityNavigation;startMovingTo(Lnet/minecraft/entity/Entity;D)Z
 *   Lnet/minecraft/entity/mob/MobEntity;tryAttack(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/Entity;)Z
 */
package net.minecraft.entity.ai.goal;

import java.util.EnumSet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;

public class AttackGoal
extends Goal {
    private final MobEntity mob;
    private LivingEntity target;
    private int cooldown;

    public AttackGoal(MobEntity mob) {
        this.mob = mob;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    @Override
    public boolean canStart() {
        LivingEntity lv = this.mob.getTarget();
        if (lv == null) {
            return false;
        }
        this.target = lv;
        return true;
    }

    @Override
    public boolean shouldContinue() {
        if (!this.target.isAlive()) {
            return false;
        }
        if (this.mob.squaredDistanceTo(this.target) > 225.0) {
            return false;
        }
        return !this.mob.getNavigation().isIdle() || this.canStart();
    }

    @Override
    public void stop() {
        this.target = null;
        this.mob.getNavigation().stop();
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        this.mob.getLookControl().lookAt(this.target, 30.0f, 30.0f);
        double d = this.mob.getWidth() * 2.0f * (this.mob.getWidth() * 2.0f);
        double e = this.mob.squaredDistanceTo(this.target.getX(), this.target.getY(), this.target.getZ());
        double f = 0.8;
        if (e > d && e < 16.0) {
            f = 1.33;
        } else if (e < 225.0) {
            f = 0.6;
        }
        this.mob.getNavigation().startMovingTo(this.target, f);
        this.cooldown = Math.max(this.cooldown - 1, 0);
        if (e > d) {
            return;
        }
        if (this.cooldown > 0) {
            return;
        }
        this.cooldown = 20;
        this.mob.tryAttack(AttackGoal.getServerWorld(this.mob), this.target);
    }
}

