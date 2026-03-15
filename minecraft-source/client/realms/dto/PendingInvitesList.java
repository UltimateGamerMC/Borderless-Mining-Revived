/*
 * External method calls:
 *   Lnet/minecraft/util/LenientJsonParser;parse(Ljava/lang/String;)Lcom/google/gson/JsonElement;
 *   Lnet/minecraft/client/realms/dto/PendingInvite;parse(Lcom/google/gson/JsonObject;)Lnet/minecraft/client/realms/dto/PendingInvite;
 */
package net.minecraft.client.realms.dto;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.realms.dto.PendingInvite;
import net.minecraft.util.LenientJsonParser;
import org.slf4j.Logger;

@Environment(value=EnvType.CLIENT)
public record PendingInvitesList(List<PendingInvite> pendingInvites) {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static PendingInvitesList parse(String json) {
        ArrayList<PendingInvite> list = new ArrayList<PendingInvite>();
        try {
            JsonObject jsonObject = LenientJsonParser.parse(json).getAsJsonObject();
            if (jsonObject.get("invites").isJsonArray()) {
                for (JsonElement jsonElement : jsonObject.get("invites").getAsJsonArray()) {
                    PendingInvite lv = PendingInvite.parse(jsonElement.getAsJsonObject());
                    if (lv == null) continue;
                    list.add(lv);
                }
            }
        } catch (Exception exception) {
            LOGGER.error("Could not parse PendingInvitesList", exception);
        }
        return new PendingInvitesList(list);
    }
}

