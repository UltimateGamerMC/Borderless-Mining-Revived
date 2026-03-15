/*
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/MinecartEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/MinecartEntityRenderState;
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.AbstractMinecartEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.MinecartEntityRenderState;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;

@Environment(value=EnvType.CLIENT)
public class MinecartEntityRenderer
extends AbstractMinecartEntityRenderer<AbstractMinecartEntity, MinecartEntityRenderState> {
    public MinecartEntityRenderer(EntityRendererFactory.Context arg, EntityModelLayer arg2) {
        super(arg, arg2);
    }

    @Override
    public MinecartEntityRenderState createRenderState() {
        return new MinecartEntityRenderState();
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

