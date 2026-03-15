/*
 * External method calls:
 *   Lnet/minecraft/registry/MutableRegistry;freeze()Lnet/minecraft/registry/Registry;
 *   Lnet/minecraft/registry/DynamicRegistryManager$ImmutableImpl;toImmutable()Lnet/minecraft/registry/DynamicRegistryManager$Immutable;
 *   Lnet/minecraft/util/ErrorReporter$Impl;report(Lnet/minecraft/util/ErrorReporter$Error;)V
 *   Lnet/minecraft/registry/MutableRegistry;streamEntries()Ljava/util/stream/Stream;
 *   Lnet/minecraft/util/ErrorReporter$Impl;apply(Ljava/util/function/BiConsumer;)V
 *   Lnet/minecraft/data/DataOutput$PathResolver;resolveJson(Lnet/minecraft/util/Identifier;)Ljava/nio/file/Path;
 *   Lnet/minecraft/data/DataProvider;writeCodecToPath(Lnet/minecraft/data/DataWriter;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;Lcom/mojang/serialization/Codec;Ljava/lang/Object;Ljava/nio/file/Path;)Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/loot/LootTableReporter;withContextType(Lnet/minecraft/util/context/ContextType;)Lnet/minecraft/loot/LootTableReporter;
 *   Lnet/minecraft/registry/entry/RegistryEntry$Reference;registryKey()Lnet/minecraft/registry/RegistryKey;
 *   Lnet/minecraft/loot/LootTableReporter;makeChild(Lnet/minecraft/util/ErrorReporter$Context;Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/loot/LootTableReporter;
 *   Lnet/minecraft/loot/LootTable;validate(Lnet/minecraft/loot/LootTableReporter;)V
 *   Lnet/minecraft/data/loottable/LootTableProvider$LootTypeGenerator;provider()Ljava/util/function/Function;
 *   Lnet/minecraft/data/loottable/LootTableGenerator;accept(Ljava/util/function/BiConsumer;)V
 *   Lnet/minecraft/util/Util;logErrorOrPause(Ljava/lang/String;)V
 *   Lnet/minecraft/loot/LootTable$Builder;randomSequenceId(Lnet/minecraft/util/Identifier;)Lnet/minecraft/loot/LootTable$Builder;
 *   Lnet/minecraft/loot/LootTable$Builder;type(Lnet/minecraft/util/context/ContextType;)Lnet/minecraft/loot/LootTable$Builder;
 *   Lnet/minecraft/loot/LootTable$Builder;build()Lnet/minecraft/loot/LootTable;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/data/loottable/LootTableProvider;run(Lnet/minecraft/data/DataWriter;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Ljava/util/concurrent/CompletableFuture;
 */
package net.minecraft.data.loottable;

import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Lifecycle;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.data.loottable.LootTableGenerator;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableReporter;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.registry.entry.RegistryEntryInfo;
import net.minecraft.util.ErrorReporter;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.context.ContextType;
import net.minecraft.util.math.random.RandomSequence;
import org.slf4j.Logger;

public class LootTableProvider
implements DataProvider {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final DataOutput.PathResolver pathResolver;
    private final Set<RegistryKey<LootTable>> lootTableIds;
    private final List<LootTypeGenerator> lootTypeGenerators;
    private final CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture;

    public LootTableProvider(DataOutput output, Set<RegistryKey<LootTable>> lootTableIds, List<LootTypeGenerator> lootTypeGenerators, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        this.pathResolver = output.getResolver(RegistryKeys.LOOT_TABLE);
        this.lootTypeGenerators = lootTypeGenerators;
        this.lootTableIds = lootTableIds;
        this.registriesFuture = registriesFuture;
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        return this.registriesFuture.thenCompose(registries -> this.run(writer, (RegistryWrapper.WrapperLookup)registries));
    }

    private CompletableFuture<?> run(DataWriter writer, RegistryWrapper.WrapperLookup registries) {
        SimpleRegistry<LootTable> lv = new SimpleRegistry<LootTable>(RegistryKeys.LOOT_TABLE, Lifecycle.experimental());
        Object2ObjectOpenHashMap map = new Object2ObjectOpenHashMap();
        this.lootTypeGenerators.forEach(lootTypeGenerator -> lootTypeGenerator.provider().apply(registries).accept((lootTable, builder) -> {
            Identifier lv = LootTableProvider.getId(lootTable);
            Identifier lv2 = map.put(RandomSequence.createSeed(lv), lv);
            if (lv2 != null) {
                Util.logErrorOrPause("Loot table random sequence seed collision on " + String.valueOf(lv2) + " and " + String.valueOf(lootTable.getValue()));
            }
            builder.randomSequenceId(lv);
            LootTable lv3 = builder.type(arg.paramSet).build();
            lv.add((RegistryKey<LootTable>)lootTable, lv3, RegistryEntryInfo.DEFAULT);
        }));
        lv.freeze();
        ErrorReporter.Impl lv2 = new ErrorReporter.Impl();
        DynamicRegistryManager.Immutable lv3 = new DynamicRegistryManager.ImmutableImpl(List.of(lv)).toImmutable();
        LootTableReporter lv4 = new LootTableReporter(lv2, LootContextTypes.GENERIC, lv3);
        Sets.SetView<RegistryKey<LootTable>> set = Sets.difference(this.lootTableIds, lv.getKeys());
        for (RegistryKey registryKey : set) {
            lv2.report(new MissingTableError(registryKey));
        }
        lv.streamEntries().forEach(entry -> ((LootTable)entry.value()).validate(lv4.withContextType(((LootTable)entry.value()).getType()).makeChild(new ErrorReporter.LootTableContext(entry.registryKey()), entry.registryKey())));
        if (!lv2.isEmpty()) {
            lv2.apply((name, error) -> LOGGER.warn("Found validation problem in {}: {}", name, (Object)error.getMessage()));
            throw new IllegalStateException("Failed to validate loot tables, see logs");
        }
        return CompletableFuture.allOf((CompletableFuture[])lv.getEntrySet().stream().map(entry -> {
            RegistryKey lv = (RegistryKey)entry.getKey();
            LootTable lv2 = (LootTable)entry.getValue();
            Path path = this.pathResolver.resolveJson(lv.getValue());
            return DataProvider.writeCodecToPath(writer, registries, LootTable.CODEC, lv2, path);
        }).toArray(CompletableFuture[]::new));
    }

    private static Identifier getId(RegistryKey<LootTable> lootTableKey) {
        return lootTableKey.getValue();
    }

    @Override
    public final String getName() {
        return "Loot Tables";
    }

    public record MissingTableError(RegistryKey<LootTable> id) implements ErrorReporter.Error
    {
        @Override
        public String getMessage() {
            return "Missing built-in table: " + String.valueOf(this.id.getValue());
        }
    }

    public record LootTypeGenerator(Function<RegistryWrapper.WrapperLookup, LootTableGenerator> provider, ContextType paramSet) {
    }
}

