/*
 * Internal private/static methods:
 *   Lnet/minecraft/dialog/type/ColumnsDialog;exitAction()Ljava/util/Optional;
 */
package net.minecraft.dialog.type;

import com.mojang.serialization.MapCodec;
import java.util.Optional;
import net.minecraft.dialog.DialogActionButtonData;
import net.minecraft.dialog.action.DialogAction;
import net.minecraft.dialog.type.Dialog;

public interface ColumnsDialog
extends Dialog {
    public MapCodec<? extends ColumnsDialog> getCodec();

    public int columns();

    public Optional<DialogActionButtonData> exitAction();

    @Override
    default public Optional<DialogAction> getCancelAction() {
        return this.exitAction().flatMap(DialogActionButtonData::action);
    }
}

