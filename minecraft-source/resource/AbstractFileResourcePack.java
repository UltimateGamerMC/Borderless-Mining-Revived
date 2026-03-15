/*
 * External method calls:
 *   Lnet/minecraft/util/JsonHelper;deserialize(Ljava/io/Reader;)Lcom/google/gson/JsonObject;
 *   Lnet/minecraft/resource/ResourcePackInfo;id()Ljava/lang/String;
 *   Lnet/minecraft/resource/metadata/ResourceMetadataSerializer;name()Ljava/lang/String;
 *   Lnet/minecraft/resource/metadata/ResourceMetadataSerializer;codec()Lcom/mojang/serialization/Codec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/resource/AbstractFileResourcePack;openRoot([Ljava/lang/String;)Lnet/minecraft/resource/InputSupplier;
 *   Lnet/minecraft/resource/AbstractFileResourcePack;parseMetadata(Lnet/minecraft/resource/metadata/ResourceMetadataSerializer;Ljava/io/InputStream;Lnet/minecraft/resource/ResourcePackInfo;)Ljava/lang/Object;
 */
package net.minecraft.resource;

import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import net.minecraft.resource.InputSupplier;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourcePackInfo;
import net.minecraft.resource.metadata.ResourceMetadataSerializer;
import net.minecraft.util.JsonHelper;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public abstract class AbstractFileResourcePack
implements ResourcePack {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final ResourcePackInfo info;

    protected AbstractFileResourcePack(ResourcePackInfo info) {
        this.info = info;
    }

    @Override
    public <T> @Nullable T parseMetadata(ResourceMetadataSerializer<T> metadataSerializer) throws IOException {
        InputSupplier<InputStream> lv = this.openRoot("pack.mcmeta");
        if (lv == null) {
            return null;
        }
        try (InputStream inputStream = lv.get();){
            T t = AbstractFileResourcePack.parseMetadata(metadataSerializer, inputStream, this.info);
            return t;
        }
    }

    public static <T> @Nullable T parseMetadata(ResourceMetadataSerializer<T> arg, InputStream inputStream, ResourcePackInfo arg2) {
        JsonObject jsonObject;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));){
            jsonObject = JsonHelper.deserialize(bufferedReader);
        } catch (Exception exception) {
            LOGGER.error("Couldn't load {} {} metadata: {}", arg2.id(), arg.name(), exception.getMessage());
            return null;
        }
        if (!jsonObject.has(arg.name())) {
            return null;
        }
        return arg.codec().parse(JsonOps.INSTANCE, jsonObject.get(arg.name())).ifError(error -> LOGGER.error("Couldn't load {} {} metadata: {}", arg2.id(), arg.name(), error.message())).result().orElse(null);
    }

    @Override
    public ResourcePackInfo getInfo() {
        return this.info;
    }
}

