/*
 * External method calls:
 *   Lnet/minecraft/command/permission/PermissionCheck;allows(Lnet/minecraft/command/permission/PermissionPredicate;)Z
 *
 * Internal private/static methods:
 *   Lnet/minecraft/command/permission/PermissionSourcePredicate;test(Lnet/minecraft/command/permission/PermissionSource;)Z
 */
package net.minecraft.command.permission;

import java.util.function.Predicate;
import net.minecraft.command.permission.PermissionCheck;
import net.minecraft.command.permission.PermissionSource;

public record PermissionSourcePredicate<T extends PermissionSource>(PermissionCheck test) implements Predicate<T>
{
    @Override
    public boolean test(T arg) {
        return this.test.allows(arg.getPermissions());
    }

    @Override
    public /* synthetic */ boolean test(Object source) {
        return this.test((T)((PermissionSource)source));
    }
}

