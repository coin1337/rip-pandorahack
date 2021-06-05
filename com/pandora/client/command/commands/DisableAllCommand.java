package com.pandora.client.command.commands;

import com.pandora.api.util.misc.MessageBus;
import com.pandora.client.command.Command;
import com.pandora.client.module.Module;
import com.pandora.client.module.ModuleManager;
import java.util.Iterator;

public class DisableAllCommand extends Command {
   public DisableAllCommand() {
      super("DisableAll");
      this.setCommandSyntax(Command.getCommandPrefix() + "disableall");
      this.setCommandAlias(new String[]{"disableall", "stop"});
   }

   public void onCommand(String command, String[] message) throws Exception {
      int count = 0;
      Iterator var4 = ModuleManager.getModules().iterator();

      while(var4.hasNext()) {
         Module module = (Module)var4.next();
         if (module.isEnabled()) {
            module.disable();
            ++count;
         }
      }

      MessageBus.sendClientPrefixMessage("Disabled " + count + " modules!");
   }
}
