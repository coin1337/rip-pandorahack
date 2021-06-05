package com.pandora.client.command.commands;

import com.pandora.api.util.misc.MessageBus;
import com.pandora.client.command.Command;
import java.awt.Desktop;
import java.io.File;

public class OpenFolderCommand extends Command {
   public OpenFolderCommand() {
      super("OpenFolder");
      this.setCommandSyntax(Command.getCommandPrefix() + "openfolder");
      this.setCommandAlias(new String[]{"openfolder", "config", "open", "folder"});
   }

   public void onCommand(String command, String[] message) throws Exception {
      Desktop.getDesktop().open(new File("Pandora/".replace("/", "")));
      MessageBus.sendClientPrefixMessage("Opened config folder!");
   }
}
