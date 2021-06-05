package com.pandora.client.module.modules.movement;

import com.pandora.api.util.misc.Wrapper;
import com.pandora.client.module.Module;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoSlow extends Module {
   private boolean sneaking;

   public NoSlow() {
      super("NoSlowBypass", Module.Category.Misc);
   }

   public void onUpdate() {
      if (Wrapper.mc.field_71441_e != null) {
         Item item = Wrapper.getPlayer().func_184607_cu().func_77973_b();
         if (this.sneaking && (!Wrapper.getPlayer().func_184587_cr() && item instanceof ItemFood || item instanceof ItemBow || item instanceof ItemPotion || !(item instanceof ItemFood) || !(item instanceof ItemBow) || !(item instanceof ItemPotion))) {
            Wrapper.getPlayer().field_71174_a.func_147297_a(new CPacketEntityAction(Wrapper.getPlayer(), Action.STOP_SNEAKING));
            this.sneaking = false;
         }
      }

   }

   @SubscribeEvent
   public void onUseItem(LivingEntityUseItemEvent event) {
      if (!this.sneaking) {
         Wrapper.getPlayer().field_71174_a.func_147297_a(new CPacketEntityAction(Wrapper.getPlayer(), Action.START_SNEAKING));
         this.sneaking = true;
      }

   }
}
