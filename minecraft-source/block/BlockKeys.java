/*
 * External method calls:
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/registry/RegistryKey;of(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/util/Identifier;)Lnet/minecraft/registry/RegistryKey;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/block/BlockKeys;of(Ljava/lang/String;)Lnet/minecraft/registry/RegistryKey;
 */
package net.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class BlockKeys {
    public static final RegistryKey<Block> PUMPKIN = BlockKeys.of("pumpkin");
    public static final RegistryKey<Block> PUMPKIN_STEM = BlockKeys.of("pumpkin_stem");
    public static final RegistryKey<Block> ATTACHED_PUMPKIN_STEM = BlockKeys.of("attached_pumpkin_stem");
    public static final RegistryKey<Block> MELON = BlockKeys.of("melon");
    public static final RegistryKey<Block> MELON_STEM = BlockKeys.of("melon_stem");
    public static final RegistryKey<Block> ATTACHED_MELON_STEM = BlockKeys.of("attached_melon_stem");

    private static RegistryKey<Block> of(String id) {
        return RegistryKey.of(RegistryKeys.BLOCK, Identifier.ofVanilla(id));
    }
}

