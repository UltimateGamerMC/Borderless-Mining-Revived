/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;readString()Ljava/lang/String;
 *   Lnet/minecraft/network/PacketByteBuf;readList(Lnet/minecraft/network/codec/PacketDecoder;)Ljava/util/List;
 *   Lnet/minecraft/network/PacketByteBuf;readCollection(Ljava/util/function/IntFunction;Lnet/minecraft/network/codec/PacketDecoder;)Ljava/util/Collection;
 *   Lnet/minecraft/network/PacketByteBuf;writeString(Ljava/lang/String;)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/PacketByteBuf;writeInt(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/PacketByteBuf;writeFloat(F)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/PacketByteBuf;writeBoolean(Z)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/PacketByteBuf;writeCollection(Ljava/util/Collection;Lnet/minecraft/network/codec/PacketEncoder;)V
 *   Lnet/minecraft/util/NameGenerator;name(Lnet/minecraft/entity/Entity;)Ljava/lang/String;
 *   Lnet/minecraft/village/VillagerData;profession()Lnet/minecraft/registry/entry/RegistryEntry;
 *   Lnet/minecraft/util/NameGenerator;name(Ljava/util/UUID;)Ljava/lang/String;
 *   Lnet/minecraft/util/StringHelper;truncate(Ljava/lang/String;IZ)Ljava/lang/String;
 *   Lnet/minecraft/network/codec/PacketCodec;ofStatic(Lnet/minecraft/network/codec/PacketEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/world/debug/data/BrainDebugData;streamMemories(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;J)Ljava/util/stream/Stream;
 *   Lnet/minecraft/world/debug/data/BrainDebugData;collectMemoryString(Lnet/minecraft/server/world/ServerWorld;JLnet/minecraft/entity/ai/brain/MemoryModuleType;Ljava/util/Optional;)Ljava/lang/String;
 *   Lnet/minecraft/world/debug/data/BrainDebugData;write(Lnet/minecraft/network/PacketByteBuf;)V
 */
package net.minecraft.world.debug.data;

import java.lang.runtime.SwitchBootstraps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.entity.Entity;
import net.minecraft.entity.InventoryOwner;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.BlockPosLookTarget;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.EntityLookTarget;
import net.minecraft.entity.ai.brain.Memory;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.WalkTarget;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.NameGenerator;
import net.minecraft.util.StringHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import org.jspecify.annotations.Nullable;

public record BrainDebugData(String name, String profession, int xp, float health, float maxHealth, String inventory, boolean wantsGolem, int angerLevel, List<String> activities, List<String> behaviors, List<String> memories, List<String> gossips, Set<BlockPos> pois, Set<BlockPos> potentialPois) {
    public static final PacketCodec<PacketByteBuf, BrainDebugData> PACKET_CODEC = PacketCodec.ofStatic((arg, arg2) -> arg2.write((PacketByteBuf)arg), BrainDebugData::new);

    public BrainDebugData(PacketByteBuf buf) {
        this(buf.readString(), buf.readString(), buf.readInt(), buf.readFloat(), buf.readFloat(), buf.readString(), buf.readBoolean(), buf.readInt(), buf.readList(PacketByteBuf::readString), buf.readList(PacketByteBuf::readString), buf.readList(PacketByteBuf::readString), buf.readList(PacketByteBuf::readString), buf.readCollection(HashSet::new, BlockPos.PACKET_CODEC), buf.readCollection(HashSet::new, BlockPos.PACKET_CODEC));
    }

    public void write(PacketByteBuf buf) {
        buf.writeString(this.name);
        buf.writeString(this.profession);
        buf.writeInt(this.xp);
        buf.writeFloat(this.health);
        buf.writeFloat(this.maxHealth);
        buf.writeString(this.inventory);
        buf.writeBoolean(this.wantsGolem);
        buf.writeInt(this.angerLevel);
        buf.writeCollection(this.activities, PacketByteBuf::writeString);
        buf.writeCollection(this.behaviors, PacketByteBuf::writeString);
        buf.writeCollection(this.memories, PacketByteBuf::writeString);
        buf.writeCollection(this.gossips, PacketByteBuf::writeString);
        buf.writeCollection(this.pois, BlockPos.PACKET_CODEC);
        buf.writeCollection(this.potentialPois, BlockPos.PACKET_CODEC);
    }

    public static BrainDebugData fromEntity(ServerWorld world, LivingEntity entity) {
        List<String> list;
        int n;
        VillagerEntity lv5;
        boolean bl;
        InventoryOwner lv3;
        SimpleInventory lv4;
        int i;
        String string2;
        String string = NameGenerator.name(entity);
        if (entity instanceof VillagerEntity) {
            VillagerEntity lv = (VillagerEntity)entity;
            string2 = lv.getVillagerData().profession().getIdAsString();
            i = lv.getExperience();
        } else {
            string2 = "";
            i = 0;
        }
        float f = entity.getHealth();
        float g = entity.getMaxHealth();
        Brain<?> lv2 = entity.getBrain();
        long l = entity.getEntityWorld().getTime();
        String string3 = entity instanceof InventoryOwner ? ((lv4 = (lv3 = (InventoryOwner)((Object)entity)).getInventory()).isEmpty() ? "" : ((Object)lv4).toString()) : "";
        boolean bl2 = bl = entity instanceof VillagerEntity && (lv5 = (VillagerEntity)entity).canSummonGolem(l);
        if (entity instanceof WardenEntity) {
            WardenEntity lv6 = (WardenEntity)entity;
            n = lv6.getAnger();
        } else {
            n = -1;
        }
        int j = n;
        List<String> list2 = lv2.getPossibleActivities().stream().map(Activity::getId).toList();
        List<String> list22 = lv2.getRunningTasks().stream().map(Task::getName).toList();
        List<String> list3 = BrainDebugData.streamMemories(world, entity, l).map(memory -> StringHelper.truncate(memory, 255, true)).toList();
        Set<BlockPos> set = BrainDebugData.getMemorizedPositions(lv2, MemoryModuleType.JOB_SITE, MemoryModuleType.HOME, MemoryModuleType.MEETING_POINT);
        Set<BlockPos> set2 = BrainDebugData.getMemorizedPositions(lv2, MemoryModuleType.POTENTIAL_JOB_SITE);
        if (entity instanceof VillagerEntity) {
            VillagerEntity lv7 = (VillagerEntity)entity;
            list = BrainDebugData.getGossips(lv7);
        } else {
            list = List.of();
        }
        List<String> list4 = list;
        return new BrainDebugData(string, string2, i, f, g, string3, bl, j, list2, list22, list3, list4, set, set2);
    }

    @SafeVarargs
    private static Set<BlockPos> getMemorizedPositions(Brain<?> brain, MemoryModuleType<GlobalPos> ... types) {
        return Stream.of(types).filter(brain::hasMemoryModule).map(brain::getOptionalRegisteredMemory).flatMap(Optional::stream).map(GlobalPos::pos).collect(Collectors.toSet());
    }

    private static List<String> getGossips(VillagerEntity villager) {
        ArrayList<String> list = new ArrayList<String>();
        villager.getGossip().getEntityReputationAssociatedGossips().forEach((uuid, gossips) -> {
            String string = NameGenerator.name(uuid);
            gossips.forEach((type, gossip) -> list.add(string + ": " + String.valueOf(type) + ": " + gossip));
        });
        return list;
    }

    private static Stream<String> streamMemories(ServerWorld world, LivingEntity entity, long time) {
        return entity.getBrain().getMemories().entrySet().stream().map(entry -> {
            MemoryModuleType lv = (MemoryModuleType)entry.getKey();
            Optional optional = (Optional)entry.getValue();
            return BrainDebugData.collectMemoryString(world, time, lv, optional);
        }).sorted();
    }

    private static String collectMemoryString(ServerWorld world, long time, MemoryModuleType<?> type, Optional<? extends Memory<?>> memory) {
        Object string;
        if (memory.isPresent()) {
            Memory<?> lv = memory.get();
            Object object = lv.getValue();
            if (type == MemoryModuleType.HEARD_BELL_TIME) {
                long m = time - (Long)object;
                string = m + " ticks ago";
            } else {
                string = lv.isTimed() ? BrainDebugData.toString(world, object) + " (ttl: " + lv.getExpiry() + ")" : BrainDebugData.toString(world, object);
            }
        } else {
            string = "-";
        }
        return Registries.MEMORY_MODULE_TYPE.getId(type).getPath() + ": " + (String)string;
    }

    private static String toString(ServerWorld world, @Nullable Object value) {
        Object object = value;
        int n = 0;
        return switch (SwitchBootstraps.typeSwitch("typeSwitch", new Object[]{UUID.class, Entity.class, WalkTarget.class, EntityLookTarget.class, GlobalPos.class, BlockPosLookTarget.class, DamageSource.class, Collection.class}, (Object)object, n)) {
            case -1 -> "-";
            case 0 -> {
                UUID uUID = (UUID)object;
                yield BrainDebugData.toString(world, world.getEntity(uUID));
            }
            case 1 -> {
                Entity lv = (Entity)object;
                yield NameGenerator.name(lv);
            }
            case 2 -> {
                WalkTarget lv2 = (WalkTarget)object;
                yield BrainDebugData.toString(world, lv2.getLookTarget());
            }
            case 3 -> {
                EntityLookTarget lv3 = (EntityLookTarget)object;
                yield BrainDebugData.toString(world, lv3.getEntity());
            }
            case 4 -> {
                GlobalPos lv4 = (GlobalPos)object;
                yield BrainDebugData.toString(world, lv4.pos());
            }
            case 5 -> {
                BlockPosLookTarget lv5 = (BlockPosLookTarget)object;
                yield BrainDebugData.toString(world, lv5.getBlockPos());
            }
            case 6 -> {
                DamageSource lv6 = (DamageSource)object;
                Entity lv7 = lv6.getAttacker();
                if (lv7 == null) {
                    yield value.toString();
                }
                yield BrainDebugData.toString(world, lv7);
            }
            case 7 -> {
                Collection collection = (Collection)object;
                yield "[" + collection.stream().map(v -> BrainDebugData.toString(world, v)).collect(Collectors.joining(", ")) + "]";
            }
            default -> value.toString();
        };
    }

    public boolean poiContains(BlockPos pos) {
        return this.pois.contains(pos);
    }

    public boolean potentialPoiContains(BlockPos pos) {
        return this.potentialPois.contains(pos);
    }
}

