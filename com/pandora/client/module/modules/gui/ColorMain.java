package com.pandora.client.module.modules.gui;

import com.pandora.api.settings.Setting;
import com.pandora.api.util.render.PandoraColor;
import com.pandora.client.module.Module;
import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.util.text.TextFormatting;

public class ColorMain extends Module {
   public static Setting.Mode colorModel;
   public static Setting.Mode friendColor;
   public static Setting.Mode enemyColor;
   public static Setting.Mode chatEnableColor;
   public static Setting.Mode chatDisableColor;
   public static Setting.Boolean customFont;

   public ColorMain() {
      super("Colors", Module.Category.GUI);
      this.setDrawn(false);
   }

   public void setup() {
      ArrayList<String> tab = new ArrayList();
      tab.add("Black");
      tab.add("Dark Green");
      tab.add("Dark Red");
      tab.add("Gold");
      tab.add("Dark Gray");
      tab.add("Green");
      tab.add("Red");
      tab.add("Yellow");
      tab.add("Dark Blue");
      tab.add("Dark Aqua");
      tab.add("Dark Purple");
      tab.add("Gray");
      tab.add("Blue");
      tab.add("Aqua");
      tab.add("Light Purple");
      tab.add("White");
      customFont = this.registerBoolean("Custom Font", "CustomFont", true);
      friendColor = this.registerMode("Friend", "FriendColor", tab, "Blue");
      enemyColor = this.registerMode("Enemy", "EnemyColor", tab, "Red");
      chatEnableColor = this.registerMode("Msg Enbl", "MsgEnbl", tab, "Green");
      chatDisableColor = this.registerMode("Msg Dsbl", "MsgDsbl", tab, "Red");
      ArrayList<String> models = new ArrayList();
      models.add("RGB");
      models.add("HSB");
      colorModel = this.registerMode("Color Model", "ColorModel", models, "HSB");
   }

   public void onEnable() {
      this.disable();
   }

   private static TextFormatting settingToFormatting(Setting.Mode setting) {
      if (setting.getValue().equalsIgnoreCase("Black")) {
         return TextFormatting.BLACK;
      } else if (setting.getValue().equalsIgnoreCase("Dark Green")) {
         return TextFormatting.DARK_GREEN;
      } else if (setting.getValue().equalsIgnoreCase("Dark Red")) {
         return TextFormatting.DARK_RED;
      } else if (setting.getValue().equalsIgnoreCase("Gold")) {
         return TextFormatting.GOLD;
      } else if (setting.getValue().equalsIgnoreCase("Dark Gray")) {
         return TextFormatting.DARK_GRAY;
      } else if (setting.getValue().equalsIgnoreCase("Green")) {
         return TextFormatting.GREEN;
      } else if (setting.getValue().equalsIgnoreCase("Red")) {
         return TextFormatting.RED;
      } else if (setting.getValue().equalsIgnoreCase("Yellow")) {
         return TextFormatting.YELLOW;
      } else if (setting.getValue().equalsIgnoreCase("Dark Blue")) {
         return TextFormatting.DARK_BLUE;
      } else if (setting.getValue().equalsIgnoreCase("Dark Aqua")) {
         return TextFormatting.DARK_AQUA;
      } else if (setting.getValue().equalsIgnoreCase("Dark Purple")) {
         return TextFormatting.DARK_PURPLE;
      } else if (setting.getValue().equalsIgnoreCase("Gray")) {
         return TextFormatting.GRAY;
      } else if (setting.getValue().equalsIgnoreCase("Blue")) {
         return TextFormatting.BLUE;
      } else if (setting.getValue().equalsIgnoreCase("Light Purple")) {
         return TextFormatting.LIGHT_PURPLE;
      } else if (setting.getValue().equalsIgnoreCase("White")) {
         return TextFormatting.WHITE;
      } else {
         return setting.getValue().equalsIgnoreCase("Aqua") ? TextFormatting.AQUA : null;
      }
   }

   public static TextFormatting getFriendColor() {
      return settingToFormatting(friendColor);
   }

   public static TextFormatting getEnemyColor() {
      return settingToFormatting(enemyColor);
   }

   public static TextFormatting getEnabledColor() {
      return settingToFormatting(chatEnableColor);
   }

   public static TextFormatting getDisabledColor() {
      return settingToFormatting(chatDisableColor);
   }

   private static Color settingToColor(Setting.Mode setting) {
      if (setting.getValue().equalsIgnoreCase("Black")) {
         return Color.BLACK;
      } else if (setting.getValue().equalsIgnoreCase("Dark Green")) {
         return Color.GREEN.darker();
      } else if (setting.getValue().equalsIgnoreCase("Dark Red")) {
         return Color.RED.darker();
      } else if (setting.getValue().equalsIgnoreCase("Gold")) {
         return Color.yellow.darker();
      } else if (setting.getValue().equalsIgnoreCase("Dark Gray")) {
         return Color.DARK_GRAY;
      } else if (setting.getValue().equalsIgnoreCase("Green")) {
         return Color.green;
      } else if (setting.getValue().equalsIgnoreCase("Red")) {
         return Color.red;
      } else if (setting.getValue().equalsIgnoreCase("Yellow")) {
         return Color.yellow;
      } else if (setting.getValue().equalsIgnoreCase("Dark Blue")) {
         return Color.blue.darker();
      } else if (setting.getValue().equalsIgnoreCase("Dark Aqua")) {
         return Color.CYAN.darker();
      } else if (setting.getValue().equalsIgnoreCase("Dark Purple")) {
         return Color.MAGENTA.darker();
      } else if (setting.getValue().equalsIgnoreCase("Gray")) {
         return Color.GRAY;
      } else if (setting.getValue().equalsIgnoreCase("Blue")) {
         return Color.blue;
      } else if (setting.getValue().equalsIgnoreCase("Light Purple")) {
         return Color.magenta;
      } else if (setting.getValue().equalsIgnoreCase("White")) {
         return Color.WHITE;
      } else {
         return setting.getValue().equalsIgnoreCase("Aqua") ? Color.cyan : Color.WHITE;
      }
   }

   public static PandoraColor getFriendGSColor() {
      return new PandoraColor(settingToColor(friendColor));
   }

   public static PandoraColor getEnemyGSColor() {
      return new PandoraColor(settingToColor(enemyColor));
   }
}
