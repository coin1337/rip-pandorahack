package com.pandora.api.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.lukflug.panelstudio.DraggableContainer;
import com.lukflug.panelstudio.FixedComponent;
import com.pandora.api.settings.Setting;
import com.pandora.api.util.players.enemy.Enemies;
import com.pandora.api.util.players.enemy.Enemy;
import com.pandora.api.util.players.friends.Friend;
import com.pandora.api.util.players.friends.Friends;
import com.pandora.client.PandoraMod;
import com.pandora.client.clickgui.PandoraGUI;
import com.pandora.client.command.Command;
import com.pandora.client.module.Module;
import com.pandora.client.module.ModuleManager;
import com.pandora.client.module.modules.misc.AutoGG;
import com.pandora.client.module.modules.misc.AutoReply;
import java.awt.Point;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.Iterator;

public class SaveConfig {
   public static final String fileName = "Pandora/";
   String moduleName = "Modules/";
   String mainName = "Main/";
   String miscName = "Misc/";

   public SaveConfig() {
      try {
         this.saveConfig();
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }

   public void saveConfig() throws IOException {
      if (!Files.exists(Paths.get("Pandora/"), new LinkOption[0])) {
         Files.createDirectories(Paths.get("Pandora/"));
      }

      if (!Files.exists(Paths.get("Pandora/" + this.moduleName), new LinkOption[0])) {
         Files.createDirectories(Paths.get("Pandora/" + this.moduleName));
      }

      if (!Files.exists(Paths.get("Pandora/" + this.mainName), new LinkOption[0])) {
         Files.createDirectories(Paths.get("Pandora/" + this.mainName));
      }

      if (!Files.exists(Paths.get("Pandora/" + this.miscName), new LinkOption[0])) {
         Files.createDirectories(Paths.get("Pandora/" + this.miscName));
      }

   }

   public void registerFiles(String location, String name) throws IOException {
      if (!Files.exists(Paths.get("Pandora/" + location + name + ".json"), new LinkOption[0])) {
         Files.createFile(Paths.get("Pandora/" + location + name + ".json"));
      } else {
         File file = new File("Pandora/" + location + name + ".json");
         file.delete();
         Files.createFile(Paths.get("Pandora/" + location + name + ".json"));
      }

   }

   public void saveModules() {
      Iterator var1 = ModuleManager.getModules().iterator();

      while(var1.hasNext()) {
         Module module = (Module)var1.next();

         try {
            this.saveModuleDirect(module);
         } catch (IOException var4) {
            var4.printStackTrace();
         }
      }

   }

   public void saveModuleDirect(Module module) throws IOException {
      this.registerFiles(this.moduleName, module.getName());
      Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
      OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream("Pandora/" + this.moduleName + module.getName() + ".json"), StandardCharsets.UTF_8);
      JsonObject moduleObject = new JsonObject();
      JsonObject settingObject = new JsonObject();
      moduleObject.add("Module", new JsonPrimitive(module.getName()));
      Iterator var6 = PandoraMod.getInstance().settingsManager.getSettingsForMod(module).iterator();

      while(var6.hasNext()) {
         Setting setting = (Setting)var6.next();
         switch(setting.getType()) {
         case BOOLEAN:
            settingObject.add(setting.getConfigName(), new JsonPrimitive(((Setting.Boolean)setting).getValue()));
            break;
         case INTEGER:
            settingObject.add(setting.getConfigName(), new JsonPrimitive(((Setting.Integer)setting).getValue()));
            break;
         case DOUBLE:
            settingObject.add(setting.getConfigName(), new JsonPrimitive(((Setting.Double)setting).getValue()));
            break;
         case COLOR:
            settingObject.add(setting.getConfigName(), new JsonPrimitive(((Setting.ColorSetting)setting).toInteger()));
            break;
         case MODE:
            settingObject.add(setting.getConfigName(), new JsonPrimitive(((Setting.Mode)setting).getValue()));
         }
      }

      moduleObject.add("Settings", settingObject);
      String jsonString = gson.toJson((new JsonParser()).parse(moduleObject.toString()));
      fileOutputStreamWriter.write(jsonString);
      fileOutputStreamWriter.close();
   }

   public void saveEnabledModules() throws IOException {
      this.registerFiles(this.mainName, "Toggle");
      Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
      OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream("Pandora/" + this.mainName + "Toggle.json"), StandardCharsets.UTF_8);
      JsonObject moduleObject = new JsonObject();
      JsonObject enabledObject = new JsonObject();
      Iterator var5 = ModuleManager.getModules().iterator();

      while(var5.hasNext()) {
         Module module = (Module)var5.next();
         enabledObject.add(module.getName(), new JsonPrimitive(module.isEnabled()));
      }

      moduleObject.add("Modules", enabledObject);
      String jsonString = gson.toJson((new JsonParser()).parse(moduleObject.toString()));
      fileOutputStreamWriter.write(jsonString);
      fileOutputStreamWriter.close();
   }

   public void saveModuleKeybinds() throws IOException {
      this.registerFiles(this.mainName, "Bind");
      Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
      OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream("Pandora/" + this.mainName + "Bind.json"), StandardCharsets.UTF_8);
      JsonObject moduleObject = new JsonObject();
      JsonObject bindObject = new JsonObject();
      Iterator var5 = ModuleManager.getModules().iterator();

      while(var5.hasNext()) {
         Module module = (Module)var5.next();
         bindObject.add(module.getName(), new JsonPrimitive(module.getBind()));
      }

      moduleObject.add("Modules", bindObject);
      String jsonString = gson.toJson((new JsonParser()).parse(moduleObject.toString()));
      fileOutputStreamWriter.write(jsonString);
      fileOutputStreamWriter.close();
   }

   public void saveDrawnModules() throws IOException {
      this.registerFiles(this.mainName, "Drawn");
      Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
      OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream("Pandora/" + this.mainName + "Drawn.json"), StandardCharsets.UTF_8);
      JsonObject moduleObject = new JsonObject();
      JsonObject drawnObject = new JsonObject();
      Iterator var5 = ModuleManager.getModules().iterator();

      while(var5.hasNext()) {
         Module module = (Module)var5.next();
         drawnObject.add(module.getName(), new JsonPrimitive(module.isDrawn()));
      }

      moduleObject.add("Modules", drawnObject);
      String jsonString = gson.toJson((new JsonParser()).parse(moduleObject.toString()));
      fileOutputStreamWriter.write(jsonString);
      fileOutputStreamWriter.close();
   }

   public void saveCommandPrefix() throws IOException {
      this.registerFiles(this.mainName, "CommandPrefix");
      Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
      OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream("Pandora/" + this.mainName + "CommandPrefix.json"), StandardCharsets.UTF_8);
      JsonObject prefixObject = new JsonObject();
      prefixObject.add("Prefix", new JsonPrimitive(Command.getCommandPrefix()));
      String jsonString = gson.toJson((new JsonParser()).parse(prefixObject.toString()));
      fileOutputStreamWriter.write(jsonString);
      fileOutputStreamWriter.close();
   }

   public void saveCustomFont() throws IOException {
      this.registerFiles(this.miscName, "CustomFont");
      Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
      OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream("Pandora/" + this.miscName + "CustomFont.json"), StandardCharsets.UTF_8);
      JsonObject fontObject = new JsonObject();
      fontObject.add("Font Name", new JsonPrimitive(PandoraMod.fontRenderer.getFontName()));
      fontObject.add("Font Size", new JsonPrimitive(PandoraMod.fontRenderer.getFontSize()));
      String jsonString = gson.toJson((new JsonParser()).parse(fontObject.toString()));
      fileOutputStreamWriter.write(jsonString);
      fileOutputStreamWriter.close();
   }

   public void saveFriendsList() throws IOException {
      this.registerFiles(this.miscName, "Friends");
      Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
      OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream("Pandora/" + this.miscName + "Friends.json"), StandardCharsets.UTF_8);
      JsonObject mainObject = new JsonObject();
      JsonArray friendArray = new JsonArray();
      Iterator var5 = Friends.getFriends().iterator();

      while(var5.hasNext()) {
         Friend friend = (Friend)var5.next();
         friendArray.add(friend.getName());
      }

      mainObject.add("Friends", friendArray);
      String jsonString = gson.toJson((new JsonParser()).parse(mainObject.toString()));
      fileOutputStreamWriter.write(jsonString);
      fileOutputStreamWriter.close();
   }

   public void saveEnemiesList() throws IOException {
      this.registerFiles(this.miscName, "Enemies");
      Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
      OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream("Pandora/" + this.miscName + "Enemies.json"), StandardCharsets.UTF_8);
      JsonObject mainObject = new JsonObject();
      JsonArray enemyArray = new JsonArray();
      Iterator var5 = Enemies.getEnemies().iterator();

      while(var5.hasNext()) {
         Enemy enemy = (Enemy)var5.next();
         enemyArray.add(enemy.getName());
      }

      mainObject.add("Enemies", enemyArray);
      String jsonString = gson.toJson((new JsonParser()).parse(mainObject.toString()));
      fileOutputStreamWriter.write(jsonString);
      fileOutputStreamWriter.close();
   }

   public void saveClickGUIPositions() throws IOException {
      this.registerFiles(this.mainName, "ClickGUI");
      Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
      OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream("Pandora/" + this.mainName + "ClickGUI.json"), StandardCharsets.UTF_8);
      JsonObject mainObject = new JsonObject();
      JsonObject panelObject = new JsonObject();
      PandoraGUI gui = PandoraMod.getInstance().clickGUI;

      FixedComponent frame;
      JsonObject valueObject;
      for(Iterator var6 = gui.gui.getComponents().iterator(); var6.hasNext(); panelObject.add(frame.getTitle(), valueObject)) {
         frame = (FixedComponent)var6.next();
         valueObject = new JsonObject();
         Point pos;
         if (frame instanceof PositionConfig) {
            pos = ((PositionConfig)frame).getConfigPos();
         } else {
            pos = frame.getPosition(gui);
         }

         valueObject.add("PosX", new JsonPrimitive(pos.x));
         valueObject.add("PosY", new JsonPrimitive(pos.y));
         if (frame instanceof DraggableContainer) {
            valueObject.add("State", new JsonPrimitive(((DraggableContainer)frame).isOn()));
         }
      }

      mainObject.add("Panels", panelObject);
      String jsonString = gson.toJson((new JsonParser()).parse(mainObject.toString()));
      fileOutputStreamWriter.write(jsonString);
      fileOutputStreamWriter.close();
   }

   public void saveAutoGG() throws IOException {
      this.registerFiles(this.miscName, "AutoGG");
      Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
      OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream("Pandora/" + this.miscName + "AutoGG.json"), StandardCharsets.UTF_8);
      JsonObject mainObject = new JsonObject();
      JsonArray messageArray = new JsonArray();
      Iterator var5 = AutoGG.getAutoGgMessages().iterator();

      while(var5.hasNext()) {
         String autoGG = (String)var5.next();
         messageArray.add(autoGG);
      }

      mainObject.add("Messages", messageArray);
      String jsonString = gson.toJson((new JsonParser()).parse(mainObject.toString()));
      fileOutputStreamWriter.write(jsonString);
      fileOutputStreamWriter.close();
   }

   public void saveAutoReply() throws IOException {
      this.registerFiles(this.miscName, "AutoReply");
      Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
      OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream("Pandora/" + this.miscName + "AutoReply.json"), StandardCharsets.UTF_8);
      JsonObject mainObject = new JsonObject();
      JsonObject messageObject = new JsonObject();
      messageObject.add("Message", new JsonPrimitive(AutoReply.getReply()));
      mainObject.add("AutoReply", messageObject);
      String jsonString = gson.toJson((new JsonParser()).parse(mainObject.toString()));
      fileOutputStreamWriter.write(jsonString);
      fileOutputStreamWriter.close();
   }
}
