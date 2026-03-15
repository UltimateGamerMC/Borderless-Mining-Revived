/**
 * The Networking API, version 1.
 *
 * <p>There are three stages of Minecraft networking, all of which are supported in this API:
 * <dl>
 *     <dt>LOGIN</dt>
 *     <dd>This is the initial stage, before the player logs into the world. If using a proxy server,
 *     the packets in this stage may be intercepted and discarded by the proxy. <strong>Most of the pre-1.20.2
 *     uses of this event should be replaced with the CONFIGURATION stage.</strong>
 *     Related events are found at {@link net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents},
 *     and related methods are found at {@link net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking}.
 *     </dd>
 *     <dt>CONFIGURATION</dt>
 *     <dd>This is the stage after LOGIN. The player is authenticated, but still hasn't joined the
 *     world at this point. Servers can use this phase to send configurations or verify client's mod
 *     versions. Note that some server mods allow players in the PLAY stage to re-enter this stage,
 *     for example when a player chooses a minigame server in a lobby.
 *     Related events are found at {@link net.fabricmc.fabric.api.networking.v1.S2CConfigurationChannelEvents}
 *     {@link net.fabricmc.fabric.api.networking.v1.ServerConfigurationConnectionEvents}, and related methods are found at
 *     {@link net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking}.
 *     </dd>
 *     <dt>PLAY</dt>
 *     <dd>This is the stage after CONFIGURATION, where gameplay-related packets are sent and received.
 *     The player has joined the world and is playing the game. Related events are found at
 *     {@link net.fabricmc.fabric.api.networking.v1.S2CPlayChannelEvents}
 *     and {@link net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents}, and related methods are found at
 *     {@link net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking}.</dd>
 * </dl>
 *
 * <p>In addition, this API includes helpers for {@linkplain
 * net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry registering custom packet payloads} and {@linkplain
 * net.fabricmc.fabric.api.networking.v1.PlayerLookup player lookups}.
 */

@NullMarked
package net.fabricmc.fabric.api.networking.v1;

import org.jspecify.annotations.NullMarked;
