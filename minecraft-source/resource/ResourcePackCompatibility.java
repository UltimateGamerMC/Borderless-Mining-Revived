/*
 * External method calls:
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/util/dynamic/Range;minInclusive()Ljava/lang/Comparable;
 *   Lnet/minecraft/util/dynamic/Range;maxInclusive()Ljava/lang/Comparable;
 *   Lnet/minecraft/resource/PackVersion;compareTo(Lnet/minecraft/resource/PackVersion;)I
 *
 * Internal private/static methods:
 *   Lnet/minecraft/resource/ResourcePackCompatibility;method_36584()[Lnet/minecraft/resource/ResourcePackCompatibility;
 */
package net.minecraft.resource;

import net.minecraft.resource.PackVersion;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.dynamic.Range;

public enum ResourcePackCompatibility {
    TOO_OLD("old"),
    TOO_NEW("new"),
    UNKNOWN("unknown"),
    COMPATIBLE("compatible");

    public static final int field_61160 = Integer.MAX_VALUE;
    private final Text notification;
    private final Text confirmMessage;

    private ResourcePackCompatibility(String translationSuffix) {
        this.notification = Text.translatable("pack.incompatible." + translationSuffix).formatted(Formatting.GRAY);
        this.confirmMessage = Text.translatable("pack.incompatible.confirm." + translationSuffix);
    }

    public boolean isCompatible() {
        return this == COMPATIBLE;
    }

    public static ResourcePackCompatibility from(Range<PackVersion> range, PackVersion arg2) {
        if (range.minInclusive().major() == Integer.MAX_VALUE) {
            return UNKNOWN;
        }
        if (range.maxInclusive().compareTo(arg2) < 0) {
            return TOO_OLD;
        }
        if (arg2.compareTo(range.minInclusive()) < 0) {
            return TOO_NEW;
        }
        return COMPATIBLE;
    }

    public Text getNotification() {
        return this.notification;
    }

    public Text getConfirmMessage() {
        return this.confirmMessage;
    }
}

