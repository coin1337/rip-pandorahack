package com.lukflug.panelstudio.tabgui;

import com.lukflug.panelstudio.Context;
import com.lukflug.panelstudio.settings.Toggleable;

public class TabGUIItem implements TabGUIComponent {
   protected String title;
   protected Toggleable toggle;

   public TabGUIItem(String title, Toggleable toggle) {
      this.title = title;
      this.toggle = toggle;
   }

   public String getTitle() {
      return this.title;
   }

   public void render(Context context) {
   }

   public void handleButton(Context context, int button) {
   }

   public void handleKey(Context context, int scancode) {
   }

   public void handleScroll(Context context, int diff) {
   }

   public void getHeight(Context context) {
   }

   public void exit(Context context) {
   }

   public boolean isActive() {
      return this.toggle.isOn();
   }

   public boolean select() {
      this.toggle.toggle();
      return false;
   }

   public void releaseFocus() {
   }
}
