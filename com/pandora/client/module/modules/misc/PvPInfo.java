package com.pandora.client.module.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.pandora.api.event.events.PacketEvent;
import com.pandora.api.event.events.TotemPopEvent;
import com.pandora.api.settings.Setting;
import com.pandora.api.util.misc.MessageBus;
import com.pandora.client.PandoraMod;
import com.pandora.client.module.Module;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketEntityStatus;

public class PvPInfo extends Module {
   List<Entity> knownPlayers = new ArrayList();
   List<Entity> antipearlspamplz = new ArrayList();
   List<Entity> players;
   List<Entity> pearls;
   List<Entity> strengthedPlayers = new ArrayList();
   private HashMap<String, Integer> popCounterHashMap = new HashMap();
   Setting.Boolean visualRange;
   Setting.Boolean pearlAlert;
   Setting.Boolean strengthDetect;
   Setting.Boolean popCounter;
   Setting.Mode ChatColor;
   @EventHandler
   private final Listener<PacketEvent.Receive> packetEventListener = new Listener((event) -> {
      if (mc.field_71441_e != null && mc.field_71439_g != null) {
         if (event.getPacket() instanceof SPacketEntityStatus) {
            SPacketEntityStatus packet = (SPacketEntityStatus)event.getPacket();
            if (packet.func_149160_c() == 35) {
               Entity entity = packet.func_149161_a(mc.field_71441_e);
               PandoraMod.EVENT_BUS.post(new TotemPopEvent(entity));
            }
         }

      }
   }, new Predicate[0]);
   @EventHandler
   private final Listener<TotemPopEvent> totemPopEventListener = new Listener((event) -> {
      if (this.popCounter.getValue()) {
         if (this.popCounterHashMap == null) {
            this.popCounterHashMap = new HashMap();
         }

         if (this.popCounterHashMap.get(event.getEntity().func_70005_c_()) == null) {
            this.popCounterHashMap.put(event.getEntity().func_70005_c_(), 1);
            MessageBus.sendClientPrefixMessage(this.getTextColor() + event.getEntity().func_70005_c_() + " popped " + ChatFormatting.RED + 1 + this.getTextColor() + " totem!");
         } else if (this.popCounterHashMap.get(event.getEntity().func_70005_c_()) != null) {
            int popCounter = (Integer)this.popCounterHashMap.get(event.getEntity().func_70005_c_());
            ++popCounter;
            this.popCounterHashMap.put(event.getEntity().func_70005_c_(), popCounter);
            MessageBus.sendClientPrefixMessage(this.getTextColor() + event.getEntity().func_70005_c_() + " popped " + ChatFormatting.RED + popCounter + this.getTextColor() + " totems!");
         }
      }

   }, new Predicate[0]);

   public PvPInfo() {
      super("PvPInfo", Module.Category.Misc);
   }

   public void setup() {
      ArrayList<String> colors = new ArrayList();
      colors.add("Black");
      colors.add("Dark Green");
      colors.add("Dark Red");
      colors.add("Gold");
      colors.add("Dark Gray");
      colors.add("Green");
      colors.add("Red");
      colors.add("Yellow");
      colors.add("Dark Blue");
      colors.add("Dark Aqua");
      colors.add("Dark Purple");
      colors.add("Gray");
      colors.add("Blue");
      colors.add("Aqua");
      colors.add("Light Purple");
      colors.add("White");
      this.visualRange = this.registerBoolean("Visual Range", "VisualRange", false);
      this.pearlAlert = this.registerBoolean("Pearl Alert", "PearlAlert", false);
      this.strengthDetect = this.registerBoolean("Strength Detect", "StrengthDetect", false);
      this.popCounter = this.registerBoolean("Pop Counter", "PopCounter", false);
      this.ChatColor = this.registerMode("Color", "Color", colors, "Light Purple");
   }

   public void onUpdate() {
      Iterator var1;
      Entity e;
      if (this.visualRange.getValue()) {
         if (mc.field_71439_g == null) {
            return;
         }

         this.players = (List)mc.field_71441_e.field_72996_f.stream().filter((ex) -> {
            return ex instanceof EntityPlayer;
         }).collect(Collectors.toList());

         try {
            var1 = this.players.iterator();

            while(var1.hasNext()) {
               e = (Entity)var1.next();
               if (e instanceof EntityPlayer && !e.func_70005_c_().equalsIgnoreCase(mc.field_71439_g.func_70005_c_()) && !this.knownPlayers.contains(e)) {
                  this.knownPlayers.add(e);
                  MessageBus.sendClientPrefixMessage(this.getTextColor() + e.func_70005_c_() + " has been spotted thanks to Pandora!");
               }
            }
         } catch (Exception var5) {
         }

         try {
            var1 = this.knownPlayers.iterator();

            while(var1.hasNext()) {
               e = (Entity)var1.next();
               if (e instanceof EntityPlayer && !e.func_70005_c_().equalsIgnoreCase(mc.field_71439_g.func_70005_c_()) && !this.players.contains(e)) {
                  this.knownPlayers.remove(e);
               }
            }
         } catch (Exception var4) {
         }
      }

      if (this.pearlAlert.getValue()) {
         this.pearls = (List)mc.field_71441_e.field_72996_f.stream().filter((ex) -> {
            return ex instanceof EntityEnderPearl;
         }).collect(Collectors.toList());

         try {
            var1 = this.pearls.iterator();

            while(var1.hasNext()) {
               e = (Entity)var1.next();
               if (e instanceof EntityEnderPearl && !this.antipearlspamplz.contains(e)) {
                  this.antipearlspamplz.add(e);
                  MessageBus.sendClientPrefixMessage(this.getTextColor() + e.func_130014_f_().func_72890_a(e, 3.0D).func_70005_c_() + " has just thrown a pearl!");
               }
            }
         } catch (Exception var3) {
         }
      }

      EntityPlayer player;
      if (this.strengthDetect.getValue() && mc.field_71439_g != null && mc.field_71441_e != null) {
         var1 = mc.field_71441_e.field_73010_i.iterator();

         while(var1.hasNext()) {
            player = (EntityPlayer)var1.next();
            if (player.func_70644_a(MobEffects.field_76420_g) && !this.strengthedPlayers.contains(player)) {
               MessageBus.sendClientPrefixMessage(this.getTextColor() + player.func_70005_c_() + " has (drank) strength!");
               this.strengthedPlayers.add(player);
            }

            if (!player.func_70644_a(MobEffects.field_76420_g) && this.strengthedPlayers.contains(player)) {
               MessageBus.sendClientPrefixMessage(this.getTextColor() + player.func_70005_c_() + " no longer has strength!");
               this.strengthedPlayers.remove(player);
            }
         }
      }

      if (this.popCounter.getValue() && mc.field_71441_e != null && mc.field_71439_g != null) {
         var1 = mc.field_71441_e.field_73010_i.iterator();

         while(var1.hasNext()) {
            player = (EntityPlayer)var1.next();
            if (player.func_110143_aJ() <= 0.0F && this.popCounterHashMap.containsKey(player.getDisplayNameString())) {
               MessageBus.sendClientPrefixMessage(this.getTextColor() + player.func_70005_c_() + " died after popping " + ChatFormatting.GREEN + this.popCounterHashMap.get(player.func_70005_c_()) + this.getTextColor() + " totems!");
               this.popCounterHashMap.remove(player.func_70005_c_(), this.popCounterHashMap.get(player.func_70005_c_()));
            }
         }
      }

   }

   public ChatFormatting getTextColor() {
      if (this.ChatColor.getValue().equalsIgnoreCase("Black")) {
         return ChatFormatting.BLACK;
      } else if (this.ChatColor.getValue().equalsIgnoreCase("Dark Green")) {
         return ChatFormatting.DARK_GREEN;
      } else if (this.ChatColor.getValue().equalsIgnoreCase("Dark Red")) {
         return ChatFormatting.DARK_RED;
      } else if (this.ChatColor.getValue().equalsIgnoreCase("Gold")) {
         return ChatFormatting.GOLD;
      } else if (this.ChatColor.getValue().equalsIgnoreCase("Dark Gray")) {
         return ChatFormatting.DARK_GRAY;
      } else if (this.ChatColor.getValue().equalsIgnoreCase("Green")) {
         return ChatFormatting.GREEN;
      } else if (this.ChatColor.getValue().equalsIgnoreCase("Red")) {
         return ChatFormatting.RED;
      } else if (this.ChatColor.getValue().equalsIgnoreCase("Yellow")) {
         return ChatFormatting.YELLOW;
      } else if (this.ChatColor.getValue().equalsIgnoreCase("Dark Blue")) {
         return ChatFormatting.DARK_BLUE;
      } else if (this.ChatColor.getValue().equalsIgnoreCase("Dark Aqua")) {
         return ChatFormatting.DARK_AQUA;
      } else if (this.ChatColor.getValue().equalsIgnoreCase("Dark Purple")) {
         return ChatFormatting.DARK_PURPLE;
      } else if (this.ChatColor.getValue().equalsIgnoreCase("Gray")) {
         return ChatFormatting.GRAY;
      } else if (this.ChatColor.getValue().equalsIgnoreCase("Blue")) {
         return ChatFormatting.BLUE;
      } else if (this.ChatColor.getValue().equalsIgnoreCase("Light Purple")) {
         return ChatFormatting.LIGHT_PURPLE;
      } else if (this.ChatColor.getValue().equalsIgnoreCase("White")) {
         return ChatFormatting.WHITE;
      } else {
         return this.ChatColor.getValue().equalsIgnoreCase("Aqua") ? ChatFormatting.AQUA : null;
      }
   }

   public void onEnable() {
      PandoraMod.EVENT_BUS.subscribe((Object)this);
      this.popCounterHashMap = new HashMap();
   }

   public void onDisable() {
      this.knownPlayers.clear();
      PandoraMod.EVENT_BUS.unsubscribe((Object)this);
   }
}
