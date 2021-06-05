package com.pandora.api.mixin.mixins;

import com.pandora.api.event.events.TransformSideFirstPersonEvent;
import com.pandora.client.PandoraMod;
import com.pandora.client.module.ModuleManager;
import com.pandora.client.module.modules.render.NoRender;
import com.pandora.client.module.modules.render.ViewModel;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ItemRenderer.class})
public class MixinItemRenderer {
   @Inject(
      method = {"transformSideFirstPerson"},
      at = {@At("HEAD")}
   )
   public void transformSideFirstPerson(EnumHandSide hand, float p_187459_2_, CallbackInfo ci) {
      TransformSideFirstPersonEvent event = new TransformSideFirstPersonEvent(hand);
      PandoraMod.EVENT_BUS.post(event);
   }

   @Inject(
      method = {"transformEatFirstPerson"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void transformEatFirstPerson(float p_187454_1_, EnumHandSide hand, ItemStack stack, CallbackInfo ci) {
      TransformSideFirstPersonEvent event = new TransformSideFirstPersonEvent(hand);
      PandoraMod.EVENT_BUS.post(event);
      if (ModuleManager.isModuleEnabled("ViewModel") && ((ViewModel)ModuleManager.getModuleByName("ViewModel")).cancelEating.getValue()) {
         ci.cancel();
      }

   }

   @Inject(
      method = {"transformFirstPerson"},
      at = {@At("HEAD")}
   )
   public void transformFirstPerson(EnumHandSide hand, float p_187453_2_, CallbackInfo ci) {
      TransformSideFirstPersonEvent event = new TransformSideFirstPersonEvent(hand);
      PandoraMod.EVENT_BUS.post(event);
   }

   @Inject(
      method = {"renderOverlays"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void renderOverlays(float partialTicks, CallbackInfo ci) {
      if (ModuleManager.isModuleEnabled("NoRender") && ((NoRender)ModuleManager.getModuleByName("NoRender")).noOverlay.getValue()) {
         ci.cancel();
      }

   }
}
