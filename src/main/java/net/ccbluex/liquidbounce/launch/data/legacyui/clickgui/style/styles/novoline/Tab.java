package net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.style.styles.novoline;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.elements.ModuleElement;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.value.Value;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Tab {

    private final ModuleCategory enumModuleType;
    private float posX;
    private float posY;
    public boolean dragging, opened;
    public List<Module> modules = new CopyOnWriteArrayList<>();

    public Tab(ModuleCategory enumModuleType, float posX, float posY) {
        this.enumModuleType = enumModuleType;
        this.posX = posX;
        this.posY = posY;
        for (net.ccbluex.liquidbounce.features.module.Module module : LiquidBounce.moduleManager.getModuleInCategory(enumModuleType)) {
            modules.add(new Module(module, this));
        }
    }

    public void drawScreen(int mouseX, int mouseY) {
        Gui.drawRect((int) posX - 1, (int) posY, (int) posX + 101, (int) posY + 15, new Color(29, 29, 29, 255).getRGB());
        Fonts.fontMiSansNormal35.drawString(enumModuleType.name().charAt(0) + enumModuleType.name().substring(1).toLowerCase(), posX + 4, posY + 4, 0xffffffff, true);
        if (opened) {
            Gui.drawRect((int) posX - 1, (int) posY + 15, (int) posX + 101, (int) posY + 15 + getTabHeight() + 1, new Color(29, 29, 29, 255).getRGB());
            modules.forEach(module -> module.drawScreen(mouseX, mouseY));
        } else {
            modules.forEach(module -> module.yPerModule = 0);
        }
    }

    public int getTabHeight() {
        int height = 0;
        for (Module module : modules) {
            height += module.yPerModule;
        }
        return height;
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= posX && mouseY >= posY && mouseX <= posX + 101 && mouseY <= posY + 15;
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (opened) {
            modules.forEach(module -> module.mouseReleased(mouseX, mouseY, state));
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        modules.forEach(module -> module.keyTyped(typedChar, keyCode));
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (isHovered(mouseX, mouseY) && mouseButton == 1) {
            opened = !opened;
            if (opened) {
                for (Module module : modules) {
                    module.fraction = 0;
                }
            }
        }

        if (opened) {
            modules.forEach(module -> {
                try {
                    module.mouseClicked(mouseX, mouseY, mouseButton);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public float getPosY() {
        return posY;
    }

    public float getPosX() {
        return posX;
    }

    public List<Module> getModules() {
        return modules;
    }
}
