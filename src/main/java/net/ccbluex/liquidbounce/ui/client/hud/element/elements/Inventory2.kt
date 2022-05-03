package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.ColorUtils
import net.ccbluex.liquidbounce.utils.render.GLUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import java.awt.Color

/**
 * CustomHUD Armor element
 *
 * Shows a horizontal display of current armor
 */
@ElementInfo(name = "Inventory2", blur = true)
class Inventory2(x: Double = 10.0, y: Double = 10.0, scale: Float = 1F) : Element(x, y, scale) {
    private val lineValue = BoolValue("Line", true)
    private val rainbowList = ListValue("Rainbow", arrayOf("Off", "CRainbow", "Sky", "LiquidSlowly", "Fade"), "LiquidSlowly")
    private val saturationValue = FloatValue("Saturation", 0.3f, 0f, 1f)
    private val brightnessValue = FloatValue("Brightness", 1f, 0f, 1f)
    private val cRainbowSecValue = IntegerValue("Seconds", 2, 1, 10)
    private val distanceValue = IntegerValue("Line-Distance", 14, 0, 400)
    private val gradientAmountValue = IntegerValue("Gradient-Amount", 35, 1, 50)
    private val colorRedValue = IntegerValue("Red", 0, 0, 255)
    private val colorGreenValue = IntegerValue("Green", 160, 0, 255)
    private val colorBlueValue = IntegerValue("Blue", 255, 0, 255)

    /**
     * Draw element
     */
    override fun drawElement(partialTicks: Float): Border? {
            RenderUtils.drawRect(8f, 25f, 8f + 165f, 30f + 65f ,Color(0, 0, 0, 120).rgb)
            Fonts.fontSFUI40.drawString("Inventory", 11, 29, Color(0xFFFFFF).rgb)

        if (lineValue.get()) {
            for (i in 0..(gradientAmountValue.get()-1)) {
                val color = Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get()).rgb
                val rainbowType = rainbowList.get()
                RenderUtils.drawGradientSideways(
                    8.0, 25.0, 8f + 165f.toDouble(), 26.0,
                    when (rainbowType) {
                        "CRainbow" -> RenderUtils.getRainbowOpaque(cRainbowSecValue.get(), saturationValue.get(), brightnessValue.get(), i * distanceValue.get())
                        "Sky" -> RenderUtils.SkyRainbow(i * distanceValue.get(), saturationValue.get(), brightnessValue.get())
                        "LiquidSlowly" -> ColorUtils.LiquidSlowly(System.nanoTime(), i * distanceValue.get(), saturationValue.get(), brightnessValue.get())!!.rgb
                        "Fade" -> ColorUtils.fade(Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get()), i * distanceValue.get(), 100).rgb
                        else -> color
                    },
                    when (rainbowType) {
                        "CRainbow" -> RenderUtils.getRainbowOpaque(cRainbowSecValue.get(), saturationValue.get(), brightnessValue.get(), (i + 1) * distanceValue.get())
                        "Sky" -> RenderUtils.SkyRainbow((i + 1) * distanceValue.get(), saturationValue.get(), brightnessValue.get())
                        "LiquidSlowly" -> ColorUtils.LiquidSlowly(System.nanoTime(), (i + 1) * distanceValue.get(), saturationValue.get(), brightnessValue.get())!!.rgb
                        "Fade" -> ColorUtils.fade(Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get()), (i + 1) * distanceValue.get(), 100).rgb
                        else -> color
                    })
            }
        }

        var itemX: Int = 10
        var itemY: Int = 40
        var airs = 0
        for (i in mc.thePlayer.inventory.mainInventory.indices) {
            if (i < 9) continue
            val stack = mc.thePlayer.inventory.mainInventory[i]
            if (stack == null) {
                airs++
            }
            val res = ScaledResolution(mc)
            GL11.glPushMatrix()
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
            if (mc.theWorld != null) GLUtils.enableGUIStandardItemLighting()
            GlStateManager.pushMatrix()
            GlStateManager.disableAlpha()
            GlStateManager.clear(256)
            mc.renderItem.zLevel = -150.0f
            mc.renderItem.renderItemAndEffectIntoGUI(stack, itemX, itemY)
            mc.renderItem.renderItemOverlays(mc.fontRendererObj, stack, itemX, itemY)
            mc.renderItem.zLevel = 0.0f
            GlStateManager.disableBlend()
            GlStateManager.scale(0.5, 0.5, 0.5)
            GlStateManager.disableDepth()
            GlStateManager.disableLighting()
            GlStateManager.enableDepth()
            GlStateManager.scale(2.0f, 2.0f, 2.0f)
            GlStateManager.enableAlpha()
            GlStateManager.popMatrix()
            GL11.glPopMatrix()
            if (itemX < 152) {
                itemX += 18
            } else {
                itemX = 10
                itemY += 18
            }
        }

        if (airs == 27) {
            Fonts.font40.drawString("Your inventory is empty...", 28, 56, Color(255, 255, 255).rgb)
        }
        return Border(8f, 30f + 10f, 8f + 163f, 30f + 65f)
    }
}
