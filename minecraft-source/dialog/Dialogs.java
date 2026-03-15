/*
 * External method calls:
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/registry/RegistryKey;of(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/util/Identifier;)Lnet/minecraft/registry/RegistryKey;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/registry/Registerable;register(Lnet/minecraft/registry/RegistryKey;Ljava/lang/Object;)Lnet/minecraft/registry/entry/RegistryEntry$Reference;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/dialog/Dialogs;of(Ljava/lang/String;)Lnet/minecraft/registry/RegistryKey;
 */
package net.minecraft.dialog;

import java.util.List;
import java.util.Optional;
import net.minecraft.dialog.AfterAction;
import net.minecraft.dialog.DialogActionButtonData;
import net.minecraft.dialog.DialogButtonData;
import net.minecraft.dialog.DialogCommonData;
import net.minecraft.dialog.type.Dialog;
import net.minecraft.dialog.type.DialogListDialog;
import net.minecraft.dialog.type.ServerLinksDialog;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.DialogTags;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class Dialogs {
    public static final RegistryKey<Dialog> SERVER_LINKS = Dialogs.of("server_links");
    public static final RegistryKey<Dialog> CUSTOM_OPTIONS = Dialogs.of("custom_options");
    public static final RegistryKey<Dialog> QUICK_ACTIONS = Dialogs.of("quick_actions");
    public static final int BUTTON_WIDTH = 310;
    private static final DialogActionButtonData BACK_BUTTON = new DialogActionButtonData(new DialogButtonData(ScreenTexts.BACK, 200), Optional.empty());

    private static RegistryKey<Dialog> of(String id) {
        return RegistryKey.of(RegistryKeys.DIALOG, Identifier.ofVanilla(id));
    }

    public static void bootstrap(Registerable<Dialog> registry) {
        RegistryEntryLookup<Dialog> lv = registry.getRegistryLookup(RegistryKeys.DIALOG);
        registry.register(SERVER_LINKS, new ServerLinksDialog(new DialogCommonData(Text.translatable("menu.server_links.title"), Optional.of(Text.translatable("menu.server_links")), true, true, AfterAction.CLOSE, List.of(), List.of()), Optional.of(BACK_BUTTON), 1, 310));
        registry.register(CUSTOM_OPTIONS, new DialogListDialog(new DialogCommonData(Text.translatable("menu.custom_options.title"), Optional.of(Text.translatable("menu.custom_options")), true, true, AfterAction.CLOSE, List.of(), List.of()), lv.getOrThrow(DialogTags.PAUSE_SCREEN_ADDITIONS), Optional.of(BACK_BUTTON), 1, 310));
        registry.register(QUICK_ACTIONS, new DialogListDialog(new DialogCommonData(Text.translatable("menu.quick_actions.title"), Optional.of(Text.translatable("menu.quick_actions")), true, true, AfterAction.CLOSE, List.of(), List.of()), lv.getOrThrow(DialogTags.QUICK_ACTIONS), Optional.of(BACK_BUTTON), 1, 310));
    }
}

