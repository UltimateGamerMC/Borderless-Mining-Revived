/*
 * External method calls:
 *   Lnet/minecraft/dialog/type/DialogInput;key()Ljava/lang/String;
 *   Lnet/minecraft/dialog/type/DialogInput;control()Lnet/minecraft/dialog/input/InputControl;
 *   Lnet/minecraft/client/gui/screen/dialog/InputControlHandlers;addControl(Lnet/minecraft/dialog/input/InputControl;Lnet/minecraft/client/gui/screen/Screen;Lnet/minecraft/client/gui/screen/dialog/InputControlHandler$Output;)V
 *   Lnet/minecraft/dialog/DialogButtonData;label()Lnet/minecraft/text/Text;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget;builder(Lnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;width(I)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/dialog/DialogButtonData;tooltip()Ljava/util/Optional;
 *   Lnet/minecraft/client/gui/tooltip/Tooltip;of(Lnet/minecraft/text/Text;)Lnet/minecraft/client/gui/tooltip/Tooltip;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;tooltip(Lnet/minecraft/client/gui/tooltip/Tooltip;)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/dialog/DialogActionButtonData;action()Ljava/util/Optional;
 *   Lnet/minecraft/dialog/DialogActionButtonData;data()Lnet/minecraft/dialog/DialogButtonData;
 *   Lnet/minecraft/client/gui/screen/dialog/DialogScreen;runAction(Ljava/util/Optional;)V
 *   Lnet/minecraft/dialog/action/DialogAction;createClickEvent(Ljava/util/Map;)Ljava/util/Optional;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/screen/dialog/DialogControls;createClickEvent(Ljava/util/Optional;)Ljava/util/function/Supplier;
 *   Lnet/minecraft/client/gui/screen/dialog/DialogControls;createButton(Lnet/minecraft/dialog/DialogButtonData;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 */
package net.minecraft.client.gui.screen.dialog;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.dialog.DialogScreen;
import net.minecraft.client.gui.screen.dialog.InputControlHandlers;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.dialog.DialogActionButtonData;
import net.minecraft.dialog.DialogButtonData;
import net.minecraft.dialog.action.DialogAction;
import net.minecraft.dialog.type.DialogInput;
import net.minecraft.text.ClickEvent;

@Environment(value=EnvType.CLIENT)
public class DialogControls {
    public static final Supplier<Optional<ClickEvent>> EMPTY_ACTION_CLICK_EVENT = Optional::empty;
    private final DialogScreen<?> screen;
    private final Map<String, DialogAction.ValueGetter> valueGetters = new HashMap<String, DialogAction.ValueGetter>();

    public DialogControls(DialogScreen<?> screen) {
        this.screen = screen;
    }

    public void addInput(DialogInput input, Consumer<Widget> widgetConsumer) {
        String string = input.key();
        InputControlHandlers.addControl(input.control(), this.screen, (widget, valueGetter) -> {
            this.valueGetters.put(string, valueGetter);
            widgetConsumer.accept(widget);
        });
    }

    private static ButtonWidget.Builder createButton(DialogButtonData data, ButtonWidget.PressAction pressAction) {
        ButtonWidget.Builder lv = ButtonWidget.builder(data.label(), pressAction);
        lv.width(data.width());
        if (data.tooltip().isPresent()) {
            lv = lv.tooltip(Tooltip.of(data.tooltip().get()));
        }
        return lv;
    }

    public Supplier<Optional<ClickEvent>> createClickEvent(Optional<DialogAction> action) {
        if (action.isPresent()) {
            DialogAction lv = action.get();
            return () -> lv.createClickEvent(this.valueGetters);
        }
        return EMPTY_ACTION_CLICK_EVENT;
    }

    public ButtonWidget.Builder createButton(DialogActionButtonData actionButtonData) {
        Supplier<Optional<ClickEvent>> supplier = this.createClickEvent(actionButtonData.action());
        return DialogControls.createButton(actionButtonData.data(), button -> this.screen.runAction((Optional)supplier.get()));
    }
}

