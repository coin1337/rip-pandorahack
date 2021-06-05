package com.pandora.client.clickgui;

import com.lukflug.panelstudio.Context;
import com.lukflug.panelstudio.settings.KeybindComponent;
import com.lukflug.panelstudio.settings.KeybindSetting;
import com.lukflug.panelstudio.theme.Renderer;

public class PandoraKeybind extends KeybindComponent {
   public PandoraKeybind(Renderer renderer, KeybindSetting keybind) {
      super(renderer, keybind);
   }

   public void handleKey(Context context, int scancode) {
      context.setHeight(this.renderer.getHeight());
      if (this.hasFocus(context) && scancode == 211) {
         this.keybind.setKey(0);
         this.releaseFocus();
      } else {
         super.handleKey(context, scancode);
      }
   }
}
