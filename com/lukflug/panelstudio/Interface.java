package com.lukflug.panelstudio;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

public interface Interface {
   int LBUTTON = 0;
   int RBUTTON = 1;

   Point getMouse();

   boolean getButton(int var1);

   void drawString(Point var1, String var2, Color var3);

   int getFontWidth(String var1);

   int getFontHeight();

   void fillTriangle(Point var1, Point var2, Point var3, Color var4, Color var5, Color var6);

   void drawLine(Point var1, Point var2, Color var3, Color var4);

   void fillRect(Rectangle var1, Color var2, Color var3, Color var4, Color var5);

   void drawRect(Rectangle var1, Color var2, Color var3, Color var4, Color var5);

   int loadImage(String var1);

   void drawImage(Rectangle var1, int var2, boolean var3, int var4);

   void window(Rectangle var1);

   void restore();
}
