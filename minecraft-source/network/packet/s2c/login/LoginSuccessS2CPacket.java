/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ClientLoginPacketListener;onSuccess(Lnet/minecraft/network/packet/s2c/login/LoginSuccessS2CPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/login/LoginSuccessS2CPacket;apply(Lnet/minecraft/network/listener/ClientLoginPacketListener;)V
 */
package net.minecraft.network.packet.s2c.login;

import com.mojang.authlib.GameProfile;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.listener.ClientLoginPacketListener;
import net.minecraft.network.packet.LoginPackets;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;

public record LoginSuccessS2CPacket(GameProfile profile) implements Packet<ClientLoginPacketListener>
{
    public static final PacketCodec<ByteBuf, LoginSuccessS2CPacket> CODEC = PacketCodec.tuple(PacketCodecs.GAME_PROFILE, LoginSuccessS2CPacket::profile, LoginSuccessS2CPacket::new);

    @Override
    public PacketType<LoginSuccessS2CPacket> getPacketType() {
        return LoginPackets.LOGIN_FINISHED;
    }

    @Override
    public void apply(ClientLoginPacketListener arg) {
        arg.onSuccess(this);
    }

    @Override
    public boolean transitionsNetworkState() {
        return true;
    }
}

