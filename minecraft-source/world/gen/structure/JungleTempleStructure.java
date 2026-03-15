/*
 * Internal private/static methods:
 *   Lnet/minecraft/world/gen/structure/JungleTempleStructure;createCodec(Ljava/util/function/Function;)Lcom/mojang/serialization/MapCodec;
 */
package net.minecraft.world.gen.structure;

import com.mojang.serialization.MapCodec;
import net.minecraft.structure.JungleTempleGenerator;
import net.minecraft.world.gen.structure.BasicTempleStructure;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

public class JungleTempleStructure
extends BasicTempleStructure {
    public static final MapCodec<JungleTempleStructure> CODEC = JungleTempleStructure.createCodec(JungleTempleStructure::new);

    public JungleTempleStructure(Structure.Config arg) {
        super(JungleTempleGenerator::new, 12, 15, arg);
    }

    @Override
    public StructureType<?> getType() {
        return StructureType.JUNGLE_TEMPLE;
    }
}

