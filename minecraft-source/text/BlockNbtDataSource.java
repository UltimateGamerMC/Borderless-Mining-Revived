/*
 * External method calls:
 *   Lnet/minecraft/command/argument/BlockPosArgumentType;blockPos()Lnet/minecraft/command/argument/BlockPosArgumentType;
 *   Lnet/minecraft/command/argument/BlockPosArgumentType;parse(Lcom/mojang/brigadier/StringReader;)Lnet/minecraft/command/argument/PosArgument;
 *   Lnet/minecraft/command/argument/PosArgument;toAbsoluteBlockPos(Lnet/minecraft/server/command/ServerCommandSource;)Lnet/minecraft/util/math/BlockPos;
 *   Lnet/minecraft/block/entity/BlockEntity;createNbtWithIdentifyingData(Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/nbt/NbtCompound;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/text/BlockNbtDataSource;parsePos(Ljava/lang/String;)Lnet/minecraft/command/argument/PosArgument;
 */
package net.minecraft.text;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.stream.Stream;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.PosArgument;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.NbtDataSource;
import net.minecraft.util.math.BlockPos;
import org.jspecify.annotations.Nullable;

public record BlockNbtDataSource(String rawPos, @Nullable PosArgument pos) implements NbtDataSource
{
    public static final MapCodec<BlockNbtDataSource> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Codec.STRING.fieldOf("block")).forGetter(BlockNbtDataSource::rawPos)).apply((Applicative<BlockNbtDataSource, ?>)instance, BlockNbtDataSource::new));

    public BlockNbtDataSource(String rawPath) {
        this(rawPath, BlockNbtDataSource.parsePos(rawPath));
    }

    private static @Nullable PosArgument parsePos(String string) {
        try {
            return BlockPosArgumentType.blockPos().parse(new StringReader(string));
        } catch (CommandSyntaxException commandSyntaxException) {
            return null;
        }
    }

    @Override
    public Stream<NbtCompound> get(ServerCommandSource source) {
        BlockEntity lv3;
        BlockPos lv2;
        ServerWorld lv;
        if (this.pos != null && (lv = source.getWorld()).isPosLoaded(lv2 = this.pos.toAbsoluteBlockPos(source)) && (lv3 = lv.getBlockEntity(lv2)) != null) {
            return Stream.of(lv3.createNbtWithIdentifyingData(source.getRegistryManager()));
        }
        return Stream.empty();
    }

    public MapCodec<BlockNbtDataSource> getCodec() {
        return CODEC;
    }

    @Override
    public String toString() {
        return "block=" + this.rawPos;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BlockNbtDataSource)) return false;
        BlockNbtDataSource lv = (BlockNbtDataSource)o;
        if (!this.rawPos.equals(lv.rawPos)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return this.rawPos.hashCode();
    }
}

