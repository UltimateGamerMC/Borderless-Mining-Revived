/*
 * External method calls:
 *   Lnet/minecraft/server/PlayerConfigEntry;read(Lcom/google/gson/JsonObject;)Lnet/minecraft/server/PlayerConfigEntry;
 *   Lnet/minecraft/server/PlayerConfigEntry;write(Lcom/google/gson/JsonObject;)V
 *   Lnet/minecraft/server/BanEntry;write(Lcom/google/gson/JsonObject;)V
 *   Lnet/minecraft/server/PlayerConfigEntry;name()Ljava/lang/String;
 *   Lnet/minecraft/text/Text;literal(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 */
package net.minecraft.server;

import com.google.gson.JsonObject;
import java.util.Date;
import net.minecraft.server.BanEntry;
import net.minecraft.server.PlayerConfigEntry;
import net.minecraft.text.Text;
import org.jspecify.annotations.Nullable;

public class BannedPlayerEntry
extends BanEntry<PlayerConfigEntry> {
    private static final Text field_61165 = Text.translatable("commands.banlist.entry.unknown");

    public BannedPlayerEntry(@Nullable PlayerConfigEntry arg) {
        this(arg, (Date)null, (String)null, (Date)null, (String)null);
    }

    public BannedPlayerEntry(@Nullable PlayerConfigEntry arg, @Nullable Date created, @Nullable String source, @Nullable Date expiry, @Nullable String reason) {
        super(arg, created, source, expiry, reason);
    }

    public BannedPlayerEntry(JsonObject json) {
        super(PlayerConfigEntry.read(json), json);
    }

    @Override
    protected void write(JsonObject json) {
        if (this.getKey() == null) {
            return;
        }
        ((PlayerConfigEntry)this.getKey()).write(json);
        super.write(json);
    }

    @Override
    public Text toText() {
        PlayerConfigEntry lv = (PlayerConfigEntry)this.getKey();
        return lv != null ? Text.literal(lv.name()) : field_61165;
    }
}

