/*
 * External method calls:
 *   Lnet/minecraft/network/message/SignedMessage;withoutUnsigned()Lnet/minecraft/network/message/SignedMessage;
 *   Lnet/minecraft/network/message/MessageType$Parameters;applyChatDecoration(Lnet/minecraft/text/Text;)Lnet/minecraft/text/Text;
 *   Lnet/minecraft/network/message/SignedMessage;signature()Lnet/minecraft/network/message/MessageSignatureData;
 *   Lnet/minecraft/client/network/message/MessageTrustStatus;createIndicator(Lnet/minecraft/network/message/SignedMessage;)Lnet/minecraft/client/gui/hud/MessageIndicator;
 *   Lnet/minecraft/network/message/SignedMessage;filterMask()Lnet/minecraft/network/message/FilterMask;
 *   Lnet/minecraft/client/gui/hud/ChatHud;addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V
 *   Lnet/minecraft/network/message/MessageType$Parameters;applyNarrationDecoration(Lnet/minecraft/text/Text;)Lnet/minecraft/text/Text;
 *   Lnet/minecraft/client/util/NarratorManager;narrateChatMessage(Lnet/minecraft/text/Text;)V
 *   Lnet/minecraft/client/session/report/log/ReceivedMessage;of(Lcom/mojang/authlib/GameProfile;Lnet/minecraft/network/message/SignedMessage;Lnet/minecraft/client/network/message/MessageTrustStatus;)Lnet/minecraft/client/session/report/log/ReceivedMessage$ChatMessage;
 *   Lnet/minecraft/client/session/report/log/ReceivedMessage;of(Lnet/minecraft/text/Text;Ljava/time/Instant;)Lnet/minecraft/client/session/report/log/ReceivedMessage$GameMessage;
 *   Lnet/minecraft/client/util/NarratorManager;narrateSystemMessage(Lnet/minecraft/text/Text;)V
 *   Lnet/minecraft/client/gui/hud/ChatHud;addMessage(Lnet/minecraft/text/Text;)V
 *   Lnet/minecraft/client/util/NarratorManager;narrate(Lnet/minecraft/text/Text;)V
 *   Lnet/minecraft/text/TextVisitFactory;removeFormattingCodes(Lnet/minecraft/text/StringVisitable;)Ljava/lang/String;
 *   Lnet/minecraft/client/network/ClientPlayNetworkHandler;acknowledge(Lnet/minecraft/network/message/MessageSignatureData;Z)V
 *   Lnet/minecraft/client/gui/hud/MessageIndicator;chatError()Lnet/minecraft/client/gui/hud/MessageIndicator;
 *   Lnet/minecraft/client/network/message/MessageHandler$ProcessableMessage;signature()Lnet/minecraft/network/message/MessageSignatureData;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/MutableText;formatted([Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/network/message/MessageHandler;process(Lnet/minecraft/network/message/MessageSignatureData;Ljava/util/function/BooleanSupplier;)V
 *   Lnet/minecraft/client/network/message/MessageHandler;narrate(Lnet/minecraft/network/message/MessageType$Parameters;Lnet/minecraft/text/Text;)V
 *   Lnet/minecraft/client/network/message/MessageHandler;addToChatLog(Lnet/minecraft/network/message/SignedMessage;Lcom/mojang/authlib/GameProfile;Lnet/minecraft/client/network/message/MessageTrustStatus;)V
 *   Lnet/minecraft/client/network/message/MessageHandler;extractSender(Lnet/minecraft/text/Text;)Ljava/util/UUID;
 *   Lnet/minecraft/client/network/message/MessageHandler;addToChatLog(Lnet/minecraft/text/Text;Ljava/time/Instant;)V
 *   Lnet/minecraft/client/network/message/MessageHandler;processChatMessageInternal(Lnet/minecraft/network/message/MessageType$Parameters;Lnet/minecraft/network/message/SignedMessage;Lnet/minecraft/text/Text;Lcom/mojang/authlib/GameProfile;ZLjava/time/Instant;)Z
 */
package net.minecraft.client.network.message;

import com.google.common.collect.Queues;
import com.mojang.authlib.GameProfile;
import java.time.Instant;
import java.util.Deque;
import java.util.UUID;
import java.util.function.BooleanSupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.message.MessageTrustStatus;
import net.minecraft.client.session.report.log.ChatLog;
import net.minecraft.client.session.report.log.ReceivedMessage;
import net.minecraft.network.message.FilterMask;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.Text;
import net.minecraft.text.TextVisitFactory;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class MessageHandler {
    private static final Text VALIDATION_ERROR_TEXT = Text.translatable("chat.validation_error").formatted(Formatting.RED, Formatting.ITALIC);
    private final MinecraftClient client;
    private final Deque<ProcessableMessage> delayedMessages = Queues.newArrayDeque();
    private long chatDelay;
    private long lastProcessTime;

    public MessageHandler(MinecraftClient client) {
        this.client = client;
    }

    public void processDelayedMessages() {
        if (this.client.isPaused()) {
            if (this.chatDelay > 0L) {
                this.lastProcessTime += 50L;
            }
            return;
        }
        if (this.chatDelay == 0L) {
            if (!this.delayedMessages.isEmpty()) {
                this.processAll();
            }
        } else if (Util.getMeasuringTimeMs() >= this.lastProcessTime + this.chatDelay) {
            ProcessableMessage lv;
            while ((lv = this.delayedMessages.poll()) != null && !lv.accept()) {
            }
        }
    }

    public void setChatDelay(double chatDelay) {
        long l = (long)(chatDelay * 1000.0);
        if (l == 0L && this.chatDelay > 0L && !this.client.isPaused()) {
            this.processAll();
        }
        this.chatDelay = l;
    }

    public void process() {
        this.delayedMessages.remove().accept();
    }

    public long getUnprocessedMessageCount() {
        return this.delayedMessages.size();
    }

    public void processAll() {
        this.delayedMessages.forEach(ProcessableMessage::accept);
        this.delayedMessages.clear();
        this.lastProcessTime = 0L;
    }

    public boolean removeDelayedMessage(MessageSignatureData signature) {
        return this.delayedMessages.removeIf(message -> signature.equals(message.signature()));
    }

    private boolean shouldDelay() {
        return this.chatDelay > 0L && Util.getMeasuringTimeMs() < this.lastProcessTime + this.chatDelay;
    }

    private void process(@Nullable MessageSignatureData signature, BooleanSupplier processor) {
        if (this.shouldDelay()) {
            this.delayedMessages.add(new ProcessableMessage(signature, processor));
        } else {
            processor.getAsBoolean();
        }
    }

    public void onChatMessage(SignedMessage message, GameProfile sender, MessageType.Parameters params) {
        boolean bl = this.client.options.getOnlyShowSecureChat().getValue();
        SignedMessage lv = bl ? message.withoutUnsigned() : message;
        Text lv2 = params.applyChatDecoration(lv.getContent());
        Instant instant = Instant.now();
        this.process(message.signature(), () -> {
            boolean bl2 = this.processChatMessageInternal(params, message, lv2, sender, bl, instant);
            ClientPlayNetworkHandler lv = this.client.getNetworkHandler();
            if (lv != null && message.signature() != null) {
                lv.acknowledge(message.signature(), bl2);
            }
            return bl2;
        });
    }

    public void onUnverifiedMessage(UUID sender, @Nullable MessageSignatureData signature, MessageType.Parameters parameters) {
        this.process(null, () -> {
            ClientPlayNetworkHandler lv = this.client.getNetworkHandler();
            if (lv != null && signature != null) {
                lv.acknowledge(signature, false);
            }
            if (this.client.shouldBlockMessages(sender)) {
                return false;
            }
            Text lv2 = parameters.applyChatDecoration(VALIDATION_ERROR_TEXT);
            this.client.inGameHud.getChatHud().addMessage(lv2, null, MessageIndicator.chatError());
            this.client.getNarratorManager().narrate(parameters.applyNarrationDecoration(VALIDATION_ERROR_TEXT));
            this.lastProcessTime = Util.getMeasuringTimeMs();
            return true;
        });
    }

    public void onProfilelessMessage(Text content, MessageType.Parameters params) {
        Instant instant = Instant.now();
        this.process(null, () -> {
            Text lv = params.applyChatDecoration(content);
            this.client.inGameHud.getChatHud().addMessage(lv);
            this.narrate(params, content);
            this.addToChatLog(lv, instant);
            this.lastProcessTime = Util.getMeasuringTimeMs();
            return true;
        });
    }

    private boolean processChatMessageInternal(MessageType.Parameters params, SignedMessage message, Text decorated, GameProfile sender, boolean onlyShowSecureChat, Instant receptionTimestamp) {
        MessageTrustStatus lv = this.getStatus(message, decorated, receptionTimestamp);
        if (onlyShowSecureChat && lv.isInsecure()) {
            return false;
        }
        if (this.client.shouldBlockMessages(message.getSender()) || message.isFullyFiltered()) {
            return false;
        }
        MessageIndicator lv2 = lv.createIndicator(message);
        MessageSignatureData lv3 = message.signature();
        FilterMask lv4 = message.filterMask();
        if (lv4.isPassThrough()) {
            this.client.inGameHud.getChatHud().addMessage(decorated, lv3, lv2);
            this.narrate(params, message.getContent());
        } else {
            Text lv5 = lv4.getFilteredText(message.getSignedContent());
            if (lv5 != null) {
                this.client.inGameHud.getChatHud().addMessage(params.applyChatDecoration(lv5), lv3, lv2);
                this.narrate(params, lv5);
            }
        }
        this.addToChatLog(message, sender, lv);
        this.lastProcessTime = Util.getMeasuringTimeMs();
        return true;
    }

    private void narrate(MessageType.Parameters params, Text message) {
        this.client.getNarratorManager().narrateChatMessage(params.applyNarrationDecoration(message));
    }

    private MessageTrustStatus getStatus(SignedMessage message, Text decorated, Instant receptionTimestamp) {
        if (this.isAlwaysTrusted(message.getSender())) {
            return MessageTrustStatus.SECURE;
        }
        return MessageTrustStatus.getStatus(message, decorated, receptionTimestamp);
    }

    private void addToChatLog(SignedMessage message, GameProfile gameProfile, MessageTrustStatus messageTrustStatus) {
        ChatLog lv = this.client.getAbuseReportContext().getChatLog();
        lv.add(ReceivedMessage.of(gameProfile, message, messageTrustStatus));
    }

    private void addToChatLog(Text message, Instant timestamp) {
        ChatLog lv = this.client.getAbuseReportContext().getChatLog();
        lv.add(ReceivedMessage.of(message, timestamp));
    }

    public void onGameMessage(Text message, boolean overlay) {
        if (this.client.options.getHideMatchedNames().getValue().booleanValue() && this.client.shouldBlockMessages(this.extractSender(message))) {
            return;
        }
        if (overlay) {
            this.client.inGameHud.setOverlayMessage(message, false);
            this.client.getNarratorManager().narrateSystemMessage(message);
        } else {
            this.client.inGameHud.getChatHud().addMessage(message);
            this.addToChatLog(message, Instant.now());
            this.client.getNarratorManager().narrate(message);
        }
    }

    private UUID extractSender(Text text) {
        String string = TextVisitFactory.removeFormattingCodes(text);
        String string2 = StringUtils.substringBetween(string, "<", ">");
        if (string2 == null) {
            return Util.NIL_UUID;
        }
        return this.client.getSocialInteractionsManager().getUuid(string2);
    }

    private boolean isAlwaysTrusted(UUID sender) {
        if (this.client.isInSingleplayer() && this.client.player != null) {
            UUID uUID2 = this.client.player.getGameProfile().id();
            return uUID2.equals(sender);
        }
        return false;
    }

    @Environment(value=EnvType.CLIENT)
    record ProcessableMessage(@Nullable MessageSignatureData signature, BooleanSupplier handler) {
        public boolean accept() {
            return this.handler.getAsBoolean();
        }
    }
}

