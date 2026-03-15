/*
 * External method calls:
 *   Lnet/minecraft/server/dedicated/MinecraftDedicatedServer;submit(Ljava/util/function/Supplier;)Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/server/dedicated/MinecraftDedicatedServer;submit(Ljava/lang/Runnable;)Ljava/util/concurrent/CompletableFuture;
 */
package net.minecraft.server.dedicated.management;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.dedicated.management.Submitter;

public class DedicatedServerSubmitter
implements Submitter {
    private final MinecraftDedicatedServer server;

    public DedicatedServerSubmitter(MinecraftDedicatedServer server) {
        this.server = server;
    }

    @Override
    public <V> CompletableFuture<V> submit(Supplier<V> task) {
        return this.server.submit(task);
    }

    @Override
    public CompletableFuture<Void> submit(Runnable task) {
        return this.server.submit(task);
    }
}

