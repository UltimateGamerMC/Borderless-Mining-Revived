/*
 * External method calls:
 *   Lnet/minecraft/component/type/BannerPatternsComponent;layers()Ljava/util/List;
 *   Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitModelPart(Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/RenderLayer;IILnet/minecraft/client/texture/Sprite;ZZILnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;I)V
 *   Lnet/minecraft/client/render/block/entity/BannerBlockEntityRenderer;renderCanvas(Lnet/minecraft/client/texture/SpriteHolder;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;IILnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/client/util/SpriteIdentifier;ZLnet/minecraft/util/DyeColor;Lnet/minecraft/component/type/BannerPatternsComponent;ZLnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;I)V
 *   Lnet/minecraft/client/model/ModelPart;collectVertices(Lnet/minecraft/client/util/math/MatrixStack;Ljava/util/function/Consumer;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/item/model/special/ShieldModelRenderer;render(Lnet/minecraft/component/ComponentMap;Lnet/minecraft/item/ItemDisplayContext;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;IIZI)V
 */
package net.minecraft.client.render.item.model.special;

import com.mojang.serialization.MapCodec;
import java.util.Objects;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.entity.BannerBlockEntityRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.ShieldEntityModel;
import net.minecraft.client.render.item.model.special.SpecialModelRenderer;
import net.minecraft.client.render.model.ModelBaker;
import net.minecraft.client.texture.SpriteHolder;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Unit;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class ShieldModelRenderer
implements SpecialModelRenderer<ComponentMap> {
    private final SpriteHolder spriteHolder;
    private final ShieldEntityModel model;

    public ShieldModelRenderer(SpriteHolder spriteHolder, ShieldEntityModel model) {
        this.spriteHolder = spriteHolder;
        this.model = model;
    }

    @Override
    public @Nullable ComponentMap getData(ItemStack arg) {
        return arg.getImmutableComponents();
    }

    @Override
    public void render(@Nullable ComponentMap arg, ItemDisplayContext arg2, MatrixStack arg3, OrderedRenderCommandQueue arg4, int i, int j, boolean bl, int k) {
        BannerPatternsComponent lv = arg != null ? arg.getOrDefault(DataComponentTypes.BANNER_PATTERNS, BannerPatternsComponent.DEFAULT) : BannerPatternsComponent.DEFAULT;
        DyeColor lv2 = arg != null ? arg.get(DataComponentTypes.BASE_COLOR) : null;
        boolean bl2 = !lv.layers().isEmpty() || lv2 != null;
        arg3.push();
        arg3.scale(1.0f, -1.0f, -1.0f);
        SpriteIdentifier lv3 = bl2 ? ModelBaker.SHIELD_BASE : ModelBaker.SHIELD_BASE_NO_PATTERN;
        arg4.submitModelPart(this.model.getHandle(), arg3, this.model.getLayer(lv3.getAtlasId()), i, j, this.spriteHolder.getSprite(lv3), false, false, -1, null, k);
        if (bl2) {
            BannerBlockEntityRenderer.renderCanvas(this.spriteHolder, arg3, arg4, i, j, this.model, Unit.INSTANCE, lv3, false, Objects.requireNonNullElse(lv2, DyeColor.WHITE), lv, bl, null, k);
        } else {
            arg4.submitModelPart(this.model.getPlate(), arg3, this.model.getLayer(lv3.getAtlasId()), i, j, this.spriteHolder.getSprite(lv3), false, bl, -1, null, k);
        }
        arg3.pop();
    }

    @Override
    public void collectVertices(Consumer<Vector3fc> consumer) {
        MatrixStack lv = new MatrixStack();
        lv.scale(1.0f, -1.0f, -1.0f);
        this.model.getRootPart().collectVertices(lv, consumer);
    }

    @Override
    public /* synthetic */ @Nullable Object getData(ItemStack stack) {
        return this.getData(stack);
    }

    @Environment(value=EnvType.CLIENT)
    public record Unbaked() implements SpecialModelRenderer.Unbaked
    {
        public static final Unbaked INSTANCE = new Unbaked();
        public static final MapCodec<Unbaked> CODEC = MapCodec.unit(INSTANCE);

        public MapCodec<Unbaked> getCodec() {
            return CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(SpecialModelRenderer.BakeContext context) {
            return new ShieldModelRenderer(context.spriteHolder(), new ShieldEntityModel(context.entityModelSet().getModelPart(EntityModelLayers.SHIELD)));
        }
    }
}

