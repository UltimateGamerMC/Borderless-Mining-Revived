package net.fabricmc.fabric.mixin.event.lifecycle;

import java.util.Map;

import com.llamalad7.mixinextras.sugar.Local;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	@Inject(method = "collectEquipmentChanges", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
	private void getEquipmentChanges(CallbackInfoReturnable<@Nullable Map<EquipmentSlot, ItemStack>> cir, @Local EquipmentSlot equipmentSlot, @Local(ordinal = 0) ItemStack previousStack, @Local(ordinal = 1) ItemStack currentStack) {
		ServerEntityEvents.EQUIPMENT_CHANGE.invoker().onChange((LivingEntity) (Object) this, equipmentSlot, previousStack, currentStack);
	}
}
