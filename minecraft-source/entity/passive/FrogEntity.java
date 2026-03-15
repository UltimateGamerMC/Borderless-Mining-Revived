/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/brain/Brain;createProfile(Ljava/util/Collection;Ljava/util/Collection;)Lnet/minecraft/entity/ai/brain/Brain$Profile;
 *   Lnet/minecraft/entity/ai/brain/Brain$Profile;deserialize(Lcom/mojang/serialization/Dynamic;)Lnet/minecraft/entity/ai/brain/Brain;
 *   Lnet/minecraft/entity/passive/FrogBrain;create(Lnet/minecraft/entity/ai/brain/Brain;)Lnet/minecraft/entity/ai/brain/Brain;
 *   Lnet/minecraft/entity/passive/AnimalEntity;initDataTracker(Lnet/minecraft/entity/data/DataTracker$Builder;)V
 *   Lnet/minecraft/entity/passive/AnimalEntity;copyComponentsFrom(Lnet/minecraft/component/ComponentsAccess;)V
 *   Lnet/minecraft/entity/passive/AnimalEntity;writeCustomData(Lnet/minecraft/storage/WriteView;)V
 *   Lnet/minecraft/entity/Variants;writeData(Lnet/minecraft/storage/WriteView;Lnet/minecraft/registry/entry/RegistryEntry;)V
 *   Lnet/minecraft/entity/passive/AnimalEntity;readCustomData(Lnet/minecraft/storage/ReadView;)V
 *   Lnet/minecraft/entity/Variants;fromData(Lnet/minecraft/storage/ReadView;Lnet/minecraft/registry/RegistryKey;)Ljava/util/Optional;
 *   Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V
 *   Lnet/minecraft/entity/ai/brain/Brain;tick(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;)V
 *   Lnet/minecraft/entity/passive/FrogBrain;updateActivities(Lnet/minecraft/entity/passive/FrogEntity;)V
 *   Lnet/minecraft/entity/passive/AnimalEntity;mobTick(Lnet/minecraft/server/world/ServerWorld;)V
 *   Lnet/minecraft/entity/AnimationState;start(I)V
 *   Lnet/minecraft/entity/passive/AnimalEntity;onTrackedDataSet(Lnet/minecraft/entity/data/TrackedData;)V
 *   Lnet/minecraft/entity/LimbAnimator;updateLimbs(FFF)V
 *   Lnet/minecraft/world/World;playSoundFromEntity(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V
 *   Lnet/minecraft/entity/EntityType;create(Lnet/minecraft/world/World;Lnet/minecraft/entity/SpawnReason;)Lnet/minecraft/entity/Entity;
 *   Lnet/minecraft/entity/passive/FrogBrain;coolDownLongJump(Lnet/minecraft/entity/passive/FrogEntity;Lnet/minecraft/util/math/random/Random;)V
 *   Lnet/minecraft/entity/ai/brain/Brain;remember(Lnet/minecraft/entity/ai/brain/MemoryModuleType;Ljava/lang/Object;)V
 *   Lnet/minecraft/entity/spawn/SpawnContext;of(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/entity/spawn/SpawnContext;
 *   Lnet/minecraft/entity/Variants;select(Lnet/minecraft/entity/spawn/SpawnContext;Lnet/minecraft/registry/RegistryKey;)Ljava/util/Optional;
 *   Lnet/minecraft/entity/passive/AnimalEntity;initialize(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;)Lnet/minecraft/entity/EntityData;
 *   Lnet/minecraft/entity/passive/AnimalEntity;createAnimalAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;
 *   Lnet/minecraft/entity/passive/AnimalEntity;computeFallDamage(DF)I
 *   Lnet/minecraft/entity/data/DataTracker;registerData(Ljava/lang/Class;Lnet/minecraft/entity/data/TrackedDataHandler;)Lnet/minecraft/entity/data/TrackedData;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/passive/FrogEntity;createBrainProfile()Lnet/minecraft/entity/ai/brain/Brain$Profile;
 *   Lnet/minecraft/entity/passive/FrogEntity;castComponentValue(Lnet/minecraft/component/ComponentType;Ljava/lang/Object;)Ljava/lang/Object;
 *   Lnet/minecraft/entity/passive/FrogEntity;copyComponentFrom(Lnet/minecraft/component/ComponentsAccess;Lnet/minecraft/component/ComponentType;)Z
 *   Lnet/minecraft/entity/passive/FrogEntity;breed(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/AnimalEntity;Lnet/minecraft/entity/passive/PassiveEntity;)V
 *   Lnet/minecraft/entity/passive/FrogEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V
 *   Lnet/minecraft/entity/passive/FrogEntity;updateVelocity(FLnet/minecraft/util/math/Vec3d;)V
 *   Lnet/minecraft/entity/passive/FrogEntity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V
 */
package net.minecraft.entity.passive;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import net.minecraft.block.BlockState;
import net.minecraft.component.ComponentType;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.Variants;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.control.AquaticMoveControl;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.pathing.AmphibiousPathNodeMaker;
import net.minecraft.entity.ai.pathing.AmphibiousSwimNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathContext;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.entity.ai.pathing.PathNodeNavigator;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.FrogBrain;
import net.minecraft.entity.passive.FrogVariant;
import net.minecraft.entity.passive.FrogVariants;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.spawn.SpawnContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.profiler.Profilers;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jspecify.annotations.Nullable;

public class FrogEntity
extends AnimalEntity {
    protected static final ImmutableList<SensorType<? extends Sensor<? super FrogEntity>>> SENSORS = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.HURT_BY, SensorType.FROG_ATTACKABLES, SensorType.FROG_TEMPTATIONS, SensorType.IS_IN_WATER);
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.MOBS, MemoryModuleType.VISIBLE_MOBS, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.BREED_TARGET, MemoryModuleType.LONG_JUMP_COOLING_DOWN, MemoryModuleType.LONG_JUMP_MID_JUMP, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.TEMPTING_PLAYER, MemoryModuleType.TEMPTATION_COOLDOWN_TICKS, new MemoryModuleType[]{MemoryModuleType.IS_TEMPTED, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.NEAREST_ATTACKABLE, MemoryModuleType.IS_IN_WATER, MemoryModuleType.IS_PREGNANT, MemoryModuleType.IS_PANICKING, MemoryModuleType.UNREACHABLE_TONGUE_TARGETS});
    private static final TrackedData<RegistryEntry<FrogVariant>> VARIANT = DataTracker.registerData(FrogEntity.class, TrackedDataHandlerRegistry.FROG_VARIANT);
    private static final TrackedData<OptionalInt> TARGET = DataTracker.registerData(FrogEntity.class, TrackedDataHandlerRegistry.OPTIONAL_INT);
    private static final int field_37459 = 5;
    private static final RegistryKey<FrogVariant> DEFAULT_VARIANT_KEY = FrogVariants.TEMPERATE;
    public final AnimationState longJumpingAnimationState = new AnimationState();
    public final AnimationState croakingAnimationState = new AnimationState();
    public final AnimationState usingTongueAnimationState = new AnimationState();
    public final AnimationState idlingInWaterAnimationState = new AnimationState();

    public FrogEntity(EntityType<? extends AnimalEntity> arg, World arg2) {
        super(arg, arg2);
        this.lookControl = new FrogLookControl(this);
        this.setPathfindingPenalty(PathNodeType.WATER, 4.0f);
        this.setPathfindingPenalty(PathNodeType.TRAPDOOR, -1.0f);
        this.moveControl = new AquaticMoveControl(this, 85, 10, 0.02f, 0.1f, true);
    }

    protected Brain.Profile<FrogEntity> createBrainProfile() {
        return Brain.createProfile(MEMORY_MODULES, SENSORS);
    }

    @Override
    protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
        return FrogBrain.create(this.createBrainProfile().deserialize(dynamic));
    }

    public Brain<FrogEntity> getBrain() {
        return super.getBrain();
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        RegistryWrapper.Impl lv = this.getRegistryManager().getOrThrow(RegistryKeys.FROG_VARIANT);
        builder.add(VARIANT, Variants.getOrDefaultOrThrow(this.getRegistryManager(), DEFAULT_VARIANT_KEY));
        builder.add(TARGET, OptionalInt.empty());
    }

    public void clearFrogTarget() {
        this.dataTracker.set(TARGET, OptionalInt.empty());
    }

    public Optional<Entity> getFrogTarget() {
        return this.dataTracker.get(TARGET).stream().mapToObj(this.getEntityWorld()::getEntityById).filter(Objects::nonNull).findFirst();
    }

    public void setFrogTarget(Entity entity) {
        this.dataTracker.set(TARGET, OptionalInt.of(entity.getId()));
    }

    @Override
    public int getMaxLookYawChange() {
        return 35;
    }

    @Override
    public int getMaxHeadRotation() {
        return 5;
    }

    public RegistryEntry<FrogVariant> getVariant() {
        return this.dataTracker.get(VARIANT);
    }

    private void setVariant(RegistryEntry<FrogVariant> variant) {
        this.dataTracker.set(VARIANT, variant);
    }

    @Override
    public <T> @Nullable T get(ComponentType<? extends T> type) {
        if (type == DataComponentTypes.FROG_VARIANT) {
            return FrogEntity.castComponentValue(type, this.getVariant());
        }
        return super.get(type);
    }

    @Override
    protected void copyComponentsFrom(ComponentsAccess from) {
        this.copyComponentFrom(from, DataComponentTypes.FROG_VARIANT);
        super.copyComponentsFrom(from);
    }

    @Override
    protected <T> boolean setApplicableComponent(ComponentType<T> type, T value) {
        if (type == DataComponentTypes.FROG_VARIANT) {
            this.setVariant(FrogEntity.castComponentValue(DataComponentTypes.FROG_VARIANT, value));
            return true;
        }
        return super.setApplicableComponent(type, value);
    }

    @Override
    protected void writeCustomData(WriteView view) {
        super.writeCustomData(view);
        Variants.writeData(view, this.getVariant());
    }

    @Override
    protected void readCustomData(ReadView view) {
        super.readCustomData(view);
        Variants.fromData(view, RegistryKeys.FROG_VARIANT).ifPresent(this::setVariant);
    }

    @Override
    protected void mobTick(ServerWorld world) {
        Profiler lv = Profilers.get();
        lv.push("frogBrain");
        this.getBrain().tick(world, this);
        lv.pop();
        lv.push("frogActivityUpdate");
        FrogBrain.updateActivities(this);
        lv.pop();
        super.mobTick(world);
    }

    @Override
    public void tick() {
        if (this.getEntityWorld().isClient()) {
            this.idlingInWaterAnimationState.setRunning(this.isTouchingWater() && !this.limbAnimator.isLimbMoving(), this.age);
        }
        super.tick();
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        if (POSE.equals(data)) {
            EntityPose lv = this.getPose();
            if (lv == EntityPose.LONG_JUMPING) {
                this.longJumpingAnimationState.start(this.age);
            } else {
                this.longJumpingAnimationState.stop();
            }
            if (lv == EntityPose.CROAKING) {
                this.croakingAnimationState.start(this.age);
            } else {
                this.croakingAnimationState.stop();
            }
            if (lv == EntityPose.USING_TONGUE) {
                this.usingTongueAnimationState.start(this.age);
            } else {
                this.usingTongueAnimationState.stop();
            }
        }
        super.onTrackedDataSet(data);
    }

    @Override
    protected void updateLimbs(float posDelta) {
        float g = this.longJumpingAnimationState.isRunning() ? 0.0f : Math.min(posDelta * 25.0f, 1.0f);
        this.limbAnimator.updateLimbs(g, 0.4f, this.isBaby() ? 3.0f : 1.0f);
    }

    @Override
    public void playEatSound() {
        this.getEntityWorld().playSoundFromEntity(null, this, SoundEvents.ENTITY_FROG_EAT, SoundCategory.NEUTRAL, 2.0f, 1.0f);
    }

    @Override
    public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        FrogEntity lv = EntityType.FROG.create(world, SpawnReason.BREEDING);
        if (lv != null) {
            FrogBrain.coolDownLongJump(lv, world.getRandom());
        }
        return lv;
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    public void setBaby(boolean baby) {
    }

    @Override
    public void breed(ServerWorld world, AnimalEntity other) {
        this.breed(world, other, null);
        this.getBrain().remember(MemoryModuleType.IS_PREGNANT, Unit.INSTANCE);
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        Variants.select(SpawnContext.of(world, this.getBlockPos()), RegistryKeys.FROG_VARIANT).ifPresent(this::setVariant);
        FrogBrain.coolDownLongJump(this, world.getRandom());
        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    public static DefaultAttributeContainer.Builder createFrogAttributes() {
        return AnimalEntity.createAnimalAttributes().add(EntityAttributes.MOVEMENT_SPEED, 1.0).add(EntityAttributes.MAX_HEALTH, 10.0).add(EntityAttributes.ATTACK_DAMAGE, 10.0).add(EntityAttributes.STEP_HEIGHT, 1.0);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_FROG_AMBIENT;
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_FROG_HURT;
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_FROG_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_FROG_STEP, 0.15f, 1.0f);
    }

    @Override
    public boolean isPushedByFluids() {
        return false;
    }

    @Override
    protected int computeFallDamage(double fallDistance, float damagePerDistance) {
        return super.computeFallDamage(fallDistance, damagePerDistance) - 5;
    }

    @Override
    protected void travelInWater(Vec3d movementInput, double gravity, boolean falling, double y) {
        this.updateVelocity(this.getMovementSpeed(), movementInput);
        this.move(MovementType.SELF, this.getVelocity());
        this.setVelocity(this.getVelocity().multiply(0.9));
    }

    public static boolean isValidFrogFood(LivingEntity entity) {
        SlimeEntity lv;
        if (entity instanceof SlimeEntity && (lv = (SlimeEntity)entity).getSize() != 1) {
            return false;
        }
        return entity.getType().isIn(EntityTypeTags.FROG_FOOD);
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        return new FrogSwimNavigation(this, world);
    }

    @Override
    public @Nullable LivingEntity getTarget() {
        return this.getTargetInBrain();
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isIn(ItemTags.FROG_FOOD);
    }

    public static boolean canSpawn(EntityType<? extends AnimalEntity> type, WorldAccess world, SpawnReason reason, BlockPos pos, Random random) {
        return world.getBlockState(pos.down()).isIn(BlockTags.FROGS_SPAWNABLE_ON) && FrogEntity.isLightLevelValidForNaturalSpawn(world, pos);
    }

    class FrogLookControl
    extends LookControl {
        FrogLookControl(MobEntity entity) {
            super(entity);
        }

        @Override
        protected boolean shouldStayHorizontal() {
            return FrogEntity.this.getFrogTarget().isEmpty();
        }
    }

    static class FrogSwimNavigation
    extends AmphibiousSwimNavigation {
        FrogSwimNavigation(FrogEntity frog, World world) {
            super(frog, world);
        }

        @Override
        public boolean canJumpToNext(PathNodeType nodeType) {
            return nodeType != PathNodeType.WATER_BORDER && super.canJumpToNext(nodeType);
        }

        @Override
        protected PathNodeNavigator createPathNodeNavigator(int range) {
            this.nodeMaker = new FrogSwimPathNodeMaker(true);
            return new PathNodeNavigator(this.nodeMaker, range);
        }
    }

    static class FrogSwimPathNodeMaker
    extends AmphibiousPathNodeMaker {
        private final BlockPos.Mutable pos = new BlockPos.Mutable();

        public FrogSwimPathNodeMaker(boolean bl) {
            super(bl);
        }

        @Override
        public PathNode getStart() {
            if (!this.entity.isTouchingWater()) {
                return super.getStart();
            }
            return this.getStart(new BlockPos(MathHelper.floor(this.entity.getBoundingBox().minX), MathHelper.floor(this.entity.getBoundingBox().minY), MathHelper.floor(this.entity.getBoundingBox().minZ)));
        }

        @Override
        public PathNodeType getDefaultNodeType(PathContext context, int x, int y, int z) {
            this.pos.set(x, y - 1, z);
            BlockState lv = context.getBlockState(this.pos);
            if (lv.isIn(BlockTags.FROG_PREFER_JUMP_TO)) {
                return PathNodeType.OPEN;
            }
            return super.getDefaultNodeType(context, x, y, z);
        }
    }
}

