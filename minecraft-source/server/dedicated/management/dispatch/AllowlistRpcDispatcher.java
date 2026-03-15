/*
 * External method calls:
 *   Lnet/minecraft/util/Util;combineSafe(Ljava/util/List;)Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/server/dedicated/management/handler/AllowlistManagementHandler;clear(Lnet/minecraft/server/dedicated/management/network/ManagementConnectionId;)V
 *   Lnet/minecraft/server/dedicated/management/handler/AllowlistManagementHandler;kickUnlisted(Lnet/minecraft/server/dedicated/management/network/ManagementConnectionId;)V
 *   Lnet/minecraft/server/dedicated/management/RpcPlayer;id()Ljava/util/Optional;
 *   Lnet/minecraft/server/dedicated/management/RpcPlayer;name()Ljava/util/Optional;
 *   Lnet/minecraft/server/dedicated/management/RpcPlayer;of(Lnet/minecraft/server/PlayerConfigEntry;)Lnet/minecraft/server/dedicated/management/RpcPlayer;
 */
package net.minecraft.server.dedicated.management.dispatch;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import net.minecraft.server.PlayerConfigEntry;
import net.minecraft.server.ServerConfigEntry;
import net.minecraft.server.WhitelistEntry;
import net.minecraft.server.dedicated.management.RpcPlayer;
import net.minecraft.server.dedicated.management.dispatch.ManagementHandlerDispatcher;
import net.minecraft.server.dedicated.management.network.ManagementConnectionId;
import net.minecraft.util.Util;

public class AllowlistRpcDispatcher {
    public static List<RpcPlayer> get(ManagementHandlerDispatcher dispatcher) {
        return dispatcher.getAllowlistHandler().getAllowlist().stream().filter(entry -> entry.getKey() != null).map(player -> RpcPlayer.of((PlayerConfigEntry)player.getKey())).toList();
    }

    public static List<RpcPlayer> add(ManagementHandlerDispatcher dispatcher, List<RpcPlayer> players, ManagementConnectionId remote) {
        List<CompletableFuture> list2 = players.stream().map(player -> dispatcher.getPlayerListHandler().getPlayerAsync(player.id(), player.name())).toList();
        for (Optional optional : Util.combineSafe(list2).join()) {
            optional.ifPresent(player -> dispatcher.getAllowlistHandler().add(new WhitelistEntry((PlayerConfigEntry)player), remote));
        }
        return AllowlistRpcDispatcher.get(dispatcher);
    }

    public static List<RpcPlayer> clear(ManagementHandlerDispatcher dispatcher, ManagementConnectionId remote) {
        dispatcher.getAllowlistHandler().clear(remote);
        return AllowlistRpcDispatcher.get(dispatcher);
    }

    public static List<RpcPlayer> remove(ManagementHandlerDispatcher dispatcher, List<RpcPlayer> players, ManagementConnectionId remote) {
        List<CompletableFuture> list2 = players.stream().map(player -> dispatcher.getPlayerListHandler().getPlayerAsync(player.id(), player.name())).toList();
        for (Optional optional : Util.combineSafe(list2).join()) {
            optional.ifPresent(player -> dispatcher.getAllowlistHandler().remove((PlayerConfigEntry)player, remote));
        }
        dispatcher.getAllowlistHandler().kickUnlisted(remote);
        return AllowlistRpcDispatcher.get(dispatcher);
    }

    public static List<RpcPlayer> set(ManagementHandlerDispatcher dispatcher, List<RpcPlayer> players, ManagementConnectionId remote) {
        List<CompletableFuture> list2 = players.stream().map(player -> dispatcher.getPlayerListHandler().getPlayerAsync(player.id(), player.name())).toList();
        Set set = Util.combineSafe(list2).join().stream().flatMap(Optional::stream).collect(Collectors.toSet());
        Set set2 = dispatcher.getAllowlistHandler().getAllowlist().stream().map(ServerConfigEntry::getKey).collect(Collectors.toSet());
        set2.stream().filter(player -> !set.contains(player)).forEach(player -> dispatcher.getAllowlistHandler().remove((PlayerConfigEntry)player, remote));
        set.stream().filter(player -> !set2.contains(player)).forEach(player -> dispatcher.getAllowlistHandler().add(new WhitelistEntry((PlayerConfigEntry)player), remote));
        dispatcher.getAllowlistHandler().kickUnlisted(remote);
        return AllowlistRpcDispatcher.get(dispatcher);
    }
}

