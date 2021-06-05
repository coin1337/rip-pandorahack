package com.pandora.api.event.events;

import com.pandora.api.event.PandoraEvent;
import net.minecraft.util.EnumHandSide;

public class TransformSideFirstPersonEvent extends PandoraEvent {
   private final EnumHandSide handSide;

   public TransformSideFirstPersonEvent(EnumHandSide handSide) {
      this.handSide = handSide;
   }

   public EnumHandSide getHandSide() {
      return this.handSide;
   }
}
