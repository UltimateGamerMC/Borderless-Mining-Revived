/*
 * External method calls:
 *   Lnet/minecraft/world/gen/feature/BlockColumnFeatureConfig;layers()Ljava/util/List;
 *   Lnet/minecraft/world/gen/feature/BlockColumnFeatureConfig$Layer;height()Lnet/minecraft/util/math/intprovider/IntProvider;
 *   Lnet/minecraft/world/gen/feature/BlockColumnFeatureConfig;direction()Lnet/minecraft/util/math/Direction;
 *   Lnet/minecraft/world/gen/feature/BlockColumnFeatureConfig;allowedPlacement()Lnet/minecraft/world/gen/blockpredicate/BlockPredicate;
 *   Lnet/minecraft/world/gen/blockpredicate/BlockPredicate;test(Ljava/lang/Object;Ljava/lang/Object;)Z
 *   Lnet/minecraft/world/gen/feature/BlockColumnFeatureConfig$Layer;state()Lnet/minecraft/world/gen/stateprovider/BlockStateProvider;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/world/gen/feature/BlockColumnFeature;adjustLayerHeights([IIIZ)V
 */
package net.minecraft.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.BlockColumnFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class BlockColumnFeature
extends Feature<BlockColumnFeatureConfig> {
    public BlockColumnFeature(Codec<BlockColumnFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<BlockColumnFeatureConfig> context) {
        int l;
        StructureWorldAccess lv = context.getWorld();
        BlockColumnFeatureConfig lv2 = context.getConfig();
        Random lv3 = context.getRandom();
        int i = lv2.layers().size();
        int[] is = new int[i];
        int j = 0;
        for (int k = 0; k < i; ++k) {
            is[k] = lv2.layers().get(k).height().get(lv3);
            j += is[k];
        }
        if (j == 0) {
            return false;
        }
        BlockPos.Mutable lv4 = context.getOrigin().mutableCopy();
        BlockPos.Mutable lv5 = lv4.mutableCopy().move(lv2.direction());
        for (l = 0; l < j; ++l) {
            if (!lv2.allowedPlacement().test(lv, lv5)) {
                BlockColumnFeature.adjustLayerHeights(is, j, l, lv2.prioritizeTip());
                break;
            }
            lv5.move(lv2.direction());
        }
        for (l = 0; l < i; ++l) {
            int m = is[l];
            if (m == 0) continue;
            BlockColumnFeatureConfig.Layer lv6 = lv2.layers().get(l);
            for (int n = 0; n < m; ++n) {
                lv.setBlockState(lv4, lv6.state().get(lv3, lv4), Block.NOTIFY_LISTENERS);
                lv4.move(lv2.direction());
            }
        }
        return true;
    }

    private static void adjustLayerHeights(int[] layerHeights, int expectedHeight, int actualHeight, boolean prioritizeTip) {
        int q;
        int k = expectedHeight - actualHeight;
        int l = prioritizeTip ? 1 : -1;
        int m = prioritizeTip ? 0 : layerHeights.length - 1;
        int n = prioritizeTip ? layerHeights.length : -1;
        for (int o = m; o != n && k > 0; k -= q, o += l) {
            int p = layerHeights[o];
            q = Math.min(p, k);
            int n2 = o;
            layerHeights[n2] = layerHeights[n2] - q;
        }
    }
}

