/*
 * External method calls:
 *   Lnet/minecraft/client/gui/tooltip/Tooltip;of(Lnet/minecraft/text/Text;)Lnet/minecraft/client/gui/tooltip/Tooltip;
 *   Lnet/minecraft/recipe/display/SlotDisplayContexts;createParameters(Lnet/minecraft/world/World;)Lnet/minecraft/util/context/ContextParameterMap;
 *   Lnet/minecraft/client/gui/screen/recipebook/AnimatedResultButton;showResultCollection(Lnet/minecraft/client/gui/screen/recipebook/RecipeResultCollection;ZLnet/minecraft/client/gui/screen/recipebook/RecipeBookResults;Lnet/minecraft/util/context/ContextParameterMap;)V
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V
 *   Lnet/minecraft/client/gui/screen/recipebook/AnimatedResultButton;render(Lnet/minecraft/client/gui/DrawContext;IIF)V
 *   Lnet/minecraft/client/gui/widget/TexturedButtonWidget;render(Lnet/minecraft/client/gui/DrawContext;IIF)V
 *   Lnet/minecraft/client/gui/screen/recipebook/RecipeAlternativesWidget;render(Lnet/minecraft/client/gui/DrawContext;IIF)V
 *   Lnet/minecraft/client/gui/DrawContext;drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;IILnet/minecraft/util/Identifier;)V
 *   Lnet/minecraft/client/gui/screen/recipebook/RecipeAlternativesWidget;mouseClicked(Lnet/minecraft/client/gui/Click;Z)Z
 *   Lnet/minecraft/client/gui/widget/TexturedButtonWidget;mouseClicked(Lnet/minecraft/client/gui/Click;Z)Z
 *   Lnet/minecraft/client/gui/screen/recipebook/AnimatedResultButton;mouseClicked(Lnet/minecraft/client/gui/Click;Z)Z
 *   Lnet/minecraft/client/gui/screen/recipebook/RecipeAlternativesWidget;showAlternativesForResult(Lnet/minecraft/client/gui/screen/recipebook/RecipeResultCollection;Lnet/minecraft/util/context/ContextParameterMap;ZIIIIF)V
 *   Lnet/minecraft/client/gui/screen/recipebook/RecipeBookWidget;onRecipeDisplayed(Lnet/minecraft/recipe/NetworkRecipeId;)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 */
package net.minecraft.client.gui.screen.recipebook;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.recipebook.AnimatedResultButton;
import net.minecraft.client.gui.screen.recipebook.CurrentIndexProvider;
import net.minecraft.client.gui.screen.recipebook.RecipeAlternativesWidget;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.util.InputUtil;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.NetworkRecipeId;
import net.minecraft.recipe.display.SlotDisplayContexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.context.ContextParameterMap;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class RecipeBookResults {
    public static final int field_32411 = 20;
    private static final ButtonTextures PAGE_FORWARD_TEXTURES = new ButtonTextures(Identifier.ofVanilla("recipe_book/page_forward"), Identifier.ofVanilla("recipe_book/page_forward_highlighted"));
    private static final ButtonTextures PAGE_BACKWARD_TEXTURES = new ButtonTextures(Identifier.ofVanilla("recipe_book/page_backward"), Identifier.ofVanilla("recipe_book/page_backward_highlighted"));
    private static final Text NEXT_PAGE_TOOLTIP = Text.translatable("gui.recipebook.next_page");
    private static final Text PREVIOUS_PAGE_TOOLTIP = Text.translatable("gui.recipebook.previous_page");
    private static final int field_64552 = 12;
    private static final int field_64553 = 17;
    private final List<AnimatedResultButton> resultButtons = Lists.newArrayListWithCapacity(20);
    private @Nullable AnimatedResultButton hoveredResultButton;
    private final RecipeAlternativesWidget alternatesWidget;
    private MinecraftClient client;
    private final RecipeBookWidget<?> recipeBookWidget;
    private List<RecipeResultCollection> resultCollections = ImmutableList.of();
    private @Nullable TexturedButtonWidget nextPageButton;
    private @Nullable TexturedButtonWidget prevPageButton;
    private int pageCount;
    private int currentPage;
    private ClientRecipeBook recipeBook;
    private @Nullable NetworkRecipeId lastClickedRecipe;
    private @Nullable RecipeResultCollection resultCollection;
    private boolean filteringCraftable;

    public RecipeBookResults(RecipeBookWidget<?> recipeBookWidget, CurrentIndexProvider currentIndexProvider, boolean furnace) {
        this.recipeBookWidget = recipeBookWidget;
        this.alternatesWidget = new RecipeAlternativesWidget(currentIndexProvider, furnace);
        for (int i = 0; i < 20; ++i) {
            this.resultButtons.add(new AnimatedResultButton(currentIndexProvider));
        }
    }

    public void initialize(MinecraftClient client, int parentLeft, int parentTop) {
        this.client = client;
        this.recipeBook = client.player.getRecipeBook();
        for (int k = 0; k < this.resultButtons.size(); ++k) {
            this.resultButtons.get(k).setPosition(parentLeft + 11 + 25 * (k % 5), parentTop + 31 + 25 * (k / 5));
        }
        this.nextPageButton = new TexturedButtonWidget(parentLeft + 93, parentTop + 137, 12, 17, PAGE_FORWARD_TEXTURES, arg -> this.hideShowPageButtons(), NEXT_PAGE_TOOLTIP);
        this.nextPageButton.setTooltip(Tooltip.of(NEXT_PAGE_TOOLTIP));
        this.prevPageButton = new TexturedButtonWidget(parentLeft + 38, parentTop + 137, 12, 17, PAGE_BACKWARD_TEXTURES, arg -> this.hideShowPageButtons(), PREVIOUS_PAGE_TOOLTIP);
        this.prevPageButton.setTooltip(Tooltip.of(PREVIOUS_PAGE_TOOLTIP));
    }

    public void setResults(List<RecipeResultCollection> resultCollections, boolean resetCurrentPage, boolean filteringCraftable) {
        this.resultCollections = resultCollections;
        this.filteringCraftable = filteringCraftable;
        this.pageCount = (int)Math.ceil((double)resultCollections.size() / 20.0);
        if (this.pageCount <= this.currentPage || resetCurrentPage) {
            this.currentPage = 0;
        }
        this.refreshResultButtons();
    }

    private void refreshResultButtons() {
        int i = 20 * this.currentPage;
        ContextParameterMap lv = SlotDisplayContexts.createParameters(this.client.world);
        for (int j = 0; j < this.resultButtons.size(); ++j) {
            AnimatedResultButton lv2 = this.resultButtons.get(j);
            if (i + j < this.resultCollections.size()) {
                RecipeResultCollection lv3 = this.resultCollections.get(i + j);
                lv2.showResultCollection(lv3, this.filteringCraftable, this, lv);
                lv2.visible = true;
                continue;
            }
            lv2.visible = false;
        }
        this.hideShowPageButtons();
    }

    private void hideShowPageButtons() {
        if (this.nextPageButton != null) {
            boolean bl = this.nextPageButton.visible = this.pageCount > 1 && this.currentPage < this.pageCount - 1;
        }
        if (this.prevPageButton != null) {
            this.prevPageButton.visible = this.pageCount > 1 && this.currentPage > 0;
        }
    }

    public void draw(DrawContext context, int x, int y, int mouseX, int mouseY, float deltaTicks) {
        if (this.pageCount > 1) {
            MutableText lv = Text.translatable("gui.recipebook.page", this.currentPage + 1, this.pageCount);
            int m = this.client.textRenderer.getWidth(lv);
            context.drawTextWithShadow(this.client.textRenderer, lv, x - m / 2 + 73, y + 141, Colors.WHITE);
        }
        this.hoveredResultButton = null;
        for (AnimatedResultButton lv2 : this.resultButtons) {
            lv2.render(context, mouseX, mouseY, deltaTicks);
            if (!lv2.visible || !lv2.isSelected()) continue;
            this.hoveredResultButton = lv2;
        }
        if (this.nextPageButton != null) {
            this.nextPageButton.render(context, mouseX, mouseY, deltaTicks);
        }
        if (this.prevPageButton != null) {
            this.prevPageButton.render(context, mouseX, mouseY, deltaTicks);
        }
        context.createNewRootLayer();
        this.alternatesWidget.render(context, mouseX, mouseY, deltaTicks);
    }

    public void drawTooltip(DrawContext context, int x, int y) {
        if (this.client.currentScreen != null && this.hoveredResultButton != null && !this.alternatesWidget.isVisible()) {
            ItemStack lv = this.hoveredResultButton.getDisplayStack();
            Identifier lv2 = lv.get(DataComponentTypes.TOOLTIP_STYLE);
            context.drawTooltip(this.client.textRenderer, this.hoveredResultButton.getTooltip(lv), x, y, lv2);
        }
    }

    public @Nullable NetworkRecipeId getLastClickedRecipe() {
        return this.lastClickedRecipe;
    }

    public @Nullable RecipeResultCollection getLastClickedResults() {
        return this.resultCollection;
    }

    public void hideAlternates() {
        this.alternatesWidget.setVisible(false);
    }

    public boolean mouseClicked(Click click, int left, int top, int width, int height, boolean doubled) {
        this.lastClickedRecipe = null;
        this.resultCollection = null;
        if (this.alternatesWidget.isVisible()) {
            if (this.alternatesWidget.mouseClicked(click, doubled)) {
                this.lastClickedRecipe = this.alternatesWidget.getLastClickedRecipe();
                this.resultCollection = this.alternatesWidget.getResults();
            } else {
                this.alternatesWidget.setVisible(false);
            }
            return true;
        }
        if (this.nextPageButton.mouseClicked(click, doubled)) {
            ++this.currentPage;
            this.refreshResultButtons();
            return true;
        }
        if (this.prevPageButton.mouseClicked(click, doubled)) {
            --this.currentPage;
            this.refreshResultButtons();
            return true;
        }
        ContextParameterMap lv = SlotDisplayContexts.createParameters(this.client.world);
        for (AnimatedResultButton lv2 : this.resultButtons) {
            if (!lv2.mouseClicked(click, doubled)) continue;
            if (click.button() == 0) {
                this.lastClickedRecipe = lv2.getCurrentId();
                this.resultCollection = lv2.getResultCollection();
            } else if (click.button() == InputUtil.GLFW_MOUSE_BUTTON_RIGHT && !this.alternatesWidget.isVisible() && !lv2.hasSingleResult()) {
                this.alternatesWidget.showAlternativesForResult(lv2.getResultCollection(), lv, this.filteringCraftable, lv2.getX(), lv2.getY(), left + width / 2, top + 13 + height / 2, lv2.getWidth());
            }
            return true;
        }
        return false;
    }

    public void onRecipeDisplayed(NetworkRecipeId recipeId) {
        this.recipeBookWidget.onRecipeDisplayed(recipeId);
    }

    public ClientRecipeBook getRecipeBook() {
        return this.recipeBook;
    }

    protected void forEachButton(Consumer<ClickableWidget> action) {
        this.resultButtons.forEach(action);
    }
}

