package net.minecraft.client.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerLikeState;
import net.minecraft.entity.player.SkinTextures;
import net.minecraft.text.Text;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public interface ClientPlayerLikeEntity {
    public ClientPlayerLikeState getState();

    public SkinTextures getSkin();

    public @Nullable Text getMannequinName();

    public  @Nullable ParrotEntity.Variant getShoulderParrotVariant(boolean var1);

    public boolean hasExtraEars();
}

