/*
 * External method calls:
 *   Lnet/minecraft/item/ItemStack;emitUseGameEvent(Lnet/minecraft/entity/Entity;Lnet/minecraft/registry/entry/RegistryEntry$Reference;)V
 *   Lnet/minecraft/world/World;syncWorldEvent(ILnet/minecraft/util/math/BlockPos;I)V
 *   Lnet/minecraft/block/Fertilizable;grow(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/random/Random;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V
 *   Lnet/minecraft/item/ItemStack;decrement(I)V
 *   Lnet/minecraft/block/BlockState;with(Lnet/minecraft/state/property/Property;Ljava/lang/Comparable;)Ljava/lang/Object;
 *   Lnet/minecraft/particle/ParticleUtil;spawnParticlesAround(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;IDDZLnet/minecraft/particle/ParticleEffect;)V
 *   Lnet/minecraft/particle/ParticleUtil;spawnParticlesAround(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;ILnet/minecraft/particle/ParticleEffect;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/item/BoneMealItem;useOnFertilizable(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z
 *   Lnet/minecraft/item/BoneMealItem;useOnGround(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z
 */
package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DeadCoralWallFanBlock;
import net.minecraft.block.Fertilizable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.ParticleUtil;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.event.GameEvent;
import org.jspecify.annotations.Nullable;

public class BoneMealItem
extends Item {
    public static final int field_30851 = 3;
    public static final int field_30852 = 1;
    public static final int field_30853 = 3;

    public BoneMealItem(Item.Settings arg) {
        super(arg);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World lv = context.getWorld();
        BlockPos lv2 = context.getBlockPos();
        BlockPos lv3 = lv2.offset(context.getSide());
        ItemStack lv4 = context.getStack();
        if (BoneMealItem.useOnFertilizable(lv4, lv, lv2)) {
            if (!lv.isClient()) {
                lv4.emitUseGameEvent(context.getPlayer(), GameEvent.ITEM_INTERACT_FINISH);
                lv.syncWorldEvent(WorldEvents.BONE_MEAL_USED, lv2, 15);
            }
            return ActionResult.SUCCESS;
        }
        BlockState lv5 = lv.getBlockState(lv2);
        boolean bl = lv5.isSideSolidFullSquare(lv, lv2, context.getSide());
        if (bl && BoneMealItem.useOnGround(lv4, lv, lv3, context.getSide())) {
            if (!lv.isClient()) {
                lv4.emitUseGameEvent(context.getPlayer(), GameEvent.ITEM_INTERACT_FINISH);
                lv.syncWorldEvent(WorldEvents.BONE_MEAL_USED, lv3, 15);
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    public static boolean useOnFertilizable(ItemStack stack, World world, BlockPos pos) {
        Fertilizable lv2;
        BlockState lv = world.getBlockState(pos);
        Block block = lv.getBlock();
        if (block instanceof Fertilizable && (lv2 = (Fertilizable)((Object)block)).isFertilizable(world, pos, lv)) {
            if (world instanceof ServerWorld) {
                if (lv2.canGrow(world, world.random, pos, lv)) {
                    lv2.grow((ServerWorld)world, world.random, pos, lv);
                }
                stack.decrement(1);
            }
            return true;
        }
        return false;
    }

    public static boolean useOnGround(ItemStack stack, World world, BlockPos blockPos, @Nullable Direction facing) {
        if (!world.getBlockState(blockPos).isOf(Blocks.WATER) || world.getFluidState(blockPos).getLevel() != 8) {
            return false;
        }
        if (!(world instanceof ServerWorld)) {
            return true;
        }
        Random lv = world.getRandom();
        block0: for (int i = 0; i < 128; ++i) {
            BlockPos lv2 = blockPos;
            BlockState lv3 = Blocks.SEAGRASS.getDefaultState();
            for (int j = 0; j < i / 16; ++j) {
                if (world.getBlockState(lv2 = lv2.add(lv.nextInt(3) - 1, (lv.nextInt(3) - 1) * lv.nextInt(3) / 2, lv.nextInt(3) - 1)).isFullCube(world, lv2)) continue block0;
            }
            RegistryEntry<Biome> lv4 = world.getBiome(lv2);
            if (lv4.isIn(BiomeTags.PRODUCES_CORALS_FROM_BONEMEAL)) {
                if (i == 0 && facing != null && facing.getAxis().isHorizontal()) {
                    lv3 = Registries.BLOCK.getRandomEntry(BlockTags.WALL_CORALS, world.random).map(blockEntry -> ((Block)blockEntry.value()).getDefaultState()).orElse(lv3);
                    if (lv3.contains(DeadCoralWallFanBlock.FACING)) {
                        lv3 = (BlockState)lv3.with(DeadCoralWallFanBlock.FACING, facing);
                    }
                } else if (lv.nextInt(4) == 0) {
                    lv3 = Registries.BLOCK.getRandomEntry(BlockTags.UNDERWATER_BONEMEALS, world.random).map(blockEntry -> ((Block)blockEntry.value()).getDefaultState()).orElse(lv3);
                }
            }
            if (lv3.isIn(BlockTags.WALL_CORALS, state -> state.contains(DeadCoralWallFanBlock.FACING))) {
                for (int k = 0; !lv3.canPlaceAt(world, lv2) && k < 4; ++k) {
                    lv3 = (BlockState)lv3.with(DeadCoralWallFanBlock.FACING, Direction.Type.HORIZONTAL.random(lv));
                }
            }
            if (!lv3.canPlaceAt(world, lv2)) continue;
            BlockState lv5 = world.getBlockState(lv2);
            if (lv5.isOf(Blocks.WATER) && world.getFluidState(lv2).getLevel() == 8) {
                world.setBlockState(lv2, lv3, Block.NOTIFY_ALL);
                continue;
            }
            if (!lv5.isOf(Blocks.SEAGRASS) || !((Fertilizable)((Object)Blocks.SEAGRASS)).isFertilizable(world, lv2, lv5) || lv.nextInt(10) != 0) continue;
            ((Fertilizable)((Object)Blocks.SEAGRASS)).grow((ServerWorld)world, lv, lv2, lv5);
        }
        stack.decrement(1);
        return true;
    }

    public static void createParticles(WorldAccess world, BlockPos pos, int count) {
        BlockState lv = world.getBlockState(pos);
        Block block = lv.getBlock();
        if (block instanceof Fertilizable) {
            Fertilizable lv2 = (Fertilizable)((Object)block);
            BlockPos lv3 = lv2.getFertilizeParticlePos(pos);
            switch (lv2.getFertilizableType()) {
                case NEIGHBOR_SPREADER: {
                    ParticleUtil.spawnParticlesAround(world, lv3, count * 3, 3.0, 1.0, false, ParticleTypes.HAPPY_VILLAGER);
                    break;
                }
                case GROWER: {
                    ParticleUtil.spawnParticlesAround(world, lv3, count, ParticleTypes.HAPPY_VILLAGER);
                }
            }
        } else if (lv.isOf(Blocks.WATER)) {
            ParticleUtil.spawnParticlesAround(world, pos, count * 3, 3.0, 1.0, false, ParticleTypes.HAPPY_VILLAGER);
        }
    }
}

