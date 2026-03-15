/*
 * Internal private/static methods:
 *   Lnet/minecraft/client/util/Untracker;untrack(J)V
 */
package net.minecraft.client.util;

import com.mojang.blaze3d.platform.GLX;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;
import org.lwjgl.system.Pointer;

@Environment(value=EnvType.CLIENT)
public class Untracker {
    private static final @Nullable MethodHandle ALLOCATOR_UNTRACK = GLX.make(() -> {
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            Class<?> class_ = Class.forName("org.lwjgl.system.MemoryManage$DebugAllocator");
            Method method = class_.getDeclaredMethod("untrack", Long.TYPE);
            method.setAccessible(true);
            Field field = Class.forName("org.lwjgl.system.MemoryUtil$LazyInit").getDeclaredField("ALLOCATOR");
            field.setAccessible(true);
            Object object = field.get(null);
            if (class_.isInstance(object)) {
                return lookup.unreflect(method);
            }
            return null;
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException reflectiveOperationException) {
            throw new RuntimeException(reflectiveOperationException);
        }
    });

    public static void untrack(long address) {
        if (ALLOCATOR_UNTRACK == null) {
            return;
        }
        try {
            ALLOCATOR_UNTRACK.invoke(address);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public static void untrack(Pointer pointer) {
        Untracker.untrack(pointer.address());
    }
}

