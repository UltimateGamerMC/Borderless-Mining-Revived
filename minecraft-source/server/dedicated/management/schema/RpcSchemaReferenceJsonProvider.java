/*
 * External method calls:
 *   Lnet/minecraft/data/DataOutput;resolvePath(Lnet/minecraft/data/DataOutput$OutputType;)Ljava/nio/file/Path;
 *   Lnet/minecraft/server/dedicated/management/RpcDiscover;handleRpcDiscover(Ljava/util/List;)Lnet/minecraft/server/dedicated/management/RpcDiscover$Document;
 *   Lnet/minecraft/data/DataProvider;writeToPath(Lnet/minecraft/data/DataWriter;Lcom/google/gson/JsonElement;Ljava/nio/file/Path;)Ljava/util/concurrent/CompletableFuture;
 */
package net.minecraft.server.dedicated.management.schema;

import com.mojang.serialization.JsonOps;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.server.dedicated.management.RpcDiscover;
import net.minecraft.server.dedicated.management.schema.RpcSchema;

public class RpcSchemaReferenceJsonProvider
implements DataProvider {
    private final Path path;

    public RpcSchemaReferenceJsonProvider(DataOutput output) {
        this.path = output.resolvePath(DataOutput.OutputType.REPORTS).resolve("json-rpc-api-schema.json");
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        RpcDiscover.Document lv = RpcDiscover.handleRpcDiscover(RpcSchema.getRegisteredSchemas());
        return DataProvider.writeToPath(writer, RpcDiscover.Document.CODEC.codec().encodeStart(JsonOps.INSTANCE, lv).getOrThrow(), this.path);
    }

    @Override
    public String getName() {
        return "Json RPC API schema";
    }
}

