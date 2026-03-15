/*
 * External method calls:
 *   Lnet/minecraft/text/Text;literal(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Text;of(Ljava/net/URI;)Lnet/minecraft/text/Text;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/session/BanReason;byId(I)Lnet/minecraft/client/session/BanReason;
 *   Lnet/minecraft/text/Style;withBold(Ljava/lang/Boolean;)Lnet/minecraft/text/Style;
 *   Lnet/minecraft/text/Texts;withStyle(Lnet/minecraft/text/Text;Lnet/minecraft/text/Style;)Lnet/minecraft/text/Text;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/screen/ScreenTexts;days(J)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/screen/ScreenTexts;minutes(J)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/screen/ScreenTexts;hours(J)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/util/Util$OperatingSystem;open(Ljava/net/URI;)V
 */
package net.minecraft.client.session;

import com.mojang.authlib.minecraft.BanDetails;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.session.BanReason;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;
import net.minecraft.util.Urls;
import net.minecraft.util.Util;
import org.apache.commons.lang3.StringUtils;

@Environment(value=EnvType.CLIENT)
public class Bans {
    private static final Text TEMPORARY_TITLE = Text.translatable("gui.banned.title.temporary").formatted(Formatting.BOLD);
    private static final Text PERMANENT_TITLE = Text.translatable("gui.banned.title.permanent").formatted(Formatting.BOLD);
    public static final Text NAME_TITLE = Text.translatable("gui.banned.name.title").formatted(Formatting.BOLD);
    private static final Text SKIN_TITLE = Text.translatable("gui.banned.skin.title").formatted(Formatting.BOLD);
    private static final Text SKIN_DESCRIPTION = Text.translatable("gui.banned.skin.description", Text.of(Urls.JAVA_MODERATION));

    public static ConfirmLinkScreen createBanScreen(BooleanConsumer callback, BanDetails banDetails) {
        return new ConfirmLinkScreen(callback, Bans.getTitle(banDetails), Bans.getDescriptionText(banDetails), Urls.JAVA_MODERATION, ScreenTexts.ACKNOWLEDGE, true);
    }

    public static ConfirmLinkScreen createSkinBanScreen(Runnable onClose) {
        URI uRI = Urls.JAVA_MODERATION;
        return new ConfirmLinkScreen(confirmed -> {
            if (confirmed) {
                Util.getOperatingSystem().open(uRI);
            }
            onClose.run();
        }, SKIN_TITLE, SKIN_DESCRIPTION, uRI, ScreenTexts.ACKNOWLEDGE, true);
    }

    public static ConfirmLinkScreen createUsernameBanScreen(String username, Runnable onClose) {
        URI uRI = Urls.JAVA_MODERATION;
        return new ConfirmLinkScreen(confirmed -> {
            if (confirmed) {
                Util.getOperatingSystem().open(uRI);
            }
            onClose.run();
        }, NAME_TITLE, (Text)Text.translatable("gui.banned.name.description", Text.literal(username).formatted(Formatting.YELLOW), Text.of(Urls.JAVA_MODERATION)), uRI, ScreenTexts.ACKNOWLEDGE, true);
    }

    private static Text getTitle(BanDetails banDetails) {
        return Bans.isTemporary(banDetails) ? TEMPORARY_TITLE : PERMANENT_TITLE;
    }

    private static Text getDescriptionText(BanDetails banDetails) {
        return Text.translatable("gui.banned.description", Bans.getReasonText(banDetails), Bans.getDurationText(banDetails), Text.of(Urls.JAVA_MODERATION));
    }

    private static Text getReasonText(BanDetails banDetails) {
        String string = banDetails.reason();
        String string2 = banDetails.reasonMessage();
        if (StringUtils.isNumeric(string)) {
            int i = Integer.parseInt(string);
            BanReason lv = BanReason.byId(i);
            Text lv2 = lv != null ? Texts.withStyle(lv.getDescription(), Style.EMPTY.withBold(true)) : (string2 != null ? Text.translatable("gui.banned.description.reason_id_message", i, string2).formatted(Formatting.BOLD) : Text.translatable("gui.banned.description.reason_id", i).formatted(Formatting.BOLD));
            return Text.translatable("gui.banned.description.reason", lv2);
        }
        return Text.translatable("gui.banned.description.unknownreason");
    }

    private static Text getDurationText(BanDetails banDetails) {
        if (Bans.isTemporary(banDetails)) {
            Text lv = Bans.getTemporaryBanDurationText(banDetails);
            return Text.translatable("gui.banned.description.temporary", Text.translatable("gui.banned.description.temporary.duration", lv).formatted(Formatting.BOLD));
        }
        return Text.translatable("gui.banned.description.permanent").formatted(Formatting.BOLD);
    }

    private static Text getTemporaryBanDurationText(BanDetails banDetails) {
        Duration duration = Duration.between(Instant.now(), banDetails.expires());
        long l = duration.toHours();
        if (l > 72L) {
            return ScreenTexts.days(duration.toDays());
        }
        if (l < 1L) {
            return ScreenTexts.minutes(duration.toMinutes());
        }
        return ScreenTexts.hours(duration.toHours());
    }

    private static boolean isTemporary(BanDetails banDetails) {
        return banDetails.expires() != null;
    }
}

