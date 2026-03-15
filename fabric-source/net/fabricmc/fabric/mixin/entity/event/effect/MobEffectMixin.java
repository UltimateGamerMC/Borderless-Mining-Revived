package net.fabricmc.fabric.mixin.entity.event.effect;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.effect.MobEffect;

import net.fabricmc.fabric.api.entity.event.v1.effect.FabricMobEffect;

@Mixin(MobEffect.class)
public final class MobEffectMixin implements FabricMobEffect {
	private MobEffectMixin() {
	}
}
