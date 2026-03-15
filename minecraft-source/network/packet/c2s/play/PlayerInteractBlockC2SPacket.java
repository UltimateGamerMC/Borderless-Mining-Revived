/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;readEnumConstant(Ljava/lang/Class;)Ljava/lang/Enum;
 *   Lnet/minecraft/network/PacketByteBuf;readBlockHitResult()Lnet/minecraft/util/hit/BlockHitResult;
 *   Lnet/minecraft/network/PacketByteBuf;writeEnumConstant(Ljava/lang/Enum;)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/PacketByteBuf;writeBlockHitResult(Lnet/minecraft/util/hit/BlockHitResult;)V
 *   Lnet/minecraft/network/PacketByteBuf;writeVarInt(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ServerPlayPacketListener;onPlayerInteractBlock(Lnet/minecraft/network/packet/c2s/play/PlayerInteractBlockC2SPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/play/PlayerInteractBlockC2SPacket;apply(Lnet/minecraft/network/listener/ServerPlayPacketListener;)V
 */
package net.minecraft.network.packet.c2s.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;

public class PlayerInteractBlockC2SPacket
implements Packet<ServerPlayPacketListener> {
    public static final PacketCodec<PacketByteBuf, PlayerInteractBlockC2SPacket> CODEC = Packet.createCodec(PlayerInteractBlockC2SPacket::write, PlayerInteractBlockC2SPacket::new);
    private final BlockHitResult blockHitResult;
    private final Hand hand;
    private final int sequence;

    public PlayerInteractBlockC2SPacket(Hand hand, BlockHitResult blockHitResult, int sequence) {
        this.hand = hand;
        this.blockHitResult = blockHitResult;
        this.sequence = sequence;
    }

    private PlayerInteractBlockC2SPacket(PacketByteBuf buf) {
        this.hand = buf.readEnumConstant(Hand.class);
        this.blockHitResult = buf.readBlockHitResult();
        this.sequence = buf.readVarInt();
    }

    private void write(PacketByteBuf buf) {
        buf.writeEnumConstant(this.hand);
        buf.writeBlockHitResult(this.blockHitResult);
        buf.writeVarInt(this.sequence);
    }

    @Override
    public PacketType<PlayerInteractBlockC2SPacket> getPacketType() {
        return PlayPackets.USE_ITEM_ON;
    }

    @Override
    public void apply(ServerPlayPacketListener arg) {
        arg.onPlayerInteractBlock(this);
    }

    public Hand getHand() {
        return this.hand;
    }

    public BlockHitResult getBlockHitResult() {
        return this.blockHitResult;
    }

    public int getSequence() {
        return this.sequence;
    }
}

