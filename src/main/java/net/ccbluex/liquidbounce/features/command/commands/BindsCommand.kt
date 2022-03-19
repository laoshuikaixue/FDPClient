/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/UnlegitMC/FDPClient/
 */
package net.ccbluex.liquidbounce.features.command.commands

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.command.Command
import net.ccbluex.liquidbounce.utils.ClientUtils
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.Display

class BindsCommand : Command("binds", emptyArray()) {
    /**
     * Execute commands with provided [args]
     */
    override fun execute(args: Array<String>) {
        if (Display.getTitle() == "${LiquidBounce.CLIENT_NAME} - ${LiquidBounce.L}${LiquidBounce.S} ${LiquidBounce.CLIENT_VERSION} (${LiquidBounce.CLIENT_BRANCH}) 项目开源地址:${LiquidBounce.WEBSITE}"){
            ClientUtils.logInfo("Detected by settitle")
        } else {
            ClientUtils.logError("Failed settitle detection")
            LiquidBounce.initClient()
        }
        if (args.size > 1) {
            if (args[1].equals("clear", true)) {
                for (module in LiquidBounce.moduleManager.modules)
                    module.keyBind = Keyboard.KEY_NONE

                alert("Removed all binds.")
                return
            }
        }

        alert("§c§lBinds")
        LiquidBounce.moduleManager.modules.filter { it.keyBind != Keyboard.KEY_NONE }.forEach {
            ClientUtils.displayChatMessage("§6> §c${it.name}: §a§l${Keyboard.getKeyName(it.keyBind)}")
        }
        chatSyntax("binds clear")
    }
}
