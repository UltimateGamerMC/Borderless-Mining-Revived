/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/IllagerEntityRenderer;updateRenderState(Lnet/minecraft/entity/mob/IllagerEntity;Lnet/minecraft/client/render/entity/state/IllagerEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/IllagerEntityRenderer;render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/IllusionerEntityRenderer;addFeature(Lnet/minecraft/client/render/entity/feature/FeatureRenderer;)Z
 *   Lnet/minecraft/client/render/entity/IllusionerEntityRenderer;updateRenderState(Lnet/minecraft/entity/mob/IllusionerEntity;Lnet/minecraft/client/render/entity/state/IllusionerEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/IllusionerEntityRenderer;render(Lnet/minecraft/client/render/entity/state/IllusionerEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V
 *   Lnet/minecraft/client/render/entity/IllusionerEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/IllusionerEntityRenderState;
 */
package net.minecraft.client.render.entity;

import java.util.Arrays;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.IllagerEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.IllagerEntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.IllusionerEntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.IllusionerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@Environment(value=EnvType.CLIENT)
public class IllusionerEntityRenderer
extends IllagerEntityRenderer<IllusionerEntity, IllusionerEntityRenderState> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/illager/illusioner.png");

    public IllusionerEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg, new IllagerEntityModel(arg.getPart(EntityModelLayers.ILLUSIONER)), 0.5f);
        this.addFeature(new HeldItemFeatureRenderer<IllusionerEntityRenderState, IllagerEntityModel<IllusionerEntityRenderState>>(this, (FeatureRendererContext)this){

            @Override
            public void render(MatrixStack arg, OrderedRenderCommandQueue arg2, int i, IllusionerEntityRenderState arg3, float f, float g) {
                if (arg3.spellcasting || arg3.attacking) {
                    super.render(arg, arg2, i, arg3, f, g);
                }
            }
        });
        ((IllagerEntityModel)this.model).getHat().visible = true;
    }

    @Override
    public Identifier getTexture(IllusionerEntityRenderState arg) {
        return TEXTURE;
    }

    @Override
    public IllusionerEntityRenderState createRenderState() {
        return new IllusionerEntityRenderState();
    }

    @Override
    public void updateRenderState(IllusionerEntity arg, IllusionerEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        Vec3d[] lvs = arg.getMirrorCopyOffsets(f);
        arg2.mirrorCopyOffsets = Arrays.copyOf(lvs, lvs.length);
        arg2.spellcasting = arg.isSpellcasting();
    }

    @Override
    public void render(IllusionerEntityRenderState arg, MatrixStack arg2, OrderedRenderCommandQueue arg3, CameraRenderState arg4) {
        if (arg.invisible) {
            Vec3d[] lvs = arg.mirrorCopyOffsets;
            for (int i = 0; i < lvs.length; ++i) {
                arg2.push();
                arg2.translate(lvs[i].x + (double)MathHelper.cos((float)i + arg.age * 0.5f) * 0.025, lvs[i].y + (double)MathHelper.cos((float)i + arg.age * 0.75f) * 0.0125, lvs[i].z + (double)MathHelper.cos((float)i + arg.age * 0.7f) * 0.025);
                super.render(arg, arg2, arg3, arg4);
                arg2.pop();
            }
        } else {
            super.render(arg, arg2, arg3, arg4);
        }
    }

    @Override
    protected boolean isVisible(IllusionerEntityRenderState arg) {
        return true;
    }

    @Override
    protected Box getBoundingBox(IllusionerEntity arg) {
        return super.getBoundingBox(arg).expand(3.0, 0.0, 3.0);
    }

    @Override
    protected /* synthetic */ boolean isVisible(LivingEntityRenderState state) {
        return this.isVisible((IllusionerEntityRenderState)state);
    }

    @Override
    public /* synthetic */ Identifier getTexture(LivingEntityRenderState state) {
        return this.getTexture((IllusionerEntityRenderState)state);
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

