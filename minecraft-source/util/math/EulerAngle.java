/*
 * External method calls:
 *   Lnet/minecraft/util/Util;decodeFixedLengthList(Ljava/util/List;I)Lcom/mojang/serialization/DataResult;
 */
package net.minecraft.util.math;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.List;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.Util;

public record EulerAngle(float pitch, float yaw, float roll) {
    public static final Codec<EulerAngle> CODEC = Codec.FLOAT.listOf().comapFlatMap(list -> Util.decodeFixedLengthList(list, 3).map(angles -> new EulerAngle(((Float)angles.get(0)).floatValue(), ((Float)angles.get(1)).floatValue(), ((Float)angles.get(2)).floatValue())), angle -> List.of(Float.valueOf(angle.pitch()), Float.valueOf(angle.yaw()), Float.valueOf(angle.roll())));
    public static final PacketCodec<ByteBuf, EulerAngle> PACKET_CODEC = new PacketCodec<ByteBuf, EulerAngle>(){

        @Override
        public EulerAngle decode(ByteBuf byteBuf) {
            return new EulerAngle(byteBuf.readFloat(), byteBuf.readFloat(), byteBuf.readFloat());
        }

        @Override
        public void encode(ByteBuf byteBuf, EulerAngle arg) {
            byteBuf.writeFloat(arg.pitch);
            byteBuf.writeFloat(arg.yaw);
            byteBuf.writeFloat(arg.roll);
        }

        @Override
        public /* synthetic */ void encode(Object object, Object object2) {
            this.encode((ByteBuf)object, (EulerAngle)object2);
        }

        @Override
        public /* synthetic */ Object decode(Object object) {
            return this.decode((ByteBuf)object);
        }
    };

    public EulerAngle {
        pitch = Float.isInfinite(pitch) || Float.isNaN(pitch) ? 0.0f : pitch % 360.0f;
        yaw = Float.isInfinite(yaw) || Float.isNaN(yaw) ? 0.0f : yaw % 360.0f;
        roll = Float.isInfinite(roll) || Float.isNaN(roll) ? 0.0f : roll % 360.0f;
    }
}

