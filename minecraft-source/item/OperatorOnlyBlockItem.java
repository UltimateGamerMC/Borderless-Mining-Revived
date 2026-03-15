package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import org.jspecify.annotations.Nullable;

public class OperatorOnlyBlockItem
extends BlockItem {
    public OperatorOnlyBlockItem(Block arg, Item.Settings arg2) {
        super(arg, arg2);
    }

    @Override
    protected @Nullable BlockState getPlacementState(ItemPlacementContext context) {
        PlayerEntity lv = context.getPlayer();
        return lv == null || lv.isCreativeLevelTwoOp() ? super.getPlacementState(context) : null;
    }
}

