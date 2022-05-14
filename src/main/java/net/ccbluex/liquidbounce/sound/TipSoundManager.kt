package net.ccbluex.liquidbounce.sound

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.utils.FileUtils
import java.io.File

class TipSoundManager {
    var enableSound: TipSoundPlayer
    var disableSound: TipSoundPlayer
    var startup: TipSoundPlayer
    var clickguiopen: TipSoundPlayer
    var enter: TipSoundPlayer

    init {
        val enableSoundFile = File(LiquidBounce.fileManager.soundsDir, "enable.wav")
        val disableSoundFile = File(LiquidBounce.fileManager.soundsDir, "disable.wav")
        val startupFile = File(LiquidBounce.fileManager.soundsDir, "startup.wav")
        val clickguiopenFile = File(LiquidBounce.fileManager.soundsDir, "clickguiopen.wav")
        val enterFile = File(LiquidBounce.fileManager.soundsDir, "enter.wav")

        if (!enableSoundFile.exists()) {
            FileUtils.unpackFile(enableSoundFile, "assets/minecraft/fdpclient/sound/enable.wav")
        }
        if (!disableSoundFile.exists()) {
            FileUtils.unpackFile(disableSoundFile, "assets/minecraft/fdpclient/sound/disable.wav")
        }
        if (!startupFile.exists()) {
            FileUtils.unpackFile(startupFile, "assets/minecraft/fdpclient/sound/startup.wav")
        }
        if (!clickguiopenFile.exists()) {
            FileUtils.unpackFile(clickguiopenFile, "assets/minecraft/fdpclient/sound/clickguiopen.wav")
        }
        if (!enterFile.exists()) {
            FileUtils.unpackFile(enterFile, "assets/minecraft/fdpclient/sound/enter.wav")
        }

        enableSound = TipSoundPlayer(enableSoundFile)
        disableSound = TipSoundPlayer(disableSoundFile)
        startup = TipSoundPlayer(startupFile)
        clickguiopen = TipSoundPlayer(clickguiopenFile)
        enter = TipSoundPlayer(enterFile)
    }
}