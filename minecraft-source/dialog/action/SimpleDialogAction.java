/*
 * External method calls:
 *   Lnet/minecraft/util/Util;make(Ljava/util/function/Supplier;)Ljava/lang/Object;
 */
package net.minecraft.dialog.action;

import com.mojang.serialization.MapCodec;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.dialog.action.DialogAction;
import net.minecraft.text.ClickEvent;
import net.minecraft.util.Util;

public record SimpleDialogAction(ClickEvent value) implements DialogAction
{
    public static final Map<ClickEvent.Action, MapCodec<SimpleDialogAction>> CODECS = Util.make(() -> {
        EnumMap<ClickEvent.Action, MapCodec<SimpleDialogAction>> map = new EnumMap<ClickEvent.Action, MapCodec<SimpleDialogAction>>(ClickEvent.Action.class);
        for (ClickEvent.Action lv : (ClickEvent.Action[])ClickEvent.Action.class.getEnumConstants()) {
            if (!lv.isUserDefinable()) continue;
            MapCodec<? extends ClickEvent> mapCodec = lv.getCodec();
            map.put(lv, mapCodec.xmap(SimpleDialogAction::new, SimpleDialogAction::value));
        }
        return Collections.unmodifiableMap(map);
    });

    public MapCodec<SimpleDialogAction> getCodec() {
        return CODECS.get(this.value.getAction());
    }

    @Override
    public Optional<ClickEvent> createClickEvent(Map<String, DialogAction.ValueGetter> valueGetters) {
        return Optional.of(this.value);
    }
}

