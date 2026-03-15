/*
 * External method calls:
 *   Lnet/minecraft/entity/mob/HostileEntity;createHostileAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;
 *   Lnet/minecraft/entity/mob/HostileEntity;initDataTracker(Lnet/minecraft/entity/data/DataTracker$Builder;)V
 *   Lnet/minecraft/entity/attribute/EntityAttributeInstance;removeModifier(Lnet/minecraft/util/Identifier;)Z
 *   Lnet/minecraft/entity/attribute/EntityAttributeInstance;addTemporaryModifier(Lnet/minecraft/entity/attribute/EntityAttributeModifier;)V
 *   Lnet/minecraft/entity/mob/HostileEntity;onTrackedDataSet(Lnet/minecraft/entity/data/TrackedData;)V
 *   Lnet/minecraft/server/world/ServerWorld;syncWorldEvent(Lnet/minecraft/entity/Entity;ILnet/minecraft/util/math/BlockPos;I)V
 *   Lnet/minecraft/entity/conversion/EntityConversionContext;create(Lnet/minecraft/entity/mob/MobEntity;ZZ)Lnet/minecraft/entity/conversion/EntityConversionContext;
 *   Lnet/minecraft/entity/passive/VillagerEntity;convertTo(Lnet/minecraft/entity/EntityType;Lnet/minecraft/entity/conversion/EntityConversionContext;Lnet/minecraft/entity/conversion/EntityConversionContext$Finalizer;)Lnet/minecraft/entity/mob/MobEntity;
 *   Lnet/minecraft/entity/mob/HostileEntity;damage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)Z
 *   Lnet/minecraft/entity/EntityType;create(Lnet/minecraft/world/World;Lnet/minecraft/entity/SpawnReason;)Lnet/minecraft/entity/Entity;
 *   Lnet/minecraft/server/world/ServerWorld;doesNotIntersectEntities(Lnet/minecraft/entity/Entity;)Z
 *   Lnet/minecraft/server/world/ServerWorld;containsFluid(Lnet/minecraft/util/math/Box;)Z
 *   Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V
 *   Lnet/minecraft/entity/attribute/EntityAttributeInstance;addPersistentModifier(Lnet/minecraft/entity/attribute/EntityAttributeModifier;)V
 *   Lnet/minecraft/entity/mob/HostileEntity;tryAttack(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/Entity;)Z
 *   Lnet/minecraft/entity/mob/HostileEntity;initEquipment(Lnet/minecraft/util/math/random/Random;Lnet/minecraft/world/LocalDifficulty;)V
 *   Lnet/minecraft/entity/mob/HostileEntity;writeCustomData(Lnet/minecraft/storage/WriteView;)V
 *   Lnet/minecraft/storage/WriteView;putBoolean(Ljava/lang/String;Z)V
 *   Lnet/minecraft/storage/WriteView;putInt(Ljava/lang/String;I)V
 *   Lnet/minecraft/entity/mob/HostileEntity;readCustomData(Lnet/minecraft/storage/ReadView;)V
 *   Lnet/minecraft/entity/mob/HostileEntity;onKilledOther(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/damage/DamageSource;)Z
 *   Lnet/minecraft/entity/mob/HostileEntity;initialize(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;)Lnet/minecraft/entity/EntityData;
 *   Lnet/minecraft/entity/passive/ChickenEntity;refreshPositionAndAngles(DDDFF)V
 *   Lnet/minecraft/entity/passive/ChickenEntity;initialize(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;)Lnet/minecraft/entity/EntityData;
 *   Lnet/minecraft/world/ServerWorldAccess;spawnEntity(Lnet/minecraft/entity/Entity;)Z
 *   Lnet/minecraft/entity/attribute/EntityAttributeInstance;overwritePersistentModifier(Lnet/minecraft/entity/attribute/EntityAttributeModifier;)V
 *   Lnet/minecraft/entity/mob/ZombieVillagerEntity;initialize(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;)Lnet/minecraft/entity/EntityData;
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/entity/data/DataTracker;registerData(Ljava/lang/Class;Lnet/minecraft/entity/data/TrackedDataHandler;)Lnet/minecraft/entity/data/TrackedData;
 *   Lnet/minecraft/entity/EntityDimensions;scaled(F)Lnet/minecraft/entity/EntityDimensions;
 *   Lnet/minecraft/entity/EntityDimensions;withEyeHeight(F)Lnet/minecraft/entity/EntityDimensions;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/mob/ZombieEntity;convertInWater(Lnet/minecraft/server/world/ServerWorld;)V
 *   Lnet/minecraft/entity/mob/ZombieEntity;convertTo(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/EntityType;)V
 *   Lnet/minecraft/entity/mob/ZombieEntity;convertTo(Lnet/minecraft/entity/EntityType;Lnet/minecraft/entity/conversion/EntityConversionContext;Lnet/minecraft/entity/conversion/EntityConversionContext$Finalizer;)Lnet/minecraft/entity/mob/MobEntity;
 *   Lnet/minecraft/entity/mob/ZombieEntity;initialize(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;)Lnet/minecraft/entity/EntityData;
 *   Lnet/minecraft/entity/mob/ZombieEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V
 *   Lnet/minecraft/entity/mob/ZombieEntity;equipStack(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/ItemStack;)V
 *   Lnet/minecraft/entity/mob/ZombieEntity;infectVillager(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/VillagerEntity;)Z
 *   Lnet/minecraft/entity/mob/ZombieEntity;startRiding(Lnet/minecraft/entity/Entity;ZZ)Z
 *   Lnet/minecraft/entity/mob/ZombieEntity;initEquipment(Lnet/minecraft/util/math/random/Random;Lnet/minecraft/world/LocalDifficulty;)V
 *   Lnet/minecraft/entity/mob/ZombieEntity;updateEnchantments(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/util/math/random/Random;Lnet/minecraft/world/LocalDifficulty;)V
 *   Lnet/minecraft/entity/mob/ZombieEntity;applyAttributeModifiers(F)V
 */
package net.minecraft.entity.mob;

import com.google.common.annotations.VisibleForTesting;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.BreakDoorGoal;
import net.minecraft.entity.ai.goal.ChargeKineticWeaponGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MoveThroughVillageGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.StepAndDestroyBlockGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.conversion.EntityConversionContext;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Holidays;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldEvents;
import org.jspecify.annotations.Nullable;

public class ZombieEntity
extends HostileEntity {
    private static final Identifier BABY_SPEED_MODIFIER_ID = Identifier.ofVanilla("baby");
    private static final EntityAttributeModifier BABY_SPEED_BONUS = new EntityAttributeModifier(BABY_SPEED_MODIFIER_ID, 0.5, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    private static final Identifier REINFORCEMENT_CALLER_CHARGE_MODIFIER_ID = Identifier.ofVanilla("reinforcement_caller_charge");
    private static final EntityAttributeModifier REINFORCEMENT_CALLEE_CHARGE_REINFORCEMENT_BONUS = new EntityAttributeModifier(Identifier.ofVanilla("reinforcement_callee_charge"), -0.05f, EntityAttributeModifier.Operation.ADD_VALUE);
    private static final Identifier LEADER_ZOMBIE_BONUS_MODIFIER_ID = Identifier.ofVanilla("leader_zombie_bonus");
    private static final Identifier ZOMBIE_RANDOM_SPAWN_BONUS_MODIFIER_ID = Identifier.ofVanilla("zombie_random_spawn_bonus");
    private static final TrackedData<Boolean> BABY = DataTracker.registerData(ZombieEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> ZOMBIE_TYPE = DataTracker.registerData(ZombieEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> CONVERTING_IN_WATER = DataTracker.registerData(ZombieEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final float field_30519 = 0.05f;
    public static final int field_30515 = 50;
    public static final int field_30516 = 40;
    public static final int field_30517 = 7;
    private static final int field_57696 = -1;
    private static final EntityDimensions BABY_BASE_DIMENSIONS = EntityType.ZOMBIE.getDimensions().scaled(0.5f).withEyeHeight(0.93f);
    private static final float field_30518 = 0.1f;
    private static final Predicate<Difficulty> DOOR_BREAK_DIFFICULTY_CHECKER = difficulty -> difficulty == Difficulty.HARD;
    private static final boolean DEFAULT_IS_BABY = false;
    private static final boolean DEFAULT_CAN_BREAK_DOORS = false;
    private static final int DEFAULT_IN_WATER_TIME = 0;
    private final BreakDoorGoal breakDoorsGoal = new BreakDoorGoal(this, DOOR_BREAK_DIFFICULTY_CHECKER);
    private boolean canBreakDoors = false;
    private int inWaterTime = 0;
    private int ticksUntilWaterConversion;

    public ZombieEntity(EntityType<? extends ZombieEntity> arg, World arg2) {
        super((EntityType<? extends HostileEntity>)arg, arg2);
    }

    public ZombieEntity(World world) {
        this((EntityType<? extends ZombieEntity>)EntityType.ZOMBIE, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(4, new DestroyEggGoal((PathAwareEntity)this, 1.0, 3));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.initCustomGoals();
    }

    protected void initCustomGoals() {
        this.goalSelector.add(2, new ChargeKineticWeaponGoal<ZombieEntity>(this, 1.0, 1.0, 10.0f, 2.0f));
        this.goalSelector.add(3, new ZombieAttackGoal(this, 1.0, false));
        this.goalSelector.add(6, new MoveThroughVillageGoal(this, 1.0, true, 4, this::canBreakDoors));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0));
        this.targetSelector.add(1, new RevengeGoal(this, new Class[0]).setGroupRevenge(ZombifiedPiglinEntity.class));
        this.targetSelector.add(2, new ActiveTargetGoal<PlayerEntity>((MobEntity)this, PlayerEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<MerchantEntity>((MobEntity)this, MerchantEntity.class, false));
        this.targetSelector.add(3, new ActiveTargetGoal<IronGolemEntity>((MobEntity)this, IronGolemEntity.class, true));
        this.targetSelector.add(5, new ActiveTargetGoal<TurtleEntity>(this, TurtleEntity.class, 10, true, false, TurtleEntity.BABY_TURTLE_ON_LAND_FILTER));
    }

    public static DefaultAttributeContainer.Builder createZombieAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.FOLLOW_RANGE, 35.0).add(EntityAttributes.MOVEMENT_SPEED, 0.23f).add(EntityAttributes.ATTACK_DAMAGE, 3.0).add(EntityAttributes.ARMOR, 2.0).add(EntityAttributes.SPAWN_REINFORCEMENTS);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(BABY, false);
        builder.add(ZOMBIE_TYPE, 0);
        builder.add(CONVERTING_IN_WATER, false);
    }

    public boolean isConvertingInWater() {
        return this.getDataTracker().get(CONVERTING_IN_WATER);
    }

    public boolean canBreakDoors() {
        return this.canBreakDoors;
    }

    public void setCanBreakDoors(boolean canBreakDoors) {
        if (this.navigation.canControlOpeningDoors()) {
            if (this.canBreakDoors != canBreakDoors) {
                this.canBreakDoors = canBreakDoors;
                this.navigation.setCanOpenDoors(canBreakDoors);
                if (canBreakDoors) {
                    this.goalSelector.add(1, this.breakDoorsGoal);
                } else {
                    this.goalSelector.remove(this.breakDoorsGoal);
                }
            }
        } else if (this.canBreakDoors) {
            this.goalSelector.remove(this.breakDoorsGoal);
            this.canBreakDoors = false;
        }
    }

    @Override
    public boolean isBaby() {
        return this.getDataTracker().get(BABY);
    }

    @Override
    protected int getExperienceToDrop(ServerWorld world) {
        if (this.isBaby()) {
            this.experiencePoints = (int)((double)this.experiencePoints * 2.5);
        }
        return super.getExperienceToDrop(world);
    }

    @Override
    public void setBaby(boolean baby) {
        this.getDataTracker().set(BABY, baby);
        if (this.getEntityWorld() != null && !this.getEntityWorld().isClient()) {
            EntityAttributeInstance lv = this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED);
            lv.removeModifier(BABY_SPEED_MODIFIER_ID);
            if (baby) {
                lv.addTemporaryModifier(BABY_SPEED_BONUS);
            }
        }
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        if (BABY.equals(data)) {
            this.calculateDimensions();
        }
        super.onTrackedDataSet(data);
    }

    protected boolean canConvertInWater() {
        return true;
    }

    @Override
    public void tick() {
        World world = this.getEntityWorld();
        if (world instanceof ServerWorld) {
            ServerWorld lv = (ServerWorld)world;
            if (this.isAlive() && !this.isAiDisabled()) {
                if (this.isConvertingInWater()) {
                    --this.ticksUntilWaterConversion;
                    if (this.ticksUntilWaterConversion < 0) {
                        this.convertInWater(lv);
                    }
                } else if (this.canConvertInWater()) {
                    if (this.isSubmergedIn(FluidTags.WATER)) {
                        ++this.inWaterTime;
                        if (this.inWaterTime >= 600) {
                            this.setTicksUntilWaterConversion(300);
                        }
                    } else {
                        this.inWaterTime = -1;
                    }
                }
            }
        }
        super.tick();
    }

    private void setTicksUntilWaterConversion(int ticksUntilWaterConversion) {
        this.ticksUntilWaterConversion = ticksUntilWaterConversion;
        this.getDataTracker().set(CONVERTING_IN_WATER, true);
    }

    protected void convertInWater(ServerWorld world) {
        this.convertTo(world, EntityType.DROWNED);
        if (!this.isSilent()) {
            world.syncWorldEvent(null, WorldEvents.ZOMBIE_CONVERTS_TO_DROWNED, this.getBlockPos(), 0);
        }
    }

    protected void convertTo(ServerWorld world, EntityType<? extends ZombieEntity> entityType) {
        this.convertTo(entityType, EntityConversionContext.create(this, true, true), newZombie -> newZombie.applyAttributeModifiers(world.getLocalDifficulty(newZombie.getBlockPos()).getClampedLocalDifficulty()));
    }

    @VisibleForTesting
    public boolean infectVillager(ServerWorld world, VillagerEntity villager) {
        ZombieVillagerEntity lv = villager.convertTo(EntityType.ZOMBIE_VILLAGER, EntityConversionContext.create(villager, true, true), zombieVillager -> {
            zombieVillager.initialize(world, world.getLocalDifficulty(zombieVillager.getBlockPos()), SpawnReason.CONVERSION, new ZombieData(false, true));
            zombieVillager.setVillagerData(villager.getVillagerData());
            zombieVillager.setGossip(villager.getGossip().copy());
            zombieVillager.setOfferData(villager.getOffers().copy());
            zombieVillager.setExperience(villager.getExperience());
            if (!this.isSilent()) {
                world.syncWorldEvent(null, WorldEvents.ZOMBIE_INFECTS_VILLAGER, this.getBlockPos(), 0);
            }
        });
        return lv != null;
    }

    protected boolean burnsInDaylight() {
        return true;
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        if (!super.damage(world, source, amount)) {
            return false;
        }
        LivingEntity lv = this.getTarget();
        if (lv == null && source.getAttacker() instanceof LivingEntity) {
            lv = (LivingEntity)source.getAttacker();
        }
        if (lv != null && world.getDifficulty() == Difficulty.HARD && (double)this.random.nextFloat() < this.getAttributeValue(EntityAttributes.SPAWN_REINFORCEMENTS) && world.shouldSpawnMonsters()) {
            int i = MathHelper.floor(this.getX());
            int j = MathHelper.floor(this.getY());
            int k = MathHelper.floor(this.getZ());
            EntityType<? extends ZombieEntity> lv2 = this.getType();
            ZombieEntity lv3 = lv2.create(world, SpawnReason.REINFORCEMENT);
            if (lv3 == null) {
                return true;
            }
            for (int l = 0; l < 50; ++l) {
                int o;
                int n;
                int m = i + MathHelper.nextInt(this.random, 7, 40) * MathHelper.nextInt(this.random, -1, 1);
                BlockPos lv4 = new BlockPos(m, n = j + MathHelper.nextInt(this.random, 7, 40) * MathHelper.nextInt(this.random, -1, 1), o = k + MathHelper.nextInt(this.random, 7, 40) * MathHelper.nextInt(this.random, -1, 1));
                if (!SpawnRestriction.isSpawnPosAllowed(lv2, world, lv4) || !SpawnRestriction.canSpawn(lv2, world, SpawnReason.REINFORCEMENT, lv4, world.random)) continue;
                lv3.setPosition(m, n, o);
                if (world.isPlayerInRange(m, n, o, 7.0) || !world.doesNotIntersectEntities(lv3) || !world.isSpaceEmpty(lv3) || !lv3.canSpawnAsReinforcementInFluid() && world.containsFluid(lv3.getBoundingBox())) continue;
                lv3.setTarget(lv);
                lv3.initialize(world, world.getLocalDifficulty(lv3.getBlockPos()), SpawnReason.REINFORCEMENT, null);
                world.spawnEntityAndPassengers(lv3);
                EntityAttributeInstance lv5 = this.getAttributeInstance(EntityAttributes.SPAWN_REINFORCEMENTS);
                EntityAttributeModifier lv6 = lv5.getModifier(REINFORCEMENT_CALLER_CHARGE_MODIFIER_ID);
                double d = lv6 != null ? lv6.value() : 0.0;
                lv5.removeModifier(REINFORCEMENT_CALLER_CHARGE_MODIFIER_ID);
                lv5.addPersistentModifier(new EntityAttributeModifier(REINFORCEMENT_CALLER_CHARGE_MODIFIER_ID, d - 0.05, EntityAttributeModifier.Operation.ADD_VALUE));
                lv3.getAttributeInstance(EntityAttributes.SPAWN_REINFORCEMENTS).addPersistentModifier(REINFORCEMENT_CALLEE_CHARGE_REINFORCEMENT_BONUS);
                break;
            }
        }
        return true;
    }

    @Override
    public boolean tryAttack(ServerWorld world, Entity target) {
        boolean bl = super.tryAttack(world, target);
        if (bl) {
            float f = world.getLocalDifficulty(this.getBlockPos()).getLocalDifficulty();
            if (this.getMainHandStack().isEmpty() && this.isOnFire() && this.random.nextFloat() < f * 0.3f) {
                target.setOnFireFor(2 * (int)f);
            }
        }
        return bl;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ZOMBIE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_ZOMBIE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ZOMBIE_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.ENTITY_ZOMBIE_STEP;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(this.getStepSound(), 0.15f, 1.0f);
    }

    public EntityType<? extends ZombieEntity> getType() {
        return super.getType();
    }

    protected boolean canSpawnAsReinforcementInFluid() {
        return false;
    }

    @Override
    protected void initEquipment(Random random, LocalDifficulty localDifficulty) {
        super.initEquipment(random, localDifficulty);
        float f = random.nextFloat();
        float f2 = this.getEntityWorld().getDifficulty() == Difficulty.HARD ? 0.05f : 0.01f;
        if (f < f2) {
            int i = random.nextInt(6);
            if (i == 0) {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
            } else if (i == 1) {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SPEAR));
            } else {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SHOVEL));
            }
        }
    }

    @Override
    protected void writeCustomData(WriteView view) {
        super.writeCustomData(view);
        view.putBoolean("IsBaby", this.isBaby());
        view.putBoolean("CanBreakDoors", this.canBreakDoors());
        view.putInt("InWaterTime", this.isTouchingWater() ? this.inWaterTime : -1);
        view.putInt("DrownedConversionTime", this.isConvertingInWater() ? this.ticksUntilWaterConversion : -1);
    }

    @Override
    protected void readCustomData(ReadView view) {
        super.readCustomData(view);
        this.setBaby(view.getBoolean("IsBaby", false));
        this.setCanBreakDoors(view.getBoolean("CanBreakDoors", false));
        this.inWaterTime = view.getInt("InWaterTime", 0);
        int i = view.getInt("DrownedConversionTime", -1);
        if (i != -1) {
            this.setTicksUntilWaterConversion(i);
        } else {
            this.getDataTracker().set(CONVERTING_IN_WATER, false);
        }
    }

    @Override
    public boolean onKilledOther(ServerWorld world, LivingEntity other, DamageSource damageSource) {
        boolean bl = super.onKilledOther(world, other, damageSource);
        if ((world.getDifficulty() == Difficulty.NORMAL || world.getDifficulty() == Difficulty.HARD) && other instanceof VillagerEntity) {
            VillagerEntity lv = (VillagerEntity)other;
            if (world.getDifficulty() != Difficulty.HARD && this.random.nextBoolean()) {
                return bl;
            }
            if (this.infectVillager(world, lv)) {
                bl = false;
            }
        }
        return bl;
    }

    @Override
    public EntityDimensions getBaseDimensions(EntityPose pose) {
        return this.isBaby() ? BABY_BASE_DIMENSIONS : super.getBaseDimensions(pose);
    }

    @Override
    public boolean canPickupItem(ItemStack stack) {
        if (stack.isIn(ItemTags.EGGS) && this.isBaby() && this.hasVehicle()) {
            return false;
        }
        return super.canPickupItem(stack);
    }

    @Override
    public boolean canGather(ServerWorld world, ItemStack stack) {
        if (stack.isOf(Items.GLOW_INK_SAC)) {
            return false;
        }
        return super.canGather(world, stack);
    }

    @Override
    public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        Random lv = world.getRandom();
        entityData = super.initialize(world, difficulty, spawnReason, entityData);
        float f = difficulty.getClampedLocalDifficulty();
        if (spawnReason != SpawnReason.CONVERSION) {
            this.setCanPickUpLoot(lv.nextFloat() < 0.55f * f);
        }
        if (entityData == null) {
            entityData = new ZombieData(ZombieEntity.shouldBeBaby(lv), true);
        }
        if (entityData instanceof ZombieData) {
            ZombieData lv2 = (ZombieData)entityData;
            if (lv2.baby) {
                this.setBaby(true);
                if (lv2.tryChickenJockey) {
                    ChickenEntity lv4;
                    if ((double)lv.nextFloat() < 0.05) {
                        List<Entity> list = world.getEntitiesByClass(ChickenEntity.class, this.getBoundingBox().expand(5.0, 3.0, 5.0), EntityPredicates.NOT_MOUNTED);
                        if (!list.isEmpty()) {
                            ChickenEntity lv3 = (ChickenEntity)list.get(0);
                            lv3.setHasJockey(true);
                            this.startRiding(lv3, false, false);
                        }
                    } else if ((double)lv.nextFloat() < 0.05 && (lv4 = EntityType.CHICKEN.create(this.getEntityWorld(), SpawnReason.JOCKEY)) != null) {
                        lv4.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), 0.0f);
                        lv4.initialize(world, difficulty, SpawnReason.JOCKEY, null);
                        lv4.setHasJockey(true);
                        this.startRiding(lv4, false, false);
                        world.spawnEntity(lv4);
                    }
                }
            }
            this.setCanBreakDoors(lv.nextFloat() < f * 0.1f);
            if (spawnReason != SpawnReason.CONVERSION) {
                this.initEquipment(lv, difficulty);
                this.updateEnchantments(world, lv, difficulty);
            }
        }
        if (this.getEquippedStack(EquipmentSlot.HEAD).isEmpty() && Holidays.isHalloween() && lv.nextFloat() < 0.25f) {
            this.equipStack(EquipmentSlot.HEAD, new ItemStack(lv.nextFloat() < 0.1f ? Blocks.JACK_O_LANTERN : Blocks.CARVED_PUMPKIN));
            this.setEquipmentDropChance(EquipmentSlot.HEAD, 0.0f);
        }
        this.applyAttributeModifiers(f);
        return entityData;
    }

    @VisibleForTesting
    public void setInWaterTime(int inWaterTime) {
        this.inWaterTime = inWaterTime;
    }

    @VisibleForTesting
    public void setTicksUntilWaterConversionDirect(int ticksUntilWaterConversion) {
        this.ticksUntilWaterConversion = ticksUntilWaterConversion;
    }

    public static boolean shouldBeBaby(Random random) {
        return random.nextFloat() < 0.05f;
    }

    protected void applyAttributeModifiers(float chanceMultiplier) {
        this.initAttributes();
        this.getAttributeInstance(EntityAttributes.KNOCKBACK_RESISTANCE).overwritePersistentModifier(new EntityAttributeModifier(RANDOM_SPAWN_BONUS_MODIFIER_ID, this.random.nextDouble() * (double)0.05f, EntityAttributeModifier.Operation.ADD_VALUE));
        double d = this.random.nextDouble() * 1.5 * (double)chanceMultiplier;
        if (d > 1.0) {
            this.getAttributeInstance(EntityAttributes.FOLLOW_RANGE).overwritePersistentModifier(new EntityAttributeModifier(ZOMBIE_RANDOM_SPAWN_BONUS_MODIFIER_ID, d, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }
        if (this.random.nextFloat() < chanceMultiplier * 0.05f) {
            this.getAttributeInstance(EntityAttributes.SPAWN_REINFORCEMENTS).overwritePersistentModifier(new EntityAttributeModifier(LEADER_ZOMBIE_BONUS_MODIFIER_ID, this.random.nextDouble() * 0.25 + 0.5, EntityAttributeModifier.Operation.ADD_VALUE));
            this.getAttributeInstance(EntityAttributes.MAX_HEALTH).overwritePersistentModifier(new EntityAttributeModifier(LEADER_ZOMBIE_BONUS_MODIFIER_ID, this.random.nextDouble() * 3.0 + 1.0, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
            this.setCanBreakDoors(true);
        }
    }

    protected void initAttributes() {
        this.getAttributeInstance(EntityAttributes.SPAWN_REINFORCEMENTS).setBaseValue(this.random.nextDouble() * (double)0.1f);
    }

    class DestroyEggGoal
    extends StepAndDestroyBlockGoal {
        DestroyEggGoal(PathAwareEntity mob, double speed, int maxYDifference) {
            super(Blocks.TURTLE_EGG, mob, speed, maxYDifference);
        }

        @Override
        public void tickStepping(WorldAccess world, BlockPos pos) {
            world.playSound(null, pos, SoundEvents.ENTITY_ZOMBIE_DESTROY_EGG, SoundCategory.HOSTILE, 0.5f, 0.9f + ZombieEntity.this.random.nextFloat() * 0.2f);
        }

        @Override
        public void onDestroyBlock(World world, BlockPos pos) {
            world.playSound(null, pos, SoundEvents.ENTITY_TURTLE_EGG_BREAK, SoundCategory.BLOCKS, 0.7f, 0.9f + world.random.nextFloat() * 0.2f);
        }

        @Override
        public double getDesiredDistanceToTarget() {
            return 1.14;
        }
    }

    public static class ZombieData
    implements EntityData {
        public final boolean baby;
        public final boolean tryChickenJockey;

        public ZombieData(boolean baby, boolean tryChickenJockey) {
            this.baby = baby;
            this.tryChickenJockey = tryChickenJockey;
        }
    }
}

