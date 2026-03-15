/*
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/BlockRenderLayerGroup;method_72169()[Lnet/minecraft/client/render/BlockRenderLayerGroup;
 */
package net.minecraft.client.render;

import java.util.Locale;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.BlockRenderLayer;

@Environment(value=EnvType.CLIENT)
public enum BlockRenderLayerGroup {
    OPAQUE(BlockRenderLayer.SOLID, BlockRenderLayer.CUTOUT),
    TRANSLUCENT(BlockRenderLayer.TRANSLUCENT),
    TRIPWIRE(BlockRenderLayer.TRIPWIRE);

    private final String name;
    private final BlockRenderLayer[] layers;

    private BlockRenderLayerGroup(BlockRenderLayer ... layers) {
        this.layers = layers;
        this.name = this.toString().toLowerCase(Locale.ROOT);
    }

    public String getName() {
        return this.name;
    }

    public BlockRenderLayer[] getLayers() {
        return this.layers;
    }

    public Framebuffer getFramebuffer() {
        MinecraftClient lv = MinecraftClient.getInstance();
        Framebuffer lv2 = switch (this.ordinal()) {
            case 2 -> lv.worldRenderer.getWeatherFramebuffer();
            case 1 -> lv.worldRenderer.getTranslucentFramebuffer();
            default -> lv.getFramebuffer();
        };
        return lv2 != null ? lv2 : lv.getFramebuffer();
    }
}

