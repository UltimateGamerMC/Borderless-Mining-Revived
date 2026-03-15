/*
 * External method calls:
 *   Lnet/minecraft/client/util/NarratorManager;narrateSystemImmediately(Lnet/minecraft/text/Text;)V
 */
package net.minecraft.client.realms;

import com.google.common.util.concurrent.RateLimiter;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.text.Text;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class RepeatedNarrator {
    private final float permitsPerSecond;
    private final AtomicReference<@Nullable Parameters> params = new AtomicReference();

    public RepeatedNarrator(Duration duration) {
        this.permitsPerSecond = 1000.0f / (float)duration.toMillis();
    }

    public void narrate(NarratorManager narratorManager, Text text) {
        Parameters lv = this.params.updateAndGet(parameters -> {
            if (parameters == null || !text.equals(parameters.message)) {
                return new Parameters(text, RateLimiter.create(this.permitsPerSecond));
            }
            return parameters;
        });
        if (lv.rateLimiter.tryAcquire(1)) {
            narratorManager.narrateSystemImmediately(text);
        }
    }

    @Environment(value=EnvType.CLIENT)
    static class Parameters {
        final Text message;
        final RateLimiter rateLimiter;

        Parameters(Text text, RateLimiter rateLimiter) {
            this.message = text;
            this.rateLimiter = rateLimiter;
        }
    }
}

