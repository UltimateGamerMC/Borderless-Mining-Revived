/*
 * External method calls:
 *   Lnet/minecraft/client/gui/screen/ingame/HandledScreen;render(Lnet/minecraft/client/gui/DrawContext;IIF)V
 *   Lnet/minecraft/client/gui/DrawContext;drawTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIFFIIII)V
 *   Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIII)V
 *   Lnet/minecraft/client/gui/DrawContext;addBannerResult(Lnet/minecraft/client/render/block/entity/model/BannerFlagBlockModel;Lnet/minecraft/util/DyeColor;Lnet/minecraft/component/type/BannerPatternsComponent;IIII)V
 *   Lnet/minecraft/block/entity/BannerPattern;translationKey()Ljava/lang/String;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/gui/DrawContext;drawTooltip(Lnet/minecraft/text/Text;II)V
 *   Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V
 *   Lnet/minecraft/client/gui/DrawContext;drawTexturedQuad(Lnet/minecraft/util/Identifier;IIIIFFFF)V
 *   Lnet/minecraft/screen/LoomScreenHandler;onButtonClick(Lnet/minecraft/entity/player/PlayerEntity;I)Z
 *   Lnet/minecraft/client/sound/PositionedSoundInstance;ui(Lnet/minecraft/sound/SoundEvent;F)Lnet/minecraft/client/sound/PositionedSoundInstance;
 *   Lnet/minecraft/client/sound/SoundManager;play(Lnet/minecraft/client/sound/SoundInstance;)Lnet/minecraft/client/sound/SoundSystem$PlayResult;
 *   Lnet/minecraft/client/network/ClientPlayerInteractionManager;clickButton(II)V
 *   Lnet/minecraft/client/gui/screen/ingame/HandledScreen;mouseClicked(Lnet/minecraft/client/gui/Click;Z)Z
 *   Lnet/minecraft/client/gui/screen/ingame/HandledScreen;mouseDragged(Lnet/minecraft/client/gui/Click;DD)Z
 *   Lnet/minecraft/client/gui/screen/ingame/HandledScreen;mouseReleased(Lnet/minecraft/client/gui/Click;)Z
 *   Lnet/minecraft/client/gui/screen/ingame/HandledScreen;mouseScrolled(DDDD)Z
 *   Lnet/minecraft/component/type/BannerPatternsComponent;layers()Ljava/util/List;
 *   Lnet/minecraft/item/ItemStack;areEqual(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/screen/ingame/LoomScreen;drawMouseoverTooltip(Lnet/minecraft/client/gui/DrawContext;II)V
 *   Lnet/minecraft/client/gui/screen/ingame/LoomScreen;drawBanner(Lnet/minecraft/client/gui/DrawContext;IILnet/minecraft/client/texture/Sprite;)V
 */
package net.minecraft.client.gui.screen.ingame;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.cursor.StandardCursors;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.block.entity.model.BannerFlagBlockModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.texture.Sprite;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BannerItem;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.LoomScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class LoomScreen
extends HandledScreen<LoomScreenHandler> {
    private static final Identifier BANNER_SLOT_TEXTURE = Identifier.ofVanilla("container/slot/banner");
    private static final Identifier DYE_SLOT_TEXTURE = Identifier.ofVanilla("container/slot/dye");
    private static final Identifier PATTERN_SLOT_TEXTURE = Identifier.ofVanilla("container/slot/banner_pattern");
    private static final Identifier SCROLLER_TEXTURE = Identifier.ofVanilla("container/loom/scroller");
    private static final Identifier SCROLLER_DISABLED_TEXTURE = Identifier.ofVanilla("container/loom/scroller_disabled");
    private static final Identifier PATTERN_SELECTED_TEXTURE = Identifier.ofVanilla("container/loom/pattern_selected");
    private static final Identifier PATTERN_HIGHLIGHTED_TEXTURE = Identifier.ofVanilla("container/loom/pattern_highlighted");
    private static final Identifier PATTERN_TEXTURE = Identifier.ofVanilla("container/loom/pattern");
    private static final Identifier ERROR_TEXTURE = Identifier.ofVanilla("container/loom/error");
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/gui/container/loom.png");
    private static final int PATTERN_LIST_COLUMNS = 4;
    private static final int PATTERN_LIST_ROWS = 4;
    private static final int SCROLLBAR_WIDTH = 12;
    private static final int SCROLLBAR_HEIGHT = 15;
    private static final int PATTERN_ENTRY_SIZE = 14;
    private static final int SCROLLBAR_AREA_HEIGHT = 56;
    private static final int PATTERN_LIST_OFFSET_X = 60;
    private static final int PATTERN_LIST_OFFSET_Y = 13;
    private static final float field_59943 = 64.0f;
    private static final float field_59944 = 21.0f;
    private static final float field_59945 = 40.0f;
    private BannerFlagBlockModel bannerField;
    private @Nullable BannerPatternsComponent bannerPatterns;
    private ItemStack banner = ItemStack.EMPTY;
    private ItemStack dye = ItemStack.EMPTY;
    private ItemStack pattern = ItemStack.EMPTY;
    private boolean canApplyDyePattern;
    private boolean hasTooManyPatterns;
    private float scrollPosition;
    private boolean scrollbarClicked;
    private int visibleTopRow;

    public LoomScreen(LoomScreenHandler screenHandler, PlayerInventory inventory, Text title) {
        super(screenHandler, inventory, title);
        screenHandler.setInventoryChangeListener(this::onInventoryChanged);
        this.titleY -= 2;
    }

    @Override
    protected void init() {
        super.init();
        ModelPart lv = this.client.getLoadedEntityModels().getModelPart(EntityModelLayers.STANDING_BANNER_FLAG);
        this.bannerField = new BannerFlagBlockModel(lv);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    private int getRows() {
        return MathHelper.ceilDiv(((LoomScreenHandler)this.handler).getBannerPatterns().size(), 4);
    }

    @Override
    protected void drawBackground(DrawContext context, float deltaTicks, int mouseX, int mouseY) {
        int p;
        int k = this.x;
        int l = this.y;
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, k, l, 0.0f, 0.0f, this.backgroundWidth, this.backgroundHeight, 256, 256);
        Slot lv = ((LoomScreenHandler)this.handler).getBannerSlot();
        Slot lv2 = ((LoomScreenHandler)this.handler).getDyeSlot();
        Slot lv3 = ((LoomScreenHandler)this.handler).getPatternSlot();
        Slot lv4 = ((LoomScreenHandler)this.handler).getOutputSlot();
        if (!lv.hasStack()) {
            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, BANNER_SLOT_TEXTURE, k + lv.x, l + lv.y, 16, 16);
        }
        if (!lv2.hasStack()) {
            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, DYE_SLOT_TEXTURE, k + lv2.x, l + lv2.y, 16, 16);
        }
        if (!lv3.hasStack()) {
            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, PATTERN_SLOT_TEXTURE, k + lv3.x, l + lv3.y, 16, 16);
        }
        int m = (int)(41.0f * this.scrollPosition);
        Identifier lv5 = this.canApplyDyePattern ? SCROLLER_TEXTURE : SCROLLER_DISABLED_TEXTURE;
        int n = k + 119;
        int o = l + 13 + m;
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, lv5, n, o, 12, 15);
        if (mouseX >= n && mouseX < n + 12 && mouseY >= o && mouseY < o + 15) {
            context.setCursor(this.scrollbarClicked ? StandardCursors.RESIZE_NS : StandardCursors.POINTING_HAND);
        }
        if (this.bannerPatterns != null && !this.hasTooManyPatterns) {
            DyeColor lv6 = ((BannerItem)lv4.getStack().getItem()).getColor();
            p = k + 141;
            int q = l + 8;
            context.addBannerResult(this.bannerField, lv6, this.bannerPatterns, p, q, p + 20, q + 40);
        } else if (this.hasTooManyPatterns) {
            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, ERROR_TEXTURE, k + lv4.x - 5, l + lv4.y - 5, 26, 26);
        }
        if (this.canApplyDyePattern) {
            int r = k + 60;
            p = l + 13;
            List<RegistryEntry<BannerPattern>> list = ((LoomScreenHandler)this.handler).getBannerPatterns();
            block0: for (int s = 0; s < 4; ++s) {
                for (int t = 0; t < 4; ++t) {
                    Identifier lv8;
                    boolean bl;
                    int u = s + this.visibleTopRow;
                    int v = u * 4 + t;
                    if (v >= list.size()) break block0;
                    int w = r + t * 14;
                    int x = p + s * 14;
                    RegistryEntry<BannerPattern> lv7 = list.get(v);
                    boolean bl2 = bl = mouseX >= w && mouseY >= x && mouseX < w + 14 && mouseY < x + 14;
                    if (v == ((LoomScreenHandler)this.handler).getSelectedPattern()) {
                        lv8 = PATTERN_SELECTED_TEXTURE;
                    } else if (bl) {
                        lv8 = PATTERN_HIGHLIGHTED_TEXTURE;
                        DyeColor lv9 = ((DyeItem)this.dye.getItem()).getColor();
                        context.drawTooltip(Text.translatable(lv7.value().translationKey() + "." + lv9.getId()), mouseX, mouseY);
                        context.setCursor(StandardCursors.POINTING_HAND);
                    } else {
                        lv8 = PATTERN_TEXTURE;
                    }
                    context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, lv8, w, x, 14, 14);
                    Sprite lv10 = context.getSprite(TexturedRenderLayers.getBannerPatternTextureId(lv7));
                    this.drawBanner(context, w, x, lv10);
                }
            }
        }
        MinecraftClient.getInstance().gameRenderer.getDiffuseLighting().setShaderLights(DiffuseLighting.Type.ITEMS_3D);
    }

    private void drawBanner(DrawContext context, int x, int y, Sprite sprite) {
        context.getMatrices().pushMatrix();
        context.getMatrices().translate(x + 4, y + 2);
        float f = sprite.getMinU();
        float g = f + (sprite.getMaxU() - sprite.getMinU()) * 21.0f / 64.0f;
        float h = sprite.getMaxV() - sprite.getMinV();
        float k = sprite.getMinV() + h / 64.0f;
        float l = k + h * 40.0f / 64.0f;
        int m = 5;
        int n = 10;
        context.fill(0, 0, 5, 10, DyeColor.GRAY.getEntityColor());
        context.drawTexturedQuad(sprite.getAtlasId(), 0, 0, 5, 10, f, g, k, l);
        context.getMatrices().popMatrix();
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (this.canApplyDyePattern) {
            int i = this.x + 60;
            int j = this.y + 13;
            for (int k = 0; k < 4; ++k) {
                for (int l = 0; l < 4; ++l) {
                    double d = click.x() - (double)(i + l * 14);
                    double e = click.y() - (double)(j + k * 14);
                    int m = k + this.visibleTopRow;
                    int n = m * 4 + l;
                    if (!(d >= 0.0) || !(e >= 0.0) || !(d < 14.0) || !(e < 14.0) || !((LoomScreenHandler)this.handler).onButtonClick(this.client.player, n)) continue;
                    MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.ui(SoundEvents.UI_LOOM_SELECT_PATTERN, 1.0f));
                    this.client.interactionManager.clickButton(((LoomScreenHandler)this.handler).syncId, n);
                    return true;
                }
            }
            i = this.x + 119;
            j = this.y + 9;
            if (click.x() >= (double)i && click.x() < (double)(i + 12) && click.y() >= (double)j && click.y() < (double)(j + 56)) {
                this.scrollbarClicked = true;
            }
        }
        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseDragged(Click click, double offsetX, double offsetY) {
        int i = this.getRows() - 4;
        if (this.scrollbarClicked && this.canApplyDyePattern && i > 0) {
            int j = this.y + 13;
            int k = j + 56;
            this.scrollPosition = ((float)click.y() - (float)j - 7.5f) / ((float)(k - j) - 15.0f);
            this.scrollPosition = MathHelper.clamp(this.scrollPosition, 0.0f, 1.0f);
            this.visibleTopRow = Math.max((int)((double)(this.scrollPosition * (float)i) + 0.5), 0);
            return true;
        }
        return super.mouseDragged(click, offsetX, offsetY);
    }

    @Override
    public boolean mouseReleased(Click click) {
        this.scrollbarClicked = false;
        return super.mouseReleased(click);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)) {
            return true;
        }
        int i = this.getRows() - 4;
        if (this.canApplyDyePattern && i > 0) {
            float h = (float)verticalAmount / (float)i;
            this.scrollPosition = MathHelper.clamp(this.scrollPosition - h, 0.0f, 1.0f);
            this.visibleTopRow = Math.max((int)(this.scrollPosition * (float)i + 0.5f), 0);
        }
        return true;
    }

    @Override
    protected boolean isClickOutsideBounds(double mouseX, double mouseY, int left, int top) {
        return mouseX < (double)left || mouseY < (double)top || mouseX >= (double)(left + this.backgroundWidth) || mouseY >= (double)(top + this.backgroundHeight);
    }

    private void onInventoryChanged() {
        ItemStack lv = ((LoomScreenHandler)this.handler).getOutputSlot().getStack();
        this.bannerPatterns = lv.isEmpty() ? null : lv.getOrDefault(DataComponentTypes.BANNER_PATTERNS, BannerPatternsComponent.DEFAULT);
        ItemStack lv2 = ((LoomScreenHandler)this.handler).getBannerSlot().getStack();
        ItemStack lv3 = ((LoomScreenHandler)this.handler).getDyeSlot().getStack();
        ItemStack lv4 = ((LoomScreenHandler)this.handler).getPatternSlot().getStack();
        BannerPatternsComponent lv5 = lv2.getOrDefault(DataComponentTypes.BANNER_PATTERNS, BannerPatternsComponent.DEFAULT);
        boolean bl = this.hasTooManyPatterns = lv5.layers().size() >= 6;
        if (this.hasTooManyPatterns) {
            this.bannerPatterns = null;
        }
        if (!(ItemStack.areEqual(lv2, this.banner) && ItemStack.areEqual(lv3, this.dye) && ItemStack.areEqual(lv4, this.pattern))) {
            boolean bl2 = this.canApplyDyePattern = !lv2.isEmpty() && !lv3.isEmpty() && !this.hasTooManyPatterns && !((LoomScreenHandler)this.handler).getBannerPatterns().isEmpty();
        }
        if (this.visibleTopRow >= this.getRows()) {
            this.visibleTopRow = 0;
            this.scrollPosition = 0.0f;
        }
        this.banner = lv2.copy();
        this.dye = lv3.copy();
        this.pattern = lv4.copy();
    }
}

