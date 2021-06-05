package com.pandora.api.util.font;

import com.pandora.api.util.render.PandoraColor;
import com.pandora.client.PandoraMod;
import net.minecraft.client.Minecraft;

public class FontUtils {
   private static final Minecraft mc = Minecraft.func_71410_x();

   public static float drawStringWithShadow(boolean customFont, String text, int x, int y, PandoraColor color) {
      return customFont ? PandoraMod.fontRenderer.drawStringWithShadow(text, (double)x, (double)y, color) : (float)mc.field_71466_p.func_175063_a(text, (float)x, (float)y, color.getRGB());
   }

   public static int getStringWidth(boolean customFont, String str) {
      return customFont ? PandoraMod.fontRenderer.getStringWidth(str) : mc.field_71466_p.func_78256_a(str);
   }

   public static int getFontHeight(boolean customFont) {
      return customFont ? PandoraMod.fontRenderer.getHeight() : mc.field_71466_p.field_78288_b;
   }
}
