package com.pandora.client.command.commands;

import com.pandora.api.util.font.CFontRenderer;
import com.pandora.api.util.misc.MessageBus;
import com.pandora.client.PandoraMod;
import com.pandora.client.command.Command;
import java.awt.Font;

public class FontCommand extends Command {
   public FontCommand() {
      super("Font");
      this.setCommandSyntax(Command.getCommandPrefix() + "font [name] size (use _ for spaces)");
      this.setCommandAlias(new String[]{"font", "setfont", "customfont", "fonts", "chatfont"});
   }

   public void onCommand(String command, String[] message) {
      String main = message[0].replace("_", " ");
      int value = Integer.parseInt(message[1]);
      if (value >= 21 || value <= 15) {
         value = 18;
      }

      PandoraMod.fontRenderer = new CFontRenderer(new Font(main, 0, value), true, true);
      PandoraMod.fontRenderer.setFontName(main);
      PandoraMod.fontRenderer.setFontSize(value);
      MessageBus.sendClientPrefixMessage("Font set to: " + main.toUpperCase() + ", size " + value + "!");
   }
}
