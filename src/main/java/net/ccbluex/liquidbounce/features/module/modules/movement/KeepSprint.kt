package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.event.AttackEvent
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.EntityLivingBase
import net.minecraft.potion.Potion

@ModuleInfo ("KeepSprint", ModuleCategory.MOVEMENT)
class KeepSprint : Module() {
    @EventTarget
    fun onAttack(attackEvent : AttackEvent) {
        val entity : EntityLivingBase ? = if(attackEvent.targetEntity is EntityLivingBase) attackEvent.targetEntity else null
        if (mc.thePlayer.fallDistance > 0F && !mc.thePlayer.onGround && !mc.thePlayer.isOnLadder &&
            !mc.thePlayer.isInWater && !mc.thePlayer.isPotionActive(Potion.blindness) && !mc.thePlayer.isRiding)
            mc.thePlayer.onCriticalHit(entity)
        if (entity != null) {
            if (EnchantmentHelper.getModifierForCreature(mc.thePlayer.heldItem, entity.creatureAttribute) > 0F)
                mc.thePlayer.onEnchantmentCritical(entity)
        }
    }
}