package com.pandora.client.module.modules.gui;

import com.pandora.api.settings.Setting;
import com.pandora.api.util.misc.MessageBus;
import com.pandora.api.util.render.PandoraColor;
import com.pandora.client.PandoraMod;
import com.pandora.client.module.Module;
import com.pandora.client.module.ModuleManager;
import com.pandora.client.module.modules.misc.Announcer;
import java.util.ArrayList;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

public class ClickGuiModule extends Module {
   public ClickGuiModule INSTANCE;
   public static Setting.Integer scrollSpeed;
   public static Setting.Integer opacity;
   public static Setting.ColorSetting enabledColor;
   public static Setting.ColorSetting outlineColor;
   public static Setting.ColorSetting backgroundColor;
   public static Setting.ColorSetting settingBackgroundColor;
   public static Setting.ColorSetting fontColor;
   public static Setting.Integer animationSpeed;
   Setting.Boolean backgroundBlur;
   public static Setting.Mode scrolling;
   public static Setting.Boolean showHUD;
   private ResourceLocation shader = new ResourceLocation("minecraft", "shaders/post/blur.json");

   public ClickGuiModule() {
      super("ClickGUI", Module.Category.GUI);
      this.setBind(24);
      this.setDrawn(false);
      this.INSTANCE = this;
   }

   public void setup() {
      this.backgroundBlur = this.registerBoolean("Blur", "Blur", false);
      opacity = this.registerInteger("Opacity", "Opacity", 150, 50, 255);
      scrollSpeed = this.registerInteger("Scroll Speed", "ScrollSpeed", 10, 1, 20);
      outlineColor = this.registerColor("Outline", "Outline", new PandoraColor(255, 0, 0, 255));
      enabledColor = this.registerColor("Enabled", "Enabled", new PandoraColor(255, 0, 0, 255));
      backgroundColor = this.registerColor("Background", "Background", new PandoraColor(0, 0, 0, 255));
      settingBackgroundColor = this.registerColor("Setting", "Setting", new PandoraColor(30, 30, 30, 255));
      fontColor = this.registerColor("Font", "Font", new PandoraColor(255, 255, 255, 255));
      animationSpeed = this.registerInteger("Animation Speed", "AnimationSpeed", 200, 0, 1000);
      ArrayList<String> models = new ArrayList();
      models.add("Screen");
      models.add("Container");
      scrolling = this.registerMode("Scrolling", "ScrollingMode", models, "Screen");
      showHUD = this.registerBoolean("Show HUD Panels", "ShowHUD", true);
   }

   public void onEnable() {
      mc.func_147108_a(PandoraMod.getInstance().clickGUI);
      if (!PandoraMod.getInstance().clickGUI.gui.isOn()) {
         PandoraMod.getInstance().clickGUI.gui.toggle();
      }

      if (this.backgroundBlur.getValue()) {
         mc.field_71460_t.func_175069_a(this.shader);
      }

      if (((Announcer)ModuleManager.getModuleByName("Announcer")).clickGui.getValue() && ModuleManager.isModuleEnabled("Announcer") && mc.field_71439_g != null) {
         if (((Announcer)ModuleManager.getModuleByName("Announcer")).clientSide.getValue()) {
            MessageBus.sendClientPrefixMessage(Announcer.guiMessage);
         } else {
            MessageBus.sendServerMessage(Announcer.guiMessage);
         }
      }

   }

   public void onUpdate() {
      if (this.backgroundBlur.getValue() && !mc.field_71460_t.func_147702_a()) {
         mc.field_71460_t.func_175069_a(this.shader);
      }

      if (!this.backgroundBlur.getValue() && mc.field_71460_t.func_147702_a()) {
         mc.field_71460_t.func_181022_b();
      }

      if (Keyboard.isKeyDown(1)) {
         this.disable();
      }

   }

   public void onDisable() {
      if (mc.field_71460_t.func_147702_a()) {
         mc.field_71460_t.func_181022_b();
      }

   }
}
