package net.fabricmc.fabric.mixin.resource;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import net.minecraft.server.packs.repository.KnownPack;

import net.fabricmc.fabric.impl.resource.pack.FabricOriginalKnownPacksGetter;

@Mixin(ServerConfigurationPacketListenerImpl.class)
public abstract class ServerConfigurationPacketListenerImplMixin extends ServerCommonPacketListenerImpl {
	public ServerConfigurationPacketListenerImplMixin(MinecraftServer server, Connection connection, CommonListenerCookie clientData) {
		super(server, connection, clientData);
	}

	/**
	 * Only use packs that were enabled at server start and are enabled now. This avoids a descync when packs have been
	 * enabled or disabled before the client joins. Since the server registry contents aren't reloaded, we don't want
	 * the client to use the new data pack data.
	 */
	@ModifyArg(method = "startConfiguration", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/config/SynchronizeRegistriesTask;<init>(Ljava/util/List;Lnet/minecraft/core/LayeredRegistryAccess;)V", ordinal = 0))
	public List<KnownPack> filterKnownPacks(List<KnownPack> currentKnownPacks) {
		return ((FabricOriginalKnownPacksGetter) this.server).fabric$getOriginalKnownPacks().stream().filter(currentKnownPacks::contains).toList();
	}
}
