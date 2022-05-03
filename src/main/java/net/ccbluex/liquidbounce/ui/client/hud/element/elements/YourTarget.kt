/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/laoshuikaixue/FDPClient
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.Palette
import net.ccbluex.liquidbounce.utils.render.GLUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtil
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemArmor
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemTool
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.math.BigDecimal
import kotlin.math.abs
import kotlin.math.pow

@ElementInfo(name = "YourTarget", blur = true)
class YourTarget : Element() {
    private val fadeSpeed = FloatValue("FadeSpeed", 2F, 1F, 9F)
    private val redValue = IntegerValue("Red", 255, 0, 255)
    private val greenValue = IntegerValue("Green", 255, 0, 255)
    private val blueValue = IntegerValue("Blue", 255, 0, 255)
    private val gredValue = IntegerValue("GradientRed", 255, 0, 255)
    private val ggreenValue = IntegerValue("GradientGreen", 255, 0, 255)
    private val gblueValue = IntegerValue("GradientBlue", 255, 0, 255)
    
    private var easingHealth: Float = 0F
    private var lastTarget: Entity? = null
    val counter1 = intArrayOf(50)
    val counter2 = intArrayOf(100)
    override fun drawElement(partialTicks: Float): Border {
        val target:Entity
        if (mc.currentScreen is GuiChat || mc.currentScreen is GuiHudDesigner) {
            target = mc.thePlayer
        } else {
            target = (LiquidBounce.moduleManager[KillAura::class.java] as KillAura).target!!
        }
        if (target is EntityPlayer) {
            if (target != lastTarget || easingHealth < 0 || easingHealth > target.maxHealth ||
                abs(easingHealth - target.health) < 0.01) {
                easingHealth = target.health
            }
            val width = (38 + Fonts.font35.getStringWidth(target.name))
                .coerceAtLeast(118)
                .toFloat()

            // Draw rect box
            RenderUtil.autoExhibition(-1.5, 0.0, width.toDouble(), 42.0, 3.0)
            val customColor = Color(redValue.get(), greenValue.get(), blueValue.get(), 255)
            val customColor1 = Color(gredValue.get(), ggreenValue.get(), gblueValue.get(), 255)
            val customColor2 = Color(150, 150, 150, 100)
            if (easingHealth > target.health)
                RenderUtils.drawRect(1.5F, 34F, (easingHealth / target.maxHealth) * width,
                    42F, customColor2.rgb)
            counter1[0] += 1
            counter2[0] += 1
            counter1[0] = counter1[0].coerceIn(0, 50)
            counter2[0] = counter2[0].coerceIn(0, 100)
            if (customColor != null) {
                RenderUtil.drawGradientSideways(1.5, 34.0, ((target.health / target.maxHealth) * width).toDouble(),
                    42.0, Palette.fade2(customColor,counter1[0], Fonts.minecraftFont.FONT_HEIGHT).rgb,
                    Palette.fade2(customColor1, counter2[0], Fonts.minecraftFont.FONT_HEIGHT).rgb)
                Fonts.minecraftFont.drawStringWithShadow(BigDecimal((target.health / target.maxHealth * 100).toDouble()).setScale(1, BigDecimal.ROUND_HALF_UP).toString() + "%", width / 2F - 10, 34F, Color.white.rgb)
            }


            easingHealth += ((target.health - easingHealth) / 2.0F.pow(10.0F - fadeSpeed.get())) * RenderUtil.deltaTime
            // Draw info
            val playerInfo = mc.netHandler.getPlayerInfo(target.uniqueID)
            if (playerInfo != null) {
                Fonts.minecraftFont.drawString(target.name, 36, 4, 0xffffff)
                //  GlStateManager.scale(0.5,0.5,0.5);
                renderArmor((target as EntityPlayer?)!!)
                // Draw head
                val locationSkin = playerInfo.locationSkin
                drawHead(locationSkin, 30, 30)
            }
        }

        lastTarget = target
        val width2 = (38 + Fonts.font35.getStringWidth(mc.thePlayer.name))
            .coerceAtLeast(118)
            .toFloat()
        return Border((-1.5).toFloat(), 0.0F, width2, 42.0F)
    }

    private fun renderArmor(player: EntityPlayer) {
        var armourStack: ItemStack?
        var renderStack = player.inventory.armorInventory
        val length = renderStack.size
        var xOffset = 36
        for (aRenderStack in renderStack) {
            armourStack = aRenderStack
        }
        if (player.heldItem != null) {
            val stock = player.heldItem.copy()
            if (stock.hasEffect() && (stock.item is ItemTool || stock.item is ItemArmor)) {
                stock.stackSize = 1
            }
            this.renderItemStack(stock, xOffset, 14)
            xOffset += 16
        }
        renderStack = player.inventory.armorInventory
        for (index in 3 downTo 0) {
            armourStack = renderStack[index]
            if (armourStack == null) continue
            this.renderItemStack(armourStack, xOffset, 14)
            xOffset += 16
        }
    }

    private fun renderItemStack(stack: ItemStack, x: Int, y: Int) {
        GL11.glPushMatrix()
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        if (mc.theWorld != null) {
            GLUtils.enableGUIStandardItemLighting()
        }
        GlStateManager.pushMatrix()
        GlStateManager.disableAlpha()
        GlStateManager.clear(256)
        mc.renderItem.zLevel = -150.0f
        mc.renderItem.renderItemAndEffectIntoGUI(stack, x, y)
        mc.renderItem.renderItemOverlays(mc.fontRendererObj, stack, x, y)
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
    }

    private fun drawHead(skin: ResourceLocation, width: Int, height: Int) {
        GL11.glColor4f(1F, 1F, 1F, 1F)
        mc.textureManager.bindTexture(skin)
        Gui.drawScaledCustomSizeModalRect(2, 2.7F.toInt(), 8F, 8F, 8, 8, width, height,
            64F, 64F)
    }
}