/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/TargetPredicate;createNonAttackable()Lnet/minecraft/entity/ai/TargetPredicate;
 *   Lnet/minecraft/entity/passive/WolfEntity;squaredDistanceTo(Lnet/minecraft/entity/Entity;)D
 *   Lnet/minecraft/entity/ai/control/LookControl;lookAt(DDDFF)V
 *   Lnet/minecraft/util/Hand;values()[Lnet/minecraft/util/Hand;
 */
package net.minecraft.entity.ai.goal;

import java.util.EnumSet;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import org.jspecify.annotations.Nullable;

public class WolfBegGoal
extends Goal {
    private final WolfEntity wolf;
    private @Nullable PlayerEntity begFrom;
    private final ServerWorld world;
    private final float begDistance;
    private int timer;
    private final TargetPredicate validPlayerPredicate;

    public WolfBegGoal(WolfEntity wolf, float begDistance) {
        this.wolf = wolf;
        this.world = WolfBegGoal.getServerWorld(wolf);
        this.begDistance = begDistance;
        this.validPlayerPredicate = TargetPredicate.createNonAttackable().setBaseMaxDistance(begDistance);
        this.setControls(EnumSet.of(Goal.Control.LOOK));
    }

    @Override
    public boolean canStart() {
        this.begFrom = this.world.getClosestPlayer(this.validPlayerPredicate, this.wolf);
        if (this.begFrom == null) {
            return false;
        }
        return this.isAttractive(this.begFrom);
    }

    @Override
    public boolean shouldContinue() {
        if (!this.begFrom.isAlive()) {
            return false;
        }
        if (this.wolf.squaredDistanceTo(this.begFrom) > (double)(this.begDistance * this.begDistance)) {
            return false;
        }
        return this.timer > 0 && this.isAttractive(this.begFrom);
    }

    @Override
    public void start() {
        this.wolf.setBegging(true);
        this.timer = this.getTickCount(40 + this.wolf.getRandom().nextInt(40));
    }

    @Override
    public void stop() {
        this.wolf.setBegging(false);
        this.begFrom = null;
    }

    @Override
    public void tick() {
        this.wolf.getLookControl().lookAt(this.begFrom.getX(), this.begFrom.getEyeY(), this.begFrom.getZ(), 10.0f, this.wolf.getMaxLookPitchChange());
        --this.timer;
    }

    private boolean isAttractive(PlayerEntity player) {
        for (Hand lv : Hand.values()) {
            ItemStack lv2 = player.getStackInHand(lv);
            if (!lv2.isOf(Items.BONE) && !this.wolf.isBreedingItem(lv2)) continue;
            return true;
        }
        return false;
    }
}

