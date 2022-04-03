package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.Render2DEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.render.CompassUtil
import net.minecraft.client.gui.ScaledResolution

@ModuleInfo(
    name = "Compass",
    category = ModuleCategory.RENDER
)
class Compass : Module() {
    @EventTarget
    private fun onRender2D(e: Render2DEvent) {
        val cpass = CompassUtil(325F, 325F, 1F, 2, true)
        val sc = ScaledResolution(mc)
        cpass.draw(sc)
    }
}