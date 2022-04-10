/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/UnlegitMC/FDPClient/
 */
package net.ccbluex.liquidbounce.features.command.commands

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.command.Command
import net.ccbluex.liquidbounce.features.command.CommandManager
import net.ccbluex.liquidbounce.features.module.modules.misc.KillInsults
import net.ccbluex.liquidbounce.ui.cape.GuiCapeManager
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.ClientUtils
import org.lwjgl.opengl.Display

class ReloadCommand : Command("reload", emptyArray()) {
    /**
     * Execute commands with provided [args]
     */
    override fun execute(args: Array<String>) {
        if (Display.getTitle() == "${LiquidBounce.CLIENT_NAME} - ${LiquidBounce.L}${LiquidBounce.S} ${LiquidBounce.CLIENT_VERSION} (${LiquidBounce.CLIENT_BRANCH}) 项目开源地址:${LiquidBounce.WEBSITE} 官方群:1028574302"){
            ClientUtils.logInfo("Detected by settitle")
        } else {
            ClientUtils.logError("Failed settitle detection")
            LiquidBounce.initClient()
        }
        alert("Reloading...")
        alert("§c§lReloading commands...")
        LiquidBounce.commandManager = CommandManager()
        LiquidBounce.commandManager.registerCommands()
        LiquidBounce.isStarting = true
        LiquidBounce.isLoadingConfig = true
        LiquidBounce.scriptManager.disableScripts()
        LiquidBounce.scriptManager.unloadScripts()
        for (module in LiquidBounce.moduleManager.modules)
            LiquidBounce.moduleManager.generateCommand(module)
        alert("§c§lReloading scripts...")
        LiquidBounce.scriptManager.loadScripts()
        LiquidBounce.scriptManager.enableScripts()
        alert("§c§lReloading fonts...")
        Fonts.loadFonts()
        alert("§c§lReloading modules...")
        LiquidBounce.configManager.load(LiquidBounce.configManager.nowConfig, false)
        KillInsults.loadFile()
        GuiCapeManager.load()
        alert("§c§lReloading accounts...")
        LiquidBounce.fileManager.loadConfig(LiquidBounce.fileManager.accountsConfig)
        alert("§c§lReloading friends...")
        LiquidBounce.fileManager.loadConfig(LiquidBounce.fileManager.friendsConfig)
        alert("§c§lReloading xray...")
        LiquidBounce.fileManager.loadConfig(LiquidBounce.fileManager.xrayConfig)
        alert("§c§lReloading HUD...")
        LiquidBounce.fileManager.loadConfig(LiquidBounce.fileManager.hudConfig)
        alert("Reloaded.")
        LiquidBounce.isStarting = false
        LiquidBounce.isLoadingConfig = false
        System.gc()
    }
}
