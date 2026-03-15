/*
 * External method calls:
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/gui/widget/CyclingButtonWidget;builder(Ljava/util/function/Function;Ljava/lang/Object;)Lnet/minecraft/client/gui/widget/CyclingButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/CyclingButtonWidget$Builder;values([Ljava/lang/Object;)Lnet/minecraft/client/gui/widget/CyclingButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/CyclingButtonWidget$Builder;build(IIIILnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/CyclingButtonWidget$UpdateCallback;)Lnet/minecraft/client/gui/widget/CyclingButtonWidget;
 *   Lnet/minecraft/client/gui/widget/CyclingButtonWidget;onOffBuilder(Z)Lnet/minecraft/client/gui/widget/CyclingButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget;builder(Lnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;dimensions(IIII)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;build()Lnet/minecraft/client/gui/widget/ButtonWidget;
 *   Lnet/minecraft/text/Text;literal(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/gui/DrawContext;IIF)V
 *   Lnet/minecraft/client/gui/DrawContext;drawCenteredTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V
 *   Lnet/minecraft/client/gui/tooltip/Tooltip;of(Lnet/minecraft/text/Text;)Lnet/minecraft/client/gui/tooltip/Tooltip;
 *   Lnet/minecraft/server/integrated/IntegratedServer;openToLan(Lnet/minecraft/world/GameMode;ZI)Z
 *   Lnet/minecraft/client/gui/hud/ChatHud;addMessage(Lnet/minecraft/text/Text;)V
 *   Lnet/minecraft/client/util/NarratorManager;narrateSystemMessage(Lnet/minecraft/text/Text;)V
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/screen/OpenToLanScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;
 *   Lnet/minecraft/client/gui/screen/OpenToLanScreen;updatePort(Ljava/lang/String;)Lnet/minecraft/text/Text;
 */
package net.minecraft.client.gui.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.command.PublishCommand;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.NetworkUtils;
import net.minecraft.world.GameMode;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class OpenToLanScreen
extends Screen {
    private static final int MIN_PORT = 1024;
    private static final int MAX_PORT = 65535;
    private static final Text ALLOW_COMMANDS_TEXT = Text.translatable("selectWorld.allowCommands");
    private static final Text GAME_MODE_TEXT = Text.translatable("selectWorld.gameMode");
    private static final Text OTHER_PLAYERS_TEXT = Text.translatable("lanServer.otherPlayers");
    private static final Text PORT_TEXT = Text.translatable("lanServer.port");
    private static final Text UNAVAILABLE_PORT_TEXT = Text.translatable("lanServer.port.unavailable", 1024, 65535);
    private static final Text INVALID_PORT_TEXT = Text.translatable("lanServer.port.invalid", 1024, 65535);
    private final Screen parent;
    private GameMode gameMode = GameMode.SURVIVAL;
    private boolean allowCommands;
    private int port = NetworkUtils.findLocalPort();
    private @Nullable TextFieldWidget portField;

    public OpenToLanScreen(Screen screen) {
        super(Text.translatable("lanServer.title"));
        this.parent = screen;
    }

    @Override
    protected void init() {
        IntegratedServer lv = this.client.getServer();
        this.gameMode = lv.getDefaultGameMode();
        this.allowCommands = lv.getSaveProperties().areCommandsAllowed();
        this.addDrawableChild(CyclingButtonWidget.builder(GameMode::getSimpleTranslatableName, this.gameMode).values((GameMode[])new GameMode[]{GameMode.SURVIVAL, GameMode.SPECTATOR, GameMode.CREATIVE, GameMode.ADVENTURE}).build(this.width / 2 - 155, 100, 150, 20, GAME_MODE_TEXT, (button, gameMode) -> {
            this.gameMode = gameMode;
        }));
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(this.allowCommands).build(this.width / 2 + 5, 100, 150, 20, ALLOW_COMMANDS_TEXT, (button, allowCommands) -> {
            this.allowCommands = allowCommands;
        }));
        ButtonWidget lv2 = ButtonWidget.builder(Text.translatable("lanServer.start"), button -> {
            this.client.setScreen(null);
            MutableText lv = lv.openToLan(this.gameMode, this.allowCommands, this.port) ? PublishCommand.getStartedText(this.port) : Text.translatable("commands.publish.failed");
            this.client.inGameHud.getChatHud().addMessage(lv);
            this.client.getNarratorManager().narrateSystemMessage(lv);
            this.client.updateWindowTitle();
        }).dimensions(this.width / 2 - 155, this.height - 28, 150, 20).build();
        this.portField = new TextFieldWidget(this.textRenderer, this.width / 2 - 75, 160, 150, 20, Text.translatable("lanServer.port"));
        this.portField.setChangedListener(portText -> {
            Text lv = this.updatePort((String)portText);
            this.portField.setPlaceholder(Text.literal("" + this.port));
            if (lv == null) {
                this.portField.setEditableColor(-2039584);
                this.portField.setTooltip(null);
                arg.active = true;
            } else {
                this.portField.setEditableColor(-2142128);
                this.portField.setTooltip(Tooltip.of(lv));
                arg.active = false;
            }
        });
        this.portField.setPlaceholder(Text.literal("" + this.port));
        this.addDrawableChild(this.portField);
        this.addDrawableChild(lv2);
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.close()).dimensions(this.width / 2 + 5, this.height - 28, 150, 20).build());
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }

    private @Nullable Text updatePort(String portText) {
        if (portText.isBlank()) {
            this.port = NetworkUtils.findLocalPort();
            return null;
        }
        try {
            this.port = Integer.parseInt(portText);
            if (this.port < 1024 || this.port > 65535) {
                return INVALID_PORT_TEXT;
            }
            if (!NetworkUtils.isPortAvailable(this.port)) {
                return UNAVAILABLE_PORT_TEXT;
            }
            return null;
        } catch (NumberFormatException numberFormatException) {
            this.port = NetworkUtils.findLocalPort();
            return INVALID_PORT_TEXT;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 50, Colors.WHITE);
        context.drawCenteredTextWithShadow(this.textRenderer, OTHER_PLAYERS_TEXT, this.width / 2, 82, Colors.WHITE);
        context.drawCenteredTextWithShadow(this.textRenderer, PORT_TEXT, this.width / 2, 142, Colors.WHITE);
    }
}

