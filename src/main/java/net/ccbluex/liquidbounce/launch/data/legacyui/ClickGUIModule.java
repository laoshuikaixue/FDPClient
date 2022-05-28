/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/laoshuikaixue/FDPClient
 */
package net.ccbluex.liquidbounce.launch.data.legacyui;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.ClickGui;
import net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.style.styles.*;
import net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.style.styles.ClickGui2.ClickGui2;
import net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.style.styles.ClickGui3.ClickGui3;
import net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.style.styles.ClickGui1.ClickGui1;
import net.ccbluex.liquidbounce.launch.options.LegacyUiLaunchOption;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@ModuleInfo(name = "ClickGUI", category = ModuleCategory.CLIENT, keyBind = Keyboard.KEY_RSHIFT, canEnable = false)
public class ClickGUIModule extends Module {
    private final ListValue styleValue = new ListValue("Style", new String[] {"ClickGui1", "ClickGui2", "ClickGui3", "LiquidBounce", "Null", "Null2", "Slowly", "Black", "White"}, "ClickGui1") {
        @Override
        protected void onChanged(final String oldValue, final String newValue) {
            updateStyle();
        }
    };

    public final FloatValue scaleValue = new FloatValue("Scale", 1F, 0.7F, 2F);
    public final IntegerValue maxElementsValue = new IntegerValue("MaxElements", 15, 1, 20);

    public static final BoolValue colorRainbow = new BoolValue("Rainbow", true);
    public static final IntegerValue colorRedValue = (IntegerValue) new IntegerValue("R", 0, 0, 255).displayable(() -> !colorRainbow.get());
    public static final IntegerValue colorGreenValue = (IntegerValue) new IntegerValue("G", 160, 0, 255).displayable(() -> !colorRainbow.get());
    public static final IntegerValue colorBlueValue = (IntegerValue) new IntegerValue("B", 255, 0, 255).displayable(() -> !colorRainbow.get());

    public static Color generateColor() {
        return colorRainbow.get() ? ColorUtils.INSTANCE.rainbow() : new Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get());
    }

    @Override
    public void onEnable() {
        LiquidBounce.tipSoundManager.getClickguiopen().asyncPlay();
        if(styleValue.get().contains("ClickGui1")) {
            mc.displayGuiScreen(new ClickGui1());
            this.setState(false);
        }
        else if(styleValue.get().contains("ClickGui2")) {
            mc.displayGuiScreen(new ClickGui2());
            this.setState(false);
        }
        else if(styleValue.get().contains("ClickGui3")) {
            mc.displayGuiScreen(new ClickGui3());
            this.setState(false);
        }
        else {
            updateStyle();
            mc.displayGuiScreen(LegacyUiLaunchOption.clickGui);
        }
    }

    private void updateStyle() {
        switch(styleValue.get().toLowerCase()) {
            case "liquidbounce":
                LegacyUiLaunchOption.clickGui.style = new LiquidBounceStyle();
                break;
            case "null":
                LegacyUiLaunchOption.clickGui.style = new NullStyle();
                break;
            case "null2":
                LegacyUiLaunchOption.clickGui.style = new NullStyle2();
                break;
            case "slowly":
                LegacyUiLaunchOption.clickGui.style = new SlowlyStyle();
                break;
            case "black":
                LegacyUiLaunchOption.clickGui.style = new BlackStyle();
                break;
            case "white":
                LegacyUiLaunchOption.clickGui.style = new WhiteStyle();
                break;
        }
    }

    @EventTarget(ignoreCondition = true)
    public void onPacket(final PacketEvent event) {
        final Packet packet = event.getPacket();

        if (packet instanceof S2EPacketCloseWindow && mc.currentScreen instanceof ClickGui) {
            event.cancelEvent();
        }
    }
}