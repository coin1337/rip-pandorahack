package com.pandora.client.module.modules.hud;

import com.pandora.api.util.font.FontUtils;
import com.pandora.api.util.render.PandoraColor;
import com.pandora.client.module.Module;
import com.pandora.client.module.modules.gui.ColorMain;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;

public class ArmorHUD extends Module {
   private static final RenderItem itemRender = Minecraft.func_71410_x().func_175599_af();

   public ArmorHUD() {
      super("ArmorHUD", Module.Category.HUD);
   }

   public void onRender() {
      GlStateManager.func_179094_E();
      GlStateManager.func_179098_w();
      ScaledResolution resolution = new ScaledResolution(mc);
      int i = resolution.func_78326_a() / 2;
      int iteration = 0;
      int y = resolution.func_78328_b() - 55 - (mc.field_71439_g.func_70090_H() ? 10 : 0);
      Iterator var5 = mc.field_71439_g.field_71071_by.field_70460_b.iterator();

      while(var5.hasNext()) {
         ItemStack is = (ItemStack)var5.next();
         ++iteration;
         if (!is.func_190926_b()) {
            int x = i - 90 + (9 - iteration) * 20 + 2;
            GlStateManager.func_179126_j();
            itemRender.field_77023_b = 200.0F;
            itemRender.func_180450_b(is, x, y);
            itemRender.func_180453_a(mc.field_71466_p, is, x, y, "");
            itemRender.field_77023_b = 0.0F;
            GlStateManager.func_179098_w();
            GlStateManager.func_179140_f();
            GlStateManager.func_179097_i();
            String s = is.func_190916_E() > 1 ? is.func_190916_E() + "" : "";
            mc.field_71466_p.func_175063_a(s, (float)(x + 19 - 2 - mc.field_71466_p.func_78256_a(s)), (float)(y + 9), (new PandoraColor(255, 255, 255)).getRGB());
            float green = ((float)is.func_77958_k() - (float)is.func_77952_i()) / (float)is.func_77958_k();
            float red = 1.0F - green;
            int dmg = 100 - (int)(red * 100.0F);
            if (green > 1.0F) {
               green = 1.0F;
            } else if (green < 0.0F) {
               green = 0.0F;
            }

            if (red > 1.0F) {
               red = 1.0F;
            }

            if (dmg < 0) {
               dmg = 0;
            }

            FontUtils.drawStringWithShadow(ColorMain.customFont.getValue(), dmg + "", x + 8 - mc.field_71466_p.func_78256_a(dmg + "") / 2, y - 11, new PandoraColor((int)(red * 255.0F), (int)(green * 255.0F), 0));
         }
      }

      GlStateManager.func_179126_j();
      GlStateManager.func_179121_F();
   }
}
