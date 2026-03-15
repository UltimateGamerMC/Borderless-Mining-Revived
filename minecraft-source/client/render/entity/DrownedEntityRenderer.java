/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/model/EquipmentModelData;mapToEntityModel(Lnet/minecraft/client/render/entity/model/EquipmentModelData;Lnet/minecraft/client/render/entity/model/LoadedEntityModels;Ljava/util/function/Function;)Lnet/minecraft/client/render/entity/model/EquipmentModelData;
 *   Lnet/minecraft/client/render/entity/ZombieBaseEntityRenderer;setupTransforms(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;FF)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/DrownedEntityRenderer;addFeature(Lnet/minecraft/client/render/entity/feature/FeatureRenderer;)Z
 *   Lnet/minecraft/client/render/entity/DrownedEntityRenderer;setupTransforms(Lnet/minecraft/client/render/entity/state/ZombieEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;FF)V
 *   Lnet/minecraft/client/render/entity/DrownedEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/ZombieEntityRenderState;
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ZombieBaseEntityRenderer;
import net.minecraft.client.render.entity.feature.DrownedOverlayFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.DrownedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EquipmentModelData;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.ZombieEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@Environment(value=EnvType.CLIENT)
public class DrownedEntityRenderer
extends ZombieBaseEntityRenderer<DrownedEntity, ZombieEntityRenderState, DrownedEntityModel> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/zombie/drowned.png");

    public DrownedEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg, new DrownedEntityModel(arg.getPart(EntityModelLayers.DROWNED)), new DrownedEntityModel(arg.getPart(EntityModelLayers.DROWNED_BABY)), EquipmentModelData.mapToEntityModel(EntityModelLayers.DROWNED_EQUIPMENT, arg.getEntityModels(), DrownedEntityModel::new), EquipmentModelData.mapToEntityModel(EntityModelLayers.DROWNED_BABY_EQUIPMENT, arg.getEntityModels(), DrownedEntityModel::new));
        this.addFeature(new DrownedOverlayFeatureRenderer(this, arg.getEntityModels()));
    }

    @Override
    public ZombieEntityRenderState createRenderState() {
        return new ZombieEntityRenderState();
    }

    @Override
    public Identifier getTexture(ZombieEntityRenderState arg) {
        return TEXTURE;
    }

    @Override
    protected void setupTransforms(ZombieEntityRenderState arg, MatrixStack arg2, float f, float g) {
        super.setupTransforms(arg, arg2, f, g);
        float h = arg.leaningPitch;
        if (h > 0.0f) {
            float i = -10.0f - arg.pitch;
            float j = MathHelper.lerp(h, 0.0f, i);
            arg2.multiply(RotationAxis.POSITIVE_X.rotationDegrees(j), 0.0f, arg.height / 2.0f / g, 0.0f);
        }
    }

    @Override
    protected BipedEntityModel.ArmPose getArmPose(DrownedEntity arg, Arm arg2) {
        ItemStack lv = arg.getStackInArm(arg2);
        if (arg.getMainArm() == arg2 && arg.isAttacking() && lv.isOf(Items.TRIDENT)) {
            return BipedEntityModel.ArmPose.THROW_TRIDENT;
        }
        return super.getArmPose(arg, arg2);
    }

    @Override
    public /* synthetic */ Identifier getTexture(LivingEntityRenderState state) {
        return this.getTexture((ZombieEntityRenderState)state);
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

