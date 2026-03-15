/*
 * External method calls:
 *   Lnet/minecraft/client/gui/navigation/GuiNavigation$Arrow;direction()Lnet/minecraft/client/gui/navigation/NavigationDirection;
 *   Lnet/minecraft/client/gui/navigation/GuiNavigationPath;of(Lnet/minecraft/client/gui/ParentElement;Lnet/minecraft/client/gui/navigation/GuiNavigationPath;)Lnet/minecraft/client/gui/navigation/GuiNavigationPath;
 *   Lnet/minecraft/client/gui/widget/ElementListWidget$Entry;children()Ljava/util/List;
 *   Lnet/minecraft/client/gui/screen/narration/NarrationMessageBuilder;nextMessage()Lnet/minecraft/client/gui/screen/narration/NarrationMessageBuilder;
 *   Lnet/minecraft/client/gui/widget/ElementListWidget$Entry;appendNarrations(Lnet/minecraft/client/gui/screen/narration/NarrationMessageBuilder;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/widget/ElementListWidget;appendNarrations(Lnet/minecraft/client/gui/screen/narration/NarrationMessageBuilder;Lnet/minecraft/client/gui/widget/EntryListWidget$Entry;)V
 */
package net.minecraft.client.gui.widget;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ParentElement;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.navigation.GuiNavigation;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.navigation.NavigationAxis;
import net.minecraft.client.gui.navigation.NavigationDirection;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public abstract class ElementListWidget<E extends Entry<E>>
extends EntryListWidget<E> {
    public ElementListWidget(MinecraftClient arg, int i, int j, int k, int l) {
        super(arg, i, j, k, l);
    }

    @Override
    public @Nullable GuiNavigationPath getNavigationPath(GuiNavigation navigation) {
        if (this.getEntryCount() == 0) {
            return null;
        }
        if (navigation instanceof GuiNavigation.Arrow) {
            GuiNavigationPath lv5;
            GuiNavigation.Arrow lv = (GuiNavigation.Arrow)navigation;
            Entry lv2 = (Entry)this.getFocused();
            if (lv.direction().getAxis() == NavigationAxis.HORIZONTAL && lv2 != null) {
                return GuiNavigationPath.of(this, lv2.getNavigationPath(navigation));
            }
            int i = -1;
            NavigationDirection lv3 = lv.direction();
            if (lv2 != null) {
                i = lv2.children().indexOf(lv2.getFocused());
            }
            if (i == -1) {
                switch (lv3) {
                    case LEFT: {
                        i = Integer.MAX_VALUE;
                        lv3 = NavigationDirection.DOWN;
                        break;
                    }
                    case RIGHT: {
                        i = 0;
                        lv3 = NavigationDirection.DOWN;
                        break;
                    }
                    default: {
                        i = 0;
                    }
                }
            }
            Entry lv4 = lv2;
            do {
                if ((lv4 = this.getNeighboringEntry(lv3, element -> !element.children().isEmpty(), lv4)) != null) continue;
                return null;
            } while ((lv5 = lv4.getNavigationPath(lv, i)) == null);
            return GuiNavigationPath.of(this, lv5);
        }
        return super.getNavigationPath(navigation);
    }

    @Override
    public void setFocused(@Nullable Element focused) {
        if (this.getFocused() == focused) {
            return;
        }
        super.setFocused(focused);
        if (focused == null) {
            this.setSelected(null);
        }
    }

    @Override
    public Selectable.SelectionType getType() {
        if (this.isFocused()) {
            return Selectable.SelectionType.FOCUSED;
        }
        return super.getType();
    }

    @Override
    protected boolean isEntrySelectionAllowed() {
        return false;
    }

    @Override
    public void appendClickableNarrations(NarrationMessageBuilder builder) {
        Object object = this.getHoveredEntry();
        if (object instanceof Entry) {
            Entry lv = (Entry)object;
            lv.appendNarrations(builder.nextMessage());
            this.appendNarrations(builder, lv);
        } else {
            object = this.getFocused();
            if (object instanceof Entry) {
                Entry lv2 = (Entry)object;
                lv2.appendNarrations(builder.nextMessage());
                this.appendNarrations(builder, lv2);
            }
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static abstract class Entry<E extends Entry<E>>
    extends EntryListWidget.Entry<E>
    implements ParentElement {
        private @Nullable Element focused;
        private @Nullable Selectable focusedSelectable;
        private boolean dragging;

        @Override
        public boolean isDragging() {
            return this.dragging;
        }

        @Override
        public void setDragging(boolean dragging) {
            this.dragging = dragging;
        }

        @Override
        public boolean mouseClicked(Click click, boolean doubled) {
            return ParentElement.super.mouseClicked(click, doubled);
        }

        @Override
        public void setFocused(@Nullable Element focused) {
            if (this.focused != null) {
                this.focused.setFocused(false);
            }
            if (focused != null) {
                focused.setFocused(true);
            }
            this.focused = focused;
        }

        @Override
        public @Nullable Element getFocused() {
            return this.focused;
        }

        public @Nullable GuiNavigationPath getNavigationPath(GuiNavigation navigation, int index) {
            if (this.children().isEmpty()) {
                return null;
            }
            GuiNavigationPath lv = this.children().get(Math.min(index, this.children().size() - 1)).getNavigationPath(navigation);
            return GuiNavigationPath.of(this, lv);
        }

        @Override
        public @Nullable GuiNavigationPath getNavigationPath(GuiNavigation navigation) {
            if (navigation instanceof GuiNavigation.Arrow) {
                int j;
                int i;
                GuiNavigation.Arrow lv = (GuiNavigation.Arrow)navigation;
                switch (lv.direction()) {
                    default: {
                        throw new MatchException(null, null);
                    }
                    case UP: 
                    case DOWN: {
                        int n = 0;
                        break;
                    }
                    case LEFT: {
                        int n = -1;
                        break;
                    }
                    case RIGHT: {
                        int n = i = 1;
                    }
                }
                if (i == 0) {
                    return null;
                }
                for (int k = j = MathHelper.clamp(i + this.children().indexOf(this.getFocused()), 0, this.children().size() - 1); k >= 0 && k < this.children().size(); k += i) {
                    Element lv2 = this.children().get(k);
                    GuiNavigationPath lv3 = lv2.getNavigationPath(navigation);
                    if (lv3 == null) continue;
                    return GuiNavigationPath.of(this, lv3);
                }
            }
            return ParentElement.super.getNavigationPath(navigation);
        }

        public abstract List<? extends Selectable> selectableChildren();

        void appendNarrations(NarrationMessageBuilder builder) {
            List<Selectable> list = this.selectableChildren();
            Screen.SelectedElementNarrationData lv = Screen.findSelectedElementData(list, this.focusedSelectable);
            if (lv != null) {
                if (lv.selectType().isFocused()) {
                    this.focusedSelectable = lv.selectable();
                }
                if (list.size() > 1) {
                    builder.put(NarrationPart.POSITION, (Text)Text.translatable("narrator.position.object_list", lv.index() + 1, list.size()));
                }
                lv.selectable().appendNarrations(builder.nextMessage());
            }
        }
    }
}

