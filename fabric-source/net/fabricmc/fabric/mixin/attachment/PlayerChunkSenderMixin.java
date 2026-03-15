package net.fabricmc.fabric.mixin.attachment;

import java.util.ArrayList;
import java.util.List;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.PlayerChunkSender;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.chunk.LevelChunk;

import net.fabricmc.fabric.impl.attachment.AttachmentTargetImpl;
import net.fabricmc.fabric.impl.attachment.sync.AttachmentChange;

@Mixin(PlayerChunkSender.class)
abstract class PlayerChunkSenderMixin {
	@WrapOperation(
			method = "sendNextChunks",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/network/PlayerChunkSender;sendChunk(Lnet/minecraft/server/network/ServerGamePacketListenerImpl;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/LevelChunk;)V"
			)
	)
	private void sendInitialAttachmentData(ServerGamePacketListenerImpl handler, ServerLevel world, LevelChunk chunk, Operation<Void> original, ServerPlayer player) {
		original.call(handler, world, chunk);
		// do a wrap operation so this packet is sent *after* the chunk ones
		List<AttachmentChange> changes = new ArrayList<>();
		((AttachmentTargetImpl) chunk).fabric_computeInitialSyncChanges(player, changes::add);

		if (!changes.isEmpty()) {
			AttachmentChange.partitionAndSendPackets(changes, player);
		}
	}
}
