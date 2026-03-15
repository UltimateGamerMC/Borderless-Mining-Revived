/*
 * External method calls:
 *   Lnet/minecraft/registry/Registry;register(Lnet/minecraft/registry/Registry;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/world/gen/chunk/placement/StructurePlacementType;register(Ljava/lang/String;Lcom/mojang/serialization/MapCodec;)Lnet/minecraft/world/gen/chunk/placement/StructurePlacementType;
 */
package net.minecraft.world.gen.chunk.placement;

import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.chunk.placement.ConcentricRingsStructurePlacement;
import net.minecraft.world.gen.chunk.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.gen.chunk.placement.StructurePlacement;

public interface StructurePlacementType<SP extends StructurePlacement> {
    public static final StructurePlacementType<RandomSpreadStructurePlacement> RANDOM_SPREAD = StructurePlacementType.register("random_spread", RandomSpreadStructurePlacement.CODEC);
    public static final StructurePlacementType<ConcentricRingsStructurePlacement> CONCENTRIC_RINGS = StructurePlacementType.register("concentric_rings", ConcentricRingsStructurePlacement.CODEC);

    public MapCodec<SP> codec();

    private static <SP extends StructurePlacement> StructurePlacementType<SP> register(String id, MapCodec<SP> codec) {
        return Registry.register(Registries.STRUCTURE_PLACEMENT, id, () -> codec);
    }
}

