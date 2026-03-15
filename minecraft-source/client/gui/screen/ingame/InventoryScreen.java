/*
 * External method calls:
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIIZ)V
 *   Lnet/minecraft/client/gui/screen/ingame/StatusEffectsDisplay;render(Lnet/minecraft/client/gui/DrawContext;II)V
 *   Lnet/minecraft/client/gui/screen/ingame/RecipeBookScreen;render(Lnet/minecraft/client/gui/DrawContext;IIF)V
 *   Lnet/minecraft/client/gui/DrawContext;drawTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIFFIIII)V
 *   Lnet/minecraft/client/gui/DrawContext;addEntity(Lnet/minecraft/client/render/entity/state/EntityRenderState;FLorg/joml/Vector3f;Lorg/joml/Quaternionf;Lorg/joml/Quaternionf;IIII)V
 *   Lnet/minecraft/client/gui/screen/ingame/RecipeBookScreen;mouseReleased(Lnet/minecraft/client/gui/Click;)Z
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;drawEntity(Lnet/minecraft/client/gui/DrawContext;IIIIIFFFLnet/minecraft/entity/LivingEntity;)V
 *   Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;drawEntity(Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/client/render/entity/state/EntityRenderState;
 */
package net.minecraft.client.gui.screen.ingame;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenPos;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.RecipeBookScreen;
import net.minecraft.client.gui.screen.ingame.StatusEffectsDisplay;
import net.minecraft.client.gui.screen.recipebook.CraftingRecipeBookWidget;
import net.minecraft.client.render.entity.EntityRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@Environment(value=EnvType.CLIENT)
public class InventoryScreen
extends RecipeBookScreen<PlayerScreenHandler> {
    private float mouseX;
    private float mouseY;
    private boolean mouseDown;
    private final StatusEffectsDisplay statusEffectsDisplay;

    public InventoryScreen(PlayerEntity player) {
        super(player.playerScreenHandler, new CraftingRecipeBookWidget(player.playerScreenHandler), player.getInventory(), Text.translatable("container.crafting"));
        this.titleX = 97;
        this.statusEffectsDisplay = new StatusEffectsDisplay(this);
    }

    @Override
    public void handledScreenTick() {
        super.handledScreenTick();
        if (this.client.player.isInCreativeMode()) {
            this.client.setScreen(new CreativeInventoryScreen(this.client.player, this.client.player.networkHandler.getEnabledFeatures(), this.client.options.getOperatorItemsTab().getValue()));
        }
    }

    @Override
    protected void init() {
        if (this.client.player.isInCreativeMode()) {
            this.client.setScreen(new CreativeInventoryScreen(this.client.player, this.client.player.networkHandler.getEnabledFeatures(), this.client.options.getOperatorItemsTab().getValue()));
            return;
        }
        super.init();
    }

    @Override
    protected ScreenPos getRecipeBookButtonPos() {
        return new ScreenPos(this.x + 104, this.height / 2 - 22);
    }

    @Override
    protected void onRecipeBookToggled() {
        this.mouseDown = true;
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        context.drawText(this.textRenderer, this.title, this.titleX, this.titleY, Colors.DARK_GRAY, false);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        this.statusEffectsDisplay.render(context, mouseX, mouseY);
        super.render(context, mouseX, mouseY, deltaTicks);
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    @Override
    public boolean showsStatusEffects() {
        return this.statusEffectsDisplay.shouldHideStatusEffectHud();
    }

    @Override
    protected boolean shouldAddPaddingToGhostResult() {
        return false;
    }

    @Override
    protected void drawBackground(DrawContext context, float deltaTicks, int mouseX, int mouseY) {
        int k = this.x;
        int l = this.y;
        context.drawTexture(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, k, l, 0.0f, 0.0f, this.backgroundWidth, this.backgroundHeight, 256, 256);
        InventoryScreen.drawEntity(context, k + 26, l + 8, k + 75, l + 78, 30, 0.0625f, this.mouseX, this.mouseY, this.client.player);
    }

    public static void drawEntity(DrawContext context, int x1, int y1, int x2, int y2, int size, float scale, float mouseX, float mouseY, LivingEntity entity) {
        float n = (float)(x1 + x2) / 2.0f;
        float o = (float)(y1 + y2) / 2.0f;
        float p = (float)Math.atan((n - mouseX) / 40.0f);
        float q = (float)Math.atan((o - mouseY) / 40.0f);
        Quaternionf quaternionf = new Quaternionf().rotateZ((float)Math.PI);
        Quaternionf quaternionf2 = new Quaternionf().rotateX(q * 20.0f * ((float)Math.PI / 180));
        quaternionf.mul(quaternionf2);
        EntityRenderState lv = InventoryScreen.drawEntity(entity);
        if (lv instanceof LivingEntityRenderState) {
            LivingEntityRenderState lv2 = (LivingEntityRenderState)lv;
            lv2.bodyYaw = 180.0f + p * 20.0f;
            lv2.relativeHeadYaw = p * 20.0f;
            lv2.pitch = lv2.pose != EntityPose.GLIDING ? -q * 20.0f : 0.0f;
            lv2.width /= lv2.baseScale;
            lv2.height /= lv2.baseScale;
            lv2.baseScale = 1.0f;
        }
        Vector3f vector3f = new Vector3f(0.0f, lv.height / 2.0f + scale, 0.0f);
        context.addEntity(lv, size, vector3f, quaternionf, quaternionf2, x1, y1, x2, y2);
    }

    private static EntityRenderState drawEntity(LivingEntity entity) {
        EntityRenderManager lv = MinecraftClient.getInstance().getEntityRenderDispatcher();
        EntityRenderer<?, LivingEntity> lv2 = lv.getRenderer(entity);
        LivingEntity lv3 = lv2.getAndUpdateRenderState(entity, 1.0f);
        ((EntityRenderState)((Object)lv3)).light = 0xF000F0;
        ((EntityRenderState)((Object)lv3)).shadowPieces.clear();
        ((EntityRenderState)((Object)lv3)).outlineColor = 0;
        return lv3;
    }

    @Override
    public boolean mouseReleased(Click click) {
        if (this.mouseDown) {
            this.mouseDown = false;
            return true;
        }
        return super.mouseReleased(click);
    }
}

