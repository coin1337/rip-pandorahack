package com.pandora.client.command.commands;

import com.pandora.api.util.misc.MessageBus;
import com.pandora.client.command.Command;

public class PrefixCommand extends Command {
   public PrefixCommand() {
      super("Prefix");
      this.setCommandSyntax(Command.getCommandPrefix() + "prefix value (no letters or numbers)");
      this.setCommandAlias(new String[]{"prefix", "setprefix", "cmdprefix", "commandprefix"});
   }

   public void onCommand(String command, String[] message) throws Exception {
      String main = message[0].toUpperCase().replaceAll("[a-zA-Z0-9]", (String)null);
      int size = message[0].length();
      if (main != null && size == 1) {
         Command.setCommandPrefix(main);
         MessageBus.sendClientPrefixMessage("Prefix set: \"" + main + "\"!");
      } else if (size > 1 || size < 1) {
         MessageBus.sendClientPrefixMessage(this.getCommandSyntax());
      }

   }
}
