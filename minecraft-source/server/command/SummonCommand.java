/*
 * External method calls:
 *   Lnet/minecraft/server/command/CommandManager;literal(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
 *   Lnet/minecraft/server/command/CommandManager;requirePermissionLevel(Lnet/minecraft/command/permission/PermissionCheck;)Lnet/minecraft/command/permission/PermissionSourcePredicate;
 *   Lnet/minecraft/command/argument/RegistryEntryReferenceArgumentType;registryEntry(Lnet/minecraft/command/CommandRegistryAccess;Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/command/argument/RegistryEntryReferenceArgumentType;
 *   Lnet/minecraft/server/command/CommandManager;argument(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
 *   Lnet/minecraft/command/suggestion/SuggestionProviders;cast(Lcom/mojang/brigadier/suggestion/SuggestionProvider;)Lcom/mojang/brigadier/suggestion/SuggestionProvider;
 *   Lnet/minecraft/command/argument/Vec3ArgumentType;vec3()Lnet/minecraft/command/argument/Vec3ArgumentType;
 *   Lnet/minecraft/command/argument/NbtCompoundArgumentType;nbtCompound()Lnet/minecraft/command/argument/NbtCompoundArgumentType;
 *   Lnet/minecraft/registry/entry/RegistryEntry$Reference;registryKey()Lnet/minecraft/registry/RegistryKey;
 *   Lnet/minecraft/nbt/NbtCompound;putString(Ljava/lang/String;Ljava/lang/String;)V
 *   Lnet/minecraft/entity/EntityType;loadEntityWithPassengers(Lnet/minecraft/nbt/NbtCompound;Lnet/minecraft/world/World;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/LoadedEntityProcessor;)Lnet/minecraft/entity/Entity;
 *   Lnet/minecraft/entity/mob/MobEntity;initialize(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;)Lnet/minecraft/entity/EntityData;
 *   Lnet/minecraft/server/world/ServerWorld;spawnNewEntityAndPassengers(Lnet/minecraft/entity/Entity;)Z
 *   Lnet/minecraft/server/command/ServerCommandSource;sendFeedback(Ljava/util/function/Supplier;Z)V
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/entity/Entity;refreshPositionAndAngles(DDDFF)V
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/command/SummonCommand;summon(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/registry/entry/RegistryEntry$Reference;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/nbt/NbtCompound;Z)Lnet/minecraft/entity/Entity;
 *   Lnet/minecraft/server/command/SummonCommand;execute(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/registry/entry/RegistryEntry$Reference;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/nbt/NbtCompound;Z)I
 */
package net.minecraft.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.NbtCompoundArgumentType;
import net.minecraft.command.argument.RegistryEntryReferenceArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

public class SummonCommand {
    private static final SimpleCommandExceptionType FAILED_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.summon.failed"));
    private static final SimpleCommandExceptionType FAILED_PEACEFUL_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.summon.failed.peaceful"));
    private static final SimpleCommandExceptionType FAILED_UUID_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.summon.failed.uuid"));
    private static final SimpleCommandExceptionType INVALID_POSITION_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.summon.invalidPosition"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal("summon").requires(CommandManager.requirePermissionLevel(CommandManager.GAMEMASTERS_CHECK))).then(((RequiredArgumentBuilder)CommandManager.argument("entity", RegistryEntryReferenceArgumentType.registryEntry(registryAccess, RegistryKeys.ENTITY_TYPE)).suggests(SuggestionProviders.cast(SuggestionProviders.SUMMONABLE_ENTITIES)).executes(context -> SummonCommand.execute((ServerCommandSource)context.getSource(), RegistryEntryReferenceArgumentType.getSummonableEntityType(context, "entity"), ((ServerCommandSource)context.getSource()).getPosition(), new NbtCompound(), true))).then(((RequiredArgumentBuilder)CommandManager.argument("pos", Vec3ArgumentType.vec3()).executes(context -> SummonCommand.execute((ServerCommandSource)context.getSource(), RegistryEntryReferenceArgumentType.getSummonableEntityType(context, "entity"), Vec3ArgumentType.getVec3(context, "pos"), new NbtCompound(), true))).then(CommandManager.argument("nbt", NbtCompoundArgumentType.nbtCompound()).executes(context -> SummonCommand.execute((ServerCommandSource)context.getSource(), RegistryEntryReferenceArgumentType.getSummonableEntityType(context, "entity"), Vec3ArgumentType.getVec3(context, "pos"), NbtCompoundArgumentType.getNbtCompound(context, "nbt"), false))))));
    }

    public static Entity summon(ServerCommandSource source, RegistryEntry.Reference<EntityType<?>> entityType, Vec3d pos, NbtCompound nbt, boolean initialize) throws CommandSyntaxException {
        BlockPos lv = BlockPos.ofFloored(pos);
        if (!World.isValid(lv)) {
            throw INVALID_POSITION_EXCEPTION.create();
        }
        if (source.getWorld().getDifficulty() == Difficulty.PEACEFUL && !entityType.value().isAllowedInPeaceful()) {
            throw FAILED_PEACEFUL_EXCEPTION.create();
        }
        NbtCompound lv2 = nbt.copy();
        lv2.putString("id", entityType.registryKey().getValue().toString());
        ServerWorld lv3 = source.getWorld();
        Entity lv4 = EntityType.loadEntityWithPassengers(lv2, (World)lv3, SpawnReason.COMMAND, entity -> {
            entity.refreshPositionAndAngles(arg.x, arg.y, arg.z, entity.getYaw(), entity.getPitch());
            return entity;
        });
        if (lv4 == null) {
            throw FAILED_EXCEPTION.create();
        }
        if (initialize && lv4 instanceof MobEntity) {
            MobEntity lv5 = (MobEntity)lv4;
            lv5.initialize(source.getWorld(), source.getWorld().getLocalDifficulty(lv4.getBlockPos()), SpawnReason.COMMAND, null);
        }
        if (!lv3.spawnNewEntityAndPassengers(lv4)) {
            throw FAILED_UUID_EXCEPTION.create();
        }
        return lv4;
    }

    private static int execute(ServerCommandSource source, RegistryEntry.Reference<EntityType<?>> entityType, Vec3d pos, NbtCompound nbt, boolean initialize) throws CommandSyntaxException {
        Entity lv = SummonCommand.summon(source, entityType, pos, nbt, initialize);
        source.sendFeedback(() -> Text.translatable("commands.summon.success", lv.getDisplayName()), true);
        return 1;
    }
}

