/*
 * External method calls:
 *   Lnet/minecraft/entity/player/SkinTextures;model()Lnet/minecraft/entity/player/PlayerSkinType;
 *   Lnet/minecraft/entity/player/SkinTextures;body()Lnet/minecraft/util/AssetInfo$TextureAsset;
 *   Lnet/minecraft/util/AssetInfo$TextureAsset;texturePath()Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/client/gui/DrawContext;addPlayerSkin(Lnet/minecraft/client/render/entity/model/PlayerEntityModel;Lnet/minecraft/util/Identifier;FFFFIIII)V
 */
package net.minecraft.client.gui.widget;

import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.navigation.GuiNavigation;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.entity.player.PlayerSkinType;
import net.minecraft.entity.player.SkinTextures;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.util.math.MathHelper;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class PlayerSkinWidget
extends ClickableWidget {
    private static final float field_45997 = 2.125f;
    private static final float field_59833 = 0.97f;
    private static final float field_45999 = 2.5f;
    private static final float field_46000 = -5.0f;
    private static final float field_46001 = 30.0f;
    private static final float field_46002 = 50.0f;
    private final PlayerEntityModel wideModel;
    private final PlayerEntityModel slimModel;
    private final Supplier<SkinTextures> skinSupplier;
    private float xRotation = -5.0f;
    private float yRotation = 30.0f;

    public PlayerSkinWidget(int width, int height, LoadedEntityModels entityModels, Supplier<SkinTextures> skinSupplier) {
        super(0, 0, width, height, ScreenTexts.EMPTY);
        this.wideModel = new PlayerEntityModel(entityModels.getModelPart(EntityModelLayers.PLAYER), false);
        this.slimModel = new PlayerEntityModel(entityModels.getModelPart(EntityModelLayers.PLAYER_SLIM), true);
        this.skinSupplier = skinSupplier;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        float g = 0.97f * (float)this.getHeight() / 2.125f;
        float h = -1.0625f;
        SkinTextures lv = this.skinSupplier.get();
        PlayerEntityModel lv2 = lv.model() == PlayerSkinType.SLIM ? this.slimModel : this.wideModel;
        context.addPlayerSkin(lv2, lv.body().texturePath(), g, this.xRotation, this.yRotation, -1.0625f, this.getX(), this.getY(), this.getRight(), this.getBottom());
    }

    @Override
    protected void onDrag(Click click, double offsetX, double offsetY) {
        this.xRotation = MathHelper.clamp(this.xRotation - (float)offsetY * 2.5f, -50.0f, 50.0f);
        this.yRotation += (float)offsetX * 2.5f;
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
    }

    @Override
    public @Nullable GuiNavigationPath getNavigationPath(GuiNavigation navigation) {
        return null;
    }
}

