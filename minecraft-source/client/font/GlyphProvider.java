package net.minecraft.client.font;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.BakedGlyph;
import net.minecraft.util.math.random.Random;

@Environment(value=EnvType.CLIENT)
public interface GlyphProvider {
    public BakedGlyph get(int var1);

    public BakedGlyph getObfuscated(Random var1, int var2);
}

