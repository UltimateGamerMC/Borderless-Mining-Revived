package net.fabricmc.fabric.api.gametest.v1;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.minecraft.world.level.block.Rotation;

/**
 * {@link GameTest} is an annotation that can be used to mark a method as a game test.
 *
 * <p>{@link GameTest} methods must be {@code public} not {@code static}, return {@code void } and take exactly one argument of type {@link net.minecraft.gametest.framework.GameTestHelper}.
 *
 * <p>The values in this class directly correspond to the values in {@link net.minecraft.gametest.framework.TestData}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface GameTest {
	/**
	 * A namespaced ID of an entry within the {@link net.minecraft.core.registries.Registries#TEST_ENVIRONMENT} registry.
	 */
	String environment() default "minecraft:default";

	/**
	 * A namespaced ID pointing to a structure resource in the {@code modid/gametest/structure/} directory.
	 *
	 * <p>Defaults to an 8x8 structure with no blocks.
	 */
	String structure() default "fabric-gametest-api-v1:empty";

	/**
	 * The maximum number of ticks the test is allowed to run for.
	 */
	int maxTicks() default 20;

	/**
	 * The number of ticks to wait before starting the test after placing the structure.
	 */
	int setupTicks() default 0;

	/**
	 * Whether the test is required to pass for the test suite to pass.
	 */
	boolean required() default true;

	/**
	 * The rotation of the structure when placed.
	 */
	Rotation rotation() default Rotation.NONE;

	/**
	 * When set the test must be run manually.
	 */
	boolean manualOnly() default false;

	/**
	 * The number of times the test should be re attempted if it fails.
	 */
	int maxAttempts() default 1;

	/**
	 * The number of times the test should be successfully ran before it is considered a success.
	 */
	int requiredSuccesses() default 1;

	/**
	 * Whether the test should have sky access. When {@code false} the test will be enclosed by barrier blocks.
	 */
	boolean skyAccess() default false;
}
