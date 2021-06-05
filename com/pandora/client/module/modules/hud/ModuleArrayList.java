package com.pandora.client.module.modules.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.pandora.api.settings.Setting;
import com.pandora.api.util.render.PandoraColor;
import com.pandora.client.PandoraMod;
import com.pandora.client.module.Module;
import com.pandora.client.module.ModuleManager;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class ModuleArrayList extends ListModule {
   private static Setting.Boolean sortUp;
   private static Setting.Boolean sortRight;
   private static Setting.ColorSetting color;
   private static ModuleArrayList.ModuleList list = new ModuleArrayList.ModuleList();

   public ModuleArrayList() {
      super(new ListModule.ListComponent("ArrayList", new Point(0, 200), list), new Point(0, 200));
   }

   public void setup() {
      sortUp = this.registerBoolean("Sort Up", "SortUp", true);
      sortRight = this.registerBoolean("Sort Right", "SortRight", false);
      color = this.registerColor("Color", "Color", new PandoraColor(255, 0, 0, 255));
   }

   public void onRender() {
      list.activeModules.clear();
      Iterator var1 = ModuleManager.getModules().iterator();

      while(var1.hasNext()) {
         Module module = (Module)var1.next();
         if (module.isEnabled() && module.isDrawn()) {
            list.activeModules.add(module);
         }
      }

      list.activeModules.sort(Comparator.comparing((modulex) -> {
         return -PandoraMod.getInstance().clickGUI.getFontWidth(modulex.getName() + ChatFormatting.GRAY + " " + modulex.getHudInfo());
      }));
   }

   private static class ModuleList implements ListModule.HUDList {
      public List<Module> activeModules;

      private ModuleList() {
         this.activeModules = new ArrayList();
      }

      public int getSize() {
         return this.activeModules.size();
      }

      public String getItem(int index) {
         Module module = (Module)this.activeModules.get(index);
         return module.getName() + ChatFormatting.GRAY + " " + module.getHudInfo();
      }

      public Color getItemColor(int index) {
         PandoraColor c = ModuleArrayList.color.getValue();
         return Color.getHSBColor(c.getHue() + (ModuleArrayList.color.getRainbow() ? 0.02F * (float)index : 0.0F), c.getSaturation(), c.getBrightness());
      }

      public boolean sortUp() {
         return ModuleArrayList.sortUp.isOn();
      }

      public boolean sortRight() {
         return ModuleArrayList.sortRight.isOn();
      }

      // $FF: synthetic method
      ModuleList(Object x0) {
         this();
      }
   }
}
