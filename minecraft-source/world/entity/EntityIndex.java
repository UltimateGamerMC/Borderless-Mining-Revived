/*
 * External method calls:
 *   Lnet/minecraft/util/TypeFilter;downcast(Ljava/lang/Object;)Ljava/lang/Object;
 *   Lnet/minecraft/util/function/LazyIterationConsumer;accept(Ljava/lang/Object;)Lnet/minecraft/util/function/LazyIterationConsumer$NextIteration;
 */
package net.minecraft.world.entity;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.function.LazyIterationConsumer;
import net.minecraft.world.entity.EntityLike;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class EntityIndex<T extends EntityLike> {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final Int2ObjectMap<T> idToEntity = new Int2ObjectLinkedOpenHashMap<T>();
    private final Map<UUID, T> uuidToEntity = Maps.newHashMap();

    public <U extends T> void forEach(TypeFilter<T, U> filter, LazyIterationConsumer<U> consumer) {
        for (EntityLike lv : this.idToEntity.values()) {
            EntityLike lv2 = (EntityLike)filter.downcast(lv);
            if (lv2 == null || !consumer.accept(lv2).shouldAbort()) continue;
            return;
        }
    }

    public Iterable<T> iterate() {
        return Iterables.unmodifiableIterable(this.idToEntity.values());
    }

    public void add(T entity) {
        UUID uUID = entity.getUuid();
        if (this.uuidToEntity.containsKey(uUID)) {
            LOGGER.warn("Duplicate entity UUID {}: {}", (Object)uUID, (Object)entity);
            return;
        }
        this.uuidToEntity.put(uUID, entity);
        this.idToEntity.put(entity.getId(), entity);
    }

    public void remove(T entity) {
        this.uuidToEntity.remove(entity.getUuid());
        this.idToEntity.remove(entity.getId());
    }

    public @Nullable T get(int id) {
        return (T)((EntityLike)this.idToEntity.get(id));
    }

    public @Nullable T get(UUID uuid) {
        return (T)((EntityLike)this.uuidToEntity.get(uuid));
    }

    public int size() {
        return this.uuidToEntity.size();
    }
}

