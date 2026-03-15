/*
 * External method calls:
 *   Lnet/minecraft/server/dedicated/management/handler/OperatorManagementHandler;clearOperators(Lnet/minecraft/server/dedicated/management/network/ManagementConnectionId;)V
 *   Lnet/minecraft/util/Util;combineSafe(Ljava/util/List;)Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/server/dedicated/management/dispatch/OperatorsRpcDispatcher$ConfigEntry;user()Lnet/minecraft/server/PlayerConfigEntry;
 *   Lnet/minecraft/server/dedicated/management/dispatch/OperatorsRpcDispatcher$ConfigEntry;permissionLevel()Ljava/util/Optional;
 *   Lnet/minecraft/server/dedicated/management/dispatch/OperatorsRpcDispatcher$ConfigEntry;bypassesPlayerLimit()Ljava/util/Optional;
 *   Lnet/minecraft/server/dedicated/management/handler/OperatorManagementHandler;addToOperators(Lnet/minecraft/server/PlayerConfigEntry;Ljava/util/Optional;Ljava/util/Optional;Lnet/minecraft/server/dedicated/management/network/ManagementConnectionId;)V
 *   Lnet/minecraft/server/dedicated/management/handler/OperatorManagementHandler;removeFromOperators(Lnet/minecraft/server/PlayerConfigEntry;Lnet/minecraft/server/dedicated/management/network/ManagementConnectionId;)V
 *   Lnet/minecraft/server/dedicated/management/dispatch/OperatorsRpcDispatcher$RpcEntry;player()Lnet/minecraft/server/dedicated/management/RpcPlayer;
 *   Lnet/minecraft/server/dedicated/management/RpcPlayer;id()Ljava/util/Optional;
 *   Lnet/minecraft/server/dedicated/management/RpcPlayer;name()Ljava/util/Optional;
 *   Lnet/minecraft/server/dedicated/management/dispatch/OperatorsRpcDispatcher$RpcEntry;permissionLevel()Ljava/util/Optional;
 *   Lnet/minecraft/server/dedicated/management/dispatch/OperatorsRpcDispatcher$RpcEntry;bypassesPlayerLimit()Ljava/util/Optional;
 */
package net.minecraft.server.dedicated.management.dispatch;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import net.minecraft.command.permission.PermissionLevel;
import net.minecraft.server.OperatorEntry;
import net.minecraft.server.PlayerConfigEntry;
import net.minecraft.server.dedicated.management.RpcPlayer;
import net.minecraft.server.dedicated.management.dispatch.ManagementHandlerDispatcher;
import net.minecraft.server.dedicated.management.network.ManagementConnectionId;
import net.minecraft.util.Util;

public class OperatorsRpcDispatcher {
    public static List<RpcEntry> get(ManagementHandlerDispatcher dispatcher) {
        return dispatcher.getOperatorHandler().getOperators().stream().filter(operator -> operator.getKey() != null).map(RpcEntry::of).toList();
    }

    public static List<RpcEntry> clear(ManagementHandlerDispatcher dispatcher, ManagementConnectionId remote) {
        dispatcher.getOperatorHandler().clearOperators(remote);
        return OperatorsRpcDispatcher.get(dispatcher);
    }

    public static List<RpcEntry> remove(ManagementHandlerDispatcher dispatcher, List<RpcPlayer> players, ManagementConnectionId remote) {
        List<CompletableFuture> list2 = players.stream().map(player -> dispatcher.getPlayerListHandler().getPlayerAsync(player.id(), player.name())).toList();
        for (Optional optional : Util.combineSafe(list2).join()) {
            optional.ifPresent(player -> dispatcher.getOperatorHandler().removeFromOperators((PlayerConfigEntry)player, remote));
        }
        return OperatorsRpcDispatcher.get(dispatcher);
    }

    public static List<RpcEntry> add(ManagementHandlerDispatcher dispatcher, List<RpcEntry> operators, ManagementConnectionId remote) {
        List<CompletableFuture> list2 = operators.stream().map(operator -> dispatcher.getPlayerListHandler().getPlayerAsync(operator.player().id(), operator.player().name()).thenApply(optionalPlayerEntry -> optionalPlayerEntry.map(playerEntry -> new ConfigEntry((PlayerConfigEntry)playerEntry, operator.permissionLevel(), operator.bypassesPlayerLimit())))).toList();
        for (Optional optional : Util.combineSafe(list2).join()) {
            optional.ifPresent(operator -> dispatcher.getOperatorHandler().addToOperators(operator.user(), operator.permissionLevel(), operator.bypassesPlayerLimit(), remote));
        }
        return OperatorsRpcDispatcher.get(dispatcher);
    }

    public static List<RpcEntry> set(ManagementHandlerDispatcher dispatcher, List<RpcEntry> operators, ManagementConnectionId remote) {
        List<CompletableFuture> list2 = operators.stream().map(operator -> dispatcher.getPlayerListHandler().getPlayerAsync(operator.player().id(), operator.player().name()).thenApply(optionalPlayerEntry -> optionalPlayerEntry.map(playerEntry -> new ConfigEntry((PlayerConfigEntry)playerEntry, operator.permissionLevel(), operator.bypassesPlayerLimit())))).toList();
        Set set = Util.combineSafe(list2).join().stream().flatMap(Optional::stream).collect(Collectors.toSet());
        Set set2 = dispatcher.getOperatorHandler().getOperators().stream().filter(entry -> entry.getKey() != null).map(operator -> new ConfigEntry((PlayerConfigEntry)operator.getKey(), Optional.of(operator.getLevel().getLevel()), Optional.of(operator.canBypassPlayerLimit()))).collect(Collectors.toSet());
        set2.stream().filter(operator -> !set.contains(operator)).forEach(operator -> dispatcher.getOperatorHandler().removeFromOperators(operator.user(), remote));
        set.stream().filter(entry -> !set2.contains(entry)).forEach(operator -> dispatcher.getOperatorHandler().addToOperators(operator.user(), operator.permissionLevel(), operator.bypassesPlayerLimit(), remote));
        return OperatorsRpcDispatcher.get(dispatcher);
    }

    record ConfigEntry(PlayerConfigEntry user, Optional<PermissionLevel> permissionLevel, Optional<Boolean> bypassesPlayerLimit) {
    }

    public record RpcEntry(RpcPlayer player, Optional<PermissionLevel> permissionLevel, Optional<Boolean> bypassesPlayerLimit) {
        public static final MapCodec<RpcEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)RpcPlayer.CODEC.codec().fieldOf("player")).forGetter(RpcEntry::player), PermissionLevel.NUMERIC_CODEC.optionalFieldOf("permissionLevel").forGetter(RpcEntry::permissionLevel), Codec.BOOL.optionalFieldOf("bypassesPlayerLimit").forGetter(RpcEntry::bypassesPlayerLimit)).apply((Applicative<RpcEntry, ?>)instance, RpcEntry::new));

        public static RpcEntry of(OperatorEntry operator) {
            return new RpcEntry(RpcPlayer.of(Objects.requireNonNull((PlayerConfigEntry)operator.getKey())), Optional.of(operator.getLevel().getLevel()), Optional.of(operator.canBypassPlayerLimit()));
        }
    }
}

