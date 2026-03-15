package net.fabricmc.fabric.mixin.content.registry;

import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;

import net.fabricmc.fabric.impl.content.registry.OxidizableBlocksRegistryImpl;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockBehaviourBlockStateBaseMixin extends StateHolder<Block, BlockState> implements OxidizableBlocksRegistryImpl.RandomTickCacheRefresher {
	@Shadow
	protected abstract BlockState asState();

	@Shadow
	private boolean isRandomlyTicking;

	private BlockBehaviourBlockStateBaseMixin(Block owner, Reference2ObjectArrayMap<Property<?>, Comparable<?>> propertyMap, MapCodec<BlockState> codec) {
		super(owner, propertyMap, codec);
	}

	@Override
	public void fabric_api$refreshRandomTickCache() {
		this.isRandomlyTicking = ((BlockBehaviourAccessor) this.owner).callHasRandomTicks(this.asState());
	}
}
