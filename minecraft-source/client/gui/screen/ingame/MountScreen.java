/*
 * External method calls:
 *   Lnet/minecraft/client/gui/DrawContext;drawTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIFFIIII)V
 *   Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIIIIIII)V
 *   Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;drawEntity(Lnet/minecraft/client/gui/DrawContext;IIIIIFFFLnet/minecraft/entity/LivingEntity;)V
 *   Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIII)V
 *   Lnet/minecraft/client/gui/screen/ingame/HandledScreen;render(Lnet/minecraft/client/gui/DrawContext;IIF)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/screen/ingame/MountScreen;drawSlot(Lnet/minecraft/client/gui/DrawContext;II)V
 *   Lnet/minecraft/client/gui/screen/ingame/MountScreen;drawMouseoverTooltip(Lnet/minecraft/client/gui/DrawContext;II)V
 */
package net.minecraft.client.gui.screen.ingame;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.MountScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public abstract class MountScreen<T extends MountScreenHandler>
extends HandledScreen<T> {
    protected final int slotColumnCount;
    protected float mouseX;
    protected float mouseY;
    protected LivingEntity mount;

    public MountScreen(T handler, PlayerInventory inventory, Text title, int slotColumnCount, LivingEntity mount) {
        super(handler, inventory, title);
        this.slotColumnCount = slotColumnCount;
        this.mount = mount;
    }

    @Override
    protected void drawBackground(DrawContext context, float deltaTicks, int mouseX, int mouseY) {
        int k = (this.width - this.backgroundWidth) / 2;
        int l = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(RenderPipelines.GUI_TEXTURED, this.getTexture(), k, l, 0.0f, 0.0f, this.backgroundWidth, this.backgroundHeight, 256, 256);
        if (this.slotColumnCount > 0 && this.getChestSlotsTexture() != null) {
            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, this.getChestSlotsTexture(), 90, 54, 0, 0, k + 79, l + 17, this.slotColumnCount * 18, 54);
        }
        if (this.canEquipSaddle()) {
            this.drawSlot(context, k + 7, l + 35 - 18);
        }
        if (this.canEquipArmor()) {
            this.drawSlot(context, k + 7, l + 35);
        }
        InventoryScreen.drawEntity(context, k + 26, l + 18, k + 78, l + 70, 17, 0.25f, this.mouseX, this.mouseY, this.mount);
    }

    protected void drawSlot(DrawContext context, int x, int y) {
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, this.getSlotTexture(), x, y, 18, 18);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        super.render(context, mouseX, mouseY, deltaTicks);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    protected abstract Identifier getTexture();

    protected abstract Identifier getSlotTexture();

    protected abstract @Nullable Identifier getChestSlotsTexture();

    protected abstract boolean canEquipSaddle();

    protected abstract boolean canEquipArmor();
}

