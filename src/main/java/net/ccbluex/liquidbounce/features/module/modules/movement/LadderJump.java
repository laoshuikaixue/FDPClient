package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;

@ModuleInfo(name = "LadderJump", category = ModuleCategory.MOVEMENT)
public class LadderJump extends Module {

    static boolean jumped;

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if(mc.thePlayer.onGround) {
            if(mc.thePlayer.isOnLadder()) {
                mc.thePlayer.motionY = 1.5D;
                jumped = true;
            }else
                jumped = false;
        }else if(!mc.thePlayer.isOnLadder() && jumped)
            mc.thePlayer.motionY += 0.059D;
    }
}
