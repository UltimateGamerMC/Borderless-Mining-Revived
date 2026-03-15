/*
 * Internal private/static methods:
 *   Lnet/minecraft/server/PlayerConfigEntry;id()Ljava/util/UUID;
 *   Lnet/minecraft/server/PlayerConfigEntry;name()Ljava/lang/String;
 */
package net.minecraft.server;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.yggdrasil.response.NameAndId;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.UUID;
import net.minecraft.util.Uuids;
import org.jspecify.annotations.Nullable;

public record PlayerConfigEntry(UUID id, String name) {
    public static final Codec<PlayerConfigEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(((MapCodec)Uuids.STRING_CODEC.fieldOf("id")).forGetter(PlayerConfigEntry::id), ((MapCodec)Codec.STRING.fieldOf("name")).forGetter(PlayerConfigEntry::name)).apply((Applicative<PlayerConfigEntry, ?>)instance, PlayerConfigEntry::new));

    public PlayerConfigEntry(GameProfile profile) {
        this(profile.id(), profile.name());
    }

    public PlayerConfigEntry(NameAndId nameAndId) {
        this(nameAndId.id(), nameAndId.name());
    }

    public static @Nullable PlayerConfigEntry read(JsonObject object) {
        UUID uUID;
        if (!object.has("uuid") || !object.has("name")) {
            return null;
        }
        String string = object.get("uuid").getAsString();
        try {
            uUID = UUID.fromString(string);
        } catch (Throwable throwable) {
            return null;
        }
        return new PlayerConfigEntry(uUID, object.get("name").getAsString());
    }

    public void write(JsonObject object) {
        object.addProperty("uuid", this.id().toString());
        object.addProperty("name", this.name());
    }

    public static PlayerConfigEntry fromNickname(String nickname) {
        UUID uUID = Uuids.getOfflinePlayerUuid(nickname);
        return new PlayerConfigEntry(uUID, nickname);
    }
}

