/*
 * External method calls:
 *   Lnet/minecraft/util/StringIdentifiable;createCodec(Ljava/util/function/Supplier;)Lnet/minecraft/util/StringIdentifiable$EnumCodec;
 *   Lnet/minecraft/util/function/ValueLists;createIndexToValueFunction(Ljava/util/function/ToIntFunction;[Ljava/lang/Object;Lnet/minecraft/util/function/ValueLists$OutOfBoundsHandling;)Ljava/util/function/IntFunction;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/command/permission/PermissionLevel;method_75029()[Lnet/minecraft/command/permission/PermissionLevel;
 *   Lnet/minecraft/command/permission/PermissionLevel;values()[Lnet/minecraft/command/permission/PermissionLevel;
 */
package net.minecraft.command.permission;

import com.mojang.serialization.Codec;
import java.util.function.IntFunction;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;

public enum PermissionLevel implements StringIdentifiable
{
    ALL("all", 0),
    MODERATORS("moderators", 1),
    GAMEMASTERS("gamemasters", 2),
    ADMINS("admins", 3),
    OWNERS("owners", 4);

    public static final Codec<PermissionLevel> CODEC;
    private static final IntFunction<PermissionLevel> BY_LEVEL;
    public static final Codec<PermissionLevel> NUMERIC_CODEC;
    private final String name;
    private final int level;

    private PermissionLevel(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public boolean isAtLeast(PermissionLevel other) {
        return this.level >= other.level;
    }

    public static PermissionLevel fromLevel(int level) {
        return BY_LEVEL.apply(level);
    }

    public int getLevel() {
        return this.level;
    }

    @Override
    public String asString() {
        return this.name;
    }

    static {
        CODEC = StringIdentifiable.createCodec(PermissionLevel::values);
        BY_LEVEL = ValueLists.createIndexToValueFunction(level -> level.level, PermissionLevel.values(), ValueLists.OutOfBoundsHandling.CLAMP);
        NUMERIC_CODEC = Codec.INT.xmap(BY_LEVEL::apply, level -> level.level);
    }
}

