/*
 * External method calls:
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 */
package net.minecraft.client.realms.exception.upload;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.realms.exception.RealmsUploadException;
import net.minecraft.text.Text;

@Environment(value=EnvType.CLIENT)
public class CancelledRealmsUploadException
extends RealmsUploadException {
    private static final Text STATUS_TEXT = Text.translatable("mco.upload.cancelled");

    @Override
    public Text getStatus() {
        return STATUS_TEXT;
    }
}

