package net.ccbluex.liquidbounce.features.module.modules.player


import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo

@ModuleInfo(name = "NoFallPlus", category = ModuleCategory.PLAYER)
class NoFallPlus : Module() {

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        val nofall = LiquidBounce.moduleManager.getModule(NoFall::class.java) as NoFall
        if(mc.theWorld.getCollidingBoundingBoxes(
                mc.thePlayer,
                mc.thePlayer.entityBoundingBox.offset(0.0, 0.0, 0.0).expand(0.0, 0.0, 0.0)
            ).isEmpty() && mc.theWorld.getCollidingBoundingBoxes(
                mc.thePlayer,
                mc.thePlayer.entityBoundingBox.offset(0.0, -10002.25, 0.0)
                    .expand(0.0, -10003.75, 0.0)
            ).isEmpty()){
            (LiquidBounce.moduleManager.getModule(NoFall::class.java) as NoFall).state = false
        }
        else{
            (LiquidBounce.moduleManager.getModule(NoFall::class.java) as NoFall).state = true
        }

    }
}
