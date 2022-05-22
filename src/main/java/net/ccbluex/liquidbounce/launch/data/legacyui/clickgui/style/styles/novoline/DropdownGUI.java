package net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.style.styles.novoline;

import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.style.styles.novoline.setting.Manager;
import net.ccbluex.liquidbounce.utils.ScaleUtils;
import net.ccbluex.liquidbounce.utils.render.BlurUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiChest;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DropdownGUI extends GuiScreen {
    private final List<Tab> tabs = new CopyOnWriteArrayList<>();
    int alphaBG = 0;
    private int dragX;
    private int dragY;

    @Override
    public void initGui() {
        float x = 10;
        alphaBG = 0;

        if (tabs.isEmpty()) {
            for (ModuleCategory value : ModuleCategory.values()) {
                tabs.add(new Tab(value, x, 10));
                x += 108;
            }
        }

        if (!(mc.currentScreen instanceof GuiChest) && mc.currentScreen != this) {
            for (Tab tab : tabs) {
                for (Module module : tab.modules) {
                    module.fraction = 0;

                    for (Setting setting : module.settings) {
                        setting.setPercent(0);
                    }
                }
            }
        }
        BlurUtils.INSTANCE.draw(0, 0, width, height, 3);
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GL11.glPushMatrix();
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int x = ScaleUtils.getScaledMouseCoordinates(mc, mouseX, mouseY)[0];
        int y = ScaleUtils.getScaledMouseCoordinates(mc, mouseX, mouseY)[1];
        ScaleUtils.scale(mc);
        for (Tab tab : tabs) {
            tab.drawScreen(x, y);
            if (tab.dragging) {
                tab.setPosX(dragX + x);
                tab.setPosY(dragY + y);
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
        GL11.glPopMatrix();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        int x = ScaleUtils.getScaledMouseCoordinates(mc, mouseX, mouseY)[0];
        int y = ScaleUtils.getScaledMouseCoordinates(mc, mouseX, mouseY)[1];
        for (Tab tab : tabs) {
            if (tab.isHovered(x, y) && mouseButton == 0) {
                if (!anyDragging()) {
                    tab.dragging = true;
                    dragX = (int) (tab.getPosX() - x);
                    dragY = (int) (tab.getPosY() - y);
                }
            }
            try {
                tab.mouseClicked(x, y, mouseButton);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private boolean anyDragging() {
        for (Tab tab : tabs) {
            if (tab.dragging) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE && !areAnyHovered()) {
            mc.displayGuiScreen(null);
            if (mc.currentScreen == null) {
                mc.setIngameFocus();
            }
        } else {
            tabs.forEach(tab -> tab.keyTyped(typedChar, keyCode));
        }

    }

    private boolean areAnyHovered() {
        for (net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.style.styles.novoline.setting.Setting setting : Manager.getSettingList()) {
            if (setting.isTextHovered()) {
                System.out.println(setting.getDisplayName());
                return true;
            }
        }
        return false;
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0) {
            tabs.forEach(tab -> tab.dragging = false);
        }

        tabs.forEach(tab -> tab.mouseReleased(mouseX, mouseY, state));
        super.mouseReleased(mouseX, mouseY, state);
    }
}