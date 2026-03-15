package net.minecraft.block;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TeleportTarget;
import org.jspecify.annotations.Nullable;

public interface Portal {
    default public int getPortalDelay(ServerWorld world, Entity entity) {
        return 0;
    }

    public @Nullable TeleportTarget createTeleportTarget(ServerWorld var1, Entity var2, BlockPos var3);

    default public Effect getPortalEffect() {
        return Effect.NONE;
    }

    public static enum Effect {
        CONFUSION,
        NONE;

    }
}

