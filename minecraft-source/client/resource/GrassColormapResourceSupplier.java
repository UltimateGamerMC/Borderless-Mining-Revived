/*
 * External method calls:
 *   Lnet/minecraft/client/util/RawTextureDataLoader;loadRawTextureData(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/Identifier;)[I
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/resource/GrassColormapResourceSupplier;apply([ILnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V
 *   Lnet/minecraft/client/resource/GrassColormapResourceSupplier;tryLoad(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)[I
 */
package net.minecraft.client.resource;

import java.io.IOException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.RawTextureDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SinglePreparationResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.biome.GrassColors;

@Environment(value=EnvType.CLIENT)
public class GrassColormapResourceSupplier
extends SinglePreparationResourceReloader<int[]> {
    private static final Identifier GRASS_COLORMAP_LOC = Identifier.ofVanilla("textures/colormap/grass.png");

    protected int[] tryLoad(ResourceManager resourceManager, Profiler profiler) {
        try {
            return RawTextureDataLoader.loadRawTextureData(resourceManager, GRASS_COLORMAP_LOC);
        } catch (IOException iOException) {
            throw new IllegalStateException("Failed to load grass color texture", iOException);
        }
    }

    @Override
    protected void apply(int[] is, ResourceManager arg, Profiler arg2) {
        GrassColors.setColorMap(is);
    }

    @Override
    protected /* synthetic */ Object prepare(ResourceManager manager, Profiler profiler) {
        return this.tryLoad(manager, profiler);
    }
}

