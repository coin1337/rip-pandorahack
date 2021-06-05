package com.pandora.client.module.modules.misc;

import com.pandora.api.event.events.PacketEvent;
import com.pandora.api.settings.Setting;
import com.pandora.client.PandoraMod;
import com.pandora.client.module.Module;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;

public class NoKick extends Module {
   public Setting.Boolean noPacketKick;
   Setting.Boolean noSlimeCrash;
   Setting.Boolean noOffhandCrash;
   @EventHandler
   private final Listener<PacketEvent.Receive> receiveListener = new Listener((event) -> {
      if (this.noOffhandCrash.getValue() && event.getPacket() instanceof SPacketSoundEffect && ((SPacketSoundEffect)event.getPacket()).func_186978_a() == SoundEvents.field_187719_p) {
         event.cancel();
      }

   }, new Predicate[0]);

   public NoKick() {
      super("NoKick", Module.Category.Misc);
   }

   public void setup() {
      this.noPacketKick = this.registerBoolean("Packet", "Packet", true);
      this.noSlimeCrash = this.registerBoolean("Slime", "Slime", false);
      this.noOffhandCrash = this.registerBoolean("Offhand", "Offhand", false);
   }

   public void onUpdate() {
      if (mc.field_71441_e != null && this.noSlimeCrash.getValue()) {
         mc.field_71441_e.field_72996_f.forEach((entity) -> {
            if (entity instanceof EntitySlime) {
               EntitySlime slime = (EntitySlime)entity;
               if (slime.func_70809_q() > 4) {
                  mc.field_71441_e.func_72900_e(entity);
               }
            }

         });
      }

   }

   public void onEnable() {
      PandoraMod.EVENT_BUS.subscribe((Object)this);
   }

   public void onDisable() {
      PandoraMod.EVENT_BUS.unsubscribe((Object)this);
   }
}
