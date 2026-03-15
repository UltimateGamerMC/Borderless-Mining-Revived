/*
 * External method calls:
 *   Lnet/minecraft/dialog/type/Dialog;common()Lnet/minecraft/dialog/DialogCommonData;
 *   Lnet/minecraft/dialog/DialogCommonData;title()Lnet/minecraft/text/Text;
 *   Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;vertical()Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;
 *   Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;spacing(I)Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;
 *   Lnet/minecraft/client/gui/widget/Positioner;alignHorizontalCenter()Lnet/minecraft/client/gui/widget/Positioner;
 *   Lnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;addHeader(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;
 *   Lnet/minecraft/dialog/DialogCommonData;body()Ljava/util/List;
 *   Lnet/minecraft/client/gui/screen/dialog/DialogBodyHandlers;createWidget(Lnet/minecraft/client/gui/screen/dialog/DialogScreen;Lnet/minecraft/dialog/body/DialogBody;)Lnet/minecraft/client/gui/widget/Widget;
 *   Lnet/minecraft/dialog/DialogCommonData;inputs()Ljava/util/List;
 *   Lnet/minecraft/client/gui/screen/dialog/DialogControls;addInput(Lnet/minecraft/dialog/type/DialogInput;Ljava/util/function/Consumer;)V
 *   Lnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;addBody(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;
 *   Lnet/minecraft/client/gui/screen/dialog/DialogControls;createClickEvent(Ljava/util/Optional;)Ljava/util/function/Supplier;
 *   Lnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;forEachChild(Ljava/util/function/Consumer;)V
 *   Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;horizontal()Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;
 *   Lnet/minecraft/client/gui/widget/Positioner;alignVerticalCenter()Lnet/minecraft/client/gui/widget/Positioner;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/gui/tooltip/Tooltip;of(Lnet/minecraft/text/Text;)Lnet/minecraft/client/gui/tooltip/Tooltip;
 *   Lnet/minecraft/dialog/DialogCommonData;afterAction()Lnet/minecraft/dialog/AfterAction;
 *   Lnet/minecraft/text/ClickEvent$RunCommand;command()Ljava/lang/String;
 *   Lnet/minecraft/server/command/CommandManager;stripLeadingSlash(Ljava/lang/String;)Ljava/lang/String;
 *   Lnet/minecraft/client/gui/screen/dialog/DialogNetworkAccess;runClickEventCommand(Ljava/lang/String;Lnet/minecraft/client/gui/screen/Screen;)V
 *   Lnet/minecraft/text/ClickEvent$ShowDialog;dialog()Lnet/minecraft/registry/entry/RegistryEntry;
 *   Lnet/minecraft/client/gui/screen/dialog/DialogNetworkAccess;showDialog(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/client/gui/screen/Screen;)V
 *   Lnet/minecraft/text/ClickEvent$Custom;id()Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/text/ClickEvent$Custom;payload()Ljava/util/Optional;
 *   Lnet/minecraft/client/gui/screen/dialog/DialogNetworkAccess;sendCustomClickActionPacket(Lnet/minecraft/util/Identifier;Ljava/util/Optional;)V
 *   Lnet/minecraft/client/gui/screen/dialog/DialogScreen$WarningScreen;create(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/gui/screen/dialog/DialogNetworkAccess;Lnet/minecraft/client/gui/screen/Screen;)Lnet/minecraft/client/gui/screen/Screen;
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/screen/dialog/DialogScreen;createWarningButton()Lnet/minecraft/client/gui/widget/ButtonWidget;
 *   Lnet/minecraft/client/gui/screen/dialog/DialogScreen;createHeader()Lnet/minecraft/client/gui/widget/Widget;
 *   Lnet/minecraft/client/gui/screen/dialog/DialogScreen;initBody(Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;Lnet/minecraft/client/gui/screen/dialog/DialogControls;Lnet/minecraft/dialog/type/Dialog;Lnet/minecraft/client/gui/screen/dialog/DialogNetworkAccess;)V
 *   Lnet/minecraft/client/gui/screen/dialog/DialogScreen;initHeaderAndFooter(Lnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;Lnet/minecraft/client/gui/screen/dialog/DialogControls;Lnet/minecraft/dialog/type/Dialog;Lnet/minecraft/client/gui/screen/dialog/DialogNetworkAccess;)V
 *   Lnet/minecraft/client/gui/screen/dialog/DialogScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;
 *   Lnet/minecraft/client/gui/screen/dialog/DialogScreen;runAction(Ljava/util/Optional;Lnet/minecraft/dialog/AfterAction;)V
 *   Lnet/minecraft/client/gui/screen/dialog/DialogScreen;handleClickEvent(Lnet/minecraft/text/ClickEvent;Lnet/minecraft/client/gui/screen/Screen;)V
 *   Lnet/minecraft/client/gui/screen/dialog/DialogScreen;handleBasicClickEvent(Lnet/minecraft/text/ClickEvent;Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/gui/screen/Screen;)V
 */
package net.minecraft.client.gui.screen.dialog;

import java.lang.runtime.SwitchBootstraps;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.dialog.DialogBodyHandlers;
import net.minecraft.client.gui.screen.dialog.DialogControls;
import net.minecraft.client.gui.screen.dialog.DialogNetworkAccess;
import net.minecraft.client.gui.screen.dialog.WaitingForResponseScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.ScrollableLayoutWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.dialog.AfterAction;
import net.minecraft.dialog.body.DialogBody;
import net.minecraft.dialog.type.Dialog;
import net.minecraft.dialog.type.DialogInput;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public abstract class DialogScreen<T extends Dialog>
extends Screen {
    public static final Text CUSTOM_SCREEN_REJECTED_DISCONNECT_TEXT = Text.translatable("menu.custom_screen_info.disconnect");
    private static final int field_60758 = 20;
    private static final ButtonTextures WARNING_BUTTON_TEXTURES = new ButtonTextures(Identifier.ofVanilla("dialog/warning_button"), Identifier.ofVanilla("dialog/warning_button_disabled"), Identifier.ofVanilla("dialog/warning_button_highlighted"));
    private final T dialog;
    private final ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);
    private final @Nullable Screen parent;
    private @Nullable ScrollableLayoutWidget contents;
    private ButtonWidget warningButton;
    private final DialogNetworkAccess networkAccess;
    private Supplier<Optional<ClickEvent>> cancelAction = DialogControls.EMPTY_ACTION_CLICK_EVENT;

    public DialogScreen(@Nullable Screen parent, T dialog, DialogNetworkAccess networkAccess) {
        super(dialog.common().title());
        this.dialog = dialog;
        this.parent = parent;
        this.networkAccess = networkAccess;
    }

    @Override
    protected final void init() {
        super.init();
        this.warningButton = this.createWarningButton();
        this.warningButton.setNavigationOrder(-10);
        DialogControls lv = new DialogControls(this);
        DirectionalLayoutWidget lv2 = DirectionalLayoutWidget.vertical().spacing(10);
        lv2.getMainPositioner().alignHorizontalCenter();
        this.layout.addHeader(this.createHeader());
        for (DialogBody lv3 : this.dialog.common().body()) {
            Widget lv4 = DialogBodyHandlers.createWidget(this, lv3);
            if (lv4 == null) continue;
            lv2.add(lv4);
        }
        for (DialogInput lv5 : this.dialog.common().inputs()) {
            lv.addInput(lv5, lv2::add);
        }
        this.initBody(lv2, lv, this.dialog, this.networkAccess);
        this.contents = new ScrollableLayoutWidget(this.client, lv2, this.layout.getContentHeight());
        this.layout.addBody(this.contents);
        this.initHeaderAndFooter(this.layout, lv, this.dialog, this.networkAccess);
        this.cancelAction = lv.createClickEvent(this.dialog.getCancelAction());
        this.layout.forEachChild(child -> {
            if (child != this.warningButton) {
                this.addDrawableChild(child);
            }
        });
        this.addDrawableChild(this.warningButton);
        this.refreshWidgetPositions();
    }

    protected void initBody(DirectionalLayoutWidget bodyLayout, DialogControls controls, T dialog, DialogNetworkAccess networkAccess) {
    }

    protected void initHeaderAndFooter(ThreePartsLayoutWidget layout, DialogControls controls, T dialog, DialogNetworkAccess networkAccess) {
    }

    @Override
    protected void refreshWidgetPositions() {
        this.contents.setHeight(this.layout.getContentHeight());
        this.layout.refreshPositions();
        this.resetWarningButtonPosition();
    }

    protected Widget createHeader() {
        DirectionalLayoutWidget lv = DirectionalLayoutWidget.horizontal().spacing(10);
        lv.getMainPositioner().alignHorizontalCenter().alignVerticalCenter();
        lv.add(new TextWidget(this.title, this.textRenderer));
        lv.add(this.warningButton);
        return lv;
    }

    protected void resetWarningButtonPosition() {
        int i = this.warningButton.getX();
        int j = this.warningButton.getY();
        if (i < 0 || j < 0 || i > this.width - 20 || j > this.height - 20) {
            this.warningButton.setX(Math.max(0, this.width - 40));
            this.warningButton.setY(Math.min(5, this.height));
        }
    }

    private ButtonWidget createWarningButton() {
        TexturedButtonWidget lv = new TexturedButtonWidget(0, 0, 20, 20, WARNING_BUTTON_TEXTURES, button -> this.client.setScreen(WarningScreen.create(this.client, this.networkAccess, this)), Text.translatable("menu.custom_screen_info.button_narration"));
        lv.setTooltip(Tooltip.of(Text.translatable("menu.custom_screen_info.tooltip")));
        return lv;
    }

    @Override
    public boolean shouldPause() {
        return this.dialog.common().pause();
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return this.dialog.common().canCloseWithEscape();
    }

    @Override
    public void close() {
        this.runAction(this.cancelAction.get(), AfterAction.CLOSE);
    }

    public void runAction(Optional<ClickEvent> clickEvent) {
        this.runAction(clickEvent, this.dialog.common().afterAction());
    }

    public void runAction(Optional<ClickEvent> clickEvent, AfterAction afterAction) {
        Screen lv;
        switch (afterAction) {
            default: {
                throw new MatchException(null, null);
            }
            case NONE: {
                Screen screen = this;
                break;
            }
            case CLOSE: {
                Screen screen = this.parent;
                break;
            }
            case WAIT_FOR_RESPONSE: {
                Screen screen = lv = new WaitingForResponseScreen(this.parent);
            }
        }
        if (clickEvent.isPresent()) {
            this.handleClickEvent(clickEvent.get(), lv);
        } else {
            this.client.setScreen(lv);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void handleClickEvent(ClickEvent clickEvent, @Nullable Screen afterActionScreen) {
        ClickEvent clickEvent2 = clickEvent;
        Objects.requireNonNull(clickEvent2);
        ClickEvent clickEvent3 = clickEvent2;
        int n = 0;
        switch (SwitchBootstraps.typeSwitch("typeSwitch", new Object[]{ClickEvent.RunCommand.class, ClickEvent.ShowDialog.class, ClickEvent.Custom.class}, (Object)clickEvent3, n)) {
            case 0: {
                ClickEvent.RunCommand runCommand = (ClickEvent.RunCommand)clickEvent3;
                try {
                    String string;
                    String string2 = string = runCommand.command();
                    this.networkAccess.runClickEventCommand(CommandManager.stripLeadingSlash(string2), afterActionScreen);
                    return;
                } catch (Throwable throwable) {
                    throw new MatchException(throwable.toString(), throwable);
                }
            }
            case 1: {
                ClickEvent.ShowDialog lv = (ClickEvent.ShowDialog)clickEvent3;
                this.networkAccess.showDialog(lv.dialog(), afterActionScreen);
                return;
            }
            case 2: {
                ClickEvent.Custom lv2 = (ClickEvent.Custom)clickEvent3;
                this.networkAccess.sendCustomClickActionPacket(lv2.id(), lv2.payload());
                this.client.setScreen(afterActionScreen);
                return;
            }
        }
        DialogScreen.handleBasicClickEvent(clickEvent, this.client, afterActionScreen);
    }

    public @Nullable Screen getParentScreen() {
        return this.parent;
    }

    protected static Widget createGridWidget(List<? extends Widget> widgets, int columns) {
        GridWidget lv = new GridWidget();
        lv.getMainPositioner().alignHorizontalCenter();
        lv.setColumnSpacing(2).setRowSpacing(2);
        int j = widgets.size();
        int k = j / columns;
        int l = k * columns;
        for (int m = 0; m < l; ++m) {
            lv.add(widgets.get(m), m / columns, m % columns);
        }
        if (j != l) {
            DirectionalLayoutWidget lv2 = DirectionalLayoutWidget.horizontal().spacing(2);
            lv2.getMainPositioner().alignHorizontalCenter();
            for (int n = l; n < j; ++n) {
                lv2.add(widgets.get(n));
            }
            lv.add(lv2, k, 0, 1, columns);
        }
        return lv;
    }

    @Environment(value=EnvType.CLIENT)
    public static class WarningScreen
    extends ConfirmScreen {
        private final MutableObject<@Nullable Screen> dialogScreen;

        public static Screen create(MinecraftClient client, DialogNetworkAccess dialogNetworkAccess, Screen dialogScreen) {
            return new WarningScreen(client, dialogNetworkAccess, new MutableObject<Screen>(dialogScreen));
        }

        private WarningScreen(MinecraftClient client, DialogNetworkAccess dialogNetworkAccess, MutableObject<Screen> dialogScreen) {
            super(disconnect -> {
                if (disconnect) {
                    dialogNetworkAccess.disconnect(CUSTOM_SCREEN_REJECTED_DISCONNECT_TEXT);
                } else {
                    client.setScreen((Screen)dialogScreen.get());
                }
            }, Text.translatable("menu.custom_screen_info.title"), Text.translatable("menu.custom_screen_info.contents"), ScreenTexts.returnToMenuOrDisconnect(client.isInSingleplayer()), ScreenTexts.BACK);
            this.dialogScreen = dialogScreen;
        }

        public @Nullable Screen getDialogScreen() {
            return (Screen)this.dialogScreen.get();
        }

        public void setDialogScreen(@Nullable Screen screen) {
            this.dialogScreen.setValue(screen);
        }
    }
}

