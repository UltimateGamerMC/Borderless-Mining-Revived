package net.minecraft.network.codec;

@FunctionalInterface
public interface PacketDecoder<I, T> {
    public T decode(I var1);
}

