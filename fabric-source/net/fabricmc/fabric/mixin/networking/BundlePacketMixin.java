package net.fabricmc.fabric.mixin.networking;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.network.protocol.BundlePacket;
import net.minecraft.network.protocol.Packet;

@Mixin(BundlePacket.class)
public class BundlePacketMixin {
	@ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
	private static Iterable<? extends Packet<?>> flattenBundlePackets(Iterable<? extends Packet<?>> value) {
		var packets = new ArrayList<Packet<?>>();
		iterateBundle(value, packets);
		return packets;
	}

	@Unique
	private static void iterateBundle(Iterable<? extends Packet<?>> value, List<Packet<?>> result) {
		for (Packet<?> packet : value) {
			if (packet instanceof BundlePacket<?> bundlePacket) {
				iterateBundle(bundlePacket.subPackets(), result);
			} else {
				result.add(packet);
			}
		}
	}
}
