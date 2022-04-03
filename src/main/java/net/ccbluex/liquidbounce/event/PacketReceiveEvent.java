package net.ccbluex.liquidbounce.event;

import net.minecraft.network.Packet;

public class PacketReceiveEvent extends EventCancellable {
	public Packet packet;
	public PacketReceiveEvent(Packet packet) {
		this.packet = packet;
	}

	public Packet getPacket() {
		return packet;
	}

	public void setPacket(Packet packet) {
		this.packet = packet;
	}
}
