package net.ccbluex.liquidbounce.ui.client

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.launch.data.GuiLaunchOptionSelectMenu
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.GuiYesNoCallback
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.util.EnumChatFormatting
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.io.IOException

class NewGuiWelcome : GuiScreen(), GuiYesNoCallback {
    var curAlpha = 255
    var alpha = 255
    override fun initGui() {}

    @Throws(IOException::class)
    override fun actionPerformed(p_actionPerformed_1_: GuiButton) {
        super.actionPerformed(p_actionPerformed_1_)
    }

    override fun drawScreen(p_drawScreen_1_: Int, p_drawScreen_2_: Int, p_drawScreen_3_: Float) {
        drawBackground(0)
        drawWelcome(p_drawScreen_1_, p_drawScreen_2_, p_drawScreen_3_)
        if (curAlpha <= 0) {
            mc.displayGuiScreen(GuiLaunchOptionSelectMenu())
        }
        super.drawScreen(p_drawScreen_1_, p_drawScreen_2_, p_drawScreen_3_)
    }

    @Throws(IOException::class)
    override fun mouseClicked(p_mouseClicked_1_: Int, p_mouseClicked_2_: Int, p_mouseClicked_3_: Int) {
        super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_2_, p_mouseClicked_3_)
        alpha = 0
    }

    fun drawWelcome(mouseX: Int, mouseY: Int, partialTicks: Float) {
        val Height: Int
        Height = ScaledResolution(mc).scaledHeight
        val Width: Int
        Width = ScaledResolution(mc).scaledWidth
        var text: String
        val Scale: Float
        if (curAlpha > alpha) curAlpha -= 20
        if (curAlpha < alpha) curAlpha += 20
        if (alpha - curAlpha < 20 && alpha - curAlpha > -20) curAlpha = alpha
        if (curAlpha == 0) return
        GL11.glPushMatrix()
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        drawRect(0, 0, Width, Height, Color(0, 0, 0, Math.min(160, curAlpha)).rgb)
        text =
            "Hello " + EnumChatFormatting.GREEN + mc.session.username + EnumChatFormatting.WHITE + " Welcome back to ${LiquidBounce.CLIENT_NAME}-${LiquidBounce.L}${LiquidBounce.S}"
        Scale = 2.0f
        GL11.glScaled(Scale.toDouble(), Scale.toDouble(), Scale.toDouble())
        Fonts.fontComfortaa40.drawString(
            text, (Width / 2f - Fonts.fontComfortaa40.getStringWidth(text)) / Scale,
            (Height / 2 - 9) / Scale, Color.WHITE.rgb
        )
        GL11.glScaled(1.0 / Scale, 1.0 / Scale, 1.0 / Scale)
        text = "Click here to continue..."
        Fonts.fontComfortaa40.drawString(
            text, Width / 2.0f - Fonts.fontComfortaa40.getStringWidth(text) / 2.0f,
            (Height / 2 + 11).toFloat(), Color.WHITE.rgb
        )
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1f)
        GL11.glPopMatrix()
    }
}