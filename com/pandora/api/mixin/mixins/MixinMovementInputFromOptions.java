package com.pandora.api.mixin.mixins;

import com.pandora.client.module.ModuleManager;
import com.pandora.client.module.modules.movement.PlayerTweaks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(
   value = {MovementInputFromOptions.class},
   priority = 10000
)
public abstract class MixinMovementInputFromOptions extends MovementInput {
   @Redirect(
      method = {"updatePlayerMoveState"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z"
)
   )
   public boolean isKeyPressed(KeyBinding keyBinding) {
      return ModuleManager.isModuleEnabled("PlayerTweaks") && ((PlayerTweaks)ModuleManager.getModuleByName("PlayerTweaks")).guiMove.getValue() && Minecraft.func_71410_x().field_71462_r != null && !(Minecraft.func_71410_x().field_71462_r instanceof GuiChat) && Minecraft.func_71410_x().field_71439_g != null ? Keyboard.isKeyDown(keyBinding.func_151463_i()) : keyBinding.func_151470_d();
   }
}
