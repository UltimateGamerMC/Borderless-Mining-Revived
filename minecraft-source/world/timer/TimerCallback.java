package net.minecraft.world.timer;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.timer.Timer;

public interface TimerCallback<T> {
    public void call(T var1, Timer<T> var2, long var3);

    public MapCodec<? extends TimerCallback<T>> getCodec();
}

