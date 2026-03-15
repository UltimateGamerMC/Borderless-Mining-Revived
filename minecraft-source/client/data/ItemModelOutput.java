/*
 * Internal private/static methods:
 *   Lnet/minecraft/client/data/ItemModelOutput;accept(Lnet/minecraft/item/Item;Lnet/minecraft/client/render/item/model/ItemModel$Unbaked;Lnet/minecraft/client/item/ItemAsset$Properties;)V
 */
package net.minecraft.client.data;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ItemAsset;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.item.Item;

@Environment(value=EnvType.CLIENT)
public interface ItemModelOutput {
    default public void accept(Item item, ItemModel.Unbaked model) {
        this.accept(item, model, ItemAsset.Properties.DEFAULT);
    }

    public void accept(Item var1, ItemModel.Unbaked var2, ItemAsset.Properties var3);

    public void acceptAlias(Item var1, Item var2);
}

