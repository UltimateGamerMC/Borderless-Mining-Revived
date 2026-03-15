package net.fabricmc.fabric.impl.transfer.fluid;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;

/**
 * Implemented by fluids to cache the FluidVariant with a null tag inside the Fluid object directly.
 */
public interface FluidVariantCache {
	FluidVariant fabric_getCachedFluidVariant();
}
