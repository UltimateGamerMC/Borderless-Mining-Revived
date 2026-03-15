/*
 * External method calls:
 *   Lnet/minecraft/dialog/type/DialogListDialog;dialogs()Lnet/minecraft/registry/entry/RegistryEntryList;
 *   Lnet/minecraft/registry/entry/RegistryEntryList;stream()Ljava/util/stream/Stream;
 *   Lnet/minecraft/dialog/type/Dialog;common()Lnet/minecraft/dialog/DialogCommonData;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/screen/dialog/DialogListDialogScreen;streamActionButtonData(Lnet/minecraft/dialog/type/DialogListDialog;Lnet/minecraft/client/gui/screen/dialog/DialogNetworkAccess;)Ljava/util/stream/Stream;
 *   Lnet/minecraft/client/gui/screen/dialog/DialogListDialogScreen;createButton(Lnet/minecraft/dialog/type/DialogListDialog;Lnet/minecraft/registry/entry/RegistryEntry;)Lnet/minecraft/dialog/DialogActionButtonData;
 */
package net.minecraft.client.gui.screen.dialog;

import java.util.Optional;
import java.util.stream.Stream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.dialog.ColumnsDialogScreen;
import net.minecraft.client.gui.screen.dialog.DialogNetworkAccess;
import net.minecraft.dialog.DialogActionButtonData;
import net.minecraft.dialog.DialogButtonData;
import net.minecraft.dialog.action.SimpleDialogAction;
import net.minecraft.dialog.type.Dialog;
import net.minecraft.dialog.type.DialogListDialog;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.ClickEvent;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class DialogListDialogScreen
extends ColumnsDialogScreen<DialogListDialog> {
    public DialogListDialogScreen(@Nullable Screen parent, DialogListDialog dialog, DialogNetworkAccess networkAccess) {
        super(parent, dialog, networkAccess);
    }

    @Override
    protected Stream<DialogActionButtonData> streamActionButtonData(DialogListDialog arg, DialogNetworkAccess arg2) {
        return arg.dialogs().stream().map(entry -> DialogListDialogScreen.createButton(arg, entry));
    }

    private static DialogActionButtonData createButton(DialogListDialog dialog, RegistryEntry<Dialog> entry) {
        return new DialogActionButtonData(new DialogButtonData(entry.value().common().getExternalTitle(), dialog.buttonWidth()), Optional.of(new SimpleDialogAction(new ClickEvent.ShowDialog(entry))));
    }
}

