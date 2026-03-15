/*
 * External method calls:
 *   Lnet/minecraft/state/StateManager$Builder;build(Ljava/util/function/Function;Lnet/minecraft/state/StateManager$Factory;)Lnet/minecraft/state/StateManager;
 *   Lnet/minecraft/block/BlockState;with(Lnet/minecraft/state/property/Property;Ljava/lang/Comparable;)Ljava/lang/Object;
 *   Lnet/minecraft/registry/entry/RegistryEntry$Reference;registryKey()Lnet/minecraft/registry/RegistryKey;
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/model/BlockStateManagers;createItemFrameStateManager()Lnet/minecraft/state/StateManager;
 */
package net.minecraft.client.render.model;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class BlockStateManagers {
    private static final StateManager<Block, BlockState> ITEM_FRAME = BlockStateManagers.createItemFrameStateManager();
    private static final StateManager<Block, BlockState> GLOW_ITEM_FRAME = BlockStateManagers.createItemFrameStateManager();
    private static final Identifier GLOW_ITEM_FRAME_ID = Identifier.ofVanilla("glow_item_frame");
    private static final Identifier ITEM_FRAME_ID = Identifier.ofVanilla("item_frame");
    private static final Map<Identifier, StateManager<Block, BlockState>> STATIC_MANAGERS = Map.of(ITEM_FRAME_ID, ITEM_FRAME, GLOW_ITEM_FRAME_ID, GLOW_ITEM_FRAME);

    private static StateManager<Block, BlockState> createItemFrameStateManager() {
        return new StateManager.Builder(Blocks.AIR).add(Properties.MAP).build(Block::getDefaultState, BlockState::new);
    }

    public static BlockState getStateForItemFrame(boolean hasGlow, boolean hasMap) {
        return (BlockState)(hasGlow ? GLOW_ITEM_FRAME : ITEM_FRAME).getDefaultState().with(Properties.MAP, hasMap);
    }

    static Function<Identifier, StateManager<Block, BlockState>> createIdToManagerMapper() {
        HashMap<Identifier, StateManager<Block, BlockState>> map = new HashMap<Identifier, StateManager<Block, BlockState>>(STATIC_MANAGERS);
        for (Block lv : Registries.BLOCK) {
            map.put(lv.getRegistryEntry().registryKey().getValue(), lv.getStateManager());
        }
        return map::get;
    }
}

