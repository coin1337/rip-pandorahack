package com.pandora.api.util.players.friends;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Friends {
   public static List<Friend> friends;

   public Friends() {
      friends = new ArrayList();
   }

   public static List<Friend> getFriends() {
      return friends;
   }

   public static boolean isFriend(String name) {
      boolean b = false;
      Iterator var2 = getFriends().iterator();

      while(var2.hasNext()) {
         Friend f = (Friend)var2.next();
         if (f.getName().equalsIgnoreCase(name)) {
            b = true;
         }
      }

      return b;
   }

   public static Friend getFriendByName(String name) {
      Friend fr = null;
      Iterator var2 = getFriends().iterator();

      while(var2.hasNext()) {
         Friend f = (Friend)var2.next();
         if (f.getName().equalsIgnoreCase(name)) {
            fr = f;
         }
      }

      return fr;
   }

   public static void addFriend(String name) {
      friends.add(new Friend(name));
   }

   public static void delFriend(String name) {
      friends.remove(getFriendByName(name));
   }
}
