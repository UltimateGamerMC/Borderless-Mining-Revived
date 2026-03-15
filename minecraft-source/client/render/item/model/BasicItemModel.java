/*
 * External method calls:
 *   Lnet/minecraft/client/render/model/BakedQuad;sprite()Lnet/minecraft/client/texture/Sprite;
 *   Lnet/minecraft/client/render/item/ItemRenderState;addModelKey(Ljava/lang/Object;)V
 *   Lnet/minecraft/client/render/item/ItemRenderState;newLayer()Lnet/minecraft/client/render/item/ItemRenderState$LayerRenderState;
 *   Lnet/minecraft/client/render/item/ItemRenderState$LayerRenderState;initTints(I)[I
 *   Lnet/minecraft/client/render/model/ModelSettings;addSettings(Lnet/minecraft/client/render/item/ItemRenderState$LayerRenderState;Lnet/minecraft/item/ItemDisplayContext;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/item/model/BasicItemModel;bakeQuads(Ljava/util/List;)[Lorg/joml/Vector3fc;
 */
package net.minecraft.client.render.item.model;

import com.google.common.base.Suppliers;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.BlockRenderLayers;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.tint.TintSource;
import net.minecraft.client.render.item.tint.TintSourceTypes;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BakedSimpleModel;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.ModelRotation;
import net.minecraft.client.render.model.ModelSettings;
import net.minecraft.client.render.model.ModelTextures;
import net.minecraft.client.render.model.ResolvableModel;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.HeldItemContext;
import net.minecraft.util.Identifier;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class BasicItemModel
implements ItemModel {
    private static final Function<ItemStack, RenderLayer> ITEMS_ATLAS_RENDER_LAYER_GETTER = stack -> TexturedRenderLayers.getItemTranslucentCull();
    private static final Function<ItemStack, RenderLayer> BLOCKS_ATLAS_RENDER_LAYER_GETTER = stack -> {
        BlockItem lv2;
        BlockRenderLayer lv3;
        Item lv = stack.getItem();
        if (lv instanceof BlockItem && (lv3 = BlockRenderLayers.getBlockLayer((lv2 = (BlockItem)lv).getBlock().getDefaultState())) != BlockRenderLayer.TRANSLUCENT) {
            return TexturedRenderLayers.getEntityCutout();
        }
        return TexturedRenderLayers.getBlockTranslucentCull();
    };
    private final List<TintSource> tints;
    private final List<BakedQuad> quads;
    private final Supplier<Vector3fc[]> vector;
    private final ModelSettings settings;
    private final boolean animated;
    private final Function<ItemStack, RenderLayer> renderLayerGetter;

    BasicItemModel(List<TintSource> tints, List<BakedQuad> quads, ModelSettings settings, Function<ItemStack, RenderLayer> renderLayerGetter) {
        this.tints = tints;
        this.quads = quads;
        this.settings = settings;
        this.renderLayerGetter = renderLayerGetter;
        this.vector = Suppliers.memoize(() -> BasicItemModel.bakeQuads(this.quads));
        boolean bl = false;
        for (BakedQuad lv : quads) {
            if (!lv.sprite().getContents().isAnimated()) continue;
            bl = true;
            break;
        }
        this.animated = bl;
    }

    public static Vector3fc[] bakeQuads(List<BakedQuad> quads) {
        HashSet<Vector3fc> set = new HashSet<Vector3fc>();
        for (BakedQuad lv : quads) {
            for (int i = 0; i < 4; ++i) {
                set.add(lv.getPosition(i));
            }
        }
        return (Vector3fc[])set.toArray(Vector3fc[]::new);
    }

    @Override
    public void update(ItemRenderState state, ItemStack stack, ItemModelManager resolver, ItemDisplayContext displayContext, @Nullable ClientWorld world, @Nullable HeldItemContext heldItemContext, int seed) {
        state.addModelKey(this);
        ItemRenderState.LayerRenderState lv = state.newLayer();
        if (stack.hasGlint()) {
            ItemRenderState.Glint lv2 = BasicItemModel.shouldUseSpecialGlint(stack) ? ItemRenderState.Glint.SPECIAL : ItemRenderState.Glint.STANDARD;
            lv.setGlint(lv2);
            state.markAnimated();
            state.addModelKey((Object)lv2);
        }
        int j = this.tints.size();
        int[] is = lv.initTints(j);
        for (int k = 0; k < j; ++k) {
            int l;
            is[k] = l = this.tints.get(k).getTint(stack, world, heldItemContext == null ? null : heldItemContext.getEntity());
            state.addModelKey(l);
        }
        lv.setVertices(this.vector);
        lv.setRenderLayer(this.renderLayerGetter.apply(stack));
        this.settings.addSettings(lv, displayContext);
        lv.getQuads().addAll(this.quads);
        if (this.animated) {
            state.markAnimated();
        }
    }

    static Function<ItemStack, RenderLayer> findRenderLayerGetter(List<BakedQuad> quads) {
        Iterator<BakedQuad> iterator = quads.iterator();
        if (!iterator.hasNext()) {
            return ITEMS_ATLAS_RENDER_LAYER_GETTER;
        }
        Identifier lv = iterator.next().sprite().getAtlasId();
        while (iterator.hasNext()) {
            BakedQuad lv2 = iterator.next();
            Identifier lv3 = lv2.sprite().getAtlasId();
            if (lv3.equals(lv)) continue;
            throw new IllegalStateException("Multiple atlases used in model, expected " + String.valueOf(lv) + ", but also got " + String.valueOf(lv3));
        }
        if (lv.equals(SpriteAtlasTexture.ITEMS_ATLAS_TEXTURE)) {
            return ITEMS_ATLAS_RENDER_LAYER_GETTER;
        }
        if (lv.equals(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)) {
            return BLOCKS_ATLAS_RENDER_LAYER_GETTER;
        }
        throw new IllegalArgumentException("Atlas " + String.valueOf(lv) + " can't be usef for item models");
    }

    private static boolean shouldUseSpecialGlint(ItemStack stack) {
        return stack.isIn(ItemTags.COMPASSES) || stack.isOf(Items.CLOCK);
    }

    @Environment(value=EnvType.CLIENT)
    public record Unbaked(Identifier model, List<TintSource> tints) implements ItemModel.Unbaked
    {
        public static final MapCodec<Unbaked> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Identifier.CODEC.fieldOf("model")).forGetter(Unbaked::model), TintSourceTypes.CODEC.listOf().optionalFieldOf("tints", List.of()).forGetter(Unbaked::tints)).apply((Applicative<Unbaked, ?>)instance, Unbaked::new));

        @Override
        public void resolve(ResolvableModel.Resolver resolver) {
            resolver.markDependency(this.model);
        }

        @Override
        public ItemModel bake(ItemModel.BakeContext context) {
            Baker lv = context.blockModelBaker();
            BakedSimpleModel lv2 = lv.getModel(this.model);
            ModelTextures lv3 = lv2.getTextures();
            List<BakedQuad> list = lv2.bakeGeometry(lv3, lv, ModelRotation.IDENTITY).getAllQuads();
            ModelSettings lv4 = ModelSettings.resolveSettings(lv, lv2, lv3);
            Function<ItemStack, RenderLayer> function = BasicItemModel.findRenderLayerGetter(list);
            return new BasicItemModel(this.tints, list, lv4, function);
        }

        public MapCodec<Unbaked> getCodec() {
            return CODEC;
        }
    }
}

