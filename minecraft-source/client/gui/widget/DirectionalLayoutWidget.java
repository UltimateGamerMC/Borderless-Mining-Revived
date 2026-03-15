/*
 * External method calls:
 *   Lnet/minecraft/client/gui/widget/GridWidget;copyPositioner()Lnet/minecraft/client/gui/widget/Positioner;
 *   Lnet/minecraft/util/Util;make(Ljava/lang/Object;Ljava/util/function/Consumer;)Ljava/lang/Object;
 *   Lnet/minecraft/client/gui/widget/GridWidget;forEachElement(Ljava/util/function/Consumer;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;copyPositioner()Lnet/minecraft/client/gui/widget/Positioner;
 */
package net.minecraft.client.gui.widget;

import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.LayoutWidget;
import net.minecraft.client.gui.widget.Positioner;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.Util;

@Environment(value=EnvType.CLIENT)
public class DirectionalLayoutWidget
implements LayoutWidget {
    private final GridWidget grid;
    private final DisplayAxis axis;
    private int currentIndex = 0;

    private DirectionalLayoutWidget(DisplayAxis axis) {
        this(0, 0, axis);
    }

    public DirectionalLayoutWidget(int x, int y, DisplayAxis axis) {
        this.grid = new GridWidget(x, y);
        this.axis = axis;
    }

    public DirectionalLayoutWidget spacing(int spacing) {
        this.axis.setSpacing(this.grid, spacing);
        return this;
    }

    public Positioner copyPositioner() {
        return this.grid.copyPositioner();
    }

    public Positioner getMainPositioner() {
        return this.grid.getMainPositioner();
    }

    public <T extends Widget> T add(T widget, Positioner positioner) {
        return this.axis.add(this.grid, widget, this.currentIndex++, positioner);
    }

    public <T extends Widget> T add(T widget) {
        return this.add(widget, this.copyPositioner());
    }

    public <T extends Widget> T add(T widget, Consumer<Positioner> callback) {
        return this.axis.add(this.grid, widget, this.currentIndex++, Util.make(this.copyPositioner(), callback));
    }

    @Override
    public void forEachElement(Consumer<Widget> consumer) {
        this.grid.forEachElement(consumer);
    }

    @Override
    public void refreshPositions() {
        this.grid.refreshPositions();
    }

    @Override
    public int getWidth() {
        return this.grid.getWidth();
    }

    @Override
    public int getHeight() {
        return this.grid.getHeight();
    }

    @Override
    public void setX(int i) {
        this.grid.setX(i);
    }

    @Override
    public void setY(int y) {
        this.grid.setY(y);
    }

    @Override
    public int getX() {
        return this.grid.getX();
    }

    @Override
    public int getY() {
        return this.grid.getY();
    }

    public static DirectionalLayoutWidget vertical() {
        return new DirectionalLayoutWidget(DisplayAxis.VERTICAL);
    }

    public static DirectionalLayoutWidget horizontal() {
        return new DirectionalLayoutWidget(DisplayAxis.HORIZONTAL);
    }

    @Environment(value=EnvType.CLIENT)
    public static enum DisplayAxis {
        HORIZONTAL,
        VERTICAL;


        void setSpacing(GridWidget grid, int spacing) {
            switch (this.ordinal()) {
                case 0: {
                    grid.setColumnSpacing(spacing);
                    break;
                }
                case 1: {
                    grid.setRowSpacing(spacing);
                }
            }
        }

        public <T extends Widget> T add(GridWidget grid, T widget, int index, Positioner positioner) {
            return switch (this.ordinal()) {
                default -> throw new MatchException(null, null);
                case 0 -> grid.add(widget, 0, index, positioner);
                case 1 -> grid.add(widget, index, 0, positioner);
            };
        }
    }
}

