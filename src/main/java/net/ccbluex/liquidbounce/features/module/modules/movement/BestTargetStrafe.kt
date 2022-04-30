/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/laoshuikaixue/FDPClient
 */
package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.client.HUD
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.RotationUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.minecraft.entity.EntityLivingBase
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.cos
import kotlin.math.sin

@ModuleInfo(name = "BestTargetStrafe", category = ModuleCategory.MOVEMENT)
class BestTargetStrafe : Module() {
    private val radiusValue = FloatValue("StrafeRadius", 2.0f, 0.1f, 4.0f)
    private val forwardRadius = FloatValue("ForwardRadius", 2.0f, 0.1f, 4.0f)
    private val custom = BoolValue("customWard", false)
    private val forward = FloatValue("Forward", 0.0f, 0.0f, 1.0f)
    private val backward = FloatValue("Backward", 0.0f, 0.0f, 1.0f)

    private val holdSpaceValue = BoolValue("HoldSpace", false)
    private val onlySpeedValue = BoolValue("OnlySpeed", true)
    private val onlyflyValue = BoolValue("keyFly", false)
    private val onlyGroundValue = BoolValue("OnlyGround", false)
    private val renderValue = BoolValue("Render", true)
    private var direction = true
    private var yaw = 0f

    @EventTarget
    fun onMotion(event: MotionEvent) {
        if (event.eventState === EventState.PRE) {
            if (mc.gameSettings.keyBindLeft.isKeyDown) {
                direction = true
            } else if (mc.gameSettings.keyBindRight.isKeyDown) {
                direction = false
            } else if (mc.thePlayer.isCollidedHorizontally) {
                direction = !direction
            }
        }
    }

    @EventTarget
    fun strafe(event: MoveEvent) {
        val target = LiquidBounce.combatManager.target
        val strafe = if(mc.thePlayer.getDistanceToEntity(target) <= forwardRadius.get()) if (direction) 1.0 else -1.0 else 0.0
        val testward = if (mc.thePlayer.getDistanceToEntity(target) <= radiusValue.get()) if(custom.get()) -backward.get().toDouble() else 0.0 else if(custom.get()) forward.get().toDouble() else 1.0

        if (canStrafe(target)) {
            if(mc.thePlayer.onGround || !onlyGroundValue.get()) {
                yaw = RotationUtils.getRotationsEntity(target).yaw
            }
            MovementUtils.setSpeed(event, MovementUtils.getSpeed().toDouble(), yaw, strafe, testward)
        }
    }

    @EventTarget
    fun onRender3D(event: Render3DEvent) {
        val target = LiquidBounce.combatManager.target
        if (renderValue.get() && canStrafe(target)) {
            GL11.glDisable(3553)
            GL11.glEnable(2848)
            GL11.glEnable(2881)
            GL11.glEnable(2832)
            GL11.glEnable(3042)
            GL11.glBlendFunc(770, 771)
            GL11.glHint(3154, 4354)
            GL11.glHint(3155, 4354)
            GL11.glHint(3153, 4354)
            GL11.glDisable(2929)
            GL11.glDepthMask(false)
            GL11.glLineWidth(3f)

            GL11.glBegin(3)
            val x = target!!.lastTickPosX + (target.posX - target.lastTickPosX) * event.partialTicks - mc.renderManager.viewerPosX
            val y = target.lastTickPosY + (target.posY - target.lastTickPosY) * event.partialTicks - mc.renderManager.viewerPosY
            val z = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * event.partialTicks - mc.renderManager.viewerPosZ
            val radius = radiusValue.get()
            for (i in 0..360 step 30) {
                RenderUtils.glColor(Color.getHSBColor(if (i < 180) { HUD.rainbowStartValue.get() + (HUD.rainbowStopValue.get() - HUD.rainbowStartValue.get()) * (i / 180f) } else { HUD.rainbowStartValue.get() + (HUD.rainbowStopValue.get() - HUD.rainbowStartValue.get()) * (-(i-360) / 180f) }, 0.7f, 1.0f))
                GL11.glVertex3d(x - sin(i * Math.PI / 180F) * radius, y, z + cos(i * Math.PI / 180F) * radius)
            }
            GL11.glEnd()

            GL11.glDepthMask(true)
            GL11.glEnable(2929)
            GL11.glDisable(2848)
            GL11.glDisable(2881)
            GL11.glEnable(2832)
            GL11.glEnable(3553)
        }
    }

    private fun canStrafe(target: EntityLivingBase?): Boolean {
        return target != null && (!holdSpaceValue.get() || mc.thePlayer.movementInput.jump) && ((!onlySpeedValue.get() || LiquidBounce.moduleManager[Speed::class.java]!!.state) || (onlyflyValue.get() && LiquidBounce.moduleManager[Fly::class.java]!!.state))
    }
}
