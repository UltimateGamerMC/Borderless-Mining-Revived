/*
 * External method calls:
 *   Lnet/minecraft/client/gui/widget/OptionListWidget$WidgetEntry;create(Lnet/minecraft/client/option/GameOptions;Lnet/minecraft/client/option/SimpleOption;Lnet/minecraft/client/gui/screen/Screen;)Lnet/minecraft/client/gui/widget/OptionListWidget$WidgetEntry;
 *   Lnet/minecraft/client/gui/widget/OptionListWidget$WidgetEntry;create(Lnet/minecraft/client/option/GameOptions;Lnet/minecraft/client/option/SimpleOption;Lnet/minecraft/client/option/SimpleOption;Lnet/minecraft/client/gui/screen/option/GameOptionsScreen;)Lnet/minecraft/client/gui/widget/OptionListWidget$WidgetEntry;
 *   Lnet/minecraft/client/gui/widget/OptionListWidget$WidgetEntry;create(Lnet/minecraft/client/gui/widget/ClickableWidget;Lnet/minecraft/client/gui/widget/ClickableWidget;Lnet/minecraft/client/gui/screen/Screen;)Lnet/minecraft/client/gui/widget/OptionListWidget$WidgetEntry;
 *   Lnet/minecraft/client/gui/widget/OptionListWidget$WidgetEntry;create(Lnet/minecraft/client/gui/widget/ClickableWidget;Lnet/minecraft/client/option/SimpleOption;Lnet/minecraft/client/gui/widget/ClickableWidget;Lnet/minecraft/client/gui/screen/Screen;)Lnet/minecraft/client/gui/widget/OptionListWidget$WidgetEntry;
 *   Lnet/minecraft/client/gui/widget/OptionListWidget$OptionAssociatedWidget;optionInstance()Lnet/minecraft/client/option/SimpleOption;
 *   Lnet/minecraft/client/gui/widget/OptionListWidget$OptionAssociatedWidget;widget()Lnet/minecraft/client/gui/widget/ClickableWidget;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/widget/OptionListWidget;addEntry(Lnet/minecraft/client/gui/widget/EntryListWidget$Entry;)I
 *   Lnet/minecraft/client/gui/widget/OptionListWidget;addWidgetEntry(Lnet/minecraft/client/gui/widget/ClickableWidget;Lnet/minecraft/client/gui/widget/ClickableWidget;)V
 *   Lnet/minecraft/client/gui/widget/OptionListWidget;children()Ljava/util/List;
 *   Lnet/minecraft/client/gui/widget/OptionListWidget;addEntry(Lnet/minecraft/client/gui/widget/EntryListWidget$Entry;I)I
 */
package net.minecraft.client.gui.widget;

import com.google.common.collect.Lists;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.Updatable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class OptionListWidget
extends ElementListWidget<Component> {
    private static final int field_49481 = 310;
    private static final int field_49482 = 25;
    private final GameOptionsScreen optionsScreen;

    public OptionListWidget(MinecraftClient client, int width, GameOptionsScreen optionsScreen) {
        super(client, width, optionsScreen.layout.getContentHeight(), optionsScreen.layout.getHeaderHeight(), 25);
        this.centerListVertically = false;
        this.optionsScreen = optionsScreen;
    }

    public void addSingleOptionEntry(SimpleOption<?> option) {
        this.addEntry(WidgetEntry.create(this.client.options, option, (Screen)this.optionsScreen));
    }

    public void addAll(SimpleOption<?> ... options) {
        for (int i = 0; i < options.length; i += 2) {
            SimpleOption<?> lv = i < options.length - 1 ? options[i + 1] : null;
            this.addEntry(WidgetEntry.create(this.client.options, options[i], lv, this.optionsScreen));
        }
    }

    public void addAll(List<ClickableWidget> widgets) {
        for (int i = 0; i < widgets.size(); i += 2) {
            this.addWidgetEntry(widgets.get(i), i < widgets.size() - 1 ? widgets.get(i + 1) : null);
        }
    }

    public void addWidgetEntry(ClickableWidget firstWidget, @Nullable ClickableWidget secondWidget) {
        this.addEntry(WidgetEntry.create(firstWidget, secondWidget, (Screen)this.optionsScreen));
    }

    public void addWidgetEntry(ClickableWidget firstWidget, SimpleOption<?> option, @Nullable ClickableWidget secondWidget) {
        this.addEntry(WidgetEntry.create(firstWidget, option, secondWidget, (Screen)this.optionsScreen));
    }

    public void addHeader(Text title) {
        int i = this.client.textRenderer.fontHeight;
        int j = this.children().isEmpty() ? 0 : i * 2;
        this.addEntry(new Header(this.optionsScreen, title, j), j + i + 4);
    }

    @Override
    public int getRowWidth() {
        return 310;
    }

    public @Nullable ClickableWidget getWidgetFor(SimpleOption<?> option) {
        for (Component lv : this.children()) {
            WidgetEntry lv2;
            ClickableWidget lv3;
            if (!(lv instanceof WidgetEntry) || (lv3 = (lv2 = (WidgetEntry)lv).getWidgetFor(option)) == null) continue;
            return lv3;
        }
        return null;
    }

    public void applyAllPendingValues() {
        for (Component lv : this.children()) {
            if (!(lv instanceof WidgetEntry)) continue;
            WidgetEntry lv2 = (WidgetEntry)lv;
            for (OptionAssociatedWidget lv3 : lv2.widgets) {
                ClickableWidget clickableWidget;
                if (lv3.optionInstance() == null || !((clickableWidget = lv3.widget()) instanceof SimpleOption.OptionSliderWidgetImpl)) continue;
                SimpleOption.OptionSliderWidgetImpl lv4 = (SimpleOption.OptionSliderWidgetImpl)clickableWidget;
                lv4.applyPendingValue();
            }
        }
    }

    public void update(SimpleOption<?> arg) {
        for (Component lv : this.children()) {
            if (!(lv instanceof WidgetEntry)) continue;
            WidgetEntry lv2 = (WidgetEntry)lv;
            for (OptionAssociatedWidget lv3 : lv2.widgets) {
                ClickableWidget clickableWidget;
                if (lv3.optionInstance() != arg || !((clickableWidget = lv3.widget()) instanceof Updatable)) continue;
                Updatable lv4 = (Updatable)((Object)clickableWidget);
                lv4.update();
                return;
            }
        }
    }

    @Environment(value=EnvType.CLIENT)
    protected static class WidgetEntry
    extends Component {
        final List<OptionAssociatedWidget> widgets;
        private final Screen screen;
        private static final int WIDGET_X_SPACING = 160;

        private WidgetEntry(List<OptionAssociatedWidget> widgets, Screen screen) {
            this.widgets = widgets;
            this.screen = screen;
        }

        public static WidgetEntry create(GameOptions options, SimpleOption<?> option, Screen screen) {
            return new WidgetEntry(List.of(new OptionAssociatedWidget(option.createWidget(options, 0, 0, 310), option)), screen);
        }

        public static WidgetEntry create(ClickableWidget firstWidget, @Nullable ClickableWidget secondWidget, Screen screen) {
            if (secondWidget == null) {
                return new WidgetEntry(List.of(new OptionAssociatedWidget(firstWidget)), screen);
            }
            return new WidgetEntry(List.of(new OptionAssociatedWidget(firstWidget), new OptionAssociatedWidget(secondWidget)), screen);
        }

        public static WidgetEntry create(ClickableWidget firstWidget, SimpleOption<?> option, @Nullable ClickableWidget secondWidget, Screen screen) {
            if (secondWidget == null) {
                return new WidgetEntry(List.of(new OptionAssociatedWidget(firstWidget, option)), screen);
            }
            return new WidgetEntry(List.of(new OptionAssociatedWidget(firstWidget, option), new OptionAssociatedWidget(secondWidget)), screen);
        }

        public static WidgetEntry create(GameOptions options, SimpleOption<?> firstOption, @Nullable SimpleOption<?> secondOption, GameOptionsScreen screen) {
            ClickableWidget lv = firstOption.createWidget(options);
            if (secondOption == null) {
                return new WidgetEntry(List.of(new OptionAssociatedWidget(lv, firstOption)), screen);
            }
            return new WidgetEntry(List.of(new OptionAssociatedWidget(lv, firstOption), new OptionAssociatedWidget(secondOption.createWidget(options), secondOption)), screen);
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            int k = 0;
            int l = this.screen.width / 2 - 155;
            for (OptionAssociatedWidget lv : this.widgets) {
                lv.widget().setPosition(l + k, this.getContentY());
                lv.widget().render(context, mouseX, mouseY, deltaTicks);
                k += 160;
            }
        }

        @Override
        public List<? extends Element> children() {
            return Lists.transform(this.widgets, OptionAssociatedWidget::widget);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return Lists.transform(this.widgets, OptionAssociatedWidget::widget);
        }

        public @Nullable ClickableWidget getWidgetFor(SimpleOption<?> option) {
            for (OptionAssociatedWidget lv : this.widgets) {
                if (lv.optionInstance != option) continue;
                return lv.widget();
            }
            return null;
        }
    }

    @Environment(value=EnvType.CLIENT)
    protected static class Header
    extends Component {
        private final Screen parent;
        private final int yOffset;
        private final TextWidget title;

        protected Header(Screen parent, Text title, int yOffset) {
            this.parent = parent;
            this.yOffset = yOffset;
            this.title = new TextWidget(title, parent.getTextRenderer());
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return List.of(this.title);
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            this.title.setPosition(this.parent.width / 2 - 155, this.getContentY() + this.yOffset);
            this.title.render(context, mouseX, mouseY, deltaTicks);
        }

        @Override
        public List<? extends Element> children() {
            return List.of(this.title);
        }
    }

    @Environment(value=EnvType.CLIENT)
    protected static abstract class Component
    extends ElementListWidget.Entry<Component> {
        protected Component() {
        }
    }

    @Environment(value=EnvType.CLIENT)
    public record OptionAssociatedWidget(ClickableWidget widget, @Nullable SimpleOption<?> optionInstance) {
        public OptionAssociatedWidget(ClickableWidget widget) {
            this(widget, null);
        }
    }
}

