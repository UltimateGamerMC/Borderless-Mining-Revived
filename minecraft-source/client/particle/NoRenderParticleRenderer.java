package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.ParticleRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.Submittable;

@Environment(value=EnvType.CLIENT)
public class NoRenderParticleRenderer
extends ParticleRenderer<NoRenderParticle> {
    private static final Submittable EMPTY = (queue, cameraRenderState) -> {};

    public NoRenderParticleRenderer(ParticleManager arg) {
        super(arg);
    }

    @Override
    public Submittable render(Frustum frustum, Camera camera, float tickProgress) {
        return EMPTY;
    }
}

