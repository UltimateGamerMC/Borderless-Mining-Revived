package net.fabricmc.fabric.api.serialization.v1;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.ApiStatus;

import net.fabricmc.fabric.impl.serialization.SpecialCodecs;

/**
 * Additional codecs that can be used by modders.
 */
@ApiStatus.NonExtendable
public interface MoreCodecs {
	Codec<long[]> LONG_ARRAY = SpecialCodecs.LONG_ARRAY;
	Codec<byte[]> BYTE_ARRAY = SpecialCodecs.BYTE_ARRAY;
}
