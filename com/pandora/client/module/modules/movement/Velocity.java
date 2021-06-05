package com.pandora.client.module.modules.movement;

import com.pandora.api.event.events.PacketEvent;
import com.pandora.client.PandoraMod;
import com.pandora.client.module.Module;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;

public class Velocity extends Module {
   @EventHandler
   private Listener<PacketEvent.Receive> receiveListener = new Listener((event) -> {
      if (event.getPacket() instanceof SPacketEntityVelocity && ((SPacketEntityVelocity)event.getPacket()).func_149412_c() == mc.field_71439_g.func_145782_y()) {
         event.cancel();
      }

      if (event.getPacket() instanceof SPacketExplosion) {
         event.cancel();
      }

   }, new Predicate[0]);

   public Velocity() {
      super("Velocity", Module.Category.Movement);
   }

   public void onEnable() {
      PandoraMod.EVENT_BUS.subscribe((Object)this);
   }

   public void onDisable() {
      PandoraMod.EVENT_BUS.unsubscribe((Object)this);
   }
}
