package com.pandora.client.command.commands;

import com.pandora.api.util.misc.MessageBus;
import com.pandora.api.util.players.friends.Friends;
import com.pandora.client.command.Command;

public class FriendCommand extends Command {
   public FriendCommand() {
      super("Friend");
      this.setCommandSyntax(Command.getCommandPrefix() + "friend add/del [player]");
      this.setCommandAlias(new String[]{"friend", "friends", "f"});
   }

   public void onCommand(String command, String[] message) throws Exception {
      String main = message[0];
      String value = message[1];
      if (main.equalsIgnoreCase("add") && !Friends.isFriend(value)) {
         Friends.addFriend(value);
         MessageBus.sendClientPrefixMessage("Added friend: " + value.toUpperCase() + "!");
      } else if (main.equalsIgnoreCase("del") && Friends.isFriend(value)) {
         Friends.addFriend(value);
         MessageBus.sendClientPrefixMessage("Deleted friend: " + value.toUpperCase() + "!");
      }

   }
}
