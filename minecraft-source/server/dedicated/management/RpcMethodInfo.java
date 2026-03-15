/*
 * External method calls:
 *   Lnet/minecraft/server/dedicated/management/RpcRequestParameter;createCodec()Lcom/mojang/serialization/MapCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/dedicated/management/RpcMethodInfo;createParamsCodec()Lcom/mojang/serialization/Codec;
 */
package net.minecraft.server.dedicated.management;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import net.minecraft.server.dedicated.management.RpcRequestParameter;
import net.minecraft.server.dedicated.management.RpcResponseResult;
import net.minecraft.util.Identifier;
import org.jspecify.annotations.Nullable;

public record RpcMethodInfo<Params, Result>(String description, Optional<RpcRequestParameter<Params>> params, Optional<RpcResponseResult<Result>> result) {
    public RpcMethodInfo(String description, @Nullable RpcRequestParameter<Params> param, @Nullable RpcResponseResult<Result> result) {
        this(description, Optional.ofNullable(param), Optional.ofNullable(result));
    }

    private static <Params> Optional<RpcRequestParameter<Params>> getParameter(List<RpcRequestParameter<Params>> params) {
        return params.isEmpty() ? Optional.empty() : Optional.of(params.getFirst());
    }

    private static <Params> List<RpcRequestParameter<Params>> toParameterList(Optional<RpcRequestParameter<Params>> param) {
        if (param.isPresent()) {
            return List.of(param.get());
        }
        return List.of();
    }

    private static <Params> Codec<Optional<RpcRequestParameter<Params>>> createParamsCodec() {
        return RpcRequestParameter.createCodec().codec().listOf().xmap(RpcMethodInfo::getParameter, RpcMethodInfo::toParameterList);
    }

    static <Params, Result> MapCodec<RpcMethodInfo<Params, Result>> createCodec() {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Codec.STRING.fieldOf("description")).forGetter(RpcMethodInfo::description), ((MapCodec)RpcMethodInfo.createParamsCodec().fieldOf("params")).forGetter(RpcMethodInfo::params), RpcResponseResult.getCodec().optionalFieldOf("result").forGetter(RpcMethodInfo::result)).apply((Applicative<RpcMethodInfo, ?>)instance, RpcMethodInfo::new));
    }

    public Entry<Params, Result> toEntry(Identifier name) {
        return new Entry(name, this);
    }

    public record Entry<Params, Result>(Identifier name, RpcMethodInfo<Params, Result> contents) {
        public static final Codec<Entry<?, ?>> CODEC = Entry.createCodec();

        public static <Params, Result> Codec<Entry<Params, Result>> createCodec() {
            return RecordCodecBuilder.create(instance -> instance.group(((MapCodec)Identifier.CODEC.fieldOf("name")).forGetter(Entry::name), RpcMethodInfo.createCodec().forGetter(Entry::contents)).apply((Applicative<Entry, ?>)instance, Entry::new));
        }
    }
}

