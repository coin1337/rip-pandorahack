package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.Context;
import java.awt.Color;
import java.awt.Rectangle;

public interface Renderer {
   int getHeight();

   int getOffset();

   int getBorder();

   void renderTitle(Context var1, String var2, boolean var3);

   void renderTitle(Context var1, String var2, boolean var3, boolean var4);

   void renderTitle(Context var1, String var2, boolean var3, boolean var4, boolean var5);

   void renderRect(Context var1, String var2, boolean var3, boolean var4, Rectangle var5, boolean var6);

   void renderBackground(Context var1, boolean var2);

   void renderBorder(Context var1, boolean var2, boolean var3, boolean var4);

   Color getMainColor(boolean var1, boolean var2);

   Color getBackgroundColor(boolean var1);

   Color getFontColor(boolean var1);

   ColorScheme getDefaultColorScheme();

   void overrideColorScheme(ColorScheme var1);

   void restoreColorScheme();

   static Color brighter(Color color) {
      int r = color.getRed();
      int g = color.getGreen();
      int b = color.getBlue();
      r += 64;
      g += 64;
      b += 64;
      if (r > 255) {
         r = 255;
      }

      if (g > 255) {
         g = 255;
      }

      if (b > 255) {
         b = 255;
      }

      return new Color(r, g, b, color.getAlpha());
   }

   static Color darker(Color color) {
      int r = color.getRed();
      int g = color.getGreen();
      int b = color.getBlue();
      r -= 64;
      g -= 64;
      b -= 64;
      if (r < 0) {
         r = 0;
      }

      if (g < 0) {
         g = 0;
      }

      if (b < 0) {
         b = 0;
      }

      return new Color(r, g, b, color.getAlpha());
   }
}
