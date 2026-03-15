/*
 * External method calls:
 *   Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIII)V
 *   Lnet/minecraft/client/sound/PositionedSoundInstance;ui(Lnet/minecraft/sound/SoundEvent;F)Lnet/minecraft/client/sound/PositionedSoundInstance;
 *   Lnet/minecraft/client/sound/SoundManager;play(Lnet/minecraft/client/sound/SoundInstance;)Lnet/minecraft/client/sound/SoundSystem$PlayResult;
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 */
package net.minecraft.client.gui.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class PageTurnWidget
extends ButtonWidget {
    private static final Identifier PAGE_FORWARD_HIGHLIGHTED_TEXTURE = Identifier.ofVanilla("widget/page_forward_highlighted");
    private static final Identifier PAGE_FORWARD_TEXTURE = Identifier.ofVanilla("widget/page_forward");
    private static final Identifier PAGE_BACKWARD_HIGHLIGHTED_TEXTURE = Identifier.ofVanilla("widget/page_backward_highlighted");
    private static final Identifier PAGE_BACKWARD_TEXTURE = Identifier.ofVanilla("widget/page_backward");
    private static final Text NEXT_TEXT = Text.translatable("book.page_button.next");
    private static final Text PREVIOUS_TEXT = Text.translatable("book.page_button.previous");
    private final boolean isNextPageButton;
    private final boolean playPageTurnSound;

    public PageTurnWidget(int x, int y, boolean isNextPageButton, ButtonWidget.PressAction action, boolean playPageTurnSound) {
        super(x, y, 23, 13, isNextPageButton ? NEXT_TEXT : PREVIOUS_TEXT, action, DEFAULT_NARRATION_SUPPLIER);
        this.isNextPageButton = isNextPageButton;
        this.playPageTurnSound = playPageTurnSound;
    }

    @Override
    public void drawIcon(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        Identifier lv = this.isNextPageButton ? (this.isSelected() ? PAGE_FORWARD_HIGHLIGHTED_TEXTURE : PAGE_FORWARD_TEXTURE) : (this.isSelected() ? PAGE_BACKWARD_HIGHLIGHTED_TEXTURE : PAGE_BACKWARD_TEXTURE);
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, lv, this.getX(), this.getY(), 23, 13);
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        if (this.playPageTurnSound) {
            soundManager.play(PositionedSoundInstance.ui(SoundEvents.ITEM_BOOK_PAGE_TURN, 1.0f));
        }
    }

    @Override
    public boolean isClickable() {
        return false;
    }
}

