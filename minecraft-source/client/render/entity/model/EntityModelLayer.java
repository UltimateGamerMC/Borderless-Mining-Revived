package net.minecraft.client.render.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public record EntityModelLayer(Identifier id, String name) {
    @Override
    public String toString() {
        return String.valueOf(this.id) + "#" + this.name;
    }
}

