package net.minecraft.world.biome;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import java.util.OptionalInt;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.biome.Biome;

public record BiomeEffects(int waterColor, Optional<Integer> foliageColor, Optional<Integer> dryFoliageColor, Optional<Integer> grassColor, GrassColorModifier grassColorModifier) {
    public static final Codec<BiomeEffects> CODEC = RecordCodecBuilder.create(instance -> instance.group(((MapCodec)Codecs.HEX_RGB.fieldOf("water_color")).forGetter(BiomeEffects::waterColor), Codecs.HEX_RGB.optionalFieldOf("foliage_color").forGetter(BiomeEffects::foliageColor), Codecs.HEX_RGB.optionalFieldOf("dry_foliage_color").forGetter(BiomeEffects::dryFoliageColor), Codecs.HEX_RGB.optionalFieldOf("grass_color").forGetter(BiomeEffects::grassColor), GrassColorModifier.CODEC.optionalFieldOf("grass_color_modifier", GrassColorModifier.NONE).forGetter(BiomeEffects::grassColorModifier)).apply((Applicative<BiomeEffects, ?>)instance, BiomeEffects::new));

    public static enum GrassColorModifier implements StringIdentifiable
    {
        NONE("none"){

            @Override
            public int getModifiedGrassColor(double x, double z, int color) {
                return color;
            }
        }
        ,
        DARK_FOREST("dark_forest"){

            @Override
            public int getModifiedGrassColor(double x, double z, int color) {
                return (color & 0xFEFEFE) + 2634762 >> 1;
            }
        }
        ,
        SWAMP("swamp"){

            @Override
            public int getModifiedGrassColor(double x, double z, int color) {
                double f = Biome.FOLIAGE_NOISE.sample(x * 0.0225, z * 0.0225, false);
                if (f < -0.1) {
                    return 5011004;
                }
                return 6975545;
            }
        };

        private final String name;
        public static final Codec<GrassColorModifier> CODEC;

        public abstract int getModifiedGrassColor(double var1, double var3, int var5);

        GrassColorModifier(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        @Override
        public String asString() {
            return this.name;
        }

        static {
            CODEC = StringIdentifiable.createCodec(GrassColorModifier::values);
        }
    }

    public static class Builder {
        private OptionalInt waterColor = OptionalInt.empty();
        private Optional<Integer> foliageColor = Optional.empty();
        private Optional<Integer> dryFoliageColor = Optional.empty();
        private Optional<Integer> grassColor = Optional.empty();
        private GrassColorModifier grassColorModifier = GrassColorModifier.NONE;

        public Builder waterColor(int waterColor) {
            this.waterColor = OptionalInt.of(waterColor);
            return this;
        }

        public Builder foliageColor(int foliageColor) {
            this.foliageColor = Optional.of(foliageColor);
            return this;
        }

        public Builder dryFoliageColor(int dryFoliageColor) {
            this.dryFoliageColor = Optional.of(dryFoliageColor);
            return this;
        }

        public Builder grassColor(int grassColor) {
            this.grassColor = Optional.of(grassColor);
            return this;
        }

        public Builder grassColorModifier(GrassColorModifier grassColorModifier) {
            this.grassColorModifier = grassColorModifier;
            return this;
        }

        public BiomeEffects build() {
            return new BiomeEffects(this.waterColor.orElseThrow(() -> new IllegalStateException("Missing 'water' color.")), this.foliageColor, this.dryFoliageColor, this.grassColor, this.grassColorModifier);
        }
    }
}

