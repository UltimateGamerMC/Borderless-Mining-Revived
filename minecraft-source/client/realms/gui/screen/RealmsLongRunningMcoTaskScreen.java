/*
 * External method calls:
 *   Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;vertical()Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;
 *   Lnet/minecraft/client/realms/RepeatedNarrator;narrate(Lnet/minecraft/client/util/NarratorManager;Lnet/minecraft/text/Text;)V
 *   Lnet/minecraft/client/realms/gui/screen/RealmsScreen;keyPressed(Lnet/minecraft/client/input/KeyInput;)Z
 *   Lnet/minecraft/client/gui/widget/Positioner;alignHorizontalCenter()Lnet/minecraft/client/gui/widget/Positioner;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget;builder(Lnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;build()Lnet/minecraft/client/gui/widget/ButtonWidget;
 *   Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;forEachChild(Ljava/util/function/Consumer;)V
 *   Lnet/minecraft/client/gui/widget/Positioner;marginTop(I)Lnet/minecraft/client/gui/widget/Positioner;
 *   Lnet/minecraft/client/gui/widget/Positioner;marginBottom(I)Lnet/minecraft/client/gui/widget/Positioner;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/realms/gui/screen/RealmsLongRunningMcoTaskScreen;createRealmsLogoIconWidget()Lnet/minecraft/client/gui/widget/IconWidget;
 *   Lnet/minecraft/client/realms/gui/screen/RealmsLongRunningMcoTaskScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;
 */
package net.minecraft.client.realms.gui.screen;

import com.mojang.logging.LogUtils;
import java.time.Duration;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.LoadingWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.realms.RepeatedNarrator;
import net.minecraft.client.realms.exception.RealmsDefaultUncaughtExceptionHandler;
import net.minecraft.client.realms.gui.screen.RealmsScreen;
import net.minecraft.client.realms.task.LongRunningTask;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(value=EnvType.CLIENT)
public class RealmsLongRunningMcoTaskScreen
extends RealmsScreen {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final RepeatedNarrator NARRATOR = new RepeatedNarrator(Duration.ofSeconds(5L));
    private final List<LongRunningTask> tasks;
    private final Screen parent;
    protected final DirectionalLayoutWidget layout = DirectionalLayoutWidget.vertical();
    private volatile Text title;
    private @Nullable LoadingWidget loading;

    public RealmsLongRunningMcoTaskScreen(Screen parent, LongRunningTask ... tasks) {
        super(NarratorManager.EMPTY);
        this.parent = parent;
        this.tasks = List.of(tasks);
        if (this.tasks.isEmpty()) {
            throw new IllegalArgumentException("No tasks added");
        }
        this.title = this.tasks.get(0).getTitle();
        Runnable runnable = () -> {
            for (LongRunningTask lv : tasks) {
                this.setTitle(lv.getTitle());
                if (lv.aborted()) break;
                lv.run();
                if (!lv.aborted()) continue;
                return;
            }
        };
        Thread thread = new Thread(runnable, "Realms-long-running-task");
        thread.setUncaughtExceptionHandler(new RealmsDefaultUncaughtExceptionHandler(LOGGER));
        thread.start();
    }

    @Override
    public boolean canInterruptOtherScreen() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.loading != null) {
            NARRATOR.narrate(this.client.getNarratorManager(), this.loading.getMessage());
        }
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if (input.key() == InputUtil.GLFW_KEY_ESCAPE) {
            this.onCancel();
            return true;
        }
        return super.keyPressed(input);
    }

    @Override
    public void init() {
        this.layout.getMainPositioner().alignHorizontalCenter();
        this.layout.add(RealmsLongRunningMcoTaskScreen.createRealmsLogoIconWidget());
        this.loading = new LoadingWidget(this.textRenderer, this.title);
        this.layout.add(this.loading, positioner -> positioner.marginTop(10).marginBottom(30));
        this.layout.add(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.onCancel()).build());
        this.layout.forEachChild(child -> {
            ClickableWidget cfr_ignored_0 = (ClickableWidget)this.addDrawableChild(child);
        });
        this.refreshWidgetPositions();
    }

    @Override
    protected void refreshWidgetPositions() {
        this.layout.refreshPositions();
        SimplePositioningWidget.setPos(this.layout, this.getNavigationFocus());
    }

    protected void onCancel() {
        for (LongRunningTask lv : this.tasks) {
            lv.abortTask();
        }
        this.client.setScreen(this.parent);
    }

    public void setTitle(Text title) {
        if (this.loading != null) {
            this.loading.setMessage(title);
        }
        this.title = title;
    }
}

