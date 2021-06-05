package com.pandora.api.util.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.pandora.client.module.ModuleManager;
import com.pandora.client.module.modules.hud.Notifications;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.util.text.TextComponentString;

public class MessageBus {
   public static String watermark;
   public static ChatFormatting messageFormatting;
   protected static final Minecraft mc;

   public static void sendClientPrefixMessage(String message) {
      TextComponentString string1 = new TextComponentString(watermark + messageFormatting + message);
      TextComponentString string2 = new TextComponentString(messageFormatting + message);
      Notifications.addMessage(string2);
      if (!ModuleManager.isModuleEnabled("Notifications") || !Notifications.disableChat.getValue()) {
         mc.field_71439_g.func_145747_a(string1);
      }
   }

   public static void sendClientRawMessage(String message) {
      TextComponentString string = new TextComponentString(messageFormatting + message);
      Notifications.addMessage(string);
      if (!ModuleManager.isModuleEnabled("Notifications") || !Notifications.disableChat.getValue()) {
         mc.field_71439_g.func_145747_a(string);
      }
   }

   public static void sendServerMessage(String message) {
      mc.field_71439_g.field_71174_a.func_147297_a(new CPacketChatMessage(message));
   }

   static {
      watermark = ChatFormatting.WHITE + "[" + ChatFormatting.BLACK + "Pandora" + ChatFormatting.DARK_PURPLE + "Hack+" + ChatFormatting.WHITE + "] " + ChatFormatting.RESET;
      messageFormatting = ChatFormatting.GRAY;
      mc = Minecraft.func_71410_x();
   }
}
