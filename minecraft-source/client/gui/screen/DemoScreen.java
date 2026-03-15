/*
 * External method calls:
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget;builder(Lnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;dimensions(IIII)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;build()Lnet/minecraft/client/gui/widget/ButtonWidget;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/font/MultilineText;create(Lnet/minecraft/client/font/TextRenderer;[Lnet/minecraft/text/Text;)Lnet/minecraft/client/font/MultilineText;
 *   Lnet/minecraft/text/MutableText;withoutShadow()Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/MutableText;withColor(I)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/font/MultilineText;create(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;I)Lnet/minecraft/client/font/MultilineText;
 *   Lnet/minecraft/client/gui/screen/Screen;renderBackground(Lnet/minecraft/client/gui/DrawContext;IIF)V
 *   Lnet/minecraft/client/gui/DrawContext;drawTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIFFIIII)V
 *   Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/gui/DrawContext;IIF)V
 *   Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIIZ)V
 *   Lnet/minecraft/client/font/MultilineText;draw(Lnet/minecraft/client/font/Alignment;IIILnet/minecraft/client/font/DrawnTextConsumer;)I
 *   Lnet/minecraft/util/Util$OperatingSystem;open(Ljava/net/URI;)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/screen/DemoScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;
 *   Lnet/minecraft/client/gui/screen/DemoScreen;styleHelp(Lnet/minecraft/text/MutableText;)Lnet/minecraft/text/Text;
 */
package net.minecraft.client.gui.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.Alignment;
import net.minecraft.client.font.DrawnTextConsumer;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Urls;
import net.minecraft.util.Util;

@Environment(value=EnvType.CLIENT)
public class DemoScreen
extends Screen {
    private static final Identifier DEMO_BG = Identifier.ofVanilla("textures/gui/demo_background.png");
    private static final int DEMO_BG_WIDTH = 256;
    private static final int DEMO_BG_HEIGHT = 256;
    private static final int field_63895 = -14737633;
    private MultilineText movementText = MultilineText.EMPTY;
    private MultilineText fullWrappedText = MultilineText.EMPTY;

    public DemoScreen() {
        super(Text.translatable("demo.help.title"));
    }

    @Override
    protected void init() {
        int i = -16;
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("demo.help.buy"), button -> {
            button.active = false;
            Util.getOperatingSystem().open(Urls.BUY_JAVA);
        }).dimensions(this.width / 2 - 116, this.height / 2 + 62 + -16, 114, 20).build());
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("demo.help.later"), button -> {
            this.client.setScreen(null);
            this.client.mouse.lockCursor();
        }).dimensions(this.width / 2 + 2, this.height / 2 + 62 + -16, 114, 20).build());
        GameOptions lv = this.client.options;
        this.movementText = MultilineText.create(this.textRenderer, this.styleHelp(Text.translatable("demo.help.movementShort", lv.forwardKey.getBoundKeyLocalizedText(), lv.leftKey.getBoundKeyLocalizedText(), lv.backKey.getBoundKeyLocalizedText(), lv.rightKey.getBoundKeyLocalizedText())), this.styleHelp(Text.translatable("demo.help.movementMouse")), this.styleHelp(Text.translatable("demo.help.jump", lv.jumpKey.getBoundKeyLocalizedText())), this.styleHelp(Text.translatable("demo.help.inventory", lv.inventoryKey.getBoundKeyLocalizedText())));
        this.fullWrappedText = MultilineText.create(this.textRenderer, (Text)Text.translatable("demo.help.fullWrapped").withoutShadow().withColor(-14737633), 218);
    }

    private Text styleHelp(MutableText text) {
        return text.withoutShadow().withColor(-11579569);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.renderBackground(context, mouseX, mouseY, deltaTicks);
        int k = (this.width - 248) / 2;
        int l = (this.height - 166) / 2;
        context.drawTexture(RenderPipelines.GUI_TEXTURED, DEMO_BG, k, l, 0.0f, 0.0f, 248, 166, 256, 256);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        int k = (this.width - 248) / 2 + 10;
        int l = (this.height - 166) / 2 + 8;
        DrawnTextConsumer lv = context.getTextConsumer();
        context.drawText(this.textRenderer, this.title, k, l, -14737633, false);
        l = this.movementText.draw(Alignment.LEFT, k, l + 12, 12, lv);
        this.fullWrappedText.draw(Alignment.LEFT, k, l + 20, this.textRenderer.fontHeight, lv);
    }
}

