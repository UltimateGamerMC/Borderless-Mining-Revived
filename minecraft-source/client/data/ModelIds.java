/*
 * External method calls:
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/util/Identifier;withPath(Ljava/util/function/UnaryOperator;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/util/Identifier;withPrefixedPath(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 */
package net.minecraft.client.data;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class ModelIds {
    @Deprecated
    public static Identifier getMinecraftNamespacedBlock(String name) {
        return Identifier.ofVanilla("block/" + name);
    }

    public static Identifier getMinecraftNamespacedItem(String name) {
        return Identifier.ofVanilla("item/" + name);
    }

    public static Identifier getBlockSubModelId(Block block, String suffix) {
        Identifier lv = Registries.BLOCK.getId(block);
        return lv.withPath(path -> "block/" + path + suffix);
    }

    public static Identifier getBlockModelId(Block block) {
        Identifier lv = Registries.BLOCK.getId(block);
        return lv.withPrefixedPath("block/");
    }

    public static Identifier getItemModelId(Item item) {
        Identifier lv = Registries.ITEM.getId(item);
        return lv.withPrefixedPath("item/");
    }

    public static Identifier getItemSubModelId(Item item, String suffix) {
        Identifier lv = Registries.ITEM.getId(item);
        return lv.withPath(path -> "item/" + path + suffix);
    }
}

