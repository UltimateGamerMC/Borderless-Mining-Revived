/*
 * External method calls:
 *   Lnet/minecraft/server/dedicated/management/schema/RpcSchema;codec()Lcom/mojang/serialization/Codec;
 *   Lnet/minecraft/server/dedicated/management/schema/RpcSchema;ofReference(Ljava/net/URI;Lcom/mojang/serialization/Codec;)Lnet/minecraft/server/dedicated/management/schema/RpcSchema;
 *   Lnet/minecraft/server/dedicated/management/schema/RpcSchema;ofArray(Lnet/minecraft/server/dedicated/management/schema/RpcSchema;Lcom/mojang/serialization/Codec;)Lnet/minecraft/server/dedicated/management/schema/RpcSchema;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/dedicated/management/schema/RpcSchemaEntry;ref()Lnet/minecraft/server/dedicated/management/schema/RpcSchema;
 */
package net.minecraft.server.dedicated.management.schema;

import java.net.URI;
import java.util.List;
import net.minecraft.server.dedicated.management.schema.RpcSchema;

public record RpcSchemaEntry<T>(String name, URI reference, RpcSchema<T> schema) {
    public RpcSchema<T> ref() {
        return RpcSchema.ofReference(this.reference, this.schema.codec());
    }

    public RpcSchema<List<T>> array() {
        return RpcSchema.ofArray(this.ref(), this.schema.codec());
    }
}

