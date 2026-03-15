/*
 * External method calls:
 *   Lnet/minecraft/util/thread/PrioritizedConsecutiveExecutor;send(Ljava/lang/Runnable;)V
 *   Lnet/minecraft/server/world/LevelPrioritizedQueue$Entry;tasks()Ljava/util/List;
 *   Lnet/minecraft/server/world/LevelPrioritizedQueue;poll()Lnet/minecraft/server/world/LevelPrioritizedQueue$Entry;
 *   Lnet/minecraft/util/thread/TaskExecutor;executeAsync(Ljava/util/function/Consumer;)Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/server/world/LevelPrioritizedQueue;updateLevel(ILnet/minecraft/util/math/ChunkPos;I)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/world/ChunkTaskScheduler;poll()Lnet/minecraft/server/world/LevelPrioritizedQueue$Entry;
 *   Lnet/minecraft/server/world/ChunkTaskScheduler;schedule(Lnet/minecraft/server/world/LevelPrioritizedQueue$Entry;)V
 *   Lnet/minecraft/server/world/ChunkTaskScheduler;onRemove(J)V
 */
package net.minecraft.server.world;

import com.mojang.logging.LogUtils;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import net.minecraft.SharedConstants;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.LevelPrioritizedQueue;
import net.minecraft.util.Unit;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.thread.PrioritizedConsecutiveExecutor;
import net.minecraft.util.thread.TaskExecutor;
import net.minecraft.util.thread.TaskQueue;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class ChunkTaskScheduler
implements ChunkHolder.LevelUpdateListener,
AutoCloseable {
    public static final int LEVELS = 4;
    private static final Logger LOGGER = LogUtils.getLogger();
    private final LevelPrioritizedQueue queue;
    private final TaskExecutor<Runnable> executor;
    private final PrioritizedConsecutiveExecutor dispatcher;
    protected boolean pollOnUpdate;

    public ChunkTaskScheduler(TaskExecutor<Runnable> executor, Executor dispatchExecutor) {
        this.queue = new LevelPrioritizedQueue(executor.getName() + "_queue");
        this.executor = executor;
        this.dispatcher = new PrioritizedConsecutiveExecutor(4, dispatchExecutor, "dispatcher");
        this.pollOnUpdate = true;
    }

    public boolean shouldDelayShutdown() {
        return this.dispatcher.hasQueuedTasks() || this.queue.hasQueuedElement();
    }

    @Override
    public void updateLevel(ChunkPos pos, IntSupplier levelGetter, int targetLevel, IntConsumer levelSetter) {
        this.dispatcher.send(new TaskQueue.PrioritizedTask(0, () -> {
            int j = levelGetter.getAsInt();
            if (SharedConstants.VERBOSE_SERVER_EVENTS) {
                LOGGER.debug("RES {} {} -> {}", pos, j, targetLevel);
            }
            this.queue.updateLevel(j, pos, targetLevel);
            levelSetter.accept(targetLevel);
        }));
    }

    public void remove(long pos, Runnable callback, boolean removeElement) {
        this.dispatcher.send(new TaskQueue.PrioritizedTask(1, () -> {
            this.queue.remove(pos, removeElement);
            this.onRemove(pos);
            if (this.pollOnUpdate) {
                this.pollOnUpdate = false;
                this.pollTask();
            }
            callback.run();
        }));
    }

    public void add(Runnable runnable, long pos, IntSupplier levelGetter) {
        this.dispatcher.send(new TaskQueue.PrioritizedTask(2, () -> {
            int i = levelGetter.getAsInt();
            if (SharedConstants.VERBOSE_SERVER_EVENTS) {
                LOGGER.debug("SUB {} {} {} {}", new ChunkPos(pos), i, this.executor, this.queue);
            }
            this.queue.add(runnable, pos, i);
            if (this.pollOnUpdate) {
                this.pollOnUpdate = false;
                this.pollTask();
            }
        }));
    }

    protected void pollTask() {
        this.dispatcher.send(new TaskQueue.PrioritizedTask(3, () -> {
            LevelPrioritizedQueue.Entry lv = this.poll();
            if (lv == null) {
                this.pollOnUpdate = true;
            } else {
                this.schedule(lv);
            }
        }));
    }

    protected void schedule(LevelPrioritizedQueue.Entry entry) {
        CompletableFuture.allOf((CompletableFuture[])entry.tasks().stream().map(runnable -> this.executor.executeAsync(future -> {
            runnable.run();
            future.complete(Unit.INSTANCE);
        })).toArray(CompletableFuture[]::new)).thenAccept(v -> this.pollTask());
    }

    protected void onRemove(long chunkPos) {
    }

    protected @Nullable LevelPrioritizedQueue.Entry poll() {
        return this.queue.poll();
    }

    @Override
    public void close() {
        this.executor.close();
    }
}

