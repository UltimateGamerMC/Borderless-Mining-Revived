/*
 * External method calls:
 *   Lnet/minecraft/client/gui/hud/debug/DebugHudProfile;toggleVisibility(Lnet/minecraft/util/Identifier;)Z
 *   Lnet/minecraft/client/gui/hud/ChatHud;addMessage(Lnet/minecraft/text/Text;)V
 *   Lnet/minecraft/client/util/NarratorManager;narrateSystemMessage(Lnet/minecraft/text/Text;)V
 *   Lnet/minecraft/text/Text;empty()Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/MutableText;formatted([Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/MutableText;append(Lnet/minecraft/text/Text;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Text;literal(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/option/KeyBinding;matchesKey(Lnet/minecraft/client/input/KeyInput;)Z
 *   Lnet/minecraft/client/gui/hud/ChatHud;clear(Z)V
 *   Lnet/minecraft/command/permission/PermissionCheck;allows(Lnet/minecraft/command/permission/PermissionPredicate;)Z
 *   Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V
 *   Lnet/minecraft/client/texture/TextureManager;dumpDynamicTextures(Ljava/nio/file/Path;)V
 *   Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/MutableText;styled(Ljava/util/function/UnaryOperator;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/MinecraftClient;reloadResources()Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/client/MinecraftClient;toggleDebugProfiler(Ljava/util/function/Consumer;)Z
 *   Lnet/minecraft/server/command/VersionCommand;acceptInfo(Ljava/util/function/Consumer;)V
 *   Lnet/minecraft/client/network/DataQueryHandler;queryBlockNbt(Lnet/minecraft/util/math/BlockPos;Ljava/util/function/Consumer;)V
 *   Lnet/minecraft/block/entity/BlockEntity;createNbt(Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/nbt/NbtCompound;
 *   Lnet/minecraft/client/network/DataQueryHandler;queryEntityNbt(ILjava/util/function/Consumer;)V
 *   Lnet/minecraft/storage/NbtWriteView;create(Lnet/minecraft/util/ErrorReporter;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/storage/NbtWriteView;
 *   Lnet/minecraft/entity/Entity;writeData(Lnet/minecraft/storage/WriteView;)V
 *   Lnet/minecraft/command/argument/BlockArgumentParser;stringifyBlockState(Lnet/minecraft/block/BlockState;)Ljava/lang/String;
 *   Lnet/minecraft/nbt/NbtHelper;toPrettyPrintedText(Lnet/minecraft/nbt/NbtElement;)Lnet/minecraft/text/Text;
 *   Lnet/minecraft/client/gui/screen/option/VideoOptionsScreen;updateFullscreenButtonValue(Z)V
 *   Lnet/minecraft/client/MinecraftClient;takePanorama(Ljava/io/File;)Lnet/minecraft/text/Text;
 *   Lnet/minecraft/client/util/ScreenshotRecorder;saveScreenshot(Ljava/io/File;Lnet/minecraft/client/gl/Framebuffer;Ljava/util/function/Consumer;)V
 *   Lnet/minecraft/client/option/NarratorMode;byId(I)Lnet/minecraft/client/option/NarratorMode;
 *   Lnet/minecraft/client/gui/screen/Screen;refreshNarrator(Z)V
 *   Lnet/minecraft/client/gui/screen/Screen;keyPressed(Lnet/minecraft/client/input/KeyInput;)Z
 *   Lnet/minecraft/client/util/InputUtil;fromKeyCode(Lnet/minecraft/client/input/KeyInput;)Lnet/minecraft/client/util/InputUtil$Key;
 *   Lnet/minecraft/client/gui/screen/Screen;keyReleased(Lnet/minecraft/client/input/KeyInput;)Z
 *   Lnet/minecraft/util/crash/CrashReport;create(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/util/crash/CrashReport;
 *   Lnet/minecraft/client/gui/screen/Screen;addCrashReportSection(Lnet/minecraft/util/crash/CrashReport;)V
 *   Lnet/minecraft/util/crash/CrashReport;addElement(Ljava/lang/String;)Lnet/minecraft/util/crash/CrashReportSection;
 *   Lnet/minecraft/client/MinecraftClient;openGameMenu(Z)V
 *   Lnet/minecraft/client/gui/screen/DebugOptionsScreen$OptionsListWidget;children()Ljava/util/List;
 *   Lnet/minecraft/client/gui/hud/debug/chart/PieChart;select(I)V
 *   Lnet/minecraft/client/option/KeyBinding;onKeyPressed(Lnet/minecraft/client/util/InputUtil$Key;)V
 *   Lnet/minecraft/client/gui/screen/Screen;charTyped(Lnet/minecraft/client/input/CharInput;)Z
 *   Lnet/minecraft/util/WinNativeModuleUtil;addDetailTo(Lnet/minecraft/util/crash/CrashReportSection;)V
 *   Lnet/minecraft/client/util/Window;logGlError(IJ)V
 *   Lnet/minecraft/client/MinecraftClient;execute(Ljava/lang/Runnable;)V
 *   Lnet/minecraft/text/Style;withClickEvent(Lnet/minecraft/text/ClickEvent;)Lnet/minecraft/text/Style;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/Keyboard;debugLog(Ljava/lang/String;)V
 *   Lnet/minecraft/client/Keyboard;debugLog(Ljava/lang/String;Z)V
 *   Lnet/minecraft/client/Keyboard;sendMessage(Lnet/minecraft/text/Text;)V
 *   Lnet/minecraft/client/Keyboard;debugLog(Lnet/minecraft/text/Text;)V
 *   Lnet/minecraft/client/Keyboard;processDebugKeys(Lnet/minecraft/client/input/KeyInput;)Z
 *   Lnet/minecraft/client/Keyboard;debugLog(Ljava/lang/String;[Ljava/lang/Object;)V
 *   Lnet/minecraft/client/Keyboard;copyLookAt(ZZ)V
 *   Lnet/minecraft/client/Keyboard;copyBlock(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/nbt/NbtCompound;)V
 *   Lnet/minecraft/client/Keyboard;copyEntity(Lnet/minecraft/util/Identifier;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/nbt/NbtCompound;)V
 *   Lnet/minecraft/client/Keyboard;processF3(Lnet/minecraft/client/input/KeyInput;)Z
 *   Lnet/minecraft/client/Keyboard;debugError(Lnet/minecraft/text/Text;)V
 *   Lnet/minecraft/client/Keyboard;onChar(JLnet/minecraft/client/input/CharInput;)V
 *   Lnet/minecraft/client/Keyboard;onKey(JILnet/minecraft/client/input/KeyInput;)V
 */
package net.minecraft.client;

import com.google.common.base.MoreObjects;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.logging.LogUtils;
import java.nio.file.Path;
import java.util.Locale;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.debug.DebugHudEntries;
import net.minecraft.client.gui.navigation.GuiNavigationType;
import net.minecraft.client.gui.screen.DebugOptionsScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.GameModeSwitcherScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.KeybindsScreen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.NarratorMode;
import net.minecraft.client.render.fog.FogRenderer;
import net.minecraft.client.util.Clipboard;
import net.minecraft.client.util.GlfwUtil;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.client.util.Window;
import net.minecraft.command.DefaultPermissions;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.packet.c2s.play.ChangeGameModeC2SPacket;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.command.GameModeCommand;
import net.minecraft.server.command.VersionCommand;
import net.minecraft.storage.NbtWriteView;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ErrorReporter;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.WinNativeModuleUtil;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.util.FeatureDebugLogger;
import org.jspecify.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

@Environment(value=EnvType.CLIENT)
public class Keyboard {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final int DEBUG_CRASH_TIME = 10000;
    private final MinecraftClient client;
    private final Clipboard clipboard = new Clipboard();
    private long debugCrashStartTime = -1L;
    private long debugCrashLastLogTime = -1L;
    private long debugCrashElapsedTime = -1L;
    private boolean switchF3State;

    public Keyboard(MinecraftClient client) {
        this.client = client;
    }

    private boolean processDebugKeys(KeyInput input) {
        switch (input.key()) {
            case 69: {
                if (this.client.player == null) {
                    return false;
                }
                boolean bl = this.client.debugHudEntryList.toggleVisibility(DebugHudEntries.CHUNK_SECTION_PATHS);
                this.debugLog("SectionPath: " + (bl ? "shown" : "hidden"));
                return true;
            }
            case 76: {
                this.client.chunkCullingEnabled = !this.client.chunkCullingEnabled;
                this.debugLog("SmartCull: ", this.client.chunkCullingEnabled);
                return true;
            }
            case 79: {
                if (this.client.player == null) {
                    return false;
                }
                boolean bl2 = this.client.debugHudEntryList.toggleVisibility(DebugHudEntries.CHUNK_SECTION_OCTREE);
                this.debugLog("Frustum culling Octree: ", bl2);
                return true;
            }
            case 70: {
                boolean bl3 = FogRenderer.toggleFog();
                this.debugLog("Fog: ", bl3);
                return true;
            }
            case 85: {
                if (input.hasShift()) {
                    this.client.worldRenderer.killFrustum();
                    this.debugLog("Killed frustum");
                } else {
                    this.client.worldRenderer.captureFrustum();
                    this.debugLog("Captured frustum");
                }
                return true;
            }
            case 86: {
                if (this.client.player == null) {
                    return false;
                }
                boolean bl4 = this.client.debugHudEntryList.toggleVisibility(DebugHudEntries.CHUNK_SECTION_VISIBILITY);
                this.debugLog("SectionVisibility: ", bl4);
                return true;
            }
            case 87: {
                this.client.wireFrame = !this.client.wireFrame;
                this.debugLog("WireFrame: ", this.client.wireFrame);
                return true;
            }
        }
        return false;
    }

    private void debugLog(String message, boolean value) {
        this.debugLog(message + (value ? "enabled" : "disabled"));
    }

    private void sendMessage(Text message) {
        this.client.inGameHud.getChatHud().addMessage(message);
        this.client.getNarratorManager().narrateSystemMessage(message);
    }

    private static Text getDebugMessage(Formatting formatting, Text message) {
        return Text.empty().append(Text.translatable("debug.prefix").formatted(formatting, Formatting.BOLD)).append(ScreenTexts.SPACE).append(message);
    }

    private void debugError(Text message) {
        this.sendMessage(Keyboard.getDebugMessage(Formatting.RED, message));
    }

    private void debugLog(Text text) {
        this.sendMessage(Keyboard.getDebugMessage(Formatting.YELLOW, text));
    }

    private void debugLog(String key, Object ... args) {
        this.debugLog(Text.translatable(key, args));
    }

    private void debugLog(String message) {
        this.debugLog(Text.literal(message));
    }

    private boolean processF3(KeyInput key) {
        boolean bl2;
        if (this.debugCrashStartTime > 0L && this.debugCrashStartTime < Util.getMeasuringTimeMs() - 100L) {
            return true;
        }
        if (SharedConstants.HOTKEYS && this.processDebugKeys(key)) {
            return true;
        }
        if (SharedConstants.FEATURE_COUNT) {
            switch (key.key()) {
                case 82: {
                    FeatureDebugLogger.clear();
                    return true;
                }
                case 76: {
                    FeatureDebugLogger.dump();
                    return true;
                }
            }
        }
        GameOptions lv = this.client.options;
        boolean bl = false;
        if (lv.debugReloadChunkKey.matchesKey(key)) {
            this.client.worldRenderer.reload();
            this.debugLog("debug.reload_chunks.message", new Object[0]);
            bl = true;
        }
        if (lv.debugShowHitboxesKey.matchesKey(key) && this.client.player != null && !this.client.player.hasReducedDebugInfo()) {
            bl2 = this.client.debugHudEntryList.toggleVisibility(DebugHudEntries.ENTITY_HITBOXES);
            this.debugLog(bl2 ? "debug.show_hitboxes.on" : "debug.show_hitboxes.off", new Object[0]);
            bl = true;
        }
        if (lv.debugClearChatKey.matchesKey(key)) {
            this.client.inGameHud.getChatHud().clear(false);
            bl = true;
        }
        if (lv.debugShowChunkBordersKey.matchesKey(key) && this.client.player != null && !this.client.player.hasReducedDebugInfo()) {
            bl2 = this.client.debugHudEntryList.toggleVisibility(DebugHudEntries.CHUNK_BORDERS);
            this.debugLog(bl2 ? "debug.chunk_boundaries.on" : "debug.chunk_boundaries.off", new Object[0]);
            bl = true;
        }
        if (lv.debugShowAdvancedTooltipsKey.matchesKey(key)) {
            lv.advancedItemTooltips = !lv.advancedItemTooltips;
            this.debugLog(lv.advancedItemTooltips ? "debug.advanced_tooltips.on" : "debug.advanced_tooltips.off", new Object[0]);
            lv.write();
            bl = true;
        }
        if (lv.debugCopyRecreateCommandKey.matchesKey(key)) {
            if (this.client.player != null && !this.client.player.hasReducedDebugInfo()) {
                this.copyLookAt(this.client.player.getPermissions().hasPermission(DefaultPermissions.GAMEMASTERS), !key.hasShift());
            }
            bl = true;
        }
        if (lv.debugSpectateKey.matchesKey(key)) {
            if (this.client.player == null || !GameModeCommand.PERMISSION_CHECK.allows(this.client.player.getPermissions())) {
                this.debugLog("debug.creative_spectator.error", new Object[0]);
            } else if (!this.client.player.isSpectator()) {
                this.client.player.networkHandler.sendPacket(new ChangeGameModeC2SPacket(GameMode.SPECTATOR));
            } else {
                GameMode lv2 = MoreObjects.firstNonNull(this.client.interactionManager.getPreviousGameMode(), GameMode.CREATIVE);
                this.client.player.networkHandler.sendPacket(new ChangeGameModeC2SPacket(lv2));
            }
            bl = true;
        }
        if (lv.debugSwitchGameModeKey.matchesKey(key) && this.client.world != null && this.client.currentScreen == null) {
            if (this.client.canSwitchGameMode() && GameModeCommand.PERMISSION_CHECK.allows(this.client.player.getPermissions())) {
                this.client.setScreen(new GameModeSwitcherScreen());
            } else {
                this.debugLog("debug.gamemodes.error", new Object[0]);
            }
            bl = true;
        }
        if (lv.debugOptionsKey.matchesKey(key)) {
            if (this.client.currentScreen instanceof DebugOptionsScreen) {
                this.client.currentScreen.close();
            } else if (this.client.canCurrentScreenInterruptOtherScreen()) {
                if (this.client.currentScreen != null) {
                    this.client.currentScreen.close();
                }
                this.client.setScreen(new DebugOptionsScreen());
            }
            bl = true;
        }
        if (lv.debugFocusPauseKey.matchesKey(key)) {
            lv.pauseOnLostFocus = !lv.pauseOnLostFocus;
            lv.write();
            this.debugLog(lv.pauseOnLostFocus ? "debug.pause_focus.on" : "debug.pause_focus.off", new Object[0]);
            bl = true;
        }
        if (lv.debugDumpDynamicTexturesKey.matchesKey(key)) {
            Path path = this.client.runDirectory.toPath().toAbsolutePath();
            Path path2 = TextureUtil.getDebugTexturePath(path);
            this.client.getTextureManager().dumpDynamicTextures(path2);
            MutableText lv3 = Text.literal(path.relativize(path2).toString()).formatted(Formatting.UNDERLINE).styled(style -> style.withClickEvent(new ClickEvent.OpenFile(path2)));
            this.debugLog(Text.translatable("debug.dump_dynamic_textures", lv3));
            bl = true;
        }
        if (lv.debugReloadResourcePacksKey.matchesKey(key)) {
            this.debugLog("debug.reload_resourcepacks.message", new Object[0]);
            this.client.reloadResources();
            bl = true;
        }
        if (lv.debugProfilingKey.matchesKey(key)) {
            if (this.client.toggleDebugProfiler(this::debugLog)) {
                this.debugLog(Text.translatable("debug.profiling.start", 10, lv.debugModifierKey.getBoundKeyLocalizedText(), lv.debugProfilingKey.getBoundKeyLocalizedText()));
            }
            bl = true;
        }
        if (lv.debugCopyLocationKey.matchesKey(key) && this.client.player != null && !this.client.player.hasReducedDebugInfo()) {
            this.debugLog("debug.copy_location.message", new Object[0]);
            this.setClipboard(String.format(Locale.ROOT, "/execute in %s run tp @s %.2f %.2f %.2f %.2f %.2f", this.client.player.getEntityWorld().getRegistryKey().getValue(), this.client.player.getX(), this.client.player.getY(), this.client.player.getZ(), Float.valueOf(this.client.player.getYaw()), Float.valueOf(this.client.player.getPitch())));
            bl = true;
        }
        if (lv.debugDumpVersionKey.matchesKey(key)) {
            this.debugLog("debug.version.header", new Object[0]);
            VersionCommand.acceptInfo(this::sendMessage);
            bl = true;
        }
        if (lv.debugProfilingChartKey.matchesKey(key)) {
            this.client.getDebugHud().toggleRenderingChart();
            bl = true;
        }
        if (lv.debugFpsChartsKey.matchesKey(key)) {
            this.client.getDebugHud().toggleRenderingAndTickCharts();
            bl = true;
        }
        if (lv.debugNetworkChartsKey.matchesKey(key)) {
            this.client.getDebugHud().togglePacketSizeAndPingCharts();
            bl = true;
        }
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void copyLookAt(boolean hasQueryPermission, boolean queryServer) {
        HitResult lv = this.client.crosshairTarget;
        if (lv == null) {
            return;
        }
        switch (lv.getType()) {
            case BLOCK: {
                BlockPos lv2 = ((BlockHitResult)lv).getBlockPos();
                World lv3 = this.client.player.getEntityWorld();
                BlockState lv4 = lv3.getBlockState(lv2);
                if (!hasQueryPermission) {
                    this.copyBlock(lv4, lv2, null);
                    this.debugLog("debug.inspect.client.block", new Object[0]);
                    return;
                }
                if (queryServer) {
                    this.client.player.networkHandler.getDataQueryHandler().queryBlockNbt(lv2, nbt -> {
                        this.copyBlock(lv4, lv2, (NbtCompound)nbt);
                        this.debugLog("debug.inspect.server.block", new Object[0]);
                    });
                    return;
                }
                BlockEntity lv5 = lv3.getBlockEntity(lv2);
                NbtCompound lv6 = lv5 != null ? lv5.createNbt(lv3.getRegistryManager()) : null;
                this.copyBlock(lv4, lv2, lv6);
                this.debugLog("debug.inspect.client.block", new Object[0]);
                return;
            }
            case ENTITY: {
                Entity lv7 = ((EntityHitResult)lv).getEntity();
                Identifier lv8 = Registries.ENTITY_TYPE.getId(lv7.getType());
                if (!hasQueryPermission) {
                    this.copyEntity(lv8, lv7.getEntityPos(), null);
                    this.debugLog("debug.inspect.client.entity", new Object[0]);
                    return;
                }
                if (queryServer) {
                    this.client.player.networkHandler.getDataQueryHandler().queryEntityNbt(lv7.getId(), nbt -> {
                        this.copyEntity(lv8, lv7.getEntityPos(), (NbtCompound)nbt);
                        this.debugLog("debug.inspect.server.entity", new Object[0]);
                    });
                    return;
                }
                try (ErrorReporter.Logging lv9 = new ErrorReporter.Logging(lv7.getErrorReporterContext(), LOGGER);){
                    NbtWriteView lv10 = NbtWriteView.create(lv9, lv7.getRegistryManager());
                    lv7.writeData(lv10);
                    this.copyEntity(lv8, lv7.getEntityPos(), lv10.getNbt());
                }
                this.debugLog("debug.inspect.client.entity", new Object[0]);
                return;
            }
        }
    }

    private void copyBlock(BlockState state, BlockPos pos, @Nullable NbtCompound nbt) {
        StringBuilder stringBuilder = new StringBuilder(BlockArgumentParser.stringifyBlockState(state));
        if (nbt != null) {
            stringBuilder.append(nbt);
        }
        String string = String.format(Locale.ROOT, "/setblock %d %d %d %s", pos.getX(), pos.getY(), pos.getZ(), stringBuilder);
        this.setClipboard(string);
    }

    private void copyEntity(Identifier id, Vec3d pos, @Nullable NbtCompound nbt) {
        String string2;
        if (nbt != null) {
            nbt.remove("UUID");
            nbt.remove("Pos");
            String string = NbtHelper.toPrettyPrintedText(nbt).getString();
            string2 = String.format(Locale.ROOT, "/summon %s %.2f %.2f %.2f %s", id, pos.x, pos.y, pos.z, string);
        } else {
            string2 = String.format(Locale.ROOT, "/summon %s %.2f %.2f %.2f", id, pos.x, pos.y, pos.z);
        }
        this.setClipboard(string2);
    }

    private void onKey(long window, @KeyInput.KeyAction int action, KeyInput input) {
        int j;
        GameMenuScreen lv8;
        Screen screen;
        boolean bl6;
        Screen lv3;
        boolean bl3;
        Window lv = this.client.getWindow();
        if (window != lv.getHandle()) {
            return;
        }
        this.client.getInactivityFpsLimiter().onInput();
        GameOptions lv2 = this.client.options;
        boolean bl = lv2.debugModifierKey.boundKey.getCode() == lv2.debugOverlayKey.boundKey.getCode();
        boolean bl2 = lv2.debugModifierKey.isPressed();
        boolean bl4 = bl3 = !lv2.debugCrashKey.isUnbound() && InputUtil.isKeyPressed(this.client.getWindow(), lv2.debugCrashKey.boundKey.getCode());
        if (this.debugCrashStartTime > 0L) {
            if (!bl3 || !bl2) {
                this.debugCrashStartTime = -1L;
            }
        } else if (bl3 && bl2) {
            this.switchF3State = bl;
            this.debugCrashStartTime = Util.getMeasuringTimeMs();
            this.debugCrashLastLogTime = Util.getMeasuringTimeMs();
            this.debugCrashElapsedTime = 0L;
        }
        if ((lv3 = this.client.currentScreen) != null) {
            switch (input.key()) {
                case 262: 
                case 263: 
                case 264: 
                case 265: {
                    this.client.setNavigationType(GuiNavigationType.KEYBOARD_ARROW);
                    break;
                }
                case 258: {
                    this.client.setNavigationType(GuiNavigationType.KEYBOARD_TAB);
                }
            }
        }
        if (!(action != InputUtil.GLFW_PRESS || this.client.currentScreen instanceof KeybindsScreen && ((KeybindsScreen)lv3).lastKeyCodeUpdateTime > Util.getMeasuringTimeMs() - 20L)) {
            if (lv2.fullscreenKey.matchesKey(input)) {
                lv.toggleFullscreen();
                boolean bl42 = lv.isFullscreen();
                lv2.getFullscreen().setValue(bl42);
                lv2.write();
                Screen screen2 = this.client.currentScreen;
                if (screen2 instanceof VideoOptionsScreen) {
                    VideoOptionsScreen lv4 = (VideoOptionsScreen)screen2;
                    lv4.updateFullscreenButtonValue(bl42);
                }
                return;
            }
            if (lv2.screenshotKey.matchesKey(input)) {
                if (input.hasCtrlOrCmd() && SharedConstants.PANORAMA_SCREENSHOT) {
                    this.sendMessage(this.client.takePanorama(this.client.runDirectory));
                } else {
                    ScreenshotRecorder.saveScreenshot(this.client.runDirectory, this.client.getFramebuffer(), message -> this.client.execute(() -> this.sendMessage((Text)message)));
                }
                return;
            }
        }
        if (action != 0) {
            boolean bl43;
            boolean bl5 = bl43 = lv3 == null || !(lv3.getFocused() instanceof TextFieldWidget) || !((TextFieldWidget)lv3.getFocused()).isActive();
            if (bl43) {
                if (input.hasCtrlOrCmd() && input.key() == InputUtil.GLFW_KEY_B && this.client.getNarratorManager().isActive() && lv2.getNarratorHotkey().getValue().booleanValue()) {
                    boolean bl52 = lv2.getNarrator().getValue() == NarratorMode.OFF;
                    lv2.getNarrator().setValue(NarratorMode.byId(lv2.getNarrator().getValue().getId() + 1));
                    lv2.write();
                    if (lv3 != null) {
                        lv3.refreshNarrator(bl52);
                    }
                }
                ClientPlayerEntity bl52 = this.client.player;
            }
        }
        if (lv3 != null) {
            try {
                if (action == InputUtil.GLFW_PRESS || action == InputUtil.GLFW_REPEAT) {
                    lv3.applyKeyPressNarratorDelay();
                    if (lv3.keyPressed(input)) {
                        if (this.client.currentScreen == null) {
                            InputUtil.Key lv5 = InputUtil.fromKeyCode(input);
                            KeyBinding.setKeyPressed(lv5, false);
                        }
                        return;
                    }
                } else if (action == 0 && lv3.keyReleased(input)) {
                    if (lv2.debugModifierKey.matchesKey(input)) {
                        this.switchF3State = false;
                    }
                    return;
                }
            } catch (Throwable throwable) {
                CrashReport lv6 = CrashReport.create(throwable, "keyPressed event handler");
                lv3.addCrashReportSection(lv6);
                CrashReportSection lv7 = lv6.addElement("Key");
                lv7.add("Key", input.key());
                lv7.add("Scancode", input.scancode());
                lv7.add("Mods", input.modifiers());
                throw new CrashException(lv6);
            }
        }
        InputUtil.Key lv5 = InputUtil.fromKeyCode(input);
        boolean bl5 = this.client.currentScreen == null;
        boolean bl7 = bl6 = bl5 || (screen = this.client.currentScreen) instanceof GameMenuScreen && !(lv8 = (GameMenuScreen)screen).shouldShowMenu() || this.client.currentScreen instanceof GameModeSwitcherScreen;
        if (bl && lv2.debugModifierKey.matchesKey(input) && action == 0) {
            if (this.switchF3State) {
                this.switchF3State = false;
            } else {
                this.client.debugHudEntryList.toggleF3Enabled();
            }
        } else if (!bl && lv2.debugOverlayKey.matchesKey(input) && action == InputUtil.GLFW_PRESS) {
            this.client.debugHudEntryList.toggleF3Enabled();
        }
        if (action == 0) {
            KeyBinding.setKeyPressed(lv5, false);
            return;
        }
        boolean bl72 = false;
        if (bl6 && input.isEscape()) {
            this.client.openGameMenu(bl2);
            bl72 = bl2;
        } else if (bl2) {
            DebugOptionsScreen lv9;
            DebugOptionsScreen.OptionsListWidget lv10;
            bl72 = this.processF3(input);
            if (bl72 && lv3 instanceof DebugOptionsScreen && (lv10 = (lv9 = (DebugOptionsScreen)lv3).getOptionsListWidget()) != null) {
                lv10.children().forEach(DebugOptionsScreen.AbstractEntry::init);
            }
        } else if (bl6 && lv2.toggleGuiKey.matchesKey(input)) {
            lv2.hudHidden = !lv2.hudHidden;
        } else if (bl6 && lv2.toggleSpectatorShaderEffectsKey.matchesKey(input)) {
            this.client.gameRenderer.togglePostProcessorEnabled();
        }
        if (bl) {
            this.switchF3State |= bl72;
        }
        if (this.client.getDebugHud().shouldShowRenderingChart() && !bl2 && (j = input.asNumber()) != -1) {
            this.client.getDebugHud().getPieChart().select(j);
        }
        if (bl5 || lv5 == lv2.debugModifierKey.boundKey) {
            if (bl72) {
                KeyBinding.setKeyPressed(lv5, false);
            } else {
                KeyBinding.setKeyPressed(lv5, true);
                KeyBinding.onKeyPressed(lv5);
            }
        }
    }

    private void onChar(long window, CharInput input) {
        if (window != this.client.getWindow().getHandle()) {
            return;
        }
        Screen lv = this.client.currentScreen;
        if (lv == null || this.client.getOverlay() != null) {
            return;
        }
        try {
            lv.charTyped(input);
        } catch (Throwable throwable) {
            CrashReport lv2 = CrashReport.create(throwable, "charTyped event handler");
            lv.addCrashReportSection(lv2);
            CrashReportSection lv3 = lv2.addElement("Key");
            lv3.add("Codepoint", input.codepoint());
            lv3.add("Mods", input.modifiers());
            throw new CrashException(lv2);
        }
    }

    public void setup(Window window2) {
        InputUtil.setKeyboardCallbacks(window2, (handle, key, scancode, action, modifiers) -> {
            KeyInput lv = new KeyInput(key, scancode, modifiers);
            this.client.execute(() -> this.onKey(handle, action, lv));
        }, (window, codePoint, modifiers) -> {
            CharInput lv = new CharInput(codePoint, modifiers);
            this.client.execute(() -> this.onChar(window, lv));
        });
    }

    public String getClipboard() {
        return this.clipboard.get(this.client.getWindow(), (error, description) -> {
            if (error != GLFW.GLFW_FORMAT_UNAVAILABLE) {
                this.client.getWindow().logGlError(error, description);
            }
        });
    }

    public void setClipboard(String clipboard) {
        if (!clipboard.isEmpty()) {
            this.clipboard.set(this.client.getWindow(), clipboard);
        }
    }

    public void pollDebugCrash() {
        if (this.debugCrashStartTime > 0L) {
            long l = Util.getMeasuringTimeMs();
            long m = 10000L - (l - this.debugCrashStartTime);
            long n = l - this.debugCrashLastLogTime;
            if (m < 0L) {
                if (this.client.isCtrlPressed()) {
                    GlfwUtil.makeJvmCrash();
                }
                String string = "Manually triggered debug crash";
                CrashReport lv = new CrashReport("Manually triggered debug crash", new Throwable("Manually triggered debug crash"));
                CrashReportSection lv2 = lv.addElement("Manual crash details");
                WinNativeModuleUtil.addDetailTo(lv2);
                throw new CrashException(lv);
            }
            if (n >= 1000L) {
                if (this.debugCrashElapsedTime == 0L) {
                    this.debugLog("debug.crash.message", this.client.options.debugModifierKey.getBoundKeyLocalizedText().getString(), this.client.options.debugCrashKey.getBoundKeyLocalizedText().getString());
                } else {
                    this.debugError(Text.translatable("debug.crash.warning", MathHelper.ceil((float)m / 1000.0f)));
                }
                this.debugCrashLastLogTime = l;
                ++this.debugCrashElapsedTime;
            }
        }
    }
}

