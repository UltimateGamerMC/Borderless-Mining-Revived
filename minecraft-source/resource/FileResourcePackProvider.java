/*
 * External method calls:
 *   Lnet/minecraft/util/path/PathUtil;createDirectories(Ljava/nio/file/Path;)V
 *   Lnet/minecraft/text/Text;literal(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/resource/FileResourcePackProvider$PackOpenerImpl;open(Ljava/nio/file/Path;Ljava/util/List;)Ljava/lang/Object;
 *   Lnet/minecraft/resource/ResourcePackProfile;create(Lnet/minecraft/resource/ResourcePackInfo;Lnet/minecraft/resource/ResourcePackProfile$PackFactory;Lnet/minecraft/resource/ResourceType;Lnet/minecraft/resource/ResourcePackPosition;)Lnet/minecraft/resource/ResourcePackProfile;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/resource/FileResourcePackProvider;forEachProfile(Ljava/nio/file/Path;Lnet/minecraft/util/path/SymlinkFinder;Ljava/util/function/BiConsumer;)V
 *   Lnet/minecraft/resource/FileResourcePackProvider;createPackInfo(Ljava/nio/file/Path;)Lnet/minecraft/resource/ResourcePackInfo;
 */
package net.minecraft.resource;

import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.resource.DirectoryResourcePack;
import net.minecraft.resource.ResourcePackInfo;
import net.minecraft.resource.ResourcePackOpener;
import net.minecraft.resource.ResourcePackPosition;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.ZipResourcePack;
import net.minecraft.resource.fs.ResourceFileSystem;
import net.minecraft.text.Text;
import net.minecraft.util.path.PathUtil;
import net.minecraft.util.path.SymlinkEntry;
import net.minecraft.util.path.SymlinkFinder;
import net.minecraft.util.path.SymlinkValidationException;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class FileResourcePackProvider
implements ResourcePackProvider {
    static final Logger LOGGER = LogUtils.getLogger();
    private static final ResourcePackPosition POSITION = new ResourcePackPosition(false, ResourcePackProfile.InsertionPosition.TOP, false);
    private final Path packsDir;
    private final ResourceType type;
    private final ResourcePackSource source;
    private final SymlinkFinder symlinkFinder;

    public FileResourcePackProvider(Path packsDir, ResourceType type, ResourcePackSource source, SymlinkFinder symlinkFinder) {
        this.packsDir = packsDir;
        this.type = type;
        this.source = source;
        this.symlinkFinder = symlinkFinder;
    }

    private static String getFileName(Path path) {
        return path.getFileName().toString();
    }

    @Override
    public void register(Consumer<ResourcePackProfile> profileAdder) {
        try {
            PathUtil.createDirectories(this.packsDir);
            FileResourcePackProvider.forEachProfile(this.packsDir, this.symlinkFinder, (path, packFactory) -> {
                ResourcePackInfo lv = this.createPackInfo((Path)path);
                ResourcePackProfile lv2 = ResourcePackProfile.create(lv, packFactory, this.type, POSITION);
                if (lv2 != null) {
                    profileAdder.accept(lv2);
                }
            });
        } catch (IOException iOException) {
            LOGGER.warn("Failed to list packs in {}", (Object)this.packsDir, (Object)iOException);
        }
    }

    private ResourcePackInfo createPackInfo(Path path) {
        String string = FileResourcePackProvider.getFileName(path);
        return new ResourcePackInfo("file/" + string, Text.literal(string), this.source, Optional.empty());
    }

    public static void forEachProfile(Path path, SymlinkFinder symlinkFinder, BiConsumer<Path, ResourcePackProfile.PackFactory> callback) throws IOException {
        PackOpenerImpl lv = new PackOpenerImpl(symlinkFinder);
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path);){
            for (Path path2 : directoryStream) {
                try {
                    ArrayList<SymlinkEntry> list = new ArrayList<SymlinkEntry>();
                    ResourcePackProfile.PackFactory lv2 = (ResourcePackProfile.PackFactory)lv.open(path2, list);
                    if (!list.isEmpty()) {
                        LOGGER.warn("Ignoring potential pack entry: {}", (Object)SymlinkValidationException.getMessage(path2, list));
                        continue;
                    }
                    if (lv2 != null) {
                        callback.accept(path2, lv2);
                        continue;
                    }
                    LOGGER.info("Found non-pack entry '{}', ignoring", (Object)path2);
                } catch (IOException iOException) {
                    LOGGER.warn("Failed to read properties of '{}', ignoring", (Object)path2, (Object)iOException);
                }
            }
        }
    }

    static class PackOpenerImpl
    extends ResourcePackOpener<ResourcePackProfile.PackFactory> {
        protected PackOpenerImpl(SymlinkFinder arg) {
            super(arg);
        }

        @Override
        protected @Nullable ResourcePackProfile.PackFactory openZip(Path path) {
            FileSystem fileSystem = path.getFileSystem();
            if (fileSystem == FileSystems.getDefault() || fileSystem instanceof ResourceFileSystem) {
                return new ZipResourcePack.ZipBackedFactory(path);
            }
            LOGGER.info("Can't open pack archive at {}", (Object)path);
            return null;
        }

        @Override
        protected ResourcePackProfile.PackFactory openDirectory(Path path) {
            return new DirectoryResourcePack.DirectoryBackedFactory(path);
        }

        @Override
        protected /* synthetic */ Object openDirectory(Path path) throws IOException {
            return this.openDirectory(path);
        }

        @Override
        protected /* synthetic */ @Nullable Object openZip(Path path) throws IOException {
            return this.openZip(path);
        }
    }
}

