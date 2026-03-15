/*
 * External method calls:
 *   Lnet/minecraft/server/command/CommandManager;literal(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
 *   Lnet/minecraft/server/command/CommandManager;requirePermissionLevel(Lnet/minecraft/command/permission/PermissionCheck;)Lnet/minecraft/command/permission/PermissionSourcePredicate;
 *   Lnet/minecraft/command/argument/EntityArgumentType;entity()Lnet/minecraft/command/argument/EntityArgumentType;
 *   Lnet/minecraft/server/command/CommandManager;argument(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
 *   Lnet/minecraft/command/argument/RegistryEntryReferenceArgumentType;registryEntry(Lnet/minecraft/command/CommandRegistryAccess;Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/command/argument/RegistryEntryReferenceArgumentType;
 *   Lnet/minecraft/command/argument/Vec3ArgumentType;vec3()Lnet/minecraft/command/argument/Vec3ArgumentType;
 *   Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)Z
 *   Lnet/minecraft/server/command/ServerCommandSource;sendFeedback(Ljava/util/function/Supplier;Z)V
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/entity/damage/DamageSources;generic()Lnet/minecraft/entity/damage/DamageSource;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/command/DamageCommand;execute(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/entity/Entity;FLnet/minecraft/entity/damage/DamageSource;)I
 */
package net.minecraft.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.RegistryEntryReferenceArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class DamageCommand {
    private static final SimpleCommandExceptionType INVULNERABLE_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.damage.invulnerable"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal("damage").requires(CommandManager.requirePermissionLevel(CommandManager.GAMEMASTERS_CHECK))).then(CommandManager.argument("target", EntityArgumentType.entity()).then((ArgumentBuilder<ServerCommandSource, ?>)((RequiredArgumentBuilder)CommandManager.argument("amount", FloatArgumentType.floatArg(0.0f)).executes(context -> DamageCommand.execute((ServerCommandSource)context.getSource(), EntityArgumentType.getEntity(context, "target"), FloatArgumentType.getFloat(context, "amount"), ((ServerCommandSource)context.getSource()).getWorld().getDamageSources().generic()))).then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)CommandManager.argument("damageType", RegistryEntryReferenceArgumentType.registryEntry(registryAccess, RegistryKeys.DAMAGE_TYPE)).executes(context -> DamageCommand.execute((ServerCommandSource)context.getSource(), EntityArgumentType.getEntity(context, "target"), FloatArgumentType.getFloat(context, "amount"), new DamageSource(RegistryEntryReferenceArgumentType.getRegistryEntry(context, "damageType", RegistryKeys.DAMAGE_TYPE))))).then(CommandManager.literal("at").then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("location", Vec3ArgumentType.vec3()).executes(context -> DamageCommand.execute((ServerCommandSource)context.getSource(), EntityArgumentType.getEntity(context, "target"), FloatArgumentType.getFloat(context, "amount"), new DamageSource(RegistryEntryReferenceArgumentType.getRegistryEntry(context, "damageType", RegistryKeys.DAMAGE_TYPE), Vec3ArgumentType.getVec3(context, "location"))))))).then(CommandManager.literal("by").then((ArgumentBuilder<ServerCommandSource, ?>)((RequiredArgumentBuilder)CommandManager.argument("entity", EntityArgumentType.entity()).executes(context -> DamageCommand.execute((ServerCommandSource)context.getSource(), EntityArgumentType.getEntity(context, "target"), FloatArgumentType.getFloat(context, "amount"), new DamageSource(RegistryEntryReferenceArgumentType.getRegistryEntry(context, "damageType", RegistryKeys.DAMAGE_TYPE), EntityArgumentType.getEntity(context, "entity"))))).then(CommandManager.literal("from").then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("cause", EntityArgumentType.entity()).executes(context -> DamageCommand.execute((ServerCommandSource)context.getSource(), EntityArgumentType.getEntity(context, "target"), FloatArgumentType.getFloat(context, "amount"), new DamageSource(RegistryEntryReferenceArgumentType.getRegistryEntry(context, "damageType", RegistryKeys.DAMAGE_TYPE), EntityArgumentType.getEntity(context, "entity"), EntityArgumentType.getEntity(context, "cause"))))))))))));
    }

    private static int execute(ServerCommandSource source, Entity target, float amount, DamageSource damageSource) throws CommandSyntaxException {
        if (target.damage(source.getWorld(), damageSource, amount)) {
            source.sendFeedback(() -> Text.translatable("commands.damage.success", Float.valueOf(amount), target.getDisplayName()), true);
            return 1;
        }
        throw INVULNERABLE_EXCEPTION.create();
    }
}

