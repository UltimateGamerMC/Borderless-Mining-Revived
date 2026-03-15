package net.minecraft.client.render.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.Geometry;
import net.minecraft.client.render.model.ModelTextures;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.util.Identifier;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public interface UnbakedModel {
    public static final String PARTICLE_TEXTURE = "particle";

    default public @Nullable Boolean ambientOcclusion() {
        return null;
    }

    default public @Nullable GuiLight guiLight() {
        return null;
    }

    default public @Nullable ModelTransformation transformations() {
        return null;
    }

    default public ModelTextures.Textures textures() {
        return ModelTextures.Textures.EMPTY;
    }

    default public @Nullable Geometry geometry() {
        return null;
    }

    default public @Nullable Identifier parent() {
        return null;
    }

    @Environment(value=EnvType.CLIENT)
    public static enum GuiLight {
        ITEM("front"),
        BLOCK("side");

        private final String name;

        private GuiLight(String name) {
            this.name = name;
        }

        public static GuiLight byName(String value) {
            for (GuiLight lv : GuiLight.values()) {
                if (!lv.name.equals(value)) continue;
                return lv;
            }
            throw new IllegalArgumentException("Invalid gui light: " + value);
        }

        public boolean isSide() {
            return this == BLOCK;
        }
    }
}

