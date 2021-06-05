package com.pandora.client.module.modules.render;

import com.pandora.api.event.events.RenderEvent;
import com.pandora.api.settings.Setting;
import com.pandora.api.util.font.FontUtils;
import com.pandora.api.util.misc.Wrapper;
import com.pandora.api.util.players.enemy.Enemies;
import com.pandora.api.util.players.friends.Friends;
import com.pandora.api.util.render.PandoraColor;
import com.pandora.api.util.render.PandoraTessellator;
import com.pandora.client.module.Module;
import com.pandora.client.module.modules.gui.ColorMain;
import java.util.Iterator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;

public class Nametags extends Module {
   Setting.Integer range;
   Setting.Boolean durability;
   Setting.Boolean armor;
   Setting.Boolean enchantnames;
   Setting.Boolean itemName;
   Setting.Boolean gamemode;
   Setting.Boolean health;
   Setting.Boolean ping;
   Setting.Boolean entityId;
   public static Setting.Boolean customColor;
   public static Setting.ColorSetting borderColor;

   public Nametags() {
      super("Nametags", Module.Category.Render);
   }

   public void setup() {
      this.range = this.registerInteger("Range", "Range", 100, 10, 260);
      this.durability = this.registerBoolean("Durability", "Durability", true);
      this.armor = this.registerBoolean("Armor", "Armor", true);
      this.enchantnames = this.registerBoolean("Enchants", "Enchants", true);
      this.itemName = this.registerBoolean("Item Name", "ItemName", false);
      this.gamemode = this.registerBoolean("Gamemode", "Gamemode", false);
      this.health = this.registerBoolean("Health", "Health", true);
      this.ping = this.registerBoolean("Ping", "Ping", false);
      this.entityId = this.registerBoolean("Entity Id", "EntityId", false);
      customColor = this.registerBoolean("Custom Color", "CustomColor", true);
      borderColor = this.registerColor("Border Color", "BorderColor");
   }

   public void onWorldRender(RenderEvent event) {
      Iterator var2 = mc.field_71441_e.field_73010_i.iterator();

      while(var2.hasNext()) {
         Object o = var2.next();
         Entity entity = (Entity)o;
         if (entity instanceof EntityPlayer && entity != mc.field_71439_g && entity.func_70089_S() && entity.func_70032_d(mc.field_71439_g) <= (float)this.range.getValue()) {
            Vec3d m = renderPosEntity(entity);
            this.renderNameTagsFor((EntityPlayer)entity, m.field_72450_a, m.field_72448_b, m.field_72449_c);
         }
      }

   }

   public void renderNameTagsFor(EntityPlayer entityPlayer, double n, double n2, double n3) {
      this.renderNametags(entityPlayer, n, n2, n3);
   }

   public static double timerPos(double n, double n2) {
      return n2 + (n - n2) * (double)Wrapper.getMinecraft().field_71428_T.field_194147_b;
   }

   public static Vec3d renderPosEntity(Entity entity) {
      return new Vec3d(timerPos(entity.field_70165_t, entity.field_70142_S), timerPos(entity.field_70163_u, entity.field_70137_T), timerPos(entity.field_70161_v, entity.field_70136_U));
   }

   private void renderEnchants(ItemStack itemStack, int x, int y) {
      GlStateManager.func_179098_w();
      Iterator iterator2;
      Iterator iterator = iterator2 = EnchantmentHelper.func_82781_a(itemStack).keySet().iterator();

      while(iterator.hasNext()) {
         Enchantment enchantment;
         if ((enchantment = (Enchantment)iterator2.next()) == null) {
            iterator = iterator2;
         } else {
            if (!this.enchantnames.getValue()) {
               return;
            }

            FontUtils.drawStringWithShadow(ColorMain.customFont.getValue(), this.stringForEnchants(enchantment, EnchantmentHelper.func_77506_a(enchantment, itemStack)), x * 2, y, new PandoraColor(255, 255, 255));
            y += 8;
            iterator = iterator2;
         }
      }

      if (itemStack.func_77973_b().equals(Items.field_151153_ao) && itemStack.func_77962_s()) {
         FontUtils.drawStringWithShadow(ColorMain.customFont.getValue(), "God", x * 2, y, new PandoraColor(195, 77, 65));
      }

      GlStateManager.func_179090_x();
   }

   private String stringForEnchants(Enchantment enchantment, int n) {
      ResourceLocation resourceLocation;
      String substring = (resourceLocation = (ResourceLocation)Enchantment.field_185264_b.func_177774_c(enchantment)) == null ? enchantment.func_77320_a() : resourceLocation.toString();
      int n2 = n > 1 ? 12 : 13;
      if (substring.length() > n2) {
         substring = substring.substring(10, n2);
      }

      StringBuilder sb = new StringBuilder();
      int n3 = false;
      String s2 = sb.insert(0, substring.substring(0, 1).toUpperCase()).append(substring.substring(1)).toString();
      if (n > 1) {
         s2 = (new StringBuilder()).insert(0, s2).append(n).toString();
      }

      return s2;
   }

   private void renderItemName(ItemStack itemStack, int x, int y) {
      GlStateManager.func_179098_w();
      GlStateManager.func_179094_E();
      GlStateManager.func_179139_a(0.5D, 0.5D, 0.5D);
      FontUtils.drawStringWithShadow(ColorMain.customFont.getValue(), itemStack.func_82833_r(), -FontUtils.getStringWidth(ColorMain.customFont.getValue(), itemStack.func_82833_r()) / 2, y, new PandoraColor(255, 255, 255));
      GlStateManager.func_179121_F();
      GlStateManager.func_179090_x();
   }

   private void renderItemDurability(ItemStack itemStack, int x, int y) {
      int maxDamage = itemStack.func_77958_k();
      float n3 = (float)(maxDamage - itemStack.func_77952_i()) / (float)maxDamage;
      float green = ((float)itemStack.func_77958_k() - (float)itemStack.func_77952_i()) / (float)itemStack.func_77958_k();
      if (green > 1.0F) {
         green = 1.0F;
      } else if (green < 0.0F) {
         green = 0.0F;
      }

      float red = 1.0F - green;
      GlStateManager.func_179098_w();
      GlStateManager.func_179094_E();
      GlStateManager.func_179139_a(0.5D, 0.5D, 0.5D);
      FontUtils.drawStringWithShadow(ColorMain.customFont.getValue(), (new StringBuilder()).insert(0, (int)(n3 * 100.0F)).append('%').toString(), x * 2, y, new PandoraColor((int)(red * 255.0F), (int)(green * 255.0F), 0));
      GlStateManager.func_179121_F();
      GlStateManager.func_179090_x();
   }

   private void renderItems(ItemStack itemStack, int n, int n2, int n3) {
      GlStateManager.func_179098_w();
      GlStateManager.func_179132_a(true);
      GlStateManager.func_179086_m(256);
      GlStateManager.func_179126_j();
      GlStateManager.func_179118_c();
      int n4 = n3 > 4 ? (n3 - 4) * 8 / 2 : 0;
      mc.func_175599_af().field_77023_b = -150.0F;
      RenderHelper.func_74519_b();
      mc.func_175599_af().func_180450_b(itemStack, n, n2 + n4);
      mc.func_175599_af().func_175030_a(mc.field_71466_p, itemStack, n, n2 + n4);
      RenderHelper.func_74518_a();
      mc.func_175599_af().field_77023_b = 0.0F;
      PandoraTessellator.prepare();
      GlStateManager.func_179094_E();
      GlStateManager.func_179139_a(0.5D, 0.5D, 0.5D);
      this.renderEnchants(itemStack, n, n2 - 24);
      GlStateManager.func_179121_F();
   }

   private void renderNametags(EntityPlayer entityPlayer, double n, double distance, double n2) {
      double tempY = distance + (entityPlayer.func_70093_af() ? 0.5D : 0.7D);
      Object var10000 = mc.func_175606_aa() == null ? mc.field_71439_g : mc.func_175606_aa();
      Entity entity2 = var10000;
      Entity entity = var10000;
      double posX = ((Entity)entity2).field_70165_t;
      double posY = ((Entity)entity2).field_70163_u;
      double posZ = ((Entity)entity2).field_70161_v;
      Vec3d m;
      ((Entity)entity2).field_70165_t = (m = renderPosEntity((Entity)entity2)).field_72450_a;
      ((Entity)entity2).field_70163_u = m.field_72448_b;
      ((Entity)entity2).field_70161_v = m.field_72449_c;
      ((Entity)entity).func_70011_f(n, distance, n2);
      String[] text = new String[]{this.renderEntityName(entityPlayer)};
      PandoraTessellator.drawNametag(n, tempY + 1.4D, n2, text, this.renderPing(entityPlayer), 2);
      ItemStack heldItemMainhand = entityPlayer.func_184614_ca();
      ItemStack heldItemOffhand = entityPlayer.func_184592_cb();
      int n10 = 0;
      int n11 = 0;
      boolean b = false;
      int i = 3;

      int m3;
      for(int n12 = 3; i >= 0; i = n12) {
         ItemStack itemStack;
         if (!(itemStack = (ItemStack)entityPlayer.field_71071_by.field_70460_b.get(n12)).func_190926_b()) {
            Boolean j = this.durability.getValue();
            n10 -= 8;
            if (j) {
               b = true;
            }

            if (this.armor.getValue() && (m3 = EnchantmentHelper.func_82781_a(itemStack).size()) > n11) {
               n11 = m3;
            }
         }

         --n12;
      }

      int l;
      if (!heldItemOffhand.func_190926_b() && (this.armor.getValue() || this.durability.getValue() && heldItemOffhand.func_77984_f())) {
         n10 -= 8;
         if (this.durability.getValue() && heldItemOffhand.func_77984_f()) {
            b = true;
         }

         if (this.armor.getValue() && (l = EnchantmentHelper.func_82781_a(heldItemOffhand).size()) > n11) {
            n11 = l;
         }
      }

      int k;
      if (!heldItemMainhand.func_190926_b()) {
         if (this.armor.getValue() && (l = EnchantmentHelper.func_82781_a(heldItemMainhand).size()) > n11) {
            n11 = l;
         }

         k = this.armorValue(n11);
         if (this.armor.getValue() || this.durability.getValue() && heldItemMainhand.func_77984_f()) {
            n10 -= 8;
         }

         if (this.armor.getValue()) {
            int n14 = k;
            k -= 32;
            this.renderItems(heldItemMainhand, n10, n14, n11);
         }

         Nametags nametags;
         if (this.durability.getValue() && heldItemMainhand.func_77984_f()) {
            this.renderItemDurability(heldItemMainhand, n10, k);
            k -= ColorMain.customFont.getValue() ? FontUtils.getFontHeight(ColorMain.customFont.getValue()) : mc.field_71466_p.field_78288_b;
            nametags = this;
         } else {
            if (b) {
               k -= ColorMain.customFont.getValue() ? FontUtils.getFontHeight(ColorMain.customFont.getValue()) : mc.field_71466_p.field_78288_b;
            }

            nametags = this;
         }

         if (nametags.itemName.getValue()) {
            this.renderItemName(heldItemMainhand, n10, k);
         }

         if (this.armor.getValue() || this.durability.getValue() && heldItemMainhand.func_77984_f()) {
            n10 += 16;
         }
      }

      l = 3;

      for(k = 3; l >= 0; l = k) {
         ItemStack itemStack3;
         if (!(itemStack3 = (ItemStack)entityPlayer.field_71071_by.field_70460_b.get(k)).func_190926_b()) {
            int m2 = this.armorValue(n11);
            if (this.armor.getValue()) {
               int n18 = m2;
               m2 -= 32;
               this.renderItems(itemStack3, n10, n18, n11);
            }

            if (this.durability.getValue() && itemStack3.func_77984_f()) {
               this.renderItemDurability(itemStack3, n10, m2);
            }

            n10 += 16;
         }

         --k;
      }

      if (!heldItemOffhand.func_190926_b()) {
         m3 = this.armorValue(n11);
         if (this.armor.getValue()) {
            int n20 = m3;
            m3 -= 32;
            this.renderItems(heldItemOffhand, n10, n20, n11);
         }

         if (this.durability.getValue() && heldItemOffhand.func_77984_f()) {
            this.renderItemDurability(heldItemOffhand, n10, m3);
         }

         n10 += 16;
      }

      GlStateManager.func_179121_F();
      ((Entity)entity).field_70165_t = posX;
      ((Entity)entity).field_70163_u = posY;
      ((Entity)entity).field_70161_v = posZ;
   }

   private PandoraColor renderPing(EntityPlayer entityPlayer) {
      if (Friends.isFriend(entityPlayer.func_70005_c_())) {
         return ColorMain.getFriendGSColor();
      } else if (Enemies.isEnemy(entityPlayer.func_70005_c_())) {
         return ColorMain.getEnemyGSColor();
      } else if (entityPlayer.func_82150_aj()) {
         return new PandoraColor(128, 128, 128);
      } else if (mc.func_147114_u() != null && mc.func_147114_u().func_175102_a(entityPlayer.func_110124_au()) == null) {
         return new PandoraColor(239, 1, 71);
      } else {
         return entityPlayer.func_70093_af() ? new PandoraColor(255, 153, 0) : new PandoraColor(255, 255, 255);
      }
   }

   private String renderEntityName(EntityPlayer entityPlayer) {
      String s = entityPlayer.func_145748_c_().func_150254_d();
      if (this.entityId.getValue()) {
         s = (new StringBuilder()).insert(0, s).append(" ID: ").append(entityPlayer.func_145782_y()).toString();
      }

      if (this.gamemode.getValue()) {
         if (entityPlayer.func_184812_l_()) {
            s = (new StringBuilder()).insert(0, s).append(" [C]").toString();
         } else if (entityPlayer.func_175149_v()) {
            s = (new StringBuilder()).insert(0, s).append(" [I]").toString();
         } else {
            s = (new StringBuilder()).insert(0, s).append(" [S]").toString();
         }
      }

      if (this.ping.getValue() && mc.func_147114_u() != null && mc.func_147114_u().func_175102_a(entityPlayer.func_110124_au()) != null) {
         s = (new StringBuilder()).insert(0, s).append(" ").append(mc.func_147114_u().func_175102_a(entityPlayer.func_110124_au()).func_178853_c()).append("ms").toString();
      }

      if (!this.health.getValue()) {
         return s;
      } else {
         String s2 = TextFormatting.GREEN.toString();
         double ceil;
         if ((ceil = Math.ceil((double)(entityPlayer.func_110143_aJ() + entityPlayer.func_110139_bj()))) > 0.0D) {
            if (entityPlayer.func_110143_aJ() + entityPlayer.func_110139_bj() <= 5.0F) {
               s2 = TextFormatting.RED.toString();
            } else if (entityPlayer.func_110143_aJ() + entityPlayer.func_110139_bj() > 5.0F && entityPlayer.func_110143_aJ() + entityPlayer.func_110139_bj() <= 10.0F) {
               s2 = TextFormatting.GOLD.toString();
            } else if (entityPlayer.func_110143_aJ() + entityPlayer.func_110139_bj() > 10.0F && entityPlayer.func_110143_aJ() + entityPlayer.func_110139_bj() <= 15.0F) {
               s2 = TextFormatting.YELLOW.toString();
            } else if (entityPlayer.func_110143_aJ() + entityPlayer.func_110139_bj() > 15.0F && entityPlayer.func_110143_aJ() + entityPlayer.func_110139_bj() <= 20.0F) {
               s2 = TextFormatting.DARK_GREEN.toString();
            } else if (entityPlayer.func_110143_aJ() + entityPlayer.func_110139_bj() > 20.0F) {
               s2 = TextFormatting.GREEN.toString();
            }
         } else {
            s2 = TextFormatting.DARK_RED.toString();
         }

         return (new StringBuilder()).insert(0, s).append(s2).append(" ").append(ceil > 0.0D ? (int)ceil : "0").toString();
      }
   }

   private int armorValue(int n) {
      int n2 = this.armor.getValue() ? -26 : -27;
      if (n > 4) {
         n2 -= (n - 4) * 8;
      }

      return n2;
   }
}
