package com.lukflug.panelstudio;

public interface Component {
   String getTitle();

   void render(Context var1);

   void handleButton(Context var1, int var2);

   void handleKey(Context var1, int var2);

   void handleScroll(Context var1, int var2);

   void getHeight(Context var1);

   void exit(Context var1);

   void releaseFocus();
}
