/*
 * External method calls:
 *   Lnet/minecraft/client/texture/TextureContents;load(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/texture/TextureContents;
 */
package net.minecraft.client.texture;

import java.io.IOException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.texture.ReloadableTexture;
import net.minecraft.client.texture.TextureContents;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class ResourceTexture
extends ReloadableTexture {
    public ResourceTexture(Identifier location) {
        super(location);
    }

    @Override
    public TextureContents loadContents(ResourceManager resourceManager) throws IOException {
        return TextureContents.load(resourceManager, this.getId());
    }
}

