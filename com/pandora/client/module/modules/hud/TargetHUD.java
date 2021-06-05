package com.pandora.client.module.modules.hud;

import com.lukflug.panelstudio.Context;
import com.lukflug.panelstudio.Interface;
import com.lukflug.panelstudio.hud.HUDComponent;
import com.pandora.api.settings.Setting;
import com.pandora.api.util.players.enemy.Enemies;
import com.pandora.api.util.players.friends.Friends;
import com.pandora.api.util.render.PandoraColor;
import com.pandora.api.util.world.EntityUtil;
import com.pandora.client.clickgui.PandoraGUI;
import com.pandora.client.module.modules.gui.ColorMain;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;

public class TargetHUD extends HUDModule {
   private static Setting.ColorSetting outline;
   private static Setting.ColorSetting background;
   private static Setting.Integer range;
   private static EntityPlayer targetPlayer;

   public TargetHUD() {
      super(new TargetHUD.TargetHUDComponent(), new Point(0, 70));
   }

   public void setup() {
      range = this.registerInteger("Range", "Range", 100, 10, 260);
      outline = this.registerColor("Outline", "Outline", new PandoraColor(255, 0, 0, 255));
      background = this.registerColor("Background", "Background", new PandoraColor(0, 0, 0, 255));
   }

   private static Color getNameColor(String playerName) {
      if (Friends.isFriend(playerName)) {
         return new PandoraColor(ColorMain.getFriendGSColor(), 255);
      } else {
         return Enemies.isEnemy(playerName) ? new PandoraColor(ColorMain.getEnemyGSColor(), 255) : new PandoraColor(255, 255, 255, 255);
      }
   }

   private static Color getHealthColor(int health) {
      if (health >= 15) {
         return new PandoraColor(0, 255, 0, 255);
      } else {
         return health >= 5 && health < 15 ? new PandoraColor(255, 255, 0, 255) : new PandoraColor(255, 0, 0, 255);
      }
   }

   private static boolean isValidEntity(Entity e) {
      if (!(e instanceof EntityPlayer)) {
         return false;
      } else {
         return e != mc.field_71439_g;
      }
   }

   private static float getPing(EntityPlayer player) {
      float ping = 0.0F;

      try {
         ping = EntityUtil.clamp((float)((NetHandlerPlayClient)Objects.requireNonNull(mc.func_147114_u())).func_175102_a(player.func_110124_au()).func_178853_c(), 1.0F, 300.0F);
      } catch (NullPointerException var3) {
      }

      return ping;
   }

   public static boolean isRenderingEntity(EntityPlayer entityPlayer) {
      return targetPlayer == entityPlayer;
   }

   private static class TargetHUDComponent extends HUDComponent {
      public TargetHUDComponent() {
         super("TargetHUD", PandoraGUI.theme.getPanelRenderer(), new Point(0, 70));
      }

      public void render(Context context) {
         super.render(context);
         if (TargetHUD.mc.field_71441_e != null && TargetHUD.mc.field_71439_g.field_70173_aa >= 10) {
            EntityPlayer entityPlayer = (EntityPlayer)TargetHUD.mc.field_71441_e.field_72996_f.stream().filter((entity) -> {
               return TargetHUD.isValidEntity(entity);
            }).map((entity) -> {
               return (EntityLivingBase)entity;
            }).min(Comparator.comparing((c) -> {
               return TargetHUD.mc.field_71439_g.func_70032_d(c);
            })).orElse((Object)null);
            if (entityPlayer != null && entityPlayer.func_70032_d(TargetHUD.mc.field_71439_g) <= (float)TargetHUD.range.getValue()) {
               Color bgcolor = new PandoraColor(TargetHUD.background.getValue(), 100);
               context.getInterface().fillRect(context.getRect(), bgcolor, bgcolor, bgcolor, bgcolor);
               Color color = TargetHUD.outline.getValue();
               context.getInterface().fillRect(new Rectangle(context.getPos(), new Dimension(context.getSize().width, 1)), color, color, color, color);
               context.getInterface().fillRect(new Rectangle(context.getPos(), new Dimension(1, context.getSize().height)), color, color, color, color);
               context.getInterface().fillRect(new Rectangle(new Point(context.getPos().x + context.getSize().width - 1, context.getPos().y), new Dimension(1, context.getSize().height)), color, color, color, color);
               context.getInterface().fillRect(new Rectangle(new Point(context.getPos().x, context.getPos().y + context.getSize().height - 1), new Dimension(context.getSize().width, 1)), color, color, color, color);
               TargetHUD.targetPlayer = entityPlayer;
               PandoraGUI.renderEntity(entityPlayer, new Point(context.getPos().x + 35, context.getPos().y + 87 - (entityPlayer.func_70093_af() ? 10 : 0)));
               TargetHUD.targetPlayer = null;
               String playerName = entityPlayer.func_70005_c_();
               Color nameColor = TargetHUD.getNameColor(playerName);
               context.getInterface().drawString(new Point(context.getPos().x + 71, context.getPos().y + 11), TextFormatting.BOLD + playerName, nameColor);
               int playerHealth = (int)(entityPlayer.func_110143_aJ() + entityPlayer.func_110139_bj());
               Color healthColor = TargetHUD.getHealthColor(playerHealth);
               context.getInterface().drawString(new Point(context.getPos().x + 71, context.getPos().y + 23), TextFormatting.WHITE + "Health: " + TextFormatting.RESET + playerHealth, healthColor);
               context.getInterface().drawString(new Point(context.getPos().x + 71, context.getPos().y + 33), "Distance: " + (int)entityPlayer.func_70032_d(TargetHUD.mc.field_71439_g), new Color(255, 255, 255));
               String info;
               if (entityPlayer.field_71071_by.func_70440_f(2).func_77973_b().equals(Items.field_185160_cR)) {
                  info = TextFormatting.LIGHT_PURPLE + "Wasp";
               } else if (entityPlayer.field_71071_by.func_70440_f(2).func_77973_b().equals(Items.field_151163_ad)) {
                  info = TextFormatting.RED + "Threat";
               } else if (entityPlayer.field_71071_by.func_70440_f(3).func_77973_b().equals(Items.field_190931_a)) {
                  info = TextFormatting.GREEN + "NewFag";
               } else {
                  info = TextFormatting.WHITE + "None";
               }

               context.getInterface().drawString(new Point(context.getPos().x + 71, context.getPos().y + 43), info + TextFormatting.WHITE + " | " + TargetHUD.getPing(entityPlayer) + " ms", new Color(255, 255, 255));
               String status = null;
               Color statusColor = null;
               Iterator var12 = entityPlayer.func_70651_bq().iterator();

               while(var12.hasNext()) {
                  PotionEffect effect = (PotionEffect)var12.next();
                  if (effect.func_188419_a() == MobEffects.field_76437_t) {
                     status = "Weakness!";
                     statusColor = new Color(135, 0, 25);
                  } else if (effect.func_188419_a() == MobEffects.field_76441_p) {
                     status = "Invisible!";
                     statusColor = new Color(90, 90, 90);
                  } else if (effect.func_188419_a() == MobEffects.field_76420_g) {
                     status = "Strength!";
                     statusColor = new Color(185, 65, 185);
                  }
               }

               if (status != null) {
                  context.getInterface().drawString(new Point(context.getPos().x + 71, context.getPos().y + 55), TextFormatting.WHITE + "Status: " + TextFormatting.RESET + status, statusColor);
               }

               int xPos = context.getPos().x + 150;
               Iterator var16 = entityPlayer.func_184193_aE().iterator();

               while(var16.hasNext()) {
                  ItemStack itemStack = (ItemStack)var16.next();
                  xPos -= 20;
                  PandoraGUI.renderItem(itemStack, new Point(xPos, context.getPos().y + 73));
               }
            }
         }

      }

      public int getWidth(Interface inter) {
         return 162;
      }

      public void getHeight(Context context) {
         context.setHeight(94);
      }
   }
}
