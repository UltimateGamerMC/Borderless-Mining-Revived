package net.minecraft.client.render.fog;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.fog.FogData;
import net.minecraft.client.render.fog.StatusEffectFogModifier;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.MathHelper;

@Environment(value=EnvType.CLIENT)
public class DarknessEffectFogModifier
extends StatusEffectFogModifier {
    @Override
    public RegistryEntry<StatusEffect> getStatusEffect() {
        return StatusEffects.DARKNESS;
    }

    @Override
    public void applyStartEndModifier(FogData data, Camera arg2, ClientWorld arg3, float f, RenderTickCounter arg4) {
        LivingEntity lv;
        StatusEffectInstance lv2;
        Entity entity = arg2.getFocusedEntity();
        if (entity instanceof LivingEntity && (lv2 = (lv = (LivingEntity)entity).getStatusEffect(this.getStatusEffect())) != null) {
            float g = MathHelper.lerp(lv2.getFadeFactor(lv, arg4.getTickProgress(false)), f, 15.0f);
            data.environmentalStart = g * 0.75f;
            data.environmentalEnd = g;
            data.skyEnd = g;
            data.cloudEnd = g;
        }
    }

    @Override
    public float applyDarknessModifier(LivingEntity cameraEntity, float darkness, float tickProgress) {
        StatusEffectInstance lv = cameraEntity.getStatusEffect(this.getStatusEffect());
        return lv != null ? Math.max(lv.getFadeFactor(cameraEntity, tickProgress), darkness) : darkness;
    }
}

