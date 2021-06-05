package com.pandora.client.module.modules.misc;

import com.pandora.api.util.misc.MessageBus;
import com.pandora.client.PandoraMod;
import com.pandora.client.module.Module;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class AutoReply extends Module {
   private static String reply = "I don't speak to newfags!";
   @EventHandler
   private final Listener<ClientChatReceivedEvent> listener = new Listener((event) -> {
      if (event.getMessage().func_150260_c().contains("whispers: ") && !event.getMessage().func_150260_c().startsWith(mc.field_71439_g.func_70005_c_())) {
         MessageBus.sendServerMessage("/r " + reply);
      } else if (event.getMessage().func_150260_c().contains("whispers: I don't speak to newfags!") && !event.getMessage().func_150260_c().startsWith(mc.field_71439_g.func_70005_c_())) {
         return;
      }

   }, new Predicate[0]);

   public AutoReply() {
      super("AutoReply", Module.Category.Misc);
   }

   public static String getReply() {
      return reply;
   }

   public static void setReply(String r) {
      reply = r;
   }

   public void onEnable() {
      PandoraMod.EVENT_BUS.subscribe((Object)this);
   }

   public void onDisable() {
      PandoraMod.EVENT_BUS.unsubscribe((Object)this);
   }
}
