/*
 * External method calls:
 *   Lnet/minecraft/client/render/block/entity/BlockEntityRendererFactory$Context;loadedEntityModels()Lnet/minecraft/client/render/entity/model/LoadedEntityModels;
 *   Lnet/minecraft/client/render/block/entity/BlockEntityRendererFactory$Context;spriteHolder()Lnet/minecraft/client/texture/SpriteHolder;
 *   Lnet/minecraft/client/render/item/model/special/SpecialModelRenderer$BakeContext;entityModelSet()Lnet/minecraft/client/render/entity/model/LoadedEntityModels;
 *   Lnet/minecraft/client/render/item/model/special/SpecialModelRenderer$BakeContext;spriteHolder()Lnet/minecraft/client/texture/SpriteHolder;
 *   Lnet/minecraft/client/render/block/entity/BlockEntityRenderer;updateRenderState(Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/client/render/block/entity/state/BlockEntityRenderState;FLnet/minecraft/util/math/Vec3d;Lnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V
 *   Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/RenderLayer;IIILnet/minecraft/client/texture/Sprite;ILnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V
 *   Lnet/minecraft/client/render/RenderLayers;entityGlint()Lnet/minecraft/client/render/RenderLayer;
 *   Lnet/minecraft/component/type/BannerPatternsComponent;layers()Ljava/util/List;
 *   Lnet/minecraft/component/type/BannerPatternsComponent$Layer;pattern()Lnet/minecraft/registry/entry/RegistryEntry;
 *   Lnet/minecraft/component/type/BannerPatternsComponent$Layer;color()Lnet/minecraft/util/DyeColor;
 *   Lnet/minecraft/client/model/ModelPart;collectVertices(Lnet/minecraft/client/util/math/MatrixStack;Ljava/util/function/Consumer;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/block/entity/BannerBlockEntityRenderer;render(Lnet/minecraft/client/texture/SpriteHolder;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;IIFLnet/minecraft/client/render/block/entity/model/BannerBlockModel;Lnet/minecraft/client/render/block/entity/model/BannerFlagBlockModel;FLnet/minecraft/util/DyeColor;Lnet/minecraft/component/type/BannerPatternsComponent;Lnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;I)V
 *   Lnet/minecraft/client/render/block/entity/BannerBlockEntityRenderer;renderCanvas(Lnet/minecraft/client/texture/SpriteHolder;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;IILnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/client/util/SpriteIdentifier;ZLnet/minecraft/util/DyeColor;Lnet/minecraft/component/type/BannerPatternsComponent;ZLnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;I)V
 *   Lnet/minecraft/client/render/block/entity/BannerBlockEntityRenderer;renderLayer(Lnet/minecraft/client/texture/SpriteHolder;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;IILnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/client/util/SpriteIdentifier;Lnet/minecraft/util/DyeColor;Lnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V
 *   Lnet/minecraft/client/render/block/entity/BannerBlockEntityRenderer;render(Lnet/minecraft/client/render/block/entity/state/BannerBlockEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V
 *   Lnet/minecraft/client/render/block/entity/BannerBlockEntityRenderer;updateRenderState(Lnet/minecraft/block/entity/BannerBlockEntity;Lnet/minecraft/client/render/block/entity/state/BannerBlockEntityRenderState;FLnet/minecraft/util/math/Vec3d;Lnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V
 *   Lnet/minecraft/client/render/block/entity/BannerBlockEntityRenderer;createRenderState()Lnet/minecraft/client/render/block/entity/state/BannerBlockEntityRenderState;
 */
package net.minecraft.client.render.block.entity;

import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BannerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallBannerBlock;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.model.BannerBlockModel;
import net.minecraft.client.render.block.entity.model.BannerFlagBlockModel;
import net.minecraft.client.render.block.entity.state.BannerBlockEntityRenderState;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.item.model.special.SpecialModelRenderer;
import net.minecraft.client.render.model.ModelBaker;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.texture.SpriteHolder;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.RotationPropertyHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class BannerBlockEntityRenderer
implements BlockEntityRenderer<BannerBlockEntity, BannerBlockEntityRenderState> {
    private static final int ROTATIONS = 16;
    private static final float field_55282 = 0.6666667f;
    private final SpriteHolder materials;
    private final BannerBlockModel standingModel;
    private final BannerBlockModel wallModel;
    private final BannerFlagBlockModel standingFlagModel;
    private final BannerFlagBlockModel wallFlagModel;

    public BannerBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this(context.loadedEntityModels(), context.spriteHolder());
    }

    public BannerBlockEntityRenderer(SpecialModelRenderer.BakeContext context) {
        this(context.entityModelSet(), context.spriteHolder());
    }

    public BannerBlockEntityRenderer(LoadedEntityModels models, SpriteHolder materials) {
        this.materials = materials;
        this.standingModel = new BannerBlockModel(models.getModelPart(EntityModelLayers.STANDING_BANNER));
        this.wallModel = new BannerBlockModel(models.getModelPart(EntityModelLayers.WALL_BANNER));
        this.standingFlagModel = new BannerFlagBlockModel(models.getModelPart(EntityModelLayers.STANDING_BANNER_FLAG));
        this.wallFlagModel = new BannerFlagBlockModel(models.getModelPart(EntityModelLayers.WALL_BANNER_FLAG));
    }

    @Override
    public BannerBlockEntityRenderState createRenderState() {
        return new BannerBlockEntityRenderState();
    }

    @Override
    public void updateRenderState(BannerBlockEntity arg, BannerBlockEntityRenderState arg2, float f, Vec3d arg3,  @Nullable ModelCommandRenderer.CrumblingOverlayCommand arg4) {
        BlockEntityRenderer.super.updateRenderState(arg, arg2, f, arg3, arg4);
        arg2.dyeColor = arg.getColorForState();
        arg2.bannerPatterns = arg.getPatterns();
        BlockState lv = arg.getCachedState();
        if (lv.getBlock() instanceof BannerBlock) {
            arg2.yaw = -RotationPropertyHelper.toDegrees(lv.get(BannerBlock.ROTATION));
            arg2.standing = true;
        } else {
            arg2.yaw = -lv.get(WallBannerBlock.FACING).getPositiveHorizontalDegrees();
            arg2.standing = false;
        }
        long l = arg.getWorld() != null ? arg.getWorld().getTime() : 0L;
        BlockPos lv2 = arg.getPos();
        arg2.pitch = ((float)Math.floorMod((long)(lv2.getX() * 7 + lv2.getY() * 9 + lv2.getZ() * 13) + l, 100L) + f) / 100.0f;
    }

    @Override
    public void render(BannerBlockEntityRenderState arg, MatrixStack arg2, OrderedRenderCommandQueue arg3, CameraRenderState arg4) {
        BannerFlagBlockModel lv2;
        BannerBlockModel lv;
        if (arg.standing) {
            lv = this.standingModel;
            lv2 = this.standingFlagModel;
        } else {
            lv = this.wallModel;
            lv2 = this.wallFlagModel;
        }
        BannerBlockEntityRenderer.render(this.materials, arg2, arg3, arg.lightmapCoordinates, OverlayTexture.DEFAULT_UV, arg.yaw, lv, lv2, arg.pitch, arg.dyeColor, arg.bannerPatterns, arg.crumblingOverlay, 0);
    }

    public void renderAsItem(MatrixStack matrices, OrderedRenderCommandQueue queue, int light, int overlay, DyeColor baseColor, BannerPatternsComponent patterns, int k) {
        BannerBlockEntityRenderer.render(this.materials, matrices, queue, light, overlay, 0.0f, this.standingModel, this.standingFlagModel, 0.0f, baseColor, patterns, null, k);
    }

    private static void render(SpriteHolder materials, MatrixStack matrices, OrderedRenderCommandQueue queue, int light, int overlay, float yaw, BannerBlockModel model, BannerFlagBlockModel flagModel, float pitch, DyeColor dyeColor, BannerPatternsComponent bannerPatterns,  @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay, int k) {
        matrices.push();
        matrices.translate(0.5f, 0.0f, 0.5f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw));
        matrices.scale(0.6666667f, -0.6666667f, -0.6666667f);
        SpriteIdentifier lv = ModelBaker.BANNER_BASE;
        queue.submitModel(model, Unit.INSTANCE, matrices, lv.getRenderLayer(RenderLayers::entitySolid), light, overlay, -1, materials.getSprite(lv), k, crumblingOverlay);
        BannerBlockEntityRenderer.renderCanvas(materials, matrices, queue, light, overlay, flagModel, Float.valueOf(pitch), lv, true, dyeColor, bannerPatterns, false, crumblingOverlay, k);
        matrices.pop();
    }

    public static <S> void renderCanvas(SpriteHolder materials, MatrixStack matrices, OrderedRenderCommandQueue queue, int light, int overlay, Model<S> model, S state, SpriteIdentifier spriteId, boolean useBannerLayer, DyeColor color, BannerPatternsComponent patterns, boolean bl2,  @Nullable ModelCommandRenderer.CrumblingOverlayCommand arg8, int k) {
        queue.submitModel(model, state, matrices, spriteId.getRenderLayer(RenderLayers::entitySolid), light, overlay, -1, materials.getSprite(spriteId), k, arg8);
        if (bl2) {
            queue.submitModel(model, state, matrices, RenderLayers.entityGlint(), light, overlay, -1, materials.getSprite(spriteId), 0, arg8);
        }
        BannerBlockEntityRenderer.renderLayer(materials, matrices, queue, light, overlay, model, state, useBannerLayer ? TexturedRenderLayers.BANNER_BASE : TexturedRenderLayers.SHIELD_BASE, color, arg8);
        for (int l = 0; l < 16 && l < patterns.layers().size(); ++l) {
            BannerPatternsComponent.Layer lv = patterns.layers().get(l);
            SpriteIdentifier lv2 = useBannerLayer ? TexturedRenderLayers.getBannerPatternTextureId(lv.pattern()) : TexturedRenderLayers.getShieldPatternTextureId(lv.pattern());
            BannerBlockEntityRenderer.renderLayer(materials, matrices, queue, light, overlay, model, state, lv2, lv.color(), null);
        }
    }

    private static <S> void renderLayer(SpriteHolder materials, MatrixStack matrices, OrderedRenderCommandQueue queue, int light, int overlay, Model<S> model, S state, SpriteIdentifier spriteId, DyeColor color,  @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
        int k = color.getEntityColor();
        queue.submitModel(model, state, matrices, spriteId.getRenderLayer(RenderLayers::entityNoOutline), light, overlay, k, materials.getSprite(spriteId), 0, crumblingOverlay);
    }

    public void collectVertices(Consumer<Vector3fc> consumer) {
        MatrixStack lv = new MatrixStack();
        lv.translate(0.5f, 0.0f, 0.5f);
        lv.scale(0.6666667f, -0.6666667f, -0.6666667f);
        this.standingModel.getRootPart().collectVertices(lv, consumer);
        this.standingFlagModel.setAngles(Float.valueOf(0.0f));
        this.standingFlagModel.getRootPart().collectVertices(lv, consumer);
    }

    @Override
    public /* synthetic */ BlockEntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

