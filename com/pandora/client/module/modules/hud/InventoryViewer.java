package com.pandora.client.module.modules.hud;

import com.lukflug.panelstudio.Context;
import com.lukflug.panelstudio.Interface;
import com.lukflug.panelstudio.hud.HUDComponent;
import com.pandora.api.settings.Setting;
import com.pandora.api.util.render.PandoraColor;
import com.pandora.client.clickgui.PandoraGUI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class InventoryViewer extends HUDModule {
   private static Setting.ColorSetting fillColor;
   private static Setting.ColorSetting outlineColor;

   public InventoryViewer() {
      super(new InventoryViewer.InventoryViewerComponent(), new Point(0, 10));
   }

   public void setup() {
      fillColor = this.registerColor("Fill", "Fill", new PandoraColor(0, 0, 0, 100));
      outlineColor = this.registerColor("Outline", "Outline", new PandoraColor(255, 0, 0, 255));
   }

   private static class InventoryViewerComponent extends HUDComponent {
      public InventoryViewerComponent() {
         super("InventoryViewer", PandoraGUI.theme.getPanelRenderer(), new Point(0, 10));
      }

      public void render(Context context) {
         super.render(context);
         Color bgcolor = new PandoraColor(InventoryViewer.fillColor.getValue(), 100);
         context.getInterface().fillRect(context.getRect(), bgcolor, bgcolor, bgcolor, bgcolor);
         Color color = InventoryViewer.outlineColor.getValue();
         context.getInterface().fillRect(new Rectangle(context.getPos(), new Dimension(context.getSize().width, 1)), color, color, color, color);
         context.getInterface().fillRect(new Rectangle(context.getPos(), new Dimension(1, context.getSize().height)), color, color, color, color);
         context.getInterface().fillRect(new Rectangle(new Point(context.getPos().x + context.getSize().width - 1, context.getPos().y), new Dimension(1, context.getSize().height)), color, color, color, color);
         context.getInterface().fillRect(new Rectangle(new Point(context.getPos().x, context.getPos().y + context.getSize().height - 1), new Dimension(context.getSize().width, 1)), color, color, color, color);
         NonNullList<ItemStack> items = Minecraft.func_71410_x().field_71439_g.field_71071_by.field_70462_a;
         int size = items.size();

         for(int item = 9; item < size; ++item) {
            int slotX = context.getPos().x + item % 9 * 18;
            int slotY = context.getPos().y + 2 + (item / 9 - 1) * 18;
            PandoraGUI.renderItem((ItemStack)items.get(item), new Point(slotX, slotY));
         }

      }

      public int getWidth(Interface inter) {
         return 162;
      }

      public void getHeight(Context context) {
         context.setHeight(56);
      }
   }
}
