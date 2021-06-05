package com.pandora.api.event.events;

import com.pandora.api.event.PandoraEvent;
import net.minecraft.entity.MoverType;

public class PlayerMoveEvent extends PandoraEvent {
   MoverType type;
   public double x;
   public double y;
   public double z;

   public PlayerMoveEvent(MoverType moverType, double xx, double yy, double zz) {
      this.type = moverType;
      this.x = xx;
      this.y = yy;
      this.z = zz;
   }

   public MoverType getType() {
      return this.type;
   }

   public void setType(MoverType type) {
      this.type = type;
   }

   public double getX() {
      return this.x;
   }

   public double getY() {
      return this.y;
   }

   public double getZ() {
      return this.z;
   }

   public void setX(double xx) {
      this.x = xx;
   }

   public void setY(double yy) {
      this.y = yy;
   }

   public void setZ(double zz) {
      this.z = zz;
   }
}
