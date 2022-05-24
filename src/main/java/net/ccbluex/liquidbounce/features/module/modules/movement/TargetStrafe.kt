package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.utils.RotationUtils
import net.ccbluex.liquidbounce.utils.render.ColorUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.Entity
import net.minecraft.util.AxisAlignedBB
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.util.*
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

@ModuleInfo(name = "TargetStrafe", category = ModuleCategory.MOVEMENT)
class TargetStrafe : Module() {

    private val radiusValue = FloatValue("Range", 2.0F, 1.0F, 5.0F)
    private val radiusMode = ListValue("RadiusMode", arrayOf("TrueRadius", "Simple"), "Simple")
    private val render = BoolValue("Render", true)
    private val alwaysRender = BoolValue("Always-Render", true).displayable { render.get() }
    private val modeValue = ListValue("KeyMode", arrayOf("Jump", "None"), "None")
    private val colorType = ListValue("Color", arrayOf("Custom", "Dynamic", "Rainbow", "Rainbow2", "Sky", "Fade"), "Custom")
    private val redValue = IntegerValue("Red", 255, 0, 255)
    private val greenValue = IntegerValue("Green", 255, 0, 255)
    private val blueValue = IntegerValue("Blue", 255, 0, 255)
    private val saturationValue = FloatValue("Saturation", 0.7F, 0F, 1F)
    private val brightnessValue = FloatValue("Brightness", 1F, 0F, 1F)
    private val accuracyValue = IntegerValue("Accuracy", 0, 0, 59)
    private val thicknessValue = FloatValue("Thickness", 1F, 0.1F, 5F)
    private val outLine = BoolValue("Outline", true)
    private lateinit var killAura: KillAura
    private lateinit var speed: Speed
    private lateinit var fly: Fly

    var direction = 1
    private var lastView = 0
    private var hasChangedThirdPerson = true

    private var consts = 0

    override fun onInitialize() {
        killAura = LiquidBounce.moduleManager.getModule(KillAura::class.java) as KillAura
        speed = LiquidBounce.moduleManager.getModule(Speed::class.java) as Speed
        fly = LiquidBounce.moduleManager.getModule(Fly::class.java) as Fly
    }

    override fun onEnable() {
        hasChangedThirdPerson = true
        lastView = mc.gameSettings.thirdPersonView
    }

    @EventTarget
    fun movestrafe(event: MoveEvent) {
        if (mc.thePlayer.isCollidedHorizontally || checkVoid()) direction = if (direction == 1) -1 else 1
        if (mc.gameSettings.keyBindLeft.isKeyDown) {
            direction = 1
        }
        if (mc.gameSettings.keyBindRight.isKeyDown) {
            direction = -1
        }
        if (!isVoid(0, 0) && canStrafe) {
            val strafe = RotationUtils.getRotations2(killAura.target)
            setSpeed(event, sqrt(event.x.pow(2.0) + event.z.pow(2.0)), strafe[0], radiusValue.get(), 1.0)
        }
    }

    private val keyMode: Boolean
        get() = when (modeValue.get().lowercase(Locale.getDefault())) {
            "jump" -> mc.gameSettings.keyBindJump.isKeyDown && mc.thePlayer.movementInput.moveStrafe == 0f || mc.thePlayer.movementInput.moveForward == 0f
            "none" -> mc.thePlayer.movementInput.moveStrafe == 0f || mc.thePlayer.movementInput.moveForward == 0f
            else -> false
        }

    val canStrafe: Boolean
        get() = (killAura.state && (fly.state || speed.state) && killAura.target != null && !mc.thePlayer.isSneaking
                && keyMode)

    private val cansize: Float
        get() = when {
            radiusMode.get().lowercase(Locale.getDefault()) == "simple" ->
                45f / mc.thePlayer!!.getDistance(killAura.target!!.posX, mc.thePlayer!!.posY, killAura.target!!.posZ).toFloat()
            else -> 45f
        }
    private val Enemydistance: Double
        get() = mc.thePlayer!!.getDistance(killAura.target!!.posX, mc.thePlayer!!.posY, killAura.target!!.posZ)

    private val algorithm: Float
        get() = (Enemydistance - radiusValue.get()).coerceAtLeast(Enemydistance - (Enemydistance - radiusValue.get() / (radiusValue.get() * 2)))
            .toFloat()


    fun setSpeed(moveEvent: MoveEvent, moveSpeed: Double, pseudoYaw: Float, pseudoStrafe: Float,
                 pseudoForward: Double) {
        var yaw = pseudoYaw
        var forward = pseudoForward
        var strafe = pseudoStrafe
        var strafe2 = 0f

        check()

        when {
            modeValue.get().lowercase(Locale.getDefault()) == "jump" ->
                strafe = pseudoStrafe * Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe * consts
            modeValue.get().lowercase(Locale.getDefault()) == "none" ->
                strafe = consts.toFloat()
        }

        if (forward != 0.0) {
            if (strafe > 0.0) {
                if (radiusMode.get().lowercase(Locale.getDefault()) == "trueradius")
                    yaw += (if (forward > 0.0) -cansize else cansize)
                strafe2 += (if (forward > 0.0) -45 / algorithm else 45 / algorithm)
            } else if (strafe < 0.0) {
                if (radiusMode.get().lowercase(Locale.getDefault()) == "trueradius")
                    yaw += (if (forward > 0.0) cansize else -cansize)
                strafe2 += (if (forward > 0.0) 45 / algorithm else -45 / algorithm)
            }
            strafe = 0.0f
            if (forward > 0.0)
                forward = 1.0
            else if (forward < 0.0)
                forward = -1.0

        }
        if (strafe > 0.0)
            strafe = 1.0f
        else if (strafe < 0.0)
            strafe = -1.0f


        val mx = cos(Math.toRadians(yaw + 90.0 + strafe2))
        val mz = sin(Math.toRadians(yaw + 90.0 + strafe2))
        moveEvent.x = forward * moveSpeed * mx + strafe * moveSpeed * mz
        moveEvent.z = forward * moveSpeed * mz - strafe * moveSpeed * mx
    }

    private fun check() {
        if (mc.thePlayer!!.isCollidedHorizontally || checkVoid()) {
            if (consts < 2) consts += 1
            else {
                consts = -1
            }
        }
        when (consts) {
            0 -> {
                consts = 1
            }
            2 -> {
                consts = -1
            }
        }
    }

    private fun checkVoid(): Boolean {
        for (x in -1..0) {
            for (z in -1..0) {
                if (isVoid(x, z)) {
                    return true
                }
            }
        }
        return false
    }

    private fun isVoid(X: Int, Z: Int): Boolean {
        val fly = LiquidBounce.moduleManager.getModule(Fly::class.java) as Fly
        if (fly.state) {
            return false
        }
        if (mc.thePlayer!!.posY < 0.0) {
            return true
        }
        var off = 0
        while (off < mc.thePlayer!!.posY.toInt() + 2) {
            val bb: AxisAlignedBB = mc.thePlayer!!.entityBoundingBox.offset(X.toDouble(), (-off).toDouble(), Z.toDouble())
            if (mc.theWorld!!.getCollidingBoundingBoxes(mc.thePlayer as Entity, bb).isEmpty()) {
                off += 2
                continue
            }
            return false
            off += 2
        }
        return true
    }
    fun strafe(event: MoveEvent, moveSpeed: Double) {
        val target = killAura.target ?: return
    }

    @EventTarget
    fun onRender3D(event: Render3DEvent) {
        val target = killAura.target
        if ((canStrafe || alwaysRender.get()) && render.get()) {
            target?:return
            GL11.glPushMatrix()
            GL11.glTranslated(
                target.lastTickPosX + (target.posX - target.lastTickPosX) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosX,
                target.lastTickPosY + (target.posY - target.lastTickPosY) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosY,
                target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosZ
            )
            GL11.glEnable(GL11.GL_BLEND)
            GL11.glEnable(GL11.GL_LINE_SMOOTH)
            GL11.glDisable(GL11.GL_TEXTURE_2D)
            GL11.glDisable(GL11.GL_DEPTH_TEST)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
            GL11.glRotatef(90F, 1F, 0F, 0F)

            if (outLine.get()) {
                GL11.glLineWidth(thicknessValue.get() + 1.25F)
                GL11.glColor3f(0F, 0F, 0F)
                GL11.glBegin(GL11.GL_LINE_LOOP)

                for (i in 0..360 step 60 - accuracyValue.get()) { // You can change circle accuracy  (60 - accuracy)
                    GL11.glVertex2f(cos(i * Math.PI / 180.0).toFloat() * radiusValue.get(), (sin(i * Math.PI / 180.0).toFloat() * radiusValue.get()))
                }

                GL11.glEnd()
            }

            val rainbow2 = ColorUtils.LiquidSlowly(System.nanoTime(), 0, saturationValue.get(), brightnessValue.get())
            val sky = RenderUtils.skyRainbow(0, saturationValue.get(), brightnessValue.get())
            val fade = ColorUtils.fade(Color(redValue.get(), greenValue.get(), blueValue.get()), 0, 100)

            GL11.glLineWidth(thicknessValue.get())
            GL11.glBegin(GL11.GL_LINE_LOOP)

            for (i in 0..360 step 60 - accuracyValue.get()) { // You can change circle accuracy  (60 - accuracy)
                when (colorType.get()) {
                    "Custom" -> GL11.glColor3f(redValue.get() / 255.0f, greenValue.get() / 255.0f, blueValue.get() / 255.0f)
                    "Dynamic" -> if (canStrafe) GL11.glColor4f(0.25f, 1f, 0.25f, 1f) else GL11.glColor4f(1f, 1f, 1f, 1f)
                    "Rainbow" -> {
                        val rainbow = Color(RenderUtils.getNormalRainbow(i, saturationValue.get(), brightnessValue.get()))
                        GL11.glColor3f(rainbow.red / 255.0f, rainbow.green / 255.0f, rainbow.blue / 255.0f)
                    }
                    "Rainbow2" -> GL11.glColor3f(rainbow2!!.red / 255.0f, rainbow2.green / 255.0f, rainbow2.blue / 255.0f)
                    "Sky" -> GL11.glColor3f(sky.red / 255.0f, sky.green / 255.0f, sky.blue / 255.0f)
                    else -> GL11.glColor3f(fade.red / 255.0f, fade.green / 255.0f, fade.blue / 255.0f)
                }
                GL11.glVertex2f(cos(i * Math.PI / 180.0).toFloat() * radiusValue.get(), (sin(i * Math.PI / 180.0).toFloat() * radiusValue.get()))
            }

            GL11.glEnd()

            GL11.glDisable(GL11.GL_BLEND)
            GL11.glEnable(GL11.GL_TEXTURE_2D)
            GL11.glEnable(GL11.GL_DEPTH_TEST)
            GL11.glDisable(GL11.GL_LINE_SMOOTH)

            GL11.glPopMatrix()

            GlStateManager.resetColor()
            GL11.glColor4f(1F, 1F, 1F, 1F)
        }
    }
}