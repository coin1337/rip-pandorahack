package com.pandora.api.util.misc;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

public class Discord {
   private static String ver = "v1.2";
   private static String discordID = "781850468129308674";
   private static DiscordRichPresence discordRichPresence = new DiscordRichPresence();
   private static DiscordRPC discordRPC;
   private static String clientVersion;

   public static void startRPC() {
      DiscordEventHandlers eventHandlers = new DiscordEventHandlers();
      eventHandlers.disconnected = (var1, var2) -> {
         System.out.println("Discord RPC disconnected, var1: " + var1 + ", var2: " + var2);
      };
      discordRPC.Discord_Initialize(discordID, eventHandlers, true, (String)null);
      discordRichPresence.startTimestamp = System.currentTimeMillis() / 1000L;
      discordRichPresence.details = clientVersion;
      discordRichPresence.largeImageKey = "pandora";
      discordRichPresence.largeImageText = "Pandora Hack+ " + ver;
      discordRichPresence.state = null;
      discordRPC.Discord_UpdatePresence(discordRichPresence);
   }

   public static void stopRPC() {
      discordRPC.Discord_Shutdown();
      discordRPC.Discord_ClearPresence();
   }

   static {
      discordRPC = DiscordRPC.INSTANCE;
      clientVersion = "v1.2";
   }
}
