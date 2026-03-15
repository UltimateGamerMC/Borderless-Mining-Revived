/*
 * External method calls:
 *   Lnet/minecraft/util/LenientJsonParser;parse(Ljava/lang/String;)Lcom/google/gson/JsonElement;
 *   Lnet/minecraft/client/realms/dto/Backup;parse(Lcom/google/gson/JsonElement;)Lnet/minecraft/client/realms/dto/Backup;
 */
package net.minecraft.client.realms.dto;

import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.realms.dto.Backup;
import net.minecraft.util.LenientJsonParser;
import org.slf4j.Logger;

@Environment(value=EnvType.CLIENT)
public record BackupList(List<Backup> backups) {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static BackupList parse(String json) {
        ArrayList<Backup> list = new ArrayList<Backup>();
        try {
            JsonElement jsonElement = LenientJsonParser.parse(json).getAsJsonObject().get("backups");
            if (jsonElement.isJsonArray()) {
                for (JsonElement jsonElement2 : jsonElement.getAsJsonArray()) {
                    Backup lv = Backup.parse(jsonElement2);
                    if (lv == null) continue;
                    list.add(lv);
                }
            }
        } catch (Exception exception) {
            LOGGER.error("Could not parse BackupList", exception);
        }
        return new BackupList(list);
    }
}

