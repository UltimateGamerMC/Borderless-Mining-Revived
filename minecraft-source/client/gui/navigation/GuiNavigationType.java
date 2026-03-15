/*
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/navigation/GuiNavigationType;method_48184()[Lnet/minecraft/client/gui/navigation/GuiNavigationType;
 */
package net.minecraft.client.gui.navigation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public enum GuiNavigationType {
    NONE,
    MOUSE,
    KEYBOARD_ARROW,
    KEYBOARD_TAB;


    public boolean isMouse() {
        return this == MOUSE;
    }

    public boolean isKeyboard() {
        return this == KEYBOARD_ARROW || this == KEYBOARD_TAB;
    }
}

