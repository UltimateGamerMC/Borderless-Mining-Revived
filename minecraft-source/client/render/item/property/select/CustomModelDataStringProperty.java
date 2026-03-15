/*
 * External method calls:
 *   Lnet/minecraft/client/render/item/property/select/SelectProperty$Type;create(Lcom/mojang/serialization/MapCodec;Lcom/mojang/serialization/Codec;)Lnet/minecraft/client/render/item/property/select/SelectProperty$Type;
 */
package net.minecraft.client.render.item.property.select;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.property.select.SelectProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.dynamic.Codecs;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public record CustomModelDataStringProperty(int index) implements SelectProperty<String>
{
    public static final PrimitiveCodec<String> VALUE_CODEC = Codec.STRING;
    public static final SelectProperty.Type<CustomModelDataStringProperty, String> TYPE = SelectProperty.Type.create(RecordCodecBuilder.mapCodec(instance -> instance.group(Codecs.NON_NEGATIVE_INT.optionalFieldOf("index", 0).forGetter(CustomModelDataStringProperty::index)).apply((Applicative<CustomModelDataStringProperty, ?>)instance, CustomModelDataStringProperty::new)), VALUE_CODEC);

    @Override
    public @Nullable String getValue(ItemStack arg, @Nullable ClientWorld arg2, @Nullable LivingEntity arg3, int i, ItemDisplayContext arg4) {
        CustomModelDataComponent lv = arg.get(DataComponentTypes.CUSTOM_MODEL_DATA);
        if (lv != null) {
            return lv.getString(this.index);
        }
        return null;
    }

    @Override
    public SelectProperty.Type<CustomModelDataStringProperty, String> getType() {
        return TYPE;
    }

    @Override
    public Codec<String> valueCodec() {
        return VALUE_CODEC;
    }

    @Override
    public /* synthetic */ @Nullable Object getValue(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity user, int seed, ItemDisplayContext displayContext) {
        return this.getValue(stack, world, user, seed, displayContext);
    }
}

