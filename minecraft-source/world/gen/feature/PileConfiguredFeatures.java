/*
 * External method calls:
 *   Lnet/minecraft/world/gen/feature/ConfiguredFeatures;register(Lnet/minecraft/registry/Registerable;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/world/gen/feature/Feature;Lnet/minecraft/world/gen/feature/FeatureConfig;)V
 *   Lnet/minecraft/world/gen/stateprovider/BlockStateProvider;of(Lnet/minecraft/block/Block;)Lnet/minecraft/world/gen/stateprovider/SimpleBlockStateProvider;
 *   Lnet/minecraft/util/collection/Pool;builder()Lnet/minecraft/util/collection/Pool$Builder;
 *   Lnet/minecraft/world/gen/feature/ConfiguredFeatures;of(Ljava/lang/String;)Lnet/minecraft/registry/RegistryKey;
 */
package net.minecraft.world.gen.feature;

import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.collection.Pool;
import net.minecraft.world.gen.feature.BlockPileFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.PillarBlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;

public class PileConfiguredFeatures {
    public static final RegistryKey<ConfiguredFeature<?, ?>> PILE_HAY = ConfiguredFeatures.of("pile_hay");
    public static final RegistryKey<ConfiguredFeature<?, ?>> PILE_MELON = ConfiguredFeatures.of("pile_melon");
    public static final RegistryKey<ConfiguredFeature<?, ?>> PILE_SNOW = ConfiguredFeatures.of("pile_snow");
    public static final RegistryKey<ConfiguredFeature<?, ?>> PILE_ICE = ConfiguredFeatures.of("pile_ice");
    public static final RegistryKey<ConfiguredFeature<?, ?>> PILE_PUMPKIN = ConfiguredFeatures.of("pile_pumpkin");

    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> featureRegisterable) {
        ConfiguredFeatures.register(featureRegisterable, PILE_HAY, Feature.BLOCK_PILE, new BlockPileFeatureConfig(new PillarBlockStateProvider(Blocks.HAY_BLOCK)));
        ConfiguredFeatures.register(featureRegisterable, PILE_MELON, Feature.BLOCK_PILE, new BlockPileFeatureConfig(BlockStateProvider.of(Blocks.MELON)));
        ConfiguredFeatures.register(featureRegisterable, PILE_SNOW, Feature.BLOCK_PILE, new BlockPileFeatureConfig(BlockStateProvider.of(Blocks.SNOW)));
        ConfiguredFeatures.register(featureRegisterable, PILE_ICE, Feature.BLOCK_PILE, new BlockPileFeatureConfig(new WeightedBlockStateProvider(Pool.builder().add(Blocks.BLUE_ICE.getDefaultState(), 1).add(Blocks.PACKED_ICE.getDefaultState(), 5))));
        ConfiguredFeatures.register(featureRegisterable, PILE_PUMPKIN, Feature.BLOCK_PILE, new BlockPileFeatureConfig(new WeightedBlockStateProvider(Pool.builder().add(Blocks.PUMPKIN.getDefaultState(), 19).add(Blocks.JACK_O_LANTERN.getDefaultState(), 1))));
    }
}

