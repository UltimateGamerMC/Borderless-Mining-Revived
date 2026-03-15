/*
 * External method calls:
 *   Lnet/minecraft/client/render/model/ModelBaker$BlockItemModels;block()Lnet/minecraft/client/render/model/BlockStateModel;
 *   Lnet/minecraft/client/render/model/ModelBaker$BlockItemModels;item()Lnet/minecraft/client/render/item/model/ItemModel;
 *   Lnet/minecraft/client/render/model/BlockStatesLoader;load(Lnet/minecraft/resource/ResourceManager;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/client/item/ItemAssetsLoader;load(Lnet/minecraft/resource/ResourceManager;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/util/profiler/Profiler;scoped(Ljava/lang/String;)Lnet/minecraft/util/profiler/ScopedProfiler;
 *   Lnet/minecraft/client/render/model/MissingModel;create()Lnet/minecraft/client/render/model/UnbakedModel;
 *   Lnet/minecraft/client/render/model/ReferencedModelsCollector;addSpecialModel(Lnet/minecraft/util/Identifier;Lnet/minecraft/client/render/model/UnbakedModel;)V
 *   Lnet/minecraft/client/render/model/BlockStatesLoader$LoadedModels;models()Ljava/util/Map;
 *   Lnet/minecraft/client/item/ItemAssetsLoader$Result;contents()Ljava/util/Map;
 *   Lnet/minecraft/client/render/model/ReferencedModelsCollector;collectModels()Ljava/util/Map;
 *   Lnet/minecraft/client/render/model/ModelBaker;bake(Lnet/minecraft/client/render/model/ErrorCollectingSpriteGetter;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/client/render/model/ModelGrouper;group(Lnet/minecraft/client/color/block/BlockColors;Lnet/minecraft/client/render/model/BlockStatesLoader$LoadedModels;)Lit/unimi/dsi/fastutil/objects/Object2IntMap;
 *   Lnet/minecraft/client/render/model/ModelBaker$BakedModels;itemStackModels()Ljava/util/Map;
 *   Lnet/minecraft/client/render/model/ModelBaker$BakedModels;itemProperties()Ljava/util/Map;
 *   Lnet/minecraft/client/render/model/ModelBaker$BakedModels;missingModels()Lnet/minecraft/client/render/model/ModelBaker$BlockItemModels;
 *   Lnet/minecraft/client/render/model/ModelBaker$BakedModels;blockStateModels()Ljava/util/Map;
 *   Lnet/minecraft/client/item/ItemAsset;model()Lnet/minecraft/client/render/item/model/ItemModel$Unbaked;
 *   Lnet/minecraft/client/render/model/ReferencedModelsCollector;resolve(Lnet/minecraft/client/render/model/ResolvableModel;)V
 *   Lnet/minecraft/util/Util;combineSafe(Ljava/util/List;)Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/resource/ResourceFinder;toResourceId(Lnet/minecraft/util/Identifier;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/client/render/model/json/JsonUnbakedModel;deserialize(Ljava/io/Reader;)Lnet/minecraft/client/render/model/json/JsonUnbakedModel;
 *   Lnet/minecraft/resource/ResourceFinder;findResources(Lnet/minecraft/resource/ResourceManager;)Ljava/util/Map;
 *   Lnet/minecraft/client/render/model/BakedModelManager$Models;models()Ljava/util/Map;
 *   Lnet/minecraft/client/render/model/BakedModelManager$Models;missing()Lnet/minecraft/client/render/model/BakedSimpleModel;
 *   Lnet/minecraft/client/render/block/entity/LoadedBlockEntityModels;fromModels(Lnet/minecraft/client/render/item/model/special/SpecialModelRenderer$BakeContext;)Lnet/minecraft/client/render/block/entity/LoadedBlockEntityModels;
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/resource/ResourceFinder;json(Ljava/lang/String;)Lnet/minecraft/resource/ResourceFinder;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/model/BakedModelManager;reloadModels(Lnet/minecraft/resource/ResourceManager;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/client/render/model/BakedModelManager;toStateMap(Ljava/util/Map;Lnet/minecraft/client/render/model/BlockStateModel;)Ljava/util/Map;
 *   Lnet/minecraft/client/render/model/BakedModelManager;bake(Lnet/minecraft/client/texture/SpriteLoader$StitchResult;Lnet/minecraft/client/texture/SpriteLoader$StitchResult;Lnet/minecraft/client/render/model/ModelBaker;Lit/unimi/dsi/fastutil/objects/Object2IntMap;Lnet/minecraft/client/render/entity/model/LoadedEntityModels;Lnet/minecraft/client/render/block/entity/LoadedBlockEntityModels;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/client/render/model/BakedModelManager;group(Lnet/minecraft/client/color/block/BlockColors;Lnet/minecraft/client/render/model/BlockStatesLoader$LoadedModels;)Lit/unimi/dsi/fastutil/objects/Object2IntMap;
 *   Lnet/minecraft/client/render/model/BakedModelManager;collect(Ljava/util/Map;Lnet/minecraft/client/render/model/BlockStatesLoader$LoadedModels;Lnet/minecraft/client/item/ItemAssetsLoader$Result;)Lnet/minecraft/client/render/model/BakedModelManager$Models;
 */
package net.minecraft.client.render.model;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import java.io.BufferedReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.item.ItemAsset;
import net.minecraft.client.item.ItemAssetsLoader;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.render.block.entity.LoadedBlockEntityModels;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.special.SpecialModelRenderer;
import net.minecraft.client.render.model.BakedSimpleModel;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.render.model.BlockStatesLoader;
import net.minecraft.client.render.model.ErrorCollectingSpriteGetter;
import net.minecraft.client.render.model.MissingModel;
import net.minecraft.client.render.model.ModelBaker;
import net.minecraft.client.render.model.ModelGrouper;
import net.minecraft.client.render.model.ReferencedModelsCollector;
import net.minecraft.client.render.model.SimpleModel;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.GeneratedItemModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.texture.AtlasManager;
import net.minecraft.client.texture.PlayerSkinCache;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.SpriteLoader;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.Registries;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.util.Atlases;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.profiler.Profilers;
import net.minecraft.util.profiler.ScopedProfiler;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(value=EnvType.CLIENT)
public class BakedModelManager
implements ResourceReloader {
    public static final Identifier BLOCK_OR_ITEM = Identifier.ofVanilla("block_or_item");
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final ResourceFinder MODELS_FINDER = ResourceFinder.json("models");
    private Map<Identifier, ItemModel> bakedItemModels = Map.of();
    private Map<Identifier, ItemAsset.Properties> itemProperties = Map.of();
    private final AtlasManager atlasManager;
    private final PlayerSkinCache skinCache;
    private final BlockModels blockModelCache;
    private final BlockColors colorMap;
    private LoadedEntityModels entityModels = LoadedEntityModels.EMPTY;
    private LoadedBlockEntityModels blockEntityModels = LoadedBlockEntityModels.EMPTY;
    private ModelBaker.BlockItemModels missingModels;
    private Object2IntMap<BlockState> modelGroups = Object2IntMaps.emptyMap();

    public BakedModelManager(BlockColors colorMap, AtlasManager atlasManager, PlayerSkinCache skinCache) {
        this.colorMap = colorMap;
        this.atlasManager = atlasManager;
        this.skinCache = skinCache;
        this.blockModelCache = new BlockModels(this);
    }

    public BlockStateModel getMissingModel() {
        return this.missingModels.block();
    }

    public ItemModel getItemModel(Identifier id) {
        return this.bakedItemModels.getOrDefault(id, this.missingModels.item());
    }

    public ItemAsset.Properties getItemProperties(Identifier id) {
        return this.itemProperties.getOrDefault(id, ItemAsset.Properties.DEFAULT);
    }

    public BlockModels getBlockModels() {
        return this.blockModelCache;
    }

    @Override
    public final CompletableFuture<Void> reload(ResourceReloader.Store store, Executor prepareExecutor, ResourceReloader.Synchronizer reloadSynchronizer, Executor applyExecutor) {
        ResourceManager lv = store.getResourceManager();
        CompletableFuture<LoadedEntityModels> completableFuture = CompletableFuture.supplyAsync(LoadedEntityModels::copy, prepareExecutor);
        CompletionStage completableFuture2 = completableFuture.thenApplyAsync(entityModels -> LoadedBlockEntityModels.fromModels(new SpecialModelRenderer.BakeContext.Simple((LoadedEntityModels)entityModels, this.atlasManager, this.skinCache)), prepareExecutor);
        CompletableFuture<Map<Identifier, UnbakedModel>> completableFuture3 = BakedModelManager.reloadModels(lv, prepareExecutor);
        CompletableFuture<BlockStatesLoader.LoadedModels> completableFuture4 = BlockStatesLoader.load(lv, prepareExecutor);
        CompletableFuture<ItemAssetsLoader.Result> completableFuture5 = ItemAssetsLoader.load(lv, prepareExecutor);
        CompletionStage completableFuture6 = CompletableFuture.allOf(completableFuture3, completableFuture4, completableFuture5).thenApplyAsync(async -> BakedModelManager.collect((Map)completableFuture3.join(), (BlockStatesLoader.LoadedModels)completableFuture4.join(), (ItemAssetsLoader.Result)completableFuture5.join()), prepareExecutor);
        CompletionStage completableFuture7 = completableFuture4.thenApplyAsync(definition -> BakedModelManager.group(this.colorMap, definition), prepareExecutor);
        AtlasManager.Stitch lv2 = store.getOrThrow(AtlasManager.stitchKey);
        CompletableFuture<SpriteLoader.StitchResult> completableFuture8 = lv2.getPreparations(Atlases.BLOCKS);
        CompletableFuture<SpriteLoader.StitchResult> completableFuture9 = lv2.getPreparations(Atlases.ITEMS);
        return ((CompletableFuture)((CompletableFuture)CompletableFuture.allOf(new CompletableFuture[]{completableFuture8, completableFuture9, completableFuture6, completableFuture7, completableFuture4, completableFuture5, completableFuture, completableFuture2, completableFuture3}).thenComposeAsync(v -> {
            SpriteLoader.StitchResult lv = (SpriteLoader.StitchResult)completableFuture8.join();
            SpriteLoader.StitchResult lv2 = (SpriteLoader.StitchResult)completableFuture9.join();
            Models lv3 = (Models)((CompletableFuture)completableFuture6).join();
            Object2IntMap object2IntMap = (Object2IntMap)((CompletableFuture)completableFuture7).join();
            Sets.SetView set = Sets.difference(((Map)completableFuture3.join()).keySet(), lv3.models.keySet());
            if (!set.isEmpty()) {
                LOGGER.debug("Unreferenced models: \n{}", (Object)set.stream().sorted().map(id -> "\t" + String.valueOf(id) + "\n").collect(Collectors.joining()));
            }
            ModelBaker lv4 = new ModelBaker((LoadedEntityModels)completableFuture.join(), this.atlasManager, this.skinCache, ((BlockStatesLoader.LoadedModels)completableFuture4.join()).models(), ((ItemAssetsLoader.Result)completableFuture5.join()).contents(), lv3.models(), lv3.missing());
            return BakedModelManager.bake(lv, lv2, lv4, object2IntMap, (LoadedEntityModels)completableFuture.join(), (LoadedBlockEntityModels)((CompletableFuture)completableFuture2).join(), prepareExecutor);
        }, prepareExecutor)).thenCompose(reloadSynchronizer::whenPrepared)).thenAcceptAsync(this::upload, applyExecutor);
    }

    private static CompletableFuture<Map<Identifier, UnbakedModel>> reloadModels(ResourceManager resourceManager, Executor executor) {
        return CompletableFuture.supplyAsync(() -> MODELS_FINDER.findResources(resourceManager), executor).thenCompose(models2 -> {
            ArrayList<CompletableFuture<@Nullable Pair>> list = new ArrayList<CompletableFuture<Pair>>(models2.size());
            for (Map.Entry entry : models2.entrySet()) {
                list.add(CompletableFuture.supplyAsync(() -> {
                    Pair<Identifier, JsonUnbakedModel> pair;
                    block8: {
                        Identifier lv = MODELS_FINDER.toResourceId((Identifier)entry.getKey());
                        @Nullable BufferedReader reader = ((Resource)entry.getValue()).getReader();
                        try {
                            pair = Pair.of(lv, JsonUnbakedModel.deserialize(reader));
                            if (reader == null) break block8;
                        } catch (Throwable throwable) {
                            try {
                                if (reader != null) {
                                    try {
                                        ((Reader)reader).close();
                                    } catch (Throwable throwable2) {
                                        throwable.addSuppressed(throwable2);
                                    }
                                }
                                throw throwable;
                            } catch (Exception exception) {
                                LOGGER.error("Failed to load model {}", entry.getKey(), (Object)exception);
                                return null;
                            }
                        }
                        ((Reader)reader).close();
                    }
                    return pair;
                }, executor));
            }
            return Util.combineSafe(list).thenApply(models -> models.stream().filter(Objects::nonNull).collect(Collectors.toUnmodifiableMap(Pair::getFirst, Pair::getSecond)));
        });
    }

    private static Models collect(Map<Identifier, UnbakedModel> modelMap, BlockStatesLoader.LoadedModels stateDefinition, ItemAssetsLoader.Result result) {
        try (ScopedProfiler lv = Profilers.get().scoped("dependencies");){
            ReferencedModelsCollector lv2 = new ReferencedModelsCollector(modelMap, MissingModel.create());
            lv2.addSpecialModel(GeneratedItemModel.GENERATED, new GeneratedItemModel());
            stateDefinition.models().values().forEach(lv2::resolve);
            result.contents().values().forEach(asset -> lv2.resolve(asset.model()));
            Models models = new Models(lv2.getMissingModel(), lv2.collectModels());
            return models;
        }
    }

    private static CompletableFuture<BakingResult> bake(final SpriteLoader.StitchResult blocksResult, final SpriteLoader.StitchResult itemsResult, ModelBaker baker, Object2IntMap<BlockState> groups, LoadedEntityModels entityModels, LoadedBlockEntityModels blockEntityModels, Executor executor) {
        final Multimap multimap = Multimaps.synchronizedMultimap(HashMultimap.create());
        final Multimap multimap2 = Multimaps.synchronizedMultimap(HashMultimap.create());
        return baker.bake(new ErrorCollectingSpriteGetter(){
            private final Sprite missingBlockSprite;
            private final Sprite missingItemSprite;
            {
                this.missingBlockSprite = blocksResult.missing();
                this.missingItemSprite = itemsResult.missing();
            }

            @Override
            public Sprite get(SpriteIdentifier id, SimpleModel model) {
                Sprite lv2;
                Identifier lv = id.getAtlasId();
                boolean bl = lv.equals(BLOCK_OR_ITEM);
                boolean bl2 = lv.equals(SpriteAtlasTexture.ITEMS_ATLAS_TEXTURE);
                boolean bl3 = lv.equals(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
                if ((bl || bl2) && (lv2 = itemsResult.getSprite(id.getTextureId())) != null) {
                    return lv2;
                }
                if ((bl || bl3) && (lv2 = blocksResult.getSprite(id.getTextureId())) != null) {
                    return lv2;
                }
                multimap.put(model.name(), id);
                return bl2 ? this.missingItemSprite : this.missingBlockSprite;
            }

            @Override
            public Sprite getMissing(String name, SimpleModel model) {
                multimap2.put(model.name(), name);
                return this.missingBlockSprite;
            }
        }, executor).thenApply(bakedModels -> {
            multimap.asMap().forEach((modelName, sprites) -> LOGGER.warn("Missing textures in model {}:\n{}", modelName, (Object)sprites.stream().sorted(SpriteIdentifier.COMPARATOR).map(spriteId -> "    " + String.valueOf(spriteId.getAtlasId()) + ":" + String.valueOf(spriteId.getTextureId())).collect(Collectors.joining("\n"))));
            multimap2.asMap().forEach((modelName, textureIds) -> LOGGER.warn("Missing texture references in model {}:\n{}", modelName, (Object)textureIds.stream().sorted().map(textureId -> "    " + textureId).collect(Collectors.joining("\n"))));
            Map<BlockState, BlockStateModel> map = BakedModelManager.toStateMap(bakedModels.blockStateModels(), bakedModels.missingModels().block());
            return new BakingResult((ModelBaker.BakedModels)bakedModels, groups, map, entityModels, blockEntityModels);
        });
    }

    private static Map<BlockState, BlockStateModel> toStateMap(Map<BlockState, BlockStateModel> blockStateModels, BlockStateModel missingModel) {
        try (ScopedProfiler lv = Profilers.get().scoped("block state dispatch");){
            IdentityHashMap<BlockState, BlockStateModel> map2 = new IdentityHashMap<BlockState, BlockStateModel>(blockStateModels);
            for (Block lv2 : Registries.BLOCK) {
                lv2.getStateManager().getStates().forEach(state -> {
                    if (blockStateModels.putIfAbsent((BlockState)state, missingModel) == null) {
                        LOGGER.warn("Missing model for variant: '{}'", state);
                    }
                });
            }
            IdentityHashMap<BlockState, BlockStateModel> identityHashMap = map2;
            return identityHashMap;
        }
    }

    private static Object2IntMap<BlockState> group(BlockColors colors, BlockStatesLoader.LoadedModels definition) {
        try (ScopedProfiler lv = Profilers.get().scoped("block groups");){
            Object2IntMap<BlockState> object2IntMap = ModelGrouper.group(colors, definition);
            return object2IntMap;
        }
    }

    private void upload(BakingResult bakingResult) {
        ModelBaker.BakedModels lv = bakingResult.bakedModels;
        this.bakedItemModels = lv.itemStackModels();
        this.itemProperties = lv.itemProperties();
        this.modelGroups = bakingResult.modelGroups;
        this.missingModels = lv.missingModels();
        this.blockModelCache.setModels(bakingResult.modelCache);
        this.blockEntityModels = bakingResult.specialBlockModelRenderer;
        this.entityModels = bakingResult.entityModelSet;
    }

    public boolean shouldRerender(BlockState from, BlockState to) {
        int j;
        if (from == to) {
            return false;
        }
        int i = this.modelGroups.getInt(from);
        if (i != -1 && i == (j = this.modelGroups.getInt(to))) {
            FluidState lv2;
            FluidState lv = from.getFluidState();
            return lv != (lv2 = to.getFluidState());
        }
        return true;
    }

    public LoadedBlockEntityModels getBlockEntityModelsSupplier() {
        return this.blockEntityModels;
    }

    public Supplier<LoadedEntityModels> getEntityModelsSupplier() {
        return () -> this.entityModels;
    }

    @Environment(value=EnvType.CLIENT)
    record Models(BakedSimpleModel missing, Map<Identifier, BakedSimpleModel> models) {
    }

    @Environment(value=EnvType.CLIENT)
    record BakingResult(ModelBaker.BakedModels bakedModels, Object2IntMap<BlockState> modelGroups, Map<BlockState, BlockStateModel> modelCache, LoadedEntityModels entityModelSet, LoadedBlockEntityModels specialBlockModelRenderer) {
    }
}

