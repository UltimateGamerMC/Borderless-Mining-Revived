/*
 * External method calls:
 *   Lnet/minecraft/block/Block;onStacksDropped(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;Z)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/block/ExperienceDroppingBlock;dropExperienceWhenMined(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/math/intprovider/IntProvider;)V
 *   Lnet/minecraft/block/ExperienceDroppingBlock;createSettingsCodec()Lcom/mojang/serialization/codecs/RecordCodecBuilder;
 */
package net.minecraft.block;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.IntProvider;

public class ExperienceDroppingBlock
extends Block {
    public static final MapCodec<ExperienceDroppingBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)IntProvider.createValidatingCodec(0, 10).fieldOf("experience")).forGetter(block -> block.experienceDropped), ExperienceDroppingBlock.createSettingsCodec()).apply((Applicative<ExperienceDroppingBlock, ?>)instance, ExperienceDroppingBlock::new));
    private final IntProvider experienceDropped;

    public MapCodec<? extends ExperienceDroppingBlock> getCodec() {
        return CODEC;
    }

    public ExperienceDroppingBlock(IntProvider experienceDropped, AbstractBlock.Settings settings) {
        super(settings);
        this.experienceDropped = experienceDropped;
    }

    @Override
    protected void onStacksDropped(BlockState state, ServerWorld world, BlockPos pos, ItemStack tool, boolean dropExperience) {
        super.onStacksDropped(state, world, pos, tool, dropExperience);
        if (dropExperience) {
            this.dropExperienceWhenMined(world, pos, tool, this.experienceDropped);
        }
    }
}

