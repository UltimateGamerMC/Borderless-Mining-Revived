/*
 * External method calls:
 *   Lnet/minecraft/command/permission/Permission$Level;level()Lnet/minecraft/command/permission/PermissionLevel;
 *   Lnet/minecraft/command/permission/PermissionPredicate;or(Lnet/minecraft/command/permission/PermissionPredicate;)Lnet/minecraft/command/permission/PermissionPredicate;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/command/permission/LeveledPermissionPredicate;create(Lnet/minecraft/command/permission/PermissionLevel;)Lnet/minecraft/command/permission/LeveledPermissionPredicate;
 */
package net.minecraft.command.permission;

import net.minecraft.command.DefaultPermissions;
import net.minecraft.command.permission.Permission;
import net.minecraft.command.permission.PermissionLevel;
import net.minecraft.command.permission.PermissionPredicate;

public interface LeveledPermissionPredicate
extends PermissionPredicate {
    @Deprecated
    public static final LeveledPermissionPredicate ALL = LeveledPermissionPredicate.create(PermissionLevel.ALL);
    public static final LeveledPermissionPredicate MODERATORS = LeveledPermissionPredicate.create(PermissionLevel.MODERATORS);
    public static final LeveledPermissionPredicate GAMEMASTERS = LeveledPermissionPredicate.create(PermissionLevel.GAMEMASTERS);
    public static final LeveledPermissionPredicate ADMINS = LeveledPermissionPredicate.create(PermissionLevel.ADMINS);
    public static final LeveledPermissionPredicate OWNERS = LeveledPermissionPredicate.create(PermissionLevel.OWNERS);

    public PermissionLevel getLevel();

    @Override
    default public boolean hasPermission(Permission arg) {
        if (arg instanceof Permission.Level) {
            Permission.Level lv = (Permission.Level)arg;
            return this.getLevel().isAtLeast(lv.level());
        }
        if (arg.equals(DefaultPermissions.ENTITY_SELECTORS)) {
            return this.getLevel().isAtLeast(PermissionLevel.GAMEMASTERS);
        }
        return false;
    }

    @Override
    default public PermissionPredicate or(PermissionPredicate other) {
        if (other instanceof LeveledPermissionPredicate) {
            LeveledPermissionPredicate lv = (LeveledPermissionPredicate)other;
            if (this.getLevel().isAtLeast(lv.getLevel())) {
                return lv;
            }
            return this;
        }
        return PermissionPredicate.super.or(other);
    }

    public static LeveledPermissionPredicate fromLevel(PermissionLevel level) {
        return switch (level) {
            default -> throw new MatchException(null, null);
            case PermissionLevel.ALL -> ALL;
            case PermissionLevel.MODERATORS -> MODERATORS;
            case PermissionLevel.GAMEMASTERS -> GAMEMASTERS;
            case PermissionLevel.ADMINS -> ADMINS;
            case PermissionLevel.OWNERS -> OWNERS;
        };
    }

    private static LeveledPermissionPredicate create(final PermissionLevel level) {
        return new LeveledPermissionPredicate(){

            @Override
            public PermissionLevel getLevel() {
                return level;
            }

            public String toString() {
                return "permission level: " + level.name();
            }
        };
    }
}

