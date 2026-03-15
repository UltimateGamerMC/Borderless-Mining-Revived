package net.fabricmc.fabric.impl.registry.sync;

import java.util.LinkedHashMap;
import java.util.Map;

import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;

public class RegistryMapSerializer {
	public static final int VERSION = 1;

	public static Map<Identifier, Object2IntMap<Identifier>> fromNbt(CompoundTag nbt) {
		CompoundTag mainNbt = nbt.getCompound("registries").orElseThrow();
		Map<Identifier, Object2IntMap<Identifier>> map = new LinkedHashMap<>();

		for (String registryId : mainNbt.keySet()) {
			Object2IntMap<Identifier> idMap = new Object2IntLinkedOpenHashMap<>();
			CompoundTag idNbt = mainNbt.getCompound(registryId).orElseThrow();

			for (String id : idNbt.keySet()) {
				idMap.put(Identifier.parse(id), idNbt.getIntOr(id, 0));
			}

			map.put(Identifier.parse(registryId), idMap);
		}

		return map;
	}

	public static CompoundTag toNbt(Map<Identifier, Object2IntMap<Identifier>> map) {
		CompoundTag mainNbt = new CompoundTag();

		map.forEach((registryId, idMap) -> {
			CompoundTag registryNbt = new CompoundTag();

			for (Object2IntMap.Entry<Identifier> idPair : idMap.object2IntEntrySet()) {
				registryNbt.putInt(idPair.getKey().toString(), idPair.getIntValue());
			}

			mainNbt.put(registryId.toString(), registryNbt);
		});

		CompoundTag nbt = new CompoundTag();
		nbt.putInt("version", VERSION);
		nbt.put("registries", mainNbt);
		return nbt;
	}
}
