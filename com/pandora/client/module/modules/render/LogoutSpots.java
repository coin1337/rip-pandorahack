package com.pandora.client.module.modules.render;

import com.pandora.api.event.events.PlayerJoinEvent;
import com.pandora.api.event.events.PlayerLeaveEvent;
import com.pandora.api.event.events.RenderEvent;
import com.pandora.api.settings.Setting;
import com.pandora.api.util.misc.MessageBus;
import com.pandora.api.util.render.PandoraColor;
import com.pandora.api.util.render.PandoraTessellator;
import com.pandora.api.util.world.Timer;
import com.pandora.client.PandoraMod;
import com.pandora.client.module.Module;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Unload;

public class LogoutSpots extends Module {
   Setting.Boolean chatMsg;
   Setting.Boolean nameTag;
   Setting.Integer lineWidth;
   Setting.Integer range;
   Setting.Mode renderMode;
   Setting.ColorSetting color;
   Map<Entity, String> loggedPlayers = new ConcurrentHashMap();
   List<EntityPlayer> worldPlayers = new ArrayList();
   Timer timer = new Timer();
   @EventHandler
   private final Listener<PlayerJoinEvent> playerJoinEventListener1 = new Listener((event) -> {
      if (mc.field_71441_e != null) {
         this.loggedPlayers.forEach((entity, string) -> {
            if (entity.func_70005_c_().equalsIgnoreCase(event.getName())) {
               this.loggedPlayers.remove(entity);
               if (this.chatMsg.getValue()) {
                  MessageBus.sendClientPrefixMessage(event.getName() + " reconnected!");
               }
            }

         });
      }

   }, new Predicate[0]);
   @EventHandler
   private final Listener<PlayerLeaveEvent> playerLeaveEventListener2 = new Listener((event) -> {
      if (mc.field_71441_e != null) {
         this.worldPlayers.forEach((entity) -> {
            if (entity.func_70005_c_().equalsIgnoreCase(event.getName()) && !this.loggedPlayers.containsKey(entity.func_70005_c_())) {
               String date = (new SimpleDateFormat("k:mm")).format(new Date());
               this.loggedPlayers.put(entity, date);
               this.worldPlayers.remove(entity);
               if (this.chatMsg.getValue() && this.timer.getTimePassed() / 50L >= 5L) {
                  String location = "(" + (int)entity.field_70165_t + "," + (int)entity.field_70163_u + "," + (int)entity.field_70161_v + ")";
                  MessageBus.sendClientPrefixMessage(event.getName() + " disconnected at " + location + "!");
                  this.timer.reset();
               }
            }

         });
      }

   }, new Predicate[0]);
   @EventHandler
   private final Listener<Unload> unloadListener1 = new Listener((event) -> {
      this.worldPlayers.clear();
      if (mc.field_71439_g == null || mc.field_71441_e == null) {
         this.loggedPlayers.clear();
      }

   }, new Predicate[0]);
   @EventHandler
   private final Listener<Load> unloadListener2 = new Listener((event) -> {
      this.worldPlayers.clear();
      if (mc.field_71439_g == null || mc.field_71441_e == null) {
         this.loggedPlayers.clear();
      }

   }, new Predicate[0]);

   public LogoutSpots() {
      super("LogoutSpots", Module.Category.Render);
   }

   public void setup() {
      ArrayList<String> renderModes = new ArrayList();
      renderModes.add("Both");
      renderModes.add("Outline");
      renderModes.add("Fill");
      this.range = this.registerInteger("Range", "Range", 100, 10, 260);
      this.chatMsg = this.registerBoolean("Chat Msgs", "ChatMsgs", true);
      this.nameTag = this.registerBoolean("Nametag", "Nametag", true);
      this.lineWidth = this.registerInteger("Width", "Width", 1, 1, 10);
      this.renderMode = this.registerMode("Render", "Render", renderModes, "Both");
      this.color = this.registerColor("Color", "Color", new PandoraColor(255, 0, 0, 255));
   }

   public void onUpdate() {
      mc.field_71441_e.field_73010_i.stream().filter((entityPlayer) -> {
         return entityPlayer != mc.field_71439_g;
      }).filter((entityPlayer) -> {
         return entityPlayer.func_70032_d(mc.field_71439_g) <= (float)this.range.getValue();
      }).forEach((entityPlayer) -> {
         this.worldPlayers.add(entityPlayer);
      });
   }

   public void onWorldRender(RenderEvent event) {
      if (mc.field_71439_g != null && mc.field_71441_e != null) {
         this.loggedPlayers.forEach((entity, string) -> {
            this.startFunction(entity, string);
         });
      }

   }

   public void onEnable() {
      this.loggedPlayers.clear();
      this.worldPlayers = new ArrayList();
      PandoraMod.EVENT_BUS.subscribe((Object)this);
   }

   public void onDisable() {
      this.worldPlayers.clear();
      PandoraMod.EVENT_BUS.unsubscribe((Object)this);
   }

   private void startFunction(Entity entity, String string) {
      if (!(entity.func_70032_d(mc.field_71439_g) > (float)this.range.getValue())) {
         int posX = (int)entity.field_70165_t;
         int posY = (int)entity.field_70163_u;
         int posZ = (int)entity.field_70161_v;
         String[] nameTagMessage = new String[]{entity.func_70005_c_() + " (" + string + ")", "(" + posX + "," + posY + "," + posZ + ")"};
         GlStateManager.func_179094_E();
         PandoraTessellator.drawNametag(entity, nameTagMessage, this.color.getValue(), 0);
         String var7 = this.renderMode.getValue();
         byte var8 = -1;
         switch(var7.hashCode()) {
         case 2076577:
            if (var7.equals("Both")) {
               var8 = 0;
            }
            break;
         case 2189731:
            if (var7.equals("Fill")) {
               var8 = 2;
            }
            break;
         case 558407714:
            if (var7.equals("Outline")) {
               var8 = 1;
            }
         }

         switch(var8) {
         case 0:
            PandoraTessellator.drawBoundingBox(entity.func_184177_bl(), (float)this.lineWidth.getValue(), this.color.getValue());
            PandoraTessellator.drawBox(entity.func_184177_bl(), true, -0.4D, new PandoraColor(this.color.getValue(), 50), 63);
            break;
         case 1:
            PandoraTessellator.drawBoundingBox(entity.func_184177_bl(), (float)this.lineWidth.getValue(), this.color.getValue());
            break;
         case 2:
            PandoraTessellator.drawBox(entity.func_184177_bl(), true, -0.4D, new PandoraColor(this.color.getValue(), 50), 63);
         }

         GlStateManager.func_179121_F();
      }
   }
}
