/*
 * External method calls:
 *   Lnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;addBody(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget;builder(Lnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;build()Lnet/minecraft/client/gui/widget/ButtonWidget;
 *   Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;horizontal()Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;
 *   Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;spacing(I)Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;
 *   Lnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;addFooter(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;
 *   Lnet/minecraft/client/gui/screen/option/ControlsListWidget;position(ILnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;)V
 *   Lnet/minecraft/client/util/InputUtil$Type;createFromCode(I)Lnet/minecraft/client/util/InputUtil$Key;
 *   Lnet/minecraft/client/gui/screen/option/GameOptionsScreen;mouseClicked(Lnet/minecraft/client/gui/Click;Z)Z
 *   Lnet/minecraft/client/util/InputUtil;fromKeyCode(Lnet/minecraft/client/input/KeyInput;)Lnet/minecraft/client/util/InputUtil$Key;
 *   Lnet/minecraft/client/gui/screen/option/GameOptionsScreen;keyPressed(Lnet/minecraft/client/input/KeyInput;)Z
 *   Lnet/minecraft/client/gui/screen/option/GameOptionsScreen;render(Lnet/minecraft/client/gui/DrawContext;IIF)V
 */
package net.minecraft.client.gui.screen.option;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class KeybindsScreen
extends GameOptionsScreen {
    private static final Text TITLE_TEXT = Text.translatable("controls.keybinds.title");
    public @Nullable KeyBinding selectedKeyBinding;
    public long lastKeyCodeUpdateTime;
    private ControlsListWidget controlsList;
    private ButtonWidget resetAllButton;

    public KeybindsScreen(Screen parent, GameOptions gameOptions) {
        super(parent, gameOptions, TITLE_TEXT);
    }

    @Override
    protected void initBody() {
        this.controlsList = this.layout.addBody(new ControlsListWidget(this, this.client));
    }

    @Override
    protected void addOptions() {
    }

    @Override
    protected void initFooter() {
        this.resetAllButton = ButtonWidget.builder(Text.translatable("controls.resetAll"), button -> {
            for (KeyBinding lv : this.gameOptions.allKeys) {
                lv.setBoundKey(lv.getDefaultKey());
            }
            this.controlsList.update();
        }).build();
        DirectionalLayoutWidget lv = this.layout.addFooter(DirectionalLayoutWidget.horizontal().spacing(8));
        lv.add(this.resetAllButton);
        lv.add(ButtonWidget.builder(ScreenTexts.DONE, button -> this.close()).build());
    }

    @Override
    protected void refreshWidgetPositions() {
        this.layout.refreshPositions();
        this.controlsList.position(this.width, this.layout);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (this.selectedKeyBinding != null) {
            this.selectedKeyBinding.setBoundKey(InputUtil.Type.MOUSE.createFromCode(click.button()));
            this.selectedKeyBinding = null;
            this.controlsList.update();
            return true;
        }
        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if (this.selectedKeyBinding != null) {
            if (input.isEscape()) {
                this.selectedKeyBinding.setBoundKey(InputUtil.UNKNOWN_KEY);
            } else {
                this.selectedKeyBinding.setBoundKey(InputUtil.fromKeyCode(input));
            }
            this.selectedKeyBinding = null;
            this.lastKeyCodeUpdateTime = Util.getMeasuringTimeMs();
            this.controlsList.update();
            return true;
        }
        return super.keyPressed(input);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        boolean bl = false;
        for (KeyBinding lv : this.gameOptions.allKeys) {
            if (lv.isDefault()) continue;
            bl = true;
            break;
        }
        this.resetAllButton.active = bl;
    }
}

