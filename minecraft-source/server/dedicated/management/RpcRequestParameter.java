package net.minecraft.server.dedicated.management;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.dedicated.management.schema.RpcSchema;

public record RpcRequestParameter<Param>(String name, RpcSchema<Param> schema, boolean required) {
    public RpcRequestParameter(String name, RpcSchema<Param> schema) {
        this(name, schema, true);
    }

    public static <Param> MapCodec<RpcRequestParameter<Param>> createCodec() {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Codec.STRING.fieldOf("name")).forGetter(RpcRequestParameter::name), ((MapCodec)RpcSchema.getCodec().fieldOf("schema")).forGetter(RpcRequestParameter::schema), ((MapCodec)Codec.BOOL.fieldOf("required")).forGetter(RpcRequestParameter::required)).apply((Applicative<RpcRequestParameter, ?>)instance, RpcRequestParameter::new));
    }
}

