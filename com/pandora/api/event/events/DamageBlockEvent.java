package com.pandora.api.event.events;

import com.pandora.api.event.PandoraEvent;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class DamageBlockEvent extends PandoraEvent {
   private BlockPos pos;
   private EnumFacing face;

   public DamageBlockEvent(BlockPos pos, EnumFacing face) {
      this.pos = pos;
      this.face = face;
   }

   public BlockPos getPos() {
      return this.pos;
   }

   public void setPos(BlockPos pos) {
      this.pos = pos;
   }

   public EnumFacing getFace() {
      return this.face;
   }

   public void setFace(EnumFacing face) {
      this.face = face;
   }
}
