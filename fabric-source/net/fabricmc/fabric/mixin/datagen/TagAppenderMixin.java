package net.fabricmc.fabric.mixin.datagen;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricProvidedTagBuilder;
import net.fabricmc.fabric.impl.datagen.FabricTagBuilder;

/**
 * Extends ProvidedTagBuilder to support setting the replace field.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Mixin(TagAppender.class)
interface TagAppenderMixin<E, T> extends FabricProvidedTagBuilder<E, T> {
	@Mixin(targets = "net.minecraft.data.tags.TagAppender$1")
	abstract class ProvidedTagBuilder1Mixin<E, T> implements TagAppenderMixin<E, T> {
		// the builder param
		@Shadow
		@Final
		TagBuilder val$builder;

		@Override
		public TagAppender<E, T> setReplace(boolean replace) {
			((FabricTagBuilder) this.val$builder).fabric_setReplace(replace);
			return (TagAppender<E, T>) this;
		}

		@Override
		public TagAppender<E, T> forceAddTag(TagKey<T> tag) {
			((FabricTagBuilder) this.val$builder).fabric_forceAddTag(tag.location());
			return (TagAppender<E, T>) this;
		}
	}

	@Mixin(targets = "net.minecraft.data.tags.TagAppender$2")
	abstract class ProvidedTagBuilder2Mixin<E, T> implements TagAppenderMixin<E, T> {
		// ProvidedTagBuilder.this
		@Shadow
		@Final
		TagAppender val$original;

		@Override
		public TagAppender<E, T> setReplace(boolean replace) {
			((FabricProvidedTagBuilder) this.val$original).setReplace(replace);
			return (TagAppender<E, T>) this;
		}

		@Override
		public TagAppender<E, T> forceAddTag(TagKey<T> tag) {
			((FabricProvidedTagBuilder) this.val$original).forceAddTag(tag);
			return (TagAppender<E, T>) this;
		}
	}
}
