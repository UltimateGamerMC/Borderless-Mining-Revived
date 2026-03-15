/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;readString()Ljava/lang/String;
 *   Lnet/minecraft/network/PacketByteBuf;writeString(Ljava/lang/String;)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ServerPlayPacketListener;onCommandExecution(Lnet/minecraft/network/packet/c2s/play/CommandExecutionC2SPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/play/CommandExecutionC2SPacket;apply(Lnet/minecraft/network/listener/ServerPlayPacketListener;)V
 */
package net.minecraft.network.packet.c2s.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public record CommandExecutionC2SPacket(String command) implements Packet<ServerPlayPacketListener>
{
    public static final PacketCodec<PacketByteBuf, CommandExecutionC2SPacket> CODEC = Packet.createCodec(CommandExecutionC2SPacket::write, CommandExecutionC2SPacket::new);

    private CommandExecutionC2SPacket(PacketByteBuf buf) {
        this(buf.readString());
    }

    private void write(PacketByteBuf buf) {
        buf.writeString(this.command);
    }

    @Override
    public PacketType<CommandExecutionC2SPacket> getPacketType() {
        return PlayPackets.CHAT_COMMAND;
    }

    @Override
    public void apply(ServerPlayPacketListener arg) {
        arg.onCommandExecution(this);
    }
}

