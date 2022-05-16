package net.ccbluex.liquidbounce.features.module.modules.player;

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Text
import net.ccbluex.liquidbounce.utils.EntityUtils
import net.minecraft.entity.EntityLivingBase
import kotlin.math.abs

@ModuleInfo(name = "PlayerHealthSend", category = ModuleCategory.PLAYER)
class PlayerHealthSend : Module() {
    private val healthData=HashMap<Int,Float>()
    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        for(entity in mc.theWorld.loadedEntityList){
                if(entity is EntityLivingBase && EntityUtils.isSelected(entity,true)){
                    val lastHealth=healthData.getOrDefault(mc.thePlayer.entityId,mc.thePlayer.maxHealth)
                      healthData[mc.thePlayer.entityId] = mc.thePlayer.health
                      if(lastHealth==mc.thePlayer.health) continue
                      if(lastHealth>mc.thePlayer.health){
                          alert("§c[Health][-]§a"+(Text.DECIMAL_FORMAT.format(lastHealth-mc.thePlayer.health))+"HP")
                      }else{
                          alert("§c[Health][+]§a"+(Text.DECIMAL_FORMAT.format(abs(lastHealth-mc.thePlayer.health)))+"HP")
                }
            }
        }
    }
}