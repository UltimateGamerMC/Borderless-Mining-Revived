/*
 * External method calls:
 *   Lnet/minecraft/client/gui/screen/dialog/DialogScreen;initHeaderAndFooter(Lnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;Lnet/minecraft/client/gui/screen/dialog/DialogControls;Lnet/minecraft/dialog/type/Dialog;Lnet/minecraft/client/gui/screen/dialog/DialogNetworkAccess;)V
 *   Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;horizontal()Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;
 *   Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;spacing(I)Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;
 *   Lnet/minecraft/client/gui/screen/dialog/DialogControls;createButton(Lnet/minecraft/dialog/DialogActionButtonData;)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;build()Lnet/minecraft/client/gui/widget/ButtonWidget;
 *   Lnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;addFooter(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/screen/dialog/SimpleDialogScreen;initHeaderAndFooter(Lnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;Lnet/minecraft/client/gui/screen/dialog/DialogControls;Lnet/minecraft/dialog/type/SimpleDialog;Lnet/minecraft/client/gui/screen/dialog/DialogNetworkAccess;)V
 */
package net.minecraft.client.gui.screen.dialog;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.dialog.DialogControls;
import net.minecraft.client.gui.screen.dialog.DialogNetworkAccess;
import net.minecraft.client.gui.screen.dialog.DialogScreen;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
import net.minecraft.dialog.DialogActionButtonData;
import net.minecraft.dialog.type.SimpleDialog;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class SimpleDialogScreen<T extends SimpleDialog>
extends DialogScreen<T> {
    public SimpleDialogScreen(@Nullable Screen parent, T dialog, DialogNetworkAccess networkAccess) {
        super(parent, dialog, networkAccess);
    }

    @Override
    protected void initHeaderAndFooter(ThreePartsLayoutWidget arg, DialogControls arg2, T arg3, DialogNetworkAccess arg4) {
        super.initHeaderAndFooter(arg, arg2, arg3, arg4);
        DirectionalLayoutWidget lv = DirectionalLayoutWidget.horizontal().spacing(8);
        for (DialogActionButtonData lv2 : arg3.getButtons()) {
            lv.add(arg2.createButton(lv2).build());
        }
        arg.addFooter(lv);
    }
}

