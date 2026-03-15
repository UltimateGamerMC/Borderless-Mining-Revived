/*
 * External method calls:
 *   Lnet/minecraft/client/gui/DrawContext;scissorContains(II)Z
 *   Lnet/minecraft/client/gui/tooltip/TooltipState;render(Lnet/minecraft/client/gui/DrawContext;IIZZLnet/minecraft/client/gui/ScreenRect;)V
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/font/DrawnTextConsumer;text(Lnet/minecraft/text/Text;IIII)V
 *   Lnet/minecraft/client/gui/Click;buttonInfo()Lnet/minecraft/client/input/MouseInput;
 *   Lnet/minecraft/client/gui/navigation/GuiNavigationPath;of(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/navigation/GuiNavigationPath;
 *   Lnet/minecraft/client/sound/PositionedSoundInstance;ui(Lnet/minecraft/registry/entry/RegistryEntry;F)Lnet/minecraft/client/sound/PositionedSoundInstance;
 *   Lnet/minecraft/client/sound/SoundManager;play(Lnet/minecraft/client/sound/SoundInstance;)Lnet/minecraft/client/sound/SoundSystem$PlayResult;
 *   Lnet/minecraft/client/gui/tooltip/TooltipState;appendNarrations(Lnet/minecraft/client/gui/screen/narration/NarrationMessageBuilder;)V
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/widget/ClickableWidget;renderWidget(Lnet/minecraft/client/gui/DrawContext;IIF)V
 *   Lnet/minecraft/client/gui/widget/ClickableWidget;playDownSound(Lnet/minecraft/client/sound/SoundManager;)V
 *   Lnet/minecraft/client/gui/widget/ClickableWidget;onClick(Lnet/minecraft/client/gui/Click;Z)V
 *   Lnet/minecraft/client/gui/widget/ClickableWidget;onRelease(Lnet/minecraft/client/gui/Click;)V
 *   Lnet/minecraft/client/gui/widget/ClickableWidget;onDrag(Lnet/minecraft/client/gui/Click;DD)V
 *   Lnet/minecraft/client/gui/widget/ClickableWidget;playClickSound(Lnet/minecraft/client/sound/SoundManager;)V
 *   Lnet/minecraft/client/gui/widget/ClickableWidget;appendClickableNarrations(Lnet/minecraft/client/gui/screen/narration/NarrationMessageBuilder;)V
 */
package net.minecraft.client.gui.widget;

import java.time.Duration;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.DrawnTextConsumer;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.cursor.StandardCursors;
import net.minecraft.client.gui.navigation.GuiNavigation;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.tooltip.TooltipState;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.input.MouseInput;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Colors;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public abstract class ClickableWidget
implements Drawable,
Element,
Widget,
Selectable {
    protected int width;
    protected int height;
    private int x;
    private int y;
    protected Text message;
    protected boolean hovered;
    public boolean active = true;
    public boolean visible = true;
    protected float alpha = 1.0f;
    private int navigationOrder;
    private boolean focused;
    private final TooltipState tooltip = new TooltipState();

    public ClickableWidget(int x, int y, int width, int height, Text message) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.message = message;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public final void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        if (!this.visible) {
            return;
        }
        this.hovered = context.scissorContains(mouseX, mouseY) && this.isInBounds(mouseX, mouseY);
        this.renderWidget(context, mouseX, mouseY, deltaTicks);
        this.tooltip.render(context, mouseX, mouseY, this.isHovered(), this.isFocused(), this.getNavigationFocus());
    }

    protected void setCursor(DrawContext context) {
        if (this.isHovered()) {
            context.setCursor(this.isInteractable() ? StandardCursors.POINTING_HAND : StandardCursors.NOT_ALLOWED);
        }
    }

    public void setTooltip(@Nullable Tooltip tooltip) {
        this.tooltip.setTooltip(tooltip);
    }

    public void setTooltipDelay(Duration tooltipDelay) {
        this.tooltip.setDelay(tooltipDelay);
    }

    protected MutableText getNarrationMessage() {
        return ClickableWidget.getNarrationMessage(this.getMessage());
    }

    public static MutableText getNarrationMessage(Text message) {
        return Text.translatable("gui.narrate.button", message);
    }

    protected abstract void renderWidget(DrawContext var1, int var2, int var3, float var4);

    protected void drawTextWithMargin(DrawnTextConsumer drawer, Text text, int marginX) {
        int j = this.getX() + marginX;
        int k = this.getX() + this.getWidth() - marginX;
        int l = this.getY();
        int m = this.getY() + this.getHeight();
        drawer.text(text, j, k, l, m);
    }

    public void onClick(Click click, boolean doubled) {
    }

    public void onRelease(Click click) {
    }

    protected void onDrag(Click click, double offsetX, double offsetY) {
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        boolean bl2;
        if (!this.isInteractable()) {
            return false;
        }
        if (this.isValidClickButton(click.buttonInfo()) && (bl2 = this.isMouseOver(click.x(), click.y()))) {
            this.playDownSound(MinecraftClient.getInstance().getSoundManager());
            this.onClick(click, doubled);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(Click click) {
        if (this.isValidClickButton(click.buttonInfo())) {
            this.onRelease(click);
            return true;
        }
        return false;
    }

    protected boolean isValidClickButton(MouseInput input) {
        return input.button() == 0;
    }

    @Override
    public boolean mouseDragged(Click click, double offsetX, double offsetY) {
        if (this.isValidClickButton(click.buttonInfo())) {
            this.onDrag(click, offsetX, offsetY);
            return true;
        }
        return false;
    }

    @Override
    public @Nullable GuiNavigationPath getNavigationPath(GuiNavigation navigation) {
        if (!this.isInteractable()) {
            return null;
        }
        if (!this.isFocused()) {
            return GuiNavigationPath.of(this);
        }
        return null;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.isInteractable() && this.isInBounds(mouseX, mouseY);
    }

    public void playDownSound(SoundManager soundManager) {
        ClickableWidget.playClickSound(soundManager);
    }

    public static void playClickSound(SoundManager soundManager) {
        soundManager.play(PositionedSoundInstance.ui(SoundEvents.UI_BUTTON_CLICK, 1.0f));
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public float getAlpha() {
        return this.alpha;
    }

    public void setMessage(Text message) {
        this.message = message;
    }

    public Text getMessage() {
        return this.message;
    }

    @Override
    public boolean isFocused() {
        return this.focused;
    }

    public boolean isHovered() {
        return this.hovered;
    }

    public boolean isSelected() {
        return this.isHovered() || this.isFocused();
    }

    @Override
    public boolean isInteractable() {
        return this.visible && this.active;
    }

    @Override
    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    @Override
    public Selectable.SelectionType getType() {
        if (this.isFocused()) {
            return Selectable.SelectionType.FOCUSED;
        }
        if (this.hovered) {
            return Selectable.SelectionType.HOVERED;
        }
        return Selectable.SelectionType.NONE;
    }

    @Override
    public final void appendNarrations(NarrationMessageBuilder builder) {
        this.appendClickableNarrations(builder);
        this.tooltip.appendNarrations(builder);
    }

    protected abstract void appendClickableNarrations(NarrationMessageBuilder var1);

    protected void appendDefaultNarrations(NarrationMessageBuilder builder) {
        builder.put(NarrationPart.TITLE, (Text)this.getNarrationMessage());
        if (this.active) {
            if (this.isFocused()) {
                builder.put(NarrationPart.USAGE, (Text)Text.translatable("narration.button.usage.focused"));
            } else {
                builder.put(NarrationPart.USAGE, (Text)Text.translatable("narration.button.usage.hovered"));
            }
        }
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public void setX(int i) {
        this.x = i;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    public int getRight() {
        return this.getX() + this.getWidth();
    }

    public int getBottom() {
        return this.getY() + this.getHeight();
    }

    @Override
    public void forEachChild(Consumer<ClickableWidget> consumer) {
        consumer.accept(this);
    }

    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public ScreenRect getNavigationFocus() {
        return Widget.super.getNavigationFocus();
    }

    private boolean isInBounds(double x, double y) {
        return x >= (double)this.getX() && y >= (double)this.getY() && x < (double)this.getRight() && y < (double)this.getBottom();
    }

    public void setDimensionsAndPosition(int width, int height, int x, int y) {
        this.setDimensions(width, height);
        this.setPosition(x, y);
    }

    @Override
    public int getNavigationOrder() {
        return this.navigationOrder;
    }

    public void setNavigationOrder(int navigationOrder) {
        this.navigationOrder = navigationOrder;
    }

    @Environment(value=EnvType.CLIENT)
    public static abstract class InactivityIndicatingWidget
    extends ClickableWidget {
        private Text inactiveMessage;

        public static Text makeInactive(Text text) {
            return Texts.withStyle(text, Style.EMPTY.withColor(Colors.LIGHT_GRAY));
        }

        public InactivityIndicatingWidget(int i, int j, int k, int l, Text arg) {
            super(i, j, k, l, arg);
            this.inactiveMessage = InactivityIndicatingWidget.makeInactive(arg);
        }

        @Override
        public Text getMessage() {
            return this.active ? super.getMessage() : this.inactiveMessage;
        }

        @Override
        public void setMessage(Text message) {
            super.setMessage(message);
            this.inactiveMessage = InactivityIndicatingWidget.makeInactive(message);
        }
    }
}

