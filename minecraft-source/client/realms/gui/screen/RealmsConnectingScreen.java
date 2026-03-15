/*
 * External method calls:
 *   Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;vertical()Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;
 *   Lnet/minecraft/client/realms/dto/RealmsServerAddress;regionData()Lnet/minecraft/client/realms/dto/RealmsServerAddress$RegionData;
 *   Lnet/minecraft/client/realms/dto/RealmsServerAddress$RegionData;region()Lnet/minecraft/client/realms/dto/RealmsRegion;
 *   Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;horizontal()Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;
 *   Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;spacing(I)Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/realms/dto/RealmsServerAddress$RegionData;serviceQuality()Lnet/minecraft/client/realms/ServiceQuality;
 *   Lnet/minecraft/client/gui/widget/IconWidget;create(IILnet/minecraft/util/Identifier;)Lnet/minecraft/client/gui/widget/IconWidget;
 *   Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;forEachChild(Ljava/util/function/Consumer;)V
 *   Lnet/minecraft/client/gui/widget/Positioner;marginTop(I)Lnet/minecraft/client/gui/widget/Positioner;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/realms/gui/screen/RealmsConnectingScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;
 */
package net.minecraft.client.realms.gui.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.IconWidget;
import net.minecraft.client.gui.widget.Positioner;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.realms.ServiceQuality;
import net.minecraft.client.realms.dto.RealmsServerAddress;
import net.minecraft.client.realms.gui.screen.RealmsLongRunningMcoTaskScreen;
import net.minecraft.client.realms.task.LongRunningTask;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class RealmsConnectingScreen
extends RealmsLongRunningMcoTaskScreen {
    private final LongRunningTask connectTask;
    private final RealmsServerAddress serverAddress;
    private final DirectionalLayoutWidget footerLayout = DirectionalLayoutWidget.vertical();

    public RealmsConnectingScreen(Screen parent, RealmsServerAddress serverAddress, LongRunningTask connectTask) {
        super(parent, connectTask);
        this.connectTask = connectTask;
        this.serverAddress = serverAddress;
    }

    @Override
    public void init() {
        super.init();
        if (this.serverAddress.regionData() == null || this.serverAddress.regionData().region() == null) {
            return;
        }
        DirectionalLayoutWidget lv = DirectionalLayoutWidget.horizontal().spacing(10);
        TextWidget lv2 = new TextWidget(Text.translatable("mco.connect.region", Text.translatable(this.serverAddress.regionData().region().translationKey)), this.textRenderer);
        lv.add(lv2);
        Identifier lv3 = this.serverAddress.regionData().serviceQuality() != null ? this.serverAddress.regionData().serviceQuality().getIcon() : ServiceQuality.UNKNOWN.getIcon();
        lv.add(IconWidget.create(10, 8, lv3), Positioner::alignTop);
        this.footerLayout.add(lv, positioner -> positioner.marginTop(40));
        this.footerLayout.forEachChild(child -> {
            ClickableWidget cfr_ignored_0 = (ClickableWidget)this.addDrawableChild(child);
        });
        this.refreshWidgetPositions();
    }

    @Override
    protected void refreshWidgetPositions() {
        super.refreshWidgetPositions();
        int i = this.layout.getY() + this.layout.getHeight();
        ScreenRect lv = new ScreenRect(0, i, this.width, this.height - i);
        this.footerLayout.refreshPositions();
        SimplePositioningWidget.setPos(this.footerLayout, lv, 0.5f, 0.0f);
    }

    @Override
    public void tick() {
        super.tick();
        this.connectTask.tick();
    }

    @Override
    protected void onCancel() {
        this.connectTask.abortTask();
        super.onCancel();
    }
}

