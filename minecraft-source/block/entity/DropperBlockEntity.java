/*
 * External method calls:
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 */
package net.minecraft.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class DropperBlockEntity
extends DispenserBlockEntity {
    private static final Text CONTAINER_NAME_TEXT = Text.translatable("container.dropper");

    public DropperBlockEntity(BlockPos arg, BlockState arg2) {
        super(BlockEntityType.DROPPER, arg, arg2);
    }

    @Override
    protected Text getContainerName() {
        return CONTAINER_NAME_TEXT;
    }
}

