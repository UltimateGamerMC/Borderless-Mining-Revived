package net.fabricmc.fabric.mixin.item;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.enchantment.Enchantment;

@Mixin(Enchantment.Builder.class)
public interface EnchantmentBuilderAccessor {
	@Accessor("definition")
	Enchantment.EnchantmentDefinition getDefinition();

	@Accessor("exclusiveSet")
	HolderSet<Enchantment> getExclusiveSet();

	@Accessor("effectMapBuilder")
	DataComponentMap.Builder getEffectMap();

	@Invoker("getEffectsList")
	<E> List<E> invokeGetEffectsList(DataComponentType<List<E>> type);
}
