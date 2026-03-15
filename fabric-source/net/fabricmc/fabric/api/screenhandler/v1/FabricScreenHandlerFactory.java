package net.fabricmc.fabric.api.screenhandler.v1;

/**
 * An extension to {@link net.minecraft.world.MenuProvider}.
 * Unlike {@link ExtendedScreenHandlerFactory}, this can be used by any screen
 * handlers, and is implemented via interface injection.
 */
public interface FabricScreenHandlerFactory {
	/**
	 * {@return whether the server should send {@link
	 * net.minecraft.network.protocol.game.ClientboundContainerClosePacket } when opening the screen}
	 *
	 * <p>In vanilla, opening a new screen will always send the close screen packet.
	 * This, among other things, causes the mouse cursor to move to the center of the screen,
	 * which might not be expected in some cases. If this returns {@code false}, the packet
	 * is not sent to the client, stopping the behavior.
	 */
	default boolean shouldCloseCurrentScreen() {
		return true;
	}
}
