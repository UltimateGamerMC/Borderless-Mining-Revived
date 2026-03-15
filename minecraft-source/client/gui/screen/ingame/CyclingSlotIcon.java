/*
 * External method calls:
 *   Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIIII)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/screen/ingame/CyclingSlotIcon;computeAlpha(F)F
 *   Lnet/minecraft/client/gui/screen/ingame/CyclingSlotIcon;drawIcon(Lnet/minecraft/screen/slot/Slot;Lnet/minecraft/util/Identifier;FLnet/minecraft/client/gui/DrawContext;II)V
 */
package net.minecraft.client.gui.screen.ingame;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

@Environment(value=EnvType.CLIENT)
public class CyclingSlotIcon {
    private static final int field_42039 = 30;
    private static final int field_42040 = 16;
    private static final int field_42041 = 4;
    private final int slotId;
    private List<Identifier> textures = List.of();
    private int timer;
    private int currentIndex;

    public CyclingSlotIcon(int slotId) {
        this.slotId = slotId;
    }

    public void updateTexture(List<Identifier> textures) {
        if (!this.textures.equals(textures)) {
            this.textures = textures;
            this.currentIndex = 0;
        }
        if (!this.textures.isEmpty() && ++this.timer % 30 == 0) {
            this.currentIndex = (this.currentIndex + 1) % this.textures.size();
        }
    }

    public void render(ScreenHandler screenHandler, DrawContext context, float deltaTicks, int x, int y) {
        float g;
        Slot lv = screenHandler.getSlot(this.slotId);
        if (this.textures.isEmpty() || lv.hasStack()) {
            return;
        }
        boolean bl = this.textures.size() > 1 && this.timer >= 30;
        float f = g = bl ? this.computeAlpha(deltaTicks) : 1.0f;
        if (g < 1.0f) {
            int k = Math.floorMod(this.currentIndex - 1, this.textures.size());
            this.drawIcon(lv, this.textures.get(k), 1.0f - g, context, x, y);
        }
        this.drawIcon(lv, this.textures.get(this.currentIndex), g, context, x, y);
    }

    private void drawIcon(Slot slot, Identifier texture, float alpha, DrawContext context, int x, int y) {
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, texture, x + slot.x, y + slot.y, 16, 16, ColorHelper.getWhite(alpha));
    }

    private float computeAlpha(float deltaTicks) {
        float g = (float)(this.timer % 30) + deltaTicks;
        return Math.min(g, 4.0f) / 4.0f;
    }
}

