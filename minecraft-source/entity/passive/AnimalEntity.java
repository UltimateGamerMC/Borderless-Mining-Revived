/*
 * External method calls:
 *   Lnet/minecraft/entity/mob/MobEntity;createMobAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;
 *   Lnet/minecraft/entity/passive/PassiveEntity;mobTick(Lnet/minecraft/server/world/ServerWorld;)V
 *   Lnet/minecraft/world/World;addParticleClient(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V
 *   Lnet/minecraft/entity/passive/PassiveEntity;applyDamage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)V
 *   Lnet/minecraft/entity/passive/PassiveEntity;writeCustomData(Lnet/minecraft/storage/WriteView;)V
 *   Lnet/minecraft/storage/WriteView;putInt(Ljava/lang/String;I)V
 *   Lnet/minecraft/entity/LazyEntityReference;writeData(Lnet/minecraft/entity/LazyEntityReference;Lnet/minecraft/storage/WriteView;Ljava/lang/String;)V
 *   Lnet/minecraft/entity/passive/PassiveEntity;readCustomData(Lnet/minecraft/storage/ReadView;)V
 *   Lnet/minecraft/entity/LazyEntityReference;fromData(Lnet/minecraft/storage/ReadView;Ljava/lang/String;)Lnet/minecraft/entity/LazyEntityReference;
 *   Lnet/minecraft/entity/passive/PassiveEntity;interactMob(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;
 *   Lnet/minecraft/entity/LazyEntityReference;of(Lnet/minecraft/world/entity/UniquelyIdentifiable;)Lnet/minecraft/entity/LazyEntityReference;
 *   Lnet/minecraft/world/World;sendEntityStatus(Lnet/minecraft/entity/Entity;B)V
 *   Lnet/minecraft/entity/LazyEntityReference;resolve(Lnet/minecraft/entity/LazyEntityReference;Lnet/minecraft/world/World;Ljava/lang/Class;)Lnet/minecraft/world/entity/UniquelyIdentifiable;
 *   Lnet/minecraft/entity/passive/PassiveEntity;refreshPositionAndAngles(DDDFF)V
 *   Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V
 *   Lnet/minecraft/server/world/ServerWorld;sendEntityStatus(Lnet/minecraft/entity/Entity;B)V
 *   Lnet/minecraft/server/world/ServerWorld;spawnEntity(Lnet/minecraft/entity/Entity;)Z
 *   Lnet/minecraft/entity/passive/PassiveEntity;handleStatus(B)V
 *   Lnet/minecraft/entity/passive/PassiveEntity;updatePassengerForDismount(Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/util/math/Vec3d;
 *   Lnet/minecraft/server/network/ServerPlayerEntity;incrementStat(Lnet/minecraft/util/Identifier;)V
 *   Lnet/minecraft/advancement/criterion/BredAnimalsCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/entity/passive/AnimalEntity;Lnet/minecraft/entity/passive/AnimalEntity;Lnet/minecraft/entity/passive/PassiveEntity;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/passive/AnimalEntity;eat(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;)V
 *   Lnet/minecraft/entity/passive/AnimalEntity;lovePlayer(Lnet/minecraft/entity/player/PlayerEntity;)V
 *   Lnet/minecraft/entity/passive/AnimalEntity;toGrowUpAge(I)I
 *   Lnet/minecraft/entity/passive/AnimalEntity;growUp(IZ)V
 *   Lnet/minecraft/entity/passive/AnimalEntity;createChild(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/PassiveEntity;)Lnet/minecraft/entity/passive/PassiveEntity;
 *   Lnet/minecraft/entity/passive/AnimalEntity;breed(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/AnimalEntity;Lnet/minecraft/entity/passive/PassiveEntity;)V
 */
package net.minecraft.entity.passive;

import java.util.Optional;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Dismounting;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LazyEntityReference;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.rule.GameRules;
import org.jspecify.annotations.Nullable;

public abstract class AnimalEntity
extends PassiveEntity {
    protected static final int BREEDING_COOLDOWN = 6000;
    private static final int DEFAULT_LOVE_TICKS = 0;
    private int loveTicks = 0;
    private @Nullable LazyEntityReference<ServerPlayerEntity> lovingPlayer;

    protected AnimalEntity(EntityType<? extends AnimalEntity> arg, World arg2) {
        super((EntityType<? extends PassiveEntity>)arg, arg2);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, 16.0f);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, -1.0f);
    }

    public static DefaultAttributeContainer.Builder createAnimalAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.TEMPT_RANGE, 10.0);
    }

    @Override
    protected void mobTick(ServerWorld world) {
        if (this.getBreedingAge() != 0) {
            this.loveTicks = 0;
        }
        super.mobTick(world);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (this.getBreedingAge() != 0) {
            this.loveTicks = 0;
        }
        if (this.loveTicks > 0) {
            --this.loveTicks;
            if (this.loveTicks % 10 == 0) {
                double d = this.random.nextGaussian() * 0.02;
                double e = this.random.nextGaussian() * 0.02;
                double f = this.random.nextGaussian() * 0.02;
                this.getEntityWorld().addParticleClient(ParticleTypes.HEART, this.getParticleX(1.0), this.getRandomBodyY() + 0.5, this.getParticleZ(1.0), d, e, f);
            }
        }
    }

    @Override
    protected void applyDamage(ServerWorld world, DamageSource source, float amount) {
        this.resetLoveTicks();
        super.applyDamage(world, source, amount);
    }

    @Override
    public float getPathfindingFavor(BlockPos pos, WorldView world) {
        if (world.getBlockState(pos.down()).isOf(Blocks.GRASS_BLOCK)) {
            return 10.0f;
        }
        return world.getPhototaxisFavor(pos);
    }

    @Override
    protected void writeCustomData(WriteView view) {
        super.writeCustomData(view);
        view.putInt("InLove", this.loveTicks);
        LazyEntityReference.writeData(this.lovingPlayer, view, "LoveCause");
    }

    @Override
    protected void readCustomData(ReadView view) {
        super.readCustomData(view);
        this.loveTicks = view.getInt("InLove", 0);
        this.lovingPlayer = LazyEntityReference.fromData(view, "LoveCause");
    }

    public static boolean isValidNaturalSpawn(EntityType<? extends AnimalEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        boolean bl = SpawnReason.isTrialSpawner(spawnReason) || AnimalEntity.isLightLevelValidForNaturalSpawn(world, pos);
        return world.getBlockState(pos.down()).isIn(BlockTags.ANIMALS_SPAWNABLE_ON) && bl;
    }

    protected static boolean isLightLevelValidForNaturalSpawn(BlockRenderView world, BlockPos pos) {
        return world.getBaseLightLevel(pos, 0) > 8;
    }

    @Override
    public int getMinAmbientSoundDelay() {
        return 120;
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return false;
    }

    @Override
    protected int getExperienceToDrop(ServerWorld world) {
        return 1 + this.random.nextInt(3);
    }

    public abstract boolean isBreedingItem(ItemStack var1);

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack lv = player.getStackInHand(hand);
        if (this.isBreedingItem(lv)) {
            int i = this.getBreedingAge();
            if (player instanceof ServerPlayerEntity) {
                ServerPlayerEntity lv2 = (ServerPlayerEntity)player;
                if (i == 0 && this.canEat()) {
                    this.eat(player, hand, lv);
                    this.lovePlayer(lv2);
                    this.playEatSound();
                    return ActionResult.SUCCESS_SERVER;
                }
            }
            if (this.isBaby()) {
                this.eat(player, hand, lv);
                this.growUp(AnimalEntity.toGrowUpAge(-i), true);
                this.playEatSound();
                return ActionResult.SUCCESS;
            }
            if (this.getEntityWorld().isClient()) {
                return ActionResult.CONSUME;
            }
        }
        return super.interactMob(player, hand);
    }

    protected void playEatSound() {
    }

    public boolean canEat() {
        return this.loveTicks <= 0;
    }

    public void lovePlayer(@Nullable PlayerEntity player) {
        this.loveTicks = 600;
        if (player instanceof ServerPlayerEntity) {
            ServerPlayerEntity lv = (ServerPlayerEntity)player;
            this.lovingPlayer = LazyEntityReference.of(lv);
        }
        this.getEntityWorld().sendEntityStatus(this, EntityStatuses.ADD_BREEDING_PARTICLES);
    }

    public void setLoveTicks(int loveTicks) {
        this.loveTicks = loveTicks;
    }

    public int getLoveTicks() {
        return this.loveTicks;
    }

    public @Nullable ServerPlayerEntity getLovingPlayer() {
        return LazyEntityReference.resolve(this.lovingPlayer, this.getEntityWorld(), ServerPlayerEntity.class);
    }

    public boolean isInLove() {
        return this.loveTicks > 0;
    }

    public void resetLoveTicks() {
        this.loveTicks = 0;
    }

    public boolean canBreedWith(AnimalEntity other) {
        if (other == this) {
            return false;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        return this.isInLove() && other.isInLove();
    }

    public void breed(ServerWorld world, AnimalEntity other) {
        PassiveEntity lv = this.createChild(world, other);
        if (lv == null) {
            return;
        }
        lv.setBaby(true);
        lv.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), 0.0f, 0.0f);
        this.breed(world, other, lv);
        world.spawnEntityAndPassengers(lv);
    }

    public void breed(ServerWorld world, AnimalEntity other, @Nullable PassiveEntity baby) {
        Optional.ofNullable(this.getLovingPlayer()).or(() -> Optional.ofNullable(other.getLovingPlayer())).ifPresent(player -> {
            player.incrementStat(Stats.ANIMALS_BRED);
            Criteria.BRED_ANIMALS.trigger((ServerPlayerEntity)player, this, other, baby);
        });
        this.setBreedingAge(6000);
        other.setBreedingAge(6000);
        this.resetLoveTicks();
        other.resetLoveTicks();
        world.sendEntityStatus(this, EntityStatuses.ADD_BREEDING_PARTICLES);
        if (world.getGameRules().getValue(GameRules.DO_MOB_LOOT).booleanValue()) {
            world.spawnEntity(new ExperienceOrbEntity(world, this.getX(), this.getY(), this.getZ(), this.getRandom().nextInt(7) + 1));
        }
    }

    @Override
    public void handleStatus(byte status) {
        if (status == EntityStatuses.ADD_BREEDING_PARTICLES) {
            for (int i = 0; i < 7; ++i) {
                double d = this.random.nextGaussian() * 0.02;
                double e = this.random.nextGaussian() * 0.02;
                double f = this.random.nextGaussian() * 0.02;
                this.getEntityWorld().addParticleClient(ParticleTypes.HEART, this.getParticleX(1.0), this.getRandomBodyY() + 0.5, this.getParticleZ(1.0), d, e, f);
            }
        } else {
            super.handleStatus(status);
        }
    }

    @Override
    public Vec3d updatePassengerForDismount(LivingEntity passenger) {
        Direction lv = this.getMovementDirection();
        if (lv.getAxis() == Direction.Axis.Y) {
            return super.updatePassengerForDismount(passenger);
        }
        int[][] is = Dismounting.getDismountOffsets(lv);
        BlockPos lv2 = this.getBlockPos();
        BlockPos.Mutable lv3 = new BlockPos.Mutable();
        for (EntityPose lv4 : passenger.getPoses()) {
            Box lv5 = passenger.getBoundingBox(lv4);
            for (int[] js : is) {
                lv3.set(lv2.getX() + js[0], lv2.getY(), lv2.getZ() + js[1]);
                double d = this.getEntityWorld().getDismountHeight(lv3);
                if (!Dismounting.canDismountInBlock(d)) continue;
                Vec3d lv6 = Vec3d.ofCenter(lv3, d);
                if (!Dismounting.canPlaceEntityAt(this.getEntityWorld(), passenger, lv5.offset(lv6))) continue;
                passenger.setPose(lv4);
                return lv6;
            }
        }
        return super.updatePassengerForDismount(passenger);
    }
}

