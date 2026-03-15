/*
 * Internal private/static methods:
 *   Lnet/minecraft/util/dynamic/NullOps;createLongList(Ljava/util/stream/LongStream;)Lnet/minecraft/util/Unit;
 *   Lnet/minecraft/util/dynamic/NullOps;createIntList(Ljava/util/stream/IntStream;)Lnet/minecraft/util/Unit;
 *   Lnet/minecraft/util/dynamic/NullOps;createByteList(Ljava/nio/ByteBuffer;)Lnet/minecraft/util/Unit;
 *   Lnet/minecraft/util/dynamic/NullOps;createList(Ljava/util/stream/Stream;)Lnet/minecraft/util/Unit;
 *   Lnet/minecraft/util/dynamic/NullOps;createMap(Ljava/util/Map;)Lnet/minecraft/util/Unit;
 *   Lnet/minecraft/util/dynamic/NullOps;createMap(Ljava/util/stream/Stream;)Lnet/minecraft/util/Unit;
 *   Lnet/minecraft/util/dynamic/NullOps;mergeToMap(Lnet/minecraft/util/Unit;Lcom/mojang/serialization/MapLike;)Lcom/mojang/serialization/DataResult;
 *   Lnet/minecraft/util/dynamic/NullOps;mergeToMap(Lnet/minecraft/util/Unit;Ljava/util/Map;)Lcom/mojang/serialization/DataResult;
 *   Lnet/minecraft/util/dynamic/NullOps;mergeToMap(Lnet/minecraft/util/Unit;Lnet/minecraft/util/Unit;Lnet/minecraft/util/Unit;)Lcom/mojang/serialization/DataResult;
 *   Lnet/minecraft/util/dynamic/NullOps;mergeToList(Lnet/minecraft/util/Unit;Ljava/util/List;)Lcom/mojang/serialization/DataResult;
 *   Lnet/minecraft/util/dynamic/NullOps;mergeToList(Lnet/minecraft/util/Unit;Lnet/minecraft/util/Unit;)Lcom/mojang/serialization/DataResult;
 *   Lnet/minecraft/util/dynamic/NullOps;createString(Ljava/lang/String;)Lnet/minecraft/util/Unit;
 *   Lnet/minecraft/util/dynamic/NullOps;createBoolean(Z)Lnet/minecraft/util/Unit;
 *   Lnet/minecraft/util/dynamic/NullOps;createDouble(D)Lnet/minecraft/util/Unit;
 *   Lnet/minecraft/util/dynamic/NullOps;createFloat(F)Lnet/minecraft/util/Unit;
 *   Lnet/minecraft/util/dynamic/NullOps;createLong(J)Lnet/minecraft/util/Unit;
 *   Lnet/minecraft/util/dynamic/NullOps;createInt(I)Lnet/minecraft/util/Unit;
 *   Lnet/minecraft/util/dynamic/NullOps;createShort(S)Lnet/minecraft/util/Unit;
 *   Lnet/minecraft/util/dynamic/NullOps;createByte(B)Lnet/minecraft/util/Unit;
 *   Lnet/minecraft/util/dynamic/NullOps;createNumeric(Ljava/lang/Number;)Lnet/minecraft/util/Unit;
 *   Lnet/minecraft/util/dynamic/NullOps;convertTo(Lcom/mojang/serialization/DynamicOps;Lnet/minecraft/util/Unit;)Ljava/lang/Object;
 *   Lnet/minecraft/util/dynamic/NullOps;emptyList()Lnet/minecraft/util/Unit;
 *   Lnet/minecraft/util/dynamic/NullOps;emptyMap()Lnet/minecraft/util/Unit;
 *   Lnet/minecraft/util/dynamic/NullOps;empty()Lnet/minecraft/util/Unit;
 */
package net.minecraft.util.dynamic;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.ListBuilder;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import net.minecraft.util.Unit;
import net.minecraft.util.dynamic.AbstractListBuilder;
import org.jspecify.annotations.Nullable;

public class NullOps
implements DynamicOps<Unit> {
    public static final NullOps INSTANCE = new NullOps();
    private static final MapLike<Unit> field_61219 = new MapLike<Unit>(){

        @Override
        public @Nullable Unit get(Unit arg) {
            return null;
        }

        @Override
        public @Nullable Unit get(String string) {
            return null;
        }

        @Override
        public Stream<Pair<Unit, Unit>> entries() {
            return Stream.empty();
        }

        @Override
        public /* synthetic */ @Nullable Object get(String string) {
            return this.get(string);
        }

        @Override
        public /* synthetic */ @Nullable Object get(Object object) {
            return this.get((Unit)((Object)object));
        }
    };

    private NullOps() {
    }

    @Override
    public <U> U convertTo(DynamicOps<U> dynamicOps, Unit arg) {
        return dynamicOps.empty();
    }

    @Override
    public Unit empty() {
        return Unit.INSTANCE;
    }

    @Override
    public Unit emptyMap() {
        return Unit.INSTANCE;
    }

    @Override
    public Unit emptyList() {
        return Unit.INSTANCE;
    }

    @Override
    public Unit createNumeric(Number number) {
        return Unit.INSTANCE;
    }

    @Override
    public Unit createByte(byte b) {
        return Unit.INSTANCE;
    }

    @Override
    public Unit createShort(short s) {
        return Unit.INSTANCE;
    }

    @Override
    public Unit createInt(int i) {
        return Unit.INSTANCE;
    }

    @Override
    public Unit createLong(long l) {
        return Unit.INSTANCE;
    }

    @Override
    public Unit createFloat(float f) {
        return Unit.INSTANCE;
    }

    @Override
    public Unit createDouble(double d) {
        return Unit.INSTANCE;
    }

    @Override
    public Unit createBoolean(boolean bl) {
        return Unit.INSTANCE;
    }

    @Override
    public Unit createString(String string) {
        return Unit.INSTANCE;
    }

    @Override
    public DataResult<Number> getNumberValue(Unit arg) {
        return DataResult.success(0);
    }

    @Override
    public DataResult<Boolean> getBooleanValue(Unit arg) {
        return DataResult.success(false);
    }

    @Override
    public DataResult<String> getStringValue(Unit arg) {
        return DataResult.success("");
    }

    @Override
    public DataResult<Unit> mergeToList(Unit arg, Unit arg2) {
        return DataResult.success(Unit.INSTANCE);
    }

    @Override
    public DataResult<Unit> mergeToList(Unit arg, List<Unit> list) {
        return DataResult.success(Unit.INSTANCE);
    }

    @Override
    public DataResult<Unit> mergeToMap(Unit arg, Unit arg2, Unit arg3) {
        return DataResult.success(Unit.INSTANCE);
    }

    @Override
    public DataResult<Unit> mergeToMap(Unit arg, Map<Unit, Unit> map) {
        return DataResult.success(Unit.INSTANCE);
    }

    @Override
    public DataResult<Unit> mergeToMap(Unit arg, MapLike<Unit> mapLike) {
        return DataResult.success(Unit.INSTANCE);
    }

    @Override
    public DataResult<Stream<Pair<Unit, Unit>>> getMapValues(Unit arg) {
        return DataResult.success(Stream.empty());
    }

    @Override
    public DataResult<Consumer<BiConsumer<Unit, Unit>>> getMapEntries(Unit arg) {
        return DataResult.success(biConsumer -> {});
    }

    @Override
    public DataResult<MapLike<Unit>> getMap(Unit arg) {
        return DataResult.success(field_61219);
    }

    @Override
    public DataResult<Stream<Unit>> getStream(Unit arg) {
        return DataResult.success(Stream.empty());
    }

    @Override
    public DataResult<Consumer<Consumer<Unit>>> getList(Unit arg) {
        return DataResult.success(consumer -> {});
    }

    @Override
    public DataResult<ByteBuffer> getByteBuffer(Unit arg) {
        return DataResult.success(ByteBuffer.wrap(new byte[0]));
    }

    @Override
    public DataResult<IntStream> getIntStream(Unit arg) {
        return DataResult.success(IntStream.empty());
    }

    @Override
    public DataResult<LongStream> getLongStream(Unit arg) {
        return DataResult.success(LongStream.empty());
    }

    @Override
    public Unit createMap(Stream<Pair<Unit, Unit>> stream) {
        return Unit.INSTANCE;
    }

    @Override
    public Unit createMap(Map<Unit, Unit> map) {
        return Unit.INSTANCE;
    }

    @Override
    public Unit createList(Stream<Unit> stream) {
        return Unit.INSTANCE;
    }

    @Override
    public Unit createByteList(ByteBuffer byteBuffer) {
        return Unit.INSTANCE;
    }

    @Override
    public Unit createIntList(IntStream intStream) {
        return Unit.INSTANCE;
    }

    @Override
    public Unit createLongList(LongStream longStream) {
        return Unit.INSTANCE;
    }

    @Override
    public Unit remove(Unit arg, String string) {
        return arg;
    }

    @Override
    public RecordBuilder<Unit> mapBuilder() {
        return new NullMapBuilder(this);
    }

    @Override
    public ListBuilder<Unit> listBuilder() {
        return new NullListBuilder(this);
    }

    public String toString() {
        return "Null";
    }

    @Override
    public /* synthetic */ Object remove(Object input, String key) {
        return this.remove((Unit)((Object)input), key);
    }

    @Override
    public /* synthetic */ Object createLongList(LongStream stream) {
        return this.createLongList(stream);
    }

    @Override
    public /* synthetic */ DataResult getLongStream(Object input) {
        return this.getLongStream((Unit)((Object)input));
    }

    @Override
    public /* synthetic */ Object createIntList(IntStream stream) {
        return this.createIntList(stream);
    }

    @Override
    public /* synthetic */ DataResult getIntStream(Object input) {
        return this.getIntStream((Unit)((Object)input));
    }

    @Override
    public /* synthetic */ Object createByteList(ByteBuffer buf) {
        return this.createByteList(buf);
    }

    @Override
    public /* synthetic */ DataResult getByteBuffer(Object input) {
        return this.getByteBuffer((Unit)((Object)input));
    }

    @Override
    public /* synthetic */ Object createList(Stream list) {
        return this.createList((Stream<Unit>)list);
    }

    @Override
    public /* synthetic */ DataResult getList(Object input) {
        return this.getList((Unit)((Object)input));
    }

    @Override
    public /* synthetic */ DataResult getStream(Object input) {
        return this.getStream((Unit)((Object)input));
    }

    @Override
    public /* synthetic */ Object createMap(Map map) {
        return this.createMap((Map<Unit, Unit>)map);
    }

    @Override
    public /* synthetic */ DataResult getMap(Object input) {
        return this.getMap((Unit)((Object)input));
    }

    @Override
    public /* synthetic */ Object createMap(Stream map) {
        return this.createMap((Stream<Pair<Unit, Unit>>)map);
    }

    @Override
    public /* synthetic */ DataResult getMapEntries(Object input) {
        return this.getMapEntries((Unit)((Object)input));
    }

    @Override
    public /* synthetic */ DataResult getMapValues(Object input) {
        return this.getMapValues((Unit)((Object)input));
    }

    @Override
    public /* synthetic */ DataResult mergeToMap(Object map, MapLike values) {
        return this.mergeToMap((Unit)((Object)map), (MapLike<Unit>)values);
    }

    @Override
    public /* synthetic */ DataResult mergeToMap(Object map, Map values) {
        return this.mergeToMap((Unit)((Object)map), (Map<Unit, Unit>)values);
    }

    @Override
    public /* synthetic */ DataResult mergeToMap(Object map, Object key, Object value) {
        return this.mergeToMap((Unit)((Object)map), (Unit)((Object)key), (Unit)((Object)value));
    }

    @Override
    public /* synthetic */ DataResult mergeToList(Object list, List values) {
        return this.mergeToList((Unit)((Object)list), (List<Unit>)values);
    }

    @Override
    public /* synthetic */ DataResult mergeToList(Object list, Object value) {
        return this.mergeToList((Unit)((Object)list), (Unit)((Object)value));
    }

    @Override
    public /* synthetic */ Object createString(String string) {
        return this.createString(string);
    }

    @Override
    public /* synthetic */ DataResult getStringValue(Object input) {
        return this.getStringValue((Unit)((Object)input));
    }

    @Override
    public /* synthetic */ Object createBoolean(boolean bl) {
        return this.createBoolean(bl);
    }

    @Override
    public /* synthetic */ DataResult getBooleanValue(Object input) {
        return this.getBooleanValue((Unit)((Object)input));
    }

    @Override
    public /* synthetic */ Object createDouble(double d) {
        return this.createDouble(d);
    }

    @Override
    public /* synthetic */ Object createFloat(float f) {
        return this.createFloat(f);
    }

    @Override
    public /* synthetic */ Object createLong(long l) {
        return this.createLong(l);
    }

    @Override
    public /* synthetic */ Object createInt(int i) {
        return this.createInt(i);
    }

    @Override
    public /* synthetic */ Object createShort(short s) {
        return this.createShort(s);
    }

    @Override
    public /* synthetic */ Object createByte(byte b) {
        return this.createByte(b);
    }

    @Override
    public /* synthetic */ Object createNumeric(Number number) {
        return this.createNumeric(number);
    }

    @Override
    public /* synthetic */ DataResult getNumberValue(Object input) {
        return this.getNumberValue((Unit)((Object)input));
    }

    @Override
    public /* synthetic */ Object convertTo(DynamicOps ops, Object unit) {
        return this.convertTo(ops, (Unit)((Object)unit));
    }

    @Override
    public /* synthetic */ Object emptyList() {
        return this.emptyList();
    }

    @Override
    public /* synthetic */ Object emptyMap() {
        return this.emptyMap();
    }

    @Override
    public /* synthetic */ Object empty() {
        return this.empty();
    }

    static final class NullMapBuilder
    extends RecordBuilder.AbstractUniversalBuilder<Unit, Unit> {
        public NullMapBuilder(DynamicOps<Unit> ops) {
            super(ops);
        }

        @Override
        protected Unit initBuilder() {
            return Unit.INSTANCE;
        }

        @Override
        protected Unit append(Unit arg, Unit arg2, Unit arg3) {
            return arg3;
        }

        @Override
        protected DataResult<Unit> build(Unit arg, Unit arg2) {
            return DataResult.success(arg2);
        }

        @Override
        protected /* synthetic */ Object append(Object key, Object value, Object builder) {
            return this.append((Unit)((Object)key), (Unit)((Object)value), (Unit)((Object)builder));
        }

        @Override
        protected /* synthetic */ DataResult build(Object builder, Object prefix) {
            return this.build((Unit)((Object)builder), (Unit)((Object)prefix));
        }

        @Override
        protected /* synthetic */ Object initBuilder() {
            return this.initBuilder();
        }
    }

    static final class NullListBuilder
    extends AbstractListBuilder<Unit, Unit> {
        public NullListBuilder(DynamicOps<Unit> dynamicOps) {
            super(dynamicOps);
        }

        @Override
        protected Unit initBuilder() {
            return Unit.INSTANCE;
        }

        @Override
        protected Unit add(Unit arg, Unit arg2) {
            return arg;
        }

        @Override
        protected DataResult<Unit> build(Unit arg, Unit arg2) {
            return DataResult.success(arg);
        }

        @Override
        protected /* synthetic */ Object initBuilder() {
            return this.initBuilder();
        }
    }
}

