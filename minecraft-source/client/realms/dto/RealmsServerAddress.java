/*
 * External method calls:
 *   Lnet/minecraft/client/realms/CheckedGson;fromJson(Ljava/lang/String;Ljava/lang/Class;)Lnet/minecraft/client/realms/RealmsSerializable;
 */
package net.minecraft.client.realms.dto;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.realms.CheckedGson;
import net.minecraft.client.realms.RealmsSerializable;
import net.minecraft.client.realms.ServiceQuality;
import net.minecraft.client.realms.dto.RealmsRegion;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(value=EnvType.CLIENT)
public record RealmsServerAddress(@SerializedName(value="address") @Nullable String address, @SerializedName(value="resourcePackUrl") @Nullable String resourcePackUrl, @SerializedName(value="resourcePackHash") @Nullable String resourcePackHash, @SerializedName(value="sessionRegionData") @Nullable RegionData regionData) implements RealmsSerializable
{
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final RealmsServerAddress NULL = new RealmsServerAddress(null, null, null, null);

    public static RealmsServerAddress parse(CheckedGson gson, String json) {
        try {
            RealmsServerAddress lv = gson.fromJson(json, RealmsServerAddress.class);
            if (lv == null) {
                LOGGER.error("Could not parse RealmsServerAddress: {}", (Object)json);
                return NULL;
            }
            return lv;
        } catch (Exception exception) {
            LOGGER.error("Could not parse RealmsServerAddress", exception);
            return NULL;
        }
    }

    @SerializedName(value="address")
    public @Nullable String address() {
        return this.address;
    }

    @SerializedName(value="resourcePackUrl")
    public @Nullable String resourcePackUrl() {
        return this.resourcePackUrl;
    }

    @SerializedName(value="resourcePackHash")
    public @Nullable String resourcePackHash() {
        return this.resourcePackHash;
    }

    @SerializedName(value="sessionRegionData")
    public @Nullable RegionData regionData() {
        return this.regionData;
    }

    @Environment(value=EnvType.CLIENT)
    public record RegionData(@SerializedName(value="regionName") @JsonAdapter(value=RealmsRegion.RegionTypeAdapter.class) @Nullable RealmsRegion region, @SerializedName(value="serviceQuality") @JsonAdapter(value=ServiceQuality.ServiceQualityTypeAdapter.class) @Nullable ServiceQuality serviceQuality) implements RealmsSerializable
    {
        @SerializedName(value="regionName")
        public @Nullable RealmsRegion region() {
            return this.region;
        }

        @SerializedName(value="serviceQuality")
        public @Nullable ServiceQuality serviceQuality() {
            return this.serviceQuality;
        }
    }
}

