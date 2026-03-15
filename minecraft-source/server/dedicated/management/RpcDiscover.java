/*
 * External method calls:
 *   Lnet/minecraft/registry/Registry;streamEntries()Ljava/util/stream/Stream;
 *   Lnet/minecraft/server/dedicated/management/schema/RpcSchemaEntry;name()Ljava/lang/String;
 *   Lnet/minecraft/server/dedicated/management/schema/RpcSchemaEntry;schema()Lnet/minecraft/server/dedicated/management/schema/RpcSchema;
 *   Lnet/minecraft/server/dedicated/management/OutgoingRpcMethod;attributes()Lnet/minecraft/server/dedicated/management/OutgoingRpcMethod$Attributes;
 *   Lnet/minecraft/server/dedicated/management/OutgoingRpcMethod;info()Lnet/minecraft/server/dedicated/management/RpcMethodInfo;
 *   Lnet/minecraft/registry/entry/RegistryEntry$Reference;registryKey()Lnet/minecraft/registry/RegistryKey;
 *   Lnet/minecraft/server/dedicated/management/RpcMethodInfo;toEntry(Lnet/minecraft/util/Identifier;)Lnet/minecraft/server/dedicated/management/RpcMethodInfo$Entry;
 *   Lnet/minecraft/server/dedicated/management/IncomingRpcMethod;attributes()Lnet/minecraft/server/dedicated/management/IncomingRpcMethod$Attributes;
 *   Lnet/minecraft/server/dedicated/management/IncomingRpcMethod;info()Lnet/minecraft/server/dedicated/management/RpcMethodInfo;
 */
package net.minecraft.server.dedicated.management;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.registry.Registries;
import net.minecraft.server.dedicated.management.IncomingRpcMethod;
import net.minecraft.server.dedicated.management.OutgoingRpcMethod;
import net.minecraft.server.dedicated.management.RpcMethodInfo;
import net.minecraft.server.dedicated.management.schema.RpcSchema;
import net.minecraft.server.dedicated.management.schema.RpcSchemaEntry;

public class RpcDiscover {
    public static Document handleRpcDiscover(List<RpcSchemaEntry<?>> entries) {
        ArrayList list2 = new ArrayList(Registries.INCOMING_RPC_METHOD.size() + Registries.OUTGOING_RPC_METHOD.size());
        Registries.INCOMING_RPC_METHOD.streamEntries().forEach(entry -> {
            if (((IncomingRpcMethod)entry.value()).attributes().discoverable()) {
                list2.add(((IncomingRpcMethod)entry.value()).info().toEntry(entry.registryKey().getValue()));
            }
        });
        Registries.OUTGOING_RPC_METHOD.streamEntries().forEach(entry -> {
            if (((OutgoingRpcMethod)entry.value()).attributes().discoverable()) {
                list2.add(((OutgoingRpcMethod)entry.value()).info().toEntry(entry.registryKey().getValue()));
            }
        });
        HashMap map = new HashMap();
        for (RpcSchemaEntry<?> lv : entries) {
            map.put(lv.name(), lv.schema().copy());
        }
        Info lv2 = new Info("Minecraft Server JSON-RPC", "2.0.0");
        return new Document("1.3.2", lv2, list2, new Components(map));
    }

    public record Info(String title, String version) {
        public static final MapCodec<Info> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Codec.STRING.fieldOf("title")).forGetter(Info::title), ((MapCodec)Codec.STRING.fieldOf("version")).forGetter(Info::version)).apply((Applicative<Info, ?>)instance, Info::new));
    }

    public record Document(String jsonRpcProtocolVersion, Info discoverInfo, List<RpcMethodInfo.Entry<?, ?>> methods, Components components) {
        public static final MapCodec<Document> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Codec.STRING.fieldOf("openrpc")).forGetter(Document::jsonRpcProtocolVersion), ((MapCodec)Info.CODEC.codec().fieldOf("info")).forGetter(Document::discoverInfo), ((MapCodec)Codec.list(RpcMethodInfo.Entry.CODEC).fieldOf("methods")).forGetter(Document::methods), ((MapCodec)Components.CODEC.codec().fieldOf("components")).forGetter(Document::components)).apply((Applicative<Document, ?>)instance, Document::new));
    }

    public record Components(Map<String, RpcSchema<?>> schemas) {
        public static final MapCodec<Components> CODEC = Components.createCodec();

        private static MapCodec<Components> createCodec() {
            return RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Codec.unboundedMap(Codec.STRING, RpcSchema.CODEC).fieldOf("schemas")).forGetter(Components::schemas)).apply((Applicative<Components, ?>)instance, Components::new));
        }
    }
}

