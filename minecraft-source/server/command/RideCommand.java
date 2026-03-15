/*
 * External method calls:
 *   Lnet/minecraft/server/command/CommandManager;literal(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
 *   Lnet/minecraft/server/command/CommandManager;requirePermissionLevel(Lnet/minecraft/command/permission/PermissionCheck;)Lnet/minecraft/command/permission/PermissionSourcePredicate;
 *   Lnet/minecraft/command/argument/EntityArgumentType;entity()Lnet/minecraft/command/argument/EntityArgumentType;
 *   Lnet/minecraft/server/command/CommandManager;argument(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
 *   Lnet/minecraft/entity/Entity;streamSelfAndPassengers()Ljava/util/stream/Stream;
 *   Lnet/minecraft/entity/Entity;startRiding(Lnet/minecraft/entity/Entity;ZZ)Z
 *   Lnet/minecraft/server/command/ServerCommandSource;sendFeedback(Ljava/util/function/Supplier;Z)V
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Text;stringifiedTranslatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/command/RideCommand;executeDismount(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/entity/Entity;)I
 *   Lnet/minecraft/server/command/RideCommand;executeMount(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;)I
 */
package net.minecraft.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class RideCommand {
    private static final DynamicCommandExceptionType NOT_RIDING_EXCEPTION = new DynamicCommandExceptionType(entity -> Text.stringifiedTranslatable("commands.ride.not_riding", entity));
    private static final Dynamic2CommandExceptionType ALREADY_RIDING_EXCEPTION = new Dynamic2CommandExceptionType((rider, vehicle) -> Text.stringifiedTranslatable("commands.ride.already_riding", rider, vehicle));
    private static final Dynamic2CommandExceptionType GENERIC_FAILURE_EXCEPTION = new Dynamic2CommandExceptionType((rider, vehicle) -> Text.stringifiedTranslatable("commands.ride.mount.failure.generic", rider, vehicle));
    private static final SimpleCommandExceptionType CANT_RIDE_PLAYERS_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.ride.mount.failure.cant_ride_players"));
    private static final SimpleCommandExceptionType RIDE_LOOP_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.ride.mount.failure.loop"));
    private static final SimpleCommandExceptionType WRONG_DIMENSION_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.ride.mount.failure.wrong_dimension"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal("ride").requires(CommandManager.requirePermissionLevel(CommandManager.GAMEMASTERS_CHECK))).then(((RequiredArgumentBuilder)CommandManager.argument("target", EntityArgumentType.entity()).then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.literal("mount").then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("vehicle", EntityArgumentType.entity()).executes(context -> RideCommand.executeMount((ServerCommandSource)context.getSource(), EntityArgumentType.getEntity(context, "target"), EntityArgumentType.getEntity(context, "vehicle")))))).then(CommandManager.literal("dismount").executes(context -> RideCommand.executeDismount((ServerCommandSource)context.getSource(), EntityArgumentType.getEntity(context, "target"))))));
    }

    private static int executeMount(ServerCommandSource source, Entity rider, Entity vehicle) throws CommandSyntaxException {
        Entity lv = rider.getVehicle();
        if (lv != null) {
            throw ALREADY_RIDING_EXCEPTION.create(rider.getDisplayName(), lv.getDisplayName());
        }
        if (vehicle.getType() == EntityType.PLAYER) {
            throw CANT_RIDE_PLAYERS_EXCEPTION.create();
        }
        if (rider.streamSelfAndPassengers().anyMatch(passenger -> passenger == vehicle)) {
            throw RIDE_LOOP_EXCEPTION.create();
        }
        if (rider.getEntityWorld() != vehicle.getEntityWorld()) {
            throw WRONG_DIMENSION_EXCEPTION.create();
        }
        if (!rider.startRiding(vehicle, true, true)) {
            throw GENERIC_FAILURE_EXCEPTION.create(rider.getDisplayName(), vehicle.getDisplayName());
        }
        source.sendFeedback(() -> Text.translatable("commands.ride.mount.success", rider.getDisplayName(), vehicle.getDisplayName()), true);
        return 1;
    }

    private static int executeDismount(ServerCommandSource source, Entity rider) throws CommandSyntaxException {
        Entity lv = rider.getVehicle();
        if (lv == null) {
            throw NOT_RIDING_EXCEPTION.create(rider.getDisplayName());
        }
        rider.stopRiding();
        source.sendFeedback(() -> Text.translatable("commands.ride.dismount.success", rider.getDisplayName(), lv.getDisplayName()), true);
        return 1;
    }
}

