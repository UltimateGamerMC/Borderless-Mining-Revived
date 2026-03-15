/*
 * External method calls:
 *   Lnet/minecraft/resource/ResourceFinder;toResourcePath(Lnet/minecraft/util/Identifier;)Lnet/minecraft/util/Identifier;
 */
package net.minecraft.client.texture.atlas;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.texture.atlas.AtlasSource;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;

@Environment(value=EnvType.CLIENT)
public record SingleAtlasSource(Identifier resourceId, Optional<Identifier> spriteId) implements AtlasSource
{
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final MapCodec<SingleAtlasSource> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Identifier.CODEC.fieldOf("resource")).forGetter(SingleAtlasSource::resourceId), Identifier.CODEC.optionalFieldOf("sprite").forGetter(SingleAtlasSource::spriteId)).apply((Applicative<SingleAtlasSource, ?>)instance, SingleAtlasSource::new));

    public SingleAtlasSource(Identifier resourceId) {
        this(resourceId, Optional.empty());
    }

    @Override
    public void load(ResourceManager resourceManager, AtlasSource.SpriteRegions regions) {
        Identifier lv = RESOURCE_FINDER.toResourcePath(this.resourceId);
        Optional<Resource> optional = resourceManager.getResource(lv);
        if (optional.isPresent()) {
            regions.add(this.spriteId.orElse(this.resourceId), optional.get());
        } else {
            LOGGER.warn("Missing sprite: {}", (Object)lv);
        }
    }

    public MapCodec<SingleAtlasSource> getCodec() {
        return CODEC;
    }
}

