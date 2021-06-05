package com.lukflug.panelstudio.hud;

import com.lukflug.panelstudio.Context;
import com.lukflug.panelstudio.FixedComponent;
import com.lukflug.panelstudio.Interface;
import com.lukflug.panelstudio.theme.Renderer;
import java.awt.Point;

public abstract class HUDComponent implements FixedComponent {
   protected String title;
   protected Renderer renderer;
   protected Point position;

   public HUDComponent(String title, Renderer renderer, Point position) {
      this.title = title;
      this.renderer = renderer;
      this.position = position;
   }

   public String getTitle() {
      return this.title;
   }

   public void render(Context context) {
      this.getHeight(context);
   }

   public void handleButton(Context context, int button) {
      this.getHeight(context);
   }

   public void handleKey(Context context, int scancode) {
      this.getHeight(context);
   }

   public void handleScroll(Context context, int diff) {
      this.getHeight(context);
   }

   public void exit(Context context) {
      this.getHeight(context);
   }

   public void releaseFocus() {
   }

   public Point getPosition(Interface inter) {
      return new Point(this.position);
   }

   public void setPosition(Interface inter, Point position) {
      this.position = position;
   }
}
