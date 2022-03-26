/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/UnlegitMC/FDPClient/
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import de.enzaxd.viaforge.ViaForge;
import de.enzaxd.viaforge.protocol.ProtocolCollection;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.special.BungeeCordSpoof;
import net.ccbluex.liquidbounce.ui.client.GuiAntiForge;
import net.ccbluex.liquidbounce.ui.client.GuiProxySelect;
import net.ccbluex.liquidbounce.ui.client.GuiServerSpoof;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiSlider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMultiplayer.class)
public abstract class MixinGuiMultiplayer extends MixinGuiScreen {
    private GuiButton bungeeCordSpoofButton;
    private GuiSlider viaSlider;

    private void updatePortalText() {
        if (this.viaSlider == null)
            return;

        this.viaSlider.displayString = "Version: " + ProtocolCollection.getProtocolById(ViaForge.getInstance().getVersion()).getName();
    }

    private int getProtocolIndex(int id) {
        for (int i = 0; i < ProtocolCollection.values().length; i++)
            if (ProtocolCollection.values()[i].getVersion().getVersion() == id)
                return i;
        return -1;
    }
    @Inject(method = "initGui", at = @At("RETURN"))
    private void initGui(CallbackInfo callbackInfo) {
        buttonList.add(viaSlider = new GuiSlider(1337, width - 104, 8, 98, 20, "Version: ", "", 0, ProtocolCollection.values().length - 1, ProtocolCollection.values().length - 1 - getProtocolIndex(ViaForge.getInstance().getVersion()), false, true,
                guiSlider -> {
                    ViaForge.getInstance().setVersion(ProtocolCollection.values()[ProtocolCollection.values().length - 1 - guiSlider.getValueInt()].getVersion().getVersion());
                    this.updatePortalText();
                }));
        this.updatePortalText();
        buttonList.add(new GuiButton(997, 5, 8, 98, 20, "%ui.antiForge%"));
        buttonList.add(new GuiButton(998, width - 104, 8, 98, 20, "%ui.serverSpoof%"));
        buttonList.add(new GuiButton(999, width - 208, 8, 98, 20, "Proxy"));
//        buttonList.add(new GuiButton(999, width - 208, 8, 98, 20, "Proxy"));
    }
    @Inject(method = "actionPerformed", at = @At("HEAD"))
    private void actionPerformed(GuiButton button, CallbackInfo callbackInfo) {
        switch(button.id) {
            case 1337:
                BungeeCordSpoof.enabled = !BungeeCordSpoof.enabled;
                bungeeCordSpoofButton.displayString = (BungeeCordSpoof.enabled ? "§a" : "§c") + "BungeeCord Spoof";
                LiquidBounce.fileManager.saveAllConfigs();
                break;
            case 997:
                mc.displayGuiScreen(new GuiAntiForge((GuiScreen) (Object) this));
                break;
            case 998:
                mc.displayGuiScreen(new GuiServerSpoof((GuiScreen) (Object) this));
                break;
            case 999:
                mc.displayGuiScreen(new GuiProxySelect((GuiScreen) (Object) this));
                break;
        }
    }
}