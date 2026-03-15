/*
 * External method calls:
 *   Lnet/minecraft/server/command/AdvancementCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/AttributeCommand;register(Lcom/mojang/brigadier/CommandDispatcher;Lnet/minecraft/command/CommandRegistryAccess;)V
 *   Lnet/minecraft/server/command/ExecuteCommand;register(Lcom/mojang/brigadier/CommandDispatcher;Lnet/minecraft/command/CommandRegistryAccess;)V
 *   Lnet/minecraft/server/command/BossBarCommand;register(Lcom/mojang/brigadier/CommandDispatcher;Lnet/minecraft/command/CommandRegistryAccess;)V
 *   Lnet/minecraft/server/command/ClearCommand;register(Lcom/mojang/brigadier/CommandDispatcher;Lnet/minecraft/command/CommandRegistryAccess;)V
 *   Lnet/minecraft/server/command/CloneCommand;register(Lcom/mojang/brigadier/CommandDispatcher;Lnet/minecraft/command/CommandRegistryAccess;)V
 *   Lnet/minecraft/server/command/DamageCommand;register(Lcom/mojang/brigadier/CommandDispatcher;Lnet/minecraft/command/CommandRegistryAccess;)V
 *   Lnet/minecraft/server/command/DataCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/DatapackCommand;register(Lcom/mojang/brigadier/CommandDispatcher;Lnet/minecraft/command/CommandRegistryAccess;)V
 *   Lnet/minecraft/server/command/DebugCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/DefaultGameModeCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/DialogCommand;register(Lcom/mojang/brigadier/CommandDispatcher;Lnet/minecraft/command/CommandRegistryAccess;)V
 *   Lnet/minecraft/server/command/DifficultyCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/EffectCommand;register(Lcom/mojang/brigadier/CommandDispatcher;Lnet/minecraft/command/CommandRegistryAccess;)V
 *   Lnet/minecraft/server/command/MeCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/EnchantCommand;register(Lcom/mojang/brigadier/CommandDispatcher;Lnet/minecraft/command/CommandRegistryAccess;)V
 *   Lnet/minecraft/server/command/ExperienceCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/FillCommand;register(Lcom/mojang/brigadier/CommandDispatcher;Lnet/minecraft/command/CommandRegistryAccess;)V
 *   Lnet/minecraft/server/command/FillBiomeCommand;register(Lcom/mojang/brigadier/CommandDispatcher;Lnet/minecraft/command/CommandRegistryAccess;)V
 *   Lnet/minecraft/server/command/ForceLoadCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/FunctionCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/GameModeCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/GameRuleCommand;register(Lcom/mojang/brigadier/CommandDispatcher;Lnet/minecraft/command/CommandRegistryAccess;)V
 *   Lnet/minecraft/server/command/GiveCommand;register(Lcom/mojang/brigadier/CommandDispatcher;Lnet/minecraft/command/CommandRegistryAccess;)V
 *   Lnet/minecraft/server/command/HelpCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/ItemCommand;register(Lcom/mojang/brigadier/CommandDispatcher;Lnet/minecraft/command/CommandRegistryAccess;)V
 *   Lnet/minecraft/server/command/KickCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/KillCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/ListCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/LocateCommand;register(Lcom/mojang/brigadier/CommandDispatcher;Lnet/minecraft/command/CommandRegistryAccess;)V
 *   Lnet/minecraft/server/command/LootCommand;register(Lcom/mojang/brigadier/CommandDispatcher;Lnet/minecraft/command/CommandRegistryAccess;)V
 *   Lnet/minecraft/server/command/MessageCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/ParticleCommand;register(Lcom/mojang/brigadier/CommandDispatcher;Lnet/minecraft/command/CommandRegistryAccess;)V
 *   Lnet/minecraft/server/command/PlaceCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/PlaySoundCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/RandomCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/ReloadCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/RecipeCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/FetchProfileCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/ReturnCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/RideCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/RotateCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/SayCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/ScheduleCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/ScoreboardCommand;register(Lcom/mojang/brigadier/CommandDispatcher;Lnet/minecraft/command/CommandRegistryAccess;)V
 *   Lnet/minecraft/server/command/SeedCommand;register(Lcom/mojang/brigadier/CommandDispatcher;Z)V
 *   Lnet/minecraft/server/command/VersionCommand;register(Lcom/mojang/brigadier/CommandDispatcher;Z)V
 *   Lnet/minecraft/server/command/SetBlockCommand;register(Lcom/mojang/brigadier/CommandDispatcher;Lnet/minecraft/command/CommandRegistryAccess;)V
 *   Lnet/minecraft/server/command/SpawnPointCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/SetWorldSpawnCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/SpectateCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/SpreadPlayersCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/StopSoundCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/StopwatchCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/SummonCommand;register(Lcom/mojang/brigadier/CommandDispatcher;Lnet/minecraft/command/CommandRegistryAccess;)V
 *   Lnet/minecraft/server/command/TagCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/TeamCommand;register(Lcom/mojang/brigadier/CommandDispatcher;Lnet/minecraft/command/CommandRegistryAccess;)V
 *   Lnet/minecraft/server/command/TeamMsgCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/TeleportCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/TellRawCommand;register(Lcom/mojang/brigadier/CommandDispatcher;Lnet/minecraft/command/CommandRegistryAccess;)V
 *   Lnet/minecraft/server/command/TestCommand;register(Lcom/mojang/brigadier/CommandDispatcher;Lnet/minecraft/command/CommandRegistryAccess;)V
 *   Lnet/minecraft/server/command/TickCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/TimeCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/TitleCommand;register(Lcom/mojang/brigadier/CommandDispatcher;Lnet/minecraft/command/CommandRegistryAccess;)V
 *   Lnet/minecraft/server/command/TriggerCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/WaypointCommand;register(Lcom/mojang/brigadier/CommandDispatcher;Lnet/minecraft/command/CommandRegistryAccess;)V
 *   Lnet/minecraft/server/command/WeatherCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/WorldBorderCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/JfrCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/ChaseCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/RaidCommand;register(Lcom/mojang/brigadier/CommandDispatcher;Lnet/minecraft/command/CommandRegistryAccess;)V
 *   Lnet/minecraft/server/command/DebugPathCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/DebugMobSpawningCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/WardenSpawnTrackerCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/SpawnArmorTrimsCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/ServerPackCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/DebugConfigCommand;register(Lcom/mojang/brigadier/CommandDispatcher;Lnet/minecraft/command/CommandRegistryAccess;)V
 *   Lnet/minecraft/server/dedicated/command/BanIpCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/dedicated/command/BanListCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/dedicated/command/BanCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/dedicated/command/DeOpCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/dedicated/command/OpCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/dedicated/command/PardonCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/dedicated/command/PardonIpCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/dedicated/command/PerfCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/dedicated/command/SaveAllCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/dedicated/command/SaveOffCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/dedicated/command/SaveOnCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/dedicated/command/SetIdleTimeoutCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/dedicated/command/StopCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/dedicated/command/TransferCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/dedicated/command/WhitelistCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/PublishCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V
 *   Lnet/minecraft/server/command/AbstractServerCommandSource;asResultConsumer()Lcom/mojang/brigadier/ResultConsumer;
 *   Lnet/minecraft/util/profiler/Profiler;push(Ljava/util/function/Supplier;)V
 *   Lnet/minecraft/text/Text;literal(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/MutableText;append(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/MutableText;styled(Ljava/util/function/UnaryOperator;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/server/command/ServerCommandSource;sendError(Lnet/minecraft/text/Text;)V
 *   Lnet/minecraft/text/Texts;toText(Lcom/mojang/brigadier/Message;)Lnet/minecraft/text/Text;
 *   Lnet/minecraft/text/Text;empty()Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/MutableText;append(Lnet/minecraft/text/Text;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/MutableText;formatted([Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V
 *   Lnet/minecraft/registry/BuiltinRegistries;createWrapperLookup()Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;
 *   Lnet/minecraft/command/argument/ArgumentHelper;collectUsedArgumentTypes(Lcom/mojang/brigadier/tree/CommandNode;)Ljava/util/Set;
 *   Lnet/minecraft/server/command/CommandManager$CommandParser;parse(Lcom/mojang/brigadier/StringReader;)V
 *   Lnet/minecraft/text/Style;withClickEvent(Lnet/minecraft/text/ClickEvent;)Lnet/minecraft/text/Style;
 *   Lnet/minecraft/text/Style;withHoverEvent(Lnet/minecraft/text/HoverEvent;)Lnet/minecraft/text/Style;
 *   Lnet/minecraft/command/CommandExecutionContext;enqueueCommand(Lnet/minecraft/command/CommandExecutionContext;Ljava/lang/String;Lcom/mojang/brigadier/context/ContextChain;Lnet/minecraft/server/command/AbstractServerCommandSource;Lnet/minecraft/command/ReturnValueConsumer;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/command/CommandManager;stripLeadingSlash(Ljava/lang/String;)Ljava/lang/String;
 *   Lnet/minecraft/server/command/CommandManager;execute(Lcom/mojang/brigadier/ParseResults;Ljava/lang/String;)V
 *   Lnet/minecraft/server/command/CommandManager;checkCommand(Lcom/mojang/brigadier/ParseResults;Ljava/lang/String;Lnet/minecraft/server/command/ServerCommandSource;)Lcom/mojang/brigadier/context/ContextChain;
 *   Lnet/minecraft/server/command/CommandManager;callWithContext(Lnet/minecraft/server/command/ServerCommandSource;Ljava/util/function/Consumer;)V
 *   Lnet/minecraft/server/command/CommandManager;throwException(Lcom/mojang/brigadier/ParseResults;)V
 *   Lnet/minecraft/server/command/CommandManager;deepCopyNodes(Lcom/mojang/brigadier/tree/CommandNode;Lcom/mojang/brigadier/tree/CommandNode;Ljava/lang/Object;Ljava/util/Map;)V
 *   Lnet/minecraft/server/command/CommandManager;createRegistryAccess(Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/command/CommandRegistryAccess;
 */
package net.minecraft.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.ContextChain;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import com.mojang.logging.LogUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.SharedConstants;
import net.minecraft.command.CommandExecutionContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.DefaultPermissions;
import net.minecraft.command.ReturnValueConsumer;
import net.minecraft.command.argument.ArgumentHelper;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.permission.PermissionCheck;
import net.minecraft.command.permission.PermissionPredicate;
import net.minecraft.command.permission.PermissionSource;
import net.minecraft.command.permission.PermissionSourcePredicate;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.network.packet.s2c.play.CommandTreeS2CPacket;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.command.AbstractServerCommandSource;
import net.minecraft.server.command.AdvancementCommand;
import net.minecraft.server.command.AttributeCommand;
import net.minecraft.server.command.BossBarCommand;
import net.minecraft.server.command.ChaseCommand;
import net.minecraft.server.command.ClearCommand;
import net.minecraft.server.command.CloneCommand;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.DamageCommand;
import net.minecraft.server.command.DataCommand;
import net.minecraft.server.command.DatapackCommand;
import net.minecraft.server.command.DebugCommand;
import net.minecraft.server.command.DebugConfigCommand;
import net.minecraft.server.command.DebugMobSpawningCommand;
import net.minecraft.server.command.DebugPathCommand;
import net.minecraft.server.command.DefaultGameModeCommand;
import net.minecraft.server.command.DialogCommand;
import net.minecraft.server.command.DifficultyCommand;
import net.minecraft.server.command.EffectCommand;
import net.minecraft.server.command.EnchantCommand;
import net.minecraft.server.command.ExecuteCommand;
import net.minecraft.server.command.ExperienceCommand;
import net.minecraft.server.command.FetchProfileCommand;
import net.minecraft.server.command.FillBiomeCommand;
import net.minecraft.server.command.FillCommand;
import net.minecraft.server.command.ForceLoadCommand;
import net.minecraft.server.command.FunctionCommand;
import net.minecraft.server.command.GameModeCommand;
import net.minecraft.server.command.GameRuleCommand;
import net.minecraft.server.command.GiveCommand;
import net.minecraft.server.command.HelpCommand;
import net.minecraft.server.command.ItemCommand;
import net.minecraft.server.command.JfrCommand;
import net.minecraft.server.command.KickCommand;
import net.minecraft.server.command.KillCommand;
import net.minecraft.server.command.ListCommand;
import net.minecraft.server.command.LocateCommand;
import net.minecraft.server.command.LootCommand;
import net.minecraft.server.command.MeCommand;
import net.minecraft.server.command.MessageCommand;
import net.minecraft.server.command.ParticleCommand;
import net.minecraft.server.command.PlaceCommand;
import net.minecraft.server.command.PlaySoundCommand;
import net.minecraft.server.command.PublishCommand;
import net.minecraft.server.command.RaidCommand;
import net.minecraft.server.command.RandomCommand;
import net.minecraft.server.command.RecipeCommand;
import net.minecraft.server.command.ReloadCommand;
import net.minecraft.server.command.ReturnCommand;
import net.minecraft.server.command.RideCommand;
import net.minecraft.server.command.RotateCommand;
import net.minecraft.server.command.SayCommand;
import net.minecraft.server.command.ScheduleCommand;
import net.minecraft.server.command.ScoreboardCommand;
import net.minecraft.server.command.SeedCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.ServerPackCommand;
import net.minecraft.server.command.SetBlockCommand;
import net.minecraft.server.command.SetWorldSpawnCommand;
import net.minecraft.server.command.SpawnArmorTrimsCommand;
import net.minecraft.server.command.SpawnPointCommand;
import net.minecraft.server.command.SpectateCommand;
import net.minecraft.server.command.SpreadPlayersCommand;
import net.minecraft.server.command.StopSoundCommand;
import net.minecraft.server.command.StopwatchCommand;
import net.minecraft.server.command.SummonCommand;
import net.minecraft.server.command.TagCommand;
import net.minecraft.server.command.TeamCommand;
import net.minecraft.server.command.TeamMsgCommand;
import net.minecraft.server.command.TeleportCommand;
import net.minecraft.server.command.TellRawCommand;
import net.minecraft.server.command.TestCommand;
import net.minecraft.server.command.TickCommand;
import net.minecraft.server.command.TimeCommand;
import net.minecraft.server.command.TitleCommand;
import net.minecraft.server.command.TriggerCommand;
import net.minecraft.server.command.VersionCommand;
import net.minecraft.server.command.WardenSpawnTrackerCommand;
import net.minecraft.server.command.WaypointCommand;
import net.minecraft.server.command.WeatherCommand;
import net.minecraft.server.command.WorldBorderCommand;
import net.minecraft.server.dedicated.command.BanCommand;
import net.minecraft.server.dedicated.command.BanIpCommand;
import net.minecraft.server.dedicated.command.BanListCommand;
import net.minecraft.server.dedicated.command.DeOpCommand;
import net.minecraft.server.dedicated.command.OpCommand;
import net.minecraft.server.dedicated.command.PardonCommand;
import net.minecraft.server.dedicated.command.PardonIpCommand;
import net.minecraft.server.dedicated.command.PerfCommand;
import net.minecraft.server.dedicated.command.SaveAllCommand;
import net.minecraft.server.dedicated.command.SaveOffCommand;
import net.minecraft.server.dedicated.command.SaveOnCommand;
import net.minecraft.server.dedicated.command.SetIdleTimeoutCommand;
import net.minecraft.server.dedicated.command.StopCommand;
import net.minecraft.server.dedicated.command.TransferCommand;
import net.minecraft.server.dedicated.command.WhitelistCommand;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profilers;
import net.minecraft.util.profiling.jfr.FlightProfiler;
import net.minecraft.world.rule.GameRules;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class CommandManager {
    public static final String PREFIX = "/";
    private static final ThreadLocal<@Nullable CommandExecutionContext<ServerCommandSource>> CURRENT_CONTEXT = new ThreadLocal();
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final PermissionCheck ALWAYS_PASS_CHECK = PermissionCheck.AlwaysPass.INSTANCE;
    public static final PermissionCheck MODERATORS_CHECK = new PermissionCheck.Require(DefaultPermissions.MODERATORS);
    public static final PermissionCheck GAMEMASTERS_CHECK = new PermissionCheck.Require(DefaultPermissions.GAMEMASTERS);
    public static final PermissionCheck ADMINS_CHECK = new PermissionCheck.Require(DefaultPermissions.ADMINS);
    public static final PermissionCheck OWNERS_CHECK = new PermissionCheck.Require(DefaultPermissions.OWNERS);
    private static final CommandTreeS2CPacket.CommandNodeInspector<ServerCommandSource> INSPECTOR = new CommandTreeS2CPacket.CommandNodeInspector<ServerCommandSource>(){
        private final ServerCommandSource source = CommandManager.createSource(PermissionPredicate.NONE);

        @Override
        public @Nullable Identifier getSuggestionProviderId(ArgumentCommandNode<ServerCommandSource, ?> node) {
            SuggestionProvider<ServerCommandSource> suggestionProvider = node.getCustomSuggestions();
            return suggestionProvider != null ? SuggestionProviders.computeId(suggestionProvider) : null;
        }

        @Override
        public boolean isExecutable(CommandNode<ServerCommandSource> node) {
            return node.getCommand() != null;
        }

        @Override
        public boolean hasRequiredLevel(CommandNode<ServerCommandSource> node) {
            Predicate<ServerCommandSource> predicate = node.getRequirement();
            return !predicate.test(this.source);
        }
    };
    private final CommandDispatcher<ServerCommandSource> dispatcher = new CommandDispatcher();

    public CommandManager(RegistrationEnvironment environment, CommandRegistryAccess commandRegistryAccess) {
        AdvancementCommand.register(this.dispatcher);
        AttributeCommand.register(this.dispatcher, commandRegistryAccess);
        ExecuteCommand.register(this.dispatcher, commandRegistryAccess);
        BossBarCommand.register(this.dispatcher, commandRegistryAccess);
        ClearCommand.register(this.dispatcher, commandRegistryAccess);
        CloneCommand.register(this.dispatcher, commandRegistryAccess);
        DamageCommand.register(this.dispatcher, commandRegistryAccess);
        DataCommand.register(this.dispatcher);
        DatapackCommand.register(this.dispatcher, commandRegistryAccess);
        DebugCommand.register(this.dispatcher);
        DefaultGameModeCommand.register(this.dispatcher);
        DialogCommand.register(this.dispatcher, commandRegistryAccess);
        DifficultyCommand.register(this.dispatcher);
        EffectCommand.register(this.dispatcher, commandRegistryAccess);
        MeCommand.register(this.dispatcher);
        EnchantCommand.register(this.dispatcher, commandRegistryAccess);
        ExperienceCommand.register(this.dispatcher);
        FillCommand.register(this.dispatcher, commandRegistryAccess);
        FillBiomeCommand.register(this.dispatcher, commandRegistryAccess);
        ForceLoadCommand.register(this.dispatcher);
        FunctionCommand.register(this.dispatcher);
        GameModeCommand.register(this.dispatcher);
        GameRuleCommand.register(this.dispatcher, commandRegistryAccess);
        GiveCommand.register(this.dispatcher, commandRegistryAccess);
        HelpCommand.register(this.dispatcher);
        ItemCommand.register(this.dispatcher, commandRegistryAccess);
        KickCommand.register(this.dispatcher);
        KillCommand.register(this.dispatcher);
        ListCommand.register(this.dispatcher);
        LocateCommand.register(this.dispatcher, commandRegistryAccess);
        LootCommand.register(this.dispatcher, commandRegistryAccess);
        MessageCommand.register(this.dispatcher);
        ParticleCommand.register(this.dispatcher, commandRegistryAccess);
        PlaceCommand.register(this.dispatcher);
        PlaySoundCommand.register(this.dispatcher);
        RandomCommand.register(this.dispatcher);
        ReloadCommand.register(this.dispatcher);
        RecipeCommand.register(this.dispatcher);
        FetchProfileCommand.register(this.dispatcher);
        ReturnCommand.register(this.dispatcher);
        RideCommand.register(this.dispatcher);
        RotateCommand.register(this.dispatcher);
        SayCommand.register(this.dispatcher);
        ScheduleCommand.register(this.dispatcher);
        ScoreboardCommand.register(this.dispatcher, commandRegistryAccess);
        SeedCommand.register(this.dispatcher, environment != RegistrationEnvironment.INTEGRATED);
        VersionCommand.register(this.dispatcher, environment != RegistrationEnvironment.INTEGRATED);
        SetBlockCommand.register(this.dispatcher, commandRegistryAccess);
        SpawnPointCommand.register(this.dispatcher);
        SetWorldSpawnCommand.register(this.dispatcher);
        SpectateCommand.register(this.dispatcher);
        SpreadPlayersCommand.register(this.dispatcher);
        StopSoundCommand.register(this.dispatcher);
        StopwatchCommand.register(this.dispatcher);
        SummonCommand.register(this.dispatcher, commandRegistryAccess);
        TagCommand.register(this.dispatcher);
        TeamCommand.register(this.dispatcher, commandRegistryAccess);
        TeamMsgCommand.register(this.dispatcher);
        TeleportCommand.register(this.dispatcher);
        TellRawCommand.register(this.dispatcher, commandRegistryAccess);
        TestCommand.register(this.dispatcher, commandRegistryAccess);
        TickCommand.register(this.dispatcher);
        TimeCommand.register(this.dispatcher);
        TitleCommand.register(this.dispatcher, commandRegistryAccess);
        TriggerCommand.register(this.dispatcher);
        WaypointCommand.register(this.dispatcher, commandRegistryAccess);
        WeatherCommand.register(this.dispatcher);
        WorldBorderCommand.register(this.dispatcher);
        if (FlightProfiler.INSTANCE.isAvailable()) {
            JfrCommand.register(this.dispatcher);
        }
        if (SharedConstants.CHASE_COMMAND) {
            ChaseCommand.register(this.dispatcher);
        }
        if (SharedConstants.DEV_COMMANDS || SharedConstants.isDevelopment) {
            RaidCommand.register(this.dispatcher, commandRegistryAccess);
            DebugPathCommand.register(this.dispatcher);
            DebugMobSpawningCommand.register(this.dispatcher);
            WardenSpawnTrackerCommand.register(this.dispatcher);
            SpawnArmorTrimsCommand.register(this.dispatcher);
            ServerPackCommand.register(this.dispatcher);
            if (environment.dedicated) {
                DebugConfigCommand.register(this.dispatcher, commandRegistryAccess);
            }
        }
        if (environment.dedicated) {
            BanIpCommand.register(this.dispatcher);
            BanListCommand.register(this.dispatcher);
            BanCommand.register(this.dispatcher);
            DeOpCommand.register(this.dispatcher);
            OpCommand.register(this.dispatcher);
            PardonCommand.register(this.dispatcher);
            PardonIpCommand.register(this.dispatcher);
            PerfCommand.register(this.dispatcher);
            SaveAllCommand.register(this.dispatcher);
            SaveOffCommand.register(this.dispatcher);
            SaveOnCommand.register(this.dispatcher);
            SetIdleTimeoutCommand.register(this.dispatcher);
            StopCommand.register(this.dispatcher);
            TransferCommand.register(this.dispatcher);
            WhitelistCommand.register(this.dispatcher);
        }
        if (environment.integrated) {
            PublishCommand.register(this.dispatcher);
        }
        this.dispatcher.setConsumer(AbstractServerCommandSource.asResultConsumer());
    }

    public static <S> ParseResults<S> withCommandSource(ParseResults<S> parseResults, UnaryOperator<S> sourceMapper) {
        CommandContextBuilder commandContextBuilder = parseResults.getContext();
        CommandContextBuilder<S> commandContextBuilder2 = commandContextBuilder.withSource(sourceMapper.apply(commandContextBuilder.getSource()));
        return new ParseResults<S>(commandContextBuilder2, parseResults.getReader(), parseResults.getExceptions());
    }

    public void parseAndExecute(ServerCommandSource source, String command) {
        command = CommandManager.stripLeadingSlash(command);
        this.execute(this.dispatcher.parse(command, source), command);
    }

    public static String stripLeadingSlash(String command) {
        return command.startsWith(PREFIX) ? command.substring(1) : command;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void execute(ParseResults<ServerCommandSource> parseResults, String command) {
        ServerCommandSource lv = parseResults.getContext().getSource();
        Profilers.get().push(() -> PREFIX + command);
        ContextChain<ServerCommandSource> contextChain = CommandManager.checkCommand(parseResults, command, lv);
        try {
            if (contextChain != null) {
                CommandManager.callWithContext(lv, context -> CommandExecutionContext.enqueueCommand(context, command, contextChain, lv, ReturnValueConsumer.EMPTY));
            }
        } catch (Exception exception) {
            MutableText lv2 = Text.literal(exception.getMessage() == null ? exception.getClass().getName() : exception.getMessage());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.error("Command exception: /{}", (Object)command, (Object)exception);
                StackTraceElement[] stackTraceElements = exception.getStackTrace();
                for (int i = 0; i < Math.min(stackTraceElements.length, 3); ++i) {
                    lv2.append("\n\n").append(stackTraceElements[i].getMethodName()).append("\n ").append(stackTraceElements[i].getFileName()).append(":").append(String.valueOf(stackTraceElements[i].getLineNumber()));
                }
            }
            lv.sendError(Text.translatable("command.failed").styled(style -> style.withHoverEvent(new HoverEvent.ShowText(lv2))));
            if (SharedConstants.VERBOSE_COMMAND_ERRORS || SharedConstants.isDevelopment) {
                lv.sendError(Text.literal(Util.getInnermostMessage(exception)));
                LOGGER.error("'/{}' threw an exception", (Object)command, (Object)exception);
            }
        } finally {
            Profilers.get().pop();
        }
    }

    private static @Nullable ContextChain<ServerCommandSource> checkCommand(ParseResults<ServerCommandSource> parseResults, String command, ServerCommandSource source) {
        try {
            CommandManager.throwException(parseResults);
            return ContextChain.tryFlatten(parseResults.getContext().build(command)).orElseThrow(() -> CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(parseResults.getReader()));
        } catch (CommandSyntaxException commandSyntaxException) {
            source.sendError(Texts.toText(commandSyntaxException.getRawMessage()));
            if (commandSyntaxException.getInput() != null && commandSyntaxException.getCursor() >= 0) {
                int i = Math.min(commandSyntaxException.getInput().length(), commandSyntaxException.getCursor());
                MutableText lv = Text.empty().formatted(Formatting.GRAY).styled(style -> style.withClickEvent(new ClickEvent.SuggestCommand(PREFIX + command)));
                if (i > 10) {
                    lv.append(ScreenTexts.ELLIPSIS);
                }
                lv.append(commandSyntaxException.getInput().substring(Math.max(0, i - 10), i));
                if (i < commandSyntaxException.getInput().length()) {
                    MutableText lv2 = Text.literal(commandSyntaxException.getInput().substring(i)).formatted(Formatting.RED, Formatting.UNDERLINE);
                    lv.append(lv2);
                }
                lv.append(Text.translatable("command.context.here").formatted(Formatting.RED, Formatting.ITALIC));
                source.sendError(lv);
            }
            return null;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void callWithContext(ServerCommandSource commandSource, Consumer<CommandExecutionContext<ServerCommandSource>> callback) {
        block9: {
            boolean bl;
            CommandExecutionContext<ServerCommandSource> lv = CURRENT_CONTEXT.get();
            boolean bl2 = bl = lv == null;
            if (bl) {
                GameRules lv2 = commandSource.getWorld().getGameRules();
                int i = Math.max(1, lv2.getValue(GameRules.MAX_COMMAND_SEQUENCE_LENGTH));
                int j = lv2.getValue(GameRules.MAX_COMMAND_FORKS);
                try (CommandExecutionContext lv3 = new CommandExecutionContext(i, j, Profilers.get());){
                    CURRENT_CONTEXT.set(lv3);
                    callback.accept(lv3);
                    lv3.run();
                    break block9;
                } finally {
                    CURRENT_CONTEXT.set(null);
                }
            }
            callback.accept(lv);
        }
    }

    public void sendCommandTree(ServerPlayerEntity player) {
        HashMap map = new HashMap();
        RootCommandNode rootCommandNode = new RootCommandNode();
        map.put(this.dispatcher.getRoot(), rootCommandNode);
        CommandManager.deepCopyNodes(this.dispatcher.getRoot(), rootCommandNode, player.getCommandSource(), map);
        player.networkHandler.sendPacket(new CommandTreeS2CPacket(rootCommandNode, INSPECTOR));
    }

    private static <S> void deepCopyNodes(CommandNode<S> root, CommandNode<S> newRoot, S source, Map<CommandNode<S>, CommandNode<S>> nodes) {
        for (CommandNode<S> commandNode3 : root.getChildren()) {
            if (!commandNode3.canUse(source)) continue;
            ArgumentBuilder<S, ?> argumentBuilder = commandNode3.createBuilder();
            if (argumentBuilder.getRedirect() != null) {
                argumentBuilder.redirect(nodes.get(argumentBuilder.getRedirect()));
            }
            CommandNode<S> commandNode4 = argumentBuilder.build();
            nodes.put(commandNode3, commandNode4);
            newRoot.addChild(commandNode4);
            if (commandNode3.getChildren().isEmpty()) continue;
            CommandManager.deepCopyNodes(commandNode3, commandNode4, source, nodes);
        }
    }

    public static LiteralArgumentBuilder<ServerCommandSource> literal(String literal) {
        return LiteralArgumentBuilder.literal(literal);
    }

    public static <T> RequiredArgumentBuilder<ServerCommandSource, T> argument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    public static Predicate<String> getCommandValidator(CommandParser parser) {
        return string -> {
            try {
                parser.parse(new StringReader((String)string));
                return true;
            } catch (CommandSyntaxException commandSyntaxException) {
                return false;
            }
        };
    }

    public CommandDispatcher<ServerCommandSource> getDispatcher() {
        return this.dispatcher;
    }

    public static <S> void throwException(ParseResults<S> parse) throws CommandSyntaxException {
        CommandSyntaxException commandSyntaxException = CommandManager.getException(parse);
        if (commandSyntaxException != null) {
            throw commandSyntaxException;
        }
    }

    public static <S> @Nullable CommandSyntaxException getException(ParseResults<S> parse) {
        if (!parse.getReader().canRead()) {
            return null;
        }
        if (parse.getExceptions().size() == 1) {
            return parse.getExceptions().values().iterator().next();
        }
        if (parse.getContext().getRange().isEmpty()) {
            return CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(parse.getReader());
        }
        return CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(parse.getReader());
    }

    public static CommandRegistryAccess createRegistryAccess(final RegistryWrapper.WrapperLookup registries) {
        return new CommandRegistryAccess(){

            @Override
            public FeatureSet getEnabledFeatures() {
                return FeatureFlags.FEATURE_MANAGER.getFeatureSet();
            }

            @Override
            public Stream<RegistryKey<? extends Registry<?>>> streamAllRegistryKeys() {
                return registries.streamAllRegistryKeys();
            }

            public <T> Optional<RegistryWrapper.Impl<T>> getOptional(RegistryKey<? extends Registry<? extends T>> registryRef) {
                return registries.getOptional(registryRef).map(this::createTagCreatingLookup);
            }

            private <T> RegistryWrapper.Impl.Delegating<T> createTagCreatingLookup(final RegistryWrapper.Impl<T> original) {
                return new RegistryWrapper.Impl.Delegating<T>(this){

                    @Override
                    public RegistryWrapper.Impl<T> getBase() {
                        return original;
                    }

                    @Override
                    public Optional<RegistryEntryList.Named<T>> getOptional(TagKey<T> tag) {
                        return Optional.of(this.getOrThrow(tag));
                    }

                    @Override
                    public RegistryEntryList.Named<T> getOrThrow(TagKey<T> tag) {
                        Optional<RegistryEntryList.Named<RegistryEntryList.Named>> optional = this.getBase().getOptional(tag);
                        return optional.orElseGet(() -> RegistryEntryList.of(this.getBase(), tag));
                    }
                };
            }
        };
    }

    public static void checkMissing() {
        CommandRegistryAccess lv = CommandManager.createRegistryAccess(BuiltinRegistries.createWrapperLookup());
        CommandDispatcher<ServerCommandSource> commandDispatcher = new CommandManager(RegistrationEnvironment.ALL, lv).getDispatcher();
        RootCommandNode<ServerCommandSource> rootCommandNode = commandDispatcher.getRoot();
        commandDispatcher.findAmbiguities((parent, child, sibling, inputs) -> LOGGER.warn("Ambiguity between arguments {} and {} with inputs: {}", commandDispatcher.getPath(child), commandDispatcher.getPath(sibling), inputs));
        Set<ArgumentType<?>> set = ArgumentHelper.collectUsedArgumentTypes(rootCommandNode);
        Set set2 = set.stream().filter(type -> !ArgumentTypes.has(type.getClass())).collect(Collectors.toSet());
        if (!set2.isEmpty()) {
            LOGGER.warn("Missing type registration for following arguments:\n {}", (Object)set2.stream().map(type -> "\t" + String.valueOf(type)).collect(Collectors.joining(",\n")));
            throw new IllegalStateException("Unregistered argument types");
        }
    }

    public static <T extends PermissionSource> PermissionSourcePredicate<T> requirePermissionLevel(PermissionCheck check) {
        return new PermissionSourcePredicate(check);
    }

    public static ServerCommandSource createSource(PermissionPredicate permissions) {
        return new ServerCommandSource(CommandOutput.DUMMY, Vec3d.ZERO, Vec2f.ZERO, null, permissions, "", ScreenTexts.EMPTY, null, null);
    }

    public static enum RegistrationEnvironment {
        ALL(true, true),
        DEDICATED(false, true),
        INTEGRATED(true, false);

        final boolean integrated;
        final boolean dedicated;

        private RegistrationEnvironment(boolean integrated, boolean dedicated) {
            this.integrated = integrated;
            this.dedicated = dedicated;
        }
    }

    @FunctionalInterface
    public static interface CommandParser {
        public void parse(StringReader var1) throws CommandSyntaxException;
    }
}

