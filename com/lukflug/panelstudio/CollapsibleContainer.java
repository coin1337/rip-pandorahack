package com.lukflug.panelstudio;

import com.lukflug.panelstudio.settings.AnimatedToggleable;
import com.lukflug.panelstudio.settings.Toggleable;
import com.lukflug.panelstudio.theme.Renderer;
import java.awt.Rectangle;

public class CollapsibleContainer extends FocusableComponent implements Toggleable {
   protected Container container;
   protected AnimatedToggleable open;
   protected int childHeight = 0;
   protected int containerHeight = 0;
   protected int scrollPosition = 0;

   public CollapsibleContainer(String title, Renderer renderer, Toggleable open, Animation animation) {
      super(title, renderer);
      this.container = new Container(title, renderer);
      this.open = new AnimatedToggleable(open, animation);
   }

   public void addComponent(Component component) {
      this.container.addComponent(component);
   }

   public void render(Context context) {
      this.getHeight(context);
      this.renderer.renderBackground(context, this.hasFocus(context));
      super.render(context);
      this.renderer.renderTitle(context, this.title, this.hasFocus(context), this.isActive(), this.open.getValue() != 0.0D);
      if (this.open.getValue() != 0.0D) {
         Context subContext = new Context(context, 0, this.getContainerOffset(), this.hasFocus(context), this.open.getValue() == 1.0D);
         this.container.getHeight(subContext);
         Rectangle rect = this.getClipRect(context, subContext.getSize().height);
         if (rect != null) {
            context.getInterface().window(rect);
         }

         this.container.render(subContext);
         if (rect != null) {
            context.getInterface().restore();
         }

         context.setHeight(this.getRenderHeight(subContext.getSize().height));
      }

      this.renderer.renderBorder(context, this.hasFocus(context), this.isActive(), this.open.getValue() != 0.0D);
   }

   public void handleButton(Context context, int button) {
      if (this.open.getValue() == 1.0D) {
         Context subContext = new Context(context, 0, this.getContainerOffset(), this.hasFocus(context));
         this.container.getHeight(subContext);
         context.setHeight(this.getRenderHeight(subContext.getSize().height));
         this.updateFocus(context, button);
         boolean onTop = true;
         Rectangle rect = this.getClipRect(context, subContext.getSize().height);
         if (rect != null) {
            onTop = rect.contains(context.getInterface().getMouse());
         }

         subContext = new Context(context, 0, this.getContainerOffset(), this.hasFocus(context), onTop);
         this.container.handleButton(subContext, button);
         context.setHeight(this.getRenderHeight(subContext.getSize().height));
      } else {
         super.handleButton(context, button);
      }

      if (context.isHovered() && context.getInterface().getMouse().y <= context.getPos().y + this.renderer.getHeight() && button == 1 && context.getInterface().getButton(1)) {
         this.open.toggle();
      }

   }

   public void handleKey(Context context, int scancode) {
      if (this.open.getValue() == 1.0D) {
         Context subContext = new Context(context, 0, this.getContainerOffset(), this.hasFocus(context));
         this.container.handleKey(subContext, scancode);
         context.setHeight(this.getRenderHeight(subContext.getSize().height));
      } else {
         super.handleKey(context, scancode);
      }

   }

   public void handleScroll(Context context, int diff) {
      if (this.open.getValue() == 1.0D) {
         Context subContext = new Context(context, 0, this.getContainerOffset(), this.hasFocus(context));
         this.container.handleKey(subContext, diff);
         context.setHeight(this.getRenderHeight(subContext.getSize().height));
         if (subContext.isHovered()) {
            this.scrollPosition += diff;
            if (this.scrollPosition > this.childHeight - this.containerHeight) {
               this.scrollPosition = this.childHeight - this.containerHeight;
            }

            if (this.scrollPosition < 0) {
               this.scrollPosition = 0;
            }
         }
      } else {
         super.handleKey(context, diff);
      }

   }

   public void getHeight(Context context) {
      if (this.open.getValue() != 0.0D) {
         Context subContext = new Context(context, 0, this.getContainerOffset(), this.hasFocus(context));
         this.container.getHeight(subContext);
         context.setHeight(this.getRenderHeight(subContext.getSize().height));
      } else {
         super.getHeight(context);
      }

   }

   public void exit(Context context) {
      if (this.open.getValue() == 1.0D) {
         Context subContext = new Context(context, 0, this.getContainerOffset(), this.hasFocus(context));
         this.container.exit(subContext);
         context.setHeight(this.getRenderHeight(subContext.getSize().height));
      } else {
         super.exit(context);
      }

   }

   protected boolean isActive() {
      return true;
   }

   protected int getContainerOffset() {
      if (this.scrollPosition > this.childHeight - this.containerHeight) {
         this.scrollPosition = this.childHeight - this.containerHeight;
      }

      if (this.scrollPosition < 0) {
         this.scrollPosition = 0;
      }

      return (int)((double)(this.renderer.getHeight() - this.scrollPosition) - (1.0D - this.open.getValue()) * (double)this.containerHeight);
   }

   protected int getScrollHeight(int childHeight) {
      return childHeight;
   }

   protected int getRenderHeight(int childHeight) {
      this.childHeight = childHeight;
      this.containerHeight = this.getScrollHeight(childHeight);
      if (this.scrollPosition > childHeight - this.containerHeight) {
         this.scrollPosition = childHeight - this.containerHeight;
      }

      if (this.scrollPosition < 0) {
         this.scrollPosition = 0;
      }

      return (int)((double)this.containerHeight * this.open.getValue() + (double)this.renderer.getHeight());
   }

   protected Rectangle getClipRect(Context context, int height) {
      return new Rectangle(context.getPos().x, context.getPos().y + this.renderer.getHeight(), context.getSize().width, this.getRenderHeight(height) - this.renderer.getHeight());
   }

   public void toggle() {
      this.open.toggle();
      if (!this.open.isOn()) {
         this.container.releaseFocus();
      }

   }

   public boolean isOn() {
      return this.open.isOn();
   }
}
