package net.fabricmc.fabric.impl.resource.pack;

import java.util.List;

import net.minecraft.server.packs.repository.KnownPack;

public interface FabricOriginalKnownPacksGetter {
	/**
	 * @return the data packs known at server start
	 */
	List<KnownPack> fabric$getOriginalKnownPacks();
}
