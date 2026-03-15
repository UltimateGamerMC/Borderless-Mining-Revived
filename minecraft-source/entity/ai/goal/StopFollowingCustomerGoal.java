/*
 * External method calls:
 *   Lnet/minecraft/entity/passive/MerchantEntity;squaredDistanceTo(Lnet/minecraft/entity/Entity;)D
 */
package net.minecraft.entity.ai.goal;

import java.util.EnumSet;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;

public class StopFollowingCustomerGoal
extends Goal {
    private final MerchantEntity merchant;

    public StopFollowingCustomerGoal(MerchantEntity merchant) {
        this.merchant = merchant;
        this.setControls(EnumSet.of(Goal.Control.JUMP, Goal.Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (!this.merchant.isAlive()) {
            return false;
        }
        if (this.merchant.isTouchingWater()) {
            return false;
        }
        if (!this.merchant.isOnGround()) {
            return false;
        }
        if (this.merchant.knockedBack) {
            return false;
        }
        PlayerEntity lv = this.merchant.getCustomer();
        if (lv == null) {
            return false;
        }
        return !(this.merchant.squaredDistanceTo(lv) > 16.0);
    }

    @Override
    public void start() {
        this.merchant.getNavigation().stop();
    }

    @Override
    public void stop() {
        this.merchant.setCustomer(null);
    }
}

