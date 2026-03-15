/*
 * External method calls:
 *   Lnet/minecraft/network/encryption/SignatureUpdatable;update(Lnet/minecraft/network/encryption/SignatureUpdatable$SignatureUpdater;)V
 *   Lnet/minecraft/network/encryption/SignatureUpdatable$SignatureUpdater;update([B)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/encryption/SignatureVerifier;validate(Lnet/minecraft/network/encryption/SignatureUpdatable;[B)Z
 *   Lnet/minecraft/network/encryption/SignatureVerifier;verify(Lnet/minecraft/network/encryption/SignatureUpdatable;[BLjava/security/Signature;)Z
 */
package net.minecraft.network.encryption;

import com.mojang.authlib.yggdrasil.ServicesKeyInfo;
import com.mojang.authlib.yggdrasil.ServicesKeySet;
import com.mojang.authlib.yggdrasil.ServicesKeyType;
import com.mojang.logging.LogUtils;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Collection;
import net.minecraft.network.encryption.SignatureUpdatable;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public interface SignatureVerifier {
    public static final SignatureVerifier NOOP = (updatable, signatureData) -> true;
    public static final Logger LOGGER = LogUtils.getLogger();

    public boolean validate(SignatureUpdatable var1, byte[] var2);

    default public boolean validate(byte[] signedData, byte[] signatureData) {
        return this.validate(updater -> updater.update(signedData), signatureData);
    }

    private static boolean verify(SignatureUpdatable updatable, byte[] signatureData, Signature signature) throws SignatureException {
        updatable.update(signature::update);
        return signature.verify(signatureData);
    }

    public static SignatureVerifier create(PublicKey publicKey, String algorithm) {
        return (updatable, signatureData) -> {
            try {
                Signature signature = Signature.getInstance(algorithm);
                signature.initVerify(publicKey);
                return SignatureVerifier.verify(updatable, signatureData, signature);
            } catch (Exception exception) {
                LOGGER.error("Failed to verify signature", exception);
                return false;
            }
        };
    }

    public static @Nullable SignatureVerifier create(ServicesKeySet servicesKeySet, ServicesKeyType servicesKeyType) {
        Collection<ServicesKeyInfo> collection = servicesKeySet.keys(servicesKeyType);
        if (collection.isEmpty()) {
            return null;
        }
        return (updatable, signatureData) -> collection.stream().anyMatch(keyInfo -> {
            Signature signature = keyInfo.signature();
            try {
                return SignatureVerifier.verify(updatable, signatureData, signature);
            } catch (SignatureException signatureException) {
                LOGGER.error("Failed to verify Services signature", signatureException);
                return false;
            }
        });
    }
}

