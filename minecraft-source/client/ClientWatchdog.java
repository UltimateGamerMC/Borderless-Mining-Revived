/*
 * External method calls:
 *   Lnet/minecraft/server/dedicated/DedicatedServerWatchdog;createCrashReport(Ljava/lang/String;J)Lnet/minecraft/util/crash/CrashReport;
 *   Lnet/minecraft/client/MinecraftClient;saveCrashReport(Ljava/io/File;Lnet/minecraft/util/crash/CrashReport;)I
 */
package net.minecraft.client;

import java.io.File;
import java.time.Duration;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.dedicated.DedicatedServerWatchdog;
import net.minecraft.util.crash.CrashReport;

@Environment(value=EnvType.CLIENT)
public class ClientWatchdog {
    private static final Duration TIMEOUT = Duration.ofSeconds(15L);

    public static void shutdownClient(File runDir, long threadId) {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(TIMEOUT);
            } catch (InterruptedException interruptedException) {
                return;
            }
            CrashReport lv = DedicatedServerWatchdog.createCrashReport("Client shutdown", threadId);
            MinecraftClient.saveCrashReport(runDir, lv);
        });
        thread.setDaemon(true);
        thread.setName("Client shutdown watchdog");
        thread.start();
    }
}

