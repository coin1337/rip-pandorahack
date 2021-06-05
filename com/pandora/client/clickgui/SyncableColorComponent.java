package com.pandora.client.clickgui;

import com.lukflug.panelstudio.Animation;
import com.lukflug.panelstudio.Context;
import com.lukflug.panelstudio.FocusableComponent;
import com.lukflug.panelstudio.settings.ColorComponent;
import com.lukflug.panelstudio.settings.Toggleable;
import com.lukflug.panelstudio.theme.Renderer;
import com.lukflug.panelstudio.theme.Theme;
import com.pandora.api.settings.Setting;
import com.pandora.client.module.modules.gui.ClickGuiModule;
import net.minecraft.util.text.TextFormatting;

public class SyncableColorComponent extends ColorComponent {
   public SyncableColorComponent(Theme theme, Setting.ColorSetting setting, Toggleable colorToggle, Animation animation) {
      super(TextFormatting.BOLD + setting.getName(), theme.getContainerRenderer(), animation, theme.getComponentRenderer(), setting, false, true, colorToggle);
      if (setting != ClickGuiModule.enabledColor) {
         this.addComponent(new SyncableColorComponent.SyncButton(theme.getComponentRenderer()));
      }

   }

   private class SyncButton extends FocusableComponent {
      public SyncButton(Renderer renderer) {
         super("Sync Color", renderer);
      }

      public void render(Context context) {
         super.render(context);
         this.renderer.overrideColorScheme(SyncableColorComponent.this.overrideScheme);
         this.renderer.renderTitle(context, this.title, this.hasFocus(context), false);
         this.renderer.restoreColorScheme();
      }

      public void handleButton(Context context, int button) {
         super.handleButton(context, button);
         if (button == 0 && context.isClicked()) {
            SyncableColorComponent.this.setting.setValue(ClickGuiModule.enabledColor.getColor());
            SyncableColorComponent.this.setting.setRainbow(ClickGuiModule.enabledColor.getRainbow());
         }

      }
   }
}
