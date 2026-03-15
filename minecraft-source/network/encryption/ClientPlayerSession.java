/*
 * External method calls:
 *   Lnet/minecraft/network/encryption/PlayerKeyPair;privateKey()Ljava/security/PrivateKey;
 *   Lnet/minecraft/network/encryption/Signer;create(Ljava/security/PrivateKey;Ljava/lang/String;)Lnet/minecraft/network/encryption/Signer;
 *   Lnet/minecraft/network/encryption/PlayerKeyPair;publicKey()Lnet/minecraft/network/encryption/PlayerPublicKey;
 */
package net.minecraft.network.encryption;

import java.util.UUID;
import net.minecraft.network.encryption.PlayerKeyPair;
import net.minecraft.network.encryption.PublicPlayerSession;
import net.minecraft.network.encryption.Signer;
import net.minecraft.network.message.MessageChain;

public record ClientPlayerSession(UUID sessionId, PlayerKeyPair keyPair) {
    public static ClientPlayerSession create(PlayerKeyPair keyPair) {
        return new ClientPlayerSession(UUID.randomUUID(), keyPair);
    }

    public MessageChain.Packer createPacker(UUID sender) {
        return new MessageChain(sender, this.sessionId).getPacker(Signer.create(this.keyPair.privateKey(), "SHA256withRSA"));
    }

    public PublicPlayerSession toPublicSession() {
        return new PublicPlayerSession(this.sessionId, this.keyPair.publicKey());
    }
}

