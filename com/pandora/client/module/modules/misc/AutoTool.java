package com.pandora.client.module.modules.misc;

import com.pandora.api.event.events.DamageBlockEvent;
import com.pandora.api.settings.Setting;
import com.pandora.client.PandoraMod;
import com.pandora.client.module.Module;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Mouse;

public class AutoTool extends Module {
   Setting.Boolean switchBack;
   boolean shouldMoveBack = false;
   int lastSlot = 0;
   long lastChange = 0L;
   @EventHandler
   private final Listener<DamageBlockEvent> leftClickListener = new Listener((event) -> {
      this.equipBestTool(mc.field_71441_e.func_180495_p(event.getPos()));
   }, new Predicate[0]);

   public AutoTool() {
      super("AutoTool", Module.Category.Misc);
   }

   public void setup() {
      this.switchBack = this.registerBoolean("Switch Back", "SwitchBack", false);
   }

   public void onUpdate() {
      if (!this.switchBack.getValue()) {
         this.shouldMoveBack = false;
      }

      if (mc.field_71462_r == null && this.switchBack.getValue()) {
         boolean mouse = Mouse.isButtonDown(0);
         if (mouse && !this.shouldMoveBack) {
            this.lastChange = System.currentTimeMillis();
            this.shouldMoveBack = true;
            this.lastSlot = mc.field_71439_g.field_71071_by.field_70461_c;
            mc.field_71442_b.func_78750_j();
         } else if (!mouse && this.shouldMoveBack) {
            this.shouldMoveBack = false;
            mc.field_71439_g.field_71071_by.field_70461_c = this.lastSlot;
            mc.field_71442_b.func_78750_j();
         }

      }
   }

   private void equipBestTool(IBlockState blockState) {
      int bestSlot = -1;
      double max = 0.0D;

      for(int i = 0; i < 9; ++i) {
         ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
         if (!stack.func_190926_b()) {
            float speed = stack.func_150997_a(blockState);
            if (speed > 1.0F) {
               int eff;
               speed = (float)((double)speed + ((eff = EnchantmentHelper.func_77506_a(Enchantments.field_185305_q, stack)) > 0 ? Math.pow((double)eff, 2.0D) + 1.0D : 0.0D));
               if ((double)speed > max) {
                  max = (double)speed;
                  bestSlot = i;
               }
            }
         }
      }

      if (bestSlot != -1) {
         equip(bestSlot);
      }

   }

   private static void equip(int slot) {
      mc.field_71439_g.field_71071_by.field_70461_c = slot;
      mc.field_71442_b.func_78750_j();
   }

   public void onEnable() {
      PandoraMod.EVENT_BUS.subscribe((Object)this);
   }

   public void onDisable() {
      PandoraMod.EVENT_BUS.unsubscribe((Object)this);
   }
}
