/*
 * External method calls:
 *   Lnet/minecraft/registry/Registry;register(Lnet/minecraft/registry/Registry;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/world/ChunkTicketType;register(Ljava/lang/String;JI)Lnet/minecraft/server/world/ChunkTicketType;
 */
package net.minecraft.server.world;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public record ChunkTicketType(long expiryTicks, @Flags int flags) {
    public static final long NO_EXPIRATION = 0L;
    public static final int SERIALIZE = 1;
    public static final int FOR_LOADING = 2;
    public static final int FOR_SIMULATION = 4;
    public static final int RESETS_IDLE_TIMEOUT = 8;
    public static final int CAN_EXPIRE_BEFORE_LOAD = 16;
    public static final ChunkTicketType PLAYER_SPAWN = ChunkTicketType.register("player_spawn", 20L, FOR_LOADING);
    public static final ChunkTicketType SPAWN_SEARCH = ChunkTicketType.register("spawn_search", 1L, FOR_LOADING);
    public static final ChunkTicketType DRAGON = ChunkTicketType.register("dragon", NO_EXPIRATION, FOR_LOADING | FOR_SIMULATION);
    public static final ChunkTicketType PLAYER_LOADING = ChunkTicketType.register("player_loading", NO_EXPIRATION, FOR_LOADING);
    public static final ChunkTicketType PLAYER_SIMULATION = ChunkTicketType.register("player_simulation", NO_EXPIRATION, FOR_SIMULATION | RESETS_IDLE_TIMEOUT);
    public static final ChunkTicketType FORCED = ChunkTicketType.register("forced", NO_EXPIRATION, SERIALIZE | FOR_LOADING | FOR_SIMULATION | RESETS_IDLE_TIMEOUT);
    public static final ChunkTicketType PORTAL = ChunkTicketType.register("portal", 300L, SERIALIZE | FOR_LOADING | FOR_SIMULATION | RESETS_IDLE_TIMEOUT);
    public static final ChunkTicketType ENDER_PEARL = ChunkTicketType.register("ender_pearl", 40L, FOR_LOADING | FOR_SIMULATION | RESETS_IDLE_TIMEOUT);
    public static final ChunkTicketType UNKNOWN = ChunkTicketType.register("unknown", 1L, CAN_EXPIRE_BEFORE_LOAD | FOR_LOADING);

    private static ChunkTicketType register(String id, long expiryTicks, @Flags int flags) {
        return Registry.register(Registries.TICKET_TYPE, id, new ChunkTicketType(expiryTicks, flags));
    }

    public boolean shouldSerialize() {
        return (this.flags & 1) != 0;
    }

    public boolean isForLoading() {
        return (this.flags & 2) != 0;
    }

    public boolean isForSimulation() {
        return (this.flags & 4) != 0;
    }

    public boolean resetsIdleTimeout() {
        return (this.flags & 8) != 0;
    }

    public boolean canExpireBeforeLoad() {
        return (this.flags & 0x10) != 0;
    }

    public boolean canExpire() {
        return this.expiryTicks != 0L;
    }

    @Flags
    public int flags() {
        return this.flags;
    }

    @Retention(value=RetentionPolicy.CLASS)
    @Target(value={ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.TYPE_USE})
    public static @interface Flags {
    }
}

