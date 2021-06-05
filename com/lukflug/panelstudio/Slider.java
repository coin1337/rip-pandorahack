package com.lukflug.panelstudio;

import com.lukflug.panelstudio.theme.Renderer;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

public abstract class Slider extends FocusableComponent {
   public Slider(String title, Renderer renderer) {
      super(title, renderer);
   }

   public void render(Context context) {
      super.render(context);
      if (context.isClicked()) {
         double value = (double)(context.getInterface().getMouse().x - context.getPos().x) / (double)(context.getSize().width - 1);
         if (value < 0.0D) {
            value = 0.0D;
         } else if (value > 1.0D) {
            value = 1.0D;
         }

         this.setValue(value);
      }

      this.renderer.renderRect(context, "", this.hasFocus(context), false, new Rectangle(new Point(context.getPos().x + (int)((double)context.getSize().width * this.getValue()), context.getPos().y), new Dimension((int)((double)context.getSize().width * (1.0D - this.getValue())), this.renderer.getHeight())), false);
      this.renderer.renderRect(context, this.title, this.hasFocus(context), true, new Rectangle(context.getPos(), new Dimension((int)((double)context.getSize().width * this.getValue()), this.renderer.getHeight())), true);
   }

   protected abstract double getValue();

   protected abstract void setValue(double var1);
}
