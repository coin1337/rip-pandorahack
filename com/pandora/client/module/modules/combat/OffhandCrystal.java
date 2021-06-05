package com.pandora.client.module.modules.combat;

import com.pandora.api.settings.Setting;
import com.pandora.client.module.Module;
import com.pandora.client.module.ModuleManager;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class OffhandCrystal extends Module {
   public int totems;
   int crystals;
   boolean moving = false;
   boolean returnI = false;
   Item item;
   Setting.Integer health;
   Setting.Boolean disableGapple;

   public OffhandCrystal() {
      super("OffhandCrystal", Module.Category.Combat);
   }

   public void setup() {
      this.disableGapple = this.registerBoolean("Disable Gap", "DisableGap", true);
      this.health = this.registerInteger("Health", "Health", 15, 0, 36);
   }

   public void onEnable() {
      if (this.disableGapple.getValue() && ModuleManager.isModuleEnabled("OffhandGap")) {
         ModuleManager.getModuleByName("OffhandGap").disable();
      }

   }

   public void onDisable() {
      if (!(mc.field_71462_r instanceof GuiContainer)) {
         this.crystals = mc.field_71439_g.field_71071_by.field_70462_a.stream().filter((itemStack) -> {
            return itemStack.func_77973_b() == Items.field_190929_cY;
         }).mapToInt(ItemStack::func_190916_E).sum();
         if (mc.field_71439_g.func_184592_cb().func_77973_b() != Items.field_190929_cY) {
            if (this.crystals == 0) {
               return;
            }

            int t = -1;

            for(int i = 0; i < 45; ++i) {
               if (mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() == Items.field_190929_cY) {
                  t = i;
                  break;
               }
            }

            if (t == -1) {
               return;
            }

            mc.field_71442_b.func_187098_a(0, 45, 0, ClickType.PICKUP, mc.field_71439_g);
            mc.field_71442_b.func_187098_a(0, t < 9 ? t + 36 : t, 0, ClickType.PICKUP, mc.field_71439_g);
            mc.field_71442_b.func_187098_a(0, 45, 0, ClickType.PICKUP, mc.field_71439_g);
         }

      }
   }

   public void onUpdate() {
      this.item = Items.field_185158_cP;
      if (!(mc.field_71462_r instanceof GuiContainer)) {
         int t;
         int i;
         if (this.returnI) {
            t = -1;

            for(i = 0; i < 45; ++i) {
               if (mc.field_71439_g.field_71071_by.func_70301_a(i).func_190926_b()) {
                  t = i;
                  break;
               }
            }

            if (t == -1) {
               return;
            }

            mc.field_71442_b.func_187098_a(0, t < 9 ? t + 36 : t, 0, ClickType.PICKUP, mc.field_71439_g);
            this.returnI = false;
         }

         this.totems = mc.field_71439_g.field_71071_by.field_70462_a.stream().filter((itemStack) -> {
            return itemStack.func_77973_b() == Items.field_190929_cY;
         }).mapToInt(ItemStack::func_190916_E).sum();
         this.crystals = mc.field_71439_g.field_71071_by.field_70462_a.stream().filter((itemStack) -> {
            return itemStack.func_77973_b() == this.item;
         }).mapToInt(ItemStack::func_190916_E).sum();
         if (this.shouldTotem() && mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_190929_cY) {
            ++this.totems;
         } else if (!this.shouldTotem() && mc.field_71439_g.func_184592_cb().func_77973_b() == this.item) {
            this.crystals += mc.field_71439_g.func_184592_cb().func_190916_E();
         } else {
            if (this.moving) {
               mc.field_71442_b.func_187098_a(0, 45, 0, ClickType.PICKUP, mc.field_71439_g);
               this.moving = false;
               this.returnI = true;
               return;
            }

            if (mc.field_71439_g.field_71071_by.func_70445_o().func_190926_b()) {
               if (!this.shouldTotem() && mc.field_71439_g.func_184592_cb().func_77973_b() == this.item) {
                  return;
               }

               if (this.shouldTotem() && mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_190929_cY) {
                  return;
               }

               if (!this.shouldTotem()) {
                  if (this.crystals == 0) {
                     return;
                  }

                  t = -1;

                  for(i = 0; i < 45; ++i) {
                     if (mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() == this.item) {
                        t = i;
                        break;
                     }
                  }

                  if (t == -1) {
                     return;
                  }

                  mc.field_71442_b.func_187098_a(0, t < 9 ? t + 36 : t, 0, ClickType.PICKUP, mc.field_71439_g);
                  this.moving = true;
               } else {
                  if (this.totems == 0) {
                     return;
                  }

                  t = -1;

                  for(i = 0; i < 45; ++i) {
                     if (mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() == Items.field_190929_cY) {
                        t = i;
                        break;
                     }
                  }

                  if (t == -1) {
                     return;
                  }

                  mc.field_71442_b.func_187098_a(0, t < 9 ? t + 36 : t, 0, ClickType.PICKUP, mc.field_71439_g);
                  this.moving = true;
               }
            } else {
               t = -1;

               for(i = 0; i < 45; ++i) {
                  if (mc.field_71439_g.field_71071_by.func_70301_a(i).func_190926_b()) {
                     t = i;
                     break;
                  }
               }

               if (t == -1) {
                  return;
               }

               mc.field_71442_b.func_187098_a(0, t < 9 ? t + 36 : t, 0, ClickType.PICKUP, mc.field_71439_g);
            }
         }

      }
   }

   private boolean shouldTotem() {
      boolean hp = mc.field_71439_g.func_110143_aJ() + mc.field_71439_g.func_110139_bj() <= (float)this.health.getValue();
      boolean endcrystal = !this.isCrystalsAABBEmpty();
      return hp;
   }

   private boolean isEmpty(BlockPos pos) {
      List<Entity> crystalsInAABB = (List)mc.field_71441_e.func_72839_b((Entity)null, new AxisAlignedBB(pos)).stream().filter((e) -> {
         return e instanceof EntityEnderCrystal;
      }).collect(Collectors.toList());
      return crystalsInAABB.isEmpty();
   }

   private boolean isCrystalsAABBEmpty() {
      return this.isEmpty(mc.field_71439_g.func_180425_c().func_177982_a(1, 0, 0)) && this.isEmpty(mc.field_71439_g.func_180425_c().func_177982_a(-1, 0, 0)) && this.isEmpty(mc.field_71439_g.func_180425_c().func_177982_a(0, 0, 1)) && this.isEmpty(mc.field_71439_g.func_180425_c().func_177982_a(0, 0, -1)) && this.isEmpty(mc.field_71439_g.func_180425_c());
   }
}
