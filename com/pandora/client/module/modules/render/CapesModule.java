package com.pandora.client.module.modules.render;

import com.pandora.api.settings.Setting;
import com.pandora.client.module.Module;
import java.util.ArrayList;

public class CapesModule extends Module {
   public Setting.Mode capeMode;

   public CapesModule() {
      super("Capes", Module.Category.Render);
      this.setDrawn(false);
   }

   public void setup() {
      ArrayList<String> CapeType = new ArrayList();
      CapeType.add("Black");
      CapeType.add("White");
      this.capeMode = this.registerMode("Type", "Type", CapeType, "Black");
   }
}
