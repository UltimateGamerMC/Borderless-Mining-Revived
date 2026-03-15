package net.minecraft.server.dedicated.management;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.dedicated.management.schema.RpcSchema;

public record RpcResponseResult<Result>(String name, RpcSchema<Result> schema) {
    public static <Result> Codec<RpcResponseResult<Result>> getCodec() {
        return RecordCodecBuilder.create(instance -> instance.group(((MapCodec)Codec.STRING.fieldOf("name")).forGetter(RpcResponseResult::name), ((MapCodec)RpcSchema.getCodec().fieldOf("schema")).forGetter(RpcResponseResult::schema)).apply((Applicative<RpcResponseResult, ?>)instance, RpcResponseResult::new));
    }
}

