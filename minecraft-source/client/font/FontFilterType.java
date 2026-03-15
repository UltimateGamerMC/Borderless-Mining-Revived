/*
 * External method calls:
 *   Lnet/minecraft/util/StringIdentifiable;createCodec(Ljava/util/function/Supplier;)Lnet/minecraft/util/StringIdentifiable$EnumCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/font/FontFilterType;method_57030()[Lnet/minecraft/client/font/FontFilterType;
 */
package net.minecraft.client.font;

import com.mojang.serialization.Codec;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.StringIdentifiable;

@Environment(value=EnvType.CLIENT)
public enum FontFilterType implements StringIdentifiable
{
    UNIFORM("uniform"),
    JAPANESE_VARIANTS("jp");

    public static final Codec<FontFilterType> CODEC;
    private final String id;

    private FontFilterType(String id) {
        this.id = id;
    }

    @Override
    public String asString() {
        return this.id;
    }

    static {
        CODEC = StringIdentifiable.createCodec(FontFilterType::values);
    }

    @Environment(value=EnvType.CLIENT)
    public static class FilterMap {
        private final Map<FontFilterType, Boolean> activeFilters;
        public static final Codec<FilterMap> CODEC = Codec.unboundedMap(CODEC, Codec.BOOL).xmap(FilterMap::new, filterType -> filterType.activeFilters);
        public static final FilterMap NO_FILTER = new FilterMap(Map.of());

        public FilterMap(Map<FontFilterType, Boolean> activeFilters) {
            this.activeFilters = activeFilters;
        }

        public boolean isAllowed(Set<FontFilterType> activeFilters) {
            for (Map.Entry<FontFilterType, Boolean> entry : this.activeFilters.entrySet()) {
                if (activeFilters.contains(entry.getKey()) == entry.getValue().booleanValue()) continue;
                return false;
            }
            return true;
        }

        public FilterMap apply(FilterMap activeFilters) {
            HashMap<FontFilterType, Boolean> map = new HashMap<FontFilterType, Boolean>(activeFilters.activeFilters);
            map.putAll(this.activeFilters);
            return new FilterMap(Map.copyOf(map));
        }
    }
}

