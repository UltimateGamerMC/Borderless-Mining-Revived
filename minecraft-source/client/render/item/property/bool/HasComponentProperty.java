package net.minecraft.client.render.item.property.bool;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.property.bool.BooleanProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public record HasComponentProperty(ComponentType<?> componentType, boolean ignoreDefault) implements BooleanProperty
{
    public static final MapCodec<HasComponentProperty> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Registries.DATA_COMPONENT_TYPE.getCodec().fieldOf("component")).forGetter(HasComponentProperty::componentType), Codec.BOOL.optionalFieldOf("ignore_default", false).forGetter(HasComponentProperty::ignoreDefault)).apply((Applicative<HasComponentProperty, ?>)instance, HasComponentProperty::new));

    @Override
    public boolean test(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed, ItemDisplayContext displayContext) {
        return this.ignoreDefault ? stack.hasChangedComponent(this.componentType) : stack.contains(this.componentType);
    }

    public MapCodec<HasComponentProperty> getCodec() {
        return CODEC;
    }
}

