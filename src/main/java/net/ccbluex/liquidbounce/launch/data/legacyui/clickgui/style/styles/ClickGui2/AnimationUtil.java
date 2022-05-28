package net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.style.styles.ClickGui2;

public class AnimationUtil {

    public static float moveTowards(float current, float end, float smoothSpeed, float minSpeed) {
        float movement = (end - current) * smoothSpeed;

        if (movement > 0.0F) {
            movement = Math.max(minSpeed, movement);
            movement = Math.min(end - current, movement);
        } else if (movement < 0.0F) {
            movement = Math.min(-minSpeed, movement);
            movement = Math.max(end - current, movement);
        }

        return current + movement;
    }

    public static float calculateCompensation(float target, float current, long delta, int speed) {
        float diff = current - target;

        if (delta < 1L) {
            delta = 1L;
        }

        double xD;

        if (diff > (float) speed) {
            xD = (double) ((long) speed * delta / 16L) < 0.25D ? 0.5D : (double) ((long) speed * delta / 16L);
            current = (float) ((double) current - xD);
            if (current < target) {
                current = target;
            }
        } else if (diff < (float) (-speed)) {
            xD = (double) ((long) speed * delta / 16L) < 0.25D ? 0.5D : (double) ((long) speed * delta / 16L);
            current = (float) ((double) current + xD);
            if (current > target) {
                current = target;
            }
        } else {
            current = target;
        }

        return current;
    }

    public static float animate(float target, float current, double speed) {
        boolean larger = target > current;

        if (speed < 0.0D) {
            speed = 0.0D;
        } else if (speed > 1.0D) {
            speed = 1.0D;
        }

        float dif = Math.max(target, current) - Math.min(target, current);
        float factor = (float) ((double) dif * speed);

        current = larger ? current + factor : current - factor;
        return current;
    }

    public static double animate(double target, double current, double speed) {
        boolean larger = target > current;

        if (speed < 0.0D) {
            speed = 0.0D;
        } else if (speed > 1.0D) {
            speed = 1.0D;
        }

        double dif = Math.max(target, current) - Math.min(target, current);
        double factor = dif * speed;

        if (factor < 0.10000000149011612D) {
            factor = 0.10000000149011612D;
        }

        current = larger ? current + factor : current - factor;
        return current;
    }

    public static float mvoeUD(float current, float end, float minSpeed) {
        return moveUD(current, end, 0.125F, minSpeed);
    }

    public static float moveUD(float current, float end, float smoothSpeed, float minSpeed) {
        float movement = (end - current) * smoothSpeed;

        if (movement > 10.0F) {
            movement = Math.max(minSpeed, movement);
            movement = Math.min(end - current, movement);
        } else if (movement < 10.0F) {
            movement = Math.min(-minSpeed, movement);
            movement = Math.max(end - current, movement);
        }

        return current + movement;
    }
}
