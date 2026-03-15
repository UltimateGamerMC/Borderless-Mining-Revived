/*
 * External method calls:
 *   Lnet/minecraft/client/gui/widget/GridWidget;createAdder(I)Lnet/minecraft/client/gui/widget/GridWidget$Adder;
 *   Lnet/minecraft/client/gui/widget/EmptyWidget;ofHeight(I)Lnet/minecraft/client/gui/widget/EmptyWidget;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget;builder(Lnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;dimensions(IIII)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;build()Lnet/minecraft/client/gui/widget/ButtonWidget;
 *   Lnet/minecraft/text/Text;empty()Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/gui/widget/NarratedMultilineTextWidget;builder(Lnet/minecraft/text/Text;Lnet/minecraft/client/font/TextRenderer;)Lnet/minecraft/client/gui/widget/NarratedMultilineTextWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/NarratedMultilineTextWidget$Builder;width(I)Lnet/minecraft/client/gui/widget/NarratedMultilineTextWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/NarratedMultilineTextWidget$Builder;build()Lnet/minecraft/client/gui/widget/NarratedMultilineTextWidget;
 *   Lnet/minecraft/client/gui/widget/Positioner;create()Lnet/minecraft/client/gui/widget/Positioner;
 *   Lnet/minecraft/client/gui/widget/Positioner;alignHorizontalCenter()Lnet/minecraft/client/gui/widget/Positioner;
 *   Lnet/minecraft/client/realms/util/RealmsUtil;openingScreenAndLogging(Ljava/util/function/Function;Ljava/lang/String;)Ljava/util/function/Consumer;
 *   Lnet/minecraft/client/realms/util/RealmsUtil;runAsync(Lnet/minecraft/client/realms/util/RealmsUtil$RealmsRunnable;Ljava/util/function/Consumer;)Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/client/realms/RealmsClient;create()Lnet/minecraft/client/realms/RealmsClient;
 *   Lnet/minecraft/client/realms/RealmsClient;subscriptionFor(J)Lnet/minecraft/client/realms/dto/Subscription;
 *   Lnet/minecraft/client/realms/dto/Subscription;startDate()Ljava/time/Instant;
 *   Lnet/minecraft/client/realms/dto/Subscription;type()Lnet/minecraft/client/realms/dto/Subscription$SubscriptionType;
 *   Lnet/minecraft/client/realms/gui/screen/RealmsConfigureWorldScreen;createErrorScreen(Lnet/minecraft/client/realms/exception/RealmsServiceException;)Lnet/minecraft/client/gui/screen/Screen;
 *   Lnet/minecraft/text/Text;literal(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/screen/ScreenTexts;joinLines([Lnet/minecraft/text/Text;)Lnet/minecraft/text/Text;
 *   Lnet/minecraft/client/realms/RealmsClient;deleteWorld(J)V
 *   Lnet/minecraft/client/realms/gui/RealmsPopups;createContinuableWarningPopup(Lnet/minecraft/client/gui/screen/Screen;Lnet/minecraft/text/Text;Ljava/util/function/Consumer;)Lnet/minecraft/client/gui/screen/PopupScreen;
 *   Lnet/minecraft/client/gui/screen/ConfirmLinkScreen;open(Lnet/minecraft/client/gui/screen/Screen;Ljava/lang/String;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/realms/gui/screen/tab/RealmsSubscriptionInfoTab;update(Lnet/minecraft/client/realms/dto/RealmsServer;)V
 *   Lnet/minecraft/client/realms/gui/screen/tab/RealmsSubscriptionInfoTab;daysLeftPresentation(I)Lnet/minecraft/text/Text;
 *   Lnet/minecraft/client/realms/gui/screen/tab/RealmsSubscriptionInfoTab;localPresentation(Ljava/time/Instant;)Lnet/minecraft/text/Text;
 */
package net.minecraft.client.realms.gui.screen.tab;

import com.mojang.logging.LogUtils;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.FormatStyle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tab.GridScreenTab;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EmptyWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.NarratedMultilineTextWidget;
import net.minecraft.client.gui.widget.Positioner;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.realms.RealmsClient;
import net.minecraft.client.realms.dto.RealmsServer;
import net.minecraft.client.realms.dto.Subscription;
import net.minecraft.client.realms.exception.RealmsServiceException;
import net.minecraft.client.realms.gui.RealmsPopups;
import net.minecraft.client.realms.gui.screen.RealmsConfigureWorldScreen;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;
import net.minecraft.client.realms.gui.screen.tab.RealmsUpdatableTab;
import net.minecraft.client.realms.util.RealmsUtil;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Urls;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(value=EnvType.CLIENT)
class RealmsSubscriptionInfoTab
extends GridScreenTab
implements RealmsUpdatableTab {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int field_60284 = 200;
    private static final int field_60285 = 2;
    private static final int field_60286 = 6;
    static final Text SUBSCRIPTION_TITLE = Text.translatable("mco.configure.world.subscription.tab");
    private static final Text SUBSCRIPTION_START_LABEL_TEXT = Text.translatable("mco.configure.world.subscription.start");
    private static final Text TIME_LEFT_LABEL_TEXT = Text.translatable("mco.configure.world.subscription.timeleft");
    private static final Text DAYS_LEFT_LABEL_TEXT = Text.translatable("mco.configure.world.subscription.recurring.daysleft");
    private static final Text EXPIRED_TEXT = Text.translatable("mco.configure.world.subscription.expired").formatted(Formatting.GRAY);
    private static final Text EXPIRES_IN_LESS_THAN_A_DAY_TEXT = Text.translatable("mco.configure.world.subscription.less_than_a_day").formatted(Formatting.GRAY);
    private static final Text UNKNOWN_TEXT = Text.translatable("mco.configure.world.subscription.unknown");
    private static final Text RECURRING_INFO_TEXT = Text.translatable("mco.configure.world.subscription.recurring.info");
    private final RealmsConfigureWorldScreen screen;
    private final MinecraftClient client;
    private final ButtonWidget deleteWorldButton;
    private final NarratedMultilineTextWidget subscriptionInfoTextWidget;
    private final TextWidget startDateTextWidget;
    private final TextWidget timeLeftLabelTextWidget;
    private final TextWidget daysLeftTextWidget;
    private RealmsServer serverData;
    private Text daysLeft = UNKNOWN_TEXT;
    private Text startDate = UNKNOWN_TEXT;
    private @Nullable Subscription.SubscriptionType type;

    RealmsSubscriptionInfoTab(RealmsConfigureWorldScreen screen, MinecraftClient client, RealmsServer server) {
        super(SUBSCRIPTION_TITLE);
        this.screen = screen;
        this.client = client;
        this.serverData = server;
        GridWidget.Adder lv = this.grid.setRowSpacing(6).createAdder(1);
        TextRenderer lv2 = screen.getTextRenderer();
        lv.add(new TextWidget(200, lv2.fontHeight, SUBSCRIPTION_START_LABEL_TEXT, lv2));
        this.startDateTextWidget = lv.add(new TextWidget(200, lv2.fontHeight, this.startDate, lv2));
        lv.add(EmptyWidget.ofHeight(2));
        this.timeLeftLabelTextWidget = lv.add(new TextWidget(200, lv2.fontHeight, TIME_LEFT_LABEL_TEXT, lv2));
        this.daysLeftTextWidget = lv.add(new TextWidget(200, lv2.fontHeight, this.daysLeft, lv2));
        lv.add(EmptyWidget.ofHeight(2));
        lv.add(ButtonWidget.builder(Text.translatable("mco.configure.world.subscription.extend"), button -> ConfirmLinkScreen.open((Screen)screen, Urls.getExtendJavaRealmsUrl(arg2.remoteSubscriptionId, client.getSession().getUuidOrNull()))).dimensions(0, 0, 200, 20).build());
        lv.add(EmptyWidget.ofHeight(2));
        this.deleteWorldButton = lv.add(ButtonWidget.builder(Text.translatable("mco.configure.world.delete.button"), button -> client.setScreen(RealmsPopups.createContinuableWarningPopup(screen, Text.translatable("mco.configure.world.delete.question.line1"), arg -> this.onDeletionConfirmed()))).dimensions(0, 0, 200, 20).build());
        lv.add(EmptyWidget.ofHeight(2));
        this.subscriptionInfoTextWidget = lv.add(NarratedMultilineTextWidget.builder(Text.empty(), lv2).width(200).build(), Positioner.create().alignHorizontalCenter());
        this.subscriptionInfoTextWidget.setCentered(false);
        this.update(server);
    }

    private void onDeletionConfirmed() {
        RealmsUtil.runAsync(client -> client.deleteWorld(this.serverData.id), RealmsUtil.openingScreenAndLogging(this.screen::createErrorScreen, "Couldn't delete world")).thenRunAsync(() -> this.client.setScreen(this.screen.getParent()), this.client);
        this.client.setScreen(this.screen);
    }

    private void getSubscription(long worldId) {
        RealmsClient lv = RealmsClient.create();
        try {
            Subscription lv2 = lv.subscriptionFor(worldId);
            this.daysLeft = this.daysLeftPresentation(lv2.daysLeft());
            this.startDate = RealmsSubscriptionInfoTab.localPresentation(lv2.startDate());
            this.type = lv2.type();
        } catch (RealmsServiceException lv3) {
            LOGGER.error("Couldn't get subscription", lv3);
            this.client.setScreen(this.screen.createErrorScreen(lv3));
        }
    }

    private static Text localPresentation(Instant time) {
        String string = ZonedDateTime.ofInstant(time, ZoneId.systemDefault()).format(Util.getDefaultLocaleFormatter(FormatStyle.MEDIUM));
        return Text.literal(string).formatted(Formatting.GRAY);
    }

    private Text daysLeftPresentation(int daysLeft) {
        boolean bl2;
        if (daysLeft < 0 && this.serverData.expired) {
            return EXPIRED_TEXT;
        }
        if (daysLeft <= 1) {
            return EXPIRES_IN_LESS_THAN_A_DAY_TEXT;
        }
        int j = daysLeft / 30;
        int k = daysLeft % 30;
        boolean bl = j > 0;
        boolean bl3 = bl2 = k > 0;
        if (bl && bl2) {
            return Text.translatable("mco.configure.world.subscription.remaining.months.days", j, k).formatted(Formatting.GRAY);
        }
        if (bl) {
            return Text.translatable("mco.configure.world.subscription.remaining.months", j).formatted(Formatting.GRAY);
        }
        if (bl2) {
            return Text.translatable("mco.configure.world.subscription.remaining.days", k).formatted(Formatting.GRAY);
        }
        return Text.empty();
    }

    @Override
    public void update(RealmsServer server) {
        this.serverData = server;
        this.getSubscription(server.id);
        this.startDateTextWidget.setMessage(this.startDate);
        if (this.type == Subscription.SubscriptionType.NORMAL) {
            this.timeLeftLabelTextWidget.setMessage(TIME_LEFT_LABEL_TEXT);
        } else if (this.type == Subscription.SubscriptionType.RECURRING) {
            this.timeLeftLabelTextWidget.setMessage(DAYS_LEFT_LABEL_TEXT);
        }
        this.daysLeftTextWidget.setMessage(this.daysLeft);
        boolean bl = RealmsMainScreen.isSnapshotRealmsEligible() && server.parentWorldName != null;
        this.deleteWorldButton.active = server.expired;
        if (bl) {
            this.subscriptionInfoTextWidget.setMessage(Text.translatable("mco.snapshot.subscription.info", server.parentWorldName));
        } else {
            this.subscriptionInfoTextWidget.setMessage(RECURRING_INFO_TEXT);
        }
        this.grid.refreshPositions();
    }

    @Override
    public Text getNarratedHint() {
        return ScreenTexts.joinLines(SUBSCRIPTION_TITLE, SUBSCRIPTION_START_LABEL_TEXT, this.startDate, TIME_LEFT_LABEL_TEXT, this.daysLeft);
    }
}

