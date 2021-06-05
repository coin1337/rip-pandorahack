package com.pandora.client.module.modules.hud;

import com.pandora.api.settings.Setting;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.text.TextComponentString;

public class Notifications extends ListModule {
   private static Setting.Boolean sortUp;
   private static Setting.Boolean sortRight;
   public static Setting.Boolean disableChat;
   private static Notifications.NotificationsList list = new Notifications.NotificationsList();
   private static int waitCounter;

   public Notifications() {
      super(new ListModule.ListComponent("Notifications", new Point(0, 50), list), new Point(0, 50));
   }

   public void setup() {
      sortUp = this.registerBoolean("Sort Up", "SortUp", false);
      sortRight = this.registerBoolean("Sort Right", "SortRight", false);
      disableChat = this.registerBoolean("No Chat Msg", "NoChatMsg", true);
   }

   public void onUpdate() {
      if (waitCounter < 500) {
         ++waitCounter;
      } else {
         waitCounter = 0;
         if (list.list.size() > 0) {
            list.list.remove(0);
         }

      }
   }

   public static void addMessage(TextComponentString m) {
      if (list.list.size() < 3) {
         list.list.remove(m);
         list.list.add(m);
      } else {
         list.list.remove(0);
         list.list.remove(m);
         list.list.add(m);
      }

   }

   private static class NotificationsList implements ListModule.HUDList {
      public List<TextComponentString> list;

      private NotificationsList() {
         this.list = new ArrayList();
      }

      public int getSize() {
         return this.list.size();
      }

      public String getItem(int index) {
         return ((TextComponentString)this.list.get(index)).func_150265_g();
      }

      public Color getItemColor(int index) {
         return new Color(255, 255, 255);
      }

      public boolean sortUp() {
         return Notifications.sortUp.isOn();
      }

      public boolean sortRight() {
         return Notifications.sortRight.isOn();
      }

      // $FF: synthetic method
      NotificationsList(Object x0) {
         this();
      }
   }
}
