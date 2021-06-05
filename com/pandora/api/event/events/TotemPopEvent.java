package com.pandora.api.event.events;

import com.pandora.api.event.PandoraEvent;
import net.minecraft.entity.Entity;

public class TotemPopEvent extends PandoraEvent {
   private final Entity entity;

   public TotemPopEvent(Entity entity) {
      this.entity = entity;
   }

   public Entity getEntity() {
      return this.entity;
   }
}
