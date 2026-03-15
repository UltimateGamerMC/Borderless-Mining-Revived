/*
 * External method calls:
 *   Lnet/minecraft/util/LenientJsonParser;parse(Ljava/lang/String;)Lcom/google/gson/JsonElement;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/realms/dto/Subscription;typeFrom(Ljava/lang/String;)Lnet/minecraft/client/realms/dto/Subscription$SubscriptionType;
 */
package net.minecraft.client.realms.dto;

import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import java.time.Instant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.realms.util.JsonUtils;
import net.minecraft.util.LenientJsonParser;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(value=EnvType.CLIENT)
public record Subscription(Instant startDate, int daysLeft, SubscriptionType type) {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static Subscription parse(String json) {
        try {
            JsonObject jsonObject = LenientJsonParser.parse(json).getAsJsonObject();
            return new Subscription(JsonUtils.getInstantOr("startDate", jsonObject), JsonUtils.getIntOr("daysLeft", jsonObject, 0), Subscription.typeFrom(JsonUtils.getNullableStringOr("subscriptionType", jsonObject, null)));
        } catch (Exception exception) {
            LOGGER.error("Could not parse Subscription", exception);
            return new Subscription(Instant.EPOCH, 0, SubscriptionType.NORMAL);
        }
    }

    private static SubscriptionType typeFrom(@Nullable String subscriptionType) {
        try {
            if (subscriptionType != null) {
                return SubscriptionType.valueOf(subscriptionType);
            }
        } catch (Exception exception) {
            // empty catch block
        }
        return SubscriptionType.NORMAL;
    }

    @Environment(value=EnvType.CLIENT)
    public static enum SubscriptionType {
        NORMAL,
        RECURRING;

    }
}

