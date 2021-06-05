package com.lukflug.panelstudio;

import com.lukflug.panelstudio.theme.Renderer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Container extends FocusableComponent {
   protected List<Component> components = new ArrayList();

   public Container(String title, Renderer renderer) {
      super(title, renderer);
   }

   public void addComponent(Component component) {
      this.components.add(component);
   }

   public void render(Context context) {
      int posy = this.renderer.getOffset();

      Context subContext;
      for(Iterator var3 = this.components.iterator(); var3.hasNext(); posy += subContext.getSize().height + this.renderer.getOffset()) {
         Component component = (Component)var3.next();
         subContext = new Context(context, this.renderer.getBorder(), posy, this.hasFocus(context));
         component.render(subContext);
      }

      context.setHeight(posy);
   }

   public void handleButton(Context context, int button) {
      int posy = this.renderer.getOffset();
      this.getHeight(context);
      this.updateFocus(context, button);

      Context subContext;
      for(Iterator var4 = this.components.iterator(); var4.hasNext(); posy += subContext.getSize().height + this.renderer.getOffset()) {
         Component component = (Component)var4.next();
         subContext = new Context(context, this.renderer.getBorder(), posy, this.hasFocus(context));
         component.handleButton(subContext, button);
      }

      context.setHeight(posy);
   }

   public void handleKey(Context context, int scancode) {
      int posy = this.renderer.getOffset();

      Context subContext;
      for(Iterator var4 = this.components.iterator(); var4.hasNext(); posy += subContext.getSize().height + this.renderer.getOffset()) {
         Component component = (Component)var4.next();
         subContext = new Context(context, this.renderer.getBorder(), posy, this.hasFocus(context));
         component.handleKey(subContext, scancode);
      }

      context.setHeight(posy);
   }

   public void handleScroll(Context context, int diff) {
      int posy = this.renderer.getOffset();

      Context subContext;
      for(Iterator var4 = this.components.iterator(); var4.hasNext(); posy += subContext.getSize().height + this.renderer.getOffset()) {
         Component component = (Component)var4.next();
         subContext = new Context(context, this.renderer.getBorder(), posy, this.hasFocus(context));
         component.handleKey(subContext, diff);
      }

      context.setHeight(posy);
   }

   public void getHeight(Context context) {
      int posy = this.renderer.getOffset();

      Context subContext;
      for(Iterator var3 = this.components.iterator(); var3.hasNext(); posy += subContext.getSize().height + this.renderer.getOffset()) {
         Component component = (Component)var3.next();
         subContext = new Context(context, this.renderer.getBorder(), posy, this.hasFocus(context));
         component.getHeight(subContext);
      }

      context.setHeight(posy);
   }

   public void exit(Context context) {
      int posy = this.renderer.getOffset();

      Context subContext;
      for(Iterator var3 = this.components.iterator(); var3.hasNext(); posy += subContext.getSize().height + this.renderer.getOffset()) {
         Component component = (Component)var3.next();
         subContext = new Context(context, this.renderer.getBorder(), posy, this.hasFocus(context));
         component.exit(subContext);
      }

      context.setHeight(posy);
   }

   public void releaseFocus() {
      super.releaseFocus();
      Iterator var1 = this.components.iterator();

      while(var1.hasNext()) {
         Component component = (Component)var1.next();
         component.releaseFocus();
      }

   }

   protected void handleFocus(Context context, boolean focus) {
      if (!focus) {
         this.releaseFocus();
      }

   }
}
