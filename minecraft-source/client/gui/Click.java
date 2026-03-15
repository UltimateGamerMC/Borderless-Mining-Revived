/*
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/Click;buttonInfo()Lnet/minecraft/client/input/MouseInput;
 */
package net.minecraft.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.input.AbstractInput;
import net.minecraft.client.input.MouseInput;

@Environment(value=EnvType.CLIENT)
public record Click(double x, double y, MouseInput buttonInfo) implements AbstractInput
{
    @Override
    public int getKeycode() {
        return this.button();
    }

    @MouseInput.ButtonCode
    public int button() {
        return this.buttonInfo().button();
    }

    @Override
    @AbstractInput.Modifier
    public int modifiers() {
        return this.buttonInfo().modifiers();
    }
}

