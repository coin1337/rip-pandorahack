package com.pandora.client.command.commands;

import com.pandora.api.util.misc.MessageBus;
import com.pandora.client.command.Command;
import com.pandora.client.command.CommandManager;
import java.util.Iterator;

public class CmdListCommand extends Command {
   public CmdListCommand() {
      super("Commands");
      this.setCommandSyntax(Command.getCommandPrefix() + "commands");
      this.setCommandAlias(new String[]{"commands", "cmd", "command", "commandlist"});
   }

   public void onCommand(String command, String[] message) throws Exception {
      Iterator var3 = CommandManager.getCommands().iterator();

      while(var3.hasNext()) {
         Command command1 = (Command)var3.next();
         MessageBus.sendClientPrefixMessage(command1.getCommandName() + ": \"" + command1.getCommandSyntax() + "\"!");
      }

   }
}
