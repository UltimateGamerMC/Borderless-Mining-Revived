/*
 * External method calls:
 *   Lnet/minecraft/util/shape/VoxelShapes;fullCube()Lnet/minecraft/util/shape/VoxelShape;
 *   Lnet/minecraft/util/shape/VoxelShape;offset(Lnet/minecraft/util/math/Vec3i;)Lnet/minecraft/util/shape/VoxelShape;
 *   Lnet/minecraft/world/StructureWorldAccess;doesNotIntersectEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/shape/VoxelShape;)Z
 *
 * Internal private/static methods:
 *   Lnet/minecraft/world/gen/blockpredicate/UnobstructedBlockPredicate;test(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/util/math/BlockPos;)Z
 */
package net.minecraft.world.gen.blockpredicate;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.blockpredicate.BlockPredicateType;

record UnobstructedBlockPredicate(Vec3i offset) implements BlockPredicate
{
    public static MapCodec<UnobstructedBlockPredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Vec3i.CODEC.optionalFieldOf("offset", Vec3i.ZERO).forGetter(UnobstructedBlockPredicate::offset)).apply((Applicative<UnobstructedBlockPredicate, ?>)instance, UnobstructedBlockPredicate::new));

    @Override
    public BlockPredicateType<?> getType() {
        return BlockPredicateType.UNOBSTRUCTED;
    }

    @Override
    public boolean test(StructureWorldAccess arg, BlockPos arg2) {
        return arg.doesNotIntersectEntities(null, VoxelShapes.fullCube().offset(arg2));
    }

    @Override
    public /* synthetic */ boolean test(Object world, Object pos) {
        return this.test((StructureWorldAccess)world, (BlockPos)pos);
    }
}

