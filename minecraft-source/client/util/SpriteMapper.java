/*
 * External method calls:
 *   Lnet/minecraft/util/Identifier;withPrefixedPath(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/util/SpriteMapper;map(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/util/SpriteIdentifier;
 */
package net.minecraft.client.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public record SpriteMapper(Identifier sheet, String prefix) {
    public SpriteIdentifier map(Identifier id) {
        return new SpriteIdentifier(this.sheet, id.withPrefixedPath(this.prefix + "/"));
    }

    public SpriteIdentifier mapVanilla(String id) {
        return this.map(Identifier.ofVanilla(id));
    }
}

