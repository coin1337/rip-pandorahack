package com.pandora.client.module.modules.movement;

import com.pandora.api.settings.Setting;
import com.pandora.client.module.Module;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class Anchor extends Module {
   Setting.Integer activateYLevel;
   BlockPos playerPos;

   public Anchor() {
      super("Anchor", Module.Category.Movement);
   }

   public void setup() {
      this.activateYLevel = this.registerInteger("Activate Y", "ActivateY", 20, 0, 256);
   }

   public void onUpdate() {
      if (mc.field_71439_g != null) {
         if (!(mc.field_71439_g.field_70163_u < 0.0D)) {
            if (!(mc.field_71439_g.field_70163_u > (double)this.activateYLevel.getValue())) {
               double newX;
               if (mc.field_71439_g.field_70165_t > (double)Math.round(mc.field_71439_g.field_70165_t)) {
                  newX = (double)Math.round(mc.field_71439_g.field_70165_t) + 0.5D;
               } else if (mc.field_71439_g.field_70165_t < (double)Math.round(mc.field_71439_g.field_70165_t)) {
                  newX = (double)Math.round(mc.field_71439_g.field_70165_t) - 0.5D;
               } else {
                  newX = mc.field_71439_g.field_70165_t;
               }

               double newZ;
               if (mc.field_71439_g.field_70161_v > (double)Math.round(mc.field_71439_g.field_70161_v)) {
                  newZ = (double)Math.round(mc.field_71439_g.field_70161_v) + 0.5D;
               } else if (mc.field_71439_g.field_70161_v < (double)Math.round(mc.field_71439_g.field_70161_v)) {
                  newZ = (double)Math.round(mc.field_71439_g.field_70161_v) - 0.5D;
               } else {
                  newZ = mc.field_71439_g.field_70161_v;
               }

               this.playerPos = new BlockPos(newX, mc.field_71439_g.field_70163_u, newZ);
               if (mc.field_71441_e.func_180495_p(this.playerPos).func_177230_c() == Blocks.field_150350_a) {
                  if (mc.field_71441_e.func_180495_p(this.playerPos.func_177977_b()).func_177230_c() == Blocks.field_150350_a && mc.field_71441_e.func_180495_p(this.playerPos.func_177977_b().func_177974_f()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(this.playerPos.func_177977_b().func_177976_e()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(this.playerPos.func_177977_b().func_177978_c()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(this.playerPos.func_177977_b().func_177968_d()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(this.playerPos.func_177979_c(2)).func_177230_c() != Blocks.field_150350_a) {
                     mc.field_71439_g.field_70159_w = 0.0D;
                     mc.field_71439_g.field_70179_y = 0.0D;
                  } else if (mc.field_71441_e.func_180495_p(this.playerPos.func_177977_b()).func_177230_c() == Blocks.field_150350_a && mc.field_71441_e.func_180495_p(this.playerPos.func_177979_c(2)).func_177230_c() == Blocks.field_150350_a && mc.field_71441_e.func_180495_p(this.playerPos.func_177979_c(2).func_177974_f()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(this.playerPos.func_177979_c(2).func_177976_e()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(this.playerPos.func_177979_c(2).func_177978_c()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(this.playerPos.func_177979_c(2).func_177968_d()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(this.playerPos.func_177979_c(3)).func_177230_c() != Blocks.field_150350_a) {
                     mc.field_71439_g.field_70159_w = 0.0D;
                     mc.field_71439_g.field_70179_y = 0.0D;
                  }

               }
            }
         }
      }
   }
}
