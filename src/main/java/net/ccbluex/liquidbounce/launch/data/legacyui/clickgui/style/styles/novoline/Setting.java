package net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.style.styles.novoline;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.client.HUD;
import net.ccbluex.liquidbounce.launch.data.legacyui.ClickGUIModule;
import net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.style.styles.novoline.setting.SettingType;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.style.styles.novoline.setting.Setting.incValue;

public class Setting {

    public net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.style.styles.novoline.setting.Setting setting;
    private Module module;
    public boolean opened;
    private final TimerUtil backSpace = new TimerUtil();
    public int height;
    public float percent = 0;

    public Setting(net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.style.styles.novoline.setting.Setting setting, Module module) {
        this.setting = setting;
        this.module = module;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public void drawScreen(int mouseX, int mouseY) {
        final int guiColor = ClickGUIModule.generateColor().getRGB();
        int y = getY();
        HUD hud = (HUD) LiquidBounce.moduleManager.getModule(HUD.class);
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        boolean scissor = scaledResolution.getScaleFactor() != 1;
        double clamp = MathHelper.clamp_double(Minecraft.getDebugFPS() / 30f, 1, 9999);

        switch (setting.getSettingType()) {
            case SLIDER:
                if (module.yPerModule == module.getY() && scissor) {
                    GL11.glPushMatrix();
                    GL11.glScissor((int) (module.tab.getPosX() * 2 + 1), 0, 197, 999999999);
                    GL11.glEnable(GL11.GL_SCISSOR_TEST);
                }

                double rounded = (int) (setting.getDouble() * 100.0D) / 100.0D;
                final double percentBar = (setting.getDouble() - ((Number) setting.getSliderNumber().getMinimum())
                        .doubleValue()) / (((Number) setting.getSliderNumber().getMaximum()).doubleValue() - ((Number) setting
                        .getSliderNumber().getMinimum()).doubleValue());

                percent = Math.max(0, Math.min(1, (float) (percent + (Math.max(0, Math.min(percentBar, 1)) - percent) * (0.2 / clamp))));
                Gui.drawRect((int) module.tab.getPosX() + 1, y + 3, (int) module.tab.getPosX() + 99, y + 14, new Color(0, 0, 0, 50).getRGB());
                Gui.drawRect((int) module.tab.getPosX() + 1, y + 3, (int) (module.tab.getPosX() + 1 + 98 * percent), y + 14, guiColor);
                Fonts.fontMiSansNormal35.drawString(setting.getDisplayName() + " " + rounded, module.tab.getPosX() + 4, y + 5.5f, 0xffffffff, true);

                if (this.dragging) {
                    double difference = ((Number) setting.getSliderNumber().getMaximum()).doubleValue() - ((Number) setting.getSliderNumber().getMinimum()).doubleValue();
                    double value = ((Number) setting.getSliderNumber().getMinimum()).doubleValue() +
                            MathHelper.clamp_double((mouseX - (module.tab.getPosX() + 1)) / 99, 0, 1) * difference;
                    double set = incValue(value, setting.getIncrement());

                    setting.setSlider(set);
                    //EventManager.call(new SettingEvent(module.getModule(), setting.getName(), setting.getSliderNumber()));
                }

                if (module.yPerModule == module.getY() && scissor) {
                    GL11.glDisable(GL11.GL_SCISSOR_TEST);
                    GL11.glPopMatrix();
                }

                break;
            case CHECKBOX:
                Gui.drawRect((int) module.tab.getPosX() + 89, y + 4, (int) module.tab.getPosX() + 99, y + 14, new Color(0, 0, 0, 50).getRGB());
                if (setting.getCheckBoxValue()) {
                    RenderUtils.drawCheck(module.tab.getPosX() + 91, y + 8.5f, 2, guiColor);
                }

                Fonts.fontMiSansNormal35.drawString(setting.getDisplayName(), module.tab.getPosX() + 4, y + 5.5f,
                        new Color(227, 227, 227, 255).getRGB(), true);
                break;

            case COMBOBOX:
                Fonts.fontMiSansNormal35.drawString(setting.getDisplayName(), module.tab.getPosX() + 3, (float) (y + 6),
                        0xffffffff, true);
                Fonts.fontMiSansNormal35.drawString(setting.getComboBoxValue().toUpperCase(),
                        module.tab.getPosX() + 97 - Fonts.fontMiSansNormal35.getStringWidth(setting.getComboBoxValue().toUpperCase()), y + 7f,
                        new Color(255, 255, 255, 255).getRGB(), true);
                break;

            //case SELECTBOX:
            //    int h = 0;
//
            //    for (String value : setting.getSelectBoxProperty().getAcceptableValues()) {
            //        h += 17;
            //    }
//
            //    h += 2;
//
            //    Gui.drawRect(module.tab.getPosX() + 1, y + 3, module.tab.getPosX() + 99, y + 14 + (opened ? h : 0), new Color(0, 0, 0, 50).getRGB());
            //    String displayName = setting.getDisplayName() + (opened ? "" : "");
            //    SF_18.drawString(displayName, module.tab.getPosX() + 49.5 - SF_18.stringWidth(displayName) / 2, y + 5.5f, Color.WHITE.getRGB(), true);
//
            //    if (opened) {
            //        int yS = y + 17;
//
            //        for (String value : setting.getSelectBoxProperty().getAcceptableValues()) {
            //            int rgb = setting.getSelectBox().contains(value) ? 0xffffffff : new Color(174, 174, 174, 150).getRGB();
            //            SF_18.drawString(value, module.tab.getPosX() + 15, (float) (yS + 6), rgb, true);
            //            SF_26.drawString(".", module.tab.getPosX() + 9, yS, setting.getSelectBox().contains(value) ? 0xffffffff : new Color(174, 174, 174, 150).getRGB());
            //            yS += 17;
            //        }
            //    }
//
            //    break;

            case TEXTBOX:
                final String s = setting.getTextBoxValue();

                if (setting.isTextHovered() && Keyboard.isKeyDown(Keyboard.KEY_BACK) && this.backSpace.delay(100) && s.length() >= 1) {
                    setting.getTextBoxValue2().set(s.substring(0, s.length() - 1));
                    this.backSpace.reset();
                }

                Gui.drawRect((int) module.tab.getPosX() + 6, y + 16, (int) module.tab.getPosX() + 84, (int) (y + 16.5), new Color(195, 195, 195, 220).getRGB());
                Fonts.fontMiSansNormal30.drawString(setting.getDisplayName(), module.tab.getPosX() + 5.5f, y + 1.5f, new Color(227, 227, 227, 255).getRGB());

                if (Fonts.fontMiSansNormal30.getStringWidth(s) > 65) {
                    Fonts.fontMiSansNormal30.drawString(Fonts.fontMiSansNormal30.trimStringToWidth(s, 78, true), module.tab.getPosX() + 6, y + 10, 0xFFFFFFFF);
                } else {
                    Fonts.fontMiSansNormal30.drawString(s, module.tab.getPosX() + 6, y + 10, 0xFFFFFFFF);
                }

                break;

            case COLOR_PICKER:
                final Integer color = setting.getColor().get();

                final int currentRed = color >> 16 & 0xFF, // @off
                        currentGreen = color >> 8 & 0xFF,
                        currentBlue = color & 0xFF; // @on

                final float[] hsb = Color.RGBtoHSB(currentRed, currentGreen, currentBlue, new float[3]);

                if (setting.isColorPickerRainbow()) {
                    setting.setSeparatorHue((setting.getSeparatorHue() + 0.35F) % 99);
                } else if (this.dragging) {
                    final double selectedX = MathHelper.clamp_double(mouseX - module.tab.getPosX() - 1, 0.35d, 97);
                    final float normalizedValue = (float) (selectedX / 99);

                    switch (setting.getColorPickerMode()) {
                        case HUE:
                            setting.setSeparatorHue((int) selectedX);
                            setting.getColor().set(Color.getHSBColor(normalizedValue, hsb[1], hsb[2]).getRGB());
                            break;

                        case SATURATION:
                            setting.setSeparatorSaturation((int) selectedX);
                            setting.getColor().set(Color.getHSBColor(hsb[0], normalizedValue, hsb[2]).getRGB());
                            break;

                        case BRIGHTNESS:
                            setting.setSeparatorBrightness((int) selectedX);
                            setting.getColor().set(Color.getHSBColor(hsb[0], hsb[1], normalizedValue).getRGB());
                            break;
                    }
                }

                switch (setting.getColorPickerMode()) {
                    case HUE:
                        for (int max = 98, i = 0; i < max; i++) {
                            Gui.drawRect((int) module.tab.getPosX() + 1 + i, y + 3, (int) module.tab.getPosX() + 2 + i, y + 14,
                                    Color.getHSBColor(i / (float) max, hsb[1], hsb[2]).getRGB());
                        }

                        Fonts.fontMiSansNormal35.drawString(setting.getDisplayName(), module.tab.getPosX() + 4, y + 5.5f, 0xffffffff, true);

                        Gui.drawRect((int) (module.tab.getPosX() + 1 + setting.getSeparatorHue()), y + 3,
                                (int) (module.tab.getPosX() + 2 + setting.getSeparatorHue()), y + 14, Color.WHITE.getRGB());
                        break;

                    case SATURATION:
                        for (int max = 98, i = 0; i < max; i++) {
                            Gui.drawRect((int) module.tab.getPosX() + 1 + i, y + 3, (int) module.tab.getPosX() + 2 + i, y + 14,
                                    Color.getHSBColor(hsb[0], i / (float) max, hsb[2]).getRGB());
                        }

                        Fonts.fontMiSansNormal35.drawString(setting.getDisplayName(), module.tab.getPosX() + 4, y + 5.5f, 0xffffffff, true);

                        Gui.drawRect((int) (module.tab.getPosX() + 1 + setting.getSeparatorSaturation()), y + 3,
                                (int) (module.tab.getPosX() + 2 + setting.getSeparatorSaturation()), y + 14, Color.WHITE.getRGB());
                        break;

                    case BRIGHTNESS:
                        for (int max = 98, i = 0; i < max; i++) {
                            Gui.drawRect((int) module.tab.getPosX() + 1 + i, y + 3, (int) module.tab.getPosX() + 2 + i, y + 14,
                                    Color.getHSBColor(hsb[0], hsb[1], i / (float) max).getRGB());
                        }

                        Fonts.fontMiSansNormal35.drawString(setting.getDisplayName(), module.tab.getPosX() + 4, y + 5.5f, 0xffffffff, true);

                        Gui.drawRect((int) (module.tab.getPosX() + 1 + setting.getSeparatorBrightness()), y + 3,
                                (int) (module.tab.getPosX() + 2 + setting.getSeparatorBrightness()), y + 14, Color.WHITE.getRGB());
                        break;
                }
                break;
        }
        // RenderUtils.drawBorderedRect(module.tab.getPosX(),y,module.tab.getPosX() + 90,y + 17,1,0xffffffff,new Color(0, 0, 0, 0).getRGB());
    }

    private int getY() {
        int y = module.y + 14;
        for (Setting dropDownSetting : module.settings.stream().filter(s -> s.setting.getSupplier() != null ? s.setting.getSupplier().get() : true).collect(Collectors.toList())) {
            if (dropDownSetting == this) {
                break;
            } else {
                y += dropDownSetting.getHeight();
            }
        }
        return y;
    }

    public int getHeight() {
        //if (setting.getSettingType() == SettingType.SELECTBOX) {
        //    return opened ? 17 + setting.getSelectBoxProperty().getAcceptableValues().size() * 17 : 15;
        //} else {
        //    return 15;
        //}
        return 15;
    }

    private boolean dragging;

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (isHovered(mouseX, mouseY)) {
            switch (setting.getSettingType()) {
                case CHECKBOX:
                    if (mouseButton == 0) {
                        setting.getCheckBoxProperty().set(!setting.getCheckBoxProperty().get());
                        //EventManager.call(new SettingEvent(module.getModule(), setting.getName(), setting.getDisplayName(), setting.getCheckBoxProperty()));
                    }

                    break;
                case COMBOBOX:
                    if (mouseButton == 0) {
                        setting.setComboBoxValue(setting.getComboBox().getModes()
                                .get(setting.getComboBox().getModes().indexOf(setting.getComboBoxValue()) + 1 > setting.getComboBox().getModes().size() - 1 ? 0 : setting.getComboBox().getModes().indexOf(setting.getComboBoxValue()) + 1));
                        //EventManager.call(new SettingEvent(module.getModule(), setting.getName(), setting.getComboBoxProperty()));
                    }
                    break;
                //case SELECTBOX:
                //    if (mouseButton == 1 || mouseButton == 0) opened = !opened;
                //    break;
                case SLIDER:
                    if (mouseButton == 0) {
                        dragging = true;
                    }
                    break;
                case COLOR_PICKER:
                    if (mouseButton == 0) dragging = true;
            }
        }
        if (setting.getSettingType() == SettingType.TEXTBOX) {
            if (isHovered(mouseX, mouseY)) {
                setting.setTextHovered(!setting.isTextHovered());
            } else if (setting.isTextHovered()) {
                setting.setTextHovered(false);
            }
        }

        //if (setting.getSettingType() == SettingType.SELECTBOX) {
        //    if (opened && mouseX >= module.tab.getPosX() && mouseX <= module.tab.getPosX() + 90) {
        //        final List<String> acceptableValues = setting.getSelectBoxProperty().getAcceptableValues();
        //        for (int i = 0; i < acceptableValues.size(); i++) {
        //            final int v = getY() + 17 + i * 17;
        //            if (mouseY >= v && mouseY <= v + 17) {
        //                final String s = acceptableValues.get(i);
//
        //                if (setting.getSelectBoxProperty().contains(s)) {
        //                    setting.getSelectBoxProperty().remove(s);
        //                } else {
        //                    setting.getSelectBoxProperty().add(s);
        //                }
//
        //                EventManager.call(new SettingEvent(module.getModule(), setting.getName(), setting.getSelectBoxProperty()));
        //            }
        //        }
        //    }
        //}
        if (mouseButton == 1 && setting.getSettingType() == SettingType.COLOR_PICKER && isHovered(mouseX, mouseY)) {
            final net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.style.styles.novoline.setting.Setting.ColorPickerMode[] values = net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.style.styles.novoline.setting.Setting.ColorPickerMode
                    .values();
            final int i = (Arrays.binarySearch(values, setting.getColorPickerMode()) + 1) % values.length;

            if (setting.getColorPickedDisabledModes() == null) {
                setting.setColorPickerMode(values[i]);
            } else if (!setting.getColorPickedDisabledModes().isEmpty()) {
                net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.style.styles.novoline.setting.Setting.ColorPickerMode mode;

                for (int i1 = 0; i1 < values.length - 1; i1++) {
                    mode = values[(i + i1) % values.length];

                    if (!setting.getColorPickedDisabledModes().contains(mode)) {
                        setting.setColorPickerMode(values[(i1 + i) % values.length]);
                    }
                }
            }
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (setting.getSettingType() == SettingType.TEXTBOX) {
            if (setting.isTextHovered()) {
                if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_RETURN) {
                    setting.setTextHovered(false);
                } else if (!(keyCode == Keyboard.KEY_BACK) && keyCode != Keyboard.KEY_RCONTROL && keyCode != Keyboard.KEY_LCONTROL && keyCode != Keyboard.KEY_RSHIFT && keyCode != Keyboard.KEY_LSHIFT && keyCode != Keyboard.KEY_TAB && keyCode != Keyboard.KEY_CAPITAL && keyCode != Keyboard.KEY_DELETE && keyCode != Keyboard.KEY_HOME && keyCode != Keyboard.KEY_INSERT && keyCode != Keyboard.KEY_UP && keyCode != Keyboard.KEY_DOWN && keyCode != Keyboard.KEY_RIGHT && keyCode != Keyboard.KEY_LEFT && keyCode != Keyboard.KEY_LMENU && keyCode != Keyboard.KEY_RMENU && keyCode != Keyboard.KEY_PAUSE && keyCode != Keyboard.KEY_SCROLL && keyCode != Keyboard.KEY_END && keyCode != Keyboard.KEY_PRIOR && keyCode != Keyboard.KEY_NEXT && keyCode != Keyboard.KEY_APPS && keyCode != Keyboard.KEY_F1 && keyCode != Keyboard.KEY_F2 && keyCode != Keyboard.KEY_F3 && keyCode != Keyboard.KEY_F4 && keyCode != Keyboard.KEY_F5 && keyCode != Keyboard.KEY_F6 && keyCode != Keyboard.KEY_F7 && keyCode != Keyboard.KEY_F8 && keyCode != Keyboard.KEY_F9 && keyCode != Keyboard.KEY_F10 && keyCode != Keyboard.KEY_F11 && keyCode != Keyboard.KEY_F12) {
                    setting.getTextBoxValue2().set(setting.getTextBoxValue2().get() + typedChar);
                }
            }
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0) dragging = false;
    }

    //private boolean areHovered(int mouseX, int mouseY) {
    //    int yS = getY() + 17;
//
    //    if (opened) {
    //        for (String value : setting.getSelectBoxProperty().getAcceptableValues()) {
    //            yS += 17;
    //        }
    //    }
//
    //    return mouseX <= module.tab.getPosX() && mouseY <= yS && mouseX >= module.tab.getPosX() + 90 && mouseY <= yS + 17;
    //}

    public boolean isHovered(int mouseX, int mouseY) {
        int y = getY();
        switch (setting.getSettingType()) {
            case SLIDER:
            case COLOR_PICKER:
                return mouseX >= module.tab.getPosX() + 1 && mouseY >= y + 3 && mouseX <= module.tab
                        .getPosX() + 99 && mouseY <= y + 14;
            case CHECKBOX:
                return mouseX >= module.tab.getPosX() + 89 && mouseY >= y + 4 && mouseX <= module.tab
                        .getPosX() + 99 && mouseY <= y + 14;
            default:
                return mouseX >= module.tab.getPosX() && mouseY >= y && mouseX <= module.tab
                        .getPosX() + 90 && mouseY <= y + 17;
        }
    }
}