/*
 * External method calls:
 *   Lnet/minecraft/world/entity/EntityIndex;iterate()Ljava/lang/Iterable;
 *   Lnet/minecraft/world/entity/EntityIndex;forEach(Lnet/minecraft/util/TypeFilter;Lnet/minecraft/util/function/LazyIterationConsumer;)V
 *   Lnet/minecraft/util/function/LazyIterationConsumer;forConsumer(Ljava/util/function/Consumer;)Lnet/minecraft/util/function/LazyIterationConsumer;
 *   Lnet/minecraft/world/entity/SectionedEntityCache;forEachIntersects(Lnet/minecraft/util/math/Box;Lnet/minecraft/util/function/LazyIterationConsumer;)V
 *   Lnet/minecraft/world/entity/SectionedEntityCache;forEachIntersects(Lnet/minecraft/util/TypeFilter;Lnet/minecraft/util/math/Box;Lnet/minecraft/util/function/LazyIterationConsumer;)V
 */
package net.minecraft.world.entity;

import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.function.LazyIterationConsumer;
import net.minecraft.util.math.Box;
import net.minecraft.world.entity.EntityIndex;
import net.minecraft.world.entity.EntityLike;
import net.minecraft.world.entity.EntityLookup;
import net.minecraft.world.entity.SectionedEntityCache;
import org.jspecify.annotations.Nullable;

public class SimpleEntityLookup<T extends EntityLike>
implements EntityLookup<T> {
    private final EntityIndex<T> index;
    private final SectionedEntityCache<T> cache;

    public SimpleEntityLookup(EntityIndex<T> index, SectionedEntityCache<T> cache) {
        this.index = index;
        this.cache = cache;
    }

    @Override
    public @Nullable T get(int id) {
        return this.index.get(id);
    }

    @Override
    public @Nullable T get(UUID uuid) {
        return this.index.get(uuid);
    }

    @Override
    public Iterable<T> iterate() {
        return this.index.iterate();
    }

    @Override
    public <U extends T> void forEach(TypeFilter<T, U> filter, LazyIterationConsumer<U> consumer) {
        this.index.forEach(filter, consumer);
    }

    @Override
    public void forEachIntersects(Box box, Consumer<T> action) {
        this.cache.forEachIntersects(box, LazyIterationConsumer.forConsumer(action));
    }

    @Override
    public <U extends T> void forEachIntersects(TypeFilter<T, U> filter, Box box, LazyIterationConsumer<U> consumer) {
        this.cache.forEachIntersects(filter, box, consumer);
    }
}

