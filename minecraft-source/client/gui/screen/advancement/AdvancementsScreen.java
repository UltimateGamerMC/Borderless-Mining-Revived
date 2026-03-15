/*
 * External method calls:
 *   Lnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;addHeader(Lnet/minecraft/text/Text;Lnet/minecraft/client/font/TextRenderer;)V
 *   Lnet/minecraft/client/network/ClientAdvancementManager;selectTab(Lnet/minecraft/advancement/AdvancementEntry;Z)V
 *   Lnet/minecraft/client/gui/widget/ButtonWidget;builder(Lnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;width(I)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;build()Lnet/minecraft/client/gui/widget/ButtonWidget;
 *   Lnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;addFooter(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;
 *   Lnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;forEachChild(Ljava/util/function/Consumer;)V
 *   Lnet/minecraft/network/packet/c2s/play/AdvancementTabC2SPacket;close()Lnet/minecraft/network/packet/c2s/play/AdvancementTabC2SPacket;
 *   Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V
 *   Lnet/minecraft/client/gui/screen/Screen;mouseClicked(Lnet/minecraft/client/gui/Click;Z)Z
 *   Lnet/minecraft/client/option/KeyBinding;matchesKey(Lnet/minecraft/client/input/KeyInput;)Z
 *   Lnet/minecraft/client/gui/screen/Screen;keyPressed(Lnet/minecraft/client/input/KeyInput;)Z
 *   Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/gui/DrawContext;IIF)V
 *   Lnet/minecraft/client/gui/screen/advancement/AdvancementTab;move(DD)V
 *   Lnet/minecraft/client/gui/screen/Screen;mouseReleased(Lnet/minecraft/client/gui/Click;)Z
 *   Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V
 *   Lnet/minecraft/client/gui/DrawContext;drawCenteredTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V
 *   Lnet/minecraft/client/gui/screen/advancement/AdvancementTab;render(Lnet/minecraft/client/gui/DrawContext;II)V
 *   Lnet/minecraft/client/gui/DrawContext;drawTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIFFIIII)V
 *   Lnet/minecraft/client/gui/screen/advancement/AdvancementTab;drawBackground(Lnet/minecraft/client/gui/DrawContext;IIIIZ)V
 *   Lnet/minecraft/client/gui/screen/advancement/AdvancementTab;drawIcon(Lnet/minecraft/client/gui/DrawContext;II)V
 *   Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIIZ)V
 *   Lnet/minecraft/client/gui/screen/advancement/AdvancementTab;drawWidgetTooltip(Lnet/minecraft/client/gui/DrawContext;IIII)V
 *   Lnet/minecraft/client/gui/DrawContext;drawTooltip(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;II)V
 *   Lnet/minecraft/client/gui/screen/advancement/AdvancementTab;create(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/gui/screen/advancement/AdvancementsScreen;ILnet/minecraft/advancement/PlacedAdvancement;)Lnet/minecraft/client/gui/screen/advancement/AdvancementTab;
 *   Lnet/minecraft/client/gui/screen/advancement/AdvancementTab;addAdvancement(Lnet/minecraft/advancement/PlacedAdvancement;)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/screen/advancement/AdvancementsScreen;drawAdvancementTree(Lnet/minecraft/client/gui/DrawContext;II)V
 *   Lnet/minecraft/client/gui/screen/advancement/AdvancementsScreen;drawWindow(Lnet/minecraft/client/gui/DrawContext;IIII)V
 *   Lnet/minecraft/client/gui/screen/advancement/AdvancementsScreen;drawWidgetTooltip(Lnet/minecraft/client/gui/DrawContext;IIII)V
 *   Lnet/minecraft/client/gui/screen/advancement/AdvancementsScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;
 */
package net.minecraft.client.gui.screen.advancement;

import com.google.common.collect.Maps;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.advancement.PlacedAdvancement;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.cursor.StandardCursors;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.advancement.AdvancementTab;
import net.minecraft.client.gui.screen.advancement.AdvancementWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.network.ClientAdvancementManager;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.c2s.play.AdvancementTabC2SPacket;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class AdvancementsScreen
extends Screen
implements ClientAdvancementManager.Listener {
    private static final Identifier WINDOW_TEXTURE = Identifier.ofVanilla("textures/gui/advancements/window.png");
    public static final int WINDOW_WIDTH = 252;
    public static final int WINDOW_HEIGHT = 140;
    private static final int PAGE_OFFSET_X = 9;
    private static final int PAGE_OFFSET_Y = 18;
    public static final int PAGE_WIDTH = 234;
    public static final int PAGE_HEIGHT = 113;
    private static final int TITLE_OFFSET_X = 8;
    private static final int TITLE_OFFSET_Y = 6;
    private static final int field_52799 = 256;
    private static final int field_52800 = 256;
    public static final int field_32302 = 16;
    public static final int field_32303 = 16;
    public static final int field_32304 = 14;
    public static final int field_32305 = 7;
    private static final double field_45431 = 16.0;
    private static final Text SAD_LABEL_TEXT = Text.translatable("advancements.sad_label");
    private static final Text EMPTY_TEXT = Text.translatable("advancements.empty");
    private static final Text ADVANCEMENTS_TEXT = Text.translatable("gui.advancements");
    private final ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);
    private final @Nullable Screen parent;
    private final ClientAdvancementManager advancementHandler;
    private final Map<AdvancementEntry, AdvancementTab> tabs = Maps.newLinkedHashMap();
    private @Nullable AdvancementTab selectedTab;
    private boolean movingTab;

    public AdvancementsScreen(ClientAdvancementManager advancementHandler) {
        this(advancementHandler, null);
    }

    public AdvancementsScreen(ClientAdvancementManager advancementHandler, @Nullable Screen parent) {
        super(ADVANCEMENTS_TEXT);
        this.advancementHandler = advancementHandler;
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.layout.addHeader(ADVANCEMENTS_TEXT, this.textRenderer);
        this.tabs.clear();
        this.selectedTab = null;
        this.advancementHandler.setListener(this);
        if (this.selectedTab == null && !this.tabs.isEmpty()) {
            AdvancementTab lv = this.tabs.values().iterator().next();
            this.advancementHandler.selectTab(lv.getRoot().getAdvancementEntry(), true);
        } else {
            this.advancementHandler.selectTab(this.selectedTab == null ? null : this.selectedTab.getRoot().getAdvancementEntry(), true);
        }
        this.layout.addFooter(ButtonWidget.builder(ScreenTexts.DONE, button -> this.close()).width(200).build());
        this.layout.forEachChild(child -> {
            ClickableWidget cfr_ignored_0 = (ClickableWidget)this.addDrawableChild(child);
        });
        this.refreshWidgetPositions();
    }

    @Override
    protected void refreshWidgetPositions() {
        this.layout.refreshPositions();
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }

    @Override
    public void removed() {
        this.advancementHandler.setListener(null);
        ClientPlayNetworkHandler lv = this.client.getNetworkHandler();
        if (lv != null) {
            lv.sendPacket(AdvancementTabC2SPacket.close());
        }
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (click.button() == 0) {
            int i = (this.width - 252) / 2;
            int j = (this.height - 140) / 2;
            for (AdvancementTab lv : this.tabs.values()) {
                if (!lv.isClickOnTab(i, j, click.x(), click.y())) continue;
                this.advancementHandler.selectTab(lv.getRoot().getAdvancementEntry(), true);
                break;
            }
        }
        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if (this.client.options.advancementsKey.matchesKey(input)) {
            this.client.setScreen(null);
            this.client.mouse.lockCursor();
            return true;
        }
        return super.keyPressed(input);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        int k = (this.width - 252) / 2;
        int l = (this.height - 140) / 2;
        context.createNewRootLayer();
        this.drawAdvancementTree(context, k, l);
        context.createNewRootLayer();
        this.drawWindow(context, k, l, mouseX, mouseY);
        if (this.movingTab && this.selectedTab != null) {
            if (this.selectedTab.canScrollHorizontally() && this.selectedTab.canScrollVertically()) {
                context.setCursor(StandardCursors.RESIZE_ALL);
            } else if (this.selectedTab.canScrollHorizontally()) {
                context.setCursor(StandardCursors.RESIZE_EW);
            } else if (this.selectedTab.canScrollVertically()) {
                context.setCursor(StandardCursors.RESIZE_NS);
            }
        }
        this.drawWidgetTooltip(context, mouseX, mouseY, k, l);
    }

    @Override
    public boolean mouseDragged(Click click, double offsetX, double offsetY) {
        if (click.button() != 0) {
            this.movingTab = false;
            return false;
        }
        if (!this.movingTab) {
            this.movingTab = true;
        } else if (this.selectedTab != null) {
            this.selectedTab.move(offsetX, offsetY);
        }
        return true;
    }

    @Override
    public boolean mouseReleased(Click click) {
        this.movingTab = false;
        return super.mouseReleased(click);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.selectedTab != null) {
            this.selectedTab.move(horizontalAmount * 16.0, verticalAmount * 16.0);
            return true;
        }
        return false;
    }

    private void drawAdvancementTree(DrawContext context, int x, int y) {
        AdvancementTab lv = this.selectedTab;
        if (lv == null) {
            context.fill(x + 9, y + 18, x + 9 + 234, y + 18 + 113, Colors.BLACK);
            int k = x + 9 + 117;
            context.drawCenteredTextWithShadow(this.textRenderer, EMPTY_TEXT, k, y + 18 + 56 - this.textRenderer.fontHeight / 2, Colors.WHITE);
            context.drawCenteredTextWithShadow(this.textRenderer, SAD_LABEL_TEXT, k, y + 18 + 113 - this.textRenderer.fontHeight, Colors.WHITE);
            return;
        }
        lv.render(context, x + 9, y + 18);
    }

    public void drawWindow(DrawContext context, int x, int y, int mouseX, int mouseY) {
        context.drawTexture(RenderPipelines.GUI_TEXTURED, WINDOW_TEXTURE, x, y, 0.0f, 0.0f, 252, 140, 256, 256);
        if (this.tabs.size() > 1) {
            for (AdvancementTab lv : this.tabs.values()) {
                lv.drawBackground(context, x, y, mouseX, mouseY, lv == this.selectedTab);
            }
            for (AdvancementTab lv : this.tabs.values()) {
                lv.drawIcon(context, x, y);
            }
        }
        context.drawText(this.textRenderer, this.selectedTab != null ? this.selectedTab.getTitle() : ADVANCEMENTS_TEXT, x + 8, y + 6, Colors.DARK_GRAY, false);
    }

    private void drawWidgetTooltip(DrawContext context, int mouseX, int mouseY, int x, int y) {
        if (this.selectedTab != null) {
            context.getMatrices().pushMatrix();
            context.getMatrices().translate(x + 9, y + 18);
            context.createNewRootLayer();
            this.selectedTab.drawWidgetTooltip(context, mouseX - x - 9, mouseY - y - 18, x, y);
            context.getMatrices().popMatrix();
        }
        if (this.tabs.size() > 1) {
            for (AdvancementTab lv : this.tabs.values()) {
                if (!lv.isClickOnTab(x, y, mouseX, mouseY)) continue;
                context.drawTooltip(this.textRenderer, lv.getTitle(), mouseX, mouseY);
            }
        }
    }

    @Override
    public void onRootAdded(PlacedAdvancement root) {
        AdvancementTab lv = AdvancementTab.create(this.client, this, this.tabs.size(), root);
        if (lv == null) {
            return;
        }
        this.tabs.put(root.getAdvancementEntry(), lv);
    }

    @Override
    public void onRootRemoved(PlacedAdvancement root) {
    }

    @Override
    public void onDependentAdded(PlacedAdvancement dependent) {
        AdvancementTab lv = this.getTab(dependent);
        if (lv != null) {
            lv.addAdvancement(dependent);
        }
    }

    @Override
    public void onDependentRemoved(PlacedAdvancement dependent) {
    }

    @Override
    public void setProgress(PlacedAdvancement advancement, AdvancementProgress progress) {
        AdvancementWidget lv = this.getAdvancementWidget(advancement);
        if (lv != null) {
            lv.setProgress(progress);
        }
    }

    @Override
    public void selectTab(@Nullable AdvancementEntry advancement) {
        this.selectedTab = this.tabs.get(advancement);
    }

    @Override
    public void onClear() {
        this.tabs.clear();
        this.selectedTab = null;
    }

    public @Nullable AdvancementWidget getAdvancementWidget(PlacedAdvancement advancement) {
        AdvancementTab lv = this.getTab(advancement);
        return lv == null ? null : lv.getWidget(advancement.getAdvancementEntry());
    }

    private @Nullable AdvancementTab getTab(PlacedAdvancement advancement) {
        PlacedAdvancement lv = advancement.getRoot();
        return this.tabs.get(lv.getAdvancementEntry());
    }
}

