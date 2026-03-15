package net.minecraft.client.realms.dto;

import com.google.gson.annotations.SerializedName;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.realms.RealmsSerializable;

@Environment(value=EnvType.CLIENT)
public record RealmsWorldResetDto(@SerializedName(value="seed") String seed, @SerializedName(value="worldTemplateId") long worldTemplateId, @SerializedName(value="levelType") int levelType, @SerializedName(value="generateStructures") boolean generateStructures, @SerializedName(value="experiments") Set<String> experiments) implements RealmsSerializable
{
    @SerializedName(value="seed")
    public String seed() {
        return this.seed;
    }

    @SerializedName(value="worldTemplateId")
    public long worldTemplateId() {
        return this.worldTemplateId;
    }

    @SerializedName(value="levelType")
    public int levelType() {
        return this.levelType;
    }

    @SerializedName(value="generateStructures")
    public boolean generateStructures() {
        return this.generateStructures;
    }

    @SerializedName(value="experiments")
    public Set<String> experiments() {
        return this.experiments;
    }
}

