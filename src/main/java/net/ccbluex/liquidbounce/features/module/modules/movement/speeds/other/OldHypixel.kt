/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/laoshuikaixue/FDPClient
 */

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other
import net.ccbluex.liquidbounce.event.MotionEvent
import net.ccbluex.liquidbounce.event.MoveEvent
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.minecraft.block.Block
import kotlin.math.max
import kotlin.math.sqrt

class OldHypixel : SpeedMode("OldHypixel") {
    var level = 1
    var lastDist = 0.0
    var speed2 = 0.0
    var iscolod = false
    var isJump = false
    override fun onEnable() {
        mc.timer.timerSpeed = 1.0F
    }

    override fun onDisable() {
        speed2 = MovementUtils.getBaseMoveSpeed()
        level = 0
    }

    override fun onMotion(event: MotionEvent) {
        val xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX
        val zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ
        lastDist = sqrt(xDist * xDist + zDist * zDist)
    }

    override fun onUpdate() {
        ++level
        if (level == 2 && mc.thePlayer.onGround && MovementUtils.isMoving()) {
            if (canJump()) {
                mc.thePlayer.jump()
            } else {
                isJump = true
            }
        } else {
            isJump = false
        }
    }

    override fun onMove(event: MoveEvent) {
        if (level > 0 && !mc.thePlayer.isInWater) {
            if (level == 2 && mc.thePlayer.onGround && MovementUtils.isMoving()) {
                if (!canJump()) {
                    mc.thePlayer.motionY = 0.41888888688698 + (MovementUtils.getJumpEffect().toFloat() * 0.1f).toDouble()
                    event.y = mc.thePlayer.motionY
                }
            }
            if (level > 3 && mc.thePlayer.isCollidedVertically) {
                level = 0
                lastDist = 0.0
            }
            speed2 = max(getHypixelBest(speed2, level), MovementUtils.getBaseMoveSpeed())
            getHypixelBest(speed2, level) - 1.688E-4
        } else {
            level = 0
        }
        if(!MovementUtils.isMoving())
            level = 0
    }

    private fun getHypixelBest(speed: Double, T: Int): Double {
        var speed = speed
        val base = MovementUtils.getBaseMoveSpeed()
        var slow = false
        val fe = 1.0E-18
        if (T == 1) {
            speed = 0.025 - fe
        } else if (mc.thePlayer.onGround && MovementUtils.isMoving() && T == 2) {
            speed *= 2.149
        } else if (T == 3) {
            val str = 0.73995
            val strafe = str * (lastDist - base)
            speed = lastDist - (strafe + fe)
            iscolod = true
        } else {
            if (T == 2 && mc.thePlayer.fallDistance.toDouble() > 0.0) {
                slow = true
            }
            level = 1
            speed = lastDist - lastDist / 159.0
        }
        speed = max(speed - if (slow) lastDist * speed * 0.0149336 else 0.0049336 * lastDist, base)
        return speed
    }
    private fun canJump(): Boolean {
        if (mc.thePlayer.posY >= 0.0) {
            var off = 0
            while (off < mc.thePlayer.posY.toInt() + 2) {
                if (!(Block.blockRegistry.equals("soul_sand") || Block.blockRegistry.equals("soul_sand"))) {
                    return false
                }
                off += 2
            }
        }
        return true
    }
}