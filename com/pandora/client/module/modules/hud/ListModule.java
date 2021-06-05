package com.pandora.client.module.modules.hud;

import com.lukflug.panelstudio.Context;
import com.lukflug.panelstudio.FixedComponent;
import com.lukflug.panelstudio.Interface;
import com.lukflug.panelstudio.hud.HUDComponent;
import com.pandora.api.config.PositionConfig;
import com.pandora.client.clickgui.PandoraGUI;
import java.awt.Color;
import java.awt.Point;

public class ListModule extends HUDModule {
   public ListModule(FixedComponent component, Point defaultPos) {
      super(component, defaultPos);
   }

   protected static class ListComponent extends HUDComponent implements PositionConfig {
      protected ListModule.HUDList list;
      protected boolean lastUp = false;

      public ListComponent(String name, Point position, ListModule.HUDList list) {
         super(name, PandoraGUI.theme.getPanelRenderer(), position);
         this.list = list;
      }

      public void render(Context context) {
         super.render(context);

         for(int i = 0; i < this.list.getSize(); ++i) {
            String s = this.list.getItem(i);
            Point p = context.getPos();
            if (this.list.sortUp()) {
               p.translate(0, context.getSize().height - (i + 1) * context.getInterface().getFontHeight());
            } else {
               p.translate(0, i * context.getInterface().getFontHeight());
            }

            if (this.list.sortRight()) {
               p.translate(this.getWidth(context.getInterface()) - context.getInterface().getFontWidth(s), 0);
            }

            context.getInterface().drawString(p, s, this.list.getItemColor(i));
         }

      }

      public Point getPosition(Interface inter) {
         int height = this.renderer.getHeight() + (this.list.getSize() - 1) * inter.getFontHeight();
         if (this.lastUp != this.list.sortUp()) {
            if (this.list.sortUp()) {
               this.position.translate(0, height);
            } else {
               this.position.translate(0, -height);
            }

            this.lastUp = this.list.sortUp();
         }

         return this.list.sortUp() ? new Point(this.position.x, this.position.y - height) : new Point(this.position);
      }

      public void setPosition(Interface inter, Point position) {
         int height = this.renderer.getHeight() + (this.list.getSize() - 1) * inter.getFontHeight();
         if (this.list.sortUp()) {
            this.position = new Point(position.x, position.y + height);
         } else {
            this.position = new Point(position);
         }

      }

      public int getWidth(Interface inter) {
         int width = inter.getFontWidth(this.getTitle());

         for(int i = 0; i < this.list.getSize(); ++i) {
            String s = this.list.getItem(i);
            width = Math.max(width, inter.getFontWidth(s));
         }

         return width;
      }

      public void getHeight(Context context) {
         context.setHeight(this.renderer.getHeight() + (this.list.getSize() - 1) * context.getInterface().getFontHeight());
      }

      public Point getConfigPos() {
         return this.position;
      }

      public void setConfigPos(Point pos) {
         this.position = pos;
         this.lastUp = this.list.sortUp();
      }
   }

   protected interface HUDList {
      int getSize();

      String getItem(int var1);

      Color getItemColor(int var1);

      boolean sortUp();

      boolean sortRight();
   }
}
