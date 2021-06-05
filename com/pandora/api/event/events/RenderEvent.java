package com.pandora.api.event.events;

import com.pandora.api.event.PandoraEvent;

public class RenderEvent extends PandoraEvent {
   private final float partialTicks;

   public RenderEvent(float ticks) {
      this.partialTicks = ticks;
   }

   public float getPartialTicks() {
      return this.partialTicks;
   }
}
