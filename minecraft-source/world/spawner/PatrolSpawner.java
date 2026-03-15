/*
 * External method calls:
 *   Lnet/minecraft/entity/EntityType;create(Lnet/minecraft/world/World;Lnet/minecraft/entity/SpawnReason;)Lnet/minecraft/entity/Entity;
 *   Lnet/minecraft/entity/mob/PatrolEntity;initialize(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;)Lnet/minecraft/entity/EntityData;
 *   Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/world/spawner/PatrolSpawner;spawnPillager(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/random/Random;Z)Z
 */
package net.minecraft.world.spawner;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.PatrolEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.rule.GameRules;
import net.minecraft.world.spawner.SpecialSpawner;

public class PatrolSpawner
implements SpecialSpawner {
    private int cooldown;

    @Override
    public void spawn(ServerWorld world, boolean spawnMonsters) {
        if (!spawnMonsters) {
            return;
        }
        if (!world.getGameRules().getValue(GameRules.SPAWN_PATROLS).booleanValue()) {
            return;
        }
        Random lv = world.random;
        --this.cooldown;
        if (this.cooldown > 0) {
            return;
        }
        this.cooldown += 12000 + lv.nextInt(1200);
        if (!world.isDay()) {
            return;
        }
        if (lv.nextInt(5) != 0) {
            return;
        }
        int i = world.getPlayers().size();
        if (i < 1) {
            return;
        }
        PlayerEntity lv2 = world.getPlayers().get(lv.nextInt(i));
        if (lv2.isSpectator()) {
            return;
        }
        if (world.isNearOccupiedPointOfInterest(lv2.getBlockPos(), 2)) {
            return;
        }
        int j = (24 + lv.nextInt(24)) * (lv.nextBoolean() ? -1 : 1);
        int k = (24 + lv.nextInt(24)) * (lv.nextBoolean() ? -1 : 1);
        BlockPos.Mutable lv3 = lv2.getBlockPos().mutableCopy().move(j, 0, k);
        int l = 10;
        if (!world.isRegionLoaded(lv3.getX() - 10, lv3.getZ() - 10, lv3.getX() + 10, lv3.getZ() + 10)) {
            return;
        }
        if (!world.getEnvironmentAttributes().getAttributeValue(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN_GAMEPLAY, lv3).booleanValue()) {
            return;
        }
        int m = (int)Math.ceil(world.getLocalDifficulty(lv3).getLocalDifficulty()) + 1;
        for (int n = 0; n < m; ++n) {
            lv3.setY(world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, lv3).getY());
            if (n == 0) {
                if (!this.spawnPillager(world, lv3, lv, true)) {
                    break;
                }
            } else {
                this.spawnPillager(world, lv3, lv, false);
            }
            lv3.setX(lv3.getX() + lv.nextInt(5) - lv.nextInt(5));
            lv3.setZ(lv3.getZ() + lv.nextInt(5) - lv.nextInt(5));
        }
    }

    private boolean spawnPillager(ServerWorld world, BlockPos pos, Random random, boolean captain) {
        BlockState lv = world.getBlockState(pos);
        if (!SpawnHelper.isClearForSpawn(world, pos, lv, lv.getFluidState(), EntityType.PILLAGER)) {
            return false;
        }
        if (!PatrolEntity.canSpawn(EntityType.PILLAGER, world, SpawnReason.PATROL, pos, random)) {
            return false;
        }
        PatrolEntity lv2 = EntityType.PILLAGER.create(world, SpawnReason.PATROL);
        if (lv2 != null) {
            if (captain) {
                lv2.setPatrolLeader(true);
                lv2.setRandomPatrolTarget();
            }
            lv2.setPosition(pos.getX(), pos.getY(), pos.getZ());
            lv2.initialize(world, world.getLocalDifficulty(pos), SpawnReason.PATROL, null);
            world.spawnEntityAndPassengers(lv2);
            return true;
        }
        return false;
    }
}

