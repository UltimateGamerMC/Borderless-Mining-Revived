/*
 * External method calls:
 *   Lnet/minecraft/world/World;playSoundClient(DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FFZ)V
 *   Lnet/minecraft/advancement/criterion/LightningStrikeCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/entity/LightningEntity;Ljava/util/List;)V
 *   Lnet/minecraft/entity/Entity;onStruckByLightning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LightningEntity;)V
 *   Lnet/minecraft/advancement/criterion/ChanneledLightningCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/Collection;)V
 *   Lnet/minecraft/world/World;syncWorldEvent(ILnet/minecraft/util/math/BlockPos;I)V
 *   Lnet/minecraft/server/network/ServerPlayerEntity;distanceTo(Lnet/minecraft/entity/Entity;)F
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/LightningEntity;spawnFire(I)V
 *   Lnet/minecraft/entity/LightningEntity;cleanOxidation(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V
 *   Lnet/minecraft/entity/LightningEntity;emitGameEvent(Lnet/minecraft/registry/entry/RegistryEntry;)V
 *   Lnet/minecraft/entity/LightningEntity;cleanOxidationAround(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos$Mutable;I)V
 *   Lnet/minecraft/entity/LightningEntity;cleanOxidationAround(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Ljava/util/Optional;
 */
package net.minecraft.entity;

import com.google.common.collect.Sets;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LightningRodBlock;
import net.minecraft.block.Oxidizable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.item.HoneycombItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;
import org.jspecify.annotations.Nullable;

public class LightningEntity
extends Entity {
    private static final int field_30062 = 2;
    private static final double field_33906 = 3.0;
    private static final double field_33907 = 15.0;
    private int ambientTick = 2;
    public long seed;
    private int remainingActions;
    private boolean cosmetic;
    private @Nullable ServerPlayerEntity channeler;
    private final Set<Entity> struckEntities = Sets.newHashSet();
    private int blocksSetOnFire;

    public LightningEntity(EntityType<? extends LightningEntity> arg, World arg2) {
        super(arg, arg2);
        this.seed = this.random.nextLong();
        this.remainingActions = this.random.nextInt(3) + 1;
    }

    public void setCosmetic(boolean cosmetic) {
        this.cosmetic = cosmetic;
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.WEATHER;
    }

    public @Nullable ServerPlayerEntity getChanneler() {
        return this.channeler;
    }

    public void setChanneler(@Nullable ServerPlayerEntity channeler) {
        this.channeler = channeler;
    }

    private void powerLightningRod() {
        BlockPos lv = this.getAffectedBlockPos();
        BlockState lv2 = this.getEntityWorld().getBlockState(lv);
        Block block = lv2.getBlock();
        if (block instanceof LightningRodBlock) {
            LightningRodBlock lv3 = (LightningRodBlock)block;
            lv3.setPowered(lv2, this.getEntityWorld(), lv);
        }
    }

    @Override
    public void tick() {
        List<Entity> list;
        super.tick();
        if (this.ambientTick == 2) {
            if (this.getEntityWorld().isClient()) {
                this.getEntityWorld().playSoundClient(this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.WEATHER, 10000.0f, 0.8f + this.random.nextFloat() * 0.2f, false);
                this.getEntityWorld().playSoundClient(this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT, SoundCategory.WEATHER, 2.0f, 0.5f + this.random.nextFloat() * 0.2f, false);
            } else {
                Difficulty lv = this.getEntityWorld().getDifficulty();
                if (lv == Difficulty.NORMAL || lv == Difficulty.HARD) {
                    this.spawnFire(4);
                }
                this.powerLightningRod();
                LightningEntity.cleanOxidation(this.getEntityWorld(), this.getAffectedBlockPos());
                this.emitGameEvent(GameEvent.LIGHTNING_STRIKE);
            }
        }
        --this.ambientTick;
        if (this.ambientTick < 0) {
            if (this.remainingActions == 0) {
                if (this.getEntityWorld() instanceof ServerWorld) {
                    list = this.getEntityWorld().getOtherEntities(this, new Box(this.getX() - 15.0, this.getY() - 15.0, this.getZ() - 15.0, this.getX() + 15.0, this.getY() + 6.0 + 15.0, this.getZ() + 15.0), arg -> arg.isAlive() && !this.struckEntities.contains(arg));
                    for (ServerPlayerEntity lv2 : ((ServerWorld)this.getEntityWorld()).getPlayers(arg -> arg.distanceTo(this) < 256.0f)) {
                        Criteria.LIGHTNING_STRIKE.trigger(lv2, this, list);
                    }
                }
                this.discard();
            } else if (this.ambientTick < -this.random.nextInt(10)) {
                --this.remainingActions;
                this.ambientTick = 1;
                this.seed = this.random.nextLong();
                this.spawnFire(0);
            }
        }
        if (this.ambientTick >= 0) {
            if (!(this.getEntityWorld() instanceof ServerWorld)) {
                this.getEntityWorld().setLightningTicksLeft(2);
            } else if (!this.cosmetic) {
                list = this.getEntityWorld().getOtherEntities(this, new Box(this.getX() - 3.0, this.getY() - 3.0, this.getZ() - 3.0, this.getX() + 3.0, this.getY() + 6.0 + 3.0, this.getZ() + 3.0), Entity::isAlive);
                for (Entity lv3 : list) {
                    lv3.onStruckByLightning((ServerWorld)this.getEntityWorld(), this);
                }
                this.struckEntities.addAll(list);
                if (this.channeler != null) {
                    Criteria.CHANNELED_LIGHTNING.trigger(this.channeler, list);
                }
            }
        }
    }

    private BlockPos getAffectedBlockPos() {
        Vec3d lv = this.getEntityPos();
        return BlockPos.ofFloored(lv.x, lv.y - 1.0E-6, lv.z);
    }

    private void spawnFire(int spreadAttempts) {
        World world;
        if (this.cosmetic || !((world = this.getEntityWorld()) instanceof ServerWorld)) {
            return;
        }
        ServerWorld lv = (ServerWorld)world;
        BlockPos lv2 = this.getBlockPos();
        if (!lv.canFireSpread(lv2)) {
            return;
        }
        BlockState lv3 = AbstractFireBlock.getState(lv, lv2);
        if (lv.getBlockState(lv2).isAir() && lv3.canPlaceAt(lv, lv2)) {
            lv.setBlockState(lv2, lv3);
            ++this.blocksSetOnFire;
        }
        for (int j = 0; j < spreadAttempts; ++j) {
            BlockPos lv4 = lv2.add(this.random.nextInt(3) - 1, this.random.nextInt(3) - 1, this.random.nextInt(3) - 1);
            lv3 = AbstractFireBlock.getState(lv, lv4);
            if (!lv.getBlockState(lv4).isAir() || !lv3.canPlaceAt(lv, lv4)) continue;
            lv.setBlockState(lv4, lv3);
            ++this.blocksSetOnFire;
        }
    }

    private static void cleanOxidation(World world, BlockPos pos) {
        BlockState lv = world.getBlockState(pos);
        boolean bl = HoneycombItem.WAXED_TO_UNWAXED_BLOCKS.get().get(lv.getBlock()) != null;
        boolean bl2 = lv.getBlock() instanceof Oxidizable;
        if (!bl2 && !bl) {
            return;
        }
        if (bl2) {
            world.setBlockState(pos, Oxidizable.getUnaffectedOxidationState(world.getBlockState(pos)));
        }
        BlockPos.Mutable lv2 = pos.mutableCopy();
        int i = world.random.nextInt(3) + 3;
        for (int j = 0; j < i; ++j) {
            int k = world.random.nextInt(8) + 1;
            LightningEntity.cleanOxidationAround(world, pos, lv2, k);
        }
    }

    private static void cleanOxidationAround(World world, BlockPos pos, BlockPos.Mutable mutablePos, int count) {
        Optional<BlockPos> optional;
        mutablePos.set(pos);
        for (int j = 0; j < count && !(optional = LightningEntity.cleanOxidationAround(world, mutablePos)).isEmpty(); ++j) {
            mutablePos.set(optional.get());
        }
    }

    private static Optional<BlockPos> cleanOxidationAround(World world, BlockPos pos) {
        for (BlockPos lv : BlockPos.iterateRandomly(world.random, 10, pos, 1)) {
            BlockState lv2 = world.getBlockState(lv);
            if (!(lv2.getBlock() instanceof Oxidizable)) continue;
            Oxidizable.getDecreasedOxidationState(lv2).ifPresent(state -> world.setBlockState(lv, (BlockState)state));
            world.syncWorldEvent(WorldEvents.ELECTRICITY_SPARKS, lv, -1);
            return Optional.of(lv);
        }
        return Optional.empty();
    }

    @Override
    public boolean shouldRender(double distance) {
        double e = 64.0 * LightningEntity.getRenderDistanceMultiplier();
        return distance < e * e;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
    }

    @Override
    protected void readCustomData(ReadView view) {
    }

    @Override
    protected void writeCustomData(WriteView view) {
    }

    public int getBlocksSetOnFire() {
        return this.blocksSetOnFire;
    }

    public Stream<Entity> getStruckEntities() {
        return this.struckEntities.stream().filter(Entity::isAlive);
    }

    @Override
    public final boolean damage(ServerWorld world, DamageSource source, float amount) {
        return false;
    }
}

