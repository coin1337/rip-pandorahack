package com.lukflug.panelstudio.settings;

import com.lukflug.panelstudio.Context;
import com.lukflug.panelstudio.FocusableComponent;
import com.lukflug.panelstudio.theme.Renderer;

public class BooleanComponent extends FocusableComponent {
   protected Toggleable setting;

   public BooleanComponent(String title, Renderer renderer, Toggleable setting) {
      super(title, renderer);
      this.setting = setting;
   }

   public void render(Context context) {
      super.render(context);
      this.renderer.renderTitle(context, this.title, this.hasFocus(context), this.setting.isOn());
   }

   public void handleButton(Context context, int button) {
      super.handleButton(context, button);
      if (button == 0 && context.isClicked()) {
         this.setting.toggle();
      }

   }
}
