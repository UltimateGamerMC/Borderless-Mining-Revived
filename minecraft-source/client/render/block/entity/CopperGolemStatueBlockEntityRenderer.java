/*
 * External method calls:
 *   Lnet/minecraft/client/render/block/entity/BlockEntityRendererFactory$Context;loadedEntityModels()Lnet/minecraft/client/render/entity/model/LoadedEntityModels;
 *   Lnet/minecraft/client/render/block/entity/BlockEntityRenderer;updateRenderState(Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/client/render/block/entity/state/BlockEntityRenderState;FLnet/minecraft/util/math/Vec3d;Lnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V
 *   Lnet/minecraft/entity/passive/CopperGolemOxidationLevel;texture()Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/client/render/RenderLayers;entityCutoutNoCull(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;
 *   Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/RenderLayer;IIILnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/block/entity/CopperGolemStatueBlockEntityRenderer;render(Lnet/minecraft/client/render/block/entity/state/CopperGolemStatueBlockEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V
 *   Lnet/minecraft/client/render/block/entity/CopperGolemStatueBlockEntityRenderer;updateRenderState(Lnet/minecraft/block/entity/CopperGolemStatueBlockEntity;Lnet/minecraft/client/render/block/entity/state/CopperGolemStatueBlockEntityRenderState;FLnet/minecraft/util/math/Vec3d;Lnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V
 *   Lnet/minecraft/client/render/block/entity/CopperGolemStatueBlockEntityRenderer;createRenderState()Lnet/minecraft/client/render/block/entity/state/CopperGolemStatueBlockEntityRenderState;
 */
package net.minecraft.client.render.block.entity;

import java.util.HashMap;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.CopperGolemStatueBlock;
import net.minecraft.block.entity.CopperGolemStatueBlockEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.model.CopperGolemStatueModel;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.block.entity.state.CopperGolemStatueBlockEntityRenderState;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.CopperGolemOxidationLevels;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class CopperGolemStatueBlockEntityRenderer
implements BlockEntityRenderer<CopperGolemStatueBlockEntity, CopperGolemStatueBlockEntityRenderState> {
    private final Map<CopperGolemStatueBlock.Pose, CopperGolemStatueModel> models = new HashMap<CopperGolemStatueBlock.Pose, CopperGolemStatueModel>();

    public CopperGolemStatueBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        LoadedEntityModels lv = context.loadedEntityModels();
        this.models.put(CopperGolemStatueBlock.Pose.STANDING, new CopperGolemStatueModel(lv.getModelPart(EntityModelLayers.COPPER_GOLEM)));
        this.models.put(CopperGolemStatueBlock.Pose.RUNNING, new CopperGolemStatueModel(lv.getModelPart(EntityModelLayers.COPPER_GOLEM_RUNNING)));
        this.models.put(CopperGolemStatueBlock.Pose.SITTING, new CopperGolemStatueModel(lv.getModelPart(EntityModelLayers.COPPER_GOLEM_SITTING)));
        this.models.put(CopperGolemStatueBlock.Pose.STAR, new CopperGolemStatueModel(lv.getModelPart(EntityModelLayers.COPPER_GOLEM_STAR)));
    }

    @Override
    public CopperGolemStatueBlockEntityRenderState createRenderState() {
        return new CopperGolemStatueBlockEntityRenderState();
    }

    @Override
    public void updateRenderState(CopperGolemStatueBlockEntity arg, CopperGolemStatueBlockEntityRenderState arg2, float f, Vec3d arg3, @Nullable ModelCommandRenderer.CrumblingOverlayCommand arg4) {
        BlockEntityRenderer.super.updateRenderState(arg, arg2, f, arg3, arg4);
        arg2.facing = arg.getCachedState().get(CopperGolemStatueBlock.FACING);
        arg2.pose = arg.getCachedState().get(Properties.COPPER_GOLEM_POSE);
    }

    @Override
    public void render(CopperGolemStatueBlockEntityRenderState arg, MatrixStack arg2, OrderedRenderCommandQueue arg3, CameraRenderState arg4) {
        Block block = arg.blockState.getBlock();
        if (block instanceof CopperGolemStatueBlock) {
            CopperGolemStatueBlock lv = (CopperGolemStatueBlock)block;
            arg2.push();
            arg2.translate(0.5f, 0.0f, 0.5f);
            CopperGolemStatueModel lv2 = this.models.get(arg.pose);
            Direction lv3 = arg.facing;
            RenderLayer lv4 = RenderLayers.entityCutoutNoCull(CopperGolemOxidationLevels.get(lv.getOxidationLevel()).texture());
            arg3.submitModel(lv2, lv3, arg2, lv4, arg.lightmapCoordinates, OverlayTexture.DEFAULT_UV, 0, arg.crumblingOverlay);
            arg2.pop();
        }
    }

    @Override
    public /* synthetic */ BlockEntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

