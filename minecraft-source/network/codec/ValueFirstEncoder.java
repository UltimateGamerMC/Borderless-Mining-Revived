package net.minecraft.network.codec;

@FunctionalInterface
public interface ValueFirstEncoder<O, T> {
    public void encode(T var1, O var2);
}

