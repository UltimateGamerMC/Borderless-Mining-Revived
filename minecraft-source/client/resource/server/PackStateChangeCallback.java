package net.minecraft.client.resource.server;

import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public interface PackStateChangeCallback {
    public void onStateChanged(UUID var1, State var2);

    public void onFinish(UUID var1, FinishState var2);

    @Environment(value=EnvType.CLIENT)
    public static enum FinishState {
        DECLINED,
        APPLIED,
        DISCARDED,
        DOWNLOAD_FAILED,
        ACTIVATION_FAILED;

    }

    @Environment(value=EnvType.CLIENT)
    public static enum State {
        ACCEPTED,
        DOWNLOADED;

    }
}

