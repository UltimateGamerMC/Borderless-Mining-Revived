package net.fabricmc.fabric.api.datagen.v1;

import java.nio.file.Path;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.data.PackOutput;

import net.fabricmc.loader.api.ModContainer;

/**
 * Extends {@link PackOutput} to keep track of the {@link ModContainer} that it originated from.
 */
public final class FabricDataOutput extends PackOutput {
	private final ModContainer modContainer;
	private final boolean strictValidation;

	@ApiStatus.Internal
	public FabricDataOutput(ModContainer modContainer, Path path, boolean strictValidation) {
		super(path);
		this.modContainer = modContainer;
		this.strictValidation = strictValidation;
	}

	/**
	 * Returns the {@link ModContainer} for the mod that this data generator has been created for.
	 *
	 * @return a {@link ModContainer} instance
	 */
	public ModContainer getModContainer() {
		return modContainer;
	}

	/**
	 * Returns the mod ID for the mod that this data generator has been created for.
	 *
	 * @return a mod ID
	 */
	public String getModId() {
		return getModContainer().getMetadata().getId();
	}

	/**
	 * When enabled data providers can do strict validation to ensure that all entries have data generated for them.
	 *
	 * @return if strict validation should be enabled
	 */
	public boolean isStrictValidationEnabled() {
		return strictValidation;
	}
}
