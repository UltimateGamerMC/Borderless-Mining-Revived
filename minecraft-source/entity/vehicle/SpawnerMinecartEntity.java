/*
 * External method calls:
 *   Lnet/minecraft/entity/vehicle/AbstractMinecartEntity;readCustomData(Lnet/minecraft/storage/ReadView;)V
 *   Lnet/minecraft/block/spawner/MobSpawnerLogic;readData(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/storage/ReadView;)V
 *   Lnet/minecraft/entity/vehicle/AbstractMinecartEntity;writeCustomData(Lnet/minecraft/storage/WriteView;)V
 *   Lnet/minecraft/block/spawner/MobSpawnerLogic;writeData(Lnet/minecraft/storage/WriteView;)V
 *   Lnet/minecraft/block/spawner/MobSpawnerLogic;handleStatus(Lnet/minecraft/world/World;I)Z
 *   Lnet/minecraft/block/spawner/MobSpawnerLogic;clientTick(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V
 *   Lnet/minecraft/block/spawner/MobSpawnerLogic;serverTick(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;)V
 */
package net.minecraft.entity.vehicle;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.spawner.MobSpawnerLogic;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpawnerMinecartEntity
extends AbstractMinecartEntity {
    private final MobSpawnerLogic logic = new MobSpawnerLogic(){

        @Override
        public void sendStatus(World world, BlockPos pos, int status) {
            world.sendEntityStatus(SpawnerMinecartEntity.this, (byte)status);
        }
    };
    private final Runnable ticker;

    public SpawnerMinecartEntity(EntityType<? extends SpawnerMinecartEntity> arg, World arg2) {
        super(arg, arg2);
        this.ticker = this.getTicker(arg2);
    }

    @Override
    protected Item asItem() {
        return Items.MINECART;
    }

    @Override
    public ItemStack getPickBlockStack() {
        return new ItemStack(Items.MINECART);
    }

    private Runnable getTicker(World world) {
        return world instanceof ServerWorld ? () -> this.logic.serverTick((ServerWorld)world, this.getBlockPos()) : () -> this.logic.clientTick(world, this.getBlockPos());
    }

    @Override
    public BlockState getDefaultContainedBlock() {
        return Blocks.SPAWNER.getDefaultState();
    }

    @Override
    protected void readCustomData(ReadView view) {
        super.readCustomData(view);
        this.logic.readData(this.getEntityWorld(), this.getBlockPos(), view);
    }

    @Override
    protected void writeCustomData(WriteView view) {
        super.writeCustomData(view);
        this.logic.writeData(view);
    }

    @Override
    public void handleStatus(byte status) {
        this.logic.handleStatus(this.getEntityWorld(), status);
    }

    @Override
    public void tick() {
        super.tick();
        this.ticker.run();
    }

    public MobSpawnerLogic getLogic() {
        return this.logic;
    }
}

