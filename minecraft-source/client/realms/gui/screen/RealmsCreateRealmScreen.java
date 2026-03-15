/*
 * External method calls:
 *   Lnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;addHeader(Lnet/minecraft/text/Text;Lnet/minecraft/client/font/TextRenderer;)V
 *   Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;vertical()Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;
 *   Lnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;addBody(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;
 *   Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;spacing(I)Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget;builder(Lnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;build()Lnet/minecraft/client/gui/widget/ButtonWidget;
 *   Lnet/minecraft/client/gui/widget/LayoutWidgets;createLabeledWidget(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/client/gui/widget/Widget;Lnet/minecraft/text/Text;)Lnet/minecraft/client/gui/widget/LayoutWidget;
 *   Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;horizontal()Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;
 *   Lnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;addFooter(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;
 *   Lnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;forEachChild(Ljava/util/function/Consumer;)V
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Text;empty()Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/realms/RealmsClient;create()Lnet/minecraft/client/realms/RealmsClient;
 *   Lnet/minecraft/client/realms/RealmsClient;createPrereleaseServer(Ljava/lang/Long;)Lnet/minecraft/client/realms/dto/RealmsServer;
 *   Lnet/minecraft/client/realms/gui/screen/RealmsCreateWorldScreen;newRealm(Lnet/minecraft/client/gui/screen/Screen;Lnet/minecraft/client/realms/dto/RealmsServer;Lnet/minecraft/client/realms/task/WorldCreationTask;Ljava/lang/Runnable;)Lnet/minecraft/client/realms/gui/screen/RealmsCreateWorldScreen;
 *   Lnet/minecraft/client/MinecraftClient;execute(Ljava/lang/Runnable;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/realms/gui/screen/RealmsCreateRealmScreen;createWorld(Lnet/minecraft/client/realms/dto/RealmsServer;)V
 *   Lnet/minecraft/client/realms/gui/screen/RealmsCreateRealmScreen;createPrereleaseServer(Lnet/minecraft/client/realms/dto/RealmsServer;)Lnet/minecraft/client/realms/dto/RealmsServer;
 *   Lnet/minecraft/client/realms/gui/screen/RealmsCreateRealmScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;
 *   Lnet/minecraft/client/realms/gui/screen/RealmsCreateRealmScreen;createWorld(Lnet/minecraft/client/realms/dto/RealmsServer;Z)V
 */
package net.minecraft.client.realms.gui.screen;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.NoticeScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.LayoutWidgets;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
import net.minecraft.client.realms.RealmsClient;
import net.minecraft.client.realms.dto.RealmsServer;
import net.minecraft.client.realms.exception.RealmsServiceException;
import net.minecraft.client.realms.gui.screen.RealmsCreateWorldScreen;
import net.minecraft.client.realms.gui.screen.RealmsGenericErrorScreen;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;
import net.minecraft.client.realms.gui.screen.RealmsScreen;
import net.minecraft.client.realms.task.WorldCreationTask;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.StringHelper;
import net.minecraft.util.Util;

@Environment(value=EnvType.CLIENT)
public class RealmsCreateRealmScreen
extends RealmsScreen {
    private static final Text TITLE_TEXT = Text.translatable("mco.selectServer.create");
    private static final Text WORLD_NAME_TEXT = Text.translatable("mco.configure.world.name");
    private static final Text WORLD_DESCRIPTION_TEXT = Text.translatable("mco.configure.world.description");
    private static final int field_45243 = 10;
    private static final int field_45244 = 210;
    private final RealmsMainScreen parent;
    private final ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);
    private TextFieldWidget nameBox;
    private TextFieldWidget descriptionBox;
    private final Runnable worldCreator;

    public RealmsCreateRealmScreen(RealmsMainScreen parent, RealmsServer server, boolean prerelease) {
        super(TITLE_TEXT);
        this.parent = parent;
        this.worldCreator = () -> this.createWorld(server, prerelease);
    }

    @Override
    public void init() {
        this.layout.addHeader(this.title, this.textRenderer);
        DirectionalLayoutWidget lv = this.layout.addBody(DirectionalLayoutWidget.vertical()).spacing(10);
        ButtonWidget lv2 = ButtonWidget.builder(ScreenTexts.CONTINUE, button -> this.worldCreator.run()).build();
        lv2.active = false;
        this.nameBox = new TextFieldWidget(this.textRenderer, 210, 20, WORLD_NAME_TEXT);
        this.nameBox.setChangedListener(name -> {
            arg.active = !StringHelper.isBlank(name);
        });
        this.descriptionBox = new TextFieldWidget(this.textRenderer, 210, 20, WORLD_DESCRIPTION_TEXT);
        lv.add(LayoutWidgets.createLabeledWidget(this.textRenderer, this.nameBox, WORLD_NAME_TEXT));
        lv.add(LayoutWidgets.createLabeledWidget(this.textRenderer, this.descriptionBox, WORLD_DESCRIPTION_TEXT));
        DirectionalLayoutWidget lv3 = this.layout.addFooter(DirectionalLayoutWidget.horizontal().spacing(10));
        lv3.add(lv2);
        lv3.add(ButtonWidget.builder(ScreenTexts.BACK, button -> this.close()).build());
        this.layout.forEachChild(child -> {
            ClickableWidget cfr_ignored_0 = (ClickableWidget)this.addDrawableChild(child);
        });
        this.refreshWidgetPositions();
    }

    @Override
    protected void setInitialFocus() {
        this.setInitialFocus(this.nameBox);
    }

    @Override
    protected void refreshWidgetPositions() {
        this.layout.refreshPositions();
    }

    private void createWorld(RealmsServer realmsServer, boolean prerelease) {
        if (!realmsServer.isPrerelease() && prerelease) {
            AtomicBoolean atomicBoolean = new AtomicBoolean();
            this.client.setScreen(new NoticeScreen(() -> {
                atomicBoolean.set(true);
                this.parent.removeSelection();
                this.client.setScreen(this.parent);
            }, Text.translatable("mco.upload.preparing"), (Text)Text.empty()));
            ((CompletableFuture)CompletableFuture.supplyAsync(() -> RealmsCreateRealmScreen.createPrereleaseServer(realmsServer), Util.getMainWorkerExecutor()).thenAcceptAsync(prereleaseServer -> {
                if (!atomicBoolean.get()) {
                    this.createWorld((RealmsServer)prereleaseServer);
                }
            }, (Executor)this.client)).exceptionallyAsync(throwable -> {
                Text lv2;
                this.parent.removeSelection();
                Throwable throwable2 = throwable.getCause();
                if (throwable2 instanceof RealmsServiceException) {
                    RealmsServiceException lv = (RealmsServiceException)throwable2;
                    lv2 = lv.error.getText();
                } else {
                    lv2 = Text.translatable("mco.errorMessage.initialize.failed");
                }
                this.client.setScreen(new RealmsGenericErrorScreen(lv2, (Screen)this.parent));
                return null;
            }, (Executor)this.client);
        } else {
            this.createWorld(realmsServer);
        }
    }

    private static RealmsServer createPrereleaseServer(RealmsServer parent) {
        RealmsClient lv = RealmsClient.create();
        try {
            return lv.createPrereleaseServer(parent.id);
        } catch (RealmsServiceException lv2) {
            throw new RuntimeException(lv2);
        }
    }

    private void createWorld(RealmsServer server) {
        WorldCreationTask lv = new WorldCreationTask(server.id, this.nameBox.getText(), this.descriptionBox.getText());
        RealmsCreateWorldScreen lv2 = RealmsCreateWorldScreen.newRealm(this, server, lv, () -> this.client.execute(() -> {
            RealmsMainScreen.resetServerList();
            this.client.setScreen(this.parent);
        }));
        this.client.setScreen(lv2);
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }
}

