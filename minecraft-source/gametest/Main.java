/*
 * External method calls:
 *   Lnet/minecraft/test/TestBootstrap;run([Ljava/lang/String;Ljava/util/function/Consumer;)V
 */
package net.minecraft.gametest;

import net.minecraft.SharedConstants;
import net.minecraft.obfuscate.DontObfuscate;
import net.minecraft.test.TestBootstrap;

public class Main {
    @DontObfuscate
    public static void main(String[] args) throws Exception {
        SharedConstants.createGameVersion();
        TestBootstrap.run(args, string -> {});
    }
}

