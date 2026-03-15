/*
 * External method calls:
 *   Lnet/minecraft/client/gui/screen/dialog/DialogBodyHandler;createWidget(Lnet/minecraft/client/gui/screen/dialog/DialogScreen;Lnet/minecraft/dialog/body/DialogBody;)Lnet/minecraft/client/gui/widget/Widget;
 *   Lnet/minecraft/client/gui/screen/dialog/DialogScreen;runAction(Ljava/util/Optional;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/screen/dialog/DialogBodyHandlers;register(Lcom/mojang/serialization/MapCodec;Lnet/minecraft/client/gui/screen/dialog/DialogBodyHandler;)V
 */
package net.minecraft.client.gui.screen.dialog;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.dialog.DialogBodyHandler;
import net.minecraft.client.gui.screen.dialog.DialogScreen;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.ItemStackWidget;
import net.minecraft.client.gui.widget.NarratedMultilineTextWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.dialog.body.DialogBody;
import net.minecraft.dialog.body.ItemDialogBody;
import net.minecraft.dialog.body.PlainMessageDialogBody;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(value=EnvType.CLIENT)
public class DialogBodyHandlers {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Map<MapCodec<? extends DialogBody>, DialogBodyHandler<?>> DIALOG_BODY_HANDLERS = new HashMap();

    private static <B extends DialogBody> void register(MapCodec<B> dialogBodyCodec, DialogBodyHandler<? super B> dialogBodyHandler) {
        DIALOG_BODY_HANDLERS.put(dialogBodyCodec, dialogBodyHandler);
    }

    private static <B extends DialogBody> @Nullable DialogBodyHandler<B> getHandler(B dialogBody) {
        return DIALOG_BODY_HANDLERS.get(dialogBody.getTypeCodec());
    }

    public static <B extends DialogBody> @Nullable Widget createWidget(DialogScreen<?> dialogScreen, B dialogBody) {
        DialogBodyHandler<B> lv = DialogBodyHandlers.getHandler(dialogBody);
        if (lv == null) {
            LOGGER.warn("Unrecognized dialog body {}", (Object)dialogBody);
            return null;
        }
        return lv.createWidget(dialogScreen, dialogBody);
    }

    public static void bootstrap() {
        DialogBodyHandlers.register(PlainMessageDialogBody.CODEC, new PlainMessageDialogBodyHandler());
        DialogBodyHandlers.register(ItemDialogBody.CODEC, new ItemDialogBodyHandler());
    }

    static void runActionFromStyle(DialogScreen<?> dialogScreen, @Nullable Style style) {
        ClickEvent lv;
        if (style != null && (lv = style.getClickEvent()) != null) {
            dialogScreen.runAction(Optional.of(lv));
        }
    }

    @Environment(value=EnvType.CLIENT)
    static class PlainMessageDialogBodyHandler
    implements DialogBodyHandler<PlainMessageDialogBody> {
        PlainMessageDialogBodyHandler() {
        }

        @Override
        public Widget createWidget(DialogScreen<?> arg, PlainMessageDialogBody arg2) {
            return NarratedMultilineTextWidget.builder(arg2.contents(), arg.getTextRenderer()).width(arg2.width()).alwaysShowBorders(false).backgroundRendering(NarratedMultilineTextWidget.BackgroundRendering.NEVER).build().setCentered(true).onClick(style -> DialogBodyHandlers.runActionFromStyle(arg, style));
        }
    }

    @Environment(value=EnvType.CLIENT)
    static class ItemDialogBodyHandler
    implements DialogBodyHandler<ItemDialogBody> {
        ItemDialogBodyHandler() {
        }

        @Override
        public Widget createWidget(DialogScreen<?> arg, ItemDialogBody arg2) {
            if (arg2.description().isPresent()) {
                PlainMessageDialogBody lv = arg2.description().get();
                DirectionalLayoutWidget lv2 = DirectionalLayoutWidget.horizontal().spacing(2);
                lv2.getMainPositioner().alignVerticalCenter();
                ItemStackWidget lv3 = new ItemStackWidget(MinecraftClient.getInstance(), 0, 0, arg2.width(), arg2.height(), ScreenTexts.EMPTY, arg2.item(), arg2.showDecorations(), arg2.showTooltip());
                lv2.add(lv3);
                lv2.add(NarratedMultilineTextWidget.builder(lv.contents(), arg.getTextRenderer()).width(lv.width()).alwaysShowBorders(false).backgroundRendering(NarratedMultilineTextWidget.BackgroundRendering.NEVER).build().onClick(style -> DialogBodyHandlers.runActionFromStyle(arg, style)));
                return lv2;
            }
            return new ItemStackWidget(MinecraftClient.getInstance(), 0, 0, arg2.width(), arg2.height(), arg2.item().getName(), arg2.item(), arg2.showDecorations(), arg2.showTooltip());
        }
    }
}

