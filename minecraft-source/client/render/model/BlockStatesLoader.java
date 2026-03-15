/*
 * External method calls:
 *   Lnet/minecraft/client/render/model/BlockStateManagers;createIdToManagerMapper()Ljava/util/function/Function;
 *   Lnet/minecraft/client/render/model/json/BlockModelDefinition;load(Lnet/minecraft/state/StateManager;Ljava/util/function/Supplier;)Ljava/util/Map;
 *   Lnet/minecraft/util/Util;combineSafe(Ljava/util/List;)Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/client/render/model/BlockStatesLoader$LoadedModels;models()Ljava/util/Map;
 *   Lnet/minecraft/resource/ResourceFinder;toResourceId(Lnet/minecraft/util/Identifier;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/util/StrictJsonParser;parse(Ljava/io/Reader;)Lcom/google/gson/JsonElement;
 *   Lnet/minecraft/resource/ResourceFinder;findAllResources(Lnet/minecraft/resource/ResourceManager;)Ljava/util/Map;
 *   Lnet/minecraft/resource/ResourceFinder;json(Ljava/lang/String;)Lnet/minecraft/resource/ResourceFinder;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/model/BlockStatesLoader;combine(Lnet/minecraft/util/Identifier;Lnet/minecraft/state/StateManager;Ljava/util/List;)Lnet/minecraft/client/render/model/BlockStatesLoader$LoadedModels;
 */
package net.minecraft.client.render.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import java.io.BufferedReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BlockStateManagers;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.render.model.json.BlockModelDefinition;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.state.StateManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.StrictJsonParser;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(value=EnvType.CLIENT)
public class BlockStatesLoader {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final ResourceFinder FINDER = ResourceFinder.json("blockstates");

    /*
     * Issues handling annotations - annotations may be inaccurate
     */
    public static CompletableFuture<LoadedModels> load(ResourceManager resourceManager, Executor prepareExecutor) {
        Function<Identifier, @Nullable StateManager<Block, BlockState>> function = BlockStateManagers.createIdToManagerMapper();
        return CompletableFuture.supplyAsync(() -> FINDER.findAllResources(resourceManager), prepareExecutor).thenCompose(resourceMap -> {
            ArrayList<CompletableFuture<@Nullable LoadedModels>> list = new ArrayList<CompletableFuture<LoadedModels>>(resourceMap.size());
            for (Map.Entry entry : resourceMap.entrySet()) {
                list.add(CompletableFuture.supplyAsync(() -> {
                    @Nullable Identifier lv = FINDER.toResourceId((Identifier)entry.getKey());
                    @Nullable StateManager lv2 = (StateManager)function.apply(lv);
                    if (lv2 == null) {
                        LOGGER.debug("Discovered unknown block state definition {}, ignoring", (Object)lv);
                        return null;
                    }
                    List list = (List)entry.getValue();
                    ArrayList<LoadedBlockStateDefinition> list2 = new ArrayList<LoadedBlockStateDefinition>(list.size());
                    for (Resource lv3 : list) {
                        try {
                            BufferedReader reader = lv3.getReader();
                            try {
                                JsonElement jsonElement = StrictJsonParser.parse(reader);
                                BlockModelDefinition lv4 = (BlockModelDefinition)BlockModelDefinition.CODEC.parse(JsonOps.INSTANCE, jsonElement).getOrThrow(JsonParseException::new);
                                list2.add(new LoadedBlockStateDefinition(lv3.getPackId(), lv4));
                            } finally {
                                if (reader == null) continue;
                                ((Reader)reader).close();
                            }
                        } catch (Exception exception) {
                            LOGGER.error("Failed to load blockstate definition {} from pack {}", lv, lv3.getPackId(), exception);
                        }
                    }
                    try {
                        return BlockStatesLoader.combine(lv, lv2, list2);
                    } catch (Exception exception2) {
                        LOGGER.error("Failed to load blockstate definition {}", (Object)lv, (Object)exception2);
                        return null;
                    }
                }, prepareExecutor));
            }
            return Util.combineSafe(list).thenApply(definitions -> {
                IdentityHashMap<BlockState, BlockStateModel.UnbakedGrouped> map = new IdentityHashMap<BlockState, BlockStateModel.UnbakedGrouped>();
                for (LoadedModels lv : definitions) {
                    if (lv == null) continue;
                    map.putAll(lv.models());
                }
                return new LoadedModels(map);
            });
        });
    }

    private static LoadedModels combine(Identifier id, StateManager<Block, BlockState> stateManager, List<LoadedBlockStateDefinition> definitions) {
        IdentityHashMap<BlockState, BlockStateModel.UnbakedGrouped> map = new IdentityHashMap<BlockState, BlockStateModel.UnbakedGrouped>();
        for (LoadedBlockStateDefinition lv : definitions) {
            map.putAll(lv.contents.load(stateManager, () -> String.valueOf(id) + "/" + arg2.source));
        }
        return new LoadedModels(map);
    }

    @Environment(value=EnvType.CLIENT)
    record LoadedBlockStateDefinition(String source, BlockModelDefinition contents) {
    }

    @Environment(value=EnvType.CLIENT)
    public record LoadedModels(Map<BlockState, BlockStateModel.UnbakedGrouped> models) {
    }
}

