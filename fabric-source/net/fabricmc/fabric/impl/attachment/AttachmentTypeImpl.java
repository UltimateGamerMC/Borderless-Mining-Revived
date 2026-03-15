package net.fabricmc.fabric.impl.attachment;

import java.util.function.Supplier;

import com.mojang.serialization.Codec;
import org.jspecify.annotations.Nullable;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

public record AttachmentTypeImpl<A>(
		Identifier identifier,
		@Nullable Supplier<A> initializer,
		@Nullable Codec<A> persistenceCodec,
		@Nullable StreamCodec<? super RegistryFriendlyByteBuf, A> packetCodec,
		@Nullable AttachmentSyncPredicate syncPredicate,
		boolean copyOnDeath
) implements AttachmentType<A> {
	@Override
	public boolean isSynced() {
		return syncPredicate != null;
	}
}
