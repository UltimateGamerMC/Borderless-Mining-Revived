/*
 * External method calls:
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;vertical()Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;
 *   Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;spacing(I)Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;
 *   Lnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;addHeader(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;
 *   Lnet/minecraft/client/gui/widget/Positioner;alignHorizontalCenter()Lnet/minecraft/client/gui/widget/Positioner;
 *   Lnet/minecraft/text/Text;empty()Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;addBody(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;
 *   Lnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;addFooter(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;
 *   Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;horizontal()Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget;builder(Lnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;build()Lnet/minecraft/client/gui/widget/ButtonWidget;
 *   Lnet/minecraft/client/gui/screen/option/LanguageOptionsScreen$LanguageSelectionListWidget;position(ILnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;)V
 *   Lnet/minecraft/client/MinecraftClient;reloadResources()Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/text/MutableText;withColor(I)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/MutableText;fillStyle(Lnet/minecraft/text/Style;)Lnet/minecraft/text/MutableText;
 */
package net.minecraft.client.gui.screen.option;

import java.util.List;
import java.util.Locale;
import java.util.SortedMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.AccessibilityOnboardingScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.FontOptionsScreen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.resource.language.LanguageDefinition;
import net.minecraft.client.resource.language.LanguageManager;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class LanguageOptionsScreen
extends GameOptionsScreen {
    private static final Text LANGUAGE_WARNING_TEXT = Text.translatable("options.languageAccuracyWarning").withColor(Colors.ALTERNATE_WHITE);
    private static final int field_49497 = 53;
    private static final Text SEARCH_TEXT = Text.translatable("gui.language.search").fillStyle(TextFieldWidget.SEARCH_STYLE);
    private static final int field_64202 = 15;
    final LanguageManager languageManager;
    private @Nullable LanguageSelectionListWidget languageSelectionList;
    private @Nullable TextFieldWidget searchBox;

    public LanguageOptionsScreen(Screen parent, GameOptions options, LanguageManager languageManager) {
        super(parent, options, (Text)Text.translatable("options.language.title"));
        this.languageManager = languageManager;
        this.layout.setFooterHeight(53);
    }

    @Override
    protected void initHeader() {
        DirectionalLayoutWidget lv = this.layout.addHeader(DirectionalLayoutWidget.vertical().spacing(4));
        lv.getMainPositioner().alignHorizontalCenter();
        lv.add(new TextWidget(this.title, this.textRenderer));
        this.searchBox = lv.add(new TextFieldWidget(this.textRenderer, 0, 0, 200, 15, Text.empty()));
        this.searchBox.setPlaceholder(SEARCH_TEXT);
        this.searchBox.setChangedListener(search -> {
            if (this.languageSelectionList != null) {
                this.languageSelectionList.setSearch((String)search);
            }
        });
        this.layout.setHeaderHeight((int)(12.0 + (double)this.textRenderer.fontHeight + 15.0));
    }

    @Override
    protected void setInitialFocus() {
        if (this.searchBox != null) {
            this.setInitialFocus(this.searchBox);
        } else {
            super.setInitialFocus();
        }
    }

    @Override
    protected void initBody() {
        this.languageSelectionList = this.layout.addBody(new LanguageSelectionListWidget(this.client));
    }

    @Override
    protected void addOptions() {
    }

    @Override
    protected void initFooter() {
        DirectionalLayoutWidget lv = this.layout.addFooter(DirectionalLayoutWidget.vertical()).spacing(8);
        lv.getMainPositioner().alignHorizontalCenter();
        lv.add(new TextWidget(LANGUAGE_WARNING_TEXT, this.textRenderer));
        DirectionalLayoutWidget lv2 = lv.add(DirectionalLayoutWidget.horizontal().spacing(8));
        lv2.add(ButtonWidget.builder(Text.translatable("options.font"), button -> this.client.setScreen(new FontOptionsScreen(this, this.gameOptions))).build());
        lv2.add(ButtonWidget.builder(ScreenTexts.DONE, button -> this.onDone()).build());
    }

    @Override
    protected void refreshWidgetPositions() {
        super.refreshWidgetPositions();
        if (this.languageSelectionList != null) {
            this.languageSelectionList.position(this.width, this.layout);
        }
    }

    void onDone() {
        Object e;
        if (this.languageSelectionList != null && (e = this.languageSelectionList.getSelectedOrNull()) instanceof LanguageSelectionListWidget.LanguageEntry) {
            LanguageSelectionListWidget.LanguageEntry lv = (LanguageSelectionListWidget.LanguageEntry)e;
            if (!lv.languageCode.equals(this.languageManager.getLanguage())) {
                this.languageManager.setLanguage(lv.languageCode);
                this.gameOptions.language = lv.languageCode;
                this.client.reloadResources();
            }
        }
        this.client.setScreen(this.parent);
    }

    @Override
    protected boolean allowRotatingPanorama() {
        return !(this.parent instanceof AccessibilityOnboardingScreen);
    }

    @Environment(value=EnvType.CLIENT)
    class LanguageSelectionListWidget
    extends AlwaysSelectedEntryListWidget<LanguageEntry> {
        public LanguageSelectionListWidget(MinecraftClient client) {
            super(client, LanguageOptionsScreen.this.width, LanguageOptionsScreen.this.height - 33 - 53, 33, 18);
            String string = LanguageOptionsScreen.this.languageManager.getLanguage();
            LanguageOptionsScreen.this.languageManager.getAllLanguages().forEach((languageCode, languageDefinition) -> {
                LanguageEntry lv = new LanguageEntry((String)languageCode, (LanguageDefinition)languageDefinition);
                this.addEntry(lv);
                if (string.equals(languageCode)) {
                    this.setSelected(lv);
                }
            });
            if (this.getSelectedOrNull() != null) {
                this.centerScrollOn((LanguageEntry)this.getSelectedOrNull());
            }
        }

        void setSearch(String search) {
            SortedMap<String, LanguageDefinition> sortedMap = LanguageOptionsScreen.this.languageManager.getAllLanguages();
            List<LanguageEntry> list = sortedMap.entrySet().stream().filter(entry -> search.isEmpty() || ((LanguageDefinition)entry.getValue()).name().toLowerCase(Locale.ROOT).contains(search.toLowerCase(Locale.ROOT)) || ((LanguageDefinition)entry.getValue()).region().toLowerCase(Locale.ROOT).contains(search.toLowerCase(Locale.ROOT))).map(entry -> new LanguageEntry((String)entry.getKey(), (LanguageDefinition)entry.getValue())).toList();
            this.replaceEntries(list);
            this.refreshScroll();
        }

        @Override
        public int getRowWidth() {
            return super.getRowWidth() + 50;
        }

        @Environment(value=EnvType.CLIENT)
        public class LanguageEntry
        extends AlwaysSelectedEntryListWidget.Entry<LanguageEntry> {
            final String languageCode;
            private final Text languageDefinition;

            public LanguageEntry(String languageCode, LanguageDefinition languageDefinition) {
                this.languageCode = languageCode;
                this.languageDefinition = languageDefinition.getDisplayText();
            }

            @Override
            public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
                context.drawCenteredTextWithShadow(LanguageOptionsScreen.this.textRenderer, this.languageDefinition, LanguageSelectionListWidget.this.width / 2, this.getContentMiddleY() - ((LanguageOptionsScreen)LanguageOptionsScreen.this).textRenderer.fontHeight / 2, Colors.WHITE);
            }

            @Override
            public boolean keyPressed(KeyInput input) {
                if (input.isEnterOrSpace()) {
                    this.onPressed();
                    LanguageOptionsScreen.this.onDone();
                    return true;
                }
                return super.keyPressed(input);
            }

            @Override
            public boolean mouseClicked(Click click, boolean doubled) {
                this.onPressed();
                if (doubled) {
                    LanguageOptionsScreen.this.onDone();
                }
                return super.mouseClicked(click, doubled);
            }

            private void onPressed() {
                LanguageSelectionListWidget.this.setSelected(this);
            }

            @Override
            public Text getNarration() {
                return Text.translatable("narrator.select", this.languageDefinition);
            }
        }
    }
}

