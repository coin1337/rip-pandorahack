package com.pandora.client.module.modules.render;

import com.pandora.api.event.events.RenderEvent;
import com.pandora.api.settings.Setting;
import com.pandora.api.util.players.enemy.Enemies;
import com.pandora.api.util.players.friends.Friends;
import com.pandora.api.util.render.PandoraColor;
import com.pandora.api.util.render.PandoraTessellator;
import com.pandora.client.module.Module;
import com.pandora.client.module.modules.gui.ColorMain;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntityShulkerBox;

public class ESP extends Module {
   Setting.Boolean playerRender;
   Setting.Boolean mobRender;
   Setting.Boolean containerRender;
   Setting.Boolean itemRender;
   Setting.Boolean entityRender;
   Setting.Boolean glowCrystals;
   Setting.Integer width;
   Setting.Integer range;
   Setting.ColorSetting mainColor;
   PandoraColor playerColor;
   PandoraColor mobColor;
   PandoraColor mainIntColor;
   PandoraColor containerColor;
   int opacityGradient;

   public ESP() {
      super("ESP", Module.Category.Render);
   }

   public void setup() {
      this.mainColor = this.registerColor("Color", "Color");
      this.range = this.registerInteger("Range", "Range", 100, 10, 260);
      this.width = this.registerInteger("Line Width", "LineWidth", 2, 1, 5);
      this.playerRender = this.registerBoolean("Player", "Player", true);
      this.mobRender = this.registerBoolean("Mob", "Mob", false);
      this.entityRender = this.registerBoolean("Entity", "Entity", false);
      this.itemRender = this.registerBoolean("Item", "Item", true);
      this.containerRender = this.registerBoolean("Container", "Container", false);
      this.glowCrystals = this.registerBoolean("Glow Crystal", "GlowCrystal", false);
   }

   public void onWorldRender(RenderEvent event) {
      mc.field_71441_e.field_72996_f.stream().filter((entity) -> {
         return entity != mc.field_71439_g;
      }).filter((entity) -> {
         return this.rangeEntityCheck(entity);
      }).forEach((entity) -> {
         this.defineEntityColors(entity);
         if (this.playerRender.getValue() && entity instanceof EntityPlayer) {
            PandoraTessellator.drawBoundingBox(entity.func_174813_aQ(), (float)this.width.getValue(), this.playerColor);
         }

         if (this.mobRender.getValue() && (entity instanceof EntityCreature || entity instanceof EntitySlime)) {
            PandoraTessellator.drawBoundingBox(entity.func_174813_aQ(), (float)this.width.getValue(), this.mobColor);
         }

         if (this.itemRender.getValue() && entity instanceof EntityItem) {
            PandoraTessellator.drawBoundingBox(entity.func_174813_aQ(), (float)this.width.getValue(), this.mainIntColor);
         }

         if (this.entityRender.getValue() && (entity instanceof EntityEnderPearl || entity instanceof EntityXPOrb || entity instanceof EntityExpBottle || entity instanceof EntityEnderCrystal)) {
            PandoraTessellator.drawBoundingBox(entity.func_174813_aQ(), (float)this.width.getValue(), this.mainIntColor);
         }

         if (this.glowCrystals.getValue() && entity instanceof EntityEnderCrystal) {
            entity.func_184195_f(true);
         }

         if (!this.glowCrystals.getValue() && entity instanceof EntityEnderCrystal && entity.func_184202_aL()) {
            entity.func_184195_f(false);
         }

      });
      if (this.containerRender.getValue()) {
         mc.field_71441_e.field_147482_g.stream().filter((tileEntity) -> {
            return this.rangeTileCheck(tileEntity);
         }).forEach((tileEntity) -> {
            if (tileEntity instanceof TileEntityChest) {
               this.containerColor = new PandoraColor(255, 255, 0, this.opacityGradient);
               PandoraTessellator.drawBoundingBox(mc.field_71441_e.func_180495_p(tileEntity.func_174877_v()).func_185918_c(mc.field_71441_e, tileEntity.func_174877_v()), (float)this.width.getValue(), this.containerColor);
            }

            if (tileEntity instanceof TileEntityEnderChest) {
               this.containerColor = new PandoraColor(180, 70, 200, this.opacityGradient);
               PandoraTessellator.drawBoundingBox(mc.field_71441_e.func_180495_p(tileEntity.func_174877_v()).func_185918_c(mc.field_71441_e, tileEntity.func_174877_v()), (float)this.width.getValue(), this.containerColor);
            }

            if (tileEntity instanceof TileEntityShulkerBox) {
               this.containerColor = new PandoraColor(255, 0, 0, this.opacityGradient);
               PandoraTessellator.drawBoundingBox(mc.field_71441_e.func_180495_p(tileEntity.func_174877_v()).func_185918_c(mc.field_71441_e, tileEntity.func_174877_v()), (float)this.width.getValue(), this.containerColor);
            }

            if (tileEntity instanceof TileEntityDispenser || tileEntity instanceof TileEntityFurnace || tileEntity instanceof TileEntityHopper || tileEntity instanceof TileEntityDropper) {
               this.containerColor = new PandoraColor(150, 150, 150, this.opacityGradient);
               PandoraTessellator.drawBoundingBox(mc.field_71441_e.func_180495_p(tileEntity.func_174877_v()).func_185918_c(mc.field_71441_e, tileEntity.func_174877_v()), (float)this.width.getValue(), this.containerColor);
            }

         });
      }

   }

   public void onDisable() {
      mc.field_71441_e.field_72996_f.stream().forEach((entity) -> {
         if (entity instanceof EntityEnderCrystal && entity.func_184202_aL()) {
            entity.func_184195_f(false);
         }

      });
   }

   private void defineEntityColors(Entity entity) {
      if (entity instanceof EntityPlayer) {
         if (Friends.isFriend(entity.func_70005_c_())) {
            this.playerColor = ColorMain.getFriendGSColor();
         } else if (Enemies.isEnemy(entity.func_70005_c_())) {
            this.playerColor = ColorMain.getEnemyGSColor();
         } else {
            this.playerColor = new PandoraColor(this.mainColor.getValue(), this.opacityGradient);
         }
      }

      if (entity instanceof EntityMob) {
         this.mobColor = new PandoraColor(255, 0, 0, this.opacityGradient);
      } else if (entity instanceof EntityAnimal) {
         this.mobColor = new PandoraColor(0, 255, 0, this.opacityGradient);
      } else {
         this.mobColor = new PandoraColor(255, 165, 0, this.opacityGradient);
      }

      if (entity instanceof EntitySlime) {
         this.mobColor = new PandoraColor(255, 0, 0, this.opacityGradient);
      }

      if (entity != null) {
         this.mainIntColor = new PandoraColor(this.mainColor.getValue(), this.opacityGradient);
      }

   }

   private boolean rangeEntityCheck(Entity entity) {
      if (entity.func_70032_d(mc.field_71439_g) > (float)this.range.getValue()) {
         return false;
      } else {
         if (entity.func_70032_d(mc.field_71439_g) >= 180.0F) {
            this.opacityGradient = 50;
         } else if (entity.func_70032_d(mc.field_71439_g) >= 130.0F && entity.func_70032_d(mc.field_71439_g) < 180.0F) {
            this.opacityGradient = 100;
         } else if (entity.func_70032_d(mc.field_71439_g) >= 80.0F && entity.func_70032_d(mc.field_71439_g) < 130.0F) {
            this.opacityGradient = 150;
         } else if (entity.func_70032_d(mc.field_71439_g) >= 30.0F && entity.func_70032_d(mc.field_71439_g) < 80.0F) {
            this.opacityGradient = 200;
         } else {
            this.opacityGradient = 255;
         }

         return true;
      }
   }

   private boolean rangeTileCheck(TileEntity tileEntity) {
      if (tileEntity.func_145835_a(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v) > (double)(this.range.getValue() * this.range.getValue())) {
         return false;
      } else {
         if (tileEntity.func_145835_a(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v) >= 32400.0D) {
            this.opacityGradient = 50;
         } else if (tileEntity.func_145835_a(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v) >= 16900.0D && tileEntity.func_145835_a(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v) < 32400.0D) {
            this.opacityGradient = 100;
         } else if (tileEntity.func_145835_a(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v) >= 6400.0D && tileEntity.func_145835_a(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v) < 16900.0D) {
            this.opacityGradient = 150;
         } else if (tileEntity.func_145835_a(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v) >= 900.0D && tileEntity.func_145835_a(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v) < 6400.0D) {
            this.opacityGradient = 200;
         } else {
            this.opacityGradient = 255;
         }

         return true;
      }
   }
}
