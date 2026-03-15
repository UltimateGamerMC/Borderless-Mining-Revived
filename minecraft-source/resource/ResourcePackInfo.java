/*
 * External method calls:
 *   Lnet/minecraft/text/Text;literal(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/resource/ResourcePackSource;decorate(Lnet/minecraft/text/Text;)Lnet/minecraft/text/Text;
 *   Lnet/minecraft/text/Texts;bracketed(Lnet/minecraft/text/Text;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/MutableText;styled(Ljava/util/function/UnaryOperator;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Style;withColor(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/Style;
 *   Lnet/minecraft/text/Style;withInsertion(Ljava/lang/String;)Lnet/minecraft/text/Style;
 *   Lnet/minecraft/text/Text;empty()Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/MutableText;append(Lnet/minecraft/text/Text;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/MutableText;append(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Style;withHoverEvent(Lnet/minecraft/text/HoverEvent;)Lnet/minecraft/text/Style;
 */
package net.minecraft.resource;

import com.mojang.brigadier.arguments.StringArgumentType;
import java.util.Optional;
import net.minecraft.registry.VersionedIdentifier;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;

public record ResourcePackInfo(String id, Text title, ResourcePackSource source, Optional<VersionedIdentifier> knownPackInfo) {
    public Text getInformationText(boolean enabled, Text description) {
        return Texts.bracketed(this.source.decorate(Text.literal(this.id))).styled(style -> style.withColor(enabled ? Formatting.GREEN : Formatting.RED).withInsertion(StringArgumentType.escapeIfRequired(this.id)).withHoverEvent(new HoverEvent.ShowText(Text.empty().append(this.title).append("\n").append(description))));
    }
}

