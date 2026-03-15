/*
 * External method calls:
 *   Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z
 *   Lnet/minecraft/world/World;emitGameEvent(Lnet/minecraft/entity/Entity;Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/util/math/Vec3d;)V
 *   Lnet/minecraft/item/ItemStack;decrementUnlessCreative(ILnet/minecraft/entity/LivingEntity;)V
 *   Lnet/minecraft/entity/player/PlayerEntity;incrementStat(Lnet/minecraft/stat/Stat;)V
 *   Lnet/minecraft/entity/EntityType;create(Lnet/minecraft/world/World;Lnet/minecraft/entity/SpawnReason;)Lnet/minecraft/entity/Entity;
 *   Lnet/minecraft/entity/vehicle/AbstractBoatEntity;initPosition(DDD)V
 *   Lnet/minecraft/entity/EntityType;copier(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;)Ljava/util/function/Consumer;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/item/BoatItem;raycast(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/RaycastContext$FluidHandling;)Lnet/minecraft/util/hit/BlockHitResult;
 *   Lnet/minecraft/item/BoatItem;createEntity(Lnet/minecraft/world/World;Lnet/minecraft/util/hit/HitResult;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/PlayerEntity;)Lnet/minecraft/entity/vehicle/AbstractBoatEntity;
 */
package net.minecraft.item;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jspecify.annotations.Nullable;

public class BoatItem
extends Item {
    private final EntityType<? extends AbstractBoatEntity> boatEntityType;

    public BoatItem(EntityType<? extends AbstractBoatEntity> boatEntityType, Item.Settings settings) {
        super(settings);
        this.boatEntityType = boatEntityType;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack lv = user.getStackInHand(hand);
        BlockHitResult lv2 = BoatItem.raycast(world, user, RaycastContext.FluidHandling.ANY);
        if (((HitResult)lv2).getType() == HitResult.Type.MISS) {
            return ActionResult.PASS;
        }
        Vec3d lv3 = user.getRotationVec(1.0f);
        double d = 5.0;
        List<Entity> list = world.getOtherEntities(user, user.getBoundingBox().stretch(lv3.multiply(5.0)).expand(1.0), EntityPredicates.CAN_HIT);
        if (!list.isEmpty()) {
            Vec3d lv4 = user.getEyePos();
            for (Entity lv5 : list) {
                Box lv6 = lv5.getBoundingBox().expand(lv5.getTargetingMargin());
                if (!lv6.contains(lv4)) continue;
                return ActionResult.PASS;
            }
        }
        if (((HitResult)lv2).getType() == HitResult.Type.BLOCK) {
            AbstractBoatEntity lv7 = this.createEntity(world, lv2, lv, user);
            if (lv7 == null) {
                return ActionResult.FAIL;
            }
            lv7.setYaw(user.getYaw());
            if (!world.isSpaceEmpty(lv7, lv7.getBoundingBox())) {
                return ActionResult.FAIL;
            }
            if (!world.isClient()) {
                world.spawnEntity(lv7);
                world.emitGameEvent((Entity)user, GameEvent.ENTITY_PLACE, lv2.getPos());
                lv.decrementUnlessCreative(1, user);
            }
            user.incrementStat(Stats.USED.getOrCreateStat(this));
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    private @Nullable AbstractBoatEntity createEntity(World world, HitResult hitResult, ItemStack stack, PlayerEntity player) {
        AbstractBoatEntity lv = this.boatEntityType.create(world, SpawnReason.SPAWN_ITEM_USE);
        if (lv != null) {
            Vec3d lv2 = hitResult.getPos();
            lv.initPosition(lv2.x, lv2.y, lv2.z);
            if (world instanceof ServerWorld) {
                ServerWorld lv3 = (ServerWorld)world;
                EntityType.copier(lv3, stack, player).accept(lv);
            }
        }
        return lv;
    }
}

