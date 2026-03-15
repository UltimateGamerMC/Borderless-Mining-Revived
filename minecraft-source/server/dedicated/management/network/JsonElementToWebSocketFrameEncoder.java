/*
 * Internal private/static methods:
 *   Lnet/minecraft/server/dedicated/management/network/JsonElementToWebSocketFrameEncoder;encode(Lio/netty/channel/ChannelHandlerContext;Lcom/google/gson/JsonElement;Ljava/util/List;)V
 */
package net.minecraft.server.dedicated.management.network;

import com.google.gson.JsonElement;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import java.util.List;

public class JsonElementToWebSocketFrameEncoder
extends MessageToMessageEncoder<JsonElement> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, JsonElement jsonElement, List<Object> list) {
        list.add(new TextWebSocketFrame(jsonElement.toString()));
    }

    @Override
    protected /* synthetic */ void encode(ChannelHandlerContext context, Object element, List out) throws Exception {
        this.encode(context, (JsonElement)element, (List<Object>)out);
    }
}

