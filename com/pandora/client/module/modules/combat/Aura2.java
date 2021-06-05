package com.pandora.client.module.modules.combat;

import com.pandora.api.settings.Setting;
import com.pandora.api.util.players.friends.Friends;
import com.pandora.api.util.world.EntityUtil;
import com.pandora.client.module.Module;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;

public class Aura2 extends Module {
   private Setting.Boolean attackPlayers = this.registerBoolean("Players", "Players", true);
   private Setting.Boolean attackMobs = this.registerBoolean("Mobs", "Mobs", false);
   private Setting.Boolean attackAnimals = this.registerBoolean("Animals", "Animals", false);
   private Setting.Boolean ignoreWalls = this.registerBoolean("Ignore Walls", "IgnoreWalls", true);
   private Setting.Boolean switchTo32k = this.registerBoolean("32k Switch", "32kSwitch", true);
   private Setting.Boolean onlyUse32k = this.registerBoolean("32k Only", "32kOnly", true);
   private Setting.Double hitRange = this.registerDouble("Hit Range", "HitRange", 5.5D, 0.0D, 25.0D);
   private Setting.Integer waitTick = this.registerInteger("Tick Delay", "TickDelay", 3, 0, 20);
   private int waitCounter = 0;

   public Aura2() {
      super("32K Aura", Module.Category.Combat);
   }

   public void onUpdate() {
      if (!mc.field_71439_g.field_70128_L) {
         if (this.waitCounter < this.waitTick.getValue()) {
            ++this.waitCounter;
         } else {
            this.waitCounter = 0;
            Iterator entityIterator = mc.field_71441_e.field_72996_f.iterator();

            Entity target;
            while(true) {
               do {
                  do {
                     do {
                        do {
                           do {
                              if (!entityIterator.hasNext()) {
                                 return;
                              }

                              target = (Entity)entityIterator.next();
                           } while(!EntityUtil.isLiving(target));
                        } while(target == mc.field_71439_g);
                     } while((double)mc.field_71439_g.func_70032_d(target) > this.hitRange.getValue());
                  } while(((EntityLivingBase)target).func_110143_aJ() <= 0.0F);
               } while(!this.ignoreWalls.getValue() && !mc.field_71439_g.func_70685_l(target) && !this.canEntityFeetBeSeen(target));

               if (this.attackPlayers.getValue() && target instanceof EntityPlayer && !Friends.isFriend(target.func_70005_c_())) {
                  this.attack(target);
                  return;
               }

               if (EntityUtil.isPassive(target)) {
                  if (this.attackAnimals.getValue()) {
                     break;
                  }
               } else if (EntityUtil.isMobAggressive(target) && this.attackMobs.getValue()) {
                  break;
               }
            }

            this.attack(target);
         }
      }
   }

   private boolean checkSharpness(ItemStack stack) {
      if (stack.func_77978_p() == null) {
         return false;
      } else {
         NBTTagList enchants = (NBTTagList)stack.func_77978_p().func_74781_a("ench");
         if (enchants == null) {
            return false;
         } else {
            for(int i = 0; i < enchants.func_74745_c(); ++i) {
               NBTTagCompound enchant = enchants.func_150305_b(i);
               if (enchant.func_74762_e("id") == 16) {
                  int lvl = enchant.func_74762_e("lvl");
                  if (lvl >= 42) {
                     return true;
                  }
                  break;
               }
            }

            return false;
         }
      }
   }

   private void attack(Entity e) {
      boolean holding32k = false;
      if (this.checkSharpness(mc.field_71439_g.func_184614_ca())) {
         holding32k = true;
      }

      if (this.switchTo32k.getValue() && !holding32k) {
         int newSlot = -1;

         for(int i = 0; i < 9; ++i) {
            ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack != ItemStack.field_190927_a && this.checkSharpness(stack)) {
               newSlot = i;
               break;
            }
         }

         if (newSlot != -1) {
            mc.field_71439_g.field_71071_by.field_70461_c = newSlot;
            holding32k = true;
         }
      }

      if (!this.onlyUse32k.getValue() || holding32k) {
         mc.field_71442_b.func_78764_a(mc.field_71439_g, e);
         mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
      }
   }

   private boolean canEntityFeetBeSeen(Entity entityIn) {
      return mc.field_71441_e.func_147447_a(new Vec3d(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + (double)mc.field_71439_g.func_70047_e(), mc.field_71439_g.field_70161_v), new Vec3d(entityIn.field_70165_t, entityIn.field_70163_u, entityIn.field_70161_v), false, true, false) == null;
   }
}
