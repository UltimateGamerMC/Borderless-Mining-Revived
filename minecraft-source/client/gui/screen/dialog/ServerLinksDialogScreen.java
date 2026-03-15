/*
 * External method calls:
 *   Lnet/minecraft/server/ServerLinks;entries()Ljava/util/List;
 *   Lnet/minecraft/server/ServerLinks$Entry;link()Ljava/net/URI;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/screen/dialog/ServerLinksDialogScreen;streamActionButtonData(Lnet/minecraft/dialog/type/ServerLinksDialog;Lnet/minecraft/client/gui/screen/dialog/DialogNetworkAccess;)Ljava/util/stream/Stream;
 *   Lnet/minecraft/client/gui/screen/dialog/ServerLinksDialogScreen;createButton(Lnet/minecraft/dialog/type/ServerLinksDialog;Lnet/minecraft/server/ServerLinks$Entry;)Lnet/minecraft/dialog/DialogActionButtonData;
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
import net.minecraft.dialog.type.ServerLinksDialog;
import net.minecraft.server.ServerLinks;
import net.minecraft.text.ClickEvent;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class ServerLinksDialogScreen
extends ColumnsDialogScreen<ServerLinksDialog> {
    public ServerLinksDialogScreen(@Nullable Screen parent, ServerLinksDialog dialog, DialogNetworkAccess networkAccess) {
        super(parent, dialog, networkAccess);
    }

    @Override
    protected Stream<DialogActionButtonData> streamActionButtonData(ServerLinksDialog arg, DialogNetworkAccess arg2) {
        return arg2.getServerLinks().entries().stream().map(entry -> ServerLinksDialogScreen.createButton(arg, entry));
    }

    private static DialogActionButtonData createButton(ServerLinksDialog dialog, ServerLinks.Entry entry) {
        return new DialogActionButtonData(new DialogButtonData(entry.getText(), dialog.buttonWidth()), Optional.of(new SimpleDialogAction(new ClickEvent.OpenUrl(entry.link()))));
    }
}

