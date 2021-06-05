package com.pandora.client.module.modules.movement;

import com.pandora.api.settings.Setting;
import com.pandora.client.module.Module;
import com.pandora.client.module.ModuleManager;

public class ReverseStep extends Module {
   Setting.Double height;

   public ReverseStep() {
      super("ReverseStep", Module.Category.Movement);
   }

   public void setup() {
      this.height = this.registerDouble("Height", "Height", 2.5D, 0.5D, 2.5D);
   }

   public void onUpdate() {
      if (mc.field_71441_e != null && mc.field_71439_g != null && !mc.field_71439_g.func_70090_H() && !mc.field_71439_g.func_180799_ab() && !mc.field_71439_g.func_70617_f_() && !mc.field_71474_y.field_74314_A.func_151470_d()) {
         if (!ModuleManager.isModuleEnabled("Speed")) {
            if (mc.field_71439_g != null && mc.field_71439_g.field_70122_E && !mc.field_71439_g.func_70090_H() && !mc.field_71439_g.func_70617_f_()) {
               for(double y = 0.0D; y < this.height.getValue() + 0.5D; y += 0.01D) {
                  if (!mc.field_71441_e.func_184144_a(mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72317_d(0.0D, -y, 0.0D)).isEmpty()) {
                     mc.field_71439_g.field_70181_x = -10.0D;
                     break;
                  }
               }
            }

         }
      }
   }
}
