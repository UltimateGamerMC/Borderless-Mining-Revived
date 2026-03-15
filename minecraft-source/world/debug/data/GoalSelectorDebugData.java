/*
 * External method calls:
 *   Lnet/minecraft/network/codec/PacketCodecs;toList()Lnet/minecraft/network/codec/PacketCodec$ResultFunction;
 *   Lnet/minecraft/network/codec/PacketCodec;collect(Lnet/minecraft/network/codec/PacketCodec$ResultFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/PacketCodec;
 */
package net.minecraft.world.debug.data;

import io.netty.buffer.ByteBuf;
import java.util.List;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record GoalSelectorDebugData(List<Goal> goals) {
    public static final PacketCodec<ByteBuf, GoalSelectorDebugData> PACKET_CODEC = PacketCodec.tuple(Goal.PACKET_CODEC.collect(PacketCodecs.toList()), GoalSelectorDebugData::goals, GoalSelectorDebugData::new);

    public record Goal(int priority, boolean isRunning, String name) {
        public static final PacketCodec<ByteBuf, Goal> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.VAR_INT, Goal::priority, PacketCodecs.BOOLEAN, Goal::isRunning, PacketCodecs.string(255), Goal::name, Goal::new);
    }
}

