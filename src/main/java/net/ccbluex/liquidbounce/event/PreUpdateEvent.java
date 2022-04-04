package net.ccbluex.liquidbounce.event;

public class PreUpdateEvent extends Event  {

    private float yaw;
    private float pitch;
    private double x, y, z;
    public boolean onGround;
    private boolean sneaking;
    public static float YAW, PITCH, prevYAW, prevPITCH;
    public static boolean SNEAKING;

    public PreUpdateEvent(double x, double y, double z, float yaw, float pitch, boolean sneaking, boolean onGround) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.y = y;
        this.x = x;
        this.z = z;
        this.onGround = onGround;
        this.sneaking = sneaking;
    }

    public void fire() {
        prevYAW = YAW;
        prevPITCH = PITCH;
        YAW = this.yaw;
        PITCH = this.pitch;
        SNEAKING = this.sneaking;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public boolean isSneaking(){
        return this.sneaking;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}
