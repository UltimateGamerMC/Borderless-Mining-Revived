/*
 * External method calls:
 *   Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;vertical()Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;
 *   Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;spacing(I)Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/widget/LayoutWidgets;createLabeledWidget(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/client/gui/widget/Widget;Lnet/minecraft/text/Text;Ljava/util/function/Consumer;)Lnet/minecraft/client/gui/widget/LayoutWidget;
 */
package net.minecraft.client.gui.widget;

import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.LayoutWidget;
import net.minecraft.client.gui.widget.Positioner;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;

@Environment(value=EnvType.CLIENT)
public class LayoutWidgets {
    private static final int SPACING = 4;

    private LayoutWidgets() {
    }

    public static LayoutWidget createLabeledWidget(TextRenderer textRenderer, Widget widget, Text label) {
        return LayoutWidgets.createLabeledWidget(textRenderer, widget, label, positioner -> {});
    }

    public static LayoutWidget createLabeledWidget(TextRenderer textRenderer, Widget widget, Text label, Consumer<Positioner> callback) {
        DirectionalLayoutWidget lv = DirectionalLayoutWidget.vertical().spacing(4);
        lv.add(new TextWidget(label, textRenderer));
        lv.add(widget, callback);
        return lv;
    }
}

