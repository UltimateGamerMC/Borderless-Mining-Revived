package net.minecraft.client.render.fog;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.fog.FogData;
import net.minecraft.client.render.fog.FogModifier;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.world.attribute.EnvironmentAttributes;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class WaterFogModifier
extends FogModifier {
    @Override
    public void applyStartEndModifier(FogData data, Camera arg2, ClientWorld arg3, float f, RenderTickCounter arg4) {
        float g = arg4.getTickProgress(false);
        data.environmentalStart = arg2.getEnvironmentAttributeInterpolator().get(EnvironmentAttributes.WATER_FOG_START_DISTANCE_VISUAL, g).floatValue();
        data.environmentalEnd = arg2.getEnvironmentAttributeInterpolator().get(EnvironmentAttributes.WATER_FOG_END_DISTANCE_VISUAL, g).floatValue();
        Entity entity = arg2.getFocusedEntity();
        if (entity instanceof ClientPlayerEntity) {
            ClientPlayerEntity lv = (ClientPlayerEntity)entity;
            data.environmentalEnd *= Math.max(0.25f, lv.getUnderwaterVisibility());
        }
        data.skyEnd = data.environmentalEnd;
        data.cloudEnd = data.environmentalEnd;
    }

    @Override
    public boolean shouldApply(@Nullable CameraSubmersionType submersionType, Entity cameraEntity) {
        return submersionType == CameraSubmersionType.WATER;
    }

    @Override
    public int getFogColor(ClientWorld world, Camera camera, int viewDistance, float skyDarkness) {
        return camera.getEnvironmentAttributeInterpolator().get(EnvironmentAttributes.WATER_FOG_COLOR_VISUAL, skyDarkness);
    }
}

