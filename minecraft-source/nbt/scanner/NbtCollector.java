/*
 * External method calls:
 *   Lnet/minecraft/nbt/scanner/NbtCollector$Node;append(Lnet/minecraft/nbt/NbtElement;)V
 *   Lnet/minecraft/nbt/NbtString;of(Ljava/lang/String;)Lnet/minecraft/nbt/NbtString;
 *   Lnet/minecraft/nbt/NbtByte;of(B)Lnet/minecraft/nbt/NbtByte;
 *   Lnet/minecraft/nbt/NbtShort;of(S)Lnet/minecraft/nbt/NbtShort;
 *   Lnet/minecraft/nbt/NbtInt;of(I)Lnet/minecraft/nbt/NbtInt;
 *   Lnet/minecraft/nbt/NbtLong;of(J)Lnet/minecraft/nbt/NbtLong;
 *   Lnet/minecraft/nbt/NbtFloat;of(F)Lnet/minecraft/nbt/NbtFloat;
 *   Lnet/minecraft/nbt/NbtDouble;of(D)Lnet/minecraft/nbt/NbtDouble;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/nbt/scanner/NbtCollector;append(Lnet/minecraft/nbt/NbtElement;)V
 *   Lnet/minecraft/nbt/scanner/NbtCollector;pushStack(Lnet/minecraft/nbt/NbtType;)V
 */
package net.minecraft.nbt.scanner;

import java.util.ArrayDeque;
import java.util.Deque;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtByteArray;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtEnd;
import net.minecraft.nbt.NbtFloat;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtIntArray;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtLong;
import net.minecraft.nbt.NbtLongArray;
import net.minecraft.nbt.NbtShort;
import net.minecraft.nbt.NbtString;
import net.minecraft.nbt.NbtType;
import net.minecraft.nbt.scanner.NbtScanner;
import org.jspecify.annotations.Nullable;

public class NbtCollector
implements NbtScanner {
    private final Deque<Node> queue = new ArrayDeque<Node>();

    public NbtCollector() {
        this.queue.addLast(new RootNode());
    }

    public @Nullable NbtElement getRoot() {
        return this.queue.getFirst().getValue();
    }

    protected int getDepth() {
        return this.queue.size() - 1;
    }

    private void append(NbtElement nbt) {
        this.queue.getLast().append(nbt);
    }

    @Override
    public NbtScanner.Result visitEnd() {
        this.append(NbtEnd.INSTANCE);
        return NbtScanner.Result.CONTINUE;
    }

    @Override
    public NbtScanner.Result visitString(String value) {
        this.append(NbtString.of(value));
        return NbtScanner.Result.CONTINUE;
    }

    @Override
    public NbtScanner.Result visitByte(byte value) {
        this.append(NbtByte.of(value));
        return NbtScanner.Result.CONTINUE;
    }

    @Override
    public NbtScanner.Result visitShort(short value) {
        this.append(NbtShort.of(value));
        return NbtScanner.Result.CONTINUE;
    }

    @Override
    public NbtScanner.Result visitInt(int value) {
        this.append(NbtInt.of(value));
        return NbtScanner.Result.CONTINUE;
    }

    @Override
    public NbtScanner.Result visitLong(long value) {
        this.append(NbtLong.of(value));
        return NbtScanner.Result.CONTINUE;
    }

    @Override
    public NbtScanner.Result visitFloat(float value) {
        this.append(NbtFloat.of(value));
        return NbtScanner.Result.CONTINUE;
    }

    @Override
    public NbtScanner.Result visitDouble(double value) {
        this.append(NbtDouble.of(value));
        return NbtScanner.Result.CONTINUE;
    }

    @Override
    public NbtScanner.Result visitByteArray(byte[] value) {
        this.append(new NbtByteArray(value));
        return NbtScanner.Result.CONTINUE;
    }

    @Override
    public NbtScanner.Result visitIntArray(int[] value) {
        this.append(new NbtIntArray(value));
        return NbtScanner.Result.CONTINUE;
    }

    @Override
    public NbtScanner.Result visitLongArray(long[] value) {
        this.append(new NbtLongArray(value));
        return NbtScanner.Result.CONTINUE;
    }

    @Override
    public NbtScanner.Result visitListMeta(NbtType<?> entryType, int length) {
        return NbtScanner.Result.CONTINUE;
    }

    @Override
    public NbtScanner.NestedResult startListItem(NbtType<?> type, int index) {
        this.pushStack(type);
        return NbtScanner.NestedResult.ENTER;
    }

    @Override
    public NbtScanner.NestedResult visitSubNbtType(NbtType<?> type) {
        return NbtScanner.NestedResult.ENTER;
    }

    @Override
    public NbtScanner.NestedResult startSubNbt(NbtType<?> type, String key) {
        this.queue.getLast().setKey(key);
        this.pushStack(type);
        return NbtScanner.NestedResult.ENTER;
    }

    private void pushStack(NbtType<?> type) {
        if (type == NbtList.TYPE) {
            this.queue.addLast(new ListNode());
        } else if (type == NbtCompound.TYPE) {
            this.queue.addLast(new CompoundNode());
        }
    }

    @Override
    public NbtScanner.Result endNested() {
        Node lv = this.queue.removeLast();
        NbtElement lv2 = lv.getValue();
        if (lv2 != null) {
            this.queue.getLast().append(lv2);
        }
        return NbtScanner.Result.CONTINUE;
    }

    @Override
    public NbtScanner.Result start(NbtType<?> rootType) {
        this.pushStack(rootType);
        return NbtScanner.Result.CONTINUE;
    }

    static class RootNode
    implements Node {
        private @Nullable NbtElement value;

        RootNode() {
        }

        @Override
        public void append(NbtElement value) {
            this.value = value;
        }

        @Override
        public @Nullable NbtElement getValue() {
            return this.value;
        }
    }

    static interface Node {
        default public void setKey(String key) {
        }

        public void append(NbtElement var1);

        public @Nullable NbtElement getValue();
    }

    static class ListNode
    implements Node {
        private final NbtList value = new NbtList();

        ListNode() {
        }

        @Override
        public void append(NbtElement value) {
            this.value.unwrapAndAdd(value);
        }

        @Override
        public NbtElement getValue() {
            return this.value;
        }
    }

    static class CompoundNode
    implements Node {
        private final NbtCompound value = new NbtCompound();
        private String key = "";

        CompoundNode() {
        }

        @Override
        public void setKey(String key) {
            this.key = key;
        }

        @Override
        public void append(NbtElement value) {
            this.value.put(this.key, value);
        }

        @Override
        public NbtElement getValue() {
            return this.value;
        }
    }
}

