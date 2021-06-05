package com.pandora.api.mixin.mixins;

import com.mojang.authlib.GameProfile;
import com.pandora.api.event.events.PlayerMoveEvent;
import com.pandora.client.PandoraMod;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.MoverType;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({EntityPlayerSP.class})
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer {
   public MixinEntityPlayerSP() {
      super((World)null, (GameProfile)null);
   }

   @Redirect(
      method = {"move"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/entity/AbstractClientPlayer;move(Lnet/minecraft/entity/MoverType;DDD)V"
)
   )
   public void move(AbstractClientPlayer player, MoverType type, double x, double y, double z) {
      PlayerMoveEvent moveEvent = new PlayerMoveEvent(type, x, y, z);
      PandoraMod.EVENT_BUS.post(moveEvent);
      if (moveEvent.isCancelled()) {
      }

      super.func_70091_d(type, moveEvent.x, moveEvent.y, moveEvent.z);
   }
}
