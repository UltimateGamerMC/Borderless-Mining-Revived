/*
 * External method calls:
 *   Lnet/minecraft/block/entity/BlockEntity;createNbtWithIdentifyingData(Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/nbt/NbtCompound;
 *   Lnet/minecraft/nbt/NbtHelper;matches(Lnet/minecraft/nbt/NbtElement;Lnet/minecraft/nbt/NbtElement;Z)Z
 *   Lnet/minecraft/block/Block;postProcessState(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;
 *   Lnet/minecraft/util/ErrorReporter$Logging;makeChild(Lnet/minecraft/util/ErrorReporter$Context;)Lnet/minecraft/util/ErrorReporter;
 *   Lnet/minecraft/util/ErrorReporter;makeChild(Lnet/minecraft/util/ErrorReporter$Context;)Lnet/minecraft/util/ErrorReporter;
 *   Lnet/minecraft/storage/NbtWriteView;create(Lnet/minecraft/util/ErrorReporter;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/storage/NbtWriteView;
 *   Lnet/minecraft/block/entity/BlockEntity;writeDataWithoutId(Lnet/minecraft/storage/WriteView;)V
 *   Lnet/minecraft/storage/NbtReadView;create(Lnet/minecraft/util/ErrorReporter;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/storage/ReadView;
 *   Lnet/minecraft/block/entity/BlockEntity;read(Lnet/minecraft/storage/ReadView;)V
 *   Lnet/minecraft/server/world/ServerChunkManager;markForUpdate(Lnet/minecraft/util/math/BlockPos;)V
 *   Lnet/minecraft/block/BlockState;withIfExists(Lnet/minecraft/state/property/Property;Ljava/lang/Comparable;)Ljava/lang/Object;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/command/argument/BlockStateArgument;test(Lnet/minecraft/block/pattern/CachedBlockPosition;)Z
 *   Lnet/minecraft/command/argument/BlockStateArgument;copyPropertiesTo(Lnet/minecraft/block/BlockState;)Lnet/minecraft/block/BlockState;
 *   Lnet/minecraft/command/argument/BlockStateArgument;copyProperty(Lnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;Lnet/minecraft/state/property/Property;)Lnet/minecraft/block/BlockState;
 */
package net.minecraft.command.argument;

import com.mojang.logging.LogUtils;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Property;
import net.minecraft.storage.NbtReadView;
import net.minecraft.storage.NbtWriteView;
import net.minecraft.util.ErrorReporter;
import net.minecraft.util.math.BlockPos;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class BlockStateArgument
implements Predicate<CachedBlockPosition> {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final BlockState state;
    private final Set<Property<?>> properties;
    private final @Nullable NbtCompound data;

    public BlockStateArgument(BlockState state, Set<Property<?>> properties, @Nullable NbtCompound data) {
        this.state = state;
        this.properties = properties;
        this.data = data;
    }

    public BlockState getBlockState() {
        return this.state;
    }

    public Set<Property<?>> getProperties() {
        return this.properties;
    }

    @Override
    public boolean test(CachedBlockPosition arg) {
        BlockState lv = arg.getBlockState();
        if (!lv.isOf(this.state.getBlock())) {
            return false;
        }
        for (Property<?> lv2 : this.properties) {
            if (lv.get(lv2) == this.state.get(lv2)) continue;
            return false;
        }
        if (this.data != null) {
            BlockEntity lv3 = arg.getBlockEntity();
            return lv3 != null && NbtHelper.matches(this.data, lv3.createNbtWithIdentifyingData(arg.getWorld().getRegistryManager()), true);
        }
        return true;
    }

    public boolean test(ServerWorld world, BlockPos pos) {
        return this.test(new CachedBlockPosition(world, pos, false));
    }

    public boolean setBlockState(ServerWorld world, BlockPos pos, @Block.SetBlockStateFlag int flags) {
        BlockEntity lv2;
        BlockState lv;
        BlockState blockState = lv = (flags & Block.FORCE_STATE) != 0 ? this.state : Block.postProcessState(this.state, world, pos);
        if (lv.isAir()) {
            lv = this.state;
        }
        lv = this.copyPropertiesTo(lv);
        boolean bl = false;
        if (world.setBlockState(pos, lv, flags)) {
            bl = true;
        }
        if (this.data != null && (lv2 = world.getBlockEntity(pos)) != null) {
            try (ErrorReporter.Logging lv3 = new ErrorReporter.Logging(LOGGER);){
                DynamicRegistryManager lv4 = world.getRegistryManager();
                ErrorReporter lv5 = lv3.makeChild(lv2.getReporterContext());
                NbtWriteView lv6 = NbtWriteView.create(lv5.makeChild(() -> "(before)"), lv4);
                lv2.writeDataWithoutId(lv6);
                NbtCompound lv7 = lv6.getNbt();
                lv2.read(NbtReadView.create(lv3, lv4, this.data));
                NbtWriteView lv8 = NbtWriteView.create(lv5.makeChild(() -> "(after)"), lv4);
                lv2.writeDataWithoutId(lv8);
                NbtCompound lv9 = lv8.getNbt();
                if (!lv9.equals(lv7)) {
                    bl = true;
                    lv2.markDirty();
                    world.getChunkManager().markForUpdate(pos);
                }
            }
        }
        return bl;
    }

    private BlockState copyPropertiesTo(BlockState state) {
        if (state == this.state) {
            return state;
        }
        for (Property<?> lv : this.properties) {
            state = BlockStateArgument.copyProperty(state, this.state, lv);
        }
        return state;
    }

    private static <T extends Comparable<T>> BlockState copyProperty(BlockState to, BlockState from, Property<T> property) {
        return (BlockState)to.withIfExists(property, from.get(property));
    }

    @Override
    public /* synthetic */ boolean test(Object context) {
        return this.test((CachedBlockPosition)context);
    }
}

