package net.fabricmc.fabric.mixin.screenhandler;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.MenuProvider;

import net.fabricmc.fabric.api.screenhandler.v1.FabricScreenHandlerFactory;

@Mixin(MenuProvider.class)
public interface MenuProviderMixin extends FabricScreenHandlerFactory {
}
