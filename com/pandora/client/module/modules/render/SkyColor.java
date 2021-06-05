package com.pandora.client.module.modules.render;

import com.pandora.api.settings.Setting;
import com.pandora.api.util.render.PandoraColor;
import com.pandora.client.module.Module;

public class SkyColor extends Module {
   public static Setting.ColorSetting color;
   public static Setting.Boolean fog;

   public SkyColor() {
      super("SkyColor", Module.Category.Render);
   }

   public void setup() {
      fog = this.registerBoolean("Fog", "Fog", true);
      color = this.registerColor("Color", "Color", new PandoraColor(0, 255, 0, 255));
   }
}
