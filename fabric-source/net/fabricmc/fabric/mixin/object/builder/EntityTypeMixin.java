package net.fabricmc.fabric.mixin.object.builder;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.entity.EntityType;

import net.fabricmc.fabric.impl.object.builder.FabricEntityTypeImpl;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin implements FabricEntityTypeImpl {
	@Unique
	@Nullable
	private Boolean alwaysUpdateVelocity;
	@Unique
	@Nullable
	private Boolean canPotentiallyExecuteCommands;

	@Inject(method = "trackDeltas", at = @At("HEAD"), cancellable = true)
	public void onAlwaysUpdateVelocity(CallbackInfoReturnable<Boolean> cir) {
		if (alwaysUpdateVelocity != null) {
			cir.setReturnValue(alwaysUpdateVelocity);
		}
	}

	@Inject(method = "onlyOpCanSetNbt", at = @At("HEAD"), cancellable = true)
	public void onCanPotentiallyExecuteCommands(CallbackInfoReturnable<Boolean> cir) {
		if (canPotentiallyExecuteCommands != null) {
			cir.setReturnValue(canPotentiallyExecuteCommands);
		}
	}

	@Override
	public void fabric_setAlwaysUpdateVelocity(@Nullable Boolean alwaysUpdateVelocity) {
		this.alwaysUpdateVelocity = alwaysUpdateVelocity;
	}

	@Override
	public void fabric_setCanPotentiallyExecuteCommands(@Nullable Boolean canPotentiallyExecuteCommands) {
		this.canPotentiallyExecuteCommands = canPotentiallyExecuteCommands;
	}
}
