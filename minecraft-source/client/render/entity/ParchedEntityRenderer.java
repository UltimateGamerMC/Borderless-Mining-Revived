/*
 * External method calls:
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/ParchedEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/SkeletonEntityRenderState;
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.AbstractSkeletonEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.SkeletonEntityRenderState;
import net.minecraft.entity.mob.ParchedEntity;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class ParchedEntityRenderer
extends AbstractSkeletonEntityRenderer<ParchedEntity, SkeletonEntityRenderState> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/skeleton/parched.png");

    public ParchedEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg, EntityModelLayers.PARCHED, EntityModelLayers.PARCHED_EQUIPMENT);
    }

    @Override
    public Identifier getTexture(SkeletonEntityRenderState arg) {
        return TEXTURE;
    }

    @Override
    public SkeletonEntityRenderState createRenderState() {
        return new SkeletonEntityRenderState();
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

