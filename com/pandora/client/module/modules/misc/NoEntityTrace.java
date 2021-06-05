package com.pandora.client.module.modules.misc;

import com.pandora.api.settings.Setting;
import com.pandora.client.module.Module;
import net.minecraft.item.ItemPickaxe;

public class NoEntityTrace extends Module {
   Setting.Boolean pickaxeOnly;
   boolean isHoldingPickaxe = false;

   public NoEntityTrace() {
      super("NoEntityTrace", Module.Category.Misc);
   }

   public void setup() {
      this.pickaxeOnly = this.registerBoolean("Pickaxe Only", "PickaxeOnly", true);
   }

   public void onUpdate() {
      this.isHoldingPickaxe = mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemPickaxe;
   }

   public boolean noTrace() {
      if (!this.pickaxeOnly.getValue()) {
         return this.isEnabled();
      } else {
         return this.isEnabled() && this.isHoldingPickaxe;
      }
   }
}
