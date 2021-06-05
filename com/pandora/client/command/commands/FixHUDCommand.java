package com.pandora.client.command.commands;

import com.pandora.api.util.misc.MessageBus;
import com.pandora.client.command.Command;
import com.pandora.client.module.Module;
import com.pandora.client.module.ModuleManager;
import com.pandora.client.module.modules.hud.HUDModule;
import java.util.Iterator;

public class FixHUDCommand extends Command {
   public FixHUDCommand() {
      super("FixHUD");
      this.setCommandSyntax(Command.getCommandPrefix() + "fixhud");
      this.setCommandAlias(new String[]{"fixhud", "hud", "resethud"});
   }

   public void onCommand(String command, String[] message) throws Exception {
      Iterator var3 = ModuleManager.getModules().iterator();

      while(var3.hasNext()) {
         Module module = (Module)var3.next();
         if (module instanceof HUDModule) {
            ((HUDModule)module).resetPosition();
         }
      }

      MessageBus.sendClientPrefixMessage("HUD positions reset!");
   }
}
