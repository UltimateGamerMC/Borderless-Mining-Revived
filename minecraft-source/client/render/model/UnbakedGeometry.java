/*
 * External method calls:
 *   Lnet/minecraft/client/render/model/json/ModelElement;from()Lorg/joml/Vector3fc;
 *   Lnet/minecraft/client/render/model/json/ModelElement;to()Lorg/joml/Vector3fc;
 *   Lnet/minecraft/client/render/model/json/ModelElement;faces()Ljava/util/Map;
 *   Lnet/minecraft/client/render/model/json/ModelElementFace;textureId()Ljava/lang/String;
 *   Lnet/minecraft/client/render/model/json/ModelElement;rotation()Lnet/minecraft/client/render/model/json/ModelElementRotation;
 *   Lnet/minecraft/client/render/model/BakedQuadFactory;bake(Lnet/minecraft/client/render/model/Baker$Vec3fInterner;Lorg/joml/Vector3fc;Lorg/joml/Vector3fc;Lnet/minecraft/client/render/model/json/ModelElementFace;Lnet/minecraft/client/texture/Sprite;Lnet/minecraft/util/math/Direction;Lnet/minecraft/client/render/model/ModelBakeSettings;Lnet/minecraft/client/render/model/json/ModelElementRotation;ZI)Lnet/minecraft/client/render/model/BakedQuad;
 *   Lnet/minecraft/client/render/model/json/ModelElementFace;cullFace()Lnet/minecraft/util/math/Direction;
 *   Lnet/minecraft/client/render/model/BakedGeometry$Builder;build()Lnet/minecraft/client/render/model/BakedGeometry;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/model/UnbakedGeometry;bakeGeometry(Ljava/util/List;Lnet/minecraft/client/render/model/ModelTextures;Lnet/minecraft/client/render/model/Baker;Lnet/minecraft/client/render/model/ModelBakeSettings;Lnet/minecraft/client/render/model/SimpleModel;)Lnet/minecraft/client/render/model/BakedGeometry;
 */
package net.minecraft.client.render.model;

import java.util.List;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.BakedGeometry;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BakedQuadFactory;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.Geometry;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelTextures;
import net.minecraft.client.render.model.SimpleModel;
import net.minecraft.client.render.model.json.ModelElement;
import net.minecraft.client.render.model.json.ModelElementFace;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.Direction;
import org.joml.Vector3fc;

@Environment(value=EnvType.CLIENT)
public record UnbakedGeometry(List<ModelElement> elements) implements Geometry
{
    @Override
    public BakedGeometry bake(ModelTextures arg, Baker arg2, ModelBakeSettings arg3, SimpleModel arg4) {
        return UnbakedGeometry.bakeGeometry(this.elements, arg, arg2, arg3, arg4);
    }

    public static BakedGeometry bakeGeometry(List<ModelElement> elements, ModelTextures textures, Baker baker, ModelBakeSettings settings, SimpleModel model) {
        BakedGeometry.Builder lv = new BakedGeometry.Builder();
        for (ModelElement lv2 : elements) {
            boolean bl = true;
            boolean bl2 = true;
            boolean bl3 = true;
            Vector3fc vector3fc = lv2.from();
            Vector3fc vector3fc2 = lv2.to();
            if (vector3fc.x() == vector3fc2.x()) {
                bl2 = false;
                bl3 = false;
            }
            if (vector3fc.y() == vector3fc2.y()) {
                bl = false;
                bl3 = false;
            }
            if (vector3fc.z() == vector3fc2.z()) {
                bl = false;
                bl2 = false;
            }
            if (!bl && !bl2 && !bl3) continue;
            for (Map.Entry<Direction, ModelElementFace> entry : lv2.faces().entrySet()) {
                boolean bl4;
                Direction lv3 = entry.getKey();
                ModelElementFace lv4 = entry.getValue();
                if (!(bl4 = (switch (lv3.getAxis()) {
                    default -> throw new MatchException(null, null);
                    case Direction.Axis.X -> bl;
                    case Direction.Axis.Y -> bl2;
                    case Direction.Axis.Z -> bl3;
                }))) continue;
                Sprite lv5 = baker.getSpriteGetter().get(textures, lv4.textureId(), model);
                BakedQuad lv6 = BakedQuadFactory.bake(baker.getVec3fInterner(), vector3fc, vector3fc2, lv4, lv5, lv3, settings, lv2.rotation(), lv2.shade(), lv2.lightEmission());
                if (lv4.cullFace() == null) {
                    lv.add(lv6);
                    continue;
                }
                lv.add(Direction.transform(settings.getRotation().getMatrix(), lv4.cullFace()), lv6);
            }
        }
        return lv.build();
    }
}

