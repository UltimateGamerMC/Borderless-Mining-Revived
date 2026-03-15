/*
 * External method calls:
 *   Lnet/minecraft/client/sound/SoundListenerTransform;position()Lnet/minecraft/util/math/Vec3d;
 *   Lnet/minecraft/client/sound/SoundListenerTransform;forward()Lnet/minecraft/util/math/Vec3d;
 *   Lnet/minecraft/client/sound/SoundListenerTransform;up()Lnet/minecraft/util/math/Vec3d;
 */
package net.minecraft.client.sound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.SoundListenerTransform;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.openal.AL10;

@Environment(value=EnvType.CLIENT)
public class SoundListener {
    private SoundListenerTransform transform = SoundListenerTransform.DEFAULT;

    public void setTransform(SoundListenerTransform transform) {
        this.transform = transform;
        Vec3d lv = transform.position();
        Vec3d lv2 = transform.forward();
        Vec3d lv3 = transform.up();
        AL10.alListener3f(4100, (float)lv.x, (float)lv.y, (float)lv.z);
        AL10.alListenerfv(4111, new float[]{(float)lv2.x, (float)lv2.y, (float)lv2.z, (float)lv3.getX(), (float)lv3.getY(), (float)lv3.getZ()});
    }

    public void init() {
        this.setTransform(SoundListenerTransform.DEFAULT);
    }

    public SoundListenerTransform getTransform() {
        return this.transform;
    }
}

