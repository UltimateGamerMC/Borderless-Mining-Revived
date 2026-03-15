/*
 * External method calls:
 *   Lnet/minecraft/client/gui/render/state/ItemGuiElementRenderState;state()Lnet/minecraft/client/render/item/KeyedItemRenderState;
 *   Lnet/minecraft/client/gui/render/state/GuiRenderState$Layer;addItem(Lnet/minecraft/client/gui/render/state/ItemGuiElementRenderState;)V
 *   Lnet/minecraft/client/gui/render/state/ItemGuiElementRenderState;bounds()Lnet/minecraft/client/gui/ScreenRect;
 *   Lnet/minecraft/client/gui/render/state/GuiRenderState$Layer;addText(Lnet/minecraft/client/gui/render/state/TextGuiElementRenderState;)V
 *   Lnet/minecraft/client/gui/render/state/TextGuiElementRenderState;bounds()Lnet/minecraft/client/gui/ScreenRect;
 *   Lnet/minecraft/client/gui/render/state/GuiRenderState$Layer;addSpecialElement(Lnet/minecraft/client/gui/render/state/special/SpecialGuiElementRenderState;)V
 *   Lnet/minecraft/client/gui/render/state/special/SpecialGuiElementRenderState;bounds()Lnet/minecraft/client/gui/ScreenRect;
 *   Lnet/minecraft/client/gui/render/state/GuiRenderState$Layer;addSimpleElement(Lnet/minecraft/client/gui/render/state/SimpleGuiElementRenderState;)V
 *   Lnet/minecraft/client/gui/render/state/SimpleGuiElementRenderState;bounds()Lnet/minecraft/client/gui/ScreenRect;
 *   Lnet/minecraft/client/texture/TextureSetup;empty()Lnet/minecraft/client/texture/TextureSetup;
 *   Lnet/minecraft/client/gui/render/state/GuiElementRenderState;bounds()Lnet/minecraft/client/gui/ScreenRect;
 *   Lnet/minecraft/client/gui/ScreenRect;intersects(Lnet/minecraft/client/gui/ScreenRect;)Z
 *   Lnet/minecraft/client/gui/render/state/GuiRenderState$Layer;addPreparedText(Lnet/minecraft/client/gui/render/state/SimpleGuiElementRenderState;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/render/state/GuiRenderState;findAndGoToLayerToAdd(Lnet/minecraft/client/gui/render/state/GuiElementRenderState;)Z
 *   Lnet/minecraft/client/gui/render/state/GuiRenderState;onElementAdded(Lnet/minecraft/client/gui/ScreenRect;)V
 *   Lnet/minecraft/client/gui/render/state/GuiRenderState;findAndGoToLayerIntersecting(Lnet/minecraft/client/gui/ScreenRect;)V
 *   Lnet/minecraft/client/gui/render/state/GuiRenderState;anyIntersect(Lnet/minecraft/client/gui/ScreenRect;Ljava/util/List;)Z
 *   Lnet/minecraft/client/gui/render/state/GuiRenderState;forEachLayer(Ljava/util/function/Consumer;Lnet/minecraft/client/gui/render/state/GuiRenderState$LayerFilter;)V
 *   Lnet/minecraft/client/gui/render/state/GuiRenderState;traverseLayers(Lnet/minecraft/client/gui/render/state/GuiRenderState$Layer;Ljava/util/function/Consumer;)V
 */
package net.minecraft.client.gui.render.state;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.render.state.ColoredQuadGuiElementRenderState;
import net.minecraft.client.gui.render.state.GuiElementRenderState;
import net.minecraft.client.gui.render.state.ItemGuiElementRenderState;
import net.minecraft.client.gui.render.state.SimpleGuiElementRenderState;
import net.minecraft.client.gui.render.state.TextGuiElementRenderState;
import net.minecraft.client.gui.render.state.TexturedQuadGuiElementRenderState;
import net.minecraft.client.gui.render.state.special.SpecialGuiElementRenderState;
import net.minecraft.client.texture.TextureSetup;
import org.joml.Matrix3x2f;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class GuiRenderState {
    private static final int field_60454 = 0x774444FF;
    private final List<Layer> rootLayers = new ArrayList<Layer>();
    private int blurLayer = Integer.MAX_VALUE;
    private Layer currentLayer;
    private final Set<Object> itemModelKeys = new HashSet<Object>();
    private @Nullable ScreenRect currentLayerBounds;

    public GuiRenderState() {
        this.createNewRootLayer();
    }

    public void createNewRootLayer() {
        this.currentLayer = new Layer(null);
        this.rootLayers.add(this.currentLayer);
    }

    public void applyBlur() {
        if (this.blurLayer != Integer.MAX_VALUE) {
            throw new IllegalStateException("Can only blur once per frame");
        }
        this.blurLayer = this.rootLayers.size() - 1;
    }

    public void goUpLayer() {
        if (this.currentLayer.up == null) {
            this.currentLayer.up = new Layer(this.currentLayer);
        }
        this.currentLayer = this.currentLayer.up;
    }

    public void addItem(ItemGuiElementRenderState state) {
        if (!this.findAndGoToLayerToAdd(state)) {
            return;
        }
        this.itemModelKeys.add(state.state().getModelKey());
        this.currentLayer.addItem(state);
        this.onElementAdded(state.bounds());
    }

    public void addText(TextGuiElementRenderState state) {
        if (!this.findAndGoToLayerToAdd(state)) {
            return;
        }
        this.currentLayer.addText(state);
        this.onElementAdded(state.bounds());
    }

    public void addSpecialElement(SpecialGuiElementRenderState state) {
        if (!this.findAndGoToLayerToAdd(state)) {
            return;
        }
        this.currentLayer.addSpecialElement(state);
        this.onElementAdded(state.bounds());
    }

    public void addSimpleElement(SimpleGuiElementRenderState state) {
        if (!this.findAndGoToLayerToAdd(state)) {
            return;
        }
        this.currentLayer.addSimpleElement(state);
        this.onElementAdded(state.bounds());
    }

    private void onElementAdded(@Nullable ScreenRect bounds) {
        if (!SharedConstants.RENDER_UI_LAYERING_RECTANGLES || bounds == null) {
            return;
        }
        this.goUpLayer();
        this.currentLayer.addSimpleElement(new ColoredQuadGuiElementRenderState(RenderPipelines.GUI, TextureSetup.empty(), new Matrix3x2f(), 0, 0, 10000, 10000, 0x774444FF, 0x774444FF, bounds));
    }

    private boolean findAndGoToLayerToAdd(GuiElementRenderState state) {
        ScreenRect lv = state.bounds();
        if (lv == null) {
            return false;
        }
        if (this.currentLayerBounds != null && this.currentLayerBounds.contains(lv)) {
            this.goUpLayer();
        } else {
            this.findAndGoToLayerIntersecting(lv);
        }
        this.currentLayerBounds = lv;
        return true;
    }

    private void findAndGoToLayerIntersecting(ScreenRect bounds) {
        Layer lv = this.rootLayers.getLast();
        while (lv.up != null) {
            lv = lv.up;
        }
        boolean bl = false;
        while (!bl) {
            boolean bl2 = bl = this.anyIntersect(bounds, lv.simpleElementRenderStates) || this.anyIntersect(bounds, lv.itemElementRenderStates) || this.anyIntersect(bounds, lv.textElementRenderStates) || this.anyIntersect(bounds, lv.specialElementRenderStates);
            if (lv.parent == null) break;
            if (bl) continue;
            lv = lv.parent;
        }
        this.currentLayer = lv;
        if (bl) {
            this.goUpLayer();
        }
    }

    private boolean anyIntersect(ScreenRect bounds, @Nullable List<? extends GuiElementRenderState> elementRenderStates) {
        if (elementRenderStates != null) {
            for (GuiElementRenderState guiElementRenderState : elementRenderStates) {
                ScreenRect lv2 = guiElementRenderState.bounds();
                if (lv2 == null || !lv2.intersects(bounds)) continue;
                return true;
            }
        }
        return false;
    }

    public void addSimpleElementToCurrentLayer(TexturedQuadGuiElementRenderState state) {
        this.currentLayer.addSimpleElement(state);
    }

    public void addPreparedTextElement(SimpleGuiElementRenderState state) {
        this.currentLayer.addPreparedText(state);
    }

    public Set<Object> getItemModelKeys() {
        return this.itemModelKeys;
    }

    public void forEachSimpleElement(Consumer<SimpleGuiElementRenderState> consumer, LayerFilter filter) {
        this.forEachLayer(layer -> {
            if (layer.simpleElementRenderStates == null && layer.preparedTextElementRenderStates == null) {
                return;
            }
            if (layer.simpleElementRenderStates != null) {
                for (SimpleGuiElementRenderState lv : layer.simpleElementRenderStates) {
                    consumer.accept(lv);
                }
            }
            if (layer.preparedTextElementRenderStates != null) {
                for (SimpleGuiElementRenderState lv : layer.preparedTextElementRenderStates) {
                    consumer.accept(lv);
                }
            }
        }, filter);
    }

    public void forEachItemElement(Consumer<ItemGuiElementRenderState> itemElementStateConsumer) {
        Layer lv = this.currentLayer;
        this.forEachLayer(layer -> {
            if (layer.itemElementRenderStates != null) {
                this.currentLayer = layer;
                for (ItemGuiElementRenderState lv : layer.itemElementRenderStates) {
                    itemElementStateConsumer.accept(lv);
                }
            }
        }, LayerFilter.ALL);
        this.currentLayer = lv;
    }

    public void forEachTextElement(Consumer<TextGuiElementRenderState> textElementStateConsumer) {
        Layer lv = this.currentLayer;
        this.forEachLayer(layer -> {
            if (layer.textElementRenderStates != null) {
                for (TextGuiElementRenderState lv : layer.textElementRenderStates) {
                    this.currentLayer = layer;
                    textElementStateConsumer.accept(lv);
                }
            }
        }, LayerFilter.ALL);
        this.currentLayer = lv;
    }

    public void forEachSpecialElement(Consumer<SpecialGuiElementRenderState> specialElementStateConsumer) {
        Layer lv = this.currentLayer;
        this.forEachLayer(layer -> {
            if (layer.specialElementRenderStates != null) {
                this.currentLayer = layer;
                for (SpecialGuiElementRenderState lv : layer.specialElementRenderStates) {
                    specialElementStateConsumer.accept(lv);
                }
            }
        }, LayerFilter.ALL);
        this.currentLayer = lv;
    }

    public void sortSimpleElements(Comparator<SimpleGuiElementRenderState> simpleElementStateComparator) {
        this.forEachLayer(layer -> {
            if (layer.simpleElementRenderStates != null) {
                if (SharedConstants.SHUFFLE_UI_RENDERING_ORDER) {
                    Collections.shuffle(layer.simpleElementRenderStates);
                }
                layer.simpleElementRenderStates.sort(simpleElementStateComparator);
            }
        }, LayerFilter.ALL);
    }

    private void forEachLayer(Consumer<Layer> layerConsumer, LayerFilter filter) {
        int i = 0;
        int j = this.rootLayers.size();
        if (filter == LayerFilter.BEFORE_BLUR) {
            j = Math.min(this.blurLayer, this.rootLayers.size());
        } else if (filter == LayerFilter.AFTER_BLUR) {
            i = this.blurLayer;
        }
        for (int k = i; k < j; ++k) {
            Layer lv = this.rootLayers.get(k);
            this.traverseLayers(lv, layerConsumer);
        }
    }

    private void traverseLayers(Layer layer, Consumer<Layer> layerConsumer) {
        layerConsumer.accept(layer);
        if (layer.up != null) {
            this.traverseLayers(layer.up, layerConsumer);
        }
    }

    public void clear() {
        this.itemModelKeys.clear();
        this.rootLayers.clear();
        this.blurLayer = Integer.MAX_VALUE;
        this.createNewRootLayer();
    }

    @Environment(value=EnvType.CLIENT)
    static class Layer {
        public final @Nullable Layer parent;
        public @Nullable Layer up;
        public @Nullable List<SimpleGuiElementRenderState> simpleElementRenderStates;
        public @Nullable List<SimpleGuiElementRenderState> preparedTextElementRenderStates;
        public @Nullable List<ItemGuiElementRenderState> itemElementRenderStates;
        public @Nullable List<TextGuiElementRenderState> textElementRenderStates;
        public @Nullable List<SpecialGuiElementRenderState> specialElementRenderStates;

        Layer(@Nullable Layer parent) {
            this.parent = parent;
        }

        public void addItem(ItemGuiElementRenderState state) {
            if (this.itemElementRenderStates == null) {
                this.itemElementRenderStates = new ArrayList<ItemGuiElementRenderState>();
            }
            this.itemElementRenderStates.add(state);
        }

        public void addText(TextGuiElementRenderState state) {
            if (this.textElementRenderStates == null) {
                this.textElementRenderStates = new ArrayList<TextGuiElementRenderState>();
            }
            this.textElementRenderStates.add(state);
        }

        public void addSpecialElement(SpecialGuiElementRenderState state) {
            if (this.specialElementRenderStates == null) {
                this.specialElementRenderStates = new ArrayList<SpecialGuiElementRenderState>();
            }
            this.specialElementRenderStates.add(state);
        }

        public void addSimpleElement(SimpleGuiElementRenderState state) {
            if (this.simpleElementRenderStates == null) {
                this.simpleElementRenderStates = new ArrayList<SimpleGuiElementRenderState>();
            }
            this.simpleElementRenderStates.add(state);
        }

        public void addPreparedText(SimpleGuiElementRenderState state) {
            if (this.preparedTextElementRenderStates == null) {
                this.preparedTextElementRenderStates = new ArrayList<SimpleGuiElementRenderState>();
            }
            this.preparedTextElementRenderStates.add(state);
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static enum LayerFilter {
        ALL,
        BEFORE_BLUR,
        AFTER_BLUR;

    }
}

