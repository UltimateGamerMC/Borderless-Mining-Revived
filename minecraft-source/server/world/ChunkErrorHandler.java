/*
 * External method calls:
 *   Lnet/minecraft/util/crash/CrashReport;create(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/util/crash/CrashReport;
 *   Lnet/minecraft/util/crash/CrashReport;addElement(Ljava/lang/String;)Lnet/minecraft/util/crash/CrashReportSection;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/world/ChunkErrorHandler;createMisplacementException(Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/util/math/ChunkPos;)Lnet/minecraft/util/crash/CrashException;
 *   Lnet/minecraft/server/world/ChunkErrorHandler;onChunkLoadFailure(Ljava/lang/Throwable;Lnet/minecraft/world/storage/StorageKey;Lnet/minecraft/util/math/ChunkPos;)V
 */
package net.minecraft.server.world;

import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.storage.StorageKey;

public interface ChunkErrorHandler {
    public void onChunkLoadFailure(Throwable var1, StorageKey var2, ChunkPos var3);

    public void onChunkSaveFailure(Throwable var1, StorageKey var2, ChunkPos var3);

    public static CrashException createMisplacementException(ChunkPos actualPos, ChunkPos expectedPos) {
        CrashReport lv = CrashReport.create(new IllegalStateException("Retrieved chunk position " + String.valueOf(actualPos) + " does not match requested " + String.valueOf(expectedPos)), "Chunk found in invalid location");
        CrashReportSection lv2 = lv.addElement("Misplaced Chunk");
        lv2.add("Stored Position", actualPos::toString);
        return new CrashException(lv);
    }

    default public void onChunkMisplacement(ChunkPos actualPos, ChunkPos expectedPos, StorageKey key) {
        this.onChunkLoadFailure(ChunkErrorHandler.createMisplacementException(actualPos, expectedPos), key, expectedPos);
    }
}

