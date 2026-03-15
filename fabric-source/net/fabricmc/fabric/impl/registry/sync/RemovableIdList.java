package net.fabricmc.fabric.impl.registry.sync;

import it.unimi.dsi.fastutil.ints.Int2IntMap;

public interface RemovableIdList<T> {
	void fabric_clear();
	void fabric_remove(T o);
	void fabric_removeId(int i);
	void fabric_remapId(int from, int to);
	void fabric_remapIds(Int2IntMap map);
}
