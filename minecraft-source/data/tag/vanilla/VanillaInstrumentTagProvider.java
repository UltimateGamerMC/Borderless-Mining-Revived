/*
 * External method calls:
 *   Lnet/minecraft/data/tag/ProvidedTagBuilder;addTag(Lnet/minecraft/registry/tag/TagKey;)Lnet/minecraft/data/tag/ProvidedTagBuilder;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/data/tag/vanilla/VanillaInstrumentTagProvider;builder(Lnet/minecraft/registry/tag/TagKey;)Lnet/minecraft/data/tag/ProvidedTagBuilder;
 */
package net.minecraft.data.tag.vanilla;

import java.util.concurrent.CompletableFuture;
import net.minecraft.data.DataOutput;
import net.minecraft.data.tag.SimpleTagProvider;
import net.minecraft.item.Instrument;
import net.minecraft.item.Instruments;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.InstrumentTags;

public class VanillaInstrumentTagProvider
extends SimpleTagProvider<Instrument> {
    public VanillaInstrumentTagProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.INSTRUMENT, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries) {
        this.builder(InstrumentTags.REGULAR_GOAT_HORNS).add(Instruments.PONDER_GOAT_HORN).add(Instruments.SING_GOAT_HORN).add(Instruments.SEEK_GOAT_HORN).add(Instruments.FEEL_GOAT_HORN);
        this.builder(InstrumentTags.SCREAMING_GOAT_HORNS).add(Instruments.ADMIRE_GOAT_HORN).add(Instruments.CALL_GOAT_HORN).add(Instruments.YEARN_GOAT_HORN).add(Instruments.DREAM_GOAT_HORN);
        this.builder(InstrumentTags.GOAT_HORNS).addTag(InstrumentTags.REGULAR_GOAT_HORNS).addTag(InstrumentTags.SCREAMING_GOAT_HORNS);
    }
}

