package com.pandora.client.module.modules.hud;

import com.pandora.api.settings.Setting;
import com.pandora.api.util.players.enemy.Enemies;
import com.pandora.api.util.players.friends.Friends;
import com.pandora.client.module.modules.gui.ColorMain;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;

public class TextRadar extends ListModule {
   private static Setting.Boolean sortUp;
   private static Setting.Boolean sortRight;
   private static Setting.Integer range;
   private static Setting.Mode display;
   private static TextRadar.PlayerList list = new TextRadar.PlayerList();

   public TextRadar() {
      super(new ListModule.ListComponent("TextRadar", new Point(0, 50), list), new Point(0, 50));
   }

   public void setup() {
      ArrayList<String> displayModes = new ArrayList();
      displayModes.add("All");
      displayModes.add("Friend");
      displayModes.add("Enemy");
      display = this.registerMode("Display", "Display", displayModes, "All");
      sortUp = this.registerBoolean("Sort Up", "SortUp", false);
      sortRight = this.registerBoolean("Sort Right", "SortRight", false);
      range = this.registerInteger("Range", "Range", 100, 1, 260);
   }

   public void onRender() {
      list.players.clear();
      mc.field_71441_e.field_72996_f.stream().filter((e) -> {
         return e instanceof EntityPlayer;
      }).filter((e) -> {
         return e != mc.field_71439_g;
      }).forEach((e) -> {
         if (!(mc.field_71439_g.func_70032_d(e) > (float)range.getValue())) {
            if (!display.getValue().equalsIgnoreCase("Friend") || Friends.isFriend(e.func_70005_c_())) {
               if (!display.getValue().equalsIgnoreCase("Enemy") || Enemies.isEnemy(e.func_70005_c_())) {
                  list.players.add((EntityPlayer)e);
               }
            }
         }
      });
   }

   private static class PlayerList implements ListModule.HUDList {
      public List<EntityPlayer> players;

      private PlayerList() {
         this.players = new ArrayList();
      }

      public int getSize() {
         return this.players.size();
      }

      public String getItem(int index) {
         EntityPlayer e = (EntityPlayer)this.players.get(index);
         TextFormatting friendcolor;
         if (Friends.isFriend(e.func_70005_c_())) {
            friendcolor = ColorMain.getFriendColor();
         } else if (Enemies.isEnemy(e.func_70005_c_())) {
            friendcolor = ColorMain.getEnemyColor();
         } else {
            friendcolor = TextFormatting.GRAY;
         }

         float health = e.func_110143_aJ() + e.func_110139_bj();
         TextFormatting healthcolor;
         if (health <= 5.0F) {
            healthcolor = TextFormatting.RED;
         } else if (health > 5.0F && health < 15.0F) {
            healthcolor = TextFormatting.YELLOW;
         } else {
            healthcolor = TextFormatting.GREEN;
         }

         float distance = TextRadar.mc.field_71439_g.func_70032_d(e);
         TextFormatting distancecolor;
         if (distance < 20.0F) {
            distancecolor = TextFormatting.RED;
         } else if (distance >= 20.0F && distance < 50.0F) {
            distancecolor = TextFormatting.YELLOW;
         } else {
            distancecolor = TextFormatting.GREEN;
         }

         return TextFormatting.GRAY + "[" + healthcolor + (int)health + TextFormatting.GRAY + "] " + friendcolor + e.func_70005_c_() + TextFormatting.GRAY + " [" + distancecolor + (int)distance + TextFormatting.GRAY + "]";
      }

      public Color getItemColor(int index) {
         return new Color(255, 255, 255);
      }

      public boolean sortUp() {
         return TextRadar.sortUp.isOn();
      }

      public boolean sortRight() {
         return TextRadar.sortRight.isOn();
      }

      // $FF: synthetic method
      PlayerList(Object x0) {
         this();
      }
   }
}
