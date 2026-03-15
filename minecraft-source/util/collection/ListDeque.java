/*
 * Internal private/static methods:
 *   Lnet/minecraft/util/collection/ListDeque;offerLast(Ljava/lang/Object;)Z
 *   Lnet/minecraft/util/collection/ListDeque;removeFirst()Ljava/lang/Object;
 *   Lnet/minecraft/util/collection/ListDeque;pollFirst()Ljava/lang/Object;
 *   Lnet/minecraft/util/collection/ListDeque;peekFirst()Ljava/lang/Object;
 *   Lnet/minecraft/util/collection/ListDeque;addFirst(Ljava/lang/Object;)V
 *   Lnet/minecraft/util/collection/ListDeque;reversed()Lnet/minecraft/util/collection/ListDeque;
 */
package net.minecraft.util.collection;

import java.io.Serializable;
import java.util.Deque;
import java.util.List;
import java.util.RandomAccess;
import java.util.SequencedCollection;
import org.jspecify.annotations.Nullable;

public interface ListDeque<T>
extends Serializable,
Cloneable,
Deque<T>,
List<T>,
RandomAccess {
    @Override
    public ListDeque<T> reversed();

    @Override
    public T getFirst();

    @Override
    public T getLast();

    @Override
    public void addFirst(T var1);

    @Override
    public void addLast(T var1);

    @Override
    public T removeFirst();

    @Override
    public T removeLast();

    @Override
    default public boolean offer(T object) {
        return this.offerLast(object);
    }

    @Override
    default public T remove() {
        return this.removeFirst();
    }

    @Override
    default public @Nullable T poll() {
        return (T)this.pollFirst();
    }

    @Override
    default public T element() {
        return this.getFirst();
    }

    @Override
    default public @Nullable T peek() {
        return (T)this.peekFirst();
    }

    @Override
    default public void push(T object) {
        this.addFirst(object);
    }

    @Override
    default public T pop() {
        return this.removeFirst();
    }

    @Override
    default public /* synthetic */ List reversed() {
        return this.reversed();
    }

    @Override
    default public /* synthetic */ SequencedCollection reversed() {
        return this.reversed();
    }

    @Override
    default public /* synthetic */ Deque reversed() {
        return this.reversed();
    }
}

