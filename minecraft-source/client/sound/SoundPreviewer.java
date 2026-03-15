/*
 * External method calls:
 *   Lnet/minecraft/client/sound/PositionedSoundInstance;ui(Lnet/minecraft/sound/SoundEvent;FF)Lnet/minecraft/client/sound/PositionedSoundInstance;
 *   Lnet/minecraft/client/sound/SoundManager;play(Lnet/minecraft/client/sound/SoundInstance;)Lnet/minecraft/client/sound/SoundSystem$PlayResult;
 *   Lnet/minecraft/client/sound/SoundManager;stop(Lnet/minecraft/client/sound/SoundInstance;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/sound/SoundPreviewer;stopPreviewOfOtherCategory(Lnet/minecraft/client/sound/SoundManager;Lnet/minecraft/sound/SoundCategory;)V
 */
package net.minecraft.client.sound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public final class SoundPreviewer {
    private static @Nullable SoundInstance currentSoundPreview;
    private static @Nullable SoundCategory category;

    public static void preview(SoundManager manager, SoundCategory category, float volume) {
        SoundPreviewer.stopPreviewOfOtherCategory(manager, category);
        if (SoundPreviewer.canPlaySound(manager)) {
            SoundEvent lv;
            switch (category) {
                case RECORDS: {
                    SoundEvent soundEvent = SoundEvents.BLOCK_NOTE_BLOCK_GUITAR.value();
                    break;
                }
                case WEATHER: {
                    SoundEvent soundEvent = SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER;
                    break;
                }
                case BLOCKS: {
                    SoundEvent soundEvent = SoundEvents.BLOCK_GRASS_PLACE;
                    break;
                }
                case HOSTILE: {
                    SoundEvent soundEvent = SoundEvents.ENTITY_ZOMBIE_AMBIENT;
                    break;
                }
                case NEUTRAL: {
                    SoundEvent soundEvent = SoundEvents.ENTITY_COW_AMBIENT;
                    break;
                }
                case PLAYERS: {
                    SoundEvent soundEvent = SoundEvents.ENTITY_GENERIC_EAT.value();
                    break;
                }
                case AMBIENT: {
                    SoundEvent soundEvent = SoundEvents.AMBIENT_CAVE.value();
                    break;
                }
                case UI: {
                    SoundEvent soundEvent = SoundEvents.UI_BUTTON_CLICK.value();
                    break;
                }
                default: {
                    SoundEvent soundEvent = lv = SoundEvents.INTENTIONALLY_EMPTY;
                }
            }
            if (lv != SoundEvents.INTENTIONALLY_EMPTY) {
                currentSoundPreview = PositionedSoundInstance.ui(lv, 1.0f, volume);
                manager.play(currentSoundPreview);
            }
        }
    }

    private static void stopPreviewOfOtherCategory(SoundManager manager, SoundCategory category) {
        if (SoundPreviewer.category != category) {
            SoundPreviewer.category = category;
            if (currentSoundPreview != null) {
                manager.stop(currentSoundPreview);
            }
        }
    }

    private static boolean canPlaySound(SoundManager manager) {
        return currentSoundPreview == null || !manager.isPlaying(currentSoundPreview);
    }
}

