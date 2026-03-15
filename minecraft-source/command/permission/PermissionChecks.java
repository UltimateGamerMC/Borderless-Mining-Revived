/*
 * External method calls:
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/registry/Registry;register(Lnet/minecraft/registry/Registry;Lnet/minecraft/util/Identifier;Ljava/lang/Object;)Ljava/lang/Object;
 */
package net.minecraft.command.permission;

import com.mojang.serialization.MapCodec;
import net.minecraft.command.permission.PermissionCheck;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class PermissionChecks {
    public static MapCodec<? extends PermissionCheck> registerAndGetDefault(Registry<MapCodec<? extends PermissionCheck>> registry) {
        Registry.register(registry, Identifier.ofVanilla("always_pass"), PermissionCheck.AlwaysPass.CODEC);
        return Registry.register(registry, Identifier.ofVanilla("require"), PermissionCheck.Require.CODEC);
    }
}

