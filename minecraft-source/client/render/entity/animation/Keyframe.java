package net.minecraft.client.render.entity.animation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.animation.Transformation;
import org.joml.Vector3fc;

@Environment(value=EnvType.CLIENT)
public record Keyframe(float timestamp, Vector3fc preTarget, Vector3fc postTarget, Transformation.Interpolation interpolation) {
    public Keyframe(float f, Vector3fc vector3fc, Transformation.Interpolation arg) {
        this(f, vector3fc, vector3fc, arg);
    }
}

