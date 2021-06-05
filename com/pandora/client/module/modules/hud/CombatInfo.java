package com.pandora.client.module.modules.hud;

import com.pandora.api.settings.Setting;
import com.pandora.api.util.players.friends.Friends;
import com.pandora.api.util.render.PandoraColor;
import com.pandora.client.module.ModuleManager;
import com.pandora.client.module.modules.combat.AutoCrystal;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class CombatInfo extends ListModule {
   private static Setting.Mode infoType;
   private static Setting.ColorSetting color1;
   private static Setting.ColorSetting color2;
   private static CombatInfo.InfoList list = new CombatInfo.InfoList();
   private static final BlockPos[] surroundOffset = new BlockPos[]{new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0)};

   public CombatInfo() {
      super(new ListModule.ListComponent("CombatInfo", new Point(0, 150), list), new Point(0, 150));
   }

   public void setup() {
      ArrayList<String> infoTypes = new ArrayList();
      infoTypes.add("Cyber");
      infoTypes.add("Hoosiers");
      infoType = this.registerMode("Type", "Type", infoTypes, "Hoosiers");
      color1 = this.registerColor("On", "On", new PandoraColor(0, 255, 0, 255));
      color2 = this.registerColor("Off", "Off", new PandoraColor(255, 0, 0, 255));
   }

   public void onRender() {
      list.totems = mc.field_71439_g.field_71071_by.field_70462_a.stream().filter((itemStack) -> {
         return itemStack.func_77973_b() == Items.field_190929_cY;
      }).mapToInt(ItemStack::func_190916_E).sum();
      list.players = (EntityOtherPlayerMP)mc.field_71441_e.field_72996_f.stream().filter((entity) -> {
         return entity instanceof EntityOtherPlayerMP;
      }).filter((entity) -> {
         return !Friends.isFriend(entity.func_70005_c_());
      }).filter((ex) -> {
         return (double)mc.field_71439_g.func_70032_d(ex) <= AutoCrystal.placeRange.getValue();
      }).map((entity) -> {
         return (EntityOtherPlayerMP)entity;
      }).min(Comparator.comparing((cl) -> {
         return mc.field_71439_g.func_70032_d(cl);
      })).orElse((Object)null);
      list.renderLby = false;
      List<EntityPlayer> entities = new ArrayList((Collection)mc.field_71441_e.field_73010_i.stream().filter((entityPlayer) -> {
         return !Friends.isFriend(entityPlayer.func_70005_c_());
      }).collect(Collectors.toList()));
      AutoCrystal a = (AutoCrystal)ModuleManager.getModuleByName("AutocrystalGS");
      Iterator var3 = entities.iterator();

      while(var3.hasNext()) {
         EntityPlayer e = (EntityPlayer)var3.next();
         int i = 0;
         BlockPos[] var6 = surroundOffset;
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            BlockPos add = var6[var8];
            ++i;
            BlockPos o = (new BlockPos(e.func_174791_d().field_72450_a, e.func_174791_d().field_72448_b, e.func_174791_d().field_72449_c)).func_177982_a(add.func_177958_n(), add.func_177956_o(), add.func_177952_p());
            if (mc.field_71441_e.func_180495_p(o).func_177230_c() == Blocks.field_150343_Z) {
               if (i == 1 && a.canPlaceCrystal(o.func_177964_d(1).func_177977_b())) {
                  list.lby = true;
                  list.renderLby = true;
               } else if (i == 2 && a.canPlaceCrystal(o.func_177965_g(1).func_177977_b())) {
                  list.lby = true;
                  list.renderLby = true;
               } else if (i == 3 && a.canPlaceCrystal(o.func_177970_e(1).func_177977_b())) {
                  list.lby = true;
                  list.renderLby = true;
               } else if (i == 4 && a.canPlaceCrystal(o.func_177985_f(1).func_177977_b())) {
                  list.lby = true;
                  list.renderLby = true;
               }
            } else {
               list.lby = false;
               list.renderLby = true;
            }
         }
      }

   }

   private static int getPing() {
      int p = true;
      int p;
      if (mc.field_71439_g != null && mc.func_147114_u() != null && mc.func_147114_u().func_175104_a(mc.field_71439_g.func_70005_c_()) != null) {
         p = mc.func_147114_u().func_175104_a(mc.field_71439_g.func_70005_c_()).func_178853_c();
      } else {
         p = -1;
      }

      return p;
   }

   private static class InfoList implements ListModule.HUDList {
      private static final String[] hoosiersModules = new String[]{"AutoCrystalGS", "KillAura", "Surround", "AutoTrap", "SelfTrap"};
      private static final String[] hoosiersNames = new String[]{"AC", "KA", "SU", "AT", "ST"};
      public int totems;
      public EntityOtherPlayerMP players;
      public boolean renderLby;
      public boolean lby;

      private InfoList() {
         this.totems = 0;
         this.players = null;
         this.renderLby = false;
         this.lby = false;
      }

      public int getSize() {
         if (CombatInfo.infoType.getValue().equals("Hoosiers")) {
            return hoosiersModules.length;
         } else if (CombatInfo.infoType.getValue().equals("Cyber")) {
            return this.renderLby ? 6 : 5;
         } else {
            return 0;
         }
      }

      public String getItem(int index) {
         if (CombatInfo.infoType.getValue().equals("Hoosiers")) {
            return ModuleManager.isModuleEnabled(hoosiersModules[index]) ? hoosiersNames[index] + ": ENBL" : hoosiersNames[index] + ": DSBL";
         } else if (CombatInfo.infoType.getValue().equals("Cyber")) {
            if (index == 0) {
               return "pandora.cc";
            } else if (index == 1) {
               return "HTR";
            } else if (index == 2) {
               return "PLR";
            } else if (index == 3) {
               return "" + this.totems;
            } else {
               return index == 4 ? "PING " + CombatInfo.getPing() : "LBY";
            }
         } else {
            return "";
         }
      }

      public Color getItemColor(int index) {
         if (CombatInfo.infoType.getValue().equals("Hoosiers")) {
            return ModuleManager.isModuleEnabled(hoosiersModules[index]) ? CombatInfo.color1.getValue() : CombatInfo.color2.getValue();
         } else if (!CombatInfo.infoType.getValue().equals("Cyber")) {
            return new Color(255, 255, 255);
         } else {
            boolean on = false;
            if (index == 0) {
               on = true;
            } else if (index == 1) {
               if (this.players != null) {
                  on = (double)CombatInfo.mc.field_71439_g.func_70032_d(this.players) <= AutoCrystal.breakRange.getValue();
               }
            } else if (index == 2) {
               if (this.players != null) {
                  on = (double)CombatInfo.mc.field_71439_g.func_70032_d(this.players) <= AutoCrystal.placeRange.getValue();
               }
            } else if (index == 3) {
               on = this.totems > 0 && ModuleManager.isModuleEnabled("AutoTotem");
            } else if (index == 4) {
               on = CombatInfo.getPing() <= 100;
            } else {
               on = this.lby;
            }

            return on ? CombatInfo.color1.getValue() : CombatInfo.color2.getValue();
         }
      }

      public boolean sortUp() {
         return false;
      }

      public boolean sortRight() {
         return false;
      }

      // $FF: synthetic method
      InfoList(Object x0) {
         this();
      }
   }
}
