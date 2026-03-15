/*
 * External method calls:
 *   Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;vertical()Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;
 *   Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;spacing(I)Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;
 *   Lnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;addHeader(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;
 *   Lnet/minecraft/client/gui/widget/Positioner;alignHorizontalCenter()Lnet/minecraft/client/gui/widget/Positioner;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/gui/screen/world/WorldListWidget$Builder;width(I)Lnet/minecraft/client/gui/screen/world/WorldListWidget$Builder;
 *   Lnet/minecraft/client/gui/screen/world/WorldListWidget$Builder;height(I)Lnet/minecraft/client/gui/screen/world/WorldListWidget$Builder;
 *   Lnet/minecraft/client/gui/screen/world/WorldListWidget$Builder;search(Ljava/lang/String;)Lnet/minecraft/client/gui/screen/world/WorldListWidget$Builder;
 *   Lnet/minecraft/client/gui/screen/world/WorldListWidget$Builder;predecessor(Lnet/minecraft/client/gui/screen/world/WorldListWidget;)Lnet/minecraft/client/gui/screen/world/WorldListWidget$Builder;
 *   Lnet/minecraft/client/gui/screen/world/WorldListWidget$Builder;uploadWorld()Lnet/minecraft/client/gui/screen/world/WorldListWidget$Builder;
 *   Lnet/minecraft/client/gui/screen/world/WorldListWidget$Builder;selectionCallback(Ljava/util/function/Consumer;)Lnet/minecraft/client/gui/screen/world/WorldListWidget$Builder;
 *   Lnet/minecraft/client/gui/screen/world/WorldListWidget$Builder;confirmationCallback(Ljava/util/function/Consumer;)Lnet/minecraft/client/gui/screen/world/WorldListWidget$Builder;
 *   Lnet/minecraft/client/gui/screen/world/WorldListWidget$Builder;toWidget()Lnet/minecraft/client/gui/screen/world/WorldListWidget;
 *   Lnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;addBody(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;
 *   Lnet/minecraft/text/Text;of(Ljava/lang/String;)Lnet/minecraft/text/Text;
 *   Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;horizontal()Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;
 *   Lnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;addFooter(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget;builder(Lnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;build()Lnet/minecraft/client/gui/widget/ButtonWidget;
 *   Lnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;forEachChild(Ljava/util/function/Consumer;)V
 *   Lnet/minecraft/client/gui/screen/world/WorldListWidget;position(ILnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;)V
 *   Lnet/minecraft/screen/ScreenTexts;joinSentences([Lnet/minecraft/text/Text;)Lnet/minecraft/text/MutableText;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/realms/gui/screen/RealmsSelectFileToUploadScreen;worldSelected(Lnet/minecraft/world/level/storage/LevelSummary;)V
 *   Lnet/minecraft/client/realms/gui/screen/RealmsSelectFileToUploadScreen;narrateLabels()Lnet/minecraft/text/Text;
 *   Lnet/minecraft/client/realms/gui/screen/RealmsSelectFileToUploadScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;
 */
package net.minecraft.client.realms.gui.screen;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
import net.minecraft.client.realms.gui.screen.RealmsCreateWorldScreen;
import net.minecraft.client.realms.gui.screen.RealmsGenericErrorScreen;
import net.minecraft.client.realms.gui.screen.RealmsScreen;
import net.minecraft.client.realms.gui.screen.RealmsUploadScreen;
import net.minecraft.client.realms.task.WorldCreationTask;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.world.level.storage.LevelSummary;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(value=EnvType.CLIENT)
public class RealmsSelectFileToUploadScreen
extends RealmsScreen {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final Text TITLE = Text.translatable("mco.upload.select.world.title");
    private static final Text LOADING_ERROR_TEXT = Text.translatable("selectWorld.unable_to_load");
    private final @Nullable WorldCreationTask creationTask;
    private final RealmsCreateWorldScreen parent;
    private final long worldId;
    private final int slotId;
    private final ThreePartsLayoutWidget field_62099;
    protected @Nullable TextFieldWidget field_62100;
    private @Nullable WorldListWidget worldSelectionList;
    private @Nullable ButtonWidget uploadButton;

    public RealmsSelectFileToUploadScreen(@Nullable WorldCreationTask creationTask, long worldId, int slotId, RealmsCreateWorldScreen parent) {
        super(TITLE);
        this.field_62099 = new ThreePartsLayoutWidget(this, 8 + MinecraftClient.getInstance().textRenderer.fontHeight + 8 + 20 + 4, 33);
        this.creationTask = creationTask;
        this.parent = parent;
        this.worldId = worldId;
        this.slotId = slotId;
    }

    @Override
    public void init() {
        DirectionalLayoutWidget lv = this.field_62099.addHeader(DirectionalLayoutWidget.vertical().spacing(4));
        lv.getMainPositioner().alignHorizontalCenter();
        lv.add(new TextWidget(this.title, this.textRenderer));
        this.field_62100 = lv.add(new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 22, 200, 20, this.field_62100, Text.translatable("selectWorld.search")));
        this.field_62100.setChangedListener(string -> {
            if (this.worldSelectionList != null) {
                this.worldSelectionList.setSearch((String)string);
            }
        });
        try {
            this.worldSelectionList = this.field_62099.addBody(new WorldListWidget.Builder(this.client, this).width(this.width).height(this.field_62099.getContentHeight()).search(this.field_62100.getText()).predecessor(this.worldSelectionList).uploadWorld().selectionCallback(this::worldSelected).confirmationCallback(this::upload).toWidget());
        } catch (Exception exception) {
            LOGGER.error("Couldn't load level list", exception);
            this.client.setScreen(new RealmsGenericErrorScreen(LOADING_ERROR_TEXT, Text.of(exception.getMessage()), this.parent));
            return;
        }
        DirectionalLayoutWidget lv2 = this.field_62099.addFooter(DirectionalLayoutWidget.horizontal().spacing(8));
        lv2.getMainPositioner().alignHorizontalCenter();
        this.uploadButton = lv2.add(ButtonWidget.builder(Text.translatable("mco.upload.button.name"), arg -> this.worldSelectionList.getSelectedAsOptional().ifPresent(this::upload)).build());
        lv2.add(ButtonWidget.builder(ScreenTexts.BACK, arg -> this.close()).build());
        this.worldSelected(null);
        this.field_62099.forEachChild(arg2 -> {
            ClickableWidget cfr_ignored_0 = (ClickableWidget)this.addDrawableChild(arg2);
        });
        this.refreshWidgetPositions();
    }

    @Override
    protected void refreshWidgetPositions() {
        if (this.worldSelectionList != null) {
            this.worldSelectionList.position(this.width, this.field_62099);
        }
        this.field_62099.refreshPositions();
    }

    @Override
    protected void setInitialFocus() {
        this.setInitialFocus(this.field_62100);
    }

    private void worldSelected(@Nullable LevelSummary level) {
        if (this.worldSelectionList != null && this.uploadButton != null) {
            this.uploadButton.active = this.worldSelectionList.getSelectedOrNull() != null;
        }
    }

    private void upload(WorldListWidget.WorldEntry arg) {
        this.client.setScreen(new RealmsUploadScreen(this.creationTask, this.worldId, this.slotId, this.parent, arg.getLevel()));
    }

    @Override
    public Text getNarratedTitle() {
        return ScreenTexts.joinSentences(this.getTitle(), this.narrateLabels());
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }
}

