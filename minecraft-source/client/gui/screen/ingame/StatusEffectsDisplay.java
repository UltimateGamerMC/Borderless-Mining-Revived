/*
 * External method calls:
 *   Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIII)V
 *   Lnet/minecraft/client/gui/widget/TextWidget;trim(Lnet/minecraft/text/Text;Lnet/minecraft/client/font/TextRenderer;I)Lnet/minecraft/text/OrderedText;
 *   Lnet/minecraft/text/Text;asOrderedText()Lnet/minecraft/text/OrderedText;
 *   Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/OrderedText;III)V
 *   Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V
 *   Lnet/minecraft/client/gui/DrawContext;drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;Ljava/util/Optional;II)V
 *   Lnet/minecraft/text/MutableText;append(Lnet/minecraft/text/Text;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/screen/ingame/StatusEffectsDisplay;drawStatusEffects(Lnet/minecraft/client/gui/DrawContext;Ljava/util/Collection;IIIII)V
 *   Lnet/minecraft/client/gui/screen/ingame/StatusEffectsDisplay;drawStatusEffectBackgrounds(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;Lnet/minecraft/text/Text;IIZI)I
 *   Lnet/minecraft/client/gui/screen/ingame/StatusEffectsDisplay;drawTexts(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/text/Text;Lnet/minecraft/text/Text;Lnet/minecraft/client/font/TextRenderer;IIIIII)V
 */
package net.minecraft.client.gui.screen.ingame;

import com.google.common.collect.Ordering;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class StatusEffectsDisplay {
    private static final Identifier BACKGROUND_TEXTURE = Identifier.ofVanilla("container/inventory/effect_background");
    private static final Identifier AMBIENT_BACKGROUND_TEXTURE = Identifier.ofVanilla("container/inventory/effect_background_ambient");
    private static final int field_63534 = 18;
    public static final int field_63530 = 7;
    private static final int field_63535 = 32;
    public static final int field_63531 = 32;
    private final HandledScreen<?> parent;
    private final MinecraftClient client;

    public StatusEffectsDisplay(HandledScreen<?> parent) {
        this.parent = parent;
        this.client = MinecraftClient.getInstance();
    }

    public boolean shouldHideStatusEffectHud() {
        int i = this.parent.x + this.parent.backgroundWidth + 2;
        int j = this.parent.width - i;
        return j >= 32;
    }

    public void render(DrawContext context, int mouseX, int mouseY) {
        int k = this.parent.x + this.parent.backgroundWidth + 2;
        int l = this.parent.width - k;
        Collection<StatusEffectInstance> collection = this.client.player.getStatusEffects();
        if (collection.isEmpty() || l < 32) {
            return;
        }
        int m = l >= 120 ? l - 7 : 32;
        int n = 33;
        if (collection.size() > 5) {
            n = 132 / (collection.size() - 1);
        }
        this.drawStatusEffects(context, collection, k, n, mouseX, mouseY, m);
    }

    private void drawStatusEffects(DrawContext context, Collection<StatusEffectInstance> effects, int x, int height, int mouseX, int mouseY, int width) {
        List<StatusEffectInstance> iterable = Ordering.natural().sortedCopy(effects);
        int n = this.parent.y;
        TextRenderer lv = this.parent.getTextRenderer();
        for (StatusEffectInstance lv2 : iterable) {
            boolean bl = lv2.isAmbient();
            Text lv3 = this.getStatusEffectDescription(lv2);
            Text lv4 = StatusEffectUtil.getDurationText(lv2, 1.0f, this.client.world.getTickManager().getTickRate());
            int o = this.drawStatusEffectBackgrounds(context, lv, lv3, lv4, x, n, bl, width);
            this.drawTexts(context, lv3, lv4, lv, x, n, o, height, mouseX, mouseY);
            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, InGameHud.getEffectTexture(lv2.getEffectType()), x + 7, n + 7, 18, 18);
            n += height;
        }
    }

    private int drawStatusEffectBackgrounds(DrawContext context, TextRenderer textRenderer, Text description, Text duration, int x, int y, boolean ambient, int width) {
        int l = 32 + textRenderer.getWidth(description) + 7;
        int m = 32 + textRenderer.getWidth(duration) + 7;
        int n = Math.min(width, Math.max(l, m));
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, ambient ? AMBIENT_BACKGROUND_TEXTURE : BACKGROUND_TEXTURE, x, y, n, 32);
        return n;
    }

    private void drawTexts(DrawContext context, Text description, Text duration, TextRenderer textRenderer, int x, int y, int width, int height, int mouseX, int mouseY) {
        boolean bl2;
        int o = x + 32;
        int p = y + 7;
        int q = width - 32 - 7;
        if (q > 0) {
            boolean bl = textRenderer.getWidth(description) > q;
            OrderedText lv = bl ? TextWidget.trim(description, textRenderer, q) : description.asOrderedText();
            context.drawTextWithShadow(textRenderer, lv, o, p, -1);
            context.drawTextWithShadow(textRenderer, duration, o, p + textRenderer.fontHeight, Colors.GRAY);
            bl2 = bl;
        } else {
            bl2 = true;
        }
        if (bl2 && mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
            context.drawTooltip(this.parent.getTextRenderer(), List.of(description, duration), Optional.empty(), mouseX, mouseY);
        }
    }

    private Text getStatusEffectDescription(StatusEffectInstance statusEffect) {
        MutableText lv = statusEffect.getEffectType().value().getName().copy();
        if (statusEffect.getAmplifier() >= 1 && statusEffect.getAmplifier() <= 9) {
            lv.append(ScreenTexts.SPACE).append(Text.translatable("enchantment.level." + (statusEffect.getAmplifier() + 1)));
        }
        return lv;
    }
}

