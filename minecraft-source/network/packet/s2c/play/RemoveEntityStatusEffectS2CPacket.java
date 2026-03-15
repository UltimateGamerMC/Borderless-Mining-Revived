/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onRemoveEntityStatusEffect(Lnet/minecraft/network/packet/s2c/play/RemoveEntityStatusEffectS2CPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/BiFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/RemoveEntityStatusEffectS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

public record RemoveEntityStatusEffectS2CPacket(int entityId, RegistryEntry<StatusEffect> effect) implements Packet<ClientPlayPacketListener>
{
    public static final PacketCodec<RegistryByteBuf, RemoveEntityStatusEffectS2CPacket> CODEC = PacketCodec.tuple(PacketCodecs.VAR_INT, RemoveEntityStatusEffectS2CPacket::entityId, StatusEffect.ENTRY_PACKET_CODEC, RemoveEntityStatusEffectS2CPacket::effect, RemoveEntityStatusEffectS2CPacket::new);

    @Override
    public PacketType<RemoveEntityStatusEffectS2CPacket> getPacketType() {
        return PlayPackets.REMOVE_MOB_EFFECT;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onRemoveEntityStatusEffect(this);
    }

    public @Nullable Entity getEntity(World world) {
        return world.getEntityById(this.entityId);
    }
}

