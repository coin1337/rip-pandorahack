package com.pandora.api.event.events;

import com.pandora.api.event.PandoraEvent;
import net.minecraft.client.gui.GuiScreen;

public class GuiScreenDisplayedEvent extends PandoraEvent {
   private final GuiScreen guiScreen;

   public GuiScreenDisplayedEvent(GuiScreen screen) {
      this.guiScreen = screen;
   }

   public GuiScreen getScreen() {
      return this.guiScreen;
   }
}
