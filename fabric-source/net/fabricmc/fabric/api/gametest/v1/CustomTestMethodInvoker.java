package net.fabricmc.fabric.api.gametest.v1;

import java.lang.reflect.Method;

import net.minecraft.gametest.framework.GameTestHelper;

/**
 * Implement this interface on test suites to provide custom logic for invoking {@link GameTest} test methods.
 */
public interface CustomTestMethodInvoker {
	/**
	 * Implement this method to provide custom logic used to invoke the test method.
	 * This can be used to run code before or after each test.
	 * You can also pass in custom parameters into the test method if desired.
	 * The structure will have been placed in the world before this method is invoked.
	 *
	 * @param context The vanilla test context
	 * @param method The test method to invoke
	 */
	void invokeTestMethod(GameTestHelper context, Method method) throws ReflectiveOperationException;
}
