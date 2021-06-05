package com.pandora.client.command.commands;

import com.pandora.api.util.misc.MessageBus;
import com.pandora.api.util.players.enemy.Enemies;
import com.pandora.client.command.Command;

public class EnemyCommand extends Command {
   public EnemyCommand() {
      super("Enemy");
      this.setCommandSyntax(Command.getCommandPrefix() + "enemy add/del [player]");
      this.setCommandAlias(new String[]{"enemy", "enemies", "e"});
   }

   public void onCommand(String command, String[] message) throws Exception {
      String main = message[0];
      String value = message[1];
      if (main.equalsIgnoreCase("add") && !Enemies.isEnemy(value)) {
         Enemies.addEnemy(value);
         MessageBus.sendClientPrefixMessage("Added enemy: " + value.toUpperCase() + "!");
      } else if (main.equalsIgnoreCase("del") && Enemies.isEnemy(value)) {
         Enemies.delEnemy(value);
         MessageBus.sendClientPrefixMessage("Deleted enemy: " + value.toUpperCase() + "!");
      }

   }
}
