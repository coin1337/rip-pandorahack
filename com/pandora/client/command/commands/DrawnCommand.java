package com.pandora.client.command.commands;

import com.pandora.api.util.misc.MessageBus;
import com.pandora.client.command.Command;
import com.pandora.client.module.Module;
import com.pandora.client.module.ModuleManager;
import java.util.Iterator;

public class DrawnCommand extends Command {
   public DrawnCommand() {
      super("Drawn");
      this.setCommandSyntax(Command.getCommandPrefix() + "drawn [module]");
      this.setCommandAlias(new String[]{"drawn", "shown"});
   }

   public void onCommand(String command, String[] message) throws Exception {
      String main = message[0];
      Iterator var4 = ModuleManager.getModules().iterator();

      while(var4.hasNext()) {
         Module module = (Module)var4.next();
         if (module.getName().equalsIgnoreCase(main)) {
            if (module.isDrawn()) {
               module.setDrawn(false);
               MessageBus.sendClientPrefixMessage("Module " + module.getName() + "drawn set to: FALSE!");
            } else if (!module.isDrawn()) {
               module.setDrawn(true);
               MessageBus.sendClientPrefixMessage("Module " + module.getName() + "drawn set to: TRUE!");
            }
         } else {
            MessageBus.sendClientPrefixMessage(this.getCommandSyntax());
         }
      }

   }
}
