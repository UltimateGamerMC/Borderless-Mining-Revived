/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;readEnumConstant(Ljava/lang/Class;)Ljava/lang/Enum;
 *   Lnet/minecraft/network/PacketByteBuf;writeEnumConstant(Ljava/lang/Enum;)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/PacketByteBuf;writeBoolean(Z)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ServerPlayPacketListener;onRecipeCategoryOptions(Lnet/minecraft/network/packet/c2s/play/RecipeCategoryOptionsC2SPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/play/RecipeCategoryOptionsC2SPacket;apply(Lnet/minecraft/network/listener/ServerPlayPacketListener;)V
 */
package net.minecraft.network.packet.c2s.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.recipe.book.RecipeBookType;

public class RecipeCategoryOptionsC2SPacket
implements Packet<ServerPlayPacketListener> {
    public static final PacketCodec<PacketByteBuf, RecipeCategoryOptionsC2SPacket> CODEC = Packet.createCodec(RecipeCategoryOptionsC2SPacket::write, RecipeCategoryOptionsC2SPacket::new);
    private final RecipeBookType category;
    private final boolean guiOpen;
    private final boolean filteringCraftable;

    public RecipeCategoryOptionsC2SPacket(RecipeBookType category, boolean guiOpen, boolean filteringCraftable) {
        this.category = category;
        this.guiOpen = guiOpen;
        this.filteringCraftable = filteringCraftable;
    }

    private RecipeCategoryOptionsC2SPacket(PacketByteBuf buf) {
        this.category = buf.readEnumConstant(RecipeBookType.class);
        this.guiOpen = buf.readBoolean();
        this.filteringCraftable = buf.readBoolean();
    }

    private void write(PacketByteBuf buf) {
        buf.writeEnumConstant(this.category);
        buf.writeBoolean(this.guiOpen);
        buf.writeBoolean(this.filteringCraftable);
    }

    @Override
    public PacketType<RecipeCategoryOptionsC2SPacket> getPacketType() {
        return PlayPackets.RECIPE_BOOK_CHANGE_SETTINGS;
    }

    @Override
    public void apply(ServerPlayPacketListener arg) {
        arg.onRecipeCategoryOptions(this);
    }

    public RecipeBookType getCategory() {
        return this.category;
    }

    public boolean isGuiOpen() {
        return this.guiOpen;
    }

    public boolean isFilteringCraftable() {
        return this.filteringCraftable;
    }
}

