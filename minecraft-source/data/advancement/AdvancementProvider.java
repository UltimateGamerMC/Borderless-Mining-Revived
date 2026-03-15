/*
 * External method calls:
 *   Lnet/minecraft/data/advancement/AdvancementTabGenerator;accept(Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;Ljava/util/function/Consumer;)V
 *   Lnet/minecraft/advancement/AdvancementEntry;id()Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/data/DataOutput$PathResolver;resolveJson(Lnet/minecraft/util/Identifier;)Ljava/nio/file/Path;
 *   Lnet/minecraft/data/DataProvider;writeCodecToPath(Lnet/minecraft/data/DataWriter;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;Lcom/mojang/serialization/Codec;Ljava/lang/Object;Ljava/nio/file/Path;)Ljava/util/concurrent/CompletableFuture;
 */
package net.minecraft.data.advancement;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.data.advancement.AdvancementTabGenerator;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

public class AdvancementProvider
implements DataProvider {
    private final DataOutput.PathResolver pathResolver;
    private final List<AdvancementTabGenerator> tabGenerators;
    private final CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture;

    public AdvancementProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, List<AdvancementTabGenerator> tabGenerators) {
        this.pathResolver = output.getResolver(RegistryKeys.ADVANCEMENT);
        this.tabGenerators = tabGenerators;
        this.registriesFuture = registriesFuture;
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        return this.registriesFuture.thenCompose(registries -> {
            HashSet set = new HashSet();
            ArrayList list = new ArrayList();
            Consumer<AdvancementEntry> consumer = advancement -> {
                if (!set.add(advancement.id())) {
                    throw new IllegalStateException("Duplicate advancement " + String.valueOf(advancement.id()));
                }
                Path path = this.pathResolver.resolveJson(advancement.id());
                list.add(DataProvider.writeCodecToPath(writer, registries, Advancement.CODEC, advancement.value(), path));
            };
            for (AdvancementTabGenerator lv : this.tabGenerators) {
                lv.accept((RegistryWrapper.WrapperLookup)registries, consumer);
            }
            return CompletableFuture.allOf((CompletableFuture[])list.toArray(CompletableFuture[]::new));
        });
    }

    @Override
    public final String getName() {
        return "Advancements";
    }
}

