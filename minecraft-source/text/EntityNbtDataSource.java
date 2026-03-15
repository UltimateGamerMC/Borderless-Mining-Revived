/*
 * External method calls:
 *   Lnet/minecraft/command/EntitySelectorReader;read()Lnet/minecraft/command/EntitySelector;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/text/EntityNbtDataSource;parseSelector(Ljava/lang/String;)Lnet/minecraft/command/EntitySelector;
 */
package net.minecraft.text;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.EntitySelectorReader;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.NbtPredicate;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.NbtDataSource;
import org.jspecify.annotations.Nullable;

public record EntityNbtDataSource(String rawSelector, @Nullable EntitySelector selector) implements NbtDataSource
{
    public static final MapCodec<EntityNbtDataSource> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Codec.STRING.fieldOf("entity")).forGetter(EntityNbtDataSource::rawSelector)).apply((Applicative<EntityNbtDataSource, ?>)instance, EntityNbtDataSource::new));

    public EntityNbtDataSource(String rawPath) {
        this(rawPath, EntityNbtDataSource.parseSelector(rawPath));
    }

    private static @Nullable EntitySelector parseSelector(String rawSelector) {
        try {
            EntitySelectorReader lv = new EntitySelectorReader(new StringReader(rawSelector), true);
            return lv.read();
        } catch (CommandSyntaxException commandSyntaxException) {
            return null;
        }
    }

    @Override
    public Stream<NbtCompound> get(ServerCommandSource source) throws CommandSyntaxException {
        if (this.selector != null) {
            List<? extends Entity> list = this.selector.getEntities(source);
            return list.stream().map(NbtPredicate::entityToNbt);
        }
        return Stream.empty();
    }

    public MapCodec<EntityNbtDataSource> getCodec() {
        return CODEC;
    }

    @Override
    public String toString() {
        return "entity=" + this.rawSelector;
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
        if (!(o instanceof EntityNbtDataSource)) return false;
        EntityNbtDataSource lv = (EntityNbtDataSource)o;
        if (!this.rawSelector.equals(lv.rawSelector)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return this.rawSelector.hashCode();
    }
}

