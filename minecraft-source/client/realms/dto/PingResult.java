package net.minecraft.client.realms.dto;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.realms.RealmsSerializable;
import net.minecraft.client.realms.dto.RegionPingResult;

@Environment(value=EnvType.CLIENT)
public record PingResult(@SerializedName(value="pingResults") List<RegionPingResult> pingResults, @SerializedName(value="worldIds") List<Long> worldIds) implements RealmsSerializable
{
    @SerializedName(value="pingResults")
    public List<RegionPingResult> pingResults() {
        return this.pingResults;
    }

    @SerializedName(value="worldIds")
    public List<Long> worldIds() {
        return this.worldIds;
    }
}

