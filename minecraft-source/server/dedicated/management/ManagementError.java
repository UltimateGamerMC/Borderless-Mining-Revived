/*
 * External method calls:
 *   Lnet/minecraft/server/dedicated/management/JsonRpc;encodeError(Lcom/google/gson/JsonElement;Ljava/lang/String;ILjava/lang/String;)Lcom/google/gson/JsonObject;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/dedicated/management/ManagementError;method_73644()[Lnet/minecraft/server/dedicated/management/ManagementError;
 */
package net.minecraft.server.dedicated.management;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import net.minecraft.server.dedicated.management.JsonRpc;
import org.jspecify.annotations.Nullable;

public enum ManagementError {
    PARSE_ERROR(-32700, "Parse error"),
    INVALID_REQUEST(-32600, "Invalid Request"),
    METHOD_NOT_FOUND(-32601, "Method not found"),
    INVALID_PARAMS(-32602, "Invalid params"),
    INTERNAL_ERROR(-32603, "Internal error");

    private final int code;
    private final String message;

    private ManagementError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public JsonObject encode(@Nullable String data) {
        return JsonRpc.encodeError(JsonNull.INSTANCE, this.message, this.code, data);
    }

    public JsonObject encode(JsonElement json) {
        return JsonRpc.encodeError(json, this.message, this.code, null);
    }

    public JsonObject encode(JsonElement json, String data) {
        return JsonRpc.encodeError(json, this.message, this.code, data);
    }
}

