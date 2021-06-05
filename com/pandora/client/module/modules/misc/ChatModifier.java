package com.pandora.client.module.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.pandora.api.event.events.PacketEvent;
import com.pandora.api.settings.Setting;
import com.pandora.client.PandoraMod;
import com.pandora.client.command.Command;
import com.pandora.client.module.Module;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class ChatModifier extends Module {
   public Setting.Boolean clearBkg;
   Setting.Boolean chatTimeStamps;
   Setting.Mode format;
   Setting.Mode color;
   Setting.Mode decoration;
   Setting.Boolean space;
   Setting.Boolean greenText;
   @EventHandler
   private final Listener<ClientChatReceivedEvent> chatReceivedEventListener = new Listener((event) -> {
      if (this.chatTimeStamps.getValue()) {
         String decoLeft = this.decoration.getValue().equalsIgnoreCase(" ") ? "" : this.decoration.getValue().split(" ")[0];
         String decoRight = this.decoration.getValue().equalsIgnoreCase(" ") ? "" : this.decoration.getValue().split(" ")[1];
         if (this.space.getValue()) {
            decoRight = decoRight + " ";
         }

         String dateFormat = this.format.getValue().replace("H24", "k").replace("H12", "h");
         String date = (new SimpleDateFormat(dateFormat)).format(new Date());
         TextComponentString time = new TextComponentString(ChatFormatting.getByName(this.color.getValue()) + decoLeft + date + decoRight + ChatFormatting.RESET);
         event.setMessage(time.func_150257_a(event.getMessage()));
      }

   }, new Predicate[0]);
   @EventHandler
   private final Listener<PacketEvent.Send> listener = new Listener((event) -> {
      if (this.greenText.getValue() && event.getPacket() instanceof CPacketChatMessage) {
         if (((CPacketChatMessage)event.getPacket()).func_149439_c().startsWith("/") || ((CPacketChatMessage)event.getPacket()).func_149439_c().startsWith(Command.getCommandPrefix())) {
            return;
         }

         String message = ((CPacketChatMessage)event.getPacket()).func_149439_c();
         String prefix = "";
         prefix = ">";
         String s = prefix + message;
         if (s.length() > 255) {
            return;
         }

         ((CPacketChatMessage)event.getPacket()).field_149440_a = s;
      }

   }, new Predicate[0]);

   public ChatModifier() {
      super("ChatModifier", Module.Category.Misc);
   }

   public void setup() {
      ArrayList<String> formats = new ArrayList();
      formats.add("H24:mm");
      formats.add("H12:mm");
      formats.add("H12:mm a");
      formats.add("H24:mm:ss");
      formats.add("H12:mm:ss");
      formats.add("H12:mm:ss a");
      ArrayList<String> deco = new ArrayList();
      deco.add("< >");
      deco.add("[ ]");
      deco.add("{ }");
      deco.add(" ");
      ArrayList<String> colors = new ArrayList();
      ChatFormatting[] var4 = ChatFormatting.values();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         ChatFormatting cf = var4[var6];
         colors.add(cf.getName());
      }

      this.clearBkg = this.registerBoolean("Clear Chat", "ClearChat", false);
      this.greenText = this.registerBoolean("Green Text", "GreenText", false);
      this.chatTimeStamps = this.registerBoolean("Chat Time Stamps", "ChatTimeStamps", false);
      this.format = this.registerMode("Format", "Format", formats, "H24:mm");
      this.decoration = this.registerMode("Deco", "Deco", deco, "[ ]");
      this.color = this.registerMode("Color", "Colors", colors, ChatFormatting.GRAY.getName());
      this.space = this.registerBoolean("Space", "Space", false);
   }

   public void onEnable() {
      PandoraMod.EVENT_BUS.subscribe((Object)this);
   }

   public void onDisable() {
      PandoraMod.EVENT_BUS.unsubscribe((Object)this);
   }
}
