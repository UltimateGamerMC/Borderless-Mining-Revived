/*
 * External method calls:
 *   Lnet/minecraft/util/Nullables;map(Ljava/lang/Object;Ljava/util/function/Function;)Ljava/lang/Object;
 *   Lnet/minecraft/client/gui/widget/OptionListWidget;addAll([Lnet/minecraft/client/option/SimpleOption;)V
 *   Lnet/minecraft/client/option/SimpleOption;emptyTooltip()Lnet/minecraft/client/option/SimpleOption$TooltipFactory;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/screen/option/OnlineOptionsScreen;collectOptions(Lnet/minecraft/client/option/GameOptions;Lnet/minecraft/client/MinecraftClient;)[Lnet/minecraft/client/option/SimpleOption;
 */
package net.minecraft.client.gui.screen.option;

import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.Codec;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.minecraft.util.Nullables;
import net.minecraft.world.Difficulty;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class OnlineOptionsScreen
extends GameOptionsScreen {
    private static final Text TITLE_TEXT = Text.translatable("options.online.title");
    private @Nullable SimpleOption<Unit> difficulty;

    public OnlineOptionsScreen(Screen parent, GameOptions gameOptions) {
        super(parent, gameOptions, TITLE_TEXT);
    }

    @Override
    protected void init() {
        ClickableWidget lv;
        super.init();
        if (this.difficulty != null && (lv = this.body.getWidgetFor(this.difficulty)) != null) {
            lv.active = false;
        }
    }

    private SimpleOption<?>[] collectOptions(GameOptions gameOptions, MinecraftClient client) {
        ArrayList<SimpleOption> list = new ArrayList<SimpleOption>();
        list.add(gameOptions.getRealmsNotifications());
        list.add(gameOptions.getAllowServerListing());
        SimpleOption lv = Nullables.map(client.world, world -> {
            Difficulty lv = world.getDifficulty();
            return new SimpleOption<Unit>("options.difficulty.online", SimpleOption.emptyTooltip(), (arg2, unit) -> lv.getTranslatableName(), new SimpleOption.PotentialValuesBasedCallbacks<Unit>(List.of(Unit.INSTANCE), Codec.EMPTY.codec()), Unit.INSTANCE, unit -> {});
        });
        if (lv != null) {
            this.difficulty = lv;
            list.add(lv);
        }
        return list.toArray(new SimpleOption[0]);
    }

    @Override
    protected void addOptions() {
        this.body.addAll(this.collectOptions(this.gameOptions, this.client));
    }
}

