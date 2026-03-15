package net.fabricmc.fabric.api.gamerule.v1;

import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;

/**
 * An extended game rule visitor which supports Fabric's own rule types.
 *
 * <p>Game rule visitors are typically used when iterating all game rules.
 * In vanilla, the visitor is used to register game rule commands and populate the {@code Edit Game Rules} screen.
 *
 * <p>Rule types specified by this interface are not exhaustive.
 * New entries may be added in the future.
 */
public interface FabricGameRuleVisitor extends GameRuleTypeVisitor {
	/**
	 * Visit a double rule.
	 */
	default void visitDouble(GameRule<Double> doubleRule) {
	}

	/**
	 * Visit an enum rule.
	 */
	default <E extends Enum<E>> void visitEnum(GameRule<E> enumRule) {
	}
}
