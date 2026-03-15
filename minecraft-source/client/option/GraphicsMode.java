/*
 * External method calls:
 *   Lnet/minecraft/client/gui/screen/option/GameOptionsScreen;update(Lnet/minecraft/client/option/SimpleOption;)V
 *   Lnet/minecraft/util/StringIdentifiable;createCodec(Ljava/util/function/Supplier;)Lnet/minecraft/util/StringIdentifiable$EnumCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/option/GraphicsMode;applyOption(Lnet/minecraft/client/gui/screen/option/GameOptionsScreen;Lnet/minecraft/client/option/SimpleOption;Ljava/lang/Object;)V
 *   Lnet/minecraft/client/option/GraphicsMode;method_36861()[Lnet/minecraft/client/option/GraphicsMode;
 */
package net.minecraft.client.option;

import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.GpuDeviceInfo;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.option.TextureFilteringMode;
import net.minecraft.client.render.ChunkBuilderMode;
import net.minecraft.particle.ParticlesMode;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public enum GraphicsMode implements StringIdentifiable
{
    FAST("fast", "options.graphics.fast"),
    FANCY("fancy", "options.graphics.fancy"),
    FABULOUS("fabulous", "options.graphics.fabulous"),
    CUSTOM("custom", "options.graphics.custom");

    private final String name;
    private final String translationKey;
    public static final Codec<GraphicsMode> CODEC;

    private GraphicsMode(String name, String translationKey) {
        this.name = name;
        this.translationKey = translationKey;
    }

    @Override
    public String asString() {
        return this.name;
    }

    public String getTranslationKey() {
        return this.translationKey;
    }

    public void apply(MinecraftClient client) {
        GameOptionsScreen lv = client.currentScreen instanceof GameOptionsScreen ? (GameOptionsScreen)client.currentScreen : null;
        GpuDevice gpuDevice = RenderSystem.getDevice();
        switch (this.ordinal()) {
            case 0: {
                int i = 8;
                this.applyOption(lv, client.options.getBiomeBlendRadius(), 1);
                this.applyOption(lv, client.options.getViewDistance(), 8);
                this.applyOption(lv, client.options.getChunkBuilderMode(), ChunkBuilderMode.NONE);
                this.applyOption(lv, client.options.getSimulationDistance(), 6);
                this.applyOption(lv, client.options.getAo(), false);
                this.applyOption(lv, client.options.getCloudRenderMode(), CloudRenderMode.FAST);
                this.applyOption(lv, client.options.getParticles(), ParticlesMode.DECREASED);
                this.applyOption(lv, client.options.getMipmapLevels(), 2);
                this.applyOption(lv, client.options.getEntityShadows(), false);
                this.applyOption(lv, client.options.getEntityDistanceScaling(), 0.75);
                this.applyOption(lv, client.options.getMenuBackgroundBlurriness(), 2);
                this.applyOption(lv, client.options.getCloudRenderDistance(), 32);
                this.applyOption(lv, client.options.getCutoutLeaves(), false);
                this.applyOption(lv, client.options.getImprovedTransparency(), false);
                this.applyOption(lv, client.options.getWeatherRadius(), 5);
                this.applyOption(lv, client.options.getMaxAnisotropy(), 1);
                this.applyOption(lv, client.options.getTextureFiltering(), TextureFilteringMode.NONE);
                break;
            }
            case 1: {
                int i = 16;
                this.applyOption(lv, client.options.getBiomeBlendRadius(), 2);
                this.applyOption(lv, client.options.getViewDistance(), 16);
                this.applyOption(lv, client.options.getChunkBuilderMode(), ChunkBuilderMode.PLAYER_AFFECTED);
                this.applyOption(lv, client.options.getSimulationDistance(), 12);
                this.applyOption(lv, client.options.getAo(), true);
                this.applyOption(lv, client.options.getCloudRenderMode(), CloudRenderMode.FANCY);
                this.applyOption(lv, client.options.getParticles(), ParticlesMode.ALL);
                this.applyOption(lv, client.options.getMipmapLevels(), 4);
                this.applyOption(lv, client.options.getEntityShadows(), true);
                this.applyOption(lv, client.options.getEntityDistanceScaling(), 1.0);
                this.applyOption(lv, client.options.getMenuBackgroundBlurriness(), 5);
                this.applyOption(lv, client.options.getCloudRenderDistance(), 64);
                this.applyOption(lv, client.options.getCutoutLeaves(), true);
                this.applyOption(lv, client.options.getImprovedTransparency(), false);
                this.applyOption(lv, client.options.getWeatherRadius(), 10);
                this.applyOption(lv, client.options.getMaxAnisotropy(), 1);
                this.applyOption(lv, client.options.getTextureFiltering(), TextureFilteringMode.RGSS);
                break;
            }
            case 2: {
                int i = 32;
                this.applyOption(lv, client.options.getBiomeBlendRadius(), 2);
                this.applyOption(lv, client.options.getViewDistance(), 32);
                this.applyOption(lv, client.options.getChunkBuilderMode(), ChunkBuilderMode.PLAYER_AFFECTED);
                this.applyOption(lv, client.options.getSimulationDistance(), 12);
                this.applyOption(lv, client.options.getAo(), true);
                this.applyOption(lv, client.options.getCloudRenderMode(), CloudRenderMode.FANCY);
                this.applyOption(lv, client.options.getParticles(), ParticlesMode.ALL);
                this.applyOption(lv, client.options.getMipmapLevels(), 4);
                this.applyOption(lv, client.options.getEntityShadows(), true);
                this.applyOption(lv, client.options.getEntityDistanceScaling(), 1.25);
                this.applyOption(lv, client.options.getMenuBackgroundBlurriness(), 5);
                this.applyOption(lv, client.options.getCloudRenderDistance(), 128);
                this.applyOption(lv, client.options.getCutoutLeaves(), true);
                this.applyOption(lv, client.options.getImprovedTransparency(), Util.getOperatingSystem() != Util.OperatingSystem.OSX);
                this.applyOption(lv, client.options.getWeatherRadius(), 10);
                this.applyOption(lv, client.options.getMaxAnisotropy(), 2);
                if (GpuDeviceInfo.get(gpuDevice).shouldUseRgssOnFabulous()) {
                    this.applyOption(lv, client.options.getTextureFiltering(), TextureFilteringMode.RGSS);
                    break;
                }
                this.applyOption(lv, client.options.getTextureFiltering(), TextureFilteringMode.ANISOTROPIC);
            }
        }
    }

    <T> void applyOption(@Nullable GameOptionsScreen screen, SimpleOption<T> option, T value) {
        if (option.getValue() != value) {
            option.setValue(value);
            if (screen != null) {
                screen.update(option);
            }
        }
    }

    static {
        CODEC = StringIdentifiable.createCodec(GraphicsMode::values);
    }
}

