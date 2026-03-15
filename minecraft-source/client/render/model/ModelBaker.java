/*
 * External method calls:
 *   Lnet/minecraft/client/render/model/ModelBaker$BlockItemModels;bake(Lnet/minecraft/client/render/model/BakedSimpleModel;Lnet/minecraft/client/render/model/ErrorCollectingSpriteGetter;Lnet/minecraft/client/render/model/Baker$Vec3fInterner;)Lnet/minecraft/client/render/model/ModelBaker$BlockItemModels;
 *   Lnet/minecraft/util/thread/AsyncHelper;mapValues(Ljava/util/Map;Ljava/util/function/BiFunction;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/client/item/ItemAsset;properties()Lnet/minecraft/client/item/ItemAsset$Properties;
 *   Lnet/minecraft/client/item/ItemAsset;model()Lnet/minecraft/client/render/item/model/ItemModel$Unbaked;
 *   Lnet/minecraft/client/item/ItemAsset;registrySwapper()Lnet/minecraft/registry/ContextSwapper;
 *   Lnet/minecraft/client/render/item/model/ItemModel$Unbaked;bake(Lnet/minecraft/client/render/item/model/ItemModel$BakeContext;)Lnet/minecraft/client/render/item/model/ItemModel;
 *   Lnet/minecraft/client/render/model/BlockStateModel$UnbakedGrouped;bake(Lnet/minecraft/block/BlockState;Lnet/minecraft/client/render/model/Baker;)Lnet/minecraft/client/render/model/BlockStateModel;
 *   Lnet/minecraft/util/Identifier;withPath(Ljava/util/function/UnaryOperator;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/client/util/SpriteMapper;mapVanilla(Ljava/lang/String;)Lnet/minecraft/client/util/SpriteIdentifier;
 */
package net.minecraft.client.render.model;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.mojang.logging.LogUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.ItemAsset;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.MissingItemModel;
import net.minecraft.client.render.model.BakedGeometry;
import net.minecraft.client.render.model.BakedSimpleModel;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.render.model.ErrorCollectingSpriteGetter;
import net.minecraft.client.render.model.GeometryBakedModel;
import net.minecraft.client.render.model.ModelRotation;
import net.minecraft.client.render.model.ModelSettings;
import net.minecraft.client.render.model.ModelTextures;
import net.minecraft.client.render.model.SimpleBlockStateModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.PlayerSkinCache;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteHolder;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.thread.AsyncHelper;
import org.joml.Vector3fc;
import org.slf4j.Logger;

@Environment(value=EnvType.CLIENT)
public class ModelBaker {
    public static final SpriteIdentifier FIRE_0 = TexturedRenderLayers.BLOCK_SPRITE_MAPPER.mapVanilla("fire_0");
    public static final SpriteIdentifier FIRE_1 = TexturedRenderLayers.BLOCK_SPRITE_MAPPER.mapVanilla("fire_1");
    public static final SpriteIdentifier LAVA_STILL = TexturedRenderLayers.BLOCK_SPRITE_MAPPER.mapVanilla("lava_still");
    public static final SpriteIdentifier LAVA_FLOW = TexturedRenderLayers.BLOCK_SPRITE_MAPPER.mapVanilla("lava_flow");
    public static final SpriteIdentifier WATER_STILL = TexturedRenderLayers.BLOCK_SPRITE_MAPPER.mapVanilla("water_still");
    public static final SpriteIdentifier WATER_FLOW = TexturedRenderLayers.BLOCK_SPRITE_MAPPER.mapVanilla("water_flow");
    public static final SpriteIdentifier WATER_OVERLAY = TexturedRenderLayers.BLOCK_SPRITE_MAPPER.mapVanilla("water_overlay");
    public static final SpriteIdentifier BANNER_BASE = new SpriteIdentifier(TexturedRenderLayers.BANNER_PATTERNS_ATLAS_TEXTURE, Identifier.ofVanilla("entity/banner_base"));
    public static final SpriteIdentifier SHIELD_BASE = new SpriteIdentifier(TexturedRenderLayers.SHIELD_PATTERNS_ATLAS_TEXTURE, Identifier.ofVanilla("entity/shield_base"));
    public static final SpriteIdentifier SHIELD_BASE_NO_PATTERN = new SpriteIdentifier(TexturedRenderLayers.SHIELD_PATTERNS_ATLAS_TEXTURE, Identifier.ofVanilla("entity/shield_base_nopattern"));
    public static final int MAX_BLOCK_DESTRUCTION_STAGE = 10;
    public static final List<Identifier> BLOCK_DESTRUCTION_STAGES = IntStream.range(0, 10).mapToObj(stage -> Identifier.ofVanilla("block/destroy_stage_" + stage)).collect(Collectors.toList());
    public static final List<Identifier> BLOCK_DESTRUCTION_STAGE_TEXTURES = BLOCK_DESTRUCTION_STAGES.stream().map(id -> id.withPath(path -> "textures/" + path + ".png")).collect(Collectors.toList());
    public static final List<RenderLayer> BLOCK_DESTRUCTION_RENDER_LAYERS = BLOCK_DESTRUCTION_STAGE_TEXTURES.stream().map(RenderLayers::crumbling).collect(Collectors.toList());
    static final Logger LOGGER = LogUtils.getLogger();
    private final LoadedEntityModels entityModels;
    private final SpriteHolder spriteHolder;
    private final PlayerSkinCache skinCache;
    private final Map<BlockState, BlockStateModel.UnbakedGrouped> blockModels;
    private final Map<Identifier, ItemAsset> itemAssets;
    final Map<Identifier, BakedSimpleModel> simpleModels;
    final BakedSimpleModel missingModel;

    public ModelBaker(LoadedEntityModels entityModels, SpriteHolder spriteHolder, PlayerSkinCache skinCache, Map<BlockState, BlockStateModel.UnbakedGrouped> blockModels, Map<Identifier, ItemAsset> itemAssets, Map<Identifier, BakedSimpleModel> simpleModels, BakedSimpleModel missingModel) {
        this.entityModels = entityModels;
        this.spriteHolder = spriteHolder;
        this.skinCache = skinCache;
        this.blockModels = blockModels;
        this.itemAssets = itemAssets;
        this.simpleModels = simpleModels;
        this.missingModel = missingModel;
    }

    public CompletableFuture<BakedModels> bake(ErrorCollectingSpriteGetter spriteGetter, Executor executor) {
        Vec3fInternerImpl lv = new Vec3fInternerImpl();
        BlockItemModels lv2 = BlockItemModels.bake(this.missingModel, spriteGetter, lv);
        BakerImpl lv3 = new BakerImpl(spriteGetter, lv, lv2);
        CompletableFuture<Map<BlockState, BlockStateModel>> completableFuture = AsyncHelper.mapValues(this.blockModels, (state, unbaked) -> {
            try {
                return unbaked.bake((BlockState)state, lv3);
            } catch (Exception exception) {
                LOGGER.warn("Unable to bake model: '{}': {}", state, (Object)exception);
                return null;
            }
        }, executor);
        CompletableFuture<Map<Identifier, ItemModel>> completableFuture2 = AsyncHelper.mapValues(this.itemAssets, (state, asset) -> {
            try {
                return asset.model().bake(new ItemModel.BakeContext(lv3, this.entityModels, this.spriteHolder, this.skinCache, arg2.item, asset.registrySwapper()));
            } catch (Exception exception) {
                LOGGER.warn("Unable to bake item model: '{}'", state, (Object)exception);
                return null;
            }
        }, executor);
        HashMap map = new HashMap(this.itemAssets.size());
        this.itemAssets.forEach((id, asset) -> {
            ItemAsset.Properties lv = asset.properties();
            if (!lv.equals(ItemAsset.Properties.DEFAULT)) {
                map.put(id, lv);
            }
        });
        return completableFuture.thenCombine(completableFuture2, (blockStateModels, itemModels) -> new BakedModels(lv2, (Map<BlockState, BlockStateModel>)blockStateModels, (Map<Identifier, ItemModel>)itemModels, map));
    }

    @Environment(value=EnvType.CLIENT)
    static class Vec3fInternerImpl
    implements Baker.Vec3fInterner {
        private final Interner<Vector3fc> INTERNER = Interners.newStrongInterner();

        Vec3fInternerImpl() {
        }

        @Override
        public Vector3fc intern(Vector3fc vec) {
            return this.INTERNER.intern(vec);
        }
    }

    @Environment(value=EnvType.CLIENT)
    public record BlockItemModels(BlockModelPart blockPart, BlockStateModel block, ItemModel item) {
        public static BlockItemModels bake(BakedSimpleModel model, final ErrorCollectingSpriteGetter arg2, final Baker.Vec3fInterner arg3) {
            Baker lv = new Baker(){

                @Override
                public BakedSimpleModel getModel(Identifier id) {
                    throw new IllegalStateException("Missing model can't have dependencies, but asked for " + String.valueOf(id));
                }

                @Override
                public BlockModelPart getBlockPart() {
                    throw new IllegalStateException();
                }

                @Override
                public <T> T compute(Baker.ResolvableCacheKey<T> key) {
                    return key.compute(this);
                }

                @Override
                public ErrorCollectingSpriteGetter getSpriteGetter() {
                    return arg2;
                }

                @Override
                public Baker.Vec3fInterner getVec3fInterner() {
                    return arg3;
                }
            };
            ModelTextures lv2 = model.getTextures();
            boolean bl = model.getAmbientOcclusion();
            boolean bl2 = model.getGuiLight().isSide();
            ModelTransformation lv3 = model.getTransformations();
            BakedGeometry lv4 = model.bakeGeometry(lv2, lv, ModelRotation.IDENTITY);
            Sprite lv5 = model.getParticleTexture(lv2, lv);
            GeometryBakedModel lv6 = new GeometryBakedModel(lv4, bl, lv5);
            SimpleBlockStateModel lv7 = new SimpleBlockStateModel(lv6);
            MissingItemModel lv8 = new MissingItemModel(lv4.getAllQuads(), new ModelSettings(bl2, lv5, lv3));
            return new BlockItemModels(lv6, lv7, lv8);
        }
    }

    @Environment(value=EnvType.CLIENT)
    class BakerImpl
    implements Baker {
        private final ErrorCollectingSpriteGetter spriteGetter;
        private final Baker.Vec3fInterner interner;
        private final BlockItemModels blockItemModels;
        private final Map<Baker.ResolvableCacheKey<Object>, Object> cache = new ConcurrentHashMap<Baker.ResolvableCacheKey<Object>, Object>();
        private final Function<Baker.ResolvableCacheKey<Object>, Object> cacheValueFunction = key -> key.compute(this);

        BakerImpl(ErrorCollectingSpriteGetter spriteGetter, Baker.Vec3fInterner interner, BlockItemModels blockItemModels) {
            this.spriteGetter = spriteGetter;
            this.interner = interner;
            this.blockItemModels = blockItemModels;
        }

        @Override
        public BlockModelPart getBlockPart() {
            return this.blockItemModels.blockPart;
        }

        @Override
        public ErrorCollectingSpriteGetter getSpriteGetter() {
            return this.spriteGetter;
        }

        @Override
        public Baker.Vec3fInterner getVec3fInterner() {
            return this.interner;
        }

        @Override
        public BakedSimpleModel getModel(Identifier id) {
            BakedSimpleModel lv = ModelBaker.this.simpleModels.get(id);
            if (lv == null) {
                LOGGER.warn("Requested a model that was not discovered previously: {}", (Object)id);
                return ModelBaker.this.missingModel;
            }
            return lv;
        }

        @Override
        public <T> T compute(Baker.ResolvableCacheKey<T> key) {
            return (T)this.cache.computeIfAbsent(key, this.cacheValueFunction);
        }
    }

    @Environment(value=EnvType.CLIENT)
    public record BakedModels(BlockItemModels missingModels, Map<BlockState, BlockStateModel> blockStateModels, Map<Identifier, ItemModel> itemStackModels, Map<Identifier, ItemAsset.Properties> itemProperties) {
    }
}

