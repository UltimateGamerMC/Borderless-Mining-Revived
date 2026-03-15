/*
 * External method calls:
 *   Lnet/minecraft/item/Item$Settings;shovel(Lnet/minecraft/item/ToolMaterial;FF)Lnet/minecraft/item/Item$Settings;
 *   Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V
 *   Lnet/minecraft/world/World;syncWorldEvent(Lnet/minecraft/entity/Entity;ILnet/minecraft/util/math/BlockPos;I)V
 *   Lnet/minecraft/block/CampfireBlock;extinguish(Lnet/minecraft/entity/Entity;Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V
 *   Lnet/minecraft/block/BlockState;with(Lnet/minecraft/state/property/Property;Ljava/lang/Comparable;)Ljava/lang/Object;
 *   Lnet/minecraft/world/event/GameEvent$Emitter;of(Lnet/minecraft/entity/Entity;Lnet/minecraft/block/BlockState;)Lnet/minecraft/world/event/GameEvent$Emitter;
 *   Lnet/minecraft/world/World;emitGameEvent(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/event/GameEvent$Emitter;)V
 *   Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/EquipmentSlot;)V
 */
package net.minecraft.item;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;

public class ShovelItem
extends Item {
    protected static final Map<Block, BlockState> PATH_STATES = Maps.newHashMap(new ImmutableMap.Builder<Block, BlockState>().put(Blocks.GRASS_BLOCK, Blocks.DIRT_PATH.getDefaultState()).put(Blocks.DIRT, Blocks.DIRT_PATH.getDefaultState()).put(Blocks.PODZOL, Blocks.DIRT_PATH.getDefaultState()).put(Blocks.COARSE_DIRT, Blocks.DIRT_PATH.getDefaultState()).put(Blocks.MYCELIUM, Blocks.DIRT_PATH.getDefaultState()).put(Blocks.ROOTED_DIRT, Blocks.DIRT_PATH.getDefaultState()).build());

    public ShovelItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Settings settings) {
        super(settings.shovel(material, attackDamage, attackSpeed));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World lv = context.getWorld();
        BlockPos lv2 = context.getBlockPos();
        BlockState lv3 = lv.getBlockState(lv2);
        if (context.getSide() != Direction.DOWN) {
            PlayerEntity lv4 = context.getPlayer();
            BlockState lv5 = PATH_STATES.get(lv3.getBlock());
            BlockState lv6 = null;
            if (lv5 != null && lv.getBlockState(lv2.up()).isAir()) {
                lv.playSound((Entity)lv4, lv2, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0f, 1.0f);
                lv6 = lv5;
            } else if (lv3.getBlock() instanceof CampfireBlock && lv3.get(CampfireBlock.LIT).booleanValue()) {
                if (!lv.isClient()) {
                    lv.syncWorldEvent(null, WorldEvents.FIRE_EXTINGUISHED, lv2, 0);
                }
                CampfireBlock.extinguish(context.getPlayer(), lv, lv2, lv3);
                lv6 = (BlockState)lv3.with(CampfireBlock.LIT, false);
            }
            if (lv6 != null) {
                if (!lv.isClient()) {
                    lv.setBlockState(lv2, lv6, Block.NOTIFY_ALL_AND_REDRAW);
                    lv.emitGameEvent(GameEvent.BLOCK_CHANGE, lv2, GameEvent.Emitter.of(lv4, lv6));
                    if (lv4 != null) {
                        context.getStack().damage(1, (LivingEntity)lv4, context.getHand().getEquipmentSlot());
                    }
                }
                return ActionResult.SUCCESS;
            }
            return ActionResult.PASS;
        }
        return ActionResult.PASS;
    }
}

