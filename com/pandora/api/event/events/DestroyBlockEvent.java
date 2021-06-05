package com.pandora.api.event.events;

import com.pandora.api.event.PandoraEvent;
import net.minecraft.util.math.BlockPos;

public class DestroyBlockEvent extends PandoraEvent {
   BlockPos pos;

   public DestroyBlockEvent(BlockPos blockPos) {
      this.pos = blockPos;
   }

   public BlockPos getBlockPos() {
      return this.pos;
   }

   public void setPos(BlockPos pos) {
      this.pos = pos;
   }
}
