/*
 * External method calls:
 *   Lnet/minecraft/block/Block;onUseWithItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;
 *   Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V
 *   Lnet/minecraft/block/BlockState;with(Lnet/minecraft/state/property/Property;Ljava/lang/Comparable;)Ljava/lang/Object;
 *   Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/EquipmentSlot;)V
 *   Lnet/minecraft/world/World;emitGameEvent(Lnet/minecraft/entity/Entity;Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/util/math/BlockPos;)V
 *   Lnet/minecraft/entity/player/PlayerEntity;incrementStat(Lnet/minecraft/stat/Stat;)V
 *   Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z
 *
 * Internal private/static methods:
 *   Lnet/minecraft/block/PumpkinBlock;generateBlockInteractLoot(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/Entity;Ljava/util/function/BiConsumer;)Z
 *   Lnet/minecraft/block/PumpkinBlock;createCodec(Ljava/util/function/Function;)Lcom/mojang/serialization/MapCodec;
 */
package net.minecraft.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CarvedPumpkinBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTables;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class PumpkinBlock
extends Block {
    public static final MapCodec<PumpkinBlock> CODEC = PumpkinBlock.createCodec(PumpkinBlock::new);

    public MapCodec<PumpkinBlock> getCodec() {
        return CODEC;
    }

    protected PumpkinBlock(AbstractBlock.Settings arg) {
        super(arg);
    }

    @Override
    protected ActionResult onUseWithItem(ItemStack stack2, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!stack2.isOf(Items.SHEARS)) {
            return super.onUseWithItem(stack2, state, world, pos, player, hand, hit);
        }
        if (!(world instanceof ServerWorld)) {
            return ActionResult.SUCCESS;
        }
        ServerWorld lv = (ServerWorld)world;
        Direction lv2 = hit.getSide();
        Direction lv3 = lv2.getAxis() == Direction.Axis.Y ? player.getHorizontalFacing().getOpposite() : lv2;
        PumpkinBlock.generateBlockInteractLoot(lv, LootTables.PUMPKIN_CARVE, state, world.getBlockEntity(pos), stack2, player, (worldx, stack) -> {
            ItemEntity lv = new ItemEntity(world, (double)pos.getX() + 0.5 + (double)lv3.getOffsetX() * 0.65, (double)pos.getY() + 0.1, (double)pos.getZ() + 0.5 + (double)lv3.getOffsetZ() * 0.65, (ItemStack)stack);
            lv.setVelocity(0.05 * (double)lv3.getOffsetX() + arg.random.nextDouble() * 0.02, 0.05, 0.05 * (double)lv3.getOffsetZ() + arg.random.nextDouble() * 0.02);
            world.spawnEntity(lv);
        });
        world.playSound(null, pos, SoundEvents.BLOCK_PUMPKIN_CARVE, SoundCategory.BLOCKS, 1.0f, 1.0f);
        world.setBlockState(pos, (BlockState)Blocks.CARVED_PUMPKIN.getDefaultState().with(CarvedPumpkinBlock.FACING, lv3), Block.NOTIFY_ALL_AND_REDRAW);
        stack2.damage(1, (LivingEntity)player, hand.getEquipmentSlot());
        world.emitGameEvent((Entity)player, GameEvent.SHEAR, pos);
        player.incrementStat(Stats.USED.getOrCreateStat(Items.SHEARS));
        return ActionResult.SUCCESS;
    }
}

