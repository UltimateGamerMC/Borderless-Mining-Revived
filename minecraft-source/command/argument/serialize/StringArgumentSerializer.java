/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeEnumConstant(Ljava/lang/Enum;)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/PacketByteBuf;readEnumConstant(Ljava/lang/Class;)Ljava/lang/Enum;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/command/argument/serialize/StringArgumentSerializer;writeJson(Lnet/minecraft/command/argument/serialize/StringArgumentSerializer$Properties;Lcom/google/gson/JsonObject;)V
 *   Lnet/minecraft/command/argument/serialize/StringArgumentSerializer;fromPacket(Lnet/minecraft/network/PacketByteBuf;)Lnet/minecraft/command/argument/serialize/StringArgumentSerializer$Properties;
 *   Lnet/minecraft/command/argument/serialize/StringArgumentSerializer;writePacket(Lnet/minecraft/command/argument/serialize/StringArgumentSerializer$Properties;Lnet/minecraft/network/PacketByteBuf;)V
 */
package net.minecraft.command.argument.serialize;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.network.PacketByteBuf;

public class StringArgumentSerializer
implements ArgumentSerializer<StringArgumentType, Properties> {
    @Override
    public void writePacket(Properties arg, PacketByteBuf arg2) {
        arg2.writeEnumConstant(arg.type);
    }

    @Override
    public Properties fromPacket(PacketByteBuf arg) {
        StringArgumentType.StringType stringType = arg.readEnumConstant(StringArgumentType.StringType.class);
        return new Properties(stringType);
    }

    @Override
    public void writeJson(Properties arg, JsonObject jsonObject) {
        jsonObject.addProperty("type", switch (arg.type) {
            default -> throw new MatchException(null, null);
            case StringArgumentType.StringType.SINGLE_WORD -> "word";
            case StringArgumentType.StringType.QUOTABLE_PHRASE -> "phrase";
            case StringArgumentType.StringType.GREEDY_PHRASE -> "greedy";
        });
    }

    @Override
    public Properties getArgumentTypeProperties(StringArgumentType stringArgumentType) {
        return new Properties(stringArgumentType.getType());
    }

    @Override
    public /* synthetic */ ArgumentSerializer.ArgumentTypeProperties fromPacket(PacketByteBuf buf) {
        return this.fromPacket(buf);
    }

    public final class Properties
    implements ArgumentSerializer.ArgumentTypeProperties<StringArgumentType> {
        final StringArgumentType.StringType type;

        public Properties(StringArgumentType.StringType type) {
            this.type = type;
        }

        @Override
        public StringArgumentType createType(CommandRegistryAccess arg) {
            return switch (this.type) {
                default -> throw new MatchException(null, null);
                case StringArgumentType.StringType.SINGLE_WORD -> StringArgumentType.word();
                case StringArgumentType.StringType.QUOTABLE_PHRASE -> StringArgumentType.string();
                case StringArgumentType.StringType.GREEDY_PHRASE -> StringArgumentType.greedyString();
            };
        }

        @Override
        public ArgumentSerializer<StringArgumentType, ?> getSerializer() {
            return StringArgumentSerializer.this;
        }

        @Override
        public /* synthetic */ ArgumentType createType(CommandRegistryAccess commandRegistryAccess) {
            return this.createType(commandRegistryAccess);
        }
    }
}

