package net.ccbluex.liquidbounce.utils

import net.ccbluex.liquidbounce.event.MoveEvent
import net.minecraft.block.*
import net.minecraft.client.Minecraft
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.Potion
import net.minecraft.util.*
import java.util.*

class MoveUtils {
    fun doStrafe(speed: Double, yaw: Double) {
        mc.thePlayer.motionX = -Math.sin(yaw) * speed
        mc.thePlayer.motionZ = Math.cos(yaw) * speed
    }

    companion object {
        private val mc = Minecraft.getMinecraft()

        @JvmOverloads
        fun defaultSpeed(entity: EntityLivingBase = mc.thePlayer, effectBoost: Double = 0.2): Double {
            var baseSpeed = 0.2873
            if (entity.isPotionActive(Potion.moveSpeed)) {
                val amplifier = entity.getActivePotionEffect(Potion.moveSpeed).amplifier
                baseSpeed *= 1.0 + effectBoost * (amplifier + 1)
            }
            return baseSpeed
        }

        fun strafe(e: MoveEvent) {
            strafe(e, speed.toDouble())
        }

        @JvmOverloads
        fun strafe(d: Double = speed.toDouble()) {
            if (!isMoving) return
            val yaw = direction
            mc.thePlayer.motionX = -Math.sin(yaw) * d
            mc.thePlayer.motionZ = Math.cos(yaw) * d
        }

        fun strafe(e: MoveEvent, d: Double) {
            if (!isMoving) return
            val yaw = direction
            e.setX(-Math.sin(yaw) * d.also { mc.thePlayer.motionX = it })
            e.setZ(Math.cos(yaw) * d.also { mc.thePlayer.motionZ = it })
        }

        @JvmOverloads
        fun doStrafe(speed: Double = Companion.speed.toDouble()) {
            if (!isMoving) return
            val yaw = getYaw(true)
            mc.thePlayer.motionX = -Math.sin(yaw) * speed
            mc.thePlayer.motionZ = Math.cos(yaw) * speed
        }

        val direction: Double
            get() {
                var rotationYaw = mc.thePlayer.rotationYaw
                if (mc.thePlayer.moveForward < 0f) rotationYaw += 180f
                var forward = 1f
                if (mc.thePlayer.moveForward < 0f) forward = -0.5f else if (mc.thePlayer.moveForward > 0f) forward =
                    0.5f
                if (mc.thePlayer.moveStrafing > 0f) rotationYaw -= 90f * forward
                if (mc.thePlayer.moveStrafing < 0f) rotationYaw += 90f * forward
                return Math.toRadians(rotationYaw.toDouble())
            }

        fun getMovementDirection(
            forward: Float,
            strafing: Float,
            yaw: Float
        ): Float {
            var yaw = yaw
            if (forward == 0.0f && strafing == 0.0f) return yaw
            val reversed = forward < 0.0f
            val strafingYaw = 90.0f *
                    if (forward > 0.0f) 0.5f else if (reversed) -0.5f else 1.0f
            if (reversed) yaw += 180.0f
            if (strafing > 0.0f) yaw -= strafingYaw else if (strafing < 0.0f) yaw += strafingYaw
            return yaw
        }

        val isOverVoid: Boolean
            get() {
                var posY = mc.thePlayer.posY
                while (posY > 0.0) {
                    if (mc.theWorld.getBlockState(
                            BlockPos(
                                mc.thePlayer.posX,
                                posY,
                                mc.thePlayer.posZ
                            )
                        ).block !is BlockAir
                    ) {
                        return false
                    }
                    posY--
                }
                return true
            }
        val isMoving: Boolean
            get() = mc.thePlayer != null && (mc.thePlayer.movementInput.moveForward != 0f || mc.thePlayer.movementInput.moveStrafe != 0f)
        val speed: Float
            get() = Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ)
                .toFloat()
        val baseMoveSpeed: Double
            get() {
                var baseSpeed = 0.2875
                if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) baseSpeed *= 1.0 + 0.2 * (mc.thePlayer.getActivePotionEffect(
                    Potion.moveSpeed
                ).amplifier + 1)
                return baseSpeed
            }
        val frictionValues: MutableList<Double> = ArrayList()
        fun calculateFriction(moveSpeed: Double, lastDist: Double, baseMoveSpeedRef: Double): Double {
            frictionValues.clear()
            frictionValues.add(lastDist - lastDist / 159.9999985)
            frictionValues.add(lastDist - (moveSpeed - lastDist) / 33.3)
            val materialFriction =
                if (mc.thePlayer.isInWater) 0.8899999856948853 else if (mc.thePlayer.isInLava) 0.5350000262260437 else 0.9800000190734863
            frictionValues.add(lastDist - baseMoveSpeedRef * (1.0 - materialFriction))
            return Collections.min(frictionValues as Collection<Double>)
        }

        fun getYaw(strafing: Boolean): Double {
            var rotationYaw = mc.thePlayer.rotationYawHead
            var forward = 1f
            val moveForward = mc.thePlayer.movementInput.moveForward.toDouble()
            val moveStrafing = mc.thePlayer.movementInput.moveStrafe.toDouble()
            val yaw = mc.thePlayer.rotationYaw
            if (moveForward < 0) {
                rotationYaw += 180f
            }
            if (moveForward < 0) {
                forward = -0.5f
            } else if (moveForward > 0) {
                forward = 0.5f
            }
            if (moveStrafing > 0) {
                rotationYaw -= 90f * forward
            } else if (moveStrafing < 0) {
                rotationYaw += 90f * forward
            }
            return Math.toRadians(rotationYaw.toDouble())
        }

        fun checkPositionValidity(vec3: Vec3?): Boolean {
            val pos = BlockPos(vec3)
            return if (isBlockSolid(pos) || isBlockSolid(pos.add(0, 1, 0))) {
                false
            } else isSafeToWalkOn(pos.add(0, -1, 0))
        }

        private fun isBlockSolid(pos: BlockPos): Boolean {
            val block = mc.theWorld.getBlockState(pos).block
            return block is BlockSlab || block is BlockStairs || block is BlockCactus || block is BlockChest || block is BlockEnderChest || block is BlockSkull || block is BlockPane || block is BlockFence || block is BlockWall || block is BlockGlass || block is BlockPistonBase || block is BlockPistonExtension || block is BlockPistonMoving || block is BlockStainedGlass || block is BlockTrapDoor
        }

        private fun isSafeToWalkOn(pos: BlockPos): Boolean {
            val block = mc.theWorld.getBlockState(pos).block
            return block !is BlockFence && block !is BlockWall
        }

        fun setMotion(speed: Double) {
            setMotion(speed, mc.thePlayer.rotationYaw)
        }

        fun setMotion(e: MoveEvent, speed: Double, yaw: Float) {
            var yaw = yaw
            var forward = mc.thePlayer.movementInput.moveForward.toDouble()
            var strafe = mc.thePlayer.movementInput.moveStrafe.toDouble()
            if (forward == 0.0 && strafe == 0.0) {
                e.x = 0.0
                mc.thePlayer.motionX = e.x
                e.z = 0.0
                mc.thePlayer.motionZ = e.z
            } else {
                if (forward != 0.0) {
                    if (strafe > 0.0) {
                        yaw += (if (forward > 0.0) -45 else 45).toFloat()
                    } else if (strafe < 0.0) {
                        yaw += (if (forward > 0.0) 45 else -45).toFloat()
                    }
                    strafe = 0.0
                    if (forward > 0.0) {
                        forward = 1.0
                    } else if (forward < 0.0) {
                        forward = -1.0
                    }
                }
                e.x = forward * speed * Math.cos(Math.toRadians((yaw + 90.0f).toDouble())) + strafe * speed * Math.sin(
                    Math.toRadians((yaw + 90.0f).toDouble())
                )
                mc.thePlayer.motionX = e.x
                e.z = forward * speed * Math.sin(Math.toRadians((yaw + 90.0f).toDouble())) - strafe * speed * Math.cos(
                    Math.toRadians((yaw + 90.0f).toDouble())
                )
                mc.thePlayer.motionZ = e.z
            }
        }

        fun setMotion(e: MoveEvent?, speed: Double) {
            e?.let { setMotion(it, speed, mc.thePlayer.rotationYaw) }
        }

        fun setMotion(speed: Double, yaw: Float) {
            var yaw = yaw
            var forward = mc.thePlayer.movementInput.moveForward.toDouble()
            var strafe = mc.thePlayer.movementInput.moveStrafe.toDouble()
            if (forward == 0.0 && strafe == 0.0) {
                mc.thePlayer.motionX = 0.0
                mc.thePlayer.motionZ = 0.0
            } else {
                if (forward != 0.0) {
                    if (strafe > 0.0) {
                        yaw += (if (forward > 0.0) -45 else 45).toFloat()
                    } else if (strafe < 0.0) {
                        yaw += (if (forward > 0.0) 45 else -45).toFloat()
                    }
                    strafe = 0.0
                    if (forward > 0.0) {
                        forward = 1.0
                    } else if (forward < 0.0) {
                        forward = -1.0
                    }
                }
                mc.thePlayer.motionX =
                    forward * speed * Math.cos(Math.toRadians((yaw + 90.0f).toDouble())) + strafe * speed * Math.sin(
                        Math.toRadians((yaw + 90.0f).toDouble())
                    )
                mc.thePlayer.motionZ =
                    forward * speed * Math.sin(Math.toRadians((yaw + 90.0f).toDouble())) - strafe * speed * Math.cos(
                        Math.toRadians((yaw + 90.0f).toDouble())
                    )
            }
        }

        fun checkTeleport(x: Double, y: Double, z: Double, distBetweenPackets: Double): Boolean {
            val distx = mc.thePlayer.posX - x
            val disty = mc.thePlayer.posY - y
            val distz = mc.thePlayer.posZ - z
            val dist = Math.sqrt(mc.thePlayer.getDistanceSq(x, y, z))
            val nbPackets = (Math.round(dist / distBetweenPackets + 0.49999999999) - 1).toDouble()
            var xtp = mc.thePlayer.posX
            var ytp = mc.thePlayer.posY
            var ztp = mc.thePlayer.posZ
            var i = 1
            while (i < nbPackets) {
                val xdi = (x - mc.thePlayer.posX) / nbPackets
                xtp += xdi
                val zdi = (z - mc.thePlayer.posZ) / nbPackets
                ztp += zdi
                val ydi = (y - mc.thePlayer.posY) / nbPackets
                ytp += ydi
                val bb = AxisAlignedBB(xtp - 0.3, ytp, ztp - 0.3, xtp + 0.3, ytp + 1.8, ztp + 0.3)
                if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty()) {
                    return false
                }
                i++
            }
            return true
        }

        fun isOnGround(height: Double): Boolean {
            return if (!mc.theWorld.getCollidingBoundingBoxes(
                    mc.thePlayer,
                    mc.thePlayer.entityBoundingBox.offset(0.0, -height, 0.0)
                ).isEmpty()
            ) {
                true
            } else {
                false
            }
        }

        fun isOnGround(entity: Entity, height: Double): Boolean {
            return if (!mc.theWorld.getCollidingBoundingBoxes(
                    entity,
                    entity.entityBoundingBox.offset(0.0, -height, 0.0)
                ).isEmpty()
            ) {
                true
            } else {
                false
            }
        }

        val jumpEffect: Int
            get() = if (mc.thePlayer.isPotionActive(Potion.jump)) mc.thePlayer.getActivePotionEffect(Potion.jump).amplifier + 1 else 0
        val speedEffect: Int
            get() = if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 else 0

        fun getSpeedEffect(player: EntityPlayer): Int {
            return if (player.isPotionActive(Potion.moveSpeed)) player.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 else 0
        }

        fun getBlockUnderPlayer(inPlayer: EntityPlayer, height: Double): Block {
            return mc.theWorld.getBlockState(BlockPos(inPlayer.posX, inPlayer.posY - height, inPlayer.posZ)).block
        }

        fun getBlockAtPosC(x: Double, y: Double, z: Double): Block {
            val inPlayer: EntityPlayer = mc.thePlayer
            return mc.theWorld.getBlockState(BlockPos(inPlayer.posX + x, inPlayer.posY + y, inPlayer.posZ + z)).block
        }

        fun getRotationsBlock(block: BlockPos, face: EnumFacing): FloatArray {
            val x = block.x + 0.5 - mc.thePlayer.posX + face.frontOffsetX
                .toDouble() / 2
            val z = block.z + 0.5 - mc.thePlayer.posZ + face.frontOffsetZ
                .toDouble() / 2
            val y = block.y + 0.5
            val d1 = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - y
            val d3 = MathHelper.sqrt_double(x * x + z * z).toDouble()
            var yaw = (Math.atan2(z, x) * 180.0 / Math.PI).toFloat() - 90.0f
            val pitch = (Math.atan2(d1, d3) * 180.0 / Math.PI).toFloat()
            if (yaw < 0.0f) {
                yaw += 360f
            }
            return floatArrayOf(yaw, pitch)
        }

        val isBlockAboveHead: Boolean
            get() {
                val bb = AxisAlignedBB(
                    mc.thePlayer.posX - 0.3, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ + 0.3,
                    mc.thePlayer.posX + 0.3, mc.thePlayer.posY + 2.5, mc.thePlayer.posZ - 0.3
                )
                return !mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty()
            }

        fun isCollidedH(dist: Double): Boolean {
            val bb = AxisAlignedBB(
                mc.thePlayer.posX - 0.3, mc.thePlayer.posY + 2, mc.thePlayer.posZ + 0.3,
                mc.thePlayer.posX + 0.3, mc.thePlayer.posY + 3, mc.thePlayer.posZ - 0.3
            )
            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb.offset(0.3 + dist, 0.0, 0.0)).isEmpty()) {
                return true
            } else if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb.offset(-0.3 - dist, 0.0, 0.0))
                    .isEmpty()
            ) {
                return true
            } else if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb.offset(0.0, 0.0, 0.3 + dist))
                    .isEmpty()
            ) {
                return true
            } else if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb.offset(0.0, 0.0, -0.3 - dist))
                    .isEmpty()
            ) {
                return true
            }
            return false
        }

        fun isRealCollidedH(dist: Double): Boolean {
            val bb = AxisAlignedBB(
                mc.thePlayer.posX - 0.3, mc.thePlayer.posY + 0.5, mc.thePlayer.posZ + 0.3,
                mc.thePlayer.posX + 0.3, mc.thePlayer.posY + 1.9, mc.thePlayer.posZ - 0.3
            )
            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb.offset(0.3 + dist, 0.0, 0.0)).isEmpty()) {
                return true
            } else if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb.offset(-0.3 - dist, 0.0, 0.0))
                    .isEmpty()
            ) {
                return true
            } else if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb.offset(0.0, 0.0, 0.3 + dist))
                    .isEmpty()
            ) {
                return true
            } else if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb.offset(0.0, 0.0, -0.3 - dist))
                    .isEmpty()
            ) {
                return true
            }
            return false
        }
    }
}