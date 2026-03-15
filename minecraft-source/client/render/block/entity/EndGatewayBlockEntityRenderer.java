/*
 * External method calls:
 *   Lnet/minecraft/client/render/block/entity/AbstractEndPortalBlockEntityRenderer;updateRenderState(Lnet/minecraft/block/entity/EndPortalBlockEntity;Lnet/minecraft/client/render/block/entity/state/EndPortalBlockEntityRenderState;FLnet/minecraft/util/math/Vec3d;Lnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V
 *   Lnet/minecraft/client/render/block/entity/BeaconBlockEntityRenderer;renderBeam(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/util/Identifier;FFIIIFF)V
 *   Lnet/minecraft/client/render/block/entity/AbstractEndPortalBlockEntityRenderer;render(Lnet/minecraft/client/render/block/entity/state/EndPortalBlockEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V
 *   Lnet/minecraft/client/render/RenderLayers;endGateway()Lnet/minecraft/client/render/RenderLayer;
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/block/entity/EndGatewayBlockEntityRenderer;render(Lnet/minecraft/client/render/block/entity/state/EndGatewayBlockEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V
 *   Lnet/minecraft/client/render/block/entity/EndGatewayBlockEntityRenderer;updateRenderState(Lnet/minecraft/block/entity/EndGatewayBlockEntity;Lnet/minecraft/client/render/block/entity/state/EndGatewayBlockEntityRenderState;FLnet/minecraft/util/math/Vec3d;Lnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V
 *   Lnet/minecraft/client/render/block/entity/EndGatewayBlockEntityRenderer;createRenderState()Lnet/minecraft/client/render/block/entity/state/EndGatewayBlockEntityRenderState;
 */
package net.minecraft.client.render.block.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.EndGatewayBlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.block.entity.AbstractEndPortalBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.block.entity.state.EndGatewayBlockEntityRenderState;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class EndGatewayBlockEntityRenderer
extends AbstractEndPortalBlockEntityRenderer<EndGatewayBlockEntity, EndGatewayBlockEntityRenderState> {
    private static final Identifier BEAM_TEXTURE = Identifier.ofVanilla("textures/entity/end_gateway_beam.png");

    @Override
    public EndGatewayBlockEntityRenderState createRenderState() {
        return new EndGatewayBlockEntityRenderState();
    }

    @Override
    public void updateRenderState(EndGatewayBlockEntity arg, EndGatewayBlockEntityRenderState arg2, float f, Vec3d arg3,  @Nullable ModelCommandRenderer.CrumblingOverlayCommand arg4) {
        super.updateRenderState(arg, arg2, f, arg3, arg4);
        World lv = arg.getWorld();
        if (arg.isRecentlyGenerated() || arg.needsCooldownBeforeTeleporting() && lv != null) {
            arg2.beamHeight = arg.isRecentlyGenerated() ? arg.getRecentlyGeneratedBeamHeight(f) : arg.getCooldownBeamHeight(f);
            double d = arg.isRecentlyGenerated() ? (double)arg.getWorld().getTopYInclusive() : 50.0;
            arg2.beamHeight = MathHelper.sin(arg2.beamHeight * (float)Math.PI);
            arg2.beamSpan = MathHelper.floor((double)arg2.beamHeight * d);
            arg2.beamColor = arg.isRecentlyGenerated() ? DyeColor.MAGENTA.getEntityColor() : DyeColor.PURPLE.getEntityColor();
            arg2.beamRotationDegrees = arg.getWorld() != null ? (float)Math.floorMod(arg.getWorld().getTime(), 40) + f : 0.0f;
        } else {
            arg2.beamSpan = 0;
        }
    }

    @Override
    public void render(EndGatewayBlockEntityRenderState arg, MatrixStack arg2, OrderedRenderCommandQueue arg3, CameraRenderState arg4) {
        if (arg.beamSpan > 0) {
            BeaconBlockEntityRenderer.renderBeam(arg2, arg3, BEAM_TEXTURE, arg.beamHeight, arg.beamRotationDegrees, -arg.beamSpan, arg.beamSpan * 2, arg.beamColor, 0.15f, 0.175f);
        }
        super.render(arg, arg2, arg3, arg4);
    }

    @Override
    protected float getTopYOffset() {
        return 1.0f;
    }

    @Override
    protected float getBottomYOffset() {
        return 0.0f;
    }

    @Override
    protected RenderLayer getLayer() {
        return RenderLayers.endGateway();
    }

    @Override
    public int getRenderDistance() {
        return 256;
    }

    @Override
    public /* synthetic */ BlockEntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

