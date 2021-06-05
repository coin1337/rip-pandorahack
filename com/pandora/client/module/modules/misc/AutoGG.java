package com.pandora.client.module.modules.misc;

import com.pandora.api.event.events.PacketEvent;
import com.pandora.api.util.misc.MessageBus;
import com.pandora.client.PandoraMod;
import com.pandora.client.module.Module;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketUseEntity.Action;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class AutoGG extends Module {
   public static AutoGG INSTANCE;
   static List<String> AutoGgMessages = new ArrayList();
   private ConcurrentHashMap targetedPlayers = null;
   int index = -1;
   @EventHandler
   private final Listener<PacketEvent.Send> sendListener = new Listener((event) -> {
      if (mc.field_71439_g != null) {
         if (this.targetedPlayers == null) {
            this.targetedPlayers = new ConcurrentHashMap();
         }

         if (event.getPacket() instanceof CPacketUseEntity) {
            CPacketUseEntity cPacketUseEntity = (CPacketUseEntity)event.getPacket();
            if (cPacketUseEntity.func_149565_c().equals(Action.ATTACK)) {
               Entity targetEntity = cPacketUseEntity.func_149564_a(mc.field_71441_e);
               if (targetEntity instanceof EntityPlayer) {
                  this.addTargetedPlayer(targetEntity.func_70005_c_());
               }
            }
         }
      }

   }, new Predicate[0]);
   @EventHandler
   private final Listener<LivingDeathEvent> livingDeathEventListener = new Listener((event) -> {
      if (mc.field_71439_g != null) {
         if (this.targetedPlayers == null) {
            this.targetedPlayers = new ConcurrentHashMap();
         }

         EntityLivingBase entity = event.getEntityLiving();
         if (entity != null && entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entity;
            if (player.func_110143_aJ() <= 0.0F) {
               String name = player.func_70005_c_();
               if (this.shouldAnnounce(name)) {
                  this.doAnnounce(name);
               }
            }
         }
      }

   }, new Predicate[0]);

   public AutoGG() {
      super("AutoGG", Module.Category.Misc);
      INSTANCE = this;
   }

   public void onEnable() {
      this.targetedPlayers = new ConcurrentHashMap();
      PandoraMod.EVENT_BUS.subscribe((Object)this);
   }

   public void onDisable() {
      this.targetedPlayers = null;
      PandoraMod.EVENT_BUS.unsubscribe((Object)this);
   }

   public void onUpdate() {
      if (this.targetedPlayers == null) {
         this.targetedPlayers = new ConcurrentHashMap();
      }

      Iterator var1 = mc.field_71441_e.func_72910_y().iterator();

      while(var1.hasNext()) {
         Entity entity = (Entity)var1.next();
         if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entity;
            if (player.func_110143_aJ() <= 0.0F) {
               String name = player.func_70005_c_();
               if (this.shouldAnnounce(name)) {
                  this.doAnnounce(name);
                  break;
               }
            }
         }
      }

      this.targetedPlayers.forEach((namex, timeout) -> {
         if ((Integer)timeout <= 0) {
            this.targetedPlayers.remove(namex);
         } else {
            this.targetedPlayers.put(namex, (Integer)timeout - 1);
         }

      });
   }

   private boolean shouldAnnounce(String name) {
      return this.targetedPlayers.containsKey(name);
   }

   private void doAnnounce(String name) {
      this.targetedPlayers.remove(name);
      if (this.index >= AutoGgMessages.size() - 1) {
         this.index = -1;
      }

      ++this.index;
      String message;
      if (AutoGgMessages.size() > 0) {
         message = (String)AutoGgMessages.get(this.index);
      } else {
         message = "GG! Pandora v1.2 is on top!";
      }

      String messageSanitized = message.replaceAll("à¸¢à¸‡", "").replace("{name}", name);
      if (messageSanitized.length() > 255) {
         messageSanitized = messageSanitized.substring(0, 255);
      }

      MessageBus.sendServerMessage(messageSanitized);
   }

   public void addTargetedPlayer(String name) {
      if (!Objects.equals(name, mc.field_71439_g.func_70005_c_())) {
         if (this.targetedPlayers == null) {
            this.targetedPlayers = new ConcurrentHashMap();
         }

         this.targetedPlayers.put(name, 20);
      }

   }

   public static void addAutoGgMessage(String s) {
      AutoGgMessages.add(s);
   }

   public static List<String> getAutoGgMessages() {
      return AutoGgMessages;
   }
}
