package net.minecraft.network.handler;

import io.netty.handler.codec.DecoderException;
import net.minecraft.network.handler.PacketCodecDispatcher;
import net.minecraft.network.handler.PacketException;

public class PacketDecoderException
extends DecoderException
implements PacketCodecDispatcher.UndecoratedException,
PacketException {
    public PacketDecoderException(String message) {
        super(message);
    }

    public PacketDecoderException(Throwable cause) {
        super(cause);
    }
}

