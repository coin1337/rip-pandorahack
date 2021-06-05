package com.pandora.api.settings;

import com.lukflug.panelstudio.settings.EnumSetting;
import com.lukflug.panelstudio.settings.NumberSetting;
import com.lukflug.panelstudio.settings.Toggleable;
import com.pandora.api.util.render.PandoraColor;
import com.pandora.client.module.Module;
import java.awt.Color;
import java.util.List;

public abstract class Setting {
   private final String name;
   private final String configName;
   private final Module parent;
   private final Module.Category category;
   private final Setting.Type type;

   public Setting(String name, String configName, Module parent, Module.Category category, Setting.Type type) {
      this.name = name;
      this.configName = configName;
      this.parent = parent;
      this.type = type;
      this.category = category;
   }

   public String getName() {
      return this.name;
   }

   public String getConfigName() {
      return this.configName;
   }

   public Module getParent() {
      return this.parent;
   }

   public Setting.Type getType() {
      return this.type;
   }

   public Module.Category getCategory() {
      return this.category;
   }

   public static enum Type {
      INTEGER,
      DOUBLE,
      BOOLEAN,
      MODE,
      COLOR;

      // $FF: synthetic method
      private static Setting.Type[] $values() {
         return new Setting.Type[]{INTEGER, DOUBLE, BOOLEAN, MODE, COLOR};
      }
   }

   public static class ColorSetting extends Setting implements com.lukflug.panelstudio.settings.ColorSetting {
      private boolean rainbow;
      private PandoraColor value;

      public ColorSetting(String name, String configName, Module parent, Module.Category category, boolean rainbow, PandoraColor value) {
         super(name, configName, parent, category, Setting.Type.COLOR);
         this.rainbow = rainbow;
         this.value = value;
      }

      public PandoraColor getValue() {
         return this.rainbow ? PandoraColor.fromHSB((float)(System.currentTimeMillis() % 11520L) / 11520.0F, 1.0F, 1.0F) : this.value;
      }

      public void setValue(boolean rainbow, PandoraColor value) {
         this.rainbow = rainbow;
         this.value = value;
      }

      public int toInteger() {
         return this.value.getRGB() & 16777215 + (this.rainbow ? 1 : 0) * 16777216;
      }

      public void fromInteger(int number) {
         this.value = new PandoraColor(number & 16777215);
         this.rainbow = (number & 16777216) != 0;
      }

      public PandoraColor getColor() {
         return this.value;
      }

      public boolean getRainbow() {
         return this.rainbow;
      }

      public void setValue(Color value) {
         this.setValue(this.getRainbow(), new PandoraColor(value));
      }

      public void setRainbow(boolean rainbow) {
         this.rainbow = rainbow;
      }
   }

   public static class Mode extends Setting implements EnumSetting {
      private String value;
      private final List<String> modes;

      public Mode(String name, String configName, Module parent, Module.Category category, List<String> modes, String value) {
         super(name, configName, parent, category, Setting.Type.MODE);
         this.value = value;
         this.modes = modes;
      }

      public String getValue() {
         return this.value;
      }

      public void setValue(String value) {
         this.value = value;
      }

      public List<String> getModes() {
         return this.modes;
      }

      public void increment() {
         int modeIndex = this.modes.indexOf(this.value);
         modeIndex = (modeIndex + 1) % this.modes.size();
         this.setValue((String)this.modes.get(modeIndex));
      }

      public String getValueName() {
         return this.value;
      }
   }

   public static class Boolean extends Setting implements Toggleable {
      private boolean value;

      public Boolean(String name, String configName, Module parent, Module.Category category, boolean value) {
         super(name, configName, parent, category, Setting.Type.BOOLEAN);
         this.value = value;
      }

      public boolean getValue() {
         return this.value;
      }

      public void setValue(boolean value) {
         this.value = value;
      }

      public void toggle() {
         this.value = !this.value;
      }

      public boolean isOn() {
         return this.value;
      }
   }

   public static class Double extends Setting implements NumberSetting {
      private double value;
      private final double min;
      private final double max;

      public Double(String name, String configName, Module parent, Module.Category category, double value, double min, double max) {
         super(name, configName, parent, category, Setting.Type.DOUBLE);
         this.value = value;
         this.min = min;
         this.max = max;
      }

      public double getValue() {
         return this.value;
      }

      public void setValue(double value) {
         this.value = value;
      }

      public double getMin() {
         return this.min;
      }

      public double getMax() {
         return this.max;
      }

      public double getNumber() {
         return this.value;
      }

      public void setNumber(double value) {
         this.value = value;
      }

      public double getMaximumValue() {
         return this.max;
      }

      public double getMinimumValue() {
         return this.min;
      }

      public int getPrecision() {
         return 2;
      }
   }

   public static class Integer extends Setting implements NumberSetting {
      private int value;
      private final int min;
      private final int max;

      public Integer(String name, String configName, Module parent, Module.Category category, int value, int min, int max) {
         super(name, configName, parent, category, Setting.Type.INTEGER);
         this.value = value;
         this.min = min;
         this.max = max;
      }

      public int getValue() {
         return this.value;
      }

      public void setValue(int value) {
         this.value = value;
      }

      public int getMin() {
         return this.min;
      }

      public int getMax() {
         return this.max;
      }

      public double getNumber() {
         return (double)this.value;
      }

      public void setNumber(double value) {
         this.value = (int)Math.round(value);
      }

      public double getMaximumValue() {
         return (double)this.max;
      }

      public double getMinimumValue() {
         return (double)this.min;
      }

      public int getPrecision() {
         return 0;
      }
   }
}
