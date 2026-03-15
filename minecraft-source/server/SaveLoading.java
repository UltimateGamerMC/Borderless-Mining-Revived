/*
 * External method calls:
 *   Lnet/minecraft/server/SaveLoading$DataPacks;load()Lcom/mojang/datafixers/util/Pair;
 *   Lnet/minecraft/registry/ServerDynamicRegistryType;createCombinedDynamicRegistries()Lnet/minecraft/registry/CombinedDynamicRegistries;
 *   Lnet/minecraft/registry/tag/TagGroupLoader;startReload(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/registry/DynamicRegistryManager;)Ljava/util/List;
 *   Lnet/minecraft/registry/tag/TagGroupLoader;collectRegistries(Lnet/minecraft/registry/DynamicRegistryManager$Immutable;Ljava/util/List;)Ljava/util/List;
 *   Lnet/minecraft/registry/RegistryLoader;loadFromResource(Lnet/minecraft/resource/ResourceManager;Ljava/util/List;Ljava/util/List;)Lnet/minecraft/registry/DynamicRegistryManager$Immutable;
 *   Lnet/minecraft/registry/DynamicRegistryManager$Immutable;stream()Ljava/util/stream/Stream;
 *   Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;of(Ljava/util/stream/Stream;)Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;
 *   Lnet/minecraft/registry/CombinedDynamicRegistries;with(Ljava/lang/Object;[Lnet/minecraft/registry/DynamicRegistryManager$Immutable;)Lnet/minecraft/registry/CombinedDynamicRegistries;
 *   Lnet/minecraft/resource/DataConfiguration;enabledFeatures()Lnet/minecraft/resource/featuretoggle/FeatureSet;
 *   Lnet/minecraft/server/SaveLoading$ServerConfig;commandEnvironment()Lnet/minecraft/server/command/CommandManager$RegistrationEnvironment;
 *   Lnet/minecraft/server/SaveLoading$ServerConfig;functionCompilationPermissions()Lnet/minecraft/command/permission/PermissionPredicate;
 *   Lnet/minecraft/server/DataPackContents;reload(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/registry/CombinedDynamicRegistries;Ljava/util/List;Lnet/minecraft/resource/featuretoggle/FeatureSet;Lnet/minecraft/server/command/CommandManager$RegistrationEnvironment;Lnet/minecraft/command/permission/PermissionPredicate;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/server/SaveLoading$SaveApplierFactory;create(Lnet/minecraft/resource/LifecycledResourceManager;Lnet/minecraft/server/DataPackContents;Lnet/minecraft/registry/CombinedDynamicRegistries;Ljava/lang/Object;)Ljava/lang/Object;
 */
package net.minecraft.server;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Stream;
import net.minecraft.command.permission.PermissionPredicate;
import net.minecraft.registry.CombinedDynamicRegistries;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryLoader;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.ServerDynamicRegistryType;
import net.minecraft.registry.tag.TagGroupLoader;
import net.minecraft.resource.DataConfiguration;
import net.minecraft.resource.LifecycledResourceManager;
import net.minecraft.resource.LifecycledResourceManagerImpl;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.DataPackContents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import org.slf4j.Logger;

public class SaveLoading {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static <D, R> CompletableFuture<R> load(ServerConfig serverConfig, LoadContextSupplier<D> loadContextSupplier, SaveApplierFactory<D, R> saveApplierFactory, Executor prepareExecutor, Executor applyExecutor) {
        try {
            Pair<DataConfiguration, LifecycledResourceManager> pair = serverConfig.dataPacks.load();
            LifecycledResourceManager lv = pair.getSecond();
            CombinedDynamicRegistries<ServerDynamicRegistryType> lv2 = ServerDynamicRegistryType.createCombinedDynamicRegistries();
            List<Registry.PendingTagLoad<?>> list = TagGroupLoader.startReload((ResourceManager)lv, lv2.get(ServerDynamicRegistryType.STATIC));
            DynamicRegistryManager.Immutable lv3 = lv2.getPrecedingRegistryManagers(ServerDynamicRegistryType.WORLDGEN);
            List<RegistryWrapper.Impl<?>> list2 = TagGroupLoader.collectRegistries(lv3, list);
            DynamicRegistryManager.Immutable lv4 = RegistryLoader.loadFromResource(lv, list2, RegistryLoader.DYNAMIC_REGISTRIES);
            List<RegistryWrapper.Impl<?>> list3 = Stream.concat(list2.stream(), lv4.stream()).toList();
            DynamicRegistryManager.Immutable lv5 = RegistryLoader.loadFromResource(lv, list3, RegistryLoader.DIMENSION_REGISTRIES);
            DataConfiguration lv6 = pair.getFirst();
            RegistryWrapper.WrapperLookup lv7 = RegistryWrapper.WrapperLookup.of(list3.stream());
            LoadContext<D> lv8 = loadContextSupplier.get(new LoadContextSupplierContext(lv, lv6, lv7, lv5));
            CombinedDynamicRegistries<ServerDynamicRegistryType> lv9 = lv2.with(ServerDynamicRegistryType.WORLDGEN, lv4, lv8.dimensionsRegistryManager);
            return ((CompletableFuture)DataPackContents.reload(lv, lv9, list, lv6.enabledFeatures(), serverConfig.commandEnvironment(), serverConfig.functionCompilationPermissions(), prepareExecutor, applyExecutor).whenComplete((dataPackContents, throwable) -> {
                if (throwable != null) {
                    lv.close();
                }
            })).thenApplyAsync(dataPackContents -> {
                dataPackContents.applyPendingTagLoads();
                return saveApplierFactory.create(lv, (DataPackContents)dataPackContents, lv9, arg4.extraData);
            }, applyExecutor);
        } catch (Exception exception) {
            return CompletableFuture.failedFuture(exception);
        }
    }

    public record ServerConfig(DataPacks dataPacks, CommandManager.RegistrationEnvironment commandEnvironment, PermissionPredicate functionCompilationPermissions) {
    }

    public record DataPacks(ResourcePackManager manager, DataConfiguration initialDataConfig, boolean safeMode, boolean initMode) {
        public Pair<DataConfiguration, LifecycledResourceManager> load() {
            DataConfiguration lv = MinecraftServer.loadDataPacks(this.manager, this.initialDataConfig, this.initMode, this.safeMode);
            List<ResourcePack> list = this.manager.createResourcePacks();
            LifecycledResourceManagerImpl lv2 = new LifecycledResourceManagerImpl(ResourceType.SERVER_DATA, list);
            return Pair.of(lv, lv2);
        }
    }

    public record LoadContextSupplierContext(ResourceManager resourceManager, DataConfiguration dataConfiguration, RegistryWrapper.WrapperLookup worldGenRegistryManager, DynamicRegistryManager.Immutable dimensionsRegistryManager) {
    }

    @FunctionalInterface
    public static interface LoadContextSupplier<D> {
        public LoadContext<D> get(LoadContextSupplierContext var1);
    }

    public record LoadContext<D>(D extraData, DynamicRegistryManager.Immutable dimensionsRegistryManager) {
    }

    @FunctionalInterface
    public static interface SaveApplierFactory<D, R> {
        public R create(LifecycledResourceManager var1, DataPackContents var2, CombinedDynamicRegistries<ServerDynamicRegistryType> var3, D var4);
    }
}

