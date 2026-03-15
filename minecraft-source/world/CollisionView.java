/*
 * External method calls:
 *   Lnet/minecraft/util/shape/VoxelShape;offset(Lnet/minecraft/util/math/Vec3i;)Lnet/minecraft/util/shape/VoxelShape;
 *   Lnet/minecraft/util/shape/VoxelShapes;cuboid(Lnet/minecraft/util/math/Box;)Lnet/minecraft/util/shape/VoxelShape;
 *   Lnet/minecraft/util/shape/VoxelShapes;matchesAnywhere(Lnet/minecraft/util/shape/VoxelShape;Lnet/minecraft/util/shape/VoxelShape;Lnet/minecraft/util/function/BooleanBiFunction;)Z
 *   Lnet/minecraft/block/ShapeContext;ofCollision(Lnet/minecraft/entity/Entity;D)Lnet/minecraft/block/ShapeContext;
 *   Lnet/minecraft/block/ShapeContext;absent()Lnet/minecraft/block/ShapeContext;
 *   Lnet/minecraft/block/ShapeContext;of(Lnet/minecraft/entity/Entity;)Lnet/minecraft/block/ShapeContext;
 *   Lnet/minecraft/block/ShapeContext;absentTreatingFluidAsCube()Lnet/minecraft/block/ShapeContext;
 *   Lnet/minecraft/block/ShapeContext;of(Lnet/minecraft/entity/Entity;Z)Lnet/minecraft/block/ShapeContext;
 *   Lnet/minecraft/world/border/WorldBorder;asVoxelShape()Lnet/minecraft/util/shape/VoxelShape;
 *   Lnet/minecraft/world/border/WorldBorder;clamp(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;
 *   Lnet/minecraft/world/BlockCollisionSpliterator;next()Ljava/lang/Object;
 *   Lnet/minecraft/util/shape/VoxelShapes;empty()Lnet/minecraft/util/shape/VoxelShape;
 *   Lnet/minecraft/util/shape/VoxelShapes;combineAndSimplify(Lnet/minecraft/util/shape/VoxelShape;Lnet/minecraft/util/shape/VoxelShape;Lnet/minecraft/util/function/BooleanBiFunction;)Lnet/minecraft/util/shape/VoxelShape;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/world/CollisionView;doesNotIntersectEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/shape/VoxelShape;)Z
 *   Lnet/minecraft/world/CollisionView;doesNotCollideWithEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Z
 *   Lnet/minecraft/world/CollisionView;doesNotCollideWithWorldBorder(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Z
 *   Lnet/minecraft/world/CollisionView;raycast(Lnet/minecraft/world/RaycastContext;)Lnet/minecraft/util/hit/BlockHitResult;
 */
package net.minecraft.world;

import com.google.common.collect.Iterables;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockCollisionSpliterator;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.border.WorldBorder;
import org.jspecify.annotations.Nullable;

public interface CollisionView
extends BlockView {
    public WorldBorder getWorldBorder();

    public @Nullable BlockView getChunkAsView(int var1, int var2);

    default public boolean doesNotIntersectEntities(@Nullable Entity except, VoxelShape shape) {
        return true;
    }

    default public boolean canPlace(BlockState state, BlockPos pos, ShapeContext context) {
        VoxelShape lv = state.getCollisionShape(this, pos, context);
        return lv.isEmpty() || this.doesNotIntersectEntities(null, lv.offset(pos));
    }

    default public boolean doesNotIntersectEntities(Entity entity) {
        return this.doesNotIntersectEntities(entity, VoxelShapes.cuboid(entity.getBoundingBox()));
    }

    default public boolean isSpaceEmpty(Box box) {
        return this.isSpaceEmpty(null, box);
    }

    default public boolean isSpaceEmpty(Entity entity) {
        return this.isSpaceEmpty(entity, entity.getBoundingBox());
    }

    default public boolean isSpaceEmpty(@Nullable Entity entity, Box box) {
        return this.isSpaceEmpty(entity, box, false);
    }

    default public boolean isSpaceEmpty(@Nullable Entity entity, Box box, boolean checkFluid) {
        return this.isBlockSpaceEmpty(entity, box, checkFluid) && this.doesNotCollideWithEntities(entity, box) && this.doesNotCollideWithWorldBorder(entity, box);
    }

    default public boolean isBlockSpaceEmpty(@Nullable Entity entity, Box box) {
        return this.isBlockSpaceEmpty(entity, box, false);
    }

    default public boolean isBlockSpaceEmpty(@Nullable Entity entity, Box box, boolean blockOrFluid) {
        Iterable<VoxelShape> iterable = blockOrFluid ? this.getBlockOrFluidCollisions(entity, box) : this.getBlockCollisions(entity, box);
        for (VoxelShape lv : iterable) {
            if (lv.isEmpty()) continue;
            return false;
        }
        return true;
    }

    default public boolean doesNotCollideWithEntities(@Nullable Entity entity, Box box) {
        return this.getEntityCollisions(entity, box).isEmpty();
    }

    default public boolean doesNotCollideWithWorldBorder(@Nullable Entity entity, Box box) {
        if (entity != null) {
            VoxelShape lv = this.getWorldBorderCollisions(entity, box);
            return lv == null || !VoxelShapes.matchesAnywhere(lv, VoxelShapes.cuboid(box), BooleanBiFunction.AND);
        }
        return true;
    }

    public List<VoxelShape> getEntityCollisions(@Nullable Entity var1, Box var2);

    default public Iterable<VoxelShape> getCollisions(@Nullable Entity entity, Box box) {
        List<VoxelShape> list = this.getEntityCollisions(entity, box);
        Iterable<VoxelShape> iterable = this.getBlockCollisions(entity, box);
        return list.isEmpty() ? iterable : Iterables.concat(list, iterable);
    }

    default public Iterable<VoxelShape> getCollisions(@Nullable Entity entity, Box box, Vec3d pos) {
        List<VoxelShape> list = this.getEntityCollisions(entity, box);
        Iterable<VoxelShape> iterable = this.getBlockOrFluidCollisions(ShapeContext.ofCollision(entity, pos.y), box);
        return list.isEmpty() ? iterable : Iterables.concat(list, iterable);
    }

    default public Iterable<VoxelShape> getBlockCollisions(@Nullable Entity entity, Box box) {
        return this.getBlockOrFluidCollisions(entity == null ? ShapeContext.absent() : ShapeContext.of(entity), box);
    }

    default public Iterable<VoxelShape> getBlockOrFluidCollisions(@Nullable Entity entity, Box box) {
        return this.getBlockOrFluidCollisions(entity == null ? ShapeContext.absentTreatingFluidAsCube() : ShapeContext.of(entity, true), box);
    }

    private Iterable<VoxelShape> getBlockOrFluidCollisions(ShapeContext shapeContext, Box box) {
        return () -> new BlockCollisionSpliterator<VoxelShape>(this, shapeContext, box, false, (pos, shape) -> shape);
    }

    private @Nullable VoxelShape getWorldBorderCollisions(Entity entity, Box box) {
        WorldBorder lv = this.getWorldBorder();
        return lv.canCollide(entity, box) ? lv.asVoxelShape() : null;
    }

    default public BlockHitResult getCollisionsIncludingWorldBorder(RaycastContext context) {
        BlockHitResult lv = this.raycast(context);
        WorldBorder lv2 = this.getWorldBorder();
        if (lv2.contains(context.getStart()) && !lv2.contains(lv.getPos())) {
            Vec3d lv3 = lv.getPos().subtract(context.getStart());
            Direction lv4 = Direction.getFacing(lv3.x, lv3.y, lv3.z);
            Vec3d lv5 = lv2.clamp(lv.getPos());
            return new BlockHitResult(lv5, lv4, BlockPos.ofFloored(lv5), false, true);
        }
        return lv;
    }

    default public boolean canCollide(@Nullable Entity entity, Box box) {
        BlockCollisionSpliterator<VoxelShape> lv = new BlockCollisionSpliterator<VoxelShape>(this, entity, box, true, (pos, voxelShape) -> voxelShape);
        while (lv.hasNext()) {
            if (((VoxelShape)lv.next()).isEmpty()) continue;
            return true;
        }
        return false;
    }

    default public Optional<BlockPos> findSupportingBlockPos(Entity entity, Box box) {
        BlockPos lv = null;
        double d = Double.MAX_VALUE;
        BlockCollisionSpliterator<BlockPos> lv2 = new BlockCollisionSpliterator<BlockPos>(this, entity, box, false, (pos, voxelShape) -> pos);
        while (lv2.hasNext()) {
            BlockPos lv3 = (BlockPos)lv2.next();
            double e = lv3.getSquaredDistance(entity.getEntityPos());
            if (!(e < d) && (e != d || lv != null && lv.compareTo(lv3) >= 0)) continue;
            lv = lv3.toImmutable();
            d = e;
        }
        return Optional.ofNullable(lv);
    }

    default public Optional<Vec3d> findClosestCollision(@Nullable Entity entity, VoxelShape shape, Vec3d target, double x, double y, double z) {
        if (shape.isEmpty()) {
            return Optional.empty();
        }
        Box lv = shape.getBoundingBox().expand(x, y, z);
        VoxelShape lv2 = StreamSupport.stream(this.getBlockCollisions(entity, lv).spliterator(), false).filter(collision -> this.getWorldBorder() == null || this.getWorldBorder().contains(collision.getBoundingBox())).flatMap(collision -> collision.getBoundingBoxes().stream()).map(box -> box.expand(x / 2.0, y / 2.0, z / 2.0)).map(VoxelShapes::cuboid).reduce(VoxelShapes.empty(), VoxelShapes::union);
        VoxelShape lv3 = VoxelShapes.combineAndSimplify(shape, lv2, BooleanBiFunction.ONLY_FIRST);
        return lv3.getClosestPointTo(target);
    }
}

