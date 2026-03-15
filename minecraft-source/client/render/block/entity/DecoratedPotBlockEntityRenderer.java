/*
 * External method calls:
 *   Lnet/minecraft/client/render/block/entity/BlockEntityRendererFactory$Context;loadedEntityModels()Lnet/minecraft/client/render/entity/model/LoadedEntityModels;
 *   Lnet/minecraft/client/render/block/entity/BlockEntityRendererFactory$Context;spriteHolder()Lnet/minecraft/client/texture/SpriteHolder;
 *   Lnet/minecraft/client/render/item/model/special/SpecialModelRenderer$BakeContext;entityModelSet()Lnet/minecraft/client/render/entity/model/LoadedEntityModels;
 *   Lnet/minecraft/client/render/item/model/special/SpecialModelRenderer$BakeContext;spriteHolder()Lnet/minecraft/client/texture/SpriteHolder;
 *   Lnet/minecraft/client/model/ModelPartBuilder;create()Lnet/minecraft/client/model/ModelPartBuilder;
 *   Lnet/minecraft/client/model/ModelPartBuilder;uv(II)Lnet/minecraft/client/model/ModelPartBuilder;
 *   Lnet/minecraft/client/model/ModelPartBuilder;cuboid(FFFFFFLnet/minecraft/client/model/Dilation;)Lnet/minecraft/client/model/ModelPartBuilder;
 *   Lnet/minecraft/client/model/ModelTransform;of(FFFFFF)Lnet/minecraft/client/model/ModelTransform;
 *   Lnet/minecraft/client/model/ModelPartData;addChild(Ljava/lang/String;Lnet/minecraft/client/model/ModelPartBuilder;Lnet/minecraft/client/model/ModelTransform;)Lnet/minecraft/client/model/ModelPartData;
 *   Lnet/minecraft/client/model/ModelPartBuilder;cuboid(FFFFFF)Lnet/minecraft/client/model/ModelPartBuilder;
 *   Lnet/minecraft/client/model/TexturedModelData;of(Lnet/minecraft/client/model/ModelData;II)Lnet/minecraft/client/model/TexturedModelData;
 *   Lnet/minecraft/client/model/ModelPartBuilder;cuboid(FFFFFFLjava/util/Set;)Lnet/minecraft/client/model/ModelPartBuilder;
 *   Lnet/minecraft/block/DecoratedPotPatterns;fromSherd(Lnet/minecraft/item/Item;)Lnet/minecraft/registry/RegistryKey;
 *   Lnet/minecraft/client/render/block/entity/BlockEntityRenderer;updateRenderState(Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/client/render/block/entity/state/BlockEntityRenderState;FLnet/minecraft/util/math/Vec3d;Lnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V
 *   Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitModelPart(Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/RenderLayer;IILnet/minecraft/client/texture/Sprite;ZZILnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;I)V
 *   Lnet/minecraft/block/entity/Sherds;front()Ljava/util/Optional;
 *   Lnet/minecraft/block/entity/Sherds;back()Ljava/util/Optional;
 *   Lnet/minecraft/block/entity/Sherds;left()Ljava/util/Optional;
 *   Lnet/minecraft/block/entity/Sherds;right()Ljava/util/Optional;
 *   Lnet/minecraft/client/model/ModelPart;collectVertices(Lnet/minecraft/client/util/math/MatrixStack;Ljava/util/function/Consumer;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/block/entity/DecoratedPotBlockEntityRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;IILnet/minecraft/block/entity/Sherds;I)V
 *   Lnet/minecraft/client/render/block/entity/DecoratedPotBlockEntityRenderer;render(Lnet/minecraft/client/render/block/entity/state/DecoratedPotBlockEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V
 *   Lnet/minecraft/client/render/block/entity/DecoratedPotBlockEntityRenderer;updateRenderState(Lnet/minecraft/block/entity/DecoratedPotBlockEntity;Lnet/minecraft/client/render/block/entity/state/DecoratedPotBlockEntityRenderState;FLnet/minecraft/util/math/Vec3d;Lnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V
 *   Lnet/minecraft/client/render/block/entity/DecoratedPotBlockEntityRenderer;createRenderState()Lnet/minecraft/client/render/block/entity/state/DecoratedPotBlockEntityRenderState;
 */
package net.minecraft.client.render.block.entity;

import java.util.EnumSet;
import java.util.Optional;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.DecoratedPotPatterns;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.block.entity.Sherds;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.block.entity.state.DecoratedPotBlockEntityRenderState;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.item.model.special.SpecialModelRenderer;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteHolder;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class DecoratedPotBlockEntityRenderer
implements BlockEntityRenderer<DecoratedPotBlockEntity, DecoratedPotBlockEntityRenderState> {
    private final SpriteHolder materials;
    private static final String NECK = "neck";
    private static final String FRONT = "front";
    private static final String BACK = "back";
    private static final String LEFT = "left";
    private static final String RIGHT = "right";
    private static final String TOP = "top";
    private static final String BOTTOM = "bottom";
    private final ModelPart neck;
    private final ModelPart front;
    private final ModelPart back;
    private final ModelPart left;
    private final ModelPart right;
    private final ModelPart top;
    private final ModelPart bottom;
    private static final float field_46728 = 0.125f;

    public DecoratedPotBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this(context.loadedEntityModels(), context.spriteHolder());
    }

    public DecoratedPotBlockEntityRenderer(SpecialModelRenderer.BakeContext context) {
        this(context.entityModelSet(), context.spriteHolder());
    }

    public DecoratedPotBlockEntityRenderer(LoadedEntityModels entityModelSet, SpriteHolder materials) {
        this.materials = materials;
        ModelPart lv = entityModelSet.getModelPart(EntityModelLayers.DECORATED_POT_BASE);
        this.neck = lv.getChild(EntityModelPartNames.NECK);
        this.top = lv.getChild(TOP);
        this.bottom = lv.getChild(EntityModelPartNames.BOTTOM);
        ModelPart lv2 = entityModelSet.getModelPart(EntityModelLayers.DECORATED_POT_SIDES);
        this.front = lv2.getChild(FRONT);
        this.back = lv2.getChild(BACK);
        this.left = lv2.getChild(LEFT);
        this.right = lv2.getChild(RIGHT);
    }

    public static TexturedModelData getTopBottomNeckTexturedModelData() {
        ModelData lv = new ModelData();
        ModelPartData lv2 = lv.getRoot();
        Dilation lv3 = new Dilation(0.2f);
        Dilation lv4 = new Dilation(-0.1f);
        lv2.addChild(EntityModelPartNames.NECK, ModelPartBuilder.create().uv(0, 0).cuboid(4.0f, 17.0f, 4.0f, 8.0f, 3.0f, 8.0f, lv4).uv(0, 5).cuboid(5.0f, 20.0f, 5.0f, 6.0f, 1.0f, 6.0f, lv3), ModelTransform.of(0.0f, 37.0f, 16.0f, (float)Math.PI, 0.0f, 0.0f));
        ModelPartBuilder lv5 = ModelPartBuilder.create().uv(-14, 13).cuboid(0.0f, 0.0f, 0.0f, 14.0f, 0.0f, 14.0f);
        lv2.addChild(TOP, lv5, ModelTransform.of(1.0f, 16.0f, 1.0f, 0.0f, 0.0f, 0.0f));
        lv2.addChild(EntityModelPartNames.BOTTOM, lv5, ModelTransform.of(1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f));
        return TexturedModelData.of(lv, 32, 32);
    }

    public static TexturedModelData getSidesTexturedModelData() {
        ModelData lv = new ModelData();
        ModelPartData lv2 = lv.getRoot();
        ModelPartBuilder lv3 = ModelPartBuilder.create().uv(1, 0).cuboid(0.0f, 0.0f, 0.0f, 14.0f, 16.0f, 0.0f, EnumSet.of(Direction.NORTH));
        lv2.addChild(BACK, lv3, ModelTransform.of(15.0f, 16.0f, 1.0f, 0.0f, 0.0f, (float)Math.PI));
        lv2.addChild(LEFT, lv3, ModelTransform.of(1.0f, 16.0f, 1.0f, 0.0f, -1.5707964f, (float)Math.PI));
        lv2.addChild(RIGHT, lv3, ModelTransform.of(15.0f, 16.0f, 15.0f, 0.0f, 1.5707964f, (float)Math.PI));
        lv2.addChild(FRONT, lv3, ModelTransform.of(1.0f, 16.0f, 15.0f, (float)Math.PI, 0.0f, 0.0f));
        return TexturedModelData.of(lv, 16, 16);
    }

    private static SpriteIdentifier getTextureIdFromSherd(Optional<Item> sherd) {
        SpriteIdentifier lv;
        if (sherd.isPresent() && (lv = TexturedRenderLayers.getDecoratedPotPatternTextureId(DecoratedPotPatterns.fromSherd(sherd.get()))) != null) {
            return lv;
        }
        return TexturedRenderLayers.DECORATED_POT_SIDE;
    }

    @Override
    public DecoratedPotBlockEntityRenderState createRenderState() {
        return new DecoratedPotBlockEntityRenderState();
    }

    @Override
    public void updateRenderState(DecoratedPotBlockEntity arg, DecoratedPotBlockEntityRenderState arg2, float f, Vec3d arg3,  @Nullable ModelCommandRenderer.CrumblingOverlayCommand arg4) {
        BlockEntityRenderer.super.updateRenderState(arg, arg2, f, arg3, arg4);
        arg2.sherds = arg.getSherds();
        arg2.facing = arg.getHorizontalFacing();
        DecoratedPotBlockEntity.WobbleType lv = arg.lastWobbleType;
        arg2.wobbleAnimationProgress = lv != null && arg.getWorld() != null ? ((float)(arg.getWorld().getTime() - arg.lastWobbleTime) + f) / (float)lv.lengthInTicks : 0.0f;
    }

    @Override
    public void render(DecoratedPotBlockEntityRenderState arg, MatrixStack arg2, OrderedRenderCommandQueue arg3, CameraRenderState arg4) {
        arg2.push();
        Direction lv = arg.facing;
        arg2.translate(0.5, 0.0, 0.5);
        arg2.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f - lv.getPositiveHorizontalDegrees()));
        arg2.translate(-0.5, 0.0, -0.5);
        if (arg.wobbleAnimationProgress >= 0.0f && arg.wobbleAnimationProgress <= 1.0f) {
            if (arg.wobbleType == DecoratedPotBlockEntity.WobbleType.POSITIVE) {
                float f = 0.015625f;
                float g = arg.wobbleAnimationProgress * ((float)Math.PI * 2);
                float h = -1.5f * (MathHelper.cos(g) + 0.5f) * MathHelper.sin(g / 2.0f);
                arg2.multiply(RotationAxis.POSITIVE_X.rotation(h * 0.015625f), 0.5f, 0.0f, 0.5f);
                float i = MathHelper.sin(g);
                arg2.multiply(RotationAxis.POSITIVE_Z.rotation(i * 0.015625f), 0.5f, 0.0f, 0.5f);
            } else {
                float f = MathHelper.sin(-arg.wobbleAnimationProgress * 3.0f * (float)Math.PI) * 0.125f;
                float g = 1.0f - arg.wobbleAnimationProgress;
                arg2.multiply(RotationAxis.POSITIVE_Y.rotation(f * g), 0.5f, 0.0f, 0.5f);
            }
        }
        this.render(arg2, arg3, arg.lightmapCoordinates, OverlayTexture.DEFAULT_UV, arg.sherds, 0);
        arg2.pop();
    }

    public void render(MatrixStack matrices, OrderedRenderCommandQueue queue, int light, int overlay, Sherds sherds, int k) {
        RenderLayer lv = TexturedRenderLayers.DECORATED_POT_BASE.getRenderLayer(RenderLayers::entitySolid);
        Sprite lv2 = this.materials.getSprite(TexturedRenderLayers.DECORATED_POT_BASE);
        queue.submitModelPart(this.neck, matrices, lv, light, overlay, lv2, false, false, -1, null, k);
        queue.submitModelPart(this.top, matrices, lv, light, overlay, lv2, false, false, -1, null, k);
        queue.submitModelPart(this.bottom, matrices, lv, light, overlay, lv2, false, false, -1, null, k);
        SpriteIdentifier lv3 = DecoratedPotBlockEntityRenderer.getTextureIdFromSherd(sherds.front());
        queue.submitModelPart(this.front, matrices, lv3.getRenderLayer(RenderLayers::entitySolid), light, overlay, this.materials.getSprite(lv3), false, false, -1, null, k);
        SpriteIdentifier lv4 = DecoratedPotBlockEntityRenderer.getTextureIdFromSherd(sherds.back());
        queue.submitModelPart(this.back, matrices, lv4.getRenderLayer(RenderLayers::entitySolid), light, overlay, this.materials.getSprite(lv4), false, false, -1, null, k);
        SpriteIdentifier lv5 = DecoratedPotBlockEntityRenderer.getTextureIdFromSherd(sherds.left());
        queue.submitModelPart(this.left, matrices, lv5.getRenderLayer(RenderLayers::entitySolid), light, overlay, this.materials.getSprite(lv5), false, false, -1, null, k);
        SpriteIdentifier lv6 = DecoratedPotBlockEntityRenderer.getTextureIdFromSherd(sherds.right());
        queue.submitModelPart(this.right, matrices, lv6.getRenderLayer(RenderLayers::entitySolid), light, overlay, this.materials.getSprite(lv6), false, false, -1, null, k);
    }

    public void collectVertices(Consumer<Vector3fc> consumer) {
        MatrixStack lv = new MatrixStack();
        this.neck.collectVertices(lv, consumer);
        this.top.collectVertices(lv, consumer);
        this.bottom.collectVertices(lv, consumer);
    }

    @Override
    public /* synthetic */ BlockEntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

