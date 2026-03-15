package net.fabricmc.fabric.api.util;

import net.minecraft.nbt.Tag;

/**
 * NBT type ID constants. Useful for filtering by value type in a few cases.
 *
 * <p>For the current list of types, check with {@link Tag}.
 *
 * @see net.minecraft.nbt.TagTypes#getType(int)
 * @deprecated Use the constants in {@link Tag} instead.
 */
@Deprecated
public final class NbtType {
	public static final int END = 0;
	public static final int BYTE = 1;
	public static final int SHORT = 2;
	public static final int INT = 3;
	public static final int LONG = 4;
	public static final int FLOAT = 5;
	public static final int DOUBLE = 6;
	public static final int BYTE_ARRAY = 7;
	public static final int STRING = 8;
	public static final int LIST = 9;
	public static final int COMPOUND = 10;
	public static final int INT_ARRAY = 11;
	public static final int LONG_ARRAY = 12;

	private NbtType() { }
}
