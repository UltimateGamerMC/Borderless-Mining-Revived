/*
 * External method calls:
 *   Lnet/minecraft/util/StringIdentifiable;createCodec(Ljava/util/function/Supplier;)Lnet/minecraft/util/StringIdentifiable$EnumCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/hud/debug/DebugHudEntryVisibility;method_72781()[Lnet/minecraft/client/gui/hud/debug/DebugHudEntryVisibility;
 */
package net.minecraft.client.gui.hud.debug;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.StringIdentifiable;

@Environment(value=EnvType.CLIENT)
public enum DebugHudEntryVisibility implements StringIdentifiable
{
    ALWAYS_ON("alwaysOn"),
    IN_OVERLAY("inOverlay"),
    NEVER("never");

    public static final StringIdentifiable.EnumCodec<DebugHudEntryVisibility> CODEC;
    private final String id;

    private DebugHudEntryVisibility(String id) {
        this.id = id;
    }

    @Override
    public String asString() {
        return this.id;
    }

    static {
        CODEC = StringIdentifiable.createCodec(DebugHudEntryVisibility::values);
    }
}

