package net.fabricmc.fabric.impl.transfer.item;

public interface SpecialLogicAccess {
	default boolean fabric_shouldSuppressSpecialLogic() {
		return false;
	}
}
