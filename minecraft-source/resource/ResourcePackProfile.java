/*
 * External method calls:
 *   Lnet/minecraft/GameVersion;packVersion(Lnet/minecraft/resource/ResourceType;)Lnet/minecraft/resource/PackVersion;
 *   Lnet/minecraft/resource/ResourcePackProfile$PackFactory;open(Lnet/minecraft/resource/ResourcePackInfo;)Lnet/minecraft/resource/ResourcePack;
 *   Lnet/minecraft/resource/ResourcePack;parseMetadata(Lnet/minecraft/resource/metadata/ResourceMetadataSerializer;)Ljava/lang/Object;
 *   Lnet/minecraft/resource/ResourcePackInfo;id()Ljava/lang/String;
 *   Lnet/minecraft/resource/metadata/PackFeatureSetMetadata;flags()Lnet/minecraft/resource/featuretoggle/FeatureSet;
 *   Lnet/minecraft/resource/featuretoggle/FeatureSet;empty()Lnet/minecraft/resource/featuretoggle/FeatureSet;
 *   Lnet/minecraft/resource/metadata/PackResourceMetadata;supportedFormats()Lnet/minecraft/util/dynamic/Range;
 *   Lnet/minecraft/resource/ResourcePackCompatibility;from(Lnet/minecraft/util/dynamic/Range;Lnet/minecraft/resource/PackVersion;)Lnet/minecraft/resource/ResourcePackCompatibility;
 *   Lnet/minecraft/resource/metadata/PackResourceMetadata;description()Lnet/minecraft/text/Text;
 *   Lnet/minecraft/resource/ResourcePackInfo;title()Lnet/minecraft/text/Text;
 *   Lnet/minecraft/resource/ResourcePackProfile$Metadata;description()Lnet/minecraft/text/Text;
 *   Lnet/minecraft/resource/ResourcePackProfile$Metadata;compatibility()Lnet/minecraft/resource/ResourcePackCompatibility;
 *   Lnet/minecraft/resource/ResourcePackProfile$Metadata;requestedFeatures()Lnet/minecraft/resource/featuretoggle/FeatureSet;
 *   Lnet/minecraft/resource/ResourcePackProfile$PackFactory;openWithOverlays(Lnet/minecraft/resource/ResourcePackInfo;Lnet/minecraft/resource/ResourcePackProfile$Metadata;)Lnet/minecraft/resource/ResourcePack;
 *   Lnet/minecraft/resource/ResourcePackPosition;defaultPosition()Lnet/minecraft/resource/ResourcePackProfile$InsertionPosition;
 *   Lnet/minecraft/resource/ResourcePackInfo;source()Lnet/minecraft/resource/ResourcePackSource;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/resource/ResourcePackProfile;loadMetadata(Lnet/minecraft/resource/ResourcePackInfo;Lnet/minecraft/resource/ResourcePackProfile$PackFactory;Lnet/minecraft/resource/PackVersion;Lnet/minecraft/resource/ResourceType;)Lnet/minecraft/resource/ResourcePackProfile$Metadata;
 */
package net.minecraft.resource;

import com.mojang.logging.LogUtils;
import java.util.List;
import java.util.function.Function;
import net.minecraft.SharedConstants;
import net.minecraft.resource.PackVersion;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourcePackCompatibility;
import net.minecraft.resource.ResourcePackInfo;
import net.minecraft.resource.ResourcePackPosition;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.resource.metadata.PackFeatureSetMetadata;
import net.minecraft.resource.metadata.PackOverlaysMetadata;
import net.minecraft.resource.metadata.PackResourceMetadata;
import net.minecraft.text.Text;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class ResourcePackProfile {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final ResourcePackInfo info;
    private final PackFactory packFactory;
    private final Metadata metaData;
    private final ResourcePackPosition position;

    public static @Nullable ResourcePackProfile create(ResourcePackInfo info, PackFactory packFactory, ResourceType type, ResourcePackPosition position) {
        PackVersion lv = SharedConstants.getGameVersion().packVersion(type);
        Metadata lv2 = ResourcePackProfile.loadMetadata(info, packFactory, lv, type);
        return lv2 != null ? new ResourcePackProfile(info, packFactory, lv2, position) : null;
    }

    public ResourcePackProfile(ResourcePackInfo info, PackFactory packFactory, Metadata metaData, ResourcePackPosition position) {
        this.info = info;
        this.packFactory = packFactory;
        this.metaData = metaData;
        this.position = position;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static @Nullable Metadata loadMetadata(ResourcePackInfo info, PackFactory packFactory, PackVersion version, ResourceType type) {
        try (ResourcePack lv = packFactory.open(info);){
            PackResourceMetadata lv2 = lv.parseMetadata(PackResourceMetadata.getSerializerFor(type));
            if (lv2 == null) {
                lv2 = lv.parseMetadata(PackResourceMetadata.DESCRIPTION_SERIALIZER);
            }
            if (lv2 == null) {
                LOGGER.warn("Missing metadata in pack {}", (Object)info.id());
                Metadata metadata = null;
                return metadata;
            }
            PackFeatureSetMetadata lv3 = lv.parseMetadata(PackFeatureSetMetadata.SERIALIZER);
            FeatureSet lv4 = lv3 != null ? lv3.flags() : FeatureSet.empty();
            ResourcePackCompatibility lv5 = ResourcePackCompatibility.from(lv2.supportedFormats(), version);
            PackOverlaysMetadata lv6 = lv.parseMetadata(PackOverlaysMetadata.getSerializerFor(type));
            List<String> list = lv6 != null ? lv6.getAppliedOverlays(version) : List.of();
            Metadata metadata = new Metadata(lv2.description(), lv5, lv4, list);
            return metadata;
        } catch (Exception exception) {
            LOGGER.warn("Failed to read pack {} metadata", (Object)info.id(), (Object)exception);
            return null;
        }
    }

    public ResourcePackInfo getInfo() {
        return this.info;
    }

    public Text getDisplayName() {
        return this.info.title();
    }

    public Text getDescription() {
        return this.metaData.description();
    }

    public Text getInformationText(boolean enabled) {
        return this.info.getInformationText(enabled, this.metaData.description);
    }

    public ResourcePackCompatibility getCompatibility() {
        return this.metaData.compatibility();
    }

    public FeatureSet getRequestedFeatures() {
        return this.metaData.requestedFeatures();
    }

    public ResourcePack createResourcePack() {
        return this.packFactory.openWithOverlays(this.info, this.metaData);
    }

    public String getId() {
        return this.info.id();
    }

    public ResourcePackPosition getPosition() {
        return this.position;
    }

    public boolean isRequired() {
        return this.position.required();
    }

    public boolean isPinned() {
        return this.position.fixedPosition();
    }

    public InsertionPosition getInitialPosition() {
        return this.position.defaultPosition();
    }

    public ResourcePackSource getSource() {
        return this.info.source();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResourcePackProfile)) {
            return false;
        }
        ResourcePackProfile lv = (ResourcePackProfile)o;
        return this.info.equals(lv.info);
    }

    public int hashCode() {
        return this.info.hashCode();
    }

    public static interface PackFactory {
        public ResourcePack open(ResourcePackInfo var1);

        public ResourcePack openWithOverlays(ResourcePackInfo var1, Metadata var2);
    }

    public record Metadata(Text description, ResourcePackCompatibility compatibility, FeatureSet requestedFeatures, List<String> overlays) {
    }

    public static enum InsertionPosition {
        TOP,
        BOTTOM;


        public <T> int insert(List<T> items, T item, Function<T, ResourcePackPosition> profileGetter, boolean listInverted) {
            ResourcePackPosition lv2;
            int i;
            InsertionPosition lv;
            InsertionPosition insertionPosition = lv = listInverted ? this.inverse() : this;
            if (lv == BOTTOM) {
                ResourcePackPosition lv22;
                int i2;
                for (i2 = 0; i2 < items.size() && (lv22 = profileGetter.apply(items.get(i2))).fixedPosition() && lv22.defaultPosition() == this; ++i2) {
                }
                items.add(i2, item);
                return i2;
            }
            for (i = items.size() - 1; i >= 0 && (lv2 = profileGetter.apply(items.get(i))).fixedPosition() && lv2.defaultPosition() == this; --i) {
            }
            items.add(i + 1, item);
            return i + 1;
        }

        public InsertionPosition inverse() {
            return this == TOP ? BOTTOM : TOP;
        }
    }
}

