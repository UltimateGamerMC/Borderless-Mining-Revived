/*
 * External method calls:
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/client/gui/screen/ingame/CyclingSlotIcon;updateTexture(Ljava/util/List;)V
 *   Lnet/minecraft/client/gui/screen/ingame/ForgingScreen;render(Lnet/minecraft/client/gui/DrawContext;IIF)V
 *   Lnet/minecraft/client/gui/screen/ingame/ForgingScreen;drawBackground(Lnet/minecraft/client/gui/DrawContext;FII)V
 *   Lnet/minecraft/client/gui/screen/ingame/CyclingSlotIcon;render(Lnet/minecraft/screen/ScreenHandler;Lnet/minecraft/client/gui/DrawContext;FII)V
 *   Lnet/minecraft/client/gui/DrawContext;addEntity(Lnet/minecraft/client/render/entity/state/EntityRenderState;FLorg/joml/Vector3f;Lorg/joml/Quaternionf;Lorg/joml/Quaternionf;IIII)V
 *   Lnet/minecraft/component/type/EquippableComponent;slot()Lnet/minecraft/entity/EquipmentSlot;
 *   Lnet/minecraft/client/item/ItemModelManager;clearAndUpdate(Lnet/minecraft/client/render/item/ItemRenderState;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemDisplayContext;Lnet/minecraft/world/World;Lnet/minecraft/util/HeldItemContext;I)V
 *   Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIII)V
 *   Lnet/minecraft/client/font/TextRenderer;wrapLines(Lnet/minecraft/text/StringVisitable;I)Ljava/util/List;
 *   Lnet/minecraft/client/gui/DrawContext;drawOrderedTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;II)V
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/screen/ingame/SmithingScreen;equipArmorStand(Lnet/minecraft/item/ItemStack;)V
 *   Lnet/minecraft/client/gui/screen/ingame/SmithingScreen;renderSlotTooltip(Lnet/minecraft/client/gui/DrawContext;II)V
 */
package net.minecraft.client.gui.screen.ingame;

import java.lang.runtime.SwitchBootstraps;
import java.util.List;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.CyclingSlotIcon;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.state.ArmorStandEntityRenderState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@Environment(value=EnvType.CLIENT)
public class SmithingScreen
extends ForgingScreen<SmithingScreenHandler> {
    private static final Identifier ERROR_TEXTURE = Identifier.ofVanilla("container/smithing/error");
    private static final Identifier EMPTY_SLOT_SMITHING_TEMPLATE_ARMOR_TRIM_TEXTURE = Identifier.ofVanilla("container/slot/smithing_template_armor_trim");
    private static final Identifier EMPTY_SLOT_SMITHING_TEMPLATE_NETHERITE_UPGRADE_TEXTURE = Identifier.ofVanilla("container/slot/smithing_template_netherite_upgrade");
    private static final Text MISSING_TEMPLATE_TOOLTIP = Text.translatable("container.upgrade.missing_template_tooltip");
    private static final Text ERROR_TOOLTIP = Text.translatable("container.upgrade.error_tooltip");
    private static final List<Identifier> EMPTY_SLOT_TEXTURES = List.of(EMPTY_SLOT_SMITHING_TEMPLATE_ARMOR_TRIM_TEXTURE, EMPTY_SLOT_SMITHING_TEMPLATE_NETHERITE_UPGRADE_TEXTURE);
    private static final int field_42057 = 44;
    private static final int field_42058 = 15;
    private static final int field_42059 = 28;
    private static final int field_42060 = 21;
    private static final int field_42061 = 65;
    private static final int field_42062 = 46;
    private static final int field_42063 = 115;
    private static final int field_42068 = 210;
    private static final int field_42047 = 25;
    private static final Vector3f ARMOR_STAND_TRANSLATION = new Vector3f(0.0f, 1.0f, 0.0f);
    private static final Quaternionf ARMOR_STAND_ROTATION = new Quaternionf().rotationXYZ(0.43633232f, 0.0f, (float)Math.PI);
    private static final int field_42049 = 25;
    private static final int field_59946 = 121;
    private static final int field_59947 = 20;
    private static final int field_59948 = 161;
    private static final int field_59949 = 80;
    private final CyclingSlotIcon templateSlotIcon = new CyclingSlotIcon(0);
    private final CyclingSlotIcon baseSlotIcon = new CyclingSlotIcon(1);
    private final CyclingSlotIcon additionsSlotIcon = new CyclingSlotIcon(2);
    private final ArmorStandEntityRenderState armorStand = new ArmorStandEntityRenderState();

    public SmithingScreen(SmithingScreenHandler handler, PlayerInventory playerInventory, Text title) {
        super(handler, playerInventory, title, Identifier.ofVanilla("textures/gui/container/smithing.png"));
        this.titleX = 44;
        this.titleY = 15;
        this.armorStand.entityType = EntityType.ARMOR_STAND;
        this.armorStand.showBasePlate = false;
        this.armorStand.showArms = true;
        this.armorStand.pitch = 25.0f;
        this.armorStand.bodyYaw = 210.0f;
    }

    @Override
    protected void setup() {
        this.equipArmorStand(((SmithingScreenHandler)this.handler).getSlot(3).getStack());
    }

    @Override
    public void handledScreenTick() {
        super.handledScreenTick();
        Optional<SmithingTemplateItem> optional = this.getSmithingTemplate();
        this.templateSlotIcon.updateTexture(EMPTY_SLOT_TEXTURES);
        this.baseSlotIcon.updateTexture(optional.map(SmithingTemplateItem::getEmptyBaseSlotTextures).orElse(List.of()));
        this.additionsSlotIcon.updateTexture(optional.map(SmithingTemplateItem::getEmptyAdditionsSlotTextures).orElse(List.of()));
    }

    private Optional<SmithingTemplateItem> getSmithingTemplate() {
        Item item;
        ItemStack lv = ((SmithingScreenHandler)this.handler).getSlot(0).getStack();
        if (!lv.isEmpty() && (item = lv.getItem()) instanceof SmithingTemplateItem) {
            SmithingTemplateItem lv2 = (SmithingTemplateItem)item;
            return Optional.of(lv2);
        }
        return Optional.empty();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        this.renderSlotTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext context, float deltaTicks, int mouseX, int mouseY) {
        super.drawBackground(context, deltaTicks, mouseX, mouseY);
        this.templateSlotIcon.render(this.handler, context, deltaTicks, this.x, this.y);
        this.baseSlotIcon.render(this.handler, context, deltaTicks, this.x, this.y);
        this.additionsSlotIcon.render(this.handler, context, deltaTicks, this.x, this.y);
        int k = this.x + 121;
        int l = this.y + 20;
        int m = this.x + 161;
        int n = this.y + 80;
        context.addEntity(this.armorStand, 25.0f, ARMOR_STAND_TRANSLATION, ARMOR_STAND_ROTATION, null, k, l, m, n);
    }

    @Override
    public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
        if (slotId == 3) {
            this.equipArmorStand(stack);
        }
    }

    private void equipArmorStand(ItemStack stack) {
        this.armorStand.leftHandItem = ItemStack.EMPTY;
        this.armorStand.leftHandItemState.clear();
        this.armorStand.equippedHeadStack = ItemStack.EMPTY;
        this.armorStand.headItemRenderState.clear();
        this.armorStand.equippedChestStack = ItemStack.EMPTY;
        this.armorStand.equippedLegsStack = ItemStack.EMPTY;
        this.armorStand.equippedFeetStack = ItemStack.EMPTY;
        if (!stack.isEmpty()) {
            EquippableComponent lv = stack.get(DataComponentTypes.EQUIPPABLE);
            EquipmentSlot lv2 = lv != null ? lv.slot() : null;
            ItemModelManager lv3 = this.client.getItemModelManager();
            EquipmentSlot equipmentSlot = lv2;
            int n = 0;
            switch (SwitchBootstraps.enumSwitch("enumSwitch", new Object[]{"HEAD", "CHEST", "LEGS", "FEET"}, (EquipmentSlot)equipmentSlot, n)) {
                case 0: {
                    if (ArmorFeatureRenderer.hasModel(stack, EquipmentSlot.HEAD)) {
                        this.armorStand.equippedHeadStack = stack.copy();
                        break;
                    }
                    lv3.clearAndUpdate(this.armorStand.headItemRenderState, stack, ItemDisplayContext.HEAD, null, null, 0);
                    break;
                }
                case 1: {
                    this.armorStand.equippedChestStack = stack.copy();
                    break;
                }
                case 2: {
                    this.armorStand.equippedLegsStack = stack.copy();
                    break;
                }
                case 3: {
                    this.armorStand.equippedFeetStack = stack.copy();
                    break;
                }
                default: {
                    this.armorStand.leftHandItem = stack.copy();
                    lv3.clearAndUpdate(this.armorStand.leftHandItemState, stack, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, null, null, 0);
                }
            }
        }
    }

    @Override
    protected void drawInvalidRecipeArrow(DrawContext context, int x, int y) {
        if (this.hasInvalidRecipe()) {
            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, ERROR_TEXTURE, x + 65, y + 46, 28, 21);
        }
    }

    private void renderSlotTooltip(DrawContext context, int mouseX, int mouseY) {
        Optional<Text> optional = Optional.empty();
        if (this.hasInvalidRecipe() && this.isPointWithinBounds(65, 46, 28, 21, mouseX, mouseY)) {
            optional = Optional.of(ERROR_TOOLTIP);
        }
        if (this.focusedSlot != null) {
            ItemStack lv = ((SmithingScreenHandler)this.handler).getSlot(0).getStack();
            ItemStack lv2 = this.focusedSlot.getStack();
            if (lv.isEmpty()) {
                if (this.focusedSlot.id == 0) {
                    optional = Optional.of(MISSING_TEMPLATE_TOOLTIP);
                }
            } else {
                Item item = lv.getItem();
                if (item instanceof SmithingTemplateItem) {
                    SmithingTemplateItem lv3 = (SmithingTemplateItem)item;
                    if (lv2.isEmpty()) {
                        if (this.focusedSlot.id == 1) {
                            optional = Optional.of(lv3.getBaseSlotDescription());
                        } else if (this.focusedSlot.id == 2) {
                            optional = Optional.of(lv3.getAdditionsSlotDescription());
                        }
                    }
                }
            }
        }
        optional.ifPresent(text -> context.drawOrderedTooltip(this.textRenderer, this.textRenderer.wrapLines((StringVisitable)text, 115), mouseX, mouseY));
    }

    private boolean hasInvalidRecipe() {
        return ((SmithingScreenHandler)this.handler).hasInvalidRecipe();
    }
}

