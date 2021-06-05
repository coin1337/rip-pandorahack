package com.pandora.client.module.modules.render;

import com.pandora.api.settings.Setting;
import com.pandora.client.module.Module;
import java.util.ArrayList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class Fullbright extends Module {
   float oldGamma;
   Setting.Mode Mode;

   public Fullbright() {
      super("Fullbright", Module.Category.Render);
   }

   public void setup() {
      ArrayList<String> modes = new ArrayList();
      modes.add("Gamma");
      modes.add("Potion");
      this.Mode = this.registerMode("Mode", "Mode", modes, "Gamma");
   }

   public void onEnable() {
      this.oldGamma = mc.field_71474_y.field_74333_Y;
   }

   public void onUpdate() {
      if (this.Mode.getValue().equalsIgnoreCase("Gamma")) {
         mc.field_71474_y.field_74333_Y = 666.0F;
         mc.field_71439_g.func_184589_d(Potion.func_188412_a(16));
      } else if (this.Mode.getValue().equalsIgnoreCase("Potion")) {
         PotionEffect potionEffect = new PotionEffect(Potion.func_188412_a(16), 123456789, 5);
         potionEffect.func_100012_b(true);
         mc.field_71439_g.func_70690_d(potionEffect);
      }

   }

   public void onDisable() {
      mc.field_71474_y.field_74333_Y = this.oldGamma;
      mc.field_71439_g.func_184589_d(Potion.func_188412_a(16));
   }
}
