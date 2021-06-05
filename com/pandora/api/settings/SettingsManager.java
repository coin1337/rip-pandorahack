package com.pandora.api.settings;

import com.pandora.client.module.Module;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class SettingsManager {
   private final List<Setting> settings = new ArrayList();

   public List<Setting> getSettings() {
      return this.settings;
   }

   public void addSetting(Setting setting) {
      this.settings.add(setting);
   }

   public Setting getSettingByNameAndMod(String name, Module parent) {
      return (Setting)this.settings.stream().filter((s) -> {
         return s.getParent().equals(parent);
      }).filter((s) -> {
         return s.getConfigName().equalsIgnoreCase(name);
      }).findFirst().orElse((Object)null);
   }

   public Setting getSettingByNameAndModConfig(String configname, Module parent) {
      return (Setting)this.settings.stream().filter((s) -> {
         return s.getParent().equals(parent);
      }).filter((s) -> {
         return s.getConfigName().equalsIgnoreCase(configname);
      }).findFirst().orElse((Object)null);
   }

   public List<Setting> getSettingsForMod(Module parent) {
      return (List)this.settings.stream().filter((s) -> {
         return s.getParent().equals(parent);
      }).collect(Collectors.toList());
   }

   public List<Setting> getSettingsByCategory(Module.Category category) {
      return (List)this.settings.stream().filter((s) -> {
         return s.getCategory().equals(category);
      }).collect(Collectors.toList());
   }

   public Setting getSettingByName(String name) {
      Iterator var2 = this.getSettings().iterator();

      Setting set;
      do {
         if (!var2.hasNext()) {
            System.err.println("[Pandora] Error Setting NOT found: '" + name + "'!");
            return null;
         }

         set = (Setting)var2.next();
      } while(!set.getName().equalsIgnoreCase(name));

      return set;
   }
}
