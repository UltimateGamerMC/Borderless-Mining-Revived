/*
 * External method calls:
 *   Lnet/minecraft/village/TradeOffer;matchesBuyItems(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z
 *   Lnet/minecraft/network/codec/PacketCodecs;toCollection(Ljava/util/function/IntFunction;)Lnet/minecraft/network/codec/PacketCodec$ResultFunction;
 *   Lnet/minecraft/network/codec/PacketCodec;collect(Lnet/minecraft/network/codec/PacketCodec$ResultFunction;)Lnet/minecraft/network/codec/PacketCodec;
 */
package net.minecraft.village;

import com.mojang.serialization.Codec;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.village.TradeOffer;
import org.jspecify.annotations.Nullable;

public class TradeOfferList
extends ArrayList<TradeOffer> {
    public static final Codec<TradeOfferList> CODEC = TradeOffer.CODEC.listOf().optionalFieldOf("Recipes", List.of()).xmap(TradeOfferList::new, Function.identity()).codec();
    public static final PacketCodec<RegistryByteBuf, TradeOfferList> PACKET_CODEC = TradeOffer.PACKET_CODEC.collect(PacketCodecs.toCollection(TradeOfferList::new));

    public TradeOfferList() {
    }

    private TradeOfferList(int size) {
        super(size);
    }

    private TradeOfferList(Collection<TradeOffer> tradeOffers) {
        super(tradeOffers);
    }

    public @Nullable TradeOffer getValidOffer(ItemStack firstBuyItem, ItemStack secondBuyItem, int index) {
        if (index > 0 && index < this.size()) {
            TradeOffer lv = (TradeOffer)this.get(index);
            if (lv.matchesBuyItems(firstBuyItem, secondBuyItem)) {
                return lv;
            }
            return null;
        }
        for (int j = 0; j < this.size(); ++j) {
            TradeOffer lv2 = (TradeOffer)this.get(j);
            if (!lv2.matchesBuyItems(firstBuyItem, secondBuyItem)) continue;
            return lv2;
        }
        return null;
    }

    public TradeOfferList copy() {
        TradeOfferList lv = new TradeOfferList(this.size());
        for (TradeOffer lv2 : this) {
            lv.add(lv2.copy());
        }
        return lv;
    }
}

