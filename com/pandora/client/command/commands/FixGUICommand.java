package com.pandora.client.command.commands;

import com.pandora.api.util.misc.MessageBus;
import com.pandora.client.PandoraMod;
import com.pandora.client.clickgui.PandoraGUI;
import com.pandora.client.command.Command;

public class FixGUICommand extends Command {
   public FixGUICommand() {
      super("FixGUI");
      this.setCommandSyntax(Command.getCommandPrefix() + "fixgui");
      this.setCommandAlias(new String[]{"fixgui", "gui", "resetgui"});
   }

   public void onCommand(String command, String[] message) throws Exception {
      PandoraMod.getInstance().clickGUI = new PandoraGUI();
      MessageBus.sendClientPrefixMessage("ClickGUI positions reset!");
   }
}
