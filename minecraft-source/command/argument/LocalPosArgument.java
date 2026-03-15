/*
 * External method calls:
 *   Lnet/minecraft/command/argument/EntityAnchorArgumentType$EntityAnchor;positionAt(Lnet/minecraft/server/command/ServerCommandSource;)Lnet/minecraft/util/math/Vec3d;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/command/argument/LocalPosArgument;readCoordinate(Lcom/mojang/brigadier/StringReader;I)D
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

public record LocalPosArgument(double x, double y, double z) implements PosArgument
{
    public static final char CARET = '^';

    @Override
    public Vec3d getPos(ServerCommandSource source) {
        Vec3d lv = source.getEntityAnchor().positionAt(source);
        return Vec3d.transformLocalPos(source.getRotation(), new Vec3d(this.x, this.y, this.z)).add(lv.x, lv.y, lv.z);
    }

    @Override
    public Vec2f getRotation(ServerCommandSource source) {
        return Vec2f.ZERO;
    }

    @Override
    public boolean isXRelative() {
        return true;
    }

    @Override
    public boolean isYRelative() {
        return true;
    }

    @Override
    public boolean isZRelative() {
        return true;
    }

    public static LocalPosArgument parse(StringReader reader) throws CommandSyntaxException {
        int i = reader.getCursor();
        double d = LocalPosArgument.readCoordinate(reader, i);
        if (!reader.canRead() || reader.peek() != ' ') {
            reader.setCursor(i);
            throw Vec3ArgumentType.INCOMPLETE_EXCEPTION.createWithContext(reader);
        }
        reader.skip();
        double e = LocalPosArgument.readCoordinate(reader, i);
        if (!reader.canRead() || reader.peek() != ' ') {
            reader.setCursor(i);
            throw Vec3ArgumentType.INCOMPLETE_EXCEPTION.createWithContext(reader);
        }
        reader.skip();
        double f = LocalPosArgument.readCoordinate(reader, i);
        return new LocalPosArgument(d, e, f);
    }

    private static double readCoordinate(StringReader reader, int startingCursorPos) throws CommandSyntaxException {
        if (!reader.canRead()) {
            throw CoordinateArgument.MISSING_COORDINATE.createWithContext(reader);
        }
        if (reader.peek() != '^') {
            reader.setCursor(startingCursorPos);
            throw Vec3ArgumentType.MIXED_COORDINATE_EXCEPTION.createWithContext(reader);
        }
        reader.skip();
        return reader.canRead() && reader.peek() != ' ' ? reader.readDouble() : 0.0;
    }
}

