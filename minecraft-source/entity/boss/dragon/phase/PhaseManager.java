/*
 * External method calls:
 *   Lnet/minecraft/entity/boss/dragon/phase/PhaseType;create(Lnet/minecraft/entity/boss/dragon/EnderDragonEntity;)Lnet/minecraft/entity/boss/dragon/phase/Phase;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/boss/dragon/phase/PhaseManager;create(Lnet/minecraft/entity/boss/dragon/phase/PhaseType;)Lnet/minecraft/entity/boss/dragon/phase/Phase;
 */
package net.minecraft.entity.boss.dragon.phase;

import com.mojang.logging.LogUtils;
import java.util.Objects;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.phase.Phase;
import net.minecraft.entity.boss.dragon.phase.PhaseType;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class PhaseManager {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final EnderDragonEntity dragon;
    private final @Nullable Phase[] phases = new Phase[PhaseType.count()];
    private @Nullable Phase current;

    public PhaseManager(EnderDragonEntity dragon) {
        this.dragon = dragon;
        this.setPhase(PhaseType.HOVER);
    }

    public void setPhase(PhaseType<?> type) {
        if (this.current != null && type == this.current.getType()) {
            return;
        }
        if (this.current != null) {
            this.current.endPhase();
        }
        this.current = this.create(type);
        if (!this.dragon.getEntityWorld().isClient()) {
            this.dragon.getDataTracker().set(EnderDragonEntity.PHASE_TYPE, type.getTypeId());
        }
        LOGGER.debug("Dragon is now in phase {} on the {}", (Object)type, (Object)(this.dragon.getEntityWorld().isClient() ? "client" : "server"));
        this.current.beginPhase();
    }

    public Phase getCurrent() {
        return Objects.requireNonNull(this.current);
    }

    public <T extends Phase> T create(PhaseType<T> type) {
        int i = type.getTypeId();
        Phase lv = this.phases[i];
        if (lv == null) {
            this.phases[i] = lv = type.create(this.dragon);
        }
        return (T)lv;
    }
}

