package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other

import io.netty.util.internal.ThreadLocalRandom
import net.ccbluex.liquidbounce.event.JumpEvent
import net.ccbluex.liquidbounce.event.MoveEvent
import net.ccbluex.liquidbounce.event.PreUpdateEvent
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.MovementUtils.isMoving
import net.ccbluex.liquidbounce.utils.MovementUtils.setMotion2
import net.ccbluex.liquidbounce.utils.MovementUtils.strafe
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.FloatValue
import net.minecraft.potion.Potion

class Hypixel2 : SpeedMode("Hypixel2") {
    private val HypixelYValue = FloatValue("HypixelY", 0f, 0f, 4f)
    private val HypixelTimerValue = FloatValue("Max-GameSpeed", 2.3f, 0.2f, 5f)
    private val HypixelDealyTimerValue = FloatValue("Min-GameSpeed", 0.7f, 0.2f, 5f)
    private var stage = false
    var stair = 0.0
    private var legitJump = false
    private val timer = MSTimer()
    override fun onEnable() {
        legitJump = true
    }

    fun onMotion() {
        if (stair > 0.0) {
            stair -= 0.25
            return
        }
        if (isMoving()) {
            if (mc.thePlayer.isInWater) return
            if (stage) {
                if (mc.thePlayer.fallDistance <= 0.1) {
                    mc.timer.timerSpeed = HypixelTimerValue.get()
                }
                if (timer.hasTimePassed(650)) {
                    timer.reset()
                    stage = !stage
                }
            } else {
                if (mc.thePlayer.fallDistance > 0.1 && mc.thePlayer.fallDistance < 1.3) {
                    mc.timer.timerSpeed = HypixelDealyTimerValue.get()
                }
                if (timer.hasTimePassed(400)) {
                    timer.reset()
                    stage = !stage
                }
            }
            if (mc.thePlayer.onGround) {
                if (legitJump) {
                    mc.thePlayer.motionY = MovementUtils.getJumpBoostModifier(HypixelYValue.get().toDouble(), true)
                    legitJump = false
                    return
                }
                mc.thePlayer.motionY = MovementUtils.getJumpBoostModifier(HypixelYValue.get().toDouble(), true)
                strafe(MovementUtils.getSpeedEffect() * 0.1f)
            }
        } else {
            legitJump = true
        }
    }

    override fun onUpdate() {}
    fun onJump(event: JumpEvent) {
        if (mc.thePlayer != null) event.cancelEvent()
    }

    override fun onMove(event: MoveEvent) {
        if (isMoving() && mc.thePlayer.isCollidedHorizontally) {
            setMotion2(event, Companion.getBaseMoveSpeed(0.28))
        }
    }

    fun onPreUpdate(event: PreUpdateEvent) {
        if (isMoving() && mc.thePlayer.isCollidedVertically && mc.thePlayer.onGround && mc.theWorld.getCollidingBoundingBoxes(
                mc.thePlayer, mc.thePlayer.entityBoundingBox.offset(0.0, 0.5, 0.0)
            ).isEmpty()
        ) {
            event.y = event.y + ThreadLocalRandom.current().nextFloat() / 1000.0
            event.onGround = true
        }
    }

    companion object {
        fun getBaseMoveSpeed(lllllIlIIlIIll: Double): Double {
            var lllllIlIIlIIlI = lllllIlIIlIIll
            if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                lllllIlIIlIIlI *= 1.0 + 0.2 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1).toDouble()
            }
            return lllllIlIIlIIlI
        }
    }
}