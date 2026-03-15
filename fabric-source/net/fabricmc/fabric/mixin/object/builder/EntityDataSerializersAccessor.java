package net.fabricmc.fabric.mixin.object.builder;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.util.CrudeIncrementalIntIdentityHashBiMap;

@Mixin(EntityDataSerializers.class)
public interface EntityDataSerializersAccessor {
	@Accessor("SERIALIZERS")
	static CrudeIncrementalIntIdentityHashBiMap<EntityDataSerializer<?>> fabric_getDataHandlers() {
		throw new AssertionError("Untransformed @Accessor");
	}
}
