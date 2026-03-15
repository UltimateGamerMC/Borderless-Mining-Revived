/*
 * External method calls:
 *   Lnet/minecraft/resource/ResourceFactory;open(Lnet/minecraft/util/Identifier;)Ljava/io/InputStream;
 *   Lnet/minecraft/client/sound/NonRepeatingAudioStream;readAll()Ljava/nio/ByteBuffer;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/sound/SoundLoader;loadStatic(Lnet/minecraft/util/Identifier;)Ljava/util/concurrent/CompletableFuture;
 */
package net.minecraft.client.sound;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.AudioStream;
import net.minecraft.client.sound.OggAudioStream;
import net.minecraft.client.sound.RepeatingAudioStream;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.StaticSound;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

@Environment(value=EnvType.CLIENT)
public class SoundLoader {
    private final ResourceFactory resourceFactory;
    private final Map<Identifier, CompletableFuture<StaticSound>> loadedSounds = Maps.newHashMap();

    public SoundLoader(ResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
    }

    public CompletableFuture<StaticSound> loadStatic(Identifier id) {
        return this.loadedSounds.computeIfAbsent(id, id2 -> CompletableFuture.supplyAsync(() -> {
            try (InputStream inputStream = this.resourceFactory.open((Identifier)id2);){
                StaticSound staticSound;
                try (OggAudioStream lv = new OggAudioStream(inputStream);){
                    ByteBuffer byteBuffer = lv.readAll();
                    staticSound = new StaticSound(byteBuffer, lv.getFormat());
                }
                return staticSound;
            } catch (IOException iOException) {
                throw new CompletionException(iOException);
            }
        }, Util.getDownloadWorkerExecutor()));
    }

    public CompletableFuture<AudioStream> loadStreamed(Identifier id, boolean repeatInstantly) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                InputStream inputStream = this.resourceFactory.open(id);
                return repeatInstantly ? new RepeatingAudioStream(OggAudioStream::new, inputStream) : new OggAudioStream(inputStream);
            } catch (IOException iOException) {
                throw new CompletionException(iOException);
            }
        }, Util.getDownloadWorkerExecutor());
    }

    public void close() {
        this.loadedSounds.values().forEach(soundFuture -> soundFuture.thenAccept(StaticSound::close));
        this.loadedSounds.clear();
    }

    public CompletableFuture<?> loadStatic(Collection<Sound> sounds) {
        return CompletableFuture.allOf((CompletableFuture[])sounds.stream().map(sound -> this.loadStatic(sound.getLocation())).toArray(CompletableFuture[]::new));
    }
}

