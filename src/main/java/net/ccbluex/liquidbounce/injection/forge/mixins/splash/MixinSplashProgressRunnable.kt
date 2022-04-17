package net.ccbluex.liquidbounce.injection.forge.mixins.splash

import net.ccbluex.liquidbounce.utils.ClientUtils.logInfo
import net.ccbluex.liquidbounce.utils.render.AnimatedValue
import net.ccbluex.liquidbounce.utils.render.ColorUtils.LiquidSlowly
import net.ccbluex.liquidbounce.utils.render.EaseUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraftforge.fml.client.SplashProgress
import net.minecraftforge.fml.common.ProgressManager
import org.lwjgl.opengl.Display
import org.lwjgl.opengl.GL11
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.io.IOException
import javax.imageio.ImageIO

@Mixin(targets = ["net.minecraftforge.fml.client.SplashProgress$3"], remap = false)
abstract class MixinSplashProgressRunnable {
    @Shadow(remap = false)
    protected abstract fun setGL()

    @Shadow(remap = false)
    protected abstract fun clearGL()

    @Inject(method = ["run()V"], at = [At(value = "HEAD")], remap = false, cancellable = true)
    private fun run(callbackInfo: CallbackInfo) {
        callbackInfo.cancel()
        setGL()
        GL11.glClearColor(1f, 1f, 1f, 1f)
        logInfo("[Splash] Loading Texture...")
        GL11.glEnable(GL11.GL_TEXTURE_2D)
        val tex: Int
        tex = try {
            RenderUtils.loadGlTexture(ImageIO.read(this.javaClass.getResourceAsStream("/assets/minecraft/fdpclient/misc/splash.png")))
        } catch (e: IOException) {
            0
        }
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        val animatedValue = AnimatedValue()
        animatedValue.type = EaseUtils.EnumEasingType.CIRC
        animatedValue.duration = 600L
        logInfo("[Splash] Starting Render Thread...")
        while (!SplashProgress.done) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT)
            val width = Display.getWidth()
            val height = Display.getHeight()
            GL11.glViewport(0, 0, width, height)
            GL11.glMatrixMode(GL11.GL_PROJECTION)
            GL11.glLoadIdentity()
            GL11.glOrtho(0.0, width.toDouble(), height.toDouble(), 0.0, -1.0, 1.0)
            GL11.glMatrixMode(GL11.GL_MODELVIEW)
            GL11.glLoadIdentity()
            GL11.glColor4f(1f, 1f, 1f, 1f)

            // draw splash background
            GL11.glEnable(GL11.GL_TEXTURE_2D)
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex)
            GL11.glBegin(GL11.GL_QUADS)
            GL11.glTexCoord2f(0.0f, 0.0f)
            GL11.glVertex2f(0.0f, 0.0f)
            GL11.glTexCoord2f(1.0f, 0.0f)
            GL11.glVertex2f(width.toFloat(), 0.0f)
            GL11.glTexCoord2f(1.0f, 1.0f)
            GL11.glVertex2f(width.toFloat(), height.toFloat())
            GL11.glTexCoord2f(0.0f, 1.0f)
            GL11.glVertex2f(0.0f, height.toFloat())
            GL11.glEnd()
            GL11.glDisable(GL11.GL_TEXTURE_2D)

            // draw progress bar
            val rectX = width * 0.2f
            val rectX2 = width * 0.8f
            val rectY = height * 0.7f
            val rectY2 = height * 0.73f
            val rectRadius = height * 0.025f
            val progress =
                animatedValue.sync(progress.toDouble()).toFloat()
            if (progress != 1f) {
                GL11.glColor4f(0f, 0f, 0f, 0.3f)
                RenderUtils.drawRect(rectX, rectY, rectX2, rectY2, rectRadius.toInt())
            }
            if (progress != 0f) {
                GL11.glColor4f(1f, 1f, 1f, 1f)
                RenderUtils.drawRect(rectX, rectY, rectX2, rectY2, rectRadius.toInt())
                for (i in 0..35) {
                    RenderUtils.drawRect(rectX,rectY,rectX + width * 0.6f * progress, rectY2, LiquidSlowly(System.nanoTime(), (i + 1) * 14,0.3f,1f))
                }
            }
            SplashProgress.mutex.acquireUninterruptibly()
            Display.update()
            SplashProgress.mutex.release()
            if (SplashProgress.pause) {
                clearGL()
                setGL()
            }
            Display.sync(60)
        }
        GL11.glDeleteTextures(tex)
        clearGL()
    }

    private val progress: Float
        get() {
            var progress = 0f
            val it = ProgressManager.barIterator()
            if (it.hasNext()) {
                val bar = it.next()
                progress = bar.step / bar.steps.toFloat()
            }
            return progress
        }
}