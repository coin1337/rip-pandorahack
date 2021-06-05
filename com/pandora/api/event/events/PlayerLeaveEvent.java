package com.pandora.api.event.events;

import com.pandora.api.event.PandoraEvent;

public class PlayerLeaveEvent extends PandoraEvent {
   private final String name;

   public PlayerLeaveEvent(String n) {
      this.name = n;
   }

   public String getName() {
      return this.name;
   }
}
