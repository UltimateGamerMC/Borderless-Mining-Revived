/*
 * External method calls:
 *   Lnet/minecraft/client/option/KeyBinding;byId(Ljava/lang/String;)Lnet/minecraft/client/option/KeyBinding;
 */
package net.minecraft.client.render.item.property.bool;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.item.property.bool.BooleanProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public record KeybindDownProperty(KeyBinding keybind) implements BooleanProperty
{
    private static final Codec<KeyBinding> KEY_BINDING_CODEC = Codec.STRING.comapFlatMap(id -> {
        KeyBinding lv = KeyBinding.byId(id);
        return lv != null ? DataResult.success(lv) : DataResult.error(() -> "Invalid keybind: " + id);
    }, KeyBinding::getId);
    public static final MapCodec<KeybindDownProperty> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)KEY_BINDING_CODEC.fieldOf("keybind")).forGetter(KeybindDownProperty::keybind)).apply((Applicative<KeybindDownProperty, ?>)instance, KeybindDownProperty::new));

    @Override
    public boolean test(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed, ItemDisplayContext displayContext) {
        return this.keybind.isPressed();
    }

    public MapCodec<KeybindDownProperty> getCodec() {
        return CODEC;
    }
}

