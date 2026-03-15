/*
 * Internal private/static methods:
 *   Lnet/minecraft/loot/slot/GroupSlotSource;createCodec(Ljava/util/function/Function;)Lcom/mojang/serialization/MapCodec;
 *   Lnet/minecraft/loot/slot/GroupSlotSource;createInlineCodec(Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;
 */
package net.minecraft.loot.slot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import java.util.List;
import net.minecraft.loot.slot.CombinedSlotSource;
import net.minecraft.loot.slot.SlotSource;

public class GroupSlotSource
extends CombinedSlotSource {
    public static final MapCodec<GroupSlotSource> CODEC = GroupSlotSource.createCodec(GroupSlotSource::new);
    public static final Codec<GroupSlotSource> INLINE_CODEC = GroupSlotSource.createInlineCodec(GroupSlotSource::new);

    private GroupSlotSource(List<SlotSource> sources) {
        super(sources);
    }

    public MapCodec<GroupSlotSource> getCodec() {
        return CODEC;
    }
}

