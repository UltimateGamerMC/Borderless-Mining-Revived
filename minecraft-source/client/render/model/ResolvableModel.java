package net.minecraft.client.render.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public interface ResolvableModel {
    public void resolve(Resolver var1);

    @Environment(value=EnvType.CLIENT)
    public static interface Resolver {
        public void markDependency(Identifier var1);
    }
}

