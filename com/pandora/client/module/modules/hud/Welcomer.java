package com.pandora.client.module.modules.hud;

import com.lukflug.panelstudio.Context;
import com.lukflug.panelstudio.Interface;
import com.lukflug.panelstudio.hud.HUDComponent;
import com.pandora.api.settings.Setting;
import com.pandora.api.util.render.PandoraColor;
import com.pandora.client.clickgui.PandoraGUI;
import java.awt.Point;
import net.minecraft.client.Minecraft;

public class Welcomer extends HUDModule {
   private static Setting.ColorSetting color;

   public Welcomer() {
      super(new Welcomer.WelcomerComponent(), new Point(450, 0));
   }

   public void setup() {
      color = this.registerColor("Color", "Color", new PandoraColor(255, 0, 0, 255));
   }

   private static class WelcomerComponent extends HUDComponent {
      public WelcomerComponent() {
         super("Welcomer", PandoraGUI.theme.getPanelRenderer(), new Point(450, 0));
      }

      private String getString() {
         return "Hello " + Minecraft.func_71410_x().field_71439_g.func_70005_c_() + " :^)";
      }

      public void render(Context context) {
         super.render(context);
         context.getInterface().drawString(context.getPos(), this.getString(), Welcomer.color.getValue());
      }

      public int getWidth(Interface inter) {
         return inter.getFontWidth(this.getString());
      }

      public void getHeight(Context context) {
         context.setHeight(this.renderer.getHeight());
      }
   }
}
