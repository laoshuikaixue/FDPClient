package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.misc.Teams;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.GLUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import org.lwjgl.opengl.GL11;

import java.awt.*;
@ModuleInfo(name = "NewNameTags", category = ModuleCategory.RENDER)
public class NewNameTags extends Module {
    private final IntegerValue colorRedValue = new IntegerValue("R", 255, 0, 255);
    private final IntegerValue colorGreenValue = new IntegerValue("G", 255, 0, 255);
    private final IntegerValue colorBlueValue = new IntegerValue("B", 255, 0, 255);
    private BoolValue invisible = new BoolValue("Invisible", false);
    private BoolValue uhc = new BoolValue("UHC", false);
    @EventTarget
    public void onRender(Render3DEvent event) {
        for (Object o : mc.theWorld.playerEntities) {
            RenderManager RenderManager = mc.getRenderManager();
            EntityPlayer p = (EntityPlayer) o;
            if(p != mc.thePlayer) {
                double pX = p.lastTickPosX + (p.posX - p.lastTickPosX) * mc.timer.renderPartialTicks
                        - RenderManager.renderPosX;
                double pY = p.lastTickPosY + (p.posY - p.lastTickPosY) * mc.timer.renderPartialTicks
                        - RenderManager.renderPosY;
                double pZ = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * mc.timer.renderPartialTicks
                        - RenderManager.renderPosZ;
                renderNameTag(p, p.getName(), pX, pY, pZ);
            }
        }
    }

    private void renderNameTag(EntityPlayer entity, String tag, double pX, double pY, double pZ) {
        if(entity.isInvisible() && !invisible.get()) {
            return;
        }
        if(entity.noClip && uhc.get()) {
            return;
        }
        FontRenderer fr = Fonts.fontMiSansNormal35;
        float size = mc.thePlayer.getDistanceToEntity(entity) / 6.0f;
        if(size < 0.8f) {
            size = 0.8f;
        }
        pY += (entity.isSneaking() ? 0.5D : 0.7D);
        float scale = size * 2.0f;
        scale /= 100f;
        tag = entity.getDisplayName().getUnformattedText();

        String bot = "";

        String team;
        Teams Teams = (Teams) LiquidBounce.moduleManager.getModule("Teams");
        if(Teams.isInYourTeam(entity) && Teams.getState()) {
            team = "\247b[TEAM]";
        } else {
            team = "";
        }

        RenderManager RenderManager = mc.getRenderManager();


        if((team + bot).equals("")) team = "\247a";
        String lol = team + bot + tag;
        String hp = "\2477HP:" + (int)entity.getHealth();
        GL11.glPushMatrix();
        GL11.glTranslatef((float) pX, (float) pY + 1.4F, (float) pZ);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-RenderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(RenderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-scale, -scale, scale);
        GLUtils.setGLCap(2896, false);
        GLUtils.setGLCap(2929, false);
        int width = Fonts.font40.getStringWidth(lol) / 2;
        GLUtils.setGLCap(3042, true);
        GL11.glBlendFunc(770, 771);

        drawBorderedRectNameTag(-width - 2, -(Fonts.font40.FONT_HEIGHT + 9), width + 2, 2.0F, 1.0F,
                RenderUtils.reAlpha(Color.BLACK.getRGB(), 0.3f), RenderUtils.reAlpha(Color.BLACK.getRGB(), 0.3f));
        GL11.glColor3f(1,1,1);
        fr.drawString(lol, -width, -(Fonts.font40.FONT_HEIGHT + 8), -1);
        fr.drawString(hp, -Fonts.font40.getStringWidth(hp) / 2, -(Fonts.font40.FONT_HEIGHT - 2), -1);
        int COLOR = new Color(colorRedValue.get(),colorGreenValue.get(), colorBlueValue.get()).getRGB();
        if(entity.getHealth() > 20) {
            COLOR = -65292;
        }
        float nowhealth = (float) Math.ceil(entity.getHealth() + entity.getAbsorptionAmount());
        float health = nowhealth / (entity.getMaxHealth() + entity.getAbsorptionAmount());

        RenderUtils.drawRect(width + health * width*2 - width*2 +2, 2f , -width -2, 0.9f, COLOR);
        GL11.glPushMatrix();
        int xOffset = 0;
        for (ItemStack armourStack : entity.inventory.armorInventory) {
            if (armourStack != null)
                xOffset -= 11;
        }
        ItemStack renderStack;
        if (entity.getHeldItem() != null) {
            xOffset -= 8;
            renderStack = entity.getHeldItem().copy();
            if ((renderStack.hasEffect())
                    && (((renderStack.getItem() instanceof ItemTool))
                    || ((renderStack.getItem() instanceof ItemArmor))))
                renderStack.stackSize = 1;
            renderItemStack(renderStack, xOffset, -35);
            xOffset += 20;
        }
        for (ItemStack armourStack : entity.inventory.armorInventory)
            if (armourStack != null) {
                ItemStack renderStack1 = armourStack.copy();
                if ((renderStack1.hasEffect()) && (((renderStack1.getItem() instanceof ItemTool))
                        || ((renderStack1.getItem() instanceof ItemArmor))))
                    renderStack1.stackSize = 1;
                renderItemStack(renderStack1, xOffset, -35);
                xOffset += 20;
            }
        GL11.glPopMatrix();
        GLUtils.revertAllCaps();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    public void renderItemStack(ItemStack stack, int x, int y) {
        GL11.glPushMatrix();
        GL11.glDepthMask(true);
        GlStateManager.clear(256);
        net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
        mc.getRenderItem().zLevel = -150.0F;
        whatTheFuckOpenGLThisFixesItemGlint();
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
        mc.getRenderItem().renderItemOverlays(Fonts.font40, stack, x, y);
        mc.getRenderItem().zLevel = 0.0F;
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.scale(0.5D, 0.5D, 0.5D);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        GL11.glPopMatrix();
    }

    private void whatTheFuckOpenGLThisFixesItemGlint() {
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
    }

    public void drawBorderedRectNameTag(final float x, final float y, final float x2, final float y2, final float l1, final int col1, final int col2) {
        RenderUtils.drawRect(x, y, x2, y2, col2);
        final float f = (col1 >> 24 & 0xFF) / 255.0f;
        final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        final float f4 = (col1 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glLineWidth(l1);
        GL11.glBegin(1);
        GL11.glVertex2d((double) x, (double) y);
        GL11.glVertex2d((double) x, (double) y2);
        GL11.glVertex2d((double) x2, (double) y2);
        GL11.glVertex2d((double) x2, (double) y);
        GL11.glVertex2d((double) x, (double) y);
        GL11.glVertex2d((double) x2, (double) y);
        GL11.glVertex2d((double) x, (double) y2);
        GL11.glVertex2d((double) x2, (double) y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
}
