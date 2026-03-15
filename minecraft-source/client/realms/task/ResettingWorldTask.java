/*
 * External method calls:
 *   Lnet/minecraft/client/realms/RealmsClient;create()Lnet/minecraft/client/realms/RealmsClient;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/realms/task/ResettingWorldTask;resetWorld(Lnet/minecraft/client/realms/RealmsClient;J)V
 *   Lnet/minecraft/client/realms/task/ResettingWorldTask;pause(J)V
 *   Lnet/minecraft/client/realms/task/ResettingWorldTask;error(Ljava/lang/Exception;)V
 */
package net.minecraft.client.realms.task;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.realms.RealmsClient;
import net.minecraft.client.realms.exception.RealmsServiceException;
import net.minecraft.client.realms.exception.RetryCallException;
import net.minecraft.client.realms.task.LongRunningTask;
import net.minecraft.text.Text;
import org.slf4j.Logger;

@Environment(value=EnvType.CLIENT)
public abstract class ResettingWorldTask
extends LongRunningTask {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final long serverId;
    private final Text title;
    private final Runnable callback;

    public ResettingWorldTask(long serverId, Text title, Runnable callback) {
        this.serverId = serverId;
        this.title = title;
        this.callback = callback;
    }

    protected abstract void resetWorld(RealmsClient var1, long var2) throws RealmsServiceException;

    @Override
    public void run() {
        RealmsClient lv = RealmsClient.create();
        for (int i = 0; i < 25; ++i) {
            try {
                if (this.aborted()) {
                    return;
                }
                this.resetWorld(lv, this.serverId);
                if (this.aborted()) {
                    return;
                }
                this.callback.run();
                return;
            } catch (RetryCallException lv2) {
                if (this.aborted()) {
                    return;
                }
                ResettingWorldTask.pause(lv2.delaySeconds);
                continue;
            } catch (Exception exception) {
                if (this.aborted()) {
                    return;
                }
                LOGGER.error("Couldn't reset world");
                this.error(exception);
                return;
            }
        }
    }

    @Override
    public Text getTitle() {
        return this.title;
    }
}

