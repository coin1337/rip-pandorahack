package com.lukflug.panelstudio.hud;

import com.lukflug.panelstudio.ClickGUI;
import com.lukflug.panelstudio.FixedComponent;
import com.lukflug.panelstudio.Interface;
import com.lukflug.panelstudio.settings.Toggleable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class HUDClickGUI extends ClickGUI implements Toggleable {
   protected List<FixedComponent> allComponents = new ArrayList();
   protected Set<FixedComponent> hudComponents = new HashSet();
   protected boolean guiOpen = false;

   public HUDClickGUI(Interface inter) {
      super(inter);
   }

   public List<FixedComponent> getComponents() {
      return this.allComponents;
   }

   public void addComponent(FixedComponent component) {
      this.allComponents.add(component);
      if (this.guiOpen) {
         super.addComponent(component);
      }

   }

   public void addHUDComponent(FixedComponent component) {
      this.hudComponents.add(component);
      this.addComponent(component);
      if (!this.guiOpen) {
         super.addComponent(component);
      }

   }

   public void toggle() {
      this.guiOpen = !this.guiOpen;
      if (this.guiOpen) {
         this.components = this.allComponents;
      } else {
         this.selectHUDComponents();
      }

   }

   public boolean isOn() {
      return this.guiOpen;
   }

   protected void selectHUDComponents() {
      this.components = new ArrayList();
      Iterator var1 = this.allComponents.iterator();

      while(var1.hasNext()) {
         FixedComponent component = (FixedComponent)var1.next();
         if (this.hudComponents.contains(component)) {
            this.components.add(component);
         }
      }

   }
}
