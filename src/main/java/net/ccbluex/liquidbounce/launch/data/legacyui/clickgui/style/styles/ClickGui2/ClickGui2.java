package net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.style.styles.ClickGui2;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.ccbluex.liquidbounce.utils.render.RenderUtil;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;

public class ClickGui2 extends GuiScreen implements GuiYesNoCallback {
    public static ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
    public static ModuleCategory currentModuleType = ModuleCategory.COMBAT;
    public static Module currentModule = LiquidBounce.moduleManager.getModuleInCategory(ClickGui2.currentModuleType).size() != 0 ? LiquidBounce.moduleManager.getModuleInCategory(ClickGui2.currentModuleType).get(0) : null;
    public static float startX = 100.0F;
    public static float startY = 100.0F;
    public int moduleStart = 0;
    public int valueStart = 0;
    boolean previousmouse = true;
    boolean mouse;
    public Opacity opacity = new Opacity(0);
    public int opacityx = 255;
    public float moveX = 0.0F;
    public float moveY = 0.0F;
    public static GameFontRenderer titlefont = Fonts.fontMiSansNormal35;
    private float animpos = 75.0F;
    private final AnimationHelper valueAnim = new AnimationHelper();
    float hue;
    public static int alpha;
    public static float scale = 1.0F;
    boolean bind = false;

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtils.drawImage(new ResourceLocation("fdpclient/misc/background.png"), 0, 0, (int) ((float) ClickGui2.sr.getScaledWidth() * (1.0F / ClickGui2.scale)), (int) ((float) ClickGui2.sr.getScaledHeight() * (1.0F / ClickGui2.scale)));
        if (Mouse.isButtonDown(0) && mouseX >= 5 && mouseX <= 50 && mouseY <= this.height - 5 && mouseY >= this.height - 50) {
            this.mc.displayGuiScreen(new GuiHudDesigner());
        }

        ClickGui2.sr = new ScaledResolution(this.mc);
        if (ClickGui2.alpha < 255) {
            ClickGui2.alpha += 5;
        }

        if (this.hue > 255.0F) {
            this.hue = 0.0F;
        }

        this.animpos = AnimationUtil.moveUD(this.animpos, 1.0F, 0.1F, 0.1F);
        GlStateManager.rotate(this.animpos, 0.0F, 0.0F, 0.0F);
        GlStateManager.translate(0.0F, this.animpos, 0.0F);
        if (this.isHovered(ClickGui2.startX, ClickGui2.startY - 25.0F, ClickGui2.startX + 400.0F, ClickGui2.startY + 25.0F, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            if (this.moveX == 0.0F && this.moveY == 0.0F) {
                this.moveX = (float) mouseX - ClickGui2.startX;
                this.moveY = (float) mouseY - ClickGui2.startY;
            } else {
                ClickGui2.startX = (float) mouseX - this.moveX;
                ClickGui2.startY = (float) mouseY - this.moveY;
            }

            this.previousmouse = true;
        } else if (this.moveX != 0.0F || this.moveY != 0.0F) {
            this.moveX = 0.0F;
            this.moveY = 0.0F;
        }

        this.opacity.interpolate((float) this.opacityx);
        RenderUtils.drawRect(ClickGui2.startX, ClickGui2.startY, ClickGui2.startX + 60.0F, ClickGui2.startY + 380.0F, (new Color(40, 40, 40, (int) this.opacity.getOpacity())).getRGB());
        RenderUtils.drawRect(ClickGui2.startX + 60.0F, ClickGui2.startY, ClickGui2.startX + 200.0F, ClickGui2.startY + 380.0F, (new Color(31, 31, 31, (int) this.opacity.getOpacity())).getRGB());
        RenderUtils.drawRect(ClickGui2.startX + 200.0F, ClickGui2.startY, ClickGui2.startX + 420.0F, ClickGui2.startY + 380.0F, (new Color(40, 40, 40, (int) this.opacity.getOpacity())).getRGB());

        int m;

        for (m = 0; m < ModuleCategory.values().length; ++m) {
            ModuleCategory[] i = ModuleCategory.values();

            GL11.glPushMatrix();
            if (i[m] != ClickGui2.currentModuleType) {
                RenderUtil.drawFilledCircle(ClickGui2.startX + 30.0F, ClickGui2.startY + 30.0F + (float) (ClickGui2.titlefont.getStringWidth("FDPClient") + 3) + (float) (m * 40), 15.0D, (new Color(56, 56, 56, (int) this.opacity.getOpacity())).getRGB(), 5);
            } else {
                RenderUtil.drawFilledCircle(ClickGui2.startX + 30.0F, ClickGui2.startY + 30.0F + (float) (ClickGui2.titlefont.getStringWidth("FDPClient") + 3) + (float) (m * 40), 15.0D, (new Color(47, 154, 241, (int) this.opacity.getOpacity())).getRGB(), 5);
            }

            GL11.glColor3f(1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();

            try {
                if (this.isCategoryHovered(ClickGui2.startX + 15.0F, ClickGui2.startY + 15.0F + (float) (ClickGui2.titlefont.getStringWidth("FDPClient") + 3) + (float) (m * 40), ClickGui2.startX + 45.0F, ClickGui2.startY + 45.0F + (float) (ClickGui2.titlefont.getStringWidth("FDPClient") + 3) + (float) (m * 40), mouseX, mouseY) && Mouse.isButtonDown(0)) {
                    ClickGui2.currentModuleType = i[m];
                    ClickGui2.currentModule = LiquidBounce.moduleManager.getModuleInCategory(ClickGui2.currentModuleType).size() != 0 ? LiquidBounce.moduleManager.getModuleInCategory(ClickGui2.currentModuleType).get(0) : null;
                    this.moduleStart = 0;
                }
            } catch (Exception exception) {
                System.err.println(exception);
            }
        }

        m = Mouse.getDWheel();
        if (this.isCategoryHovered(ClickGui2.startX + 60.0F, ClickGui2.startY + (float) (ClickGui2.titlefont.getStringWidth("FDPClient") + 3), ClickGui2.startX + 200.0F, ClickGui2.startY + (float) (ClickGui2.titlefont.getStringWidth("FDPClient") + 3) + 320.0F, mouseX, mouseY)) {
            if (m < 0 && this.moduleStart < LiquidBounce.moduleManager.getModuleInCategory(ClickGui2.currentModuleType).size() - 1) {
                ++this.moduleStart;
            }

            if (m > 0 && this.moduleStart > 0) {
                --this.moduleStart;
            }
        }

        if (this.isCategoryHovered(ClickGui2.startX + 200.0F, ClickGui2.startY + (float) (ClickGui2.titlefont.getStringWidth("FDPClient") + 3), ClickGui2.startX + 420.0F, ClickGui2.startY + (float) (ClickGui2.titlefont.getStringWidth("FDPClient") + 3) + 320.0F, mouseX, mouseY)) {
            if (m < 0 && this.valueStart < ClickGui2.currentModule.getValues().size() - 1) {
                ++this.valueStart;
            }

            if (m > 0 && this.valueStart > 0) {
                --this.valueStart;
            }
        }

        if (ClickGui2.currentModule == null) {
            Fonts.fontMiSansNormal35.drawStringWithShadow("Empty Modules", ClickGui2.startX + 80.0F, ClickGui2.startY + 130.0F, (new Color(255, 255, 255, 255)).getRGB());
        }

        Fonts.fontMiSansLight35.drawString(ClickGui2.currentModule == null ? ClickGui2.currentModuleType.toString() : ClickGui2.currentModuleType.toString() + "/" + ClickGui2.currentModule.getName(), ClickGui2.startX + 70.0F, ClickGui2.startY + 16.0F, (new Color(248, 248, 248)).getRGB());
        ClickGui2.titlefont.drawStringWithShadow(EnumChatFormatting.AQUA + "F" + EnumChatFormatting.WHITE + "DPClient ", ClickGui2.startX + 5.0F, ClickGui2.startY + 8.0F, 0);
        if (ClickGui2.currentModule != null) {
            float mY = ClickGui2.startY + 30.0F;

            int i;

            for (i = 0; i < LiquidBounce.moduleManager.getModuleInCategory(ClickGui2.currentModuleType).size(); ++i) {
                Module x = LiquidBounce.moduleManager.getModuleInCategory(ClickGui2.currentModuleType).get(i);

                if (mY > ClickGui2.startY + 300.0F) {
                    break;
                }

                if (i >= this.moduleStart) {
                    RenderUtils.drawRect(ClickGui2.startX + 75.0F, mY, ClickGui2.startX + 185.0F, mY + 2.0F, (new Color(40, 40, 40, (int) this.opacity.getOpacity())).getRGB());
                    Fonts.fontMiSansNormal35.drawString(x.getName(), ClickGui2.startX + 90.0F, mY + 9.0F, (new Color(255, 255, 255, (int) this.opacity.getOpacity())).getRGB());
                    if (!x.getState()) {
                        RenderUtil.drawFilledCircle(ClickGui2.startX + 75.0F, mY + 13.0F, 2.0D, (new Color(108, 108, 108, (int) this.opacity.getOpacity())).getRGB(), 5);
                    } else {
                        RenderUtil.drawFilledCircle(ClickGui2.startX + 75.0F, mY + 13.0F, 2.0D, (new Color(47, 154, 241, (int) this.opacity.getOpacity())).getRGB(), 5);
                    }

                    if (this.isSettingsButtonHovered(ClickGui2.startX + 90.0F, mY, ClickGui2.startX + 100.0F + (float) Fonts.fontMiSansNormal35.getStringWidth(x.getName()), mY + 8.0F + (float) Fonts.fontMiSansNormal35.getHeight(), mouseX, mouseY)) {
                        if (!this.previousmouse && Mouse.isButtonDown(0)) {
                            x.setState(!x.getState());

                            this.previousmouse = true;
                        }

                        if (!this.previousmouse && Mouse.isButtonDown(1)) {
                            this.previousmouse = true;
                        }
                    }

                    if (!Mouse.isButtonDown(0)) {
                        this.previousmouse = false;
                    }

                    if (this.isSettingsButtonHovered(ClickGui2.startX + 90.0F, mY, ClickGui2.startX + 100.0F + (float) Fonts.fontMiSansNormal35.getStringWidth(x.getName()), mY + 8.0F + (float) Fonts.fontMiSansNormal35.getHeight(), mouseX, mouseY) && Mouse.isButtonDown(1)) {
                        ClickGui2.currentModule = x;
                        this.valueStart = 0;
                    }

                    mY += 25.0F;
                }
            }

            mY = ClickGui2.startY + 30.0F;
            if (ClickGui2.currentModule.getValues().isEmpty()) {
                Fonts.fontMiSansNormal35.drawString("Empty Settings", ClickGui2.startX + 250.0F, ClickGui2.startY + 130.0F, (new Color(255, 255, 255, this.valueAnim.getAlpha())).getRGB());
            }

            for (i = 0; i < ClickGui2.currentModule.getValues().size() && mY <= ClickGui2.startY + 300.0F; ++i) {
                if (i >= this.valueStart) {
                    Value value = ClickGui2.currentModule.getValues().get(i);
                    double listValue;
                    double max;
                    double inc;
                    double valAbs;
                    double perc;
                    double valRel;
                    double val;
                    float f;

                    if (value instanceof IntegerValue) {
                        IntegerValue font = (IntegerValue) value;

                        f = ClickGui2.startX + 300.0F;
                        listValue = 68.0F * (float) (font.get().intValue() - (Integer) font.getMinimum()) / (float) (font.getMaximum().intValue() - font.getMinimum().intValue());
                        RenderUtils.drawRect(f - 2.0F, mY + 4.0F, (float) ((double) f + 75.0D), mY + 3.0F, (new Color(10, 50, 50, (int) this.opacity.getOpacity())).getRGB());
                        RenderUtils.drawRect(f - 6.0F, mY + 2.0F, (float) ((double) f + listValue + 6.5D), mY + 3.0F, (new Color(61, 141, 255, (int) this.opacity.getOpacity())).getRGB());
                        RenderUtils.drawRect((float) ((double) f + listValue + 2.0D), mY, (float) ((double) f + listValue + 7.0D), mY + 5.0F, (new Color(61, 141, 255, (int) this.opacity.getOpacity())).getRGB());
                        Fonts.fontMiSansNormal35.drawStringWithShadow(value.getName() + ": " + value.get(), ClickGui2.startX + 210.0F, mY + 1.0F, -1);
                        if (!Mouse.isButtonDown(0)) {
                            this.previousmouse = false;
                        }

                        if (this.isButtonHovered(f, mY - 2.0F, f + 100.0F, mY + 7.0F, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                            if (!this.previousmouse && Mouse.isButtonDown(0)) {
                                listValue = ((Number) ((IntegerValue) value).getMinimum()).doubleValue();
                                max = font.getMaximum();
                                inc = 1.0D;
                                valAbs = (double) mouseX - ((double) f + 1.0D);
                                perc = valAbs / 68.0D;
                                perc = Math.min(Math.max(0.0D, perc), 1.0D);
                                valRel = (max - listValue) * perc;
                                val = listValue + valRel;
                                val = (float) ((double) Math.round(val * (1.0D / inc)) / (1.0D / inc));
                                font.set((Number) Integer.valueOf((int) val));
                            }

                            if (!Mouse.isButtonDown(0)) {
                                this.previousmouse = false;
                            }
                        }

                        mY += 20.0F;
                    }

                    if (value instanceof FloatValue) {
                        FloatValue floatvalue = (FloatValue) value;

                        f = ClickGui2.startX + 300.0F;
                        listValue = 68.0F * ((Float) floatvalue.get() - (Float) floatvalue.getMinimum()) / ((Float) floatvalue.getMaximum() - floatvalue.getMinimum().floatValue());
                        RenderUtils.drawRect(f - 2.0F, mY + 4.0F, (float) ((double) f + 75.0D), mY + 3.0F, (new Color(10, 50, 50, (int) this.opacity.getOpacity())).getRGB());
                        RenderUtils.drawRect(f - 6.0F, mY + 2.0F, (float) ((double) f + listValue + 6.5D), mY + 3.0F, (new Color(61, 141, 255, (int) this.opacity.getOpacity())).getRGB());
                        RenderUtils.drawRect((float) ((double) f + listValue + 2.0D), mY, (float) ((double) f + listValue + 7.0D), mY + 5.0F, (new Color(61, 141, 255, (int) this.opacity.getOpacity())).getRGB());
                        Fonts.fontMiSansNormal35.drawStringWithShadow(value.getName() + ": " + value.get(), ClickGui2.startX + 210.0F, mY + 1.0F, -1);
                        if (!Mouse.isButtonDown(0)) {
                            this.previousmouse = false;
                        }

                        if (this.isButtonHovered(f, mY - 2.0F, f + 100.0F, mY + 7.0F, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                            if (!this.previousmouse && Mouse.isButtonDown(0)) {
                                listValue = ((Number) ((FloatValue) value).getMinimum()).doubleValue();
                                max = floatvalue.getMaximum().floatValue();
                                inc = 0.009999999776482582D;
                                valAbs = (double) mouseX - ((double) f + 1.0D);
                                perc = valAbs / 68.0D;
                                perc = Math.min(Math.max(0.0D, perc), 1.0D);
                                valRel = (max - listValue) * perc;
                                val = listValue + valRel;
                                val = (float) ((double) Math.round(val * (1.0D / inc)) / (1.0D / inc));
                                floatvalue.set((Number) (float) val);
                            }

                            if (!Mouse.isButtonDown(0)) {
                                this.previousmouse = false;
                            }
                        }

                        mY += 20.0F;
                    }

                    GameFontRenderer gamefontrenderer = Fonts.fontMiSansNormal35;

                    if (value instanceof BoolValue) {
                        float f1 = ClickGui2.startX + 320.0F;
                        float yyy = ClickGui2.startY + 240.0F;

                        gamefontrenderer.drawStringWithShadow("Bind", ClickGui2.startX + 220.0F, yyy + 100.0F, (new Color(170, 170, 170, ClickGui2.alpha)).getRGB());
                        RenderUtils.drawRect(f1 + 5.0F, yyy + 95.0F, f1 + 75.0F, yyy + 115.0F, this.isHovered(f1 + 2.0F, yyy + 95.0F, f1 + 78.0F, yyy + 115.0F, mouseX, mouseY) ? (new Color(80, 80, 80, ClickGui2.alpha)).getRGB() : (new Color(56, 56, 56, ClickGui2.alpha)).getRGB());
                        RenderUtils.drawRect(f1 + 2.0F, yyy + 98.0F, f1 + 78.0F, yyy + 112.0F, this.isHovered(f1 + 2.0F, yyy + 95.0F, f1 + 78.0F, yyy + 115.0F, mouseX, mouseY) ? (new Color(80, 80, 80, ClickGui2.alpha)).getRGB() : (new Color(56, 56, 56, ClickGui2.alpha)).getRGB());
                        RenderUtil.drawFilledCircle(f1 + 5.0F, yyy + 98.0F, 3.0D, this.isHovered(f1 + 2.0F, yyy + 95.0F, f1 + 78.0F, yyy + 115.0F, mouseX, mouseY) ? (new Color(80, 80, 80, ClickGui2.alpha)).getRGB() : (new Color(56, 56, 56, ClickGui2.alpha)).getRGB(), 5);
                        RenderUtil.drawFilledCircle(f1 + 5.0F, yyy + 112.0F, 3.0D, this.isHovered(f1 + 2.0F, yyy + 95.0F, f1 + 78.0F, yyy + 115.0F, mouseX, mouseY) ? (new Color(80, 80, 80, ClickGui2.alpha)).getRGB() : (new Color(56, 56, 56, ClickGui2.alpha)).getRGB(), 5);
                        RenderUtil.drawFilledCircle(f1 + 75.0F, yyy + 98.0F, 3.0D, this.isHovered(f1 + 2.0F, yyy + 95.0F, f1 + 78.0F, yyy + 115.0F, mouseX, mouseY) ? (new Color(80, 80, 80, ClickGui2.alpha)).getRGB() : (new Color(56, 56, 56, ClickGui2.alpha)).getRGB(), 5);
                        RenderUtil.drawFilledCircle(f1 + 75.0F, yyy + 112.0F, 3.0D, this.isHovered(f1 + 2.0F, yyy + 95.0F, f1 + 78.0F, yyy + 115.0F, mouseX, mouseY) ? (new Color(80, 80, 80, ClickGui2.alpha)).getRGB() : (new Color(56, 56, 56, ClickGui2.alpha)).getRGB(), 5);
                        gamefontrenderer.drawStringWithShadow(Keyboard.getKeyName(ClickGui2.currentModule.getKeyBind()), f1 + 40.0F - (float) (gamefontrenderer.getStringWidth(Keyboard.getKeyName(ClickGui2.currentModule.getKeyBind())) / 2), yyy + 103.0F, (new Color(255, 255, 255, ClickGui2.alpha)).getRGB());
                        f = ClickGui2.startX + 300.0F;
                        Fonts.fontMiSansNormal35.drawStringWithShadow(value.getName(), ClickGui2.startX + 210.0F, mY + 1.0F, -1);
                        RenderUtils.drawRect(f + 56.0F, mY, f + 76.0F, mY + 1.0F, (new Color(255, 255, 255, (int) this.opacity.getOpacity())).getRGB());
                        RenderUtils.drawRect(f + 56.0F, mY + 8.0F, f + 76.0F, mY + 9.0F, (new Color(255, 255, 255, (int) this.opacity.getOpacity())).getRGB());
                        RenderUtils.drawRect(f + 56.0F, mY, f + 57.0F, mY + 9.0F, (new Color(255, 255, 255, (int) this.opacity.getOpacity())).getRGB());
                        RenderUtils.drawRect(f + 77.0F, mY, f + 76.0F, mY + 9.0F, (new Color(255, 255, 255, (int) this.opacity.getOpacity())).getRGB());
                        if (((Boolean) value.get()).booleanValue()) {
                            RenderUtils.drawRect(f + 67.0F, mY + 2.0F, f + 75.0F, mY + 7.0F, (new Color(61, 141, 255, (int) this.opacity.getOpacity())).getRGB());
                        } else {
                            RenderUtils.drawRect(f + 58.0F, mY + 2.0F, f + 65.0F, mY + 7.0F, (new Color(150, 150, 150, (int) this.opacity.getOpacity())).getRGB());
                        }

                        if (this.isCheckBoxHovered(f + 56.0F, mY, f + 76.0F, mY + 9.0F, mouseX, mouseY)) {
                            if (!this.previousmouse && Mouse.isButtonDown(0)) {
                                this.previousmouse = true;
                                this.mouse = true;
                            }

                            if (this.mouse) {
                                value.set(Boolean.valueOf(!((Boolean) value.get()).booleanValue()));
                                this.mouse = false;
                            }
                        }

                        if (!Mouse.isButtonDown(0)) {
                            this.previousmouse = false;
                        }

                        mY += 20.0F;
                    }

                    if (value instanceof ListValue) {
                        f = ClickGui2.startX + 300.0F;
                        ListValue listvalue = (ListValue) value;

                        Fonts.fontMiSansNormal35.drawStringWithShadow(value.getName(), ClickGui2.startX + 210.0F, mY + 1.0F, -1);
                        RenderUtils.drawRect(f - 5.0F, mY - 5.0F, f + 90.0F, mY + 15.0F, (new Color(56, 56, 56, (int) this.opacity.getOpacity())).getRGB());
                        RenderUtil.drawBorderRect(f - 5.0F, mY - 5.0F, f + 90.0F, mY + 15.0F, (float) (new Color(101, 81, 255, (int) this.opacity.getOpacity())).getRGB(), 2);
                        Fonts.fontMiSansNormal35.drawStringWithShadow(((ListValue) value).get(), f + 40.0F - (float) (Fonts.fontMiSansNormal35.getStringWidth(listvalue.get()) / 2), mY + 1.0F, -1);
                        if (this.isStringHovered(f, mY - 5.0F, f + 100.0F, mY + 15.0F, mouseX, mouseY)) {
                            if (Mouse.isButtonDown(0) && !this.previousmouse) {
                                if (listvalue.getValues().length <= listvalue.getModeListNumber(listvalue.get()) + 1) {
                                    listvalue.set(listvalue.getValues()[0]);
                                } else {
                                    listvalue.set(listvalue.getValues()[listvalue.getModeListNumber(listvalue.get()) + 1]);
                                }

                                this.previousmouse = true;
                            }

                            if (!Mouse.isButtonDown(0)) {
                                this.previousmouse = false;
                            }
                        }

                        mY += 25.0F;
                    }
                }
            }
        }

    }

    public void keyTyped(char typedChar, int keyCode) {
        if (this.bind) {
            ClickGui2.currentModule.setKeyBind(keyCode);
            if (keyCode == 1) {
                ClickGui2.currentModule.setKeyBind(0);
            }

            this.bind = false;
        } else if (keyCode == 1) {
            mc.displayGuiScreen(null);
            if (mc.currentScreen == null) {
                mc.setIngameFocus();
            }
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        float mY = ClickGui2.startY + 30.0F;

        for (int x1 = 0; x1 < ClickGui2.currentModule.getValues().size() && mY <= ClickGui2.startY + 350.0F; ++x1) {
            if (x1 >= this.valueStart) {
                Value yyy = ClickGui2.currentModule.getValues().get(x1);

                if (yyy instanceof FloatValue) {
                    mY += 20.0F;
                }

                if (yyy instanceof BoolValue) {
                    mY += 20.0F;
                }

                if (yyy instanceof ListValue) {
                    mY += 25.0F;
                }
            }
        }

        float f = ClickGui2.startX + 320.0F;
        float f1 = ClickGui2.startY + 240.0F;

        if (this.isHovered(f + 2.0F, f1 + 95.0F, f + 78.0F, f1 + 115.0F, mouseX, mouseY)) {
            this.bind = true;
        }

        super.mouseClicked(mouseX, mouseY, button);
    }

    public boolean isStringHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
        return (float) mouseX >= f && (float) mouseX <= g && (float) mouseY >= y && (float) mouseY <= y2;
    }

    public boolean isSettingsButtonHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return (float) mouseX >= x && (float) mouseX <= x2 && (float) mouseY >= y && (float) mouseY <= y2;
    }

    public boolean isButtonHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
        return (float) mouseX >= f && (float) mouseX <= g && (float) mouseY >= y && (float) mouseY <= y2;
    }

    public boolean isCheckBoxHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
        return (float) mouseX >= f && (float) mouseX <= g && (float) mouseY >= y && (float) mouseY <= y2;
    }

    public boolean isCategoryHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return (float) mouseX >= x && (float) mouseX <= x2 && (float) mouseY >= y && (float) mouseY <= y2;
    }

    public boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return (float) mouseX >= x && (float) mouseX <= x2 && (float) mouseY >= y && (float) mouseY <= y2;
    }

    public void onGuiClosed() {
        this.opacity.setOpacity(0.0F);
    }

    public boolean doesGuiPauseGame() {
        return false;
    }
}
