/*
 * External method calls:
 *   Lnet/minecraft/client/gui/render/state/ItemGuiElementRenderState;pose()Lorg/joml/Matrix3x2f;
 *   Lnet/minecraft/client/gui/render/state/ItemGuiElementRenderState;scissorArea()Lnet/minecraft/client/gui/ScreenRect;
 *   Lnet/minecraft/client/gui/render/state/ItemGuiElementRenderState;bounds()Lnet/minecraft/client/gui/ScreenRect;
 */
package net.minecraft.client.gui.render.state.special;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.render.state.ItemGuiElementRenderState;
import net.minecraft.client.gui.render.state.special.SpecialGuiElementRenderState;
import org.joml.Matrix3x2f;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public record OversizedItemGuiElementRenderState(ItemGuiElementRenderState guiItemRenderState, int x1, int y1, int x2, int y2) implements SpecialGuiElementRenderState
{
    @Override
    public float scale() {
        return 16.0f;
    }

    @Override
    public Matrix3x2f pose() {
        return this.guiItemRenderState.pose();
    }

    @Override
    public @Nullable ScreenRect scissorArea() {
        return this.guiItemRenderState.scissorArea();
    }

    @Override
    public @Nullable ScreenRect bounds() {
        return this.guiItemRenderState.bounds();
    }
}

