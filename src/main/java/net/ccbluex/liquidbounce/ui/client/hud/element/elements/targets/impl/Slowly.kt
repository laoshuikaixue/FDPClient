package net.ccbluex.liquidbounce.ui.client.hud.element.elements.targets.impl

import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Target2
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.targets.TargetStyle
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.player.EntityPlayer

class Slowly(inst: Target2): TargetStyle("Slowly", inst) {

    override fun drawTarget(entity: EntityPlayer) {
        val font = Fonts.minecraftFont
        val healthString = "${decimalFormat2.format(entity.health)} ❤"
        val length = 60.coerceAtLeast(font.getStringWidth(entity.name)).coerceAtLeast(font.getStringWidth(healthString)).toFloat() + 10F

        RenderUtils.drawRect(0F, 0F, 32F + length, 36F, targetInstance.bgColor.rgb)

        if (mc.netHandler.getPlayerInfo(entity.uniqueID) != null) 
            drawHead(mc.netHandler.getPlayerInfo(entity.uniqueID).locationSkin, 1, 1, 30, 30, 1F - targetInstance.getFadeProgress())

        font.drawStringWithShadow(entity.name, 33F, 2F, getColor(-1).rgb)
        font.drawStringWithShadow(healthString, length + 31F - font.getStringWidth(healthString).toFloat(), 22F, targetInstance.barColor.rgb)

        updateAnim(entity.health)

        RenderUtils.drawRect(0F, 32F, (easingHealth / entity.maxHealth.toFloat()).coerceIn(0F, entity.maxHealth.toFloat()) * (length + 32F), 36F, targetInstance.barColor.rgb)
    }

    override fun handleBlur(entity: EntityPlayer) {
        val font = Fonts.minecraftFont
        val healthString = "${decimalFormat2.format(entity.health)} ❤"
        val length = 60.coerceAtLeast(font.getStringWidth(entity.name)).coerceAtLeast(font.getStringWidth(healthString)).toFloat() + 10F

        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        RenderUtils.quickDrawRect(0F, 0F, 32F + length, 36F)
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
    }

    override fun getBorder(entity: EntityPlayer?): Border? {
        entity ?: return Border(0F, 0F, 102F, 36F)
        val font = Fonts.minecraftFont
        val healthString = "${decimalFormat2.format(entity.health)} ❤"
        val length = 60.coerceAtLeast(font.getStringWidth(entity.name)).coerceAtLeast(font.getStringWidth(healthString)).toFloat() + 10F
        return Border(0F, 0F, 32F + length, 36F)
    }

}