/*
 * External method calls:
 *   Lnet/minecraft/nbt/NbtHelper;matches(Lnet/minecraft/nbt/NbtElement;Lnet/minecraft/nbt/NbtElement;Z)Z
 *   Lnet/minecraft/network/codec/PacketCodec;xmap(Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/component/type/NbtComponent;apply(Ljava/util/function/Consumer;)Lnet/minecraft/component/type/NbtComponent;
 *   Lnet/minecraft/component/type/NbtComponent;of(Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/component/type/NbtComponent;
 */
package net.minecraft.component.type;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.Consumer;
import net.minecraft.component.ComponentType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public final class NbtComponent {
    public static final NbtComponent DEFAULT = new NbtComponent(new NbtCompound());
    public static final Codec<NbtCompound> COMPOUND_CODEC = Codec.withAlternative(NbtCompound.CODEC, StringNbtReader.STRINGIFIED_CODEC);
    public static final Codec<NbtComponent> CODEC = COMPOUND_CODEC.xmap(NbtComponent::new, component -> component.nbt);
    @Deprecated
    public static final PacketCodec<ByteBuf, NbtComponent> PACKET_CODEC = PacketCodecs.NBT_COMPOUND.xmap(NbtComponent::new, component -> component.nbt);
    private final NbtCompound nbt;

    private NbtComponent(NbtCompound nbt) {
        this.nbt = nbt;
    }

    public static NbtComponent of(NbtCompound nbt) {
        return new NbtComponent(nbt.copy());
    }

    public boolean matches(NbtCompound nbt) {
        return NbtHelper.matches(nbt, this.nbt, true);
    }

    public static void set(ComponentType<NbtComponent> type, ItemStack stack, Consumer<NbtCompound> nbtSetter) {
        NbtComponent lv = stack.getOrDefault(type, DEFAULT).apply(nbtSetter);
        if (lv.nbt.isEmpty()) {
            stack.remove(type);
        } else {
            stack.set(type, lv);
        }
    }

    public static void set(ComponentType<NbtComponent> type, ItemStack stack, NbtCompound nbt) {
        if (!nbt.isEmpty()) {
            stack.set(type, NbtComponent.of(nbt));
        } else {
            stack.remove(type);
        }
    }

    public NbtComponent apply(Consumer<NbtCompound> nbtConsumer) {
        NbtCompound lv = this.nbt.copy();
        nbtConsumer.accept(lv);
        return new NbtComponent(lv);
    }

    public boolean isEmpty() {
        return this.nbt.isEmpty();
    }

    public NbtCompound copyNbt() {
        return this.nbt.copy();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof NbtComponent) {
            NbtComponent lv = (NbtComponent)o;
            return this.nbt.equals(lv.nbt);
        }
        return false;
    }

    public int hashCode() {
        return this.nbt.hashCode();
    }

    public String toString() {
        return this.nbt.toString();
    }
}

