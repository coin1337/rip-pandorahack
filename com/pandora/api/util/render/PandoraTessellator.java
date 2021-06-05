package com.pandora.api.util.render;

import com.pandora.api.util.font.FontUtils;
import com.pandora.api.util.misc.Wrapper;
import com.pandora.api.util.world.EntityUtil;
import com.pandora.client.module.modules.gui.ColorMain;
import com.pandora.client.module.modules.render.Nametags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;

public class PandoraTessellator {
   private static final Minecraft mc = Wrapper.getMinecraft();

   public static void drawBox(BlockPos blockPos, double height, PandoraColor color, int sides) {
      drawBox((double)blockPos.func_177958_n(), (double)blockPos.func_177956_o(), (double)blockPos.func_177952_p(), 1.0D, height, 1.0D, color, sides);
   }

   public static void drawBox(AxisAlignedBB bb, boolean check, double height, PandoraColor color, int sides) {
      if (check) {
         drawBox(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c, bb.field_72336_d - bb.field_72340_a, bb.field_72337_e - bb.field_72338_b, bb.field_72334_f - bb.field_72339_c, color, sides);
      } else {
         drawBox(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c, bb.field_72336_d - bb.field_72340_a, height, bb.field_72334_f - bb.field_72339_c, color, sides);
      }

   }

   public static void drawBox(double x, double y, double z, double w, double h, double d, PandoraColor color, int sides) {
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder bufferbuilder = tessellator.func_178180_c();
      color.glColor();
      bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181705_e);
      if ((sides & 1) != 0) {
         vertex(x + w, y, z, bufferbuilder);
         vertex(x + w, y, z + d, bufferbuilder);
         vertex(x, y, z + d, bufferbuilder);
         vertex(x, y, z, bufferbuilder);
      }

      if ((sides & 2) != 0) {
         vertex(x + w, y + h, z, bufferbuilder);
         vertex(x, y + h, z, bufferbuilder);
         vertex(x, y + h, z + d, bufferbuilder);
         vertex(x + w, y + h, z + d, bufferbuilder);
      }

      if ((sides & 4) != 0) {
         vertex(x + w, y, z, bufferbuilder);
         vertex(x, y, z, bufferbuilder);
         vertex(x, y + h, z, bufferbuilder);
         vertex(x + w, y + h, z, bufferbuilder);
      }

      if ((sides & 8) != 0) {
         vertex(x, y, z + d, bufferbuilder);
         vertex(x + w, y, z + d, bufferbuilder);
         vertex(x + w, y + h, z + d, bufferbuilder);
         vertex(x, y + h, z + d, bufferbuilder);
      }

      if ((sides & 16) != 0) {
         vertex(x, y, z, bufferbuilder);
         vertex(x, y, z + d, bufferbuilder);
         vertex(x, y + h, z + d, bufferbuilder);
         vertex(x, y + h, z, bufferbuilder);
      }

      if ((sides & 32) != 0) {
         vertex(x + w, y, z + d, bufferbuilder);
         vertex(x + w, y, z, bufferbuilder);
         vertex(x + w, y + h, z, bufferbuilder);
         vertex(x + w, y + h, z + d, bufferbuilder);
      }

      tessellator.func_78381_a();
   }

   public static void drawBoundingBox(BlockPos bp, double height, float width, PandoraColor color) {
      drawBoundingBox(getBoundingBox(bp, 1.0D, height, 1.0D), width, color);
   }

   public static void drawBoundingBox(AxisAlignedBB bb, float width, PandoraColor color) {
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder bufferbuilder = tessellator.func_178180_c();
      GlStateManager.func_187441_d(width);
      color.glColor();
      bufferbuilder.func_181668_a(3, DefaultVertexFormats.field_181705_e);
      vertex(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c, bufferbuilder);
      vertex(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f, bufferbuilder);
      vertex(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f, bufferbuilder);
      vertex(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c, bufferbuilder);
      vertex(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c, bufferbuilder);
      vertex(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c, bufferbuilder);
      vertex(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f, bufferbuilder);
      vertex(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f, bufferbuilder);
      vertex(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f, bufferbuilder);
      vertex(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f, bufferbuilder);
      vertex(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f, bufferbuilder);
      vertex(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f, bufferbuilder);
      vertex(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c, bufferbuilder);
      vertex(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c, bufferbuilder);
      vertex(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c, bufferbuilder);
      vertex(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c, bufferbuilder);
      tessellator.func_78381_a();
   }

   public static void drawBoundingBoxWithSides(BlockPos blockPos, int width, PandoraColor color, int sides) {
      drawBoundingBoxWithSides(getBoundingBox(blockPos, 1.0D, 1.0D, 1.0D), width, color, sides);
   }

   public static void drawBoundingBoxWithSides(AxisAlignedBB axisAlignedBB, int width, PandoraColor color, int sides) {
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder bufferbuilder = tessellator.func_178180_c();
      GlStateManager.func_187441_d((float)width);
      color.glColor();
      double w = axisAlignedBB.field_72336_d - axisAlignedBB.field_72340_a;
      double h = axisAlignedBB.field_72337_e - axisAlignedBB.field_72338_b;
      double d = axisAlignedBB.field_72334_f - axisAlignedBB.field_72339_c;
      bufferbuilder.func_181668_a(3, DefaultVertexFormats.field_181705_e);
      if ((sides & 32) != 0) {
         vertex(axisAlignedBB.field_72340_a + w, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c + d, bufferbuilder);
         vertex(axisAlignedBB.field_72340_a + w, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, bufferbuilder);
         vertex(axisAlignedBB.field_72340_a + w, axisAlignedBB.field_72338_b + h, axisAlignedBB.field_72339_c, bufferbuilder);
         vertex(axisAlignedBB.field_72340_a + w, axisAlignedBB.field_72338_b + h, axisAlignedBB.field_72339_c + d, bufferbuilder);
         vertex(axisAlignedBB.field_72340_a + w, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c + d, bufferbuilder);
      }

      if ((sides & 16) != 0) {
         vertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, bufferbuilder);
         vertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c + d, bufferbuilder);
         vertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b + h, axisAlignedBB.field_72339_c + d, bufferbuilder);
         vertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b + h, axisAlignedBB.field_72339_c, bufferbuilder);
         vertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, bufferbuilder);
      }

      if ((sides & 4) != 0) {
         vertex(axisAlignedBB.field_72340_a + w, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, bufferbuilder);
         vertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, bufferbuilder);
         vertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b + h, axisAlignedBB.field_72339_c, bufferbuilder);
         vertex(axisAlignedBB.field_72340_a + w, axisAlignedBB.field_72338_b + h, axisAlignedBB.field_72339_c, bufferbuilder);
         vertex(axisAlignedBB.field_72340_a + w, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, bufferbuilder);
      }

      if ((sides & 8) != 0) {
         vertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c + d, bufferbuilder);
         vertex(axisAlignedBB.field_72340_a + w, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c + d, bufferbuilder);
         vertex(axisAlignedBB.field_72340_a + w, axisAlignedBB.field_72338_b + h, axisAlignedBB.field_72339_c + d, bufferbuilder);
         vertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b + h, axisAlignedBB.field_72339_c + d, bufferbuilder);
         vertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c + d, bufferbuilder);
      }

      if ((sides & 2) != 0) {
         vertex(axisAlignedBB.field_72340_a + w, axisAlignedBB.field_72338_b + h, axisAlignedBB.field_72339_c, bufferbuilder);
         vertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b + h, axisAlignedBB.field_72339_c, bufferbuilder);
         vertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b + h, axisAlignedBB.field_72339_c + d, bufferbuilder);
         vertex(axisAlignedBB.field_72340_a + w, axisAlignedBB.field_72338_b + h, axisAlignedBB.field_72339_c + d, bufferbuilder);
         vertex(axisAlignedBB.field_72340_a + w, axisAlignedBB.field_72338_b + h, axisAlignedBB.field_72339_c, bufferbuilder);
      }

      if ((sides & 1) != 0) {
         vertex(axisAlignedBB.field_72340_a + w, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, bufferbuilder);
         vertex(axisAlignedBB.field_72340_a + w, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c + d, bufferbuilder);
         vertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c + d, bufferbuilder);
         vertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, bufferbuilder);
         vertex(axisAlignedBB.field_72340_a + w, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, bufferbuilder);
      }

      tessellator.func_78381_a();
   }

   public static void drawLine(double posx, double posy, double posz, double posx2, double posy2, double posz2, PandoraColor color) {
      GlStateManager.func_187441_d(1.0F);
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder bufferbuilder = tessellator.func_178180_c();
      color.glColor();
      bufferbuilder.func_181668_a(1, DefaultVertexFormats.field_181705_e);
      vertex(posx, posy, posz, bufferbuilder);
      vertex(posx2, posy2, posz2, bufferbuilder);
      tessellator.func_78381_a();
   }

   public static void drawSphere(double x, double y, double z, float size, int slices, int stacks) {
      Sphere s = new Sphere();
      GlStateManager.func_187441_d(1.2F);
      s.setDrawStyle(100013);
      GlStateManager.func_179094_E();
      GlStateManager.func_179137_b(x - mc.func_175598_ae().field_78730_l, y - mc.func_175598_ae().field_78731_m, z - mc.func_175598_ae().field_78728_n);
      s.draw(size, slices, stacks);
      GlStateManager.func_179121_F();
   }

   public static void drawNametag(Entity entity, String[] text, PandoraColor color, int type) {
      Vec3d pos = EntityUtil.getInterpolatedPos(entity, mc.func_184121_ak());
      drawNametag(pos.field_72450_a, pos.field_72448_b + (double)entity.field_70131_O, pos.field_72449_c, text, color, type);
   }

   public static void drawNametag(double x, double y, double z, String[] text, PandoraColor color, int type) {
      double dist = mc.field_71439_g.func_70011_f(x, y, z);
      double scale = 1.0D;
      double offset = 0.0D;
      int start = 0;
      switch(type) {
      case 0:
         scale = dist / 20.0D * Math.pow(1.2589254D, 0.1D / (dist < 25.0D ? 0.5D : 2.0D));
         scale = Math.min(Math.max(scale, 0.5D), 5.0D);
         offset = scale > 2.0D ? scale / 2.0D : scale;
         scale /= 40.0D;
         start = 10;
         break;
      case 1:
         scale = (double)(-((int)dist)) / 6.0D;
         if (scale < 1.0D) {
            scale = 1.0D;
         }

         scale *= 0.02666666666666667D;
         break;
      case 2:
         scale = 0.0018D + 0.003D * dist;
         if (dist <= 8.0D) {
            scale = 0.0245D;
         }

         start = -8;
      }

      GlStateManager.func_179094_E();
      GlStateManager.func_179137_b(x - mc.func_175598_ae().field_78730_l, y + offset - mc.func_175598_ae().field_78731_m, z - mc.func_175598_ae().field_78728_n);
      GlStateManager.func_179114_b(-mc.func_175598_ae().field_78735_i, 0.0F, 1.0F, 0.0F);
      float var10001 = mc.field_71474_y.field_74320_O == 2 ? -1.0F : 1.0F;
      GlStateManager.func_179114_b(mc.func_175598_ae().field_78732_j, var10001, 0.0F, 0.0F);
      GlStateManager.func_179139_a(-scale, -scale, scale);
      if (type == 2) {
         double width = 0.0D;
         PandoraColor bcolor = new PandoraColor(0, 0, 0, 51);
         if (Nametags.customColor.getValue()) {
            bcolor = Nametags.borderColor.getValue();
         }

         for(int i = 0; i < text.length; ++i) {
            double w = (double)(FontUtils.getStringWidth(ColorMain.customFont.getValue(), text[i]) / 2);
            if (w > width) {
               width = w;
            }
         }

         drawBorderedRect(-width - 1.0D, (double)(-mc.field_71466_p.field_78288_b), width + 2.0D, 1.0D, 1.8F, new PandoraColor(0, 4, 0, 85), bcolor);
      }

      GlStateManager.func_179098_w();

      for(int i = 0; i < text.length; ++i) {
         FontUtils.drawStringWithShadow(ColorMain.customFont.getValue(), text[i], -FontUtils.getStringWidth(ColorMain.customFont.getValue(), text[i]) / 2, i * (mc.field_71466_p.field_78288_b + 1) + start, color);
      }

      GlStateManager.func_179090_x();
      if (type != 2) {
         GlStateManager.func_179121_F();
      }

   }

   private static void drawBorderedRect(double x, double y, double x1, double y1, float lineWidth, PandoraColor inside, PandoraColor border) {
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder bufferbuilder = tessellator.func_178180_c();
      inside.glColor();
      bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181705_e);
      bufferbuilder.func_181662_b(x, y1, 0.0D).func_181675_d();
      bufferbuilder.func_181662_b(x1, y1, 0.0D).func_181675_d();
      bufferbuilder.func_181662_b(x1, y, 0.0D).func_181675_d();
      bufferbuilder.func_181662_b(x, y, 0.0D).func_181675_d();
      tessellator.func_78381_a();
      border.glColor();
      GlStateManager.func_187441_d(lineWidth);
      bufferbuilder.func_181668_a(3, DefaultVertexFormats.field_181705_e);
      bufferbuilder.func_181662_b(x, y, 0.0D).func_181675_d();
      bufferbuilder.func_181662_b(x, y1, 0.0D).func_181675_d();
      bufferbuilder.func_181662_b(x1, y1, 0.0D).func_181675_d();
      bufferbuilder.func_181662_b(x1, y, 0.0D).func_181675_d();
      bufferbuilder.func_181662_b(x, y, 0.0D).func_181675_d();
      tessellator.func_78381_a();
   }

   private static void vertex(double x, double y, double z, BufferBuilder bufferbuilder) {
      bufferbuilder.func_181662_b(x - mc.func_175598_ae().field_78730_l, y - mc.func_175598_ae().field_78731_m, z - mc.func_175598_ae().field_78728_n).func_181675_d();
   }

   private static AxisAlignedBB getBoundingBox(BlockPos bp, double width, double height, double depth) {
      double x = (double)bp.func_177958_n();
      double y = (double)bp.func_177956_o();
      double z = (double)bp.func_177952_p();
      return new AxisAlignedBB(x, y, z, x + width, y + height, z + depth);
   }

   public static void prepare() {
      GL11.glHint(3154, 4354);
      GlStateManager.func_179120_a(770, 771, 0, 1);
      GlStateManager.func_179103_j(7425);
      GlStateManager.func_179132_a(false);
      GlStateManager.func_179147_l();
      GlStateManager.func_179097_i();
      GlStateManager.func_179090_x();
      GlStateManager.func_179140_f();
      GlStateManager.func_179129_p();
      GlStateManager.func_179141_d();
      GL11.glEnable(2848);
      GL11.glEnable(34383);
   }

   public static void release() {
      GL11.glDisable(34383);
      GL11.glDisable(2848);
      GlStateManager.func_179141_d();
      GlStateManager.func_179089_o();
      GlStateManager.func_179145_e();
      GlStateManager.func_179098_w();
      GlStateManager.func_179126_j();
      GlStateManager.func_179084_k();
      GlStateManager.func_179132_a(true);
      GlStateManager.func_187441_d(1.0F);
      GlStateManager.func_179103_j(7424);
      GL11.glHint(3154, 4352);
   }
}
