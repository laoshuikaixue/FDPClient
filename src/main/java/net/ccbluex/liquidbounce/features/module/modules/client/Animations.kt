/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/laoshuikaixue/FDPClient
 */
package net.ccbluex.liquidbounce.features.module.modules.client

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.render.EaseUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue

@ModuleInfo(name = "Animations", category = ModuleCategory.CLIENT, canEnable = false)
object Animations : Module() {
    val blockingModeValue = ListValue(
        "BlockingMode", arrayOf(
            "1.7", "Throw", "Spin", "Forward", "Old", "Dortware", "Dortware 2", "Cockless", "Shove", "Chill", "Smooth", "Butter", "Stab", "Small", "Wobble",
            "Chungus", "Bitch Slap", "Leaked", "Rise", "Flush", "Whack", "Big Whack", "Spinny", "Inwards", "Rhys",
            "Akrien", "Avatar", "ETB", "Exhibition", "Push", "Reverse",
            "Shield", "SigmaNew", "SigmaOld", "Slide", "SlideDown", "HSlide", "Swong", "VisionFX",
            "Swank", "Jello", "Rotate", "Liquid", "None"
        ),
        "SlideDown"
    )
    val invModeValue = ListValue("InvMode", arrayOf("None", "Slide", "Zoom"), "Slide")
    val invEaseModeValue = EaseUtils.getEnumEasingList("InvEase")
    val invEaseOrderModeValue = EaseUtils.getEnumEasingOrderList("InvEaseOrder")
    val invTimeValue = IntegerValue("InvTime", 500, 0, 1000).displayable { !invModeValue.equals("None") }
    private val tabShowPlayerSkinValue = BoolValue("TabShowPlayerSkin", false)
    val tabModeValue = ListValue("TabMode", arrayOf("None", "UpSlide", "DownSlide", "Zoom"), "Zoom")
    val tabEaseModeValue = EaseUtils.getEnumEasingList("TabEase")
    val tabEaseOrderModeValue = EaseUtils.getEnumEasingOrderList("TabEaseOrder")
    val tabTimeValue = IntegerValue("TabTime", 500, 0, 1000).displayable { !tabModeValue.equals("None") }
    val translateXValue = FloatValue("TranslateX", 0.0f, 0.0f, 1.5f)
    val translateYValue = FloatValue("TranslateY", 0.0f, 0.0f, 0.5f)
    val translateZValue = FloatValue("TranslateZ", 0.0f, 0.0f, -2.0f)
    val itemPosXValue = FloatValue("ItemPosX", 0.56F, -1.0F, 1.0F)
    val itemPosYValue = FloatValue("ItemPosY", -0.52F, -1.0F, 1.0F)
    val itemPosZValue = FloatValue("ItemPosZ", -0.71999997F, -1.0F, 1.0F)
    val itemScaleValue = FloatValue("ItemScale", 0.4f, 0.0f, 2.0f)
    val swingAnimValue = BoolValue("SwingAnim", false)
    val swingSpeedValue = FloatValue("SwingSpeed", 1f, 0.5f, 5.0f)
    val anythingBlockValue = BoolValue("AnythingBlock", false)

    var flagRenderTabOverlay = false
        get() = field && tabShowPlayerSkinValue.get()

    var tabPercent = 0f
    var tabHopePercent = 0f
    var lastTabSync = 0L
}
