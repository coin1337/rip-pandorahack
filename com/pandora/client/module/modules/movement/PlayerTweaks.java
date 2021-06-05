package com.pandora.client.module.modules.movement;

import com.pandora.api.event.events.PacketEvent;
import com.pandora.api.event.events.WaterPushEvent;
import com.pandora.api.settings.Setting;
import com.pandora.client.PandoraMod;
import com.pandora.client.module.Module;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.util.MovementInput;
import net.minecraftforge.client.event.InputUpdateEvent;
import org.lwjgl.input.Keyboard;

public class PlayerTweaks extends Module {
   public Setting.Boolean guiMove;
   public Setting.Boolean noPush;
   public Setting.Boolean noSlow;
   Setting.Boolean antiKnockBack;
   @EventHandler
   private final Listener<InputUpdateEvent> eventListener = new Listener((event) -> {
      if (this.noSlow.getValue() && mc.field_71439_g.func_184587_cr() && !mc.field_71439_g.func_184218_aH()) {
         MovementInput var10000 = event.getMovementInput();
         var10000.field_78902_a *= 5.0F;
         var10000 = event.getMovementInput();
         var10000.field_192832_b *= 5.0F;
      }

   }, new Predicate[0]);
   @EventHandler
   private final Listener<PacketEvent.Receive> receiveListener = new Listener((event) -> {
      if (this.antiKnockBack.getValue()) {
         if (event.getPacket() instanceof SPacketEntityVelocity && ((SPacketEntityVelocity)event.getPacket()).func_149412_c() == mc.field_71439_g.func_145782_y()) {
            event.cancel();
         }

         if (event.getPacket() instanceof SPacketExplosion) {
            event.cancel();
         }
      }

   }, new Predicate[0]);
   @EventHandler
   private final Listener<WaterPushEvent> waterPushEventListener = new Listener((event) -> {
      if (this.noPush.getValue()) {
         event.cancel();
      }

   }, new Predicate[0]);

   public PlayerTweaks() {
      super("PlayerTweaks", Module.Category.Movement);
   }

   public void setup() {
      this.guiMove = this.registerBoolean("Gui Move", "GuiMove", false);
      this.noPush = this.registerBoolean("No Push", "NoPush", false);
      this.noSlow = this.registerBoolean("No Slow", "NoSlow", false);
      this.antiKnockBack = this.registerBoolean("Velocity", "Velocity", false);
   }

   public void onUpdate() {
      if (this.guiMove.getValue() && mc.field_71462_r != null && !(mc.field_71462_r instanceof GuiChat)) {
         EntityPlayerSP var10000;
         if (Keyboard.isKeyDown(200)) {
            var10000 = mc.field_71439_g;
            var10000.field_70125_A -= 5.0F;
         }

         if (Keyboard.isKeyDown(208)) {
            var10000 = mc.field_71439_g;
            var10000.field_70125_A += 5.0F;
         }

         if (Keyboard.isKeyDown(205)) {
            var10000 = mc.field_71439_g;
            var10000.field_70177_z += 5.0F;
         }

         if (Keyboard.isKeyDown(203)) {
            var10000 = mc.field_71439_g;
            var10000.field_70177_z -= 5.0F;
         }

         if (mc.field_71439_g.field_70125_A > 90.0F) {
            mc.field_71439_g.field_70125_A = 90.0F;
         }

         if (mc.field_71439_g.field_70125_A < -90.0F) {
            mc.field_71439_g.field_70125_A = -90.0F;
         }
      }

   }

   public void onEnable() {
      PandoraMod.EVENT_BUS.subscribe((Object)this);
   }

   public void onDisable() {
      PandoraMod.EVENT_BUS.unsubscribe((Object)this);
   }
}
