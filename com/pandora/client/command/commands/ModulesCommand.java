package com.pandora.client.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.pandora.client.command.Command;
import com.pandora.client.module.Module;
import com.pandora.client.module.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.event.HoverEvent.Action;

public class ModulesCommand extends Command {
   public ModulesCommand() {
      super("Modules");
      this.setCommandSyntax(Command.getCommandPrefix() + "modules (click to toggle)");
      this.setCommandAlias(new String[]{"modules", "module", "modulelist", "mod", "mods"});
   }

   public void onCommand(String command, String[] message) throws Exception {
      int size = ModuleManager.getModules().size();
      TextComponentString msg = new TextComponentString("§7Modules: §f ");

      for(int i = 0; i < size; ++i) {
         Module module = (Module)ModuleManager.getModules().get(i);
         if (module != null) {
            msg.func_150257_a((new TextComponentString((module.isEnabled() ? ChatFormatting.GREEN : ChatFormatting.RED) + module.getName() + "§7" + (i == size - 1 ? "" : ", "))).func_150255_a((new Style()).func_150209_a(new HoverEvent(Action.SHOW_TEXT, new TextComponentString(module.getCategory().name()))).func_150241_a(new ClickEvent(net.minecraft.util.text.event.ClickEvent.Action.RUN_COMMAND, Command.getCommandPrefix() + "toggle " + module.getName()))));
         }
      }

      msg.func_150257_a(new TextComponentString(ChatFormatting.GRAY + "!"));
      Minecraft.func_71410_x().field_71456_v.func_146158_b().func_146227_a(msg);
   }
}
