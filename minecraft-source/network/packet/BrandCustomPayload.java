/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;readString()Ljava/lang/String;
 *   Lnet/minecraft/network/PacketByteBuf;writeString(Ljava/lang/String;)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/packet/CustomPayload;codecOf(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/packet/CustomPayload;id(Ljava/lang/String;)Lnet/minecraft/network/packet/CustomPayload$Id;
 */
package net.minecraft.network.packet;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record BrandCustomPayload(String brand) implements CustomPayload
{
    public static final PacketCodec<PacketByteBuf, BrandCustomPayload> CODEC = CustomPayload.codecOf(BrandCustomPayload::write, BrandCustomPayload::new);
    public static final CustomPayload.Id<BrandCustomPayload> ID = CustomPayload.id("brand");

    private BrandCustomPayload(PacketByteBuf buf) {
        this(buf.readString());
    }

    private void write(PacketByteBuf buf) {
        buf.writeString(this.brand);
    }

    public CustomPayload.Id<BrandCustomPayload> getId() {
        return ID;
    }
}

