package com.pandora.api.util.render;

import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;

public class PandoraColor extends Color {
   private static final long serialVersionUID = 1L;

   public PandoraColor(int rgb) {
      super(rgb);
   }

   public PandoraColor(int rgba, boolean hasalpha) {
      super(rgba, hasalpha);
   }

   public PandoraColor(int r, int g, int b) {
      super(r, g, b);
   }

   public PandoraColor(int r, int g, int b, int a) {
      super(r, g, b, a);
   }

   public PandoraColor(Color color) {
      super(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
   }

   public PandoraColor(PandoraColor color, int a) {
      super(color.getRed(), color.getGreen(), color.getBlue(), a);
   }

   public static PandoraColor fromHSB(float hue, float saturation, float brightness) {
      return new PandoraColor(Color.getHSBColor(hue, saturation, brightness));
   }

   public float getHue() {
      return RGBtoHSB(this.getRed(), this.getGreen(), this.getBlue(), (float[])null)[0];
   }

   public float getSaturation() {
      return RGBtoHSB(this.getRed(), this.getGreen(), this.getBlue(), (float[])null)[1];
   }

   public float getBrightness() {
      return RGBtoHSB(this.getRed(), this.getGreen(), this.getBlue(), (float[])null)[2];
   }

   public void glColor() {
      GlStateManager.func_179131_c((float)this.getRed() / 255.0F, (float)this.getGreen() / 255.0F, (float)this.getBlue() / 255.0F, (float)this.getAlpha() / 255.0F);
   }
}
