package com.pandora.client.module.modules.misc;

import com.pandora.api.settings.Setting;
import com.pandora.client.module.Module;
import net.minecraft.init.Items;

public class FastPlace extends Module {
   Setting.Boolean exp;
   Setting.Boolean crystals;
   Setting.Boolean everything;

   public FastPlace() {
      super("FastPlace", Module.Category.Misc);
   }

   public void setup() {
      this.exp = this.registerBoolean("Exp", "Exp", false);
      this.crystals = this.registerBoolean("Crystals", "Crystals", false);
      this.everything = this.registerBoolean("Everything", "Everything", false);
   }

   public void onUpdate() {
      if (this.exp.getValue() && mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_151062_by || mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151062_by) {
         mc.field_71467_ac = 0;
      }

      if (this.crystals.getValue() && mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP) {
         mc.field_71467_ac = 0;
      }

      if (this.everything.getValue()) {
         mc.field_71467_ac = 0;
      }

      mc.field_71442_b.field_78781_i = 0;
   }
}
