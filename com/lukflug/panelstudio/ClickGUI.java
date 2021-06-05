package com.lukflug.panelstudio;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClickGUI {
   protected List<FixedComponent> components;
   protected Interface inter;

   public ClickGUI(Interface inter) {
      this.inter = inter;
      this.components = new ArrayList();
   }

   public List<FixedComponent> getComponents() {
      return this.components;
   }

   public void addComponent(FixedComponent component) {
      this.components.add(component);
   }

   public void render() {
      int highest = 0;
      FixedComponent focusComponent = null;

      int i;
      FixedComponent component;
      Context context;
      for(i = this.components.size() - 1; i >= 0; --i) {
         component = (FixedComponent)this.components.get(i);
         context = new Context(this.inter, component.getWidth(this.inter), component.getPosition(this.inter), true, true);
         component.getHeight(context);
         if (context.isHovered()) {
            highest = i;
            break;
         }
      }

      for(i = 0; i < this.components.size(); ++i) {
         component = (FixedComponent)this.components.get(i);
         context = new Context(this.inter, component.getWidth(this.inter), component.getPosition(this.inter), true, i >= highest);
         component.render(context);
         if (context.foucsRequested()) {
            focusComponent = component;
         }
      }

      if (focusComponent != null) {
         this.components.remove(focusComponent);
         this.components.add(focusComponent);
      }

   }

   public void handleButton(int button) {
      boolean highest = true;
      FixedComponent focusComponent = null;

      for(int i = this.components.size() - 1; i >= 0; --i) {
         FixedComponent component = (FixedComponent)this.components.get(i);
         Context context = new Context(this.inter, component.getWidth(this.inter), component.getPosition(this.inter), true, highest);
         component.handleButton(context, button);
         if (context.isHovered()) {
            highest = false;
         }

         if (context.foucsRequested()) {
            focusComponent = component;
         }
      }

      if (focusComponent != null) {
         this.components.remove(focusComponent);
         this.components.add(focusComponent);
      }

   }

   public void handleKey(int scancode) {
      boolean highest = true;
      FixedComponent focusComponent = null;
      Iterator var4 = this.components.iterator();

      while(var4.hasNext()) {
         FixedComponent component = (FixedComponent)var4.next();
         Context context = new Context(this.inter, component.getWidth(this.inter), component.getPosition(this.inter), true, highest);
         component.handleKey(context, scancode);
         if (context.isHovered()) {
            highest = false;
         }

         if (context.foucsRequested()) {
            focusComponent = component;
         }
      }

      if (focusComponent != null) {
         this.components.remove(focusComponent);
         this.components.add(focusComponent);
      }

   }

   public void handleScroll(int diff) {
      boolean highest = true;
      FixedComponent focusComponent = null;
      Iterator var4 = this.components.iterator();

      while(var4.hasNext()) {
         FixedComponent component = (FixedComponent)var4.next();
         Context context = new Context(this.inter, component.getWidth(this.inter), component.getPosition(this.inter), true, highest);
         component.handleScroll(context, diff);
         if (context.isHovered()) {
            highest = false;
         }

         if (context.foucsRequested()) {
            focusComponent = component;
         }
      }

      if (focusComponent != null) {
         this.components.remove(focusComponent);
         this.components.add(focusComponent);
      }

   }

   public void exit() {
      boolean highest = true;
      FixedComponent focusComponent = null;
      Iterator var3 = this.components.iterator();

      while(var3.hasNext()) {
         FixedComponent component = (FixedComponent)var3.next();
         Context context = new Context(this.inter, component.getWidth(this.inter), component.getPosition(this.inter), true, highest);
         component.exit(context);
         if (context.isHovered()) {
            highest = false;
         }

         if (context.foucsRequested()) {
            focusComponent = component;
         }
      }

      if (focusComponent != null) {
         this.components.remove(focusComponent);
         this.components.add(focusComponent);
      }

   }
}
