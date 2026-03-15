/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeNullable(Ljava/lang/Object;Lnet/minecraft/network/codec/PacketEncoder;)V
 *   Lnet/minecraft/network/PacketByteBuf;readNullable(Lnet/minecraft/network/codec/PacketDecoder;)Ljava/lang/Object;
 */
package net.minecraft.advancement.criterion;

import java.time.Instant;
import net.minecraft.network.PacketByteBuf;
import org.jspecify.annotations.Nullable;

public class CriterionProgress {
    private @Nullable Instant obtainedTime;

    public CriterionProgress() {
    }

    public CriterionProgress(Instant obtainedTime) {
        this.obtainedTime = obtainedTime;
    }

    public boolean isObtained() {
        return this.obtainedTime != null;
    }

    public void obtain() {
        this.obtainedTime = Instant.now();
    }

    public void reset() {
        this.obtainedTime = null;
    }

    public @Nullable Instant getObtainedTime() {
        return this.obtainedTime;
    }

    public String toString() {
        return "CriterionProgress{obtained=" + String.valueOf(this.obtainedTime == null ? "false" : this.obtainedTime) + "}";
    }

    public void toPacket(PacketByteBuf buf) {
        buf.writeNullable(this.obtainedTime, PacketByteBuf::writeInstant);
    }

    public static CriterionProgress fromPacket(PacketByteBuf buf) {
        CriterionProgress lv = new CriterionProgress();
        lv.obtainedTime = buf.readNullable(PacketByteBuf::readInstant);
        return lv;
    }
}

