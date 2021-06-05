package com.pandora.client.module.modules.hud;

import com.lukflug.panelstudio.tabgui.DefaultRenderer;
import com.lukflug.panelstudio.tabgui.TabGUI;
import com.lukflug.panelstudio.tabgui.TabGUIContainer;
import com.lukflug.panelstudio.tabgui.TabGUIItem;
import com.lukflug.panelstudio.tabgui.TabGUIRenderer;
import com.lukflug.panelstudio.theme.ColorScheme;
import com.pandora.client.clickgui.PandoraGUI;
import com.pandora.client.module.Module;
import com.pandora.client.module.ModuleManager;
import com.pandora.client.module.modules.gui.ClickGuiModule;
import java.awt.Color;
import java.awt.Point;
import java.util.Iterator;

public class TabGUIModule extends TabGUI {
   private static TabGUIModule instance;
   private static final TabGUIRenderer renderer = new TabGUIModule.PandoraRenderer();

   public TabGUIModule() {
      super("TabGUI", renderer, new PandoraGUI.GameSenseAnimation(), new Point(10, 10), 75);
      instance = this;
   }

   private void reset() {
      this.components.clear();
   }

   public static void populate() {
      instance.reset();
      Module.Category[] var0 = Module.Category.values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         Module.Category category = var0[var2];
         TabGUIContainer tab = new TabGUIContainer(category.name(), renderer, new PandoraGUI.GameSenseAnimation());
         instance.addComponent(tab);
         Iterator var5 = ModuleManager.getModulesInCategory(category).iterator();

         while(var5.hasNext()) {
            Module module = (Module)var5.next();
            tab.addComponent(new TabGUIItem(module.getName(), module));
         }
      }

   }

   private static class PandoraRenderer extends DefaultRenderer {
      public PandoraRenderer() {
         super(new TabGUIModule.PandoraScheme(), 12, 5, 200, 208, 203, 205, 28);
      }
   }

   private static class PandoraScheme implements ColorScheme {
      private PandoraScheme() {
      }

      public Color getActiveColor() {
         return ClickGuiModule.enabledColor.getValue();
      }

      public Color getInactiveColor() {
         return ClickGuiModule.backgroundColor.getValue();
      }

      public Color getBackgroundColor() {
         return ClickGuiModule.settingBackgroundColor.getValue();
      }

      public Color getOutlineColor() {
         return ClickGuiModule.backgroundColor.getValue();
      }

      public Color getFontColor() {
         return ClickGuiModule.fontColor.getValue();
      }

      public int getOpacity() {
         return ClickGuiModule.opacity.getValue();
      }

      // $FF: synthetic method
      PandoraScheme(Object x0) {
         this();
      }
   }
}
