package com.pandora.client.command.commands;

import com.pandora.api.util.misc.MessageBus;
import com.pandora.client.command.Command;
import com.pandora.client.module.modules.misc.AutoGG;

public class AutoGGCommand extends Command {
   public AutoGGCommand() {
      super("AutoGG");
      this.setCommandSyntax(Command.getCommandPrefix() + "autogg add/del [message] (use _ for spaces)");
      this.setCommandAlias(new String[]{"autogg", "gg"});
   }

   public void onCommand(String command, String[] message) throws Exception {
      String main = message[0];
      String value = message[1].replace("_", " ");
      if (main.equalsIgnoreCase("add") && !AutoGG.getAutoGgMessages().contains(value)) {
         AutoGG.addAutoGgMessage(value);
         MessageBus.sendClientPrefixMessage("Added AutoGG message: " + value + "!");
      } else if (main.equalsIgnoreCase("del") && AutoGG.getAutoGgMessages().contains(value)) {
         AutoGG.getAutoGgMessages().remove(value);
         MessageBus.sendClientPrefixMessage("Deleted AutoGG message: " + value + "!");
      }

   }
}
