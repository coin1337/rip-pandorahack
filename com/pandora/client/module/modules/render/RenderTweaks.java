package com.pandora.client.module.modules.render;

import com.pandora.api.settings.Setting;
import com.pandora.client.module.Module;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemSword;

public class RenderTweaks extends Module {
   public Setting.Boolean viewClip;
   Setting.Boolean nekoAnimation;
   Setting.Boolean lowOffhand;
   Setting.Boolean fovChanger;
   Setting.Double lowOffhandSlider;
   Setting.Integer fovChangerSlider;
   ItemRenderer itemRenderer;
   private float oldFOV;

   public RenderTweaks() {
      super("RenderTweaks", Module.Category.Render);
      this.itemRenderer = mc.field_71460_t.field_78516_c;
   }

   public void setup() {
      this.viewClip = this.registerBoolean("View Clip", "ViewClip", false);
      this.nekoAnimation = this.registerBoolean("Neko Animation", "NekoAnimation", false);
      this.lowOffhand = this.registerBoolean("Low Offhand", "LowOffhand", false);
      this.lowOffhandSlider = this.registerDouble("Offhand Height", "OffhandHeight", 1.0D, 0.1D, 1.0D);
      this.fovChanger = this.registerBoolean("FOV", "FOV", false);
      this.fovChangerSlider = this.registerInteger("FOV Slider", "FOVSlider", 90, 70, 200);
   }

   public void onUpdate() {
      if (this.nekoAnimation.getValue() && mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemSword && (double)mc.field_71460_t.field_78516_c.field_187470_g >= 0.9D) {
         mc.field_71460_t.field_78516_c.field_187469_f = 1.0F;
         mc.field_71460_t.field_78516_c.field_187467_d = mc.field_71439_g.func_184614_ca();
      }

      if (this.lowOffhand.getValue()) {
         this.itemRenderer.field_187471_h = (float)this.lowOffhandSlider.getValue();
      }

      if (this.fovChanger.getValue()) {
         mc.field_71474_y.field_74334_X = (float)this.fovChangerSlider.getValue();
      }

      if (!this.fovChanger.getValue()) {
         mc.field_71474_y.field_74334_X = this.oldFOV;
      }

   }

   public void onEnable() {
      this.oldFOV = mc.field_71474_y.field_74334_X;
   }

   public void onDisable() {
      mc.field_71474_y.field_74334_X = this.oldFOV;
   }
}
