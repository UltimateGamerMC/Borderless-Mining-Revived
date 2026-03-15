/*
 * External method calls:
 *   Lnet/minecraft/registry/RegistryKey;of(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/util/Identifier;)Lnet/minecraft/registry/RegistryKey;
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/util/Identifier;of(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/registry/Registerable;register(Lnet/minecraft/registry/RegistryKey;Ljava/lang/Object;)Lnet/minecraft/registry/entry/RegistryEntry$Reference;
 *   Lnet/minecraft/structure/BastionRemnantGenerator;bootstrap(Lnet/minecraft/registry/Registerable;)V
 *   Lnet/minecraft/structure/PillagerOutpostGenerator;bootstrap(Lnet/minecraft/registry/Registerable;)V
 *   Lnet/minecraft/structure/VillageGenerator;bootstrap(Lnet/minecraft/registry/Registerable;)V
 *   Lnet/minecraft/structure/AncientCityGenerator;bootstrap(Lnet/minecraft/registry/Registerable;)V
 *   Lnet/minecraft/structure/TrailRuinsGenerator;bootstrap(Lnet/minecraft/registry/Registerable;)V
 *   Lnet/minecraft/structure/TrialChamberData;bootstrap(Lnet/minecraft/registry/Registerable;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/structure/pool/StructurePools;of(Lnet/minecraft/util/Identifier;)Lnet/minecraft/registry/RegistryKey;
 *   Lnet/minecraft/structure/pool/StructurePools;ofVanilla(Ljava/lang/String;)Lnet/minecraft/registry/RegistryKey;
 */
package net.minecraft.structure.pool;

import com.google.common.collect.ImmutableList;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.AncientCityGenerator;
import net.minecraft.structure.BastionRemnantGenerator;
import net.minecraft.structure.PillagerOutpostGenerator;
import net.minecraft.structure.TrailRuinsGenerator;
import net.minecraft.structure.TrialChamberData;
import net.minecraft.structure.VillageGenerator;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.util.Identifier;

public class StructurePools {
    public static final RegistryKey<StructurePool> EMPTY = StructurePools.ofVanilla("empty");

    public static RegistryKey<StructurePool> of(Identifier id) {
        return RegistryKey.of(RegistryKeys.TEMPLATE_POOL, id);
    }

    public static RegistryKey<StructurePool> ofVanilla(String id) {
        return StructurePools.of(Identifier.ofVanilla(id));
    }

    public static RegistryKey<StructurePool> of(String id) {
        return StructurePools.of(Identifier.of(id));
    }

    public static void register(Registerable<StructurePool> structurePoolsRegisterable, String id, StructurePool pool) {
        structurePoolsRegisterable.register(StructurePools.ofVanilla(id), pool);
    }

    public static void bootstrap(Registerable<StructurePool> structurePoolsRegisterable) {
        RegistryEntryLookup<StructurePool> lv = structurePoolsRegisterable.getRegistryLookup(RegistryKeys.TEMPLATE_POOL);
        RegistryEntry.Reference<StructurePool> lv2 = lv.getOrThrow(EMPTY);
        structurePoolsRegisterable.register(EMPTY, new StructurePool(lv2, ImmutableList.of(), StructurePool.Projection.RIGID));
        BastionRemnantGenerator.bootstrap(structurePoolsRegisterable);
        PillagerOutpostGenerator.bootstrap(structurePoolsRegisterable);
        VillageGenerator.bootstrap(structurePoolsRegisterable);
        AncientCityGenerator.bootstrap(structurePoolsRegisterable);
        TrailRuinsGenerator.bootstrap(structurePoolsRegisterable);
        TrialChamberData.bootstrap(structurePoolsRegisterable);
    }
}

