/*
 * External method calls:
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/registry/Registry;register(Lnet/minecraft/registry/Registry;Lnet/minecraft/util/Identifier;Ljava/lang/Object;)Ljava/lang/Object;
 *   Lnet/minecraft/text/ClickEvent$Action;asString()Ljava/lang/String;
 */
package net.minecraft.dialog;

import com.mojang.serialization.MapCodec;
import net.minecraft.dialog.action.DialogAction;
import net.minecraft.dialog.action.DynamicCustomDialogAction;
import net.minecraft.dialog.action.DynamicRunCommandDialogAction;
import net.minecraft.dialog.action.SimpleDialogAction;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class DialogActionTypes {
    public static MapCodec<? extends DialogAction> registerAndGetDefault(Registry<MapCodec<? extends DialogAction>> registry) {
        SimpleDialogAction.CODECS.forEach((clickEventAction, codec) -> Registry.register(registry, Identifier.ofVanilla(clickEventAction.asString()), codec));
        Registry.register(registry, Identifier.ofVanilla("dynamic/run_command"), DynamicRunCommandDialogAction.CODEC);
        return Registry.register(registry, Identifier.ofVanilla("dynamic/custom"), DynamicCustomDialogAction.CODEC);
    }
}

