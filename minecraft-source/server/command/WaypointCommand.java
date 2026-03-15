/*
 * External method calls:
 *   Lnet/minecraft/server/command/CommandManager;literal(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
 *   Lnet/minecraft/server/command/CommandManager;requirePermissionLevel(Lnet/minecraft/command/permission/PermissionCheck;)Lnet/minecraft/command/permission/PermissionSourcePredicate;
 *   Lnet/minecraft/command/argument/EntityArgumentType;entity()Lnet/minecraft/command/argument/EntityArgumentType;
 *   Lnet/minecraft/server/command/CommandManager;argument(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
 *   Lnet/minecraft/command/argument/ColorArgumentType;color()Lnet/minecraft/command/argument/ColorArgumentType;
 *   Lnet/minecraft/command/argument/HexColorArgumentType;hexColor()Lnet/minecraft/command/argument/HexColorArgumentType;
 *   Lnet/minecraft/command/argument/IdentifierArgumentType;identifier()Lnet/minecraft/command/argument/IdentifierArgumentType;
 *   Lnet/minecraft/server/command/ServerCommandSource;sendFeedback(Ljava/util/function/Supplier;Z)V
 *   Lnet/minecraft/text/Texts;join(Ljava/util/Collection;Ljava/util/function/Function;)Lnet/minecraft/text/Text;
 *   Lnet/minecraft/server/network/ServerWaypointHandler;onUntrack(Lnet/minecraft/world/waypoint/ServerWaypoint;)V
 *   Lnet/minecraft/server/network/ServerWaypointHandler;onTrack(Lnet/minecraft/world/waypoint/ServerWaypoint;)V
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/MutableText;styled(Ljava/util/function/UnaryOperator;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Text;literal(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Style;withClickEvent(Lnet/minecraft/text/ClickEvent;)Lnet/minecraft/text/Style;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Style;withHoverEvent(Lnet/minecraft/text/HoverEvent;)Lnet/minecraft/text/Style;
 *   Lnet/minecraft/text/Style;withColor(I)Lnet/minecraft/text/Style;
 *   Lnet/minecraft/text/MutableText;withColor(I)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/registry/RegistryKey;of(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/util/Identifier;)Lnet/minecraft/registry/RegistryKey;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/command/WaypointCommand;updateWaypointConfig(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/world/waypoint/ServerWaypoint;Ljava/util/function/Consumer;)V
 *   Lnet/minecraft/server/command/WaypointCommand;executeStyle(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/world/waypoint/ServerWaypoint;Lnet/minecraft/registry/RegistryKey;)I
 *   Lnet/minecraft/server/command/WaypointCommand;executeReset(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/world/waypoint/ServerWaypoint;)I
 *   Lnet/minecraft/server/command/WaypointCommand;executeColor(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/world/waypoint/ServerWaypoint;Ljava/lang/Integer;)I
 *   Lnet/minecraft/server/command/WaypointCommand;executeColor(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/world/waypoint/ServerWaypoint;Lnet/minecraft/util/Formatting;)I
 *   Lnet/minecraft/server/command/WaypointCommand;executeList(Lnet/minecraft/server/command/ServerCommandSource;)I
 */
package net.minecraft.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import java.util.HexFormat;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.ColorArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.HexColorArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.command.argument.WaypointArgument;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.world.waypoint.ServerWaypoint;
import net.minecraft.world.waypoint.Waypoint;
import net.minecraft.world.waypoint.WaypointStyle;
import net.minecraft.world.waypoint.WaypointStyles;

public class WaypointCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal("waypoint").requires(CommandManager.requirePermissionLevel(CommandManager.GAMEMASTERS_CHECK))).then(CommandManager.literal("list").executes(context -> WaypointCommand.executeList((ServerCommandSource)context.getSource())))).then(CommandManager.literal("modify").then((ArgumentBuilder<ServerCommandSource, ?>)((RequiredArgumentBuilder)CommandManager.argument("waypoint", EntityArgumentType.entity()).then((ArgumentBuilder<ServerCommandSource, ?>)((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal("color").then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("color", ColorArgumentType.color()).executes(context -> WaypointCommand.executeColor((ServerCommandSource)context.getSource(), WaypointArgument.getWaypoint(context, "waypoint"), ColorArgumentType.getColor(context, "color"))))).then(CommandManager.literal("hex").then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("color", HexColorArgumentType.hexColor()).executes(context -> WaypointCommand.executeColor((ServerCommandSource)context.getSource(), WaypointArgument.getWaypoint(context, "waypoint"), HexColorArgumentType.getArgbColor(context, "color")))))).then(CommandManager.literal("reset").executes(context -> WaypointCommand.executeReset((ServerCommandSource)context.getSource(), WaypointArgument.getWaypoint(context, "waypoint")))))).then(((LiteralArgumentBuilder)CommandManager.literal("style").then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.literal("reset").executes(context -> WaypointCommand.executeStyle((ServerCommandSource)context.getSource(), WaypointArgument.getWaypoint(context, "waypoint"), WaypointStyles.DEFAULT)))).then(CommandManager.literal("set").then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("style", IdentifierArgumentType.identifier()).executes(context -> WaypointCommand.executeStyle((ServerCommandSource)context.getSource(), WaypointArgument.getWaypoint(context, "waypoint"), RegistryKey.of(WaypointStyles.REGISTRY, IdentifierArgumentType.getIdentifier(context, "style"))))))))));
    }

    private static int executeStyle(ServerCommandSource source, ServerWaypoint waypoint, RegistryKey<WaypointStyle> style) {
        WaypointCommand.updateWaypointConfig(source, waypoint, config -> {
            config.style = style;
        });
        source.sendFeedback(() -> Text.translatable("commands.waypoint.modify.style"), false);
        return 0;
    }

    private static int executeColor(ServerCommandSource source, ServerWaypoint waypoint, Formatting color) {
        WaypointCommand.updateWaypointConfig(source, waypoint, config -> {
            config.color = Optional.of(color.getColorValue());
        });
        source.sendFeedback(() -> Text.translatable("commands.waypoint.modify.color", Text.literal(color.getName()).formatted(color)), false);
        return 0;
    }

    private static int executeColor(ServerCommandSource source, ServerWaypoint waypoint, Integer color) {
        WaypointCommand.updateWaypointConfig(source, waypoint, config -> {
            config.color = Optional.of(color);
        });
        source.sendFeedback(() -> Text.translatable("commands.waypoint.modify.color", Text.literal(HexFormat.of().withUpperCase().toHexDigits(ColorHelper.withAlpha(0, (int)color), 6)).withColor(color)), false);
        return 0;
    }

    private static int executeReset(ServerCommandSource source, ServerWaypoint waypoint) {
        WaypointCommand.updateWaypointConfig(source, waypoint, config -> {
            config.color = Optional.empty();
        });
        source.sendFeedback(() -> Text.translatable("commands.waypoint.modify.color.reset"), false);
        return 0;
    }

    private static int executeList(ServerCommandSource source) {
        ServerWorld lv = source.getWorld();
        Set<ServerWaypoint> set = lv.getWaypointHandler().getWaypoints();
        String string = lv.getRegistryKey().getValue().toString();
        if (set.isEmpty()) {
            source.sendFeedback(() -> Text.translatable("commands.waypoint.list.empty", string), false);
            return 0;
        }
        Text lv2 = Texts.join(set.stream().map(waypoint -> {
            if (waypoint instanceof LivingEntity) {
                LivingEntity lv = (LivingEntity)waypoint;
                BlockPos lv2 = lv.getBlockPos();
                return lv.getStyledDisplayName().copy().styled(style -> style.withClickEvent(new ClickEvent.SuggestCommand("/execute in " + string + " run tp @s " + lv2.getX() + " " + lv2.getY() + " " + lv2.getZ())).withHoverEvent(new HoverEvent.ShowText(Text.translatable("chat.coordinates.tooltip"))).withColor(arg2.getWaypointConfig().color.orElse(-1)));
            }
            return Text.literal(waypoint.toString());
        }).toList(), Function.identity());
        source.sendFeedback(() -> Text.translatable("commands.waypoint.list.success", set.size(), string, lv2), false);
        return set.size();
    }

    private static void updateWaypointConfig(ServerCommandSource source, ServerWaypoint waypoint, Consumer<Waypoint.Config> configConsumer) {
        ServerWorld lv = source.getWorld();
        lv.getWaypointHandler().onUntrack(waypoint);
        configConsumer.accept(waypoint.getWaypointConfig());
        lv.getWaypointHandler().onTrack(waypoint);
    }
}

