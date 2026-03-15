/*
 * External method calls:
 *   Lnet/minecraft/world/World;updateComparators(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V
 *   Lnet/minecraft/server/world/ServerWorld;playSound(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V
 *   Lnet/minecraft/entity/mob/CreakingEntity;playSound(Lnet/minecraft/sound/SoundEvent;)V
 *   Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V
 *   Lnet/minecraft/block/BlockState;with(Lnet/minecraft/state/property/Property;Ljava/lang/Comparable;)Ljava/lang/Object;
 *   Lnet/minecraft/entity/LargeEntitySpawnHelper;trySpawnAt(Lnet/minecraft/entity/EntityType;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;IIILnet/minecraft/entity/LargeEntitySpawnHelper$Requirements;Z)Ljava/util/Optional;
 *   Lnet/minecraft/server/world/ServerWorld;emitGameEvent(Lnet/minecraft/entity/Entity;Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/util/math/Vec3d;)V
 *   Lnet/minecraft/server/world/ServerWorld;sendEntityStatus(Lnet/minecraft/entity/Entity;B)V
 *   Lnet/minecraft/entity/mob/CreakingEntity;initHomePos(Lnet/minecraft/util/math/BlockPos;)V
 *   Lnet/minecraft/network/packet/s2c/play/BlockEntityUpdateS2CPacket;create(Lnet/minecraft/block/entity/BlockEntity;)Lnet/minecraft/network/packet/s2c/play/BlockEntityUpdateS2CPacket;
 *   Lnet/minecraft/server/world/ServerWorld;spawnParticles(Lnet/minecraft/particle/ParticleEffect;ZZDDDIDDDD)I
 *   Lnet/minecraft/entity/mob/CreakingEntity;killFromHeart(Lnet/minecraft/entity/damage/DamageSource;)V
 *   Lnet/minecraft/block/entity/BlockEntity;readData(Lnet/minecraft/storage/ReadView;)V
 *   Lnet/minecraft/storage/ReadView;read(Ljava/lang/String;Lcom/mojang/serialization/Codec;)Ljava/util/Optional;
 *   Lnet/minecraft/block/entity/BlockEntity;writeData(Lnet/minecraft/storage/WriteView;)V
 *   Lnet/minecraft/util/Util;copyShuffled([Ljava/lang/Object;Lnet/minecraft/util/math/random/Random;)Ljava/util/List;
 *   Lnet/minecraft/world/event/GameEvent$Emitter;of(Lnet/minecraft/block/BlockState;)Lnet/minecraft/world/event/GameEvent$Emitter;
 *   Lnet/minecraft/world/World;emitGameEvent(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/event/GameEvent$Emitter;)V
 *   Lnet/minecraft/entity/mob/CreakingEntity;squaredDistanceTo(Lnet/minecraft/util/math/Vec3d;)D
 *
 * Internal private/static methods:
 *   Lnet/minecraft/block/entity/CreakingHeartBlockEntity;spawnTrailParticles(Lnet/minecraft/server/world/ServerWorld;IZ)V
 *   Lnet/minecraft/block/entity/CreakingHeartBlockEntity;killPuppet(Lnet/minecraft/entity/damage/DamageSource;)V
 *   Lnet/minecraft/block/entity/CreakingHeartBlockEntity;spawnCreakingPuppet(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/block/entity/CreakingHeartBlockEntity;)Lnet/minecraft/entity/mob/CreakingEntity;
 *   Lnet/minecraft/block/entity/CreakingHeartBlockEntity;createComponentlessNbt(Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/nbt/NbtCompound;
 *   Lnet/minecraft/block/entity/CreakingHeartBlockEntity;findResinGenerationPos(Lnet/minecraft/server/world/ServerWorld;)Ljava/util/Optional;
 *   Lnet/minecraft/block/entity/CreakingHeartBlockEntity;toUpdatePacket()Lnet/minecraft/network/packet/s2c/play/BlockEntityUpdateS2CPacket;
 */
package net.minecraft.block.entity;

import com.mojang.datafixers.util.Either;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CreakingHeartBlock;
import net.minecraft.block.MultifaceBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.CreakingHeartState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LargeEntitySpawnHelper;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CreakingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.TrailParticleEffect;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Util;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.event.GameEvent;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jspecify.annotations.Nullable;

public class CreakingHeartBlockEntity
extends BlockEntity {
    private static final int field_54776 = 32;
    public static final int field_54775 = 32;
    private static final int field_54777 = 34;
    private static final int field_54778 = 16;
    private static final int field_54779 = 8;
    private static final int field_54780 = 5;
    private static final int field_54781 = 20;
    private static final int field_55498 = 5;
    private static final int field_54782 = 100;
    private static final int field_54783 = 10;
    private static final int field_54784 = 10;
    private static final int field_54785 = 50;
    private static final int field_55085 = 2;
    private static final int field_55086 = 64;
    private static final int field_55499 = 30;
    private static final Optional<CreakingEntity> DEFAULT_CREAKING_PUPPET = Optional.empty();
    private @Nullable Either<CreakingEntity, UUID> creakingPuppet;
    private long ticks;
    private int creakingUpdateTimer;
    private int trailParticlesSpawnTimer;
    private @Nullable Vec3d lastCreakingPuppetPos;
    private int comparatorOutput;

    public CreakingHeartBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityType.CREAKING_HEART, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, CreakingHeartBlockEntity blockEntity) {
        CreakingEntity lv6;
        ++blockEntity.ticks;
        if (!(world instanceof ServerWorld)) {
            return;
        }
        ServerWorld lv = (ServerWorld)world;
        int i = blockEntity.calcComparatorOutput();
        if (blockEntity.comparatorOutput != i) {
            blockEntity.comparatorOutput = i;
            world.updateComparators(pos, Blocks.CREAKING_HEART);
        }
        if (blockEntity.trailParticlesSpawnTimer > 0) {
            if (blockEntity.trailParticlesSpawnTimer > 50) {
                blockEntity.spawnTrailParticles(lv, 1, true);
                blockEntity.spawnTrailParticles(lv, 1, false);
            }
            if (blockEntity.trailParticlesSpawnTimer % 10 == 0 && blockEntity.lastCreakingPuppetPos != null) {
                blockEntity.getCreakingPuppet().ifPresent(creaking -> {
                    arg.lastCreakingPuppetPos = creaking.getBoundingBox().getCenter();
                });
                Vec3d lv2 = Vec3d.ofCenter(pos);
                float f = 0.2f + 0.8f * (float)(100 - blockEntity.trailParticlesSpawnTimer) / 100.0f;
                Vec3d lv3 = lv2.subtract(blockEntity.lastCreakingPuppetPos).multiply(f).add(blockEntity.lastCreakingPuppetPos);
                BlockPos lv4 = BlockPos.ofFloored(lv3);
                float g = (float)blockEntity.trailParticlesSpawnTimer / 2.0f / 100.0f + 0.5f;
                lv.playSound(null, lv4, SoundEvents.BLOCK_CREAKING_HEART_HURT, SoundCategory.BLOCKS, g, 1.0f);
            }
            --blockEntity.trailParticlesSpawnTimer;
        }
        if (blockEntity.creakingUpdateTimer-- >= 0) {
            return;
        }
        blockEntity.creakingUpdateTimer = blockEntity.world == null ? 20 : blockEntity.world.random.nextInt(5) + 20;
        BlockState lv5 = CreakingHeartBlockEntity.getBlockState(world, state, pos, blockEntity);
        if (lv5 != state) {
            world.setBlockState(pos, lv5, Block.NOTIFY_ALL);
            if (lv5.get(CreakingHeartBlock.ACTIVE) == CreakingHeartState.UPROOTED) {
                return;
            }
        }
        if (blockEntity.creakingPuppet != null) {
            Optional<CreakingEntity> optional = blockEntity.getCreakingPuppet();
            if (optional.isPresent()) {
                lv6 = optional.get();
                if (world.getEnvironmentAttributes().getAttributeValue(EnvironmentAttributes.CREAKING_ACTIVE_GAMEPLAY, pos) == false && !lv6.isPersistent() || blockEntity.getDistanceToPuppet() > 34.0 || lv6.isStuckWithPlayer()) {
                    blockEntity.killPuppet(null);
                }
            }
            return;
        }
        if (lv5.get(CreakingHeartBlock.ACTIVE) != CreakingHeartState.AWAKE) {
            return;
        }
        if (!lv.shouldSpawnMonsters()) {
            return;
        }
        PlayerEntity lv7 = world.getClosestPlayer((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), 32.0, false);
        if (lv7 != null && (lv6 = CreakingHeartBlockEntity.spawnCreakingPuppet(lv, blockEntity)) != null) {
            blockEntity.setCreakingPuppet(lv6);
            lv6.playSound(SoundEvents.ENTITY_CREAKING_SPAWN);
            world.playSound(null, blockEntity.getPos(), SoundEvents.BLOCK_CREAKING_HEART_SPAWN, SoundCategory.BLOCKS, 1.0f, 1.0f);
        }
    }

    private static BlockState getBlockState(World world, BlockState state, BlockPos pos, CreakingHeartBlockEntity creakingHeart) {
        if (!CreakingHeartBlock.shouldBeEnabled(state, world, pos) && creakingHeart.creakingPuppet == null) {
            return (BlockState)state.with(CreakingHeartBlock.ACTIVE, CreakingHeartState.UPROOTED);
        }
        CreakingHeartState lv = world.getEnvironmentAttributes().getAttributeValue(EnvironmentAttributes.CREAKING_ACTIVE_GAMEPLAY, pos) != false ? CreakingHeartState.AWAKE : CreakingHeartState.DORMANT;
        return (BlockState)state.with(CreakingHeartBlock.ACTIVE, lv);
    }

    private double getDistanceToPuppet() {
        return this.getCreakingPuppet().map(creaking -> Math.sqrt(creaking.squaredDistanceTo(Vec3d.ofBottomCenter(this.getPos())))).orElse(0.0);
    }

    private void clearCreakingPuppet() {
        this.creakingPuppet = null;
        this.markDirty();
    }

    public void setCreakingPuppet(CreakingEntity creakingPuppet) {
        this.creakingPuppet = Either.left(creakingPuppet);
        this.markDirty();
    }

    public void setCreakingPuppetFromUuid(UUID creakingPuppetUuid) {
        this.creakingPuppet = Either.right(creakingPuppetUuid);
        this.ticks = 0L;
        this.markDirty();
    }

    private Optional<CreakingEntity> getCreakingPuppet() {
        World world;
        if (this.creakingPuppet == null) {
            return DEFAULT_CREAKING_PUPPET;
        }
        if (this.creakingPuppet.left().isPresent()) {
            CreakingEntity lv = this.creakingPuppet.left().get();
            if (!lv.isRemoved()) {
                return Optional.of(lv);
            }
            this.setCreakingPuppetFromUuid(lv.getUuid());
        }
        if ((world = this.world) instanceof ServerWorld) {
            ServerWorld lv2 = (ServerWorld)world;
            if (this.creakingPuppet.right().isPresent()) {
                UUID uUID = this.creakingPuppet.right().get();
                Entity lv3 = lv2.getEntity(uUID);
                if (lv3 instanceof CreakingEntity) {
                    CreakingEntity lv4 = (CreakingEntity)lv3;
                    this.setCreakingPuppet(lv4);
                    return Optional.of(lv4);
                }
                if (this.ticks >= 30L) {
                    this.clearCreakingPuppet();
                }
                return DEFAULT_CREAKING_PUPPET;
            }
        }
        return DEFAULT_CREAKING_PUPPET;
    }

    private static @Nullable CreakingEntity spawnCreakingPuppet(ServerWorld world, CreakingHeartBlockEntity blockEntity) {
        BlockPos lv = blockEntity.getPos();
        Optional<CreakingEntity> optional = LargeEntitySpawnHelper.trySpawnAt(EntityType.CREAKING, SpawnReason.SPAWNER, world, lv, 5, 16, 8, LargeEntitySpawnHelper.Requirements.CREAKING, true);
        if (optional.isEmpty()) {
            return null;
        }
        CreakingEntity lv2 = optional.get();
        world.emitGameEvent((Entity)lv2, GameEvent.ENTITY_PLACE, lv2.getEntityPos());
        world.sendEntityStatus(lv2, EntityStatuses.ADD_DEATH_PARTICLES);
        lv2.initHomePos(lv);
        return lv2;
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
        return this.createComponentlessNbt(registries);
    }

    public void onPuppetDamage() {
        Object var2_1 = this.getCreakingPuppet().orElse(null);
        if (!(var2_1 instanceof CreakingEntity)) {
            return;
        }
        CreakingEntity lv = var2_1;
        World world = this.world;
        if (!(world instanceof ServerWorld)) {
            return;
        }
        ServerWorld lv2 = (ServerWorld)world;
        if (this.trailParticlesSpawnTimer > 0) {
            return;
        }
        this.spawnTrailParticles(lv2, 20, false);
        if (this.getCachedState().get(CreakingHeartBlock.ACTIVE) == CreakingHeartState.AWAKE) {
            int i = this.world.getRandom().nextBetween(2, 3);
            for (int j = 0; j < i; ++j) {
                this.findResinGenerationPos(lv2).ifPresent(pos -> {
                    this.world.playSound(null, (BlockPos)pos, SoundEvents.BLOCK_RESIN_PLACE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    this.world.emitGameEvent((RegistryEntry<GameEvent>)GameEvent.BLOCK_PLACE, (BlockPos)pos, GameEvent.Emitter.of(this.getCachedState()));
                });
            }
        }
        this.trailParticlesSpawnTimer = 100;
        this.lastCreakingPuppetPos = lv.getBoundingBox().getCenter();
    }

    private Optional<BlockPos> findResinGenerationPos(ServerWorld world) {
        MutableObject<Object> mutable = new MutableObject<Object>(null);
        BlockPos.iterateRecursively(this.pos, 2, 64, (pos, consumer) -> {
            for (Direction lv : Util.copyShuffled(Direction.values(), arg.random)) {
                BlockPos lv2 = pos.offset(lv);
                if (!world.getBlockState(lv2).isIn(BlockTags.PALE_OAK_LOGS)) continue;
                consumer.accept(lv2);
            }
        }, pos -> {
            if (!world.getBlockState((BlockPos)pos).isIn(BlockTags.PALE_OAK_LOGS)) {
                return BlockPos.IterationState.ACCEPT;
            }
            for (Direction lv : Util.copyShuffled(Direction.values(), arg.random)) {
                BlockPos lv2 = pos.offset(lv);
                BlockState lv3 = world.getBlockState(lv2);
                Direction lv4 = lv.getOpposite();
                if (lv3.isAir()) {
                    lv3 = Blocks.RESIN_CLUMP.getDefaultState();
                } else if (lv3.isOf(Blocks.WATER) && lv3.getFluidState().isStill()) {
                    lv3 = (BlockState)Blocks.RESIN_CLUMP.getDefaultState().with(MultifaceBlock.WATERLOGGED, true);
                }
                if (!lv3.isOf(Blocks.RESIN_CLUMP) || MultifaceBlock.hasDirection(lv3, lv4)) continue;
                world.setBlockState(lv2, (BlockState)lv3.with(MultifaceBlock.getProperty(lv4), true), Block.NOTIFY_ALL);
                mutable.setValue(lv2);
                return BlockPos.IterationState.STOP;
            }
            return BlockPos.IterationState.ACCEPT;
        });
        return Optional.ofNullable((BlockPos)mutable.get());
    }

    private void spawnTrailParticles(ServerWorld world, int count, boolean towardsPuppet) {
        Object var5_4 = this.getCreakingPuppet().orElse(null);
        if (!(var5_4 instanceof CreakingEntity)) {
            return;
        }
        CreakingEntity lv = var5_4;
        int j = towardsPuppet ? 16545810 : 0x5F5F5F;
        Random lv2 = world.random;
        for (double d = 0.0; d < (double)count; d += 1.0) {
            Box lv3 = lv.getBoundingBox();
            Vec3d lv4 = lv3.getMinPos().add(lv2.nextDouble() * lv3.getLengthX(), lv2.nextDouble() * lv3.getLengthY(), lv2.nextDouble() * lv3.getLengthZ());
            Vec3d lv5 = Vec3d.of(this.getPos()).add(lv2.nextDouble(), lv2.nextDouble(), lv2.nextDouble());
            if (towardsPuppet) {
                Vec3d lv6 = lv4;
                lv4 = lv5;
                lv5 = lv6;
            }
            TrailParticleEffect lv7 = new TrailParticleEffect(lv5, j, lv2.nextInt(40) + 10);
            world.spawnParticles(lv7, true, true, lv4.x, lv4.y, lv4.z, 1, 0.0, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public void onBlockReplaced(BlockPos pos, BlockState oldState) {
        this.killPuppet(null);
    }

    public void killPuppet(@Nullable DamageSource damageSource) {
        Object var3_2 = this.getCreakingPuppet().orElse(null);
        if (var3_2 instanceof CreakingEntity) {
            CreakingEntity lv = var3_2;
            if (damageSource == null) {
                lv.finishCrumbling();
            } else {
                lv.killFromHeart(damageSource);
                lv.setCrumbling();
                lv.setHealth(0.0f);
            }
            this.clearCreakingPuppet();
        }
    }

    public boolean isPuppet(CreakingEntity creaking) {
        return this.getCreakingPuppet().map(puppet -> puppet == creaking).orElse(false);
    }

    public int getComparatorOutput() {
        return this.comparatorOutput;
    }

    public int calcComparatorOutput() {
        if (this.creakingPuppet == null || this.getCreakingPuppet().isEmpty()) {
            return 0;
        }
        double d = this.getDistanceToPuppet();
        double e = Math.clamp(d, 0.0, 32.0) / 32.0;
        return 15 - (int)Math.floor(e * 15.0);
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        view.read("creaking", Uuids.INT_STREAM_CODEC).ifPresentOrElse(this::setCreakingPuppetFromUuid, this::clearCreakingPuppet);
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        if (this.creakingPuppet != null) {
            view.put("creaking", Uuids.INT_STREAM_CODEC, this.creakingPuppet.map(Entity::getUuid, uuid -> uuid));
        }
    }

    public /* synthetic */ Packet toUpdatePacket() {
        return this.toUpdatePacket();
    }
}

