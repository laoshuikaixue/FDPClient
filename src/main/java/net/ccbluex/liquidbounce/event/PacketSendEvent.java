package net.ccbluex.liquidbounce.event;

import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class PacketSendEvent extends EventCancellable {
	public Packet packet;

    public Object getPacket() {
        this.packet = packet;
        if (packet instanceof C03PacketPlayer.C05PacketPlayerLook || packet instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
            float yaw = ((C03PacketPlayer) packet).getYaw();
            float pitch = ((C03PacketPlayer) packet).getPitch();
            RotationUtils.serverRotation.setYaw(yaw);
            RotationUtils.serverRotation.setPitch(pitch);
        }
        return null;
    }
}
