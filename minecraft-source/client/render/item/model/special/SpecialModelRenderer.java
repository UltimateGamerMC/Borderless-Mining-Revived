package net.minecraft.client.render.item.model.special;

import com.mojang.serialization.MapCodec;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.texture.PlayerSkinCache;
import net.minecraft.client.texture.SpriteHolder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public interface SpecialModelRenderer<T> {
    public void render(@Nullable T var1, ItemDisplayContext var2, MatrixStack var3, OrderedRenderCommandQueue var4, int var5, int var6, boolean var7, int var8);

    public void collectVertices(Consumer<Vector3fc> var1);

    public @Nullable T getData(ItemStack var1);

    @Environment(value=EnvType.CLIENT)
    public static interface BakeContext {
        public LoadedEntityModels entityModelSet();

        public SpriteHolder spriteHolder();

        public PlayerSkinCache playerSkinRenderCache();

        @Environment(value=EnvType.CLIENT)
        public record Simple(LoadedEntityModels entityModelSet, SpriteHolder spriteHolder, PlayerSkinCache playerSkinRenderCache) implements BakeContext
        {
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static interface Unbaked {
        public @Nullable SpecialModelRenderer<?> bake(BakeContext var1);

        public MapCodec<? extends Unbaked> getCodec();
    }
}

