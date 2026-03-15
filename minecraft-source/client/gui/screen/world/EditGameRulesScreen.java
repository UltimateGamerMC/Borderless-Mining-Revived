/*
 * External method calls:
 *   Lnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;addHeader(Lnet/minecraft/text/Text;Lnet/minecraft/client/font/TextRenderer;)V
 *   Lnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;addBody(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;
 *   Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;horizontal()Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;
 *   Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;spacing(I)Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;
 *   Lnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;addFooter(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget;builder(Lnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;build()Lnet/minecraft/client/gui/widget/ButtonWidget;
 *   Lnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;forEachChild(Ljava/util/function/Consumer;)V
 *   Lnet/minecraft/client/gui/screen/world/EditGameRulesScreen$RuleListWidget;position(ILnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;)V
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/screen/world/EditGameRulesScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;
 */
package net.minecraft.client.gui.screen.world;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.serialization.DataResult;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import net.minecraft.world.rule.GameRule;
import net.minecraft.world.rule.GameRuleCategory;
import net.minecraft.world.rule.GameRuleVisitor;
import net.minecraft.world.rule.GameRules;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class EditGameRulesScreen
extends Screen {
    private static final Text TITLE = Text.translatable("editGamerule.title");
    private static final int field_49559 = 8;
    final ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);
    private final Consumer<Optional<GameRules>> ruleSaver;
    private final Set<AbstractRuleWidget> invalidRuleWidgets = Sets.newHashSet();
    final GameRules gameRules;
    private @Nullable RuleListWidget ruleListWidget;
    private @Nullable ButtonWidget doneButton;

    public EditGameRulesScreen(GameRules gameRules, Consumer<Optional<GameRules>> ruleSaveConsumer) {
        super(TITLE);
        this.gameRules = gameRules;
        this.ruleSaver = ruleSaveConsumer;
    }

    @Override
    protected void init() {
        this.layout.addHeader(TITLE, this.textRenderer);
        this.ruleListWidget = this.layout.addBody(new RuleListWidget(this.gameRules));
        DirectionalLayoutWidget lv = this.layout.addFooter(DirectionalLayoutWidget.horizontal().spacing(8));
        this.doneButton = lv.add(ButtonWidget.builder(ScreenTexts.DONE, button -> this.ruleSaver.accept(Optional.of(this.gameRules))).build());
        lv.add(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.close()).build());
        this.layout.forEachChild(child -> {
            ClickableWidget cfr_ignored_0 = (ClickableWidget)this.addDrawableChild(child);
        });
        this.refreshWidgetPositions();
    }

    @Override
    protected void refreshWidgetPositions() {
        this.layout.refreshPositions();
        if (this.ruleListWidget != null) {
            this.ruleListWidget.position(this.width, this.layout);
        }
    }

    @Override
    public void close() {
        this.ruleSaver.accept(Optional.empty());
    }

    private void updateDoneButton() {
        if (this.doneButton != null) {
            this.doneButton.active = this.invalidRuleWidgets.isEmpty();
        }
    }

    void markInvalid(AbstractRuleWidget ruleWidget) {
        this.invalidRuleWidgets.add(ruleWidget);
        this.updateDoneButton();
    }

    void markValid(AbstractRuleWidget ruleWidget) {
        this.invalidRuleWidgets.remove(ruleWidget);
        this.updateDoneButton();
    }

    @Environment(value=EnvType.CLIENT)
    public class RuleListWidget
    extends ElementListWidget<AbstractRuleWidget> {
        private static final int field_49561 = 24;

        public RuleListWidget(GameRules gameRules) {
            super(MinecraftClient.getInstance(), EditGameRulesScreen.this.width, EditGameRulesScreen.this.layout.getContentHeight(), EditGameRulesScreen.this.layout.getHeaderHeight(), 24);
            final HashMap map = Maps.newHashMap();
            gameRules.accept(new GameRuleVisitor(){

                @Override
                public void visitBoolean(GameRule<Boolean> rule2) {
                    this.createRuleWidget(rule2, (name, description, ruleName, rule) -> new BooleanRuleWidget(name, description, ruleName, rule));
                }

                @Override
                public void visitInt(GameRule<Integer> rule2) {
                    this.createRuleWidget(rule2, (name, description, ruleName, rule) -> new IntRuleWidget(name, description, ruleName, rule));
                }

                private <T> void createRuleWidget(GameRule<T> key, RuleWidgetFactory<T> widgetFactory) {
                    Object string2;
                    ImmutableCollection list;
                    MutableText lv = Text.translatable(key.getTranslationKey());
                    MutableText lv2 = Text.literal(key.toShortString()).formatted(Formatting.YELLOW);
                    MutableText lv3 = Text.translatable("editGamerule.default", Text.literal(key.getValueName(key.getDefaultValue()))).formatted(Formatting.GRAY);
                    String string = key.getTranslationKey() + ".description";
                    if (I18n.hasTranslation(string)) {
                        ImmutableCollection.Builder builder = ImmutableList.builder().add(lv2.asOrderedText());
                        MutableText lv4 = Text.translatable(string);
                        EditGameRulesScreen.this.textRenderer.wrapLines(lv4, 150).forEach(((ImmutableList.Builder)builder)::add);
                        list = ((ImmutableList.Builder)((ImmutableList.Builder)builder).add(lv3.asOrderedText())).build();
                        string2 = lv4.getString() + "\n" + lv3.getString();
                    } else {
                        list = ImmutableList.of(lv2.asOrderedText(), lv3.asOrderedText());
                        string2 = lv3.getString();
                    }
                    map.computeIfAbsent(key.getCategory(), category -> Maps.newHashMap()).put(key, widgetFactory.create(lv, (List<OrderedText>)((Object)list), (String)string2, key));
                }
            });
            map.entrySet().stream().sorted(Map.Entry.comparingByKey(Comparator.comparing(GameRuleCategory::getCategory))).forEach(entry -> {
                this.addEntry(new RuleCategoryWidget(((GameRuleCategory)entry.getKey()).getText().formatted(Formatting.BOLD, Formatting.YELLOW)));
                ((Map)entry.getValue()).entrySet().stream().sorted(Map.Entry.comparingByKey(Comparator.comparing(GameRule::getTranslationKey))).forEach(e -> this.addEntry((AbstractRuleWidget)e.getValue()));
            });
        }

        @Override
        public void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
            super.renderWidget(context, mouseX, mouseY, deltaTicks);
            AbstractRuleWidget lv = (AbstractRuleWidget)this.getHoveredEntry();
            if (lv != null && lv.description != null) {
                context.drawTooltip(lv.description, mouseX, mouseY);
            }
        }
    }

    @Environment(value=EnvType.CLIENT)
    public class IntRuleWidget
    extends NamedRuleWidget {
        private final TextFieldWidget valueWidget;

        public IntRuleWidget(Text name, List<OrderedText> description, String ruleName, GameRule<Integer> rule) {
            super(description, name);
            this.valueWidget = new TextFieldWidget(((EditGameRulesScreen)EditGameRulesScreen.this).client.textRenderer, 10, 5, 44, 20, name.copy().append("\n").append(ruleName).append("\n"));
            this.valueWidget.setText(EditGameRulesScreen.this.gameRules.getRuleValueName(rule));
            this.valueWidget.setChangedListener(value -> {
                DataResult dataResult = rule.deserialize((String)value);
                if (dataResult.isSuccess()) {
                    this.valueWidget.setEditableColor(-2039584);
                    EditGameRulesScreen.this.markValid(this);
                    EditGameRulesScreen.this.gameRules.setValue(rule, (Integer)dataResult.getOrThrow(), null);
                } else {
                    this.valueWidget.setEditableColor(-65536);
                    EditGameRulesScreen.this.markInvalid(this);
                }
            });
            this.children.add(this.valueWidget);
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            this.drawName(context, this.getContentY(), this.getContentX());
            this.valueWidget.setX(this.getContentRightEnd() - 45);
            this.valueWidget.setY(this.getContentY());
            this.valueWidget.render(context, mouseX, mouseY, deltaTicks);
        }
    }

    @Environment(value=EnvType.CLIENT)
    public class BooleanRuleWidget
    extends NamedRuleWidget {
        private final CyclingButtonWidget<Boolean> toggleButton;

        public BooleanRuleWidget(Text name, List<OrderedText> description, String ruleName, GameRule<Boolean> rule) {
            super(description, name);
            this.toggleButton = CyclingButtonWidget.onOffBuilder(EditGameRulesScreen.this.gameRules.getValue(rule)).omitKeyText().narration(button -> button.getGenericNarrationMessage().append("\n").append(ruleName)).build(10, 5, 44, 20, name, (button, value) -> EditGameRulesScreen.this.gameRules.setValue(rule, value, null));
            this.children.add(this.toggleButton);
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            this.drawName(context, this.getContentY(), this.getContentX());
            this.toggleButton.setX(this.getContentRightEnd() - 45);
            this.toggleButton.setY(this.getContentY());
            this.toggleButton.render(context, mouseX, mouseY, deltaTicks);
        }
    }

    @Environment(value=EnvType.CLIENT)
    public abstract class NamedRuleWidget
    extends AbstractRuleWidget {
        private final List<OrderedText> name;
        protected final List<ClickableWidget> children;

        public NamedRuleWidget(List<OrderedText> description, Text name) {
            super(description);
            this.children = Lists.newArrayList();
            this.name = ((EditGameRulesScreen)EditGameRulesScreen.this).client.textRenderer.wrapLines(name, 175);
        }

        @Override
        public List<? extends Element> children() {
            return this.children;
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return this.children;
        }

        protected void drawName(DrawContext context, int x, int y) {
            if (this.name.size() == 1) {
                context.drawTextWithShadow(((EditGameRulesScreen)EditGameRulesScreen.this).client.textRenderer, this.name.get(0), y, x + 5, -1);
            } else if (this.name.size() >= 2) {
                context.drawTextWithShadow(((EditGameRulesScreen)EditGameRulesScreen.this).client.textRenderer, this.name.get(0), y, x, -1);
                context.drawTextWithShadow(((EditGameRulesScreen)EditGameRulesScreen.this).client.textRenderer, this.name.get(1), y, x + 10, -1);
            }
        }
    }

    @FunctionalInterface
    @Environment(value=EnvType.CLIENT)
    static interface RuleWidgetFactory<T> {
        public AbstractRuleWidget create(Text var1, List<OrderedText> var2, String var3, GameRule<T> var4);
    }

    @Environment(value=EnvType.CLIENT)
    public class RuleCategoryWidget
    extends AbstractRuleWidget {
        final Text name;

        public RuleCategoryWidget(Text text) {
            super(null);
            this.name = text;
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            context.drawCenteredTextWithShadow(((EditGameRulesScreen)EditGameRulesScreen.this).client.textRenderer, this.name, this.getContentMiddleX(), this.getContentY() + 5, Colors.WHITE);
        }

        @Override
        public List<? extends Element> children() {
            return ImmutableList.of();
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return ImmutableList.of(new Selectable(){

                @Override
                public Selectable.SelectionType getType() {
                    return Selectable.SelectionType.HOVERED;
                }

                @Override
                public void appendNarrations(NarrationMessageBuilder builder) {
                    builder.put(NarrationPart.TITLE, RuleCategoryWidget.this.name);
                }
            });
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static abstract class AbstractRuleWidget
    extends ElementListWidget.Entry<AbstractRuleWidget> {
        final @Nullable List<OrderedText> description;

        public AbstractRuleWidget(@Nullable List<OrderedText> description) {
            this.description = description;
        }
    }
}

