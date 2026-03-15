package net.fabricmc.fabric.mixin.particle;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.util.ParticleUtils;
import net.minecraft.world.level.LevelAccessor;

import net.fabricmc.fabric.impl.particle.BlockStateParticleEffectExtension;

@Mixin(ParticleUtils.class)
abstract class ParticleUtilsMixin {
	@ModifyExpressionValue(method = "spawnSmashAttackParticles", at = @At(value = "NEW", target = "(Lnet/minecraft/core/particles/ParticleType;Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/core/particles/BlockParticleOption;"))
	private static BlockParticleOption modifyBlockStateParticleEffect(BlockParticleOption original, LevelAccessor world, BlockPos pos, int count) {
		((BlockStateParticleEffectExtension) original).fabric_setBlockPos(pos);
		return original;
	}
}
