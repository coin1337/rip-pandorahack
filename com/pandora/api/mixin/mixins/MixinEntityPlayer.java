package com.pandora.api.mixin.mixins;

import com.pandora.api.event.events.PlayerJumpEvent;
import com.pandora.api.event.events.WaterPushEvent;
import com.pandora.client.PandoraMod;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({EntityPlayer.class})
public abstract class MixinEntityPlayer {
   @Shadow
   public abstract String func_70005_c_();

   @Inject(
      method = {"jump"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onJump(CallbackInfo ci) {
      if (Minecraft.func_71410_x().field_71439_g.func_70005_c_() == this.func_70005_c_()) {
         PandoraMod.EVENT_BUS.post(new PlayerJumpEvent());
      }

   }

   @Inject(
      method = {"isPushedByWater"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onPushedByWater(CallbackInfoReturnable<Boolean> cir) {
      WaterPushEvent event = new WaterPushEvent();
      PandoraMod.EVENT_BUS.post(event);
      if (event.isCancelled()) {
         cir.setReturnValue(false);
      }

   }
}
