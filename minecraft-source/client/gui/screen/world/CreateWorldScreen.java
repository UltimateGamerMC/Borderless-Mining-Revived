/*
 * External method calls:
 *   Lnet/minecraft/server/SaveLoading;load(Lnet/minecraft/server/SaveLoading$ServerConfig;Lnet/minecraft/server/SaveLoading$LoadContextSupplier;Lnet/minecraft/server/SaveLoading$SaveApplierFactory;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/client/MinecraftClient;runTasks(Ljava/util/function/BooleanSupplier;)V
 *   Lnet/minecraft/client/world/GeneratorOptionsHolder;selectedDimensions()Lnet/minecraft/world/dimension/DimensionOptionsRegistryHolder;
 *   Lnet/minecraft/client/world/GeneratorOptionsHolder;generatorOptions()Lnet/minecraft/world/gen/GeneratorOptions;
 *   Lnet/minecraft/world/rule/GameRules;copyFrom(Lnet/minecraft/world/rule/GameRules;Lnet/minecraft/server/MinecraftServer;)V
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/gui/widget/TabNavigationWidget;builder(Lnet/minecraft/client/gui/tab/TabManager;I)Lnet/minecraft/client/gui/widget/TabNavigationWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/TabNavigationWidget$Builder;tabs([Lnet/minecraft/client/gui/tab/Tab;)Lnet/minecraft/client/gui/widget/TabNavigationWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/TabNavigationWidget$Builder;build()Lnet/minecraft/client/gui/widget/TabNavigationWidget;
 *   Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;horizontal()Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;
 *   Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;spacing(I)Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;
 *   Lnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;addFooter(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget;builder(Lnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;build()Lnet/minecraft/client/gui/widget/ButtonWidget;
 *   Lnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;forEachChild(Ljava/util/function/Consumer;)V
 *   Lnet/minecraft/client/gui/widget/TabNavigationWidget;selectTab(IZ)V
 *   Lnet/minecraft/client/world/GeneratorOptionsHolder;dimensionOptionsRegistry()Lnet/minecraft/registry/Registry;
 *   Lnet/minecraft/world/dimension/DimensionOptionsRegistryHolder;toConfig(Lnet/minecraft/registry/Registry;)Lnet/minecraft/world/dimension/DimensionOptionsRegistryHolder$DimensionsConfig;
 *   Lnet/minecraft/client/world/GeneratorOptionsHolder;combinedDynamicRegistries()Lnet/minecraft/registry/CombinedDynamicRegistries;
 *   Lnet/minecraft/world/dimension/DimensionOptionsRegistryHolder$DimensionsConfig;toDynamicRegistryManager()Lnet/minecraft/registry/DynamicRegistryManager$Immutable;
 *   Lnet/minecraft/registry/CombinedDynamicRegistries;with(Ljava/lang/Object;[Lnet/minecraft/registry/DynamicRegistryManager$Immutable;)Lnet/minecraft/registry/CombinedDynamicRegistries;
 *   Lnet/minecraft/client/world/GeneratorOptionsHolder;dataConfiguration()Lnet/minecraft/resource/DataConfiguration;
 *   Lnet/minecraft/resource/DataConfiguration;enabledFeatures()Lnet/minecraft/resource/featuretoggle/FeatureSet;
 *   Lnet/minecraft/world/dimension/DimensionOptionsRegistryHolder$DimensionsConfig;specialWorldProperty()Lnet/minecraft/world/level/LevelProperties$SpecialProperty;
 *   Lnet/minecraft/server/integrated/IntegratedServerLoader;tryLoad(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/gui/screen/world/CreateWorldScreen;Lcom/mojang/serialization/Lifecycle;Ljava/lang/Runnable;Z)V
 *   Lnet/minecraft/client/gui/screen/world/CreateWorldCallback;create(Lnet/minecraft/client/gui/screen/world/CreateWorldScreen;Lnet/minecraft/registry/CombinedDynamicRegistries;Lnet/minecraft/world/level/LevelProperties;Ljava/nio/file/Path;)Z
 *   Lnet/minecraft/client/toast/SystemToast;addPackCopyFailure(Lnet/minecraft/client/MinecraftClient;Ljava/lang/String;)V
 *   Lnet/minecraft/client/MinecraftClient;createIntegratedServerLoader()Lnet/minecraft/server/integrated/IntegratedServerLoader;
 *   Lnet/minecraft/client/world/GeneratorOptionsHolder;dataPackContents()Lnet/minecraft/server/DataPackContents;
 *   Lnet/minecraft/server/integrated/IntegratedServerLoader;startNewWorld(Lnet/minecraft/world/level/storage/LevelStorage$Session;Lnet/minecraft/server/DataPackContents;Lnet/minecraft/registry/CombinedDynamicRegistries;Lnet/minecraft/world/SaveProperties;)V
 *   Lnet/minecraft/client/gui/widget/TabNavigationWidget;keyPressed(Lnet/minecraft/client/input/KeyInput;)Z
 *   Lnet/minecraft/client/gui/screen/Screen;keyPressed(Lnet/minecraft/client/input/KeyInput;)Z
 *   Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/gui/DrawContext;IIF)V
 *   Lnet/minecraft/client/gui/DrawContext;drawTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIFFIIII)V
 *   Lnet/minecraft/client/gui/screen/world/WorldCreator;updateDataConfiguration(Lnet/minecraft/resource/DataConfiguration;)Z
 *   Lnet/minecraft/util/Util;relativeCopy(Ljava/nio/file/Path;Ljava/nio/file/Path;Ljava/nio/file/Path;)V
 *   Lnet/minecraft/world/level/storage/LevelStorage;createSessionWithoutSymlinkCheck(Ljava/lang/String;)Lnet/minecraft/world/level/storage/LevelStorage$Session;
 *   Lnet/minecraft/util/path/PathUtil;createDirectories(Ljava/nio/file/Path;)V
 *   Lnet/minecraft/resource/VanillaDataPackProvider;createManager(Ljava/nio/file/Path;Lnet/minecraft/util/path/SymlinkFinder;)Lnet/minecraft/resource/ResourcePackManager;
 *   Lnet/minecraft/resource/DataConfiguration;dataPacks()Lnet/minecraft/resource/DataPackSettings;
 *   Lnet/minecraft/client/gui/screen/world/WorldCreationSettings;worldGenSettings()Lnet/minecraft/world/level/WorldGenSettings;
 *   Lnet/minecraft/client/gui/screen/world/WorldCreationSettings;dataConfiguration()Lnet/minecraft/resource/DataConfiguration;
 *   Lnet/minecraft/server/SaveLoading$LoadContextSupplierContext;worldGenRegistryManager()Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;
 *   Lnet/minecraft/registry/RegistryWrapper$Impl;streamEntries()Ljava/util/stream/Stream;
 *   Lnet/minecraft/world/level/WorldGenSettings;encode(Lcom/mojang/serialization/DynamicOps;Lnet/minecraft/world/gen/GeneratorOptions;Lnet/minecraft/world/dimension/DimensionOptionsRegistryHolder;)Lcom/mojang/serialization/DataResult;
 *   Lnet/minecraft/server/SaveLoading$LoadContextSupplierContext;dataConfiguration()Lnet/minecraft/resource/DataConfiguration;
 *   Lnet/minecraft/server/SaveLoading$LoadContextSupplierContext;dimensionsRegistryManager()Lnet/minecraft/registry/DynamicRegistryManager$Immutable;
 *   Lnet/minecraft/client/world/GeneratorOptionsFactory;apply(Lnet/minecraft/server/DataPackContents;Lnet/minecraft/registry/CombinedDynamicRegistries;Lnet/minecraft/client/gui/screen/world/WorldCreationSettings;)Lnet/minecraft/client/world/GeneratorOptionsHolder;
 *   Lnet/minecraft/world/gen/GeneratorOptions;createTestWorld()Lnet/minecraft/world/gen/GeneratorOptions;
 *   Lnet/minecraft/world/gen/WorldPresets;createTestOptions(Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/world/dimension/DimensionOptionsRegistryHolder;
 *   Lnet/minecraft/world/level/WorldGenSettings;generatorOptions()Lnet/minecraft/world/gen/GeneratorOptions;
 *   Lnet/minecraft/world/level/WorldGenSettings;dimensionOptionsRegistryHolder()Lnet/minecraft/world/dimension/DimensionOptionsRegistryHolder;
 *   Lnet/minecraft/world/rule/ServerGameRules$Builder;build()Lnet/minecraft/world/rule/ServerGameRules;
 *   Lnet/minecraft/world/gen/GeneratorOptions;createRandom()Lnet/minecraft/world/gen/GeneratorOptions;
 *   Lnet/minecraft/world/gen/WorldPresets;createDemoOptions(Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/world/dimension/DimensionOptionsRegistryHolder;
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/screen/world/CreateWorldScreen;show(Lnet/minecraft/client/MinecraftClient;Ljava/lang/Runnable;Lnet/minecraft/client/gui/screen/world/CreateWorldCallback;)V
 *   Lnet/minecraft/client/gui/screen/world/CreateWorldScreen;show(Lnet/minecraft/client/MinecraftClient;Ljava/lang/Runnable;Ljava/util/function/Function;Lnet/minecraft/client/world/GeneratorOptionsFactory;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/client/gui/screen/world/CreateWorldCallback;)V
 *   Lnet/minecraft/client/gui/screen/world/CreateWorldScreen;showMessage(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/text/Text;)V
 *   Lnet/minecraft/client/gui/screen/world/CreateWorldScreen;createServerConfig(Lnet/minecraft/resource/ResourcePackManager;Lnet/minecraft/resource/DataConfiguration;)Lnet/minecraft/server/SaveLoading$ServerConfig;
 *   Lnet/minecraft/client/gui/screen/world/CreateWorldScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;
 *   Lnet/minecraft/client/gui/screen/world/CreateWorldScreen;createLevelInfo(Z)Lnet/minecraft/world/level/LevelInfo;
 *   Lnet/minecraft/client/gui/screen/world/CreateWorldScreen;createSession(Lnet/minecraft/client/MinecraftClient;Ljava/lang/String;Ljava/nio/file/Path;)Ljava/util/Optional;
 *   Lnet/minecraft/client/gui/screen/world/CreateWorldScreen;renderDarkening(Lnet/minecraft/client/gui/DrawContext;IIII)V
 *   Lnet/minecraft/client/gui/screen/world/CreateWorldScreen;validateDataPacks(Lnet/minecraft/resource/ResourcePackManager;Lnet/minecraft/resource/DataConfiguration;Ljava/util/function/Consumer;)V
 *   Lnet/minecraft/client/gui/screen/world/CreateWorldScreen;copyDataPack(Ljava/nio/file/Path;Ljava/nio/file/Path;Ljava/nio/file/Path;)V
 *   Lnet/minecraft/client/gui/screen/world/CreateWorldScreen;applyDataPacks(Lnet/minecraft/resource/ResourcePackManager;ZLjava/util/function/Consumer;)V
 *   Lnet/minecraft/client/gui/screen/world/CreateWorldScreen;createAndClearTempDir(Lnet/minecraft/registry/CombinedDynamicRegistries;Lnet/minecraft/world/level/LevelProperties;)V
 *   Lnet/minecraft/client/gui/screen/world/CreateWorldScreen;startServer(Lnet/minecraft/registry/CombinedDynamicRegistries;Lnet/minecraft/world/SaveProperties;)Z
 */
package net.minecraft.client.gui.screen.world;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.MessageScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.pack.ExperimentalWarningScreen;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.screen.world.CreateWorldCallback;
import net.minecraft.client.gui.screen.world.EditGameRulesScreen;
import net.minecraft.client.gui.screen.world.ExperimentsScreen;
import net.minecraft.client.gui.screen.world.InitialWorldOptions;
import net.minecraft.client.gui.screen.world.LevelScreenProvider;
import net.minecraft.client.gui.screen.world.WorldCreationSettings;
import net.minecraft.client.gui.screen.world.WorldCreator;
import net.minecraft.client.gui.screen.world.WorldScreenOptionGrid;
import net.minecraft.client.gui.tab.GridScreenTab;
import net.minecraft.client.gui.tab.TabManager;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.LayoutWidgets;
import net.minecraft.client.gui.widget.Positioner;
import net.minecraft.client.gui.widget.TabNavigationWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.world.GeneratorOptionsFactory;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.command.permission.LeveledPermissionPredicate;
import net.minecraft.registry.CombinedDynamicRegistries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.ServerDynamicRegistryType;
import net.minecraft.resource.DataConfiguration;
import net.minecraft.resource.DataPackSettings;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.VanillaDataPackProvider;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.SaveLoading;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.integrated.IntegratedServerLoader;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.path.PathUtil;
import net.minecraft.util.path.SymlinkFinder;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameMode;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.dimension.DimensionOptionsRegistryHolder;
import net.minecraft.world.gen.FlatLevelGeneratorPresets;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.WorldPreset;
import net.minecraft.world.gen.WorldPresets;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;
import net.minecraft.world.level.WorldGenSettings;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.rule.GameRules;
import net.minecraft.world.rule.ServerGameRules;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(value=EnvType.CLIENT)
public class CreateWorldScreen
extends Screen {
    private static final int field_42165 = 1;
    private static final int field_42166 = 210;
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String TEMP_DIR_PREFIX = "mcworld-";
    static final Text GAME_MODE_TEXT = Text.translatable("selectWorld.gameMode");
    static final Text ENTER_NAME_TEXT = Text.translatable("selectWorld.enterName");
    static final Text EXPERIMENTS_TEXT = Text.translatable("selectWorld.experiments");
    static final Text ALLOW_COMMANDS_INFO_TEXT = Text.translatable("selectWorld.allowCommands.info");
    private static final Text PREPARING_TEXT = Text.translatable("createWorld.preparing");
    private static final int field_42170 = 10;
    private static final int field_42171 = 8;
    public static final Identifier TAB_HEADER_BACKGROUND_TEXTURE = Identifier.ofVanilla("textures/gui/tab_header_background.png");
    private final ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);
    final WorldCreator worldCreator;
    private final TabManager tabManager = new TabManager(clickableWidget -> {
        ClickableWidget cfr_ignored_0 = (ClickableWidget)this.addDrawableChild(clickableWidget);
    }, child -> this.remove((Element)child));
    private boolean recreated;
    private final SymlinkFinder symlinkFinder;
    private final CreateWorldCallback callback;
    private final Runnable onClosed;
    private @Nullable Path dataPackTempDir;
    private @Nullable ResourcePackManager packManager;
    private @Nullable TabNavigationWidget tabNavigation;

    public static void show(MinecraftClient client, Runnable onClosed) {
        CreateWorldScreen.show(client, onClosed, (screen, combinedDynamicRegistries, levelProperties, dataPackTempDir) -> screen.startServer(combinedDynamicRegistries, levelProperties));
    }

    public static void show(MinecraftClient client, Runnable onClosed, CreateWorldCallback callback) {
        GeneratorOptionsFactory lv = (dataPackContents, dynamicRegistries, settings) -> new GeneratorOptionsHolder(settings.worldGenSettings(), dynamicRegistries, dataPackContents, settings.dataConfiguration());
        Function<SaveLoading.LoadContextSupplierContext, WorldGenSettings> function = context -> new WorldGenSettings(GeneratorOptions.createRandom(), WorldPresets.createDemoOptions(context.worldGenRegistryManager()));
        CreateWorldScreen.show(client, onClosed, function, lv, WorldPresets.DEFAULT, callback);
    }

    public static void showTestWorld(MinecraftClient client, Runnable onClosed) {
        GeneratorOptionsFactory lv = (dataPackContents, dynamicRegistries, settings) -> new GeneratorOptionsHolder(settings.worldGenSettings().generatorOptions(), settings.worldGenSettings().dimensionOptionsRegistryHolder(), dynamicRegistries, dataPackContents, settings.dataConfiguration(), new InitialWorldOptions(WorldCreator.Mode.CREATIVE, new ServerGameRules.Builder().put(GameRules.ADVANCE_TIME, false).put(GameRules.ADVANCE_WEATHER, false).put(GameRules.DO_MOB_SPAWNING, false).build(), FlatLevelGeneratorPresets.REDSTONE_READY));
        Function<SaveLoading.LoadContextSupplierContext, WorldGenSettings> function = context -> new WorldGenSettings(GeneratorOptions.createTestWorld(), WorldPresets.createTestOptions(context.worldGenRegistryManager()));
        CreateWorldScreen.show(client, onClosed, function, lv, WorldPresets.FLAT, (screen, combinedDynamicRegistries, levelProperties, dataPackTempDir) -> screen.startServer(combinedDynamicRegistries, levelProperties));
    }

    private static void show(MinecraftClient client, Runnable onClosed, Function<SaveLoading.LoadContextSupplierContext, WorldGenSettings> settingsSupplier, GeneratorOptionsFactory generatorOptionsFactory, RegistryKey<WorldPreset> presetKey, CreateWorldCallback callback) {
        CreateWorldScreen.showMessage(client, PREPARING_TEXT);
        ResourcePackManager lv = new ResourcePackManager(new VanillaDataPackProvider(client.getSymlinkFinder()));
        DataConfiguration lv2 = SharedConstants.isDevelopment ? new DataConfiguration(new DataPackSettings(List.of("vanilla", "tests"), List.of()), FeatureFlags.DEFAULT_ENABLED_FEATURES) : DataConfiguration.SAFE_MODE;
        SaveLoading.ServerConfig lv3 = CreateWorldScreen.createServerConfig(lv, lv2);
        CompletableFuture<GeneratorOptionsHolder> completableFuture = SaveLoading.load(lv3, context -> new SaveLoading.LoadContext<WorldCreationSettings>(new WorldCreationSettings((WorldGenSettings)settingsSupplier.apply(context), context.dataConfiguration()), context.dimensionsRegistryManager()), (resourceManager, dataPackContents, dynamicRegistries, settings) -> {
            resourceManager.close();
            return generatorOptionsFactory.apply(dataPackContents, dynamicRegistries, (WorldCreationSettings)settings);
        }, Util.getMainWorkerExecutor(), client);
        client.runTasks(completableFuture::isDone);
        client.setScreen(new CreateWorldScreen(client, onClosed, completableFuture.join(), Optional.of(presetKey), OptionalLong.empty(), callback));
    }

    public static CreateWorldScreen create(MinecraftClient client, Runnable onClosed, LevelInfo levelInfo, GeneratorOptionsHolder generatorOptionsHolder, @Nullable Path dataPackTempDir2) {
        CreateWorldScreen lv = new CreateWorldScreen(client, onClosed, generatorOptionsHolder, WorldPresets.getWorldPreset(generatorOptionsHolder.selectedDimensions()), OptionalLong.of(generatorOptionsHolder.generatorOptions().getSeed()), (screen, combinedDynamicRegistries, levelProperties, dataPackTempDir) -> screen.startServer(combinedDynamicRegistries, levelProperties));
        lv.recreated = true;
        lv.worldCreator.setWorldName(levelInfo.getLevelName());
        lv.worldCreator.setCheatsEnabled(levelInfo.areCommandsAllowed());
        lv.worldCreator.setDifficulty(levelInfo.getDifficulty());
        lv.worldCreator.getGameRules().copyFrom(levelInfo.getGameRules(), null);
        if (levelInfo.isHardcore()) {
            lv.worldCreator.setGameMode(WorldCreator.Mode.HARDCORE);
        } else if (levelInfo.getGameMode().isSurvivalLike()) {
            lv.worldCreator.setGameMode(WorldCreator.Mode.SURVIVAL);
        } else if (levelInfo.getGameMode().isCreative()) {
            lv.worldCreator.setGameMode(WorldCreator.Mode.CREATIVE);
        }
        lv.dataPackTempDir = dataPackTempDir2;
        return lv;
    }

    private CreateWorldScreen(MinecraftClient client, Runnable onClosed, GeneratorOptionsHolder generatorOptionsHolder, Optional<RegistryKey<WorldPreset>> defaultWorldType, OptionalLong seed, CreateWorldCallback callback) {
        super(Text.translatable("selectWorld.create"));
        this.onClosed = onClosed;
        this.symlinkFinder = client.getSymlinkFinder();
        this.callback = callback;
        this.worldCreator = new WorldCreator(client.getLevelStorage().getSavesDirectory(), generatorOptionsHolder, defaultWorldType, seed);
    }

    public WorldCreator getWorldCreator() {
        return this.worldCreator;
    }

    @Override
    protected void init() {
        this.tabNavigation = TabNavigationWidget.builder(this.tabManager, this.width).tabs(new GameTab(), new WorldTab(), new MoreTab()).build();
        this.addDrawableChild(this.tabNavigation);
        DirectionalLayoutWidget lv = this.layout.addFooter(DirectionalLayoutWidget.horizontal().spacing(8));
        lv.add(ButtonWidget.builder(Text.translatable("selectWorld.create"), button -> this.createLevel()).build());
        lv.add(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.onCloseScreen()).build());
        this.layout.forEachChild(child -> {
            child.setNavigationOrder(1);
            this.addDrawableChild(child);
        });
        this.tabNavigation.selectTab(0, false);
        this.worldCreator.update();
        this.refreshWidgetPositions();
    }

    @Override
    protected void setInitialFocus() {
    }

    @Override
    public void refreshWidgetPositions() {
        if (this.tabNavigation == null) {
            return;
        }
        this.tabNavigation.setWidth(this.width);
        this.tabNavigation.init();
        int i = this.tabNavigation.getNavigationFocus().getBottom();
        ScreenRect lv = new ScreenRect(0, i, this.width, this.height - this.layout.getFooterHeight() - i);
        this.tabManager.setTabArea(lv);
        this.layout.setHeaderHeight(i);
        this.layout.refreshPositions();
    }

    private static void showMessage(MinecraftClient client, Text text) {
        client.setScreenAndRender(new MessageScreen(text));
    }

    private void createLevel() {
        GeneratorOptionsHolder lv = this.worldCreator.getGeneratorOptionsHolder();
        DimensionOptionsRegistryHolder.DimensionsConfig lv2 = lv.selectedDimensions().toConfig(lv.dimensionOptionsRegistry());
        CombinedDynamicRegistries<ServerDynamicRegistryType> lv3 = lv.combinedDynamicRegistries().with(ServerDynamicRegistryType.DIMENSIONS, lv2.toDynamicRegistryManager());
        Lifecycle lifecycle = FeatureFlags.isNotVanilla(lv.dataConfiguration().enabledFeatures()) ? Lifecycle.experimental() : Lifecycle.stable();
        Lifecycle lifecycle2 = lv3.getCombinedRegistryManager().getLifecycle();
        Lifecycle lifecycle3 = lifecycle2.add(lifecycle);
        boolean bl = !this.recreated && lifecycle2 == Lifecycle.stable();
        LevelInfo lv4 = this.createLevelInfo(lv2.specialWorldProperty() == LevelProperties.SpecialProperty.DEBUG);
        LevelProperties lv5 = new LevelProperties(lv4, this.worldCreator.getGeneratorOptionsHolder().generatorOptions(), lv2.specialWorldProperty(), lifecycle3);
        IntegratedServerLoader.tryLoad(this.client, this, lifecycle3, () -> this.createAndClearTempDir(lv3, lv5), bl);
    }

    private void createAndClearTempDir(CombinedDynamicRegistries<ServerDynamicRegistryType> combinedDynamicRegistries, LevelProperties levelProperties) {
        boolean bl = this.callback.create(this, combinedDynamicRegistries, levelProperties, this.dataPackTempDir);
        this.clearDataPackTempDir();
        if (!bl) {
            this.onCloseScreen();
        }
    }

    private boolean startServer(CombinedDynamicRegistries<ServerDynamicRegistryType> combinedDynamicRegistries, SaveProperties saveProperties) {
        String string = this.worldCreator.getWorldDirectoryName();
        GeneratorOptionsHolder lv = this.worldCreator.getGeneratorOptionsHolder();
        CreateWorldScreen.showMessage(this.client, PREPARING_TEXT);
        Optional<LevelStorage.Session> optional = CreateWorldScreen.createSession(this.client, string, this.dataPackTempDir);
        if (optional.isEmpty()) {
            SystemToast.addPackCopyFailure(this.client, string);
            return false;
        }
        this.client.createIntegratedServerLoader().startNewWorld(optional.get(), lv.dataPackContents(), combinedDynamicRegistries, saveProperties);
        return true;
    }

    private LevelInfo createLevelInfo(boolean debugWorld) {
        String string = this.worldCreator.getWorldName().trim();
        if (debugWorld) {
            GameRules lv = new GameRules(DataConfiguration.SAFE_MODE.enabledFeatures());
            lv.setValue(GameRules.ADVANCE_TIME, false, null);
            return new LevelInfo(string, GameMode.SPECTATOR, false, Difficulty.PEACEFUL, true, lv, DataConfiguration.SAFE_MODE);
        }
        return new LevelInfo(string, this.worldCreator.getGameMode().defaultGameMode, this.worldCreator.isHardcore(), this.worldCreator.getDifficulty(), this.worldCreator.areCheatsEnabled(), this.worldCreator.getGameRules(), this.worldCreator.getGeneratorOptionsHolder().dataConfiguration());
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if (this.tabNavigation.keyPressed(input)) {
            return true;
        }
        if (super.keyPressed(input)) {
            return true;
        }
        if (input.isEnter()) {
            this.createLevel();
            return true;
        }
        return false;
    }

    @Override
    public void close() {
        this.onCloseScreen();
    }

    public void onCloseScreen() {
        this.onClosed.run();
        this.clearDataPackTempDir();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, Screen.FOOTER_SEPARATOR_TEXTURE, 0, this.height - this.layout.getFooterHeight() - 2, 0.0f, 0.0f, this.width, 2, 32, 2);
    }

    @Override
    protected void renderDarkening(DrawContext context) {
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TAB_HEADER_BACKGROUND_TEXTURE, 0, 0, 0.0f, 0.0f, this.width, this.layout.getHeaderHeight(), 16, 16);
        this.renderDarkening(context, 0, this.layout.getHeaderHeight(), this.width, this.height);
    }

    private @Nullable Path getOrCreateDataPackTempDir() {
        if (this.dataPackTempDir == null) {
            try {
                this.dataPackTempDir = Files.createTempDirectory(TEMP_DIR_PREFIX, new FileAttribute[0]);
            } catch (IOException iOException) {
                LOGGER.warn("Failed to create temporary dir", iOException);
                SystemToast.addPackCopyFailure(this.client, this.worldCreator.getWorldDirectoryName());
                this.onCloseScreen();
            }
        }
        return this.dataPackTempDir;
    }

    void openExperimentsScreen(DataConfiguration dataConfiguration) {
        Pair<Path, ResourcePackManager> pair = this.getScannedPack(dataConfiguration);
        if (pair != null) {
            this.client.setScreen(new ExperimentsScreen(this, pair.getSecond(), resourcePackManager -> this.applyDataPacks((ResourcePackManager)resourcePackManager, false, this::openExperimentsScreen)));
        }
    }

    void openPackScreen(DataConfiguration dataConfiguration) {
        Pair<Path, ResourcePackManager> pair = this.getScannedPack(dataConfiguration);
        if (pair != null) {
            this.client.setScreen(new PackScreen(pair.getSecond(), resourcePackManager -> this.applyDataPacks((ResourcePackManager)resourcePackManager, true, this::openPackScreen), pair.getFirst(), Text.translatable("dataPack.title")));
        }
    }

    private void applyDataPacks(ResourcePackManager dataPackManager, boolean fromPackScreen, Consumer<DataConfiguration> configurationSetter) {
        List list2;
        ImmutableList<String> list = ImmutableList.copyOf(dataPackManager.getEnabledIds());
        DataConfiguration lv = new DataConfiguration(new DataPackSettings(list, list2 = (List)dataPackManager.getIds().stream().filter(name -> !list.contains(name)).collect(ImmutableList.toImmutableList())), this.worldCreator.getGeneratorOptionsHolder().dataConfiguration().enabledFeatures());
        if (this.worldCreator.updateDataConfiguration(lv)) {
            this.client.setScreen(this);
            return;
        }
        FeatureSet lv2 = dataPackManager.getRequestedFeatures();
        if (FeatureFlags.isNotVanilla(lv2) && fromPackScreen) {
            this.client.setScreen(new ExperimentalWarningScreen(dataPackManager.getEnabledProfiles(), confirmed -> {
                if (confirmed) {
                    this.validateDataPacks(dataPackManager, lv, configurationSetter);
                } else {
                    configurationSetter.accept(this.worldCreator.getGeneratorOptionsHolder().dataConfiguration());
                }
            }));
        } else {
            this.validateDataPacks(dataPackManager, lv, configurationSetter);
        }
    }

    private void validateDataPacks(ResourcePackManager dataPackManager, DataConfiguration dataConfiguration, Consumer<DataConfiguration> configurationSetter) {
        this.client.setScreenAndRender(new MessageScreen(Text.translatable("dataPack.validation.working")));
        SaveLoading.ServerConfig lv = CreateWorldScreen.createServerConfig(dataPackManager, dataConfiguration);
        ((CompletableFuture)((CompletableFuture)SaveLoading.load(lv, context -> {
            if (context.worldGenRegistryManager().getOrThrow(RegistryKeys.WORLD_PRESET).streamEntries().findAny().isEmpty()) {
                throw new IllegalStateException("Needs at least one world preset to continue");
            }
            if (context.worldGenRegistryManager().getOrThrow(RegistryKeys.BIOME).streamEntries().findAny().isEmpty()) {
                throw new IllegalStateException("Needs at least one biome continue");
            }
            GeneratorOptionsHolder lv = this.worldCreator.getGeneratorOptionsHolder();
            RegistryOps<JsonElement> dynamicOps = lv.getCombinedRegistryManager().getOps(JsonOps.INSTANCE);
            DataResult<JsonElement> dataResult = WorldGenSettings.encode(dynamicOps, lv.generatorOptions(), lv.selectedDimensions()).setLifecycle(Lifecycle.stable());
            RegistryOps<JsonElement> dynamicOps2 = context.worldGenRegistryManager().getOps(JsonOps.INSTANCE);
            WorldGenSettings lv2 = (WorldGenSettings)dataResult.flatMap(json -> WorldGenSettings.CODEC.parse(dynamicOps2, json)).getOrThrow(error -> new IllegalStateException("Error parsing worldgen settings after loading data packs: " + error));
            return new SaveLoading.LoadContext<WorldCreationSettings>(new WorldCreationSettings(lv2, context.dataConfiguration()), context.dimensionsRegistryManager());
        }, (resourceManager, dataPackContents, combinedDynamicRegistries, context) -> {
            resourceManager.close();
            return new GeneratorOptionsHolder(context.worldGenSettings(), combinedDynamicRegistries, dataPackContents, context.dataConfiguration());
        }, Util.getMainWorkerExecutor(), this.client).thenApply(generatorOptionsHolder -> {
            generatorOptionsHolder.initializeIndexedFeaturesLists();
            return generatorOptionsHolder;
        })).thenAcceptAsync(this.worldCreator::setGeneratorOptionsHolder, (Executor)this.client)).handleAsync((void_, throwable) -> {
            if (throwable != null) {
                LOGGER.warn("Failed to validate datapack", (Throwable)throwable);
                this.client.setScreen(new ConfirmScreen(confirmed -> {
                    if (confirmed) {
                        configurationSetter.accept(this.worldCreator.getGeneratorOptionsHolder().dataConfiguration());
                    } else {
                        configurationSetter.accept(DataConfiguration.SAFE_MODE);
                    }
                }, Text.translatable("dataPack.validation.failed"), ScreenTexts.EMPTY, Text.translatable("dataPack.validation.back"), Text.translatable("dataPack.validation.reset")));
            } else {
                this.client.setScreen(this);
            }
            return null;
        }, (Executor)this.client);
    }

    private static SaveLoading.ServerConfig createServerConfig(ResourcePackManager dataPackManager, DataConfiguration dataConfiguration) {
        SaveLoading.DataPacks lv = new SaveLoading.DataPacks(dataPackManager, dataConfiguration, false, true);
        return new SaveLoading.ServerConfig(lv, CommandManager.RegistrationEnvironment.INTEGRATED, LeveledPermissionPredicate.GAMEMASTERS);
    }

    private void clearDataPackTempDir() {
        if (this.dataPackTempDir != null && Files.exists(this.dataPackTempDir, new LinkOption[0])) {
            try (Stream<Path> stream = Files.walk(this.dataPackTempDir, new FileVisitOption[0]);){
                stream.sorted(Comparator.reverseOrder()).forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException iOException) {
                        LOGGER.warn("Failed to remove temporary file {}", path, (Object)iOException);
                    }
                });
            } catch (IOException iOException) {
                LOGGER.warn("Failed to list temporary dir {}", (Object)this.dataPackTempDir);
            }
        }
        this.dataPackTempDir = null;
    }

    private static void copyDataPack(Path srcFolder, Path destFolder, Path dataPackFile) {
        try {
            Util.relativeCopy(srcFolder, destFolder, dataPackFile);
        } catch (IOException iOException) {
            LOGGER.warn("Failed to copy datapack file from {} to {}", (Object)dataPackFile, (Object)destFolder);
            throw new UncheckedIOException(iOException);
        }
    }

    /*
     * WARNING - bad return control flow
     */
    private static Optional<LevelStorage.Session> createSession(MinecraftClient client, String worldDirectoryName, @Nullable Path dataPackTempDir) {
        Optional<LevelStorage.Session> optional;
        block12: {
            LevelStorage.Session lv;
            block11: {
                lv = client.getLevelStorage().createSessionWithoutSymlinkCheck(worldDirectoryName);
                if (dataPackTempDir != null) break block11;
                return Optional.of(lv);
            }
            Stream<Path> stream = Files.walk(dataPackTempDir, new FileVisitOption[0]);
            try {
                Path path22 = lv.getDirectory(WorldSavePath.DATAPACKS);
                PathUtil.createDirectories(path22);
                stream.filter(path2 -> !path2.equals(dataPackTempDir)).forEach(path3 -> CreateWorldScreen.copyDataPack(dataPackTempDir, path22, path3));
                optional = Optional.of(lv);
                if (stream == null) break block12;
            } catch (Throwable throwable) {
                try {
                    try {
                        if (stream != null) {
                            try {
                                stream.close();
                            } catch (Throwable throwable2) {
                                throwable.addSuppressed(throwable2);
                            }
                        }
                        throw throwable;
                    } catch (IOException | UncheckedIOException exception) {
                        LOGGER.warn("Failed to copy datapacks to world {}", (Object)worldDirectoryName, (Object)exception);
                        lv.close();
                    }
                } catch (IOException | UncheckedIOException exception2) {
                    LOGGER.warn("Failed to create access for {}", (Object)worldDirectoryName, (Object)exception2);
                }
            }
            stream.close();
        }
        return optional;
        return Optional.empty();
    }

    public static @Nullable Path copyDataPack(Path srcFolder, MinecraftClient client) {
        MutableObject<@Nullable T> mutableObject = new MutableObject();
        try (Stream<Path> stream = Files.walk(srcFolder, new FileVisitOption[0]);){
            stream.filter(dataPackFile -> !dataPackFile.equals(srcFolder)).forEach(dataPackFile -> {
                Path path3 = (Path)mutableObject.get();
                if (path3 == null) {
                    try {
                        path3 = Files.createTempDirectory(TEMP_DIR_PREFIX, new FileAttribute[0]);
                    } catch (IOException iOException) {
                        LOGGER.warn("Failed to create temporary dir");
                        throw new UncheckedIOException(iOException);
                    }
                    mutableObject.setValue(path3);
                }
                CreateWorldScreen.copyDataPack(srcFolder, path3, dataPackFile);
            });
        } catch (IOException | UncheckedIOException exception) {
            LOGGER.warn("Failed to copy datapacks from world {}", (Object)srcFolder, (Object)exception);
            SystemToast.addPackCopyFailure(client, srcFolder.toString());
            return null;
        }
        return (Path)mutableObject.get();
    }

    private @Nullable Pair<Path, ResourcePackManager> getScannedPack(DataConfiguration dataConfiguration) {
        Path path = this.getOrCreateDataPackTempDir();
        if (path != null) {
            if (this.packManager == null) {
                this.packManager = VanillaDataPackProvider.createManager(path, this.symlinkFinder);
                this.packManager.scanPacks();
            }
            this.packManager.setEnabledProfiles(dataConfiguration.dataPacks().getEnabled());
            return Pair.of(path, this.packManager);
        }
        return null;
    }

    @Environment(value=EnvType.CLIENT)
    class GameTab
    extends GridScreenTab {
        private static final Text GAME_TAB_TITLE_TEXT = Text.translatable("createWorld.tab.game.title");
        private static final Text ALLOW_COMMANDS_TEXT = Text.translatable("selectWorld.allowCommands");
        private final TextFieldWidget worldNameField;

        GameTab() {
            super(GAME_TAB_TITLE_TEXT);
            GridWidget.Adder lv = this.grid.setRowSpacing(8).createAdder(1);
            Positioner lv2 = lv.copyPositioner();
            this.worldNameField = new TextFieldWidget(CreateWorldScreen.this.textRenderer, 208, 20, Text.translatable("selectWorld.enterName"));
            this.worldNameField.setText(CreateWorldScreen.this.worldCreator.getWorldName());
            this.worldNameField.setChangedListener(CreateWorldScreen.this.worldCreator::setWorldName);
            CreateWorldScreen.this.worldCreator.addListener(creator -> this.worldNameField.setTooltip(Tooltip.of(Text.translatable("selectWorld.targetFolder", Text.literal(creator.getWorldDirectoryName()).formatted(Formatting.ITALIC)))));
            CreateWorldScreen.this.setInitialFocus(this.worldNameField);
            lv.add(LayoutWidgets.createLabeledWidget(CreateWorldScreen.this.textRenderer, this.worldNameField, ENTER_NAME_TEXT), lv.copyPositioner().alignHorizontalCenter());
            CyclingButtonWidget<WorldCreator.Mode> lv3 = lv.add(CyclingButtonWidget.builder(value -> value.name, CreateWorldScreen.this.worldCreator.getGameMode()).values((WorldCreator.Mode[])new WorldCreator.Mode[]{WorldCreator.Mode.SURVIVAL, WorldCreator.Mode.HARDCORE, WorldCreator.Mode.CREATIVE}).build(0, 0, 210, 20, GAME_MODE_TEXT, (button, value) -> CreateWorldScreen.this.worldCreator.setGameMode((WorldCreator.Mode)((Object)value))), lv2);
            CreateWorldScreen.this.worldCreator.addListener(creator -> {
                lv3.setValue(creator.getGameMode());
                arg.active = !creator.isDebug();
                lv3.setTooltip(Tooltip.of(creator.getGameMode().getInfo()));
            });
            CyclingButtonWidget<Difficulty> lv4 = lv.add(CyclingButtonWidget.builder(Difficulty::getTranslatableName, CreateWorldScreen.this.worldCreator.getDifficulty()).values((Difficulty[])Difficulty.values()).build(0, 0, 210, 20, Text.translatable("options.difficulty"), (button, value) -> CreateWorldScreen.this.worldCreator.setDifficulty((Difficulty)value)), lv2);
            CreateWorldScreen.this.worldCreator.addListener(creator -> {
                lv4.setValue(CreateWorldScreen.this.worldCreator.getDifficulty());
                CreateWorldScreen.this.active = !CreateWorldScreen.this.worldCreator.isHardcore();
                lv4.setTooltip(Tooltip.of(CreateWorldScreen.this.worldCreator.getDifficulty().getInfo()));
            });
            CyclingButtonWidget<Boolean> lv5 = lv.add(CyclingButtonWidget.onOffBuilder(CreateWorldScreen.this.worldCreator.areCheatsEnabled()).tooltip(value -> Tooltip.of(ALLOW_COMMANDS_INFO_TEXT)).build(0, 0, 210, 20, ALLOW_COMMANDS_TEXT, (button, value) -> CreateWorldScreen.this.worldCreator.setCheatsEnabled((boolean)value)));
            CreateWorldScreen.this.worldCreator.addListener(creator -> {
                lv5.setValue(CreateWorldScreen.this.worldCreator.areCheatsEnabled());
                CreateWorldScreen.this.active = !CreateWorldScreen.this.worldCreator.isDebug() && !CreateWorldScreen.this.worldCreator.isHardcore();
            });
            if (!SharedConstants.getGameVersion().stable()) {
                lv.add(ButtonWidget.builder(EXPERIMENTS_TEXT, button -> CreateWorldScreen.this.openExperimentsScreen(CreateWorldScreen.this.worldCreator.getGeneratorOptionsHolder().dataConfiguration())).width(210).build());
            }
        }
    }

    @Environment(value=EnvType.CLIENT)
    class WorldTab
    extends GridScreenTab {
        private static final Text WORLD_TAB_TITLE_TEXT = Text.translatable("createWorld.tab.world.title");
        private static final Text AMPLIFIED_GENERATOR_INFO_TEXT = Text.translatable("generator.minecraft.amplified.info");
        private static final Text MAP_FEATURES_TEXT = Text.translatable("selectWorld.mapFeatures");
        private static final Text MAP_FEATURES_INFO_TEXT = Text.translatable("selectWorld.mapFeatures.info");
        private static final Text BONUS_ITEMS_TEXT = Text.translatable("selectWorld.bonusItems");
        private static final Text ENTER_SEED_TEXT = Text.translatable("selectWorld.enterSeed");
        static final Text SEED_INFO_TEXT = Text.translatable("selectWorld.seedInfo");
        private static final int field_42190 = 310;
        private final TextFieldWidget seedField;
        private final ButtonWidget customizeButton;

        WorldTab() {
            super(WORLD_TAB_TITLE_TEXT);
            GridWidget.Adder lv = this.grid.setColumnSpacing(10).setRowSpacing(8).createAdder(2);
            CyclingButtonWidget<WorldCreator.WorldType> lv2 = lv.add(CyclingButtonWidget.builder(WorldCreator.WorldType::getName, CreateWorldScreen.this.worldCreator.getWorldType()).values(this.getWorldTypes()).narration(WorldTab::getWorldTypeNarrationMessage).build(0, 0, 150, 20, Text.translatable("selectWorld.mapType"), (button, worldType) -> CreateWorldScreen.this.worldCreator.setWorldType((WorldCreator.WorldType)worldType)));
            lv2.setValue(CreateWorldScreen.this.worldCreator.getWorldType());
            CreateWorldScreen.this.worldCreator.addListener(creator -> {
                WorldCreator.WorldType lv = creator.getWorldType();
                lv2.setValue(lv);
                if (lv.isAmplified()) {
                    lv2.setTooltip(Tooltip.of(AMPLIFIED_GENERATOR_INFO_TEXT));
                } else {
                    lv2.setTooltip(null);
                }
                CreateWorldScreen.this.active = CreateWorldScreen.this.worldCreator.getWorldType().preset() != null;
            });
            this.customizeButton = lv.add(ButtonWidget.builder(Text.translatable("selectWorld.customizeType"), button -> this.openCustomizeScreen()).build());
            CreateWorldScreen.this.worldCreator.addListener(creator -> {
                this.customizeButton.active = !creator.isDebug() && creator.getLevelScreenProvider() != null;
            });
            this.seedField = new TextFieldWidget(this, CreateWorldScreen.this.textRenderer, 308, 20, Text.translatable("selectWorld.enterSeed")){

                @Override
                protected MutableText getNarrationMessage() {
                    return super.getNarrationMessage().append(ScreenTexts.SENTENCE_SEPARATOR).append(SEED_INFO_TEXT);
                }
            };
            this.seedField.setPlaceholder(SEED_INFO_TEXT);
            this.seedField.setText(CreateWorldScreen.this.worldCreator.getSeed());
            this.seedField.setChangedListener(seed -> CreateWorldScreen.this.worldCreator.setSeed(this.seedField.getText()));
            lv.add(LayoutWidgets.createLabeledWidget(CreateWorldScreen.this.textRenderer, this.seedField, ENTER_SEED_TEXT), 2);
            WorldScreenOptionGrid.Builder lv3 = WorldScreenOptionGrid.builder(310);
            lv3.add(MAP_FEATURES_TEXT, CreateWorldScreen.this.worldCreator::shouldGenerateStructures, CreateWorldScreen.this.worldCreator::setGenerateStructures).toggleable(() -> !CreateWorldScreen.this.worldCreator.isDebug()).tooltip(MAP_FEATURES_INFO_TEXT);
            lv3.add(BONUS_ITEMS_TEXT, CreateWorldScreen.this.worldCreator::isBonusChestEnabled, CreateWorldScreen.this.worldCreator::setBonusChestEnabled).toggleable(() -> !CreateWorldScreen.this.worldCreator.isHardcore() && !CreateWorldScreen.this.worldCreator.isDebug());
            WorldScreenOptionGrid lv4 = lv3.build();
            lv.add(lv4.getLayout(), 2);
            CreateWorldScreen.this.worldCreator.addListener(creator -> lv4.refresh());
        }

        private void openCustomizeScreen() {
            LevelScreenProvider lv = CreateWorldScreen.this.worldCreator.getLevelScreenProvider();
            if (lv != null) {
                CreateWorldScreen.this.client.setScreen(lv.createEditScreen(CreateWorldScreen.this, CreateWorldScreen.this.worldCreator.getGeneratorOptionsHolder()));
            }
        }

        private CyclingButtonWidget.Values<WorldCreator.WorldType> getWorldTypes() {
            return new CyclingButtonWidget.Values<WorldCreator.WorldType>(){

                @Override
                public List<WorldCreator.WorldType> getCurrent() {
                    return CyclingButtonWidget.HAS_ALT_DOWN.getAsBoolean() ? CreateWorldScreen.this.worldCreator.getExtendedWorldTypes() : CreateWorldScreen.this.worldCreator.getNormalWorldTypes();
                }

                @Override
                public List<WorldCreator.WorldType> getDefaults() {
                    return CreateWorldScreen.this.worldCreator.getNormalWorldTypes();
                }
            };
        }

        private static MutableText getWorldTypeNarrationMessage(CyclingButtonWidget<WorldCreator.WorldType> worldTypeButton) {
            if (worldTypeButton.getValue().isAmplified()) {
                return ScreenTexts.joinSentences(worldTypeButton.getGenericNarrationMessage(), AMPLIFIED_GENERATOR_INFO_TEXT);
            }
            return worldTypeButton.getGenericNarrationMessage();
        }
    }

    @Environment(value=EnvType.CLIENT)
    class MoreTab
    extends GridScreenTab {
        private static final Text MORE_TAB_TITLE_TEXT = Text.translatable("createWorld.tab.more.title");
        private static final Text GAME_RULES_TEXT = Text.translatable("selectWorld.gameRules");
        private static final Text DATA_PACKS_TEXT = Text.translatable("selectWorld.dataPacks");

        MoreTab() {
            super(MORE_TAB_TITLE_TEXT);
            GridWidget.Adder lv = this.grid.setRowSpacing(8).createAdder(1);
            lv.add(ButtonWidget.builder(GAME_RULES_TEXT, button -> this.openGameRulesScreen()).width(210).build());
            lv.add(ButtonWidget.builder(EXPERIMENTS_TEXT, button -> CreateWorldScreen.this.openExperimentsScreen(CreateWorldScreen.this.worldCreator.getGeneratorOptionsHolder().dataConfiguration())).width(210).build());
            lv.add(ButtonWidget.builder(DATA_PACKS_TEXT, button -> CreateWorldScreen.this.openPackScreen(CreateWorldScreen.this.worldCreator.getGeneratorOptionsHolder().dataConfiguration())).width(210).build());
        }

        private void openGameRulesScreen() {
            CreateWorldScreen.this.client.setScreen(new EditGameRulesScreen(CreateWorldScreen.this.worldCreator.getGameRules().withEnabledFeatures(CreateWorldScreen.this.worldCreator.getGeneratorOptionsHolder().dataConfiguration().enabledFeatures()), gameRules -> {
                CreateWorldScreen.this.client.setScreen(CreateWorldScreen.this);
                gameRules.ifPresent(CreateWorldScreen.this.worldCreator::setGameRules);
            }));
        }
    }
}

