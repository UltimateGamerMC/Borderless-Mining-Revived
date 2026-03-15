/*
 * External method calls:
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/gui/widget/TextIconButtonWidget;builder(Lnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;Z)Lnet/minecraft/client/gui/widget/TextIconButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/TextIconButtonWidget$Builder;width(I)Lnet/minecraft/client/gui/widget/TextIconButtonWidget$Builder;
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/client/gui/widget/TextIconButtonWidget$Builder;texture(Lnet/minecraft/util/Identifier;II)Lnet/minecraft/client/gui/widget/TextIconButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/TextIconButtonWidget$Builder;build()Lnet/minecraft/client/gui/widget/TextIconButtonWidget;
 */
package net.minecraft.client.gui.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextIconButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class AccessibilityOnboardingButtons {
    public static TextIconButtonWidget createLanguageButton(int width, ButtonWidget.PressAction onPress, boolean hideText) {
        return TextIconButtonWidget.builder(Text.translatable("options.language"), onPress, hideText).width(width).texture(Identifier.ofVanilla("icon/language"), 15, 15).build();
    }

    public static TextIconButtonWidget createAccessibilityButton(int width, ButtonWidget.PressAction onPress, boolean hideText) {
        MutableText lv = hideText ? Text.translatable("options.accessibility") : Text.translatable("accessibility.onboarding.accessibility.button");
        return TextIconButtonWidget.builder(lv, onPress, hideText).width(width).texture(Identifier.ofVanilla("icon/accessibility"), 15, 15).build();
    }
}

