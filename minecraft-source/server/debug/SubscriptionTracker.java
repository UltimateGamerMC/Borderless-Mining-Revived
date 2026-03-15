/*
 * External method calls:
 *   Lnet/minecraft/server/debug/TrackedSubscription;refreshTracking(Lnet/minecraft/server/world/ServerWorld;)V
 *   Lnet/minecraft/server/world/ServerChunkLoadingManager;forEachChunk(Ljava/util/function/Consumer;)V
 *   Lnet/minecraft/server/world/ServerWorld;iterateEntities()Ljava/lang/Iterable;
 *   Lnet/minecraft/world/chunk/WorldChunk;registerTracking(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/debug/DebugTrackable$Tracker;)V
 *   Lnet/minecraft/server/debug/TrackedSubscription$UpdateTrackedSubscription;untrackChunk(Lnet/minecraft/util/math/ChunkPos;)V
 *   Lnet/minecraft/block/entity/BlockEntity;registerTracking(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/debug/DebugTrackable$Tracker;)V
 *   Lnet/minecraft/server/debug/TrackedSubscription$UpdateTrackedSubscription;untrackBlockEntity(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;)V
 *   Lnet/minecraft/entity/Entity;registerTracking(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/debug/DebugTrackable$Tracker;)V
 *   Lnet/minecraft/server/debug/TrackedSubscription$UpdateTrackedSubscription;untrackEntity(Lnet/minecraft/entity/Entity;)V
 *   Lnet/minecraft/server/debug/TrackedSubscription;sendInitialIfSubscribed(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/util/math/ChunkPos;)V
 *   Lnet/minecraft/server/debug/TrackedSubscription;sendInitialIfSubscribed(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/entity/Entity;)V
 *   Lnet/minecraft/server/debug/TrackedSubscription$TrackedPoi;onPoiAdded(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/poi/PointOfInterest;)V
 *   Lnet/minecraft/server/debug/TrackedSubscription$TrackedVillageSections;onPoiAdded(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/poi/PointOfInterest;)V
 *   Lnet/minecraft/server/debug/TrackedSubscription$TrackedPoi;onPoiUpdated(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;)V
 *   Lnet/minecraft/server/debug/TrackedSubscription$TrackedPoi;onPoiRemoved(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;)V
 *   Lnet/minecraft/server/debug/TrackedSubscription$TrackedVillageSections;onPoiRemoved(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;)V
 *   Lnet/minecraft/world/debug/DebugSubscriptionType;optionalValueFor(Ljava/lang/Object;)Lnet/minecraft/world/debug/DebugSubscriptionType$OptionalValue;
 *   Lnet/minecraft/world/debug/DebugSubscriptionType;optionalValueFor()Lnet/minecraft/world/debug/DebugSubscriptionType$OptionalValue;
 *   Lnet/minecraft/world/debug/DebugSubscriptionType;valueFor(Ljava/lang/Object;)Lnet/minecraft/world/debug/DebugSubscriptionType$Value;
 *   Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V
 *   Lnet/minecraft/server/world/ServerChunkLoadingManager;sendToOtherNearbyPlayersIf(Lnet/minecraft/entity/Entity;Lnet/minecraft/network/packet/Packet;Ljava/util/function/Predicate;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/debug/SubscriptionTracker;trackEntity(Lnet/minecraft/entity/Entity;)V
 *   Lnet/minecraft/server/debug/SubscriptionTracker;sendToTrackingPlayers(Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/world/debug/DebugSubscriptionType;Lnet/minecraft/network/packet/Packet;)V
 *   Lnet/minecraft/server/debug/SubscriptionTracker;sendToTrackingPlayers(Lnet/minecraft/entity/Entity;Lnet/minecraft/world/debug/DebugSubscriptionType;Lnet/minecraft/network/packet/Packet;)V
 */
package net.minecraft.server.debug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockValueDebugS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityValueDebugS2CPacket;
import net.minecraft.network.packet.s2c.play.EventDebugS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.server.debug.SubscriberTracker;
import net.minecraft.server.debug.TrackedSubscription;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerChunkLoadingManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.debug.DebugSubscriptionType;
import net.minecraft.world.debug.DebugTrackable;
import net.minecraft.world.poi.PointOfInterest;

public class SubscriptionTracker {
    private final ServerWorld world;
    private final List<TrackedSubscription<?>> subscriptions = new ArrayList();
    private final Map<DebugSubscriptionType<?>, TrackedSubscription.UpdateTrackedSubscription<?>> subscriptionsByTypes = new HashMap();
    private final TrackedSubscription.TrackedPoi trackedPoi = new TrackedSubscription.TrackedPoi();
    private final TrackedSubscription.TrackedVillageSections trackedVillageSections = new TrackedSubscription.TrackedVillageSections();
    private boolean stopped = true;
    private Set<DebugSubscriptionType<?>> subscribedTypes = Set.of();

    public SubscriptionTracker(ServerWorld world) {
        this.world = world;
        for (DebugSubscriptionType debugSubscriptionType : Registries.DEBUG_SUBSCRIPTION) {
            if (debugSubscriptionType.getPacketCodec() == null) continue;
            this.subscriptionsByTypes.put(debugSubscriptionType, new TrackedSubscription.UpdateTrackedSubscription(debugSubscriptionType));
        }
        this.subscriptions.addAll(this.subscriptionsByTypes.values());
        this.subscriptions.add(this.trackedPoi);
        this.subscriptions.add(this.trackedVillageSections);
    }

    public void tick(SubscriberTracker subscriberTracker) {
        this.subscribedTypes = subscriberTracker.getSubscribedTypes();
        boolean bl = this.subscribedTypes.isEmpty();
        if (this.stopped != bl) {
            this.stopped = bl;
            if (bl) {
                for (TrackedSubscription<?> lv : this.subscriptions) {
                    lv.clear();
                }
            } else {
                this.startTracking();
            }
        }
        if (!this.stopped) {
            for (TrackedSubscription<?> lv : this.subscriptions) {
                lv.refreshTracking(this.world);
            }
        }
    }

    private void startTracking() {
        ServerChunkLoadingManager lv = this.world.getChunkManager().chunkLoadingManager;
        lv.forEachChunk(this::trackChunk);
        for (Entity lv2 : this.world.iterateEntities()) {
            if (!lv.hasTrackingPlayer(lv2)) continue;
            this.trackEntity(lv2);
        }
    }

    <T> TrackedSubscription.UpdateTrackedSubscription<T> get(DebugSubscriptionType<T> type) {
        return this.subscriptionsByTypes.get(type);
    }

    public void trackChunk(final WorldChunk chunk) {
        if (this.stopped) {
            return;
        }
        chunk.registerTracking(this.world, new DebugTrackable.Tracker(){

            @Override
            public <T> void track(DebugSubscriptionType<T> type, DebugTrackable.DebugDataSupplier<T> dataSupplier) {
                SubscriptionTracker.this.get(type).trackChunk(chunk.getPos(), dataSupplier);
            }
        });
        chunk.getBlockEntities().values().forEach(this::trackBlockEntity);
    }

    public void untrackChunk(ChunkPos chunkPos) {
        if (this.stopped) {
            return;
        }
        for (TrackedSubscription.UpdateTrackedSubscription<?> lv : this.subscriptionsByTypes.values()) {
            lv.untrackChunk(chunkPos);
        }
    }

    public void trackBlockEntity(final BlockEntity blockEntity) {
        if (this.stopped) {
            return;
        }
        blockEntity.registerTracking(this.world, new DebugTrackable.Tracker(){

            @Override
            public <T> void track(DebugSubscriptionType<T> type, DebugTrackable.DebugDataSupplier<T> dataSupplier) {
                SubscriptionTracker.this.get(type).trackBlockEntity(blockEntity.getPos(), dataSupplier);
            }
        });
    }

    public void untrackBlockEntity(BlockPos pos) {
        if (this.stopped) {
            return;
        }
        for (TrackedSubscription.UpdateTrackedSubscription<?> lv : this.subscriptionsByTypes.values()) {
            lv.untrackBlockEntity(this.world, pos);
        }
    }

    public void trackEntity(final Entity entity) {
        if (this.stopped) {
            return;
        }
        entity.registerTracking(this.world, new DebugTrackable.Tracker(){

            @Override
            public <T> void track(DebugSubscriptionType<T> type, DebugTrackable.DebugDataSupplier<T> dataSupplier) {
                SubscriptionTracker.this.get(type).trackEntity(entity.getUuid(), dataSupplier);
            }
        });
    }

    public void untrackEntity(Entity entity) {
        if (this.stopped) {
            return;
        }
        for (TrackedSubscription.UpdateTrackedSubscription<?> lv : this.subscriptionsByTypes.values()) {
            lv.untrackEntity(entity);
        }
    }

    public void sendInitialIfSubscribed(ServerPlayerEntity player, ChunkPos chunkPos) {
        if (this.stopped) {
            return;
        }
        for (TrackedSubscription<?> lv : this.subscriptions) {
            lv.sendInitialIfSubscribed(player, chunkPos);
        }
    }

    public void sendInitialIfSubscribed(ServerPlayerEntity player, Entity entity) {
        if (this.stopped) {
            return;
        }
        for (TrackedSubscription<?> lv : this.subscriptions) {
            lv.sendInitialIfSubscribed(player, entity);
        }
    }

    public void onPoiAdded(PointOfInterest poi) {
        if (this.stopped) {
            return;
        }
        this.trackedPoi.onPoiAdded(this.world, poi);
        this.trackedVillageSections.onPoiAdded(this.world, poi);
    }

    public void onPoiUpdated(BlockPos pos) {
        if (this.stopped) {
            return;
        }
        this.trackedPoi.onPoiUpdated(this.world, pos);
    }

    public void onPoiRemoved(BlockPos pos) {
        if (this.stopped) {
            return;
        }
        this.trackedPoi.onPoiRemoved(this.world, pos);
        this.trackedVillageSections.onPoiRemoved(this.world, pos);
    }

    public boolean isSubscribed(DebugSubscriptionType<?> type) {
        return this.subscribedTypes.contains(type);
    }

    public <T> void sendBlockDebugData(BlockPos pos, DebugSubscriptionType<T> type, T value) {
        if (this.isSubscribed(type)) {
            this.sendToTrackingPlayers(new ChunkPos(pos), type, (Packet<? super ClientPlayPacketListener>)new BlockValueDebugS2CPacket(pos, type.optionalValueFor(value)));
        }
    }

    public <T> void removeBlockDebugData(BlockPos pos, DebugSubscriptionType<T> type) {
        if (this.isSubscribed(type)) {
            this.sendToTrackingPlayers(new ChunkPos(pos), type, (Packet<? super ClientPlayPacketListener>)new BlockValueDebugS2CPacket(pos, type.optionalValueFor()));
        }
    }

    public <T> void sendEntityDebugData(Entity entity, DebugSubscriptionType<T> type, T value) {
        if (this.isSubscribed(type)) {
            this.sendToTrackingPlayers(entity, type, (Packet<? super ClientPlayPacketListener>)new EntityValueDebugS2CPacket(entity.getId(), type.optionalValueFor(value)));
        }
    }

    public <T> void removeEntityDebugData(Entity entity, DebugSubscriptionType<T> type) {
        if (this.isSubscribed(type)) {
            this.sendToTrackingPlayers(entity, type, (Packet<? super ClientPlayPacketListener>)new EntityValueDebugS2CPacket(entity.getId(), type.optionalValueFor()));
        }
    }

    public <T> void sendEventDebugData(BlockPos pos, DebugSubscriptionType<T> type, T value) {
        if (this.isSubscribed(type)) {
            this.sendToTrackingPlayers(new ChunkPos(pos), type, (Packet<? super ClientPlayPacketListener>)new EventDebugS2CPacket(type.valueFor(value)));
        }
    }

    private void sendToTrackingPlayers(ChunkPos chunkPos, DebugSubscriptionType<?> type, Packet<? super ClientPlayPacketListener> packet) {
        ServerChunkLoadingManager lv = this.world.getChunkManager().chunkLoadingManager;
        for (ServerPlayerEntity lv2 : lv.getPlayersWatchingChunk(chunkPos, false)) {
            if (!lv2.getSubscribedTypes().contains(type)) continue;
            lv2.networkHandler.sendPacket(packet);
        }
    }

    private void sendToTrackingPlayers(Entity entity, DebugSubscriptionType<?> type, Packet<? super ClientPlayPacketListener> packet) {
        ServerChunkLoadingManager lv = this.world.getChunkManager().chunkLoadingManager;
        lv.sendToOtherNearbyPlayersIf(entity, packet, player -> player.getSubscribedTypes().contains(type));
    }
}

