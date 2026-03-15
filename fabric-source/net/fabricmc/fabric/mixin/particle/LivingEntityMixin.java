package net.fabricmc.fabric.mixin.particle;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;

import net.fabricmc.fabric.impl.particle.BlockStateParticleEffectExtension;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin {
	@ModifyExpressionValue(method = "checkFallDamage", at = @At(value = "NEW", target = "(Lnet/minecraft/core/particles/ParticleType;Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/core/particles/BlockParticleOption;"))
	private BlockParticleOption modifyBlockStateParticleEffect(BlockParticleOption original, double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
		((BlockStateParticleEffectExtension) original).fabric_setBlockPos(landedPosition);
		return original;
	}
}
