/*
 * External method calls:
 *   Lnet/minecraft/entity/spawn/SpawnConditionSelectors;selectors()Ljava/util/List;
 *   Lnet/minecraft/util/ModelAndTexture;createMapCodec(Lcom/mojang/serialization/Codec;Ljava/lang/Object;)Lcom/mojang/serialization/MapCodec;
 *   Lnet/minecraft/registry/entry/RegistryFixedCodec;of(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/registry/entry/RegistryFixedCodec;
 *   Lnet/minecraft/network/codec/PacketCodecs;registryEntry(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/network/codec/PacketCodec;
 */
package net.minecraft.entity.passive;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.entity.VariantSelectorProvider;
import net.minecraft.entity.spawn.SpawnCondition;
import net.minecraft.entity.spawn.SpawnConditionSelectors;
import net.minecraft.entity.spawn.SpawnContext;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.util.ModelAndTexture;
import net.minecraft.util.StringIdentifiable;

public record PigVariant(ModelAndTexture<Model> modelAndTexture, SpawnConditionSelectors spawnConditions) implements VariantSelectorProvider<SpawnContext, SpawnCondition>
{
    public static final Codec<PigVariant> CODEC = RecordCodecBuilder.create(instance -> instance.group(ModelAndTexture.createMapCodec(Model.CODEC, Model.NORMAL).forGetter(PigVariant::modelAndTexture), ((MapCodec)SpawnConditionSelectors.CODEC.fieldOf("spawn_conditions")).forGetter(PigVariant::spawnConditions)).apply((Applicative<PigVariant, ?>)instance, PigVariant::new));
    public static final Codec<PigVariant> NETWORK_CODEC = RecordCodecBuilder.create(instance -> instance.group(ModelAndTexture.createMapCodec(Model.CODEC, Model.NORMAL).forGetter(PigVariant::modelAndTexture)).apply((Applicative<PigVariant, ?>)instance, PigVariant::new));
    public static final Codec<RegistryEntry<PigVariant>> ENTRY_CODEC = RegistryFixedCodec.of(RegistryKeys.PIG_VARIANT);
    public static final PacketCodec<RegistryByteBuf, RegistryEntry<PigVariant>> ENTRY_PACKET_CODEC = PacketCodecs.registryEntry(RegistryKeys.PIG_VARIANT);

    private PigVariant(ModelAndTexture<Model> modelAndTexture) {
        this(modelAndTexture, SpawnConditionSelectors.EMPTY);
    }

    @Override
    public List<VariantSelectorProvider.Selector<SpawnContext, SpawnCondition>> getSelectors() {
        return this.spawnConditions.selectors();
    }

    public static enum Model implements StringIdentifiable
    {
        NORMAL("normal"),
        COLD("cold");

        public static final Codec<Model> CODEC;
        private final String id;

        private Model(String id) {
            this.id = id;
        }

        @Override
        public String asString() {
            return this.id;
        }

        static {
            CODEC = StringIdentifiable.createCodec(Model::values);
        }
    }
}

