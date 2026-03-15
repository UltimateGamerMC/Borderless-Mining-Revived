package net.fabricmc.fabric.impl.networking;

import java.util.Collection;

import net.minecraft.network.ConnectionProtocol;
import net.minecraft.resources.Identifier;

public interface ChannelInfoHolder {
	/**
	 * @return Channels which are declared as receivable by the other side but have not been declared yet.
	 */
	Collection<Identifier> fabric_getPendingChannelsNames(ConnectionProtocol state);
}
