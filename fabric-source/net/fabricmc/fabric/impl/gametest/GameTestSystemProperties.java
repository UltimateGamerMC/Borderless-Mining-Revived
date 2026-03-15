package net.fabricmc.fabric.impl.gametest;

public final class GameTestSystemProperties {
	/**
	 * Enable the game test system.
	 */
	public static final String ENABLED = "fabric-api.gametest";

	/**
	 * A JUnit XML report file to write the test results to.
	 */
	public static final String REPORT_FILE = "fabric-api.gametest.report-file";

	/**
	 * Filter the tests to run by the test name.
	 */
	public static final String FILTER = "fabric-api.gametest.filter";

	/**
	 * Run the enabled tests 100 times for each 90 degree rotation.
	 */
	public static final String VERIFY = "fabric-api.gametest.verify";

	private GameTestSystemProperties() {
	}
}
