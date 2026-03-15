/*
 * Internal private/static methods:
 *   Lnet/minecraft/client/input/CursorMovement;method_44446()[Lnet/minecraft/client/input/CursorMovement;
 */
package net.minecraft.client.input;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public enum CursorMovement {
    ABSOLUTE,
    RELATIVE,
    END;

}

