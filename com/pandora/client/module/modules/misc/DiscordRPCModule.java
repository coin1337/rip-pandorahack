package com.pandora.client.module.modules.misc;

import com.pandora.api.util.misc.Discord;
import com.pandora.api.util.misc.MessageBus;
import com.pandora.client.module.Module;

public class DiscordRPCModule extends Module {
   public DiscordRPCModule() {
      super("DiscordRPC", Module.Category.Misc);
      this.setDrawn(false);
   }

   public void onEnable() {
      Discord.startRPC();
      if (mc.field_71439_g != null || mc.field_71441_e != null) {
         MessageBus.sendClientPrefixMessage("Discord RPC started!");
      }

   }

   public void onDisable() {
      Discord.stopRPC();
      if (mc.field_71439_g != null || mc.field_71441_e != null) {
         MessageBus.sendClientPrefixMessage("Discord RPC stopped!");
      }

   }
}
