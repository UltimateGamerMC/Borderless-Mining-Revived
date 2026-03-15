package net.minecraft.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface ScreenHandlerFactory {
    public @Nullable ScreenHandler createMenu(int var1, PlayerInventory var2, PlayerEntity var3);
}

