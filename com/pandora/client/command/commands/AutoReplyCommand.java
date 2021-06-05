package com.pandora.client.command.commands;

import com.pandora.api.util.misc.MessageBus;
import com.pandora.client.command.Command;
import com.pandora.client.module.modules.misc.AutoReply;

public class AutoReplyCommand extends Command {
   public AutoReplyCommand() {
      super("AutoReply");
      this.setCommandSyntax(Command.getCommandPrefix() + "autoreply set [message] (use _ for spaces)");
      this.setCommandAlias(new String[]{"autoreply", "reply"});
   }

   public void onCommand(String command, String[] message) throws Exception {
      String main = message[0];
      String value = message[1].replace("_", " ");
      if (main.equalsIgnoreCase("set")) {
         AutoReply.setReply(value);
         MessageBus.sendClientPrefixMessage("Set AutoReply message: " + value + "!");
      }

   }
}
