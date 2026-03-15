/*
 * External method calls:
 *   Lnet/minecraft/network/codec/PacketCodec;decode(Ljava/lang/Object;)Ljava/lang/Object;
 *   Lnet/minecraft/network/RegistryByteBuf;readEnumConstant(Ljava/lang/Class;)Ljava/lang/Enum;
 *   Lnet/minecraft/network/codec/PacketCodec;encode(Ljava/lang/Object;Ljava/lang/Object;)V
 *   Lnet/minecraft/network/RegistryByteBuf;writeEnumConstant(Ljava/lang/Enum;)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/RegistryByteBuf;writeVarInt(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/RegistryByteBuf;writeFloat(F)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/RegistryByteBuf;writeLong(J)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onPlaySoundFromEntity(Lnet/minecraft/network/packet/s2c/play/PlaySoundFromEntityS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/PlaySoundFromEntityS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.entity.Entity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

public class PlaySoundFromEntityS2CPacket
implements Packet<ClientPlayPacketListener> {
    public static final PacketCodec<RegistryByteBuf, PlaySoundFromEntityS2CPacket> CODEC = Packet.createCodec(PlaySoundFromEntityS2CPacket::write, PlaySoundFromEntityS2CPacket::new);
    private final RegistryEntry<SoundEvent> sound;
    private final SoundCategory category;
    private final int entityId;
    private final float volume;
    private final float pitch;
    private final long seed;

    public PlaySoundFromEntityS2CPacket(RegistryEntry<SoundEvent> sound, SoundCategory category, Entity entity, float volume, float pitch, long seed) {
        this.sound = sound;
        this.category = category;
        this.entityId = entity.getId();
        this.volume = volume;
        this.pitch = pitch;
        this.seed = seed;
    }

    private PlaySoundFromEntityS2CPacket(RegistryByteBuf buf) {
        this.sound = (RegistryEntry)SoundEvent.ENTRY_PACKET_CODEC.decode(buf);
        this.category = buf.readEnumConstant(SoundCategory.class);
        this.entityId = buf.readVarInt();
        this.volume = buf.readFloat();
        this.pitch = buf.readFloat();
        this.seed = buf.readLong();
    }

    private void write(RegistryByteBuf buf) {
        SoundEvent.ENTRY_PACKET_CODEC.encode(buf, this.sound);
        buf.writeEnumConstant(this.category);
        buf.writeVarInt(this.entityId);
        buf.writeFloat(this.volume);
        buf.writeFloat(this.pitch);
        buf.writeLong(this.seed);
    }

    @Override
    public PacketType<PlaySoundFromEntityS2CPacket> getPacketType() {
        return PlayPackets.SOUND_ENTITY;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onPlaySoundFromEntity(this);
    }

    public RegistryEntry<SoundEvent> getSound() {
        return this.sound;
    }

    public SoundCategory getCategory() {
        return this.category;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public float getVolume() {
        return this.volume;
    }

    public float getPitch() {
        return this.pitch;
    }

    public long getSeed() {
        return this.seed;
    }
}

