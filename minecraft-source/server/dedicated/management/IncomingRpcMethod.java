package net.minecraft.server.dedicated.management;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import java.util.Locale;
import java.util.function.Function;
import net.minecraft.registry.Registry;
import net.minecraft.server.dedicated.management.RpcEncodingException;
import net.minecraft.server.dedicated.management.RpcException;
import net.minecraft.server.dedicated.management.RpcMethodInfo;
import net.minecraft.server.dedicated.management.RpcRequestParameter;
import net.minecraft.server.dedicated.management.RpcResponseResult;
import net.minecraft.server.dedicated.management.dispatch.ManagementHandlerDispatcher;
import net.minecraft.server.dedicated.management.network.ManagementConnectionId;
import net.minecraft.server.dedicated.management.schema.RpcSchema;
import net.minecraft.util.Identifier;
import org.jspecify.annotations.Nullable;

public interface IncomingRpcMethod<Params, Result> {
    public RpcMethodInfo<Params, Result> info();

    public Attributes attributes();

    public JsonElement handle(ManagementHandlerDispatcher var1, @Nullable JsonElement var2, ManagementConnectionId var3);

    public static <Result> Builder<Void, Result> createParameterlessBuilder(ParameterlessHandler<Result> handler) {
        return new Builder(handler);
    }

    public static <Params, Result> Builder<Params, Result> createParameterizedBuilder(ParameterizedHandler<Params, Result> handler) {
        return new Builder<Params, Result>(handler);
    }

    public static <Result> Builder<Void, Result> createParameterlessBuilder(Function<ManagementHandlerDispatcher, Result> handler) {
        return new Builder(handler);
    }

    public static class Builder<Params, Result> {
        private String description = "";
        private @Nullable RpcRequestParameter<Params> params;
        private @Nullable RpcResponseResult<Result> result;
        private boolean runOnMainThread = true;
        private boolean discoverable = true;
        private @Nullable ParameterlessHandler<Result> parameterlessHandler;
        private @Nullable ParameterizedHandler<Params, Result> parameterizedHandler;

        public Builder(ParameterlessHandler<Result> parameterlessHandler) {
            this.parameterlessHandler = parameterlessHandler;
        }

        public Builder(ParameterizedHandler<Params, Result> parameterizedHandler) {
            this.parameterizedHandler = parameterizedHandler;
        }

        public Builder(Function<ManagementHandlerDispatcher, Result> parameterlessHandler) {
            this.parameterlessHandler = (dispatcher, remote) -> parameterlessHandler.apply(dispatcher);
        }

        public Builder<Params, Result> description(String description) {
            this.description = description;
            return this;
        }

        public Builder<Params, Result> result(String name, RpcSchema<Result> schema) {
            this.result = new RpcResponseResult<Result>(name, schema.copy());
            return this;
        }

        public Builder<Params, Result> parameter(String name, RpcSchema<Params> schema) {
            this.params = new RpcRequestParameter<Params>(name, schema.copy());
            return this;
        }

        public Builder<Params, Result> noRequireMainThread() {
            this.runOnMainThread = false;
            return this;
        }

        public Builder<Params, Result> notDiscoverable() {
            this.discoverable = false;
            return this;
        }

        public IncomingRpcMethod<Params, Result> build() {
            if (this.result == null) {
                throw new IllegalStateException("No response defined");
            }
            Attributes lv = new Attributes(this.discoverable, this.runOnMainThread);
            RpcMethodInfo<Params, Result> lv2 = new RpcMethodInfo<Params, Result>(this.description, this.params, this.result);
            if (this.parameterlessHandler != null) {
                return new Parameterless<Params, Result>(lv2, lv, this.parameterlessHandler);
            }
            if (this.parameterizedHandler != null) {
                if (this.params == null) {
                    throw new IllegalStateException("No param schema defined");
                }
                return new Parameterized<Params, Result>(lv2, lv, this.parameterizedHandler);
            }
            throw new IllegalStateException("No method defined");
        }

        public IncomingRpcMethod<?, ?> buildAndRegisterVanilla(Registry<IncomingRpcMethod<?, ?>> registry, String path) {
            return this.buildAndRegister(registry, Identifier.ofVanilla(path));
        }

        private IncomingRpcMethod<?, ?> buildAndRegister(Registry<IncomingRpcMethod<?, ?>> registry, Identifier id) {
            return Registry.register(registry, id, this.build());
        }
    }

    @FunctionalInterface
    public static interface ParameterlessHandler<Result> {
        public Result apply(ManagementHandlerDispatcher var1, ManagementConnectionId var2);
    }

    @FunctionalInterface
    public static interface ParameterizedHandler<Params, Result> {
        public Result apply(ManagementHandlerDispatcher var1, Params var2, ManagementConnectionId var3);
    }

    public record Parameterized<Params, Result>(RpcMethodInfo<Params, Result> info, Attributes attributes, ParameterizedHandler<Params, Result> handler) implements IncomingRpcMethod<Params, Result>
    {
        @Override
        public JsonElement handle(ManagementHandlerDispatcher dispatcher, @Nullable JsonElement parameters, ManagementConnectionId remote) {
            JsonElement jsonElement3;
            if (parameters == null || !parameters.isJsonArray() && !parameters.isJsonObject()) {
                throw new RpcException("Expected params as array or named");
            }
            if (this.info.params().isEmpty()) {
                throw new IllegalArgumentException("Method defined as having parameters without describing them");
            }
            if (parameters.isJsonObject()) {
                String string = this.info.params().get().name();
                JsonElement jsonElement2 = parameters.getAsJsonObject().get(string);
                if (jsonElement2 == null) {
                    throw new RpcException(String.format(Locale.ROOT, "Params passed by-name, but expected param [%s] does not exist", string));
                }
                jsonElement3 = jsonElement2;
            } else {
                JsonArray jsonArray = parameters.getAsJsonArray();
                if (jsonArray.isEmpty() || jsonArray.size() > 1) {
                    throw new RpcException("Expected exactly one element in the params array");
                }
                jsonElement3 = jsonArray.get(0);
            }
            Object object = this.info.params().get().schema().codec().parse(JsonOps.INSTANCE, jsonElement3).getOrThrow(RpcException::new);
            Result object2 = this.handler.apply(dispatcher, object, remote);
            if (this.info.result().isEmpty()) {
                throw new IllegalStateException("No result codec defined");
            }
            return this.info.result().get().schema().codec().encodeStart(JsonOps.INSTANCE, object2).getOrThrow(RpcEncodingException::new);
        }
    }

    public record Parameterless<Params, Result>(RpcMethodInfo<Params, Result> info, Attributes attributes, ParameterlessHandler<Result> handler) implements IncomingRpcMethod<Params, Result>
    {
        @Override
        public JsonElement handle(ManagementHandlerDispatcher dispatcher, @Nullable JsonElement parameters, ManagementConnectionId remote) {
            if (!(parameters == null || parameters.isJsonArray() && parameters.getAsJsonArray().isEmpty())) {
                throw new RpcException("Expected no params, or an empty array");
            }
            if (this.info.params().isPresent()) {
                throw new IllegalArgumentException("Parameterless method unexpectedly has parameter description");
            }
            Result object = this.handler.apply(dispatcher, remote);
            if (this.info.result().isEmpty()) {
                throw new IllegalStateException("No result codec defined");
            }
            return this.info.result().get().schema().codec().encodeStart(JsonOps.INSTANCE, object).getOrThrow(RpcException::new);
        }
    }

    public record Attributes(boolean runOnMainThread, boolean discoverable) {
    }
}

