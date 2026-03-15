/*
 * External method calls:
 *   Lnet/minecraft/block/entity/BlockEntity;readData(Lnet/minecraft/storage/ReadView;)V
 *   Lnet/minecraft/block/spawner/MobSpawnerLogic;readData(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/storage/ReadView;)V
 *   Lnet/minecraft/block/entity/BlockEntity;writeData(Lnet/minecraft/storage/WriteView;)V
 *   Lnet/minecraft/block/spawner/MobSpawnerLogic;writeData(Lnet/minecraft/storage/WriteView;)V
 *   Lnet/minecraft/block/spawner/MobSpawnerLogic;clientTick(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V
 *   Lnet/minecraft/block/spawner/MobSpawnerLogic;serverTick(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;)V
 *   Lnet/minecraft/network/packet/s2c/play/BlockEntityUpdateS2CPacket;create(Lnet/minecraft/block/entity/BlockEntity;)Lnet/minecraft/network/packet/s2c/play/BlockEntityUpdateS2CPacket;
 *   Lnet/minecraft/block/spawner/MobSpawnerLogic;handleStatus(Lnet/minecraft/world/World;I)Z
 *   Lnet/minecraft/block/entity/BlockEntity;onSyncedBlockEvent(II)Z
 *
 * Internal private/static methods:
 *   Lnet/minecraft/block/entity/MobSpawnerBlockEntity;createComponentlessNbt(Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/nbt/NbtCompound;
 *   Lnet/minecraft/block/entity/MobSpawnerBlockEntity;toUpdatePacket()Lnet/minecraft/network/packet/s2c/play/BlockEntityUpdateS2CPacket;
 */
package net.minecraft.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.Spawner;
import net.minecraft.block.spawner.MobSpawnerEntry;
import net.minecraft.block.spawner.MobSpawnerLogic;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

public class MobSpawnerBlockEntity
extends BlockEntity
implements Spawner {
    private final MobSpawnerLogic logic = new MobSpawnerLogic(this){

        @Override
        public void sendStatus(World world, BlockPos pos, int status) {
            world.addSyncedBlockEvent(pos, Blocks.SPAWNER, status, 0);
        }

        @Override
        public void setSpawnEntry(@Nullable World world, BlockPos pos, MobSpawnerEntry spawnEntry) {
            super.setSpawnEntry(world, pos, spawnEntry);
            if (world != null) {
                BlockState lv = world.getBlockState(pos);
                world.updateListeners(pos, lv, lv, Block.SKIP_REDRAW_AND_BLOCK_ENTITY_REPLACED_CALLBACK);
            }
        }
    };

    public MobSpawnerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityType.MOB_SPAWNER, pos, state);
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        this.logic.readData(this.world, this.pos, view);
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        this.logic.writeData(view);
    }

    public static void clientTick(World world, BlockPos pos, BlockState state, MobSpawnerBlockEntity blockEntity) {
        blockEntity.logic.clientTick(world, pos);
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, MobSpawnerBlockEntity blockEntity) {
        blockEntity.logic.serverTick((ServerWorld)world, pos);
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
        NbtCompound lv = this.createComponentlessNbt(registries);
        lv.remove("SpawnPotentials");
        return lv;
    }

    @Override
    public boolean onSyncedBlockEvent(int type, int data) {
        if (this.logic.handleStatus(this.world, type)) {
            return true;
        }
        return super.onSyncedBlockEvent(type, data);
    }

    @Override
    public void setEntityType(EntityType<?> type, Random random) {
        this.logic.setEntityId(type, this.world, random, this.pos);
        this.markDirty();
    }

    public MobSpawnerLogic getLogic() {
        return this.logic;
    }

    public /* synthetic */ Packet toUpdatePacket() {
        return this.toUpdatePacket();
    }
}

