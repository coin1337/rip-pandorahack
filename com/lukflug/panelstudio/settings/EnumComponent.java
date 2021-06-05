package com.lukflug.panelstudio.settings;

import com.lukflug.panelstudio.Context;
import com.lukflug.panelstudio.FocusableComponent;
import com.lukflug.panelstudio.theme.Renderer;

public class EnumComponent extends FocusableComponent {
   protected EnumSetting setting;

   public EnumComponent(String title, Renderer renderer, EnumSetting setting) {
      super(title, renderer);
      this.setting = setting;
   }

   public void render(Context context) {
      super.render(context);
      this.renderer.renderTitle(context, this.title + ": §7" + this.setting.getValueName(), this.hasFocus(context));
   }

   public void handleButton(Context context, int button) {
      super.handleButton(context, button);
      if (button == 0 && context.isClicked()) {
         this.setting.increment();
      }

   }
}
