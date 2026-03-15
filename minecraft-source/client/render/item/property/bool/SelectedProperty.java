package net.minecraft.client.render.item.property.bool;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.item.property.bool.BooleanProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public record SelectedProperty() implements BooleanProperty
{
    public static final MapCodec<SelectedProperty> CODEC = MapCodec.unit(new SelectedProperty());

    @Override
    public boolean test(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed, ItemDisplayContext displayContext) {
        ClientPlayerEntity lv;
        return entity instanceof ClientPlayerEntity && (lv = (ClientPlayerEntity)entity).getInventory().getSelectedStack() == stack;
    }

    public MapCodec<SelectedProperty> getCodec() {
        return CODEC;
    }
}

