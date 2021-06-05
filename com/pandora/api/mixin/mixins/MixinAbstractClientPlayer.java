package com.pandora.api.mixin.mixins;

import com.pandora.client.PandoraMod;
import com.pandora.client.module.ModuleManager;
import com.pandora.client.module.modules.render.CapesModule;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({AbstractClientPlayer.class})
public abstract class MixinAbstractClientPlayer {
   @Shadow
   @Nullable
   protected abstract NetworkPlayerInfo func_175155_b();

   @Inject(
      method = {"getLocationCape"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getLocationCape(CallbackInfoReturnable<ResourceLocation> cir) {
      UUID uuid = this.func_175155_b().func_178845_a().getId();
      CapesModule capesModule = (CapesModule)ModuleManager.getModuleByName("Capes");
      if (ModuleManager.isModuleEnabled("Capes") && PandoraMod.getInstance().capeUtils.hasCape(uuid)) {
         if (capesModule.capeMode.getValue().equalsIgnoreCase("Black")) {
            cir.setReturnValue(new ResourceLocation("pandora:capeblack.png"));
         } else {
            cir.setReturnValue(new ResourceLocation("pandora:capewhite.png"));
         }
      }

   }
}
