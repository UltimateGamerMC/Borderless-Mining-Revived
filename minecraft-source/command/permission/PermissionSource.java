package net.minecraft.command.permission;

import net.minecraft.command.permission.PermissionPredicate;

public interface PermissionSource {
    public PermissionPredicate getPermissions();
}

