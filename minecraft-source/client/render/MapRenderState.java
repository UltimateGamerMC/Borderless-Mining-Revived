package net.minecraft.client.render;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.texture.Sprite;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class MapRenderState {
    public @Nullable Identifier texture;
    public final List<Decoration> decorations = new ArrayList<Decoration>();

    @Environment(value=EnvType.CLIENT)
    public static class Decoration {
        public @Nullable Sprite sprite;
        public byte x;
        public byte z;
        public byte rotation;
        public boolean alwaysRendered;
        public @Nullable Text name;
    }
}

