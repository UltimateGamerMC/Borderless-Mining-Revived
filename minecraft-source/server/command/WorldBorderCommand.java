/*
 * External method calls:
 *   Lnet/minecraft/server/command/CommandManager;literal(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
 *   Lnet/minecraft/server/command/CommandManager;requirePermissionLevel(Lnet/minecraft/command/permission/PermissionCheck;)Lnet/minecraft/command/permission/PermissionSourcePredicate;
 *   Lnet/minecraft/server/command/CommandManager;argument(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
 *   Lnet/minecraft/command/argument/TimeArgumentType;time(I)Lnet/minecraft/command/argument/TimeArgumentType;
 *   Lnet/minecraft/command/argument/Vec2ArgumentType;vec2()Lnet/minecraft/command/argument/Vec2ArgumentType;
 *   Lnet/minecraft/server/command/ServerCommandSource;sendFeedback(Ljava/util/function/Supplier;Z)V
 *   Lnet/minecraft/world/border/WorldBorder;interpolateSize(DDJJ)V
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/command/WorldBorderCommand;toSeconds(J)Ljava/lang/String;
 *   Lnet/minecraft/server/command/WorldBorderCommand;executeWarningTime(Lnet/minecraft/server/command/ServerCommandSource;I)I
 *   Lnet/minecraft/server/command/WorldBorderCommand;executeWarningDistance(Lnet/minecraft/server/command/ServerCommandSource;I)I
 *   Lnet/minecraft/server/command/WorldBorderCommand;executeGet(Lnet/minecraft/server/command/ServerCommandSource;)I
 *   Lnet/minecraft/server/command/WorldBorderCommand;executeBuffer(Lnet/minecraft/server/command/ServerCommandSource;F)I
 *   Lnet/minecraft/server/command/WorldBorderCommand;executeDamage(Lnet/minecraft/server/command/ServerCommandSource;F)I
 *   Lnet/minecraft/server/command/WorldBorderCommand;executeCenter(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/util/math/Vec2f;)I
 *   Lnet/minecraft/server/command/WorldBorderCommand;executeSet(Lnet/minecraft/server/command/ServerCommandSource;DJ)I
 */
package net.minecraft.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Locale;
import net.minecraft.command.argument.TimeArgumentType;
import net.minecraft.command.argument.Vec2ArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.border.WorldBorder;

public class WorldBorderCommand {
    private static final SimpleCommandExceptionType CENTER_FAILED_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.worldborder.center.failed"));
    private static final SimpleCommandExceptionType SET_FAILED_NO_CHANGE_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.worldborder.set.failed.nochange"));
    private static final SimpleCommandExceptionType SET_FAILED_SMALL_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.worldborder.set.failed.small"));
    private static final SimpleCommandExceptionType SET_FAILED_BIG_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.worldborder.set.failed.big", 5.9999968E7));
    private static final SimpleCommandExceptionType SET_FAILED_FAR_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.worldborder.set.failed.far", 2.9999984E7));
    private static final SimpleCommandExceptionType WARNING_TIME_FAILED_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.worldborder.warning.time.failed"));
    private static final SimpleCommandExceptionType WARNING_DISTANCE_FAILED_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.worldborder.warning.distance.failed"));
    private static final SimpleCommandExceptionType DAMAGE_BUFFER_FAILED_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.worldborder.damage.buffer.failed"));
    private static final SimpleCommandExceptionType DAMAGE_AMOUNT_FAILED_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.worldborder.damage.amount.failed"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal("worldborder").requires(CommandManager.requirePermissionLevel(CommandManager.GAMEMASTERS_CHECK))).then(CommandManager.literal("add").then((ArgumentBuilder<ServerCommandSource, ?>)((RequiredArgumentBuilder)CommandManager.argument("distance", DoubleArgumentType.doubleArg(-5.9999968E7, 5.9999968E7)).executes(context -> WorldBorderCommand.executeSet((ServerCommandSource)context.getSource(), ((ServerCommandSource)context.getSource()).getWorld().getWorldBorder().getSize() + DoubleArgumentType.getDouble(context, "distance"), 0L))).then(CommandManager.argument("time", TimeArgumentType.time(0)).executes(context -> WorldBorderCommand.executeSet((ServerCommandSource)context.getSource(), ((ServerCommandSource)context.getSource()).getWorld().getWorldBorder().getSize() + DoubleArgumentType.getDouble(context, "distance"), ((ServerCommandSource)context.getSource()).getWorld().getWorldBorder().getSizeLerpTime() + (long)IntegerArgumentType.getInteger(context, "time"))))))).then(CommandManager.literal("set").then((ArgumentBuilder<ServerCommandSource, ?>)((RequiredArgumentBuilder)CommandManager.argument("distance", DoubleArgumentType.doubleArg(-5.9999968E7, 5.9999968E7)).executes(context -> WorldBorderCommand.executeSet((ServerCommandSource)context.getSource(), DoubleArgumentType.getDouble(context, "distance"), 0L))).then(CommandManager.argument("time", TimeArgumentType.time(0)).executes(context -> WorldBorderCommand.executeSet((ServerCommandSource)context.getSource(), DoubleArgumentType.getDouble(context, "distance"), IntegerArgumentType.getInteger(context, "time"))))))).then(CommandManager.literal("center").then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("pos", Vec2ArgumentType.vec2()).executes(context -> WorldBorderCommand.executeCenter((ServerCommandSource)context.getSource(), Vec2ArgumentType.getVec2(context, "pos")))))).then(((LiteralArgumentBuilder)CommandManager.literal("damage").then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.literal("amount").then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("damagePerBlock", FloatArgumentType.floatArg(0.0f)).executes(context -> WorldBorderCommand.executeDamage((ServerCommandSource)context.getSource(), FloatArgumentType.getFloat(context, "damagePerBlock")))))).then(CommandManager.literal("buffer").then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("distance", FloatArgumentType.floatArg(0.0f)).executes(context -> WorldBorderCommand.executeBuffer((ServerCommandSource)context.getSource(), FloatArgumentType.getFloat(context, "distance"))))))).then(CommandManager.literal("get").executes(context -> WorldBorderCommand.executeGet((ServerCommandSource)context.getSource())))).then(((LiteralArgumentBuilder)CommandManager.literal("warning").then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.literal("distance").then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("distance", IntegerArgumentType.integer(0)).executes(context -> WorldBorderCommand.executeWarningDistance((ServerCommandSource)context.getSource(), IntegerArgumentType.getInteger(context, "distance")))))).then(CommandManager.literal("time").then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("time", TimeArgumentType.time(0)).executes(context -> WorldBorderCommand.executeWarningTime((ServerCommandSource)context.getSource(), IntegerArgumentType.getInteger(context, "time")))))));
    }

    private static int executeBuffer(ServerCommandSource source, float distance) throws CommandSyntaxException {
        WorldBorder lv = source.getWorld().getWorldBorder();
        if (lv.getSafeZone() == (double)distance) {
            throw DAMAGE_BUFFER_FAILED_EXCEPTION.create();
        }
        lv.setSafeZone(distance);
        source.sendFeedback(() -> Text.translatable("commands.worldborder.damage.buffer.success", String.format(Locale.ROOT, "%.2f", Float.valueOf(distance))), true);
        return (int)distance;
    }

    private static int executeDamage(ServerCommandSource source, float damagePerBlock) throws CommandSyntaxException {
        WorldBorder lv = source.getWorld().getWorldBorder();
        if (lv.getDamagePerBlock() == (double)damagePerBlock) {
            throw DAMAGE_AMOUNT_FAILED_EXCEPTION.create();
        }
        lv.setDamagePerBlock(damagePerBlock);
        source.sendFeedback(() -> Text.translatable("commands.worldborder.damage.amount.success", String.format(Locale.ROOT, "%.2f", Float.valueOf(damagePerBlock))), true);
        return (int)damagePerBlock;
    }

    private static int executeWarningTime(ServerCommandSource source, int time) throws CommandSyntaxException {
        WorldBorder lv = source.getWorld().getWorldBorder();
        if (lv.getWarningTime() == time) {
            throw WARNING_TIME_FAILED_EXCEPTION.create();
        }
        lv.setWarningTime(time);
        source.sendFeedback(() -> Text.translatable("commands.worldborder.warning.time.success", WorldBorderCommand.toSeconds(time)), true);
        return time;
    }

    private static int executeWarningDistance(ServerCommandSource source, int distance) throws CommandSyntaxException {
        WorldBorder lv = source.getWorld().getWorldBorder();
        if (lv.getWarningBlocks() == distance) {
            throw WARNING_DISTANCE_FAILED_EXCEPTION.create();
        }
        lv.setWarningBlocks(distance);
        source.sendFeedback(() -> Text.translatable("commands.worldborder.warning.distance.success", distance), true);
        return distance;
    }

    private static int executeGet(ServerCommandSource source) {
        double d = source.getWorld().getWorldBorder().getSize();
        source.sendFeedback(() -> Text.translatable("commands.worldborder.get", String.format(Locale.ROOT, "%.0f", d)), false);
        return MathHelper.floor(d + 0.5);
    }

    private static int executeCenter(ServerCommandSource source, Vec2f pos) throws CommandSyntaxException {
        WorldBorder lv = source.getWorld().getWorldBorder();
        if (lv.getCenterX() == (double)pos.x && lv.getCenterZ() == (double)pos.y) {
            throw CENTER_FAILED_EXCEPTION.create();
        }
        if ((double)Math.abs(pos.x) > 2.9999984E7 || (double)Math.abs(pos.y) > 2.9999984E7) {
            throw SET_FAILED_FAR_EXCEPTION.create();
        }
        lv.setCenter(pos.x, pos.y);
        source.sendFeedback(() -> Text.translatable("commands.worldborder.center.success", String.format(Locale.ROOT, "%.2f", Float.valueOf(arg.x)), String.format(Locale.ROOT, "%.2f", Float.valueOf(arg.y))), true);
        return 0;
    }

    private static int executeSet(ServerCommandSource source, double distance, long time) throws CommandSyntaxException {
        ServerWorld lv = source.getWorld();
        WorldBorder lv2 = lv.getWorldBorder();
        double e = lv2.getSize();
        if (e == distance) {
            throw SET_FAILED_NO_CHANGE_EXCEPTION.create();
        }
        if (distance < 1.0) {
            throw SET_FAILED_SMALL_EXCEPTION.create();
        }
        if (distance > 5.9999968E7) {
            throw SET_FAILED_BIG_EXCEPTION.create();
        }
        String string = String.format(Locale.ROOT, "%.1f", distance);
        if (time > 0L) {
            lv2.interpolateSize(e, distance, time, lv.getTime());
            if (distance > e) {
                source.sendFeedback(() -> Text.translatable("commands.worldborder.set.grow", string, WorldBorderCommand.toSeconds(time)), true);
            } else {
                source.sendFeedback(() -> Text.translatable("commands.worldborder.set.shrink", string, WorldBorderCommand.toSeconds(time)), true);
            }
        } else {
            lv2.setSize(distance);
            source.sendFeedback(() -> Text.translatable("commands.worldborder.set.immediate", string), true);
        }
        return (int)(distance - e);
    }

    private static String toSeconds(long ticks) {
        return String.format(Locale.ROOT, "%.2f", (double)ticks / 20.0);
    }
}

