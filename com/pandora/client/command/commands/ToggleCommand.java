package com.pandora.client.command.commands;

import com.pandora.api.util.misc.MessageBus;
import com.pandora.client.command.Command;
import com.pandora.client.module.Module;
import com.pandora.client.module.ModuleManager;
import java.util.Iterator;

public class ToggleCommand extends Command {
   public ToggleCommand() {
      super("Toggle");
      this.setCommandSyntax(Command.getCommandPrefix() + "toggle [module]");
      this.setCommandAlias(new String[]{"toggle", "t", "enable", "disable"});
   }

   public void onCommand(String command, String[] message) throws Exception {
      String main = message[0];
      Iterator var4 = ModuleManager.getModules().iterator();

      while(true) {
         while(var4.hasNext()) {
            Module module = (Module)var4.next();
            if (module.getName().equalsIgnoreCase(main) && !module.isEnabled()) {
               module.enable();
               MessageBus.sendClientPrefixMessage("Module " + module.getName() + " set to: ENABLED!");
            } else if (module.getName().equalsIgnoreCase(main) && module.isEnabled()) {
               module.disable();
               MessageBus.sendClientPrefixMessage("Module " + module.getName() + " set to: DISABLED!");
            }
         }

         if (main == null || ModuleManager.getModuleByName(main) == null) {
            MessageBus.sendClientPrefixMessage(this.getCommandSyntax());
         }

         return;
      }
   }
}
