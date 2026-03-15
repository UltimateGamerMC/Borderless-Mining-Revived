/*
 * External method calls:
 *   Lnet/minecraft/network/message/SignedMessage;unsignedContent()Lnet/minecraft/text/Text;
 *   Lnet/minecraft/text/Text;visit(Lnet/minecraft/text/StringVisitable$StyledVisitor;Lnet/minecraft/text/Style;)Ljava/util/Optional;
 *   Lnet/minecraft/client/gui/hud/MessageIndicator;modified(Ljava/lang/String;)Lnet/minecraft/client/gui/hud/MessageIndicator;
 *   Lnet/minecraft/client/gui/hud/MessageIndicator;notSecure()Lnet/minecraft/client/gui/hud/MessageIndicator;
 *   Lnet/minecraft/util/StringIdentifiable;createCodec(Ljava/util/function/Supplier;)Lnet/minecraft/util/StringIdentifiable$EnumCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/network/message/MessageTrustStatus;method_44743()[Lnet/minecraft/client/network/message/MessageTrustStatus;
 */
package net.minecraft.client.network.message;

import com.mojang.serialization.Codec;
import java.time.Instant;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.Style;
import net.minecraft.text.StyleSpriteSource;
import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public enum MessageTrustStatus implements StringIdentifiable
{
    SECURE("secure"),
    MODIFIED("modified"),
    NOT_SECURE("not_secure");

    public static final Codec<MessageTrustStatus> CODEC;
    private final String id;

    private MessageTrustStatus(String id) {
        this.id = id;
    }

    public static MessageTrustStatus getStatus(SignedMessage message, Text decorated, Instant receptionTimestamp) {
        if (!message.hasSignature() || message.isExpiredOnClient(receptionTimestamp)) {
            return NOT_SECURE;
        }
        if (MessageTrustStatus.isModified(message, decorated)) {
            return MODIFIED;
        }
        return SECURE;
    }

    private static boolean isModified(SignedMessage message, Text decorated) {
        if (!decorated.getString().contains(message.getSignedContent())) {
            return true;
        }
        Text lv = message.unsignedContent();
        if (lv == null) {
            return false;
        }
        return MessageTrustStatus.isNotInDefaultFont(lv);
    }

    private static boolean isNotInDefaultFont(Text content) {
        return content.visit((style, part) -> {
            if (MessageTrustStatus.isNotInDefaultFont(style)) {
                return Optional.of(true);
            }
            return Optional.empty();
        }, Style.EMPTY).orElse(false);
    }

    private static boolean isNotInDefaultFont(Style style) {
        return !style.getFont().equals(StyleSpriteSource.DEFAULT);
    }

    public boolean isInsecure() {
        return this == NOT_SECURE;
    }

    public @Nullable MessageIndicator createIndicator(SignedMessage message) {
        return switch (this.ordinal()) {
            case 1 -> MessageIndicator.modified(message.getSignedContent());
            case 2 -> MessageIndicator.notSecure();
            default -> null;
        };
    }

    @Override
    public String asString() {
        return this.id;
    }

    static {
        CODEC = StringIdentifiable.createCodec(MessageTrustStatus::values);
    }
}

