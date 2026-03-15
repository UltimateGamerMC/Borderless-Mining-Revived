/*
 * External method calls:
 *   Lnet/minecraft/network/message/LastSeenMessageList$Acknowledgment;acknowledged()Ljava/util/BitSet;
 *   Lnet/minecraft/network/message/AcknowledgedMessage;unmarkAsPending()Lnet/minecraft/network/message/AcknowledgedMessage;
 *   Lnet/minecraft/network/message/AcknowledgedMessage;signature()Lnet/minecraft/network/message/MessageSignatureData;
 *   Lnet/minecraft/network/message/LastSeenMessageList$Acknowledgment;checksumEquals(Lnet/minecraft/network/message/LastSeenMessageList;)Z
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/message/AcknowledgmentValidator;removeUntil(I)V
 */
package net.minecraft.network.message;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.network.message.AcknowledgedMessage;
import net.minecraft.network.message.LastSeenMessageList;
import net.minecraft.network.message.MessageSignatureData;
import org.jspecify.annotations.Nullable;

public class AcknowledgmentValidator {
    private final int size;
    private final ObjectList<AcknowledgedMessage> messages = new ObjectArrayList<AcknowledgedMessage>();
    private @Nullable MessageSignatureData lastSignature;

    public AcknowledgmentValidator(int size) {
        this.size = size;
        for (int j = 0; j < size; ++j) {
            this.messages.add(null);
        }
    }

    public void addPending(MessageSignatureData signature) {
        if (!signature.equals(this.lastSignature)) {
            this.messages.add(new AcknowledgedMessage(signature, true));
            this.lastSignature = signature;
        }
    }

    public int getMessageCount() {
        return this.messages.size();
    }

    public void removeUntil(int index) throws ValidationException {
        int j = this.messages.size() - this.size;
        if (index < 0 || index > j) {
            throw new ValidationException("Advanced last seen window by " + index + " messages, but expected at most " + j);
        }
        this.messages.removeElements(0, index);
    }

    public LastSeenMessageList validate(LastSeenMessageList.Acknowledgment acknowledgment) throws ValidationException {
        this.removeUntil(acknowledgment.offset());
        ObjectArrayList<MessageSignatureData> objectList = new ObjectArrayList<MessageSignatureData>(acknowledgment.acknowledged().cardinality());
        if (acknowledgment.acknowledged().length() > this.size) {
            throw new ValidationException("Last seen update contained " + acknowledgment.acknowledged().length() + " messages, but maximum window size is " + this.size);
        }
        for (int i = 0; i < this.size; ++i) {
            boolean bl = acknowledgment.acknowledged().get(i);
            AcknowledgedMessage lv = (AcknowledgedMessage)this.messages.get(i);
            if (bl) {
                if (lv == null) {
                    throw new ValidationException("Last seen update acknowledged unknown or previously ignored message at index " + i);
                }
                this.messages.set(i, lv.unmarkAsPending());
                objectList.add(lv.signature());
                continue;
            }
            if (lv != null && !lv.pending()) {
                throw new ValidationException("Last seen update ignored previously acknowledged message at index " + i + " and signature " + String.valueOf(lv.signature()));
            }
            this.messages.set(i, null);
        }
        LastSeenMessageList lv2 = new LastSeenMessageList(objectList);
        if (!acknowledgment.checksumEquals(lv2)) {
            throw new ValidationException("Checksum mismatch on last seen update: the client and server must have desynced");
        }
        return lv2;
    }

    public static class ValidationException
    extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }
}

