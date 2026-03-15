/*
 * External method calls:
 *   Lnet/minecraft/util/Util;lastIndexGetter(Ljava/util/List;)Ljava/util/function/ToIntFunction;
 *   Lnet/minecraft/client/search/IdentifierSearcher;of(Ljava/util/List;Ljava/util/function/Function;)Lnet/minecraft/client/search/IdentifierSearcher;
 *   Lnet/minecraft/client/search/IdentifierSearcher;searchPath(Ljava/lang/String;)Ljava/util/List;
 *   Lnet/minecraft/client/search/IdentifierSearcher;searchNamespace(Ljava/lang/String;)Ljava/util/List;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/search/IdentifierSearchProvider;search(Ljava/lang/String;)Ljava/util/List;
 *   Lnet/minecraft/client/search/IdentifierSearchProvider;search(Ljava/lang/String;Ljava/lang/String;)Ljava/util/List;
 */
package net.minecraft.client.search;

import com.google.common.collect.ImmutableList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.search.IdentifierSearchableIterator;
import net.minecraft.client.search.IdentifierSearcher;
import net.minecraft.client.search.SearchProvider;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

@Environment(value=EnvType.CLIENT)
public class IdentifierSearchProvider<T>
implements SearchProvider<T> {
    protected final Comparator<T> lastIndexComparator;
    protected final IdentifierSearcher<T> idSearcher;

    public IdentifierSearchProvider(Function<T, Stream<Identifier>> identifiersGetter, List<T> values) {
        ToIntFunction<T> toIntFunction = Util.lastIndexGetter(values);
        this.lastIndexComparator = Comparator.comparingInt(toIntFunction);
        this.idSearcher = IdentifierSearcher.of(values, identifiersGetter);
    }

    @Override
    public List<T> findAll(String text) {
        int i = text.indexOf(58);
        if (i == -1) {
            return this.search(text);
        }
        return this.search(text.substring(0, i).trim(), text.substring(i + 1).trim());
    }

    protected List<T> search(String text) {
        return this.idSearcher.searchPath(text);
    }

    protected List<T> search(String namespace, String path) {
        List<T> list = this.idSearcher.searchNamespace(namespace);
        List<T> list2 = this.idSearcher.searchPath(path);
        return ImmutableList.copyOf(new IdentifierSearchableIterator<T>(list.iterator(), list2.iterator(), this.lastIndexComparator));
    }
}

