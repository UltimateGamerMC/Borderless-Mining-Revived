/*
 * External method calls:
 *   Lnet/minecraft/util/profiler/ServerTickType;values()[Lnet/minecraft/util/profiler/ServerTickType;
 *   Lnet/minecraft/client/util/BufferAllocator;fixedSized(I)Lnet/minecraft/client/util/BufferAllocator;
 *   Lnet/minecraft/client/render/BufferBuilder;vertex(FFF)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;color(I)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;normal(FFF)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;lineWidth(F)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/BufferBuilder;end()Lnet/minecraft/client/render/BuiltBuffer;
 *   Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V
 *   Lnet/minecraft/client/gui/hud/debug/DebugHudEntry;render(Lnet/minecraft/client/gui/hud/debug/DebugHudLines;Lnet/minecraft/world/World;Lnet/minecraft/world/chunk/WorldChunk;Lnet/minecraft/world/chunk/WorldChunk;)V
 *   Lnet/minecraft/client/gui/hud/debug/chart/RenderingChart;render(Lnet/minecraft/client/gui/DrawContext;II)V
 *   Lnet/minecraft/client/gui/hud/debug/chart/TickChart;render(Lnet/minecraft/client/gui/DrawContext;II)V
 *   Lnet/minecraft/client/gui/hud/debug/chart/PacketSizeChart;render(Lnet/minecraft/client/gui/DrawContext;II)V
 *   Lnet/minecraft/client/gui/hud/debug/chart/PingChart;render(Lnet/minecraft/client/gui/DrawContext;II)V
 *   Lnet/minecraft/server/integrated/IntegratedServer;createChunkLoadMap(I)Lnet/minecraft/world/chunk/ChunkLoadMap;
 *   Lnet/minecraft/world/chunk/ChunkLoadMap;initSpawnPos(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/util/math/ChunkPos;)V
 *   Lnet/minecraft/client/gui/screen/world/LevelLoadingScreen;drawChunkMap(Lnet/minecraft/client/gui/DrawContext;IIIILnet/minecraft/world/chunk/ChunkLoadMap;)V
 *   Lnet/minecraft/util/profiler/Profiler;scoped(Ljava/lang/String;)Lnet/minecraft/util/profiler/ScopedProfiler;
 *   Lnet/minecraft/client/gui/hud/debug/chart/PieChart;render(Lnet/minecraft/client/gui/DrawContext;)V
 *   Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V
 *   Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;IIIZ)V
 *   Lnet/minecraft/util/profiler/MultiValueDebugSampleLogImpl;push(J)V
 *   Lnet/minecraft/client/gl/DynamicUniforms;write(Lorg/joml/Matrix4fc;Lorg/joml/Vector4fc;Lorg/joml/Vector3fc;Lorg/joml/Matrix4fc;)Lcom/mojang/blaze3d/buffers/GpuBufferSlice;
 *   Lnet/minecraft/server/world/OptionalChunk;orElse(Ljava/lang/Object;)Ljava/lang/Object;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/hud/DebugHud;drawText(Lnet/minecraft/client/gui/DrawContext;Ljava/util/List;Z)V
 */
package net.minecraft.client.gui.hud;

import com.google.common.base.Strings;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.datafixers.DataFixUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.debug.DebugHudEntries;
import net.minecraft.client.gui.hud.debug.DebugHudEntry;
import net.minecraft.client.gui.hud.debug.DebugHudLines;
import net.minecraft.client.gui.hud.debug.DebugHudProfile;
import net.minecraft.client.gui.hud.debug.chart.PacketSizeChart;
import net.minecraft.client.gui.hud.debug.chart.PieChart;
import net.minecraft.client.gui.hud.debug.chart.PingChart;
import net.minecraft.client.gui.hud.debug.chart.RenderingChart;
import net.minecraft.client.gui.hud.debug.chart.TickChart;
import net.minecraft.client.gui.screen.world.LevelLoadingScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.world.ChunkLevels;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.profiler.MultiValueDebugSampleLogImpl;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.profiler.Profilers;
import net.minecraft.util.profiler.ScopedProfiler;
import net.minecraft.util.profiler.ServerTickType;
import net.minecraft.util.profiler.log.DebugSampleType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkLoadMap;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.WorldChunk;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class DebugHud {
    private static final float DEBUG_CROSSHAIR_SCALE = 0.01f;
    private static final int field_57920 = 36;
    private static final int field_32188 = 2;
    private static final int field_32189 = 2;
    private static final int field_32190 = 2;
    private final MinecraftClient client;
    private final TextRenderer textRenderer;
    private final GpuBuffer debugCrosshairBuffer;
    private final RenderSystem.ShapeIndexBuffer debugCrosshairIndexBuffer = RenderSystem.getSequentialBuffer(VertexFormat.DrawMode.LINES);
    private @Nullable ChunkPos pos;
    private @Nullable WorldChunk chunk;
    private @Nullable CompletableFuture<WorldChunk> chunkFuture;
    private boolean renderingChartVisible;
    private boolean renderingAndTickChartsVisible;
    private boolean packetSizeAndPingChartsVisible;
    private final MultiValueDebugSampleLogImpl frameNanosLog = new MultiValueDebugSampleLogImpl(1);
    private final MultiValueDebugSampleLogImpl tickNanosLog = new MultiValueDebugSampleLogImpl(ServerTickType.values().length);
    private final MultiValueDebugSampleLogImpl pingLog = new MultiValueDebugSampleLogImpl(1);
    private final MultiValueDebugSampleLogImpl packetSizeLog = new MultiValueDebugSampleLogImpl(1);
    private final Map<DebugSampleType, MultiValueDebugSampleLogImpl> receivedDebugSamples = Map.of(DebugSampleType.TICK_TIME, this.tickNanosLog);
    private final RenderingChart renderingChart;
    private final TickChart tickChart;
    private final PingChart pingChart;
    private final PacketSizeChart packetSizeChart;
    private final PieChart pieChart;

    public DebugHud(MinecraftClient client) {
        this.client = client;
        this.textRenderer = client.textRenderer;
        this.renderingChart = new RenderingChart(this.textRenderer, this.frameNanosLog);
        this.tickChart = new TickChart(this.textRenderer, this.tickNanosLog, () -> Float.valueOf(arg.world == null ? 0.0f : arg.world.getTickManager().getMillisPerTick()));
        this.pingChart = new PingChart(this.textRenderer, this.pingLog);
        this.packetSizeChart = new PacketSizeChart(this.textRenderer, this.packetSizeLog);
        this.pieChart = new PieChart(this.textRenderer);
        try (BufferAllocator lv = BufferAllocator.fixedSized(VertexFormats.POSITION_COLOR_NORMAL_LINE_WIDTH.getVertexSize() * 12 * 2);){
            BufferBuilder lv2 = new BufferBuilder(lv, VertexFormat.DrawMode.LINES, VertexFormats.POSITION_COLOR_NORMAL_LINE_WIDTH);
            lv2.vertex(0.0f, 0.0f, 0.0f).color(Colors.BLACK).normal(1.0f, 0.0f, 0.0f).lineWidth(4.0f);
            lv2.vertex(1.0f, 0.0f, 0.0f).color(Colors.BLACK).normal(1.0f, 0.0f, 0.0f).lineWidth(4.0f);
            lv2.vertex(0.0f, 0.0f, 0.0f).color(Colors.BLACK).normal(0.0f, 1.0f, 0.0f).lineWidth(4.0f);
            lv2.vertex(0.0f, 1.0f, 0.0f).color(Colors.BLACK).normal(0.0f, 1.0f, 0.0f).lineWidth(4.0f);
            lv2.vertex(0.0f, 0.0f, 0.0f).color(Colors.BLACK).normal(0.0f, 0.0f, 1.0f).lineWidth(4.0f);
            lv2.vertex(0.0f, 0.0f, 1.0f).color(Colors.BLACK).normal(0.0f, 0.0f, 1.0f).lineWidth(4.0f);
            lv2.vertex(0.0f, 0.0f, 0.0f).color(Colors.RED).normal(1.0f, 0.0f, 0.0f).lineWidth(2.0f);
            lv2.vertex(1.0f, 0.0f, 0.0f).color(Colors.RED).normal(1.0f, 0.0f, 0.0f).lineWidth(2.0f);
            lv2.vertex(0.0f, 0.0f, 0.0f).color(Colors.GREEN).normal(0.0f, 1.0f, 0.0f).lineWidth(2.0f);
            lv2.vertex(0.0f, 1.0f, 0.0f).color(Colors.GREEN).normal(0.0f, 1.0f, 0.0f).lineWidth(2.0f);
            lv2.vertex(0.0f, 0.0f, 0.0f).color(-8421377).normal(0.0f, 0.0f, 1.0f).lineWidth(2.0f);
            lv2.vertex(0.0f, 0.0f, 1.0f).color(-8421377).normal(0.0f, 0.0f, 1.0f).lineWidth(2.0f);
            try (BuiltBuffer lv3 = lv2.end();){
                this.debugCrosshairBuffer = RenderSystem.getDevice().createBuffer(() -> "Crosshair vertex buffer", GpuBuffer.USAGE_VERTEX, lv3.getBuffer());
            }
        }
    }

    public void resetChunk() {
        this.chunkFuture = null;
        this.chunk = null;
    }

    public void render(DrawContext context) {
        IntegratedServer lv10;
        ArrayList list4;
        ChunkPos lv4;
        GameOptions lv = this.client.options;
        if (!this.client.isFinishedLoading() || lv.hudHidden && this.client.currentScreen == null) {
            return;
        }
        Collection<Identifier> collection = this.client.debugHudEntryList.getVisibleEntries();
        if (collection.isEmpty()) {
            return;
        }
        context.createNewRootLayer();
        Profiler lv2 = Profilers.get();
        lv2.push("debug");
        if (this.client.getCameraEntity() != null && this.client.world != null) {
            BlockPos lv3 = this.client.getCameraEntity().getBlockPos();
            lv4 = new ChunkPos(lv3);
        } else {
            lv4 = null;
        }
        if (!Objects.equals(this.pos, lv4)) {
            this.pos = lv4;
            this.resetChunk();
        }
        final ArrayList<String> list = new ArrayList<String>();
        final ArrayList<String> list2 = new ArrayList<String>();
        final LinkedHashMap map = new LinkedHashMap();
        final ArrayList list3 = new ArrayList();
        DebugHudLines lv5 = new DebugHudLines(){

            @Override
            public void addPriorityLine(String line) {
                if (list.size() > list2.size()) {
                    list2.add(line);
                } else {
                    list.add(line);
                }
            }

            @Override
            public void addLine(String line) {
                list3.add(line);
            }

            @Override
            public void addLinesToSection(Identifier sectionId, Collection<String> lines) {
                map.computeIfAbsent(sectionId, sectionLines -> new ArrayList()).addAll(lines);
            }

            @Override
            public void addLineToSection(Identifier sectionId, String line) {
                map.computeIfAbsent(sectionId, sectionLines -> new ArrayList()).add(line);
            }
        };
        World lv6 = this.getWorld();
        for (Identifier lv7 : collection) {
            DebugHudEntry lv8 = DebugHudEntries.get(lv7);
            if (lv8 == null) continue;
            lv8.render(lv5, lv6, this.getClientChunk(), this.getChunk());
        }
        if (!list.isEmpty()) {
            list.add("");
        }
        if (!list2.isEmpty()) {
            list2.add("");
        }
        if (!list3.isEmpty()) {
            int i = (list3.size() + 1) / 2;
            list.addAll(list3.subList(0, i));
            list2.addAll(list3.subList(i, list3.size()));
            list.add("");
            if (i < list3.size()) {
                list2.add("");
            }
        }
        if (!(list4 = new ArrayList(map.values())).isEmpty()) {
            int j = (list4.size() + 1) / 2;
            for (int k = 0; k < list4.size(); ++k) {
                Collection collection2 = (Collection)list4.get(k);
                if (collection2.isEmpty()) continue;
                if (k < j) {
                    list.addAll(collection2);
                    list.add("");
                    continue;
                }
                list2.addAll(collection2);
                list2.add("");
            }
        }
        if (this.client.debugHudEntryList.isF3Enabled()) {
            list.add("");
            boolean bl = this.client.getServer() != null;
            KeyBinding lv9 = lv.debugModifierKey;
            String string = lv9.getBoundKeyLocalizedText().getString();
            String string2 = "[" + (String)(lv9.isUnbound() ? "" : string + "+");
            String string3 = string2 + lv.debugProfilingChartKey.getBoundKeyLocalizedText().getString() + "]";
            String string4 = string2 + lv.debugFpsChartsKey.getBoundKeyLocalizedText().getString() + "]";
            String string5 = string2 + lv.debugNetworkChartsKey.getBoundKeyLocalizedText().getString() + "]";
            list.add("Debug charts: " + string3 + " Profiler " + (this.renderingChartVisible ? "visible" : "hidden") + "; " + string4 + " " + (bl ? "FPS + TPS " : "FPS ") + (this.renderingAndTickChartsVisible ? "visible" : "hidden") + "; " + string5 + " " + (!this.client.isInSingleplayer() ? "Bandwidth + Ping" : "Ping") + (this.packetSizeAndPingChartsVisible ? " visible" : " hidden"));
            String string6 = string2 + lv.debugOptionsKey.getBoundKeyLocalizedText().getString() + "]";
            list.add("To edit: press " + string6);
        }
        this.drawText(context, list, true);
        this.drawText(context, list2, false);
        context.createNewRootLayer();
        this.pieChart.setBottomMargin(10);
        if (this.shouldRenderTickCharts()) {
            int j = context.getScaledWindowWidth();
            int k = j / 2;
            this.renderingChart.render(context, 0, this.renderingChart.getWidth(k));
            if (this.tickNanosLog.getLength() > 0) {
                int l = this.tickChart.getWidth(k);
                this.tickChart.render(context, j - l, l);
            }
            this.pieChart.setBottomMargin(this.tickChart.getHeight());
        }
        if (this.shouldShowPacketSizeAndPingCharts() && this.client.getNetworkHandler() != null) {
            int j = context.getScaledWindowWidth();
            int k = j / 2;
            if (!this.client.isInSingleplayer()) {
                this.packetSizeChart.render(context, 0, this.packetSizeChart.getWidth(k));
            }
            int l = this.pingChart.getWidth(k);
            this.pingChart.render(context, j - l, l);
            this.pieChart.setBottomMargin(this.pingChart.getHeight());
        }
        if (this.client.debugHudEntryList.isEntryVisible(DebugHudEntries.VISUALIZE_CHUNKS_ON_SERVER) && (lv10 = this.client.getServer()) != null && this.client.player != null) {
            ChunkLoadMap lv11 = lv10.createChunkLoadMap(16 + ChunkLevels.FULL_GENERATION_REQUIRED_LEVEL);
            lv11.initSpawnPos(this.client.player.getEntityWorld().getRegistryKey(), this.client.player.getChunkPos());
            LevelLoadingScreen.drawChunkMap(context, context.getScaledWindowWidth() / 2, context.getScaledWindowHeight() / 2, 4, 1, lv11);
        }
        try (ScopedProfiler lv12 = lv2.scoped("profilerPie");){
            this.pieChart.render(context);
        }
        lv2.pop();
    }

    private void drawText(DrawContext context, List<String> text, boolean left) {
        int m;
        int l;
        int k;
        String string;
        int j;
        int i = this.textRenderer.fontHeight;
        for (j = 0; j < text.size(); ++j) {
            string = text.get(j);
            if (Strings.isNullOrEmpty(string)) continue;
            k = this.textRenderer.getWidth(string);
            l = left ? 2 : context.getScaledWindowWidth() - 2 - k;
            m = 2 + i * j;
            context.fill(l - 1, m - 1, l + k + 1, m + i - 1, -1873784752);
        }
        for (j = 0; j < text.size(); ++j) {
            string = text.get(j);
            if (Strings.isNullOrEmpty(string)) continue;
            k = this.textRenderer.getWidth(string);
            l = left ? 2 : context.getScaledWindowWidth() - 2 - k;
            m = 2 + i * j;
            context.drawText(this.textRenderer, string, l, m, Colors.LIGHTER_GRAY, false);
        }
    }

    private @Nullable ServerWorld getServerWorld() {
        if (this.client.world == null) {
            return null;
        }
        IntegratedServer lv = this.client.getServer();
        if (lv != null) {
            return lv.getWorld(this.client.world.getRegistryKey());
        }
        return null;
    }

    private @Nullable World getWorld() {
        if (this.client.world == null) {
            return null;
        }
        return DataFixUtils.orElse(Optional.ofNullable(this.client.getServer()).flatMap(server -> Optional.ofNullable(server.getWorld(this.client.world.getRegistryKey()))), this.client.world);
    }

    private @Nullable WorldChunk getChunk() {
        if (this.client.world == null || this.pos == null) {
            return null;
        }
        if (this.chunkFuture == null) {
            ServerWorld lv = this.getServerWorld();
            if (lv == null) {
                return null;
            }
            this.chunkFuture = lv.getChunkManager().getChunkFutureSyncOnMainThread(this.pos.x, this.pos.z, ChunkStatus.FULL, false).thenApply(chunk -> chunk.orElse(null));
        }
        return this.chunkFuture.getNow(null);
    }

    private @Nullable WorldChunk getClientChunk() {
        if (this.client.world == null || this.pos == null) {
            return null;
        }
        if (this.chunk == null) {
            this.chunk = this.client.world.getChunk(this.pos.x, this.pos.z);
        }
        return this.chunk;
    }

    public boolean shouldShowDebugHud() {
        DebugHudProfile lv = this.client.debugHudEntryList;
        return !(!lv.isF3Enabled() && lv.getVisibleEntries().isEmpty() || this.client.options.hudHidden && this.client.currentScreen == null);
    }

    public boolean shouldShowRenderingChart() {
        return this.client.debugHudEntryList.isF3Enabled() && this.renderingChartVisible;
    }

    public boolean shouldShowPacketSizeAndPingCharts() {
        return this.client.debugHudEntryList.isF3Enabled() && this.packetSizeAndPingChartsVisible;
    }

    public boolean shouldRenderTickCharts() {
        return this.client.debugHudEntryList.isF3Enabled() && this.renderingAndTickChartsVisible;
    }

    public void togglePacketSizeAndPingCharts() {
        boolean bl = this.packetSizeAndPingChartsVisible = !this.client.debugHudEntryList.isF3Enabled() || !this.packetSizeAndPingChartsVisible;
        if (this.packetSizeAndPingChartsVisible) {
            this.client.debugHudEntryList.setF3Enabled(true);
            this.renderingAndTickChartsVisible = false;
        }
    }

    public void toggleRenderingAndTickCharts() {
        boolean bl = this.renderingAndTickChartsVisible = !this.client.debugHudEntryList.isF3Enabled() || !this.renderingAndTickChartsVisible;
        if (this.renderingAndTickChartsVisible) {
            this.client.debugHudEntryList.setF3Enabled(true);
            this.packetSizeAndPingChartsVisible = false;
        }
    }

    public void toggleRenderingChart() {
        boolean bl = this.renderingChartVisible = !this.client.debugHudEntryList.isF3Enabled() || !this.renderingChartVisible;
        if (this.renderingChartVisible) {
            this.client.debugHudEntryList.setF3Enabled(true);
        }
    }

    public void pushToFrameLog(long value) {
        this.frameNanosLog.push(value);
    }

    public MultiValueDebugSampleLogImpl getTickNanosLog() {
        return this.tickNanosLog;
    }

    public MultiValueDebugSampleLogImpl getPingLog() {
        return this.pingLog;
    }

    public MultiValueDebugSampleLogImpl getPacketSizeLog() {
        return this.packetSizeLog;
    }

    public PieChart getPieChart() {
        return this.pieChart;
    }

    public void set(long[] values, DebugSampleType type) {
        MultiValueDebugSampleLogImpl lv = this.receivedDebugSamples.get((Object)type);
        if (lv != null) {
            lv.set(values);
        }
    }

    public void clear() {
        this.tickNanosLog.clear();
        this.pingLog.clear();
        this.packetSizeLog.clear();
    }

    public void renderDebugCrosshair(Camera camera) {
        Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
        matrix4fStack.pushMatrix();
        matrix4fStack.translate(0.0f, 0.0f, -1.0f);
        matrix4fStack.rotateX(camera.getPitch() * ((float)Math.PI / 180));
        matrix4fStack.rotateY(camera.getYaw() * ((float)Math.PI / 180));
        float f = 0.01f * (float)this.client.getWindow().getScaleFactor();
        matrix4fStack.scale(-f, f, -f);
        RenderPipeline renderPipeline = RenderPipelines.LINES;
        Framebuffer lv = MinecraftClient.getInstance().getFramebuffer();
        GpuTextureView gpuTextureView = lv.getColorAttachmentView();
        GpuTextureView gpuTextureView2 = lv.getDepthAttachmentView();
        GpuBuffer gpuBuffer = this.debugCrosshairIndexBuffer.getIndexBuffer(36);
        GpuBufferSlice gpuBufferSlice = RenderSystem.getDynamicUniforms().write(matrix4fStack, new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), new Vector3f(), new Matrix4f());
        try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "3d crosshair", gpuTextureView, OptionalInt.empty(), gpuTextureView2, OptionalDouble.empty());){
            renderPass.setPipeline(renderPipeline);
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.setVertexBuffer(0, this.debugCrosshairBuffer);
            renderPass.setIndexBuffer(gpuBuffer, this.debugCrosshairIndexBuffer.getIndexType());
            renderPass.setUniform("DynamicTransforms", gpuBufferSlice);
            renderPass.drawIndexed(0, 0, 36, 1);
        }
        matrix4fStack.popMatrix();
    }
}

