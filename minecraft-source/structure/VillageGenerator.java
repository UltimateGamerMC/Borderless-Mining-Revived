/*
 * External method calls:
 *   Lnet/minecraft/structure/PlainsVillageData;bootstrap(Lnet/minecraft/registry/Registerable;)V
 *   Lnet/minecraft/structure/SnowyVillageData;bootstrap(Lnet/minecraft/registry/Registerable;)V
 *   Lnet/minecraft/structure/SavannaVillageData;bootstrap(Lnet/minecraft/registry/Registerable;)V
 *   Lnet/minecraft/structure/DesertVillageData;bootstrap(Lnet/minecraft/registry/Registerable;)V
 *   Lnet/minecraft/structure/TaigaVillageData;bootstrap(Lnet/minecraft/registry/Registerable;)V
 */
package net.minecraft.structure;

import net.minecraft.registry.Registerable;
import net.minecraft.structure.DesertVillageData;
import net.minecraft.structure.PlainsVillageData;
import net.minecraft.structure.SavannaVillageData;
import net.minecraft.structure.SnowyVillageData;
import net.minecraft.structure.TaigaVillageData;
import net.minecraft.structure.pool.StructurePool;

public class VillageGenerator {
    public static void bootstrap(Registerable<StructurePool> poolRegisterable) {
        PlainsVillageData.bootstrap(poolRegisterable);
        SnowyVillageData.bootstrap(poolRegisterable);
        SavannaVillageData.bootstrap(poolRegisterable);
        DesertVillageData.bootstrap(poolRegisterable);
        TaigaVillageData.bootstrap(poolRegisterable);
    }
}

