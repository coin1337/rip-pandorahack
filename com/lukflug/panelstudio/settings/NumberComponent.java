package com.lukflug.panelstudio.settings;

import com.lukflug.panelstudio.Context;
import com.lukflug.panelstudio.Slider;
import com.lukflug.panelstudio.theme.Renderer;

public class NumberComponent extends Slider {
   protected NumberSetting setting;
   protected String text;

   public NumberComponent(String text, Renderer renderer, NumberSetting setting, double min, double max) {
      super("", renderer);
      this.setting = setting;
      this.text = text;
   }

   public void render(Context context) {
      if (this.setting.getPrecision() == 0) {
         this.title = String.format("%s: §7%d", this.text, (int)this.setting.getNumber());
      } else {
         this.title = String.format("%s: §7%." + this.setting.getPrecision() + "f", this.text, this.setting.getNumber());
      }

      super.render(context);
   }

   protected double getValue() {
      return (this.setting.getNumber() - this.setting.getMinimumValue()) / (this.setting.getMaximumValue() - this.setting.getMinimumValue());
   }

   protected void setValue(double value) {
      this.setting.setNumber(value * (this.setting.getMaximumValue() - this.setting.getMinimumValue()) + this.setting.getMinimumValue());
   }
}
