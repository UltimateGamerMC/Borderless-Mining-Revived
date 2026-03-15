package net.minecraft.predicate.entity;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public record EntityFlagsPredicate(Optional<Boolean> isOnGround, Optional<Boolean> isOnFire, Optional<Boolean> isSneaking, Optional<Boolean> isSprinting, Optional<Boolean> isSwimming, Optional<Boolean> isFlying, Optional<Boolean> isBaby, Optional<Boolean> isInWater, Optional<Boolean> isFallFlying) {
    public static final Codec<EntityFlagsPredicate> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.BOOL.optionalFieldOf("is_on_ground").forGetter(EntityFlagsPredicate::isOnGround), Codec.BOOL.optionalFieldOf("is_on_fire").forGetter(EntityFlagsPredicate::isOnFire), Codec.BOOL.optionalFieldOf("is_sneaking").forGetter(EntityFlagsPredicate::isSneaking), Codec.BOOL.optionalFieldOf("is_sprinting").forGetter(EntityFlagsPredicate::isSprinting), Codec.BOOL.optionalFieldOf("is_swimming").forGetter(EntityFlagsPredicate::isSwimming), Codec.BOOL.optionalFieldOf("is_flying").forGetter(EntityFlagsPredicate::isFlying), Codec.BOOL.optionalFieldOf("is_baby").forGetter(EntityFlagsPredicate::isBaby), Codec.BOOL.optionalFieldOf("is_in_water").forGetter(EntityFlagsPredicate::isInWater), Codec.BOOL.optionalFieldOf("is_fall_flying").forGetter(EntityFlagsPredicate::isFallFlying)).apply((Applicative<EntityFlagsPredicate, ?>)instance, EntityFlagsPredicate::new));

    /*
     * Unable to fully structure code
     */
    public boolean test(Entity entity) {
        block11: {
            if (this.isOnGround.isPresent() && entity.isOnGround() != this.isOnGround.get().booleanValue()) {
                return false;
            }
            if (this.isOnFire.isPresent() && entity.isOnFire() != this.isOnFire.get().booleanValue()) {
                return false;
            }
            if (this.isSneaking.isPresent() && entity.isInSneakingPose() != this.isSneaking.get().booleanValue()) {
                return false;
            }
            if (this.isSprinting.isPresent() && entity.isSprinting() != this.isSprinting.get().booleanValue()) {
                return false;
            }
            if (this.isSwimming.isPresent() && entity.isSwimming() != this.isSwimming.get().booleanValue()) {
                return false;
            }
            if (!this.isFlying.isPresent()) break block11;
            if (!(entity instanceof LivingEntity)) ** GOTO lbl-1000
            lv = (LivingEntity)entity;
            if (lv.isGliding()) ** GOTO lbl-1000
            if (lv instanceof PlayerEntity) {
                lv2 = (PlayerEntity)lv;
                ** if (!lv2.getAbilities().flying) goto lbl-1000
            }
            ** GOTO lbl-1000
lbl-1000:
            // 2 sources

            {
                v0 = true;
                ** GOTO lbl22
            }
lbl-1000:
            // 3 sources

            {
                v0 = bl = false;
            }
lbl22:
            // 2 sources

            if (bl != this.isFlying.get()) {
                return false;
            }
        }
        if (this.isInWater.isPresent() && entity.isTouchingWater() != this.isInWater.get().booleanValue()) {
            return false;
        }
        if (this.isFallFlying.isPresent() && entity instanceof LivingEntity && (lv3 = (LivingEntity)entity).isGliding() != this.isFallFlying.get().booleanValue()) {
            return false;
        }
        return this.isBaby.isPresent() == false || entity instanceof LivingEntity == false || (lv3 = (LivingEntity)entity).isBaby() == this.isBaby.get().booleanValue();
    }

    public static class Builder {
        private Optional<Boolean> isOnGround = Optional.empty();
        private Optional<Boolean> isOnFire = Optional.empty();
        private Optional<Boolean> isSneaking = Optional.empty();
        private Optional<Boolean> isSprinting = Optional.empty();
        private Optional<Boolean> isSwimming = Optional.empty();
        private Optional<Boolean> isFlying = Optional.empty();
        private Optional<Boolean> isBaby = Optional.empty();
        private Optional<Boolean> isInWater = Optional.empty();
        private Optional<Boolean> isFallFlying = Optional.empty();

        public static Builder create() {
            return new Builder();
        }

        public Builder onGround(Boolean onGround) {
            this.isOnGround = Optional.of(onGround);
            return this;
        }

        public Builder onFire(Boolean onFire) {
            this.isOnFire = Optional.of(onFire);
            return this;
        }

        public Builder sneaking(Boolean sneaking) {
            this.isSneaking = Optional.of(sneaking);
            return this;
        }

        public Builder sprinting(Boolean sprinting) {
            this.isSprinting = Optional.of(sprinting);
            return this;
        }

        public Builder swimming(Boolean swimming) {
            this.isSwimming = Optional.of(swimming);
            return this;
        }

        public Builder flying(Boolean flying) {
            this.isFlying = Optional.of(flying);
            return this;
        }

        public Builder isBaby(Boolean isBaby) {
            this.isBaby = Optional.of(isBaby);
            return this;
        }

        public Builder isInWater(Boolean isInWater) {
            this.isInWater = Optional.of(isInWater);
            return this;
        }

        public Builder isFallFlying(Boolean isFallFlying) {
            this.isFallFlying = Optional.of(isFallFlying);
            return this;
        }

        public EntityFlagsPredicate build() {
            return new EntityFlagsPredicate(this.isOnGround, this.isOnFire, this.isSneaking, this.isSprinting, this.isSwimming, this.isFlying, this.isBaby, this.isInWater, this.isFallFlying);
        }
    }
}

