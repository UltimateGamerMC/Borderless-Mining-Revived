package net.minecraft.network.listener;

import net.minecraft.network.listener.PacketListener;

public interface TickablePacketListener
extends PacketListener {
    public void tick();
}

