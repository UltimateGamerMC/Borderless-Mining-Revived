/*
 * External method calls:
 *   Lnet/minecraft/network/codec/PacketCodec;decode(Ljava/lang/Object;)Ljava/lang/Object;
 *   Lnet/minecraft/network/RegistryByteBuf;readRegistryKey(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/registry/RegistryKey;
 *   Lnet/minecraft/world/GameMode;byIndex(I)Lnet/minecraft/world/GameMode;
 *   Lnet/minecraft/network/RegistryByteBuf;readOptional(Lnet/minecraft/network/codec/PacketDecoder;)Ljava/util/Optional;
 *   Lnet/minecraft/network/codec/PacketCodec;encode(Ljava/lang/Object;Ljava/lang/Object;)V
 *   Lnet/minecraft/network/RegistryByteBuf;writeRegistryKey(Lnet/minecraft/registry/RegistryKey;)V
 *   Lnet/minecraft/network/RegistryByteBuf;writeLong(J)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/RegistryByteBuf;writeByte(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/RegistryByteBuf;writeBoolean(Z)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/RegistryByteBuf;writeOptional(Ljava/util/Optional;Lnet/minecraft/network/codec/PacketEncoder;)V
 *   Lnet/minecraft/network/RegistryByteBuf;writeVarInt(I)Lnet/minecraft/network/PacketByteBuf;
 */
package net.minecraft.network.packet.s2c.play;

import java.util.Optional;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.jspecify.annotations.Nullable;

public record CommonPlayerSpawnInfo(RegistryEntry<DimensionType> dimensionType, RegistryKey<World> dimension, long seed, GameMode gameMode, @Nullable GameMode lastGameMode, boolean isDebug, boolean isFlat, Optional<GlobalPos> lastDeathLocation, int portalCooldown, int seaLevel) {
    public CommonPlayerSpawnInfo(RegistryByteBuf buf) {
        this((RegistryEntry)DimensionType.PACKET_CODEC.decode(buf), buf.readRegistryKey(RegistryKeys.WORLD), buf.readLong(), GameMode.byIndex(buf.readByte()), GameMode.getOrNull(buf.readByte()), buf.readBoolean(), buf.readBoolean(), buf.readOptional(PacketByteBuf::readGlobalPos), buf.readVarInt(), buf.readVarInt());
    }

    public void write(RegistryByteBuf buf) {
        DimensionType.PACKET_CODEC.encode(buf, this.dimensionType);
        buf.writeRegistryKey(this.dimension);
        buf.writeLong(this.seed);
        buf.writeByte(this.gameMode.getIndex());
        buf.writeByte(GameMode.getId(this.lastGameMode));
        buf.writeBoolean(this.isDebug);
        buf.writeBoolean(this.isFlat);
        buf.writeOptional(this.lastDeathLocation, PacketByteBuf::writeGlobalPos);
        buf.writeVarInt(this.portalCooldown);
        buf.writeVarInt(this.seaLevel);
    }
}

