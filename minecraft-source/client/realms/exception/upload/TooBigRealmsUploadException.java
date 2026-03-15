/*
 * External method calls:
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/realms/SizeUnit;humanReadableSize(JLnet/minecraft/client/realms/SizeUnit;)Ljava/lang/String;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 */
package net.minecraft.client.realms.exception.upload;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.realms.SizeUnit;
import net.minecraft.client.realms.exception.RealmsUploadException;
import net.minecraft.text.Text;

@Environment(value=EnvType.CLIENT)
public class TooBigRealmsUploadException
extends RealmsUploadException {
    final long maxSizeInBytes;

    public TooBigRealmsUploadException(long maxSizeInBytes) {
        this.maxSizeInBytes = maxSizeInBytes;
    }

    @Override
    public Text[] getStatusTexts() {
        return new Text[]{Text.translatable("mco.upload.failed.too_big.title"), Text.translatable("mco.upload.failed.too_big.description", SizeUnit.humanReadableSize(this.maxSizeInBytes, SizeUnit.getLargestUnit(this.maxSizeInBytes)))};
    }
}

