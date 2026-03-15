package net.fabricmc.fabric.mixin.content.registry;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.behavior.GiveGiftToHero;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.level.storage.loot.LootTable;

@Mixin(GiveGiftToHero.class)
public interface GiveGiftToHeroAccessor {
	@Accessor("GIFTS")
	static Map<ResourceKey<VillagerProfession>, ResourceKey<LootTable>> fabric_getGifts() {
		throw new AssertionError("Untransformed @Accessor");
	}
}
