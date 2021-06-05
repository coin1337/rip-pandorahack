package com.pandora.client.command;

import com.pandora.api.util.misc.MessageBus;
import com.pandora.client.command.commands.AutoGGCommand;
import com.pandora.client.command.commands.AutoReplyCommand;
import com.pandora.client.command.commands.BindCommand;
import com.pandora.client.command.commands.CmdListCommand;
import com.pandora.client.command.commands.DisableAllCommand;
import com.pandora.client.command.commands.DrawnCommand;
import com.pandora.client.command.commands.EnemyCommand;
import com.pandora.client.command.commands.FixGUICommand;
import com.pandora.client.command.commands.FixHUDCommand;
import com.pandora.client.command.commands.FontCommand;
import com.pandora.client.command.commands.FriendCommand;
import com.pandora.client.command.commands.ModulesCommand;
import com.pandora.client.command.commands.OpenFolderCommand;
import com.pandora.client.command.commands.PrefixCommand;
import com.pandora.client.command.commands.SetCommand;
import com.pandora.client.command.commands.ToggleCommand;
import java.util.ArrayList;
import java.util.Iterator;

public class CommandManager {
   public static ArrayList<Command> commands = new ArrayList();
   boolean isValidCommand = false;

   public static void registerCommands() {
      addCommand(new AutoGGCommand());
      addCommand(new AutoReplyCommand());
      addCommand(new BindCommand());
      addCommand(new CmdListCommand());
      addCommand(new DisableAllCommand());
      addCommand(new DrawnCommand());
      addCommand(new EnemyCommand());
      addCommand(new FixGUICommand());
      addCommand(new FixHUDCommand());
      addCommand(new FontCommand());
      addCommand(new FriendCommand());
      addCommand(new ModulesCommand());
      addCommand(new OpenFolderCommand());
      addCommand(new PrefixCommand());
      addCommand(new SetCommand());
      addCommand(new ToggleCommand());
   }

   public static void addCommand(Command command) {
      commands.add(command);
   }

   public static ArrayList<Command> getCommands() {
      return commands;
   }

   public static Command getCommandByName(String name) {
      Iterator var1 = commands.iterator();

      Command command;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         command = (Command)var1.next();
      } while(command.getCommandName() != name);

      return command;
   }

   public void callCommand(String input) {
      String[] split = input.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
      String command1 = split[0];
      String args = input.substring(command1.length()).trim();
      this.isValidCommand = false;
      commands.forEach((command) -> {
         String[] var4 = command.getCommandAlias();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String string = var4[var6];
            if (string.equalsIgnoreCase(command1)) {
               this.isValidCommand = true;

               try {
                  command.onCommand(args, args.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));
               } catch (Exception var9) {
                  MessageBus.sendClientPrefixMessage(command.getCommandSyntax());
               }
            }
         }

      });
      if (!this.isValidCommand) {
         MessageBus.sendClientPrefixMessage("Error! Invalid command!");
      }

   }
}
