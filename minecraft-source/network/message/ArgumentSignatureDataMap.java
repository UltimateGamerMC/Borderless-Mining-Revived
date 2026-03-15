/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;readCollection(Ljava/util/function/IntFunction;Lnet/minecraft/network/codec/PacketDecoder;)Ljava/util/Collection;
 *   Lnet/minecraft/network/PacketByteBuf;writeCollection(Ljava/util/Collection;Lnet/minecraft/network/codec/PacketEncoder;)V
 *   Lnet/minecraft/command/argument/SignedArgumentList;arguments()Ljava/util/List;
 *   Lnet/minecraft/network/message/ArgumentSignatureDataMap$ArgumentSigner;sign(Ljava/lang/String;)Lnet/minecraft/network/message/MessageSignatureData;
 *   Lnet/minecraft/network/message/ArgumentSignatureDataMap$Entry;write(Lnet/minecraft/network/PacketByteBuf;)V
 */
package net.minecraft.network.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.command.argument.SignedArgumentList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.message.MessageSignatureData;
import org.jspecify.annotations.Nullable;

public record ArgumentSignatureDataMap(List<Entry> entries) {
    public static final ArgumentSignatureDataMap EMPTY = new ArgumentSignatureDataMap(List.of());
    private static final int MAX_ARGUMENTS = 8;
    private static final int MAX_ARGUMENT_NAME_LENGTH = 16;

    public ArgumentSignatureDataMap(PacketByteBuf buf) {
        this(buf.readCollection(PacketByteBuf.getMaxValidator(ArrayList::new, 8), Entry::new));
    }

    public void write(PacketByteBuf buf) {
        buf.writeCollection(this.entries, (buf2, entry) -> entry.write((PacketByteBuf)buf2));
    }

    public static ArgumentSignatureDataMap sign(SignedArgumentList<?> arguments, ArgumentSigner signer) {
        List<Entry> list = arguments.arguments().stream().map(argument -> {
            MessageSignatureData lv = signer.sign(argument.value());
            if (lv != null) {
                return new Entry(argument.getNodeName(), lv);
            }
            return null;
        }).filter(Objects::nonNull).toList();
        return new ArgumentSignatureDataMap(list);
    }

    @FunctionalInterface
    public static interface ArgumentSigner {
        public @Nullable MessageSignatureData sign(String var1);
    }

    public record Entry(String name, MessageSignatureData signature) {
        public Entry(PacketByteBuf buf) {
            this(buf.readString(16), MessageSignatureData.fromBuf(buf));
        }

        public void write(PacketByteBuf buf) {
            buf.writeString(this.name, 16);
            MessageSignatureData.write(buf, this.signature);
        }
    }
}

