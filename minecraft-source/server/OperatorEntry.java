/*
 * External method calls:
 *   Lnet/minecraft/server/PlayerConfigEntry;read(Lcom/google/gson/JsonObject;)Lnet/minecraft/server/PlayerConfigEntry;
 *   Lnet/minecraft/command/permission/PermissionLevel;fromLevel(I)Lnet/minecraft/command/permission/PermissionLevel;
 *   Lnet/minecraft/command/permission/LeveledPermissionPredicate;fromLevel(Lnet/minecraft/command/permission/PermissionLevel;)Lnet/minecraft/command/permission/LeveledPermissionPredicate;
 *   Lnet/minecraft/server/PlayerConfigEntry;write(Lcom/google/gson/JsonObject;)V
 */
package net.minecraft.server;

import com.google.gson.JsonObject;
import net.minecraft.command.permission.LeveledPermissionPredicate;
import net.minecraft.command.permission.PermissionLevel;
import net.minecraft.server.PlayerConfigEntry;
import net.minecraft.server.ServerConfigEntry;

public class OperatorEntry
extends ServerConfigEntry<PlayerConfigEntry> {
    private final LeveledPermissionPredicate level;
    private final boolean bypassPlayerLimit;

    public OperatorEntry(PlayerConfigEntry player, LeveledPermissionPredicate level, boolean bypassPlayerLimit) {
        super(player);
        this.level = level;
        this.bypassPlayerLimit = bypassPlayerLimit;
    }

    public OperatorEntry(JsonObject json) {
        super(PlayerConfigEntry.read(json));
        PermissionLevel lv = json.has("level") ? PermissionLevel.fromLevel(json.get("level").getAsInt()) : PermissionLevel.ALL;
        this.level = LeveledPermissionPredicate.fromLevel(lv);
        this.bypassPlayerLimit = json.has("bypassesPlayerLimit") && json.get("bypassesPlayerLimit").getAsBoolean();
    }

    public LeveledPermissionPredicate getLevel() {
        return this.level;
    }

    public boolean canBypassPlayerLimit() {
        return this.bypassPlayerLimit;
    }

    @Override
    protected void write(JsonObject json) {
        if (this.getKey() == null) {
            return;
        }
        ((PlayerConfigEntry)this.getKey()).write(json);
        json.addProperty("level", this.level.getLevel().getLevel());
        json.addProperty("bypassesPlayerLimit", this.bypassPlayerLimit);
    }
}

