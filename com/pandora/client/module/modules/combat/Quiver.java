package com.pandora.client.module.modules.combat;

import com.pandora.api.settings.Setting;
import com.pandora.api.util.misc.MessageBus;
import com.pandora.client.module.Module;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class Quiver extends Module {
   Setting.Boolean strength;
   Setting.Boolean speed;
   Setting.Boolean shootOne;
   boolean hasSpeed = false;
   boolean hasStrength = false;
   private int randomVariation;

   public Quiver() {
      super("Quiver", Module.Category.Combat);
   }

   public void setup() {
      this.strength = this.registerBoolean("Strength", "Strength", true);
      this.speed = this.registerBoolean("Speed", "Speed", true);
      this.shootOne = this.registerBoolean("Disable", "Disable", true);
   }

   public void onUpdate() {
      PotionEffect speedEffect = mc.field_71439_g.func_70660_b(Potion.func_188412_a(1));
      PotionEffect strengthEffect = mc.field_71439_g.func_70660_b(Potion.func_188412_a(5));
      if (speedEffect != null) {
         this.hasSpeed = true;
      } else {
         this.hasSpeed = false;
      }

      if (strengthEffect != null) {
         this.hasStrength = true;
      } else {
         this.hasStrength = false;
      }

      if (this.strength.getValue() && !this.hasStrength && mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() == Items.field_151031_f && this.isArrowInInventory("Arrow of Strength")) {
         mc.field_71439_g.field_71174_a.func_147297_a(new Rotation(0.0F, -90.0F, true));
         if (mc.field_71439_g.func_184612_cw() >= this.getBowCharge()) {
            mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerDigging(Action.RELEASE_USE_ITEM, BlockPos.field_177992_a, mc.field_71439_g.func_174811_aO()));
            mc.field_71439_g.func_184597_cx();
            mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            mc.field_71439_g.func_184598_c(EnumHand.MAIN_HAND);
         } else if (mc.field_71439_g.func_184612_cw() == 0) {
            mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            mc.field_71439_g.func_184598_c(EnumHand.MAIN_HAND);
         }
      }

      if (this.speed.getValue() && !this.hasSpeed && mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() == Items.field_151031_f && this.isArrowInInventory("Arrow of Speed")) {
         mc.field_71439_g.field_71174_a.func_147297_a(new Rotation(0.0F, -90.0F, true));
         if (mc.field_71439_g.func_184612_cw() >= this.getBowCharge()) {
            mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerDigging(Action.RELEASE_USE_ITEM, BlockPos.field_177992_a, mc.field_71439_g.func_174811_aO()));
            mc.field_71439_g.func_184597_cx();
            mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            mc.field_71439_g.func_184598_c(EnumHand.MAIN_HAND);
         } else if (mc.field_71439_g.func_184612_cw() == 0) {
            mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            mc.field_71439_g.func_184598_c(EnumHand.MAIN_HAND);
         }
      }

      if (this.shootOne.getValue()) {
         this.disable();
      }

   }

   private boolean isArrowInInventory(String type) {
      boolean inInv = false;

      for(int i = 0; i < 36; ++i) {
         ItemStack itemStack = mc.field_71439_g.field_71071_by.func_70301_a(i);
         if (itemStack.func_77973_b() == Items.field_185167_i && itemStack.func_82833_r().equalsIgnoreCase(type)) {
            inInv = true;
            this.switchArrow(i);
            break;
         }
      }

      return inInv;
   }

   private void switchArrow(int oldSlot) {
      int bowSlot = mc.field_71439_g.field_71071_by.field_70461_c;
      int placeSlot = bowSlot + 1;
      if (placeSlot > 8) {
         placeSlot = 1;
      }

      if (placeSlot != oldSlot) {
         if (mc.field_71462_r instanceof GuiContainer) {
            return;
         }

         mc.field_71442_b.func_187098_a(0, oldSlot, 0, ClickType.PICKUP, mc.field_71439_g);
         mc.field_71442_b.func_187098_a(0, placeSlot, 0, ClickType.PICKUP, mc.field_71439_g);
         mc.field_71442_b.func_187098_a(0, oldSlot, 0, ClickType.PICKUP, mc.field_71439_g);
         MessageBus.sendClientPrefixMessage("Placed arrow at pos " + placeSlot);
      }

   }

   private int getBowCharge() {
      if (this.randomVariation == 0) {
         this.randomVariation = 1;
      }

      return 3 + this.randomVariation;
   }
}
