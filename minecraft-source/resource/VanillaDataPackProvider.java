/*
 * External method calls:
 *   Lnet/minecraft/registry/VersionedIdentifier;createVanilla(Ljava/lang/String;)Lnet/minecraft/registry/VersionedIdentifier;
 *   Lnet/minecraft/resource/DefaultResourcePackBuilder;withMetadataMap(Lnet/minecraft/resource/metadata/ResourceMetadataMap;)Lnet/minecraft/resource/DefaultResourcePackBuilder;
 *   Lnet/minecraft/resource/DefaultResourcePackBuilder;withNamespaces([Ljava/lang/String;)Lnet/minecraft/resource/DefaultResourcePackBuilder;
 *   Lnet/minecraft/resource/DefaultResourcePackBuilder;runCallback()Lnet/minecraft/resource/DefaultResourcePackBuilder;
 *   Lnet/minecraft/resource/DefaultResourcePackBuilder;withDefaultPaths()Lnet/minecraft/resource/DefaultResourcePackBuilder;
 *   Lnet/minecraft/resource/DefaultResourcePackBuilder;build(Lnet/minecraft/resource/ResourcePackInfo;)Lnet/minecraft/resource/DefaultResourcePack;
 *   Lnet/minecraft/text/Text;literal(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/resource/ResourcePackProfile;create(Lnet/minecraft/resource/ResourcePackInfo;Lnet/minecraft/resource/ResourcePackProfile$PackFactory;Lnet/minecraft/resource/ResourceType;Lnet/minecraft/resource/ResourcePackPosition;)Lnet/minecraft/resource/ResourcePackProfile;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/GameVersion;packVersion(Lnet/minecraft/resource/ResourceType;)Lnet/minecraft/resource/PackVersion;
 *   Lnet/minecraft/resource/PackVersion;majorRange()Lnet/minecraft/util/dynamic/Range;
 *   Lnet/minecraft/resource/metadata/ResourceMetadataMap;of(Lnet/minecraft/resource/metadata/ResourceMetadataSerializer;Ljava/lang/Object;Lnet/minecraft/resource/metadata/ResourceMetadataSerializer;Ljava/lang/Object;)Lnet/minecraft/resource/metadata/ResourceMetadataMap;
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/resource/VanillaDataPackProvider;createDefaultPack()Lnet/minecraft/resource/DefaultResourcePack;
 *   Lnet/minecraft/resource/VanillaDataPackProvider;createPackFactory(Lnet/minecraft/resource/ResourcePack;)Lnet/minecraft/resource/ResourcePackProfile$PackFactory;
 *   Lnet/minecraft/resource/VanillaDataPackProvider;createInfo(Ljava/lang/String;Lnet/minecraft/text/Text;)Lnet/minecraft/resource/ResourcePackInfo;
 *   Lnet/minecraft/resource/VanillaDataPackProvider;createManager(Ljava/nio/file/Path;Lnet/minecraft/util/path/SymlinkFinder;)Lnet/minecraft/resource/ResourcePackManager;
 */
package net.minecraft.resource;

import com.google.common.annotations.VisibleForTesting;
import java.nio.file.Path;
import java.util.Optional;
import net.minecraft.SharedConstants;
import net.minecraft.registry.VersionedIdentifier;
import net.minecraft.resource.DefaultResourcePack;
import net.minecraft.resource.DefaultResourcePackBuilder;
import net.minecraft.resource.FileResourcePackProvider;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourcePackInfo;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackPosition;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.VanillaResourcePackProvider;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.metadata.PackFeatureSetMetadata;
import net.minecraft.resource.metadata.PackResourceMetadata;
import net.minecraft.resource.metadata.ResourceMetadataMap;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.path.SymlinkFinder;
import net.minecraft.world.level.storage.LevelStorage;
import org.jspecify.annotations.Nullable;

public class VanillaDataPackProvider
extends VanillaResourcePackProvider {
    private static final PackResourceMetadata METADATA = new PackResourceMetadata(Text.translatable("dataPack.vanilla.description"), SharedConstants.getGameVersion().packVersion(ResourceType.SERVER_DATA).majorRange());
    private static final PackFeatureSetMetadata FEATURE_FLAGS = new PackFeatureSetMetadata(FeatureFlags.DEFAULT_ENABLED_FEATURES);
    private static final ResourceMetadataMap METADATA_MAP = ResourceMetadataMap.of(PackResourceMetadata.SERVER_DATA_SERIALIZER, METADATA, PackFeatureSetMetadata.SERIALIZER, FEATURE_FLAGS);
    private static final ResourcePackInfo INFO = new ResourcePackInfo("vanilla", Text.translatable("dataPack.vanilla.name"), ResourcePackSource.BUILTIN, Optional.of(VANILLA_ID));
    private static final ResourcePackPosition BOTTOM_POSITION = new ResourcePackPosition(false, ResourcePackProfile.InsertionPosition.BOTTOM, false);
    private static final ResourcePackPosition TOP_POSITION = new ResourcePackPosition(false, ResourcePackProfile.InsertionPosition.TOP, false);
    private static final Identifier ID = Identifier.ofVanilla("datapacks");

    public VanillaDataPackProvider(SymlinkFinder symlinkFinder) {
        super(ResourceType.SERVER_DATA, VanillaDataPackProvider.createDefaultPack(), ID, symlinkFinder);
    }

    private static ResourcePackInfo createInfo(String id, Text title) {
        return new ResourcePackInfo(id, title, ResourcePackSource.FEATURE, Optional.of(VersionedIdentifier.createVanilla(id)));
    }

    @VisibleForTesting
    public static DefaultResourcePack createDefaultPack() {
        return new DefaultResourcePackBuilder().withMetadataMap(METADATA_MAP).withNamespaces("minecraft").runCallback().withDefaultPaths().build(INFO);
    }

    @Override
    protected Text getDisplayName(String id) {
        return Text.literal(id);
    }

    @Override
    protected @Nullable ResourcePackProfile createDefault(ResourcePack pack) {
        return ResourcePackProfile.create(INFO, VanillaDataPackProvider.createPackFactory(pack), ResourceType.SERVER_DATA, BOTTOM_POSITION);
    }

    @Override
    protected @Nullable ResourcePackProfile create(String fileName, ResourcePackProfile.PackFactory packFactory, Text displayName) {
        return ResourcePackProfile.create(VanillaDataPackProvider.createInfo(fileName, displayName), packFactory, ResourceType.SERVER_DATA, TOP_POSITION);
    }

    public static ResourcePackManager createManager(Path dataPacksPath, SymlinkFinder symlinkFinder) {
        return new ResourcePackManager(new VanillaDataPackProvider(symlinkFinder), new FileResourcePackProvider(dataPacksPath, ResourceType.SERVER_DATA, ResourcePackSource.WORLD, symlinkFinder));
    }

    public static ResourcePackManager createClientManager() {
        return new ResourcePackManager(new VanillaDataPackProvider(new SymlinkFinder(path -> true)));
    }

    public static ResourcePackManager createManager(LevelStorage.Session session) {
        return VanillaDataPackProvider.createManager(session.getDirectory(WorldSavePath.DATAPACKS), session.getLevelStorage().getSymlinkFinder());
    }
}

