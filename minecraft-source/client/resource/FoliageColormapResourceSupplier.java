/*
 * External method calls:
 *   Lnet/minecraft/client/util/RawTextureDataLoader;loadRawTextureData(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/Identifier;)[I
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/resource/FoliageColormapResourceSupplier;apply([ILnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V
 *   Lnet/minecraft/client/resource/FoliageColormapResourceSupplier;reload(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)[I
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
import net.minecraft.world.biome.FoliageColors;

@Environment(value=EnvType.CLIENT)
public class FoliageColormapResourceSupplier
extends SinglePreparationResourceReloader<int[]> {
    private static final Identifier FOLIAGE_COLORMAP = Identifier.ofVanilla("textures/colormap/foliage.png");

    protected int[] reload(ResourceManager resourceManager, Profiler profiler) {
        try {
            return RawTextureDataLoader.loadRawTextureData(resourceManager, FOLIAGE_COLORMAP);
        } catch (IOException iOException) {
            throw new IllegalStateException("Failed to load foliage color texture", iOException);
        }
    }

    @Override
    protected void apply(int[] is, ResourceManager arg, Profiler arg2) {
        FoliageColors.setColorMap(is);
    }

    @Override
    protected /* synthetic */ Object prepare(ResourceManager manager, Profiler profiler) {
        return this.reload(manager, profiler);
    }
}

