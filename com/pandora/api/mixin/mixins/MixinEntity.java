package com.pandora.api.mixin.mixins;

import com.pandora.client.module.ModuleManager;
import com.pandora.client.module.modules.movement.PlayerTweaks;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({Entity.class})
public class MixinEntity {
   @Redirect(
      method = {"applyEntityCollision"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"
)
   )
   public void velocity(Entity entity, double x, double y, double z) {
      if (!((PlayerTweaks)ModuleManager.getModuleByName("PlayerTweaks")).noPush.getValue()) {
         entity.field_70159_w += x;
         entity.field_70181_x += y;
         entity.field_70179_y += z;
         entity.field_70160_al = true;
      }

   }
}
