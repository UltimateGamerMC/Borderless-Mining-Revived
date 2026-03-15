/*
 * External method calls:
 *   Lnet/minecraft/entity/passive/AnimalEntity;createAnimalAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;
 *   Lnet/minecraft/entity/passive/AnimalEntity;initDataTracker(Lnet/minecraft/entity/data/DataTracker$Builder;)V
 *   Lnet/minecraft/entity/EntityDimensions;scaled(F)Lnet/minecraft/entity/EntityDimensions;
 *   Lnet/minecraft/entity/Leashable;createQuadLeashOffsets(Lnet/minecraft/entity/Entity;DDDD)[Lnet/minecraft/util/math/Vec3d;
 *   Lnet/minecraft/entity/AnimationState;startIfNotRunning(I)V
 *   Lnet/minecraft/entity/passive/AnimalEntity;onTrackedDataSet(Lnet/minecraft/entity/data/TrackedData;)V
 *   Lnet/minecraft/world/World;sendEntityStatus(Lnet/minecraft/entity/Entity;B)V
 *   Lnet/minecraft/entity/ai/pathing/EntityNavigation;findPathTo(Lnet/minecraft/util/math/BlockPos;I)Lnet/minecraft/entity/ai/pathing/Path;
 *   Lnet/minecraft/world/World;addParticleClient(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V
 *   Lnet/minecraft/world/World;playSoundClient(DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FFZ)V
 *   Lnet/minecraft/world/event/GameEvent$Emitter;of(Lnet/minecraft/entity/Entity;)Lnet/minecraft/world/event/GameEvent$Emitter;
 *   Lnet/minecraft/world/World;emitGameEvent(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/event/GameEvent$Emitter;)V
 *   Lnet/minecraft/entity/ai/brain/Brain;remember(Lnet/minecraft/entity/ai/brain/MemoryModuleType;Ljava/lang/Object;)V
 *   Lnet/minecraft/server/world/ServerWorld;spawnEntity(Lnet/minecraft/entity/Entity;)Z
 *   Lnet/minecraft/entity/passive/AnimalEntity;onDeath(Lnet/minecraft/entity/damage/DamageSource;)V
 *   Lnet/minecraft/entity/passive/AnimalEntity;interactMob(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;
 *   Lnet/minecraft/world/World;playSoundFromEntity(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V
 *   Lnet/minecraft/entity/EntityType;create(Lnet/minecraft/world/World;Lnet/minecraft/entity/SpawnReason;)Lnet/minecraft/entity/Entity;
 *   Lnet/minecraft/entity/ai/brain/Brain$Profile;deserialize(Lcom/mojang/serialization/Dynamic;)Lnet/minecraft/entity/ai/brain/Brain;
 *   Lnet/minecraft/entity/passive/SnifferBrain;create(Lnet/minecraft/entity/ai/brain/Brain;)Lnet/minecraft/entity/ai/brain/Brain;
 *   Lnet/minecraft/entity/ai/brain/Brain;createProfile(Ljava/util/Collection;Ljava/util/Collection;)Lnet/minecraft/entity/ai/brain/Brain$Profile;
 *   Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V
 *   Lnet/minecraft/entity/ai/brain/Brain;tick(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;)V
 *   Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V
 *   Lnet/minecraft/entity/passive/SnifferBrain;updateActivities(Lnet/minecraft/entity/passive/SnifferEntity;)V
 *   Lnet/minecraft/entity/passive/AnimalEntity;mobTick(Lnet/minecraft/server/world/ServerWorld;)V
 *   Lnet/minecraft/entity/ai/FuzzyTargeting;find(Lnet/minecraft/entity/mob/PathAwareEntity;II)Lnet/minecraft/util/math/Vec3d;
 *   Lnet/minecraft/entity/EntityDimensions;changing(FF)Lnet/minecraft/entity/EntityDimensions;
 *   Lnet/minecraft/entity/EntityDimensions;withEyeHeight(F)Lnet/minecraft/entity/EntityDimensions;
 *   Lnet/minecraft/entity/data/DataTracker;registerData(Ljava/lang/Class;Lnet/minecraft/entity/data/TrackedDataHandler;)Lnet/minecraft/entity/data/TrackedData;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/passive/SnifferEntity;playScentingSound()Lnet/minecraft/entity/passive/SnifferEntity;
 *   Lnet/minecraft/entity/passive/SnifferEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V
 *   Lnet/minecraft/entity/passive/SnifferEntity;addExploredPosition(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/entity/passive/SnifferEntity;
 *   Lnet/minecraft/entity/passive/SnifferEntity;forEachGiftedItem(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/registry/RegistryKey;Ljava/util/function/BiConsumer;)Z
 *   Lnet/minecraft/entity/passive/SnifferEntity;updateVelocity(FLnet/minecraft/util/math/Vec3d;)V
 *   Lnet/minecraft/entity/passive/SnifferEntity;breed(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/AnimalEntity;Lnet/minecraft/entity/passive/PassiveEntity;)V
 *   Lnet/minecraft/entity/passive/SnifferEntity;startState(Lnet/minecraft/entity/passive/SnifferEntity$State;)Lnet/minecraft/entity/passive/SnifferEntity;
 *   Lnet/minecraft/entity/passive/SnifferEntity;spawnDiggingParticles(Lnet/minecraft/entity/AnimationState;)Lnet/minecraft/entity/passive/SnifferEntity;
 *   Lnet/minecraft/entity/passive/SnifferEntity;createBrainProfile()Lnet/minecraft/entity/ai/brain/Brain$Profile;
 */
package net.minecraft.entity.passive;

import com.mojang.serialization.Dynamic;
import io.netty.buffer.ByteBuf;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.Leashable;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.SnifferBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTables;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.ValueLists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.profiler.Profilers;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class SnifferEntity
extends AnimalEntity {
    private static final int field_42656 = 1700;
    private static final int field_42657 = 6000;
    private static final int field_42658 = 30;
    private static final int field_42659 = 120;
    private static final int field_42661 = 48000;
    private static final float field_44785 = 0.4f;
    private static final EntityDimensions DIMENSIONS = EntityDimensions.changing(EntityType.SNIFFER.getWidth(), EntityType.SNIFFER.getHeight() - 0.4f).withEyeHeight(0.81f);
    private static final TrackedData<State> STATE = DataTracker.registerData(SnifferEntity.class, TrackedDataHandlerRegistry.SNIFFER_STATE);
    private static final TrackedData<Integer> FINISH_DIG_TIME = DataTracker.registerData(SnifferEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public final AnimationState feelingHappyAnimationState = new AnimationState();
    public final AnimationState scentingAnimationState = new AnimationState();
    public final AnimationState sniffingAnimationState = new AnimationState();
    public final AnimationState diggingAnimationState = new AnimationState();
    public final AnimationState risingAnimationState = new AnimationState();

    public static DefaultAttributeContainer.Builder createSnifferAttributes() {
        return AnimalEntity.createAnimalAttributes().add(EntityAttributes.MOVEMENT_SPEED, 0.1f).add(EntityAttributes.MAX_HEALTH, 14.0);
    }

    public SnifferEntity(EntityType<? extends AnimalEntity> arg, World arg2) {
        super(arg, arg2);
        this.getNavigation().setCanSwim(true);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0f);
        this.setPathfindingPenalty(PathNodeType.DANGER_POWDER_SNOW, -1.0f);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_CAUTIOUS, -1.0f);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(STATE, State.IDLING);
        builder.add(FINISH_DIG_TIME, 0);
    }

    @Override
    public void onStartPathfinding() {
        super.onStartPathfinding();
        if (this.isOnFire() || this.isTouchingWater()) {
            this.setPathfindingPenalty(PathNodeType.WATER, 0.0f);
        }
    }

    @Override
    public void onFinishPathfinding() {
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0f);
    }

    @Override
    public EntityDimensions getBaseDimensions(EntityPose pose) {
        if (this.getState() == State.DIGGING) {
            return DIMENSIONS.scaled(this.getScaleFactor());
        }
        return super.getBaseDimensions(pose);
    }

    public boolean isSearching() {
        return this.getState() == State.SEARCHING;
    }

    public boolean isTempted() {
        return this.brain.getOptionalRegisteredMemory(MemoryModuleType.IS_TEMPTED).orElse(false);
    }

    public boolean canTryToDig() {
        return !this.isTempted() && !this.isPanicking() && !this.isTouchingWater() && !this.isInLove() && this.isOnGround() && !this.hasVehicle() && !this.isLeashed();
    }

    public boolean isDiggingOrSearching() {
        return this.getState() == State.DIGGING || this.getState() == State.SEARCHING;
    }

    private BlockPos getDigPos() {
        Vec3d lv = this.getDigLocation();
        return BlockPos.ofFloored(lv.getX(), this.getY() + (double)0.2f, lv.getZ());
    }

    private Vec3d getDigLocation() {
        return this.getEntityPos().add(this.getRotationVecClient().multiply(2.25));
    }

    @Override
    public boolean canUseQuadLeashAttachmentPoint() {
        return true;
    }

    @Override
    public Vec3d[] getQuadLeashOffsets() {
        return Leashable.createQuadLeashOffsets(this, -0.01, 0.63, 0.38, 1.15);
    }

    private State getState() {
        return this.dataTracker.get(STATE);
    }

    private SnifferEntity setState(State state) {
        this.dataTracker.set(STATE, state);
        return this;
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        if (STATE.equals(data)) {
            State lv = this.getState();
            this.stopAnimations();
            switch (lv.ordinal()) {
                case 2: {
                    this.scentingAnimationState.startIfNotRunning(this.age);
                    break;
                }
                case 3: {
                    this.sniffingAnimationState.startIfNotRunning(this.age);
                    break;
                }
                case 5: {
                    this.diggingAnimationState.startIfNotRunning(this.age);
                    break;
                }
                case 6: {
                    this.risingAnimationState.startIfNotRunning(this.age);
                    break;
                }
                case 1: {
                    this.feelingHappyAnimationState.startIfNotRunning(this.age);
                }
            }
            this.calculateDimensions();
        }
        super.onTrackedDataSet(data);
    }

    private void stopAnimations() {
        this.diggingAnimationState.stop();
        this.sniffingAnimationState.stop();
        this.risingAnimationState.stop();
        this.feelingHappyAnimationState.stop();
        this.scentingAnimationState.stop();
    }

    public SnifferEntity startState(State state) {
        switch (state.ordinal()) {
            case 0: {
                this.setState(State.IDLING);
                break;
            }
            case 2: {
                this.setState(State.SCENTING).playScentingSound();
                break;
            }
            case 3: {
                this.playSound(SoundEvents.ENTITY_SNIFFER_SNIFFING, 1.0f, 1.0f);
                this.setState(State.SNIFFING);
                break;
            }
            case 4: {
                this.setState(State.SEARCHING);
                break;
            }
            case 5: {
                this.setState(State.DIGGING).setDigging();
                break;
            }
            case 6: {
                this.playSound(SoundEvents.ENTITY_SNIFFER_DIGGING_STOP, 1.0f, 1.0f);
                this.setState(State.RISING);
                break;
            }
            case 1: {
                this.playSound(SoundEvents.ENTITY_SNIFFER_HAPPY, 1.0f, 1.0f);
                this.setState(State.FEELING_HAPPY);
            }
        }
        return this;
    }

    private SnifferEntity playScentingSound() {
        this.playSound(SoundEvents.ENTITY_SNIFFER_SCENTING, 1.0f, this.isBaby() ? 1.3f : 1.0f);
        return this;
    }

    private SnifferEntity setDigging() {
        this.dataTracker.set(FINISH_DIG_TIME, this.age + 120);
        this.getEntityWorld().sendEntityStatus(this, EntityStatuses.START_DIGGING);
        return this;
    }

    public SnifferEntity finishDigging(boolean explored) {
        if (explored) {
            this.addExploredPosition(this.getSteppingPos());
        }
        return this;
    }

    Optional<BlockPos> findSniffingTargetPos() {
        return IntStream.range(0, 5).mapToObj(i -> FuzzyTargeting.find(this, 10 + 2 * i, 3)).filter(Objects::nonNull).map(BlockPos::ofFloored).filter(pos -> this.getEntityWorld().getWorldBorder().contains((BlockPos)pos)).map(BlockPos::down).filter(this::isDiggable).findFirst();
    }

    boolean canDig() {
        return !this.isPanicking() && !this.isTempted() && !this.isBaby() && !this.isTouchingWater() && this.isOnGround() && !this.hasVehicle() && this.isDiggable(this.getDigPos().down());
    }

    private boolean isDiggable(BlockPos pos) {
        return this.getEntityWorld().getBlockState(pos).isIn(BlockTags.SNIFFER_DIGGABLE_BLOCK) && this.getExploredPositions().noneMatch(arg2 -> GlobalPos.create(this.getEntityWorld().getRegistryKey(), pos).equals(arg2)) && Optional.ofNullable(this.getNavigation().findPathTo(pos, 1)).map(Path::reachesTarget).orElse(false) != false;
    }

    private void dropSeeds() {
        ServerWorld lv;
        block3: {
            block2: {
                World world = this.getEntityWorld();
                if (!(world instanceof ServerWorld)) break block2;
                lv = (ServerWorld)world;
                if (this.dataTracker.get(FINISH_DIG_TIME) == this.age) break block3;
            }
            return;
        }
        BlockPos lv2 = this.getDigPos();
        this.forEachGiftedItem(lv, LootTables.SNIFFER_DIGGING_GAMEPLAY, (arg2, arg3) -> {
            ItemEntity lv = new ItemEntity(this.getEntityWorld(), lv2.getX(), lv2.getY(), lv2.getZ(), (ItemStack)arg3);
            lv.setToDefaultPickupDelay();
            arg2.spawnEntity(lv);
        });
        this.playSound(SoundEvents.ENTITY_SNIFFER_DROP_SEED, 1.0f, 1.0f);
    }

    private SnifferEntity spawnDiggingParticles(AnimationState diggingAnimationState) {
        boolean bl;
        boolean bl2 = bl = diggingAnimationState.getTimeInMilliseconds(this.age) > 1700L && diggingAnimationState.getTimeInMilliseconds(this.age) < 6000L;
        if (bl) {
            BlockPos lv = this.getDigPos();
            BlockState lv2 = this.getEntityWorld().getBlockState(lv.down());
            if (lv2.getRenderType() != BlockRenderType.INVISIBLE) {
                for (int i = 0; i < 30; ++i) {
                    Vec3d lv3 = Vec3d.ofCenter(lv).add(0.0, -0.65f, 0.0);
                    this.getEntityWorld().addParticleClient(new BlockStateParticleEffect(ParticleTypes.BLOCK, lv2), lv3.x, lv3.y, lv3.z, 0.0, 0.0, 0.0);
                }
                if (this.age % 10 == 0) {
                    this.getEntityWorld().playSoundClient(this.getX(), this.getY(), this.getZ(), lv2.getSoundGroup().getHitSound(), this.getSoundCategory(), 0.5f, 0.5f, false);
                }
            }
        }
        if (this.age % 10 == 0) {
            this.getEntityWorld().emitGameEvent(GameEvent.ENTITY_ACTION, this.getDigPos(), GameEvent.Emitter.of(this));
        }
        return this;
    }

    private SnifferEntity addExploredPosition(BlockPos pos) {
        List list = this.getExploredPositions().limit(20L).collect(Collectors.toList());
        list.add(0, GlobalPos.create(this.getEntityWorld().getRegistryKey(), pos));
        this.getBrain().remember(MemoryModuleType.SNIFFER_EXPLORED_POSITIONS, list);
        return this;
    }

    private Stream<GlobalPos> getExploredPositions() {
        return this.getBrain().getOptionalRegisteredMemory(MemoryModuleType.SNIFFER_EXPLORED_POSITIONS).stream().flatMap(Collection::stream);
    }

    @Override
    public void jump() {
        double e;
        super.jump();
        double d = this.moveControl.getSpeed();
        if (d > 0.0 && (e = this.getVelocity().horizontalLengthSquared()) < 0.01) {
            this.updateVelocity(0.1f, new Vec3d(0.0, 0.0, 1.0));
        }
    }

    @Override
    public void breed(ServerWorld world, AnimalEntity other) {
        ItemStack lv = new ItemStack(Items.SNIFFER_EGG);
        ItemEntity lv2 = new ItemEntity(world, this.getEntityPos().getX(), this.getEntityPos().getY(), this.getEntityPos().getZ(), lv);
        lv2.setToDefaultPickupDelay();
        this.breed(world, other, null);
        this.playSound(SoundEvents.BLOCK_SNIFFER_EGG_PLOP, 1.0f, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 0.5f);
        world.spawnEntity(lv2);
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        this.startState(State.IDLING);
        super.onDeath(damageSource);
    }

    @Override
    public void tick() {
        switch (this.getState().ordinal()) {
            case 5: {
                this.spawnDiggingParticles(this.diggingAnimationState).dropSeeds();
                break;
            }
            case 4: {
                this.playSearchingSound();
            }
        }
        super.tick();
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack lv = player.getStackInHand(hand);
        boolean bl = this.isBreedingItem(lv);
        ActionResult lv2 = super.interactMob(player, hand);
        if (lv2.isAccepted() && bl) {
            this.playEatSound();
        }
        return lv2;
    }

    @Override
    protected void playEatSound() {
        this.getEntityWorld().playSoundFromEntity(null, this, SoundEvents.ENTITY_SNIFFER_EAT, SoundCategory.NEUTRAL, 1.0f, MathHelper.nextBetween(this.getEntityWorld().random, 0.8f, 1.2f));
    }

    private void playSearchingSound() {
        if (this.getEntityWorld().isClient() && this.age % 20 == 0) {
            this.getEntityWorld().playSoundClient(this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_SNIFFER_SEARCHING, this.getSoundCategory(), 1.0f, 1.0f, false);
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_SNIFFER_STEP, 0.15f, 1.0f);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return Set.of(State.DIGGING, State.SEARCHING).contains((Object)this.getState()) ? null : SoundEvents.ENTITY_SNIFFER_IDLE;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_SNIFFER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SNIFFER_DEATH;
    }

    @Override
    public int getMaxHeadRotation() {
        return 50;
    }

    @Override
    public void setBaby(boolean baby) {
        this.setBreedingAge(baby ? -48000 : 0);
    }

    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return EntityType.SNIFFER.create(world, SpawnReason.BREEDING);
    }

    @Override
    public boolean canBreedWith(AnimalEntity other) {
        if (other instanceof SnifferEntity) {
            SnifferEntity lv = (SnifferEntity)other;
            Set<State> set = Set.of(State.IDLING, State.SCENTING, State.FEELING_HAPPY);
            return set.contains((Object)this.getState()) && set.contains((Object)lv.getState()) && super.canBreedWith(other);
        }
        return false;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isIn(ItemTags.SNIFFER_FOOD);
    }

    @Override
    protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
        return SnifferBrain.create(this.createBrainProfile().deserialize(dynamic));
    }

    public Brain<SnifferEntity> getBrain() {
        return super.getBrain();
    }

    protected Brain.Profile<SnifferEntity> createBrainProfile() {
        return Brain.createProfile(SnifferBrain.MEMORY_MODULES, SnifferBrain.SENSORS);
    }

    @Override
    protected void mobTick(ServerWorld world) {
        Profiler lv = Profilers.get();
        lv.push("snifferBrain");
        this.getBrain().tick(world, this);
        lv.swap("snifferActivityUpdate");
        SnifferBrain.updateActivities(this);
        lv.pop();
        super.mobTick(world);
    }

    public static enum State {
        IDLING(0),
        FEELING_HAPPY(1),
        SCENTING(2),
        SNIFFING(3),
        SEARCHING(4),
        DIGGING(5),
        RISING(6);

        public static final IntFunction<State> INDEX_TO_VALUE;
        public static final PacketCodec<ByteBuf, State> PACKET_CODEC;
        private final int index;

        private State(int index) {
            this.index = index;
        }

        public int getIndex() {
            return this.index;
        }

        static {
            INDEX_TO_VALUE = ValueLists.createIndexToValueFunction(State::getIndex, State.values(), ValueLists.OutOfBoundsHandling.ZERO);
            PACKET_CODEC = PacketCodecs.indexed(INDEX_TO_VALUE, State::getIndex);
        }
    }
}

