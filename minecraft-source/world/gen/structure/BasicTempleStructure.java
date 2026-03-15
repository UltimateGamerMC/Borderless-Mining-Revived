/*
 * External method calls:
 *   Lnet/minecraft/world/gen/structure/Structure$Context;chunkGenerator()Lnet/minecraft/world/gen/chunk/ChunkGenerator;
 *   Lnet/minecraft/world/gen/structure/Structure$Context;chunkPos()Lnet/minecraft/util/math/ChunkPos;
 *   Lnet/minecraft/world/gen/structure/Structure$Context;random()Lnet/minecraft/util/math/random/ChunkRandom;
 *   Lnet/minecraft/world/gen/structure/BasicTempleStructure$Constructor;construct(Lnet/minecraft/util/math/random/ChunkRandom;II)Lnet/minecraft/structure/StructurePiece;
 *   Lnet/minecraft/structure/StructurePiecesCollector;addPiece(Lnet/minecraft/structure/StructurePiece;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/world/gen/structure/BasicTempleStructure;addPieces(Lnet/minecraft/structure/StructurePiecesCollector;Lnet/minecraft/world/gen/structure/Structure$Context;)V
 */
package net.minecraft.world.gen.structure;

import java.util.Optional;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePiecesCollector;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.structure.Structure;

public abstract class BasicTempleStructure
extends Structure {
    private final Constructor constructor;
    private final int width;
    private final int height;

    protected BasicTempleStructure(Constructor constructor, int width, int height, Structure.Config config) {
        super(config);
        this.constructor = constructor;
        this.width = width;
        this.height = height;
    }

    @Override
    public Optional<Structure.StructurePosition> getStructurePosition(Structure.Context context) {
        if (BasicTempleStructure.getMinCornerHeight(context, this.width, this.height) < context.chunkGenerator().getSeaLevel()) {
            return Optional.empty();
        }
        return BasicTempleStructure.getStructurePosition(context, Heightmap.Type.WORLD_SURFACE_WG, collector -> this.addPieces((StructurePiecesCollector)collector, context));
    }

    private void addPieces(StructurePiecesCollector collector, Structure.Context context) {
        ChunkPos lv = context.chunkPos();
        collector.addPiece(this.constructor.construct(context.random(), lv.getStartX(), lv.getStartZ()));
    }

    @FunctionalInterface
    protected static interface Constructor {
        public StructurePiece construct(ChunkRandom var1, int var2, int var3);
    }
}

