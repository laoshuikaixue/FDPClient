package today.flux.module.implement.Render

import com.darkmagician6.eventapi.EventTarget
import com.darkmagician6.eventapi.types.Priority
import net.ccbluex.liquidbounce.event.EventManager
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.ModuleManager
import net.ccbluex.liquidbounce.features.module.modules.player.HackerDetector
import net.ccbluex.liquidbounce.features.module.modules.render.NoRender
import net.ccbluex.liquidbounce.utils.MathUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.util.EnumChatFormatting
import org.lwjgl.opengl.GL11
import sun.font.FontManager
import today.flux.Flux
import today.flux.event.DrawTextEvent
import today.flux.event.NameTagRenderEvent
import today.flux.event.WorldRenderEvent
import today.flux.module.implement.Combat.AntiBots
import today.flux.utility.WorldRenderUtils
import today.flux.utility.WorldUtil
import java.awt.Color

@ModuleInfo(name = "NewNameTag", category = ModuleCategory.RENDER)
class NewNameTag : Module() {
    private val scaleValue = FloatValue("Scale", 1F, 0.1F, 1.0F)
    private val invisible = BoolValue("Bot & Invis", false)
    private val health = BoolValue("Health", true)
    private val distance = BoolValue("Distance", true)
    private val renderArmor = BoolValue("Armor", true)
    private val background = BoolValue("Background", true)
    private val pro = "pro"
    private val sha = "sha"

    @EventTarget
    private fun onRenderNameTag(event: NameTagRenderEvent) {
        if (event.getEntity() is EntityPlayer && isValid(event.getEntity() as EntityPlayer)) {
            event.setCancelled(true)
        }
    }

    @EventTarget(Priority.LOWEST)
    private fun onRender3DEvent(event: WorldRenderEvent) {
        if (this.mc.theWorld == null) return
        for (entity in WorldUtil.getLivingPlayers()) {
            if (isValid(entity)) {
                val yOffset = if (entity.isSneaking) -0.25 else 0.0
                val posX: Double =
                    entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks - this.mc.getRenderManager()
                        .getRenderPosX()
                val posY: Double =
                    entity.lastTickPosY + yOffset + (entity.posY + yOffset - (entity.lastTickPosY + yOffset)) * mc.timer.renderPartialTicks - this.mc.getRenderManager()
                        .getRenderPosY()
                val posZ: Double =
                    entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks - this.mc.getRenderManager()
                        .getRenderPosZ()
                mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 0)
                renderNameTag(entity, posX, posY, posZ, event.getPartialTicks())
            }
        }
    }

    fun isValid(entity: EntityLivingBase): Boolean {
        if (ModuleManager.noRenderMod.isEnabled() && NoRender.players.getValueState() && entity is EntityPlayer) return false
        if (entity.isInvisible && !invisible.getValue()) return false
        return if (!AntiBots.isInTabList(entity) && !invisible.getValue()) false else true
    }

    private fun getDisplayColour(player: EntityPlayer): Int {
        var colour = Color(0xFFFFFF).rgb
        return if (Flux.INSTANCE.getFriendManager().isFriend(player.name)) {
            -11157267
        } else {
            if (player.isInvisible) {
                colour = -1113785
            }
            colour
        }
    }

    private fun interpolate(previous: Double, current: Double, delta: Float): Double {
        return previous + (current - previous) * delta.toDouble()
    }

    private fun renderNameTag(player: EntityPlayer, x: Double, y: Double, z: Double, delta: Float) {
        val tempY = y + 0.7
        val camera: Entity = this.mc.getRenderViewEntity()
        val originalPositionX = camera.posX
        val originalPositionY = camera.posY
        val originalPositionZ = camera.posZ
        camera.posX = interpolate(camera.prevPosX, camera.posX, delta)
        camera.posY = interpolate(camera.prevPosY, camera.posY, delta)
        camera.posZ = interpolate(camera.prevPosZ, camera.posZ, delta)
        val distance = camera.getDistance(
            x + this.mc.getRenderManager().viewerPosX,
            y + this.mc.getRenderManager().viewerPosY,
            z + this.mc.getRenderManager().viewerPosZ
        )
        val width: Float = FontManager.normal2.getStringWidth(getDisplayName(player)) / 2
        var scale = (0.004f * scale.value).toDouble() * distance
        if (scale < 0.01) scale = 0.01
        GlStateManager.pushMatrix()
        GlStateManager.enablePolygonOffset()
        GlStateManager.doPolygonOffset(1.0f, -1500000.0f)
        GlStateManager.disableLighting()
        GlStateManager.translate(x.toFloat(), tempY.toFloat() + 1.4f, z.toFloat())
        GlStateManager.rotate(-this.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f)
        val var10001 = if (this.mc.gameSettings.thirdPersonView === 2) -1.0f else 1.0f
        GlStateManager.rotate(this.mc.getRenderManager().playerViewX, var10001, 0.0f, 0.0f)
        GlStateManager.scale(-scale, -scale, scale)
        if (background.getValue()) {
            WorldRenderUtils.drawBorderedRectReliant(
                -width - 2,
                -(FontManager.normal2.FONT_HEIGHT + 3),
                width + 2.0f,
                2.0f,
                scale.toFloat(),
                Color(if (HackerDetector.isHacker(player)) 255 else 25, 25, 25, 160).rgb,
                Color(0, 0, 0, 180).rgb
            )
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)

        //Fix Color Glitch
        GL11.glDepthMask(false)
        FontManager.normal2.drawStringWithSuperShadow(
            getDisplayName(player), -width, -FontManager.normal2.FONT_HEIGHT,
            getDisplayColour(player)
        )
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        GL11.glDepthMask(true)
        if (renderArmor.getValue()) {
            renderArmor(player)

            //No Paper item
            GlStateManager.disableBlend()
            GlStateManager.enableAlpha()
        }
        GlStateManager.disableLighting()
        camera.posX = originalPositionX
        camera.posY = originalPositionY
        camera.posZ = originalPositionZ
        GlStateManager.doPolygonOffset(1.0f, 1500000.0f)
        GlStateManager.disablePolygonOffset()
        GlStateManager.popMatrix()
    }

    private fun renderArmor(player: EntityPlayer) {
        var xOffset = 0
        var index: Int
        var stack: ItemStack?
        index = 3
        while (index >= 0) {
            stack = player.inventory.armorInventory[index]
            if (stack != null) {
                xOffset -= 8
            }
            --index
        }
        if (player.currentEquippedItem != null) {
            xOffset -= 8
            val var27 = player.currentEquippedItem.copy()
            if (var27.hasEffect() && (var27.item is ItemTool || var27.item is ItemArmor)) {
                var27.stackSize = 1
            }
            renderItemStack(var27, xOffset, -26)
            xOffset += 16
        }
        index = 3
        while (index >= 0) {
            stack = player.inventory.armorInventory[index]
            if (stack != null) {
                val armourStack = stack.copy()
                if (armourStack.hasEffect() && (armourStack.item is ItemTool || armourStack.item is ItemArmor)) {
                    armourStack.stackSize = 1
                }
                renderItemStack(armourStack, xOffset, -26)
                xOffset += 16
            }
            --index
        }
    }

    private fun renderItemStack(stack: ItemStack, x: Int, y: Int) {
        GlStateManager.pushMatrix()
        GlStateManager.disableAlpha()
        this.mc.getRenderItem().zLevel = -150.0f
        GlStateManager.disableCull()
        this.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y)
        this.mc.getRenderItem().renderItemOverlays(this.mc.fontRendererObj, stack, x, y)
        GlStateManager.enableCull()
        this.mc.getRenderItem().zLevel = 0
        GlStateManager.disableBlend()
        GlStateManager.scale(0.5f, 0.5f, 0.5f)
        GlStateManager.disableDepth()
        GlStateManager.disableLighting()
        renderEnchantmentText(stack, x, y)
        GlStateManager.enableLighting()
        GlStateManager.enableDepth()
        GlStateManager.scale(2.0f, 2.0f, 2.0f)
        GlStateManager.enableAlpha()
        GlStateManager.popMatrix()
    }

    private fun renderEnchantmentText(stack: ItemStack, x: Int, y: Int) {
        try {
            var enchantmentY = y - 24
            val color = Color(0xFFFFFF).rgb
            if (stack.item is ItemArmor) {
                val protection = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack)
                val projectileProtection =
                    EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, stack)
                val blastProtection = EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack)
                val fireProtectionLevel =
                    EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack)
                val thornsLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack)
                val featherFallingLevel =
                    EnchantmentHelper.getEnchantmentLevel(Enchantment.featherFalling.effectId, stack)
                val unbreakingLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack)
                if (protection > 0) {
                    this.mc.fontRendererObj.drawStringWithShadow(pro + protection, x * 2, enchantmentY, color)
                    enchantmentY += 8
                }
                if (unbreakingLevel > 0) {
                    this.mc.fontRendererObj.drawStringWithShadow(
                        Enchantment.unbreaking.name.substring(
                            0,
                            3
                        ) + unbreakingLevel, x * 2, enchantmentY, color
                    )
                    enchantmentY += 8
                }
                if (projectileProtection > 0) {
                    this.mc.fontRendererObj.drawStringWithShadow(
                        Enchantment.projectileProtection.name.substring(
                            0,
                            3
                        ) + projectileProtection, x * 2, enchantmentY, color
                    )
                    enchantmentY += 8
                }
                if (blastProtection > 0) {
                    this.mc.fontRendererObj.drawStringWithShadow(
                        Enchantment.blastProtection.name.substring(
                            0,
                            3
                        ) + blastProtection, x * 2, enchantmentY, color
                    )
                    enchantmentY += 8
                }
                if (fireProtectionLevel > 0) {
                    this.mc.fontRendererObj.drawStringWithShadow(
                        Enchantment.fireAspect.name.substring(
                            0,
                            3
                        ) + fireProtectionLevel, x * 2, enchantmentY, color
                    )
                    enchantmentY += 8
                }
                if (thornsLevel > 0) {
                    this.mc.fontRendererObj.drawStringWithShadow(
                        Enchantment.thorns.name.substring(0, 3) + thornsLevel,
                        x * 2,
                        enchantmentY,
                        color
                    )
                    enchantmentY += 8
                }
                if (featherFallingLevel > 0) {
                    this.mc.fontRendererObj.drawStringWithShadow(
                        Enchantment.featherFalling.name.substring(
                            0,
                            3
                        ) + featherFallingLevel, x * 2, enchantmentY, color
                    )
                    enchantmentY += 8
                }

                /*
                boolean dura = false;
                if(dura && stack.getMaxDamage() - stack.getItemDamage() < stack.getMaxDamage()) {
                    this.mc.fontRendererObj.drawStringWithShadow(stack.getMaxDamage() - stack.getItemDamage() + "", x * 2, enchantmentY + 2, -26215);
                    enchantmentY += 8;
                }*/
            }
            if (stack.item is ItemBow) {
                val powerLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack)
                val punchLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack)
                val flameLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack)
                if (powerLevel > 0) {
                    this.mc.fontRendererObj.drawStringWithShadow(
                        Enchantment.power.name.substring(0, 3) + powerLevel,
                        x * 2,
                        enchantmentY,
                        color
                    )
                    enchantmentY += 8
                }
                if (punchLevel > 0) {
                    this.mc.fontRendererObj.drawStringWithShadow(
                        Enchantment.punch.name.substring(0, 3) + punchLevel,
                        x * 2,
                        enchantmentY,
                        color
                    )
                    enchantmentY += 8
                }
                if (flameLevel > 0) {
                    this.mc.fontRendererObj.drawStringWithShadow(
                        Enchantment.flame.name.substring(0, 3) + flameLevel,
                        x * 2,
                        enchantmentY,
                        color
                    )
                    enchantmentY += 8
                }
            }
            if (stack.item is ItemPickaxe) {
                val efficiencyLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack)
                val fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, stack)
                if (efficiencyLevel > 0) {
                    this.mc.fontRendererObj.drawStringWithShadow(
                        Enchantment.efficiency.name.substring(
                            0,
                            3
                        ) + efficiencyLevel, x * 2, enchantmentY, color
                    )
                    enchantmentY += 8
                }
                if (fortuneLevel > 0) {
                    this.mc.fontRendererObj.drawStringWithShadow(
                        Enchantment.fortune.name.substring(
                            0,
                            3
                        ) + fortuneLevel, x * 2, enchantmentY, color
                    )
                    enchantmentY += 8
                }
            }
            if (stack.item is ItemAxe) {
                val sharpnessLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack)
                val fireAspect = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack)
                val efficiencyLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack)
                if (sharpnessLevel > 0) {
                    this.mc.fontRendererObj.drawStringWithShadow(
                        Enchantment.sharpness.name.substring(
                            0,
                            3
                        ) + sharpnessLevel, x * 2, enchantmentY, color
                    )
                    enchantmentY += 8
                }
                if (fireAspect > 0) {
                    this.mc.fontRendererObj.drawStringWithShadow(
                        Enchantment.fireAspect.name.substring(
                            0,
                            3
                        ) + fireAspect, x * 2, enchantmentY, color
                    )
                    enchantmentY += 8
                }
                if (efficiencyLevel > 0) {
                    this.mc.fontRendererObj.drawStringWithShadow(
                        Enchantment.efficiency.name.substring(
                            0,
                            3
                        ) + efficiencyLevel, x * 2, enchantmentY, color
                    )
                    enchantmentY += 8
                }
            }
            if (stack.item is ItemSword) {
                val sharpnessLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack)
                val knockbackLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, stack)
                val fireAspectLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack)
                if (sharpnessLevel > 0) {
                    this.mc.fontRendererObj.drawStringWithShadow(sha + sharpnessLevel, x * 2, enchantmentY, color)
                    enchantmentY += 8
                }
                if (knockbackLevel > 0) {
                    this.mc.fontRendererObj.drawStringWithShadow(
                        Enchantment.knockback.name.substring(
                            0,
                            3
                        ) + knockbackLevel, x * 2, enchantmentY, color
                    )
                    enchantmentY += 8
                }
                if (fireAspectLevel > 0) {
                    this.mc.fontRendererObj.drawStringWithShadow(
                        Enchantment.fireAspect.name.substring(
                            0,
                            3
                        ) + fireAspectLevel, x * 2, enchantmentY, color
                    )
                    enchantmentY += 8
                }
            }

            /* if(stack.getItem() == Items.golden_apple && stack.hasEffect()) {
                this.mc.fontRendererObj.drawStringWithShadowWithShadow("god", (float)(x * 2), (float)enchantmentY, -3977919);
            }*/
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getDisplayName(entity: EntityLivingBase): String {
        var drawTag = entity.displayName.formattedText
        if (Flux.INSTANCE.getFriendManager().isFriend(entity.name)) {
            drawTag =
                drawTag.replace(entity.displayName.formattedText, EnumChatFormatting.AQUA.toString() + entity.name)
        }
        val color: EnumChatFormatting
        val health: Double = MathUtils.roundToPlace(entity.health / 2.00, 2)
        color = if (health >= 6.0) {
            EnumChatFormatting.GREEN
        } else if (health >= 2.0) {
            EnumChatFormatting.YELLOW
        } else {
            EnumChatFormatting.RED
        }
        val clientTag = ""
        drawTag =
            (if (distance.getValue()) EnumChatFormatting.GRAY.toString() + "[" + entity.getDistanceToEntity(this.mc.thePlayer)
                .toInt() + "m] " else "") + EnumChatFormatting.RESET + clientTag + EnumChatFormatting.RESET + EnumChatFormatting.GRAY + drawTag + " " + if (Companion.health.getValue()) "" + color + health else ""
        val drawTextEvent = DrawTextEvent(drawTag)
        EventManager.call(drawTextEvent)
        drawTag = drawTextEvent.text
        return drawTag
    }
}