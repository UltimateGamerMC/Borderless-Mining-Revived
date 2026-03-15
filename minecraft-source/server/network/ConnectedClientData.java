/*
 * External method calls:
 *   Lnet/minecraft/network/packet/c2s/common/SyncedClientOptions;createDefault()Lnet/minecraft/network/packet/c2s/common/SyncedClientOptions;
 */
package net.minecraft.server.network;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;

public record ConnectedClientData(GameProfile gameProfile, int latency, SyncedClientOptions syncedOptions, boolean transferred) {
    public static ConnectedClientData createDefault(GameProfile profile, boolean bl) {
        return new ConnectedClientData(profile, 0, SyncedClientOptions.createDefault(), bl);
    }
}

