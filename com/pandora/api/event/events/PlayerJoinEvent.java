package com.pandora.api.event.events;

import com.pandora.api.event.PandoraEvent;

public class PlayerJoinEvent extends PandoraEvent {
   private final String name;

   public PlayerJoinEvent(String n) {
      this.name = n;
   }

   public String getName() {
      return this.name;
   }
}
