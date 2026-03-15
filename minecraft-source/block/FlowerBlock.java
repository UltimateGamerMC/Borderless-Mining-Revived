/*
 * External method calls:
 *   Lnet/minecraft/util/shape/VoxelShape;offset(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/shape/VoxelShape;
 *   Lnet/minecraft/block/Block;createColumnShape(DDD)Lnet/minecraft/util/shape/VoxelShape;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/block/FlowerBlock;createStewEffectList(Lnet/minecraft/registry/entry/RegistryEntry;F)Lnet/minecraft/component/type/SuspiciousStewEffectsComponent;
 *   Lnet/minecraft/block/FlowerBlock;createSettingsCodec()Lcom/mojang/serialization/codecs/RecordCodecBuilder;
 */
package net.minecraft.block;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.SuspiciousStewIngredient;
import net.minecraft.component.type.SuspiciousStewEffectsComponent;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.jspecify.annotations.Nullable;

public class FlowerBlock
extends PlantBlock
implements SuspiciousStewIngredient {
    protected static final MapCodec<SuspiciousStewEffectsComponent> STEW_EFFECT_CODEC = SuspiciousStewEffectsComponent.CODEC.fieldOf("suspicious_stew_effects");
    public static final MapCodec<FlowerBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(STEW_EFFECT_CODEC.forGetter(FlowerBlock::getStewEffects), FlowerBlock.createSettingsCodec()).apply((Applicative<FlowerBlock, ?>)instance, FlowerBlock::new));
    private static final VoxelShape SHAPE = Block.createColumnShape(6.0, 0.0, 10.0);
    private final SuspiciousStewEffectsComponent stewEffects;

    public MapCodec<? extends FlowerBlock> getCodec() {
        return CODEC;
    }

    public FlowerBlock(RegistryEntry<StatusEffect> stewEffect, float effectLengthInSeconds, AbstractBlock.Settings settings) {
        this(FlowerBlock.createStewEffectList(stewEffect, effectLengthInSeconds), settings);
    }

    public FlowerBlock(SuspiciousStewEffectsComponent stewEffects, AbstractBlock.Settings settings) {
        super(settings);
        this.stewEffects = stewEffects;
    }

    protected static SuspiciousStewEffectsComponent createStewEffectList(RegistryEntry<StatusEffect> effect, float effectLengthInSeconds) {
        return new SuspiciousStewEffectsComponent(List.of(new SuspiciousStewEffectsComponent.StewEffect(effect, MathHelper.floor(effectLengthInSeconds * 20.0f))));
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE.offset(state.getModelOffset(pos));
    }

    @Override
    public SuspiciousStewEffectsComponent getStewEffects() {
        return this.stewEffects;
    }

    public @Nullable StatusEffectInstance getContactEffect() {
        return null;
    }
}

