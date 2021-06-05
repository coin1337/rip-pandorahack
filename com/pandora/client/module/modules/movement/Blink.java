package com.pandora.client.module.modules.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.pandora.api.event.events.PacketEvent;
import com.pandora.api.settings.Setting;
import com.pandora.client.PandoraMod;
import com.pandora.client.module.Module;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketTabComplete;

public class Blink extends Module {
   Setting.Boolean ghostPlayer;
   EntityOtherPlayerMP entity;
   private final Queue<Packet> packets = new ConcurrentLinkedQueue();
   @EventHandler
   private final Listener<PacketEvent.Send> packetSendListener = new Listener((event) -> {
      Packet packet = event.getPacket();
      if (!(packet instanceof CPacketChatMessage) && !(packet instanceof CPacketConfirmTeleport) && !(packet instanceof CPacketKeepAlive) && !(packet instanceof CPacketTabComplete) && !(packet instanceof CPacketClientStatus)) {
         if (mc.field_71439_g == null || mc.field_71439_g.field_70128_L) {
            this.packets.add(packet);
            event.cancel();
         }

      }
   }, new Predicate[0]);

   public Blink() {
      super("Blink", Module.Category.Movement);
   }

   public void setup() {
      this.ghostPlayer = this.registerBoolean("Ghost Player", "GhostPlayer", true);
   }

   public void onEnable() {
      PandoraMod.EVENT_BUS.subscribe((Object)this);
      if (this.ghostPlayer.getValue() && mc.field_71439_g != null) {
         this.entity = new EntityOtherPlayerMP(mc.field_71441_e, mc.func_110432_I().func_148256_e());
         this.entity.func_82149_j(mc.field_71439_g);
         this.entity.field_71071_by.func_70455_b(mc.field_71439_g.field_71071_by);
         this.entity.field_70177_z = mc.field_71439_g.field_70177_z;
         this.entity.field_70759_as = mc.field_71439_g.field_70759_as;
         mc.field_71441_e.func_73027_a(667, this.entity);
      }

   }

   public void onUpdate() {
      if (!this.ghostPlayer.getValue() && this.entity != null) {
         mc.field_71441_e.func_72900_e(this.entity);
      }

   }

   public void onDisable() {
      PandoraMod.EVENT_BUS.unsubscribe((Object)this);
      if (this.entity != null) {
         mc.field_71441_e.func_72900_e(this.entity);
      }

      if (this.packets.size() > 0 && mc.field_71439_g != null) {
         Iterator var1 = this.packets.iterator();

         while(var1.hasNext()) {
            Packet packet = (Packet)var1.next();
            mc.field_71439_g.field_71174_a.func_147297_a(packet);
         }

         this.packets.clear();
      }

   }

   public String getHudInfo() {
      String t = "[" + ChatFormatting.WHITE + this.packets.size() + ChatFormatting.GRAY + "]";
      return t;
   }
}
