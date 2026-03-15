/*
 * External method calls:
 *   Lnet/minecraft/util/Util;memoize(Ljava/util/function/Function;)Ljava/util/function/Function;
 *   Lnet/minecraft/client/render/RenderLayers;armorCutoutNoCull(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;
 *   Lnet/minecraft/client/render/command/RenderCommandQueue;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/RenderLayer;IIILnet/minecraft/client/texture/Sprite;ILnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V
 *   Lnet/minecraft/client/render/RenderLayers;armorEntityGlint()Lnet/minecraft/client/render/RenderLayer;
 *   Lnet/minecraft/item/equipment/trim/ArmorTrim;pattern()Lnet/minecraft/registry/entry/RegistryEntry;
 *   Lnet/minecraft/client/render/entity/equipment/EquipmentModel$Layer;dyeable()Ljava/util/Optional;
 *   Lnet/minecraft/client/render/entity/equipment/EquipmentModel$Dyeable;colorWhenUndyed()Ljava/util/Optional;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/equipment/EquipmentRenderer;render(Lnet/minecraft/client/render/entity/equipment/EquipmentModel$LayerType;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/util/Identifier;II)V
 */
package net.minecraft.client.render.entity.equipment;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.equipment.EquipmentModel;
import net.minecraft.client.render.entity.equipment.EquipmentModelLoader;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.trim.ArmorTrim;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class EquipmentRenderer {
    private static final int field_54178 = 0;
    private final EquipmentModelLoader equipmentModelLoader;
    private final Function<LayerTextureKey, Identifier> layerTextures;
    private final Function<TrimSpriteKey, Sprite> trimSprites;

    public EquipmentRenderer(EquipmentModelLoader equipmentModelLoader, SpriteAtlasTexture armorTrimsAtlas) {
        this.equipmentModelLoader = equipmentModelLoader;
        this.layerTextures = Util.memoize(key -> key.layer.getFullTextureId(key.layerType));
        this.trimSprites = Util.memoize(key -> armorTrimsAtlas.getSprite(key.getTexture()));
    }

    public <S> void render(EquipmentModel.LayerType layerType, RegistryKey<EquipmentAsset> assetKey, Model<? super S> model, S state, ItemStack stack, MatrixStack matrices, OrderedRenderCommandQueue queue, int light, int outlineColor) {
        this.render(layerType, assetKey, model, state, stack, matrices, queue, light, null, outlineColor, 1);
    }

    public <S> void render(EquipmentModel.LayerType layerType, RegistryKey<EquipmentAsset> assetKey, Model<? super S> model, S state, ItemStack stack, MatrixStack matrices, OrderedRenderCommandQueue queue, int light, @Nullable Identifier textureId, int outlineColor, int initialOrder) {
        List<EquipmentModel.Layer> list = this.equipmentModelLoader.get(assetKey).getLayers(layerType);
        if (list.isEmpty()) {
            return;
        }
        int l = DyedColorComponent.getColor(stack, 0);
        boolean bl = stack.hasGlint();
        int m = initialOrder;
        for (EquipmentModel.Layer lv : list) {
            int n = EquipmentRenderer.getDyeColor(lv, l);
            if (n == 0) continue;
            Identifier lv2 = lv.usePlayerTexture() && textureId != null ? textureId : this.layerTextures.apply(new LayerTextureKey(layerType, lv));
            queue.getBatchingQueue(m++).submitModel(model, state, matrices, RenderLayers.armorCutoutNoCull(lv2), light, OverlayTexture.DEFAULT_UV, n, null, outlineColor, null);
            if (bl) {
                queue.getBatchingQueue(m++).submitModel(model, state, matrices, RenderLayers.armorEntityGlint(), light, OverlayTexture.DEFAULT_UV, n, null, outlineColor, null);
            }
            bl = false;
        }
        ArmorTrim lv3 = stack.get(DataComponentTypes.TRIM);
        if (lv3 != null) {
            Sprite lv4 = this.trimSprites.apply(new TrimSpriteKey(lv3, layerType, assetKey));
            RenderLayer lv5 = TexturedRenderLayers.getArmorTrims(lv3.pattern().value().decal());
            queue.getBatchingQueue(m++).submitModel(model, state, matrices, lv5, light, OverlayTexture.DEFAULT_UV, -1, lv4, outlineColor, null);
        }
    }

    private static int getDyeColor(EquipmentModel.Layer layer, int dyeColor) {
        Optional<EquipmentModel.Dyeable> optional = layer.dyeable();
        if (optional.isPresent()) {
            int j = optional.get().colorWhenUndyed().map(ColorHelper::fullAlpha).orElse(0);
            return dyeColor != 0 ? dyeColor : j;
        }
        return -1;
    }

    @Environment(value=EnvType.CLIENT)
    record LayerTextureKey(EquipmentModel.LayerType layerType, EquipmentModel.Layer layer) {
    }

    @Environment(value=EnvType.CLIENT)
    record TrimSpriteKey(ArmorTrim trim, EquipmentModel.LayerType layerType, RegistryKey<EquipmentAsset> equipmentAssetId) {
        public Identifier getTexture() {
            return this.trim.getTextureId(this.layerType.getTrimsDirectory(), this.equipmentAssetId);
        }
    }
}

