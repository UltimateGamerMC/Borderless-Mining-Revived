package net.fabricmc.fabric.mixin.particle;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.BrushItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import net.fabricmc.fabric.impl.particle.BlockStateParticleEffectExtension;

@Mixin(BrushItem.class)
abstract class BrushItemMixin {
	@ModifyExpressionValue(method = "spawnDustParticles", at = @At(value = "NEW", target = "(Lnet/minecraft/core/particles/ParticleType;Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/core/particles/BlockParticleOption;"))
	private BlockParticleOption modifyBlockStateParticleEffect(BlockParticleOption original, Level world, BlockHitResult hitResult, BlockState state, Vec3 userRotation, HumanoidArm arm) {
		((BlockStateParticleEffectExtension) original).fabric_setBlockPos(hitResult.getBlockPos());
		return original;
	}
}
