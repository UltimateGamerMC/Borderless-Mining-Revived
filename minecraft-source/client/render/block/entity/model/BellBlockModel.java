/*
 * External method calls:
 *   Lnet/minecraft/client/model/ModelPartBuilder;create()Lnet/minecraft/client/model/ModelPartBuilder;
 *   Lnet/minecraft/client/model/ModelPartBuilder;uv(II)Lnet/minecraft/client/model/ModelPartBuilder;
 *   Lnet/minecraft/client/model/ModelPartBuilder;cuboid(FFFFFF)Lnet/minecraft/client/model/ModelPartBuilder;
 *   Lnet/minecraft/client/model/ModelTransform;origin(FFF)Lnet/minecraft/client/model/ModelTransform;
 *   Lnet/minecraft/client/model/ModelPartData;addChild(Ljava/lang/String;Lnet/minecraft/client/model/ModelPartBuilder;Lnet/minecraft/client/model/ModelTransform;)Lnet/minecraft/client/model/ModelPartData;
 *   Lnet/minecraft/client/model/TexturedModelData;of(Lnet/minecraft/client/model/ModelData;II)Lnet/minecraft/client/model/TexturedModelData;
 */
package net.minecraft.client.render.block.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class BellBlockModel
extends Model<BellModelState> {
    private static final String BELL_BODY = "bell_body";
    private final ModelPart bellBody;

    public BellBlockModel(ModelPart root) {
        super(root, RenderLayers::entitySolid);
        this.bellBody = root.getChild(BELL_BODY);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData lv = new ModelData();
        ModelPartData lv2 = lv.getRoot();
        ModelPartData lv3 = lv2.addChild(BELL_BODY, ModelPartBuilder.create().uv(0, 0).cuboid(-3.0f, -6.0f, -3.0f, 6.0f, 7.0f, 6.0f), ModelTransform.origin(8.0f, 12.0f, 8.0f));
        lv3.addChild("bell_base", ModelPartBuilder.create().uv(0, 13).cuboid(4.0f, 4.0f, 4.0f, 8.0f, 2.0f, 8.0f), ModelTransform.origin(-8.0f, -12.0f, -8.0f));
        return TexturedModelData.of(lv, 32, 32);
    }

    @Override
    public void setAngles(BellModelState arg) {
        super.setAngles(arg);
        float f = 0.0f;
        float g = 0.0f;
        if (arg.shakeDirection != null) {
            float h = MathHelper.sin(arg.ticks / (float)Math.PI) / (4.0f + arg.ticks / 3.0f);
            switch (arg.shakeDirection) {
                case NORTH: {
                    f = -h;
                    break;
                }
                case SOUTH: {
                    f = h;
                    break;
                }
                case EAST: {
                    g = -h;
                    break;
                }
                case WEST: {
                    g = h;
                }
            }
        }
        this.bellBody.pitch = f;
        this.bellBody.roll = g;
    }

    @Environment(value=EnvType.CLIENT)
    public record BellModelState(float ticks, @Nullable Direction shakeDirection) {
    }
}

