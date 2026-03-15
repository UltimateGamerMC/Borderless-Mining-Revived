/*
 * External method calls:
 *   Lnet/minecraft/command/argument/CoordinateArgument;toAbsoluteCoordinate(D)D
 *   Lnet/minecraft/command/argument/CoordinateArgument;parse(Lcom/mojang/brigadier/StringReader;)Lnet/minecraft/command/argument/CoordinateArgument;
 *   Lnet/minecraft/command/argument/CoordinateArgument;parse(Lcom/mojang/brigadier/StringReader;Z)Lnet/minecraft/command/argument/CoordinateArgument;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/command/argument/DefaultPosArgument;absolute(Lnet/minecraft/util/math/Vec2f;)Lnet/minecraft/command/argument/DefaultPosArgument;
 */
package net.minecraft.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.CoordinateArgument;
import net.minecraft.command.argument.PosArgument;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public record DefaultPosArgument(CoordinateArgument x, CoordinateArgument y, CoordinateArgument z) implements PosArgument
{
    public static final DefaultPosArgument DEFAULT_ROTATION = DefaultPosArgument.absolute(new Vec2f(0.0f, 0.0f));

    @Override
    public Vec3d getPos(ServerCommandSource source) {
        Vec3d lv = source.getPosition();
        return new Vec3d(this.x.toAbsoluteCoordinate(lv.x), this.y.toAbsoluteCoordinate(lv.y), this.z.toAbsoluteCoordinate(lv.z));
    }

    @Override
    public Vec2f getRotation(ServerCommandSource source) {
        Vec2f lv = source.getRotation();
        return new Vec2f((float)this.x.toAbsoluteCoordinate(lv.x), (float)this.y.toAbsoluteCoordinate(lv.y));
    }

    @Override
    public boolean isXRelative() {
        return this.x.isRelative();
    }

    @Override
    public boolean isYRelative() {
        return this.y.isRelative();
    }

    @Override
    public boolean isZRelative() {
        return this.z.isRelative();
    }

    public static DefaultPosArgument parse(StringReader reader) throws CommandSyntaxException {
        int i = reader.getCursor();
        CoordinateArgument lv = CoordinateArgument.parse(reader);
        if (!reader.canRead() || reader.peek() != ' ') {
            reader.setCursor(i);
            throw Vec3ArgumentType.INCOMPLETE_EXCEPTION.createWithContext(reader);
        }
        reader.skip();
        CoordinateArgument lv2 = CoordinateArgument.parse(reader);
        if (!reader.canRead() || reader.peek() != ' ') {
            reader.setCursor(i);
            throw Vec3ArgumentType.INCOMPLETE_EXCEPTION.createWithContext(reader);
        }
        reader.skip();
        CoordinateArgument lv3 = CoordinateArgument.parse(reader);
        return new DefaultPosArgument(lv, lv2, lv3);
    }

    public static DefaultPosArgument parse(StringReader reader, boolean centerIntegers) throws CommandSyntaxException {
        int i = reader.getCursor();
        CoordinateArgument lv = CoordinateArgument.parse(reader, centerIntegers);
        if (!reader.canRead() || reader.peek() != ' ') {
            reader.setCursor(i);
            throw Vec3ArgumentType.INCOMPLETE_EXCEPTION.createWithContext(reader);
        }
        reader.skip();
        CoordinateArgument lv2 = CoordinateArgument.parse(reader, false);
        if (!reader.canRead() || reader.peek() != ' ') {
            reader.setCursor(i);
            throw Vec3ArgumentType.INCOMPLETE_EXCEPTION.createWithContext(reader);
        }
        reader.skip();
        CoordinateArgument lv3 = CoordinateArgument.parse(reader, centerIntegers);
        return new DefaultPosArgument(lv, lv2, lv3);
    }

    public static DefaultPosArgument absolute(double x, double y, double z) {
        return new DefaultPosArgument(new CoordinateArgument(false, x), new CoordinateArgument(false, y), new CoordinateArgument(false, z));
    }

    public static DefaultPosArgument absolute(Vec2f vec) {
        return new DefaultPosArgument(new CoordinateArgument(false, vec.x), new CoordinateArgument(false, vec.y), new CoordinateArgument(true, 0.0));
    }
}

