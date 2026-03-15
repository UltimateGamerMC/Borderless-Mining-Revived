/*
 * External method calls:
 *   Lnet/minecraft/client/gui/screen/advancement/AdvancementTabType;drawBackground(Lnet/minecraft/client/gui/DrawContext;IIZI)V
 *   Lnet/minecraft/client/gui/screen/advancement/AdvancementTabType;drawIcon(Lnet/minecraft/client/gui/DrawContext;IIILnet/minecraft/item/ItemStack;)V
 *   Lnet/minecraft/client/gui/DrawContext;enableScissor(IIII)V
 *   Lnet/minecraft/client/gui/DrawContext;drawTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIFFIIII)V
 *   Lnet/minecraft/client/gui/screen/advancement/AdvancementWidget;renderLines(Lnet/minecraft/client/gui/DrawContext;IIZ)V
 *   Lnet/minecraft/client/gui/screen/advancement/AdvancementWidget;renderWidgets(Lnet/minecraft/client/gui/DrawContext;II)V
 *   Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V
 *   Lnet/minecraft/client/gui/screen/advancement/AdvancementWidget;drawTooltip(Lnet/minecraft/client/gui/DrawContext;IIFII)V
 *   Lnet/minecraft/advancement/Advancement;display()Ljava/util/Optional;
 *   Lnet/minecraft/client/gui/screen/advancement/AdvancementTabType;values()[Lnet/minecraft/client/gui/screen/advancement/AdvancementTabType;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/screen/advancement/AdvancementTab;addWidget(Lnet/minecraft/client/gui/screen/advancement/AdvancementWidget;Lnet/minecraft/advancement/AdvancementEntry;)V
 */
package net.minecraft.client.gui.screen.advancement;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.PlacedAdvancement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.cursor.StandardCursors;
import net.minecraft.client.gui.screen.advancement.AdvancementTabType;
import net.minecraft.client.gui.screen.advancement.AdvancementWidget;
import net.minecraft.client.gui.screen.advancement.AdvancementsScreen;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.AssetInfo;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class AdvancementTab {
    private final MinecraftClient client;
    private final AdvancementsScreen screen;
    private final AdvancementTabType type;
    private final int index;
    private final PlacedAdvancement root;
    private final AdvancementDisplay display;
    private final ItemStack icon;
    private final Text title;
    private final AdvancementWidget rootWidget;
    private final Map<AdvancementEntry, AdvancementWidget> widgets = Maps.newLinkedHashMap();
    private double originX;
    private double originY;
    private int minPanX = Integer.MAX_VALUE;
    private int minPanY = Integer.MAX_VALUE;
    private int maxPanX = Integer.MIN_VALUE;
    private int maxPanY = Integer.MIN_VALUE;
    private float alpha;
    private boolean initialized;

    public AdvancementTab(MinecraftClient client, AdvancementsScreen screen, AdvancementTabType type, int index, PlacedAdvancement root, AdvancementDisplay display) {
        this.client = client;
        this.screen = screen;
        this.type = type;
        this.index = index;
        this.root = root;
        this.display = display;
        this.icon = display.getIcon();
        this.title = display.getTitle();
        this.rootWidget = new AdvancementWidget(this, client, root, display);
        this.addWidget(this.rootWidget, root.getAdvancementEntry());
    }

    public AdvancementTabType getType() {
        return this.type;
    }

    public int getIndex() {
        return this.index;
    }

    public PlacedAdvancement getRoot() {
        return this.root;
    }

    public Text getTitle() {
        return this.title;
    }

    public AdvancementDisplay getDisplay() {
        return this.display;
    }

    public void drawBackground(DrawContext context, int x, int y, int mouseX, int mouseY, boolean selected) {
        int m = x + this.type.getTabX(this.index);
        int n = y + this.type.getTabY(this.index);
        this.type.drawBackground(context, m, n, selected, this.index);
        if (!selected && mouseX > m && mouseY > n && mouseX < m + this.type.getWidth() && mouseY < n + this.type.getHeight()) {
            context.setCursor(StandardCursors.POINTING_HAND);
        }
    }

    public void drawIcon(DrawContext context, int x, int y) {
        this.type.drawIcon(context, x, y, this.index, this.icon);
    }

    public void render(DrawContext context, int x, int y) {
        if (!this.initialized) {
            this.originX = 117 - (this.maxPanX + this.minPanX) / 2;
            this.originY = 56 - (this.maxPanY + this.minPanY) / 2;
            this.initialized = true;
        }
        context.enableScissor(x, y, x + 234, y + 113);
        context.getMatrices().pushMatrix();
        context.getMatrices().translate(x, y);
        Identifier lv = this.display.getBackground().map(AssetInfo.TextureAssetInfo::texturePath).orElse(TextureManager.MISSING_IDENTIFIER);
        int k = MathHelper.floor(this.originX);
        int l = MathHelper.floor(this.originY);
        int m = k % 16;
        int n = l % 16;
        for (int o = -1; o <= 15; ++o) {
            for (int p = -1; p <= 8; ++p) {
                context.drawTexture(RenderPipelines.GUI_TEXTURED, lv, m + 16 * o, n + 16 * p, 0.0f, 0.0f, 16, 16, 16, 16);
            }
        }
        this.rootWidget.renderLines(context, k, l, true);
        this.rootWidget.renderLines(context, k, l, false);
        this.rootWidget.renderWidgets(context, k, l);
        context.getMatrices().popMatrix();
        context.disableScissor();
    }

    public void drawWidgetTooltip(DrawContext context, int mouseX, int mouseY, int x, int y) {
        context.fill(0, 0, 234, 113, MathHelper.floor(this.alpha * 255.0f) << 24);
        boolean bl = false;
        int m = MathHelper.floor(this.originX);
        int n = MathHelper.floor(this.originY);
        if (mouseX > 0 && mouseX < 234 && mouseY > 0 && mouseY < 113) {
            for (AdvancementWidget lv : this.widgets.values()) {
                if (!lv.shouldRender(m, n, mouseX, mouseY)) continue;
                bl = true;
                lv.drawTooltip(context, m, n, this.alpha, x, y);
                break;
            }
        }
        this.alpha = bl ? MathHelper.clamp(this.alpha + 0.02f, 0.0f, 0.3f) : MathHelper.clamp(this.alpha - 0.04f, 0.0f, 1.0f);
    }

    public boolean isClickOnTab(int screenX, int screenY, double mouseX, double mouseY) {
        return this.type.isClickOnTab(screenX, screenY, this.index, mouseX, mouseY);
    }

    public static @Nullable AdvancementTab create(MinecraftClient client, AdvancementsScreen screen, int index, PlacedAdvancement root) {
        Optional<AdvancementDisplay> optional = root.getAdvancement().display();
        if (optional.isEmpty()) {
            return null;
        }
        for (AdvancementTabType lv : AdvancementTabType.values()) {
            if (index >= lv.getTabCount()) {
                index -= lv.getTabCount();
                continue;
            }
            return new AdvancementTab(client, screen, lv, index, root, optional.get());
        }
        return null;
    }

    public void move(double offsetX, double offsetY) {
        if (this.canScrollHorizontally()) {
            this.originX = MathHelper.clamp(this.originX + offsetX, (double)(-(this.maxPanX - 234)), 0.0);
        }
        if (this.canScrollVertically()) {
            this.originY = MathHelper.clamp(this.originY + offsetY, (double)(-(this.maxPanY - 113)), 0.0);
        }
    }

    public boolean canScrollHorizontally() {
        return this.maxPanX - this.minPanX > 234;
    }

    public boolean canScrollVertically() {
        return this.maxPanY - this.minPanY > 113;
    }

    public void addAdvancement(PlacedAdvancement advancement) {
        Optional<AdvancementDisplay> optional = advancement.getAdvancement().display();
        if (optional.isEmpty()) {
            return;
        }
        AdvancementWidget lv = new AdvancementWidget(this, this.client, advancement, optional.get());
        this.addWidget(lv, advancement.getAdvancementEntry());
    }

    private void addWidget(AdvancementWidget widget, AdvancementEntry advancement) {
        this.widgets.put(advancement, widget);
        int i = widget.getX();
        int j = i + 28;
        int k = widget.getY();
        int l = k + 27;
        this.minPanX = Math.min(this.minPanX, i);
        this.maxPanX = Math.max(this.maxPanX, j);
        this.minPanY = Math.min(this.minPanY, k);
        this.maxPanY = Math.max(this.maxPanY, l);
        for (AdvancementWidget lv : this.widgets.values()) {
            lv.addToTree();
        }
    }

    public @Nullable AdvancementWidget getWidget(AdvancementEntry advancement) {
        return this.widgets.get(advancement);
    }

    public AdvancementsScreen getScreen() {
        return this.screen;
    }
}

