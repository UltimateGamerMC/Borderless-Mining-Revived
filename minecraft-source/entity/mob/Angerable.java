/*
 * External method calls:
 *   Lnet/minecraft/storage/WriteView;putLong(Ljava/lang/String;J)V
 *   Lnet/minecraft/entity/LazyEntityReference;createCodec()Lcom/mojang/serialization/Codec;
 *   Lnet/minecraft/storage/WriteView;putNullable(Ljava/lang/String;Lcom/mojang/serialization/Codec;Ljava/lang/Object;)V
 *   Lnet/minecraft/entity/LazyEntityReference;fromData(Lnet/minecraft/storage/ReadView;Ljava/lang/String;)Lnet/minecraft/entity/LazyEntityReference;
 *   Lnet/minecraft/entity/LazyEntityReference;uuidEquals(Lnet/minecraft/world/entity/UniquelyIdentifiable;)Z
 *   Lnet/minecraft/entity/LazyEntityReference;of(Lnet/minecraft/world/entity/UniquelyIdentifiable;)Lnet/minecraft/entity/LazyEntityReference;
 */
package net.minecraft.entity.mob;

import java.util.Optional;
import net.minecraft.entity.LazyEntityReference;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.world.World;
import net.minecraft.world.rule.GameRules;
import org.jspecify.annotations.Nullable;

public interface Angerable {
    public static final String ANGER_END_TIME_KEY = "anger_end_time";
    public static final String ANGRY_AT_KEY = "angry_at";
    public static final long NO_ANGER_END_TIME = -1L;

    public long getAngerEndTime();

    default public void setAngerDuration(long durationInTicks) {
        this.setAngerEndTime(this.getEntityWorld().getTime() + durationInTicks);
    }

    public void setAngerEndTime(long var1);

    public @Nullable LazyEntityReference<LivingEntity> getAngryAt();

    public void setAngryAt(@Nullable LazyEntityReference<LivingEntity> var1);

    public void chooseRandomAngerTime();

    public World getEntityWorld();

    default public void writeAngerToData(WriteView view) {
        view.putLong(ANGER_END_TIME_KEY, this.getAngerEndTime());
        view.putNullable(ANGRY_AT_KEY, LazyEntityReference.createCodec(), this.getAngryAt());
    }

    default public void readAngerFromData(World world, ReadView view) {
        Optional<Long> optional = view.getOptionalLong(ANGER_END_TIME_KEY);
        if (optional.isPresent()) {
            this.setAngerEndTime(optional.get());
        } else {
            Optional<Integer> optional2 = view.getOptionalInt("AngerTime");
            if (optional2.isPresent()) {
                this.setAngerDuration(optional2.get().intValue());
            } else {
                this.setAngerEndTime(-1L);
            }
        }
        if (!(world instanceof ServerWorld)) {
            return;
        }
        this.setAngryAt(LazyEntityReference.fromData(view, ANGRY_AT_KEY));
        this.setTarget(LazyEntityReference.getLivingEntity(this.getAngryAt(), world));
    }

    default public void tickAngerLogic(ServerWorld world, boolean angerPersistent) {
        LivingEntity lv = this.getTarget();
        LazyEntityReference<LivingEntity> lv2 = this.getAngryAt();
        if (lv != null && lv.isDead() && lv2 != null && lv2.uuidEquals(lv) && lv instanceof MobEntity) {
            this.stopAnger();
            return;
        }
        if (lv != null) {
            if (lv2 == null || !lv2.uuidEquals(lv)) {
                this.setAngryAt(LazyEntityReference.of(lv));
            }
            this.chooseRandomAngerTime();
        }
        if (!(lv2 == null || this.hasAngerTime() || lv != null && Angerable.canAngerAt(lv) && angerPersistent)) {
            this.stopAnger();
        }
    }

    private static boolean canAngerAt(LivingEntity target) {
        PlayerEntity lv;
        return target instanceof PlayerEntity && !(lv = (PlayerEntity)target).isCreative() && !lv.isSpectator();
    }

    default public boolean shouldAngerAt(LivingEntity target, ServerWorld world) {
        if (!this.canTarget(target)) {
            return false;
        }
        if (Angerable.canAngerAt(target) && this.isUniversallyAngry(world)) {
            return true;
        }
        LazyEntityReference<LivingEntity> lv = this.getAngryAt();
        return lv != null && lv.uuidEquals(target);
    }

    default public boolean isUniversallyAngry(ServerWorld world) {
        return world.getGameRules().getValue(GameRules.UNIVERSAL_ANGER) != false && this.hasAngerTime() && this.getAngryAt() == null;
    }

    default public boolean hasAngerTime() {
        long l = this.getAngerEndTime();
        if (l > 0L) {
            long m = l - this.getEntityWorld().getTime();
            return m > 0L;
        }
        return false;
    }

    default public void forgive(ServerWorld world, PlayerEntity player) {
        if (!world.getGameRules().getValue(GameRules.FORGIVE_DEAD_PLAYERS).booleanValue()) {
            return;
        }
        LazyEntityReference<LivingEntity> lv = this.getAngryAt();
        if (lv == null || !lv.uuidEquals(player)) {
            return;
        }
        this.stopAnger();
    }

    default public void universallyAnger() {
        this.stopAnger();
        this.chooseRandomAngerTime();
    }

    default public void stopAnger() {
        this.setAttacker(null);
        this.setAngryAt(null);
        this.setTarget(null);
        this.setAngerEndTime(-1L);
    }

    public @Nullable LivingEntity getAttacker();

    public void setAttacker(@Nullable LivingEntity var1);

    public void setTarget(@Nullable LivingEntity var1);

    public boolean canTarget(LivingEntity var1);

    public @Nullable LivingEntity getTarget();
}

