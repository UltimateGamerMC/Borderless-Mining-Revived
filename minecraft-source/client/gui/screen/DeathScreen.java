/*
 * External method calls:
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Text;literal(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget;builder(Lnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;dimensions(IIII)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;build()Lnet/minecraft/client/gui/widget/ButtonWidget;
 *   Lnet/minecraft/client/gui/screen/ConfirmScreen;disableButtons(I)V
 *   Lnet/minecraft/client/world/ClientWorld;disconnect(Lnet/minecraft/text/Text;)V
 *   Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/gui/DrawContext;IIF)V
 *   Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIII)V
 *   Lnet/minecraft/client/font/DrawnTextConsumer$Transformation;scaled(F)Lnet/minecraft/client/font/DrawnTextConsumer$Transformation;
 *   Lnet/minecraft/client/font/DrawnTextConsumer;text(Lnet/minecraft/client/font/Alignment;IILnet/minecraft/text/Text;)V
 *   Lnet/minecraft/client/gui/DrawContext;fillGradient(IIIIII)V
 *   Lnet/minecraft/text/ClickEvent$OpenUrl;uri()Ljava/net/URI;
 *   Lnet/minecraft/client/gui/screen/Screen;mouseClicked(Lnet/minecraft/client/gui/Click;Z)Z
 *   Lnet/minecraft/client/session/report/AbuseReportContext;tryShowDraftScreen(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/gui/screen/Screen;Ljava/lang/Runnable;Z)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/screen/DeathScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;
 *   Lnet/minecraft/client/gui/screen/DeathScreen;drawTitles(Lnet/minecraft/client/font/DrawnTextConsumer;)V
 *   Lnet/minecraft/client/gui/screen/DeathScreen;fillBackgroundGradient(Lnet/minecraft/client/gui/DrawContext;II)V
 *   Lnet/minecraft/client/gui/screen/DeathScreen;handleOpenUri(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/gui/screen/Screen;Ljava/net/URI;)Z
 */
package net.minecraft.client.gui.screen;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.Alignment;
import net.minecraft.client.font.DrawnTextConsumer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class DeathScreen
extends Screen {
    private static final int field_63892 = 2;
    private static final Identifier DRAFT_REPORT_ICON_TEXTURE = Identifier.ofVanilla("icon/draft_report");
    private int ticksSinceDeath;
    private final @Nullable Text message;
    private final boolean isHardcore;
    private final ClientPlayerEntity decedent;
    private final Text scoreText;
    private final List<ButtonWidget> buttons = Lists.newArrayList();
    private @Nullable ButtonWidget titleScreenButton;

    public DeathScreen(@Nullable Text message, boolean isHardcore, ClientPlayerEntity decedent) {
        super(Text.translatable(isHardcore ? "deathScreen.title.hardcore" : "deathScreen.title"));
        this.message = message;
        this.isHardcore = isHardcore;
        this.decedent = decedent;
        MutableText lv = Text.literal(Integer.toString(decedent.getScore())).formatted(Formatting.YELLOW);
        this.scoreText = Text.translatable("deathScreen.score.value", lv);
    }

    @Override
    protected void init() {
        this.ticksSinceDeath = 0;
        this.buttons.clear();
        MutableText lv = this.isHardcore ? Text.translatable("deathScreen.spectate") : Text.translatable("deathScreen.respawn");
        this.buttons.add(this.addDrawableChild(ButtonWidget.builder(lv, button -> {
            this.decedent.requestRespawn();
            button.active = false;
        }).dimensions(this.width / 2 - 100, this.height / 4 + 72, 200, 20).build()));
        this.titleScreenButton = this.addDrawableChild(ButtonWidget.builder(Text.translatable("deathScreen.titleScreen"), button -> this.client.getAbuseReportContext().tryShowDraftScreen(this.client, this, this::onTitleScreenButtonClicked, true)).dimensions(this.width / 2 - 100, this.height / 4 + 96, 200, 20).build());
        this.buttons.add(this.titleScreenButton);
        this.setButtonsActive(false);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    private void onTitleScreenButtonClicked() {
        if (this.isHardcore) {
            this.quitLevel();
            return;
        }
        TitleScreenConfirmScreen lv = new TitleScreenConfirmScreen(confirmed -> {
            if (confirmed) {
                this.quitLevel();
            } else {
                this.decedent.requestRespawn();
                this.client.setScreen(null);
            }
        }, Text.translatable("deathScreen.quit.confirm"), ScreenTexts.EMPTY, Text.translatable("deathScreen.titleScreen"), Text.translatable("deathScreen.respawn"));
        this.client.setScreen(lv);
        lv.disableButtons(20);
    }

    private void quitLevel() {
        if (this.client.world != null) {
            this.client.world.disconnect(ClientWorld.QUITTING_MULTIPLAYER_TEXT);
        }
        this.client.disconnectWithSavingScreen();
        this.client.setScreen(new TitleScreen());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        this.drawTitles(context.getTextConsumer(DrawContext.HoverType.TOOLTIP_AND_CURSOR));
        if (this.titleScreenButton != null && this.client.getAbuseReportContext().hasDraft()) {
            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, DRAFT_REPORT_ICON_TEXTURE, this.titleScreenButton.getX() + this.titleScreenButton.getWidth() - 17, this.titleScreenButton.getY() + 3, 15, 15);
        }
    }

    private void drawTitles(DrawnTextConsumer drawer) {
        DrawnTextConsumer.Transformation lv = drawer.getTransformation();
        int i = this.width / 2;
        drawer.setTransformation(lv.scaled(2.0f));
        drawer.text(Alignment.CENTER, i / 2, 30, this.title);
        drawer.setTransformation(lv);
        if (this.message != null) {
            drawer.text(Alignment.CENTER, i, 85, this.message);
        }
        drawer.text(Alignment.CENTER, i, 100, this.scoreText);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        DeathScreen.fillBackgroundGradient(context, this.width, this.height);
    }

    static void fillBackgroundGradient(DrawContext context, int width, int height) {
        context.fillGradient(0, 0, width, height, 0x60500000, -1602211792);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        ClickEvent clickEvent;
        DrawnTextConsumer.ClickHandler lv = new DrawnTextConsumer.ClickHandler(this.getTextRenderer(), (int)click.x(), (int)click.y());
        this.drawTitles(lv);
        Style lv2 = lv.getStyle();
        if (lv2 != null && (clickEvent = lv2.getClickEvent()) instanceof ClickEvent.OpenUrl) {
            ClickEvent.OpenUrl lv3 = (ClickEvent.OpenUrl)clickEvent;
            return DeathScreen.handleOpenUri(this.client, this, lv3.uri());
        }
        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean keepOpenThroughPortal() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        ++this.ticksSinceDeath;
        if (this.ticksSinceDeath == 20) {
            this.setButtonsActive(true);
        }
    }

    private void setButtonsActive(boolean active) {
        for (ButtonWidget lv : this.buttons) {
            lv.active = active;
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static class TitleScreenConfirmScreen
    extends ConfirmScreen {
        public TitleScreenConfirmScreen(BooleanConsumer booleanConsumer, Text arg, Text arg2, Text arg3, Text arg4) {
            super(booleanConsumer, arg, arg2, arg3, arg4);
        }

        @Override
        public void renderBackground(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
            DeathScreen.fillBackgroundGradient(context, this.width, this.height);
        }
    }
}

