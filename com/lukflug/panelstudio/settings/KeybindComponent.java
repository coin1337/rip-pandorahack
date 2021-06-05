package com.lukflug.panelstudio.settings;

import com.lukflug.panelstudio.Context;
import com.lukflug.panelstudio.FocusableComponent;
import com.lukflug.panelstudio.theme.Renderer;

public class KeybindComponent extends FocusableComponent {
   protected KeybindSetting keybind;

   public KeybindComponent(Renderer renderer, KeybindSetting keybind) {
      super("Keybind: §7", renderer);
      this.keybind = keybind;
   }

   public void render(Context context) {
      super.render(context);
      String text = this.title + this.keybind.getKeyName();
      if (this.hasFocus(context)) {
         text = this.title + "...";
      }

      this.renderer.renderTitle(context, text, this.hasFocus(context), this.hasFocus(context));
   }

   public void handleButton(Context context, int button) {
      super.handleButton(context, button);
      context.setHeight(this.renderer.getHeight());
      boolean isSelected = this.hasFocus(context);
      super.handleButton(context, button);
      if (isSelected && !this.hasFocus(context)) {
         this.keybind.setKey(0);
      }

   }

   public void handleKey(Context context, int scancode) {
      super.handleKey(context, scancode);
      if (this.hasFocus(context)) {
         this.keybind.setKey(scancode);
         this.releaseFocus();
      }

   }

   public void exit(Context context) {
      super.exit(context);
      if (this.hasFocus(context)) {
         this.keybind.setKey(0);
         this.releaseFocus();
      }

   }
}
