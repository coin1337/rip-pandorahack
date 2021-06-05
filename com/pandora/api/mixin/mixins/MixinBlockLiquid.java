package com.pandora.api.mixin.mixins;

import com.pandora.client.module.ModuleManager;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({BlockLiquid.class})
public class MixinBlockLiquid {
   @Inject(
      method = {"canCollideCheck"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void canCollideCheck(IBlockState blockState, boolean b, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
      callbackInfoReturnable.setReturnValue(ModuleManager.isModuleEnabled("LiquidInteract") || b && (Integer)blockState.func_177229_b(BlockLiquid.field_176367_b) == 0);
   }
}
