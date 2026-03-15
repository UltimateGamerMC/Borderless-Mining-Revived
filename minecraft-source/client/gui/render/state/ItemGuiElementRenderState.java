/*
 * External method calls:
 *   Lnet/minecraft/client/gui/ScreenRect;transformEachVertex(Lorg/joml/Matrix3x2fc;)Lnet/minecraft/client/gui/ScreenRect;
 *   Lnet/minecraft/client/gui/ScreenRect;intersection(Lnet/minecraft/client/gui/ScreenRect;)Lnet/minecraft/client/gui/ScreenRect;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/render/state/ItemGuiElementRenderState;state()Lnet/minecraft/client/render/item/KeyedItemRenderState;
 *   Lnet/minecraft/client/gui/render/state/ItemGuiElementRenderState;createOversizedBounds()Lnet/minecraft/client/gui/ScreenRect;
 *   Lnet/minecraft/client/gui/render/state/ItemGuiElementRenderState;createBounds(Lnet/minecraft/client/gui/ScreenRect;)Lnet/minecraft/client/gui/ScreenRect;
 */
package net.minecraft.client.gui.render.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.render.state.GuiElementRenderState;
import net.minecraft.client.render.item.KeyedItemRenderState;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix3x2f;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public final class ItemGuiElementRenderState
implements GuiElementRenderState {
    private final String name;
    private final Matrix3x2f pose;
    private final KeyedItemRenderState state;
    private final int x;
    private final int y;
    private final @Nullable ScreenRect scissorArea;
    private final @Nullable ScreenRect oversizedBounds;
    private final @Nullable ScreenRect bounds;

    public ItemGuiElementRenderState(String name, Matrix3x2f pose, KeyedItemRenderState state, int x, int y, @Nullable ScreenRect scissor) {
        this.name = name;
        this.pose = pose;
        this.state = state;
        this.x = x;
        this.y = y;
        this.scissorArea = scissor;
        this.oversizedBounds = this.state().isOversizedInGui() ? this.createOversizedBounds() : null;
        this.bounds = this.createBounds(this.oversizedBounds != null ? this.oversizedBounds : new ScreenRect(this.x, this.y, 16, 16));
    }

    private @Nullable ScreenRect createOversizedBounds() {
        Box lv = this.state.getModelBoundingBox();
        int i = MathHelper.ceil(lv.getLengthX() * 16.0);
        int j = MathHelper.ceil(lv.getLengthY() * 16.0);
        if (i > 16 || j > 16) {
            float f = (float)(lv.minX * 16.0);
            float g = (float)(lv.maxY * 16.0);
            int k = MathHelper.floor(f);
            int l = MathHelper.floor(g);
            int m = this.x + k + 8;
            int n = this.y - l + 8;
            return new ScreenRect(m, n, i, j);
        }
        return null;
    }

    private @Nullable ScreenRect createBounds(ScreenRect rect) {
        ScreenRect lv = rect.transformEachVertex(this.pose);
        return this.scissorArea != null ? this.scissorArea.intersection(lv) : lv;
    }

    public String name() {
        return this.name;
    }

    public Matrix3x2f pose() {
        return this.pose;
    }

    public KeyedItemRenderState state() {
        return this.state;
    }

    public int x() {
        return this.x;
    }

    public int y() {
        return this.y;
    }

    public @Nullable ScreenRect scissorArea() {
        return this.scissorArea;
    }

    public @Nullable ScreenRect oversizedBounds() {
        return this.oversizedBounds;
    }

    @Override
    public @Nullable ScreenRect bounds() {
        return this.bounds;
    }
}

