package com.lukflug.panelstudio.settings;

import com.lukflug.panelstudio.Animation;
import com.lukflug.panelstudio.CollapsibleContainer;
import com.lukflug.panelstudio.Context;
import com.lukflug.panelstudio.FocusableComponent;
import com.lukflug.panelstudio.Slider;
import com.lukflug.panelstudio.theme.ColorScheme;
import com.lukflug.panelstudio.theme.Renderer;
import java.awt.Color;

public class ColorComponent extends CollapsibleContainer {
   protected ColorSetting setting;
   protected final boolean alpha;
   protected final boolean rainbow;
   protected ColorScheme scheme;
   protected ColorScheme overrideScheme;
   protected Toggleable colorModel;

   public ColorComponent(String title, Renderer renderer, Animation animation, Renderer componentRenderer, ColorSetting setting, boolean alpha, boolean rainbow, Toggleable colorModel) {
      super(title, renderer, new SimpleToggleable(false), animation);
      this.setting = setting;
      this.alpha = alpha;
      this.rainbow = rainbow;
      this.scheme = new ColorComponent.ColorSettingScheme(renderer);
      this.overrideScheme = new ColorComponent.ColorSettingScheme(componentRenderer);
      this.colorModel = colorModel;
      if (rainbow) {
         this.addComponent(new ColorComponent.ColorButton(componentRenderer));
      }

      this.addComponent(new ColorComponent.ColorSlider(componentRenderer, 0));
      this.addComponent(new ColorComponent.ColorSlider(componentRenderer, 1));
      this.addComponent(new ColorComponent.ColorSlider(componentRenderer, 2));
      if (alpha) {
         this.addComponent(new ColorComponent.ColorSlider(componentRenderer, 3));
      }

   }

   public void render(Context context) {
      this.renderer.overrideColorScheme(this.scheme);
      super.render(context);
      this.renderer.restoreColorScheme();
   }

   protected class ColorSettingScheme implements ColorScheme {
      ColorScheme scheme;

      public ColorSettingScheme(Renderer renderer) {
         this.scheme = renderer.getDefaultColorScheme();
      }

      public Color getActiveColor() {
         return ColorComponent.this.setting.getValue();
      }

      public Color getInactiveColor() {
         return this.scheme.getInactiveColor();
      }

      public Color getBackgroundColor() {
         return this.scheme.getBackgroundColor();
      }

      public Color getOutlineColor() {
         return this.scheme.getOutlineColor();
      }

      public Color getFontColor() {
         return this.scheme.getFontColor();
      }

      public int getOpacity() {
         return this.scheme.getOpacity();
      }
   }

   protected class ColorSlider extends Slider {
      private final int value;

      public ColorSlider(Renderer renderer, int value) {
         super("", renderer);
         this.value = value;
      }

      public void render(Context context) {
         this.title = this.getTitle(this.value) + (int)((double)this.getMax() * this.getValue());
         this.renderer.overrideColorScheme(ColorComponent.this.overrideScheme);
         super.render(context);
         this.renderer.restoreColorScheme();
      }

      protected double getValue() {
         Color c = ColorComponent.this.setting.getColor();
         if (this.value < 3) {
            if (ColorComponent.this.colorModel.isOn()) {
               return (double)Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), (float[])null)[this.value];
            }

            switch(this.value) {
            case 0:
               return (double)c.getRed() / 255.0D;
            case 1:
               return (double)c.getGreen() / 255.0D;
            case 2:
               return (double)c.getBlue() / 255.0D;
            }
         }

         return (double)c.getAlpha() / 255.0D;
      }

      protected void setValue(double value) {
         Color c = ColorComponent.this.setting.getColor();
         float[] color = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), (float[])null);
         switch(this.value) {
         case 0:
            if (ColorComponent.this.colorModel.isOn()) {
               c = Color.getHSBColor((float)value, color[1], color[2]);
            } else {
               c = new Color((int)(255.0D * value), c.getGreen(), c.getBlue());
            }

            if (ColorComponent.this.alpha) {
               ColorComponent.this.setting.setValue(new Color(c.getRed(), c.getGreen(), c.getBlue(), ColorComponent.this.setting.getColor().getAlpha()));
            } else {
               ColorComponent.this.setting.setValue(c);
            }
            break;
         case 1:
            if (ColorComponent.this.colorModel.isOn()) {
               c = Color.getHSBColor(color[0], (float)value, color[2]);
            } else {
               c = new Color(c.getRed(), (int)(255.0D * value), c.getBlue());
            }

            if (ColorComponent.this.alpha) {
               ColorComponent.this.setting.setValue(new Color(c.getRed(), c.getGreen(), c.getBlue(), ColorComponent.this.setting.getColor().getAlpha()));
            } else {
               ColorComponent.this.setting.setValue(c);
            }
            break;
         case 2:
            if (ColorComponent.this.colorModel.isOn()) {
               c = Color.getHSBColor(color[0], color[1], (float)value);
            } else {
               c = new Color(c.getRed(), c.getGreen(), (int)(255.0D * value));
            }

            if (ColorComponent.this.alpha) {
               ColorComponent.this.setting.setValue(new Color(c.getRed(), c.getGreen(), c.getBlue(), ColorComponent.this.setting.getColor().getAlpha()));
            } else {
               ColorComponent.this.setting.setValue(c);
            }
            break;
         case 3:
            ColorComponent.this.setting.setValue(new Color((float)c.getRed(), (float)c.getGreen(), (float)c.getBlue(), (float)value * 255.0F));
         }

      }

      protected String getTitle(int value) {
         switch(value) {
         case 0:
            return (ColorComponent.this.colorModel.isOn() ? "Hue:" : "Red:") + " §7";
         case 1:
            return (ColorComponent.this.colorModel.isOn() ? "Saturation:" : "Green:") + " §7";
         case 2:
            return (ColorComponent.this.colorModel.isOn() ? "Brightness:" : "Blue:") + " §7";
         case 3:
            return "Alpha: §7";
         default:
            return "";
         }
      }

      protected int getMax() {
         if (!ColorComponent.this.colorModel.isOn()) {
            return 255;
         } else if (this.value == 0) {
            return 360;
         } else {
            return this.value < 3 ? 100 : 255;
         }
      }
   }

   protected class ColorButton extends FocusableComponent {
      public ColorButton(Renderer renderer) {
         super("Rainbow", renderer);
      }

      public void render(Context context) {
         super.render(context);
         this.renderer.overrideColorScheme(ColorComponent.this.overrideScheme);
         this.renderer.renderTitle(context, this.title, this.hasFocus(context), ColorComponent.this.setting.getRainbow());
         this.renderer.restoreColorScheme();
      }

      public void handleButton(Context context, int button) {
         super.handleButton(context, button);
         if (button == 0 && context.isClicked()) {
            ColorComponent.this.setting.setRainbow(!ColorComponent.this.setting.getRainbow());
         }

      }
   }
}
