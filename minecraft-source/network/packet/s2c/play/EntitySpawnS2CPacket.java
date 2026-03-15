/*
 * External method calls:
 *   Lnet/minecraft/network/RegistryByteBuf;readUuid()Ljava/util/UUID;
 *   Lnet/minecraft/network/codec/PacketCodecs;registryValue(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodec;decode(Ljava/lang/Object;)Ljava/lang/Object;
 *   Lnet/minecraft/network/RegistryByteBuf;readVelocity()Lnet/minecraft/util/math/Vec3d;
 *   Lnet/minecraft/network/RegistryByteBuf;writeVarInt(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/RegistryByteBuf;writeUuid(Ljava/util/UUID;)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/codec/PacketCodec;encode(Ljava/lang/Object;Ljava/lang/Object;)V
 *   Lnet/minecraft/network/RegistryByteBuf;writeDouble(D)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/RegistryByteBuf;writeVelocity(Lnet/minecraft/util/math/Vec3d;)V
 *   Lnet/minecraft/network/RegistryByteBuf;writeByte(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onEntitySpawn(Lnet/minecraft/network/packet/s2c/play/EntitySpawnS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/EntitySpawnS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class EntitySpawnS2CPacket
implements Packet<ClientPlayPacketListener> {
    public static final PacketCodec<RegistryByteBuf, EntitySpawnS2CPacket> CODEC = Packet.createCodec(EntitySpawnS2CPacket::write, EntitySpawnS2CPacket::new);
    private final int entityId;
    private final UUID uuid;
    private final EntityType<?> entityType;
    private final double x;
    private final double y;
    private final double z;
    private final Vec3d velocity;
    private final byte pitch;
    private final byte yaw;
    private final byte headYaw;
    private final int entityData;

    public EntitySpawnS2CPacket(Entity entity, EntityTrackerEntry entityTrackerEntry) {
        this(entity, entityTrackerEntry, 0);
    }

    public EntitySpawnS2CPacket(Entity entity, EntityTrackerEntry entityTrackerEntry, int entityData) {
        this(entity.getId(), entity.getUuid(), entityTrackerEntry.getPos().getX(), entityTrackerEntry.getPos().getY(), entityTrackerEntry.getPos().getZ(), entityTrackerEntry.getPitch(), entityTrackerEntry.getYaw(), entity.getType(), entityData, entityTrackerEntry.getVelocity(), entityTrackerEntry.getHeadYaw());
    }

    public EntitySpawnS2CPacket(Entity entity, int entityData, BlockPos pos) {
        this(entity.getId(), entity.getUuid(), pos.getX(), pos.getY(), pos.getZ(), entity.getPitch(), entity.getYaw(), entity.getType(), entityData, entity.getVelocity(), entity.getHeadYaw());
    }

    public EntitySpawnS2CPacket(int entityId, UUID uuid, double x, double y, double z, float pitch, float yaw, EntityType<?> entityType, int entityData, Vec3d velocity, double headYaw) {
        this.entityId = entityId;
        this.uuid = uuid;
        this.x = x;
        this.y = y;
        this.z = z;
        this.velocity = velocity;
        this.pitch = MathHelper.packDegrees(pitch);
        this.yaw = MathHelper.packDegrees(yaw);
        this.headYaw = MathHelper.packDegrees((float)headYaw);
        this.entityType = entityType;
        this.entityData = entityData;
    }

    private EntitySpawnS2CPacket(RegistryByteBuf buf) {
        this.entityId = buf.readVarInt();
        this.uuid = buf.readUuid();
        this.entityType = (EntityType)PacketCodecs.registryValue(RegistryKeys.ENTITY_TYPE).decode(buf);
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.velocity = buf.readVelocity();
        this.pitch = buf.readByte();
        this.yaw = buf.readByte();
        this.headYaw = buf.readByte();
        this.entityData = buf.readVarInt();
    }

    private void write(RegistryByteBuf buf) {
        buf.writeVarInt(this.entityId);
        buf.writeUuid(this.uuid);
        PacketCodecs.registryValue(RegistryKeys.ENTITY_TYPE).encode(buf, this.entityType);
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeVelocity(this.velocity);
        buf.writeByte(this.pitch);
        buf.writeByte(this.yaw);
        buf.writeByte(this.headYaw);
        buf.writeVarInt(this.entityData);
    }

    @Override
    public PacketType<EntitySpawnS2CPacket> getPacketType() {
        return PlayPackets.ADD_ENTITY;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onEntitySpawn(this);
    }

    public int getEntityId() {
        return this.entityId;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public EntityType<?> getEntityType() {
        return this.entityType;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public Vec3d getVelocity() {
        return this.velocity;
    }

    public float getPitch() {
        return MathHelper.unpackDegrees(this.pitch);
    }

    public float getYaw() {
        return MathHelper.unpackDegrees(this.yaw);
    }

    public float getHeadYaw() {
        return MathHelper.unpackDegrees(this.headYaw);
    }

    public int getEntityData() {
        return this.entityData;
    }
}

