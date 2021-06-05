package com.lukflug.panelstudio.settings;

public class SimpleToggleable implements Toggleable {
   private boolean value;

   public SimpleToggleable(boolean value) {
      this.value = value;
   }

   public void toggle() {
      this.value = !this.value;
   }

   public boolean isOn() {
      return this.value;
   }
}
