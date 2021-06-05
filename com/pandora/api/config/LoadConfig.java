package com.pandora.api.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lukflug.panelstudio.DraggableContainer;
import com.lukflug.panelstudio.FixedComponent;
import com.pandora.api.settings.Setting;
import com.pandora.api.util.font.CFontRenderer;
import com.pandora.api.util.players.enemy.Enemies;
import com.pandora.api.util.players.friends.Friends;
import com.pandora.client.PandoraMod;
import com.pandora.client.command.Command;
import com.pandora.client.module.Module;
import com.pandora.client.module.ModuleManager;
import com.pandora.client.module.modules.misc.AutoGG;
import com.pandora.client.module.modules.misc.AutoReply;
import java.awt.Font;
import java.awt.Point;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.Iterator;

public class LoadConfig {
   String fileName = "Pandora/";
   String moduleName = "Modules/";
   String mainName = "Main/";
   String miscName = "Misc/";

   public LoadConfig() {
      try {
         this.loadConfig();
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }

   public void loadConfig() throws IOException {
      this.loadModules();
      this.loadEnabledModules();
      this.loadModuleKeybinds();
      this.loadDrawnModules();
      this.loadCommandPrefix();
      this.loadCustomFont();
      this.loadFriendsList();
      this.loadEnemiesList();
      this.loadClickGUIPositions();
      this.loadAutoGG();
      this.loadAutoReply();
   }

   public void loadModules() {
      String moduleLocation = this.fileName + this.moduleName;
      Iterator var2 = ModuleManager.getModules().iterator();

      while(var2.hasNext()) {
         Module module = (Module)var2.next();

         try {
            this.loadModuleDirect(moduleLocation, module);
         } catch (IOException var5) {
            System.out.println(module.getName());
            var5.printStackTrace();
         }
      }

   }

   public void loadModuleDirect(String moduleLocation, Module module) throws IOException {
      if (Files.exists(Paths.get(moduleLocation + module.getName() + ".json"), new LinkOption[0])) {
         InputStream inputStream = Files.newInputStream(Paths.get(moduleLocation + module.getName() + ".json"));
         JsonObject moduleObject = (new JsonParser()).parse(new InputStreamReader(inputStream)).getAsJsonObject();
         if (moduleObject.get("Module") != null) {
            JsonObject settingObject = moduleObject.get("Settings").getAsJsonObject();
            Iterator var6 = PandoraMod.getInstance().settingsManager.getSettingsForMod(module).iterator();

            while(var6.hasNext()) {
               Setting setting = (Setting)var6.next();
               JsonElement dataObject = settingObject.get(setting.getConfigName());
               if (dataObject != null && dataObject.isJsonPrimitive()) {
                  switch(setting.getType()) {
                  case BOOLEAN:
                     ((Setting.Boolean)setting).setValue(dataObject.getAsBoolean());
                     break;
                  case INTEGER:
                     ((Setting.Integer)setting).setValue(dataObject.getAsInt());
                     break;
                  case DOUBLE:
                     ((Setting.Double)setting).setValue(dataObject.getAsDouble());
                     break;
                  case COLOR:
                     ((Setting.ColorSetting)setting).fromInteger(dataObject.getAsInt());
                     break;
                  case MODE:
                     ((Setting.Mode)setting).setValue(dataObject.getAsString());
                  }
               }
            }

            inputStream.close();
         }
      }
   }

   public void loadEnabledModules() throws IOException {
      String enabledLocation = this.fileName + this.mainName;
      if (Files.exists(Paths.get(enabledLocation + "Toggle.json"), new LinkOption[0])) {
         InputStream inputStream = Files.newInputStream(Paths.get(enabledLocation + "Toggle.json"));
         JsonObject moduleObject = (new JsonParser()).parse(new InputStreamReader(inputStream)).getAsJsonObject();
         if (moduleObject.get("Modules") != null) {
            JsonObject settingObject = moduleObject.get("Modules").getAsJsonObject();
            Iterator var5 = ModuleManager.getModules().iterator();

            while(var5.hasNext()) {
               Module module = (Module)var5.next();
               JsonElement dataObject = settingObject.get(module.getName());
               if (dataObject != null && dataObject.isJsonPrimitive() && dataObject.getAsBoolean()) {
                  module.enable();
               }
            }

            inputStream.close();
         }
      }
   }

   public void loadModuleKeybinds() throws IOException {
      String bindLocation = this.fileName + this.mainName;
      if (Files.exists(Paths.get(bindLocation + "Bind.json"), new LinkOption[0])) {
         InputStream inputStream = Files.newInputStream(Paths.get(bindLocation + "Bind.json"));
         JsonObject moduleObject = (new JsonParser()).parse(new InputStreamReader(inputStream)).getAsJsonObject();
         if (moduleObject.get("Modules") != null) {
            JsonObject settingObject = moduleObject.get("Modules").getAsJsonObject();
            Iterator var5 = ModuleManager.getModules().iterator();

            while(var5.hasNext()) {
               Module module = (Module)var5.next();
               JsonElement dataObject = settingObject.get(module.getName());
               if (dataObject != null && dataObject.isJsonPrimitive()) {
                  module.setBind(dataObject.getAsInt());
               }
            }

            inputStream.close();
         }
      }
   }

   public void loadDrawnModules() throws IOException {
      String drawnLocation = this.fileName + this.mainName;
      if (Files.exists(Paths.get(drawnLocation + "Drawn.json"), new LinkOption[0])) {
         InputStream inputStream = Files.newInputStream(Paths.get(drawnLocation + "Drawn.json"));
         JsonObject moduleObject = (new JsonParser()).parse(new InputStreamReader(inputStream)).getAsJsonObject();
         if (moduleObject.get("Modules") != null) {
            JsonObject settingObject = moduleObject.get("Modules").getAsJsonObject();
            Iterator var5 = ModuleManager.getModules().iterator();

            while(var5.hasNext()) {
               Module module = (Module)var5.next();
               JsonElement dataObject = settingObject.get(module.getName());
               if (dataObject != null && dataObject.isJsonPrimitive()) {
                  module.setDrawn(dataObject.getAsBoolean());
               }
            }

            inputStream.close();
         }
      }
   }

   public void loadCommandPrefix() throws IOException {
      String prefixLocation = this.fileName + this.mainName;
      if (Files.exists(Paths.get(prefixLocation + "CommandPrefix.json"), new LinkOption[0])) {
         InputStream inputStream = Files.newInputStream(Paths.get(prefixLocation + "CommandPrefix.json"));
         JsonObject mainObject = (new JsonParser()).parse(new InputStreamReader(inputStream)).getAsJsonObject();
         if (mainObject.get("Prefix") != null) {
            JsonElement prefixObject = mainObject.get("Prefix");
            if (prefixObject != null && prefixObject.isJsonPrimitive()) {
               Command.setCommandPrefix(prefixObject.getAsString());
            }

            inputStream.close();
         }
      }
   }

   public void loadCustomFont() throws IOException {
      String fontLocation = this.fileName + this.miscName;
      if (Files.exists(Paths.get(fontLocation + "CustomFont.json"), new LinkOption[0])) {
         InputStream inputStream = Files.newInputStream(Paths.get(fontLocation + "CustomFont.json"));
         JsonObject mainObject = (new JsonParser()).parse(new InputStreamReader(inputStream)).getAsJsonObject();
         if (mainObject.get("Font Name") != null && mainObject.get("Font Size") != null) {
            JsonElement fontNameObject = mainObject.get("Font Name");
            String name = null;
            if (fontNameObject != null && fontNameObject.isJsonPrimitive()) {
               name = fontNameObject.getAsString();
            }

            JsonElement fontSizeObject = mainObject.get("Font Size");
            int size = -1;
            if (fontSizeObject != null && fontSizeObject.isJsonPrimitive()) {
               size = fontSizeObject.getAsInt();
            }

            if (name != null && size != -1) {
               PandoraMod.fontRenderer = new CFontRenderer(new Font(name, 0, size), true, true);
               PandoraMod.fontRenderer.setFont(new Font(name, 0, size));
               PandoraMod.fontRenderer.setAntiAlias(true);
               PandoraMod.fontRenderer.setFractionalMetrics(true);
               PandoraMod.fontRenderer.setFontName(name);
               PandoraMod.fontRenderer.setFontSize(size);
            }

            inputStream.close();
         }
      }
   }

   public void loadFriendsList() throws IOException {
      String friendLocation = this.fileName + this.miscName;
      if (Files.exists(Paths.get(friendLocation + "Friends.json"), new LinkOption[0])) {
         InputStream inputStream = Files.newInputStream(Paths.get(friendLocation + "Friends.json"));
         JsonObject mainObject = (new JsonParser()).parse(new InputStreamReader(inputStream)).getAsJsonObject();
         if (mainObject.get("Friends") != null) {
            JsonArray friendObject = mainObject.get("Friends").getAsJsonArray();
            friendObject.forEach((object) -> {
               Friends.addFriend(object.getAsString());
            });
            inputStream.close();
         }
      }
   }

   public void loadEnemiesList() throws IOException {
      String enemyLocation = this.fileName + this.miscName;
      if (Files.exists(Paths.get(enemyLocation + "Enemies.json"), new LinkOption[0])) {
         InputStream inputStream = Files.newInputStream(Paths.get(enemyLocation + "Enemies.json"));
         JsonObject mainObject = (new JsonParser()).parse(new InputStreamReader(inputStream)).getAsJsonObject();
         if (mainObject.get("Enemies") != null) {
            JsonArray enemyObject = mainObject.get("Enemies").getAsJsonArray();
            enemyObject.forEach((object) -> {
               Enemies.addEnemy(object.getAsString());
            });
            inputStream.close();
         }
      }
   }

   public void loadClickGUIPositions() throws IOException {
      String fileLocation = this.fileName + this.mainName;
      if (Files.exists(Paths.get(fileLocation + "ClickGUI.json"), new LinkOption[0])) {
         InputStream inputStream = Files.newInputStream(Paths.get(fileLocation + "ClickGUI.json"));
         JsonObject mainObject = (new JsonParser()).parse(new InputStreamReader(inputStream)).getAsJsonObject();
         if (mainObject.get("Panels") != null) {
            JsonObject panelObject = mainObject.get("Panels").getAsJsonObject();
            Iterator var5 = PandoraMod.getInstance().clickGUI.gui.getComponents().iterator();

            while(var5.hasNext()) {
               FixedComponent frames = (FixedComponent)var5.next();
               if (panelObject.get(frames.getTitle()) != null) {
                  JsonObject panelObject2 = panelObject.get(frames.getTitle()).getAsJsonObject();
                  Point point = new Point();
                  JsonElement panelPosXObject = panelObject2.get("PosX");
                  if (panelPosXObject != null && panelPosXObject.isJsonPrimitive()) {
                     point.x = panelPosXObject.getAsInt();
                  }

                  JsonElement panelPosYObject = panelObject2.get("PosY");
                  if (panelPosYObject != null && panelPosYObject.isJsonPrimitive()) {
                     point.y = panelPosYObject.getAsInt();
                  }

                  if (frames instanceof PositionConfig) {
                     ((PositionConfig)frames).setConfigPos(point);
                  } else {
                     frames.setPosition(PandoraMod.getInstance().clickGUI, point);
                  }

                  JsonElement panelOpenObject = panelObject2.get("State");
                  if (panelOpenObject != null && panelOpenObject.isJsonPrimitive() && frames instanceof DraggableContainer && panelOpenObject.getAsBoolean() && !((DraggableContainer)frames).isOn()) {
                     ((DraggableContainer)frames).toggle();
                  }
               }
            }

            inputStream.close();
         }
      }
   }

   public void loadAutoGG() throws IOException {
      String fileLocation = this.fileName + this.miscName;
      if (Files.exists(Paths.get(fileLocation + "AutoGG.json"), new LinkOption[0])) {
         InputStream inputStream = Files.newInputStream(Paths.get(fileLocation + "AutoGG.json"));
         JsonObject mainObject = (new JsonParser()).parse(new InputStreamReader(inputStream)).getAsJsonObject();
         if (mainObject.get("Messages") != null) {
            JsonArray messageObject = mainObject.get("Messages").getAsJsonArray();
            messageObject.forEach((object) -> {
               AutoGG.addAutoGgMessage(object.getAsString());
            });
            inputStream.close();
         }
      }
   }

   public void loadAutoReply() throws IOException {
      String fileLocation = this.fileName + this.miscName;
      if (Files.exists(Paths.get(fileLocation + "AutoReply.json"), new LinkOption[0])) {
         InputStream inputStream = Files.newInputStream(Paths.get(fileLocation + "AutoReply.json"));
         JsonObject mainObject = (new JsonParser()).parse(new InputStreamReader(inputStream)).getAsJsonObject();
         if (mainObject.get("AutoReply") != null) {
            JsonObject arObject = mainObject.get("AutoReply").getAsJsonObject();
            JsonElement dataObject = arObject.get("Message");
            if (dataObject != null && dataObject.isJsonPrimitive()) {
               AutoReply.setReply(dataObject.getAsString());
            }

            inputStream.close();
         }
      }
   }
}
