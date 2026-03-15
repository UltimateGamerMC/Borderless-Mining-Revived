/*
 * External method calls:
 *   Lnet/minecraft/client/realms/CheckedGson;fromJson(Ljava/lang/String;Ljava/lang/Class;)Lnet/minecraft/client/realms/RealmsSerializable;
 */
package net.minecraft.client.realms.dto;

import com.google.gson.annotations.SerializedName;
import com.mojang.logging.LogUtils;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.realms.CheckedGson;
import net.minecraft.client.realms.RealmsSerializable;
import net.minecraft.client.realms.dto.RealmsServer;
import org.slf4j.Logger;

@Environment(value=EnvType.CLIENT)
public record RealmsServerList(@SerializedName(value="servers") List<RealmsServer> servers) implements RealmsSerializable
{
    private static final Logger LOGGER = LogUtils.getLogger();

    public static RealmsServerList parse(CheckedGson gson, String json) {
        try {
            RealmsServerList lv = gson.fromJson(json, RealmsServerList.class);
            if (lv != null) {
                lv.servers.forEach(RealmsServer::replaceNullsWithDefaults);
                return lv;
            }
            LOGGER.error("Could not parse McoServerList: {}", (Object)json);
        } catch (Exception exception) {
            LOGGER.error("Could not parse McoServerList", exception);
        }
        return new RealmsServerList(List.of());
    }

    @SerializedName(value="servers")
    public List<RealmsServer> servers() {
        return this.servers;
    }
}

