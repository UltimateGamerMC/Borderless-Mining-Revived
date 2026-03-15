/*
 * Internal private/static methods:
 *   Lnet/minecraft/util/profiling/jfr/InstanceType;method_37988()[Lnet/minecraft/util/profiling/jfr/InstanceType;
 */
package net.minecraft.util.profiling.jfr;

import net.minecraft.server.MinecraftServer;

public enum InstanceType {
    CLIENT("client"),
    SERVER("server");

    private final String name;

    private InstanceType(String name) {
        this.name = name;
    }

    public static InstanceType get(MinecraftServer server) {
        return server.isDedicated() ? SERVER : CLIENT;
    }

    public String getName() {
        return this.name;
    }
}

