package com.pandora.api.mixin.mixins;

import com.pandora.api.event.events.GuiScreenDisplayedEvent;
import com.pandora.client.PandoraMod;
import com.pandora.client.module.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
   value = {Minecraft.class},
   priority = 9999
)
public class MixinMinecraft {
   @Shadow
   public EntityPlayerSP field_71439_g;
   @Shadow
   public PlayerControllerMP field_71442_b;

   @Inject(
      method = {"displayGuiScreen"},
      at = {@At("HEAD")}
   )
   private void displayGuiScreen(GuiScreen guiScreenIn, CallbackInfo info) {
      GuiScreenDisplayedEvent screenEvent = new GuiScreenDisplayedEvent(guiScreenIn);
      PandoraMod.EVENT_BUS.post(screenEvent);
   }

   @Redirect(
      method = {"sendClickBlockToController"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/entity/EntityPlayerSP;isHandActive()Z"
)
   )
   private boolean isHandActive(EntityPlayerSP player) {
      return ModuleManager.isModuleEnabled("MultiTask") ? false : this.field_71439_g.func_184587_cr();
   }

   @Redirect(
      method = {"rightClickMouse"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;getIsHittingBlock()Z"
)
   )
   private boolean isHittingBlock(PlayerControllerMP playerControllerMP) {
      return ModuleManager.isModuleEnabled("MultiTask") ? false : this.field_71442_b.func_181040_m();
   }
}
