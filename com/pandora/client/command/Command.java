package com.pandora.client.command;

import net.minecraft.client.Minecraft;

public abstract class Command {
   protected static final Minecraft mc = Minecraft.func_71410_x();
   public static String commandPrefix = "-";
   String commandName;
   String[] commandAlias;
   String commandSyntax;

   public Command(String commandName) {
      this.commandName = commandName;
   }

   public static String getCommandPrefix() {
      return commandPrefix;
   }

   public String getCommandName() {
      return this.commandName;
   }

   public String getCommandSyntax() {
      return this.commandSyntax;
   }

   public String[] getCommandAlias() {
      return this.commandAlias;
   }

   public static void setCommandPrefix(String prefix) {
      commandPrefix = prefix;
   }

   public void setCommandSyntax(String syntax) {
      this.commandSyntax = syntax;
   }

   public void setCommandAlias(String[] alias) {
      this.commandAlias = alias;
   }

   public abstract void onCommand(String var1, String[] var2) throws Exception;
}
