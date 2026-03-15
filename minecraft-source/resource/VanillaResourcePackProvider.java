/*
 * External method calls:
 *   Lnet/minecraft/resource/DefaultResourcePack;forEachNamespacedPath(Lnet/minecraft/resource/ResourceType;Lnet/minecraft/util/Identifier;Ljava/util/function/Consumer;)V
 *   Lnet/minecraft/resource/FileResourcePackProvider;forEachProfile(Ljava/nio/file/Path;Lnet/minecraft/util/path/SymlinkFinder;Ljava/util/function/BiConsumer;)V
 *   Lnet/minecraft/registry/VersionedIdentifier;createVanilla(Ljava/lang/String;)Lnet/minecraft/registry/VersionedIdentifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/resource/VanillaResourcePackProvider;createDefault(Lnet/minecraft/resource/ResourcePack;)Lnet/minecraft/resource/ResourcePackProfile;
 *   Lnet/minecraft/resource/VanillaResourcePackProvider;forEachProfile(Ljava/util/function/Consumer;)V
 *   Lnet/minecraft/resource/VanillaResourcePackProvider;forEachProfile(Ljava/util/function/BiConsumer;)V
 *   Lnet/minecraft/resource/VanillaResourcePackProvider;create(Ljava/lang/String;Lnet/minecraft/resource/ResourcePackProfile$PackFactory;Lnet/minecraft/text/Text;)Lnet/minecraft/resource/ResourcePackProfile;
 *   Lnet/minecraft/resource/VanillaResourcePackProvider;forEachProfile(Ljava/nio/file/Path;Ljava/util/function/BiConsumer;)V
 */
package net.minecraft.resource;

import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.registry.VersionedIdentifier;
import net.minecraft.resource.DefaultResourcePack;
import net.minecraft.resource.FileResourcePackProvider;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourcePackInfo;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.path.SymlinkFinder;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public abstract class VanillaResourcePackProvider
implements ResourcePackProvider {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String VANILLA_KEY = "vanilla";
    public static final String TESTS_KEY = "tests";
    public static final VersionedIdentifier VANILLA_ID = VersionedIdentifier.createVanilla("core");
    private final ResourceType type;
    private final DefaultResourcePack resourcePack;
    private final Identifier id;
    private final SymlinkFinder symlinkFinder;

    public VanillaResourcePackProvider(ResourceType type, DefaultResourcePack resourcePack, Identifier id, SymlinkFinder symlinkFinder) {
        this.type = type;
        this.resourcePack = resourcePack;
        this.id = id;
        this.symlinkFinder = symlinkFinder;
    }

    @Override
    public void register(Consumer<ResourcePackProfile> profileAdder) {
        ResourcePackProfile lv = this.createDefault(this.resourcePack);
        if (lv != null) {
            profileAdder.accept(lv);
        }
        this.forEachProfile(profileAdder);
    }

    protected abstract @Nullable ResourcePackProfile createDefault(ResourcePack var1);

    protected abstract Text getDisplayName(String var1);

    public DefaultResourcePack getResourcePack() {
        return this.resourcePack;
    }

    /*
     * Issues handling annotations - annotations may be inaccurate
     */
    private void forEachProfile(Consumer<ResourcePackProfile> consumer) {
        HashMap<String, @Nullable Function> map = new HashMap<String, Function>();
        this.forEachProfile(map::put);
        map.forEach((id, packFactory) -> {
            ResourcePackProfile lv = (ResourcePackProfile)packFactory.apply(id);
            if (lv != null) {
                consumer.accept(lv);
            }
        });
    }

    protected void forEachProfile(BiConsumer<String, Function<String, ResourcePackProfile>> consumer) {
        this.resourcePack.forEachNamespacedPath(this.type, this.id, namespacedPath -> this.forEachProfile((Path)namespacedPath, consumer));
    }

    protected void forEachProfile(@Nullable Path namespacedPath, BiConsumer<String, Function<String, @Nullable ResourcePackProfile>> consumer) {
        if (namespacedPath != null && Files.isDirectory(namespacedPath, new LinkOption[0])) {
            try {
                FileResourcePackProvider.forEachProfile(namespacedPath, this.symlinkFinder, (profilePath, factory) -> consumer.accept(VanillaResourcePackProvider.getFileName(profilePath), id -> this.create((String)id, (ResourcePackProfile.PackFactory)factory, this.getDisplayName((String)id))));
            } catch (IOException iOException) {
                LOGGER.warn("Failed to discover packs in {}", (Object)namespacedPath, (Object)iOException);
            }
        }
    }

    private static String getFileName(Path path) {
        return StringUtils.removeEnd(path.getFileName().toString(), ".zip");
    }

    protected abstract @Nullable ResourcePackProfile create(String var1, ResourcePackProfile.PackFactory var2, Text var3);

    protected static ResourcePackProfile.PackFactory createPackFactory(final ResourcePack pack) {
        return new ResourcePackProfile.PackFactory(){

            @Override
            public ResourcePack open(ResourcePackInfo info) {
                return pack;
            }

            @Override
            public ResourcePack openWithOverlays(ResourcePackInfo info, ResourcePackProfile.Metadata metadata) {
                return pack;
            }
        };
    }
}

