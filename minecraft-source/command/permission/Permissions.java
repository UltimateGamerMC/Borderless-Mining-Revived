/*
 * External method calls:
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/registry/Registry;register(Lnet/minecraft/registry/Registry;Lnet/minecraft/util/Identifier;Ljava/lang/Object;)Ljava/lang/Object;
 */
package net.minecraft.command.permission;

import com.mojang.serialization.MapCodec;
import net.minecraft.command.permission.Permission;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class Permissions {
    public static MapCodec<? extends Permission> registerAndGetDefault(Registry<MapCodec<? extends Permission>> registry) {
        Registry.register(registry, Identifier.ofVanilla("atom"), Permission.Atom.CODEC);
        return Registry.register(registry, Identifier.ofVanilla("command_level"), Permission.Level.CODEC);
    }
}

