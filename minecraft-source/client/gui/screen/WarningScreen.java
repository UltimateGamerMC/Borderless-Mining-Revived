/*
 * External method calls:
 *   Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;vertical()Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;
 *   Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;spacing(I)Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;
 *   Lnet/minecraft/client/gui/widget/Positioner;alignHorizontalCenter()Lnet/minecraft/client/gui/widget/Positioner;
 *   Lnet/minecraft/client/gui/widget/CheckboxWidget;builder(Lnet/minecraft/text/Text;Lnet/minecraft/client/font/TextRenderer;)Lnet/minecraft/client/gui/widget/CheckboxWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/CheckboxWidget$Builder;build()Lnet/minecraft/client/gui/widget/CheckboxWidget;
 *   Lnet/minecraft/client/gui/widget/SimplePositioningWidget;forEachChild(Ljava/util/function/Consumer;)V
 *   Lnet/minecraft/client/gui/widget/Positioner;margin(I)Lnet/minecraft/client/gui/widget/Positioner;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/screen/WarningScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;
 */
package net.minecraft.client.gui.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.LayoutWidget;
import net.minecraft.client.gui.widget.ScrollableTextWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public abstract class WarningScreen
extends Screen {
    private static final int field_49538 = 100;
    private final Text messageText;
    private final @Nullable Text checkMessage;
    private final Text narratedText;
    protected @Nullable CheckboxWidget checkbox;
    private @Nullable ScrollableTextWidget textWidget;
    private final SimplePositioningWidget positioningWidget;

    protected WarningScreen(Text header, Text message, Text narratedText) {
        this(header, message, null, narratedText);
    }

    protected WarningScreen(Text header, Text messageText, @Nullable Text checkMessage, Text narratedText) {
        super(header);
        this.messageText = messageText;
        this.checkMessage = checkMessage;
        this.narratedText = narratedText;
        this.positioningWidget = new SimplePositioningWidget(0, 0, this.width, this.height);
    }

    protected abstract LayoutWidget getLayout();

    @Override
    protected void init() {
        DirectionalLayoutWidget lv = this.positioningWidget.add(DirectionalLayoutWidget.vertical().spacing(8));
        lv.getMainPositioner().alignHorizontalCenter();
        lv.add(new TextWidget(this.getTitle(), this.textRenderer));
        this.textWidget = lv.add(new ScrollableTextWidget(0, 0, this.width - 100, this.height - 100, this.messageText, this.textRenderer), positioner -> positioner.margin(12));
        DirectionalLayoutWidget lv2 = lv.add(DirectionalLayoutWidget.vertical().spacing(8));
        lv2.getMainPositioner().alignHorizontalCenter();
        if (this.checkMessage != null) {
            this.checkbox = lv2.add(CheckboxWidget.builder(this.checkMessage, this.textRenderer).build());
        }
        lv2.add(this.getLayout());
        this.positioningWidget.forEachChild(child -> {
            ClickableWidget cfr_ignored_0 = (ClickableWidget)this.addDrawableChild(child);
        });
        this.refreshWidgetPositions();
    }

    @Override
    protected void refreshWidgetPositions() {
        if (this.textWidget != null) {
            this.textWidget.setWidth(this.width - 100);
            this.textWidget.setHeight(this.height - 100);
            this.textWidget.updateHeight();
        }
        this.positioningWidget.refreshPositions();
        SimplePositioningWidget.setPos(this.positioningWidget, this.getNavigationFocus());
    }

    @Override
    public Text getNarratedTitle() {
        return this.narratedText;
    }
}

