/*
 * External method calls:
 *   Lnet/minecraft/command/permission/Permission$Atom;id()Lnet/minecraft/util/Identifier;
 */
package net.minecraft.command.permission;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.command.permission.PermissionLevel;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public interface Permission {
    public static final Codec<Permission> UNABBREVIATED_CODEC = Registries.PERMISSION_TYPE.getCodec().dispatch(Permission::getCodec, codec -> codec);
    public static final Codec<Permission> CODEC = Codec.either(UNABBREVIATED_CODEC, Identifier.CODEC).xmap(either -> either.map(perm -> perm, Atom::of), perm -> {
        Either<Permission, Object> either;
        if (perm instanceof Atom) {
            Atom lv = (Atom)perm;
            either = Either.right(lv.id());
        } else {
            either = Either.left(perm);
        }
        return either;
    });

    public MapCodec<? extends Permission> getCodec();

    public record Atom(Identifier id) implements Permission
    {
        public static final MapCodec<Atom> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Identifier.CODEC.fieldOf("id")).forGetter(Atom::id)).apply((Applicative<Atom, ?>)instance, Atom::new));

        public MapCodec<Atom> getCodec() {
            return CODEC;
        }

        public static Atom ofVanilla(String path) {
            return Atom.of(Identifier.ofVanilla(path));
        }

        public static Atom of(Identifier id) {
            return new Atom(id);
        }
    }

    public record Level(PermissionLevel level) implements Permission
    {
        public static final MapCodec<Level> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)PermissionLevel.CODEC.fieldOf("level")).forGetter(Level::level)).apply((Applicative<Level, ?>)instance, Level::new));

        public MapCodec<Level> getCodec() {
            return CODEC;
        }
    }
}

