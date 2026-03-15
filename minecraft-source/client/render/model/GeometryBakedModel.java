/*
 * External method calls:
 *   Lnet/minecraft/client/render/model/BakedSimpleModel;bakeGeometry(Lnet/minecraft/client/render/model/ModelTextures;Lnet/minecraft/client/render/model/Baker;Lnet/minecraft/client/render/model/ModelBakeSettings;)Lnet/minecraft/client/render/model/BakedGeometry;
 *   Lnet/minecraft/client/render/model/BakedQuad;sprite()Lnet/minecraft/client/texture/Sprite;
 */
package net.minecraft.client.render.model;

import com.google.common.collect.HashMultimap;
import com.mojang.logging.LogUtils;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.BakedGeometry;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BakedSimpleModel;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelTextures;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(value=EnvType.CLIENT)
public record GeometryBakedModel(BakedGeometry quads, boolean useAmbientOcclusion, Sprite particleSprite) implements BlockModelPart
{
    private static final Logger LOGGER = LogUtils.getLogger();

    public static BlockModelPart create(Baker baker, Identifier id, ModelBakeSettings bakeSettings) {
        BakedSimpleModel lv = baker.getModel(id);
        ModelTextures lv2 = lv.getTextures();
        boolean bl = lv.getAmbientOcclusion();
        Sprite lv3 = lv.getParticleTexture(lv2, baker);
        BakedGeometry lv4 = lv.bakeGeometry(lv2, baker, bakeSettings);
        HashMultimap<Identifier, Identifier> multimap = null;
        for (BakedQuad lv5 : lv4.getAllQuads()) {
            Sprite lv6 = lv5.sprite();
            if (lv6.getAtlasId().equals(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)) continue;
            if (multimap == null) {
                multimap = HashMultimap.create();
            }
            multimap.put(lv6.getAtlasId(), lv6.getContents().getId());
        }
        if (multimap != null) {
            LOGGER.warn("Rejecting block model {}, since it contains sprites from outside of supported atlas: {}", (Object)id, (Object)multimap);
            return baker.getBlockPart();
        }
        return new GeometryBakedModel(lv4, bl, lv3);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction side) {
        return this.quads.getQuads(side);
    }
}

