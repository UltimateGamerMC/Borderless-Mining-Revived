package net.minecraft.client.render.fog;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.fog.FogData;
import net.minecraft.client.render.fog.FogModifier;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class PowderSnowFogModifier
extends FogModifier {
    private static final int FOG_COLOR = -6308916;

    @Override
    public int getFogColor(ClientWorld world, Camera camera, int viewDistance, float skyDarkness) {
        return -6308916;
    }

    @Override
    public void applyStartEndModifier(FogData data, Camera arg2, ClientWorld arg3, float f, RenderTickCounter arg4) {
        if (arg2.getFocusedEntity().isSpectator()) {
            data.environmentalStart = -8.0f;
            data.environmentalEnd = f * 0.5f;
        } else {
            data.environmentalStart = 0.0f;
            data.environmentalEnd = 2.0f;
        }
        data.skyEnd = data.environmentalEnd;
        data.cloudEnd = data.environmentalEnd;
    }

    @Override
    public boolean shouldApply(@Nullable CameraSubmersionType submersionType, Entity cameraEntity) {
        return submersionType == CameraSubmersionType.POWDER_SNOW;
    }
}

