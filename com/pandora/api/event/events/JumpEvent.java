package com.pandora.api.event.events;

import com.pandora.api.event.PandoraEvent;
import com.pandora.api.util.world.Location;

public class JumpEvent extends PandoraEvent {
   private Location location;

   public JumpEvent(Location location) {
      this.location = location;
   }

   public Location getLocation() {
      return this.location;
   }

   public void setLocation(Location location) {
      this.location = location;
   }
}
