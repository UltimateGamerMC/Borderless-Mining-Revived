package net.minecraft.world.timer.stopwatch;

public record Stopwatch(long creationTime, long accumulatedElapsedTime) {
    public Stopwatch(long creationTimeMs) {
        this(creationTimeMs, 0L);
    }

    public long getElapsedTimeMs(long timeMs) {
        long m = timeMs - this.creationTime;
        return this.accumulatedElapsedTime + m;
    }

    public double getElapsedTimeSeconds(long timeMs) {
        return (double)this.getElapsedTimeMs(timeMs) / 1000.0;
    }
}

