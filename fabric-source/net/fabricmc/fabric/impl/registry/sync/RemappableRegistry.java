package net.fabricmc.fabric.impl.registry.sync;

import it.unimi.dsi.fastutil.objects.Object2IntMap;

import net.minecraft.resources.Identifier;

public interface RemappableRegistry {
	/**
	 * The mode the remapping process should take.
	 */
	enum RemapMode {
		/**
		 * Any differences (local-&gt;remote, remote-&gt;local) are allowed. This should
		 * be used when a side is authoritative (f.e. loading a world on the server).
		 */
		AUTHORITATIVE,
		/**
		 * Entries missing on the remote side are hidden on the local side, while
		 * entries missing on the local side cause an exception. This should be
		 * used when a side is remote (f.e. connecting to a remote server as a
		 * client).
		 */
		REMOTE,
	}

	void remap(Object2IntMap<Identifier> remoteIndexedEntries, RemapMode mode) throws RemapException;

	void unmap() throws RemapException;
}
