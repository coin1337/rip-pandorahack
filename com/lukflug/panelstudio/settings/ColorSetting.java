package com.lukflug.panelstudio.settings;

import java.awt.Color;

public interface ColorSetting {
   Color getValue();

   void setValue(Color var1);

   Color getColor();

   boolean getRainbow();

   void setRainbow(boolean var1);
}
