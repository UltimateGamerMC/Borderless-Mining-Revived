/*
 * Internal private/static methods:
 *   Lnet/minecraft/SaveVersion;series()Ljava/lang/String;
 */
package net.minecraft;

import net.minecraft.SharedConstants;

public record SaveVersion(int id, String series) {
    public static final String MAIN_SERIES = "main";

    public boolean isNotMainSeries() {
        return !this.series.equals(MAIN_SERIES);
    }

    public boolean isAvailableTo(SaveVersion other) {
        if (SharedConstants.OPEN_INCOMPATIBLE_WORLDS) {
            return true;
        }
        return this.series().equals(other.series());
    }
}

