/*
 * External method calls:
 *   Lnet/minecraft/block/pattern/BlockPattern;searchAround(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/pattern/BlockPattern$Result;
 *   Lnet/minecraft/entity/EntityType;create(Lnet/minecraft/world/World;Lnet/minecraft/entity/SpawnReason;)Lnet/minecraft/entity/Entity;
 *   Lnet/minecraft/block/CarvedPumpkinBlock;breakPatternBlocks(Lnet/minecraft/world/World;Lnet/minecraft/block/pattern/BlockPattern$Result;)V
 *   Lnet/minecraft/block/pattern/BlockPattern$Result;translate(III)Lnet/minecraft/block/pattern/CachedBlockPosition;
 *   Lnet/minecraft/entity/boss/WitherEntity;refreshPositionAndAngles(DDDFF)V
 *   Lnet/minecraft/advancement/criterion/SummonedEntityCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/entity/Entity;)V
 *   Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z
 *   Lnet/minecraft/block/CarvedPumpkinBlock;updatePatternBlocks(Lnet/minecraft/world/World;Lnet/minecraft/block/pattern/BlockPattern$Result;)V
 *   Lnet/minecraft/block/pattern/BlockPatternBuilder;start()Lnet/minecraft/block/pattern/BlockPatternBuilder;
 *   Lnet/minecraft/block/pattern/BlockPatternBuilder;aisle([Ljava/lang/String;)Lnet/minecraft/block/pattern/BlockPatternBuilder;
 *   Lnet/minecraft/block/pattern/BlockPatternBuilder;where(CLjava/util/function/Predicate;)Lnet/minecraft/block/pattern/BlockPatternBuilder;
 *   Lnet/minecraft/predicate/block/BlockStatePredicate;forBlock(Lnet/minecraft/block/Block;)Lnet/minecraft/predicate/block/BlockStatePredicate;
 *   Lnet/minecraft/predicate/block/BlockStatePredicate;or(Ljava/util/function/Predicate;)Ljava/util/function/Predicate;
 *   Lnet/minecraft/block/pattern/CachedBlockPosition;matchesBlockState(Ljava/util/function/Predicate;)Ljava/util/function/Predicate;
 *   Lnet/minecraft/block/pattern/BlockPatternBuilder;build()Lnet/minecraft/block/pattern/BlockPattern;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/block/WitherSkullBlock;onPlaced(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V
 *   Lnet/minecraft/block/WitherSkullBlock;onPlaced(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/SkullBlockEntity;)V
 *   Lnet/minecraft/block/WitherSkullBlock;createCodec(Ljava/util/function/Function;)Lcom/mojang/serialization/MapCodec;
 */
package net.minecraft.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CarvedPumpkinBlock;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

public class WitherSkullBlock
extends SkullBlock {
    public static final MapCodec<WitherSkullBlock> CODEC = WitherSkullBlock.createCodec(WitherSkullBlock::new);
    private static @Nullable BlockPattern witherBossPattern;
    private static @Nullable BlockPattern witherDispenserPattern;

    public MapCodec<WitherSkullBlock> getCodec() {
        return CODEC;
    }

    protected WitherSkullBlock(AbstractBlock.Settings arg) {
        super(SkullBlock.Type.WITHER_SKELETON, arg);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        WitherSkullBlock.onPlaced(world, pos);
    }

    public static void onPlaced(World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof SkullBlockEntity) {
            SkullBlockEntity lv = (SkullBlockEntity)blockEntity;
            WitherSkullBlock.onPlaced(world, pos, lv);
        }
    }

    public static void onPlaced(World world, BlockPos pos, SkullBlockEntity blockEntity) {
        boolean bl;
        if (world.isClient()) {
            return;
        }
        BlockState lv = blockEntity.getCachedState();
        boolean bl2 = bl = lv.isOf(Blocks.WITHER_SKELETON_SKULL) || lv.isOf(Blocks.WITHER_SKELETON_WALL_SKULL);
        if (!bl || pos.getY() < world.getBottomY() || world.getDifficulty() == Difficulty.PEACEFUL) {
            return;
        }
        BlockPattern.Result lv2 = WitherSkullBlock.getWitherBossPattern().searchAround(world, pos);
        if (lv2 == null) {
            return;
        }
        WitherEntity lv3 = EntityType.WITHER.create(world, SpawnReason.TRIGGERED);
        if (lv3 != null) {
            CarvedPumpkinBlock.breakPatternBlocks(world, lv2);
            BlockPos lv4 = lv2.translate(1, 2, 0).getBlockPos();
            lv3.refreshPositionAndAngles((double)lv4.getX() + 0.5, (double)lv4.getY() + 0.55, (double)lv4.getZ() + 0.5, lv2.getForwards().getAxis() == Direction.Axis.X ? 0.0f : 90.0f, 0.0f);
            lv3.bodyYaw = lv2.getForwards().getAxis() == Direction.Axis.X ? 0.0f : 90.0f;
            lv3.onSummoned();
            for (ServerPlayerEntity lv5 : world.getNonSpectatingEntities(ServerPlayerEntity.class, lv3.getBoundingBox().expand(50.0))) {
                Criteria.SUMMONED_ENTITY.trigger(lv5, lv3);
            }
            world.spawnEntity(lv3);
            CarvedPumpkinBlock.updatePatternBlocks(world, lv2);
        }
    }

    public static boolean canDispense(World world, BlockPos pos, ItemStack stack) {
        if (stack.isOf(Items.WITHER_SKELETON_SKULL) && pos.getY() >= world.getBottomY() + 2 && world.getDifficulty() != Difficulty.PEACEFUL && !world.isClient()) {
            return WitherSkullBlock.getWitherDispenserPattern().searchAround(world, pos) != null;
        }
        return false;
    }

    private static BlockPattern getWitherBossPattern() {
        if (witherBossPattern == null) {
            witherBossPattern = BlockPatternBuilder.start().aisle("^^^", "###", "~#~").where('#', pos -> pos.getBlockState().isIn(BlockTags.WITHER_SUMMON_BASE_BLOCKS)).where('^', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.WITHER_SKELETON_SKULL).or(BlockStatePredicate.forBlock(Blocks.WITHER_SKELETON_WALL_SKULL)))).where('~', pos -> pos.getBlockState().isAir()).build();
        }
        return witherBossPattern;
    }

    private static BlockPattern getWitherDispenserPattern() {
        if (witherDispenserPattern == null) {
            witherDispenserPattern = BlockPatternBuilder.start().aisle("   ", "###", "~#~").where('#', pos -> pos.getBlockState().isIn(BlockTags.WITHER_SUMMON_BASE_BLOCKS)).where('~', pos -> pos.getBlockState().isAir()).build();
        }
        return witherDispenserPattern;
    }
}

