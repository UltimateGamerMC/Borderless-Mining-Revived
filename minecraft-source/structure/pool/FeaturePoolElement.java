/*
 * External method calls:
 *   Lnet/minecraft/nbt/NbtCompound;putString(Ljava/lang/String;Ljava/lang/String;)V
 *   Lnet/minecraft/block/enums/Orientation;byDirections(Lnet/minecraft/util/math/Direction;Lnet/minecraft/util/math/Direction;)Lnet/minecraft/block/enums/Orientation;
 *   Lnet/minecraft/block/BlockState;with(Lnet/minecraft/state/property/Property;Ljava/lang/Comparable;)Ljava/lang/Object;
 *   Lnet/minecraft/structure/StructureTemplate$JigsawBlockInfo;of(Lnet/minecraft/structure/StructureTemplate$StructureBlockInfo;)Lnet/minecraft/structure/StructureTemplate$JigsawBlockInfo;
 *   Lnet/minecraft/world/gen/feature/PlacedFeature;generateUnregistered(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/util/math/random/Random;Lnet/minecraft/util/math/BlockPos;)Z
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/structure/pool/FeaturePoolElement;createDefaultJigsawNbt()Lnet/minecraft/nbt/NbtCompound;
 *   Lnet/minecraft/structure/pool/FeaturePoolElement;projectionGetter()Lcom/mojang/serialization/codecs/RecordCodecBuilder;
 */
package net.minecraft.structure.pool;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.JigsawBlock;
import net.minecraft.block.entity.JigsawBlockEntity;
import net.minecraft.block.enums.Orientation;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.StructureLiquidSettings;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePoolElementType;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.PlacedFeature;

public class FeaturePoolElement
extends StructurePoolElement {
    public static final MapCodec<FeaturePoolElement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)PlacedFeature.REGISTRY_CODEC.fieldOf("feature")).forGetter(pool -> pool.feature), FeaturePoolElement.projectionGetter()).apply((Applicative<FeaturePoolElement, ?>)instance, FeaturePoolElement::new));
    private static final Identifier DEFAULT_NAME = Identifier.ofVanilla("bottom");
    private final RegistryEntry<PlacedFeature> feature;
    private final NbtCompound nbt;

    protected FeaturePoolElement(RegistryEntry<PlacedFeature> feature, StructurePool.Projection projection) {
        super(projection);
        this.feature = feature;
        this.nbt = this.createDefaultJigsawNbt();
    }

    private NbtCompound createDefaultJigsawNbt() {
        NbtCompound lv = new NbtCompound();
        lv.put("name", Identifier.CODEC, DEFAULT_NAME);
        lv.putString("final_state", "minecraft:air");
        lv.put("pool", JigsawBlockEntity.STRUCTURE_POOL_KEY_CODEC, StructurePools.EMPTY);
        lv.put("target", Identifier.CODEC, JigsawBlockEntity.DEFAULT_NAME);
        lv.put("joint", JigsawBlockEntity.Joint.CODEC, JigsawBlockEntity.Joint.ROLLABLE);
        return lv;
    }

    @Override
    public Vec3i getStart(StructureTemplateManager structureTemplateManager, BlockRotation rotation) {
        return Vec3i.ZERO;
    }

    @Override
    public List<StructureTemplate.JigsawBlockInfo> getStructureBlockInfos(StructureTemplateManager structureTemplateManager, BlockPos pos, BlockRotation rotation, Random random) {
        return List.of(StructureTemplate.JigsawBlockInfo.of(new StructureTemplate.StructureBlockInfo(pos, (BlockState)Blocks.JIGSAW.getDefaultState().with(JigsawBlock.ORIENTATION, Orientation.byDirections(Direction.DOWN, Direction.SOUTH)), this.nbt)));
    }

    @Override
    public BlockBox getBoundingBox(StructureTemplateManager structureTemplateManager, BlockPos pos, BlockRotation rotation) {
        Vec3i lv = this.getStart(structureTemplateManager, rotation);
        return new BlockBox(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + lv.getX(), pos.getY() + lv.getY(), pos.getZ() + lv.getZ());
    }

    @Override
    public boolean generate(StructureTemplateManager structureTemplateManager, StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, BlockPos pos, BlockPos pivot, BlockRotation rotation, BlockBox box, Random random, StructureLiquidSettings liquidSettings, boolean keepJigsaws) {
        return this.feature.value().generateUnregistered(world, chunkGenerator, random, pos);
    }

    @Override
    public StructurePoolElementType<?> getType() {
        return StructurePoolElementType.FEATURE_POOL_ELEMENT;
    }

    public String toString() {
        return "Feature[" + String.valueOf(this.feature) + "]";
    }
}

