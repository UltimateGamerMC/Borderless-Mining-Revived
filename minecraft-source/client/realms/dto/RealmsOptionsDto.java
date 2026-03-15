package net.minecraft.client.realms.dto;

import com.google.gson.annotations.SerializedName;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.realms.RealmsSerializable;
import net.minecraft.client.realms.dto.RealmsServer;
import net.minecraft.client.realms.dto.RealmsWorldOptions;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public record RealmsOptionsDto(@SerializedName(value="slotId") int slotId, @SerializedName(value="spawnProtection") int spawnProtection, @SerializedName(value="forceGameMode") boolean forceGameMode, @SerializedName(value="difficulty") int difficulty, @SerializedName(value="gameMode") int gameMode, @SerializedName(value="slotName") String slotName, @SerializedName(value="version") String version, @SerializedName(value="compatibility") RealmsServer.Compatibility compatibility, @SerializedName(value="worldTemplateId") long worldTemplateId, @SerializedName(value="worldTemplateImage") @Nullable String worldTemplateImage, @SerializedName(value="hardcore") boolean hardcore) implements RealmsSerializable
{
    public RealmsOptionsDto(int slotId, RealmsWorldOptions options, boolean hardcore) {
        this(slotId, options.spawnProtection, options.forceGameMode, options.difficulty, options.gameMode, options.getSlotName(slotId), options.version, options.compatibility, options.templateId, options.templateImage, hardcore);
    }

    @SerializedName(value="slotId")
    public int slotId() {
        return this.slotId;
    }

    @SerializedName(value="spawnProtection")
    public int spawnProtection() {
        return this.spawnProtection;
    }

    @SerializedName(value="forceGameMode")
    public boolean forceGameMode() {
        return this.forceGameMode;
    }

    @SerializedName(value="difficulty")
    public int difficulty() {
        return this.difficulty;
    }

    @SerializedName(value="gameMode")
    public int gameMode() {
        return this.gameMode;
    }

    @SerializedName(value="slotName")
    public String slotName() {
        return this.slotName;
    }

    @SerializedName(value="version")
    public String version() {
        return this.version;
    }

    @SerializedName(value="compatibility")
    public RealmsServer.Compatibility compatibility() {
        return this.compatibility;
    }

    @SerializedName(value="worldTemplateId")
    public long worldTemplateId() {
        return this.worldTemplateId;
    }

    @SerializedName(value="worldTemplateImage")
    public @Nullable String worldTemplateImage() {
        return this.worldTemplateImage;
    }

    @SerializedName(value="hardcore")
    public boolean hardcore() {
        return this.hardcore;
    }
}

