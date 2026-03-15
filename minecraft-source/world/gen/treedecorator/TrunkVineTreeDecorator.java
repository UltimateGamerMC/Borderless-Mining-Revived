/*
 * External method calls:
 *   Lnet/minecraft/world/gen/treedecorator/TreeDecorator$Generator;replaceWithVine(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/state/property/BooleanProperty;)V
 */
package net.minecraft.world.gen.treedecorator;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.VineBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

public class TrunkVineTreeDecorator
extends TreeDecorator {
    public static final MapCodec<TrunkVineTreeDecorator> CODEC = MapCodec.unit(() -> INSTANCE);
    public static final TrunkVineTreeDecorator INSTANCE = new TrunkVineTreeDecorator();

    @Override
    protected TreeDecoratorType<?> getType() {
        return TreeDecoratorType.TRUNK_VINE;
    }

    @Override
    public void generate(TreeDecorator.Generator generator) {
        Random lv = generator.getRandom();
        generator.getLogPositions().forEach(pos -> {
            BlockPos lv;
            if (lv.nextInt(3) > 0 && generator.isAir(lv = pos.west())) {
                generator.replaceWithVine(lv, VineBlock.EAST);
            }
            if (lv.nextInt(3) > 0 && generator.isAir(lv = pos.east())) {
                generator.replaceWithVine(lv, VineBlock.WEST);
            }
            if (lv.nextInt(3) > 0 && generator.isAir(lv = pos.north())) {
                generator.replaceWithVine(lv, VineBlock.SOUTH);
            }
            if (lv.nextInt(3) > 0 && generator.isAir(lv = pos.south())) {
                generator.replaceWithVine(lv, VineBlock.NORTH);
            }
        });
    }
}

