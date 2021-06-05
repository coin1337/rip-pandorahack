package com.pandora.api.util.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

public class Wrapper {
   private static FontRenderer fontRenderer;
   public static Minecraft mc = Minecraft.func_71410_x();

   public static Minecraft getMinecraft() {
      return Minecraft.func_71410_x();
   }

   public static EntityPlayerSP getPlayer() {
      return getMinecraft().field_71439_g;
   }

   public static World getWorld() {
      return getMinecraft().field_71441_e;
   }

   public static int getKey(String keyname) {
      return Keyboard.getKeyIndex(keyname.toUpperCase());
   }

   public static FontRenderer getFontRenderer() {
      return fontRenderer;
   }
}
