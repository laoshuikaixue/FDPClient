package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold
import net.ccbluex.liquidbounce.features.module.modules.world.Timer
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import kotlin.math.max

class Hypixel2: SpeedMode("Hypixel2") {
    private val timerValue = BoolValue("UseTimer", true)
    private val smoothStrafe = BoolValue("SmoothStrafe", true)
    private val customSpeedValue = FloatValue("StrSpeed", 0.42f, 0.2f, 2f)
    private val motionYValue = FloatValue("MotionY", 0.42f, 0f, 2f)

    @EventTarget
    fun onJump(event: JumpEvent) {
        if (mc.thePlayer != null && MovementUtils.isMoving())
            event.cancelEvent()
    }

    override fun onMotion(event: MotionEvent) {
        val thePlayer = mc.thePlayer ?: return
        val scaffoldModule = LiquidBounce.moduleManager.getModule(Scaffold::class.java)
        val timer = LiquidBounce.moduleManager.getModule(Timer::class.java)

        if (MovementUtils.isMoving()) {
            mc.gameSettings.keyBindJump.pressed = false
            when {
                thePlayer.onGround && thePlayer.isCollidedVertically -> {
                    thePlayer.motionY = MovementUtils.getJumpBoostModifier(if (scaffoldModule!!.state) 0.41999 else motionYValue.get().toDouble(), true)

                    if (scaffoldModule.state) {
                        MovementUtils.strafe(0.37F)
                    } else {
                        MovementUtils.strafe((max(customSpeedValue.get() + MovementUtils.getSpeedEffect() * 0.1, MovementUtils.getBaseMoveSpeed2(0.2873))).toFloat())
                    }
                }

                else -> {
                    if (!timer!!.state && timerValue.get())
                        mc.timer.timerSpeed = 1.07f

                    MovementUtils.setMotion3(MovementUtils.getSpeed().toDouble(), smoothStrafe.get())
                }
            }
        } else {
            thePlayer.motionX *= 0.0
            thePlayer.motionZ *= 0.0
        }
    }

    override fun onMove(event: MoveEvent) {
        val thePlayer = mc.thePlayer ?: return
        LiquidBounce.moduleManager.getModule(Speed::class.java)!!

        if (MovementUtils.isMoving()) {
            when {
                thePlayer.isCollidedHorizontally -> {
                    MovementUtils.setMotion3(event, MovementUtils.getBaseMoveSpeed2(0.258), 1.0, smoothStrafe.get())
                }
            }
        }
    }
}