/*
 * External method calls:
 *   Lnet/minecraft/util/collection/ArrayListDeque;addAll(Ljava/util/Collection;)Z
 *   Lnet/minecraft/client/gui/hud/ChatHud$OpacityRule;calculate(Lnet/minecraft/client/gui/hud/ChatHudLine$Visible;)F
 *   Lnet/minecraft/client/gui/hud/ChatHud$LineConsumer;accept(Lnet/minecraft/client/gui/hud/ChatHudLine$Visible;IF)V
 *   Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V
 *   Lnet/minecraft/client/gui/hud/ChatHud$OpacityRule;timeBased(I)Lnet/minecraft/client/gui/hud/ChatHud$OpacityRule;
 *   Lnet/minecraft/client/gui/hud/ChatHud$Backend;updatePose(Ljava/util/function/Consumer;)V
 *   Lnet/minecraft/client/gui/hud/ChatHud$Backend;fill(IIIII)V
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Text;asOrderedText()Lnet/minecraft/text/OrderedText;
 *   Lnet/minecraft/client/gui/hud/ChatHud$Backend;text(IFLnet/minecraft/text/OrderedText;)Z
 *   Lnet/minecraft/client/gui/hud/MessageIndicator;singlePlayer()Lnet/minecraft/client/gui/hud/MessageIndicator;
 *   Lnet/minecraft/client/gui/hud/MessageIndicator;system()Lnet/minecraft/client/gui/hud/MessageIndicator;
 *   Lnet/minecraft/client/gui/hud/ChatHudLine;content()Lnet/minecraft/text/Text;
 *   Lnet/minecraft/client/gui/hud/ChatHudLine;indicator()Lnet/minecraft/client/gui/hud/MessageIndicator;
 *   Lnet/minecraft/util/Nullables;map(Ljava/lang/Object;Ljava/util/function/Function;)Ljava/lang/Object;
 *   Lnet/minecraft/client/gui/hud/ChatHudLine;breakLines(Lnet/minecraft/client/font/TextRenderer;I)Ljava/util/List;
 *   Lnet/minecraft/client/gui/hud/ChatHudLine;signature()Lnet/minecraft/network/message/MessageSignatureData;
 *   Lnet/minecraft/util/collection/ArrayListDeque;peekLast()Ljava/lang/Object;
 *   Lnet/minecraft/util/collection/ArrayListDeque;removeFirst()Ljava/lang/Object;
 *   Lnet/minecraft/util/collection/ArrayListDeque;addLast(Ljava/lang/Object;)V
 *   Lnet/minecraft/client/gui/hud/ChatHud$Draft;text()Ljava/lang/String;
 *   Lnet/minecraft/client/gui/screen/ChatScreen$Factory;create(Ljava/lang/String;Z)Lnet/minecraft/client/gui/screen/ChatScreen;
 *   Lnet/minecraft/client/gui/hud/ChatHud$RemovalQueuedMessage;signature()Lnet/minecraft/network/message/MessageSignatureData;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/MutableText;formatted([Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/text/Style;withClickEvent(Lnet/minecraft/text/ClickEvent;)Lnet/minecraft/text/Style;
 *   Lnet/minecraft/text/Style;withHoverEvent(Lnet/minecraft/text/HoverEvent;)Lnet/minecraft/text/Style;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/hud/ChatHud;render(Lnet/minecraft/client/gui/hud/ChatHud$Backend;IIZ)V
 *   Lnet/minecraft/client/gui/hud/ChatHud;forEachVisibleLine(Lnet/minecraft/client/gui/hud/ChatHud$OpacityRule;Lnet/minecraft/client/gui/hud/ChatHud$LineConsumer;)I
 *   Lnet/minecraft/client/gui/hud/ChatHud;addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V
 *   Lnet/minecraft/client/gui/hud/ChatHud;logChatMessage(Lnet/minecraft/client/gui/hud/ChatHudLine;)V
 *   Lnet/minecraft/client/gui/hud/ChatHud;addVisibleMessage(Lnet/minecraft/client/gui/hud/ChatHudLine;)V
 *   Lnet/minecraft/client/gui/hud/ChatHud;addMessage(Lnet/minecraft/client/gui/hud/ChatHudLine;)V
 *   Lnet/minecraft/client/gui/hud/ChatHud;scroll(I)V
 *   Lnet/minecraft/client/gui/hud/ChatHud;queueForRemoval(Lnet/minecraft/network/message/MessageSignatureData;)Lnet/minecraft/client/gui/hud/ChatHud$RemovalQueuedMessage;
 *   Lnet/minecraft/client/gui/hud/ChatHud;createRemovalMarker(Lnet/minecraft/client/gui/hud/ChatHudLine;)Lnet/minecraft/client/gui/hud/ChatHudLine;
 *   Lnet/minecraft/client/gui/hud/ChatHud;createScreen(Lnet/minecraft/client/gui/hud/ChatHud$ChatMethod;Lnet/minecraft/client/gui/screen/ChatScreen$Factory;)Lnet/minecraft/client/gui/screen/ChatScreen;
 */
package net.minecraft.client.gui.hud;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.Alignment;
import net.minecraft.client.font.DrawnTextConsumer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.cursor.StandardCursors;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.message.ChatVisibility;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Nullables;
import net.minecraft.util.collection.ArrayListDeque;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.profiler.Profilers;
import org.joml.Matrix3x2f;
import org.joml.Vector2f;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(value=EnvType.CLIENT)
public class ChatHud {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int MAX_MESSAGES = 100;
    private static final int field_39772 = 4;
    private static final int OFFSET_FROM_BOTTOM = 40;
    private static final int field_63864 = 210;
    private static final int REMOVAL_QUEUE_TICKS = 60;
    private static final Text DELETED_MARKER_TEXT = Text.translatable("chat.deleted_marker").formatted(Formatting.GRAY, Formatting.ITALIC);
    public static final int field_63862 = 8;
    public static final Identifier EXPAND_CHAT_QUEUE_ID = Identifier.ofVanilla("internal/expand_chat_queue");
    private static final Style CHAT_QUEUE_STYLE = Style.EMPTY.withClickEvent(new ClickEvent.Custom(EXPAND_CHAT_QUEUE_ID, Optional.empty())).withHoverEvent(new HoverEvent.ShowText(Text.translatable("chat.queue.tooltip")));
    final MinecraftClient client;
    private final ArrayListDeque<String> messageHistory = new ArrayListDeque(100);
    private final List<ChatHudLine> messages = Lists.newArrayList();
    private final List<ChatHudLine.Visible> visibleMessages = Lists.newArrayList();
    private int scrolledLines;
    private boolean hasUnreadNewMessages;
    private @Nullable Draft draft;
    private @Nullable ChatScreen screen;
    private final List<RemovalQueuedMessage> removalQueue = new ArrayList<RemovalQueuedMessage>();

    public ChatHud(MinecraftClient client) {
        this.client = client;
        this.messageHistory.addAll(client.getCommandHistoryManager().getHistory());
    }

    public void tickRemovalQueueIfExists() {
        if (!this.removalQueue.isEmpty()) {
            this.tickRemovalQueue();
        }
    }

    private int forEachVisibleLine(OpacityRule opacityRule, LineConsumer lineConsumer) {
        int i = this.getVisibleLineCount();
        int j = 0;
        for (int k = Math.min(this.visibleMessages.size() - this.scrolledLines, i) - 1; k >= 0; --k) {
            int l = k + this.scrolledLines;
            ChatHudLine.Visible lv = this.visibleMessages.get(l);
            float f = opacityRule.calculate(lv);
            if (!(f > 1.0E-5f)) continue;
            ++j;
            lineConsumer.accept(lv, k, f);
        }
        return j;
    }

    public void render(DrawContext context, TextRenderer textRenderer, int currentTick, int mouseX, int mouseY, boolean interactable, boolean bl2) {
        context.getMatrices().pushMatrix();
        this.render(interactable ? new Interactable(context, textRenderer, mouseX, mouseY, bl2) : new Hud(context), context.getScaledWindowHeight(), currentTick, interactable);
        context.getMatrices().popMatrix();
    }

    public void render(DrawnTextConsumer textConsumer, int windowHeight, int currentTick, boolean expanded) {
        this.render(new Forwarder(textConsumer), windowHeight, currentTick, expanded);
    }

    private void render(final Backend drawer, int windowHeight, int currentTick, boolean expanded) {
        int t;
        if (this.isChatHidden()) {
            return;
        }
        int k = this.visibleMessages.size();
        if (k <= 0) {
            return;
        }
        Profiler lv = Profilers.get();
        lv.push("chat");
        float f = (float)this.getChatScale();
        int l = MathHelper.ceil((float)this.getWidth() / f);
        final int m = MathHelper.floor((float)(windowHeight - 40) / f);
        final float g = this.client.options.getChatOpacity().getValue().floatValue() * 0.9f + 0.1f;
        float h = this.client.options.getTextBackgroundOpacity().getValue().floatValue();
        final int n = this.client.textRenderer.fontHeight;
        int o = 8;
        double d = this.client.options.getChatLineSpacing().getValue();
        final int p = (int)((double)n * (d + 1.0));
        final int q = (int)Math.round(8.0 * (d + 1.0) - 4.0 * d);
        long r = this.client.getMessageHandler().getUnprocessedMessageCount();
        OpacityRule lv2 = expanded ? OpacityRule.CONSTANT : OpacityRule.timeBased(currentTick);
        drawer.updatePose(pose -> {
            pose.scale(f, f);
            pose.translate(4.0f, 0.0f);
        });
        this.forEachVisibleLine(lv2, (line, y, opacity) -> {
            int m = m - y * p;
            int n = m - p;
            drawer.fill(-4, n, l + 4 + 4, m, ColorHelper.toAlpha(opacity * h));
        });
        if (r > 0L) {
            drawer.fill(-2, m, l + 4, m + n, ColorHelper.toAlpha(h));
        }
        int s = this.forEachVisibleLine(lv2, new LineConsumer(){
            boolean styledCurrentLine;

            @Override
            public void accept(ChatHudLine.Visible arg, int i, float f) {
                boolean bl2;
                int j = m - i * p;
                int k = j - p;
                int l = j - q;
                boolean bl = drawer.text(l, f * g, arg.content());
                this.styledCurrentLine |= bl;
                if (arg.endOfEntry()) {
                    bl2 = this.styledCurrentLine;
                    this.styledCurrentLine = false;
                } else {
                    bl2 = false;
                }
                MessageIndicator lv = arg.indicator();
                if (lv != null) {
                    drawer.indicator(-4, k, -2, j, f * g, lv);
                    if (lv.icon() != null) {
                        int m2 = arg.getWidth(ChatHud.this.client.textRenderer);
                        int n2 = l + n;
                        drawer.indicatorIcon(m2, n2, bl2, lv, lv.icon());
                    }
                }
            }
        });
        if (r > 0L) {
            t = m + n;
            MutableText lv3 = Text.translatable("chat.queue", r).setStyle(CHAT_QUEUE_STYLE);
            drawer.text(t - 8, 0.5f * g, lv3.asOrderedText());
        }
        if (expanded) {
            t = k * p;
            int u = s * p;
            int v = this.scrolledLines * u / k - m;
            int w = u * u / t;
            if (t != u) {
                int x = v > 0 ? 170 : 96;
                int y2 = this.hasUnreadNewMessages ? 0xCC3333 : 0x3333AA;
                int z = l + 4;
                drawer.fill(z, -v, z + 2, -v - w, ColorHelper.withAlpha(x, y2));
                drawer.fill(z + 2, -v, z + 1, -v - w, ColorHelper.withAlpha(x, 0xCCCCCC));
            }
        }
        lv.pop();
    }

    private boolean isChatHidden() {
        return this.client.options.getChatVisibility().getValue() == ChatVisibility.HIDDEN;
    }

    public void clear(boolean clearHistory) {
        this.client.getMessageHandler().processAll();
        this.removalQueue.clear();
        this.visibleMessages.clear();
        this.messages.clear();
        if (clearHistory) {
            this.messageHistory.clear();
            this.messageHistory.addAll(this.client.getCommandHistoryManager().getHistory());
        }
    }

    public void addMessage(Text message) {
        this.addMessage(message, null, this.client.isConnectedToLocalServer() ? MessageIndicator.singlePlayer() : MessageIndicator.system());
    }

    public void addMessage(Text message, @Nullable MessageSignatureData signatureData, @Nullable MessageIndicator indicator) {
        ChatHudLine lv = new ChatHudLine(this.client.inGameHud.getTicks(), message, signatureData, indicator);
        this.logChatMessage(lv);
        this.addVisibleMessage(lv);
        this.addMessage(lv);
    }

    private void logChatMessage(ChatHudLine message) {
        String string = message.content().getString().replaceAll("\r", "\\\\r").replaceAll("\n", "\\\\n");
        String string2 = Nullables.map(message.indicator(), MessageIndicator::loggedName);
        if (string2 != null) {
            LOGGER.info("[{}] [CHAT] {}", (Object)string2, (Object)string);
        } else {
            LOGGER.info("[CHAT] {}", (Object)string);
        }
    }

    private void addVisibleMessage(ChatHudLine message) {
        int i = MathHelper.floor((double)this.getWidth() / this.getChatScale());
        List<OrderedText> list = message.breakLines(this.client.textRenderer, i);
        boolean bl = this.isChatFocused();
        for (int j = 0; j < list.size(); ++j) {
            OrderedText lv = list.get(j);
            if (bl && this.scrolledLines > 0) {
                this.hasUnreadNewMessages = true;
                this.scroll(1);
            }
            boolean bl2 = j == list.size() - 1;
            this.visibleMessages.addFirst(new ChatHudLine.Visible(message.creationTick(), lv, message.indicator(), bl2));
        }
        while (this.visibleMessages.size() > 100) {
            this.visibleMessages.removeLast();
        }
    }

    private void addMessage(ChatHudLine message) {
        this.messages.addFirst(message);
        while (this.messages.size() > 100) {
            this.messages.removeLast();
        }
    }

    private void tickRemovalQueue() {
        int i = this.client.inGameHud.getTicks();
        this.removalQueue.removeIf(message -> {
            if (i >= message.deletableAfter()) {
                return this.queueForRemoval(message.signature()) == null;
            }
            return false;
        });
    }

    public void removeMessage(MessageSignatureData signature) {
        RemovalQueuedMessage lv = this.queueForRemoval(signature);
        if (lv != null) {
            this.removalQueue.add(lv);
        }
    }

    private @Nullable RemovalQueuedMessage queueForRemoval(MessageSignatureData signature) {
        int i = this.client.inGameHud.getTicks();
        ListIterator<ChatHudLine> listIterator = this.messages.listIterator();
        while (listIterator.hasNext()) {
            ChatHudLine lv = listIterator.next();
            if (!signature.equals(lv.signature())) continue;
            int j = lv.creationTick() + 60;
            if (i >= j) {
                listIterator.set(this.createRemovalMarker(lv));
                this.refresh();
                return null;
            }
            return new RemovalQueuedMessage(signature, j);
        }
        return null;
    }

    private ChatHudLine createRemovalMarker(ChatHudLine original) {
        return new ChatHudLine(original.creationTick(), DELETED_MARKER_TEXT, null, MessageIndicator.system());
    }

    public void reset() {
        this.resetScroll();
        this.refresh();
    }

    private void refresh() {
        this.visibleMessages.clear();
        for (ChatHudLine lv : Lists.reverse(this.messages)) {
            this.addVisibleMessage(lv);
        }
    }

    public ArrayListDeque<String> getMessageHistory() {
        return this.messageHistory;
    }

    public void addToMessageHistory(String message) {
        if (!message.equals(this.messageHistory.peekLast())) {
            if (this.messageHistory.size() >= 100) {
                this.messageHistory.removeFirst();
            }
            this.messageHistory.addLast(message);
        }
        if (message.startsWith("/")) {
            this.client.getCommandHistoryManager().add(message);
        }
    }

    public void resetScroll() {
        this.scrolledLines = 0;
        this.hasUnreadNewMessages = false;
    }

    public void scroll(int scroll) {
        this.scrolledLines += scroll;
        int j = this.visibleMessages.size();
        if (this.scrolledLines > j - this.getVisibleLineCount()) {
            this.scrolledLines = j - this.getVisibleLineCount();
        }
        if (this.scrolledLines <= 0) {
            this.scrolledLines = 0;
            this.hasUnreadNewMessages = false;
        }
    }

    public boolean isChatFocused() {
        return this.client.currentScreen instanceof ChatScreen;
    }

    private int getWidth() {
        return ChatHud.getWidth(this.client.options.getChatWidth().getValue());
    }

    private int getHeight() {
        return ChatHud.getHeight(this.isChatFocused() ? this.client.options.getChatHeightFocused().getValue() : this.client.options.getChatHeightUnfocused().getValue());
    }

    private double getChatScale() {
        return this.client.options.getChatScale().getValue();
    }

    public static int getWidth(double widthOption) {
        int i = 320;
        int j = 40;
        return MathHelper.floor(widthOption * 280.0 + 40.0);
    }

    public static int getHeight(double heightOption) {
        int i = 180;
        int j = 20;
        return MathHelper.floor(heightOption * 160.0 + 20.0);
    }

    public static double getDefaultUnfocusedHeight() {
        int i = 180;
        int j = 20;
        return 70.0 / (double)(ChatHud.getHeight(1.0) - 20);
    }

    public int getVisibleLineCount() {
        return this.getHeight() / this.getLineHeight();
    }

    private int getLineHeight() {
        return (int)((double)this.client.textRenderer.fontHeight * (this.client.options.getChatLineSpacing().getValue() + 1.0));
    }

    public void saveDraft(String text) {
        boolean bl = text.startsWith("/");
        this.draft = new Draft(text, bl ? ChatMethod.COMMAND : ChatMethod.MESSAGE);
    }

    public void discardDraft() {
        this.draft = null;
    }

    public <T extends ChatScreen> T createScreen(ChatMethod method, ChatScreen.Factory<T> factory) {
        if (this.draft != null && method.shouldKeepDraft(this.draft)) {
            return factory.create(this.draft.text(), true);
        }
        return factory.create(method.getReplacement(), false);
    }

    public void setClientScreen(ChatMethod method, ChatScreen.Factory<?> factory) {
        this.client.setScreen((Screen)this.createScreen(method, factory));
    }

    public void setScreen() {
        Screen screen = this.client.currentScreen;
        if (screen instanceof ChatScreen) {
            ChatScreen lv;
            this.screen = lv = (ChatScreen)screen;
        }
    }

    public @Nullable ChatScreen removeScreen() {
        ChatScreen lv = this.screen;
        this.screen = null;
        return lv;
    }

    public ChatState toChatState() {
        return new ChatState(List.copyOf(this.messages), List.copyOf(this.messageHistory), List.copyOf(this.removalQueue));
    }

    public void restoreChatState(ChatState state) {
        this.messageHistory.clear();
        this.messageHistory.addAll(state.messageHistory);
        this.removalQueue.clear();
        this.removalQueue.addAll(state.removalQueue);
        this.messages.clear();
        this.messages.addAll(state.messages);
        this.refresh();
    }

    @FunctionalInterface
    @Environment(value=EnvType.CLIENT)
    static interface OpacityRule {
        public static final OpacityRule CONSTANT = line -> 1.0f;

        public static OpacityRule timeBased(int currentTick) {
            return line -> {
                int j = currentTick - line.addedTime();
                double d = (double)j / 200.0;
                d = 1.0 - d;
                d *= 10.0;
                d = MathHelper.clamp(d, 0.0, 1.0);
                d *= d;
                return (float)d;
            };
        }

        public float calculate(ChatHudLine.Visible var1);
    }

    @FunctionalInterface
    @Environment(value=EnvType.CLIENT)
    static interface LineConsumer {
        public void accept(ChatHudLine.Visible var1, int var2, float var3);
    }

    @Environment(value=EnvType.CLIENT)
    static class Interactable
    implements Backend,
    Consumer<Style> {
        private final DrawContext context;
        private final TextRenderer textRenderer;
        private final DrawnTextConsumer drawer;
        private DrawnTextConsumer.Transformation transformation;
        private final int mouseX;
        private final int mouseY;
        private final Vector2f untransformedOffset = new Vector2f();
        private @Nullable Style style;
        private final boolean field_64672;

        public Interactable(DrawContext context, TextRenderer textRenderer, int mouseX, int mouseY, boolean bl) {
            this.context = context;
            this.textRenderer = textRenderer;
            this.drawer = context.getTextConsumer(DrawContext.HoverType.TOOLTIP_AND_CURSOR, this);
            this.mouseX = mouseX;
            this.mouseY = mouseY;
            this.field_64672 = bl;
            this.transformation = this.drawer.getTransformation();
            this.calculateUntransformedOffset();
        }

        private void calculateUntransformedOffset() {
            this.context.getMatrices().invert(new Matrix3x2f()).transformPosition(this.mouseX, this.mouseY, this.untransformedOffset);
        }

        @Override
        public void updatePose(Consumer<Matrix3x2f> transformer) {
            transformer.accept(this.context.getMatrices());
            this.transformation = this.transformation.withPose(new Matrix3x2f(this.context.getMatrices()));
            this.calculateUntransformedOffset();
        }

        @Override
        public void fill(int x1, int y1, int x2, int y2, int color) {
            this.context.fill(x1, y1, x2, y2, color);
        }

        @Override
        public void accept(Style arg) {
            this.style = arg;
        }

        @Override
        public boolean text(int y, float opacity, OrderedText text) {
            this.style = null;
            this.drawer.text(Alignment.LEFT, 0, y, this.transformation.withOpacity(opacity), text);
            if (this.field_64672 && this.style != null && this.style.getInsertion() != null) {
                this.context.setCursor(StandardCursors.POINTING_HAND);
            }
            return this.style != null;
        }

        private boolean isWithinBounds(int left, int top, int right, int bottom) {
            return DrawnTextConsumer.isWithinBounds(this.untransformedOffset.x, this.untransformedOffset.y, left, top, right, bottom);
        }

        @Override
        public void indicator(int x1, int y1, int x2, int y2, float opacity, MessageIndicator indicator) {
            int m = ColorHelper.withAlpha(opacity, indicator.indicatorColor());
            this.context.fill(x1, y1, x2, y2, m);
            if (this.isWithinBounds(x1, y1, x2, y2)) {
                this.indicatorTooltip(indicator);
            }
        }

        @Override
        public void indicatorIcon(int left, int bottom, boolean forceDraw, MessageIndicator indicator, MessageIndicator.Icon icon) {
            int k = bottom - icon.height - 1;
            int l = left + icon.width;
            boolean bl2 = this.isWithinBounds(left, k, l, bottom);
            if (bl2) {
                this.indicatorTooltip(indicator);
            }
            if (forceDraw || bl2) {
                icon.draw(this.context, left, k);
            }
        }

        private void indicatorTooltip(MessageIndicator indicator) {
            if (indicator.text() != null) {
                this.context.drawOrderedTooltip(this.textRenderer, this.textRenderer.wrapLines(indicator.text(), 210), this.mouseX, this.mouseY);
            }
        }

        @Override
        public /* synthetic */ void accept(Object style) {
            this.accept((Style)style);
        }
    }

    @Environment(value=EnvType.CLIENT)
    static class Hud
    implements Backend {
        private final DrawContext context;
        private final DrawnTextConsumer textConsumer;
        private DrawnTextConsumer.Transformation transformation;

        public Hud(DrawContext context) {
            this.context = context;
            this.textConsumer = context.getTextConsumer(DrawContext.HoverType.NONE, null);
            this.transformation = this.textConsumer.getTransformation();
        }

        @Override
        public void updatePose(Consumer<Matrix3x2f> transformer) {
            transformer.accept(this.context.getMatrices());
            this.transformation = this.transformation.withPose(new Matrix3x2f(this.context.getMatrices()));
        }

        @Override
        public void fill(int x1, int y1, int x2, int y2, int color) {
            this.context.fill(x1, y1, x2, y2, color);
        }

        @Override
        public boolean text(int y, float opacity, OrderedText text) {
            this.textConsumer.text(Alignment.LEFT, 0, y, this.transformation.withOpacity(opacity), text);
            return false;
        }

        @Override
        public void indicator(int x1, int y1, int x2, int y2, float opacity, MessageIndicator indicator) {
            int m = ColorHelper.withAlpha(opacity, indicator.indicatorColor());
            this.context.fill(x1, y1, x2, y2, m);
        }

        @Override
        public void indicatorIcon(int left, int bottom, boolean forceDraw, MessageIndicator indicator, MessageIndicator.Icon icon) {
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static interface Backend {
        public void updatePose(Consumer<Matrix3x2f> var1);

        public void fill(int var1, int var2, int var3, int var4, int var5);

        public boolean text(int var1, float var2, OrderedText var3);

        public void indicator(int var1, int var2, int var3, int var4, float var5, MessageIndicator var6);

        public void indicatorIcon(int var1, int var2, boolean var3, MessageIndicator var4, MessageIndicator.Icon var5);
    }

    @Environment(value=EnvType.CLIENT)
    static class Forwarder
    implements Backend {
        private final DrawnTextConsumer drawer;

        public Forwarder(DrawnTextConsumer drawer) {
            this.drawer = drawer;
        }

        @Override
        public void updatePose(Consumer<Matrix3x2f> transformer) {
            DrawnTextConsumer.Transformation lv = this.drawer.getTransformation();
            Matrix3x2f matrix3x2f = new Matrix3x2f(lv.pose());
            transformer.accept(matrix3x2f);
            this.drawer.setTransformation(lv.withPose(matrix3x2f));
        }

        @Override
        public void fill(int x1, int y1, int x2, int y2, int color) {
        }

        @Override
        public boolean text(int y, float opacity, OrderedText text) {
            this.drawer.text(Alignment.LEFT, 0, y, text);
            return false;
        }

        @Override
        public void indicator(int x1, int y1, int x2, int y2, float opacity, MessageIndicator indicator) {
        }

        @Override
        public void indicatorIcon(int left, int bottom, boolean forceDraw, MessageIndicator indicator, MessageIndicator.Icon icon) {
        }
    }

    @Environment(value=EnvType.CLIENT)
    record RemovalQueuedMessage(MessageSignatureData signature, int deletableAfter) {
    }

    @Environment(value=EnvType.CLIENT)
    public record Draft(String text, ChatMethod chatMethod) {
    }

    @Environment(value=EnvType.CLIENT)
    public static enum ChatMethod {
        MESSAGE(""){

            @Override
            public boolean shouldKeepDraft(Draft draft) {
                return true;
            }
        }
        ,
        COMMAND("/"){

            @Override
            public boolean shouldKeepDraft(Draft draft) {
                return this == draft.chatMethod;
            }
        };

        private final String replacement;

        ChatMethod(String replacement) {
            this.replacement = replacement;
        }

        public String getReplacement() {
            return this.replacement;
        }

        public abstract boolean shouldKeepDraft(Draft var1);
    }

    @Environment(value=EnvType.CLIENT)
    public static class ChatState {
        final List<ChatHudLine> messages;
        final List<String> messageHistory;
        final List<RemovalQueuedMessage> removalQueue;

        public ChatState(List<ChatHudLine> messages, List<String> messageHistory, List<RemovalQueuedMessage> removalQueue) {
            this.messages = messages;
            this.messageHistory = messageHistory;
            this.removalQueue = removalQueue;
        }
    }
}

