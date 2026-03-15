/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ClientLoginPacketListener;onDisconnect(Lnet/minecraft/network/packet/s2c/login/LoginDisconnectS2CPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodecs;lenientJson(I)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodecs;fromCodec(Lcom/mojang/serialization/DynamicOps;Lcom/mojang/serialization/Codec;)Lnet/minecraft/network/codec/PacketCodec$ResultFunction;
 *   Lnet/minecraft/network/codec/PacketCodec;collect(Lnet/minecraft/network/codec/PacketCodec$ResultFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/login/LoginDisconnectS2CPacket;apply(Lnet/minecraft/network/listener/ClientLoginPacketListener;)V
 */
package net.minecraft.network.packet.s2c.login;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.listener.ClientLoginPacketListener;
import net.minecraft.network.packet.LoginPackets;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryOps;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;

public record LoginDisconnectS2CPacket(Text reason) implements Packet<ClientLoginPacketListener>
{
    private static final RegistryOps<JsonElement> OPS = DynamicRegistryManager.EMPTY.getOps(JsonOps.INSTANCE);
    public static final PacketCodec<ByteBuf, LoginDisconnectS2CPacket> CODEC = PacketCodec.tuple(PacketCodecs.lenientJson(262144).collect(PacketCodecs.fromCodec(OPS, TextCodecs.CODEC)), LoginDisconnectS2CPacket::reason, LoginDisconnectS2CPacket::new);

    @Override
    public PacketType<LoginDisconnectS2CPacket> getPacketType() {
        return LoginPackets.LOGIN_DISCONNECT;
    }

    @Override
    public void apply(ClientLoginPacketListener arg) {
        arg.onDisconnect(this);
    }
}

