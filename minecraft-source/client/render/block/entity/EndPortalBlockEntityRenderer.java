/*
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/block/entity/EndPortalBlockEntityRenderer;createRenderState()Lnet/minecraft/client/render/block/entity/state/EndPortalBlockEntityRenderState;
 */
package net.minecraft.client.render.block.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.EndPortalBlockEntity;
import net.minecraft.client.render.block.entity.AbstractEndPortalBlockEntityRenderer;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.block.entity.state.EndPortalBlockEntityRenderState;

@Environment(value=EnvType.CLIENT)
public class EndPortalBlockEntityRenderer
extends AbstractEndPortalBlockEntityRenderer<EndPortalBlockEntity, EndPortalBlockEntityRenderState> {
    @Override
    public EndPortalBlockEntityRenderState createRenderState() {
        return new EndPortalBlockEntityRenderState();
    }

    @Override
    public /* synthetic */ BlockEntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

