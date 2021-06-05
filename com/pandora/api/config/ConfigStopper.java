package com.pandora.api.config;

import com.pandora.client.PandoraMod;
import java.io.IOException;

public class ConfigStopper extends Thread {
   public void run() {
      saveConfig();
   }

   public static void saveConfig() {
      try {
         PandoraMod.getInstance().saveConfig.saveConfig();
         PandoraMod.getInstance().saveConfig.saveModules();
         PandoraMod.getInstance().saveConfig.saveEnabledModules();
         PandoraMod.getInstance().saveConfig.saveModuleKeybinds();
         PandoraMod.getInstance().saveConfig.saveDrawnModules();
         PandoraMod.getInstance().saveConfig.saveCommandPrefix();
         PandoraMod.getInstance().saveConfig.saveCustomFont();
         PandoraMod.getInstance().saveConfig.saveFriendsList();
         PandoraMod.getInstance().saveConfig.saveEnemiesList();
         PandoraMod.getInstance().saveConfig.saveClickGUIPositions();
         PandoraMod.getInstance().saveConfig.saveAutoGG();
         PandoraMod.getInstance().saveConfig.saveAutoReply();
         PandoraMod.log.info("Saved Config!");
      } catch (IOException var1) {
         var1.printStackTrace();
      }

   }
}
