package net.fabricmc.fabric.api.screenhandler.v1;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;

/**
 * An extension of {@code NamedScreenHandlerFactory} that can write additional data to a screen opening packet.
 * This is used for {@linkplain ExtendedScreenHandlerType extended screen handlers}.
 *
 * @see ExtendedScreenHandlerType usage examples
 */
public interface ExtendedScreenHandlerFactory<D> extends MenuProvider {
	/**
	 * Writes additional server -&gt; client screen opening data to the buffer.
	 *
	 * @param player the player that is opening the screen
	 * @return the screen opening data
	 */
	D getScreenOpeningData(ServerPlayer player);
}
