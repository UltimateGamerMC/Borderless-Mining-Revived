/*
 * External method calls:
 *   Lnet/minecraft/client/render/block/entity/BlockEntityRendererFactory$Context;loadedEntityModels()Lnet/minecraft/client/render/entity/model/LoadedEntityModels;
 *   Lnet/minecraft/client/render/block/entity/BlockEntityRendererFactory$Context;spriteHolder()Lnet/minecraft/client/texture/SpriteHolder;
 *   Lnet/minecraft/client/render/item/model/special/SpecialModelRenderer$BakeContext;entityModelSet()Lnet/minecraft/client/render/entity/model/LoadedEntityModels;
 *   Lnet/minecraft/client/render/item/model/special/SpecialModelRenderer$BakeContext;spriteHolder()Lnet/minecraft/client/texture/SpriteHolder;
 *   Lnet/minecraft/client/render/block/entity/BlockEntityRenderer;updateRenderState(Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/client/render/block/entity/state/BlockEntityRenderState;FLnet/minecraft/util/math/Vec3d;Lnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V
 *   Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/RenderLayer;IIILnet/minecraft/client/texture/Sprite;ILnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V
 *   Lnet/minecraft/client/model/ModelPart;collectVertices(Lnet/minecraft/client/util/math/MatrixStack;Ljava/util/function/Consumer;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/block/entity/ShulkerBoxBlockEntityRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;IILnet/minecraft/util/math/Direction;FLnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;Lnet/minecraft/client/util/SpriteIdentifier;I)V
 *   Lnet/minecraft/client/render/block/entity/ShulkerBoxBlockEntityRenderer;render(Lnet/minecraft/client/render/block/entity/state/ShulkerBoxBlockEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V
 *   Lnet/minecraft/client/render/block/entity/ShulkerBoxBlockEntityRenderer;updateRenderState(Lnet/minecraft/block/entity/ShulkerBoxBlockEntity;Lnet/minecraft/client/render/block/entity/state/ShulkerBoxBlockEntityRenderState;FLnet/minecraft/util/math/Vec3d;Lnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V
 *   Lnet/minecraft/client/render/block/entity/ShulkerBoxBlockEntityRenderer;createRenderState()Lnet/minecraft/client/render/block/entity/state/ShulkerBoxBlockEntityRenderState;
 */
package net.minecraft.client.render.block.entity;

import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.block.entity.state.ShulkerBoxBlockEntityRenderState;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.item.model.special.SpecialModelRenderer;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.texture.SpriteHolder;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class ShulkerBoxBlockEntityRenderer
implements BlockEntityRenderer<ShulkerBoxBlockEntity, ShulkerBoxBlockEntityRenderState> {
    private final SpriteHolder materials;
    private final ShulkerBoxBlockModel model;

    public ShulkerBoxBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this(ctx.loadedEntityModels(), ctx.spriteHolder());
    }

    public ShulkerBoxBlockEntityRenderer(SpecialModelRenderer.BakeContext context) {
        this(context.entityModelSet(), context.spriteHolder());
    }

    public ShulkerBoxBlockEntityRenderer(LoadedEntityModels models, SpriteHolder materials) {
        this.materials = materials;
        this.model = new ShulkerBoxBlockModel(models.getModelPart(EntityModelLayers.SHULKER_BOX));
    }

    @Override
    public ShulkerBoxBlockEntityRenderState createRenderState() {
        return new ShulkerBoxBlockEntityRenderState();
    }

    @Override
    public void updateRenderState(ShulkerBoxBlockEntity arg, ShulkerBoxBlockEntityRenderState arg2, float f, Vec3d arg3,  @Nullable ModelCommandRenderer.CrumblingOverlayCommand arg4) {
        BlockEntityRenderer.super.updateRenderState(arg, arg2, f, arg3, arg4);
        arg2.facing = arg.getCachedState().get(ShulkerBoxBlock.FACING, Direction.UP);
        arg2.dyeColor = arg.getColor();
        arg2.animationProgress = arg.getAnimationProgress(f);
    }

    @Override
    public void render(ShulkerBoxBlockEntityRenderState arg, MatrixStack arg2, OrderedRenderCommandQueue arg3, CameraRenderState arg4) {
        DyeColor lv = arg.dyeColor;
        SpriteIdentifier lv2 = lv == null ? TexturedRenderLayers.SHULKER_TEXTURE_ID : TexturedRenderLayers.getShulkerBoxTextureId(lv);
        this.render(arg2, arg3, arg.lightmapCoordinates, OverlayTexture.DEFAULT_UV, arg.facing, arg.animationProgress, arg.crumblingOverlay, lv2, 0);
    }

    public void render(MatrixStack matrices, OrderedRenderCommandQueue queue, int light, int overlay, Direction facing, float openness,  @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay, SpriteIdentifier spriteId, int k) {
        matrices.push();
        this.setTransforms(matrices, facing, openness);
        queue.submitModel(this.model, Float.valueOf(openness), matrices, spriteId.getRenderLayer(this.model::getLayer), light, overlay, -1, this.materials.getSprite(spriteId), k, crumblingOverlay);
        matrices.pop();
    }

    private void setTransforms(MatrixStack matrices, Direction facing, float openness) {
        matrices.translate(0.5f, 0.5f, 0.5f);
        float g = 0.9995f;
        matrices.scale(0.9995f, 0.9995f, 0.9995f);
        matrices.multiply(facing.getRotationQuaternion());
        matrices.scale(1.0f, -1.0f, -1.0f);
        matrices.translate(0.0f, -1.0f, 0.0f);
        this.model.setAngles(Float.valueOf(openness));
    }

    public void collectVertices(Direction facing, float openness, Consumer<Vector3fc> consumer) {
        MatrixStack lv = new MatrixStack();
        this.setTransforms(lv, facing, openness);
        this.model.getRootPart().collectVertices(lv, consumer);
    }

    @Override
    public /* synthetic */ BlockEntityRenderState createRenderState() {
        return this.createRenderState();
    }

    @Environment(value=EnvType.CLIENT)
    static class ShulkerBoxBlockModel
    extends Model<Float> {
        private final ModelPart lid;

        public ShulkerBoxBlockModel(ModelPart root) {
            super(root, RenderLayers::entityCutoutNoCull);
            this.lid = root.getChild("lid");
        }

        @Override
        public void setAngles(Float float_) {
            super.setAngles(float_);
            this.lid.setOrigin(0.0f, 24.0f - float_.floatValue() * 0.5f * 16.0f, 0.0f);
            this.lid.yaw = 270.0f * float_.floatValue() * ((float)Math.PI / 180);
        }
    }
}

