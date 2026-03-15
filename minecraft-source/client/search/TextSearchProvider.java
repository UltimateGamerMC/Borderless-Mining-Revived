/*
 * External method calls:
 *   Lnet/minecraft/client/search/SearchProvider;plainText(Ljava/util/List;Ljava/util/function/Function;)Lnet/minecraft/client/search/SearchProvider;
 *   Lnet/minecraft/client/search/SearchProvider;findAll(Ljava/lang/String;)Ljava/util/List;
 *   Lnet/minecraft/client/search/IdentifierSearcher;searchNamespace(Ljava/lang/String;)Ljava/util/List;
 *   Lnet/minecraft/client/search/IdentifierSearcher;searchPath(Ljava/lang/String;)Ljava/util/List;
 */
package net.minecraft.client.search;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.search.IdentifierSearchProvider;
import net.minecraft.client.search.IdentifierSearchableIterator;
import net.minecraft.client.search.SearchProvider;
import net.minecraft.client.search.TextSearchableIterator;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class TextSearchProvider<T>
extends IdentifierSearchProvider<T> {
    private final SearchProvider<T> textSearcher;

    public TextSearchProvider(Function<T, Stream<String>> textsGetter, Function<T, Stream<Identifier>> identifiersGetter, List<T> values) {
        super(identifiersGetter, values);
        this.textSearcher = SearchProvider.plainText(values, textsGetter);
    }

    @Override
    protected List<T> search(String text) {
        return this.textSearcher.findAll(text);
    }

    @Override
    protected List<T> search(String namespace, String path) {
        List list = this.idSearcher.searchNamespace(namespace);
        List list2 = this.idSearcher.searchPath(path);
        List<T> list3 = this.textSearcher.findAll(path);
        TextSearchableIterator iterator = new TextSearchableIterator(list2.iterator(), list3.iterator(), this.lastIndexComparator);
        return ImmutableList.copyOf(new IdentifierSearchableIterator(list.iterator(), iterator, this.lastIndexComparator));
    }
}

