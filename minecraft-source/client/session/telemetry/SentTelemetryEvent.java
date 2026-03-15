/*
 * External method calls:
 *   Lnet/minecraft/client/session/telemetry/PropertyMap;keySet()Ljava/util/Set;
 *   Lnet/minecraft/client/session/telemetry/TelemetryEventType;createEvent(Lcom/mojang/authlib/minecraft/TelemetrySession;Lnet/minecraft/client/session/telemetry/PropertyMap;)Lcom/mojang/authlib/minecraft/TelemetryEvent;
 *   Lnet/minecraft/client/session/telemetry/TelemetryEventProperty;id()Ljava/lang/String;
 */
package net.minecraft.client.session.telemetry;

import com.mojang.authlib.minecraft.TelemetryEvent;
import com.mojang.authlib.minecraft.TelemetrySession;
import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.session.telemetry.PropertyMap;
import net.minecraft.client.session.telemetry.TelemetryEventType;

@Environment(value=EnvType.CLIENT)
public record SentTelemetryEvent(TelemetryEventType type, PropertyMap properties) {
    public static final Codec<SentTelemetryEvent> CODEC = TelemetryEventType.CODEC.dispatchStable(SentTelemetryEvent::type, TelemetryEventType::getCodec);

    public SentTelemetryEvent {
        properties.keySet().forEach(property -> {
            if (!type.hasProperty(property)) {
                throw new IllegalArgumentException("Property '" + property.id() + "' not expected for event: '" + type.getId() + "'");
            }
        });
    }

    public TelemetryEvent createEvent(TelemetrySession session) {
        return this.type.createEvent(session, this.properties);
    }
}

