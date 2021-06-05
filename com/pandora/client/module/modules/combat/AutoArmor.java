package com.pandora.client.module.modules.combat;

import com.pandora.client.module.Module;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class AutoArmor extends Module {
   public AutoArmor() {
      super("AutoArmor", Module.Category.Combat);
   }

   public void onUpdate() {
      if (mc.field_71439_g.field_70173_aa % 2 != 0) {
         if (!(mc.field_71462_r instanceof GuiContainer) || mc.field_71462_r instanceof InventoryEffectRenderer) {
            int[] bestArmorSlots = new int[4];
            int[] bestArmorValues = new int[4];

            int armorType;
            ItemStack stack;
            for(armorType = 0; armorType < 4; ++armorType) {
               stack = mc.field_71439_g.field_71071_by.func_70440_f(armorType);
               if (stack != null && stack.func_77973_b() instanceof ItemArmor) {
                  bestArmorValues[armorType] = ((ItemArmor)stack.func_77973_b()).field_77879_b;
               }

               bestArmorSlots[armorType] = -1;
            }

            for(armorType = 0; armorType < 36; ++armorType) {
               stack = mc.field_71439_g.field_71071_by.func_70301_a(armorType);
               if (stack.func_190916_E() <= 1 && stack != null && stack.func_77973_b() instanceof ItemArmor) {
                  ItemArmor armor = (ItemArmor)stack.func_77973_b();
                  int armorType = armor.field_77881_a.ordinal() - 2;
                  if (armorType != 2 || !mc.field_71439_g.field_71071_by.func_70440_f(armorType).func_77973_b().equals(Items.field_185160_cR)) {
                     int armorValue = armor.field_77879_b;
                     if (armorValue > bestArmorValues[armorType]) {
                        bestArmorSlots[armorType] = armorType;
                        bestArmorValues[armorType] = armorValue;
                     }
                  }
               }
            }

            for(armorType = 0; armorType < 4; ++armorType) {
               int slot = bestArmorSlots[armorType];
               if (slot != -1) {
                  ItemStack oldArmor = mc.field_71439_g.field_71071_by.func_70440_f(armorType);
                  if (oldArmor == null || oldArmor != ItemStack.field_190927_a || mc.field_71439_g.field_71071_by.func_70447_i() != -1) {
                     if (slot < 9) {
                        slot += 36;
                     }

                     mc.field_71442_b.func_187098_a(0, 8 - armorType, 0, ClickType.QUICK_MOVE, mc.field_71439_g);
                     mc.field_71442_b.func_187098_a(0, slot, 0, ClickType.QUICK_MOVE, mc.field_71439_g);
                     break;
                  }
               }
            }

         }
      }
   }
}
