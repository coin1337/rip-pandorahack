package com.pandora.client.module;

import com.lukflug.panelstudio.settings.KeybindSetting;
import com.lukflug.panelstudio.settings.Toggleable;
import com.pandora.api.event.events.RenderEvent;
import com.pandora.api.settings.Setting;
import com.pandora.api.util.render.PandoraColor;
import com.pandora.client.PandoraMod;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public abstract class Module implements Toggleable, KeybindSetting {
   protected static final Minecraft mc = Minecraft.func_71410_x();
   String name;
   Module.Category category;
   int bind;
   boolean enabled;
   boolean drawn;

   public Module(String n, Module.Category c) {
      this.name = n;
      this.category = c;
      this.bind = 0;
      this.enabled = false;
      this.drawn = true;
      this.setup();
   }

   public String getName() {
      return this.name;
   }

   public void setName(String n) {
      this.name = n;
   }

   public Module.Category getCategory() {
      return this.category;
   }

   public void setCategory(Module.Category c) {
      this.category = c;
   }

   public int getBind() {
      return this.bind;
   }

   public void setBind(int b) {
      this.bind = b;
   }

   protected void onEnable() {
   }

   protected void onDisable() {
   }

   public void onUpdate() {
   }

   public void onRender() {
   }

   public void onWorldRender(RenderEvent event) {
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean e) {
      this.enabled = e;
   }

   public void enable() {
      this.setEnabled(true);
      this.onEnable();
   }

   public void disable() {
      this.setEnabled(false);
      this.onDisable();
   }

   public void toggle() {
      if (this.isEnabled()) {
         this.disable();
      } else if (!this.isEnabled()) {
         this.enable();
      }

   }

   public String getHudInfo() {
      return "";
   }

   public void setup() {
   }

   public boolean isDrawn() {
      return this.drawn;
   }

   public void setDrawn(boolean d) {
      this.drawn = d;
   }

   protected Setting.Integer registerInteger(String name, String configName, int value, int min, int max) {
      Setting.Integer setting = new Setting.Integer(name, configName, this, this.getCategory(), value, min, max);
      PandoraMod.getInstance().settingsManager.addSetting(setting);
      return setting;
   }

   protected Setting.Double registerDouble(String name, String configName, double value, double min, double max) {
      Setting.Double setting = new Setting.Double(name, configName, this, this.getCategory(), value, min, max);
      PandoraMod.getInstance().settingsManager.addSetting(setting);
      return setting;
   }

   protected Setting.Boolean registerBoolean(String name, String configName, boolean value) {
      Setting.Boolean setting = new Setting.Boolean(name, configName, this, this.getCategory(), value);
      PandoraMod.getInstance().settingsManager.addSetting(setting);
      return setting;
   }

   protected Setting.Mode registerMode(String name, String configName, List<String> modes, String value) {
      Setting.Mode setting = new Setting.Mode(name, configName, this, this.getCategory(), modes, value);
      PandoraMod.getInstance().settingsManager.addSetting(setting);
      return setting;
   }

   protected Setting.ColorSetting registerColor(String name, String configName, PandoraColor color) {
      Setting.ColorSetting setting = new Setting.ColorSetting(name, configName, this, this.getCategory(), false, color);
      PandoraMod.getInstance().settingsManager.addSetting(setting);
      return setting;
   }

   protected Setting.ColorSetting registerColor(String name, String configName) {
      return this.registerColor(name, configName, new PandoraColor(90, 145, 240));
   }

   public boolean isOn() {
      return this.enabled;
   }

   public int getKey() {
      return this.bind;
   }

   public void setKey(int key) {
      this.bind = key;
   }

   public String getKeyName() {
      return Keyboard.getKeyName(this.bind);
   }

   public static enum Category {
      Combat,
      Exploits,
      Movement,
      Misc,
      Render,
      HUD,
      GUI;

      // $FF: synthetic method
      private static Module.Category[] $values() {
         return new Module.Category[]{Combat, Exploits, Movement, Misc, Render, HUD, GUI};
      }
   }
}
