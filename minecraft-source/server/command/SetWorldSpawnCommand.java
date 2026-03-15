/*
 * External method calls:
 *   Lnet/minecraft/server/command/CommandManager;literal(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
 *   Lnet/minecraft/server/command/CommandManager;requirePermissionLevel(Lnet/minecraft/command/permission/PermissionCheck;)Lnet/minecraft/command/permission/PermissionSourcePredicate;
 *   Lnet/minecraft/command/argument/BlockPosArgumentType;blockPos()Lnet/minecraft/command/argument/BlockPosArgumentType;
 *   Lnet/minecraft/server/command/CommandManager;argument(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
 *   Lnet/minecraft/command/argument/RotationArgumentType;rotation()Lnet/minecraft/command/argument/RotationArgumentType;
 *   Lnet/minecraft/world/WorldProperties$SpawnPoint;create(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/util/math/BlockPos;FF)Lnet/minecraft/world/WorldProperties$SpawnPoint;
 *   Lnet/minecraft/server/command/ServerCommandSource;sendFeedback(Ljava/util/function/Supplier;Z)V
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/command/SetWorldSpawnCommand;execute(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/command/argument/PosArgument;)I
 */
package net.minecraft.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.DefaultPosArgument;
import net.minecraft.command.argument.PosArgument;
import net.minecraft.command.argument.RotationArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.WorldProperties;

public class SetWorldSpawnCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal("setworldspawn").requires(CommandManager.requirePermissionLevel(CommandManager.GAMEMASTERS_CHECK))).executes(context -> SetWorldSpawnCommand.execute((ServerCommandSource)context.getSource(), BlockPos.ofFloored(((ServerCommandSource)context.getSource()).getPosition()), DefaultPosArgument.DEFAULT_ROTATION))).then(((RequiredArgumentBuilder)CommandManager.argument("pos", BlockPosArgumentType.blockPos()).executes(context -> SetWorldSpawnCommand.execute((ServerCommandSource)context.getSource(), BlockPosArgumentType.getValidBlockPos(context, "pos"), DefaultPosArgument.DEFAULT_ROTATION))).then(CommandManager.argument("rotation", RotationArgumentType.rotation()).executes(context -> SetWorldSpawnCommand.execute((ServerCommandSource)context.getSource(), BlockPosArgumentType.getValidBlockPos(context, "pos"), RotationArgumentType.getRotation(context, "rotation"))))));
    }

    private static int execute(ServerCommandSource source, BlockPos pos, PosArgument rotation) {
        ServerWorld lv = source.getWorld();
        Vec2f lv2 = rotation.getRotation(source);
        float f = lv2.y;
        float g = lv2.x;
        WorldProperties.SpawnPoint lv3 = WorldProperties.SpawnPoint.create(lv.getRegistryKey(), pos, f, g);
        lv.setSpawnPoint(lv3);
        source.sendFeedback(() -> Text.translatable("commands.setworldspawn.success", pos.getX(), pos.getY(), pos.getZ(), Float.valueOf(lv3.yaw()), Float.valueOf(lv3.pitch()), lv.getRegistryKey().getValue().toString()), true);
        return 1;
    }
}

