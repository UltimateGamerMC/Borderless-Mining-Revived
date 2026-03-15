/*
 * External method calls:
 *   Lnet/minecraft/client/gui/hud/MessageIndicator;icon()Lnet/minecraft/client/gui/hud/MessageIndicator$Icon;
 *   Lnet/minecraft/client/util/ChatMessages;breakRenderedChatMessageLines(Lnet/minecraft/text/StringVisitable;ILnet/minecraft/client/font/TextRenderer;)Ljava/util/List;
 */
package net.minecraft.client.gui.hud;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.client.util.ChatMessages;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public record ChatHudLine(int creationTick, Text content, @Nullable MessageSignatureData signature, @Nullable MessageIndicator indicator) {
    private static final int PADDING = 4;

    public List<OrderedText> breakLines(TextRenderer textRenderer, int width) {
        if (this.indicator != null && this.indicator.icon() != null) {
            width -= this.indicator.icon().width + 4 + 2;
        }
        return ChatMessages.breakRenderedChatMessageLines(this.content, width, textRenderer);
    }

    @Environment(value=EnvType.CLIENT)
    public record Visible(int addedTime, OrderedText content, @Nullable MessageIndicator indicator, boolean endOfEntry) {
        public int getWidth(TextRenderer textRenderer) {
            return textRenderer.getWidth(this.content) + 4;
        }
    }
}

