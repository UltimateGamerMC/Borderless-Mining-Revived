package net.minecraft.client.realms.dto;

import com.google.gson.annotations.SerializedName;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.realms.RealmsSerializable;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public record RealmsDescriptionDto(@SerializedName(value="name") @Nullable String name, @SerializedName(value="description") String description) implements RealmsSerializable
{
    @SerializedName(value="name")
    public @Nullable String name() {
        return this.name;
    }

    @SerializedName(value="description")
    public String description() {
        return this.description;
    }
}

