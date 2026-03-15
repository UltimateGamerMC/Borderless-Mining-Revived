/*
 * External method calls:
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Text;literal(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/gui/widget/CyclingButtonWidget;onOffBuilder(Lnet/minecraft/text/Text;Lnet/minecraft/text/Text;Z)Lnet/minecraft/client/gui/widget/CyclingButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/CyclingButtonWidget$Builder;omitKeyText()Lnet/minecraft/client/gui/widget/CyclingButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/CyclingButtonWidget$Builder;build(IIIILnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/CyclingButtonWidget$UpdateCallback;)Lnet/minecraft/client/gui/widget/CyclingButtonWidget;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget;builder(Lnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;dimensions(IIII)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;build()Lnet/minecraft/client/gui/widget/ButtonWidget;
 *   Lnet/minecraft/client/gui/screen/ChatInputSuggestor;keyPressed(Lnet/minecraft/client/input/KeyInput;)Z
 *   Lnet/minecraft/client/gui/screen/Screen;keyPressed(Lnet/minecraft/client/input/KeyInput;)Z
 *   Lnet/minecraft/client/gui/screen/ChatInputSuggestor;mouseScrolled(D)Z
 *   Lnet/minecraft/client/gui/screen/Screen;mouseScrolled(DDDD)Z
 *   Lnet/minecraft/client/gui/screen/ChatInputSuggestor;mouseClicked(Lnet/minecraft/client/gui/Click;)Z
 *   Lnet/minecraft/client/gui/screen/Screen;mouseClicked(Lnet/minecraft/client/gui/Click;Z)Z
 *   Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/gui/DrawContext;IIF)V
 *   Lnet/minecraft/client/gui/DrawContext;drawCenteredTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V
 *   Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V
 *   Lnet/minecraft/client/gui/widget/TextFieldWidget;render(Lnet/minecraft/client/gui/DrawContext;IIF)V
 *   Lnet/minecraft/client/gui/screen/ChatInputSuggestor;render(Lnet/minecraft/client/gui/DrawContext;II)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/screen/ingame/AbstractCommandBlockScreen;addSelectableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;
 *   Lnet/minecraft/client/gui/screen/ingame/AbstractCommandBlockScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;
 *   Lnet/minecraft/client/gui/screen/ingame/AbstractCommandBlockScreen;init(II)V
 */
package net.minecraft.client.gui.screen.ingame;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.world.CommandBlockExecutor;

@Environment(value=EnvType.CLIENT)
public abstract class AbstractCommandBlockScreen
extends Screen {
    private static final Text SET_COMMAND_TEXT = Text.translatable("advMode.setCommand");
    private static final Text COMMAND_TEXT = Text.translatable("advMode.command");
    private static final Text PREVIOUS_OUTPUT_TEXT = Text.translatable("advMode.previousOutput");
    protected TextFieldWidget consoleCommandTextField;
    protected TextFieldWidget previousOutputTextField;
    protected ButtonWidget doneButton;
    protected ButtonWidget cancelButton;
    protected CyclingButtonWidget<Boolean> toggleTrackingOutputButton;
    ChatInputSuggestor commandSuggestor;

    public AbstractCommandBlockScreen() {
        super(NarratorManager.EMPTY);
    }

    @Override
    public void tick() {
        if (!this.getCommandExecutor().isEditable()) {
            this.close();
        }
    }

    abstract CommandBlockExecutor getCommandExecutor();

    abstract int getTrackOutputButtonHeight();

    @Override
    protected void init() {
        boolean bl = this.getCommandExecutor().isTrackingOutput();
        this.consoleCommandTextField = new TextFieldWidget(this.textRenderer, this.width / 2 - 150, 50, 300, 20, (Text)Text.translatable("advMode.command")){

            @Override
            protected MutableText getNarrationMessage() {
                return super.getNarrationMessage().append(AbstractCommandBlockScreen.this.commandSuggestor.getNarration());
            }
        };
        this.consoleCommandTextField.setMaxLength(32500);
        this.consoleCommandTextField.setChangedListener(this::onCommandChanged);
        this.addSelectableChild(this.consoleCommandTextField);
        this.previousOutputTextField = new TextFieldWidget(this.textRenderer, this.width / 2 - 150, this.getTrackOutputButtonHeight(), 276, 20, Text.translatable("advMode.previousOutput"));
        this.previousOutputTextField.setMaxLength(32500);
        this.previousOutputTextField.setEditable(false);
        this.previousOutputTextField.setText("-");
        this.addSelectableChild(this.previousOutputTextField);
        this.toggleTrackingOutputButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(Text.literal("O"), Text.literal("X"), bl).omitKeyText().build(this.width / 2 + 150 - 20, this.getTrackOutputButtonHeight(), 20, 20, Text.translatable("advMode.trackOutput"), (button, trackOutput) -> {
            CommandBlockExecutor lv = this.getCommandExecutor();
            lv.setTrackOutput((boolean)trackOutput);
            this.setPreviousOutputText((boolean)trackOutput);
        }));
        this.addAdditionalButtons();
        this.doneButton = this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.commitAndClose()).dimensions(this.width / 2 - 4 - 150, this.height / 4 + 120 + 12, 150, 20).build());
        this.cancelButton = this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.close()).dimensions(this.width / 2 + 4, this.height / 4 + 120 + 12, 150, 20).build());
        this.commandSuggestor = new ChatInputSuggestor(this.client, this, this.consoleCommandTextField, this.textRenderer, true, true, 0, 7, false, Integer.MIN_VALUE);
        this.commandSuggestor.setWindowActive(true);
        this.commandSuggestor.refresh();
        this.setPreviousOutputText(bl);
    }

    protected void addAdditionalButtons() {
    }

    @Override
    protected void setInitialFocus() {
        this.setInitialFocus(this.consoleCommandTextField);
    }

    @Override
    protected Text getUsageNarrationText() {
        if (this.commandSuggestor.isOpen()) {
            return this.commandSuggestor.getSuggestionUsageNarrationText();
        }
        return super.getUsageNarrationText();
    }

    @Override
    public void resize(int width, int height) {
        String string = this.consoleCommandTextField.getText();
        this.init(width, height);
        this.consoleCommandTextField.setText(string);
        this.commandSuggestor.refresh();
    }

    protected void setPreviousOutputText(boolean trackOutput) {
        this.previousOutputTextField.setText(trackOutput ? this.getCommandExecutor().getLastOutput().getString() : "-");
    }

    protected void commitAndClose() {
        this.syncSettingsToServer();
        CommandBlockExecutor lv = this.getCommandExecutor();
        if (!lv.isTrackingOutput()) {
            lv.setLastOutput(null);
        }
        this.client.setScreen(null);
    }

    protected abstract void syncSettingsToServer();

    private void onCommandChanged(String text) {
        this.commandSuggestor.refresh();
    }

    @Override
    public boolean deferSubtitles() {
        return true;
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if (this.commandSuggestor.keyPressed(input)) {
            return true;
        }
        if (super.keyPressed(input)) {
            return true;
        }
        if (input.isEnter()) {
            this.commitAndClose();
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.commandSuggestor.mouseScrolled(verticalAmount)) {
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (this.commandSuggestor.mouseClicked(click)) {
            return true;
        }
        return super.mouseClicked(click, doubled);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        context.drawCenteredTextWithShadow(this.textRenderer, SET_COMMAND_TEXT, this.width / 2, 20, Colors.WHITE);
        context.drawTextWithShadow(this.textRenderer, COMMAND_TEXT, this.width / 2 - 150 + 1, 40, Colors.LIGHT_GRAY);
        this.consoleCommandTextField.render(context, mouseX, mouseY, deltaTicks);
        int k = 75;
        if (!this.previousOutputTextField.getText().isEmpty()) {
            context.drawTextWithShadow(this.textRenderer, PREVIOUS_OUTPUT_TEXT, this.width / 2 - 150 + 1, (k += 5 * this.textRenderer.fontHeight + 1 + this.getTrackOutputButtonHeight() - 135) + 4, Colors.LIGHT_GRAY);
            this.previousOutputTextField.render(context, mouseX, mouseY, deltaTicks);
        }
        this.commandSuggestor.render(context, mouseX, mouseY);
    }
}

