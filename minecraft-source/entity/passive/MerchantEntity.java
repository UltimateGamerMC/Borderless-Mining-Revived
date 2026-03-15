/*
 * External method calls:
 *   Lnet/minecraft/entity/passive/PassiveEntity;initialize(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;)Lnet/minecraft/entity/EntityData;
 *   Lnet/minecraft/entity/passive/PassiveEntity;initDataTracker(Lnet/minecraft/entity/data/DataTracker$Builder;)V
 *   Lnet/minecraft/advancement/criterion/VillagerTradeCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/entity/passive/MerchantEntity;Lnet/minecraft/item/ItemStack;)V
 *   Lnet/minecraft/entity/passive/PassiveEntity;writeCustomData(Lnet/minecraft/storage/WriteView;)V
 *   Lnet/minecraft/entity/passive/PassiveEntity;readCustomData(Lnet/minecraft/storage/ReadView;)V
 *   Lnet/minecraft/storage/ReadView;read(Ljava/lang/String;Lcom/mojang/serialization/Codec;)Ljava/util/Optional;
 *   Lnet/minecraft/entity/passive/PassiveEntity;teleportTo(Lnet/minecraft/world/TeleportTarget;)Lnet/minecraft/entity/Entity;
 *   Lnet/minecraft/entity/passive/PassiveEntity;onDeath(Lnet/minecraft/entity/damage/DamageSource;)V
 *   Lnet/minecraft/world/World;addParticleClient(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V
 *   Lnet/minecraft/village/TradeOffers$Factory;create(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/random/Random;)Lnet/minecraft/village/TradeOffer;
 *   Lnet/minecraft/entity/data/DataTracker;registerData(Ljava/lang/Class;Lnet/minecraft/entity/data/TrackedDataHandler;)Lnet/minecraft/entity/data/TrackedData;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/passive/MerchantEntity;fillRecipes(Lnet/minecraft/server/world/ServerWorld;)V
 *   Lnet/minecraft/entity/passive/MerchantEntity;afterUsing(Lnet/minecraft/village/TradeOffer;)V
 *   Lnet/minecraft/entity/passive/MerchantEntity;playSound(Lnet/minecraft/sound/SoundEvent;)V
 *   Lnet/minecraft/entity/passive/MerchantEntity;writeInventory(Lnet/minecraft/storage/WriteView;)V
 *   Lnet/minecraft/entity/passive/MerchantEntity;readInventory(Lnet/minecraft/storage/ReadView;)V
 */
package net.minecraft.entity.passive;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.InventoryOwner;
import net.minecraft.entity.Npc;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.Merchant;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.TradeOffers;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

public abstract class MerchantEntity
extends PassiveEntity
implements InventoryOwner,
Npc,
Merchant {
    private static final TrackedData<Integer> HEAD_ROLLING_TIME_LEFT = DataTracker.registerData(MerchantEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public static final int field_30599 = 300;
    private static final int INVENTORY_SIZE = 8;
    private @Nullable PlayerEntity customer;
    protected @Nullable TradeOfferList offers;
    private final SimpleInventory inventory = new SimpleInventory(8);

    public MerchantEntity(EntityType<? extends MerchantEntity> arg, World arg2) {
        super((EntityType<? extends PassiveEntity>)arg, arg2);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, 16.0f);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, -1.0f);
    }

    @Override
    public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        if (entityData == null) {
            entityData = new PassiveEntity.PassiveData(false);
        }
        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    public int getHeadRollingTimeLeft() {
        return this.dataTracker.get(HEAD_ROLLING_TIME_LEFT);
    }

    public void setHeadRollingTimeLeft(int ticks) {
        this.dataTracker.set(HEAD_ROLLING_TIME_LEFT, ticks);
    }

    @Override
    public int getExperience() {
        return 0;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(HEAD_ROLLING_TIME_LEFT, 0);
    }

    @Override
    public void setCustomer(@Nullable PlayerEntity customer) {
        this.customer = customer;
    }

    @Override
    public @Nullable PlayerEntity getCustomer() {
        return this.customer;
    }

    public boolean hasCustomer() {
        return this.customer != null;
    }

    @Override
    public TradeOfferList getOffers() {
        World world = this.getEntityWorld();
        if (!(world instanceof ServerWorld)) {
            throw new IllegalStateException("Cannot load Villager offers on the client");
        }
        ServerWorld lv = (ServerWorld)world;
        if (this.offers == null) {
            this.offers = new TradeOfferList();
            this.fillRecipes(lv);
        }
        return this.offers;
    }

    @Override
    public void setOffersFromServer(@Nullable TradeOfferList offers) {
    }

    @Override
    public void setExperienceFromServer(int experience) {
    }

    @Override
    public void trade(TradeOffer offer) {
        offer.use();
        this.ambientSoundChance = -this.getMinAmbientSoundDelay();
        this.afterUsing(offer);
        if (this.customer instanceof ServerPlayerEntity) {
            Criteria.VILLAGER_TRADE.trigger((ServerPlayerEntity)this.customer, this, offer.getSellItem());
        }
    }

    protected abstract void afterUsing(TradeOffer var1);

    @Override
    public boolean isLeveledMerchant() {
        return true;
    }

    @Override
    public void onSellingItem(ItemStack stack) {
        if (!this.getEntityWorld().isClient() && this.ambientSoundChance > -this.getMinAmbientSoundDelay() + 20) {
            this.ambientSoundChance = -this.getMinAmbientSoundDelay();
            this.playSound(this.getTradingSound(!stack.isEmpty()));
        }
    }

    @Override
    public SoundEvent getYesSound() {
        return SoundEvents.ENTITY_VILLAGER_YES;
    }

    protected SoundEvent getTradingSound(boolean sold) {
        return sold ? SoundEvents.ENTITY_VILLAGER_YES : SoundEvents.ENTITY_VILLAGER_NO;
    }

    public void playCelebrateSound() {
        this.playSound(SoundEvents.ENTITY_VILLAGER_CELEBRATE);
    }

    @Override
    protected void writeCustomData(WriteView view) {
        TradeOfferList lv;
        super.writeCustomData(view);
        if (!this.getEntityWorld().isClient() && !(lv = this.getOffers()).isEmpty()) {
            view.put("Offers", TradeOfferList.CODEC, lv);
        }
        this.writeInventory(view);
    }

    @Override
    protected void readCustomData(ReadView view) {
        super.readCustomData(view);
        this.offers = view.read("Offers", TradeOfferList.CODEC).orElse(null);
        this.readInventory(view);
    }

    @Override
    public @Nullable Entity teleportTo(TeleportTarget teleportTarget) {
        this.resetCustomer();
        return super.teleportTo(teleportTarget);
    }

    protected void resetCustomer() {
        this.setCustomer(null);
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);
        this.resetCustomer();
    }

    protected void produceParticles(ParticleEffect parameters) {
        for (int i = 0; i < 5; ++i) {
            double d = this.random.nextGaussian() * 0.02;
            double e = this.random.nextGaussian() * 0.02;
            double f = this.random.nextGaussian() * 0.02;
            this.getEntityWorld().addParticleClient(parameters, this.getParticleX(1.0), this.getRandomBodyY() + 1.0, this.getParticleZ(1.0), d, e, f);
        }
    }

    @Override
    public boolean canBeLeashed() {
        return false;
    }

    @Override
    public SimpleInventory getInventory() {
        return this.inventory;
    }

    @Override
    public @Nullable StackReference getStackReference(int slot) {
        int j = slot - 300;
        if (j >= 0 && j < this.inventory.size()) {
            return this.inventory.getStackReference(j);
        }
        return super.getStackReference(slot);
    }

    protected abstract void fillRecipes(ServerWorld var1);

    protected void fillRecipesFromPool(ServerWorld world, TradeOfferList recipeList, TradeOffers.Factory[] pool, int count) {
        ArrayList<TradeOffers.Factory> arrayList = Lists.newArrayList(pool);
        int j = 0;
        while (j < count && !arrayList.isEmpty()) {
            TradeOffer lv = arrayList.remove(this.random.nextInt(arrayList.size())).create(world, this, this.random);
            if (lv == null) continue;
            recipeList.add(lv);
            ++j;
        }
    }

    @Override
    public Vec3d getLeashPos(float tickProgress) {
        float g = MathHelper.lerp(tickProgress, this.lastBodyYaw, this.bodyYaw) * ((float)Math.PI / 180);
        Vec3d lv = new Vec3d(0.0, this.getBoundingBox().getLengthY() - 1.0, 0.2);
        return this.getLerpedPos(tickProgress).add(lv.rotateY(-g));
    }

    @Override
    public boolean isClient() {
        return this.getEntityWorld().isClient();
    }

    @Override
    public boolean canInteract(PlayerEntity player) {
        return this.getCustomer() == player && this.isAlive() && player.canInteractWithEntity(this, 4.0);
    }
}

