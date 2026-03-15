/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/brain/Brain;refreshActivities(Lnet/minecraft/world/attribute/WorldEnvironmentAttributeAccess;JLnet/minecraft/util/math/Vec3d;)V
 *   Lnet/minecraft/util/DyeColor;values()[Lnet/minecraft/util/DyeColor;
 *   Lnet/minecraft/entity/projectile/ProjectileEntity;spawn(Lnet/minecraft/entity/projectile/ProjectileEntity;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/projectile/ProjectileEntity;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/ai/brain/task/CelebrateRaidWinTask;createFirework(Lnet/minecraft/util/DyeColor;I)Lnet/minecraft/item/ItemStack;
 *   Lnet/minecraft/entity/ai/brain/task/CelebrateRaidWinTask;finishRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/VillagerEntity;J)V
 *   Lnet/minecraft/entity/ai/brain/task/CelebrateRaidWinTask;keepRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/VillagerEntity;J)V
 */
package net.minecraft.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.List;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.component.type.FireworksComponent;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.ai.brain.task.SeekSkyTask;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.raid.Raid;
import org.jspecify.annotations.Nullable;

public class CelebrateRaidWinTask
extends MultiTickTask<VillagerEntity> {
    private @Nullable Raid raid;

    public CelebrateRaidWinTask(int minRunTime, int maxRunTime) {
        super(ImmutableMap.of(), minRunTime, maxRunTime);
    }

    @Override
    protected boolean shouldRun(ServerWorld arg, VillagerEntity arg2) {
        BlockPos lv = arg2.getBlockPos();
        this.raid = arg.getRaidAt(lv);
        return this.raid != null && this.raid.hasWon() && SeekSkyTask.isSkyVisible(arg, arg2, lv);
    }

    @Override
    protected boolean shouldKeepRunning(ServerWorld arg, VillagerEntity arg2, long l) {
        return this.raid != null && !this.raid.hasStopped();
    }

    @Override
    protected void finishRunning(ServerWorld arg, VillagerEntity arg2, long l) {
        this.raid = null;
        arg2.getBrain().refreshActivities(arg.getEnvironmentAttributes(), arg.getTime(), arg2.getEntityPos());
    }

    @Override
    protected void keepRunning(ServerWorld arg, VillagerEntity arg2, long l) {
        Random lv = arg2.getRandom();
        if (lv.nextInt(100) == 0) {
            arg2.playCelebrateSound();
        }
        if (lv.nextInt(200) == 0 && SeekSkyTask.isSkyVisible(arg, arg2, arg2.getBlockPos())) {
            DyeColor lv2 = Util.getRandom(DyeColor.values(), lv);
            int i = lv.nextInt(3);
            ItemStack lv3 = this.createFirework(lv2, i);
            ProjectileEntity.spawn(new FireworkRocketEntity(arg2.getEntityWorld(), arg2, arg2.getX(), arg2.getEyeY(), arg2.getZ(), lv3), arg, lv3);
        }
    }

    private ItemStack createFirework(DyeColor color, int flight) {
        ItemStack lv = new ItemStack(Items.FIREWORK_ROCKET);
        lv.set(DataComponentTypes.FIREWORKS, new FireworksComponent((byte)flight, List.of(new FireworkExplosionComponent(FireworkExplosionComponent.Type.BURST, IntList.of(color.getFireworkColor()), IntList.of(), false, false))));
        return lv;
    }
}

