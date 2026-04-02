package link.infra.borderlessmining.mixin;

import com.mojang.blaze3d.platform.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Window.class)
public interface WindowDirtyAccessor {
	@Accessor("dirty")
	void borderlessmining_setDirty(boolean dirty);
}
