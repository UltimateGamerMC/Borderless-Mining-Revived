/*
 * External method calls:
 *   Lnet/minecraft/entity/spawn/SpawnContext;world()Lnet/minecraft/world/ServerWorldAccess;
 *   Lnet/minecraft/world/ServerWorldAccess;toServerWorld()Lnet/minecraft/server/world/ServerWorld;
 *   Lnet/minecraft/entity/spawn/SpawnContext;pos()Lnet/minecraft/util/math/BlockPos;
 *   Lnet/minecraft/registry/RegistryCodecs;entryList(Lnet/minecraft/registry/RegistryKey;)Lcom/mojang/serialization/Codec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/spawn/StructureSpawnCondition;test(Lnet/minecraft/entity/spawn/SpawnContext;)Z
 */
package net.minecraft.entity.spawn;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.spawn.SpawnCondition;
import net.minecraft.entity.spawn.SpawnContext;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.world.gen.structure.Structure;

public record StructureSpawnCondition(RegistryEntryList<Structure> requiredStructures) implements SpawnCondition
{
    public static final MapCodec<StructureSpawnCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)RegistryCodecs.entryList(RegistryKeys.STRUCTURE).fieldOf("structures")).forGetter(StructureSpawnCondition::requiredStructures)).apply((Applicative<StructureSpawnCondition, ?>)instance, StructureSpawnCondition::new));

    @Override
    public boolean test(SpawnContext arg) {
        return arg.world().toServerWorld().getStructureAccessor().getStructureContaining(arg.pos(), this.requiredStructures).hasChildren();
    }

    public MapCodec<StructureSpawnCondition> getCodec() {
        return CODEC;
    }

    @Override
    public /* synthetic */ boolean test(Object context) {
        return this.test((SpawnContext)context);
    }
}

