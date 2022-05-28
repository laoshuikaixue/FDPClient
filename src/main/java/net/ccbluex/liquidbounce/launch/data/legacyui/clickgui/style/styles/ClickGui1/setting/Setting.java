package net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.style.styles.ClickGui1.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.TimerUtil;
import net.ccbluex.liquidbounce.value.*;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import java.awt.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static net.ccbluex.liquidbounce.utils.render.RenderUtils.drawBorderedRect;

public class Setting {

    /* fields */
    private final String name;
    private final String displayName;
    private final SettingType settingType;
    private final Module parentModule;


    private Supplier<Boolean> supplier;

    private int x = 0;
    private int y = 0;
    private int offset = 30;
    private int widthm = 5;

    /* check box */
    private BoolValue checkBoxProperty;

    /* combo box */
    private ListValue comboBoxValue;
    private boolean opened = false;

    /* slider */
    private NumberValue sliderNumber;
    private double increment;
    private final double width = 70;
    private boolean dragging;

    /* text field */
    private String hint;
    private TextValue textBoxValue;
    private boolean textHovered;
    private final TimerUtil backspace = new TimerUtil();
    private final TimerUtil caretTimer = new TimerUtil();

    /* color picker */
    private ColorPickerMode colorPickerMode = ColorPickerMode.HUE;
    private boolean colorPickerRainbow;
    private long colorPickerLastClickTime;
    private ColorValue color;
    private float separatorHue, separatorSaturation, separatorBrightness;
    private Set<ColorPickerMode> colorPickedDisabledModes;

    public void update() {
        this.x = 268;
        this.y = 100 + this.offset + Manager.getSettingsByMod(this.parentModule).
                stream().
                filter(setting -> setting.getSupplier() != null ? setting.getSupplier().get() : true).
                collect(Collectors.toList()).
                indexOf(this) * 25;
        this.widthm = 145 + 105 + 190 - 18;
    }

    /* checkbox */
    public Setting(String name, String displayName, SettingType settingType, Module module,
                   BoolValue property) {
        this.name = name;
        this.displayName = displayName;
        this.settingType = settingType;
        this.parentModule = module;
        this.checkBoxProperty = property;
    }

    public Setting(String name, String displayName, SettingType settingType, Module module,
                   BoolValue property, Supplier<Boolean> supplier) {
        this.name = name;
        this.displayName = displayName;
        this.settingType = settingType;
        this.parentModule = module;
        this.checkBoxProperty = property;
        this.supplier = supplier;
    }

    public Setting(String name, String displayName, SettingType settingType, Module module,
                   boolean selectBoxValue) {
        this.name = name;
        this.displayName = displayName;
        this.settingType = settingType;
        this.parentModule = module;
        this.checkBoxProperty = new BoolValue(name, selectBoxValue);
    }

    /* combobox */
    public Setting(String name, String displayName, SettingType settingType, Module module, ListValue value) {
        this.name = name;
        this.displayName = displayName;
        this.settingType = settingType;
        this.parentModule = module;
        this.comboBoxValue = value;
    }

    public Setting(String name, String displayName, SettingType settingType, Module module,
                   ListValue value, Supplier<Boolean> supplier) {
        this.name = name;
        this.displayName = displayName;
        this.settingType = settingType;
        this.parentModule = module;
        this.comboBoxValue = value;
        this.supplier = supplier;
    }

	/*public Setting(String name, String displayName, SettingType settingType, AbstractModule module, String comboValue, List<String> options) {
		this.name = name;
		this.displayName = displayName;
		this.settingType = settingType;
		this.parentModule = module;
		this.comboBoxValue = new StringProperty(comboValue).acceptableValues(options);
	}*/

    /* slider */
    public Setting(String name, String displayName, SettingType settingType, Module module,
                   NumberValue<?> number, double increment) {
        this.name = name;
        this.displayName = displayName;
        this.settingType = settingType;
        this.parentModule = module;
        this.sliderNumber = number;
        this.increment = increment;
    }

    public Setting(String name, String displayName, SettingType settingType, Module module,
                   NumberValue<?> number, double increment, Supplier<Boolean> supplier) {
        this.name = name;
        this.displayName = displayName;
        this.settingType = settingType;
        this.parentModule = module;
        this.sliderNumber = number;
        this.increment = increment;
        this.supplier = supplier;
    }

    public Setting(String name, String displayName, SettingType settingType, Module module, double sliderValue,
                   double minValue, double maxValue, double increment) {
        this.name = name;
        this.displayName = displayName;
        this.settingType = settingType;
        this.parentModule = module;
        this.sliderNumber = new NumberValue(name, sliderValue, minValue, maxValue) {
            @Override
            public JsonElement toJson() {
                return new JsonPrimitive((Number) this.getValue());
            }

            @Override
            public void fromJson(@NotNull JsonElement element) {
                if (element.isJsonPrimitive()) {
                    this.setValue(element.getAsNumber());
                }
            }
        };
        this.increment = increment;
    }

    /* textbox */
    public Setting(String name, String displayName, SettingType settingType, Module module, String hint,
                   TextValue text) {
        this.name = name;
        this.displayName = displayName;
        this.settingType = settingType;
        this.parentModule = module;
        this.hint = hint;
        this.textBoxValue = text;
    }

    public Setting(String name, String displayName, SettingType settingType, Module module, String hint,
                   TextValue text, Supplier<Boolean> supplier) {
        this.name = name;
        this.displayName = displayName;
        this.settingType = settingType;
        this.parentModule = module;
        this.hint = hint;
        this.textBoxValue = text;
        this.supplier = supplier;
    }

    public Setting(String name, String displayName, SettingType settingType, Module module, String hint,
                   String text) {
        this.name = name;
        this.displayName = displayName;
        this.settingType = settingType;
        this.parentModule = module;
        this.hint = hint;
        this.textBoxValue = new TextValue(name, text);
    }

    /* color picker */
    public Setting(String name, String displayName, SettingType settingType, Module module, ColorValue color,
                   @Nullable EnumSet<ColorPickerMode> disabledModes) {
        this.name = name;
        this.displayName = displayName;
        this.settingType = settingType;
        this.parentModule = module;
        this.color = color;
        this.colorPickedDisabledModes = disabledModes;

        final float[] hsb = color.getHSB();
        this.separatorHue = hsb[0] * 70;
        this.separatorSaturation = hsb[1] * 70;
        this.separatorBrightness = hsb[2] * 70;
    }

    public Setting(String name, String displayName, SettingType settingType, Module module, ColorValue color,
                   @Nullable EnumSet<ColorPickerMode> disabledModes, Supplier<Boolean> supplier) {
        this.name = name;
        this.displayName = displayName;
        this.settingType = settingType;
        this.parentModule = module;
        this.color = color;
        this.colorPickedDisabledModes = disabledModes;

        final float[] hsb = color.getHSB();
        this.separatorHue = hsb[0] * 70;
        this.separatorSaturation = hsb[1] * 70;
        this.separatorBrightness = hsb[2] * 70;
        this.supplier = supplier;
    }

    public Setting(String name, String displayName, SettingType settingType, Module module, Integer color,
                   @Nullable EnumSet<ColorPickerMode> disabledModes) {
        this.name = name;
        this.displayName = displayName;
        this.settingType = settingType;
        this.parentModule = module;
        this.color = new ColorValue(name, color);
        this.colorPickedDisabledModes = disabledModes;
    }

    public Supplier<Boolean> getSupplier() {
        return supplier;
    }

    /* methods */
    public void drawScreen(int mouseX, int mouseY) {
        try {
            boolean isMaterial = false;
            final int oColor = new Color(0xFF8A8AFF).getRGB();

            switch (this.settingType) {
                case COMBOBOX: {
                    final boolean topHovered = isHovered(mouseX, mouseY);
                    final int col1 = topHovered ? oColor : 0x64000000;

                    Fonts.fontMiSansNormal35.drawString(this.displayName, this.x, this.y, 0xFFFFFFFF);
                    drawBorderedRect(this.widthm - 70, this.y - 2, this.widthm, this.y + 8, 1, col1,
                            0xFF2F2F2F);
                    Fonts.fontMiSansNormal35.drawCenteredString(this.comboBoxValue.get(), this.widthm - 35, this.y, 0xFFFFFFFF);


                    if (this.opened) {
                        final List<String> acceptableValues = Arrays.asList(this.comboBoxValue.getValues());

                        drawBorderedRect(this.widthm - 70, this.y + 10, this.widthm,
                                this.y + 10 + acceptableValues.size() * 11, 1, 0x64000000,
                                0xFF2F2F2F);

                        for (String option : acceptableValues) {
                            Fonts.fontMiSansNormal35.drawCenteredString(option, this.widthm - 35,
                                    this.y + 13 + acceptableValues.indexOf(option) * 11,
                                    getComboBoxValue().equalsIgnoreCase(option) ? oColor : 0xFFFFFFFF);
                        }
                    }
                    break;
                }
                case CHECKBOX: {
                    drawBorderedRect(this.widthm - 10, this.y - 2, this.widthm, this.y + 8, 1, 0x64000000,
                            0xFF2F2F2F);
                    if (checkBoxProperty.get())
                        RenderUtils.drawCheck(widthm - 8, y + 2, 2, oColor);
                    Fonts.fontMiSansNormal35.drawString(this.displayName, this.x, this.y, 0xFFFFFFFF);
                    break;
                }
                case SLIDER: {
                    final double percentBar = (((Number) this.sliderNumber.get()).doubleValue() - ((Number) this.sliderNumber
                            .getMinimum()).doubleValue()) / (((Number) this.sliderNumber.getMaximum()).doubleValue() - ((Number) this.sliderNumber.getMinimum()).doubleValue());

                    drawBorderedRect(this.widthm - 70, this.y + 2, this.widthm, this.y + 4, 1, 0x64000000,
                            0xFF2F2F2F);
                    Gui.drawRect(this.widthm - 70, this.y + 2, (int) (this.widthm - 70 + percentBar * this.width), this.y + 4, oColor);
                    RenderUtils.drawFilledCircle((int) (this.widthm - 70 + percentBar * this.width), this.y + 3, 2, new Color(0xffffffff));


                    Fonts.fontMiSansNormal35.drawString(this.displayName, this.x, this.y, 0xFFFFFFFF);
                    Fonts.fontMiSansNormal30.drawCenteredString(getValue() + "", (float) (this.widthm - 70 + this.width * percentBar),
                            this.y - 5, 0xFFFFFFFF);

                    if (this.dragging) {
                        final double difference = ((Number) this.sliderNumber.getMaximum()).doubleValue() - ((Number) this.sliderNumber
                                .getMinimum()).doubleValue(), //
                                value = ((Number) this.sliderNumber.getMinimum()).doubleValue() + MathHelper
                                        .clamp_double((mouseX - (this.widthm - 70)) / this.width, 0, 1) * difference;
                        double set = incValue(value, getIncrement());

                        setSlider(set);
                        //EventManager.call(new SettingEvent(parentModule, this.getName(), this.sliderNumber));
                    }

                    break;
                }
                case TEXTBOX: {
                    final String s = this.textBoxValue.get();

                    if (this.textHovered && Keyboard.isKeyDown(Keyboard.KEY_BACK) && this.backspace.delay(100) && s
                            .length() >= 1) {
                        this.textBoxValue.set(s.substring(0, s.length() - 1));
                        this.backspace.reset();
                    }

                    Fonts.fontMiSansNormal35.drawString(this.displayName, this.x, this.y, 0xFFFFFFFF);
                    drawBorderedRect(this.widthm - 70, this.y - 2, this.widthm, this.y + 8, 1,
                            isTextHovered() ? oColor : 0x64000000,
                            isMaterial ? new Color(22, 23, 26).getRGB() : 0xFF2F2F2F);

                    if (s.isEmpty()) {
                        if (isTextHovered()) {
                            if (this.caretTimer.delay(500)) {
                                Fonts.fontMiSansNormal35.drawString("|", this.widthm - 68, (float) (this.y - 0.5), 0x64FFFFFF);

                                if (this.caretTimer.delay(1000)) {
                                    this.caretTimer.reset();
                                }
                            }
                        }

                        Fonts.fontMiSansNormal35.drawString(this.hint, this.widthm - 65, this.y, 0x64FFFFFF);
                    }

                    if (Fonts.fontMiSansNormal35.getStringWidth(s) > 65) {
                        Fonts.fontMiSansNormal35.drawString(Fonts.fontMiSansNormal35.trimStringToWidth(s, 60, true), this.widthm - 68, this.y,
                                0xFFFFFFFF);
                    } else {
                        Fonts.fontMiSansNormal35.drawString(s, this.widthm - 68, this.y, 0xFFFFFFFF);
                    }

                    break;
                }
                case COLOR_PICKER: {
                    Fonts.fontMiSansNormal35.drawString(this.displayName, this.x, this.y, 0xFFFFFFFF);
                    drawBorderedRect(this.widthm - 70, this.y - 2, this.widthm, this.y + 8, 1,
                            isTextHovered() ? oColor : 0x64000000,
                            0xFF2F2F2F);

                    final Integer color = this.color.get();

                    final int currentRed = color >> 16 & 0xFF, // @off
                            currentGreen = color >> 8 & 0xFF,
                            currentBlue = color & 0xFF; // @on

                    final float[] hsb = Color.RGBtoHSB(currentRed, currentGreen, currentBlue, new float[3]);

                    if (this.colorPickerRainbow) {
                        this.separatorHue = (this.separatorHue + 0.35F) % 70;
                    } else if (this.dragging) {
                        final double selectedX = MathHelper.clamp_double(mouseX - this.widthm + 70, 0.35d, 70);
                        final float normalizedValue = (float) (selectedX / 70);

                        switch (this.colorPickerMode) {
                            case HUE:
                                this.separatorHue = (int) selectedX;
                                this.color.set(Color.getHSBColor(normalizedValue, hsb[1], hsb[2]).getRGB());
                                break;

                            case SATURATION:
                                this.separatorSaturation = (int) selectedX;
                                this.color.set(Color.getHSBColor(hsb[0], normalizedValue, hsb[2]).getRGB());
                                break;

                            case BRIGHTNESS:
                                this.separatorBrightness = (int) selectedX;
                                this.color.set(Color.getHSBColor(hsb[0], hsb[1], normalizedValue).getRGB());
                                break;
                        }
                    }

                    switch (this.colorPickerMode) {
                        case HUE:
                            for (int max = 70, i = 0; i < max; i++) {
                                Gui.drawRect(this.widthm - 70 + i, this.y - 2, this.widthm - 69 + i, this.y + 8,
                                        Color.getHSBColor(i / (float) max, hsb[1], hsb[2]).getRGB());
                            }

                            Gui.drawRect((int) (this.widthm - 70 + this.separatorHue), this.y - 2,
                                    (int) (this.widthm - 69 + this.separatorHue), this.y + 8, 0xFF000000);
                            break;

                        case SATURATION:
                            for (int max = 70, i = 0; i < max; i++) {
                                Gui.drawRect(this.widthm - 70 + i, this.y - 2, this.widthm - 69 + i, this.y + 8,
                                        Color.getHSBColor(hsb[0], i / (float) max, hsb[2]).getRGB());
                            }

                            Gui.drawRect((int) (this.widthm - 70 + this.separatorSaturation), this.y - 2,
                                    (int) (this.widthm - 69 + this.separatorSaturation), this.y + 8, 0xFF000000);
                            break;

                        case BRIGHTNESS:
                            for (int max = 70, i = 0; i < max; i++) {
                                Gui.drawRect(this.widthm - 70 + i, this.y - 2, this.widthm - 69 + i, this.y + 8,
                                        Color.getHSBColor(hsb[0], hsb[1], i / (float) max).getRGB());
                            }

                            Gui.drawRect((int) (this.widthm - 70 + this.separatorBrightness), this.y - 2,
                                    (int) (this.widthm - 69 + this.separatorBrightness), this.y + 8, 0xFF000000);
                            break;
                    }

                    // Gui.drawRect(x + 70, y + 20, x + 140, y + 40, this.color);
                    break;
                }
                default: {
                    throw new IllegalStateException("Unexpected value: " + this.settingType);
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            switch (this.settingType) {
                case COMBOBOX:
                    if (toggleOpened(mouseX, mouseY)) {
                        break;
                    }

                    if (this.opened //
                            && mouseX >= this.widthm - 70 // minimum x
                            && mouseX <= this.widthm // maximum x
                    ) {
                        for (int i = 0; i < this.comboBoxValue.getValues().length; i++) {
                            final int v = this.y + 10 + i * 11;

                            if (mouseY >= v && mouseY <= v + 11) {
                                this.comboBoxValue.set(this.comboBoxValue.getValues()[i]);
                                this.opened = false;
                                //EventManager.call(new SettingEvent(parentModule, this.getName(), comboBoxValue));
                                return true;
                            }
                        }
                    }
                    break;
                case CHECKBOX:
                    if (isHovered(mouseX, mouseY)) {
                        this.checkBoxProperty.set(!this.checkBoxProperty.get());
                        //EventManager.call(new SettingEvent(parentModule, this.getName(), this.getDisplayName(), checkBoxProperty));
                    }

                    break;

                case SLIDER:
                    if (isHovered(mouseX, mouseY)) this.dragging = true;
                    break;

                case TEXTBOX:
                    if (isHovered(mouseX, mouseY)) {
                        this.textHovered = !this.textHovered;
                    } else if (this.textHovered) {
                        this.textHovered = false;
                    }

                    break;

                case COLOR_PICKER:
                    if (isHovered(mouseX, mouseY)) {
						/*if(this.colorPickerMode == ColorPickerMode.HUE) {
							val systemTime = Minecraft.getSystemTime();

							if(systemTime - this.colorPickerLastClickTime <= 250) {
								this.colorPickerRainbow = true;
							} else {
								this.colorPickerLastClickTime = systemTime;
								this.colorPickerRainbow = false;
								this.dragging = true;
							}
						} else {}*/

                        this.dragging = true;
                    }

                    break;
            }
        } else if (mouseButton == 1 && this.settingType == SettingType.COLOR_PICKER && isHovered(mouseX, mouseY)) {
            final ColorPickerMode[] values = ColorPickerMode.values();
            final int i = (Arrays.binarySearch(values, this.colorPickerMode) + 1) % values.length;

            if (this.colorPickedDisabledModes == null) {
                this.colorPickerMode = values[i];
            } else if (!this.colorPickedDisabledModes.isEmpty()) {
                ColorPickerMode mode;

                for (int i1 = 0; i1 < values.length - 1; i1++) {
                    mode = values[(i + i1) % values.length];

                    if (!this.colorPickedDisabledModes.contains(mode)) {
                        this.colorPickerMode = values[(i1 + i) % values.length];
                    }
                }
            }
        }

        return false;
    }

    public Set<ColorPickerMode> getColorPickedDisabledModes() {
        return colorPickedDisabledModes;
    }

    public void setColorPickerMode(ColorPickerMode colorPickerMode) {
        this.colorPickerMode = colorPickerMode;
    }

    private boolean toggleOpened(int mouseX, int mouseY) {
        return isHovered(mouseX, mouseY);
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        this.dragging = false;
    }

    private boolean isHovered(int mouseX, int mouseY) {
        switch (this.settingType) { // @off
            case CHECKBOX:
                return mouseX >= this.widthm - 10 && mouseX <= this.widthm && mouseY >= this.y - 2 && mouseY <= this.y + 8;
            case COMBOBOX:
            case COLOR_PICKER:
            case TEXTBOX:
                return mouseX >= this.widthm - 70 && mouseX <= this.widthm && mouseY >= this.y - 2 && mouseY <= this.y + 8;
            case SLIDER:
                return mouseX >= this.widthm - 70 && mouseX <= this.widthm - 70 + this.width && mouseY >= this.y + 2 && mouseY <= this.y + 8;
            default:
                return false;
        } // @on
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (this.settingType == SettingType.TEXTBOX) {
            if (this.textHovered) {
                if (keyCode == Keyboard.KEY_ESCAPE) {
                    this.textHovered = false;
                } else if (!(keyCode == Keyboard.KEY_BACK) && keyCode != Keyboard.KEY_RCONTROL && keyCode != Keyboard.KEY_LCONTROL && keyCode != Keyboard.KEY_RSHIFT && keyCode != Keyboard.KEY_LSHIFT && keyCode != Keyboard.KEY_TAB && keyCode != Keyboard.KEY_CAPITAL && keyCode != Keyboard.KEY_DELETE && keyCode != Keyboard.KEY_HOME && keyCode != Keyboard.KEY_INSERT && keyCode != Keyboard.KEY_UP && keyCode != Keyboard.KEY_DOWN && keyCode != Keyboard.KEY_RIGHT && keyCode != Keyboard.KEY_LEFT && keyCode != Keyboard.KEY_LMENU && keyCode != Keyboard.KEY_RMENU && keyCode != Keyboard.KEY_PAUSE && keyCode != Keyboard.KEY_SCROLL && keyCode != Keyboard.KEY_END && keyCode != Keyboard.KEY_PRIOR && keyCode != Keyboard.KEY_NEXT && keyCode != Keyboard.KEY_APPS && keyCode != Keyboard.KEY_F1 && keyCode != Keyboard.KEY_F2 && keyCode != Keyboard.KEY_F3 && keyCode != Keyboard.KEY_F4 && keyCode != Keyboard.KEY_F5 && keyCode != Keyboard.KEY_F6 && keyCode != Keyboard.KEY_F7 && keyCode != Keyboard.KEY_F8 && keyCode != Keyboard.KEY_F9 && keyCode != Keyboard.KEY_F10 && keyCode != Keyboard.KEY_F11 && keyCode != Keyboard.KEY_F12) {
                    this.textBoxValue.set(this.textBoxValue.getValue() + typedChar);
                }
            }
        } else if (keyCode == Keyboard.KEY_ESCAPE && this.settingType == SettingType.COMBOBOX && this.opened) {
            this.opened = false;
        }
    }


    private final DecimalFormat decimalFormat = new DecimalFormat("#.#");

    {
        this.decimalFormat.setRoundingMode(RoundingMode.CEILING);
    }

    public Object getValue() {
        switch (this.settingType) { // @off
            case CHECKBOX:
                return this.checkBoxProperty;
            case SLIDER:
                return this.decimalFormat.format(getDouble());
            case COMBOBOX:
                return this.comboBoxValue;
            default:
                return null;
        } // @on
    }

    //region Lombok
    public String getName() {
        return this.name;
    }

    public SettingType getSettingType() {
        return this.settingType;
    }

    public boolean isColorPickerRainbow() {
        return colorPickerRainbow;
    }

    public float getSeparatorHue() {
        return separatorHue;
    }

    public float getSeparatorBrightness() {
        return separatorBrightness;
    }

    public float getSeparatorSaturation() {
        return separatorSaturation;
    }

    public void setSeparatorBrightness(float separatorBrightness) {
        this.separatorBrightness = separatorBrightness;
    }

    public void setSeparatorHue(float separatorHue) {
        this.separatorHue = separatorHue;
    }

    public void setSeparatorSaturation(float separatorSaturation) {
        this.separatorSaturation = separatorSaturation;
    }

    public ColorPickerMode getColorPickerMode() {
        return colorPickerMode;
    }

    public ColorValue getColor() {
        return color;
    }

    public NumberValue getSliderNumber() {
        return sliderNumber;
    }

    public Module getParentModule() {
        return this.parentModule;
    }

    public String getTextBoxValue() {
        return this.textBoxValue.get();
    }

    public TextValue getTextBoxValue2() {
        return this.textBoxValue;
    }


    public void setTextBoxValue(String message) {
        this.textBoxValue.set(message);
    }

    public BoolValue getCheckBoxProperty() {
        return this.checkBoxProperty;
    }

    public Boolean getCheckBoxValue() {
        return this.checkBoxProperty.get();
    }

    public double getDouble() {
        return Math.round(((Number) this.sliderNumber.get()).doubleValue() / this.increment) * this.increment;
    }

    public float getFloat() {
        return (float) getDouble();
    }

    public int getInt() {
        return (int) getDouble();
    }

    public long getLong() {
        return (long) getDouble();
    }

    public String getComboBoxValue() {
        return this.comboBoxValue.get();
    }

    public ListValue getComboBoxProperty() {
        return this.comboBoxValue;
    }


    public void setComboBoxValue(String comboBoxValue) {
        this.comboBoxValue.set(comboBoxValue);
    }

    public ListValue getComboBox() {
        return this.comboBoxValue;
    }

    @SuppressWarnings("unchecked")
    public void setSliderValue(Number sliderValue) {
        if (this.sliderNumber instanceof IntegerValue) {
            this.sliderNumber.set(sliderValue.intValue());
        } else if (this.sliderNumber instanceof FloatValue) {
            this.sliderNumber.set(sliderValue.floatValue());
        }
    }

    public void setSelectBoxValue(boolean selectBoxValue) {
        this.checkBoxProperty.set(selectBoxValue);
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isOpened() {
        return this.opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public boolean isTextHovered() {
        return this.textHovered;
    }

    public void setTextHovered(boolean textHovered) {
        this.textHovered = textHovered;
    }

    public int getY() {
        return this.y;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public Color getColorPickerColor() {
        return this.color.getAwtColor();
    }

    public void setColorPickerColor(int color) {
        this.color.set(color);
    }

    public double getIncrement() {
        return this.increment;
    }

    public float[] getColorPickerHSB() {
        return this.color.getHSB();
    }

    public float[] getHSBforStupidMinecraft() {
        final Integer color = this.color.get();

        if (color == null) return Color.RGBtoHSB(0, 0, 0, new float[3]);

        return Color.RGBtoHSB(color & 0xFF, color >> 8 & 0xFF, color >> 16 & 0xFF, new float[3]);
    }

    public int getColorPickerInteger() {
        return this.color.get();
    }

    //endregion

    public enum ColorPickerMode {
        HUE,
        SATURATION,
        BRIGHTNESS

    }

    public void setSlider(double sliderValue) {
        setSliderValue(MathHelper.clamp_double(sliderValue, ((Number) this.sliderNumber.getMinimum()).doubleValue(),
                ((Number) this.sliderNumber.getMaximum()).doubleValue()));
    }

    public static double incValue(double val, double inc) {
        double one = 1.0 / inc;
        return Math.round(val * one) / one;
    }

}