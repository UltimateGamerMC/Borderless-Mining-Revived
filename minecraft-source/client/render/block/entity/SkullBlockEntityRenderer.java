/*
 * External method calls:
 *   Lnet/minecraft/client/render/block/entity/BlockEntityRendererFactory$Context;loadedEntityModels()Lnet/minecraft/client/render/entity/model/LoadedEntityModels;
 *   Lnet/minecraft/client/render/block/entity/BlockEntityRendererFactory$Context;playerSkinRenderCache()Lnet/minecraft/client/texture/PlayerSkinCache;
 *   Lnet/minecraft/util/Util;memoize(Ljava/util/function/Function;)Ljava/util/function/Function;
 *   Lnet/minecraft/client/render/block/entity/BlockEntityRenderer;updateRenderState(Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/client/render/block/entity/state/BlockEntityRenderState;FLnet/minecraft/util/math/Vec3d;Lnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V
 *   Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/RenderLayer;IIILnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V
 *   Lnet/minecraft/client/render/RenderLayers;entityCutoutNoCullZOffset(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;
 *   Lnet/minecraft/client/render/RenderLayers;entityTranslucent(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/util/Util;make(Ljava/lang/Object;Ljava/util/function/Consumer;)Ljava/lang/Object;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/block/entity/SkullBlockEntityRenderer;renderSkull(Lnet/minecraft/block/SkullBlock$SkullType;Lnet/minecraft/block/entity/SkullBlockEntity;)Lnet/minecraft/client/render/RenderLayer;
 *   Lnet/minecraft/client/render/block/entity/SkullBlockEntityRenderer;render(Lnet/minecraft/util/math/Direction;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/client/render/block/entity/SkullBlockEntityModel;Lnet/minecraft/client/render/RenderLayer;ILnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V
 *   Lnet/minecraft/client/render/block/entity/SkullBlockEntityRenderer;render(Lnet/minecraft/client/render/block/entity/state/SkullBlockEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V
 *   Lnet/minecraft/client/render/block/entity/SkullBlockEntityRenderer;updateRenderState(Lnet/minecraft/block/entity/SkullBlockEntity;Lnet/minecraft/client/render/block/entity/state/SkullBlockEntityRenderState;FLnet/minecraft/util/math/Vec3d;Lnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V
 *   Lnet/minecraft/client/render/block/entity/SkullBlockEntityRenderer;createRenderState()Lnet/minecraft/client/render/block/entity/state/SkullBlockEntityRenderState;
 */
package net.minecraft.client.render.block.entity;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.WallSkullBlock;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.SkullBlockEntityModel;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.block.entity.state.SkullBlockEntityRenderState;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.model.DragonHeadEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.entity.model.PiglinHeadEntityModel;
import net.minecraft.client.render.entity.model.SkullEntityModel;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.texture.PlayerSkinCache;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationPropertyHelper;
import net.minecraft.util.math.Vec3d;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class SkullBlockEntityRenderer
implements BlockEntityRenderer<SkullBlockEntity, SkullBlockEntityRenderState> {
    private final Function<SkullBlock.SkullType, SkullBlockEntityModel> models;
    private static final Map<SkullBlock.SkullType, Identifier> TEXTURES = Util.make(Maps.newHashMap(), map -> {
        map.put(SkullBlock.Type.SKELETON, Identifier.ofVanilla("textures/entity/skeleton/skeleton.png"));
        map.put(SkullBlock.Type.WITHER_SKELETON, Identifier.ofVanilla("textures/entity/skeleton/wither_skeleton.png"));
        map.put(SkullBlock.Type.ZOMBIE, Identifier.ofVanilla("textures/entity/zombie/zombie.png"));
        map.put(SkullBlock.Type.CREEPER, Identifier.ofVanilla("textures/entity/creeper/creeper.png"));
        map.put(SkullBlock.Type.DRAGON, Identifier.ofVanilla("textures/entity/enderdragon/dragon.png"));
        map.put(SkullBlock.Type.PIGLIN, Identifier.ofVanilla("textures/entity/piglin/piglin.png"));
        map.put(SkullBlock.Type.PLAYER, DefaultSkinHelper.getTexture());
    });
    private final PlayerSkinCache skinCache;

    public static @Nullable SkullBlockEntityModel getModels(LoadedEntityModels models, SkullBlock.SkullType type) {
        if (type instanceof SkullBlock.Type) {
            SkullBlock.Type lv = (SkullBlock.Type)type;
            return switch (lv) {
                default -> throw new MatchException(null, null);
                case SkullBlock.Type.SKELETON -> new SkullEntityModel(models.getModelPart(EntityModelLayers.SKELETON_SKULL));
                case SkullBlock.Type.WITHER_SKELETON -> new SkullEntityModel(models.getModelPart(EntityModelLayers.WITHER_SKELETON_SKULL));
                case SkullBlock.Type.PLAYER -> new SkullEntityModel(models.getModelPart(EntityModelLayers.PLAYER_HEAD));
                case SkullBlock.Type.ZOMBIE -> new SkullEntityModel(models.getModelPart(EntityModelLayers.ZOMBIE_HEAD));
                case SkullBlock.Type.CREEPER -> new SkullEntityModel(models.getModelPart(EntityModelLayers.CREEPER_HEAD));
                case SkullBlock.Type.DRAGON -> new DragonHeadEntityModel(models.getModelPart(EntityModelLayers.DRAGON_SKULL));
                case SkullBlock.Type.PIGLIN -> new PiglinHeadEntityModel(models.getModelPart(EntityModelLayers.PIGLIN_HEAD));
            };
        }
        return null;
    }

    public SkullBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        LoadedEntityModels lv = context.loadedEntityModels();
        this.skinCache = context.playerSkinRenderCache();
        this.models = Util.memoize(type -> SkullBlockEntityRenderer.getModels(lv, type));
    }

    @Override
    public SkullBlockEntityRenderState createRenderState() {
        return new SkullBlockEntityRenderState();
    }

    @Override
    public void updateRenderState(SkullBlockEntity arg, SkullBlockEntityRenderState arg2, float f, Vec3d arg3,  @Nullable ModelCommandRenderer.CrumblingOverlayCommand arg4) {
        BlockEntityRenderer.super.updateRenderState(arg, arg2, f, arg3, arg4);
        arg2.poweredTicks = arg.getPoweredTicks(f);
        BlockState lv = arg.getCachedState();
        boolean bl = lv.getBlock() instanceof WallSkullBlock;
        arg2.facing = bl ? lv.get(WallSkullBlock.FACING) : null;
        int i = bl ? RotationPropertyHelper.fromDirection(arg2.facing.getOpposite()) : lv.get(SkullBlock.ROTATION);
        arg2.yaw = RotationPropertyHelper.toDegrees(i);
        arg2.skullType = ((AbstractSkullBlock)lv.getBlock()).getSkullType();
        arg2.renderLayer = this.renderSkull(arg2.skullType, arg);
    }

    @Override
    public void render(SkullBlockEntityRenderState arg, MatrixStack arg2, OrderedRenderCommandQueue arg3, CameraRenderState arg4) {
        SkullBlockEntityModel lv = this.models.apply(arg.skullType);
        SkullBlockEntityRenderer.render(arg.facing, arg.yaw, arg.poweredTicks, arg2, arg3, arg.lightmapCoordinates, lv, arg.renderLayer, 0, arg.crumblingOverlay);
    }

    public static void render(@Nullable Direction facing, float yaw, float poweredTicks, MatrixStack matrices, OrderedRenderCommandQueue queue, int light, SkullBlockEntityModel model, RenderLayer renderLayer, int outlineColor,  @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
        matrices.push();
        if (facing == null) {
            matrices.translate(0.5f, 0.0f, 0.5f);
        } else {
            float h = 0.25f;
            matrices.translate(0.5f - (float)facing.getOffsetX() * 0.25f, 0.25f, 0.5f - (float)facing.getOffsetZ() * 0.25f);
        }
        matrices.scale(-1.0f, -1.0f, 1.0f);
        SkullBlockEntityModel.SkullModelState lv = new SkullBlockEntityModel.SkullModelState();
        lv.poweredTicks = poweredTicks;
        lv.yaw = yaw;
        queue.submitModel(model, lv, matrices, renderLayer, light, OverlayTexture.DEFAULT_UV, outlineColor, crumblingOverlay);
        matrices.pop();
    }

    private RenderLayer renderSkull(SkullBlock.SkullType skullType, SkullBlockEntity blockEntity) {
        ProfileComponent lv;
        if (skullType == SkullBlock.Type.PLAYER && (lv = blockEntity.getOwner()) != null) {
            return this.skinCache.get(lv).getRenderLayer();
        }
        return SkullBlockEntityRenderer.getCutoutRenderLayer(skullType, null);
    }

    public static RenderLayer getCutoutRenderLayer(SkullBlock.SkullType type, @Nullable Identifier texture) {
        return RenderLayers.entityCutoutNoCullZOffset(texture != null ? texture : TEXTURES.get(type));
    }

    public static RenderLayer getTranslucentRenderLayer(Identifier texture) {
        return RenderLayers.entityTranslucent(texture);
    }

    @Override
    public /* synthetic */ BlockEntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

