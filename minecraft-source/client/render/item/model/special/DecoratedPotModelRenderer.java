/*
 * External method calls:
 *   Lnet/minecraft/client/render/block/entity/DecoratedPotBlockEntityRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;IILnet/minecraft/block/entity/Sherds;I)V
 *   Lnet/minecraft/client/render/block/entity/DecoratedPotBlockEntityRenderer;collectVertices(Ljava/util/function/Consumer;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/item/model/special/DecoratedPotModelRenderer;render(Lnet/minecraft/block/entity/Sherds;Lnet/minecraft/item/ItemDisplayContext;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;IIZI)V
 */
package net.minecraft.client.render.item.model.special;

import com.mojang.serialization.MapCodec;
import java.util.Objects;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.Sherds;
import net.minecraft.client.render.block.entity.DecoratedPotBlockEntityRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.item.model.special.SpecialModelRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class DecoratedPotModelRenderer
implements SpecialModelRenderer<Sherds> {
    private final DecoratedPotBlockEntityRenderer blockEntityRenderer;

    public DecoratedPotModelRenderer(DecoratedPotBlockEntityRenderer blockEntityRenderer) {
        this.blockEntityRenderer = blockEntityRenderer;
    }

    @Override
    public @Nullable Sherds getData(ItemStack arg) {
        return arg.get(DataComponentTypes.POT_DECORATIONS);
    }

    @Override
    public void render(@Nullable Sherds arg, ItemDisplayContext arg2, MatrixStack arg3, OrderedRenderCommandQueue arg4, int i, int j, boolean bl, int k) {
        this.blockEntityRenderer.render(arg3, arg4, i, j, Objects.requireNonNullElse(arg, Sherds.DEFAULT), k);
    }

    @Override
    public void collectVertices(Consumer<Vector3fc> consumer) {
        this.blockEntityRenderer.collectVertices(consumer);
    }

    @Override
    public /* synthetic */ @Nullable Object getData(ItemStack stack) {
        return this.getData(stack);
    }

    @Environment(value=EnvType.CLIENT)
    public record Unbaked() implements SpecialModelRenderer.Unbaked
    {
        public static final MapCodec<Unbaked> CODEC = MapCodec.unit(new Unbaked());

        public MapCodec<Unbaked> getCodec() {
            return CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(SpecialModelRenderer.BakeContext context) {
            return new DecoratedPotModelRenderer(new DecoratedPotBlockEntityRenderer(context));
        }
    }
}

