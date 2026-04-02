package link.infra.borderlessmining.mixin;

import net.minecraft.client.OptionInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.Consumer;

@Mixin(OptionInstance.class)
public interface OptionInstanceAccessor<T> {
	@Accessor("onValueUpdate")
	Consumer<T> borderlessmining_getOnValueUpdate();
}
