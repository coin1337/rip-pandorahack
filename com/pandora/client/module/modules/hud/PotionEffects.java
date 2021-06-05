package com.pandora.client.module.modules.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.pandora.api.settings.Setting;
import com.pandora.api.util.render.PandoraColor;
import java.awt.Color;
import java.awt.Point;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class PotionEffects extends ListModule {
   private static Setting.Boolean sortUp;
   private static Setting.Boolean sortRight;
   private static Setting.ColorSetting color;
   private static PotionEffects.PotionList list = new PotionEffects.PotionList();

   public PotionEffects() {
      super(new ListModule.ListComponent("PotionEffects", new Point(0, 300), list), new Point(0, 300));
   }

   public void setup() {
      sortUp = this.registerBoolean("Sort Up", "SortUp", false);
      sortRight = this.registerBoolean("Sort Right", "SortRight", false);
      color = this.registerColor("Color", "Color", new PandoraColor(0, 255, 0, 255));
   }

   private static class PotionList implements ListModule.HUDList {
      private PotionList() {
      }

      public int getSize() {
         return PotionEffects.mc.field_71439_g.func_70651_bq().size();
      }

      public String getItem(int index) {
         PotionEffect effect = (PotionEffect)PotionEffects.mc.field_71439_g.func_70651_bq().toArray()[index];
         String name = I18n.func_135052_a(effect.func_188419_a().func_76393_a(), new Object[0]);
         int amplifier = effect.func_76458_c() + 1;
         return name + " " + amplifier + ChatFormatting.GRAY + " " + Potion.func_188410_a(effect, 1.0F);
      }

      public Color getItemColor(int index) {
         return PotionEffects.color.getValue();
      }

      public boolean sortUp() {
         return PotionEffects.sortUp.isOn();
      }

      public boolean sortRight() {
         return PotionEffects.sortRight.isOn();
      }

      // $FF: synthetic method
      PotionList(Object x0) {
         this();
      }
   }
}
