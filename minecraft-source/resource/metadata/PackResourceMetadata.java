/*
 * External method calls:
 *   Lnet/minecraft/resource/PackVersion;createRangeCodec(Lnet/minecraft/resource/ResourceType;)Lcom/mojang/serialization/MapCodec;
 *   Lnet/minecraft/resource/PackVersion;of(I)Lnet/minecraft/resource/PackVersion;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/resource/metadata/PackResourceMetadata;createCodec(Lnet/minecraft/resource/ResourceType;)Lcom/mojang/serialization/Codec;
 */
package net.minecraft.resource.metadata;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resource.PackVersion;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.metadata.ResourceMetadataSerializer;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.dynamic.Range;

public record PackResourceMetadata(Text description, Range<PackVersion> supportedFormats) {
    private static final Codec<PackResourceMetadata> DESCRIPTION_CODEC = RecordCodecBuilder.create(instance -> instance.group(((MapCodec)TextCodecs.CODEC.fieldOf("description")).forGetter(PackResourceMetadata::description)).apply((Applicative<PackResourceMetadata, ?>)instance, description -> new PackResourceMetadata((Text)description, new Range<PackVersion>(PackVersion.of(Integer.MAX_VALUE)))));
    public static final ResourceMetadataSerializer<PackResourceMetadata> CLIENT_RESOURCES_SERIALIZER = new ResourceMetadataSerializer<PackResourceMetadata>("pack", PackResourceMetadata.createCodec(ResourceType.CLIENT_RESOURCES));
    public static final ResourceMetadataSerializer<PackResourceMetadata> SERVER_DATA_SERIALIZER = new ResourceMetadataSerializer<PackResourceMetadata>("pack", PackResourceMetadata.createCodec(ResourceType.SERVER_DATA));
    public static final ResourceMetadataSerializer<PackResourceMetadata> DESCRIPTION_SERIALIZER = new ResourceMetadataSerializer<PackResourceMetadata>("pack", DESCRIPTION_CODEC);

    private static Codec<PackResourceMetadata> createCodec(ResourceType type) {
        return RecordCodecBuilder.create(instance -> instance.group(((MapCodec)TextCodecs.CODEC.fieldOf("description")).forGetter(PackResourceMetadata::description), PackVersion.createRangeCodec(type).forGetter(PackResourceMetadata::supportedFormats)).apply((Applicative<PackResourceMetadata, ?>)instance, PackResourceMetadata::new));
    }

    public static ResourceMetadataSerializer<PackResourceMetadata> getSerializerFor(ResourceType type) {
        return switch (type) {
            default -> throw new MatchException(null, null);
            case ResourceType.CLIENT_RESOURCES -> CLIENT_RESOURCES_SERIALIZER;
            case ResourceType.SERVER_DATA -> SERVER_DATA_SERIALIZER;
        };
    }
}

