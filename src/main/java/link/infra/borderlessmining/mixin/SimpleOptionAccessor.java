package link.infra.borderlessmining.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.option.SimpleOption;

import java.util.function.Consumer;

@Mixin(SimpleOption.class)
public interface SimpleOptionAccessor<T> {
	@Accessor("changeCallback")
	Consumer<T> borderlessmining_getChangeCallback();
}
