/*
 * External method calls:
 *   Lnet/minecraft/util/Util;copyShuffled(Lit/unimi/dsi/fastutil/objects/ObjectArrayList;Lnet/minecraft/util/math/random/Random;)Ljava/util/List;
 *   Lnet/minecraft/block/BlockState;with(Lnet/minecraft/state/property/Property;Ljava/lang/Comparable;)Ljava/lang/Object;
 *   Lnet/minecraft/world/gen/treedecorator/TreeDecorator$Generator;replace(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V
 *   Lnet/minecraft/world/StructureWorldAccess;toServerWorld()Lnet/minecraft/server/world/ServerWorld;
 *   Lnet/minecraft/world/gen/feature/ConfiguredFeature;generate(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/util/math/random/Random;Lnet/minecraft/util/math/BlockPos;)Z
 *
 * Internal private/static methods:
 *   Lnet/minecraft/world/gen/treedecorator/PaleMossTreeDecorator;decorate(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/gen/treedecorator/TreeDecorator$Generator;)V
 */
package net.minecraft.world.gen.treedecorator;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HangingMossBlock;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.VegetationConfiguredFeatures;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

public class PaleMossTreeDecorator
extends TreeDecorator {
    public static final MapCodec<PaleMossTreeDecorator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Codec.floatRange(0.0f, 1.0f).fieldOf("leaves_probability")).forGetter(treeDecorator -> Float.valueOf(treeDecorator.leavesProbability)), ((MapCodec)Codec.floatRange(0.0f, 1.0f).fieldOf("trunk_probability")).forGetter(treeDecorator -> Float.valueOf(treeDecorator.trunkProbability)), ((MapCodec)Codec.floatRange(0.0f, 1.0f).fieldOf("ground_probability")).forGetter(treeDecorator -> Float.valueOf(treeDecorator.groundProbability))).apply((Applicative<PaleMossTreeDecorator, ?>)instance, PaleMossTreeDecorator::new));
    private final float leavesProbability;
    private final float trunkProbability;
    private final float groundProbability;

    @Override
    protected TreeDecoratorType<?> getType() {
        return TreeDecoratorType.PALE_MOSS;
    }

    public PaleMossTreeDecorator(float leavesProbability, float trunkProbability, float groundProbability) {
        this.leavesProbability = leavesProbability;
        this.trunkProbability = trunkProbability;
        this.groundProbability = groundProbability;
    }

    @Override
    public void generate(TreeDecorator.Generator generator) {
        Random lv = generator.getRandom();
        StructureWorldAccess lv2 = (StructureWorldAccess)generator.getWorld();
        List<BlockPos> list = Util.copyShuffled(generator.getLogPositions(), lv);
        if (list.isEmpty()) {
            return;
        }
        BlockPos lv3 = Collections.min(list, Comparator.comparingInt(Vec3i::getY));
        if (lv.nextFloat() < this.groundProbability) {
            lv2.getRegistryManager().getOptional(RegistryKeys.CONFIGURED_FEATURE).flatMap(registry -> registry.getOptional(VegetationConfiguredFeatures.PALE_MOSS_PATCH)).ifPresent(entry -> ((ConfiguredFeature)entry.value()).generate(lv2, lv2.toServerWorld().getChunkManager().getChunkGenerator(), lv, lv3.up()));
        }
        generator.getLogPositions().forEach(pos -> {
            BlockPos lv;
            if (lv.nextFloat() < this.trunkProbability && generator.isAir(lv = pos.down())) {
                PaleMossTreeDecorator.decorate(lv, generator);
            }
        });
        generator.getLeavesPositions().forEach(pos -> {
            BlockPos lv;
            if (lv.nextFloat() < this.leavesProbability && generator.isAir(lv = pos.down())) {
                PaleMossTreeDecorator.decorate(lv, generator);
            }
        });
    }

    private static void decorate(BlockPos pos, TreeDecorator.Generator generator) {
        while (generator.isAir(pos.down()) && !((double)generator.getRandom().nextFloat() < 0.5)) {
            generator.replace(pos, (BlockState)Blocks.PALE_HANGING_MOSS.getDefaultState().with(HangingMossBlock.TIP, false));
            pos = pos.down();
        }
        generator.replace(pos, (BlockState)Blocks.PALE_HANGING_MOSS.getDefaultState().with(HangingMossBlock.TIP, true));
    }
}

