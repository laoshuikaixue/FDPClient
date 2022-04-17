package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.utils.Colors;
import net.ccbluex.liquidbounce.utils.PlayerUtil;
import net.ccbluex.liquidbounce.utils.render.LnkRenderUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@ModuleInfo(name = "LnkTargetHUD", category = ModuleCategory.RENDER, array = false)
public class Lnktarget extends Module {
    private final IntegerValue customX = new IntegerValue("X", -150, -500, 500);
    private final IntegerValue customY = new IntegerValue("Y", 80, -500, 500);

    double animation = 0;

    final KillAura ka = (KillAura) LiquidBounce.moduleManager.getModule(KillAura.class);

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        ScaledResolution res = new ScaledResolution(mc);
        int x = res.getScaledWidth() / 2;
        int y = res.getScaledHeight() / 2;

        EntityLivingBase ent = ka.getTarget();

        if (ent == null) {
            return;
        }


        GlStateManager.pushMatrix();
        double width = mc.fontRendererObj.getStringWidth(ent.getName());
        width = PlayerUtil.getIncremental(width, -50);

        LnkRenderUtils.drawtargethudRect(customX.get() + x, customY.get() + y - 0.5, customX.get() + x + 80 + width, customY.get() + y + 37.0, Colors.getColor(0, 0, 0, 120), Colors.getColor(200, 255, 255));
        mc.fontRendererObj.drawStringWithShadow("\247l" + ent.getName(), customX.get() + x + 38, customY.get() + y + 3.0f, -1);

        BigDecimal DT = BigDecimal.valueOf(mc.thePlayer.getDistanceToEntity(ent));
        DT = DT.setScale(1, RoundingMode .HALF_UP);
        final
        double Dis = DT.doubleValue();
        //double Food = ent.fo.getFoodLevel();
        double Armor = ent.getTotalArmorValue();

        final float health = ent.getHealth();
        final float[] fractions = {0.0f, 0.5f, 1.0f};
        final Color[] colors = {Color.RED, Color.YELLOW, Color.GREEN};
        final float progress = health / ent.getMaxHealth();
        final Color customColor = (health >= 0.0f) ? Colors.blendColors(fractions, colors, progress).brighter() : Color.RED;

        if (width < 80.0) {
            width = 80.0;
        }

        if (width > 80.0) {
            width = 80.0;
        }

        final double healthLocation = width * progress;

        if (animation < healthLocation + 5) {
            animation++;
        }

        if (animation > healthLocation + 2) {
            animation--;
        }
        final Color color = new Color(1,255,1);
        final Color color1 = new Color(255,1,1);

        RenderUtils.drawGradientSideways(customX.get() + x + 37.5, customY.get() + y + 13.5, customX.get() + x + 37.5 + animation, customY.get() + y + 16.5, new Color(225, 50, 50).getRGB(), customColor.getRGB());
        RenderUtils.rectangleBordered(customX.get() + x + 37.0, customY.get() + y + 13.5, customX.get() + x + 40.0 + width, customY.get() + y + 15.5 + 1, 0.5, Colors.getColor(0, 0), Colors.getColor(0));
        RenderUtils.drawFace(customX.get() + x + 3, customY.get() + y + 2, 32, (AbstractClientPlayer) ent);

        GlStateManager.scale(0.5, 0.5, 0.5);
        final String str = "Dist>> " + Dis;
        mc.fontRendererObj.drawStringWithShadow(str, (customX.get() + x) * 2 + 76.0f, (customY.get() + y) * 2 + 40.0f, -1);
        mc.fontRendererObj.drawStringWithShadow(ent.getHealth() > mc.thePlayer.getHealth() ? "Losing" : "Winning",(customX.get()+x)*2+150f,(customY.get() + y) * 2 + 40.0f, ent.getHealth() > mc.thePlayer.getHealth() ? color1.getRGB() : color.getRGB());
        final String str2 = String.format("Yaw>> %s", (int) ent.rotationYaw + "   Armor>> " + Armor);
        mc.fontRendererObj.drawStringWithShadow(str2, (customX.get() + x) * 2 + 76.0f, (customY.get() + y) * 2 + 55.0f, -1);
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.popMatrix();
    }
}

