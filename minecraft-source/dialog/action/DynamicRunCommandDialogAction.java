/*
 * External method calls:
 *   Lnet/minecraft/dialog/action/DialogAction$ValueGetter;resolveAll(Ljava/util/Map;)Ljava/util/Map;
 *   Lnet/minecraft/dialog/action/ParsedTemplate;apply(Ljava/util/Map;)Ljava/lang/String;
 */
package net.minecraft.dialog.action;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Map;
import java.util.Optional;
import net.minecraft.dialog.action.DialogAction;
import net.minecraft.dialog.action.ParsedTemplate;
import net.minecraft.text.ClickEvent;

public record DynamicRunCommandDialogAction(ParsedTemplate template) implements DialogAction
{
    public static final MapCodec<DynamicRunCommandDialogAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)ParsedTemplate.CODEC.fieldOf("template")).forGetter(DynamicRunCommandDialogAction::template)).apply((Applicative<DynamicRunCommandDialogAction, ?>)instance, DynamicRunCommandDialogAction::new));

    public MapCodec<DynamicRunCommandDialogAction> getCodec() {
        return CODEC;
    }

    @Override
    public Optional<ClickEvent> createClickEvent(Map<String, DialogAction.ValueGetter> valueGetters) {
        String string = this.template.apply(DialogAction.ValueGetter.resolveAll(valueGetters));
        return Optional.of(new ClickEvent.RunCommand(string));
    }
}

