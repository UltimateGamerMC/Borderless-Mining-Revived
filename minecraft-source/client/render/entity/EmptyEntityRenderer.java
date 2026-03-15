package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.Entity;

@Environment(value=EnvType.CLIENT)
public class EmptyEntityRenderer<T extends Entity>
extends EntityRenderer<T, EntityRenderState> {
    public EmptyEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg);
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }
}

