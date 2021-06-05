package com.pandora.client.module.modules.misc;

import com.pandora.api.settings.Setting;
import com.pandora.api.util.misc.Pair;
import com.pandora.client.module.Module;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class HotbarRefill extends Module {
   Setting.Integer threshold;
   Setting.Integer tickDelay;
   private int delayStep = 0;

   public HotbarRefill() {
      super("HotbarRefill", Module.Category.Misc);
   }

   public void setup() {
      this.threshold = this.registerInteger("Threshold", "Threshold", 32, 1, 63);
      this.tickDelay = this.registerInteger("Tick Delay", "TickDelay", 2, 1, 10);
   }

   private static Map<Integer, ItemStack> getInventory() {
      return getInventorySlots(9, 35);
   }

   private static Map<Integer, ItemStack> getHotbar() {
      return getInventorySlots(36, 44);
   }

   private static Map<Integer, ItemStack> getInventorySlots(int current, int last) {
      HashMap fullInventorySlots;
      for(fullInventorySlots = new HashMap(); current <= last; ++current) {
         fullInventorySlots.put(current, (ItemStack)mc.field_71439_g.field_71069_bz.func_75138_a().get(current));
      }

      return fullInventorySlots;
   }

   public void onUpdate() {
      if (mc.field_71439_g != null) {
         if (!(mc.field_71462_r instanceof GuiContainer)) {
            if (this.delayStep < this.tickDelay.getValue()) {
               ++this.delayStep;
            } else {
               this.delayStep = 0;
               Pair<Integer, Integer> slots = this.findReplenishableHotbarSlot();
               if (slots != null) {
                  int inventorySlot = (Integer)slots.getKey();
                  int hotbarSlot = (Integer)slots.getValue();
                  mc.field_71442_b.func_187098_a(0, inventorySlot, 0, ClickType.PICKUP, mc.field_71439_g);
                  mc.field_71442_b.func_187098_a(0, hotbarSlot, 0, ClickType.PICKUP, mc.field_71439_g);
                  mc.field_71442_b.func_187098_a(0, inventorySlot, 0, ClickType.PICKUP, mc.field_71439_g);
               }
            }
         }
      }
   }

   private Pair<Integer, Integer> findReplenishableHotbarSlot() {
      Pair<Integer, Integer> returnPair = null;
      Iterator var2 = getHotbar().entrySet().iterator();

      while(var2.hasNext()) {
         Entry<Integer, ItemStack> hotbarSlot = (Entry)var2.next();
         ItemStack stack = (ItemStack)hotbarSlot.getValue();
         if (!stack.field_190928_g && stack.func_77973_b() != Items.field_190931_a && stack.func_77985_e() && stack.field_77994_a < stack.func_77976_d() && stack.field_77994_a <= this.threshold.getValue()) {
            int inventorySlot = this.findCompatibleInventorySlot(stack);
            if (inventorySlot != -1) {
               returnPair = new Pair(inventorySlot, (Integer)hotbarSlot.getKey());
            }
         }
      }

      return returnPair;
   }

   private int findCompatibleInventorySlot(ItemStack hotbarStack) {
      int inventorySlot = -1;
      int smallestStackSize = 999;
      Iterator var4 = getInventory().entrySet().iterator();

      while(var4.hasNext()) {
         Entry<Integer, ItemStack> entry = (Entry)var4.next();
         ItemStack inventoryStack = (ItemStack)entry.getValue();
         if (!inventoryStack.field_190928_g && inventoryStack.func_77973_b() != Items.field_190931_a && this.isCompatibleStacks(hotbarStack, inventoryStack)) {
            int currentStackSize = ((ItemStack)mc.field_71439_g.field_71069_bz.func_75138_a().get((Integer)entry.getKey())).field_77994_a;
            if (smallestStackSize > currentStackSize) {
               smallestStackSize = currentStackSize;
               inventorySlot = (Integer)entry.getKey();
            }
         }
      }

      return inventorySlot;
   }

   private boolean isCompatibleStacks(ItemStack stack1, ItemStack stack2) {
      if (!stack1.func_77973_b().equals(stack2.func_77973_b())) {
         return false;
      } else {
         if (stack1.func_77973_b() instanceof ItemBlock && stack2.func_77973_b() instanceof ItemBlock) {
            Block block1 = ((ItemBlock)stack1.func_77973_b()).func_179223_d();
            Block block2 = ((ItemBlock)stack2.func_77973_b()).func_179223_d();
            if (!block1.field_149764_J.equals(block2.field_149764_J)) {
               return false;
            }
         }

         if (!stack1.func_82833_r().equals(stack2.func_82833_r())) {
            return false;
         } else {
            return stack1.func_77952_i() == stack2.func_77952_i();
         }
      }
   }
}
