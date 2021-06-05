package com.pandora.api.mixin;

import com.pandora.client.PandoraMod;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

public class PandoraMixinLoader implements IFMLLoadingPlugin {
   private static boolean isObfuscatedEnvironment = false;

   public PandoraMixinLoader() {
      PandoraMod.log.info("Pandora mixins initialized");
      MixinBootstrap.init();
      Mixins.addConfiguration("mixins.pandora.json");
      MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
      PandoraMod.log.info(MixinEnvironment.getDefaultEnvironment().getObfuscationContext());
   }

   public String[] getASMTransformerClass() {
      return new String[0];
   }

   public String getModContainerClass() {
      return null;
   }

   @Nullable
   public String getSetupClass() {
      return null;
   }

   public void injectData(Map<String, Object> data) {
      isObfuscatedEnvironment = (Boolean)data.get("runtimeDeobfuscationEnabled");
   }

   public String getAccessTransformerClass() {
      return null;
   }
}
