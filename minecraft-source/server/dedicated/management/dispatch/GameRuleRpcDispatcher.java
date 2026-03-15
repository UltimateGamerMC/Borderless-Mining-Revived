/*
 * External method calls:
 *   Lnet/minecraft/server/dedicated/management/handler/GameRuleManagementHandler;toEntry(Lnet/minecraft/world/rule/GameRule;Ljava/lang/Object;)Lnet/minecraft/server/dedicated/management/dispatch/GameRuleRpcDispatcher$RuleEntry;
 *   Lnet/minecraft/server/dedicated/management/handler/GameRuleManagementHandler;updateRule(Lnet/minecraft/server/dedicated/management/dispatch/GameRuleRpcDispatcher$RuleEntry;Lnet/minecraft/server/dedicated/management/network/ManagementConnectionId;)Lnet/minecraft/server/dedicated/management/dispatch/GameRuleRpcDispatcher$RuleEntry;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/dedicated/management/dispatch/GameRuleRpcDispatcher;toEntry(Lnet/minecraft/server/dedicated/management/dispatch/ManagementHandlerDispatcher;Lnet/minecraft/world/rule/GameRule;Ljava/lang/Object;)Lnet/minecraft/server/dedicated/management/dispatch/GameRuleRpcDispatcher$RuleEntry;
 */
package net.minecraft.server.dedicated.management.dispatch;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.registry.Registries;
import net.minecraft.server.dedicated.management.RpcException;
import net.minecraft.server.dedicated.management.dispatch.ManagementHandlerDispatcher;
import net.minecraft.server.dedicated.management.network.ManagementConnectionId;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.world.rule.GameRule;
import net.minecraft.world.rule.GameRuleType;

public class GameRuleRpcDispatcher {
    public static List<RuleEntry<?>> get(ManagementHandlerDispatcher dispatcher) {
        ArrayList list = new ArrayList();
        dispatcher.getGameRuleHandler().getRules().forEach(rule -> GameRuleRpcDispatcher.add(dispatcher, rule, list));
        return list;
    }

    private static <T> void add(ManagementHandlerDispatcher dispatcher, GameRule<T> rule, List<RuleEntry<?>> results) {
        T object = dispatcher.getGameRuleHandler().getValue(rule);
        results.add(GameRuleRpcDispatcher.toEntry(dispatcher, rule, Objects.requireNonNull(object)));
    }

    public static <T> RuleEntry<T> toEntry(ManagementHandlerDispatcher dispatcher, GameRule<T> rule, T value) {
        return dispatcher.getGameRuleHandler().toEntry(rule, value);
    }

    public static <T> RuleEntry<T> updateRule(ManagementHandlerDispatcher dispatcher, RuleEntry<T> rule, ManagementConnectionId remote) {
        return dispatcher.getGameRuleHandler().updateRule(rule, remote);
    }

    public record RuleEntry<T>(GameRule<T> gameRule, T value) {
        public static final Codec<RuleEntry<?>> TYPED_CODEC = Registries.GAME_RULE.getCodec().dispatch("key", RuleEntry::gameRule, RuleEntry::typedCodec);
        public static final Codec<RuleEntry<?>> UNTYPED_CODEC = Registries.GAME_RULE.getCodec().dispatch("key", RuleEntry::gameRule, RuleEntry::untypedCodec);

        private static <T> MapCodec<? extends RuleEntry<T>> untypedCodec(GameRule<T> rule) {
            return ((MapCodec)rule.getCodec().fieldOf("value")).xmap(entry -> new RuleEntry<Object>(rule, entry), RuleEntry::value);
        }

        private static <T> MapCodec<? extends RuleEntry<T>> typedCodec(GameRule<T> rule) {
            return RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)StringIdentifiable.createCodec(GameRuleType::values).fieldOf("type")).forGetter(entry -> entry.gameRule.getType()), ((MapCodec)rule.getCodec().fieldOf("value")).forGetter(RuleEntry::value)).apply((Applicative<RuleEntry, ?>)instance, (type, value) -> RuleEntry.validateType(rule, type, value)));
        }

        private static <T> RuleEntry<T> validateType(GameRule<T> rule, GameRuleType type, T value) {
            if (rule.getType() != type) {
                throw new RpcException("Stated type \"" + String.valueOf(type) + "\" mismatches with actual type \"" + String.valueOf(rule.getType()) + "\" of gamerule \"" + rule.toShortString() + "\"");
            }
            return new RuleEntry<T>(rule, value);
        }
    }
}

