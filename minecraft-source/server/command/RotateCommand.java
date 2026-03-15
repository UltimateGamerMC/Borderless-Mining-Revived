/*
 * External method calls:
 *   Lnet/minecraft/server/command/CommandManager;literal(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
 *   Lnet/minecraft/server/command/CommandManager;requirePermissionLevel(Lnet/minecraft/command/permission/PermissionCheck;)Lnet/minecraft/command/permission/PermissionSourcePredicate;
 *   Lnet/minecraft/command/argument/EntityArgumentType;entity()Lnet/minecraft/command/argument/EntityArgumentType;
 *   Lnet/minecraft/server/command/CommandManager;argument(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
 *   Lnet/minecraft/command/argument/RotationArgumentType;rotation()Lnet/minecraft/command/argument/RotationArgumentType;
 *   Lnet/minecraft/command/argument/EntityAnchorArgumentType;entityAnchor()Lnet/minecraft/command/argument/EntityAnchorArgumentType;
 *   Lnet/minecraft/command/argument/Vec3ArgumentType;vec3()Lnet/minecraft/command/argument/Vec3ArgumentType;
 *   Lnet/minecraft/entity/Entity;rotate(FZFZ)V
 *   Lnet/minecraft/server/command/ServerCommandSource;sendFeedback(Ljava/util/function/Supplier;Z)V
 *   Lnet/minecraft/server/command/LookTarget;look(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/entity/Entity;)V
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/command/RotateCommand;rotateFacingLookTarget(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/entity/Entity;Lnet/minecraft/server/command/LookTarget;)I
 *   Lnet/minecraft/server/command/RotateCommand;rotateToPos(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/entity/Entity;Lnet/minecraft/command/argument/PosArgument;)I
 */
package net.minecraft.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.PosArgument;
import net.minecraft.command.argument.RotationArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.LookTarget;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec2f;

public class RotateCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal("rotate").requires(CommandManager.requirePermissionLevel(CommandManager.GAMEMASTERS_CHECK))).then(((RequiredArgumentBuilder)CommandManager.argument("target", EntityArgumentType.entity()).then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("rotation", RotationArgumentType.rotation()).executes(context -> RotateCommand.rotateToPos((ServerCommandSource)context.getSource(), EntityArgumentType.getEntity(context, "target"), RotationArgumentType.getRotation(context, "rotation"))))).then(((LiteralArgumentBuilder)CommandManager.literal("facing").then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.literal("entity").then((ArgumentBuilder<ServerCommandSource, ?>)((RequiredArgumentBuilder)CommandManager.argument("facingEntity", EntityArgumentType.entity()).executes(context -> RotateCommand.rotateFacingLookTarget((ServerCommandSource)context.getSource(), EntityArgumentType.getEntity(context, "target"), new LookTarget.LookAtEntity(EntityArgumentType.getEntity(context, "facingEntity"), EntityAnchorArgumentType.EntityAnchor.FEET)))).then(CommandManager.argument("facingAnchor", EntityAnchorArgumentType.entityAnchor()).executes(context -> RotateCommand.rotateFacingLookTarget((ServerCommandSource)context.getSource(), EntityArgumentType.getEntity(context, "target"), new LookTarget.LookAtEntity(EntityArgumentType.getEntity(context, "facingEntity"), EntityAnchorArgumentType.getEntityAnchor(context, "facingAnchor")))))))).then(CommandManager.argument("facingLocation", Vec3ArgumentType.vec3()).executes(context -> RotateCommand.rotateFacingLookTarget((ServerCommandSource)context.getSource(), EntityArgumentType.getEntity(context, "target"), new LookTarget.LookAtPosition(Vec3ArgumentType.getVec3(context, "facingLocation"))))))));
    }

    private static int rotateToPos(ServerCommandSource source, Entity entity, PosArgument pos) {
        Vec2f lv = pos.getRotation(source);
        float f = pos.isYRelative() ? lv.y - entity.getYaw() : lv.y;
        float g = pos.isXRelative() ? lv.x - entity.getPitch() : lv.x;
        entity.rotate(f, pos.isYRelative(), g, pos.isXRelative());
        source.sendFeedback(() -> Text.translatable("commands.rotate.success", entity.getDisplayName()), true);
        return 1;
    }

    private static int rotateFacingLookTarget(ServerCommandSource source, Entity entity, LookTarget lookTarget) {
        lookTarget.look(source, entity);
        source.sendFeedback(() -> Text.translatable("commands.rotate.success", entity.getDisplayName()), true);
        return 1;
    }
}

