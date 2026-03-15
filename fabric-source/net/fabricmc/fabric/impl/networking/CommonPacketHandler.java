package net.fabricmc.fabric.impl.networking;

public interface CommonPacketHandler {
	void onCommonVersionPacket(int negotiatedVersion);

	void onCommonRegisterPacket(CommonRegisterPayload payload);

	CommonRegisterPayload createRegisterPayload();

	int getNegotiatedVersion();
}
