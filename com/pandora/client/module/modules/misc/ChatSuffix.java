package com.pandora.client.module.modules.misc;

import com.pandora.api.event.events.PacketEvent;
import com.pandora.api.settings.Setting;
import com.pandora.client.PandoraMod;
import com.pandora.client.command.Command;
import com.pandora.client.module.Module;
import java.util.ArrayList;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.CPacketChatMessage;

public class ChatSuffix extends Module {
   Setting.Mode Separator;
   @EventHandler
   private final Listener<PacketEvent.Send> listener = new Listener((event) -> {
      if (event.getPacket() instanceof CPacketChatMessage) {
         if (((CPacketChatMessage)event.getPacket()).func_149439_c().startsWith("/") || ((CPacketChatMessage)event.getPacket()).func_149439_c().startsWith(Command.getCommandPrefix())) {
            return;
         }

         String Separator2 = null;
         if (this.Separator.getValue().equalsIgnoreCase(">>")) {
            Separator2 = " 》";
         }

         if (this.Separator.getValue().equalsIgnoreCase("<<")) {
            Separator2 = " 《";
         } else if (this.Separator.getValue().equalsIgnoreCase("|")) {
            Separator2 = " ⏐ ";
         }

         String old = ((CPacketChatMessage)event.getPacket()).func_149439_c();
         String suffix = Separator2 + this.toUnicode(PandoraMod.MODNAME);
         String s = old + suffix;
         if (s.length() > 255) {
            return;
         }

         ((CPacketChatMessage)event.getPacket()).field_149440_a = s;
      }

   }, new Predicate[0]);

   public ChatSuffix() {
      super("ChatSuffix", Module.Category.Misc);
   }

   public void setup() {
      ArrayList<String> Separators = new ArrayList();
      Separators.add(">>");
      Separators.add("<<");
      Separators.add("|");
      this.Separator = this.registerMode("Separator", "Separator", Separators, "|");
   }

   public void onEnable() {
      PandoraMod.EVENT_BUS.subscribe((Object)this);
   }

   public void onDisable() {
      PandoraMod.EVENT_BUS.unsubscribe((Object)this);
   }

   public String toUnicode(String s) {
      return s.toLowerCase().replace("a", "ᴀ").replace("b", "ʙ").replace("c", "ᴄ").replace("d", "ᴅ").replace("e", "ᴇ").replace("f", "ꜰ").replace("g", "ɢ").replace("h", "ʜ").replace("i", "ɪ").replace("j", "ᴊ").replace("k", "ᴋ").replace("l", "ʟ").replace("m", "ᴍ").replace("n", "ɴ").replace("o", "ᴏ").replace("p", "ᴘ").replace("q", "ǫ").replace("r", "ʀ").replace("s", "ꜱ").replace("t", "ᴛ").replace("u", "ᴜ").replace("v", "ᴠ").replace("w", "ᴡ").replace("x", "ˣ").replace("y", "ʏ").replace("z", "ᴢ");
   }
}
