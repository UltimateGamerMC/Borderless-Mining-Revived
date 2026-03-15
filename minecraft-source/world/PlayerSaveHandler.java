/*
 * External method calls:
 *   Lnet/minecraft/storage/NbtWriteView;create(Lnet/minecraft/util/ErrorReporter;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/storage/NbtWriteView;
 *   Lnet/minecraft/entity/player/PlayerEntity;writeData(Lnet/minecraft/storage/WriteView;)V
 *   Lnet/minecraft/nbt/NbtIo;writeCompressed(Lnet/minecraft/nbt/NbtCompound;Ljava/nio/file/Path;)V
 *   Lnet/minecraft/util/Util;backupAndReplace(Ljava/nio/file/Path;Ljava/nio/file/Path;Ljava/nio/file/Path;)V
 *   Lnet/minecraft/server/PlayerConfigEntry;id()Ljava/util/UUID;
 *   Lnet/minecraft/server/PlayerConfigEntry;name()Ljava/lang/String;
 *   Lnet/minecraft/nbt/NbtSizeTracker;ofUnlimitedBytes()Lnet/minecraft/nbt/NbtSizeTracker;
 *   Lnet/minecraft/nbt/NbtIo;readCompressed(Ljava/nio/file/Path;Lnet/minecraft/nbt/NbtSizeTracker;)Lnet/minecraft/nbt/NbtCompound;
 *   Lnet/minecraft/datafixer/DataFixTypes;update(Lcom/mojang/datafixers/DataFixer;Lnet/minecraft/nbt/NbtCompound;I)Lnet/minecraft/nbt/NbtCompound;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/world/PlayerSaveHandler;loadPlayerData(Lnet/minecraft/server/PlayerConfigEntry;Ljava/lang/String;)Ljava/util/Optional;
 *   Lnet/minecraft/world/PlayerSaveHandler;backupCorruptedPlayerData(Lnet/minecraft/server/PlayerConfigEntry;Ljava/lang/String;)V
 */
package net.minecraft.world;

import com.mojang.datafixers.DataFixer;
import com.mojang.logging.LogUtils;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.time.ZonedDateTime;
import java.util.Optional;
import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtSizeTracker;
import net.minecraft.server.PlayerConfigEntry;
import net.minecraft.storage.NbtWriteView;
import net.minecraft.util.DateTimeFormatters;
import net.minecraft.util.ErrorReporter;
import net.minecraft.util.Util;
import net.minecraft.util.WorldSavePath;
import net.minecraft.world.level.storage.LevelStorage;
import org.slf4j.Logger;

public class PlayerSaveHandler {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final File playerDataDir;
    protected final DataFixer dataFixer;

    public PlayerSaveHandler(LevelStorage.Session session, DataFixer dataFixer) {
        this.dataFixer = dataFixer;
        this.playerDataDir = session.getDirectory(WorldSavePath.PLAYERDATA).toFile();
        this.playerDataDir.mkdirs();
    }

    public void savePlayerData(PlayerEntity player) {
        try (ErrorReporter.Logging lv = new ErrorReporter.Logging(player.getErrorReporterContext(), LOGGER);){
            NbtWriteView lv2 = NbtWriteView.create(lv, player.getRegistryManager());
            player.writeData(lv2);
            Path path = this.playerDataDir.toPath();
            Path path2 = Files.createTempFile(path, player.getUuidAsString() + "-", ".dat", new FileAttribute[0]);
            NbtCompound lv3 = lv2.getNbt();
            NbtIo.writeCompressed(lv3, path2);
            Path path3 = path.resolve(player.getUuidAsString() + ".dat");
            Path path4 = path.resolve(player.getUuidAsString() + ".dat_old");
            Util.backupAndReplace(path3, path2, path4);
        } catch (Exception exception) {
            LOGGER.warn("Failed to save player data for {}", (Object)player.getStringifiedName());
        }
    }

    private void backupCorruptedPlayerData(PlayerConfigEntry arg, String extension) {
        Path path = this.playerDataDir.toPath();
        String string2 = arg.id().toString();
        Path path2 = path.resolve(string2 + extension);
        Path path3 = path.resolve(string2 + "_corrupted_" + ZonedDateTime.now().format(DateTimeFormatters.MINUTES) + extension);
        if (!Files.isRegularFile(path2, new LinkOption[0])) {
            return;
        }
        try {
            Files.copy(path2, path3, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
        } catch (Exception exception) {
            LOGGER.warn("Failed to copy the player.dat file for {}", (Object)arg.name(), (Object)exception);
        }
    }

    private Optional<NbtCompound> loadPlayerData(PlayerConfigEntry arg, String extension) {
        File file = new File(this.playerDataDir, String.valueOf(arg.id()) + extension);
        if (file.exists() && file.isFile()) {
            try {
                return Optional.of(NbtIo.readCompressed(file.toPath(), NbtSizeTracker.ofUnlimitedBytes()));
            } catch (Exception exception) {
                LOGGER.warn("Failed to load player data for {}", (Object)arg.name());
            }
        }
        return Optional.empty();
    }

    public Optional<NbtCompound> loadPlayerData(PlayerConfigEntry arg2) {
        Optional<NbtCompound> optional = this.loadPlayerData(arg2, ".dat");
        if (optional.isEmpty()) {
            this.backupCorruptedPlayerData(arg2, ".dat");
        }
        return optional.or(() -> this.loadPlayerData(arg2, ".dat_old")).map(arg -> {
            int i = NbtHelper.getDataVersion(arg);
            arg = DataFixTypes.PLAYER.update(this.dataFixer, (NbtCompound)arg, i);
            return arg;
        });
    }
}

