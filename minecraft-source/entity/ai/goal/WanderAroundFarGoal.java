/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/FuzzyTargeting;find(Lnet/minecraft/entity/mob/PathAwareEntity;II)Lnet/minecraft/util/math/Vec3d;
 */
package net.minecraft.entity.ai.goal;

import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.Vec3d;
import org.jspecify.annotations.Nullable;

public class WanderAroundFarGoal
extends WanderAroundGoal {
    public static final float CHANCE = 0.001f;
    protected final float probability;

    public WanderAroundFarGoal(PathAwareEntity arg, double d) {
        this(arg, d, 0.001f);
    }

    public WanderAroundFarGoal(PathAwareEntity mob, double speed, float probability) {
        super(mob, speed);
        this.probability = probability;
    }

    @Override
    protected @Nullable Vec3d getWanderTarget() {
        if (this.mob.isTouchingWater()) {
            Vec3d lv = FuzzyTargeting.find(this.mob, 15, 7);
            return lv == null ? super.getWanderTarget() : lv;
        }
        if (this.mob.getRandom().nextFloat() >= this.probability) {
            return FuzzyTargeting.find(this.mob, 10, 7);
        }
        return super.getWanderTarget();
    }
}

