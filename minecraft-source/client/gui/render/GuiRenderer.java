/*
 * External method calls:
 *   Lnet/minecraft/client/gui/render/state/GuiRenderState;sortSimpleElements(Ljava/util/Comparator;)V
 *   Lnet/minecraft/client/gui/render/state/GuiRenderState;forEachSimpleElement(Ljava/util/function/Consumer;Lnet/minecraft/client/gui/render/state/GuiRenderState$LayerFilter;)V
 *   Lnet/minecraft/client/gl/DynamicUniforms;write(Lorg/joml/Matrix4fc;Lorg/joml/Vector4fc;Lorg/joml/Vector3fc;Lorg/joml/Matrix4fc;)Lcom/mojang/blaze3d/buffers/GpuBufferSlice;
 *   Lnet/minecraft/client/gui/render/state/SimpleGuiElementRenderState;pipeline()Lcom/mojang/blaze3d/pipeline/RenderPipeline;
 *   Lnet/minecraft/client/gui/render/state/SimpleGuiElementRenderState;textureSetup()Lnet/minecraft/client/texture/TextureSetup;
 *   Lnet/minecraft/client/gui/render/state/SimpleGuiElementRenderState;scissorArea()Lnet/minecraft/client/gui/ScreenRect;
 *   Lnet/minecraft/client/gui/render/state/SimpleGuiElementRenderState;setupVertices(Lnet/minecraft/client/render/VertexConsumer;)V
 *   Lnet/minecraft/client/gui/render/state/GuiRenderState;forEachTextElement(Ljava/util/function/Consumer;)V
 *   Lnet/minecraft/client/gui/render/state/GuiRenderState;forEachItemElement(Ljava/util/function/Consumer;)V
 *   Lnet/minecraft/client/gui/render/state/GuiRenderState;forEachSpecialElement(Ljava/util/function/Consumer;)V
 *   Lnet/minecraft/client/gui/render/SpecialGuiElementRenderer;render(Lnet/minecraft/client/gui/render/state/special/SpecialGuiElementRenderState;Lnet/minecraft/client/gui/render/state/GuiRenderState;I)V
 *   Lnet/minecraft/client/render/item/KeyedItemRenderState;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;III)V
 *   Lnet/minecraft/client/texture/TextureSetup;of(Lcom/mojang/blaze3d/textures/GpuTextureView;Lnet/minecraft/client/gl/GpuSampler;)Lnet/minecraft/client/texture/TextureSetup;
 *   Lnet/minecraft/client/gui/render/state/ItemGuiElementRenderState;pose()Lorg/joml/Matrix3x2f;
 *   Lnet/minecraft/client/gui/render/state/ItemGuiElementRenderState;scissorArea()Lnet/minecraft/client/gui/ScreenRect;
 *   Lnet/minecraft/client/gui/render/state/GuiRenderState;addSimpleElementToCurrentLayer(Lnet/minecraft/client/gui/render/state/TexturedQuadGuiElementRenderState;)V
 *   Lnet/minecraft/client/render/BufferBuilder;endNullable()Lnet/minecraft/client/render/BuiltBuffer;
 *   Lnet/minecraft/client/render/BuiltBuffer$DrawParameters;format()Lcom/mojang/blaze3d/vertex/VertexFormat;
 *   Lnet/minecraft/client/render/BuiltBuffer$DrawParameters;mode()Lcom/mojang/blaze3d/vertex/VertexFormat$DrawMode;
 *   Lnet/minecraft/client/gui/render/GuiRenderer$Draw;pipeline()Lcom/mojang/blaze3d/pipeline/RenderPipeline;
 *   Lnet/minecraft/client/gui/render/GuiRenderer$Draw;scissorArea()Lnet/minecraft/client/gui/ScreenRect;
 *   Lnet/minecraft/client/texture/TextureSetup;texure0()Lcom/mojang/blaze3d/textures/GpuTextureView;
 *   Lnet/minecraft/client/texture/TextureSetup;sampler0()Lnet/minecraft/client/gl/GpuSampler;
 *   Lnet/minecraft/client/texture/TextureSetup;texure1()Lcom/mojang/blaze3d/textures/GpuTextureView;
 *   Lnet/minecraft/client/texture/TextureSetup;sampler1()Lnet/minecraft/client/gl/GpuSampler;
 *   Lnet/minecraft/client/texture/TextureSetup;texure2()Lcom/mojang/blaze3d/textures/GpuTextureView;
 *   Lnet/minecraft/client/texture/TextureSetup;sampler2()Lnet/minecraft/client/gl/GpuSampler;
 *   Lnet/minecraft/client/gui/render/state/ItemGuiElementRenderState;oversizedBounds()Lnet/minecraft/client/gui/ScreenRect;
 *   Lnet/minecraft/client/gui/render/state/ItemGuiElementRenderState;state()Lnet/minecraft/client/render/item/KeyedItemRenderState;
 *   Lnet/minecraft/client/gui/render/OversizedItemGuiElementRenderer;render(Lnet/minecraft/client/gui/render/state/special/SpecialGuiElementRenderState;Lnet/minecraft/client/gui/render/state/GuiRenderState;I)V
 *   Lnet/minecraft/client/gui/render/state/TextGuiElementRenderState;prepare()Lnet/minecraft/client/font/TextRenderer$GlyphDrawable;
 *   Lnet/minecraft/client/font/TextRenderer$GlyphDrawable;draw(Lnet/minecraft/client/font/TextRenderer$GlyphDrawer;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/render/GuiRenderer;renderPreparedDraws(Lcom/mojang/blaze3d/buffers/GpuBufferSlice;)V
 *   Lnet/minecraft/client/gui/render/GuiRenderer;prepareSimpleElements(Lnet/minecraft/client/gui/render/state/GuiRenderState$LayerFilter;)V
 *   Lnet/minecraft/client/gui/render/GuiRenderer;endBuffer(Lnet/minecraft/client/render/BufferBuilder;Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/client/texture/TextureSetup;Lnet/minecraft/client/gui/ScreenRect;)V
 *   Lnet/minecraft/client/gui/render/GuiRenderer;render(Ljava/util/function/Supplier;Lnet/minecraft/client/gl/Framebuffer;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;Lcom/mojang/blaze3d/buffers/GpuBuffer;Lcom/mojang/blaze3d/vertex/VertexFormat$IndexType;II)V
 *   Lnet/minecraft/client/gui/render/GuiRenderer;render(Lnet/minecraft/client/gui/render/GuiRenderer$Draw;Lcom/mojang/blaze3d/systems/RenderPass;Lcom/mojang/blaze3d/buffers/GpuBuffer;Lcom/mojang/blaze3d/vertex/VertexFormat$IndexType;)V
 *   Lnet/minecraft/client/gui/render/GuiRenderer;scissorChanged(Lnet/minecraft/client/gui/ScreenRect;Lnet/minecraft/client/gui/ScreenRect;)Z
 *   Lnet/minecraft/client/gui/render/GuiRenderer;startBuffer(Lcom/mojang/blaze3d/pipeline/RenderPipeline;)Lnet/minecraft/client/render/BufferBuilder;
 *   Lnet/minecraft/client/gui/render/GuiRenderer;calcItemAtlasSideLength(I)I
 *   Lnet/minecraft/client/gui/render/GuiRenderer;createItemAtlas(I)V
 *   Lnet/minecraft/client/gui/render/GuiRenderer;collectVertexSizes()Lit/unimi/dsi/fastutil/objects/Object2IntMap;
 *   Lnet/minecraft/client/gui/render/GuiRenderer;enableScissor(Lnet/minecraft/client/gui/ScreenRect;Lcom/mojang/blaze3d/systems/RenderPass;)V
 *   Lnet/minecraft/client/gui/render/GuiRenderer;prepareSpecialElement(Lnet/minecraft/client/gui/render/state/special/SpecialGuiElementRenderState;I)V
 *   Lnet/minecraft/client/gui/render/GuiRenderer;prepareItem(Lnet/minecraft/client/gui/render/state/ItemGuiElementRenderState;FFII)V
 *   Lnet/minecraft/client/gui/render/GuiRenderer;prepareItemInitially(Lnet/minecraft/client/render/item/KeyedItemRenderState;Lnet/minecraft/client/util/math/MatrixStack;III)V
 */
package net.minecraft.client.gui.render;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.ProjectionType;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.textures.TextureFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextDrawable;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.MappableRingBuffer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.render.OversizedItemGuiElementRenderer;
import net.minecraft.client.gui.render.SpecialGuiElementRenderer;
import net.minecraft.client.gui.render.state.GlyphGuiElementRenderState;
import net.minecraft.client.gui.render.state.GuiRenderState;
import net.minecraft.client.gui.render.state.ItemGuiElementRenderState;
import net.minecraft.client.gui.render.state.SimpleGuiElementRenderState;
import net.minecraft.client.gui.render.state.TexturedQuadGuiElementRenderState;
import net.minecraft.client.gui.render.state.special.OversizedItemGuiElementRenderState;
import net.minecraft.client.gui.render.state.special.SpecialGuiElementRenderState;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.ProjectionMatrix2;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.command.RenderDispatcher;
import net.minecraft.client.render.item.KeyedItemRenderState;
import net.minecraft.client.texture.TextureSetup;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.joml.Matrix3x2fc;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.jspecify.annotations.Nullable;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;

@Environment(value=EnvType.CLIENT)
public class GuiRenderer
implements AutoCloseable {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final float field_59906 = 10000.0f;
    public static final float field_59901 = 0.0f;
    private static final float field_59907 = 1000.0f;
    public static final int field_59902 = 1000;
    public static final int field_59903 = -1000;
    public static final int field_59908 = 16;
    private static final int field_59909 = 512;
    private static final int MAX_TEXTURE_SIZE = RenderSystem.getDevice().getMaxTextureSize();
    public static final int field_59904 = 0;
    private static final Comparator<ScreenRect> SCISSOR_AREA_COMPARATOR = Comparator.nullsFirst(Comparator.comparing(ScreenRect::getTop).thenComparing(ScreenRect::getBottom).thenComparing(ScreenRect::getLeft).thenComparing(ScreenRect::getRight));
    private static final Comparator<TextureSetup> TEXTURE_SETUP_COMPARATOR = Comparator.nullsFirst(Comparator.comparing(TextureSetup::getSortKey));
    private static final Comparator<SimpleGuiElementRenderState> SIMPLE_ELEMENT_COMPARATOR = Comparator.comparing(SimpleGuiElementRenderState::scissorArea, SCISSOR_AREA_COMPARATOR).thenComparing(SimpleGuiElementRenderState::pipeline, Comparator.comparing(RenderPipeline::getSortKey)).thenComparing(SimpleGuiElementRenderState::textureSetup, TEXTURE_SETUP_COMPARATOR);
    private final Map<Object, RenderedItem> renderedItems = new Object2ObjectOpenHashMap<Object, RenderedItem>();
    private final Map<Object, OversizedItemGuiElementRenderer> oversizedItems = new Object2ObjectOpenHashMap<Object, OversizedItemGuiElementRenderer>();
    final GuiRenderState state;
    private final List<Draw> draws = new ArrayList<Draw>();
    private final List<Preparation> preparations = new ArrayList<Preparation>();
    private final BufferAllocator allocator = new BufferAllocator(786432);
    private final Map<VertexFormat, MappableRingBuffer> bufferByVertexFormat = new Object2ObjectOpenHashMap<VertexFormat, MappableRingBuffer>();
    private int blurLayer = Integer.MAX_VALUE;
    private final ProjectionMatrix2 guiProjectionMatrix = new ProjectionMatrix2("gui", 1000.0f, 11000.0f, true);
    private final ProjectionMatrix2 itemsProjectionMatrix = new ProjectionMatrix2("items", -1000.0f, 1000.0f, true);
    private final VertexConsumerProvider.Immediate vertexConsumers;
    private final OrderedRenderCommandQueue commandQueue;
    private final RenderDispatcher dispatcher;
    private final Map<Class<? extends SpecialGuiElementRenderState>, SpecialGuiElementRenderer<?>> specialElementRenderers;
    private @Nullable GpuTexture itemAtlasTexture;
    private @Nullable GpuTextureView itemAtlasTextureView;
    private @Nullable GpuTexture itemAtlasDepthTexture;
    private @Nullable GpuTextureView itemAtlasDepthTextureView;
    private int itemAtlasX;
    private int itemAtlasY;
    private int windowScaleFactor;
    private int frame;
    private @Nullable ScreenRect scissorArea = null;
    private @Nullable RenderPipeline pipeline = null;
    private @Nullable TextureSetup textureSetup = null;
    private @Nullable BufferBuilder buffer = null;

    public GuiRenderer(GuiRenderState state, VertexConsumerProvider.Immediate vertexConsumers, OrderedRenderCommandQueue queue, RenderDispatcher dispatcher, List<SpecialGuiElementRenderer<?>> specialElementRenderers) {
        this.state = state;
        this.vertexConsumers = vertexConsumers;
        this.commandQueue = queue;
        this.dispatcher = dispatcher;
        ImmutableMap.Builder<Class<?>, SpecialGuiElementRenderer<?>> builder = ImmutableMap.builder();
        for (SpecialGuiElementRenderer<?> lv : specialElementRenderers) {
            builder.put(lv.getElementClass(), lv);
        }
        this.specialElementRenderers = builder.buildOrThrow();
    }

    public void incrementFrame() {
        ++this.frame;
    }

    public void render(GpuBufferSlice fogBuffer) {
        this.prepare();
        this.renderPreparedDraws(fogBuffer);
        for (MappableRingBuffer lv : this.bufferByVertexFormat.values()) {
            lv.rotate();
        }
        this.draws.clear();
        this.preparations.clear();
        this.state.clear();
        this.blurLayer = Integer.MAX_VALUE;
        this.clearOversizedItems();
        if (SharedConstants.SHUFFLE_UI_RENDERING_ORDER) {
            RenderPipeline.updateSortKeySeed();
            TextureSetup.shuffleRenderingOrder();
        }
    }

    private void clearOversizedItems() {
        Iterator<Map.Entry<Object, OversizedItemGuiElementRenderer>> iterator = this.oversizedItems.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Object, OversizedItemGuiElementRenderer> entry = iterator.next();
            OversizedItemGuiElementRenderer lv = entry.getValue();
            if (!lv.isOversized()) {
                lv.close();
                iterator.remove();
                continue;
            }
            lv.clearOversized();
        }
    }

    private void prepare() {
        this.vertexConsumers.draw();
        this.prepareSpecialElements();
        this.prepareItemElements();
        this.prepareTextElements();
        this.state.sortSimpleElements(SIMPLE_ELEMENT_COMPARATOR);
        this.prepareSimpleElements(GuiRenderState.LayerFilter.BEFORE_BLUR);
        this.blurLayer = this.preparations.size();
        this.prepareSimpleElements(GuiRenderState.LayerFilter.AFTER_BLUR);
        this.finishPreparation();
    }

    private void prepareSimpleElements(GuiRenderState.LayerFilter filter) {
        this.scissorArea = null;
        this.pipeline = null;
        this.textureSetup = null;
        this.buffer = null;
        this.state.forEachSimpleElement(this::prepareSimpleElement, filter);
        if (this.buffer != null) {
            this.endBuffer(this.buffer, this.pipeline, this.textureSetup, this.scissorArea);
        }
    }

    private void renderPreparedDraws(GpuBufferSlice fogBuffer) {
        if (this.draws.isEmpty()) {
            return;
        }
        MinecraftClient lv = MinecraftClient.getInstance();
        Window lv2 = lv.getWindow();
        RenderSystem.setProjectionMatrix(this.guiProjectionMatrix.set((float)lv2.getFramebufferWidth() / (float)lv2.getScaleFactor(), (float)lv2.getFramebufferHeight() / (float)lv2.getScaleFactor()), ProjectionType.ORTHOGRAPHIC);
        Framebuffer lv3 = lv.getFramebuffer();
        int i = 0;
        for (Draw lv4 : this.draws) {
            if (lv4.indexCount <= i) continue;
            i = lv4.indexCount;
        }
        RenderSystem.ShapeIndexBuffer lv5 = RenderSystem.getSequentialBuffer(VertexFormat.DrawMode.QUADS);
        GpuBuffer gpuBuffer = lv5.getIndexBuffer(i);
        VertexFormat.IndexType lv6 = lv5.getIndexType();
        GpuBufferSlice gpuBufferSlice2 = RenderSystem.getDynamicUniforms().write(new Matrix4f().setTranslation(0.0f, 0.0f, -11000.0f), new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), new Vector3f(), new Matrix4f());
        if (this.blurLayer > 0) {
            this.render(() -> "GUI before blur", lv3, fogBuffer, gpuBufferSlice2, gpuBuffer, lv6, 0, Math.min(this.blurLayer, this.draws.size()));
        }
        if (this.draws.size() <= this.blurLayer) {
            return;
        }
        RenderSystem.getDevice().createCommandEncoder().clearDepthTexture(lv3.getDepthAttachment(), 1.0);
        lv.gameRenderer.renderBlur();
        this.render(() -> "GUI after blur", lv3, fogBuffer, gpuBufferSlice2, gpuBuffer, lv6, this.blurLayer, this.draws.size());
    }

    private void render(Supplier<String> nameSupplier, Framebuffer framebuffer, GpuBufferSlice fogBuffer, GpuBufferSlice dynamicTransformsBuffer, GpuBuffer buffer, VertexFormat.IndexType indexType, int from, int to) {
        try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(nameSupplier, framebuffer.getColorAttachmentView(), OptionalInt.empty(), framebuffer.useDepthAttachment ? framebuffer.getDepthAttachmentView() : null, OptionalDouble.empty());){
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.setUniform("Fog", fogBuffer);
            renderPass.setUniform("DynamicTransforms", dynamicTransformsBuffer);
            for (int k = from; k < to; ++k) {
                Draw lv = this.draws.get(k);
                this.render(lv, renderPass, buffer, indexType);
            }
        }
    }

    private void prepareSimpleElement(SimpleGuiElementRenderState state) {
        RenderPipeline renderPipeline = state.pipeline();
        TextureSetup lv = state.textureSetup();
        ScreenRect lv2 = state.scissorArea();
        if (renderPipeline != this.pipeline || this.scissorChanged(lv2, this.scissorArea) || !lv.equals(this.textureSetup)) {
            if (this.buffer != null) {
                this.endBuffer(this.buffer, this.pipeline, this.textureSetup, this.scissorArea);
            }
            this.buffer = this.startBuffer(renderPipeline);
            this.pipeline = renderPipeline;
            this.textureSetup = lv;
            this.scissorArea = lv2;
        }
        state.setupVertices(this.buffer);
    }

    private void prepareTextElements() {
        this.state.forEachTextElement(state -> {
            final Matrix3x2fc matrix3x2fc = state.matrix;
            final ScreenRect lv = state.clipBounds;
            state.prepare().draw(new TextRenderer.GlyphDrawer(){

                @Override
                public void drawGlyph(TextDrawable.DrawnGlyphRect glyph) {
                    this.draw(glyph);
                }

                @Override
                public void drawRectangle(TextDrawable rect) {
                    this.draw(rect);
                }

                private void draw(TextDrawable drawable) {
                    GuiRenderer.this.state.addPreparedTextElement(new GlyphGuiElementRenderState(matrix3x2fc, drawable, lv));
                }
            });
        });
    }

    private void prepareItemElements() {
        if (this.state.getItemModelKeys().isEmpty()) {
            return;
        }
        int i = this.getWindowScaleFactor();
        int j = 16 * i;
        int k = this.calcItemAtlasSideLength(j);
        if (this.itemAtlasTexture == null) {
            this.createItemAtlas(k);
        }
        RenderSystem.outputColorTextureOverride = this.itemAtlasTextureView;
        RenderSystem.outputDepthTextureOverride = this.itemAtlasDepthTextureView;
        RenderSystem.setProjectionMatrix(this.itemsProjectionMatrix.set(k, k), ProjectionType.ORTHOGRAPHIC);
        MinecraftClient.getInstance().gameRenderer.getDiffuseLighting().setShaderLights(DiffuseLighting.Type.ITEMS_3D);
        MatrixStack lv = new MatrixStack();
        MutableBoolean mutableBoolean = new MutableBoolean(false);
        MutableBoolean mutableBoolean2 = new MutableBoolean(false);
        this.state.forEachItemElement(elem -> {
            int l;
            boolean bl;
            if (elem.oversizedBounds() != null) {
                mutableBoolean2.setTrue();
                return;
            }
            KeyedItemRenderState lv = elem.state();
            RenderedItem lv2 = this.renderedItems.get(lv.getModelKey());
            if (!(lv2 == null || lv.isAnimated() && lv2.frame != this.frame)) {
                this.prepareItem((ItemGuiElementRenderState)elem, lv2.u, lv2.v, j, k);
                return;
            }
            if (this.itemAtlasX + j > k) {
                this.itemAtlasX = 0;
                this.itemAtlasY += j;
            }
            boolean bl2 = bl = lv.isAnimated() && lv2 != null;
            if (!bl && this.itemAtlasY + j > k) {
                if (mutableBoolean.isFalse()) {
                    LOGGER.warn("Trying to render too many items in GUI at the same time. Skipping some of them.");
                    mutableBoolean.setTrue();
                }
                return;
            }
            int k = bl ? lv2.x : this.itemAtlasX;
            int n = l = bl ? lv2.y : this.itemAtlasY;
            if (bl) {
                RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(this.itemAtlasTexture, 0, this.itemAtlasDepthTexture, 1.0, k, k - l - j, j, j);
            }
            this.prepareItemInitially(lv, lv, k, l, j);
            float f = (float)k / (float)k;
            float g = (float)(k - l) / (float)k;
            this.prepareItem((ItemGuiElementRenderState)elem, f, g, j, k);
            if (bl) {
                lv2.frame = this.frame;
            } else {
                this.renderedItems.put(elem.state().getModelKey(), new RenderedItem(this.itemAtlasX, this.itemAtlasY, f, g, this.frame));
                this.itemAtlasX += j;
            }
        });
        RenderSystem.outputColorTextureOverride = null;
        RenderSystem.outputDepthTextureOverride = null;
        if (mutableBoolean2.booleanValue()) {
            this.state.forEachItemElement(elem -> {
                if (elem.oversizedBounds() != null) {
                    KeyedItemRenderState lv = elem.state();
                    OversizedItemGuiElementRenderer lv2 = this.oversizedItems.computeIfAbsent(lv.getModelKey(), object -> new OversizedItemGuiElementRenderer(this.vertexConsumers));
                    ScreenRect lv3 = elem.oversizedBounds();
                    OversizedItemGuiElementRenderState lv4 = new OversizedItemGuiElementRenderState((ItemGuiElementRenderState)elem, lv3.getLeft(), lv3.getTop(), lv3.getRight(), lv3.getBottom());
                    lv2.render(lv4, this.state, i);
                }
            });
        }
    }

    private void prepareSpecialElements() {
        int i = MinecraftClient.getInstance().getWindow().getScaleFactor();
        this.state.forEachSpecialElement(state -> this.prepareSpecialElement(state, i));
    }

    private <T extends SpecialGuiElementRenderState> void prepareSpecialElement(T elementState, int windowScaleFactor) {
        SpecialGuiElementRenderer<?> lv = this.specialElementRenderers.get(elementState.getClass());
        if (lv != null) {
            lv.render(elementState, this.state, windowScaleFactor);
        }
    }

    private void prepareItemInitially(KeyedItemRenderState state, MatrixStack matrices, int x, int y, int scale) {
        boolean bl;
        matrices.push();
        matrices.translate((float)x + (float)scale / 2.0f, (float)y + (float)scale / 2.0f, 0.0f);
        matrices.scale(scale, -scale, scale);
        boolean bl2 = bl = !state.isSideLit();
        if (bl) {
            MinecraftClient.getInstance().gameRenderer.getDiffuseLighting().setShaderLights(DiffuseLighting.Type.ITEMS_FLAT);
        } else {
            MinecraftClient.getInstance().gameRenderer.getDiffuseLighting().setShaderLights(DiffuseLighting.Type.ITEMS_3D);
        }
        RenderSystem.enableScissorForRenderTypeDraws(x, this.itemAtlasTexture.getHeight(0) - y - scale, scale, scale);
        state.render(matrices, this.commandQueue, 0xF000F0, OverlayTexture.DEFAULT_UV, 0);
        this.dispatcher.render();
        this.vertexConsumers.draw();
        RenderSystem.disableScissorForRenderTypeDraws();
        matrices.pop();
    }

    private void prepareItem(ItemGuiElementRenderState state, float u, float v, int pixelsPerItem, int itemAtlasSideLength) {
        float h = u + (float)pixelsPerItem / (float)itemAtlasSideLength;
        float k = v + (float)(-pixelsPerItem) / (float)itemAtlasSideLength;
        this.state.addSimpleElementToCurrentLayer(new TexturedQuadGuiElementRenderState(RenderPipelines.GUI_TEXTURED_PREMULTIPLIED_ALPHA, TextureSetup.of(this.itemAtlasTextureView, RenderSystem.getSamplerCache().getRepeated(FilterMode.NEAREST)), state.pose(), state.x(), state.y(), state.x() + 16, state.y() + 16, u, h, v, k, -1, state.scissorArea(), null));
    }

    private void createItemAtlas(int sideLength) {
        GpuDevice gpuDevice = RenderSystem.getDevice();
        this.itemAtlasTexture = gpuDevice.createTexture("UI items atlas", 12, TextureFormat.RGBA8, sideLength, sideLength, 1, 1);
        this.itemAtlasTextureView = gpuDevice.createTextureView(this.itemAtlasTexture);
        this.itemAtlasDepthTexture = gpuDevice.createTexture("UI items atlas depth", 8, TextureFormat.DEPTH32, sideLength, sideLength, 1, 1);
        this.itemAtlasDepthTextureView = gpuDevice.createTextureView(this.itemAtlasDepthTexture);
        gpuDevice.createCommandEncoder().clearColorAndDepthTextures(this.itemAtlasTexture, 0, this.itemAtlasDepthTexture, 1.0);
    }

    private int calcItemAtlasSideLength(int itemCount) {
        int j;
        Set<Object> set = this.state.getItemModelKeys();
        if (this.renderedItems.isEmpty()) {
            j = set.size();
        } else {
            j = this.renderedItems.size();
            for (Object object : set) {
                if (this.renderedItems.containsKey(object)) continue;
                ++j;
            }
        }
        if (this.itemAtlasTexture != null) {
            int k = this.itemAtlasTexture.getWidth(0) / itemCount;
            int l = k * k;
            if (j < l) {
                return this.itemAtlasTexture.getWidth(0);
            }
            this.onItemAtlasChanged();
        }
        int k = set.size();
        int l = MathHelper.smallestEncompassingSquareSideLength(k + k / 2);
        return Math.clamp((long)MathHelper.smallestEncompassingPowerOfTwo(l * itemCount), 512, MAX_TEXTURE_SIZE);
    }

    private int getWindowScaleFactor() {
        int i = MinecraftClient.getInstance().getWindow().getScaleFactor();
        if (i != this.windowScaleFactor) {
            this.onItemAtlasChanged();
            for (OversizedItemGuiElementRenderer lv : this.oversizedItems.values()) {
                lv.clearModel();
            }
            this.windowScaleFactor = i;
        }
        return i;
    }

    private void onItemAtlasChanged() {
        this.itemAtlasX = 0;
        this.itemAtlasY = 0;
        this.renderedItems.clear();
        if (this.itemAtlasTexture != null) {
            this.itemAtlasTexture.close();
            this.itemAtlasTexture = null;
        }
        if (this.itemAtlasTextureView != null) {
            this.itemAtlasTextureView.close();
            this.itemAtlasTextureView = null;
        }
        if (this.itemAtlasDepthTexture != null) {
            this.itemAtlasDepthTexture.close();
            this.itemAtlasDepthTexture = null;
        }
        if (this.itemAtlasDepthTextureView != null) {
            this.itemAtlasDepthTextureView.close();
            this.itemAtlasDepthTextureView = null;
        }
    }

    private void endBuffer(BufferBuilder builder, RenderPipeline pipeline, TextureSetup textureSetup, @Nullable ScreenRect scissorArea) {
        BuiltBuffer lv = builder.endNullable();
        if (lv != null) {
            this.preparations.add(new Preparation(lv, pipeline, textureSetup, scissorArea));
        }
    }

    private void finishPreparation() {
        this.initVertexBuffers();
        CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();
        Object2IntOpenHashMap<VertexFormat> object2IntMap = new Object2IntOpenHashMap<VertexFormat>();
        for (Preparation lv : this.preparations) {
            BuiltBuffer lv2 = lv.mesh;
            BuiltBuffer.DrawParameters lv3 = lv2.getDrawParameters();
            VertexFormat vertexFormat = lv3.format();
            MappableRingBuffer lv4 = this.bufferByVertexFormat.get(vertexFormat);
            if (!object2IntMap.containsKey(vertexFormat)) {
                object2IntMap.put(vertexFormat, 0);
            }
            ByteBuffer byteBuffer = lv2.getBuffer();
            int i = byteBuffer.remaining();
            int j = object2IntMap.getInt(vertexFormat);
            try (GpuBuffer.MappedView mappedView = commandEncoder.mapBuffer(lv4.getBlocking().slice(j, i), false, true);){
                MemoryUtil.memCopy(byteBuffer, mappedView.data());
            }
            object2IntMap.put(vertexFormat, j + i);
            this.draws.add(new Draw(lv4.getBlocking(), j / vertexFormat.getVertexSize(), lv3.mode(), lv3.indexCount(), lv.pipeline, lv.textureSetup, lv.scissorArea));
            lv.close();
        }
    }

    private void initVertexBuffers() {
        Object2IntMap<VertexFormat> object2IntMap = this.collectVertexSizes();
        for (Object2IntMap.Entry entry : object2IntMap.object2IntEntrySet()) {
            VertexFormat vertexFormat = (VertexFormat)entry.getKey();
            int i = entry.getIntValue();
            MappableRingBuffer lv = this.bufferByVertexFormat.get(vertexFormat);
            if (lv != null && lv.size() >= i) continue;
            if (lv != null) {
                lv.close();
            }
            this.bufferByVertexFormat.put(vertexFormat, new MappableRingBuffer(() -> "GUI vertex buffer for " + String.valueOf(vertexFormat), 34, i));
        }
    }

    private Object2IntMap<VertexFormat> collectVertexSizes() {
        Object2IntOpenHashMap<VertexFormat> object2IntMap = new Object2IntOpenHashMap<VertexFormat>();
        for (Preparation lv : this.preparations) {
            BuiltBuffer.DrawParameters lv2 = lv.mesh.getDrawParameters();
            VertexFormat vertexFormat = lv2.format();
            if (!object2IntMap.containsKey(vertexFormat)) {
                object2IntMap.put(vertexFormat, 0);
            }
            object2IntMap.put(vertexFormat, object2IntMap.getInt(vertexFormat) + lv2.vertexCount() * vertexFormat.getVertexSize());
        }
        return object2IntMap;
    }

    private void render(Draw draw, RenderPass pass, GpuBuffer indexBuffer, VertexFormat.IndexType indexType) {
        RenderPipeline renderPipeline = draw.pipeline();
        pass.setPipeline(renderPipeline);
        pass.setVertexBuffer(0, draw.vertexBuffer);
        ScreenRect lv = draw.scissorArea();
        if (lv != null) {
            this.enableScissor(lv, pass);
        } else {
            pass.disableScissor();
        }
        if (draw.textureSetup.texure0() != null) {
            pass.bindTexture("Sampler0", draw.textureSetup.texure0(), draw.textureSetup.sampler0());
        }
        if (draw.textureSetup.texure1() != null) {
            pass.bindTexture("Sampler1", draw.textureSetup.texure1(), draw.textureSetup.sampler1());
        }
        if (draw.textureSetup.texure2() != null) {
            pass.bindTexture("Sampler2", draw.textureSetup.texure2(), draw.textureSetup.sampler2());
        }
        pass.setIndexBuffer(indexBuffer, indexType);
        pass.drawIndexed(draw.baseVertex, 0, draw.indexCount, 1);
    }

    private BufferBuilder startBuffer(RenderPipeline pipeline) {
        return new BufferBuilder(this.allocator, pipeline.getVertexFormatMode(), pipeline.getVertexFormat());
    }

    private boolean scissorChanged(@Nullable ScreenRect oldScissorArea, @Nullable ScreenRect newScissorArea) {
        if (oldScissorArea == newScissorArea) {
            return false;
        }
        if (oldScissorArea != null) {
            return !oldScissorArea.equals(newScissorArea);
        }
        return true;
    }

    private void enableScissor(ScreenRect scissorArea, RenderPass pass) {
        Window lv = MinecraftClient.getInstance().getWindow();
        int i = lv.getFramebufferHeight();
        int j = lv.getScaleFactor();
        double d = scissorArea.getLeft() * j;
        double e = i - scissorArea.getBottom() * j;
        double f = scissorArea.width() * j;
        double g = scissorArea.height() * j;
        pass.enableScissor((int)d, (int)e, Math.max(0, (int)f), Math.max(0, (int)g));
    }

    @Override
    public void close() {
        this.allocator.close();
        if (this.itemAtlasTexture != null) {
            this.itemAtlasTexture.close();
        }
        if (this.itemAtlasTextureView != null) {
            this.itemAtlasTextureView.close();
        }
        if (this.itemAtlasDepthTexture != null) {
            this.itemAtlasDepthTexture.close();
        }
        if (this.itemAtlasDepthTextureView != null) {
            this.itemAtlasDepthTextureView.close();
        }
        this.specialElementRenderers.values().forEach(SpecialGuiElementRenderer::close);
        this.guiProjectionMatrix.close();
        this.itemsProjectionMatrix.close();
        for (MappableRingBuffer lv : this.bufferByVertexFormat.values()) {
            lv.close();
        }
        this.oversizedItems.values().forEach(SpecialGuiElementRenderer::close);
    }

    @Environment(value=EnvType.CLIENT)
    record Draw(GpuBuffer vertexBuffer, int baseVertex, VertexFormat.DrawMode mode, int indexCount, RenderPipeline pipeline, TextureSetup textureSetup, @Nullable ScreenRect scissorArea) {
    }

    @Environment(value=EnvType.CLIENT)
    record Preparation(BuiltBuffer mesh, RenderPipeline pipeline, TextureSetup textureSetup, @Nullable ScreenRect scissorArea) implements AutoCloseable
    {
        @Override
        public void close() {
            this.mesh.close();
        }
    }

    @Environment(value=EnvType.CLIENT)
    static final class RenderedItem {
        final int x;
        final int y;
        final float u;
        final float v;
        int frame;

        RenderedItem(int x, int y, float u, float v, int frame) {
            this.x = x;
            this.y = y;
            this.u = u;
            this.v = v;
            this.frame = frame;
        }
    }
}

