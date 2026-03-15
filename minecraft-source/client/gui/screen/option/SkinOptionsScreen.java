/*
 * External method calls:
 *   Lnet/minecraft/entity/player/PlayerModelPart;values()[Lnet/minecraft/entity/player/PlayerModelPart;
 *   Lnet/minecraft/client/gui/widget/CyclingButtonWidget;onOffBuilder(Z)Lnet/minecraft/client/gui/widget/CyclingButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/CyclingButtonWidget$Builder;build(Lnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/CyclingButtonWidget$UpdateCallback;)Lnet/minecraft/client/gui/widget/CyclingButtonWidget;
 *   Lnet/minecraft/client/option/SimpleOption;createWidget(Lnet/minecraft/client/option/GameOptions;)Lnet/minecraft/client/gui/widget/ClickableWidget;
 *   Lnet/minecraft/client/gui/widget/OptionListWidget;addAll(Ljava/util/List;)V
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 */
package net.minecraft.client.gui.screen.option;

import java.util.ArrayList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.text.Text;

@Environment(value=EnvType.CLIENT)
public class SkinOptionsScreen
extends GameOptionsScreen {
    private static final Text TITLE_TEXT = Text.translatable("options.skinCustomisation.title");

    public SkinOptionsScreen(Screen parent, GameOptions gameOptions) {
        super(parent, gameOptions, TITLE_TEXT);
    }

    @Override
    protected void addOptions() {
        ArrayList<ClickableWidget> list = new ArrayList<ClickableWidget>();
        for (PlayerModelPart lv : PlayerModelPart.values()) {
            list.add(CyclingButtonWidget.onOffBuilder(this.gameOptions.isPlayerModelPartEnabled(lv)).build(lv.getOptionName(), (button, enabled) -> this.gameOptions.setPlayerModelPart(lv, (boolean)enabled)));
        }
        list.add(this.gameOptions.getMainArm().createWidget(this.gameOptions));
        this.body.addAll(list);
    }
}

