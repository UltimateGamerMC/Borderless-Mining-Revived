/*
 * External method calls:
 *   Lnet/minecraft/data/DataOutput;resolvePath(Lnet/minecraft/data/DataOutput$OutputType;)Ljava/nio/file/Path;
 *   Lnet/minecraft/data/DataProvider;writeToPath(Lnet/minecraft/data/DataWriter;Lcom/google/gson/JsonElement;Ljava/nio/file/Path;)Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/world/biome/source/MultiNoiseBiomeSourceParameterList$Preset;id()Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/registry/RegistryKey;createCodec(Lnet/minecraft/registry/RegistryKey;)Lcom/mojang/serialization/Codec;
 *   Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$Entries;createCodec(Lcom/mojang/serialization/MapCodec;)Lcom/mojang/serialization/Codec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/data/report/BiomeParametersProvider;resolvePath(Lnet/minecraft/util/Identifier;)Ljava/nio/file/Path;
 *   Lnet/minecraft/data/report/BiomeParametersProvider;write(Ljava/nio/file/Path;Lnet/minecraft/data/DataWriter;Lcom/mojang/serialization/DynamicOps;Lcom/mojang/serialization/Encoder;Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;
 */
package net.minecraft.data.report;

import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSourceParameterList;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import org.slf4j.Logger;

public class BiomeParametersProvider
implements DataProvider {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final Path path;
    private final CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture;
    private static final MapCodec<RegistryKey<Biome>> BIOME_KEY_CODEC = RegistryKey.createCodec(RegistryKeys.BIOME).fieldOf("biome");
    private static final Codec<MultiNoiseUtil.Entries<RegistryKey<Biome>>> BIOME_ENTRY_CODEC = ((MapCodec)MultiNoiseUtil.Entries.createCodec(BIOME_KEY_CODEC).fieldOf("biomes")).codec();

    public BiomeParametersProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        this.path = output.resolvePath(DataOutput.OutputType.REPORTS).resolve("biome_parameters");
        this.registriesFuture = registriesFuture;
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        return this.registriesFuture.thenCompose(registries -> {
            RegistryOps<JsonElement> dynamicOps = registries.getOps(JsonOps.INSTANCE);
            ArrayList list = new ArrayList();
            MultiNoiseBiomeSourceParameterList.getPresetToEntriesMap().forEach((preset, entries) -> list.add(BiomeParametersProvider.write(this.resolvePath(preset.id()), writer, dynamicOps, BIOME_ENTRY_CODEC, entries)));
            return CompletableFuture.allOf((CompletableFuture[])list.toArray(CompletableFuture[]::new));
        });
    }

    private static <E> CompletableFuture<?> write(Path path, DataWriter writer, DynamicOps<JsonElement> ops, Encoder<E> codec, E biomeSource) {
        Optional<JsonElement> optional = codec.encodeStart(ops, biomeSource).resultOrPartial(error -> LOGGER.error("Couldn't serialize element {}: {}", (Object)path, error));
        if (optional.isPresent()) {
            return DataProvider.writeToPath(writer, optional.get(), path);
        }
        return CompletableFuture.completedFuture(null);
    }

    private Path resolvePath(Identifier id) {
        return this.path.resolve(id.getNamespace()).resolve(id.getPath() + ".json");
    }

    @Override
    public final String getName() {
        return "Biome Parameters";
    }
}

