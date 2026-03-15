package net.fabricmc.fabric.mixin.tag.convention;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.tags.TagKey;

import net.fabricmc.fabric.api.tag.FabricTagKey;

@Mixin(TagKey.class)
public interface TagKeyMixin extends FabricTagKey {
}
