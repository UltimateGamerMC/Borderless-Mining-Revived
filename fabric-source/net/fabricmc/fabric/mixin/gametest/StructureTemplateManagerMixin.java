package net.fabricmc.fabric.mixin.gametest;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.DataFixer;
import org.apache.commons.io.IOUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.HolderGetter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.LevelStorageSource;

import net.fabricmc.fabric.impl.gametest.FabricGameTestRunner;

@Mixin(StructureTemplateManager.class)
public abstract class StructureTemplateManagerMixin {
	@Shadow
	private ResourceManager resourceManager;

	@Shadow
	public abstract StructureTemplate readStructure(CompoundTag compoundTag);

	@Unique
	private Optional<StructureTemplate> fabric_loadSnbtFromResource(Identifier id) {
		Identifier path = FabricGameTestRunner.GAMETEST_STRUCTURE_FINDER.idToFile(id);
		Optional<Resource> resource = this.resourceManager.getResource(path);

		if (resource.isPresent()) {
			try {
				String snbt = IOUtils.toString(resource.get().openAsReader());
				CompoundTag nbt = NbtUtils.snbtToStructure(snbt);
				return Optional.of(this.readStructure(nbt));
			} catch (IOException | CommandSyntaxException e) {
				throw new RuntimeException("Failed to load GameTest structure " + id, e);
			}
		}

		return Optional.empty();
	}

	@Unique
	private Stream<Identifier> streamTemplatesFromResource() {
		FileToIdConverter finder = FabricGameTestRunner.GAMETEST_STRUCTURE_FINDER;
		return finder.listMatchingResources(this.resourceManager).keySet().stream().map(finder::fileToId);
	}

	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList$Builder;add(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList$Builder;", ordinal = 2, shift = At.Shift.AFTER))
	private void addFabricTemplateProvider(ResourceManager resourceManager, LevelStorageSource.LevelStorageAccess session, DataFixer dataFixer, HolderGetter<Block> blockLookup, CallbackInfo ci, @Local ImmutableList.Builder<StructureTemplateManager.Source> builder) {
		builder.add(new StructureTemplateManager.Source(this::fabric_loadSnbtFromResource, this::streamTemplatesFromResource));
	}
}
