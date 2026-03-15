/*
 * External method calls:
 *   Lnet/minecraft/dialog/type/MultiActionDialog;actions()Ljava/util/List;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/screen/dialog/MultiActionDialogScreen;streamActionButtonData(Lnet/minecraft/dialog/type/MultiActionDialog;Lnet/minecraft/client/gui/screen/dialog/DialogNetworkAccess;)Ljava/util/stream/Stream;
 */
package net.minecraft.client.gui.screen.dialog;

import java.util.stream.Stream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.dialog.ColumnsDialogScreen;
import net.minecraft.client.gui.screen.dialog.DialogNetworkAccess;
import net.minecraft.dialog.DialogActionButtonData;
import net.minecraft.dialog.type.MultiActionDialog;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class MultiActionDialogScreen
extends ColumnsDialogScreen<MultiActionDialog> {
    public MultiActionDialogScreen(@Nullable Screen parent, MultiActionDialog dialog, DialogNetworkAccess networkAccess) {
        super(parent, dialog, networkAccess);
    }

    @Override
    protected Stream<DialogActionButtonData> streamActionButtonData(MultiActionDialog arg, DialogNetworkAccess arg2) {
        return arg.actions().stream();
    }
}

