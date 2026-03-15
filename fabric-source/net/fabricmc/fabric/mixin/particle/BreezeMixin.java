package net.fabricmc.fabric.mixin.particle;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.breeze.Breeze;
import net.minecraft.world.level.Level;

import net.fabricmc.fabric.impl.particle.BlockStateParticleEffectExtension;

@Mixin(Breeze.class)
abstract class BreezeMixin extends Monster {
	private BreezeMixin(EntityType<? extends Monster> entityType, Level world) {
		super(entityType, world);
	}

	@ModifyExpressionValue(method = {"emitJumpTrailParticles", "emitGroundParticles"}, at = @At(value = "NEW", target = "(Lnet/minecraft/core/particles/ParticleType;Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/core/particles/BlockParticleOption;"))
	private BlockParticleOption modifyBlockStateParticleEffect(BlockParticleOption original) {
		BlockPos blockPos = !getInBlockState().isAir() ? blockPosition() : getOnPos();
		((BlockStateParticleEffectExtension) original).fabric_setBlockPos(blockPos);
		return original;
	}
}
