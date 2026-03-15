/*
 * External method calls:
 *   Lnet/minecraft/client/gui/screen/recipebook/RecipeBookWidget$Tab;category()Lnet/minecraft/recipe/book/RecipeBookGroup;
 *   Lnet/minecraft/client/gui/screen/recipebook/RecipeResultCollection;filter(Lnet/minecraft/client/gui/screen/recipebook/RecipeResultCollection$RecipeFilterMode;)Ljava/util/List;
 *   Lnet/minecraft/recipe/RecipeDisplayEntry;id()Lnet/minecraft/recipe/NetworkRecipeId;
 *   Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIII)V
 *   Lnet/minecraft/client/gui/screen/recipebook/RecipeBookWidget$Tab;secondaryIcon()Ljava/util/Optional;
 *   Lnet/minecraft/client/gui/screen/recipebook/RecipeBookWidget$Tab;primaryIcon()Lnet/minecraft/item/ItemStack;
 *   Lnet/minecraft/client/gui/DrawContext;drawItemWithoutEntity(Lnet/minecraft/item/ItemStack;II)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/screen/recipebook/RecipeGroupButtonWidget;renderIcons(Lnet/minecraft/client/gui/DrawContext;)V
 */
package net.minecraft.client.gui.screen.recipebook;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.recipe.RecipeDisplayEntry;
import net.minecraft.recipe.book.RecipeBookGroup;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class RecipeGroupButtonWidget
extends TexturedButtonWidget {
    private static final ButtonTextures TEXTURES = new ButtonTextures(Identifier.ofVanilla("recipe_book/tab"), Identifier.ofVanilla("recipe_book/tab_selected"));
    public static final int field_64554 = 35;
    public static final int field_64555 = 27;
    private final RecipeBookWidget.Tab tab;
    private static final float field_32412 = 15.0f;
    private float bounce;
    private boolean groupFocused = false;

    public RecipeGroupButtonWidget(int x, int y, RecipeBookWidget.Tab tab, ButtonWidget.PressAction onPress) {
        super(x, y, 35, 27, TEXTURES, onPress);
        this.tab = tab;
    }

    public void checkForNewRecipes(ClientRecipeBook recipeBook, boolean filteringCraftable) {
        RecipeResultCollection.RecipeFilterMode lv = filteringCraftable ? RecipeResultCollection.RecipeFilterMode.CRAFTABLE : RecipeResultCollection.RecipeFilterMode.ANY;
        List<RecipeResultCollection> list = recipeBook.getResultsForCategory(this.tab.category());
        for (RecipeResultCollection lv2 : list) {
            for (RecipeDisplayEntry lv3 : lv2.filter(lv)) {
                if (!recipeBook.isHighlighted(lv3.id())) continue;
                this.bounce = 15.0f;
                return;
            }
        }
    }

    @Override
    public void drawIcon(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        if (this.bounce > 0.0f) {
            float g = 1.0f + 0.1f * (float)Math.sin(this.bounce / 15.0f * (float)Math.PI);
            context.getMatrices().pushMatrix();
            context.getMatrices().translate(this.getX() + 8, this.getY() + 12);
            context.getMatrices().scale(1.0f, g);
            context.getMatrices().translate(-(this.getX() + 8), -(this.getY() + 12));
        }
        Identifier lv = this.textures.get(true, this.groupFocused);
        int k = this.getX();
        if (this.groupFocused) {
            k -= 2;
        }
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, lv, k, this.getY(), this.width, this.height);
        this.renderIcons(context);
        if (this.bounce > 0.0f) {
            context.getMatrices().popMatrix();
            this.bounce -= deltaTicks;
        }
    }

    @Override
    protected void setCursor(DrawContext context) {
        if (!this.groupFocused) {
            super.setCursor(context);
        }
    }

    private void renderIcons(DrawContext context) {
        int i;
        int n = i = this.groupFocused ? -2 : 0;
        if (this.tab.secondaryIcon().isPresent()) {
            context.drawItemWithoutEntity(this.tab.primaryIcon(), this.getX() + 3 + i, this.getY() + 5);
            context.drawItemWithoutEntity(this.tab.secondaryIcon().get(), this.getX() + 14 + i, this.getY() + 5);
        } else {
            context.drawItemWithoutEntity(this.tab.primaryIcon(), this.getX() + 9 + i, this.getY() + 5);
        }
    }

    public RecipeBookGroup getCategory() {
        return this.tab.category();
    }

    public boolean hasKnownRecipes(ClientRecipeBook recipeBook) {
        List<RecipeResultCollection> list = recipeBook.getResultsForCategory(this.tab.category());
        this.visible = false;
        for (RecipeResultCollection lv : list) {
            if (!lv.hasDisplayableRecipes()) continue;
            this.visible = true;
            break;
        }
        return this.visible;
    }

    public void focus() {
        this.groupFocused = true;
    }

    public void unfocus() {
        this.groupFocused = false;
    }
}

