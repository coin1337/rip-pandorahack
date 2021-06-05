package com.pandora.client.module.modules.hud;

import com.lukflug.panelstudio.Context;
import com.lukflug.panelstudio.Interface;
import com.lukflug.panelstudio.hud.HUDComponent;
import com.pandora.api.settings.Setting;
import com.pandora.api.util.render.PandoraColor;
import com.pandora.client.clickgui.PandoraGUI;
import java.awt.Point;

public class Watermark extends HUDModule {
   private static Setting.ColorSetting color;

   public Watermark() {
      super(new Watermark.WatermarkComponent(), new Point(0, 0));
   }

   public void setup() {
      color = this.registerColor("Color", "Color", new PandoraColor(255, 0, 0, 255));
   }

   private static class WatermarkComponent extends HUDComponent {
      public WatermarkComponent() {
         super("Watermark", PandoraGUI.theme.getPanelRenderer(), new Point(0, 0));
      }

      private String getString() {
         return "Pandora v1.2";
      }

      public void render(Context context) {
         super.render(context);
         context.getInterface().drawString(context.getPos(), this.getString(), Watermark.color.getValue());
      }

      public int getWidth(Interface inter) {
         return inter.getFontWidth(this.getString());
      }

      public void getHeight(Context context) {
         context.setHeight(this.renderer.getHeight());
      }
   }
}
