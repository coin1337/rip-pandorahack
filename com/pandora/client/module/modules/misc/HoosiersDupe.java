package com.pandora.client.module.modules.misc;

import com.pandora.client.module.Module;
import java.util.Random;

public class HoosiersDupe extends Module {
   public HoosiersDupe() {
      super("HoosiersDupe", Module.Category.Misc);
   }

   public void onEnable() {
      if (mc.field_71439_g != null) {
         mc.field_71439_g.func_71165_d("I just used the Go_Hoosiers Dupe and got " + ((new Random()).nextInt(31) + 1) + " shulkers thanks to Pandora!");
      }

      this.disable();
   }
}
