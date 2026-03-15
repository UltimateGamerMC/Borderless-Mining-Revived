/*
 * External method calls:
 *   Lnet/minecraft/client/realms/CheckedGson;fromJson(Ljava/lang/String;Ljava/lang/Class;)Lnet/minecraft/client/realms/RealmsSerializable;
 *   Lnet/minecraft/client/realms/CheckedGson;toJson(Lnet/minecraft/client/realms/RealmsSerializable;)Ljava/lang/String;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/realms/util/RealmsPersistence;readFile()Lnet/minecraft/client/realms/util/RealmsPersistence$RealmsPersistenceData;
 *   Lnet/minecraft/client/realms/util/RealmsPersistence;writeFile(Lnet/minecraft/client/realms/util/RealmsPersistence$RealmsPersistenceData;)V
 */
package net.minecraft.client.realms.util;

import com.google.gson.annotations.SerializedName;
import com.mojang.logging.LogUtils;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.realms.CheckedGson;
import net.minecraft.client.realms.RealmsSerializable;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(value=EnvType.CLIENT)
public class RealmsPersistence {
    private static final String FILE_NAME = "realms_persistence.json";
    private static final CheckedGson CHECKED_GSON = new CheckedGson();
    private static final Logger LOGGER = LogUtils.getLogger();

    public RealmsPersistenceData load() {
        return RealmsPersistence.readFile();
    }

    public void save(RealmsPersistenceData data) {
        RealmsPersistence.writeFile(data);
    }

    public static RealmsPersistenceData readFile() {
        Path path = RealmsPersistence.getFile();
        try {
            String string = Files.readString(path, StandardCharsets.UTF_8);
            RealmsPersistenceData lv = CHECKED_GSON.fromJson(string, RealmsPersistenceData.class);
            if (lv != null) {
                return lv;
            }
        } catch (NoSuchFileException string) {
        } catch (Exception exception) {
            LOGGER.warn("Failed to read Realms storage {}", (Object)path, (Object)exception);
        }
        return new RealmsPersistenceData();
    }

    public static void writeFile(RealmsPersistenceData data) {
        Path path = RealmsPersistence.getFile();
        try {
            Files.writeString(path, (CharSequence)CHECKED_GSON.toJson(data), StandardCharsets.UTF_8, new OpenOption[0]);
        } catch (Exception exception) {
            // empty catch block
        }
    }

    private static Path getFile() {
        return MinecraftClient.getInstance().runDirectory.toPath().resolve(FILE_NAME);
    }

    @Environment(value=EnvType.CLIENT)
    public static class RealmsPersistenceData
    implements RealmsSerializable {
        @SerializedName(value="newsLink")
        public @Nullable String newsLink;
        @SerializedName(value="hasUnreadNews")
        public boolean hasUnreadNews;
    }
}

