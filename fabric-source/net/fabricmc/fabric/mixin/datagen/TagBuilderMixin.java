package net.fabricmc.fabric.mixin.datagen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagEntry;

import net.fabricmc.fabric.impl.datagen.FabricTagBuilder;
import net.fabricmc.fabric.impl.datagen.ForcedTagEntry;

@Mixin(TagBuilder.class)
public abstract class TagBuilderMixin implements FabricTagBuilder {
	@Shadow
	public abstract TagBuilder add(TagEntry entry);

	@Unique
	private boolean replace = false;

	@Override
	public void fabric_setReplace(boolean replace) {
		this.replace = replace;
	}

	@Override
	public boolean fabric_isReplaced() {
		return this.replace;
	}

	@Override
	public void fabric_forceAddTag(Identifier tag) {
		this.add(new ForcedTagEntry(tag));
	}
}
