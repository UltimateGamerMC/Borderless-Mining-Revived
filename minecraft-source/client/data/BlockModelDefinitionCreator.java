package net.minecraft.client.data;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.client.render.model.json.BlockModelDefinition;

@Environment(value=EnvType.CLIENT)
public interface BlockModelDefinitionCreator {
    public Block getBlock();

    public BlockModelDefinition createBlockModelDefinition();
}

