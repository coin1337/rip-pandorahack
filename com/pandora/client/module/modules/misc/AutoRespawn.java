package com.pandora.client.module.modules.misc;

import com.pandora.api.event.events.GuiScreenDisplayedEvent;
import com.pandora.api.settings.Setting;
import com.pandora.api.util.misc.MessageBus;
import com.pandora.client.PandoraMod;
import com.pandora.client.module.Module;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiScreen;

public class AutoRespawn extends Module {
   Setting.Boolean coords;
   @EventHandler
   private final Listener<GuiScreenDisplayedEvent> listener = new Listener((event) -> {
      if (event.getScreen() instanceof GuiGameOver) {
         if (this.coords.getValue()) {
            MessageBus.sendClientPrefixMessage(String.format("You died at x%d y%d z%d", (int)mc.field_71439_g.field_70165_t, (int)mc.field_71439_g.field_70163_u, (int)mc.field_71439_g.field_70161_v));
         }

         if (mc.field_71439_g != null) {
            mc.field_71439_g.func_71004_bE();
         }

         mc.func_147108_a((GuiScreen)null);
      }

   }, new Predicate[0]);

   public AutoRespawn() {
      super("AutoRespawn", Module.Category.Misc);
   }

   public void setup() {
      this.coords = this.registerBoolean("Coords", "Coords", false);
   }

   public void onEnable() {
      PandoraMod.EVENT_BUS.subscribe((Object)this);
   }

   public void onDisable() {
      PandoraMod.EVENT_BUS.unsubscribe((Object)this);
   }
}
