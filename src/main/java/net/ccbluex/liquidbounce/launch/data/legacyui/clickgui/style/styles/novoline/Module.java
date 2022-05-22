package net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.style.styles.novoline;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.client.HUD;
import net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.style.styles.novoline.setting.Manager;
import net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.style.styles.novoline.setting.SettingType;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class Module {

    private final net.ccbluex.liquidbounce.features.module.Module module;
    public int yPerModule, y;
    public Tab tab;
    public boolean opened;
    public List<net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.style.styles.novoline.Setting> settings = new CopyOnWriteArrayList<>();

    public Module(net.ccbluex.liquidbounce.features.module.Module module, Tab tab) {
        this.module = module;
        this.tab = tab;
        for (net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.style.styles.novoline.setting.Setting setting : Manager.getSettingsByMod(module)) {
            settings.add(new net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.style.styles.novoline.Setting(setting, this));
        }
    }

    private double length = 3, anim = 5;
    private int alph = 0;

    float fraction = 0;
    float fractionBackground = 0;

    public void drawScreen(int mouseX, int mouseY) {
        // интерполяция цвета
        Minecraft instance = Minecraft.getMinecraft();
        int debugFPS = Minecraft.getDebugFPS();
        if (module.getState() && fraction < 1) {
            fraction += 0.0025 * (2000f / debugFPS);
        }
        if (!module.getState() && fraction > 0) {
            fraction -= 0.0025 * (2000f / debugFPS);
        }

        if (!module.getState()) {
            if (isHovered(mouseX, mouseY) && fractionBackground < 1) {
                fractionBackground += 0.0025 * (2000f / debugFPS);
            }
            if (!isHovered(mouseX, mouseY) && fractionBackground > 0) {
                fractionBackground -= 0.0025 * (2000f / debugFPS);
            }

        }

        fraction = MathHelper.clamp_float(fraction, 0.0F, 1.0F);
        fractionBackground = MathHelper.clamp_float(fractionBackground, 0.0F, 1F);


        if (yPerModule < getY()) {
            yPerModule = (int) (yPerModule + (getY() + 9 - yPerModule) * 0.1);
        } else if (yPerModule > getY()) {
            yPerModule = (int) (yPerModule + (getY() - yPerModule) * 0.1);
        }


        y = (int) (tab.getPosY() + 15);
        for (Module tabModule : tab.getModules()) {
            if (tabModule == this) {
                break;
            } else {
                y += tabModule.yPerModule;
            }
        }

        HUD hud = (HUD) LiquidBounce.moduleManager.getModule(HUD.class);
        Color colorHUD = new Color(0xFF8A8AFF);
        Gui.drawRect((int) tab.getPosX(), y, (int) tab.getPosX() + 100, y + yPerModule, new Color(40, 40, 40, 255).getRGB());
        if (!module.getState() && fraction == 0) {
            Gui.drawRect((int) tab.getPosX(), y, (int) tab.getPosX() + 100, y + 14, interpolateColor(new Color(40, 40, 40, 255), new Color(29, 29, 29, 255), MathHelper.clamp_float(fractionBackground, 0, 1)));
        } else {
            Gui.drawRect((int) tab.getPosX(), y, (int) tab.getPosX() + 100, y + 14, interpolateColor(new Color(40, 40, 40, 255), colorHUD, MathHelper.clamp_float(fraction, 0, 1)));
        }
        Fonts.fontMiSansNormal30.drawString(module.getName(), tab.getPosX() + 2, (float) (y + 4), 0xffffffff, true);

        if (!settings.isEmpty()) {
            double val = debugFPS / 8.3;
            if (opened && length > -3) {
                length -= 3 / val;
            } else if (!opened && length < 3) {
                length += 3 / val;
            }
            if (opened && anim < 8) {
                anim += 3 / val;
            } else if (!opened && anim > 5) {
                anim -= 3 / val;
            }
            RenderUtils.drawArrow(tab.getPosX() + 92, y + anim, 2, 0xffffffff, length);
        }
        if (opened || yPerModule != 14) {
            ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
            if (yPerModule != getY() && scaledResolution.getScaleFactor() != 1) {
                GL11.glScissor(0,
                        scaledResolution.getScaledHeight() * 2 - y * 2 - yPerModule * 2,scaledResolution.getScaledWidth() * 2,
                        yPerModule * 2);
                GL11.glEnable(GL11.GL_SCISSOR_TEST);
                settings.stream().filter(s -> s.setting.getSupplier() != null ? s.setting.getSupplier().get() : true).forEach(setting -> setting.drawScreen(mouseX, mouseY));
                GL11.glDisable(GL11.GL_SCISSOR_TEST);
                settings.stream().filter(s -> s.setting.getSupplier() != null && !s.setting.getSupplier().get()).forEach(setting -> setting.setPercent(0));
            } else {
                settings.stream().filter(s -> s.setting.getSupplier() != null ? s.setting.getSupplier().get() : true).forEach(setting -> setting.drawScreen(mouseX, mouseY));
            }
        } else {
            settings.forEach(setting -> setting.setPercent(0));
        }
    }

    private int interpolateColor(Color color1, Color color2, float fraction) {
        int red = (int) (color1.getRed() + (color2.getRed() - color1.getRed()) * fraction);
        int green = (int) (color1.getGreen() + (color2.getGreen() - color1.getGreen()) * fraction);
        int blue = (int) (color1.getBlue() + (color2.getBlue() - color1.getBlue()) * fraction);
        int alpha = (int) (color1.getAlpha() + (color2.getAlpha() - color1.getAlpha()) * fraction);
        try {
            return new Color(red, green, blue, alpha).getRGB();
        } catch (Exception ex) {
            return 0xffffffff;
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (opened) {
            settings.stream().filter(s -> s.setting.getSupplier() != null ? s.setting.getSupplier().get() : true).forEach(setting -> setting.keyTyped(typedChar, keyCode));
        }
    }

    public int getY() {
        if (opened) {
            int gay = 17;
            for (net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.style.styles.novoline.Setting setting : settings.stream().filter(s -> s.setting.getSupplier() != null ? s.setting.getSupplier().get() : true).collect(Collectors.toList())) {
                switch (setting.setting.getSettingType()) {
                    case COLOR_PICKER:
                    case TEXTBOX:
                    case CHECKBOX:
                    case COMBOBOX:
                    case SLIDER:
                        //case SELECTBOX:
                        //    if (setting.opened)
                        //        gay += 15 + setting.setting.getSelectBoxProperty().getAcceptableValues().size() * 17;
                        //    else
                        //        gay += 15;
                        gay+= 15;
                }
            }
            return gay;
        } else {
            return 14;
        }
    }

    private float alpha = 0;

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (isHovered(mouseX, mouseY)) {
            switch (mouseButton) {
                case 0:
                    module.toggle();
                    break;
                case 1:
                    if (!Manager.getSettingsByMod(module).isEmpty()) {
                        if (!opened)
                            tab.modules.forEach(module -> {
                                if (module.opened)
                                    module.opened = false;
                            });
                        opened = !opened;
                        for (net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.style.styles.novoline.setting.Setting setting : Manager.getSettingsByMod(module)) {
                            if (setting.getSettingType() == SettingType.TEXTBOX) {
                                setting.setTextHovered(false);
                            }

                        }
                    }
                    break;
            }

        }
        if (opened) {
            settings.stream().filter(s -> s.setting.getSupplier() != null ? s.setting.getSupplier().get() : true).forEach(setting -> {
                try {
                    setting.mouseClicked(mouseX, mouseY, mouseButton);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (opened) {
            settings.stream().filter(s -> s.setting.getSupplier() != null ? s.setting.getSupplier().get() : true).forEach(setting -> setting.mouseReleased(mouseX, mouseY, state));
        }
    }

    public boolean isHovered(int mouseX, int mouseY) {
        y = (int) (tab.getPosY() + 15);
        for (Module tabModule : tab.getModules()) {
            if (tabModule == this) {
                break;
            } else {
                y += tabModule.yPerModule;
            }
        }
        if (opened)
            return mouseX >= tab.getPosX() && mouseY >= y && mouseX <= tab.getPosX() + 101 && mouseY <= y + 14;
        return mouseX >= tab.getPosX() && mouseY >= y && mouseX <= tab.getPosX() + 101 && mouseY <= y + yPerModule;
    }

    private void update() {
    }

    public net.ccbluex.liquidbounce.features.module.Module getModule() {
        return module;
    }
}