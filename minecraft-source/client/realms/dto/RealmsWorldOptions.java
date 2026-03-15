/*
 * External method calls:
 *   Lnet/minecraft/client/realms/CheckedGson;fromJson(Ljava/lang/String;Ljava/lang/Class;)Lnet/minecraft/client/realms/RealmsSerializable;
 *   Lnet/minecraft/client/resource/language/I18n;translate(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/realms/dto/RealmsWorldOptions;create(Lnet/minecraft/world/GameMode;Lnet/minecraft/world/Difficulty;ZLjava/lang/String;Ljava/lang/String;)Lnet/minecraft/client/realms/dto/RealmsWorldOptions;
 *   Lnet/minecraft/client/realms/dto/RealmsWorldOptions;replaceNullsWithDefaults(Lnet/minecraft/client/realms/dto/RealmsWorldOptions;)V
 */
package net.minecraft.client.realms.dto;

import com.google.gson.annotations.SerializedName;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.realms.CheckedGson;
import net.minecraft.client.realms.RealmsSerializable;
import net.minecraft.client.realms.dto.RealmsServer;
import net.minecraft.client.realms.dto.ValueObject;
import net.minecraft.client.realms.util.DontSerialize;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.util.StringHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameMode;
import net.minecraft.world.level.LevelInfo;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class RealmsWorldOptions
extends ValueObject
implements RealmsSerializable {
    @SerializedName(value="spawnProtection")
    public int spawnProtection = 0;
    @SerializedName(value="forceGameMode")
    public boolean forceGameMode = false;
    @SerializedName(value="difficulty")
    public int difficulty = 2;
    @SerializedName(value="gameMode")
    public int gameMode = 0;
    @SerializedName(value="slotName")
    private String slotName = "";
    @SerializedName(value="version")
    public String version = "";
    @SerializedName(value="compatibility")
    public RealmsServer.Compatibility compatibility = RealmsServer.Compatibility.UNVERIFIABLE;
    @SerializedName(value="worldTemplateId")
    public long templateId = -1L;
    @SerializedName(value="worldTemplateImage")
    public @Nullable String templateImage = null;
    @DontSerialize
    public boolean empty;

    private RealmsWorldOptions() {
    }

    public RealmsWorldOptions(int spawnProtection, int difficulty, int gameMode, boolean forceGameMode, String slotName, String version, RealmsServer.Compatibility compatibility) {
        this.spawnProtection = spawnProtection;
        this.difficulty = difficulty;
        this.gameMode = gameMode;
        this.forceGameMode = forceGameMode;
        this.slotName = slotName;
        this.version = version;
        this.compatibility = compatibility;
    }

    public static RealmsWorldOptions getDefaults() {
        return new RealmsWorldOptions();
    }

    public static RealmsWorldOptions create(GameMode gameMode, Difficulty difficulty, boolean hardcore, String version, String slotName) {
        RealmsWorldOptions lv = RealmsWorldOptions.getDefaults();
        lv.difficulty = difficulty.getId();
        lv.gameMode = gameMode.getIndex();
        lv.slotName = slotName;
        lv.version = version;
        return lv;
    }

    public static RealmsWorldOptions create(LevelInfo levelInfo, String slotName) {
        return RealmsWorldOptions.create(levelInfo.getGameMode(), levelInfo.getDifficulty(), levelInfo.isHardcore(), slotName, levelInfo.getLevelName());
    }

    public static RealmsWorldOptions getEmptyDefaults() {
        RealmsWorldOptions lv = RealmsWorldOptions.getDefaults();
        lv.setEmpty(true);
        return lv;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public static RealmsWorldOptions fromJson(CheckedGson gson, String json) {
        RealmsWorldOptions lv = gson.fromJson(json, RealmsWorldOptions.class);
        if (lv == null) {
            return RealmsWorldOptions.getDefaults();
        }
        RealmsWorldOptions.replaceNullsWithDefaults(lv);
        return lv;
    }

    private static void replaceNullsWithDefaults(RealmsWorldOptions options) {
        if (options.slotName == null) {
            options.slotName = "";
        }
        if (options.version == null) {
            options.version = "";
        }
        if (options.compatibility == null) {
            options.compatibility = RealmsServer.Compatibility.UNVERIFIABLE;
        }
    }

    public String getSlotName(int index) {
        if (StringHelper.isBlank(this.slotName)) {
            if (this.empty) {
                return I18n.translate("mco.configure.world.slot.empty", new Object[0]);
            }
            return this.getDefaultSlotName(index);
        }
        return this.slotName;
    }

    public String getDefaultSlotName(int index) {
        return I18n.translate("mco.configure.world.slot", index);
    }

    public RealmsWorldOptions copy() {
        return new RealmsWorldOptions(this.spawnProtection, this.difficulty, this.gameMode, this.forceGameMode, this.slotName, this.version, this.compatibility);
    }
}

