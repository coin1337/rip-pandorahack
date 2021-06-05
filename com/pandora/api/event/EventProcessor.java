package com.pandora.api.event;

import com.google.common.collect.Maps;
import com.mojang.realmsclient.gui.ChatFormatting;
import com.pandora.api.event.events.PacketEvent;
import com.pandora.api.event.events.PlayerJoinEvent;
import com.pandora.api.event.events.PlayerLeaveEvent;
import com.pandora.api.util.misc.MessageBus;
import com.pandora.api.util.render.PandoraColor;
import com.pandora.client.PandoraMod;
import com.pandora.client.command.Command;
import com.pandora.client.command.CommandManager;
import com.pandora.client.module.ModuleManager;
import com.pandora.client.module.modules.render.SkyColor;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketPlayerListItem.Action;
import net.minecraft.network.play.server.SPacketPlayerListItem.AddPlayerData;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Post;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent.Finish;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Unload;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.MouseInputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class EventProcessor {
   public static EventProcessor INSTANCE;
   Minecraft mc = Minecraft.func_71410_x();
   CommandManager commandManager = new CommandManager();
   @EventHandler
   private final Listener<PacketEvent.Receive> receiveListener = new Listener((event) -> {
      if (event.getPacket() instanceof SPacketPlayerListItem) {
         SPacketPlayerListItem packet = (SPacketPlayerListItem)event.getPacket();
         Iterator var3;
         AddPlayerData playerData;
         if (packet.func_179768_b() == Action.ADD_PLAYER) {
            var3 = packet.func_179767_a().iterator();

            while(var3.hasNext()) {
               playerData = (AddPlayerData)var3.next();
               if (playerData.func_179962_a().getId() != this.mc.field_71449_j.func_148256_e().getId()) {
                  (new Thread(() -> {
                     String name = this.resolveName(playerData.func_179962_a().getId().toString());
                     if (name != null && this.mc.field_71439_g != null && this.mc.field_71439_g.field_70173_aa >= 1000) {
                        PandoraMod.EVENT_BUS.post(new PlayerJoinEvent(name));
                     }

                  })).start();
               }
            }
         }

         if (packet.func_179768_b() == Action.REMOVE_PLAYER) {
            var3 = packet.func_179767_a().iterator();

            while(var3.hasNext()) {
               playerData = (AddPlayerData)var3.next();
               if (playerData.func_179962_a().getId() != this.mc.field_71449_j.func_148256_e().getId()) {
                  (new Thread(() -> {
                     String name = this.resolveName(playerData.func_179962_a().getId().toString());
                     if (name != null && this.mc.field_71439_g != null && this.mc.field_71439_g.field_70173_aa >= 1000) {
                        PandoraMod.EVENT_BUS.post(new PlayerLeaveEvent(name));
                     }

                  })).start();
               }
            }
         }
      }

   }, new Predicate[0]);
   private final Map<String, String> uuidNameCache = Maps.newConcurrentMap();

   public EventProcessor() {
      INSTANCE = this;
   }

   @SubscribeEvent
   public void onTick(ClientTickEvent event) {
      if (this.mc.field_71439_g != null) {
         ModuleManager.onUpdate();
      }

   }

   @SubscribeEvent
   public void onWorldRender(RenderWorldLastEvent event) {
      if (!event.isCanceled()) {
         ModuleManager.onWorldRender(event);
      }
   }

   @SubscribeEvent
   public void onRender(Post event) {
      PandoraMod.EVENT_BUS.post(event);
      if (event.getType() == ElementType.HOTBAR) {
         ModuleManager.onRender();
      }

   }

   @SubscribeEvent(
      priority = EventPriority.NORMAL,
      receiveCanceled = true
   )
   public void onKeyInput(KeyInputEvent event) {
      if (Keyboard.getEventKeyState()) {
         if (Keyboard.getEventKey() == 0 || Keyboard.getEventKey() == 0) {
            return;
         }

         ModuleManager.onBind(Keyboard.getEventKey());
         PandoraMod.getInstance().clickGUI.handleKeyEvent(Keyboard.getEventKey());
      }

   }

   @SubscribeEvent
   public void onMouseInput(MouseInputEvent event) {
      if (Mouse.getEventButtonState()) {
         PandoraMod.EVENT_BUS.post(event);
      }

   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onChatSent(ClientChatEvent event) {
      if (event.getMessage().startsWith(Command.getCommandPrefix())) {
         event.setCanceled(true);

         try {
            this.mc.field_71456_v.func_146158_b().func_146239_a(event.getMessage());
            this.commandManager.callCommand(event.getMessage().substring(1));
         } catch (Exception var3) {
            var3.printStackTrace();
            MessageBus.sendClientPrefixMessage(ChatFormatting.DARK_RED + "Error: " + var3.getMessage());
         }
      }

   }

   @SubscribeEvent
   public void onFogColorRender(FogColors event) {
      if (ModuleManager.isModuleEnabled("SkyColor")) {
         PandoraColor color = SkyColor.color.getValue();
         event.setRed((float)color.getRed() / 255.0F);
         event.setGreen((float)color.getGreen() / 255.0F);
         event.setBlue((float)color.getBlue() / 255.0F);
      }

   }

   @SubscribeEvent
   public void fog(FogDensity event) {
      if (ModuleManager.isModuleEnabled("SkyColor") && !SkyColor.fog.getValue()) {
         event.setDensity(0.0F);
         event.setCanceled(true);
      }

   }

   @SubscribeEvent
   public void onRenderScreen(Text event) {
      PandoraMod.EVENT_BUS.post(event);
   }

   @SubscribeEvent
   public void onChatReceived(ClientChatReceivedEvent event) {
      PandoraMod.EVENT_BUS.post(event);
   }

   @SubscribeEvent
   public void onAttackEntity(AttackEntityEvent event) {
      PandoraMod.EVENT_BUS.post(event);
   }

   @SubscribeEvent
   public void onPlayerRespawn(PlayerRespawnEvent event) {
      PandoraMod.EVENT_BUS.post(event);
   }

   @SubscribeEvent
   public void onDrawBlockHighlight(DrawBlockHighlightEvent event) {
      PandoraMod.EVENT_BUS.post(event);
   }

   @SubscribeEvent
   public void onRenderBlockOverlay(RenderBlockOverlayEvent event) {
      PandoraMod.EVENT_BUS.post(event);
   }

   @SubscribeEvent
   public void onLivingDamage(LivingDamageEvent event) {
      PandoraMod.EVENT_BUS.post(event);
   }

   @SubscribeEvent
   public void onLivingEntityUseItemFinish(Finish event) {
      PandoraMod.EVENT_BUS.post(event);
   }

   @SubscribeEvent
   public void onInputUpdate(InputUpdateEvent event) {
      PandoraMod.EVENT_BUS.post(event);
   }

   @SubscribeEvent
   public void onLivingDeath(LivingDeathEvent event) {
      PandoraMod.EVENT_BUS.post(event);
   }

   @SubscribeEvent
   public void onPlayerPush(PlayerSPPushOutOfBlocksEvent event) {
      PandoraMod.EVENT_BUS.post(event);
   }

   @SubscribeEvent
   public void onWorldUnload(Unload event) {
      PandoraMod.EVENT_BUS.post(event);
   }

   @SubscribeEvent
   public void onWorldLoad(Load event) {
      PandoraMod.EVENT_BUS.post(event);
   }

   public String resolveName(String uuid) {
      uuid = uuid.replace("-", "");
      if (this.uuidNameCache.containsKey(uuid)) {
         return (String)this.uuidNameCache.get(uuid);
      } else {
         String url = "https://api.mojang.com/user/profiles/" + uuid + "/names";

         try {
            String nameJson = IOUtils.toString(new URL(url));
            if (nameJson != null && nameJson.length() > 0) {
               JSONArray jsonArray = (JSONArray)JSONValue.parseWithException(nameJson);
               if (jsonArray != null) {
                  JSONObject latestName = (JSONObject)jsonArray.get(jsonArray.size() - 1);
                  if (latestName != null) {
                     return latestName.get("name").toString();
                  }
               }
            }
         } catch (ParseException | IOException var6) {
            var6.printStackTrace();
         }

         return null;
      }
   }

   public void init() {
      PandoraMod.EVENT_BUS.subscribe((Object)this);
      MinecraftForge.EVENT_BUS.register(this);
   }
}
