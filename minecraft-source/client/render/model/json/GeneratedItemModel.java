/*
 * External method calls:
 *   Lnet/minecraft/client/render/model/UnbakedGeometry;bakeGeometry(Ljava/util/List;Lnet/minecraft/client/render/model/ModelTextures;Lnet/minecraft/client/render/model/Baker;Lnet/minecraft/client/render/model/ModelBakeSettings;Lnet/minecraft/client/render/model/SimpleModel;)Lnet/minecraft/client/render/model/BakedGeometry;
 *   Lnet/minecraft/client/render/model/json/GeneratedItemModel$class_12295;facing()Lnet/minecraft/client/render/model/json/GeneratedItemModel$Side;
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/client/render/model/ModelTextures$Textures$Builder;addTextureReference(Ljava/lang/String;Ljava/lang/String;)Lnet/minecraft/client/render/model/ModelTextures$Textures$Builder;
 *   Lnet/minecraft/client/render/model/ModelTextures$Textures$Builder;build()Lnet/minecraft/client/render/model/ModelTextures$Textures;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/model/json/GeneratedItemModel;addLayerElements(ILjava/lang/String;Lnet/minecraft/client/texture/SpriteContents;)Ljava/util/List;
 *   Lnet/minecraft/client/render/model/json/GeneratedItemModel;addSubComponents(Lnet/minecraft/client/texture/SpriteContents;Ljava/lang/String;I)Ljava/util/List;
 *   Lnet/minecraft/client/render/model/json/GeneratedItemModel;buildCube(Lnet/minecraft/client/render/model/json/GeneratedItemModel$Side;Ljava/util/Set;Lnet/minecraft/client/texture/SpriteContents;IIIII)V
 */
package net.minecraft.client.render.model.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.BakedGeometry;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.Geometry;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelTextures;
import net.minecraft.client.render.model.SimpleModel;
import net.minecraft.client.render.model.UnbakedGeometry;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.ModelElement;
import net.minecraft.client.render.model.json.ModelElementFace;
import net.minecraft.client.texture.SpriteContents;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.AxisRotation;
import net.minecraft.util.math.Direction;
import org.joml.Vector3f;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class GeneratedItemModel
implements UnbakedModel {
    public static final Identifier GENERATED = Identifier.ofVanilla("builtin/generated");
    public static final List<String> LAYERS = List.of("layer0", "layer1", "layer2", "layer3", "layer4");
    private static final float field_32806 = 7.5f;
    private static final float field_32807 = 8.5f;
    private static final ModelTextures.Textures TEXTURES = new ModelTextures.Textures.Builder().addTextureReference("particle", "layer0").build();
    private static final ModelElementFace.UV FACING_SOUTH_UV = new ModelElementFace.UV(0.0f, 0.0f, 16.0f, 16.0f);
    private static final ModelElementFace.UV FACING_NORTH_UV = new ModelElementFace.UV(16.0f, 0.0f, 0.0f, 16.0f);
    private static final float field_64230 = 0.1f;

    @Override
    public ModelTextures.Textures textures() {
        return TEXTURES;
    }

    @Override
    public Geometry geometry() {
        return GeneratedItemModel::bakeGeometry;
    }

    @Override
    public @Nullable UnbakedModel.GuiLight guiLight() {
        return UnbakedModel.GuiLight.ITEM;
    }

    private static BakedGeometry bakeGeometry(ModelTextures textures, Baker baker, ModelBakeSettings settings, SimpleModel model) {
        String string;
        SpriteIdentifier lv;
        ArrayList<ModelElement> list = new ArrayList<ModelElement>();
        for (int i = 0; i < LAYERS.size() && (lv = textures.get(string = LAYERS.get(i))) != null; ++i) {
            SpriteContents lv2 = baker.getSpriteGetter().get(lv, model).getContents();
            list.addAll(GeneratedItemModel.addLayerElements(i, string, lv2));
        }
        return UnbakedGeometry.bakeGeometry(list, textures, baker, settings, model);
    }

    private static List<ModelElement> addLayerElements(int tintIndex, String name, SpriteContents arg) {
        Map<Direction, ModelElementFace> map = Map.of(Direction.SOUTH, new ModelElementFace(null, tintIndex, name, FACING_SOUTH_UV, AxisRotation.R0), Direction.NORTH, new ModelElementFace(null, tintIndex, name, FACING_NORTH_UV, AxisRotation.R0));
        ArrayList<ModelElement> list = new ArrayList<ModelElement>();
        list.add(new ModelElement(new Vector3f(0.0f, 0.0f, 7.5f), new Vector3f(16.0f, 16.0f, 8.5f), map));
        list.addAll(GeneratedItemModel.addSubComponents(arg, name, tintIndex));
        return list;
    }

    private static List<ModelElement> addSubComponents(SpriteContents sprite, String textureId, int tintIndex) {
        float f = 16.0f / (float)sprite.getWidth();
        float g = 16.0f / (float)sprite.getHeight();
        ArrayList<ModelElement> list = new ArrayList<ModelElement>();
        for (class_12295 lv : GeneratedItemModel.getFrames(sprite)) {
            float n;
            float m;
            float h = lv.x();
            float j = lv.y();
            Side lv2 = lv.facing();
            float k = h + 0.1f;
            float l = h + 1.0f - 0.1f;
            if (lv2.isVertical()) {
                m = j + 0.1f;
                n = j + 1.0f - 0.1f;
            } else {
                m = j + 1.0f - 0.1f;
                n = j + 0.1f;
            }
            float o = h;
            float p = j;
            float q = h;
            float r = j;
            switch (lv2.ordinal()) {
                case 0: {
                    q += 1.0f;
                    break;
                }
                case 1: {
                    q += 1.0f;
                    p += 1.0f;
                    r += 1.0f;
                    break;
                }
                case 2: {
                    r += 1.0f;
                    break;
                }
                case 3: {
                    o += 1.0f;
                    q += 1.0f;
                    r += 1.0f;
                }
            }
            o *= f;
            q *= f;
            p *= g;
            r *= g;
            p = 16.0f - p;
            r = 16.0f - r;
            Map<Direction, ModelElementFace> map = Map.of(lv2.getDirection(), new ModelElementFace(null, tintIndex, textureId, new ModelElementFace.UV(k * f, m * f, l * g, n * g), AxisRotation.R0));
            switch (lv2.ordinal()) {
                case 0: {
                    list.add(new ModelElement(new Vector3f(o, p, 7.5f), new Vector3f(q, p, 8.5f), map));
                    break;
                }
                case 1: {
                    list.add(new ModelElement(new Vector3f(o, r, 7.5f), new Vector3f(q, r, 8.5f), map));
                    break;
                }
                case 2: {
                    list.add(new ModelElement(new Vector3f(o, p, 7.5f), new Vector3f(o, r, 8.5f), map));
                    break;
                }
                case 3: {
                    list.add(new ModelElement(new Vector3f(q, p, 7.5f), new Vector3f(q, r, 8.5f), map));
                }
            }
        }
        return list;
    }

    private static Collection<class_12295> getFrames(SpriteContents arg) {
        int i = arg.getWidth();
        int j = arg.getHeight();
        HashSet<class_12295> set = new HashSet<class_12295>();
        arg.getDistinctFrameCount().forEach(k -> {
            for (int l = 0; l < j; ++l) {
                for (int m = 0; m < i; ++m) {
                    boolean bl;
                    boolean bl2 = bl = !GeneratedItemModel.isPixelTransparent(arg, k, m, l, i, j);
                    if (!bl) continue;
                    GeneratedItemModel.buildCube(Side.UP, set, arg, k, m, l, i, j);
                    GeneratedItemModel.buildCube(Side.DOWN, set, arg, k, m, l, i, j);
                    GeneratedItemModel.buildCube(Side.LEFT, set, arg, k, m, l, i, j);
                    GeneratedItemModel.buildCube(Side.RIGHT, set, arg, k, m, l, i, j);
                }
            }
        });
        return set;
    }

    private static void buildCube(Side arg, Set<class_12295> set, SpriteContents arg2, int i, int j, int k, int l, int m) {
        if (GeneratedItemModel.isPixelTransparent(arg2, i, j - arg.direction.getOffsetX(), k - arg.direction.getOffsetY(), l, m)) {
            set.add(new class_12295(arg, j, k));
        }
    }

    private static boolean isPixelTransparent(SpriteContents arg, int i, int j, int k, int l, int m) {
        if (j < 0 || k < 0 || j >= l || k >= m) {
            return true;
        }
        return arg.isPixelTransparent(i, j, k);
    }

    @Environment(value=EnvType.CLIENT)
    record class_12295(Side facing, int x, int y) {
    }

    @Environment(value=EnvType.CLIENT)
    static enum Side {
        UP(Direction.UP),
        DOWN(Direction.DOWN),
        LEFT(Direction.EAST),
        RIGHT(Direction.WEST);

        final Direction direction;

        private Side(Direction direction) {
            this.direction = direction;
        }

        public Direction getDirection() {
            return this.direction;
        }

        boolean isVertical() {
            return this == DOWN || this == UP;
        }
    }
}

