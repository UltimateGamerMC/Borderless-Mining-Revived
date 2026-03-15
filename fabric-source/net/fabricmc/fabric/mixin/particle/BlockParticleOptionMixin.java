package net.fabricmc.fabric.mixin.particle;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import net.fabricmc.fabric.api.particle.v1.FabricBlockStateParticleEffect;
import net.fabricmc.fabric.impl.particle.BlockStateParticleEffectExtension;
import net.fabricmc.fabric.impl.particle.ExtendedBlockStateParticleEffectPacketCodec;

@Mixin(BlockParticleOption.class)
abstract class BlockParticleOptionMixin implements FabricBlockStateParticleEffect, BlockStateParticleEffectExtension {
	@Nullable
	@Unique
	private BlockPos blockPos;

	@Override
	@Nullable
	public BlockPos getBlockPos() {
		return blockPos;
	}

	@Override
	public void fabric_setBlockPos(@Nullable BlockPos pos) {
		blockPos = pos;
	}

	@ModifyReturnValue(method = "streamCodec", at = @At("RETURN"))
	private static StreamCodec<? super RegistryFriendlyByteBuf, BlockParticleOption> modifyPacketCodec(StreamCodec<? super RegistryFriendlyByteBuf, BlockParticleOption> codec) {
		return new ExtendedBlockStateParticleEffectPacketCodec(codec);
	}
}
