/*
 * External method calls:
 *   Lnet/minecraft/server/MinecraftServer;execute(Ljava/lang/Runnable;)V
 *   Lnet/minecraft/server/command/CommandManager;parseAndExecute(Lnet/minecraft/server/command/ServerCommandSource;Ljava/lang/String;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/chase/ChaseClient;parseMessage(Ljava/lang/String;)V
 *   Lnet/minecraft/server/chase/ChaseClient;executeTeleportCommand(Ljava/util/Scanner;)V
 *   Lnet/minecraft/server/chase/ChaseClient;executeCommand(Ljava/lang/String;)V
 */
package net.minecraft.server.chase;

import com.mojang.logging.LogUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;
import net.minecraft.command.permission.LeveledPermissionPredicate;
import net.minecraft.registry.RegistryKey;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ChaseCommand;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.io.IOUtils;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class ChaseClient {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int CONNECTION_RETRY_INTERVAL = 5;
    private final String ip;
    private final int port;
    private final MinecraftServer minecraftServer;
    private volatile boolean running;
    private @Nullable Socket socket;
    private @Nullable Thread thread;

    public ChaseClient(String ip, int port, MinecraftServer minecraftServer) {
        this.ip = ip;
        this.port = port;
        this.minecraftServer = minecraftServer;
    }

    public void start() {
        if (this.thread != null && this.thread.isAlive()) {
            LOGGER.warn("Remote control client was asked to start, but it is already running. Will ignore.");
        }
        this.running = true;
        this.thread = new Thread(this::run, "chase-client");
        this.thread.setDaemon(true);
        this.thread.start();
    }

    public void stop() {
        this.running = false;
        IOUtils.closeQuietly(this.socket);
        this.socket = null;
        this.thread = null;
    }

    public void run() {
        String string = this.ip + ":" + this.port;
        while (this.running) {
            try {
                LOGGER.info("Connecting to remote control server {}", (Object)string);
                this.socket = new Socket(this.ip, this.port);
                LOGGER.info("Connected to remote control server! Will continuously execute the command broadcasted by that server.");
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), StandardCharsets.US_ASCII));){
                    while (this.running) {
                        String string2 = bufferedReader.readLine();
                        if (string2 == null) {
                            LOGGER.warn("Lost connection to remote control server {}. Will retry in {}s.", (Object)string, (Object)5);
                            break;
                        }
                        this.parseMessage(string2);
                    }
                } catch (IOException iOException) {
                    LOGGER.warn("Lost connection to remote control server {}. Will retry in {}s.", (Object)string, (Object)5);
                }
            } catch (IOException iOException) {
                LOGGER.warn("Failed to connect to remote control server {}. Will retry in {}s.", (Object)string, (Object)5);
            }
            if (!this.running) continue;
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException interruptedException) {}
        }
    }

    private void parseMessage(String message) {
        try (Scanner scanner = new Scanner(new StringReader(message));){
            scanner.useLocale(Locale.ROOT);
            String string2 = scanner.next();
            if ("t".equals(string2)) {
                this.executeTeleportCommand(scanner);
            } else {
                LOGGER.warn("Unknown message type '{}'", (Object)string2);
            }
        } catch (NoSuchElementException noSuchElementException) {
            LOGGER.warn("Could not parse message '{}', ignoring", (Object)message);
        }
    }

    private void executeTeleportCommand(Scanner scanner) {
        this.getTeleportPos(scanner).ifPresent(pos -> this.executeCommand(String.format(Locale.ROOT, "execute in %s run tp @s %.3f %.3f %.3f %.3f %.3f", pos.dimension.getValue(), pos.pos.x, pos.pos.y, pos.pos.z, Float.valueOf(pos.rot.y), Float.valueOf(pos.rot.x))));
    }

    private Optional<TeleportPos> getTeleportPos(Scanner scanner) {
        RegistryKey lv = (RegistryKey)ChaseCommand.DIMENSIONS.get(scanner.next());
        if (lv == null) {
            return Optional.empty();
        }
        float f = scanner.nextFloat();
        float g = scanner.nextFloat();
        float h = scanner.nextFloat();
        float i = scanner.nextFloat();
        float j = scanner.nextFloat();
        return Optional.of(new TeleportPos(lv, new Vec3d(f, g, h), new Vec2f(j, i)));
    }

    private void executeCommand(String command) {
        this.minecraftServer.execute(() -> {
            List<ServerPlayerEntity> list = this.minecraftServer.getPlayerManager().getPlayerList();
            if (list.isEmpty()) {
                return;
            }
            ServerPlayerEntity lv = list.get(0);
            ServerWorld lv2 = this.minecraftServer.getOverworld();
            ServerCommandSource lv3 = new ServerCommandSource(lv.getCommandOutput(), Vec3d.of(lv2.getSpawnPoint().getPos()), Vec2f.ZERO, lv2, LeveledPermissionPredicate.OWNERS, "", ScreenTexts.EMPTY, this.minecraftServer, lv);
            CommandManager lv4 = this.minecraftServer.getCommandManager();
            lv4.parseAndExecute(lv3, command);
        });
    }

    record TeleportPos(RegistryKey<World> dimension, Vec3d pos, Vec2f rot) {
    }
}

