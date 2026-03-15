package net.minecraft.client.util.math;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public record Vector2f(float x, float y) {
    @Override
    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }

    public static long toLong(float x, float y) {
        long l = (long)Float.floatToIntBits(x) & 0xFFFFFFFFL;
        long m = (long)Float.floatToIntBits(y) & 0xFFFFFFFFL;
        return l << 32 | m;
    }

    public static float getX(long x) {
        int i = (int)(x >> 32);
        return Float.intBitsToFloat(i);
    }

    public static float getY(long y) {
        return Float.intBitsToFloat((int)y);
    }
}

