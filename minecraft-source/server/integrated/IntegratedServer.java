/*
 * External method calls:
 *   Lnet/minecraft/GameVersion;name()Ljava/lang/String;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;using(Lnet/minecraft/world/debug/gizmo/GizmoCollector;)Lnet/minecraft/world/debug/gizmo/GizmoDrawing$CollectorScope;
 *   Lnet/minecraft/server/MinecraftServer;processPacketsAndTick(Z)V
 *   Lnet/minecraft/world/debug/gizmo/GizmoCollectorImpl;extractGizmos()Ljava/util/List;
 *   Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V
 *   Lnet/minecraft/server/MinecraftServer;tick(Ljava/util/function/BooleanSupplier;)V
 *   Lnet/minecraft/server/network/ServerPlayerEntity;incrementStat(Lnet/minecraft/util/Identifier;)V
 *   Lnet/minecraft/util/SystemDetails;addSection(Ljava/lang/String;Ljava/lang/String;)V
 *   Lnet/minecraft/util/SystemDetails;addSection(Ljava/lang/String;Ljava/util/function/Supplier;)V
 *   Lnet/minecraft/util/ModStatus;combine(Lnet/minecraft/util/ModStatus;)Lnet/minecraft/util/ModStatus;
 *   Lnet/minecraft/server/ServerNetworkIo;bind(Ljava/net/InetAddress;I)V
 *   Lnet/minecraft/server/command/CommandManager;sendCommandTree(Lnet/minecraft/server/network/ServerPlayerEntity;)V
 *   Lnet/minecraft/server/MinecraftServer;stop(Z)V
 *   Lnet/minecraft/server/PlayerConfigEntry;name()Ljava/lang/String;
 *   Lnet/minecraft/storage/NbtReadView;create(Lnet/minecraft/util/ErrorReporter;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/storage/ReadView;
 *   Lnet/minecraft/storage/ReadView;read(Lcom/mojang/serialization/MapCodec;)Ljava/util/Optional;
 *   Lnet/minecraft/server/network/ServerPlayerEntity$SavePos;dimension()Ljava/util/Optional;
 *   Lnet/minecraft/server/network/ServerPlayerEntity$SavePos;position()Ljava/util/Optional;
 *   Lnet/minecraft/server/MinecraftServer;saveAll(ZZZ)Z
 *   Lnet/minecraft/client/MinecraftClient;execute(Ljava/lang/Runnable;)V
 *   Lnet/minecraft/server/MinecraftServer;onChunkLoadFailure(Ljava/lang/Throwable;Lnet/minecraft/world/storage/StorageKey;Lnet/minecraft/util/math/ChunkPos;)V
 *   Lnet/minecraft/server/MinecraftServer;onChunkSaveFailure(Ljava/lang/Throwable;Lnet/minecraft/world/storage/StorageKey;Lnet/minecraft/util/math/ChunkPos;)V
 *   Lnet/minecraft/client/toast/SystemToast;addChunkSaveFailure(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/util/math/ChunkPos;)V
 *   Lnet/minecraft/client/toast/SystemToast;addChunkLoadFailure(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/util/math/ChunkPos;)V
 *   Lnet/minecraft/client/toast/SystemToast;addLowDiskSpace(Lnet/minecraft/client/MinecraftClient;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/integrated/IntegratedServer;saveAll(ZZZ)Z
 *   Lnet/minecraft/server/integrated/IntegratedServer;submitAndJoin(Ljava/lang/Runnable;)V
 */
package net.minecraft.server.integrated;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.BooleanSupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.LanServerPinger;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.command.permission.LeveledPermissionPredicate;
import net.minecraft.command.permission.PermissionPredicate;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerConfigEntry;
import net.minecraft.server.SaveLoader;
import net.minecraft.server.integrated.IntegratedPlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.storage.NbtReadView;
import net.minecraft.storage.ReadView;
import net.minecraft.util.ApiServices;
import net.minecraft.util.ErrorReporter;
import net.minecraft.util.ModStatus;
import net.minecraft.util.SystemDetails;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.profiler.MultiValueDebugSampleLogImpl;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.profiler.Profilers;
import net.minecraft.util.profiler.log.DebugSampleLog;
import net.minecraft.world.GameMode;
import net.minecraft.world.chunk.ChunkLoadProgress;
import net.minecraft.world.debug.gizmo.GizmoCollectorImpl;
import net.minecraft.world.debug.gizmo.GizmoDrawing;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.storage.StorageKey;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(value=EnvType.CLIENT)
public class IntegratedServer
extends MinecraftServer {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int field_34964 = 2;
    public static final int field_62489 = 8;
    private final MinecraftClient client;
    private boolean paused = true;
    private int lanPort = -1;
    private @Nullable GameMode forcedGameMode;
    private @Nullable LanServerPinger lanPinger;
    private @Nullable UUID localPlayerUuid;
    private int simulationDistance = 0;
    private volatile List<GizmoCollectorImpl.Entry> gizmoEntries = new ArrayList<GizmoCollectorImpl.Entry>();
    private final GizmoCollectorImpl gizmoCollector = new GizmoCollectorImpl();

    public IntegratedServer(Thread serverThread, MinecraftClient client, LevelStorage.Session session, ResourcePackManager dataPackManager, SaveLoader saveLoader, ApiServices apiServices, ChunkLoadProgress chunkLoadProgress) {
        super(serverThread, session, dataPackManager, saveLoader, client.getNetworkProxy(), client.getDataFixer(), apiServices, chunkLoadProgress);
        this.setHostProfile(client.getGameProfile());
        this.setDemo(client.isDemo());
        this.setPlayerManager(new IntegratedPlayerManager(this, this.getCombinedDynamicRegistries(), this.saveHandler));
        this.client = client;
    }

    @Override
    public boolean setupServer() {
        LOGGER.info("Starting integrated minecraft server version {}", (Object)SharedConstants.getGameVersion().name());
        this.setOnlineMode(true);
        this.generateKeyPair();
        this.loadWorld();
        GameProfile gameProfile = this.getHostProfile();
        String string = this.getSaveProperties().getLevelName();
        this.setMotd((String)(gameProfile != null ? gameProfile.name() + " - " + string : string));
        return true;
    }

    @Override
    public boolean isPaused() {
        return this.paused;
    }

    @Override
    public void processPacketsAndTick(boolean sprint) {
        try (GizmoDrawing.CollectorScope lv = GizmoDrawing.using(this.gizmoCollector);){
            super.processPacketsAndTick(sprint);
        }
        if (this.getTickManager().shouldTick()) {
            this.gizmoEntries = this.gizmoCollector.extractGizmos();
        }
    }

    @Override
    public void tick(BooleanSupplier shouldKeepTicking) {
        int j;
        boolean bl = this.paused;
        this.paused = MinecraftClient.getInstance().isPaused() || this.getPlayerManager().getPlayerList().isEmpty();
        Profiler lv = Profilers.get();
        if (!bl && this.paused) {
            lv.push("autoSave");
            LOGGER.info("Saving and pausing game...");
            this.saveAll(false, false, false);
            lv.pop();
        }
        if (this.paused) {
            this.incrementTotalWorldTimeStat();
            return;
        }
        if (bl) {
            this.sendTimeUpdatePackets();
        }
        super.tick(shouldKeepTicking);
        int i = Math.max(2, this.client.options.getViewDistance().getValue());
        if (i != this.getPlayerManager().getViewDistance()) {
            LOGGER.info("Changing view distance to {}, from {}", (Object)i, (Object)this.getPlayerManager().getViewDistance());
            this.getPlayerManager().setViewDistance(i);
        }
        if ((j = Math.max(2, this.client.options.getSimulationDistance().getValue())) != this.simulationDistance) {
            LOGGER.info("Changing simulation distance to {}, from {}", (Object)j, (Object)this.simulationDistance);
            this.getPlayerManager().setSimulationDistance(j);
            this.simulationDistance = j;
        }
    }

    @Override
    protected MultiValueDebugSampleLogImpl getDebugSampleLog() {
        return this.client.getDebugHud().getTickNanosLog();
    }

    @Override
    public boolean shouldPushTickTimeLog() {
        return true;
    }

    private void incrementTotalWorldTimeStat() {
        this.tickNetworkIo();
        for (ServerPlayerEntity lv : this.getPlayerManager().getPlayerList()) {
            lv.incrementStat(Stats.TOTAL_WORLD_TIME);
        }
    }

    @Override
    public boolean shouldBroadcastRconToOps() {
        return true;
    }

    @Override
    public boolean shouldBroadcastConsoleToOps() {
        return true;
    }

    @Override
    public Path getRunDirectory() {
        return this.client.runDirectory.toPath();
    }

    @Override
    public boolean isDedicated() {
        return false;
    }

    @Override
    public int getRateLimit() {
        return 0;
    }

    @Override
    public boolean isUsingNativeTransport() {
        return this.client.options.shouldUseNativeTransport();
    }

    @Override
    public void setCrashReport(CrashReport report) {
        this.client.setCrashReportSupplier(report);
    }

    @Override
    public SystemDetails addExtraSystemDetails(SystemDetails details) {
        details.addSection("Type", "Integrated Server (map_client.txt)");
        details.addSection("Is Modded", () -> this.getModStatus().getMessage());
        details.addSection("Launched Version", this.client::getGameVersion);
        return details;
    }

    @Override
    public ModStatus getModStatus() {
        return MinecraftClient.getModStatus().combine(super.getModStatus());
    }

    @Override
    public boolean openToLan(@Nullable GameMode gameMode, boolean cheatsAllowed, int port) {
        try {
            this.client.loadBlockList();
            this.client.getNetworkHandler().fetchProfileKey();
            this.getNetworkIo().bind(null, port);
            LOGGER.info("Started serving on {}", (Object)port);
            this.lanPort = port;
            this.lanPinger = new LanServerPinger(this.getServerMotd(), "" + port);
            this.lanPinger.start();
            this.forcedGameMode = gameMode;
            this.getPlayerManager().setCheatsAllowed(cheatsAllowed);
            LeveledPermissionPredicate lv = this.getPermissionLevel(this.client.player.getPlayerConfigEntry());
            this.client.player.setPermissions(lv);
            for (ServerPlayerEntity lv2 : this.getPlayerManager().getPlayerList()) {
                this.getCommandManager().sendCommandTree(lv2);
            }
            return true;
        } catch (IOException iOException) {
            return false;
        }
    }

    @Override
    public void shutdown() {
        super.shutdown();
        if (this.lanPinger != null) {
            this.lanPinger.interrupt();
            this.lanPinger = null;
        }
    }

    @Override
    public void stop(boolean waitForShutdown) {
        this.submitAndJoin(() -> {
            ArrayList<ServerPlayerEntity> list = Lists.newArrayList(this.getPlayerManager().getPlayerList());
            for (ServerPlayerEntity lv : list) {
                if (lv.getUuid().equals(this.localPlayerUuid)) continue;
                this.getPlayerManager().remove(lv);
            }
        });
        super.stop(waitForShutdown);
        if (this.lanPinger != null) {
            this.lanPinger.interrupt();
            this.lanPinger = null;
        }
    }

    @Override
    public boolean isRemote() {
        return this.lanPort > -1;
    }

    @Override
    public int getServerPort() {
        return this.lanPort;
    }

    @Override
    public void setDefaultGameMode(GameMode gameMode) {
        super.setDefaultGameMode(gameMode);
        this.forcedGameMode = null;
    }

    @Override
    public LeveledPermissionPredicate getOpPermissionLevel() {
        return LeveledPermissionPredicate.GAMEMASTERS;
    }

    @Override
    public LeveledPermissionPredicate getFunctionPermissions() {
        return LeveledPermissionPredicate.GAMEMASTERS;
    }

    public void setLocalPlayerUuid(UUID localPlayerUuid) {
        this.localPlayerUuid = localPlayerUuid;
    }

    @Override
    public boolean isHost(PlayerConfigEntry player) {
        return this.getHostProfile() != null && player.name().equalsIgnoreCase(this.getHostProfile().name());
    }

    @Override
    public int adjustTrackingDistance(int initialDistance) {
        return (int)(this.client.options.getEntityDistanceScaling().getValue() * (double)initialDistance);
    }

    @Override
    public boolean syncChunkWrites() {
        return this.client.options.syncChunkWrites;
    }

    @Override
    public @Nullable GameMode getForcedGameMode() {
        if (this.isRemote() && !this.isHardcore()) {
            return MoreObjects.firstNonNull(this.forcedGameMode, this.saveProperties.getGameMode());
        }
        return null;
    }

    @Override
    public GlobalPos getSpawnPos() {
        NbtCompound lv = this.saveProperties.getPlayerData();
        if (lv == null) {
            return super.getSpawnPos();
        }
        try (ErrorReporter.Logging lv2 = new ErrorReporter.Logging(LOGGER);){
            ReadView lv3 = NbtReadView.create(lv2, this.getRegistryManager(), lv);
            ServerPlayerEntity.SavePos lv4 = lv3.read(ServerPlayerEntity.SavePos.CODEC).orElse(ServerPlayerEntity.SavePos.EMPTY);
            if (lv4.dimension().isPresent() && lv4.position().isPresent()) {
                GlobalPos globalPos = new GlobalPos(lv4.dimension().get(), BlockPos.ofFloored(lv4.position().get()));
                return globalPos;
            }
        }
        return super.getSpawnPos();
    }

    @Override
    public boolean saveAll(boolean suppressLogs, boolean flush, boolean force) {
        boolean bl4 = super.saveAll(suppressLogs, flush, force);
        this.checkLowDiskSpaceWarning();
        return bl4;
    }

    private void checkLowDiskSpaceWarning() {
        if (this.session.shouldShowLowDiskSpaceWarning()) {
            this.client.execute(() -> SystemToast.addLowDiskSpace(this.client));
        }
    }

    @Override
    public void onChunkLoadFailure(Throwable exception, StorageKey key, ChunkPos chunkPos) {
        super.onChunkLoadFailure(exception, key, chunkPos);
        this.checkLowDiskSpaceWarning();
        this.client.execute(() -> SystemToast.addChunkLoadFailure(this.client, chunkPos));
    }

    @Override
    public void onChunkSaveFailure(Throwable exception, StorageKey key, ChunkPos chunkPos) {
        super.onChunkSaveFailure(exception, key, chunkPos);
        this.checkLowDiskSpaceWarning();
        this.client.execute(() -> SystemToast.addChunkSaveFailure(this.client, chunkPos));
    }

    @Override
    public int getMaxPlayerCount() {
        return 8;
    }

    public Collection<GizmoCollectorImpl.Entry> getGizmoEntries() {
        return this.gizmoEntries;
    }

    @Override
    public /* synthetic */ DebugSampleLog getDebugSampleLog() {
        return this.getDebugSampleLog();
    }

    @Override
    public /* synthetic */ PermissionPredicate getFunctionPermissions() {
        return this.getFunctionPermissions();
    }
}

