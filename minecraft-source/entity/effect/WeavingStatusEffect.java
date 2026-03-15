/*
 * External method calls:
 *   Lnet/minecraft/server/world/ServerWorld;syncWorldEvent(ILnet/minecraft/util/math/BlockPos;I)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/effect/WeavingStatusEffect;tryPlaceCobweb(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/random/Random;Lnet/minecraft/util/math/BlockPos;)V
 */
package net.minecraft.entity.effect;

import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.function.ToIntFunction;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.rule.GameRules;

class WeavingStatusEffect
extends StatusEffect {
    private final ToIntFunction<Random> cobwebChanceFunction;

    protected WeavingStatusEffect(StatusEffectCategory category, int color, ToIntFunction<Random> cobwebChanceFunction) {
        super(category, color, ParticleTypes.ITEM_COBWEB);
        this.cobwebChanceFunction = cobwebChanceFunction;
    }

    @Override
    public void onEntityRemoval(ServerWorld world, LivingEntity entity, int amplifier, Entity.RemovalReason reason) {
        if (reason == Entity.RemovalReason.KILLED && (entity instanceof PlayerEntity || world.getGameRules().getValue(GameRules.DO_MOB_GRIEFING).booleanValue())) {
            this.tryPlaceCobweb(world, entity.getRandom(), entity.getBlockPos());
        }
    }

    private void tryPlaceCobweb(ServerWorld world, Random random, BlockPos pos) {
        HashSet<BlockPos> set = Sets.newHashSet();
        int i = this.cobwebChanceFunction.applyAsInt(random);
        for (BlockPos lv : BlockPos.iterateRandomly(random, 15, pos, 1)) {
            BlockPos lv2 = lv.down();
            if (set.contains(lv) || !world.getBlockState(lv).isReplaceable() || !world.getBlockState(lv2).isSideSolidFullSquare(world, lv2, Direction.UP)) continue;
            set.add(lv.toImmutable());
            if (set.size() < i) continue;
            break;
        }
        for (BlockPos lv : set) {
            world.setBlockState(lv, Blocks.COBWEB.getDefaultState(), Block.NOTIFY_ALL);
            world.syncWorldEvent(WorldEvents.COBWEB_WEAVED, lv, 0);
        }
    }
}

