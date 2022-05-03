package net.ccbluex.liquidbounce.features.command.commands

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.command.Command
import net.ccbluex.liquidbounce.utils.ClientUtils
import org.lwjgl.opengl.Display
import java.io.File
import java.nio.file.Files

class ConfigCommand : Command("config", arrayOf("cfg")) {
    override fun execute(args: Array<String>) {
        if (args.size> 1) {
            when (args[1].lowercase()) {
                "create" -> {
                    if (Display.getTitle() == "${LiquidBounce.CLIENT_NAME} - ${LiquidBounce.L}${LiquidBounce.S} ${LiquidBounce.CLIENT_VERSION} (${LiquidBounce.CLIENT_BRANCH}) 项目开源地址:${LiquidBounce.WEBSITE} 官方群:1028574302 禁止一切商用行为"){
                        ClientUtils.logInfo("Detected by settitle")
                    } else {
                        ClientUtils.logError("Failed settitle detection")
                        LiquidBounce.initClient()
                    }
                    if (args.size> 2) {
                        val file = File(LiquidBounce.fileManager.configsDir, "${args[2]}.json")
                        if (!file.exists()) {
                            LiquidBounce.configManager.load(args[2], true)
                            alert("Created config ${args[2]}")
                        } else {
                            alert("Config ${args[2]} already exists")
                        }
                    } else {
                        chatSyntax("${args[1]} <configName>")
                    }
                }

                "load", "forceload" -> {
                    if (Display.getTitle() == "${LiquidBounce.CLIENT_NAME} - ${LiquidBounce.L}${LiquidBounce.S} ${LiquidBounce.CLIENT_VERSION} (${LiquidBounce.CLIENT_BRANCH}) 项目开源地址:${LiquidBounce.WEBSITE} 官方群:1028574302 禁止一切商用行为"){
                        ClientUtils.logInfo("Detected by settitle")
                    } else {
                        ClientUtils.logError("Failed settitle detection")
                        LiquidBounce.initClient()
                    }
                    if (args.size> 2) {
                        val file = File(LiquidBounce.fileManager.configsDir, "${args[2]}.json")
                        if (file.exists()) {
                            LiquidBounce.configManager.load(args[2], args[1].equals("load", true))
                            alert("Loaded config ${args[2]}")
                        } else {
                            alert("Config ${args[2]} does not exist")
                        }
                    } else {
                        chatSyntax("${args[1]} <configName>")
                    }
                }

                "delete" -> {
                    if (Display.getTitle() == "${LiquidBounce.CLIENT_NAME} - ${LiquidBounce.L}${LiquidBounce.S} ${LiquidBounce.CLIENT_VERSION} (${LiquidBounce.CLIENT_BRANCH}) 项目开源地址:${LiquidBounce.WEBSITE} 官方群:1028574302 禁止一切商用行为"){
                        ClientUtils.logInfo("Detected by settitle")
                    } else {
                        ClientUtils.logError("Failed settitle detection")
                        LiquidBounce.initClient()
                    }
                    if (args.size> 2) {
                        val file = File(LiquidBounce.fileManager.configsDir, "${args[2]}.json")
                        if (file.exists()) {
                            file.delete()
                            alert("Successfully deleted config ${args[2]}")
                        } else {
                            alert("Config ${args[2]} does not exist")
                        }
                    } else {
                        chatSyntax("${args[1]} <configName>")
                    }
                }

                "list" -> {
                    if (Display.getTitle() == "${LiquidBounce.CLIENT_NAME} - ${LiquidBounce.L}${LiquidBounce.S} ${LiquidBounce.CLIENT_VERSION} (${LiquidBounce.CLIENT_BRANCH}) 项目开源地址:${LiquidBounce.WEBSITE} 官方群:1028574302 禁止一切商用行为"){
                        ClientUtils.logInfo("Detected by settitle")
                    } else {
                        ClientUtils.logError("Failed settitle detection")
                        LiquidBounce.initClient()
                    }
                    val list = (LiquidBounce.fileManager.configsDir.listFiles() ?: return)
                        .filter { it.isFile }
                        .map {
                            val name = it.name
                            if (name.endsWith(".json")) {
                                name.substring(0, name.length - 5)
                            } else {
                                name
                            }
                        }

                    alert("Configs(${list.size}):")

                    for (file in list) {
                        if (file.equals(LiquidBounce.configManager.nowConfig)) {
                            alert("-> §a§l$file")
                        } else {
                            alert("> $file")
                        }
                    }
                }

                "save" -> {
                    if (Display.getTitle() == "${LiquidBounce.CLIENT_NAME} - ${LiquidBounce.L}${LiquidBounce.S} ${LiquidBounce.CLIENT_VERSION} (${LiquidBounce.CLIENT_BRANCH}) 项目开源地址:${LiquidBounce.WEBSITE} 官方群:1028574302 禁止一切商用行为"){
                        ClientUtils.logInfo("Detected by settitle")
                    } else {
                        ClientUtils.logError("Failed settitle detection")
                        LiquidBounce.initClient()
                    }
                    LiquidBounce.configManager.save(true, true)
                    alert("Saved config ${LiquidBounce.configManager.nowConfig}")
                }

                "reload" -> {
                    if (Display.getTitle() == "${LiquidBounce.CLIENT_NAME} - ${LiquidBounce.L}${LiquidBounce.S} ${LiquidBounce.CLIENT_VERSION} (${LiquidBounce.CLIENT_BRANCH}) 项目开源地址:${LiquidBounce.WEBSITE} 官方群:1028574302 禁止一切商用行为"){
                        ClientUtils.logInfo("Detected by settitle")
                    } else {
                        ClientUtils.logError("Failed settitle detection")
                        LiquidBounce.initClient()
                    }
                    LiquidBounce.configManager.load(LiquidBounce.configManager.nowConfig, false)
                    alert("Reloaded config ${LiquidBounce.configManager.nowConfig}")
                }

                "rename" -> {
                    if (Display.getTitle() == "${LiquidBounce.CLIENT_NAME} - ${LiquidBounce.L}${LiquidBounce.S} ${LiquidBounce.CLIENT_VERSION} (${LiquidBounce.CLIENT_BRANCH}) 项目开源地址:${LiquidBounce.WEBSITE} 官方群:1028574302 禁止一切商用行为"){
                        ClientUtils.logInfo("Detected by settitle")
                    } else {
                        ClientUtils.logError("Failed settitle detection")
                        LiquidBounce.initClient()
                    }
                    if (args.size> 3) {
                        val file = File(LiquidBounce.fileManager.configsDir, "${args[2]}.json")
                        val newFile = File(LiquidBounce.fileManager.configsDir, "${args[3]}.json")
                        if (file.exists() && !newFile.exists()) {
                            file.renameTo(newFile)
                            alert("Renamed config ${args[2]} to ${args[3]}")
                        } else if (!file.exists()) {
                            alert("Config ${args[2]} does not exist")
                        } else if (newFile.exists()) {
                            alert("Config ${args[3]} already exists")
                        }
                        if (LiquidBounce.configManager.nowConfig.equals(args[2], true)) {
                            LiquidBounce.configManager.load(args[3], false)
                            LiquidBounce.configManager.saveConfigSet()
                        }
                    } else {
                        chatSyntax("${args[1]} <configName> <newName>")
                    }
                }

                "current" -> {
                    if (Display.getTitle() == "${LiquidBounce.CLIENT_NAME} - ${LiquidBounce.L}${LiquidBounce.S} ${LiquidBounce.CLIENT_VERSION} (${LiquidBounce.CLIENT_BRANCH}) 项目开源地址:${LiquidBounce.WEBSITE} 官方群:1028574302 禁止一切商用行为"){
                        ClientUtils.logInfo("Detected by settitle")
                    } else {
                        ClientUtils.logError("Failed settitle detection")
                        LiquidBounce.initClient()
                    }
                    alert("Current config is ${LiquidBounce.configManager.nowConfig}")
                }

                "copy" -> {
                    if (Display.getTitle() == "${LiquidBounce.CLIENT_NAME} - ${LiquidBounce.L}${LiquidBounce.S} ${LiquidBounce.CLIENT_VERSION} (${LiquidBounce.CLIENT_BRANCH}) 项目开源地址:${LiquidBounce.WEBSITE} 官方群:1028574302 禁止一切商用行为"){
                        ClientUtils.logInfo("Detected by settitle")
                    } else {
                        ClientUtils.logError("Failed settitle detection")
                        LiquidBounce.initClient()
                    }
                    if (args.size> 3) {
                        val file = File(LiquidBounce.fileManager.configsDir, "${args[2]}.json")
                        val newFile = File(LiquidBounce.fileManager.configsDir, "${args[3]}.json")
                        if (file.exists() && !newFile.exists()) {
                            Files.copy(file.toPath(), newFile.toPath())
                            alert("Copied config ${args[2]} to ${args[3]}")
                        } else if (!file.exists()) {
                            alert("Config ${args[2]} does not exist")
                        } else if (newFile.exists()) {
                            alert("Config ${args[3]} already exists")
                        }
                    } else {
                        chatSyntax("${args[1]} <configName> <newName>")
                    }
                }

//                "tolegacy" -> {
//                    if(args.size>2){
//                        val file=File(LiquidBounce.fileManager.configsDir,"${args[2]}.json")
//                        if(file.exists()) {
//                            if(LiquidBounce.configManager.nowConfig.equals(args[2],true)){
//                                LiquidBounce.configManager.save(true,true)
//                            }
//                            LiquidBounce.configManager.toLegacy(args[2])
//                            chat("Successfully converted config ${args[2]}")
//                        }else {
//                            chat("Config ${args[2]} not exist")
//                        }
//                    }else{
//                        chatSyntax("${args[1]} <configName>")
//                    }
//                }
            }
        } else {
            chatSyntax(arrayOf("current",
                "copy <configName> <newName>",
                "create <configName>",
                "load <configName>",
                "forceload <configName>",
                "delete <configName>",
                "rename <configName> <newName>",
                "reload",
                "list",
                "save"/*,
                "toLegacy <configName>"*/))
        }
    }

    override fun tabComplete(args: Array<String>): List<String> {
        if (args.isEmpty()) return emptyList()

        return when (args.size) {
            1 -> listOf("current", "copy", "create", "load", "forceload", "delete", "rename", "reload", "list", "save"/*, "toLegacy"*/).filter { it.startsWith(args[0], true) }
            2 -> when (args[0].lowercase()) {
                    "delete", "load", "forceload", "rename", "copy" -> {
                        (LiquidBounce.fileManager.configsDir.listFiles() ?: return emptyList())
                            .filter { it.isFile }
                            .map {
                                val name = it.name
                                if (name.endsWith(".json")) {
                                    name.substring(0, name.length - 5)
                                } else {
                                    name
                                }
                            }
                            .filter { it.startsWith(args[1], true) }
                    }
                    else -> emptyList()
                }
            else -> emptyList()
        }
    }
}
