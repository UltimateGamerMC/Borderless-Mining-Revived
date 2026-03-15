/*
 * External method calls:
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/gui/widget/CyclingButtonWidget;builder(Ljava/util/function/Function;Ljava/lang/Object;)Lnet/minecraft/client/gui/widget/CyclingButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/CyclingButtonWidget$Builder;values(Ljava/util/Collection;)Lnet/minecraft/client/gui/widget/CyclingButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/CyclingButtonWidget$Builder;omitKeyText()Lnet/minecraft/client/gui/widget/CyclingButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/CyclingButtonWidget$Builder;build(IIIILnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/CyclingButtonWidget$UpdateCallback;)Lnet/minecraft/client/gui/widget/CyclingButtonWidget;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget;builder(Lnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;dimensions(IIII)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;build()Lnet/minecraft/client/gui/widget/ButtonWidget;
 *   Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/gui/DrawContext;IIF)V
 *   Lnet/minecraft/client/gui/DrawContext;drawCenteredTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V
 *   Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V
 *   Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V
 *   Lnet/minecraft/block/enums/TestBlockMode;values()[Lnet/minecraft/block/enums/TestBlockMode;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/screen/ingame/TestBlockScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;
 */
package net.minecraft.client.gui.screen.ingame;

import java.util.Collection;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.TestBlockEntity;
import net.minecraft.block.enums.TestBlockMode;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.network.packet.c2s.play.SetTestBlockC2SPacket;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.math.BlockPos;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class TestBlockScreen
extends Screen {
    private static final List<TestBlockMode> MODES = List.of(TestBlockMode.values());
    private static final Text TITLE_TEXT = Text.translatable(Blocks.TEST_BLOCK.getTranslationKey());
    private static final Text MESSAGE_TEXT = Text.translatable("test_block.message");
    private final BlockPos pos;
    private TestBlockMode mode;
    private String message;
    private @Nullable TextFieldWidget textField;

    public TestBlockScreen(TestBlockEntity blockEntity) {
        super(TITLE_TEXT);
        this.pos = blockEntity.getPos();
        this.mode = blockEntity.getMode();
        this.message = blockEntity.getMessage();
    }

    @Override
    public void init() {
        this.textField = new TextFieldWidget(this.textRenderer, this.width / 2 - 152, 80, 240, 20, Text.translatable("test_block.message"));
        this.textField.setMaxLength(128);
        this.textField.setText(this.message);
        this.addDrawableChild(this.textField);
        this.setMode(this.mode);
        this.addDrawableChild(CyclingButtonWidget.builder(TestBlockMode::getName, this.mode).values((Collection<TestBlockMode>)MODES).omitKeyText().build(this.width / 2 - 4 - 150, 185, 50, 20, TITLE_TEXT, (button, mode) -> this.setMode((TestBlockMode)mode)));
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.onDone()).dimensions(this.width / 2 - 4 - 150, 210, 150, 20).build());
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.onCancel()).dimensions(this.width / 2 + 4, 210, 150, 20).build());
    }

    @Override
    protected void setInitialFocus() {
        if (this.textField != null) {
            this.setInitialFocus(this.textField);
        } else {
            super.setInitialFocus();
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 10, Colors.WHITE);
        if (this.mode != TestBlockMode.START) {
            context.drawTextWithShadow(this.textRenderer, MESSAGE_TEXT, this.width / 2 - 153, 70, Colors.LIGHT_GRAY);
        }
        context.drawTextWithShadow(this.textRenderer, this.mode.getInfo(), this.width / 2 - 153, 174, Colors.LIGHT_GRAY);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean deferSubtitles() {
        return true;
    }

    private void onDone() {
        this.message = this.textField.getText();
        this.client.getNetworkHandler().sendPacket(new SetTestBlockC2SPacket(this.pos, this.mode, this.message));
        this.close();
    }

    @Override
    public void close() {
        this.onCancel();
    }

    private void onCancel() {
        this.client.setScreen(null);
    }

    private void setMode(TestBlockMode mode) {
        this.mode = mode;
        this.textField.visible = mode != TestBlockMode.START;
    }
}

