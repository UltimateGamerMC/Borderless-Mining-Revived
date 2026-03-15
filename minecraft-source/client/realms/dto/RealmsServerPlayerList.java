/*
 * External method calls:
 *   Lnet/minecraft/util/JsonHelper;deserialize(Ljava/lang/String;)Lcom/google/gson/JsonObject;
 *   Lnet/minecraft/util/LenientJsonParser;parse(Ljava/lang/String;)Lcom/google/gson/JsonElement;
 *   Lnet/minecraft/client/MinecraftClient;uuidEquals(Ljava/util/UUID;)Z
 *   Lnet/minecraft/component/type/ProfileComponent;ofDynamic(Ljava/util/UUID;)Lnet/minecraft/component/type/ProfileComponent;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/realms/dto/RealmsServerPlayerList;parsePlayers(Lcom/google/gson/JsonArray;)Ljava/util/List;
 */
package net.minecraft.client.realms.dto;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.realms.util.JsonUtils;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.LenientJsonParser;
import org.slf4j.Logger;

@Environment(value=EnvType.CLIENT)
public record RealmsServerPlayerList(Map<Long, List<ProfileComponent>> serverIdToPlayers) {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static RealmsServerPlayerList parse(String json) {
        ImmutableMap.Builder builder = ImmutableMap.builder();
        try {
            JsonObject jsonObject = JsonHelper.deserialize(json);
            if (JsonHelper.hasArray(jsonObject, "lists")) {
                JsonArray jsonArray = jsonObject.getAsJsonArray("lists");
                for (JsonElement jsonElement : jsonArray) {
                    JsonElement jsonElement2;
                    JsonObject jsonObject2 = jsonElement.getAsJsonObject();
                    String string2 = JsonUtils.getNullableStringOr("playerList", jsonObject2, null);
                    List<Object> list = string2 != null ? ((jsonElement2 = LenientJsonParser.parse(string2)).isJsonArray() ? RealmsServerPlayerList.parsePlayers(jsonElement2.getAsJsonArray()) : Lists.newArrayList()) : Lists.newArrayList();
                    builder.put(JsonUtils.getLongOr("serverId", jsonObject2, -1L), list);
                }
            }
        } catch (Exception exception) {
            LOGGER.error("Could not parse RealmsServerPlayerLists", exception);
        }
        return new RealmsServerPlayerList(builder.build());
    }

    private static List<ProfileComponent> parsePlayers(JsonArray jsonArray) {
        ArrayList<ProfileComponent> list = new ArrayList<ProfileComponent>(jsonArray.size());
        for (JsonElement jsonElement : jsonArray) {
            UUID uUID;
            if (!jsonElement.isJsonObject() || (uUID = JsonUtils.getUuidOr("playerId", jsonElement.getAsJsonObject(), null)) == null || MinecraftClient.getInstance().uuidEquals(uUID)) continue;
            list.add(ProfileComponent.ofDynamic(uUID));
        }
        return list;
    }

    public List<ProfileComponent> get(long serverId) {
        List<ProfileComponent> list = this.serverIdToPlayers.get(serverId);
        if (list != null) {
            return list;
        }
        return List.of();
    }
}

