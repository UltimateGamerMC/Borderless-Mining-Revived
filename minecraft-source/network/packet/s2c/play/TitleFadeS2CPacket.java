/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeInt(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onTitleFade(Lnet/minecraft/network/packet/s2c/play/TitleFadeS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/TitleFadeS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public class TitleFadeS2CPacket
implements Packet<ClientPlayPacketListener> {
    public static final PacketCodec<PacketByteBuf, TitleFadeS2CPacket> CODEC = Packet.createCodec(TitleFadeS2CPacket::write, TitleFadeS2CPacket::new);
    private final int fadeInTicks;
    private final int stayTicks;
    private final int fadeOutTicks;

    public TitleFadeS2CPacket(int fadeInTicks, int stayTicks, int fadeOutTicks) {
        this.fadeInTicks = fadeInTicks;
        this.stayTicks = stayTicks;
        this.fadeOutTicks = fadeOutTicks;
    }

    private TitleFadeS2CPacket(PacketByteBuf buf) {
        this.fadeInTicks = buf.readInt();
        this.stayTicks = buf.readInt();
        this.fadeOutTicks = buf.readInt();
    }

    private void write(PacketByteBuf buf) {
        buf.writeInt(this.fadeInTicks);
        buf.writeInt(this.stayTicks);
        buf.writeInt(this.fadeOutTicks);
    }

    @Override
    public PacketType<TitleFadeS2CPacket> getPacketType() {
        return PlayPackets.SET_TITLES_ANIMATION;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onTitleFade(this);
    }

    public int getFadeInTicks() {
        return this.fadeInTicks;
    }

    public int getStayTicks() {
        return this.stayTicks;
    }

    public int getFadeOutTicks() {
        return this.fadeOutTicks;
    }
}

