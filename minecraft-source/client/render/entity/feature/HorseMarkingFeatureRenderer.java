/*
 * External method calls:
 *   Lnet/minecraft/client/render/RenderLayers;entityTranslucent(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;
 *   Lnet/minecraft/client/render/command/RenderCommandQueue;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/RenderLayer;IIILnet/minecraft/client/texture/Sprite;ILnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/feature/HorseMarkingFeatureRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/client/render/entity/state/HorseEntityRenderState;FF)V
 */
package net.minecraft.client.render.entity.feature;

import com.google.common.collect.Maps;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.HorseEntityModel;
import net.minecraft.client.render.entity.state.HorseEntityRenderState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.HorseMarking;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class HorseMarkingFeatureRenderer
extends FeatureRenderer<HorseEntityRenderState, HorseEntityModel> {
    private static final Identifier INVISIBLE_ID = Identifier.ofVanilla("invisible");
    private static final Map<HorseMarking, Identifier> TEXTURES = Maps.newEnumMap(Map.of(HorseMarking.NONE, INVISIBLE_ID, HorseMarking.WHITE, Identifier.ofVanilla("textures/entity/horse/horse_markings_white.png"), HorseMarking.WHITE_FIELD, Identifier.ofVanilla("textures/entity/horse/horse_markings_whitefield.png"), HorseMarking.WHITE_DOTS, Identifier.ofVanilla("textures/entity/horse/horse_markings_whitedots.png"), HorseMarking.BLACK_DOTS, Identifier.ofVanilla("textures/entity/horse/horse_markings_blackdots.png")));

    public HorseMarkingFeatureRenderer(FeatureRendererContext<HorseEntityRenderState, HorseEntityModel> arg) {
        super(arg);
    }

    @Override
    public void render(MatrixStack arg, OrderedRenderCommandQueue arg2, int i, HorseEntityRenderState arg3, float f, float g) {
        Identifier lv = TEXTURES.get((Object)arg3.marking);
        if (lv == INVISIBLE_ID || arg3.invisible) {
            return;
        }
        arg2.getBatchingQueue(1).submitModel(this.getContextModel(), arg3, arg, RenderLayers.entityTranslucent(lv), i, LivingEntityRenderer.getOverlay(arg3, 0.0f), -1, (Sprite)null, arg3.outlineColor, (ModelCommandRenderer.CrumblingOverlayCommand)null);
    }
}

