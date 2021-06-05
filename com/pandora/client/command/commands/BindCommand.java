package com.pandora.client.command.commands;

import com.pandora.api.util.misc.MessageBus;
import com.pandora.client.command.Command;
import com.pandora.client.module.Module;
import com.pandora.client.module.ModuleManager;
import java.util.Iterator;
import org.lwjgl.input.Keyboard;

public class BindCommand extends Command {
   public BindCommand() {
      super("Bind");
      this.setCommandSyntax(Command.getCommandPrefix() + "bind [module] key");
      this.setCommandAlias(new String[]{"bind", "b", "setbind", "key"});
   }

   public void onCommand(String command, String[] message) throws Exception {
      String main = message[0];
      String value = message[1].toUpperCase();
      Iterator var5 = ModuleManager.getModules().iterator();

      while(var5.hasNext()) {
         Module module = (Module)var5.next();
         if (module.getName().equalsIgnoreCase(main)) {
            if (value.equalsIgnoreCase("none")) {
               module.setBind(0);
               MessageBus.sendClientPrefixMessage("Module " + module.getName() + " bind set to: " + value + "!");
            } else if (value.length() == 1) {
               int key = Keyboard.getKeyIndex(value);
               module.setBind(key);
               MessageBus.sendClientPrefixMessage("Module " + module.getName() + " bind set to: " + value + "!");
            } else if (value.length() > 1) {
               MessageBus.sendClientPrefixMessage(this.getCommandSyntax());
            }
         }
      }

   }
}
