package com.lukflug.panelstudio.tabgui;

import com.lukflug.panelstudio.Context;
import com.lukflug.panelstudio.theme.ColorScheme;

public interface TabGUIRenderer {
   int getHeight();

   int getBorder();

   void renderBackground(Context var1, int var2, int var3);

   void renderCaption(Context var1, String var2, int var3, int var4, boolean var5);

   ColorScheme getColorScheme();

   boolean isUpKey(int var1);

   boolean isDownKey(int var1);

   boolean isSelectKey(int var1);

   boolean isEscapeKey(int var1);
}
