package com.lukflug.panelstudio;

import com.lukflug.panelstudio.settings.Toggleable;
import com.lukflug.panelstudio.theme.Renderer;
import java.awt.Point;

public class DraggableContainer extends CollapsibleContainer implements FixedComponent {
   protected boolean dragging = false;
   protected Point attachPoint;
   protected Point position;
   protected int width;
   protected boolean bodyDrag = false;

   public DraggableContainer(String title, Renderer renderer, Toggleable open, Animation animation, Point position, int width) {
      super(title, renderer, open, animation);
      this.position = position;
      this.width = width;
   }

   public void handleButton(Context context, int button) {
      if (this.bodyDrag) {
         super.handleButton(context, button);
      } else {
         context.setHeight(this.renderer.getHeight());
      }

      if (context.isClicked() && button == 0) {
         this.dragging = true;
         this.attachPoint = context.getInterface().getMouse();
      } else if (!context.getInterface().getButton(0) && this.dragging) {
         Point mouse = context.getInterface().getMouse();
         this.dragging = false;
         Point p = this.getPosition(context.getInterface());
         p.translate(mouse.x - this.attachPoint.x, mouse.y - this.attachPoint.y);
         this.setPosition(context.getInterface(), p);
      }

      if (!this.bodyDrag) {
         super.handleButton(context, button);
      }

   }

   public Point getPosition(Interface inter) {
      if (this.dragging) {
         Point point = new Point(this.position);
         point.translate(inter.getMouse().x - this.attachPoint.x, inter.getMouse().y - this.attachPoint.y);
         return point;
      } else {
         return this.position;
      }
   }

   public void setPosition(Interface inter, Point position) {
      this.position = new Point(position);
   }

   public int getWidth(Interface inter) {
      return this.width;
   }

   protected void handleFocus(Context context, boolean focus) {
      if (focus) {
         context.requestFocus();
      }

   }
}
