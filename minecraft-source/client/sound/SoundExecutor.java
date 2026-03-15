/*
 * External method calls:
 *   Lnet/minecraft/util/thread/ThreadExecutor;send(Ljava/lang/Runnable;)V
 *   Lnet/minecraft/util/crash/CrashReport;create(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/util/crash/CrashReport;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/sound/SoundExecutor;createThread()Ljava/lang/Thread;
 *   Lnet/minecraft/client/sound/SoundExecutor;runTasks(Ljava/util/function/BooleanSupplier;)V
 */
package net.minecraft.client.sound;

import java.util.concurrent.locks.LockSupport;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.thread.ThreadExecutor;

@Environment(value=EnvType.CLIENT)
public class SoundExecutor
extends ThreadExecutor<Runnable> {
    private Thread thread = this.createThread();
    private volatile boolean stopped;

    public SoundExecutor() {
        super("Sound executor");
    }

    private Thread createThread() {
        Thread thread2 = new Thread(this::waitForStop);
        thread2.setDaemon(true);
        thread2.setName("Sound engine");
        thread2.setUncaughtExceptionHandler((thread, throwable) -> MinecraftClient.getInstance().setCrashReportSupplierAndAddDetails(CrashReport.create(throwable, "Uncaught exception on thread: " + thread.getName())));
        thread2.start();
        return thread2;
    }

    @Override
    public Runnable createTask(Runnable runnable) {
        return runnable;
    }

    @Override
    public void send(Runnable runnable) {
        if (!this.stopped) {
            super.send(runnable);
        }
    }

    @Override
    protected boolean canExecute(Runnable task) {
        return !this.stopped;
    }

    @Override
    protected Thread getThread() {
        return this.thread;
    }

    private void waitForStop() {
        while (!this.stopped) {
            this.runTasks(() -> this.stopped);
        }
    }

    @Override
    protected void waitForTasks() {
        LockSupport.park("waiting for tasks");
    }

    public void stop() {
        this.stopped = true;
        this.cancelTasks();
        this.thread.interrupt();
        try {
            this.thread.join();
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
        }
    }

    public void restart() {
        this.stopped = false;
        this.thread = this.createThread();
    }
}

