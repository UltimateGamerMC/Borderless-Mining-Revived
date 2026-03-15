/*
 * External method calls:
 *   Lnet/minecraft/client/texture/atlas/AtlasSource$SpriteRegions;removeIf(Ljava/util/function/Predicate;)V
 */
package net.minecraft.client.texture.atlas;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.texture.atlas.AtlasSource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.metadata.BlockEntry;

@Environment(value=EnvType.CLIENT)
public record FilterAtlasSource(BlockEntry pattern) implements AtlasSource
{
    public static final MapCodec<FilterAtlasSource> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)BlockEntry.CODEC.fieldOf("pattern")).forGetter(FilterAtlasSource::pattern)).apply((Applicative<FilterAtlasSource, ?>)instance, FilterAtlasSource::new));

    @Override
    public void load(ResourceManager resourceManager, AtlasSource.SpriteRegions regions) {
        regions.removeIf(this.pattern.getIdentifierPredicate());
    }

    public MapCodec<FilterAtlasSource> getCodec() {
        return CODEC;
    }
}

