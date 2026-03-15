package net.fabricmc.fabric.api.datagen.v1.provider;

import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.TagKey;

/**
 * Interface-injected to {@link net.minecraft.data.tags.TagAppender}.
 */
@SuppressWarnings("unchecked")
public interface FabricProvidedTagBuilder<E, T> {
	/**
	 * Sets the value of the {@code replace} flag. When set to {@code true}
	 * this tag will replace contents of any other tag.
	 * @param replace whether to replace the contents of the tag
	 * @return this, for chaining
	 */
	default TagAppender<E, T> setReplace(boolean replace) {
		return (TagAppender<E, T>) this;
	}

	default TagAppender<E, T> forceAddTag(TagKey<T> tag) {
		return (TagAppender<E, T>) this;
	}
}
