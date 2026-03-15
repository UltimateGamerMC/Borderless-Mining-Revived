package net.minecraft.client.item;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelTypes;
import net.minecraft.registry.ContextSwapper;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public record ItemAsset(ItemModel.Unbaked model, Properties properties, @Nullable ContextSwapper registrySwapper) {
    public static final Codec<ItemAsset> CODEC = RecordCodecBuilder.create(instance -> instance.group(((MapCodec)ItemModelTypes.CODEC.fieldOf("model")).forGetter(ItemAsset::model), Properties.CODEC.forGetter(ItemAsset::properties)).apply((Applicative<ItemAsset, ?>)instance, ItemAsset::new));

    public ItemAsset(ItemModel.Unbaked model, Properties properties) {
        this(model, properties, null);
    }

    public ItemAsset withContextSwapper(ContextSwapper contextSwapper) {
        return new ItemAsset(this.model, this.properties, contextSwapper);
    }

    @Environment(value=EnvType.CLIENT)
    public record Properties(boolean handAnimationOnSwap, boolean oversizedInGui, float swapAnimationScale) {
        public static final Properties DEFAULT = new Properties(true, false, 1.0f);
        public static final MapCodec<Properties> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.BOOL.optionalFieldOf("hand_animation_on_swap", true).forGetter(Properties::handAnimationOnSwap), Codec.BOOL.optionalFieldOf("oversized_in_gui", false).forGetter(Properties::oversizedInGui), Codec.FLOAT.optionalFieldOf("swap_animation_scale", Float.valueOf(1.0f)).forGetter(Properties::swapAnimationScale)).apply((Applicative<Properties, ?>)instance, Properties::new));
    }
}

