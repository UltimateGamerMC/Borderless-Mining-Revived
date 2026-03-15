/*
 * External method calls:
 *   Lnet/minecraft/entity/passive/AnimalEntity;mobTick(Lnet/minecraft/server/world/ServerWorld;)V
 *   Lnet/minecraft/entity/passive/AnimalEntity;createAnimalAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;
 *   Lnet/minecraft/entity/passive/AnimalEntity;initDataTracker(Lnet/minecraft/entity/data/DataTracker$Builder;)V
 *   Lnet/minecraft/entity/passive/AnimalEntity;handleStatus(B)V
 *   Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/EquipmentSlot;)V
 *   Lnet/minecraft/entity/passive/AnimalEntity;interactMob(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;
 *   Lnet/minecraft/server/world/ServerWorld;playSoundFromEntity(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V
 *   Lnet/minecraft/entity/passive/AnimalEntity;writeCustomData(Lnet/minecraft/storage/WriteView;)V
 *   Lnet/minecraft/storage/WriteView;putBoolean(Ljava/lang/String;Z)V
 *   Lnet/minecraft/entity/passive/AnimalEntity;readCustomData(Lnet/minecraft/storage/ReadView;)V
 *   Lnet/minecraft/storage/ReadView;read(Ljava/lang/String;Lcom/mojang/serialization/Codec;)Ljava/util/Optional;
 *   Lnet/minecraft/util/DyeColor;byIndex(I)Lnet/minecraft/util/DyeColor;
 *   Lnet/minecraft/entity/passive/AnimalEntity;copyComponentsFrom(Lnet/minecraft/component/ComponentsAccess;)V
 *   Lnet/minecraft/entity/passive/SheepColors;select(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/util/math/random/Random;)Lnet/minecraft/util/DyeColor;
 *   Lnet/minecraft/entity/EntityType;create(Lnet/minecraft/world/World;Lnet/minecraft/entity/SpawnReason;)Lnet/minecraft/entity/Entity;
 *   Lnet/minecraft/util/DyeColor;mixColors(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/DyeColor;Lnet/minecraft/util/DyeColor;)Lnet/minecraft/util/DyeColor;
 *   Lnet/minecraft/entity/passive/AnimalEntity;initialize(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;)Lnet/minecraft/entity/EntityData;
 *   Lnet/minecraft/item/ItemStack;copyWithCount(I)Lnet/minecraft/item/ItemStack;
 *   Lnet/minecraft/entity/data/DataTracker;registerData(Ljava/lang/Class;Lnet/minecraft/entity/data/TrackedDataHandler;)Lnet/minecraft/entity/data/TrackedData;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/passive/SheepEntity;sheared(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/sound/SoundCategory;Lnet/minecraft/item/ItemStack;)V
 *   Lnet/minecraft/entity/passive/SheepEntity;emitGameEvent(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/entity/Entity;)V
 *   Lnet/minecraft/entity/passive/SheepEntity;forEachShearedItem(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/item/ItemStack;Ljava/util/function/BiConsumer;)V
 *   Lnet/minecraft/entity/passive/SheepEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V
 *   Lnet/minecraft/entity/passive/SheepEntity;castComponentValue(Lnet/minecraft/component/ComponentType;Ljava/lang/Object;)Ljava/lang/Object;
 *   Lnet/minecraft/entity/passive/SheepEntity;copyComponentFrom(Lnet/minecraft/component/ComponentsAccess;Lnet/minecraft/component/ComponentType;)Z
 *   Lnet/minecraft/entity/passive/SheepEntity;growUp(I)V
 *   Lnet/minecraft/entity/passive/SheepEntity;selectSpawnColor(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/DyeColor;
 *   Lnet/minecraft/entity/passive/SheepEntity;createChild(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/PassiveEntity;)Lnet/minecraft/entity/passive/SheepEntity;
 *   Lnet/minecraft/entity/passive/SheepEntity;dropStack(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;F)Lnet/minecraft/entity/ItemEntity;
 */
package net.minecraft.entity.passive;

import net.minecraft.block.BlockState;
import net.minecraft.component.ComponentType;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Shearable;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.ai.goal.EatGrassGoal;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.SheepColors;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTables;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.event.GameEvent;
import org.jspecify.annotations.Nullable;

public class SheepEntity
extends AnimalEntity
implements Shearable {
    private static final int MAX_GRASS_TIMER = 40;
    private static final TrackedData<Byte> COLOR = DataTracker.registerData(SheepEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final DyeColor DEFAULT_COLOR = DyeColor.WHITE;
    private static final boolean DEFAULT_SHEARED = false;
    private int eatGrassTimer;
    private EatGrassGoal eatGrassGoal;

    public SheepEntity(EntityType<? extends SheepEntity> arg, World arg2) {
        super((EntityType<? extends AnimalEntity>)arg, arg2);
    }

    @Override
    protected void initGoals() {
        this.eatGrassGoal = new EatGrassGoal(this);
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.25));
        this.goalSelector.add(2, new AnimalMateGoal(this, 1.0));
        this.goalSelector.add(3, new TemptGoal(this, 1.1, stack -> stack.isIn(ItemTags.SHEEP_FOOD), false));
        this.goalSelector.add(4, new FollowParentGoal(this, 1.1));
        this.goalSelector.add(5, this.eatGrassGoal);
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0f));
        this.goalSelector.add(8, new LookAroundGoal(this));
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isIn(ItemTags.SHEEP_FOOD);
    }

    @Override
    protected void mobTick(ServerWorld world) {
        this.eatGrassTimer = this.eatGrassGoal.getTimer();
        super.mobTick(world);
    }

    @Override
    public void tickMovement() {
        if (this.getEntityWorld().isClient()) {
            this.eatGrassTimer = Math.max(0, this.eatGrassTimer - 1);
        }
        super.tickMovement();
    }

    public static DefaultAttributeContainer.Builder createSheepAttributes() {
        return AnimalEntity.createAnimalAttributes().add(EntityAttributes.MAX_HEALTH, 8.0).add(EntityAttributes.MOVEMENT_SPEED, 0.23f);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(COLOR, (byte)0);
    }

    @Override
    public void handleStatus(byte status) {
        if (status == EntityStatuses.SET_SHEEP_EAT_GRASS_TIMER_OR_PRIME_TNT_MINECART) {
            this.eatGrassTimer = 40;
        } else {
            super.handleStatus(status);
        }
    }

    public float getNeckAngle(float tickProgress) {
        if (this.eatGrassTimer <= 0) {
            return 0.0f;
        }
        if (this.eatGrassTimer >= 4 && this.eatGrassTimer <= 36) {
            return 1.0f;
        }
        if (this.eatGrassTimer < 4) {
            return ((float)this.eatGrassTimer - tickProgress) / 4.0f;
        }
        return -((float)(this.eatGrassTimer - 40) - tickProgress) / 4.0f;
    }

    public float getHeadAngle(float tickProgress) {
        if (this.eatGrassTimer > 4 && this.eatGrassTimer <= 36) {
            float g = ((float)(this.eatGrassTimer - 4) - tickProgress) / 32.0f;
            return 0.62831855f + 0.21991149f * MathHelper.sin(g * 28.7f);
        }
        if (this.eatGrassTimer > 0) {
            return 0.62831855f;
        }
        return this.getLerpedPitch(tickProgress) * ((float)Math.PI / 180);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack lv = player.getStackInHand(hand);
        if (lv.isOf(Items.SHEARS)) {
            World world = this.getEntityWorld();
            if (world instanceof ServerWorld) {
                ServerWorld lv2 = (ServerWorld)world;
                if (this.isShearable()) {
                    this.sheared(lv2, SoundCategory.PLAYERS, lv);
                    this.emitGameEvent(GameEvent.SHEAR, player);
                    lv.damage(1, (LivingEntity)player, hand.getEquipmentSlot());
                    return ActionResult.SUCCESS_SERVER;
                }
            }
            return ActionResult.CONSUME;
        }
        return super.interactMob(player, hand);
    }

    @Override
    public void sheared(ServerWorld world2, SoundCategory shearedSoundCategory, ItemStack shears) {
        world2.playSoundFromEntity(null, this, SoundEvents.ENTITY_SHEEP_SHEAR, shearedSoundCategory, 1.0f, 1.0f);
        this.forEachShearedItem(world2, LootTables.SHEEP_SHEARING, shears, (world, stack) -> {
            for (int i = 0; i < stack.getCount(); ++i) {
                ItemEntity lv = this.dropStack((ServerWorld)world, stack.copyWithCount(1), 1.0f);
                if (lv == null) continue;
                lv.setVelocity(lv.getVelocity().add((this.random.nextFloat() - this.random.nextFloat()) * 0.1f, this.random.nextFloat() * 0.05f, (this.random.nextFloat() - this.random.nextFloat()) * 0.1f));
            }
        });
        this.setSheared(true);
    }

    @Override
    public boolean isShearable() {
        return this.isAlive() && !this.isSheared() && !this.isBaby();
    }

    @Override
    protected void writeCustomData(WriteView view) {
        super.writeCustomData(view);
        view.putBoolean("Sheared", this.isSheared());
        view.put("Color", DyeColor.INDEX_CODEC, this.getColor());
    }

    @Override
    protected void readCustomData(ReadView view) {
        super.readCustomData(view);
        this.setSheared(view.getBoolean("Sheared", false));
        this.setColor(view.read("Color", DyeColor.INDEX_CODEC).orElse(DEFAULT_COLOR));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SHEEP_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_SHEEP_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SHEEP_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_SHEEP_STEP, 0.15f, 1.0f);
    }

    public DyeColor getColor() {
        return DyeColor.byIndex(this.dataTracker.get(COLOR) & 0xF);
    }

    public void setColor(DyeColor color) {
        byte b = this.dataTracker.get(COLOR);
        this.dataTracker.set(COLOR, (byte)(b & 0xF0 | color.getIndex() & 0xF));
    }

    @Override
    public <T> @Nullable T get(ComponentType<? extends T> type) {
        if (type == DataComponentTypes.SHEEP_COLOR) {
            return SheepEntity.castComponentValue(type, this.getColor());
        }
        return super.get(type);
    }

    @Override
    protected void copyComponentsFrom(ComponentsAccess from) {
        this.copyComponentFrom(from, DataComponentTypes.SHEEP_COLOR);
        super.copyComponentsFrom(from);
    }

    @Override
    protected <T> boolean setApplicableComponent(ComponentType<T> type, T value) {
        if (type == DataComponentTypes.SHEEP_COLOR) {
            this.setColor(SheepEntity.castComponentValue(DataComponentTypes.SHEEP_COLOR, value));
            return true;
        }
        return super.setApplicableComponent(type, value);
    }

    public boolean isSheared() {
        return (this.dataTracker.get(COLOR) & 0x10) != 0;
    }

    public void setSheared(boolean sheared) {
        byte b = this.dataTracker.get(COLOR);
        if (sheared) {
            this.dataTracker.set(COLOR, (byte)(b | 0x10));
        } else {
            this.dataTracker.set(COLOR, (byte)(b & 0xFFFFFFEF));
        }
    }

    public static DyeColor selectSpawnColor(ServerWorldAccess world, BlockPos pos) {
        RegistryEntry<Biome> lv = world.getBiome(pos);
        return SheepColors.select(lv, world.getRandom());
    }

    @Override
    public @Nullable SheepEntity createChild(ServerWorld arg, PassiveEntity arg2) {
        SheepEntity lv = EntityType.SHEEP.create(arg, SpawnReason.BREEDING);
        if (lv != null) {
            DyeColor lv2 = this.getColor();
            DyeColor lv3 = ((SheepEntity)arg2).getColor();
            lv.setColor(DyeColor.mixColors(arg, lv2, lv3));
        }
        return lv;
    }

    @Override
    public void onEatingGrass() {
        super.onEatingGrass();
        this.setSheared(false);
        if (this.isBaby()) {
            this.growUp(60);
        }
    }

    @Override
    public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        this.setColor(SheepEntity.selectSpawnColor(world, this.getBlockPos()));
        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    @Override
    public /* synthetic */ @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return this.createChild(world, entity);
    }
}

