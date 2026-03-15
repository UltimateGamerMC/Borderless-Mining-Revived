/*
 * External method calls:
 *   Lnet/minecraft/nbt/visitor/StringNbtWriter;visitEnd(Lnet/minecraft/nbt/NbtEnd;)V
 *   Lnet/minecraft/nbt/visitor/NbtElementVisitor;visitEnd(Lnet/minecraft/nbt/NbtEnd;)V
 *   Lnet/minecraft/nbt/scanner/NbtScanner;visitEnd()Lnet/minecraft/nbt/scanner/NbtScanner$Result;
 */
package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtSizeTracker;
import net.minecraft.nbt.NbtType;
import net.minecraft.nbt.scanner.NbtScanner;
import net.minecraft.nbt.visitor.NbtElementVisitor;
import net.minecraft.nbt.visitor.StringNbtWriter;

public final class NbtEnd
implements NbtElement {
    private static final int SIZE = 8;
    public static final NbtType<NbtEnd> TYPE = new NbtType<NbtEnd>(){

        @Override
        public NbtEnd read(DataInput dataInput, NbtSizeTracker arg) {
            arg.add(8L);
            return INSTANCE;
        }

        @Override
        public NbtScanner.Result doAccept(DataInput input, NbtScanner visitor, NbtSizeTracker tracker) {
            tracker.add(8L);
            return visitor.visitEnd();
        }

        @Override
        public void skip(DataInput input, int count, NbtSizeTracker tracker) {
        }

        @Override
        public void skip(DataInput input, NbtSizeTracker tracker) {
        }

        @Override
        public String getCrashReportName() {
            return "END";
        }

        @Override
        public String getCommandFeedbackName() {
            return "TAG_End";
        }

        @Override
        public /* synthetic */ NbtElement read(DataInput input, NbtSizeTracker tracker) throws IOException {
            return this.read(input, tracker);
        }
    };
    public static final NbtEnd INSTANCE = new NbtEnd();

    private NbtEnd() {
    }

    @Override
    public void write(DataOutput output) throws IOException {
    }

    @Override
    public int getSizeInBytes() {
        return 8;
    }

    @Override
    public byte getType() {
        return NbtElement.END_TYPE;
    }

    public NbtType<NbtEnd> getNbtType() {
        return TYPE;
    }

    @Override
    public String toString() {
        StringNbtWriter lv = new StringNbtWriter();
        lv.visitEnd(this);
        return lv.getString();
    }

    @Override
    public NbtEnd copy() {
        return this;
    }

    @Override
    public void accept(NbtElementVisitor visitor) {
        visitor.visitEnd(this);
    }

    @Override
    public NbtScanner.Result doAccept(NbtScanner visitor) {
        return visitor.visitEnd();
    }

    @Override
    public /* synthetic */ NbtElement copy() {
        return this.copy();
    }
}

