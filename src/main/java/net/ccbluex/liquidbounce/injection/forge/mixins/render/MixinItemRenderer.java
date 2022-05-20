/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/laoshuikaixue/FDPClient
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.client.Animations;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.render.AntiBlind;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {
    @Shadow
    private float prevEquippedProgress;

    @Shadow
    private float equippedProgress;

    @Shadow
    @Final
    private Minecraft mc;

    @Shadow
    protected abstract void rotateArroundXAndY(float angle, float angleY);

    @Shadow
    protected abstract void setLightMapFromPlayer(AbstractClientPlayer clientPlayer);

    @Shadow
    protected abstract void rotateWithPlayerRotations(EntityPlayerSP entityPlayerSP, float partialTicks);

    @Shadow
    private ItemStack itemToRender;

    @Shadow
    protected abstract void renderItemMap(AbstractClientPlayer clientPlayer, float pitch, float equipmentProgress, float swingProgress);

    @Shadow
    protected abstract void performDrinking(AbstractClientPlayer clientPlayer, float partialTicks);

    @Shadow
    protected abstract void doBlockTransformations();

    @Shadow
    protected abstract void doBowTransformations(float partialTicks, AbstractClientPlayer clientPlayer);

    @Shadow
    protected abstract void doItemUsedTransformations(float swingProgress);

    @Shadow
    public abstract void renderItem(EntityLivingBase entityIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform);

    @Shadow
    protected abstract void renderPlayerArm(AbstractClientPlayer clientPlayer, float equipProgress, float swingProgress);

    private final Animations animations = Animations.INSTANCE;

    /**
     * @author Liuli
     */
    @Overwrite
    private void transformFirstPersonItem(float equipProgress, float swingProgress) {
        doItemRenderGLTranslate();
        GlStateManager.translate(0.0F, equipProgress * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float f = MathHelper.sin(swingProgress * swingProgress * 3.1415927F);
        float f1 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927F);
        GlStateManager.rotate(f * -20.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f1 * -20.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(f1 * -80.0F, 1.0F, 0.0F, 0.0F);
        doItemRenderGLScale();
    }

    /**
     * @author Liuli
     */
    @Overwrite
    public void renderItemInFirstPerson(float partialTicks) {
        float f = 1.0F - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks);
        AbstractClientPlayer abstractclientplayer = mc.thePlayer;
        float f1 = abstractclientplayer.getSwingProgress(partialTicks);
        float f2 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
        float f3 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;
        this.rotateArroundXAndY(f2, f3);
        this.setLightMapFromPlayer(abstractclientplayer);
        this.rotateWithPlayerRotations((EntityPlayerSP) abstractclientplayer, partialTicks);
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();

        if (this.itemToRender != null) {
            final boolean displayBlocking = LiquidBounce.moduleManager.getModule(KillAura.class).getDisplayBlocking();
            float equipProgress = 1.0F - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks);
            final float swingProgress = abstractclientplayer.getSwingProgress(partialTicks);
            final float funny = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float) Math.PI);
            if (this.itemToRender.getItem() instanceof ItemMap) {
                this.renderItemMap(abstractclientplayer, f2, f, f1);
            } else if ((abstractclientplayer.isUsingItem() || (mc.gameSettings.keyBindUseItem.isKeyDown() && animations.getAnythingBlockValue().get())) || ((itemToRender.getItem() instanceof ItemSword || animations.getAnythingBlockValue().get()) && displayBlocking)) {
                switch((displayBlocking || animations.getAnythingBlockValue().get()) ? EnumAction.BLOCK : this.itemToRender.getItemUseAction()) {
                    case NONE:
                        this.transformFirstPersonItem(f, 0.0F);
                        break;
                    case EAT:
                    case DRINK:
                        this.performDrinking(abstractclientplayer, partialTicks);
                        this.transformFirstPersonItem(f, f1);
                        break;
                    case BLOCK:
                        GL11.glTranslated(animations.getTranslateXValue().get(), animations.getTranslateYValue().get(), animations.getTranslateZValue().get());
                        switch (animations.getBlockingModeValue().get()) {
                            case "1.7": {
                                this.transformFirstPersonItem(f, f1);
                                GlStateManager.translate(0, 0.3, 0);
                                this.doBlockTransformations();
                                break;
                            }
                            case "Throw": {
                                this.transformFirstPersonItem(equipProgress, swingProgress);
                                this.func_178103_d();
                                GlStateManager.translate(-0.3, 0.3F, 0);
                                break;
                            }
                            case "Spin": {
                                this.transformFirstPersonItem(equipProgress, 0.0F);
                                GlStateManager.translate(0, 0.2F, -1);
                                GlStateManager.rotate(-59, -1, 0, 3);
                                GlStateManager.rotate(-(System.currentTimeMillis() / 2 % 360), 1, 0, 0.0F);
                                GlStateManager.rotate(60.0F, 0.0F, 1.0F, 0.0F);
                                break;
                            }
                            case "Forward": {
                                this.transformFirstPersonItem(equipProgress, swingProgress - 1);
                                this.func_178103_d();
                                break;
                            }
                            case "Old": {
                                GlStateManager.translate(0, 0.18F, 0);
                                this.transformFirstPersonItem(equipProgress / 2.0F, swingProgress);
                                this.func_178103_d();
                                break;
                            }
                            case "Dortware": {
                                final float var1 = MathHelper.sin((float) (swingProgress * swingProgress * Math.PI - 3));
                                final float var = MathHelper.sin((float) (MathHelper.sqrt_float(swingProgress) * Math.PI));
                                this.transformFirstPersonItem(equipProgress, 1.0f);
                                GlStateManager.rotate(-var * 10, 0.0f, 15.0f, 200.0f);
                                GlStateManager.rotate(-var * 10f, 300.0f, var / 2.0f, 1.0f);
                                this.func_178103_d();
                                GL11.glTranslated(2.4, 0.3, 0.5);
                                GL11.glTranslatef(-2.10f, -0.2f, 0.1f);
                                GlStateManager.rotate(var1 * 13.0f, -10.0f, -1.4f, -10.0f);
                                break;
                            }
                            case "Dortware 2": {
                                this.transformFirstPersonItem(equipProgress, swingProgress);

                                GL11.glTranslatef(-0.35F, 0.1F, 0.0F);
                                GL11.glTranslatef(-0.05F, -0.1F, 0.1F);

                                this.func_178103_d();
                                break;
                            }
                            case "Cockless": {
                                this.transformFirstPersonItem(equipProgress / 1.5F, 0.0F);
                                this.func_178103_d();
                                GlStateManager.translate(-0.05F, 0.3F, 0.3F);
                                GlStateManager.rotate(-funny * 140.0F, 8.0F, 0.0F, 8.0F);
                                GlStateManager.rotate(funny * 90.0F, 8.0F, 0.0F, 8.0F);
                                break;
                            }
                            case "Shove": {
                                float nekker = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float) Math.PI);
                                GL11.glTranslated(0.03, -0.055, 0.05);
                                this.transformFirstPersonItem(equipProgress, 0.0f);
                                GL11.glTranslatef(0.1f, 0.4f, -0.1f);
                                GL11.glRotated(-nekker * 42.0f, nekker / 2.0f, 0.0, 9.0);
                                GL11.glRotated(-nekker * 50.0f, 0.8f, nekker / 2.0f, 0.0);
                                this.func_178103_d();
                                break;
                            }
                            case "Chill": {
                                GlStateManager.translate((0.9F / ((funny + 1.45f) / 5) / 3) + funny / 10, -0.75f - funny / 20, -0.71999997F);
                                GlStateManager.translate(0.3F, equipProgress * -0.6F, -1.0F);
                                GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);

                                final float x = MathHelper.sin(swingProgress * swingProgress * (float) Math.PI);
                                final float y = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float) Math.PI);

                                GlStateManager.rotate(y * -60.0F, 1.0F, 0.0F, 0.0F);
                                GlStateManager.rotate(x * 5.0F, 1.0F, -1.0F, 0.0F);
                                GlStateManager.rotate(y * 10.0F, 0.0F, 1.0F, 0.0F);
                                GlStateManager.scale(0.4F, 0.4F, 0.4F);

                                this.func_178103_d();

                                break;
                            }
                            case "Smooth": {
                                final float convertedSwingProgress = (float) Math.sin(Math.sqrt(swingProgress) * Math.PI);

                                this.transformFirstPersonItem(equipProgress, 0.0F);
                                final float angle = -convertedSwingProgress * 2;
                                final float y = -convertedSwingProgress * 2;
                                GL11.glTranslatef(0, y / 10 + 0.1f, 0);
                                GL11.glRotatef(y * 10, 0, 1, 0);
                                GL11.glRotatef(250, 0.2f, 1, -0.6f);
                                GL11.glRotatef(-10, 1, 0.5f, 1);
                                GL11.glRotatef(-angle * 20, 1, 0.5f, 1);
                                break;
                            }
                            case "Butter": {
                                GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
                                GlStateManager.translate(0.0F, equipProgress * -0.6F, 0.0F);
                                GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
                                GlStateManager.rotate(f * -20.0F, 0.0F, 1.0F, 0.0F);
                                GlStateManager.rotate(f1 * -20.0F, 0.0F, 0.0F, 1.0F);
                                GlStateManager.rotate(f1 * -20.0F, 1.0F, 0.0F, 0.0F);
                                GlStateManager.scale(0.4F, 0.4F, 0.4F);
                                this.func_178103_d();
                                break;
                            }
                            case "Stab": {
                                GlStateManager.translate(0.6f, 0.3f, -0.6f + -funny * 0.7);
                                GlStateManager.rotate(6090, 0.0f, 0.0f, 0.1f);
                                GlStateManager.rotate(6085, 0.0f, 0.1f, 0.0f);
                                GlStateManager.rotate(6110, 0.1f, 0.0f, 0.0f);
                                this.transformFirstPersonItem(0.0F, 0.0f);
                                this.func_178103_d();
                                break;
                            }
                            case "Small": {
                                GL11.glTranslated(0.5D, 0.0D, -0.5D);
                                this.transformFirstPersonItem(equipProgress - 0.2f, swingProgress);
                                this.func_178103_d();
                                break;
                            }
                            case "Wobble": {
                                GlStateManager.translate((0.9F / ((funny + 1.45f) / 5) / 3) + funny / 100, -0.75f - funny / 20, -0.71999997F);
                                GlStateManager.translate(0.3F, equipProgress * -0.6F, -1.0F);
                                GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
                                final float x = MathHelper.sin(swingProgress * swingProgress * (float) Math.PI);
                                final float y = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float) Math.PI);
                                GlStateManager.rotate(y * -70.0F, 1.0F, 0.0F, 0.0F);
                                GlStateManager.rotate(x * 110.0F, 1.0F, -1.0F, 0.0F);
                                GlStateManager.rotate(y * 10.0F, 0.0F, 1.0F, 0.0F);
                                GlStateManager.scale(0.65F, 0.65F, 0.65F);
                                this.func_178103_d();
                                break;
                            }
                            case "Chungus": {
                                final float f12 = MathHelper.sin((float) (MathHelper.sqrt_float(swingProgress) * Math.PI));
                                GlStateManager.translate(0, -Math.sin(equipProgress) / 3, 0);
                                GlStateManager.translate(0.66F, -0.8f, -0.9F);
                                GlStateManager.translate(0.35F, f12 * -0.17F, -0.4F);
                                GlStateManager.rotate(55.0F, 0.3F, 1.0F, 0.0F);
                                GlStateManager.rotate(f * -40.0F, 4.0F, 1.0F, 2.0F);
                                GlStateManager.rotate(f12 * -20.0F, 0.0F, 4.0F, 1.0F);
                                GlStateManager.rotate(f12 * -40.0F, 1.0F, 0.0F, 0.0F);
                                GlStateManager.scale(0.7F, 0.7F, 0.7F);
                                this.func_178103_d();
                                break;
                            }
                            case "Bitch Slap": {
                                this.transformFirstPersonItem(equipProgress, swingProgress);
                                GlStateManager.translate(3, 0, 0F);
                                this.func_178103_d();
                                break;
                            }
                            case "Leaked": {
                                this.transformFirstPersonItem(equipProgress, 0);
                                GlStateManager.scale(0.8f, 0.8f, 0.8f);
                                GlStateManager.translate(0, 0.1f, 0);
                                this.func_178103_d();
                                GlStateManager.rotate(funny * 35.0f / 2.0f, 0.0f, 1, 1.5f);
                                GlStateManager.rotate(-funny * 135.0f / 4.0f, 1, 1, 0.0f);
                                break;
                            }
                            case "Rise": {
                                this.transformFirstPersonItem(equipProgress, swingProgress);
                                GlStateManager.translate(0.3, 0, -0.3);
                                this.func_178103_d();
                                break;
                            }
                            case "Flush": {
                                final float owo = MathHelper.sin((float) (MathHelper.sqrt_float(swingProgress) * Math.PI));

                                GlStateManager.translate(0, -Math.sin(equipProgress) / 3, 0);
                                GlStateManager.translate(0.56F, -0.4F, -0.71999997F);
                                GlStateManager.translate(0.0F, 0.0F, 0.0F);
                                GlStateManager.rotate(70.0F, 0.0F, 1.0F, 0.0F);
                                GlStateManager.rotate(owo * -20.0F, 10.0F, 6.0F, 0.0F);
                                GlStateManager.scale(0.4, 0.4, 0.4);
                                this.func_178103_d();
                                break;
                            }
                            case "Whack": {
                                this.transformFirstPersonItem(equipProgress / 2.0F - 0.18F, 0.0F);
                                final float swing = MathHelper.sin((float) (MathHelper.sqrt_float(swingProgress) * Math.PI));
                                GL11.glRotatef(-swing * 80.0f / 5.0f, swing / 3.0f, -0.0f, 9.0f);
                                GL11.glRotatef(-swing * 40.0f, 8.0f, swing / 9.0f, -0.1f);
                                this.func_178103_d();
                                break;
                            }
                            case "Big Whack": {
                                this.transformFirstPersonItem(equipProgress + 0.0F, swingProgress);
                                final float swing = MathHelper.sin((float) (MathHelper.sqrt_float(swingProgress) * Math.PI));
                                GL11.glRotatef(-swing * 80.0f / 5.0f, swing / 3.0f, -0.0f, 9.0f);
                                GL11.glRotatef(-swing * 40.0f, 8.0f, swing / 9.0f, -0.1f);
                                this.func_178103_d();
                                break;
                            }
                            case "Spinny": {
                                this.transformFirstPersonItem(equipProgress, swingProgress);
                                this.func_178103_d();
                                final float var18 = MathHelper.sin(swingProgress * swingProgress * 5.1415925f);
                                GL11.glTranslatef(0.1f, -0.1f, 0.3f);
                                GlStateManager.translate(0.1f, -0.1f, 0.4f);
                                GlStateManager.rotate(var18 * 360, 215.0f, 1.4f, 525.0f);
                                break;
                            }
                            case "Inwards":
                                this.transformFirstPersonItem(equipProgress, swingProgress);
                                GlStateManager.translate(0.05F, 0.2F, 0.05F);
                                GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
                                GlStateManager.rotate(50.0F, 1.0F, 0.0F, 0.0F);
                                break;
                            case "Rhys": {
                                GlStateManager.translate(0.41F, -0.25F, -0.5555557F);
                                GlStateManager.translate(0.0F, 0, 0.0F);
                                GlStateManager.rotate(35.0F, 0f, 1.5F, 0.0F);
                                GlStateManager.translate(0, -Math.sin(equipProgress) / 3, 0);
                                final float lololol = MathHelper.sin(swingProgress * swingProgress / 64 * (float) Math.PI);
                                final float no = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float) Math.PI);
                                GlStateManager.rotate(lololol * -5.0F, 0.0F, 0.0F, 0.0F);
                                GlStateManager.rotate(no * -12.0F, 0.0F, 0.0F, 1.0F);
                                GlStateManager.rotate(no * -65.0F, 1.0F, 0.0F, 0.0F);
                                GlStateManager.scale(0.3F, 0.3F, 0.3F);
                                this.func_178103_d();
                                break;
                            }
                            case "Akrien": {
                                transformFirstPersonItem(f1, 0.0F);
                                doBlockTransformations();
                                break;
                            }
                            case "Avatar": {
                                avatar(f1);
                                doBlockTransformations();
                                break;
                            }
                            case "ETB": {
                                etb(f, f1);
                                doBlockTransformations();
                                break;
                            }
                            case "Exhibition": {
                                transformFirstPersonItem(f, 0.83F);
                                float f4 = MathHelper.sin(MathHelper.sqrt_float(f1) * 3.83F);
                                GlStateManager.translate(-0.5F, 0.2F, 0.2F);
                                GlStateManager.rotate(-f4 * 0.0F, 0.0F, 0.0F, 0.0F);
                                GlStateManager.rotate(-f4 * 43.0F, 58.0F, 23.0F, 45.0F);
                                doBlockTransformations();
                                break;
                            }
                            case "Push": {
                                push(f1);
                                doBlockTransformations();
                                break;
                            }
                            case "Reverse": {
                                transformFirstPersonItem(f1, f1);
                                doBlockTransformations();
                                GlStateManager.rotate(0.0F, 1.0F, 0.0F, 0.0F);
                                break;
                            }
                            case "Shield": {
                                jello(f1);
                                doBlockTransformations();
                                break;
                            }
                            case "SigmaNew": {
                                sigmaNew(0.2F, f1);
                                doBlockTransformations();
                                break;
                            }
                            case "SigmaOld": {
                                sigmaOld(f);
                                float var15 = MathHelper.sin(MathHelper.sqrt_float(f1) * 3.1415927F);
                                GlStateManager.rotate(-var15 * 55.0F / 2.0F, -8.0F, -0.0F, 9.0F);
                                GlStateManager.rotate(-var15 * 45.0F, 1.0F, var15 / 2.0F, -0.0F);
                                doBlockTransformations();
                                GL11.glTranslated(1.2D, 0.3D, 0.5D);
                                GL11.glTranslatef(-1.0F, mc.thePlayer.isSneaking() ? -0.1F : -0.2F, 0.2F);
                                GlStateManager.scale(1.2F, 1.2F, 1.2F);
                                break;
                            }
                            case "Slide": {
                                slide(f1);
                                doBlockTransformations();
                                break;
                            }
                            case "SlideDown": {
                                transformFirstPersonItem(0.2F, f1);
                                doBlockTransformations();
                                break;
                            }
                            case "Swong": {
                                transformFirstPersonItem(f / 2.0F, 0.0F);
                                GlStateManager.rotate(-MathHelper.sin(MathHelper.sqrt_float(f1) * 3.1415927F) * 40.0F / 2.0F, MathHelper.sqrt_float(f1) / 2.0F, -0.0F, 9.0F);
                                GlStateManager.rotate(-MathHelper.sqrt_float(f1) * 30.0F, 1.0F, MathHelper.sqrt_float(f1) / 2.0F, -0.0F);
                                doBlockTransformations();
                                break;
                            }
                            case "VisionFX": {
                                continuity(f1);
                                doBlockTransformations();
                                break;
                            }
                            case "Swank":{
                                GL11.glTranslated(-0.1, 0.15, 0.0);
                                this.transformFirstPersonItem(f / 0.15f, f1);
                                final float rot = MathHelper.sin(MathHelper.sqrt_float(f2) * 3.1415927f);
                                GlStateManager.rotate(rot * 30.0f, 2.0f, -rot, 9.0f);
                                GlStateManager.rotate(rot * 35.0f, 1.0f, -rot, -0.0f);
                                this.doBlockTransformations();
                                break;
                            }
                            case "Jello":{
                                this.transformFirstPersonItem(0.0f, 0.0f);
                                this.doBlockTransformations();
                                final int alpha = (int)Math.min(255L, ((System.currentTimeMillis() % 255L > 127L) ? Math.abs(Math.abs(System.currentTimeMillis()) % 255L - 255L) : (System.currentTimeMillis() % 255L)) * 2L);
                                GlStateManager.translate(0.3f, -0.0f, 0.4f);
                                GlStateManager.rotate(0.0f, 0.0f, 0.0f, 1.0f);
                                GlStateManager.translate(0.0f, 0.5f, 0.0f);
                                GlStateManager.rotate(90.0f, 1.0f, 0.0f, -1.0f);
                                GlStateManager.translate(0.6f, 0.5f, 0.0f);
                                GlStateManager.rotate(-90.0f, 1.0f, 0.0f, -1.0f);
                                GlStateManager.rotate(-10.0f, 1.0f, 0.0f, -1.0f);
                                GlStateManager.rotate(abstractclientplayer.isSwingInProgress ? (-alpha / 5.0f) : 1.0f, 1.0f, -0.0f, 1.0f);
                                break;
                            }
                            case "HSlide":{
                                transformFirstPersonItem(f1!=0?Math.max(1-(f1*2),0)*0.7F:0, 1F);
                                doBlockTransformations();
                                break;
                            }
                            case "None":{
                                transformFirstPersonItem(0F,0F);
                                doBlockTransformations();
                                break;
                            }
                            case "Rotate":{
                                rotateSword(f1);
                                break;
                            }
                            case "Liquid": {
                                this.transformFirstPersonItem(f + 0.1F, f1);
                                this.doBlockTransformations();
                                GlStateManager.translate(-0.5F, 0.2F, 0.0F);
                                break;
                            }
                        }
                        break;
                    case BOW:
                        this.transformFirstPersonItem(f, 0.0F);
                        this.doBowTransformations(partialTicks, abstractclientplayer);
                }
            } else {
                this.doItemUsedTransformations(f1);
                this.transformFirstPersonItem(f, f1);
            }

            this.renderItem(abstractclientplayer, this.itemToRender, ItemCameraTransforms.TransformType.FIRST_PERSON);
        } else if (!abstractclientplayer.isInvisible()) {
            this.renderPlayerArm(abstractclientplayer, f, f1);
        }

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }

    private void doItemRenderGLTranslate(){
        GlStateManager.translate(animations.getItemPosXValue().get(), animations.getItemPosYValue().get(), animations.getItemPosZValue().get());
    }

    private void doItemRenderGLScale(){
        GlStateManager.scale(animations.getItemScaleValue().get(), animations.getItemScaleValue().get(), animations.getItemScaleValue().get());
    }

    private void sigmaOld(float f) {
        doItemRenderGLTranslate();
        GlStateManager.translate(0.0F, f * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(0F, 0.0F, 1.0F, 0.2F);
        GlStateManager.rotate(0F, 0.2F, 0.1F, 1.0F);
        GlStateManager.rotate(0F, 1.3F, 0.1F, 0.2F);
        doItemRenderGLScale();
    }

    //methods in LiquidBounce b73 Animation-No-Cross
    private void avatar(float swingProgress) {
        doItemRenderGLTranslate();
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float f = MathHelper.sin(swingProgress * swingProgress * 3.1415927F);
        float f2 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927F);
        GlStateManager.rotate(f * -20.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f2 * -20.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(f2 * -40.0F, 1.0F, 0.0F, 0.0F);
        doItemRenderGLScale();
    }

    private void slide(float var9) {
        doItemRenderGLTranslate();
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float var11 = MathHelper.sin(var9 * var9 * 3.1415927F);
        float var12 = MathHelper.sin(MathHelper.sqrt_float(var9) * 3.1415927F);
        GlStateManager.rotate(var11 * 0.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(var12 * 0.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(var12 * -40.0F, 1.0F, 0.0F, 0.0F);
        doItemRenderGLScale();
    }

    private void rotateSword(float f1){
        genCustom(0.0F, 0.0F);
        doBlockTransformations();
        GlStateManager.translate(-0.5F, 0.2F, 0.0F);
        GlStateManager.rotate(MathHelper.sqrt_float(f1) * 10.0F * 40.0F, 1.0F, -0.0F, 2.0F);
    }

    private void genCustom(float p_178096_1_, float p_178096_2_) {
        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.translate(0.0F, p_178096_1_ * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float var3 = MathHelper.sin(p_178096_2_ * p_178096_2_ * 3.1415927F);
        float var4 = MathHelper.sin(MathHelper.sqrt_float(p_178096_2_) * 3.1415927F);
        GlStateManager.rotate(var3 * -34.0F, 0.0F, 1.0F, 0.2F);
        GlStateManager.rotate(var4 * -20.7F, 0.2F, 0.1F, 1.0F);
        GlStateManager.rotate(var4 * -68.6F, 1.3F, 0.1F, 0.2F);
        GlStateManager.scale(0.4F, 0.4F, 0.4F);
    }


    private void jello(float var12) {
        doItemRenderGLTranslate();
        GlStateManager.rotate(48.57F, 0.0F, 0.24F, 0.14F);
        float var13 = MathHelper.sin(var12 * var12 * 3.1415927F);
        float var14 = MathHelper.sin(MathHelper.sqrt_float(var12) * 3.1415927F);
        GlStateManager.rotate(var13 * -35.0F, 0.0F, 0.0F, 0.0F);
        GlStateManager.rotate(var14 * 0.0F, 0.0F, 0.0F, 0.0F);
        GlStateManager.rotate(var14 * 20.0F, 1.0F, 1.0F, 1.0F);
        doItemRenderGLScale();
    }

    private void continuity(float var10) {
        doItemRenderGLTranslate();
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float var12 = -MathHelper.sin(var10 * var10 * 3.1415927F);
        float var13 = MathHelper.cos(MathHelper.sqrt_float(var10) * 3.1415927F);
        float var14 = MathHelper.abs(MathHelper.sqrt_float((float) 0.1) * 3.1415927F);
        GlStateManager.rotate(var12 * var14 * 30.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(var13 * 0.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(var13 * 20.0F, 1.0F, 0.0F, 0.0F);
        doItemRenderGLScale();
    }

    public void sigmaNew(float var22, float var23) {
        doItemRenderGLTranslate();
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float var24 = MathHelper.sin(var23 * MathHelper.sqrt_float(var22) * 3.1415927F);
        float var25 = MathHelper.abs(MathHelper.sqrt_double(var22) * 3.1415927F);
        GlStateManager.rotate(var24 * 20.0F * var25, 0.0F, 1.0F, 1.0F);
        doItemRenderGLScale();
    }

    private void etb(float equipProgress, float swingProgress) {
        doItemRenderGLTranslate();
        GlStateManager.translate(0.0F, equipProgress * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float var3 = MathHelper.sin(swingProgress * swingProgress * 3.1415927F);
        float var4 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927F);
        GlStateManager.rotate(var3 * -34.0F, 0.0F, 1.0F, 0.2F);
        GlStateManager.rotate(var4 * -20.7F, 0.2F, 0.1F, 1.0F);
        GlStateManager.rotate(var4 * -68.6F, 1.3F, 0.1F, 0.2F);
        doItemRenderGLScale();
    }

    private void push(float idc) {
        doItemRenderGLTranslate();
        GlStateManager.translate(0.0F, (float) 0.1 * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float var3 = MathHelper.sin(idc * idc * 3.1415927F);
        float var4 = MathHelper.sin(MathHelper.sqrt_float(idc) * 3.1415927F);
        GlStateManager.rotate(var3 * -10.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.rotate(var4 * -10.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.rotate(var4 * -10.0F, 1.0F, 1.0F, 1.0F);
        doItemRenderGLScale();
    }

    /**
     * @author Liuli
     */
    @Redirect(method="renderFireInFirstPerson", at=@At(value="INVOKE", target="Lnet/minecraft/client/renderer/GlStateManager;color(FFFF)V"))
    private void renderFireInFirstPerson(float p_color_0_, float p_color_1_, float p_color_2_, float p_color_3_) {
        final AntiBlind antiBlind = LiquidBounce.moduleManager.getModule(AntiBlind.class);
        if(p_color_3_ != 1.0f && antiBlind.getState()){
            GlStateManager.color(p_color_0_, p_color_1_, p_color_2_, antiBlind.getFireEffectValue().get());
        }else{
            GlStateManager.color(p_color_0_, p_color_1_, p_color_2_, p_color_3_);
        }
    }

    private void func_178103_d() {
        GlStateManager.translate(-0.5F, 0.2F, 0.0F);
        GlStateManager.rotate(30.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-80.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(60.0F, 0.0F, 1.0F, 0.0F);
    }
}
