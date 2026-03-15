package net.minecraft.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Vector3fc;

@Environment(value=EnvType.CLIENT)
public record CameraOverride(Vector3fc forwardVector) {
}

