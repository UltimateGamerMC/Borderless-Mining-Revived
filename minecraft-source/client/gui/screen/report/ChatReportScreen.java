/*
 * External method calls:
 *   Lnet/minecraft/client/gui/widget/ButtonWidget;builder(Lnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;width(I)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;build()Lnet/minecraft/client/gui/widget/ButtonWidget;
 *   Lnet/minecraft/client/gui/widget/LayoutWidgets;createLabeledWidget(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/client/gui/widget/Widget;Lnet/minecraft/text/Text;)Lnet/minecraft/client/gui/widget/LayoutWidget;
 *   Lnet/minecraft/client/gui/widget/LayoutWidgets;createLabeledWidget(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/client/gui/widget/Widget;Lnet/minecraft/text/Text;Ljava/util/function/Consumer;)Lnet/minecraft/client/gui/widget/LayoutWidget;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/gui/screen/report/ReportScreen;mouseReleased(Lnet/minecraft/client/gui/Click;)Z
 *   Lnet/minecraft/client/gui/widget/EditBoxWidget;mouseReleased(Lnet/minecraft/client/gui/Click;)Z
 *   Lnet/minecraft/client/gui/widget/Positioner;marginBottom(I)Lnet/minecraft/client/gui/widget/Positioner;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/screen/report/ChatReportScreen;createCommentsBox(IILjava/util/function/Consumer;)Lnet/minecraft/client/gui/widget/EditBoxWidget;
 */
package net.minecraft.client.gui.screen.report;

import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.report.AbuseReportReasonScreen;
import net.minecraft.client.gui.screen.report.ChatSelectionScreen;
import net.minecraft.client.gui.screen.report.ReportScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EditBoxWidget;
import net.minecraft.client.gui.widget.LayoutWidgets;
import net.minecraft.client.session.report.AbuseReportContext;
import net.minecraft.client.session.report.AbuseReportReason;
import net.minecraft.client.session.report.AbuseReportType;
import net.minecraft.client.session.report.ChatAbuseReport;
import net.minecraft.text.Text;

@Environment(value=EnvType.CLIENT)
public class ChatReportScreen
extends ReportScreen<ChatAbuseReport.Builder> {
    private static final Text TITLE_TEXT = Text.translatable("gui.chatReport.title");
    private static final Text SELECT_CHAT_TEXT = Text.translatable("gui.chatReport.select_chat");
    private EditBoxWidget commentsBox;
    private ButtonWidget selectChatButton;
    private ButtonWidget selectReasonButton;

    private ChatReportScreen(Screen parent, AbuseReportContext context, ChatAbuseReport.Builder reportBuilder) {
        super(TITLE_TEXT, parent, context, reportBuilder);
    }

    public ChatReportScreen(Screen parent, AbuseReportContext reporter, UUID reportedPlayerUuid) {
        this(parent, reporter, new ChatAbuseReport.Builder(reportedPlayerUuid, reporter.getSender().getLimits()));
    }

    public ChatReportScreen(Screen parent, AbuseReportContext context, ChatAbuseReport report) {
        this(parent, context, new ChatAbuseReport.Builder(report, context.getSender().getLimits()));
    }

    @Override
    protected void addContent() {
        this.selectChatButton = this.layout.add(ButtonWidget.builder(SELECT_CHAT_TEXT, button -> this.client.setScreen(new ChatSelectionScreen(this, this.context, (ChatAbuseReport.Builder)this.reportBuilder, updatedReportBuilder -> {
            this.reportBuilder = updatedReportBuilder;
            this.onChange();
        }))).width(280).build());
        this.selectReasonButton = ButtonWidget.builder(SELECT_REASON_TEXT, button -> this.client.setScreen(new AbuseReportReasonScreen(this, ((ChatAbuseReport.Builder)this.reportBuilder).getReason(), AbuseReportType.CHAT, reason -> {
            ((ChatAbuseReport.Builder)this.reportBuilder).setReason((AbuseReportReason)((Object)((Object)reason)));
            this.onChange();
        }))).width(280).build();
        this.layout.add(LayoutWidgets.createLabeledWidget(this.textRenderer, this.selectReasonButton, OBSERVED_WHAT_TEXT));
        this.commentsBox = this.createCommentsBox(280, this.textRenderer.fontHeight * 8, comments -> {
            ((ChatAbuseReport.Builder)this.reportBuilder).setOpinionComments((String)comments);
            this.onChange();
        });
        this.layout.add(LayoutWidgets.createLabeledWidget(this.textRenderer, this.commentsBox, MORE_COMMENTS_TEXT, positioner -> positioner.marginBottom(12)));
    }

    @Override
    protected void onChange() {
        IntSet intSet = ((ChatAbuseReport.Builder)this.reportBuilder).getSelectedMessages();
        if (intSet.isEmpty()) {
            this.selectChatButton.setMessage(SELECT_CHAT_TEXT);
        } else {
            this.selectChatButton.setMessage(Text.translatable("gui.chatReport.selected_chat", intSet.size()));
        }
        AbuseReportReason lv = ((ChatAbuseReport.Builder)this.reportBuilder).getReason();
        if (lv != null) {
            this.selectReasonButton.setMessage(lv.getText());
        } else {
            this.selectReasonButton.setMessage(SELECT_REASON_TEXT);
        }
        super.onChange();
    }

    @Override
    public boolean mouseReleased(Click click) {
        if (super.mouseReleased(click)) {
            return true;
        }
        return this.commentsBox.mouseReleased(click);
    }
}

