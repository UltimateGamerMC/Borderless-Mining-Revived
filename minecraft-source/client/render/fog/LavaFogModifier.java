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
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class LavaFogModifier
extends FogModifier {
    private static final int COLOR = -6743808;

    @Override
    public int getFogColor(ClientWorld world, Camera camera, int viewDistance, float skyDarkness) {
        return -6743808;
    }

    @Override
    public void applyStartEndModifier(FogData data, Camera arg2, ClientWorld arg3, float f, RenderTickCounter arg4) {
        if (arg2.getFocusedEntity().isSpectator()) {
            data.environmentalStart = -8.0f;
            data.environmentalEnd = f * 0.5f;
        } else {
            LivingEntity lv;
            Entity entity = arg2.getFocusedEntity();
            if (entity instanceof LivingEntity && (lv = (LivingEntity)entity).hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) {
                data.environmentalStart = 0.0f;
                data.environmentalEnd = 5.0f;
            } else {
                data.environmentalStart = 0.25f;
                data.environmentalEnd = 1.0f;
            }
        }
        data.skyEnd = data.environmentalEnd;
        data.cloudEnd = data.environmentalEnd;
    }

    @Override
    public boolean shouldApply(@Nullable CameraSubmersionType submersionType, Entity cameraEntity) {
        return submersionType == CameraSubmersionType.LAVA;
    }
}

