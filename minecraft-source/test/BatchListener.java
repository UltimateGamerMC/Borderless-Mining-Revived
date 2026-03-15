package net.minecraft.test;

import net.minecraft.test.GameTestBatch;

public interface BatchListener {
    public void onStarted(GameTestBatch var1);

    public void onFinished(GameTestBatch var1);
}

