package com.pandora.client.module.modules.combat;

import com.pandora.api.event.events.PacketEvent;
import com.pandora.api.settings.Setting;
import com.pandora.api.util.players.friends.Friends;
import com.pandora.client.PandoraMod;
import com.pandora.client.module.Module;
import com.pandora.client.module.ModuleManager;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.network.play.client.CPacketUseEntity.Action;
import net.minecraft.util.EnumHand;

public class KillAura extends Module {
   Setting.Boolean players;
   Setting.Boolean hostileMobs;
   Setting.Boolean passiveMobs;
   Setting.Boolean swordOnly;
   Setting.Boolean caCheck;
   Setting.Boolean criticals;
   Setting.Double range;
   private boolean isAttacking = false;
   @EventHandler
   private final Listener<PacketEvent.Send> listener = new Listener((event) -> {
      if (event.getPacket() instanceof CPacketUseEntity && this.criticals.getValue() && ((CPacketUseEntity)event.getPacket()).func_149565_c() == Action.ATTACK && mc.field_71439_g.field_70122_E && this.isAttacking) {
         mc.field_71439_g.field_71174_a.func_147297_a(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 0.10000000149011612D, mc.field_71439_g.field_70161_v, false));
         mc.field_71439_g.field_71174_a.func_147297_a(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, false));
      }

   }, new Predicate[0]);

   public KillAura() {
      super("KillAura", Module.Category.Combat);
   }

   public void setup() {
      this.players = this.registerBoolean("Players", "Players", true);
      this.passiveMobs = this.registerBoolean("Animals", "Animals", false);
      this.hostileMobs = this.registerBoolean("Monsters", "Monsters", false);
      this.range = this.registerDouble("Range", "Range", 5.0D, 0.0D, 10.0D);
      this.swordOnly = this.registerBoolean("Sword Only", "SwordOnly", true);
      this.criticals = this.registerBoolean("Criticals", "Criticals", true);
      this.caCheck = this.registerBoolean("AC Check", "ACCheck", false);
   }

   public void onUpdate() {
      if (mc.field_71439_g != null && !mc.field_71439_g.field_70128_L) {
         List<Entity> targets = (List)mc.field_71441_e.field_72996_f.stream().filter((entity) -> {
            return entity != mc.field_71439_g;
         }).filter((entity) -> {
            return (double)mc.field_71439_g.func_70032_d(entity) <= this.range.getValue();
         }).filter((entity) -> {
            return !entity.field_70128_L;
         }).filter((entity) -> {
            return this.attackCheck(entity);
         }).sorted(Comparator.comparing((e) -> {
            return mc.field_71439_g.func_70032_d(e);
         })).collect(Collectors.toList());
         targets.forEach((target) -> {
            if (!this.swordOnly.getValue() || mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemSword) {
               if (!this.caCheck.getValue() || !((AutoCrystal)ModuleManager.getModuleByName("AutoCrystalGS")).isActive) {
                  this.attack(target);
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

   public void attack(Entity e) {
      if (mc.field_71439_g.func_184825_o(0.0F) >= 1.0F) {
         this.isAttacking = true;
         mc.field_71442_b.func_78764_a(mc.field_71439_g, e);
         mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
         this.isAttacking = false;
      }

   }

   private boolean attackCheck(Entity entity) {
      if (this.players.getValue() && entity instanceof EntityPlayer && !Friends.isFriend(entity.func_70005_c_()) && ((EntityPlayer)entity).func_110143_aJ() > 0.0F) {
         return true;
      } else if (this.passiveMobs.getValue() && entity instanceof EntityAnimal) {
         return !(entity instanceof EntityTameable);
      } else {
         return this.hostileMobs.getValue() && entity instanceof EntityMob;
      }
   }
}
