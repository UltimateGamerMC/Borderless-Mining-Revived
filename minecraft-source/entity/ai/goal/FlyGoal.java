/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/AboveGroundTargeting;find(Lnet/minecraft/entity/mob/PathAwareEntity;IIDDFII)Lnet/minecraft/util/math/Vec3d;
 *   Lnet/minecraft/entity/ai/NoPenaltySolidTargeting;find(Lnet/minecraft/entity/mob/PathAwareEntity;IIIDDD)Lnet/minecraft/util/math/Vec3d;
 */
package net.minecraft.entity.ai.goal;

import net.minecraft.entity.ai.AboveGroundTargeting;
import net.minecraft.entity.ai.NoPenaltySolidTargeting;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.Vec3d;
import org.jspecify.annotations.Nullable;

public class FlyGoal
extends WanderAroundFarGoal {
    public FlyGoal(PathAwareEntity arg, double d) {
        super(arg, d);
    }

    @Override
    protected @Nullable Vec3d getWanderTarget() {
        Vec3d lv = this.mob.getRotationVec(0.0f);
        int i = 8;
        Vec3d lv2 = AboveGroundTargeting.find(this.mob, 8, 7, lv.x, lv.z, 1.5707964f, 3, 1);
        if (lv2 != null) {
            return lv2;
        }
        return NoPenaltySolidTargeting.find(this.mob, 8, 4, -2, lv.x, lv.z, 1.5707963705062866);
    }
}

