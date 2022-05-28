package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import java.util.Iterator;
import java.util.Objects;
import net.ccbluex.liquidbounce.event.AttackEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtil;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

@ModuleInfo(name = "DuelInfo", category = ModuleCategory.RENDER)
public class DuelInfo extends Module {
    private IntegerValue X = new IntegerValue("X", 15,0,1000);
    private IntegerValue Y = new IntegerValue("Y",150, 0, 1000);;
    public static ListValue mode = new ListValue("Mode", new String[]{"Single","Near","Remiaft"}, "Single");;
    float curHealthX1;
    float curAbsorptionAmountX1;
    float curHealthX2;
    float curAbsorptionAmountX2;
    int SelfHurt;
    int TargetHurt;
    int HurtEntity;
    boolean Hurted;
    private EntityPlayer FindedEntity;
    public DuelInfo() {
        this.curHealthX1 = 0.0F;
        this.curAbsorptionAmountX1 = 0.0F;
        this.curHealthX2 = 0.0F;
        this.curAbsorptionAmountX2 = 0.0F;
        this.SelfHurt = 0;
        this.TargetHurt = 0;
        this.HurtEntity = -1;
        this.Hurted = false;
    }

    public static int getHealthPotCount(EntityPlayer e) {
        int Count = 0;

        for(int i = 0; i < 45; ++i) {
            if(e.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                Item item = is.getItem();
                if(is.getItem() instanceof ItemPotion) {
                    ItemPotion pot = (ItemPotion)is.getItem();
                    if(!pot.getEffects(is).isEmpty()) {
                        PotionEffect effect = pot.getEffects(is).get(0);
                        int potionID = effect.getPotionID();
                        if(potionID == 6) {
                            Count += is.stackSize;
                        }
                    }
                }
            }
        }

        return Count;
    }

    public static int getSpeedPotCount(EntityPlayer e) {
        int Count = 0;

        for(int i = 0; i < 45; ++i) {
            if(e.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                Item item = is.getItem();
                if(is.getItem() instanceof ItemPotion) {
                    ItemPotion pot = (ItemPotion)is.getItem();
                    if(!pot.getEffects(is).isEmpty()) {
                        PotionEffect effect = pot.getEffects(is).get(0);
                        int potionID = effect.getPotionID();
                        if(potionID == 1) {
                            Count += is.stackSize;
                        }
                    }
                }
            }
        }

        return Count;
    }

    @EventTarget
    public void onRender2D(Render2DEvent e) {
        String Text = "";
        Fonts.fontMiSansNormal35.getStringWidth(Text);
        int MaxHealth = (int)mc.thePlayer.getMaxHealth();
        int Health = (int)mc.thePlayer.getHealth();
        int AbsorptionAmount = (int)mc.thePlayer.getAbsorptionAmount();
        if(this.curHealthX1 > (float)Health) {
            this.curHealthX1 = (float)((double)this.curHealthX1 - 0.2D);
        }

        if(this.curHealthX1 < (float)Health) {
            this.curHealthX1 = (float)((double)this.curHealthX1 + 0.2D);
        }

        if(this.curAbsorptionAmountX1 > (float)AbsorptionAmount) {
            this.curAbsorptionAmountX1 = (float)((double)this.curAbsorptionAmountX1 - 0.2D);
        }

        if(this.curAbsorptionAmountX1 < (float)AbsorptionAmount) {
            this.curAbsorptionAmountX1 = (float)((double)this.curAbsorptionAmountX1 + 0.2D);
        }

        RenderUtil.drawFastRoundedRect((float)this.X.get(), (float)this.Y.get(), (float)(this.X.get() + 20 + MaxHealth * 5 + AbsorptionAmount * 5 + 5), (float)(20 + this.Y.get() + 20 + 5 + 20), 2.0F, (new Color(0, 0, 0, 120)).getRGB());
        RenderUtils.drawImage(new ResourceLocation("fdpclient/misc/duelinfo/Head.png"), this.X.get() + 4, this.Y.get() + 5, 12, 12);
        Text = mc.thePlayer.getName();
        Fonts.fontMiSansNormal35.getStringWidth(Text);
        Fonts.fontMiSansNormal35.drawStringWithShadow(Text, this.X.get() + 20, this.Y.get() + 8, (new Color(0, 200, 0)).getRGB());
        RenderUtils.drawImage(new ResourceLocation("fdpclient/misc/duelinfo/Heart.png"), this.X.get() + 4, 20 + this.Y.get() + 4, 12, 12);
        int color = (new Color(250 - (int)(this.curHealthX1 / (float)MaxHealth * 10.0F * 25.0F), (int) (this.curHealthX1 / (float) MaxHealth * 10.0F * 25.0F), 0, 255)).getRGB();
        if(this.curHealthX1 > 0.5F) {
            RenderUtil.drawFastRoundedRect((float)(this.X.get() + 20), (float)(20 + this.Y.get() + 6), (float)(this.X.get() + 20) + this.curHealthX1 * 5.0F, (float)(20 + this.Y.get() + 20 - 6), 2.0F, color);
        } else if((float)Health < 0.5F && (float)Health != 0.0F) {
            this.curHealthX1 = 0.0F;
        }

        if((double)this.curAbsorptionAmountX1 > 0.5D) {
            RenderUtils.drawRect(this.X.get() + 20.0D + (double)(this.curHealthX1 * 5.0F), 20.0D + this.Y.get() + 6.0D, this.X.get() + 20.0D + (double)(this.curHealthX1 * 5.0F) - 4.0D, 20.0D + this.Y.get() + 20.0D - 6.0D, color);
            RenderUtils.drawRect(this.X.get() + 20.0D + (double)(this.curHealthX1 * 5.0F), 20.0D + this.Y.get() + 6.0D, this.X.get() + 20.0D + (double)(this.curHealthX1 * 5.0F) + 4.0D, 20.0D + this.Y.get() + 20.0D - 6.0D, (new Color(255, 225, 100, 255)).getRGB());
            RenderUtil.drawFastRoundedRect((float)(this.X.get() + 20) + this.curHealthX1 * 5.0F, (float)(20 + this.Y.get() + 6), (float)(this.X.get() + 20) + this.curHealthX1 * 5.0F + this.curAbsorptionAmountX1 * 5.0F, (float)(20 + this.Y.get() + 20 - 6), 2.0F, (new Color(255, 225, 100, 255)).getRGB());
        } else if((float)AbsorptionAmount < 0.5F && (float)AbsorptionAmount != 0.0F) {
            this.curAbsorptionAmountX1 = 0.0F;
        }

        RenderUtils.drawRect(this.X.get() + 5.0D, 20.0D + this.Y.get() + 22.0D, this.X.get() + 20.0D + (double)(MaxHealth * 5) + (double)(AbsorptionAmount * 5) + 5.0D - 5.0D, 20.0D + this.Y.get() + 23.0D, (new Color(255, 255, 255, 160)).getRGB());
        RenderUtils.drawImage(new ResourceLocation("fdpclient/misc/duelinfo/Health.png"), this.X.get() + 4, 20 + this.Y.get() + 4 + 20 + 4, 12, 12);
        RenderUtils.drawImage(new ResourceLocation("fdpclient/misc/duelinfo/Speed.png"), this.X.get() + (20 + MaxHealth * 5 + AbsorptionAmount * 5 + 5) / 2, 20 + this.Y.get() + 4 + 20 + 4, 12, 12);
        Text = /*((Boolean)HUD.中文.getValue()).booleanValue()? getHealthPotCount(mc.thePlayer) + " \u74f6": */getHealthPotCount(mc.thePlayer) + " Pots";
        int TextLength = Fonts.fontMiSansNormal35.getStringWidth(Text);
        Fonts.fontMiSansNormal35.drawStringWithShadow(Text, this.X.get() + 20 + ((MaxHealth * 5 + AbsorptionAmount * 5 + 5) / 2 - 12) / 2 - TextLength / 2, 20 + this.Y.get() + 4 + 20 + 4 + 4, (new Color(255, 255, 255, 255)).getRGB());
        Text = /*((Boolean)HUD.中文.getValue()).booleanValue()?String.valueOf(getSpeedPotCount(mc.thePlayer)) + " \u74f6":*/String.valueOf(getSpeedPotCount(mc.thePlayer)) + " Pots";
        TextLength = Fonts.fontMiSansNormal35.getStringWidth(Text);
        Fonts.fontMiSansNormal35.drawStringWithShadow(Text, this.X.get() + 20 + ((MaxHealth * 5 + AbsorptionAmount * 5 + 5) / 2 - 12) / 2 - TextLength / 2 + (20 + MaxHealth * 5 + AbsorptionAmount * 5 + 5) / 2 - 6, 20 + this.Y.get() + 4 + 20 + 4 + 4, (new Color(255, 255, 255, 255)).getRGB());
    }

    @EventTarget
    public void onNear(Render2DEvent e) {
        if(mode.get().equalsIgnoreCase("Near")) {
            float lastDis = -1.0F;
            Iterator WorldEntity = mc.theWorld.playerEntities.iterator();
            EntityPlayer Entity = null;

            while(true) {
                EntityPlayer TextLength;
                float Health;
                do {
                    Object Text;
                    do {
                        do {
                            if(!WorldEntity.hasNext()) {
                                if(Entity == null) {
                                    this.SelfHurt = 0;
                                    this.TargetHurt = 0;
                                    return;
                                }

                                String Text1 = "";
                                Fonts.fontMiSansNormal35.getStringWidth(Text1);
                                int MaxHealth1 = (int)mc.thePlayer.getMaxHealth();
                                int Health1 = (int)mc.thePlayer.getHealth();
                                int AbsorptionAmount = (int)mc.thePlayer.getAbsorptionAmount();
                                RenderUtil.drawFastRoundedRect((float)this.X.get(), (float)(70 + this.Y.get()), (float)(this.X.get() + 20 + MaxHealth1 * 5 + AbsorptionAmount * 5 + 5), (float)(90 + this.Y.get() + 20 + 5), 2.0F, (new Color(0, 0, 0, 120)).getRGB());
                                RenderUtils.drawImage(new ResourceLocation("fdpclient/misc/duelinfo/Head.png"), this.X.get() + 4, 70 + this.Y.get() + 5, 12, 12);
                                Text1 = Entity.getName();
                                Fonts.fontMiSansNormal35.getStringWidth(Text1);
                                Fonts.fontMiSansNormal35.drawStringWithShadow(Text1, this.X.get() + 20, 70 + this.Y.get() + 8, (new Color(200, 0, 0)).getRGB());
                                RenderUtils.drawRect(this.X.get() + 5.0D, 70.0D + this.Y.get() + 22.0D, this.X.get() + 20.0D + (double)(MaxHealth1 * 5) + (double)(AbsorptionAmount * 5) + 5.0D - 5.0D, 70.0D + this.Y.get() + 23.0D, (new Color(255, 255, 255, 160)).getRGB());
                                RenderUtils.drawImage(new ResourceLocation("fdpclient/misc/duelinfo/Sword.png"), this.X.get() + 4, 20 + this.Y.get() + 4 + 4 + 70, 12, 12);
                                RenderUtils.drawImage(new ResourceLocation("fdpclient/misc/duelinfo/Half_Heart.png"), this.X.get() + (20 + MaxHealth1 * 5 + AbsorptionAmount * 5 + 5) / 2, 20 + this.Y.get() + 4 + 4 + 70, 12, 12);
                                Text1 = /*((Boolean)HUD.中文.getValue()).booleanValue()?String.valueOf(this.TargetHurt) + " \u5200":*/String.valueOf(this.TargetHurt) + " Hits";
                                int TextLength1 = Fonts.fontMiSansNormal35.getStringWidth(Text1);
                                Fonts.fontMiSansNormal35.drawStringWithShadow(Text1, this.X.get() + 20 + ((MaxHealth1 * 5 + AbsorptionAmount * 5 + 5) / 2 - 12) / 2 - TextLength1 / 2, 20 + this.Y.get() + 4 + 4 + 4 + 70, (new Color(255, 255, 255, 255)).getRGB());
                                Text1 = /*((Boolean)HUD.中文.getValue()).booleanValue()?String.valueOf(this.SelfHurt) + " \u5200":*/String.valueOf(this.SelfHurt) + " Hits";
                                TextLength1 = Fonts.fontMiSansNormal35.getStringWidth(Text1);
                                Fonts.fontMiSansNormal35.drawStringWithShadow(Text1, this.X.get() + 20 + ((MaxHealth1 * 5 + AbsorptionAmount * 5 + 5) / 2 - 12) / 2 - TextLength1 / 2 + (20 + MaxHealth1 * 5 + AbsorptionAmount * 5 + 5) / 2 - 6, 20 + this.Y.get() + 4 + 4 + 4 + 70, (new Color(255, 255, 255, 255)).getRGB());
                                return;
                            }

                            Text = WorldEntity.next();
                        } while(!(Text instanceof EntityPlayer));
                    } while(Text == mc.thePlayer);

                    TextLength = (EntityPlayer)Text;
                    String MaxHealth = TextLength.getDisplayName().getFormattedText();
                    Health = mc.thePlayer.getDistanceToEntity(TextLength);
                } while(lastDis != -1.0F && lastDis <= Health);

                lastDis = Health;
                Entity = TextLength;
                this.FindedEntity = TextLength;
            }
        }
    }

    @EventTarget
    public void onRemiaft(Render2DEvent e) {
        if(mode.get().equalsIgnoreCase("Remiaft")) {
            Iterator WorldEntity = mc.theWorld.playerEntities.iterator();
            EntityPlayer Entity = null;

            while(WorldEntity.hasNext()) {
                Object Text = WorldEntity.next();
                if(Text instanceof EntityPlayer) {
                    EntityPlayer TextLength = (EntityPlayer)Text;
                    String MaxHealth = TextLength.getDisplayName().getFormattedText();
                    if(MaxHealth.startsWith("\u00a7c")) {
                        Entity = TextLength;
                        this.FindedEntity = TextLength;
                        break;
                    }
                }
            }

            if(Entity == null) {
                this.SelfHurt = 0;
                this.TargetHurt = 0;
            } else {
                String Text1 = "";
                Fonts.fontMiSansNormal35.getStringWidth(Text1);
                int MaxHealth1 = (int)mc.thePlayer.getMaxHealth();
                int Health = (int)mc.thePlayer.getHealth();
                int AbsorptionAmount = (int)mc.thePlayer.getAbsorptionAmount();
                RenderUtil.drawFastRoundedRect((float)this.X.get(), (float)(70 + this.Y.get()), (float)(this.X.get() + 20 + MaxHealth1 * 5 + AbsorptionAmount * 5 + 5), (float)(90 + this.Y.get() + 20 + 5), 2.0F, (new Color(0, 0, 0, 120)).getRGB());
                RenderUtils.drawImage(new ResourceLocation("fdpclient/misc/duelinfo/Head.png"), this.X.get() + 4, 70 + this.Y.get() + 5, 12, 12);
                Text1 = Entity.getName();
                Fonts.fontMiSansNormal35.getStringWidth(Text1);
                Fonts.fontMiSansNormal35.drawStringWithShadow(Text1, this.X.get() + 20, 70 + this.Y.get() + 8, (new Color(200, 0, 0)).getRGB());
                RenderUtils.drawRect(this.X.get() + 5.0D, 70.0D + this.Y.get() + 22.0D, this.X.get() + 20.0D + (double)(MaxHealth1 * 5) + (double)(AbsorptionAmount * 5) + 5.0D - 5.0D, 70.0D + this.Y.get() + 23.0D, (new Color(255, 255, 255, 160)).getRGB());
                RenderUtils.drawImage(new ResourceLocation("fdpclient/misc/duelinfo/Sword.png"), this.X.get() + 4, 20 + this.Y.get() + 4 + 4 + 70, 12, 12);
                RenderUtils.drawImage(new ResourceLocation("fdpclient/misc/duelinfo/Half_Heart.png"), this.X.get() + (20 + MaxHealth1 * 5 + AbsorptionAmount * 5 + 5) / 2, 20 + this.Y.get() + 4 + 4 + 70, 12, 12);
                Text1 = /*((Boolean)HUD.中文.getValue()).booleanValue()?String.valueOf(this.TargetHurt) + " \u5200":*/String.valueOf(this.TargetHurt) + " Hits";
                int TextLength1 = Fonts.fontMiSansNormal35.getStringWidth(Text1);
                Fonts.fontMiSansNormal35.drawStringWithShadow(Text1, this.X.get() + 20 + ((MaxHealth1 * 5 + AbsorptionAmount * 5 + 5) / 2 - 12) / 2 - TextLength1 / 2, 20 + this.Y.get() + 4 + 4 + 4 + 70, (new Color(255, 255, 255, 255)).getRGB());
                Text1 = /*((Boolean)HUD.中文.getValue()).booleanValue()?String.valueOf(this.SelfHurt) + " \u5200":*/String.valueOf(this.SelfHurt) + " Hits";
                TextLength1 = Fonts.fontMiSansNormal35.getStringWidth(Text1);
                Fonts.fontMiSansNormal35.drawStringWithShadow(Text1, this.X.get() + 20 + ((MaxHealth1 * 5 + AbsorptionAmount * 5 + 5) / 2 - 12) / 2 - TextLength1 / 2 + (20 + MaxHealth1 * 5 + AbsorptionAmount * 5 + 5) / 2 - 6, 20 + this.Y.get() + 4 + 4 + 4 + 70, (new Color(255, 255, 255, 255)).getRGB());
            }
        }
    }

    public void onEntityStatusPacket(S19PacketEntityStatus packet) {
        if(packet.getOpCode() == 2) {
            Entity target = packet.getEntity(mc.theWorld);
            if(target != null) {
                if(mc.thePlayer.getEntityId() == target.getEntityId()) {
                    ++this.SelfHurt;
                }

                if(this.HurtEntity == target.getEntityId() && this.HurtEntity == this.FindedEntity.getEntityId()) {
                    ++this.TargetHurt;
                    this.Hurted = true;
                }

            }
        }
    }

    @EventTarget
    public void onAttack(AttackEvent event) {
        this.HurtEntity = Objects.requireNonNull(event.getTargetEntity()).getEntityId();
        this.Hurted = false;
    }

    @EventTarget
    public void onPacket(PacketEvent e) {
        Packet p = e.getPacket();
        if(p instanceof S19PacketEntityStatus) {
            this.onEntityStatusPacket((S19PacketEntityStatus)p);
        }
    }
}
