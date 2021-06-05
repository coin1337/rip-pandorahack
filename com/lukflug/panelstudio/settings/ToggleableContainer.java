package com.lukflug.panelstudio.settings;

import com.lukflug.panelstudio.Animation;
import com.lukflug.panelstudio.CollapsibleContainer;
import com.lukflug.panelstudio.Context;
import com.lukflug.panelstudio.theme.Renderer;

public class ToggleableContainer extends CollapsibleContainer {
   protected Toggleable toggle;

   public ToggleableContainer(String title, Renderer renderer, Toggleable open, Animation animation, Toggleable toggle) {
      super(title, renderer, open, animation);
      this.toggle = toggle;
   }

   public void handleButton(Context context, int button) {
      context.setHeight(this.renderer.getHeight());
      if (context.isClicked() && context.getInterface().getMouse().y <= context.getPos().y + context.getSize().height && button == 0) {
         this.toggle.toggle();
      }

      super.handleButton(context, button);
   }

   protected boolean isActive() {
      return this.toggle.isOn();
   }
}
