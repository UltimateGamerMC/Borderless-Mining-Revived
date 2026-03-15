package net.minecraft.client.realms.dto;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.realms.RealmsSerializable;
import net.minecraft.client.realms.dto.RealmsDescriptionDto;
import net.minecraft.client.realms.dto.RealmsOptionsDto;
import net.minecraft.client.realms.dto.RealmsRegionSelectionPreference;
import net.minecraft.client.realms.dto.RealmsSettingDto;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public record RealmsConfigurationDto(@SerializedName(value="options") RealmsOptionsDto options, @SerializedName(value="settings") List<RealmsSettingDto> settings, @SerializedName(value="regionSelectionPreference") @Nullable RealmsRegionSelectionPreference regionSelectionPreference, @SerializedName(value="description") @Nullable RealmsDescriptionDto description) implements RealmsSerializable
{
    @SerializedName(value="options")
    public RealmsOptionsDto options() {
        return this.options;
    }

    @SerializedName(value="settings")
    public List<RealmsSettingDto> settings() {
        return this.settings;
    }

    @SerializedName(value="regionSelectionPreference")
    public @Nullable RealmsRegionSelectionPreference regionSelectionPreference() {
        return this.regionSelectionPreference;
    }

    @SerializedName(value="description")
    public @Nullable RealmsDescriptionDto description() {
        return this.description;
    }
}

