/*
 * External method calls:
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/registry/Registry;register(Lnet/minecraft/registry/Registry;Lnet/minecraft/util/Identifier;Ljava/lang/Object;)Ljava/lang/Object;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/loot/provider/nbt/LootNbtProviderTypes;register(Ljava/lang/String;Lcom/mojang/serialization/MapCodec;)Lnet/minecraft/loot/provider/nbt/LootNbtProviderType;
 */
package net.minecraft.loot.provider.nbt;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.loot.provider.nbt.ContextLootNbtProvider;
import net.minecraft.loot.provider.nbt.LootNbtProvider;
import net.minecraft.loot.provider.nbt.LootNbtProviderType;
import net.minecraft.loot.provider.nbt.StorageLootNbtProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class LootNbtProviderTypes {
    private static final Codec<LootNbtProvider> BASE_CODEC = Registries.LOOT_NBT_PROVIDER_TYPE.getCodec().dispatch(LootNbtProvider::getType, LootNbtProviderType::codec);
    public static final Codec<LootNbtProvider> CODEC = Codec.lazyInitialized(() -> Codec.either(ContextLootNbtProvider.INLINE_CODEC, BASE_CODEC).xmap(Either::unwrap, provider -> {
        Either<Object, LootNbtProvider> either;
        if (provider instanceof ContextLootNbtProvider) {
            ContextLootNbtProvider lv = (ContextLootNbtProvider)provider;
            either = Either.left(lv);
        } else {
            either = Either.right(provider);
        }
        return either;
    }));
    public static final LootNbtProviderType STORAGE = LootNbtProviderTypes.register("storage", StorageLootNbtProvider.CODEC);
    public static final LootNbtProviderType CONTEXT = LootNbtProviderTypes.register("context", ContextLootNbtProvider.CODEC);

    private static LootNbtProviderType register(String id, MapCodec<? extends LootNbtProvider> codec) {
        return Registry.register(Registries.LOOT_NBT_PROVIDER_TYPE, Identifier.ofVanilla(id), new LootNbtProviderType(codec));
    }
}

