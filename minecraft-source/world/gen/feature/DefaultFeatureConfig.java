package net.minecraft.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.gen.feature.FeatureConfig;

public class DefaultFeatureConfig
implements FeatureConfig {
    public static final DefaultFeatureConfig INSTANCE = new DefaultFeatureConfig();
    public static final Codec<DefaultFeatureConfig> CODEC = MapCodec.unitCodec(INSTANCE);
}

